package me.moodi1999.argument_annotation.annotation

import me.moodi1999.argument_annotation.bundler.ArgumentBundler
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class CustomBundler(
    val bundler: KClass<out ArgumentBundler<*>>
)