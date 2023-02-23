package com.example.hastkala.adapters

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import com.example.hastkala.CategorywiseProductActivity
import com.example.hastkala.ProductActivity
import com.example.hastkala.model.Category
import com.example.hastkala.R

class CategoryGridAdapter(private val activity: Activity, private val objects: Array<Category>) : ArrayAdapter<Category>(activity, R.layout.categorygrid, objects) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View
    {
        var view: View? = convertView
        val viewHolder: ViewHolder

        if (view == null)
        {
            view = activity.layoutInflater.inflate(R.layout.categorygrid, parent, false)

            viewHolder = ViewHolder()
            viewHolder.categoryname = view.findViewById(R.id.name)
            viewHolder.images = view.findViewById(R.id.catimages)

            view.tag = viewHolder
        }
        else
            viewHolder = view.tag as ViewHolder

        viewHolder.categoryname.text = objects[position].name
        viewHolder.images.setImageURI(Uri.parse("${activity.externalCacheDir}/${objects[position].imagepath}/${objects[position].image}"))
        viewHolder.categoryname.setOnClickListener {
            val intent = Intent(context, CategorywiseProductActivity::class.java)
            intent.putExtra("Catname",objects[position].name)
            context.startActivity(intent)
        }
        return view!!
    }
    companion object
    {
        class ViewHolder
        {
            lateinit var categoryname: Button
            lateinit var images:ImageView
        }
    }
}