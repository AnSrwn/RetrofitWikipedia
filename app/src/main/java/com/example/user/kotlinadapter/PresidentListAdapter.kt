package com.example.user.kotlinadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class PresidentListAdapter(context: Context, private val presidents: MutableList<President>): BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rowView = inflater.inflate(R.layout.item_president, parent, false)
        val thisPresident = presidents[position]

        var tv = rowView.findViewById(R.id.textViewName) as TextView
        tv.text = thisPresident.name

        tv = rowView.findViewById(R.id.textViewStartDuty) as TextView
        tv.text = String.format(thisPresident.startDuty.toString())
        //tv.text = Integer.toString(thisPresident.startDuty)

        tv = rowView.findViewById(R.id.textViewEndDuty) as TextView
        tv.text = String.format(thisPresident.endDuty.toString())
        //tv.text = Integer.toString(thisPresident.endDuty)

        return rowView
    }

    override fun getCount(): Int {
        return presidents.size
    }

    override fun getItem(position: Int): Any {
        return presidents[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}