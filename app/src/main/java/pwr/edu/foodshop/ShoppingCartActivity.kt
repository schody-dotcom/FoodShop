package pwr.edu.foodshop

import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_shopping_cart.*
import pwr.edu.foodshop.adapter.ShoppingCartAdapter
import pwr.edu.foodshop.database.DatabaseHandler
import pwr.edu.foodshop.database.SelectedProduct
import pwr.edu.foodshop.database.product.Product
import java.lang.Exception

class ShoppingCartActivity  : AppCompatActivity() {

    companion object{
        lateinit var cart : List<SelectedProduct>
    }

    private lateinit var adapter: ShoppingCartAdapter


    private val db :DatabaseHandler by lazy{DatabaseHandler(this)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_cart)


        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material)
        upArrow?.setColorFilter(ContextCompat.getColor(this, R.color.design_default_color_on_primary), PorterDuff.Mode.SRC_ATOP)
        supportActionBar?.setHomeAsUpIndicator(upArrow)


        cart = db.readCartData()?: listOf()

        adapter = ShoppingCartAdapter(this, cart)
        adapter.notifyDataSetChanged()

        shopping_cart_recyclerView.adapter = adapter

        shopping_cart_recyclerView.layoutManager = LinearLayoutManager(this)

        updateTotalPrice()

        checkoutButton.setOnClickListener { view ->

            db.removeAllProductsFromCart()
            adapter.removeItem()

            total_price.text = "Kupione!"

        }

    }

    fun updateCartData(){
        cart = db.readCartData()?: listOf()
        updateTotalPrice()
    }
//^^^^^
    fun updateTotalPrice(){
        val totalPrice = cart.map{ selectedProduct ->

            selectedProduct.charge

        }.sum()

        total_price.text = "$totalPrice zł"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}