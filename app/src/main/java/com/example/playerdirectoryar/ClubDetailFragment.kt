package com.example.playerdirectoryar

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_club_detail.*

class ClubDetailFragment : Fragment() {

    private var callback: OnHeadlineSelectedListener? = null

    private lateinit var clubName: String
    private lateinit var pathClub: String

    private lateinit var playerListAdapter: PlayerListAdapter
    private lateinit var arrayListPlayers : ArrayList<Player>
    private lateinit var arrayListGk : ArrayList<Player>
    private lateinit var arrayListDf : ArrayList<Player>
    private lateinit var arrayListMf : ArrayList<Player>
    private lateinit var arrayListFw : ArrayList<Player>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_club_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clubName = arguments!!.getString(INTENT_KEY_CLUB_NAME)!!

        val clubRef = FirebaseDatabase.getInstance().reference.child(PATH_CLUB)
        clubRef.addValueEventListener(valueEventListenerForPlayerRef)

        listViewClubDetail.setOnItemClickListener { adapterView, view, position, id ->
            if (arrayListPlayers[position].playerEng == "") {
                adapterView.selectedView?.isEnabled = false
            } else {
                val intent = Intent(context, PlayerDetailActivity::class.java)
                intent.putExtra(INTENT_KEY_CLUB_PATH, pathClub)
                intent.putExtra(INTENT_KEY_PLAYER_NAME, arrayListPlayers[position].playerName)
                startActivity(intent)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        callback?.onSetTitle(clubName)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        callback!!.onSetTitle(getString(R.string.title_app))
    }

    private val valueEventListenerForPlayerRef = object : ValueEventListener {
        override fun onDataChange(p0: DataSnapshot) {
            for (childClub in p0.children) {
                val mapClub = childClub.value as Map<*, *>?
                if (mapClub != null) {
                    val clubNameMap = mapClub["clubName"] as String
                    if (clubNameMap == clubName) { // e.g. playerRef -> fctokyo
                        pathClub = childClub.key as String
                        val playerPathRef = FirebaseDatabase.getInstance().reference
                            .child(PATH_CLUB).child(pathClub).child(PATH_PLAYER)
                        playerPathRef.addValueEventListener(valueEventListenerForPlayers)
                    }
                }
            }
        }

        override fun onCancelled(p0: DatabaseError) {

        }
    }

    private val valueEventListenerForPlayers = object : ValueEventListener {
        override fun onDataChange(p0: DataSnapshot) {
            arrayListPlayers = arrayListOf()
            arrayListGk = arrayListOf()
            arrayListDf = arrayListOf()
            arrayListMf = arrayListOf()
            arrayListFw = arrayListOf()
            for (childPlayer in p0.children) {
                val mapPlayer = childPlayer.value as Map<*, *>?
                if (mapPlayer != null) {
                    val playerName = mapPlayer["playerName"] as String
                    val playerEng = mapPlayer["playerEng"] as String
                    val squadNumber = mapPlayer["squadNumber"] as Long
                    val position = mapPlayer["position"] as String
                    val intPosition =
                        when (position) {
                            "GK" -> 0
                            "DF" -> 1
                            "MF" -> 2
                            "FW" -> 3
                            else -> null
                        }

                    val imageRef = FirebaseStorage.getInstance().reference
                        .child("playerImages/$playerEng.jpg")

                    imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener { byteArray: ByteArray ->
                        // Data for "images/island.jpg" is returned, use this as needed
                        if (byteArray.isNotEmpty()) {
                            val bitmapPlayer = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                                .copy(Bitmap.Config.ARGB_8888, true)
                            val player = Player(
                                playerName,
                                "",
                                playerEng,
                                squadNumber.toInt(),
                                intPosition.toString(),
                                0F,
                                0F,
                                "",
                                0
                            )
                            player.bitmapPlayer = bitmapPlayer
                            when (position) {
                                "GK" -> {
                                    if (arrayListGk.isEmpty()) {
                                        val gk = Player("GK", "", "", 0, "0", 0F, 0F, "", 0)
                                        arrayListGk.add(gk)
                                        arrayListPlayers.add(gk)
                                    }
                                    arrayListGk.add(player)
                                    arrayListPlayers.add(player)
                                }
                                "DF" -> {
                                    if (arrayListDf.isEmpty()) {
                                        val df = Player("DF", "", "", 0, "1", 0F, 0F, "", 0)
                                        arrayListDf.add(df)
                                        arrayListPlayers.add(df)
                                    }
                                    arrayListDf.add(player)
                                    arrayListPlayers.add(player)
                                }
                                "MF" -> {
                                    if (arrayListMf.isEmpty()) {
                                        val mf = Player("MF", "", "", 0, "2", 0F, 0F, "", 0)
                                        arrayListMf.add(mf)
                                        arrayListPlayers.add(mf)
                                    }
                                    arrayListMf.add(player)
                                    arrayListPlayers.add(player)
                                }
                                "FW" -> {
                                    if (arrayListFw.isEmpty()) {
                                        val fw = Player("FW", "", "", 0, "3", 0F, 0F, "", 0)
                                        arrayListFw.add(fw)
                                        arrayListPlayers.add(fw)
                                    }
                                    arrayListFw.add(player)
                                    arrayListPlayers.add(player)
                                }
                            }
                        }
                    }.addOnFailureListener {
                        // Handle any errors
                    }.addOnCompleteListener {
                        if (context != null) {
                            arrayListPlayers.sortBy { player: Player ->
                                player.squadNumber
                            }
                            arrayListPlayers.sortBy { player: Player ->
                                player.position // this position is intPosition: 0(GK), 1(DF), 2(MF), 3(FW)
                            }
                            playerListAdapter = PlayerListAdapter(context!!, clubName)
                            playerListAdapter.setArrayListPlayers(arrayListPlayers)
                            listViewClubDetail?.adapter = playerListAdapter

                            progressBarClubDetail?.visibility = View.GONE
                        }
                    }
                }
            }
        }

        override fun onCancelled(p0: DatabaseError) {

        }
    }

    fun updateClubView() {
        callback?.onSetTitle(clubName)
    }

    fun setOnHeadlineSelectedListener(callback: OnHeadlineSelectedListener) {
        this.callback = callback
    }

    interface OnHeadlineSelectedListener {
        fun onReplaceFragment(fragment: Fragment)
        fun onSetTitle(title: String)
    }
}