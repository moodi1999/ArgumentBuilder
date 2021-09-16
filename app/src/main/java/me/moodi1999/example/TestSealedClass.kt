package me.moodi1999.example

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class TestSealedClass : Parcelable {
    @Parcelize
    class First(val a: String): TestSealedClass()
    @Parcelize
    class Second(val b: Int): TestSealedClass()
}
