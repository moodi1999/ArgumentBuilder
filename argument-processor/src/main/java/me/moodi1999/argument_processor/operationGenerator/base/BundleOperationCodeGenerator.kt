package me.moodi1999.argument_processor.operationGenerator.base

import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import me.moodi1999.argument_processor.model.TargetFieldInfoInterface
import me.moodi1999.argument_processor.operationGenerator.BOCGHelper
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements

/**
 * BOCG => Bundle Operation Code Generator
 * */
abstract class BundleOperationCodeGenerator() {

    lateinit var processingEnv: ProcessingEnvironment
    lateinit var argInfo: TargetFieldInfoInterface

    constructor(processingEnv: ProcessingEnvironment, argInfo: TargetFieldInfoInterface) : this() {
        this.processingEnv = processingEnv
        this.argInfo = argInfo
    }

    fun initializePrimaryProps(processingEnv: ProcessingEnvironment, argInfo: TargetFieldInfoInterface) = apply {
        this.processingEnv = processingEnv
        this.argInfo = argInfo
    }

    abstract val isApplicable: Boolean

    abstract fun generatePutOperationCode(): CodeBlock
    abstract fun generateGetOperationCode(): CodeBlock

    protected open val argType: TypeName
        get() = argInfo.type

    protected val argName: String
        get() = argInfo.name

    protected val keyProperty: PropertySpec
        get() = BOCGHelper.getGenerateKeyProperty(argInfo.key)

    protected val argElement: Element
        get() = argInfo.element

    protected val elementUtils: Elements
        get() = processingEnv.elementUtils

    infix fun TypeMirror?.isAssignableTo(other: TypeMirror?) = processingEnv.typeUtils.isAssignable(this, other)
}