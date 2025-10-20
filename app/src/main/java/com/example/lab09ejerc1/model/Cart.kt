package com.example.lab09ejerc1.model

// Representa un carrito de compras que contiene una lista de productos.
data class Cart(
    val id: Int,
    val products: List<Product>, // Lista de productos en este carrito
    val total: Double,
    val discountedTotal: Double,
    val userId: Int,
    val totalProducts: Int,
    val totalQuantity: Int
)
