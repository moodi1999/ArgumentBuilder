package me.moodi1999.argument_processor.generator

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.metadata.KotlinPoetMetadataPreview
import me.moodi1999.argument_processor.model.ArgumentInfo
import me.moodi1999.argument_processor.model.BuilderTargetInfo
import me.moodi1999.argument_processor.model.TargetType
import me.moodi1999.argument_processor.model.toNonNullArgument
import me.moodi1999.argument_processor.operationGenerator.BOCGHelper
import me.moodi1999.argument_processor.operationGenerator.generateGetBundleCodeBlock
import me.moodi1999.argument_processor.operationGenerator.generatePutBundleCodeBlock
import javax.annotation.processing.ProcessingEnvironment


@KotlinPoetMetadataPreview
class ArgumentBuilderCodeGenerator(
    private val targetInfo: BuilderTargetInfo,
    private val processingEnv: ProcessingEnvironment,
    private val arguments: List<ArgumentInfo>,
) {
    private val requireArguments
        get() = arguments.filter { it.isRequired }
    private val optionalArguments
        get() = arguments.filter { !it.isRequired }

    private val targetClassName = ClassName(targetInfo.packageName, targetInfo.targetName)
    private val creatorClassName = ClassName(targetInfo.packageName, targetInfo.fileName)
    private val bundleClassName = ClassName("android.os", "Bundle")

    private val nullableBundleProperty = PropertySpec.builder("_bundle", bundleClassName.copy(nullable = true))
        .addModifiers(KModifier.PRIVATE)
        .mutable()
        .initializer(CodeBlock.of(" %L", "Bundle()"))
        .build()

    private val bundleProperty = PropertySpec.builder("bundle", bundleClassName)
        .addModifiers(KModifier.PRIVATE)
        .getter(
            FunSpec.getterBuilder()
                .addStatement("return %N!!", nullableBundleProperty)
                .build()
        )
        .build()

    fun create(): TypeSpec {
        return TypeSpec.classBuilder(creatorClassName)
            .addKdoc("Creator and Bundle provider for [%T]", targetClassName)
            .primaryConstructor(buildPrimaryConstructorSpec())
            .addProperty(nullableBundleProperty)
            .addProperty(bundleProperty)
            .apply {
                if (requireArguments.isNotEmpty())
                    addInitializerBlock(buildInitializerBlockSpec())
            }
            .apply {
                optionalArguments.forEach {
                    addFunction(createOptionalArgumentFunSpec(it))
                }
            }
            .addFunction(buildCreateBundleFunSpec())
            .apply {
                if (targetInfo.target == TargetType.Fragment)
                    addFunction(buildCreateTargetFunSpec())
            }
            .addType(buildCompanionObject())
            .build()
    }

    private fun buildPrimaryConstructorSpec() = FunSpec.constructorBuilder()
        .apply {
            requireArguments.forEach {
                addParameter(it.name, it.propertySpec.type.copy(nullable = false))
            }
        }
        .build()

    private fun buildInitializerBlockSpec() = CodeBlock.builder()
        .apply {
            addStatement("%N.apply {", bundleProperty)
            indent()
            requireArguments.forEach {
                addStatement("%L", it.toNonNullArgument().generatePutBundleCodeBlock(processingEnv, bundleProperty))
            }
            unindent()
            addStatement("}")
        }
        .build()


    private fun buildCreateBundleFunSpec() = FunSpec.builder("createBundle")
        .returns(
            bundleClassName,
            CodeBlock.of("copy of [%N]", bundleProperty)
        )
        .addCode(
            "return %T(%N).also { %N = null }",
            bundleClassName,
            bundleProperty,
            nullableBundleProperty
        )
        .build()


    private fun buildCreateTargetFunSpec() = FunSpec.builder("create")
        .returns(
            targetClassName,
            CodeBlock.of("new instance of [%T] with %N", targetClassName, bundleProperty)
        )
        .addCode(
            CodeBlock.of(
                "return %T().apply { arguments = %N() }",
                targetClassName,
                buildCreateBundleFunSpec(),
            )
        )
        .build()


    private fun createOptionalArgumentFunSpec(it: ArgumentInfo) =
        FunSpec.builder(it.name)
            .addParameter(ParameterSpec(it.name, it.type.copy(nullable = false)))
            .returns(creatorClassName)
            .addStatement(
                "%L",
                it.toNonNullArgument().generatePutBundleCodeBlock(processingEnv, bundleProperty)
            )
            .addStatement("return this")
            .build()

    private fun buildCompanionObject() = TypeSpec.companionObjectBuilder()
        .addProperties(arguments.map { BOCGHelper.getGenerateKeyProperty(it.key) })
        .addFunction(buildArgumentInitializerFuncSpec(targetInfo.target))
        .build()

    private fun buildArgumentInitializerFuncSpec(target: TargetType): FunSpec {
        val targetName = target.name.lowercase()
        return FunSpec.builder("initialize")
            .addParameter(ParameterSpec(targetName, targetClassName))
            .apply {
                val argumentsProperty = PropertySpec.builder("arguments", bundleProperty.type).build()
                addCode(CodeBlock.Builder()
                    .addStatement(
                        "val %N = requireNotNull(%L.%L) {",
                        argumentsProperty,
                        targetName,
                        target.argumentsStore
                    )
                    .indent()
                    .addStatement(
                        "%S",
                        "No arguments set. Have you set up this $targetName with the @ArgumentBuilderTarget?"
                    )
                    .unindent()
                    .addStatement("}")
                    .apply {

                        requireArguments.forEach {
                            add("%L.%L = ", targetName, it.name)
                            addStatement(
                                "requireNotNull(%L) {",
                                it.generateGetBundleCodeBlock(processingEnv, argumentsProperty)
                            )
                            indent()
                            addStatement(
                                "%S",
                                "Required argument: " + it.name + ", is not present in the arguments."
                            )
                            unindent()
                            addStatement("}")
                        }

                        optionalArguments.forEach {
                            addStatement(
                                "if (arguments.containsKey(%N)) {",
                                BOCGHelper.getGenerateKeyProperty(it.key, isConst = false),
                            )
                            indent()
                            addStatement(
                                "%L.%L = requireNotNull(%L)",
                                targetName,
                                it.name,
                                it.generateGetBundleCodeBlock(processingEnv, argumentsProperty)
                            )
                            unindent()
                            addStatement("}")
                        }
                    }
                    .build()
                )
            }
            .build()
    }
}