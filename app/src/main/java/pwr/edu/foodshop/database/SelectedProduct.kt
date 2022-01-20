package pwr.edu.foodshop.database

import android.content.Context
import pwr.edu.foodshop.database.product.LooseProduct
import pwr.edu.foodshop.database.product.Product
import kotlin.properties.Delegates

class SelectedProduct {

    var id: Int = 0
    var product: Product = LooseProduct()
    var charge: Double = 0.0
    var quantity: Int by Delegates.observable(0) {
            _, _, _ ->
        charge = quantity * product.price / product.quantity()
    }





    constructor(id: Int, product: Product, quantity: Int) {
        this.id = id
        this.product = product
        this.quantity = quantity
    }

    constructor(product: Product, quantity: Int) {
        this.product = product
        this.quantity = quantity
    }

    constructor(product: Product) {
        this.product = product
        this.quantity = product.quantity()
    }

    constructor()

}