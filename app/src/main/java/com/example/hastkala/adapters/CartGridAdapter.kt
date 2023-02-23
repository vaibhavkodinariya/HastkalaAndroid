package com.example.hastkala.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.addTextChangedListener
import com.example.hastkala.ProductActivity
import com.example.hastkala.R
import com.example.hastkala.model.Cart
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartGridAdapter(private val activity: Activity, private val objects: Array<Cart>) :
    ArrayAdapter<Cart>(activity, R.layout.activity_cart, objects) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View? = convertView
        val viewHolder: ViewHolder

        if (view == null) {
            view = activity.layoutInflater.inflate(R.layout.cartgrid, parent, false)

            viewHolder = ViewHolder()

            viewHolder.productname = view.findViewById(R.id.ProductName)
            viewHolder.productdetails = view.findViewById(R.id.discription)
            viewHolder.size = view.findViewById(R.id.Productsize)
            viewHolder.price = view.findViewById(R.id.price)
            viewHolder.image = view.findViewById(R.id.ProductImage)
            viewHolder.autoCompleteTextView = view.findViewById(R.id.complete)
            viewHolder.sizetext = view.findViewById(R.id.size)
            viewHolder.btnremove = view.findViewById(R.id.remove)

            val quantity = view.resources.getStringArray(R.array.Quantity)
            val adapter = ArrayAdapter(context, R.layout.quantitydropdown, quantity)

            val fetchqty = objects[position].Quantity.toInt() - 1
            viewHolder.autoCompleteTextView.setAdapter(adapter)
            viewHolder.autoCompleteTextView.setText(adapter.getItem(fetchqty), false)

            view.tag = viewHolder
        } else
            viewHolder = view.tag as ViewHolder

        viewHolder.productname.text = objects[position].Name
        viewHolder.productdetails.text = objects[position].Details

//        var qty = arrayListOf<String>()
        val pref = activity.getSharedPreferences("hastkala", Context.MODE_PRIVATE)
        val userid = pref.getInt("UserID", 0)
        viewHolder.autoCompleteTextView.setOnItemClickListener { _, _, i, _ ->
            val qty=viewHolder.autoCompleteTextView.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                val messege = com.example.hastkala.api.Cart.updateincart(
                    qty.toInt(),
                    objects[position].ProductId,
                    userid,
                    objects[position].OrderId
                )
                withContext(Dispatchers.Main)
                {
                    Toast.makeText(
                        context,
                        messege.getString("Messege"),
                        Toast.LENGTH_SHORT
                    ).show()
                    activity.recreate()
                }
            }
        }
        if (objects[position].Size.equals("null")) {
            viewHolder.size.visibility = View.INVISIBLE
            viewHolder.sizetext.visibility = View.INVISIBLE
        } else {
            viewHolder.size.visibility = View.VISIBLE
            viewHolder.sizetext.visibility = View.VISIBLE
            viewHolder.size.text = objects[position].Size
        }

        viewHolder.price.text = "₹${objects[position].Totalprice}"
        viewHolder.image.setImageURI(Uri.parse("${activity.externalCacheDir}/${objects[position].Imagepath}/${objects[position].Image}"))

        viewHolder.image.setOnClickListener {
            val intent = Intent(context, ProductActivity::class.java)
            intent.putExtra("productid", objects[position].ProductId)
            context.startActivity(intent)
        }

        viewHolder.btnremove.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val messege =
                    com.example.hastkala.api.Cart.deletefromcart(objects[position].OrderId, userid)
                withContext(Dispatchers.Main) {
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
            lateinit var btnremove: Button
            lateinit var image: ImageView
            lateinit var autoCompleteTextView: AutoCompleteTextView
            lateinit var sizetext: TextView
        }
    }
}