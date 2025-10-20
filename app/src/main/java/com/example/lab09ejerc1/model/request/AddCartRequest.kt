package com.example.lab09ejerc1.model.request

// Request para crear un nuevo carrito
import com.example.lab09ejerc1.model.request.CartProductRequest

data class AddCartRequest(
    val userId: Int,
    val products: List<CartProductRequest>
)
