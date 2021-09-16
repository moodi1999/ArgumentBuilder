package me.moodi1999.argument_processor.processor.targetProcessor

import com.squareup.kotlinpoet.metadata.KotlinPoetMetadataPreview
import me.moodi1999.argument_annotation.annotation.Argument
import me.moodi1999.argument_annotation.annotation.ArgumentBuilderTarget
import me.moodi1999.argument_processor.generator.ArgumentBuilderCodeGenerator
import me.moodi1999.argument_processor.model.ArgumentInfo
import me.moodi1999.argument_processor.model.TargetFieldInfo
import java.io.File
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment

class TargetArgumentProcessor(processingEnv: ProcessingEnvironment) : TargetBaseProcessor(processingEnv) {

    @OptIn(KotlinPoetMetadataPreview::class)
    override fun process(roundEnv: RoundEnvironment, kaptKotlinGeneratedDir: String) {
        roundEnv.getElementsAnnotatedWith(ArgumentBuilderTarget::class.java)
            .map { getTargetElementData(it, "${it.simpleName}Creator") }
            .forEach { targetInfo ->
                val arguments = collectTargetFieldsAnnotatedWith(Argument::class.java, targetInfo)
                    .map { (annotation, fieldInfo) ->
                        createArgumentInfo(annotation, fieldInfo)
                    }

                if (arguments.isNotEmpty()) {
                    val typeSpec = ArgumentBuilderCodeGenerator(targetInfo, processingEnv, arguments).create()
                    buildFileSpec(targetInfo, typeSpec).writeTo(File(kaptKotlinGeneratedDir))
                }
            }
    }

    private fun createArgumentInfo(
        annotation: Argument,
        fieldInfo: TargetFieldInfo
    ) = ArgumentInfo(
        fieldInfo,
        annotation.key,
        annotation.isRequired,
    )

}