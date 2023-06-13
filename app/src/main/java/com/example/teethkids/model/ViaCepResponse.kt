package com.example.teethkids.model

data class ViaCepResponse(
    val cep: String,
    val logradouro: String,
    val bairro: String,
    val localidade: String,
    val uf: String,
)