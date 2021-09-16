package me.moodi1999.argument_processor.operationGenerator.className

import com.squareup.kotlinpoet.CodeBlock
import me.moodi1999.argument_processor.operationGenerator.className.base.ClassNameTypeBOCG
import javax.lang.model.element.ElementKind

/**
 * @generates:
 * puts and gets Enums
 *
 * */
class EnumTypeBOCG : ClassNameTypeBOCG() {

    override val isApplicable
        get() = super.isApplicable && argTypeElement?.kind == ElementKind.ENUM

    override fun generatePutOperationCode() = CodeBlock.of(
        "putString(%N, %N.name)",
        keyProperty,
        argName
    )

    override fun generateGetOperationCode() = CodeBlock.of(
        "getString(%N)!!.run { %T.valueOf(this) }",
        keyProperty,
        argType.copy(nullable = false),
    )
}

