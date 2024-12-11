package com.digitalsoch.network

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class AdminViewModelFactory(private val adminRepository: AdminRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AdminViewModel(adminRepository) as T
    }
}