package me.moodi1999.argument_processor.model

import me.moodi1999.argument_processor.utils.isActivityClass
import me.moodi1999.argument_processor.utils.isFragmentClass
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element


sealed class TargetType(val name: String, val argumentsStore: String) {
    object Activity : TargetType("Activity","intent.extras")
    object Fragment : TargetType("Fragment","arguments")
}

fun ProcessingEnvironment.findTargetType(element: Element): TargetType? = when {
    isFragmentClass(element) -> TargetType.Fragment
    isActivityClass(element) -> TargetType.Activity
    else -> null
}