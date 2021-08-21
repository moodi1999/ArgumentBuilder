package me.moodi1999.argument_processor.processor

import com.squareup.kotlinpoet.FileSpec
import com.sun.jdi.connect.Connector
import me.moodi1999.argument_annotation.annotation.Argument
import me.moodi1999.argument_annotation.annotation.ArgumentBuilderTarget
import me.moodi1999.argument_annotation.bundler.ArgumentBundler
import me.moodi1999.argument_annotation.bundler.NoneArgumentBundler
import me.moodi1999.argument_processor.generator.ArgumentBuilderCodeGenerator
import me.moodi1999.argument_processor.model.ArgumentInfo
import me.moodi1999.argument_processor.model.BuilderTargetInfo
import java.io.File
import java.lang.reflect.Modifier
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.type.MirroredTypeException
import javax.tools.Diagnostic

@SupportedSourceVersion(SourceVersion.RELEASE_8)
class ArgumentProcessor : AbstractProcessor() {

    override fun getSupportedAnnotationTypes() = mutableSetOf(
        Connector.Argument::class.java.canonicalName,
        ArgumentBuilderTarget::class.java.canonicalName,
    )

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        val kaptKotlinGeneratedDir =
            processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]
                ?: return false

        roundEnv.getElementsAnnotatedWith(ArgumentBuilderTarget::class.java)
            .forEach {
                val targetInfo = getTargetElementData(it)
                val fileName = "${targetInfo.targetName}Builder"

                FileSpec.builder(targetInfo.packageName, fileName)
                    .addType(ArgumentBuilderCodeGenerator(fileName, targetInfo).create())
                    .build()
                    .writeTo(File(kaptKotlinGeneratedDir))
            }

        return false
    }

    private fun getTargetElementData(element: Element): BuilderTargetInfo {
        val packageName = processingEnv.elementUtils.getPackageOf(element).toString()
        val modelName = element.simpleName.toString()

        val arguments = element.enclosedElements
            .mapNotNull {
                it.getAnnotation(Argument::class.java)?.let { annotation ->
                    val elementName = it.simpleName.toString()
                    val bundlerClass = try {
                        getFullQualifiedNameByClass(element, annotation.bundler.java)
                    } catch (e: MirroredTypeException) {
                        processingEnv.messager.printMessage(Diagnostic.Kind.NOTE, "element name is : ${e.typeMirror}")
                        e.typeMirror.toString()
                    }
                    ArgumentInfo(
                        elementName,
                        annotation.key,
                        annotation.required,
                        bundlerClass!!
                    )
                }
            }

        return BuilderTargetInfo(packageName, modelName, arguments)
    }


    @Throws(ProcessingException::class)
    private fun getFullQualifiedNameByClass(element: Element, clazz: Class<out ArgumentBundler<*>>): String? {

        // It's the none Args bundler, hence no future checks are needed
        if (clazz == NoneArgumentBundler::class.java) {
            return NoneArgumentBundler::class.java.canonicalName
        }

        // Check public
        if (!Modifier.isPublic(clazz.modifiers)) {
            throw ProcessingException(
                element,
                "The %s must be a public class to be a valid ArgsBundler", clazz.canonicalName
            )
        }

        // Check constructors
        val constructors = clazz.constructors
        var foundDefaultConstructor = false
        for (c in constructors) {
            val isPublicConstructor = Modifier.isPublic(c.modifiers)
            val pType = c.parameterTypes
            if (pType.size == 0 && isPublicConstructor) {
                foundDefaultConstructor = true
                break
            }
        }
        if (!foundDefaultConstructor) {
            throw ProcessingException(
                element,
                "The %s must provide a public empty default constructor", clazz.canonicalName
            )
        }
        return clazz.canonicalName
    }


    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }
}