package me.moodi1999.argument_processor.generator

import com.squareup.kotlinpoet.TypeSpec
import me.moodi1999.argument_processor.model.BuilderTargetInfo

class ArgumentBuilderCodeGenerator(private val fileName: String, private val targetInfo: BuilderTargetInfo) {
    fun create() = TypeSpec.classBuilder(fileName)
        .build()
}