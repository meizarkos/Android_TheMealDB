package com.example.myapplication.model

import android.os.Parcelable

data class IngredientModel(val id:String?,val name: String?, var isSelected: Boolean = false):Parcelable{
    constructor(parcel: android.os.Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun describeContents(): Int {
        return hashCode()
    }

    override fun writeToParcel(parcel: android.os.Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeByte(if (isSelected) 1 else 0)
    }

    companion object CREATOR : Parcelable.Creator<IngredientModel> {
        override fun createFromParcel(parcel: android.os.Parcel): IngredientModel {
            return IngredientModel(parcel)
        }

        override fun newArray(size: Int): Array<IngredientModel?> {
            return arrayOfNulls(size)
        }
    }
}