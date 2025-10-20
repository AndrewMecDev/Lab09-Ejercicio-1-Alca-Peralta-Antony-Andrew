package com.example.lab09ejerc1

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.lab09ejerc1.adapter.ProductAdapter
import com.example.lab09ejerc1.databinding.ActivityMainBinding
import com.example.lab09ejerc1.model.CartsApiResponse
import com.example.lab09ejerc1.model.Product
import com.example.lab09ejerc1.model.request.AddCartRequest
import com.example.lab09ejerc1.model.request.CartProductRequest
import com.example.lab09ejerc1.model.request.UpdateCartRequest
import com.example.lab09ejerc1.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflar el layout usando ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar el RecyclerView
        setupRecyclerView()

        // Configurar acciones de botones (agregar/actualizar/eliminar)
        setupActions()

        // Cargar los datos desde la API
        fetchCartsData()
    }

    private fun setupRecyclerView() {
        // Asignar un LayoutManager al RecyclerView
        binding.recyclerViewProducts.layoutManager = LinearLayoutManager(this)
    }

    private fun setupActions() {
        binding.btnAddCart.setOnClickListener {
            setLoading(true, "Agregando productos al carrito...")
            val request = AddCartRequest(
                userId = 1,
                products = listOf(
                    CartProductRequest(id = 144, quantity = 4),
                    CartProductRequest(id = 98, quantity = 1)
                )
            )
            RetrofitClient.instance.addCart(request).enqueue(object : Callback<com.example.lab09ejerc1.model.Cart> {
                override fun onResponse(
                    call: Call<com.example.lab09ejerc1.model.Cart>,
                    response: Response<com.example.lab09ejerc1.model.Cart>
                ) {
                    setLoading(false)
                    if (response.isSuccessful) {
                        Toast.makeText(this@MainActivity, "Carrito agregado correctamente", Toast.LENGTH_SHORT).show()
                        fetchCartsData()
                    } else {
                        showError("Error al agregar: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<com.example.lab09ejerc1.model.Cart>, t: Throwable) {
                    setLoading(false)
                    showError("Fallo al agregar: ${t.message}")
                }
            })
        }

        binding.btnUpdateCart.setOnClickListener {
            setLoading(true, "Actualizando carrito #1...")
            val request = UpdateCartRequest(
                merge = true,
                products = listOf(
                    CartProductRequest(id = 1, quantity = 1)
                )
            )
            RetrofitClient.instance.updateCart(1, request).enqueue(object : Callback<com.example.lab09ejerc1.model.Cart> {
                override fun onResponse(
                    call: Call<com.example.lab09ejerc1.model.Cart>,
                    response: Response<com.example.lab09ejerc1.model.Cart>
                ) {
                    setLoading(false)
                    if (response.isSuccessful) {
                        Toast.makeText(this@MainActivity, "Carrito actualizado", Toast.LENGTH_SHORT).show()
                        fetchCartsData()
                    } else {
                        showError("Error al actualizar: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<com.example.lab09ejerc1.model.Cart>, t: Throwable) {
                    setLoading(false)
                    showError("Fallo al actualizar: ${t.message}")
                }
            })
        }

        binding.btnDeleteCart.setOnClickListener {
            setLoading(true, "Eliminando carrito #1...")
            RetrofitClient.instance.deleteCart(1).enqueue(object : Callback<com.example.lab09ejerc1.model.Cart> {
                override fun onResponse(
                    call: Call<com.example.lab09ejerc1.model.Cart>,
                    response: Response<com.example.lab09ejerc1.model.Cart>
                ) {
                    setLoading(false)
                    if (response.isSuccessful) {
                        Toast.makeText(this@MainActivity, "Carrito eliminado", Toast.LENGTH_SHORT).show()
                        fetchCartsData()
                    } else {
                        showError("Error al eliminar: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<com.example.lab09ejerc1.model.Cart>, t: Throwable) {
                    setLoading(false)
                    showError("Fallo al eliminar: ${t.message}")
                }
            })
        }
    }

    private fun setLoading(loading: Boolean, message: String? = null) {
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        binding.btnAddCart.isEnabled = !loading
        binding.btnUpdateCart.isEnabled = !loading
        binding.btnDeleteCart.isEnabled = !loading
        if (loading && !message.isNullOrBlank()) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchCartsData() {
        // Mostrar la barra de progreso mientras se cargan los datos
        binding.progressBar.visibility = View.VISIBLE

        // Realizar la llamada a la API
        RetrofitClient.instance.getCarts().enqueue(object : Callback<CartsApiResponse> {

            // Se ejecuta cuando la respuesta HTTP es recibida
            override fun onResponse(call: Call<CartsApiResponse>, response: Response<CartsApiResponse>) {
                // Ocultar la barra de progreso
                binding.progressBar.visibility = View.GONE

                if (response.isSuccessful) {
                    val cartsResponse = response.body()
                    cartsResponse?.let {
                        // Aplanar la lista: obtener todos los productos de todos los carritos en una sola lista
                        val allProducts = it.carts.flatMap { cart -> cart.products }
                        displayProducts(allProducts)
                    }
                } else {
                    // Mostrar un mensaje de error si la respuesta no fue exitosa
                    showError("Error al cargar los datos: ${response.code()}")
                }
            }

            // Se ejecuta cuando ocurre un error en la comunicación (ej. sin internet)
            override fun onFailure(call: Call<CartsApiResponse>, t: Throwable) {
                // Ocultar la barra de progreso
                binding.progressBar.visibility = View.GONE
                // Mostrar un mensaje de error
                showError("Fallo en la conexión: ${t.message}")
            }
        })
    }

    private fun displayProducts(products: List<Product>) {
        // Crear el adaptador con la lista de productos y asignarlo al RecyclerView
        val adapter = ProductAdapter(products) { product ->
            showProductDetails(product)
        }
        binding.recyclerViewProducts.adapter = adapter
    }

    private fun showProductDetails(product: Product) {
        val message = StringBuilder().apply {
            appendLine(product.title)
            appendLine()
            appendLine("Precio: $${String.format("%.2f", product.price)}")
            appendLine("Cantidad: ${product.quantity}")
            appendLine("Total: $${String.format("%.2f", product.total)}")
            appendLine("Descuento: ${String.format("%.2f", product.discountPercentage)}%")
            appendLine("Precio con descuento: $${String.format("%.2f", product.discountedPrice)}")
        }.toString()

        AlertDialog.Builder(this)
            .setTitle("Detalles del producto")
            .setMessage(message)
            .setPositiveButton("Cerrar", null)
            .show()
    }

    private fun showError(message: String) {
        // Muestra un mensaje Toast con el error
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
