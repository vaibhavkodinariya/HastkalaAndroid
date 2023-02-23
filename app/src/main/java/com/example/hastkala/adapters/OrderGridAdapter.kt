package com.example.hastkala.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.hastkala.ProductActivity
import com.example.hastkala.R
import com.example.hastkala.model.Order
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrderGridAdapter(
    private val activity: Activity,
    private val objects: Array<Order>
) : ArrayAdapter<Order>(activity, R.layout.activity_order, objects) {
    @SuppressLint("SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View? = convertView
        val viewHolder: ViewHolder

        if (view == null) {
            view = activity.layoutInflater.inflate(R.layout.ordergrid, parent, false)

            viewHolder = ViewHolder()
            viewHolder.productname = view.findViewById(R.id.Ordered_ProductName)
            viewHolder.productdetails = view.findViewById(R.id.Ordered_ProductDiscription)
            viewHolder.size = view.findViewById(R.id.Ordered_ProductSize)
            viewHolder.price = view.findViewById(R.id.Ordered_ProductPrice)
            viewHolder.quantity = view.findViewById(R.id.Ordered_Quantity)
            viewHolder.image = view.findViewById(R.id.Ordered_ProductImage)
            viewHolder.orderstatus = view.findViewById(R.id.Order_Status_value)
            viewHolder.orderid = view.findViewById(R.id.orderid)
            viewHolder.cancel = view.findViewById(R.id.edit_details)

            view.tag = viewHolder
        } else
            viewHolder = view.tag as ViewHolder

        viewHolder.productname.text = objects[position].Name
        viewHolder.productdetails.text = objects[position].Details
        viewHolder.price.text = "₹${objects[position].TotalPrice}"
        viewHolder.image.setImageURI(Uri.parse("${activity.externalCacheDir}/${objects[position].Imagepath}/${objects[position].Image}"))
        viewHolder.quantity.text = objects[position].Purchaseqty
        viewHolder.orderstatus.text = objects[position].Status

        if (objects[position].Size == "null") {
            viewHolder.size.visibility = View.INVISIBLE
        } else {
            viewHolder.size.visibility = View.VISIBLE
            viewHolder.size.text = objects[position].Size
        }
        viewHolder.orderid.text = objects[position].OrderId.toString()

        viewHolder.image.setOnClickListener {
            val intent = Intent(context, ProductActivity::class.java)
            intent.putExtra("productid", objects[position].ProductId)
            context.startActivity(intent)
        }

        val pref = context.getSharedPreferences("hastkala", Context.MODE_PRIVATE)
        val userid = pref.getInt("UserID", 0)
        viewHolder.cancel.isEnabled = objects[position].Status == "Ordered Placed"
        viewHolder.cancel.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val messege = com.example.hastkala.api.Order.deletefromorder(
                    objects[position].ProductId,
                    objects[position].OrderId,
                    userid,
                    objects[position].Purchaseqty
                )
                withContext(Dispatchers.Main){
                    Toast.makeText(context, messege.getString("Messege"), Toast.LENGTH_SHORT).show()
                    activity.recreate()
                }
            }

        }

        return view!!
    }

    companion object {
        class ViewHolder {
            lateinit var productname: TextView
            lateinit var productdetails: TextView
            lateinit var size: TextView
            lateinit var price: TextView
            lateinit var image: ImageView
            lateinit var quantity: TextView
            lateinit var orderstatus: TextView
            lateinit var orderid: TextView
            lateinit var cancel: Button
        }
    }
}