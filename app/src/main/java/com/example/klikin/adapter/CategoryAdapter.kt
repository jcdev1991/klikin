package com.example.klikin.adapter

import android.graphics.Color
import android.location.Location
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.klikin.App
import com.example.klikin.R
import com.example.klikin.databinding.ItemCategoryBinding
import com.example.klikin.model.Category
import com.example.klikin.util.AppConstants
import java.util.*

class CategoryAdapter(private var categories: List<Category>, private var onClickAdapterListener: SetOnClickAdapterListener)
    : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    interface SetOnClickAdapterListener {
        fun onClick(position: Int)
    }

    inner class CategoryViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    private var _binding: ItemCategoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        _binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        with (holder) {
            with (categories[position]) {
                binding.llCategory.setOnClickListener {
                    onClickAdapterListener.onClick(position)
                }
                binding.ivIcon.setImageDrawable(this.icon)
                when (this.name) {
                    AppConstants.CATEGORY_SHOPPING -> {
                        binding.tvName.setTextColor(Color.parseColor("#00BCD4"))
                    }
                    AppConstants.CATEGORY_FOOD -> {
                        binding.tvName.setTextColor(Color.parseColor("#FFC107"))
                    }
                    AppConstants.CATEGORY_BEAUTY -> {
                        binding.tvName.setTextColor(Color.parseColor("#009688"))
                    }
                    AppConstants.CATEGORY_LEISURE -> {
                        binding.tvName.setTextColor(Color.parseColor("#673AB7"))
                    }
                    AppConstants.CATEGORY_OTHER -> {
                        binding.tvName.setTextColor(Color.parseColor("#E91E63"))
                    }
                    "DELETE FILTERS" -> {
                        binding.llCategory.setBackgroundColor(Color.parseColor("#484848"))
                        binding.tvName.setTextColor(Color.parseColor("#FFFFFF"))
                    }
                }
                binding.tvName.text = this.name
            }
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }
}