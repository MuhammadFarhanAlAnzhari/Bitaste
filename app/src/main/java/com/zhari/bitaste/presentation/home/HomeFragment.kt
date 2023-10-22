package com.zhari.bitaste.presentation.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.firebase.auth.FirebaseAuth
import com.zhari.bitaste.R
import com.zhari.bitaste.data.local.datastore.UserPreferenceDataSourceImpl
import com.zhari.bitaste.data.local.datastore.appDataStore
import com.zhari.bitaste.data.network.api.datasource.BitasteDataSourceImpl
import com.zhari.bitaste.data.network.api.service.RestaurantService
import com.zhari.bitaste.data.network.firebase.auth.FirebaseAuthDataSourceImpl
import com.zhari.bitaste.data.repository.MenuRepository
import com.zhari.bitaste.data.repository.MenuRepositoryImpl
import com.zhari.bitaste.data.repository.UserRepository
import com.zhari.bitaste.data.repository.UserRepositoryImpl
import com.zhari.bitaste.databinding.FragmentHomeBinding
import com.zhari.bitaste.model.product.Menu
import com.zhari.bitaste.presentation.detailmenu.MenuDetailActivity
import com.zhari.bitaste.presentation.home.adapter.AdapterLayout
import com.zhari.bitaste.presentation.home.adapter.CategoryAdapter
import com.zhari.bitaste.presentation.main.MainViewModel
import com.zhari.bitaste.utils.GenericViewModelFactory
import com.zhari.bitaste.utils.PreferenceDataStoreHelperImpl
import com.zhari.bitaste.utils.proceedWhen

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val menuAdapter: MenuListAdapter by lazy {
        MenuListAdapter(AdapterLayout.LINEAR){
                menu: Menu -> navigateToDetail(menu)
        }
    }

    private val categoryAdapter: CategoryAdapter by lazy {
        CategoryAdapter{
            homeViewModel.getMenus(it.slug)
        }
    }

    private val viewModel: MainViewModel by viewModels{
        val dataStore =  this.requireContext().appDataStore
        val dataStoreHelper = PreferenceDataStoreHelperImpl(dataStore)
        val userPreferenceDataSource = UserPreferenceDataSourceImpl(dataStoreHelper)
        GenericViewModelFactory.create(MainViewModel(userPreferenceDataSource))
    }


    private val homeViewModel: HomeViewModel by viewModels {
        val chucker = ChuckerInterceptor(requireContext().applicationContext)
        val service = RestaurantService.invoke(chucker)
        val firebaseAuth = FirebaseAuth.getInstance()
        val menuDataSource = BitasteDataSourceImpl(service)
        val userDataSource = FirebaseAuthDataSourceImpl(firebaseAuth)
        val menuRepo: MenuRepository = MenuRepositoryImpl(menuDataSource)
        val userRepo: UserRepository = UserRepositoryImpl(userDataSource)
        GenericViewModelFactory.create(HomeViewModel(menuRepo,userRepo))
    }

    private fun navigateToDetail(menu: Menu) {
        MenuDetailActivity.startActivity(requireContext(), menu)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
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
        binding.tvName.setText("Halo, ${firstName}!")
    }

    private fun getData() {
        /*homeViewModel.getMenus()*/
        homeViewModel.getCategories()
        homeViewModel.getMenuList()
    }

    private fun setObserveDataMenu() {
        homeViewModel.menuList.observe(viewLifecycleOwner){
            it.proceedWhen(
                doOnSuccess = { result ->
                    binding.clSubheader.isVisible = true
                    binding.layoutState.root.isVisible = false
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = false
                    binding.rvMenu.isVisible = true
                    result.payload?.let {
                        menuAdapter.setData(it)
                    }
                },
                doOnLoading = {
                    binding.clSubheader.isVisible = false
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
                    binding.layoutState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = true
                    binding.layoutState.tvError.text = getString(R.string.no_data_text)
                    binding.rvMenu.isVisible = false
                }
            )
        }
    }

    private fun setRecyclerViewMenu() {
        val span = if(menuAdapter.adapterLayoutMode == AdapterLayout.LINEAR) 1 else 2
        binding.rvMenu.apply {
            layoutManager = GridLayoutManager(requireContext(),span)
            adapter = this@HomeFragment.menuAdapter
        }
        setObserveDataMenu()
    }

    private fun setupSwitch() {
        viewModel.userGridModeLiveData.observe(viewLifecycleOwner){
            binding.listSwitch.isChecked = it
        }

        binding.listSwitch.setOnCheckedChangeListener { _, isUsingLinear ->
            viewModel.setGridModePref(isUsingLinear)
            (binding.rvMenu.layoutManager as GridLayoutManager).spanCount = if (isUsingLinear) 2 else 1
            menuAdapter.adapterLayoutMode = if(isUsingLinear) AdapterLayout.GRID else AdapterLayout.LINEAR
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
        homeViewModel.categories.observe(viewLifecycleOwner){
            it.proceedWhen(
                doOnSuccess = { result ->
                    binding.clSubheader.isVisible = true
                    binding.layoutState.root.isVisible = false
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = false
                    binding.rvCategory.isVisible = true
                    result.payload?.let {
                        categoryAdapter.setItems(it)
                    }
                },
                doOnLoading = {
                    binding.clSubheader.isVisible = false
                    binding.layoutState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = false
                    binding.rvCategory.isVisible = false
                },
                doOnError = { err ->
                    binding.layoutState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = true
                    binding.layoutState.tvError.text = err.exception?.message.orEmpty()
                    binding.rvCategory.isVisible = false
                }, doOnEmpty = {
                    binding.layoutState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = true
                    binding.layoutState.tvError.text = getString(R.string.no_data_text)
                    binding.rvCategory.isVisible = false
                }
            )
        }
    }
}