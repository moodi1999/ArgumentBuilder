package me.moodi1999.argument_processor.operationGenerator.parameterized.array

import com.squareup.kotlinpoet.CodeBlock
import javax.lang.model.type.TypeMirror

/**
 * @generates:
 * (put/get)ParcelableSparseArray
 *
 * */
class ParcelableSparseArrayTypeBOCG : ArrayTypeBOCG() {

    override val isApplicable
        get() = super.isApplicable && argElement.asType() isAssignableTo getWildcardType()

    private fun getWildcardType(): TypeMirror {
        return processingEnv.typeUtils.getDeclaredType(
            elementUtils.getTypeElement("android.util.SparseArray"),
            processingEnv.typeUtils.getWildcardType(elementUtils.getTypeElement("android.os.Parcelable").asType(), null)
        )
    }

    private val operationName
        get() = "SparseParcelableArray"

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

