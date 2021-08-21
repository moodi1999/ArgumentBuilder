package me.moodi1999.argument_annotation.bundler

import android.os.Bundle
import android.os.Parcelable

class CastedArrayListArgumentBundler : ArgumentBundler<List<Parcelable?>> {
    override fun put(key: String?, value: List<Parcelable?>, bundle: Bundle) {
        if (value !is ArrayList<*>) {
            throw ClassCastException(
                "CastedArrayListArgsBundler assumes that the List is instance of ArrayList, but it's instance of "
                    + value.javaClass.canonicalName
            )
        }
        bundle.putParcelableArrayList(key, value as ArrayList<out Parcelable?>)
    }

    override fun <T : List<Parcelable?>?> get(key: String?, bundle: Bundle): T? {
        return bundle.get(key) as T?
    }
}