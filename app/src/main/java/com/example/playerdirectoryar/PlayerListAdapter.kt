package com.example.playerdirectoryar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class PlayerListAdapter(context: Context, private val clubName: String?): BaseAdapter() {
    private val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private var arrayListPlayers = ArrayList<Player>()

    override fun getCount(): Int {
        return arrayListPlayers.size
    }

    override fun getItem(position: Int): Any {
        return arrayListPlayers[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_players, parent, false)
        }

        val textViewPlayerName = convertView!!.findViewById<View>(R.id.textViewPlayerNameList) as TextView
        val textViewUserName = convertView.findViewById<View>(R.id.textViewSquadNumberList) as TextView
        val textViewClubName = convertView.findViewById<View>(R.id.textViewBelongingClub) as TextView
        val imageViewPlayer = convertView.findViewById<View>(R.id.imageViewPlayerList) as ImageView

        if (arrayListPlayers[position].squadNumber == 0) {
            // just display GK, DF, MF, FW
            textViewPlayerName.text = arrayListPlayers[position].playerName  // actually playerName is position: "GK","DF",..
            textViewUserName.visibility = View.INVISIBLE
            textViewClubName.visibility = View.INVISIBLE
            imageViewPlayer.visibility = View.INVISIBLE
        } else { // display players info
            textViewUserName.visibility = View.VISIBLE
            textViewClubName.visibility = View.VISIBLE
            imageViewPlayer.visibility = View.VISIBLE

            textViewPlayerName.text = arrayListPlayers[position].playerName
            textViewUserName.text = arrayListPlayers[position].squadNumber.toString()
            if (clubName != null) {
                textViewClubName.text = clubName
            } else if (arrayListPlayers[position].clubName != null) {
                textViewClubName.text = arrayListPlayers[position].clubName
            }
            imageViewPlayer.setImageBitmap(arrayListPlayers[position].bitmapPlayer)
        }
        return  convertView
    }

    fun setArrayListPlayers(arrayListPlayers: ArrayList<Player>) {
        this.arrayListPlayers = arrayListPlayers
    }
}