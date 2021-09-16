package me.moodi1999.argument_processor.operationGenerator.parameterized.map

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.PropertySpec
import me.moodi1999.argument_processor.model.ArgumentInfo
import me.moodi1999.argument_processor.model.TargetFieldInfo
import me.moodi1999.argument_processor.operationGenerator.BOCGHelper
import me.moodi1999.argument_processor.operationGenerator.BOCGHelper.getBundleOperationGeneratorsSequence
import me.moodi1999.argument_processor.operationGenerator.generateGetOperation
import me.moodi1999.argument_processor.operationGenerator.generateNullAssertionIfNullable
import me.moodi1999.argument_processor.operationGenerator.generatePutOperation


/**
 * @generates:
 * (put/get)Map
 *
 * */
class MapEntryIterateTypeBOCG : MapTypeBOCG() {

    private val keyArgumentInfo by lazy {

        ArgumentInfo(
            TargetFieldInfo(
                "mKey",
                argumentsElement.first,
                PropertySpec.builder("mKey", argumentsType.first).build(),
            ),
            _key = "key_key_${hashCode()}",
            isRequired = true,
        )
    }

    private val valueArgumentInfo by lazy {
        ArgumentInfo(
            TargetFieldInfo(
                "mValue",
                argumentsElement.second,
                PropertySpec.builder("mValue", argumentsType.second).build(),
            ),
            _key = "value_key_${hashCode()}",
            isRequired = true,
        )
    }

    override fun generatePutOperationCode() = CodeBlock.Builder().apply {
        add(
            """
            putBundle(%N, Bundle().apply {
                %N%L.onEachIndexed { index, (%L, %L) ->
                   val %N = %P
                   val %N = %P
                   %L
                   %L
                }
            })
        """.trimIndent(),
            keyProperty,
            argName,
            argType.generateNullAssertionIfNullable(),
            keyArgumentInfo.name,
            valueArgumentInfo.name,
            BOCGHelper.getGenerateKeyProperty(keyArgumentInfo.key, isConst = false),
            "key" + "$" + "index",
            BOCGHelper.getGenerateKeyProperty(valueArgumentInfo.key, isConst = false),
            "value" + "$" + "index",
            getBundleOperationGeneratorsSequence(
                keyArgumentInfo,
                processingEnv,
            ).generatePutOperation(),
            getBundleOperationGeneratorsSequence(
                valueArgumentInfo,
                processingEnv,
            ).generatePutOperation(),
        )
    }.build()


    override fun generateGetOperationCode(): CodeBlock {
        val outMapVarName = "outMap${hashCode()}"
        val indexVarName = "index${hashCode()}"
        return CodeBlock.of(
            """
            getBundle(%N)?.run {
                val %L = mutableMapOf<%T, %T>()
                for (%L in 0..(size() / 2)) {
                    val %N = %P
                    val %N = %P
                    %L[%L!!] = %L%L
                }
                %L
            }
            """.trimIndent(),
            keyProperty,
            outMapVarName,
            argumentsType.first,
            argumentsType.second,
            indexVarName,
            BOCGHelper.getGenerateKeyProperty(keyArgumentInfo.key, isConst = false),
            "key$$indexVarName",
            BOCGHelper.getGenerateKeyProperty(valueArgumentInfo.key, isConst = false),
            "value$$indexVarName",
            outMapVarName,
            getBundleOperationGeneratorsSequence(
                keyArgumentInfo,
                processingEnv,
            ).generateGetOperation(),
            getBundleOperationGeneratorsSequence(
                valueArgumentInfo,
                processingEnv,
            ).generateGetOperation(),
            if (argumentsType.second.isNullable) "" else "!!",
            outMapVarName
        )
    }

}

