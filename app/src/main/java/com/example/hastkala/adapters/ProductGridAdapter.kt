package com.example.hastkala.adapters

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.hastkala.ProductActivity
import com.example.hastkala.R
import com.example.hastkala.model.Product

class ProductGridAdapter(private val activity: Activity, private val objects: Array<Product>) : ArrayAdapter<Product>(activity,
    R.layout.productgrid_home, objects)
{
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View
    {
        var view: View? = convertView
        val viewHolder: ViewHolder

        if (view == null)
        {
            view = activity.layoutInflater.inflate(R.layout.productgrid_home, parent, false)

            viewHolder = ViewHolder()
            viewHolder.name = view.findViewById(R.id.ProductName)
            viewHolder.details=view.findViewById(R.id.discription)
            viewHolder.discountprice=view.findViewById(R.id.product_discount)
            viewHolder.price = view.findViewById(R.id.product_price)
            viewHolder.image = view.findViewById(R.id.ProductImage)

            view.tag = viewHolder
        }
        else
            viewHolder = view.tag as ViewHolder

        viewHolder.name.text = objects[position].name
        viewHolder.details.text=objects[position].details
        viewHolder.price.text = "₹${objects[position].price}"
        val percentageprice = objects[position].price * 25 / 100
        val discountprice = objects[position].price + percentageprice
        viewHolder.discountprice.text="₹$discountprice"
        viewHolder.image.setImageURI(Uri.parse("${activity.externalCacheDir}/${objects[position].path}/${objects[position].image}"))
        viewHolder.image.setOnClickListener {
            val intent = Intent(context, ProductActivity::class.java)
            intent.putExtra("productid", objects[position].id)
            context.startActivity(intent)
        }
        return view!!
    }

    companion object
    {
        class ViewHolder
        {
            lateinit var name: TextView
            lateinit var details:TextView
            lateinit var discountprice:TextView
            lateinit var price: TextView
            lateinit var image: ImageView
        }
    }
}