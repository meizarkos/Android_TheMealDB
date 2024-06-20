package com.example.myapplication.network

import com.example.myapplication.model.IngredientListDto
import retrofit2.Call
import retrofit2.http.GET

interface IngredientServices {
    @GET("list.php?i=list")
    fun getIngredients(): Call<IngredientListDto>
}