package me.moodi1999.argument_annotation.annotation

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class Argument(
    val key: String = "",
    val isRequired: Boolean = false
)