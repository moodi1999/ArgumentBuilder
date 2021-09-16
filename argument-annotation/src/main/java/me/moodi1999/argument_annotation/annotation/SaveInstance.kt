package me.moodi1999.argument_annotation.annotation

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class SaveInstance(
    val key: String = ""
)

