package me.moodi1999.argument_processor.utils

import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element

fun ProcessingEnvironment.isFragmentClass(classElement: Element): Boolean {
    val fragment = elementUtils.getTypeElement("android.app.Fragment")
    val supportFragment = elementUtils.getTypeElement("android.support.v4.app.Fragment")
    val androidxFragment = elementUtils.getTypeElement("androidx.fragment.app.Fragment")
    val fragmentTypeElements = listOf(fragment, supportFragment, androidxFragment)
    for (fragmentTypeElement in fragmentTypeElements) {
        if (fragmentTypeElement != null && typeUtils.isSubtype(classElement.asType(), fragmentTypeElement.asType())) {
            return true
        }
    }
    return false
}

fun ProcessingEnvironment.isActivityClass(classElement: Element): Boolean {
    val activity = elementUtils.getTypeElement("android.app.Activity")
    return activity != null && typeUtils.isSubtype(classElement.asType(), activity.asType())
}
