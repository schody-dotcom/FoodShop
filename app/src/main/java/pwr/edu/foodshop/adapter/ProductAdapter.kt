package pwr.edu.foodshop.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.product_row_item.view.*
import kotlinx.android.synthetic.main.product_row_item.view.product_image
import kotlinx.android.synthetic.main.product_row_item.view.product_name
import kotlinx.android.synthetic.main.product_row_item.view.product_price
import pwr.edu.foodshop.MainActivity
import pwr.edu.foodshop.R
import pwr.edu.foodshop.database.*
import pwr.edu.foodshop.database.product.LooseProduct
import pwr.edu.foodshop.database.product.Product
import java.lang.Exception

class ProductAdapter(var context: Context, var products: List<Product> = arrayListOf()) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    private lateinit var db: DatabaseHandler


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.product_row_item, null)

        db = DatabaseHandler(context)

        return ViewHolder(view, db)

    }


    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.bindProduct(products[position])
    }

    class ViewHolder(view: View, db: DatabaseHandler) : RecyclerView.ViewHolder(view) {

        private val db = db
        private fun Double.chargeToString( perWhat :String): String = "$this zł / $perWhat"


        fun bindProduct(product: Product) {

            itemView.product_name.text = product.name

            itemView.product_price.text =
                if (product is LooseProduct)
                    product.price.chargeToString("100 g")
                else
                    product.price.chargeToString("szt.")


            Picasso.get().load(product.image).into(itemView.product_image)

            try {
                db.getCartProductById(product.id)
                itemView.addToCart.isVisible = false
                itemView.removeFromCart.isVisible = true
            }catch (e: Exception){
                itemView.addToCart.isVisible = true
                itemView.removeFromCart.isVisible = false
            }

            fun updateCartCount() {

                val count = db.readCartData()?.size?: 0

                (itemView.context as MainActivity).cart_size.text = count.toString()
            }

            itemView.addToCart.setOnClickListener { view ->

                db.addProductToCart(product)


                Snackbar.make(
                    (itemView.context as MainActivity), view,
                    "${product.name} added to your cart",
                    Snackbar.LENGTH_LONG
                ).show()

                itemView.addToCart.isVisible = false
                itemView.removeFromCart.isVisible = true

                updateCartCount()
            }

            itemView.removeFromCart.setOnClickListener { view ->

                db.deleteProductFromCart(product)


                Snackbar.make(
                    (itemView.context as MainActivity), view,
                    "${product.name} removed from your cart",
                    Snackbar.LENGTH_LONG
                ).show()

                itemView.addToCart.isVisible = true
                itemView.removeFromCart.isVisible = false

                updateCartCount()
            }


        }


    }

}