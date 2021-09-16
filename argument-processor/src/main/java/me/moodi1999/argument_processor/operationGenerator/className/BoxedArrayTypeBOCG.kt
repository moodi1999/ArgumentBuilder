package me.moodi1999.argument_processor.operationGenerator.className

import com.squareup.kotlinpoet.*
import me.moodi1999.argument_processor.operationGenerator.className.base.ClassNameTypeBOCG

/**
 * @generates:
 * (put/get)BooleanArray
 * (put/get)ByteArray
 * (put/get)CharArray
 * (put/get)ShortArray
 * (put/get)IntArray
 * (put/get)LongArray
 * (put/get)FloatArray
 * (put/get)DoubleArray
 *
 * */
class BoxedArrayTypeBOCG : ClassNameTypeBOCG() {

    override val isApplicable
        get() = super.isApplicable && argType.copy(nullable = false) in supportedBoxedArray

    private val operationName
        get() = argType.copy(nullable = false)

    override fun generatePutOperationCode() = CodeBlock.of(
        "put%T(%N, %N)",
        operationName,
        keyProperty,
        argName
    )

    override fun generateGetOperationCode() = CodeBlock.of(
        "get%T(%N)",
        operationName,
        keyProperty,
    )

    private val supportedBoxedArray: List<TypeName>
        get() = listOf(
            BOOLEAN_ARRAY,
            BYTE_ARRAY,
            SHORT_ARRAY,
            INT_ARRAY,
            LONG_ARRAY,
            CHAR_ARRAY,
            FLOAT_ARRAY,
            DOUBLE_ARRAY,
        )

}
