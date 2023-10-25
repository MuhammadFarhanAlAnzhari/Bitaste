package com.zhari.bitaste.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zhari.bitaste.data.repository.MenuRepository
import com.zhari.bitaste.data.repository.UserRepository
import com.zhari.bitaste.model.category.Category
import com.zhari.bitaste.model.product.Menu
import com.zhari.bitaste.utils.ResultWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(
    private val menuRepo: MenuRepository,
    private val userRepo: UserRepository,
) : ViewModel() {

    private val _categories = MutableLiveData<ResultWrapper<List<Category>>>()
    val categories : LiveData<ResultWrapper<List<Category>>>
        get() = _categories

    private val _menus = MutableLiveData<ResultWrapper<List<Menu>>>()
    val menus : LiveData<ResultWrapper<List<Menu>>>
        get() = _menus

    private val _menuList = MutableLiveData<ResultWrapper<List<Menu>>>()
    val menuList : LiveData<ResultWrapper<List<Menu>>>
        get() = _menuList

    fun getCategories(){
        viewModelScope.launch(Dispatchers.IO) {
            menuRepo.getCategories().collect{
                _categories.postValue(it)
            }
        }
    }

    fun getMenus(category: String? = null){
        viewModelScope.launch(Dispatchers.IO) {
            menuRepo.getMenus(if(category == "all") null else category).collect{
                _menus.postValue(it)
            }
        }
    }

    fun getMenuList(){
        viewModelScope.launch(Dispatchers.IO) {
            menuRepo.getMenuList().collect{
                _menuList.postValue(it)
            }
        }
    }
    fun getCurrentUser() = userRepo.getCurrentUser()
}
