package me.moodi1999.argument_processor.operationGenerator.className

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import me.moodi1999.argument_processor.operationGenerator.className.base.ClassNameTypeBOCG
import java.io.Serializable

/**
 * @generates:
 * (put/get)Serializable
 *
 * */
class SerializableTypeBOCG : ClassNameTypeBOCG() {

    private val serializableType by lazy {
        elementUtils.getTypeElement(Serializable::class.java.name).asType()
    }

    private val kotlinHashMapClassName = ClassName("kotlin.collections", "HashMap")
    private val javaHashMapClassName = ClassName("java.util", "HashMap")

    override val isApplicable
        get() = super.isApplicable && argElement.asType() isAssignableTo serializableType
            && argType.copy(nullable = false) !in arrayOf(kotlinHashMapClassName, javaHashMapClassName)

    private val operationName
        get() = "Serializable"

    override fun generatePutOperationCode() = CodeBlock.of(
        "put%L(%N, %N)",
        operationName,
        keyProperty,
        argName
    )

    override fun generateGetOperationCode() = CodeBlock.of(
        "get%L(%N) as %T",
        operationName,
        keyProperty,
        argType
    )
}

