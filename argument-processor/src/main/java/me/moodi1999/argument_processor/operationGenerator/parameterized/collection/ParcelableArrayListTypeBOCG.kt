package me.moodi1999.argument_processor.operationGenerator.parameterized.collection

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.MUTABLE_LIST

/**
 * @generates:
 * (put/get)ParcelableList
 *
 * */
class ParcelableArrayListTypeBOCG : CollectionClassNameArgTypeBOCG() {

    private val parcelableType by lazy {
        elementUtils.getTypeElement("android.os.Parcelable").asType()
    }

    override val isApplicable
        get() = super.isApplicable && collectionArgElement?.asType() isAssignableTo parcelableType

    private val operationName
        get() = "ParcelableList"

    // using KBundleOperationExtension::putParcelableList(key: String, value: List<T?>)
    override fun generatePutOperationCode() = CodeBlock.of(
        "put%L(%N, %N)",
        operationName,
        keyProperty,
        argName
    )

    // using KBundleOperationExtension::getParcelableList(key: String)
    override fun generateGetOperationCode() = CodeBlock.Builder()
        .add(
            "get%L<%T>(%N)",
            operationName,
            collectionArgumentType,
            keyProperty,
        )
        .apply {
            if (rawType.copy(nullable = false) == MUTABLE_LIST)
                add("?.toMutableList()")
        }
        .build()
}

