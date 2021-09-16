package me.moodi1999.argument_processor.operationGenerator.parameterized.array

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.INT

/**
 * @generates:
 * (put/get)StingArray
 * (put/get)CharSequenceArray
 * put
 * getNullableIntArray
 *
 * */
class NullableIntArrayTypeBOCG : ArrayTypeBOCG() {

    override val isApplicable
        get() = super.isApplicable && arrayArgumentType == INT.copy(nullable = true)

    // using KBundleOperationExtension::put(key: String, value: Array<out Int?>)
    override fun generatePutOperationCode() = CodeBlock.of(
        "put(%N, %N)",
        keyProperty,
        argName
    )

    // using KBundleOperationExtension::getNullableIntArray(key: String, value: Array<out Int?>)
    override fun generateGetOperationCode() = CodeBlock.of(
        "getNullableIntArray(%N)",
        keyProperty,
    )

}
