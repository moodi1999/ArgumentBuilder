package me.moodi1999.argument_annotation.annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class ArgumentBuilderTarget(
    val includeParentsArgument: Boolean = false
)
