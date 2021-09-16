package me.moodi1999.argument_processor.operationGenerator.parameterized.base

import com.squareup.kotlinpoet.ParameterizedTypeName
import me.moodi1999.argument_processor.operationGenerator.base.BundleOperationCodeGenerator

abstract class ParameterizedTypeBOCG : BundleOperationCodeGenerator() {

    protected val rawType
        get() = argType.rawType

    override val isApplicable: Boolean
        get() = argInfo.type is ParameterizedTypeName

    override val argType: ParameterizedTypeName
        get() = super.argType as ParameterizedTypeName
}