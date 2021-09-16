package me.moodi1999.argument_processor.operationGenerator.parameterized.array

import com.squareup.kotlinpoet.ARRAY
import com.squareup.kotlinpoet.ClassName
import me.moodi1999.argument_processor.operationGenerator.parameterized.base.ParameterizedTypeBOCG
import javax.lang.model.element.TypeElement

abstract class ArrayTypeBOCG : ParameterizedTypeBOCG() {

    protected val arrayArgumentType by lazy {
        argType.typeArguments.first() as ClassName
    }

    protected val arrayArgElement: TypeElement? by lazy {
        elementUtils.getTypeElement(arrayArgumentType.canonicalName)
    }

    override val isApplicable: Boolean
        get() = super.isApplicable && rawType.copy(nullable = false) == ARRAY
}