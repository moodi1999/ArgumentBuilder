package me.moodi1999.argument_annotation.bundler

import android.os.Bundle


interface ArgumentBundler<T> {
    /**
     * Put (save) a value into the bundle.
     *
     * @param key The key you have to use as the key for the bundle to save the value
     * @param value The value you have to save into the bundle (for the given key)
     * @param bundle The Bundle to save key / value. It's not null.
     */
    fun put(key: String?, value: T, bundle: Bundle)

    /**
     * Get a value from the bundle
     *
     * @param key The key for the value
     * @param bundle The Bundle where the value is saved in
     * @return The value retrieved from the Bundle with the given key
     */
    operator fun <V : T?> get(key: String?, bundle: Bundle): V?
}