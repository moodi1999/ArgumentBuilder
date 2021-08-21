package me.moodi1999.argument_annotation.annotation

import me.moodi1999.argument_annotation.bundler.ArgumentBundler
import me.moodi1999.argument_annotation.bundler.NoneArgumentBundler
import kotlin.reflect.KClass


typealias ArgumentBundlerClass = KClass<out ArgumentBundler<*>>

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class Argument(
    val key: String = "",
    val required: Boolean = true,
    val bundler: ArgumentBundlerClass = NoneArgumentBundler::class
)