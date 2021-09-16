package me.moodi1999.argument_processor.model

data class ArgumentInfo(
    val fieldInfo: TargetFieldInfo,
    private val _key: String,
    val isRequired: Boolean
) : TargetFieldInfoInterface by fieldInfo {

    override val key: String by lazy {
        _key.ifEmpty { "${name}_arg_key" }
    }
}

fun ArgumentInfo.toNonNullArgument() = copy(fieldInfo = fieldInfo.toNonNullTargetField())
