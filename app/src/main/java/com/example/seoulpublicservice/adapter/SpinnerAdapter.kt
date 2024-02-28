package com.example.seoulpublicservice.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.seoulpublicservice.R

class SpinnerAdapter(context: Context, resource: Int, items: List<String>) : ArrayAdapter<String>(context, resource, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }

    private fun getCustomView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_spinner, parent, false)

        val textView = view.findViewById<TextView>(R.id.spinner_text)
        textView.text = getItem(position)

        return view
    }
}