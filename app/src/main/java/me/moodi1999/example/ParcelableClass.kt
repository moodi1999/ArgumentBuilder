package me.moodi1999.example

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class ParcelableClass(
    val a: Int,
    val b: String?
) : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(a)
        parcel.writeString(b)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ParcelableClass> {
        override fun createFromParcel(parcel: Parcel): ParcelableClass {
            return ParcelableClass(parcel)
        }

        override fun newArray(size: Int): Array<ParcelableClass?> {
            return arrayOfNulls(size)
        }
    }
}