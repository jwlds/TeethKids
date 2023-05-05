package com.example.teethkids.service

import com.example.teethkids.model.Address
import com.example.teethkids.model.AddressViaCep
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface AddressService {
    @GET("{cep}/json")
    fun getAddress(@Path("cep") cep: String): Call<AddressViaCep>
}