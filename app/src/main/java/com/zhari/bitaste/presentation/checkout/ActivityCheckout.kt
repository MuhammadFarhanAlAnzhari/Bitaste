package com.zhari.bitaste.presentation.checkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.firebase.auth.FirebaseAuth
import com.zhari.bitaste.R
import com.zhari.bitaste.data.local.database.AppDatabase
import com.zhari.bitaste.data.local.datasource.CartDataSource
import com.zhari.bitaste.data.local.datasource.CartDatabaseDataSource
import com.zhari.bitaste.data.network.api.datasource.BitasteDataSourceImpl
import com.zhari.bitaste.data.network.api.service.RestaurantService
import com.zhari.bitaste.data.network.firebase.auth.FirebaseAuthDataSourceImpl
import com.zhari.bitaste.data.repository.CartRepository
import com.zhari.bitaste.data.repository.CartRepositoryImpl
import com.zhari.bitaste.data.repository.UserRepository
import com.zhari.bitaste.data.repository.UserRepositoryImpl
import com.zhari.bitaste.databinding.ActivityCheckoutBinding
import com.zhari.bitaste.presentation.cart.CartListAdapter
import com.zhari.bitaste.utils.GenericViewModelFactory
import com.zhari.bitaste.utils.proceedWhen

class ActivityCheckout : AppCompatActivity() {

    private val viewModel: CheckoutViewModel by viewModels {
        val database = AppDatabase.getInstance(this)
        val firebaseAuth = FirebaseAuth.getInstance()
        val cartDao = database.cartDao()
        val chucker = ChuckerInterceptor(this)
        val service = RestaurantService.invoke(chucker)
        val orderDataSource = BitasteDataSourceImpl(service)
        val cartDataSource: CartDataSource = CartDatabaseDataSource(cartDao)
        val cartRepo: CartRepository = CartRepositoryImpl(cartDataSource, orderDataSource)
        val userDataSource = FirebaseAuthDataSourceImpl(firebaseAuth)
        val userRepo: UserRepository = UserRepositoryImpl(userDataSource)
        GenericViewModelFactory.create(CheckoutViewModel(
            cartRepo, userRepo
        ))
    }

    private val binding: ActivityCheckoutBinding by lazy {
        ActivityCheckoutBinding.inflate(
            layoutInflater
        )
    }

    private val adapter: CartListAdapter by lazy {
        CartListAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupList()
        observeData()
        backToHomeClickListener()
        orderClickListener()
        observeOrderResult()
    }

    private fun observeOrderResult() {
        viewModel.orderResult.observe(this){
            it.proceedWhen (
                doOnSuccess = {
                    dialogOrder()
                    deleteAllCart()
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = false
                },
                doOnError = {
                    Toast.makeText(
                        this,
                        "Pesanan gagal dibuat",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.layoutState.pbLoading.isVisible = true
                    binding.layoutState.tvError.isVisible = false
                },
                doOnLoading = {
                    binding.layoutState.pbLoading.isVisible = true
                    binding.layoutState.tvError.isVisible = false
                    binding.layoutState.root.isVisible = true
                    binding.svCheckoutController.isVisible = false
                }
            )
        }
    }

    private fun dialogOrder() {
        AlertDialog.Builder(this)
            .setMessage("Pesanan berhasil dibuat")
            .setPositiveButton("Ok") { _, _ -> finish() }
            .show()
    }

    private fun orderClickListener() {
        binding.btnOrder.setOnClickListener {
            viewModel.createOrder()
        }
    }

    private fun deleteAllCart() {
        viewModel.deleteAll()
    }

    private fun backToHomeClickListener() {
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setupList() {
        binding.rvCheckout.adapter = adapter
    }

    private fun observeData() {
        viewModel.cartList.observe(this) {
            it.proceedWhen(
                doOnSuccess = { result ->
                    binding.layoutState.root.isVisible =
                        false
                    binding.layoutState.pbLoading.isVisible =
                        false
                    binding.layoutState.tvError.isVisible =
                        false
                    binding.svCheckoutController.isVisible =
                        true
                    result.payload?.let { (carts, totalPrice) ->
                        adapter.submitData(carts)
                        binding.totalPembayaran.text =
                            String.format(
                                "Rp. %,.0f",
                                totalPrice
                            )
                    }
                },
                doOnLoading = {
                    binding.layoutState.root.isVisible =
                        true
                    binding.layoutState.pbLoading.isVisible =
                        true
                    binding.layoutState.tvError.isVisible =
                        false
                    binding.svCheckoutController.isVisible =
                        false
                },
                doOnEmpty = { data ->
                    binding.layoutState.root.isVisible =
                        true
                    binding.layoutState.pbLoading.isVisible =
                        false
                    binding.layoutState.tvError.isVisible =
                        true
                    binding.layoutState.tvError.text =
                        getString(R.string.no_data_text)
                    binding.btnOrder.isClickable =
                        false
                },
                doOnError = { err ->
                    binding.layoutState.root.isVisible =
                        true
                    binding.layoutState.pbLoading.isVisible =
                        false
                    binding.layoutState.tvError.isVisible =
                        true
                    binding.layoutState.tvError.text =
                        err.exception?.message.orEmpty()
                    binding.svCheckoutController.isVisible =
                        false
                    binding.btnOrder.isClickable =
                        false
                }
            )
        }
    }
}