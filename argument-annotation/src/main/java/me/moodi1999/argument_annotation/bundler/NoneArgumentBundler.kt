package me.moodi1999.argument_annotation.bundler

import android.os.Bundle

class NoneArgumentBundler private constructor() : ArgumentBundler<Any?> {
    override fun put(key: String?, value: Any?, bundle: Bundle) {}

    override fun <V> get(key: String?, bundle: Bundle): V? = null
}