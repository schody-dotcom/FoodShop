package pwr.edu.foodshop.database.product

import android.content.Context
import pwr.edu.foodshop.database.DatabaseHandler

abstract class Product {

    var id: Int = 0
    var name: String = ""
    var price: Double = 0.0
    var image : String = ""

    constructor(name: String, price : Double, image : String){
        this.name = name
        this.price = price
        this.image = image
    }

    constructor()

    abstract fun quantity():Int
    abstract fun quantityUnit(): String



}