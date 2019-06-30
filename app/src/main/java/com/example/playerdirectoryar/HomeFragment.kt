package com.example.playerdirectoryar

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {

    private var callback: OnHeadlineSelectedListener? = null

    private var arrayListClubInfo = ArrayList<ClubInfo>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val firebaseDatabase = FirebaseDatabase.getInstance()
        firebaseDatabase.reference.child(PATH_CLUB_INFO).addValueEventListener(valueEventListener)

        gridViewClubs.setOnItemClickListener { parent, view, position, id ->
            callback!!.onArticleSelected(position, arrayListClubInfo[position].clubName)
        }
    }

    private val valueEventListener = object : ValueEventListener {
        override fun onDataChange(p0: DataSnapshot) {
            arrayListClubInfo = arrayListOf()
            for (child in p0.children) {
                val mapClub = child.value as Map<*, *>?
                if (mapClub != null) {
                    val clubName = mapClub["clubName"] as String
                    val clubEng = mapClub["clubEng"] as String
                    val clubNum = mapClub["clubNum"] as Long
//                    val rank = mapClub["rank"] as Long
                    val emblemIdDrawable = resources.getIdentifier(clubEng.toLowerCase(), "drawable", "com.example.playerdirectoryar")
                    Log.d("emblemIdDrawable", emblemIdDrawable.toString())

                    val clubInfo = ClubInfo(clubName, clubEng, clubNum.toInt(), 0)
                    clubInfo.emblemIdDrawable = emblemIdDrawable
                    arrayListClubInfo.add(clubInfo)
                }
            }
            if (context != null) {
                arrayListClubInfo.sortBy { clubInfo: ClubInfo ->
                    clubInfo.clubNum
                }
                val clubGridAdapter = ClubGridAdapter(context!!)
                clubGridAdapter.setArrayListClubInfo(arrayListClubInfo)
                gridViewClubs?.adapter = clubGridAdapter

                progressBarHome?.visibility = View.GONE
            }
        }

        override fun onCancelled(p0: DatabaseError) {

        }
    }

    fun setOnHeadlineSelectedListener(callback: OnHeadlineSelectedListener) {
        this.callback = callback
    }

    // This interface can be implemented by the Activity, parent Fragment,
    // or a separate test implementation.
    interface OnHeadlineSelectedListener {
        fun onArticleSelected(position: Int, clubName: String)
        fun onSetTitle(title: String)
    }
}