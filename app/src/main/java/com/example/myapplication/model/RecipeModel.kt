package com.example.myapplication.model

import android.os.Parcelable

data class RecipeModel(val idMeal: String?, val strMealThumb: String?, val strMeal: String?):Parcelable{
    constructor(parcel: android.os.Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun describeContents(): Int {
        return hashCode()
    }

    override fun writeToParcel(parcel: android.os.Parcel, flags: Int) {
        parcel.writeString(idMeal)
        parcel.writeString(strMealThumb)
        parcel.writeString(strMeal)
    }

    companion object CREATOR : Parcelable.Creator<RecipeModel> {
        override fun createFromParcel(parcel: android.os.Parcel): RecipeModel {
            return RecipeModel(parcel)
        }

        override fun newArray(size: Int): Array<RecipeModel?> {
            return arrayOfNulls(size)
        }
    }

}