package pwr.edu.foodshop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import pwr.edu.foodshop.adapter.ProductAdapter
import pwr.edu.foodshop.database.*
import pwr.edu.foodshop.database.product.LooseProduct
import pwr.edu.foodshop.database.product.PieceProduct
import pwr.edu.foodshop.database.product.Product
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    companion object {
         lateinit var products: List<Product>
    }

    private val db :DatabaseHandler by lazy{DatabaseHandler(this)}
    private val adapter by lazy{ ProductAdapter(this, products)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        products_recyclerview.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        swipeRefreshLayout.isRefreshing = false

        //insertData(db)
        getProducts(db)

        updateCartCount(db)

        swipeRefreshLayout.setOnRefreshListener {
            refresh()
        }


        basketButton.setOnClickListener {
            startActivity(Intent(this, ShoppingCartActivity::class.java))
        }

    }

    override fun onRestart() {
        super.onRestart()

        refresh()
    }

    private fun updateCartCount(db: DatabaseHandler) {
        try {
            cart_size.text = db.readCartData()?.size.toString()
        } catch (e: Exception) {
            Log.e("MainActivity", e.message.toString())
            cart_size.text = 0.toString()
        }
    }

    private fun getProducts(db: DatabaseHandler) {
        products = db.readData().toList()
       // val adapter = ProductAdapter(this, products)
        products_recyclerview.adapter = adapter
        adapter.notifyDataSetChanged()
    }


    private fun refresh(): Unit {

        adapter.notifyDataSetChanged()

        updateCartCount(db)

        Log.d("MainActivity", "Refreshed")
        swipeRefreshLayout.isRefreshing = false
    }


    private fun insertData(db: DatabaseHandler) {

        val cashew = LooseProduct(
            "orzechy nerkowca",
            5.0,
            "https://balidirectstore.com/wp-content/uploads/2018/10/whole-cashew.jpg"
        )
        db.insertProduct(cashew)

        val pear = PieceProduct(
            "gruszka",
            1.0,
            "https://amico.market/cache/images/zoom/2019/img_g_cron_6464460-5fdf3cd170c1c6.20014720.jpg"
        )
        db.insertProduct(pear)


        val apple = PieceProduct(
            "jabłko",
            0.5,
            "https://iranfreshfruit.net/wp-content/uploads/2020/01/red-apple-fruit.jpg"
        )
        db.insertProduct(apple)


        val cocoa = LooseProduct(
            "kakao",
            12.0,
            "https://www.aromatika.com.pl/environment/cache/images/300_300_productGfx_bd5ce349e349cb51ffec6c00f369299c.jpg"
        )
        db.insertProduct(cocoa)

        val poppy = LooseProduct(
            "mak",
            2.1,
            "https://www.aromatika.com.pl/userdata/public/gfx/0e41a97158a6b458edeabb4857135eff.jpg"
        )
        db.insertProduct(poppy)

        val anise = LooseProduct(
            "anyż",
            25.0,
            "https://www.aromatika.com.pl/userdata/public/gfx/eab5a83921d6accfcbc90169f2ada847.jpg"
        )
        db.insertProduct(anise)

        val avocado = PieceProduct(
                "awokado",
                7.50,
                "https://wyposazamysklepy.pl/userdata/public/gfx/93c66a6a6bb8006469f6a5743d0d9609.jpg"
        )
        db.insertProduct(avocado)

        val sunflower = LooseProduct(
                "słonecznik",
                0.45,
                "https://medycznewiadomosci.pl/wp-content/uploads/2020/07/pestki-slonecznika-na-odchudzanie-1.jpg"
        )
        db.insertProduct(sunflower)

    }


}