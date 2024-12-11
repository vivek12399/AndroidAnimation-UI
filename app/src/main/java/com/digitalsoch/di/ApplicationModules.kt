package com.digitalsoch.di


import com.digitalsoch.network.AdminApi
import com.digitalsoch.network.AdminRepository
import com.digitalsoch.network.AdminViewModel
import com.digitalsoch.network.AdminViewModelFactory
import com.digitalsoch.network.PreferenceProvider
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val applicationModule = module {
    single { AdminApi(get()) }
    single { PreferenceProvider(get()) }
    single { AdminRepository(get()) }
}

val viewModel = module {
    viewModel{ AdminViewModel(get()) }
}

val viewModelFactory = module {
    factory { AdminViewModelFactory(get()) }
}