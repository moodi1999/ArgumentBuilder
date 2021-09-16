package me.moodi1999.example

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class SerializableClass(
    val a: Int,
    val b: String?
): Serializable {

}