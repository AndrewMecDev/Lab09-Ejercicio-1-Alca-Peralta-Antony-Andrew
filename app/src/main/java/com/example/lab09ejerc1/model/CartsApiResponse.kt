package com.example.lab09ejerc1.model

// Representa la respuesta completa de la API, que contiene una lista de carritos.
data class CartsApiResponse(
    val carts: List<Cart>, // La lista principal de carritos
    val total: Int,
    val skip: Int,
    val limit: Int
)
