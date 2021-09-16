package me.moodi1999.argument_processor.model

import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import javax.lang.model.element.Element


data class TargetFieldInfo(
    override val name: String,
    private val _element: Element?,
    override val propertySpec: PropertySpec,
    override val ownerSpec: TypeSpec? = null
) : TargetFieldInfoInterface {

    override val key: String
        get() = "${name}_key"

    override val type: TypeName
        get() = propertySpec.type

    override val element: Element
        get() = _element!!

    override fun toString(): String {
        return "ArgumentInfo(name=$name, type=$type, owner=${ownerSpec?.name})"
    }
}

fun TargetFieldInfo.toNonNullTargetField(): TargetFieldInfo {
    val nonNullProp = propertySpec.toBuilder(type = propertySpec.type.copy(nullable = false)).build()
    return copy(propertySpec = nonNullProp)
}
