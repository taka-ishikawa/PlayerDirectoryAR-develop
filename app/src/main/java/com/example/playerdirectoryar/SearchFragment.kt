package com.example.playerdirectoryar

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.SearchView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_search.*


class SearchFragment : Fragment(), SearchView.OnQueryTextListener {

    private var callback: OnHeadlineSelectedListener? = null

    private var searchView: SearchView? = null
    private var searchWord: String = ""
    private var arrayListSearch = ArrayList<Player>()

    private var namePlayerSelected: String = ""
    private var clubPlayerSelected: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        listViewSearch.visibility = View.INVISIBLE

        imageViewRamosSearch.setOnClickListener {
            textViewRamosSearch.text = "VAMOS! RAMOS!"
        }
        setListViewSearch()

        listViewSearch.setOnItemClickListener { parent, view, position, id ->
            namePlayerSelected = arrayListSearch[position].playerName
            val playerRef = FirebaseDatabase.getInstance().reference.child(PATH_ALL_PLAYERS)
            playerRef.addValueEventListener(valueEventListenerForIntentToPlayerDetailActivity)
        }
    }

    override fun onResume() {
        super.onResume()

        callback?.onSetTitle(getString(R.string.title_search))
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.appbar_search, menu)
        searchView = menu?.findItem(R.id.item_search_view)?.actionView as SearchView
        searchView!!.queryHint = getString(R.string.query_hint)
        searchView!!.setOnQueryTextListener(this)
        searchView!!.isSubmitButtonEnabled = false
        return super.onCreateOptionsMenu(menu, inflater)
    }

    // this method is NOT called for now
    override fun onQueryTextChange(newText: String?): Boolean {
//        setListViewSearch()
        imageViewRamosSearch.visibility = View.INVISIBLE
        textViewRamosSearch.text = ""
        listViewSearch.visibility = View.VISIBLE
        return false
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query == "ラモス瑠偉") {
            imageViewRamosSearch.visibility = View.VISIBLE
            listViewSearch.visibility = View.INVISIBLE
        } else {
            setListViewSearch()
        }
        return false
    }

    private fun setListViewSearch() {
        imageViewRamosSearch.visibility = View.INVISIBLE
        textViewRamosSearch.text = ""
        progressBarSearch.visibility = View.VISIBLE
        arrayListSearch = arrayListOf()

        searchWord =
            if (searchView == null) {
                ""
            } else {
                searchView!!.query.toString()
            }
        val playerPath = FirebaseDatabase.getInstance().reference.child(PATH_ALL_PLAYERS)
        playerPath.addValueEventListener(valueEventListenerForSearchList)
    }

    private val valueEventListenerForSearchList = object : ValueEventListener {
        override fun onDataChange(p0: DataSnapshot) {
            for (childPlayer in p0.children) {
                val mapPlayer = childPlayer.value as Map<*, *>?
                if (mapPlayer != null) {
                    val playerName = mapPlayer["playerName"] as String
                    val playerPron = mapPlayer["playerPron"] as String
                    val playerEng = mapPlayer["playerEng"] as String
                    val clubName = mapPlayer["clubName"] as String
                    val squadNumber = mapPlayer["squadNumber"] as Long

                    val imageRef = FirebaseStorage.getInstance().reference
                        .child("playerImages/$playerEng.jpg")

                    imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener { byteArray: ByteArray ->
                        // Data for "images/island.jpg" is returned, use this as needed
                        if (byteArray.isNotEmpty()) {
                            val bitmapPlayer = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                                .copy(Bitmap.Config.ARGB_8888, true)
                            val player = Player(
                                playerName,
                                playerPron,
                                playerEng,
                                squadNumber.toInt(),
                                "",
                                0F,
                                0F,
                                "",
                                0
                            )
                            player.bitmapPlayer = bitmapPlayer
                            player.clubName = clubName
                            if (playerName.contains(searchWord) || playerPron.contains(searchWord) || clubName.contains(searchWord)) {
                                arrayListSearch.add(player)
                                progressBarSearch?.visibility = View.INVISIBLE
                            }
                        }
                    }.addOnFailureListener {

                    }.addOnCompleteListener {
                        if (context != null) {
                            arrayListSearch.sortBy {player: Player ->
                                player.clubName
                            }
                            val playerListAdapter = PlayerListAdapter(context!!, null)
                            playerListAdapter.setArrayListPlayers(arrayListSearch)
                            listViewSearch?.adapter = playerListAdapter

                            if (imageViewRamosSearch.visibility == View.INVISIBLE) {
                                listViewSearch?.visibility = View.VISIBLE
                            }
                        }
                    }
                }
            }
        }

        override fun onCancelled(p0: DatabaseError) {

        }
    }

    private val valueEventListenerForIntentToPlayerDetailActivity = object : ValueEventListener {
        override fun onDataChange(p0: DataSnapshot) {
            for (childPlayer in p0.children) {
                val mapPlayer = childPlayer.value as Map<*, *>?
                if (mapPlayer != null) {
                    val playerName = mapPlayer["playerName"] as String
                    if (playerName == namePlayerSelected) {
                        clubPlayerSelected = mapPlayer["clubName"] as String
                    }
                }
            }
            val intent = Intent(context, PlayerDetailActivity::class.java)
            intent.putExtra(INTENT_KEY_PLAYER_NAME, namePlayerSelected)
            intent.putExtra(INTENT_KEY_CLUB_NAME, clubPlayerSelected)
            startActivity(intent)
        }

        override fun onCancelled(p0: DatabaseError) {

        }
    }

    fun setOnHeadlineSelectedListener(callback: OnHeadlineSelectedListener) {
        this.callback = callback
    }

    interface OnHeadlineSelectedListener {
        fun onSetTitle(title: String)
    }
}