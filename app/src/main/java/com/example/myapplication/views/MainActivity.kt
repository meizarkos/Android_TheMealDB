package com.example.myapplication.views

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.IngredientModel
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent


class MainActivity : AppCompatActivity(), IngredientOnClickLListener{

    private lateinit var ingredientListRecyclerView: RecyclerView
    private lateinit var ingredientList: ArrayList<IngredientModel>
    private lateinit var choiceIngredientList: MutableList<IngredientModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.choiceIngredientList = arrayListOf()
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

    private fun <T> MutableList<T>.moveLeft(position: Int) {
        val element = this[position]
        for (i in position downTo 1) {
            this[i] = this[i - 1]
        }
        this[0] = element
    }

    override fun handleListChoice(ingredient: IngredientModel, position: Int) {
        if(this.choiceIngredientList.contains(ingredient)){
            this.choiceIngredientList.remove(ingredient)
        }
        else{
            this.choiceIngredientList.add(ingredient)
        }
    }
}

interface IngredientOnClickLListener {
    fun handleListChoice(ingredient: IngredientModel, position: Int)
}