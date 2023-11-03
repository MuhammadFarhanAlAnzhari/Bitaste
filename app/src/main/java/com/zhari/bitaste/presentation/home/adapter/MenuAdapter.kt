package com.zhari.bitaste.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.zhari.bitaste.core.ViewHolderBinder
import com.zhari.bitaste.databinding.ItemMenuGridBinding
import com.zhari.bitaste.databinding.ItemMenuListBinding
import com.zhari.bitaste.model.product.Menu

enum class MenuAdapter {
    GRID,
    LIST
}

class MenuListAdapter(
    private var adapterLayoutMode: MenuAdapter,
    private val onItemClicked: (Menu) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dataDiffer = AsyncListDiffer(
        this,
        object : DiffUtil.ItemCallback<Menu>() {
            override fun areItemsTheSame(oldItem: Menu, newItem: Menu): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Menu, newItem: Menu): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }
        }
    )

    fun submitData(list: List<Menu>) = dataDiffer.submitList(list)

    fun setAdapterLayoutMode(mode: MenuAdapter) {
        adapterLayoutMode = mode
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (adapterLayoutMode) {
            MenuAdapter.LIST -> ListMenuItemViewHolder(
                ItemMenuListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                onItemClicked
            )
            MenuAdapter.GRID -> GridMenuItemViewHolder(
                ItemMenuGridBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                onItemClicked
            )
        }
    }

    override fun getItemCount(): Int = dataDiffer.currentList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = dataDiffer.currentList[position]
        when (holder) {
            is GridMenuItemViewHolder -> holder.bind(item)
            is ListMenuItemViewHolder -> holder.bind(item)
        }
    }
}

class GridMenuItemViewHolder(
    private val binding: ItemMenuGridBinding,
    private val onItemClicked: (Menu) -> Unit
) : RecyclerView.ViewHolder(binding.root), ViewHolderBinder<Menu> {

    override fun bind(item: Menu) {
        binding.root.setOnClickListener {
            onItemClicked.invoke(item)
        }
        binding.ivMenu.load(item.imageUrl)
        binding.tvNamaMakanan.text = item.name
    }
}

class ListMenuItemViewHolder(
    private val binding: ItemMenuListBinding,
    private val onItemClicked: (Menu) -> Unit
) : RecyclerView.ViewHolder(binding.root), ViewHolderBinder<Menu> {

    override fun bind(item: Menu) {
        binding.root.setOnClickListener {
            onItemClicked.invoke(item)
        }
        binding.ivMenuImg.load(item.imageUrl)
        binding.tvMenuName.text = item.name
    }
}
