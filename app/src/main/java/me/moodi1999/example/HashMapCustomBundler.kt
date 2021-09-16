package me.moodi1999.example

import android.os.Bundle
import me.moodi1999.argument_annotation.bundler.ArgumentBundler

object HashMapCustomBundler : ArgumentBundler<HashMap<out ParcelableClass?, out String?>?> {
    override fun put(key: String?, value: HashMap<out ParcelableClass?, out String?>?, bundle: Bundle) {
        println("some Testy")
    }

    override fun <V : HashMap<out ParcelableClass?, out String?>??> get(key: String?, bundle: Bundle): V? {
        return hashMapOf<ParcelableClass?, String?>() as V?
    }

}