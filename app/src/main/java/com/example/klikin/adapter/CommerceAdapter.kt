package com.example.klikin.adapter

import android.graphics.Color
import android.location.Location
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.klikin.App
import com.example.klikin.util.AppConstants
import com.example.klikin.R
import com.example.klikin.databinding.ItemCommerceBinding
import com.example.klikin.model.Commerce
import com.squareup.picasso.Picasso

class CommerceAdapter(var commerces: List<Commerce>, private var onClickAdapterListener: SetOnClickAdapterListener)
    : RecyclerView.Adapter<CommerceAdapter.CommerceViewHolder>() {

    interface SetOnClickAdapterListener {
        fun onClick(position: Int)
    }

    inner class CommerceViewHolder(val binding: ItemCommerceBinding) : RecyclerView.ViewHolder(binding.root)

    private var _binding: ItemCommerceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommerceViewHolder {
        _binding = ItemCommerceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommerceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommerceViewHolder, position: Int) {
        with (holder) {
            with (commerces[position]) {
                binding.clCommerce.setOnClickListener {
                    onClickAdapterListener.onClick(position)
                }
                if (!this.urlPhoto?.url.isNullOrEmpty()) {
                    Picasso.get().load(this.urlPhoto!!.url).into(binding.ivPhoto)
                }
                binding.tvName.text = this.name!!.replace("-", " ").capitalize()
                binding.tvShotDescription.text = this.shortDescription
                when (this.category) {
                    AppConstants.CATEGORY_SHOPPING -> {
                        binding.clHead.setBackgroundColor(Color.parseColor("#00BCD4"))
                        binding.ivIcon.setImageDrawable(ContextCompat.getDrawable(App.getContext(), R.drawable.icon_payment_regulated_parking_white))
                    }
                    AppConstants.CATEGORY_FOOD -> {
                        binding.clHead.setBackgroundColor(Color.parseColor("#FFC107"))
                        binding.ivIcon.setImageDrawable(ContextCompat.getDrawable(App.getContext(), R.drawable.icon_catering_white))
                    }
                    AppConstants.CATEGORY_BEAUTY -> {
                        binding.clHead.setBackgroundColor(Color.parseColor("#009688"))
                        binding.ivIcon.setImageDrawable(ContextCompat.getDrawable(App.getContext(), R.drawable.icon_car_wash_white))
                    }
                    AppConstants.CATEGORY_LEISURE -> {
                        binding.clHead.setBackgroundColor(Color.parseColor("##673AB7"))
                        binding.ivIcon.setImageDrawable(ContextCompat.getDrawable(App.getContext(), R.drawable.icon_leisure_white))
                    }
                    else -> {
                        binding.clHead.setBackgroundColor(Color.parseColor("#E91E63"))
                        binding.ivIcon.setImageDrawable(ContextCompat.getDrawable(App.getContext(), R.drawable.icon_cart_white))
                    }
                }
                binding.tvDistance.text = "$distance m".replace("f", " ")
            }
        }
    }

    override fun getItemCount(): Int {
        return commerces.size
    }
}