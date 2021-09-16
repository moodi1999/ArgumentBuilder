package me.moodi1999.argument_processor.operationGenerator.parameterized.map

import com.squareup.kotlinpoet.*
import me.moodi1999.argument_processor.operationGenerator.parameterized.base.ParameterizedTypeBOCG
import javax.lang.model.element.TypeElement

abstract class MapTypeBOCG : ParameterizedTypeBOCG() {

    protected val argumentsType by lazy {
        Pair(
            argType.typeArguments[0],
            argType.typeArguments[1],
        )
    }

    // TODO(arm): 9/4/2021 check TypeVariableName
    protected val argumentsElement: Pair<TypeElement?, TypeElement?> by lazy {
        val first = argType.typeArguments[0]
        val second = argType.typeArguments[1]
        Pair(
            elementUtils.getTypeElement(
                when (first) {
                    is ClassName -> first.canonicalName
                    is ParameterizedTypeName -> first.rawType.canonicalName
                    is TypeVariableName -> first.name
                    else -> null
                }
            ),
            elementUtils.getTypeElement(
                when (second) {
                    is ClassName -> second.canonicalName
                    is ParameterizedTypeName -> second.rawType.canonicalName
                    is TypeVariableName -> second.name
                    else -> null
                }
            )
        )
    }

    override val isApplicable: Boolean
        get() = super.isApplicable && rawType.copy(nullable = false) in supportedMapCollections
//            && argType.typeArguments[0] in arrayOf(ClassName, ParameterizedTypeName, TypeVariableName)
//            && argType.typeArguments[1] in arrayOf(ClassName, ParameterizedTypeName, TypeVariableName)

    private val supportedMapCollections = listOf(
        MAP,
        MAP_ENTRY,
        MUTABLE_MAP,
        MUTABLE_MAP_ENTRY,
        MAP.copy(nullable = true),
        MAP_ENTRY.copy(nullable = true),
        MUTABLE_MAP.copy(nullable = true),
        MUTABLE_MAP_ENTRY.copy(nullable = true),
    )

}

