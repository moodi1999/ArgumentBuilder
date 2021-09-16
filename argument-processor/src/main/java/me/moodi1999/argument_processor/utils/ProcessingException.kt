package me.moodi1999.argument_processor.utils

import me.moodi1999.argument_processor.model.TargetFieldInfoInterface
import me.moodi1999.argument_processor.model.asString
import javax.lang.model.element.Element

/**
 * A simple exception that will be thrown if something went wrong (error message will be printed
 * before throwing exception)
 *
 */
open class ProcessingException(
    val element: Element?,
    override val message: String,
    vararg messageArgs: Any,
) : Exception() {
    val messageArgs: Array<out Any> = messageArgs
}

class InvalidTargetTypeException(element: Element?) :
    ProcessingException(element, "@ArgumentBuilderTarget can be used only for Fragment and Activity")

class NoArgumentBundlerFound(argumentInfo: TargetFieldInfoInterface) :
    ProcessingException(null, "No internal or custom Bundler found for field= ${argumentInfo.asString()}")