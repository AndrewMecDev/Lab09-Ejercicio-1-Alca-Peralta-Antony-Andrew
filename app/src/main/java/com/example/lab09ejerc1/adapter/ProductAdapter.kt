package com.example.lab09ejerc1.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.lab09ejerc1.databinding.ItemProductBinding
import com.example.lab09ejerc1.model.Product
import java.util.Locale

// Adaptador para mostrar una lista de productos en un RecyclerView.
class ProductAdapter(
    private var products: List<Product>,
    private val onItemClick: (Product) -> Unit = {}
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    // ViewHolder que contiene las vistas para un único item de producto.
    class ProductViewHolder(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root)

    // Crea nuevas vistas (invocado por el layout manager).
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        // Infla el layout del item usando ViewBinding
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    // Retorna el tamaño de tu dataset (invocado por el layout manager).
    override fun getItemCount() = products.size

    // Reemplaza el contenido de una vista (invocado por el layout manager).
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.binding.apply {
            // Asigna el título del producto
            tvProductTitle.text = product.title

            // Formatea y asigna el precio
            tvProductPrice.text = String.format(Locale.US, "$%.2f", product.price)

            // Asigna la cantidad
            tvProductQuantity.text = "Cantidad: ${product.quantity}"

            // Carga la imagen del producto usando Glide
            Glide.with(holder.itemView.context)
                .load(product.thumbnail)
                .centerCrop()
                .into(ivProductThumbnail)

            // Click para detalles
            root.setOnClickListener { onItemClick(product) }
        }
    }
}
