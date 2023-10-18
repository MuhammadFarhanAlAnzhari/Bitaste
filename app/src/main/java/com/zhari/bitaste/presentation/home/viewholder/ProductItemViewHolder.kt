package com.zhari.bitaste.presentation.home.viewholder

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.zhari.bitaste.core.ViewHolderBinder
import com.zhari.bitaste.databinding.ItemMenuGridBinding
import com.zhari.bitaste.databinding.ItemMenuListBinding
import com.zhari.bitaste.model.Menu

class ProductItemViewHolder {
}

class ProductLinearViewHolder(
    private val binding: ItemMenuListBinding,
    private val onClickListener: (Menu) -> Unit
) : RecyclerView.ViewHolder(binding.root), ViewHolderBinder<Menu> {
    override fun bind(item: Menu) {
        binding.ivMenuImg.load(item.menuImgUrl) {
            crossfade(true)
        }
        binding.tvMenuName.text = item.name
        binding.tvMenuPrice.text = item.price.toString()
        binding.root.setOnClickListener {
            onClickListener.invoke(item)
        }
    }
}

class ProductGridViewHolder(
    private val binding: ItemMenuGridBinding,
    private val onClickListener: (Menu) -> Unit
) : RecyclerView.ViewHolder(binding.root), ViewHolderBinder<Menu> {
    override fun bind(item: Menu) {
        binding.ivMenu.load(item.menuImgUrl) {
            crossfade(true)
        }
        binding.tvNamaMakanan.text = item.name
        binding.tvPriceFood.text = item.price.toString()
        binding.root.setOnClickListener {
            onClickListener.invoke(item)
        }
    }
}