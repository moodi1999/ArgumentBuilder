package me.moodi1999.argument_processor.operationGenerator.parameterized.collection

import com.squareup.kotlinpoet.*
import me.moodi1999.argument_processor.operationGenerator.parameterized.base.ParameterizedTypeBOCG
import javax.lang.model.element.TypeElement

abstract class CollectionClassNameArgTypeBOCG : CollectionTypeBOCG() {

    protected val collectionArgumentType by lazy {
        argType.typeArguments.first() as ClassName
    }

    protected val collectionArgElement: TypeElement? by lazy {
        elementUtils.getTypeElement(collectionArgumentType.canonicalName)
    }

    override val isApplicable: Boolean
        get() = super.isApplicable && argType.typeArguments.first() is ClassName

}

