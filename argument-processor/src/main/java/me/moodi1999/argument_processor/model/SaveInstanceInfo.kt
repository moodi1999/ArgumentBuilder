package me.moodi1999.argument_processor.model

data class SaveInstanceInfo(
    val fieldInfo: TargetFieldInfo,
    private val _key: String,
) : TargetFieldInfoInterface by fieldInfo {

    override val key: String by lazy {
        _key.ifEmpty { "${name}_saveInstance_key" }
    }

}

fun SaveInstanceInfo.toNonNullArgument() = copy(fieldInfo = fieldInfo.toNonNullTargetField())
