package com.zhari.bitaste.presentation.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.zhari.bitaste.data.local.datastore.UserPreferenceDataSourceImpl
import com.zhari.bitaste.data.local.datastore.appDataStore
import com.zhari.bitaste.data.dummy.CategoryDataSource
import com.zhari.bitaste.data.dummy.CategoryDataSourceImpl
import com.zhari.bitaste.data.dummy.ProductDataSource
import com.zhari.bitaste.data.dummy.ProductDataSourceImpl
import com.zhari.bitaste.data.local.AppDatabase
import com.zhari.bitaste.data.local.datasource.ProductDatabaseDataSource
import com.zhari.bitaste.data.repository.ProductRepository
import com.zhari.bitaste.data.repository.ProductRepositoryImpl
import com.zhari.bitaste.databinding.FragmentHomeBinding
import com.zhari.bitaste.model.Menu
import com.zhari.bitaste.presentation.detailmenu.ActivityDetailMenu
import com.zhari.bitaste.presentation.home.adapter.AdapterLayout
import com.zhari.bitaste.presentation.home.adapter.CategoryAdapter
import com.zhari.bitaste.presentation.main.MainViewModel
import com.zhari.bitaste.utils.GenericViewModelFactory
import com.zhari.bitaste.utils.PreferenceDataStoreHelperImpl
import com.zhari.bitaste.utils.proceedWhen


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val datasourceProduct: ProductDataSource by lazy {
        ProductDataSourceImpl()
    }

    private val adapter: MenuListAdapter by lazy {
        MenuListAdapter(AdapterLayout.LINEAR) { product: Menu ->
            navigateToDetailFragment(product)
        }
    }

    private val adapterCategory: CategoryAdapter by lazy {
        CategoryAdapter {

        }
    }

    private fun navigateToDetailFragment(product: Menu) {
        ActivityDetailMenu.startActivity(requireContext(), product)
    }

    private val viewModel: HomeViewModel by viewModels {
        val cds: CategoryDataSource = CategoryDataSourceImpl()
        val database = AppDatabase.getInstance(requireContext())
        val productDao = database.productDao()
        val productDataSource = ProductDatabaseDataSource(productDao)
        val repo: ProductRepository = ProductRepositoryImpl(productDataSource, cds)
        GenericViewModelFactory.create(HomeViewModel(repo))
    }

    private val mainViewModel: MainViewModel by viewModels {
        val dataStore = this.requireContext().appDataStore
        val dataStoreHelper = PreferenceDataStoreHelperImpl(dataStore)
        val userPreferenceDataSource = UserPreferenceDataSourceImpl(dataStoreHelper)
        GenericViewModelFactory.create(MainViewModel(userPreferenceDataSource))
    }

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
        setupCategoryRecyclerView()
        setupRecyclerView()
        setupSwitch()
        fetchData()
    }


    private fun setObserveData() {
        viewModel.productData.observe(viewLifecycleOwner) {
            it.proceedWhen(
                doOnSuccess = { result ->
                    binding.layoutState.root.isVisible = false
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = false
                    binding.rvMenu.isVisible = true
                    result.payload?.let {
                        adapter.setData(it)
                    }
                },
                doOnLoading = {
                    binding.layoutState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = true
                    binding.layoutState.tvError.isVisible = false
                    binding.rvMenu.isVisible = false
                },
                doOnError = { err ->
                    binding.layoutState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = true
                    binding.layoutState.tvError.text = err.exception?.message.orEmpty()
                    binding.rvMenu.isVisible = false
                }, doOnEmpty = {
                    Log.d("Home Fragment", "Data is empty")
                }
            )
        }
    }

    private fun setupCategoryRecyclerView() {
        binding.rvCategory.adapter = adapterCategory
        adapterCategory.setItems(CategoryDataSourceImpl().getProductCategory())
    }

    private fun setupRecyclerView() {
        val span = if (adapter.adapterLayoutMode == AdapterLayout.LINEAR) 1 else 2
        binding.rvMenu.apply {
            layoutManager = GridLayoutManager(requireContext(), span)
            adapter = this@HomeFragment.adapter
        }
        setObserveData()
    }


    private fun setupSwitch() {
        mainViewModel.userGridModeLiveData.observe(viewLifecycleOwner) { isUsingGridMode ->
            val layoutManager = binding.rvMenu.layoutManager as GridLayoutManager
            binding.listSwitch.setOnClickListener {
                val newSpanCount = if (layoutManager.spanCount == 1) 2 else 1
                layoutManager.spanCount = newSpanCount

                val adapterLayout =
                    if (newSpanCount == 2) AdapterLayout.GRID else AdapterLayout.LINEAR

                mainViewModel.setGridModePref(isUsingGridMode)

                adapter.adapterLayoutMode = adapterLayout
                adapter.refreshList()
            }
        }
    }

    private fun fetchData() {
        viewModel.productData.observe(viewLifecycleOwner) {
            adapter.setData(datasourceProduct.getProductList())
        }
    }
}
