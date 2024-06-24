package com.example.myapplication.utils

import android.widget.TextView
import androidx.appcompat.widget.SearchView

fun setSearchViewTextColor(searchView: SearchView, color: Int) {
    val searchText = searchView.findViewById<TextView>(androidx.appcompat.R.id.search_src_text)
    searchText.setTextColor(color)
    searchText.setHintTextColor(color) // Optional: Also change the hint text color
}