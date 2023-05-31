package com.example.teethkids.service

import com.example.teethkids.model.ViaCepResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ViaCepService {
    @GET("{cep}/json")
    fun getAddress(@Path("cep") cep: String): Call<ViaCepResponse>
}