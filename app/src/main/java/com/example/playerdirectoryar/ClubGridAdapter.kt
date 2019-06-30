package com.example.playerdirectoryar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class ClubGridAdapter(context: Context): BaseAdapter() {
    private val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private var arrayListClubInfo = ArrayList<ClubInfo>()

    override fun getCount(): Int {
        return arrayListClubInfo.size
    }

    override fun getItem(position: Int): Any {
        return arrayListClubInfo[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.grid_clubs, parent, false)
        }

        val imageViewEmblem = convertView!!.findViewById<View>(R.id.imageViewEmblem) as ImageView
        val textView = convertView.findViewById<View>(R.id.textViewClubNameGrid) as TextView

        imageViewEmblem.setImageResource(arrayListClubInfo[position].emblemIdDrawable!!)
        textView.text = arrayListClubInfo[position].clubName

        return convertView
    }

    fun setArrayListClubInfo(arrayListClubInfo: ArrayList<ClubInfo>) {
        this.arrayListClubInfo = arrayListClubInfo
    }
}