package com.example.lab09ejerc1.model.request

// Representa un producto (por id y cantidad) para requests de carrito
data class CartProductRequest(
    val id: Int,
    val quantity: Int
)
