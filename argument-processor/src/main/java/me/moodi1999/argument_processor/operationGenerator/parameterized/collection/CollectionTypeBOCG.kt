package me.moodi1999.argument_processor.operationGenerator.parameterized.collection

import com.squareup.kotlinpoet.*
import me.moodi1999.argument_processor.operationGenerator.parameterized.base.ParameterizedTypeBOCG

abstract class CollectionTypeBOCG : ParameterizedTypeBOCG() {

    override val isApplicable: Boolean
        get() = super.isApplicable && rawType.copy(nullable = false) in supportedListCollections

    private val supportedListCollections = listOf(
        COLLECTION,
        LIST,
        SET,
        MUTABLE_ITERABLE,
        MUTABLE_COLLECTION,
        MUTABLE_LIST,
        MUTABLE_SET,
    )
}

