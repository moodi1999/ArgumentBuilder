package me.moodi1999.argument_processor.operationGenerator

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.metadata.KotlinPoetMetadataPreview
import me.moodi1999.argument_annotation.annotation.CustomBundler
import me.moodi1999.argument_processor.model.TargetFieldInfoInterface
import me.moodi1999.argument_processor.model.hasAnnotation
import me.moodi1999.argument_processor.operationGenerator.base.BundleOperationCodeGenerator
import me.moodi1999.argument_processor.operationGenerator.className.*
import me.moodi1999.argument_processor.operationGenerator.parameterized.array.NullableIntArrayTypeBOCG
import me.moodi1999.argument_processor.operationGenerator.parameterized.array.ParcelableArrayTypeBOCG
import me.moodi1999.argument_processor.operationGenerator.parameterized.array.ParcelableSparseArrayTypeBOCG
import me.moodi1999.argument_processor.operationGenerator.parameterized.array.StringAndCharSeqArrayTypeBOCG
import me.moodi1999.argument_processor.operationGenerator.parameterized.collection.CollectionParameterizedArgTypeBOCG
import me.moodi1999.argument_processor.operationGenerator.parameterized.collection.ParcelableArrayListTypeBOCG
import me.moodi1999.argument_processor.operationGenerator.parameterized.collection.PrimaryArrayListTypeBOCG
import me.moodi1999.argument_processor.operationGenerator.parameterized.map.MapEntryIterateTypeBOCG
import me.moodi1999.argument_processor.utils.NoArgumentBundlerFound
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.type.MirroredTypeException

object BOCGHelper {
    fun getGenerateKeyProperty(key: String, isConst: Boolean = true) = PropertySpec.builder(key, String::class)
        .apply {
            if (isConst)
                addModifiers(KModifier.PRIVATE, KModifier.CONST)
        }
        .initializer("%S", key)
        .build()

    fun getBundleOperationGeneratorsSequence(
        argInfo: TargetFieldInfoInterface,
        processingEnv: ProcessingEnvironment,
    ) = sequenceOf(
        ScalarsTypeBOCG(),
        ReferencesTypeBOCG(),
        BoxedArrayTypeBOCG(),
        EnumTypeBOCG(),
        NullableIntArrayTypeBOCG(),
        StringAndCharSeqArrayTypeBOCG(),
        PrimaryArrayListTypeBOCG(),
        ParcelableArrayListTypeBOCG(),
        CollectionParameterizedArgTypeBOCG(),
        ParcelableSparseArrayTypeBOCG(),
        ParcelableArrayTypeBOCG(),
        ParcelableTypeBOCG(),
        MapEntryIterateTypeBOCG(),
        SerializableTypeBOCG(),
    ).onEach {
        it.initializePrimaryProps(processingEnv, argInfo)
    }

    @OptIn(KotlinPoetMetadataPreview::class)
    fun getCustomBundlerName(argumentElement: Element): String? {
        return getCustomBundlerName(argumentElement.getAnnotation(CustomBundler::class.java))
    }

    @OptIn(KotlinPoetMetadataPreview::class)
    fun getCustomBundlerName(customBundler: CustomBundler): String? {
        return try {
            val bundler = customBundler.bundler
            bundler.simpleName
        } catch (e: MirroredTypeException) {
            val asTypeName = e.typeMirror.asTypeName()
            (asTypeName as ClassName).simpleName
        } catch (e: Throwable) {
            null
        }
    }
}

fun Sequence<BundleOperationCodeGenerator>.firstApplicable() =
    firstOrNull { it.isApplicable } ?: throw NoArgumentBundlerFound(first().argInfo)

fun Sequence<BundleOperationCodeGenerator>.generatePutOperation() = firstApplicable().generatePutOperationCode()
fun Sequence<BundleOperationCodeGenerator>.generateGetOperation() = firstApplicable().generateGetOperationCode()

fun TargetFieldInfoInterface.generatePutBundleCodeBlock(
    processingEnv: ProcessingEnvironment,
    bundleProperty: Taggable
): CodeBlock {
    val argumentInfo = this
    return CodeBlock.builder().apply {
        if (argumentInfo.hasAnnotation<CustomBundler>()) {
            add(
                "%L.put(%N, %L, %N)",
                BOCGHelper.getCustomBundlerName(argumentInfo.element),
                BOCGHelper.getGenerateKeyProperty(argumentInfo.key, isConst = false),
                argumentInfo.name,
                bundleProperty
            )
        } else {
            val generator = BOCGHelper.getBundleOperationGeneratorsSequence(
                argumentInfo,
                processingEnv,
            ).firstApplicable()

            add(
                "%N.%L",
                bundleProperty,
                generator.generatePutOperationCode(),
            )
        }

    }.build()
}

fun TargetFieldInfoInterface.generateGetBundleCodeBlock(
    processingEnv: ProcessingEnvironment,
    bundleProperty: Taggable
): CodeBlock {
    val argumentInfo = this
    return CodeBlock.builder().apply {
        if (argumentInfo.hasAnnotation<CustomBundler>()) {
            add(
                "%L.get(%N, %N)",
                BOCGHelper.getCustomBundlerName(argumentInfo.element),
                BOCGHelper.getGenerateKeyProperty(argumentInfo.key, isConst = false),
                bundleProperty
            )
        } else {
            val generator = BOCGHelper.getBundleOperationGeneratorsSequence(
                argumentInfo,
                processingEnv,
            ).firstApplicable()

            add("%N.%L", bundleProperty, generator.generateGetOperationCode())
        }
    }.build()
}

fun TypeName.generateNullAssertionIfNullable() = CodeBlock.of(if (isNullable) "!!" else "")
fun TypeName.generateNullSafetyIfNullable() = CodeBlock.of(if (isNullable) "?" else "")