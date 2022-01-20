package pwr.edu.foodshop.database.product

class LooseProduct : Product {


    constructor(name: String, price: Double, image: String) : super(name, price, image)
    constructor() : super()


    override fun quantity(): Int = 100

    override fun quantityUnit(): String = "g"
}