package me.moodi1999.argument_processor.operationGenerator.className

import com.squareup.kotlinpoet.*
import me.moodi1999.argument_processor.operationGenerator.className.base.ClassNameTypeBOCG

/**
 * @generates:
 * (put/get)Bundle
 * (put/get)String
 * (put/get)CharSequence
 *
 * */
class ReferencesTypeBOCG : ClassNameTypeBOCG() {

    override val isApplicable
        get() = super.isApplicable && argType.copy(nullable = false) in supportedClassName

    private val operationName: TypeName
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

    private val supportedClassName: List<TypeName>
        get() = listOf(
            ClassName("android.os", "Bundle"),
            STRING,
            CHAR_SEQUENCE,
        )
}

