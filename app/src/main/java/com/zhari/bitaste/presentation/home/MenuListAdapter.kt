package com.zhari.bitaste.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.zhari.bitaste.core.ViewHolderBinder
import com.zhari.bitaste.databinding.ItemMenuGridBinding
import com.zhari.bitaste.databinding.ItemMenuListBinding
import com.zhari.bitaste.model.product.Menu
import com.zhari.bitaste.presentation.home.adapter.AdapterLayout

enum class AdapterLayoutMode {
    GRID,
    LIST,
    LINEAR
}

class MenuListAdapter(
    var adapterLayoutMode: AdapterLayout,
    private val onItemClicked: (Menu) -> Unit
) :
    RecyclerView.Adapter<ViewHolder>() {

    private val dataDiffer = AsyncListDiffer(this, object : DiffUtil.ItemCallback<Menu>() {
        override fun areItemsTheSame(oldItem: Menu, newItem: Menu): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Menu, newItem: Menu): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    })

    fun submitData(list: List<Menu>) = dataDiffer.submitList(list)

    fun refreshList() {
        notifyItemRangeChanged(0, dataDiffer.currentList.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            AdapterLayoutMode.LIST.ordinal -> LinearPlanetItemViewHolder(
                ItemMenuListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                onItemClicked
            )

            else -> GridPlanetItemViewHolder(
                ItemMenuGridBinding.inflate(
                    LayoutInflater.from(
                        parent.context
                    ), parent, false
                ),
                onItemClicked
            )
        }
    }

    override fun getItemCount(): Int = dataDiffer.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataDiffer.currentList[position]
        when (holder) {
            is GridPlanetItemViewHolder -> holder.bind(item)
            is LinearPlanetItemViewHolder -> holder.bind(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return adapterLayoutMode.ordinal
    }

    fun setData(data: List<Menu>) {
        dataDiffer.submitList(data)
    }
}

class GridPlanetItemViewHolder(
    private val binding: ItemMenuGridBinding,
    private val onItemClicked: (Menu) -> Unit
) : ViewHolder(binding.root), ViewHolderBinder<Menu> {

    override fun bind(item: Menu) {
        binding.root.setOnClickListener {
            onItemClicked.invoke(item)
        }
        binding.ivMenu.load(item.imageUrl)
        binding.tvNamaMakanan.text = item.name
        binding.tvPriceFood.text =item.formattedPrice
    }
}

class LinearPlanetItemViewHolder(
    private val binding: ItemMenuListBinding,
    private val onItemClicked: (Menu) -> Unit
) : ViewHolder(binding.root), ViewHolderBinder<Menu> {

    override fun bind(item: Menu) {
        binding.root.setOnClickListener {
            onItemClicked.invoke(item)
        }
        binding.ivMenuImg.load(item.imageUrl)
        binding.tvMenuName.text = item.name
        binding.tvMenuPrice.text = item.formattedPrice
    }
}