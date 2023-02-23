package com.example.hastkala.adapters

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.hastkala.R
import com.example.hastkala.model.ProductSize

class DropDownAdapter(private val activity: Activity, private val objects: Array<ProductSize>) :
    ArrayAdapter<ProductSize>(activity, R.layout.sizedropdown, objects) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view: View? = convertView
        val viewHolder:ViewHolder
        if (view == null) {
            view = activity.layoutInflater.inflate(R.layout.sizedropdown, parent, false)
            viewHolder = ViewHolder()
            viewHolder.sizeid=view.findViewById(R.id.size)
            viewHolder.fixedsize=view.findViewById(R.id.fixed)
            view.tag = viewHolder
        } else
            viewHolder = view.tag as ViewHolder

            viewHolder.sizeid.text=objects[position]!!.sizeId.toString()
            viewHolder.fixedsize.text= objects[position]!!.fixedSize

            viewHolder.sizeid
        return view!!
    }

    companion object {
        class ViewHolder {
            lateinit var sizeid: TextView
            lateinit var fixedsize: TextView
        }
    }
}