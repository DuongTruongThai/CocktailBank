package com.example.cocktailbank.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class IngredientList(
    @SerializedName("ingredients")
    val ingredients: List<Ingredient>
): Parcelable