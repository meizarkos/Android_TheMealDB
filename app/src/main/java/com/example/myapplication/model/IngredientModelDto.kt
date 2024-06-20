package com.example.myapplication.model
data class IngredientListDto(
    val meals: List<IngredientModelDto>
)
data class IngredientModelDto(
    val idIngredient: String,
    val strIngredient: String,
    val strDescription: String,
    val strType: String
)