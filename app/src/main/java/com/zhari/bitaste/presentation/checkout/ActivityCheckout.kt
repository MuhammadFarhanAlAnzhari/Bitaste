package com.zhari.bitaste.presentation.checkout

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.zhari.bitaste.R
import com.zhari.bitaste.databinding.ActivityCheckoutBinding
import com.zhari.bitaste.presentation.cart.CartListAdapter
import com.zhari.bitaste.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel

class ActivityCheckout : AppCompatActivity() {
    private val viewModel: CheckoutViewModel by viewModel()

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
        viewModel.orderResult.observe(this) {
            it.proceedWhen(doOnSuccess = {
                binding.layoutState.root.isVisible = false
                binding.layoutState.pbLoading.isVisible = false
                dialogOrder()
            }, doOnError = {
                    binding.layoutState.root.isVisible = false
                    binding.layoutState.pbLoading.isVisible = false
                    Toast.makeText(this, "Checkout Error", Toast.LENGTH_SHORT).show()
                }, doOnLoading = {
                    binding.layoutState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = true
                })
        }
    }

    private fun dialogOrder() {
        AlertDialog.Builder(this)
            .setMessage("Pesanan berhasil dibuat")
            .setPositiveButton("Ok") { _, _ ->
                viewModel.deleteAll()
                finish()
            }
            .create().show()
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
