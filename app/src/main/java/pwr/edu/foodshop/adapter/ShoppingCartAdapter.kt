package pwr.edu.foodshop.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cart_list_item.view.*
import kotlinx.android.synthetic.main.cart_list_item.view.product_image
import kotlinx.android.synthetic.main.cart_list_item.view.product_name
import kotlinx.android.synthetic.main.cart_list_item.view.product_price
import pwr.edu.foodshop.MainActivity
import pwr.edu.foodshop.R
import pwr.edu.foodshop.ShoppingCartActivity
import pwr.edu.foodshop.database.DatabaseHandler
import pwr.edu.foodshop.database.SelectedProduct

class ShoppingCartAdapter(
    private var context: Context,
    private var cartItems: List<SelectedProduct>
) :
    RecyclerView.Adapter<ShoppingCartAdapter.ViewHolder>() {

    private val db :DatabaseHandler by lazy{DatabaseHandler(context)}


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ShoppingCartAdapter.ViewHolder {

        val layout = LayoutInflater.from(context).inflate(R.layout.cart_list_item, parent, false)


        return ViewHolder(layout, db, context)
    }

    override fun getItemCount(): Int = cartItems.size

    override fun onBindViewHolder(viewHolder: ShoppingCartAdapter.ViewHolder, position: Int) {

        viewHolder.bindItem(cartItems[position])
    }

    fun removeItem() {

        cartItems = db.readCartData()!!


        (context as? ShoppingCartActivity)?.updateCartData()

        notifyDataSetChanged()

    }

    inner class ViewHolder(view: View, db: DatabaseHandler, context: Context) :
        RecyclerView.ViewHolder(view) {

        private val db = db
        private val context = context

        private fun Double.chargeToString(): String = "$this zł"

        fun bindItem(cartItem: SelectedProduct) {

            fun updateProductInfo() {

                cartItem.quantity = db.getCartProductQuantity(cartItem.product.id)
                Picasso.get().load(cartItem.product.image).into(itemView.product_image)

                itemView.product_name.text = cartItem.product.name

                itemView.product_price.text = cartItem.charge.chargeToString()
                //itemView.product_price.text = cartItem.calculateCharge().chargeToString()

                itemView.product_quantity.text =
                    "${cartItem.quantity} ${cartItem.product.quantityUnit()}"
            }

            updateProductInfo()

            itemView.btnPlus.setOnClickListener { view ->
                db.updateQuantity(cartItem.product, cartItem.quantity + cartItem.product.quantity())
                updateProductInfo()

                (context as? ShoppingCartActivity)?.updateTotalPrice()
            }

            itemView.btnMinus.setOnClickListener { view ->
                if(cartItem.quantity - cartItem.product.quantity() > 0) {
                    db.updateQuantity(
                        cartItem.product,
                        cartItem.quantity - cartItem.product.quantity()
                    )
                    updateProductInfo()
                }
                else
                {
                    db.deleteProductFromCart(cartItem.product)


                    removeItem()

                }
                if (context is ShoppingCartActivity) {context.updateTotalPrice()}
            }

        }
    }




}