package me.moodi1999.argument_processor.generator

import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.metadata.KotlinPoetMetadataPreview
import me.moodi1999.argument_processor.model.BuilderTargetInfo
import me.moodi1999.argument_processor.model.SaveInstanceInfo
import me.moodi1999.argument_processor.model.TargetType
import me.moodi1999.argument_processor.operationGenerator.BOCGHelper
import me.moodi1999.argument_processor.operationGenerator.generateGetBundleCodeBlock
import me.moodi1999.argument_processor.operationGenerator.generatePutBundleCodeBlock
import javax.annotation.processing.ProcessingEnvironment


@KotlinPoetMetadataPreview
class SaveInstanceBuilderCodeGenerator(
    private val targetInfo: BuilderTargetInfo,
    private val processingEnv: ProcessingEnvironment,
    private val saveInstanceFields: List<SaveInstanceInfo>,
) {

    private val targetClassName = ClassName(targetInfo.packageName, targetInfo.targetName)
    private val creatorClassName = ClassName(targetInfo.packageName, targetInfo.fileName)
    private val bundleClassName = ClassName("android.os", "Bundle")

    fun create(): TypeSpec {
        return TypeSpec.objectBuilder(creatorClassName)
            .addKdoc("saving %L state for [%T]", targetInfo.target.name, targetClassName)
            .addProperties(saveInstanceFields.map { BOCGHelper.getGenerateKeyProperty(it.key) })
            .addFunction(buildSaveInstanceFunSpec())
            .addFunction(buildArgumentInitializerFuncSpec(targetInfo.target))
            .build()
    }

    private fun buildSaveInstanceFunSpec(): FunSpec {
        val outStateParameter = ParameterSpec("outState", bundleClassName)
        val targetParameter = ParameterSpec(targetInfo.targetName, targetClassName)
        return FunSpec.builder("saveInstanceState")
            .addParameter(targetParameter)
            .addParameter(outStateParameter)
            .addCode(
                CodeBlock.builder()
                    .addStatement("%N.apply {", targetParameter)
                    .indent()
                    .apply {
                        saveInstanceFields.forEach {
                            addStatement(
                                "%L",
                                it.generatePutBundleCodeBlock(processingEnv, outStateParameter)
                            )
                        }
                    }
                    .unindent()
                    .addStatement("}")
                    .build(),
            )
            .build()
    }

    private fun buildArgumentInitializerFuncSpec(target: TargetType): FunSpec {
        val targetParameter = ParameterSpec(target.name, targetClassName)
        val saveInstanceStateParameter = ParameterSpec("savedInstanceState", bundleClassName.copy(nullable = true))
        return FunSpec.builder("restoreSavedInstance")
            .addParameter(targetParameter)
            .addParameter(saveInstanceStateParameter)
            .apply {
                addCode(CodeBlock.Builder()
                    .addStatement(
                        "if (%N == null) {",
                        saveInstanceStateParameter,
                    )
                    .indent()
                    .addStatement("return")
                    .unindent()
                    .addStatement("}")
                    .addStatement("%N.apply {", targetParameter)
                    .indent()
                    .apply {
                        saveInstanceFields.forEach {
                            add("%L = ", it.name)
                            val bundleOperation =
                                it.generateGetBundleCodeBlock(processingEnv, saveInstanceStateParameter)
                            if (it.type.isNullable) {
                                addStatement("%L", bundleOperation)
                            } else {
                                addStatement("requireNotNull(%L)", bundleOperation)
                            }
                        }
                    }
                    .unindent()
                    .addStatement("}")
                    .build()
                )
            }
            .build()
    }

}

