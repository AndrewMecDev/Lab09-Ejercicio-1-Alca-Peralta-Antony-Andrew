package com.example.lab09ejerc1.model

// Representa un único producto dentro de un carrito.
data class Product(
    val id: Int,
    val title: String,
    val price: Double,
    val quantity: Int,
    val total: Double,
    val discountPercentage: Double,
    val discountedPrice: Double,
    val thumbnail: String // URL de la imagen miniatura del producto
)
