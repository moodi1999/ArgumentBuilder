package me.moodi1999.argument_processor.processor

import javax.lang.model.element.Element

/**
 * A simple exception that will be thrown if something went wrong (error message will be printed
 * before throwing exception)
 *
 * @author Hannes Dorfmann
 */
class ProcessingException(
    val element: Element,
    override val message: String,
    vararg messageArgs: Any,
) : Exception() {
    val messageArgs: Array<out Any> = messageArgs
}