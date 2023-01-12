package com.example.cocktailbank.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Ingredient(
    @SerializedName("idIngredient")
    val idIngredient: String?,
    @SerializedName("strABV")
    val strABV: String?,
    @SerializedName("strAlcohol")
    val strAlcohol: String?,
    @SerializedName("strDescription")
    val strDescription: String?,
    @SerializedName("strIngredient")
    val strIngredient: String?,
    @SerializedName("strType")
    val strType: String?
): Parcelable