package com.example.myapplication.utils

import com.example.myapplication.model.IngredientModel

fun containSubstring(source: String, letters: String): Boolean {
    return source.contains(letters, ignoreCase = true)
}

fun filterListBySubstring(sourceList: MutableList<IngredientModel>, substring: String): MutableList<IngredientModel> {
    return sourceList.filter { it.name?.contains(substring, ignoreCase = true) == true }.toMutableList()
}

fun transformNullinEmpty(chain:String?):String{
    if(chain == null){
        return ""
    }
    return chain
}