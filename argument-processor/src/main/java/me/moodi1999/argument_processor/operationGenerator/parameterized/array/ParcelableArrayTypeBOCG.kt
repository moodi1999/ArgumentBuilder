package me.moodi1999.argument_processor.operationGenerator.parameterized.array

import com.squareup.kotlinpoet.CodeBlock

/**
 * @generates:
 * (put/get)ParcelableArray
 *
 * */
class ParcelableArrayTypeBOCG : ArrayTypeBOCG() {

    private val parcelableType by lazy {
        elementUtils.getTypeElement("android.os.Parcelable").asType()
    }

    override val isApplicable
        get() = super.isApplicable && arrayArgElement?.asType() isAssignableTo parcelableType

    private val operationName
        get() = "ParcelableArray"

    override fun generatePutOperationCode() = CodeBlock.of(
        "put%L(%N, %N)",
        operationName,
        keyProperty,
        argName
    )

    override fun generateGetOperationCode() = CodeBlock.of(
        "get%L<%T>(%N)",
        operationName,
        arrayArgumentType,
        keyProperty,
    )
}

