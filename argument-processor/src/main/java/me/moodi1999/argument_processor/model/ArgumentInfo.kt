package me.moodi1999.argument_processor.model

import me.moodi1999.argument_annotation.annotation.ArgumentBundlerClass
import me.moodi1999.argument_annotation.bundler.ArgumentBundler
import kotlin.reflect.KClass

class ArgumentInfo(
    val name: String,
    val key: String,
    val isRequired: Boolean,
    val bundlerClass: String
)