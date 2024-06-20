package com.example.myapplication.views

import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.IngredientModel
import com.example.myapplication.viewmodels.IngredientViewModel


class IngredientListAdapter(private val ingredients:MutableList<IngredientModel>): RecyclerView.Adapter<IngredientListAdapter.IngredientViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val ingredientView = LayoutInflater.from(parent.context)
            .inflate(R.layout.ingredient_cell_layout, parent, false)
        return IngredientViewHolder(ingredientView)
    }

    override fun getItemCount(): Int {
        return this.ingredients.size
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val currentIngredientData = this.ingredients[position] // Get the data at the right position
        holder.bind(currentIngredientData)
        holder.itemView.setOnClickListener {
            holder.updateBackgroundLayout(currentIngredientData,position)
        }
    }

    inner class IngredientViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var ingredientTextView : TextView
        private var layout: LinearLayout

        init {
            this.ingredientTextView = itemView.findViewById(R.id.cell_layout_ingredient_text_view)
            this.layout = itemView.findViewById(R.id.cell_layout_ingredient_linear_layout)
        }

        private fun createLayoutBackground(color:Int): GradientDrawable {
            return GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                cornerRadius = 40f
                setColor(color)
            }
        }

        private fun manageLayoutBackground(ingredient:IngredientModel) {
            if (ingredient.isSelected) {
                this.layout.background = createLayoutBackground(0xFFF3CE99.toInt())
            } else {
                this.layout.background = createLayoutBackground(0xFFE0E0E0.toInt())
            }
        }


        fun bind(ingredient:IngredientModel) {
            this.ingredientTextView.text = ingredient.name
            manageLayoutBackground(ingredient)
        }

        fun updateBackgroundLayout(ingredient:IngredientModel,position: Int) {
            ingredient.isSelected = !ingredient.isSelected
            if(IngredientViewModel.choiceIngredientList.contains(ingredient)) {
                IngredientViewModel.choiceIngredientList.remove(ingredient)
                IngredientViewModel.ingredientList.value?.set(position,ingredient)
            }
            else {
                IngredientViewModel.choiceIngredientList.add(ingredient)
                IngredientViewModel.ingredientList.value?.set(position,ingredient)
            }
            manageLayoutBackground(ingredient)
        }
    }
}