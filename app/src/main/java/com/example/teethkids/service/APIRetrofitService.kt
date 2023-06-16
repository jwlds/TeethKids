package com.example.teethkids.service

import com.example.teethkids.model.ViaCepResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


// Contrato para a chamada à API do ViaCEP.
interface APIRetrofitService {
    @GET("{cep}/json")
    fun getAddress(@Path("cep") cep: String): Call<ViaCepResponse>
}