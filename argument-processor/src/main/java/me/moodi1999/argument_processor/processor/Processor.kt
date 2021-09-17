package me.moodi1999.argument_processor.processor

import com.squareup.kotlinpoet.metadata.KotlinPoetMetadataPreview
import me.moodi1999.argument_annotation.annotation.Argument
import me.moodi1999.argument_annotation.annotation.ArgumentBuilderTarget
import me.moodi1999.argument_processor.utils.ProcessingException
import me.moodi1999.argument_processor.processor.targetProcessor.TargetArgumentProcessor
import me.moodi1999.argument_processor.processor.targetProcessor.TargetSaveInstanceFieldProcessor
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

@KotlinPoetMetadataPreview
@SupportedSourceVersion(SourceVersion.RELEASE_8)
class Processor : AbstractProcessor() {

    override fun getSupportedAnnotationTypes() = mutableSetOf(
        Argument::class.java.canonicalName,
        ArgumentBuilderTarget::class.java.canonicalName,
    )

    override fun init(processingEnv: ProcessingEnvironment?) {
        super.init(processingEnv)
    }

    override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        val kaptKotlinGeneratedDir =
            processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]
                ?: return false

        catchException {
            TargetArgumentProcessor(processingEnv).process(roundEnv, kaptKotlinGeneratedDir)
            TargetSaveInstanceFieldProcessor(processingEnv).process(roundEnv, kaptKotlinGeneratedDir)
        }

        return false
    }

    private fun catchException(block: () -> Unit) {
        try {
            block()
        } catch (e: ProcessingException) {
            val args = "<<args=${e.messageArgs.joinToString()}>>|".takeIf { e.messageArgs.isNotEmpty() }.orEmpty()
            val element = "<<element=${e.element}>>|".takeIf { e.element != null }.orEmpty()
            val msg = "${e.message}.|$element$args"
            processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, msg)
        } catch (e: Throwable) {
            processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, e.stackTraceToString())
        }
    }

    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }
}