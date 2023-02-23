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
import com.example.hastkala.model.Proceedtopay

class ProceedtopayGridAdapter(
    private val activity: Activity,
    private val objects: Array<Proceedtopay>
) : ArrayAdapter<Proceedtopay>(activity, R.layout.activity_proceedtopay, objects) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View? = convertView
        val viewHolder: ViewHolder

        if (view == null) {
            view = activity.layoutInflater.inflate(R.layout.proceedtopaygrid, parent, false)

            viewHolder = ViewHolder()
            viewHolder.productname=view.findViewById(R.id.ProductName)
            viewHolder.productdetails=view.findViewById(R.id.discription)
            viewHolder.size=view.findViewById(R.id.size)
            viewHolder.price=view.findViewById(R.id.price)
            viewHolder.quantity=view.findViewById(R.id.quantity)
            viewHolder.image = view.findViewById(R.id.ProductImage)
            viewHolder.sizetextview=view.findViewById(R.id.sizetext)
            view.tag = viewHolder
        } else
            viewHolder = view.tag as ViewHolder

        viewHolder.productname.text = objects[position].Name
        viewHolder.productdetails.text=objects[position].Details
        if (objects[position].Size.equals("false")) {
            viewHolder.size.visibility = View.INVISIBLE
            viewHolder.sizetextview.visibility=View.INVISIBLE
        } else {
            viewHolder.size.text = objects[position].Size
        }
        viewHolder.price.text="₹${objects[position].TotalPrice}"
        viewHolder.image.setImageURI(Uri.parse("${activity.externalCacheDir}/${objects[position].ImagesPath}/${objects[position].Image}"))
        viewHolder.quantity.text = objects[position].Purchaseqty

        viewHolder.image.setOnClickListener{
            var intent= Intent(context, ProductActivity::class.java)
            intent.putExtra("productid",objects[position].ProductID)
            context.startActivity(intent)
        }
        return view!!
    }
    companion object
    {
        class ViewHolder
        {
            lateinit var productname: TextView
            lateinit var productdetails: TextView
            lateinit var size: TextView
            lateinit var price: TextView
            lateinit var image: ImageView
            lateinit var quantity: TextView
            lateinit var sizetextview:TextView
        }
    }
}