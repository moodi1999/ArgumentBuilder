package me.moodi1999.argument_processor.operationGenerator.parameterized.array

import com.squareup.kotlinpoet.CHAR_SEQUENCE
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeName

/**
 * @generates:
 * (put/get)StingArray
 * (put/get)CharSequenceArray
 *
 * */
class StringAndCharSeqArrayTypeBOCG : ArrayTypeBOCG() {

    override val isApplicable
        get() = super.isApplicable
            && arrayArgumentType in supportedTypes


    private val nonNullArrayArgumentType: TypeName
        get() = arrayArgumentType.copy(nullable = false)

    override fun generatePutOperationCode() = CodeBlock.of(
        "put%TArray(%N, %N)",
        nonNullArrayArgumentType,
        keyProperty,
        argName,
    )

    override fun generateGetOperationCode() = CodeBlock.of(
        "get%TArray(%N)",
        nonNullArrayArgumentType,
        keyProperty,
    )

    private val supportedTypes = arrayOf(
        STRING,
        CHAR_SEQUENCE,
        STRING.copy(nullable = true),
        CHAR_SEQUENCE.copy(nullable = true)
    )
}
