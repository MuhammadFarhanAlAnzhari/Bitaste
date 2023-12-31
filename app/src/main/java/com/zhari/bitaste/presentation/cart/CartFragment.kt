package com.zhari.bitaste.presentation.cart

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zhari.bitaste.R
import com.zhari.bitaste.databinding.FragmentCartBinding
import com.zhari.bitaste.model.Cart
import com.zhari.bitaste.presentation.checkout.ActivityCheckout
import com.zhari.bitaste.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding

    private val cartListAdapter: CartListAdapter by lazy {
        CartListAdapter(object : CartListener {
            override fun onCartClicked(item: Cart) {
            }

            override fun onPlusTotalItemCartClicked(
                cart: Cart
            ) {
                viewModel.increaseCart(cart)
            }

            override fun onMinusTotalItemCartClicked(
                cart: Cart
            ) {
                viewModel.decreaseCart(cart)
            }

            override fun onRemoveCartClicked(cart: Cart) {
                viewModel.deleteCart(cart)
            }

            override fun onUserDoneEditingNotes(
                newCart: Cart
            ) {
                viewModel.updateNotes(newCart)
            }
        })
    }
    private val viewModel: CartViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(
            view,
            savedInstanceState
        )

        setupCartList()
        observeData()
        setClickListener()
    }

    private fun setClickListener() {
        binding.btnCheckoutCart.setOnClickListener {
            context?.startActivity(Intent(requireContext(), ActivityCheckout::class.java))
        }
    }

    private fun observeData() {
        viewModel.cartList.observe(
            viewLifecycleOwner
        ) {
            it.proceedWhen(
                doOnSuccess = { result ->
                    binding.layoutState.root.isVisible =
                        false
                    binding.layoutState.pbLoading.isVisible =
                        false
                    binding.layoutState.tvError.isVisible =
                        false
                    binding.rvMenuCart.isVisible =
                        true
                    binding.clCheckout.isVisible =
                        true
                    result.payload?.let { (carts, totalPrice) ->
                        cartListAdapter.submitData(
                            carts
                        )
                        binding.tvTotalPrice.text =
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
                    binding.rvMenuCart.isVisible =
                        false
                },
                doOnEmpty = { result ->
                    binding.layoutState.root.isVisible =
                        true
                    binding.rvMenuCart.isVisible =
                        false
                    binding.layoutState.pbLoading.isVisible =
                        false
                    binding.layoutState.tvError.isVisible =
                        true
                    binding.layoutState.tvError.text =
                        getString(R.string.no_data_text)
                    result.payload?.let { (_, totalPrice) ->
                        binding.tvTotalPrice.text =
                            String.format(
                                "Rp. %,.0f",
                                totalPrice
                            )
                    }
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
                    binding.rvMenuCart.isVisible =
                        false
                }
            )
        }
    }

    private fun setupCartList() {
//        binding.rvMenuCart.adapter = cartListAdapter
        binding.rvMenuCart.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = cartListAdapter
        }
    }
}
