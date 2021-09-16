package me.moodi1999.argument_processor.operationGenerator.parameterized.collection

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.PropertySpec
import me.moodi1999.argument_processor.model.ArgumentInfo
import me.moodi1999.argument_processor.model.TargetFieldInfo
import me.moodi1999.argument_processor.operationGenerator.BOCGHelper
import me.moodi1999.argument_processor.operationGenerator.generateGetOperation
import me.moodi1999.argument_processor.operationGenerator.generateNullAssertionIfNullable
import me.moodi1999.argument_processor.operationGenerator.generatePutOperation
import javax.lang.model.element.TypeElement


class CollectionParameterizedArgTypeBOCG : CollectionTypeBOCG() {

    private val arguemntType by lazy {
        argType.typeArguments.first() as ParameterizedTypeName
    }

    private val argumentElement: TypeElement? by lazy {
        elementUtils.getTypeElement(arguemntType.rawType.canonicalName)
    }

    override val isApplicable: Boolean
        get() = super.isApplicable && argType.typeArguments.first() is ParameterizedTypeName

    private val valueArgumentInfo by lazy {

        ArgumentInfo(
            TargetFieldInfo(
                "lValue",
                argumentElement,
                PropertySpec.builder("lValue", arguemntType).build(),
            ),
            _key = "value_key_${hashCode()}",
            isRequired = true,
        )
    }

    override fun generatePutOperationCode() = CodeBlock.Builder().apply {
        add(
            """
            putBundle(%N, Bundle().apply {
                %N%L.onEachIndexed { index, %L ->
                   val %N = %P
                   %L
                }
            })
        """.trimIndent(),
            keyProperty,
            argName,
            argType.generateNullAssertionIfNullable(),
            valueArgumentInfo.name,
            BOCGHelper.getGenerateKeyProperty(valueArgumentInfo.key, isConst = false),
            "value" + "$" + "index",
            BOCGHelper.getBundleOperationGeneratorsSequence(
                valueArgumentInfo,
                processingEnv,
            ).generatePutOperation(),
        )
    }.build()

    override fun generateGetOperationCode(): CodeBlock {
        val outListVarName = "outList${hashCode()}"
        val indexVarName = "index${hashCode()}"
        return CodeBlock.of(
            """
            getBundle(%N)?.run {
                val %L = mutableListOf<%T>()
                for (%L in 0..size()) {
                    val %N = %P
                    %L.add(%L%L)
                }
                %L
            }
            """.trimIndent(),
            keyProperty,
            outListVarName,
            arguemntType,
            indexVarName,
            BOCGHelper.getGenerateKeyProperty(valueArgumentInfo.key, isConst = false),
            "value$$indexVarName",
            outListVarName,
            BOCGHelper.getBundleOperationGeneratorsSequence(
                valueArgumentInfo,
                processingEnv,
            ).generateGetOperation(),
            if (arguemntType.isNullable) "" else "!!",
            outListVarName,
        )
    }

}

