package com.example.teethkids.repository

import com.example.teethkids.model.ViaCepResponse
import com.example.teethkids.service.APIRetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

 class ViaCepRepository {

     private lateinit var service: APIRetrofitService

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://viacep.com.br/ws/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

     fun getAddressByCep(cep: String, onResponse: (ViaCepResponse?) -> Unit, onFailure: (Throwable) -> Unit) {
        service = retrofit.create(APIRetrofitService::class.java)
        val call = service.getAddress(cep)
        call.enqueue(object : Callback<ViaCepResponse> {
            override fun onResponse(call: Call<ViaCepResponse>, response: Response<ViaCepResponse>) {
                if (response.isSuccessful) {
                    val address = response.body()
                    onResponse(address)
                } else {
                    onFailure(Throwable("Failed to get address"))
                }
            }

            override fun onFailure(call: Call<ViaCepResponse>, t: Throwable) {
                onFailure(t)
            }
        })
    }
}
