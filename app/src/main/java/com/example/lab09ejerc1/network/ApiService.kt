package com.example.lab09ejerc1.network

import com.example.lab09ejerc1.model.CartsApiResponse
import com.example.lab09ejerc1.model.Cart
import com.example.lab09ejerc1.model.request.AddCartRequest
import com.example.lab09ejerc1.model.request.UpdateCartRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

// Interfaz que define los endpoints de la API.
interface ApiService {
    // Obtener todos los carritos
    @GET("carts")
    fun getCarts(): Call<CartsApiResponse>

    // Obtener un carrito por ID
    @GET("carts/{id}")
    fun getCart(@Path("id") cartId: Int): Call<Cart>

    // Obtener carritos por usuario
    @GET("carts/user/{userId}")
    fun getCartsByUser(@Path("userId") userId: Int): Call<CartsApiResponse>

    // Agregar un nuevo carrito
    @POST("carts/add")
    fun addCart(@Body request: AddCartRequest): Call<Cart>

    // Actualizar un carrito existente
    @PUT("carts/{id}")
    fun updateCart(@Path("id") cartId: Int, @Body request: UpdateCartRequest): Call<Cart>

    // Eliminar un carrito
    @DELETE("carts/{id}")
    fun deleteCart(@Path("id") cartId: Int): Call<Cart>
}
