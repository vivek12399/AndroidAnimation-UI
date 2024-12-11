package com.digitalsoch.network

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AdminViewModel(private var adminRepository: AdminRepository) :ViewModel(){
    /*suspend fun adminLogin(hashMap : HashMap<String,Any>) = withContext(Dispatchers.IO) {
        adminRepository.adminLogin(hashMap)
    }*/

}