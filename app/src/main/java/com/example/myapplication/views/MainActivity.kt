package com.example.myapplication.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.IngredientModel
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent


class MainActivity : AppCompatActivity(), IngredientOnClickLListener{

    private lateinit var ingredientListRecyclerView: RecyclerView
    private lateinit var ingredientList: ArrayList<IngredientModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.ingredientList = arrayListOf(IngredientModel("Eggsaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"),IngredientModel("Eggs"),IngredientModel("Eggs"),IngredientModel("Eggs"), IngredientModel("Milk"), IngredientModel("Flour"), IngredientModel("Sugar"), IngredientModel("Butter"))
        setUpActivityViews(this.ingredientList)
    }

    private fun setUpActivityViews(data: ArrayList<IngredientModel>) {
        this.ingredientListRecyclerView = findViewById(R.id.ingredient_list_recycler_view)

        // Setup RV Adapter
        val ingredientAdapter = IngredientListAdapter(data, this)

        // Setup Linear layout manager
        val flexboxLayoutManager = FlexboxLayoutManager(this).apply {
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
        }

        this.ingredientListRecyclerView.layoutManager = flexboxLayoutManager
        this.ingredientListRecyclerView.setAdapter(ingredientAdapter)
    }
}

interface IngredientOnClickLListener {
    //fun displayIngredientDetail(ingredient: String, position: Int)
}