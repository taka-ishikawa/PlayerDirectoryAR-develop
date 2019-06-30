package com.example.playerdirectoryar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class TableClubsListAdapter(context: Context): BaseAdapter() {
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
            convertView = layoutInflater.inflate(R.layout.list_table_clubs, parent, false)
        }

        val imageViewEmblem = convertView!!.findViewById<View>(R.id.imageViewEmblemTable) as ImageView
        val textViewClubName  = convertView.findViewById<View>(R.id.textViewClubNameTable) as TextView
        val textViewRank = convertView.findViewById<View>(R.id.textViewRank) as TextView

        imageViewEmblem.setImageResource(arrayListClubInfo[position].emblemIdDrawable!!)
        textViewClubName.text = arrayListClubInfo[position].clubName
        if (arrayListClubInfo[position].rank.toString().length == 1) {
            textViewRank.text = "  ${arrayListClubInfo[position].rank}"
        } else if (arrayListClubInfo[position].rank.toString().length == 2) {
            textViewRank.text = "${arrayListClubInfo[position].rank}"
        }

        return convertView
    }

    fun setArrayListClubInfo(arrayListClubInfo: ArrayList<ClubInfo>) {
        this.arrayListClubInfo = arrayListClubInfo
    }
}