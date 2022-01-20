package pwr.edu.foodshop.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import pwr.edu.foodshop.database.product.LooseProduct
import pwr.edu.foodshop.database.product.PieceProduct
import pwr.edu.foodshop.database.product.Product


val DATABASE_NAME = "MyDB2"

val TABLE_NAME = "Products"
val COL_ID = "id"
val COL_NAME = "name"
val COL_PRICE = "price"
val COL_IMAGE = "image"
val COL_TYPE = "type"

val TABLE_NAME2 = "Cart"
val COL_PROD_ID = "prod_id"
val COL_QUANTITY = "quantity"

class DatabaseHandler(var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {


    override fun onCreate(db: SQLiteDatabase?) {

        val createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " VARCHAR (256), " +
                COL_PRICE + " REAL, " +
                COL_IMAGE + " VARCHAR (256), " +
                COL_TYPE + " VARCHAR (20))"

        db?.execSQL(createTable)


        val createTable2 = "CREATE TABLE " + TABLE_NAME2 + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PROD_ID + " INTEGER, " +
                COL_QUANTITY + " INTEGER) "

        db?.execSQL(createTable2)


    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun insertProduct(product: Product) {

        val db = this.writableDatabase

        var cv = ContentValues()

        cv.put(COL_NAME, product.name)
        cv.put(COL_PRICE, product.price)
        cv.put(COL_IMAGE, product.image)
        when (product) {
            is PieceProduct -> cv.put(COL_TYPE, "PIECE")
            is LooseProduct -> cv.put(COL_TYPE, "LOOSE")
        }


        var result =
                db.insert(TABLE_NAME, null, cv)




    }

    fun readData(): MutableList<Product> {
        var list: MutableList<Product> = ArrayList()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val result = db.rawQuery(query, null)

        if (result.moveToFirst()) {
            do {
                var product: Product =
                    when (result.getString(4)) {
                        "LOOSE" -> LooseProduct()
                        "PIECE" -> PieceProduct()
                        else -> PieceProduct()
                    }
                product.id = result.getString(0).toInt()
                product.name = result.getString(1)
                product.price = result.getDouble(2)
                product.image = result.getString(3)
                list.add(product)
            } while (result.moveToNext())
        }

        result.close()


        return list
    }

    fun deleteProduct(product: Product) {
        val db = this.writableDatabase

        db.delete(TABLE_NAME, "$COL_NAME=? ", arrayOf(product.name))
    }

    fun deleteByIdBiggerThan(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$COL_ID > ? ", arrayOf(id.toString()))
    }


    fun getProductById(id: Int): Product {



        val products: ArrayList<Product> = readData().toList() as ArrayList<Product>
        return products.toSet().filter { p ->
                p.id == id
            }[0]
    }

    fun readCartData(): MutableList<SelectedProduct>? {
        var list: MutableList<SelectedProduct> = ArrayList()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME2"
        val result = db.rawQuery(query, null)

        if (result.moveToFirst()) {
            do {
                val product = SelectedProduct()

                product.id = result.getString(0).toInt()
                val prod_id = result.getInt(1)
                val prod_id2 = result.getString(1)
                product.product = getProductById(prod_id)
                product.quantity = result.getInt(2)
                list.add(product)
            } while (result.moveToNext())
        }

        result.close()

        return list
    }

    fun addProductToCart(product: Product) {
        val db = this.writableDatabase

        val cv = ContentValues()

        cv.put(COL_PROD_ID, product.id)
        cv.put(COL_QUANTITY, product.quantity())


        val result = db.insert(TABLE_NAME2, null, cv)


    }

    fun deleteProductFromCart(product: Product) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME2, "$COL_PROD_ID=?", arrayOf(product.id.toString()))
    }

    fun updateQuantity(product: Product, quantity: Int) {
        var list: MutableList<SelectedProduct> = ArrayList()
        val db = this.readableDatabase

        var cv = ContentValues()
        cv.put(COL_QUANTITY, quantity)

        db.update(TABLE_NAME2, cv, "$COL_PROD_ID=?", arrayOf(product.id.toString()))
    }

    fun increaseQuantity(product: Product, quantity: Int) {
        var list: MutableList<SelectedProduct> = ArrayList()
        val db = this.readableDatabase

        var cv = ContentValues()
        cv.put(COL_QUANTITY, quantity)

        db.update(TABLE_NAME2, cv, "$COL_PROD_ID=?", arrayOf(product.id.toString()))
    }

    fun getCartProductById(id: Int): Product {

        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME2 WHERE $COL_PROD_ID = $id"
        val result = db.rawQuery(query, null)

        result.moveToFirst()

        val products: ArrayList<Product> = readData().toList() as ArrayList<Product>

        val product = products[result.getInt(1)]

        result.close()

        return product

    }

    fun getCartProductQuantity(productId: Int): Int {

        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME2 WHERE $COL_PROD_ID = $productId"
        val result = db.rawQuery(query, null)

        result.moveToFirst()

        val quantity = result.getInt(2)

        result.getInt(1)

        return quantity

    }

    fun removeAllProductsFromCart(){
        val db = this.readableDatabase
        db.execSQL("DELETE FROM  $TABLE_NAME2")


    }

}