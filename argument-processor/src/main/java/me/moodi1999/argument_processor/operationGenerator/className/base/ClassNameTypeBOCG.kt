package me.moodi1999.argument_processor.operationGenerator.className.base

import com.squareup.kotlinpoet.ClassName
import me.moodi1999.argument_processor.operationGenerator.base.BundleOperationCodeGenerator
import javax.lang.model.element.TypeElement

abstract class ClassNameTypeBOCG : BundleOperationCodeGenerator() {

    protected val argTypeElement: TypeElement? by lazy {
        elementUtils.getTypeElement(argType.canonicalName)
    }

    override val isApplicable: Boolean
        get() = argInfo.type is ClassName

    override val argType: ClassName
        get() = super.argType as ClassName
}