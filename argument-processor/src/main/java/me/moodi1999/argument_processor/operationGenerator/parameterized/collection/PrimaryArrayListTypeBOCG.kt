package me.moodi1999.argument_processor.operationGenerator.parameterized.collection

import com.squareup.kotlinpoet.*

/**
 * @generates:
 * (put/get)IntList
 * (put/get)CharSequenceList
 * (put/get)StringList
 *
 * */
class PrimaryArrayListTypeBOCG : CollectionClassNameArgTypeBOCG() {

    override val isApplicable
        get() = super.isApplicable && collectionArgumentType in supportedPrimaryArrayList

    private val operationName
        get() = (collectionArgumentType.copy(nullable = false) as ClassName).simpleName + "List"

    // using KBundleOperationExtension::put(key: String, value: Array<out Int?>)
    override fun generatePutOperationCode() = CodeBlock.of(
        "put%L(%N, %N)",
        operationName,
        keyProperty,
        argName
    )

    // using KBundleOperationExtension::getNullableIntArray(key: String, value: Array<out Int?>)
    override fun generateGetOperationCode() = CodeBlock.of(
        "get%L(%N)",
        operationName,
        keyProperty,
    )

    private val supportedPrimaryArrayList = arrayOf(
        INT,
        STRING,
        CHAR_SEQUENCE,
        INT.copy(nullable = true),
        STRING.copy(nullable = true),
        CHAR_SEQUENCE.copy(nullable = true),
    )

}

