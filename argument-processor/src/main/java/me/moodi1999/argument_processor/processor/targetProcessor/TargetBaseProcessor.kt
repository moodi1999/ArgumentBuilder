package me.moodi1999.argument_processor.processor.targetProcessor

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.classinspector.elements.ElementsClassInspector
import com.squareup.kotlinpoet.metadata.ImmutableKmClass
import com.squareup.kotlinpoet.metadata.KotlinPoetMetadataPreview
import com.squareup.kotlinpoet.metadata.specs.toTypeSpec
import com.squareup.kotlinpoet.metadata.toImmutableKmClass
import me.moodi1999.argument_annotation.annotation.ArgumentBuilderTarget
import me.moodi1999.argument_processor.model.BuilderTargetInfo
import me.moodi1999.argument_processor.utils.InvalidTargetTypeException
import me.moodi1999.argument_processor.model.TargetFieldInfo
import me.moodi1999.argument_processor.model.findTargetType
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeKind

@OptIn(KotlinPoetMetadataPreview::class)
abstract class TargetBaseProcessor(
    protected val processingEnv: ProcessingEnvironment
) {

    abstract fun process(roundEnv: RoundEnvironment, kaptKotlinGeneratedDir: String)

    protected fun getTargetElementData(targetElement: Element, name: String): BuilderTargetInfo {
        val targetType = processingEnv.findTargetType(targetElement) ?: throw InvalidTargetTypeException(targetElement)
        val packageName = processingEnv.elementUtils.getPackageOf(targetElement).toString()
        val targetAnnotation = targetElement.getAnnotation(ArgumentBuilderTarget::class.java)
        val targetMetadata: ImmutableKmClass = (targetElement as TypeElement).toImmutableKmClass()
        val targetTypeSpec: TypeSpec = targetMetadata.toTypeSpec()

        return BuilderTargetInfo(
            packageName,
            targetElement,
            targetType,
            targetMetadata,
            targetTypeSpec,
            targetAnnotation,
            name
        )
    }

    protected fun <T : Annotation> collectTargetFieldsAnnotatedWith(
        annotation: Class<T>,
        targetInfo: BuilderTargetInfo
    ): MutableList<Pair<T, TargetFieldInfo>> {
        val fields = mutableListOf<Pair<T, TargetFieldInfo>>()
        var currentElement: TypeElement? = targetInfo.element as TypeElement?
        var currentElementTypeSpec = targetInfo.immutableKmClass.toTypeSpec()
        do {
            val currentlyAddedFieldsNames = fields.map { field -> field.second.name }

            currentElement!!.enclosedElements
                .filter { it.simpleName.toString() !in currentlyAddedFieldsNames }
                .forEach { element ->
                    element.getAnnotation(annotation)?.let { annotation ->
                        fields.add(annotation to createTargetFieldInfo(element, currentElementTypeSpec))
                    }
                }

            val superClassType = currentElement.superclass
            if (superClassType.kind == TypeKind.NONE) {
                // Basis class (java.lang.Object) reached, so exit
                currentElement = null
                break
            } else {
                currentElement = processingEnv.typeUtils.asElement(superClassType) as TypeElement?
                try {
                    currentElementTypeSpec = currentElement!!.toImmutableKmClass().toTypeSpec()
                } catch (e: IllegalStateException) {
                    // No kotlin super class, so we can't process them without kotlin metadata.
                    break
                }
            }
        } while (currentElement != null && targetInfo.targetAnnotation.includeParentsArgument)
        return fields
    }

    protected fun buildFileSpec(
        targetInfo: BuilderTargetInfo,
        typeSpec: TypeSpec
    ) = FileSpec.builder(targetInfo.packageName, targetInfo.fileName)
        .addImport(
            "me.moodi1999.argument_annotation.bundler",
            "put",
            "putIntList",
            "putCharSequenceList",
            "putStringList",
            "putParcelableList",
            "getNullableIntArray",
            "getIntList",
            "putMap",
            "getMap",
            "getStringList",
            "getCharSequenceList",
            "getParcelableList",
        )
        .addType(typeSpec)
        .build()

    private fun createTargetFieldInfo(
        element: Element,
        ownerTypeSpec: TypeSpec,
    ) = TargetFieldInfo(
        element.simpleName.toString(),
        element,
        ownerTypeSpec.propertySpecs.find { it.name == element.simpleName.toString() }!!,
        ownerTypeSpec
    )

    private fun ImmutableKmClass.toTypeSpec() = toTypeSpec(
        ElementsClassInspector.create(processingEnv.elementUtils, processingEnv.typeUtils)
    )
}