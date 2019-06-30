package com.example.playerdirectoryar

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_player_detail.*
import kotlinx.android.synthetic.main.content_player_detail.*
import java.time.Year

class PlayerDetailActivity : AppCompatActivity() {

    private lateinit var playerName: String
    private lateinit var playerEng: String
    private var playerSquadNumber: Long = 0
    private var playerHeight: Long = 0
    private var playerWeight: Long = 0
    private lateinit var playerPosition: String
    private var playerAge: Long = 0
    private var playerDominantFoot: String = "right"

    private var playerClubPath: String? = null

    private var clubName: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_detail)
        setSupportActionBar(toolbarPlayerDetailActivity)

        toolbarPlayerDetailActivity.setNavigationOnClickListener {
            finish()
        }

        playerName = intent.extras!![INTENT_KEY_PLAYER_NAME] as String
        toolbarPlayerDetailActivity.title = playerName

        playerClubPath = intent.extras!![INTENT_KEY_CLUB_PATH] as String?
        if (playerClubPath != null) {
            val playerPathRef = FirebaseDatabase.getInstance().reference
                .child(PATH_CLUB).child(playerClubPath!!).child(PATH_PLAYER)
            playerPathRef.addValueEventListener(valueEventListenerForPlayerInfo)
        } else {
            clubName = intent.extras!![INTENT_KEY_CLUB_NAME] as String?
            val playerPathRef = FirebaseDatabase.getInstance().reference
                .child(PATH_CLUB)
            playerPathRef.addValueEventListener(valueEventListenerForPlayersRef)
        }

        fabCamera.setOnClickListener {
            val intent = Intent(this, ArActivity::class.java)
            intent.putExtra(INTENT_KEY_PLAYER_NAME, playerName)
            intent.putExtra(INTENT_KEY_PLAYER_SQUAD_NUMBER, playerSquadNumber)
            intent.putExtra(INTENT_KEY_PLAYER_HEIGHT, playerHeight)
            intent.putExtra(INTENT_KEY_PLAYER_WEIGHT, playerWeight)
            intent.putExtra(INTENT_KEY_PLAYER_DOMINANT_FOOT, playerDominantFoot)
            intent.putExtra(INTENT_KEY_PLAYER_CLUB_PATH, playerClubPath)
            startActivity(intent)
        }
    }

    private val valueEventListenerForPlayersRef = object : ValueEventListener {
        override fun onDataChange(p0: DataSnapshot) {
            for (childClub in p0.children) {
                val mapClub = childClub.value as Map<*, *>?
                if (mapClub != null) {
                    val clubNameMap = mapClub["clubName"] as String
                    if (clubNameMap == clubName) { // e.g. playerRef -> fctokyo
                        playerClubPath = childClub.key as String
                    }
                }
            }
            val playerPathRef = FirebaseDatabase.getInstance().reference
                .child(PATH_CLUB).child(playerClubPath!!).child(PATH_PLAYER)
            playerPathRef.addValueEventListener(valueEventListenerForPlayerInfo)
        }

        override fun onCancelled(p0: DatabaseError) {

        }
    }

    private val valueEventListenerForPlayerInfo = object : ValueEventListener {
        override fun onDataChange(p0: DataSnapshot) {
            for (childPlayer in p0.children) {
                val mapPlayer = childPlayer.value as Map<*, *>?
                if (mapPlayer!!["playerName"] as String == playerName) {
                    playerSquadNumber = mapPlayer["squadNumber"] as Long
                    playerHeight = mapPlayer["height"] as Long
                    playerWeight = mapPlayer["weight"] as Long
                    playerPosition = mapPlayer["position"] as String
                    val birthYear = mapPlayer["birthYear"] as Long
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        playerAge = Year.now().toString().toLong() - birthYear
                    }
                    playerDominantFoot =
                        if (mapPlayer["dominantFoot"] == "") {
                            "right"
                        } else {
                            mapPlayer["dominantFoot"] as String
                        }
                    playerEng = mapPlayer["playerEng"] as String

                    textViewPlayerName.text = playerName
                    textViewSquadNumber.text = playerSquadNumber.toString()
                    textViewHeight.text = playerHeight.toString()
                    textViewWeight.text = playerWeight.toString()
                    textViewPosition.text = playerPosition
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        textViewAge.text = playerAge.toString()
                    } else {
                        textViewAgeKawashima.text = ""
                        textViewAge.text = ""
                    }
                    when (playerDominantFoot) {
                        "right" -> imageViewRightFoot.setImageResource(R.drawable.ic_foot_right)
                        "left" -> imageViewLeftFoot.setImageResource(R.drawable.ic_foot_left)
                    }
                    val imageRef = FirebaseStorage.getInstance().reference
                        .child("playerImages/$playerEng.jpg")

                    imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener { byteArray: ByteArray ->
                        if (byteArray.isNotEmpty()) {
                            val bitmapPlayer =
                                BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size).copy(Bitmap.Config.ARGB_8888, true)
                            imageViewPlayerDetail.setImageBitmap(bitmapPlayer)

                            progressBarPlayerDetail?.visibility = android.view.View.GONE
                        }
                    }.addOnFailureListener {
                        // Handle any errors
                    }
                }
            }
        }

        override fun onCancelled(p0: DatabaseError) {

        }
    }
}