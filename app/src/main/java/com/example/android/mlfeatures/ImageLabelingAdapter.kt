package com.example.android.mlfeatures

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

import java.util.ArrayList

class ImageLabelingAdapter(context: Context, textViewResourceId: Int, objects: ArrayList<ImageLabellingModel>) :
    ArrayAdapter<ImageLabellingModel>(context, textViewResourceId, objects) {

    internal var animalList = ArrayList<ImageLabellingModel>()

    init {
        animalList = objects
    }

    override fun getCount(): Int {
        return super.getCount()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var v = convertView
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        v = inflater.inflate(R.layout.list_item_labelling, null)
        val name = v!!.findViewById(R.id.name) as TextView
        val prob = v!!.findViewById(R.id.probability) as TextView
        val str: String =  " "+animalList[position].probability
        name.setText(animalList[position].name)
        prob.setText(str)
        return v

    }

}