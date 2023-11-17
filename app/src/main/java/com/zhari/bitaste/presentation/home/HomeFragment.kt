package com.zhari.bitaste.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zhari.bitaste.R
import com.zhari.bitaste.databinding.FragmentHomeBinding
import com.zhari.bitaste.model.product.Menu
import com.zhari.bitaste.presentation.detailmenu.MenuDetailActivity
import com.zhari.bitaste.presentation.home.adapter.AdapterLayout
import com.zhari.bitaste.presentation.home.adapter.CategoryAdapter
import com.zhari.bitaste.presentation.main.MainViewModel
import com.zhari.bitaste.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val homeViewModel: HomeViewModel by viewModel()

    private val mainViewModel: MainViewModel by viewModel()

    private val menuAdapter: MenuListAdapter by lazy {
        MenuListAdapter(AdapterLayout.LINEAR) {
                menu: Menu ->
            navigateToDetail(menu)
        }
    }

    private val categoryAdapter: CategoryAdapter by lazy {
        CategoryAdapter {
            homeViewModel.getMenus(it.catName.toLowerCase())
        }
    }

    private fun navigateToDetail(menu: Menu) {
        MenuDetailActivity.startActivity(requireContext(), menu)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerViewCategory()
        setRecyclerViewMenu()
        setupSwitch()
        getData()
        setUsername()
    }

    private fun setUsername() {
        val fullName = homeViewModel.getCurrentUser()?.fullName
        val split = fullName?.split(" ")
        val firstName = split?.get(0)
        binding.tvName.setText("Halo, $firstName!")
    }

    private fun getData() {
        /*homeViewModel.getMenus()*/
        homeViewModel.categories
        homeViewModel.getMenus()
    }

    private fun setObserveDataMenu() {
        homeViewModel.menuList.observe(viewLifecycleOwner) {
            it.proceedWhen(
                doOnSuccess = { result ->
                    binding.clSubheader.isVisible = true
                    binding.layoutStateMenu.root.isVisible = false
                    binding.layoutStateMenu.pbLoading.isVisible = false
                    binding.layoutStateMenu.tvError.isVisible = false
                    binding.rvMenu.isVisible = true
                    result.payload?.let {
                        menuAdapter.setData(it)
                    }
                },
                doOnLoading = {
                    binding.clSubheader.isVisible = false
                    binding.layoutStateMenu.root.isVisible = true
                    binding.layoutStateMenu.pbLoading.isVisible = true
                    binding.layoutStateMenu.tvError.isVisible = false
                    binding.rvMenu.isVisible = false
                },
                doOnError = { err ->
                    binding.layoutStateMenu.root.isVisible = true
                    binding.layoutStateMenu.pbLoading.isVisible = false
                    binding.layoutStateMenu.tvError.isVisible = true
                    binding.layoutStateMenu.tvError.text = err.exception?.message.orEmpty()
                    binding.rvMenu.isVisible = false
                },
                doOnEmpty = {
                    binding.layoutStateMenu.root.isVisible = true
                    binding.layoutStateMenu.pbLoading.isVisible = false
                    binding.layoutStateMenu.tvError.isVisible = true
                    binding.layoutStateMenu.tvError.text = getString(R.string.no_data_text)
                    binding.rvMenu.isVisible = false
                }
            )
        }
    }

    private fun setRecyclerViewMenu() {
        val span = if (menuAdapter.adapterLayoutMode == AdapterLayout.LINEAR) 1 else 2
        binding.rvMenu.apply {
            layoutManager = GridLayoutManager(requireContext(), span)
            adapter = this@HomeFragment.menuAdapter
        }
        setObserveDataMenu()
    }

    private fun setupSwitch() {
        mainViewModel.userGridModeLiveData.observe(viewLifecycleOwner) {
            binding.listSwitch.isChecked = it
        }

        binding.listSwitch.setOnCheckedChangeListener { _, isUsingLinear ->
            mainViewModel.setGridModePref(isUsingLinear)
            (binding.rvMenu.layoutManager as GridLayoutManager).spanCount = if (isUsingLinear) 2 else 1
            menuAdapter.adapterLayoutMode = if (isUsingLinear) AdapterLayout.GRID else AdapterLayout.LINEAR
            setObserveDataMenu()
        }
    }

    private fun setRecyclerViewCategory() {
        binding.rvCategory.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = categoryAdapter
        }
        setObserveDataCategory()
    }

    private fun setObserveDataCategory() {
        homeViewModel.categories.observe(viewLifecycleOwner) {
            it.proceedWhen(
                doOnSuccess = { result ->
                    binding.clSubheader.isVisible = true
                    binding.layoutStateCategory.root.isVisible = false
                    binding.layoutStateCategory.pbLoading.isVisible = false
                    binding.layoutStateCategory.tvError.isVisible = false
                    binding.rvCategory.isVisible = true
                    result.payload?.let {
                        categoryAdapter.setItems(it)
                    }
                },
                doOnLoading = {
                    binding.clSubheader.isVisible = false
                    binding.layoutStateCategory.root.isVisible = true
                    binding.layoutStateCategory.pbLoading.isVisible = false
                    binding.layoutStateCategory.tvError.isVisible = false
                    binding.rvCategory.isVisible = false
                },
                doOnError = { err ->
                    binding.layoutStateCategory.root.isVisible = true
                    binding.layoutStateCategory.pbLoading.isVisible = false
                    binding.layoutStateCategory.tvError.isVisible = true
                    binding.layoutStateCategory.tvError.text = err.exception?.message.orEmpty()
                    binding.rvCategory.isVisible = false
                },
                doOnEmpty = {
                    binding.layoutStateCategory.root.isVisible = true
                    binding.layoutStateCategory.pbLoading.isVisible = false
                    binding.layoutStateCategory.tvError.isVisible = true
                    binding.layoutStateCategory.tvError.text = getString(R.string.no_data_text)
                    binding.rvCategory.isVisible = false
                }
            )
        }
    }
}
