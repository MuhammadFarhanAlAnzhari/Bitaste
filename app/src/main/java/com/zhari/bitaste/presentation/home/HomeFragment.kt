package com.zhari.bitaste.presentation.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.zhari.bitaste.data.datasource.ProductDataSource
import com.zhari.bitaste.data.datasource.ProductDataSourceImpl
import com.zhari.bitaste.databinding.FragmentHomeBinding
import com.zhari.bitaste.model.Menu

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupList()
        setupSwitch()
    }
    private fun setupList() {
        binding.rvMenu.layoutManager = GridLayoutManager(requireContext(), if (adapter.adapterLayoutMode == AdapterLayoutMode.LIST) 1 else 2)
        binding.rvMenu.adapter = adapter
        adapter.submitData(dataSource.getProductList())
    }

    private fun setupSwitch() {
        binding.listSwitch.setOnCheckedChangeListener { _, isChecked ->
            val layoutManager = binding.rvMenu.layoutManager as? GridLayoutManager
            layoutManager?.spanCount = if (isChecked) 2 else 1
            adapter.adapterLayoutMode = if (isChecked) AdapterLayoutMode.GRID else AdapterLayoutMode.LIST
            adapter.refreshList()
        }
    }

    private val dataSource: ProductDataSource by lazy {
        ProductDataSourceImpl()
    }

    private val adapter: MenuListAdapter by lazy {
        MenuListAdapter(AdapterLayoutMode.LIST) { menu: Menu ->
            navigateToDetail(menu)
        }
    }

    private fun navigateToDetail(menu: Menu) {
        val intent = Intent(requireActivity(), HomeFragment::class.java)
        intent.putExtra("menu", menu)
        startActivity(intent)
    }
}
