package com.digitalsoch.network


import com.digitalsoch.network.AdminApi

class AdminRepository(private val adminApi: AdminApi) : ApiRequest() {

    /*suspend fun adminLogin(hashMap: HashMap<String, Any>): LoginResponse {
        return apiRequest { adminApi.adminLogin(hashMap) }
    }*/


}