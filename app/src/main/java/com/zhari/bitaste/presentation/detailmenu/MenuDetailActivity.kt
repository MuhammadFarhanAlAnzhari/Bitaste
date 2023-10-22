package com.zhari.bitaste.presentation.detailmenu

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.zhari.bitaste.R
import com.zhari.bitaste.data.local.AppDatabase
import com.zhari.bitaste.data.local.datasource.CartDataSource
import com.zhari.bitaste.data.local.datasource.CartDatabaseDataSource
import com.zhari.bitaste.data.network.api.datasource.BitasteDataSourceImpl
import com.zhari.bitaste.data.network.api.service.RestaurantService
import com.zhari.bitaste.data.repository.CartRepository
import com.zhari.bitaste.data.repository.CartRepositoryImpl
import com.zhari.bitaste.databinding.ActivityDetailMenuBinding
import com.zhari.bitaste.model.product.Menu
import com.zhari.bitaste.utils.GenericViewModelFactory
import com.zhari.bitaste.utils.proceedWhen

class MenuDetailActivity : AppCompatActivity() {

    private val binding: ActivityDetailMenuBinding by lazy {
        ActivityDetailMenuBinding.inflate(
            layoutInflater
        )
    }

    private val viewModel: MenuDetailViewModel by viewModels {
        val database = AppDatabase.getInstance(this)
        val cartDao = database.cartDao()
        val chucker = ChuckerInterceptor(this)
        val service = RestaurantService.invoke(chucker)
        val orderDataSource = BitasteDataSourceImpl(service)
        val cartDataSource: CartDataSource = CartDatabaseDataSource(cartDao)
        val cartRepo: CartRepository = CartRepositoryImpl(cartDataSource, orderDataSource)
        GenericViewModelFactory.create(MenuDetailViewModel(intent?.extras, cartRepo))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        backToHomeClickListener()
        countingClickListener()
        observeData()
        mapsClickListener()
        viewModel.menu?.let { showMenuData(it) }
    }

    private fun observeData() {
        viewModel.priceLiveData.observe(this){
            binding.btnAddToCart.text =  getString(R.string.add_to_cart, it.toInt())
        }

        viewModel.menuCountLiveData.observe(this){
            binding.tvMenuCount.text = it.toString()
        }

        viewModel.addToCartResult.observe(this) {
            it.proceedWhen(
                doOnSuccess = {
                    Toast.makeText(this, "Added to basket", Toast.LENGTH_SHORT).show()
                    finish()
                }, doOnError = {
                    Toast.makeText(this, it.exception?.message.orEmpty(), Toast.LENGTH_SHORT).show()
                })
        }
    }

    private fun backToHomeClickListener() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun countingClickListener() {
        binding.ivPlus.setOnClickListener {
            viewModel.add()
        }
        binding.ivMinus.setOnClickListener {
            viewModel.minus()
        }
        binding.btnAddToCart.setOnClickListener{
            viewModel.addToCart()
        }
    }

    private fun showMenuData(menu: Menu) {
        menu?.let {
            binding.banner.load(it.imageUrl)
            binding.tvFoodNameDetail.text = it.name
            binding.tvPriceDetail.text = String.format("Rp. %,.0f", it.price?.toDouble())
            binding.tvDescription.text = it.detail
            binding.tvLocation.text = it.restaurantAddress
            binding.btnAddToCart.text = getString(R.string.add_to_cart, it.price?.toInt())
        }
    }

    private fun mapsClickListener() {
        binding.cvLocation.setOnClickListener {
            navigateToGoogleMaps()
        }
    }

    private fun navigateToGoogleMaps() {
        val mapsIntentUri =
            Uri.parse("https://maps.app.goo.gl/QLChXJcYJUuQWPQh8")
        val mapsIntent = Intent(
            Intent.ACTION_VIEW,
            mapsIntentUri
        )
        startActivity(mapsIntent)
    }

    companion object {
        const val EXTRA_PRODUCT = "EXTRA_PRODUCT"
        fun startActivity(context: Context, menu: Menu) {
            val intent = Intent(context, MenuDetailActivity::class.java)
            intent.putExtra(EXTRA_PRODUCT, menu)
            context.startActivity(intent)
        }
    }
}