package com.example.playerdirectoryar

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_table_clubs.*


class TableClubsFragment : Fragment() {

    private var callback: OnHeadlineSelectedListener? = null

    private var arrayListClubInfo = ArrayList<ClubInfo>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_table_clubs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseDatabase.reference.child(PATH_CLUB_INFO).addValueEventListener(valueEventListener)

        listViewTableClubs.visibility = View.INVISIBLE

        listViewTableClubs.setOnItemClickListener { adapterView, view, position, long ->
            callback!!.onArticleSelected(position, arrayListClubInfo[position].clubName)
        }
    }

    fun setOnHeadlineSelectedListener(callback: OnHeadlineSelectedListener?) {
        this.callback = callback
    }

    // This interface can be implemented by the Activity, parent Fragment,
    // or a separate test implementation.
    interface OnHeadlineSelectedListener {
        fun onArticleSelected(position: Int, clubName: String)
        fun onSetTitle(title: String)
    }

    private val valueEventListener = object : ValueEventListener {
        override fun onDataChange(p0: DataSnapshot) {
            arrayListClubInfo = arrayListOf()
            for (child in p0.children) {
                val mapClub = child.value as Map<*, *>?
                if (mapClub != null) {
                    val clubName = mapClub["clubName"] as String
                    val clubEng = mapClub["clubEng"] as String
//                    val clubNum = mapClub["clubNum"] as Long
                    val rank = mapClub["rank"] as Long
                    val emblemIdDrawable = resources.getIdentifier(clubEng.toLowerCase(), "drawable", "com.example.playerdirectoryar")

                    val clubInfo = ClubInfo(clubName, clubEng, 0, rank.toInt())
                    clubInfo.emblemIdDrawable = emblemIdDrawable
                    arrayListClubInfo.add(clubInfo)
                }
            }
            if (context != null) {
                arrayListClubInfo.sortBy { clubInfo: ClubInfo ->
                    clubInfo.rank
                }
                val tableClubsListAdapter = TableClubsListAdapter(context!!)
                tableClubsListAdapter.setArrayListClubInfo(arrayListClubInfo)
                listViewTableClubs?.adapter = tableClubsListAdapter

                listViewTableClubs?.visibility = View.VISIBLE
                progressBarTableClubs?.visibility = View.GONE
            }
        }

        override fun onCancelled(p0: DatabaseError) {

        }
    }
}