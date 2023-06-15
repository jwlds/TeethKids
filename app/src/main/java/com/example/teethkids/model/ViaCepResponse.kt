package com.example.teethkids.model


// Classe representando os dados retornados pela API ViaCep
data class ViaCepResponse(
    val cep: String,
    val logradouro: String,
    val bairro: String,
    val localidade: String,
    val uf: String,
)