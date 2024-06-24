package com.example.myapplication.model

data class RecipeListDto(
    val meals: List<RecipeDto>
)
data class RecipeDto(
    val idMeal: String,
    val strMealThumb: String,
    val strMeal: String,
)