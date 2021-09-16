package me.moodi1999.argument_annotation.bundler

import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable

// region put

inline fun Bundle.put(key: String, value: Array<out Int?>) = putIntList(key, value.toList())
inline fun Bundle.putIntList(key: String, value: List<Int?>) =
    putIntegerArrayList(key, value.toList() as ArrayList<Int?>)

inline fun Bundle.putCharSequenceList(key: String, value: List<CharSequence?>) =
    putCharSequenceArrayList(key, value as ArrayList<CharSequence?>)

inline fun Bundle.putStringList(key: String, value: List<String?>) =
    putStringArrayList(key, value as ArrayList<String?>)

inline fun <reified T : Parcelable> Bundle.putParcelableList(key: String, value: List<T?>) =
    putParcelableArrayList(key, value.toList() as ArrayList<T?>)

inline fun <reified T, reified S> Bundle.put(key: String, value: Map<T, S>) =
    putSerializable(key, value as HashMap<T, S>)

inline fun Bundle.put(key: String) = putString(key, null)

inline fun <reified T> Bundle.putMap(name: String, map: T) where T : Map<*, *>?, T : Serializable? {
    putSerializable(name, MapWrapper(map))
}
// endregion

// region get
inline fun Bundle.getNullableIntArray(key: String): Array<Int?>? = getIntList(key)?.toTypedArray()

inline fun Bundle.getIntList(key: String) = getIntegerArrayList(key)?.toList()
inline fun Bundle.getStringList(key: String) = getStringArrayList(key)?.toList()
inline fun Bundle.getCharSequenceList(key: String) = getCharSequenceArrayList(key)?.toList()

inline fun <reified T : Parcelable> Bundle.getParcelableList(key: String) = getParcelableArrayList<T>(key)?.toList()

@Suppress("UNCHECKED_CAST")
@Throws(ClassCastException::class)
inline fun <reified T> Bundle.getMap(name: String): T? where T : Map<*, *>?, T : Serializable? {
    val s = getSerializable(name)
    return if (s == null) null else (s as MapWrapper<T>).map
}
// endregion