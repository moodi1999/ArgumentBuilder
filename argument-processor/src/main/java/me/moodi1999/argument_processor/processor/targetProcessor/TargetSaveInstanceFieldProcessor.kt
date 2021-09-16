package me.moodi1999.argument_processor.processor.targetProcessor

import com.squareup.kotlinpoet.metadata.KotlinPoetMetadataPreview
import me.moodi1999.argument_annotation.annotation.ArgumentBuilderTarget
import me.moodi1999.argument_annotation.annotation.SaveInstance
import me.moodi1999.argument_processor.generator.SaveInstanceBuilderCodeGenerator
import me.moodi1999.argument_processor.model.SaveInstanceInfo
import me.moodi1999.argument_processor.model.TargetFieldInfo
import java.io.File
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment

class TargetSaveInstanceFieldProcessor(processingEnv: ProcessingEnvironment) : TargetBaseProcessor(processingEnv) {

    @OptIn(KotlinPoetMetadataPreview::class)
    override fun process(roundEnv: RoundEnvironment, kaptKotlinGeneratedDir: String) {
        roundEnv.getElementsAnnotatedWith(ArgumentBuilderTarget::class.java)
            .map { getTargetElementData(it, "${it.simpleName}SaveInstanceBuilder") }
            .forEach { targetInfo ->
                val saveInstances = collectTargetFieldsAnnotatedWith(SaveInstance::class.java, targetInfo)
                    .map { (annotation, fieldInfo) ->
                        createSaveInstanceInfo(annotation, fieldInfo)
                    }

                if (saveInstances.isNotEmpty()) {
                    val typeSpec = SaveInstanceBuilderCodeGenerator(targetInfo, processingEnv, saveInstances).create()
                    buildFileSpec(targetInfo, typeSpec).writeTo(File(kaptKotlinGeneratedDir))
                }
            }
    }

    private fun createSaveInstanceInfo(
        annotation: SaveInstance,
        fieldInfo: TargetFieldInfo
    ) = SaveInstanceInfo(
        fieldInfo,
        annotation.key,
    )
}

