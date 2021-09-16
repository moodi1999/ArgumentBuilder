package me.moodi1999.argument_processor.operationGenerator.className

import com.squareup.kotlinpoet.*
import me.moodi1999.argument_processor.operationGenerator.className.base.ClassNameTypeBOCG
import me.moodi1999.argument_processor.operationGenerator.generateNullAssertionIfNullable

/**
 * @generates:
 * (put/get)Boolean
 * (put/get)Byte
 * (put/get)Char
 * (put/get)Short
 * (put/get)Int
 * (put/get)Long
 * (put/get)Float
 * (put/get)Double
 *
 * */
class ScalarsTypeBOCG : ClassNameTypeBOCG() {

    override val isApplicable
        get() = super.isApplicable && argType.copy(nullable = false) in supportedClassName

    private val operationName: TypeName
        get() = argType.copy(nullable = false)

    override fun generatePutOperationCode() = CodeBlock.of(
        "put%T(%N, %N%L)",
        operationName,
        keyProperty,
        argName,
        argType.generateNullAssertionIfNullable()
    )

    override fun generateGetOperationCode() = CodeBlock.of(
        "get%T(%N)",
        operationName,
        keyProperty,
    )

    private val supportedClassName: List<TypeName>
        get() = listOf(
            BOOLEAN,
            BYTE,
            CHAR,
            SHORT,
            INT,
            LONG,
            FLOAT,
            DOUBLE,
        )
}

