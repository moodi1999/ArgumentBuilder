package me.moodi1999.argument_processor.operationGenerator.className

import com.squareup.kotlinpoet.CodeBlock
import me.moodi1999.argument_processor.operationGenerator.className.base.ClassNameTypeBOCG

/**
 * @generates:
 * (put/get)Parcelable
 *
 * */
class ParcelableTypeBOCG : ClassNameTypeBOCG() {

    private val parcelableType by lazy {
        elementUtils.getTypeElement("android.os.Parcelable").asType()
    }

    override val isApplicable
        get() = super.isApplicable && argElement.asType() isAssignableTo parcelableType

    private val operationName
        get() = "Parcelable"

    override fun generatePutOperationCode() = CodeBlock.of(
        "put%L(%N, %N)",
        operationName,
        keyProperty,
        argName
    )

    override fun generateGetOperationCode() = CodeBlock.of(
        "get%L<%T>(%N)",
        operationName,
        argType,
        keyProperty,
    )
}

