package com.example.myapplication.views

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.IngredientModel

class IngredientListAdapter(private val ingredients:ArrayList<IngredientModel>, private val ingredientClickHandler: IngredientOnClickLListener): RecyclerView.Adapter<IngredientListAdapter.IngredientViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val ingredientView = LayoutInflater.from(parent.context)
            .inflate(R.layout.ingredient_cell_layout, parent, false)
        return IngredientViewHolder(ingredientView)
    }

    override fun getItemCount(): Int {
        return this.ingredients.size
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val currentTodoData = this.ingredients[position] // Get the data at the right position
        holder.bind(currentTodoData)
        holder.itemView.setOnClickListener {
            ingredientClickHandler.handleListChoice(currentTodoData, position)
        }
    }

    // Class representing the object linked to the XML
    // of on cell of the RV
    inner class IngredientViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var ingredient : TextView
        private var layout:LinearLayout

        init {
            this.ingredient = itemView.findViewById(R.id.cell_layout_ingredient_text_view)
            this.layout = itemView.findViewById(R.id.cell_layout_ingredient_linear_layout)
        }

        fun bind(ingredient:IngredientModel) {
            this.ingredient.text = ingredient.name
            this.ingredient.setOnClickListener{
                
            }
        }
    }
}