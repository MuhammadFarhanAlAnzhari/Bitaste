package com.zhari.bitaste.di

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.firebase.auth.FirebaseAuth
import com.zhari.bitaste.data.local.database.AppDatabase
import com.zhari.bitaste.data.local.datasource.CartDataSource
import com.zhari.bitaste.data.local.datasource.CartDatabaseDataSource
import com.zhari.bitaste.data.local.datastore.UserPreferenceDataSource
import com.zhari.bitaste.data.local.datastore.UserPreferenceDataSourceImpl
import com.zhari.bitaste.data.local.datastore.appDataStore
import com.zhari.bitaste.data.network.api.datasource.BitasteDataSource
import com.zhari.bitaste.data.network.api.datasource.BitasteDataSourceImpl
import com.zhari.bitaste.data.network.api.service.RestaurantService
import com.zhari.bitaste.data.network.firebase.auth.FirebaseAuthDataSource
import com.zhari.bitaste.data.network.firebase.auth.FirebaseAuthDataSourceImpl
import com.zhari.bitaste.data.repository.CartRepository
import com.zhari.bitaste.data.repository.CartRepositoryImpl
import com.zhari.bitaste.data.repository.MenuRepository
import com.zhari.bitaste.data.repository.MenuRepositoryImpl
import com.zhari.bitaste.data.repository.UserRepository
import com.zhari.bitaste.data.repository.UserRepositoryImpl
import com.zhari.bitaste.presentation.cart.CartViewModel
import com.zhari.bitaste.presentation.checkout.CheckoutViewModel
import com.zhari.bitaste.presentation.detailmenu.MenuDetailViewModel
import com.zhari.bitaste.presentation.home.HomeViewModel
import com.zhari.bitaste.presentation.login.LoginViewModel
import com.zhari.bitaste.presentation.main.MainViewModel
import com.zhari.bitaste.presentation.profile.ProfileViewModel
import com.zhari.bitaste.presentation.register.RegisterViewModel
import com.zhari.bitaste.presentation.splashscreen.SplashViewModel
import com.zhari.bitaste.utils.AssetWrapper
import com.zhari.bitaste.utils.PreferenceDataStoreHelper
import com.zhari.bitaste.utils.PreferenceDataStoreHelperImpl
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.dsl.module

object AppModules {

    private val localModule = module {
        single { AppDatabase.getInstance(androidContext()) }
        single { get<AppDatabase>().cartDao() }
        single { androidContext().appDataStore }
        single<PreferenceDataStoreHelper> { PreferenceDataStoreHelperImpl(get()) }
    }

    private val networkModule = module {
        single { ChuckerInterceptor(androidContext()) }
        single { RestaurantService.invoke(get()) }
        single { FirebaseAuth.getInstance() }
    }

    private val dataSourceModule = module {
        single<CartDataSource> { CartDatabaseDataSource(get()) }
        single<BitasteDataSource> { BitasteDataSourceImpl(get()) }
        single<UserPreferenceDataSource> { UserPreferenceDataSourceImpl(get()) }
        single<FirebaseAuthDataSource> { FirebaseAuthDataSourceImpl(get()) }
    }

    private val repositoryModule = module {
        single<CartRepository> { CartRepositoryImpl(get(), get()) }
        single<MenuRepository> { MenuRepositoryImpl(get()) }
        single<UserRepository> { UserRepositoryImpl(get()) }
    }

    private val utilsModule = module {
        single { AssetWrapper(androidContext()) }
    }

    private val viewModelModule = module {
        viewModelOf(::MainViewModel)
        viewModelOf(::HomeViewModel)
        viewModelOf(::CartViewModel)
        viewModelOf(::SplashViewModel)
        viewModelOf(::LoginViewModel)
        viewModelOf(::RegisterViewModel)
        viewModelOf(::ProfileViewModel)
        viewModelOf(::CheckoutViewModel)
        viewModel { MenuDetailViewModel(get(), get()) }
    }

    val modules: List<Module> = listOf(
        localModule,
        networkModule,
        dataSourceModule,
        repositoryModule,
        viewModelModule,
        utilsModule
    )
}
