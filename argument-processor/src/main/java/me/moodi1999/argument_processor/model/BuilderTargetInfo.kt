package me.moodi1999.argument_processor.model

import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.metadata.ImmutableKmClass
import com.squareup.kotlinpoet.metadata.KotlinPoetMetadataPreview
import me.moodi1999.argument_annotation.annotation.ArgumentBuilderTarget
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

@KotlinPoetMetadataPreview
class BuilderTargetInfo(
    val packageName: String,
    val element: Element,
    val target: TargetType,
    val immutableKmClass: ImmutableKmClass,
    val typeSpec: TypeSpec,
    val targetAnnotation: ArgumentBuilderTarget,
    val fileName: String
) {
    val targetName: String
        get() = element.simpleName.toString()

}