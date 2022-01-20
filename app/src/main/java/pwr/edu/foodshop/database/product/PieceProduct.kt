package pwr.edu.foodshop.database.product

class PieceProduct : Product {

    constructor(name: String, price: Double, image: String) : super(name, price, image)
    constructor() : super()

    override fun quantity(): Int = 1
    override fun quantityUnit(): String = "szt."
}