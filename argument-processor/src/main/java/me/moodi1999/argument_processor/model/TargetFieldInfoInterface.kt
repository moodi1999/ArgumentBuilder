package me.moodi1999.argument_processor.model

import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import javax.lang.model.element.Element

interface TargetFieldInfoInterface {
    val name: String
    val propertySpec: PropertySpec
    val ownerSpec: TypeSpec?
    val element: Element
    val type: TypeName
    val key: String
}

fun TargetFieldInfoInterface.asString() = "Field( name=$name, type=$type, owner=${ownerSpec?.name})"

inline fun <reified T> TargetFieldInfoInterface.hasAnnotation() =
    T::class.asTypeName() in propertySpec.annotations.map { it.typeName }
