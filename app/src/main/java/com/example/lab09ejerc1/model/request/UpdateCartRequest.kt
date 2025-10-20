package com.example.lab09ejerc1.model.request

// Request para actualizar un carrito existente
import com.example.lab09ejerc1.model.request.CartProductRequest

data class UpdateCartRequest(
    val merge: Boolean = true,
    val products: List<CartProductRequest>
)
