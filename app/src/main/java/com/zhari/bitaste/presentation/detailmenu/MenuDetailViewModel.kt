package com.zhari.bitaste.presentation.detailmenu

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhari.bitaste.data.repository.CartRepository
import com.zhari.bitaste.model.product.Menu
import com.zhari.bitaste.utils.ResultWrapper
import kotlinx.coroutines.launch

class MenuDetailViewModel(
    private val extras: Bundle?,
    private val cartRepository: CartRepository
) : ViewModel(){

    val menu = extras?.getParcelable<Menu>(MenuDetailActivity.EXTRA_PRODUCT)

    val priceLiveData = MutableLiveData<Double>().apply {
        postValue(0.0)
    }
    val menuCountLiveData = MutableLiveData<Int>().apply {
        postValue(0)
    }
    private val _addToCartResult = MutableLiveData<ResultWrapper<Boolean>>()
    val addToCartResult: LiveData<ResultWrapper<Boolean>>
        get() = _addToCartResult

    fun add() {
        val count = (menuCountLiveData.value ?: 0) + 1
        menuCountLiveData.postValue(count)
        priceLiveData.postValue((menu?.price?.times(count) ?: 0.0).toDouble())
    }

    fun minus() {
        if((menuCountLiveData.value ?: 0) > 0){
            val count = (menuCountLiveData.value ?: 0) -1
            menuCountLiveData.postValue(count)
            priceLiveData.postValue((menu?.price?.times(count) ?: 0.0).toDouble())
        }
    }

    fun addToCart() {
        viewModelScope.launch {
            val qty =
                if ((menuCountLiveData.value ?: 0) <= 0) 1 else menuCountLiveData.value ?: 0
            menu?.let {
                cartRepository.createCart(it, qty).collect { result ->
                    _addToCartResult.postValue(result)
                }
            }
        }
    }
}