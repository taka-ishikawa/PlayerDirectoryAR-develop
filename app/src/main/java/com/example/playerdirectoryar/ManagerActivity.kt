package com.example.playerdirectoryar

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_manager.*

class ManagerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager)

        buttonGo.setOnClickListener {

            // write what to do

//            val fctokyoPlayerRef = FirebaseDatabase.getInstance().reference
//                .child(PATH_CLUB).child("fctokyo").child("player")
//            val arrayListFcTokyo = arrayListOf<Player>()
//            // GK
//            arrayListFcTokyo.add(Player("林彰洋", "はやしあきひろ", "Akihiro Hayashi",
//                33, "GK", 195F, 91F, "right", 1987))
//            // DF
//            arrayListFcTokyo.add(Player("室屋成", "むろやせい", "Sei Muroya",
//                2, "DF", 176F, 69F, "", 1994))
//            arrayListFcTokyo.add(Player("森重真人", "もりしげまさと", "Masato Morishige",
//                3, "DF", 183F, 78F, "right", 1987))
//            arrayListFcTokyo.add(Player("太田宏介", "おおたこうすけ", "Kosuke Ota",
//                6, "DF", 178F, 78F, "", 1987))
//            arrayListFcTokyo.add(Player("小川諒也", "おがわりょうや", "Ryoya Ogawa",
//                25, "DF", 183F, 78F, "", 1996))
//            // MF
//            arrayListFcTokyo.add(Player("高萩洋次郎", "たかはぎようじろう", "Yojiro Takahagi",
//                8, "MF", 183F, 69F, "right",1986))
//            arrayListFcTokyo.add(Player("東慶悟", "ひがしけいご", "Keigo Higashi",
//                10, "MF", 178F, 72F, "right",1990))
//            arrayListFcTokyo.add(Player("久保建英", "くぼたけふさ", "Takefusa Kubo",
//                15, "MF", 173F, 67F, "left",2001))
//            arrayListFcTokyo.add(Player("ナ・サンホ", "なさんほ", "Na Sangho",
//                17, "MF", 173F, 70F, "",1996))
//            arrayListFcTokyo.add(Player("大森晃太郎", "おおもりこうたろう", "Kotaro Omori",
//                39, "MF", 170F, 70F, "", 1992))
//            // FW
//            arrayListFcTokyo.add(Player("ディエゴ・オリヴェイラ", "でぃえごおりゔぇいら", "Diego Oliveira",
//                9, "FW", 179F, 78F, "", 1990))
//            arrayListFcTokyo.add(Player("永井謙佑", "ながいけんすけ", "Kensuke Nagai",
//                11, "FW", 178F, 71F, "",1989))
//
//            for (player in arrayListFcTokyo) {
//                fctokyoPlayerRef.push().setValue(player)
//            }

//            val visselkobePlayerRef = FirebaseDatabase.getInstance().reference
//                .child(PATH_CLUB).child("visselkobe").child("player")
//            val arrayListPlayer = arrayListOf<Player>()
//
//            arrayListPlayer.add(Player("前川黛也", "まえかわだいや", "Daiya Maekawa", 1,
//                "GK", 191F, 86F, "", 1994))
//            arrayListPlayer.add(Player("キム・スンギュ", "きむすんぎゅ", "Kim Seung Gyu",18,
//                "GK", 187F, 84F, "", 1990))
//
//            arrayListPlayer.add(Player("渡辺博文", "わたなべひろふみ", "Hirofumi Watanabe", 3,
//                "DF", 186F, 77F, "", 1987))
//            arrayListPlayer.add(Player("ダンクレー", "だんくれー", "Dankler",33,
//                "DF", 187F, 80F, "", 1992))
//            arrayListPlayer.add(Player("西大伍", "にしだいご", "Daigo Nishi", 22,
//                "DF", 178F, 74F, "right", 1987))
//
//            arrayListPlayer.add(Player("山口蛍", "やまぐちほたる", "Hotaru Yamaguchi",5,
//                "MF", 173F, 72F, "right", 1990))
//            arrayListPlayer.add(Player("セルジ・サンペール", "せるじさんぺーる", "Sergi Samper",6,
//                "MF", 182F, 73F, "right", 1995))
//            arrayListPlayer.add(Player("アンドレス・イニエスタ", "あんどれすいにえすた", "Andres Iniesta",8,
//                "MF", 171F, 68F, "right", 1984))
//            arrayListPlayer.add(Player("三田啓貴",  "みたひろたか", "Hirotaka Mita", 14,
//                "MF", 173F, 63F, "left", 1990))
//
//            arrayListPlayer.add(Player("ダビド・ビジャ", "だびどびじゃ","David Villa", 7,
//                "FW", 174F, 68F, "right", 1981))
//            arrayListPlayer.add(Player("ルーカス・ポドルスキ", "るーかすぽどるすき", "Lukas Podolski", 10,
//                "FW", 180F, 80F, "left", 1985))
//            arrayListPlayer.add(Player("古橋亨梧", "ふるはしきょうご", "Kyogo Furuhashi", 16,
//                "FW", 170F, 63F, "right", 1995))
//            arrayListPlayer.add(Player("ウェリントン", "うぇりんとん", "Wellington",17,
//                "FW", 188F, 90F, "right", 1988))
//
//            for (player in arrayListPlayer) {
//                visselkobePlayerRef.push().setValue(player)
//            }

//            val arrayClubs = arrayOf(
//            getString(R.string.item_club_1),
//            getString(R.string.item_club_2),
//            getString(R.string.item_club_3),
//            getString(R.string.item_club_4),
//            getString(R.string.item_club_5),
//            getString(R.string.item_club_6),
//            getString(R.string.item_club_7),
//            getString(R.string.item_club_8),
//            getString(R.string.item_club_9),
//            getString(R.string.item_club_10),
//            getString(R.string.item_club_11),
//            getString(R.string.item_club_12),
//            getString(R.string.item_club_13),
//            getString(R.string.item_club_14),
//            getString(R.string.item_club_15),
//            getString(R.string.item_club_16),
//            getString(R.string.item_club_17),
//            getString(R.string.item_club_18))

//            val firebaseDatabase = FirebaseDatabase.getInstance()
//            firebaseDatabase.reference.child(PATH_CLUB_INFO).push().
//                setValue(ClubInfo(getString(R.string.item_club_1), 1, 7))
//            firebaseDatabase.reference.child(PATH_CLUB_INFO).push().
//                setValue(ClubInfo(getString(R.string.item_club_2), 2, 16))
//            firebaseDatabase.reference.child(PATH_CLUB_INFO).push().
//                setValue(ClubInfo(getString(R.string.item_club_3), 3, 5))
//            firebaseDatabase.reference.child(PATH_CLUB_INFO).push().
//                setValue(ClubInfo(getString(R.string.item_club_4), 4, 10))
//            firebaseDatabase.reference.child(PATH_CLUB_INFO).push().
//                setValue(ClubInfo(getString(R.string.item_club_5), 5, 7))
//            firebaseDatabase.reference.child(PATH_CLUB_INFO).push().
//                setValue(ClubInfo(getString(R.string.item_club_6), 6, 7))
//            firebaseDatabase.reference.child(PATH_CLUB_INFO).push().
//                setValue(ClubInfo(getString(R.string.item_club_7), 7, 7))
//            firebaseDatabase.reference.child(PATH_CLUB_INFO).push().
//                setValue(ClubInfo(getString(R.string.item_club_8), 8, 7))
//            firebaseDatabase.reference.child(PATH_CLUB_INFO).push().
//                setValue(ClubInfo(getString(R.string.item_club_9), 9, 7))
//            firebaseDatabase.reference.child(PATH_CLUB_INFO).push().
//                setValue(ClubInfo(getString(R.string.item_club_10), 10, 7))
//            firebaseDatabase.reference.child(PATH_CLUB_INFO).push().
//                setValue(ClubInfo(getString(R.string.item_club_11), 11, 7))
//            firebaseDatabase.reference.child(PATH_CLUB_INFO).push().
//                setValue(ClubInfo(getString(R.string.item_club_12), 12, 7))
//            firebaseDatabase.reference.child(PATH_CLUB_INFO).push().
//                setValue(ClubInfo(getString(R.string.item_club_13), 13, 7))
//            firebaseDatabase.reference.child(PATH_CLUB_INFO).push().
//                setValue(ClubInfo(getString(R.string.item_club_14), 14, 7))
//            firebaseDatabase.reference.child(PATH_CLUB_INFO).push().
//                setValue(ClubInfo(getString(R.string.item_club_15), 15, 7))
//            firebaseDatabase.reference.child(PATH_CLUB_INFO).push().
//                setValue(ClubInfo(getString(R.string.item_club_16), 16, 7))
//            firebaseDatabase.reference.child(PATH_CLUB_INFO).push().
//                setValue(ClubInfo(getString(R.string.item_club_17), 17, 7))
//            firebaseDatabase.reference.child(PATH_CLUB_INFO).push().
//                setValue(ClubInfo(getString(R.string.item_club_18), 18, 7))

//            val clubRef = firebaseDatabase.getReference(PATH_CLUB)
//            var arrayListPlayer = arrayListOf<Player>()
//
//            val club = Club("FC東京", null)
//            clubRef.child("fctokyo").setValue(club)
//
//            arrayListPlayer.add(Player("久保建英", "くぼたけふさ", 15, "MF", 173F, 67F, 2001))
//            arrayListPlayer.add(Player("森重真人", "もりしげまさと", 3, "DF", 183F, 78F, 1987))
//            arrayListPlayer.add(Player("林彰洋", "はやしあきひろ", 33, "GK", 195F, 91F, 1987))
//            arrayListPlayer.add(Player("東慶悟", "ひがしけいご", 10, "MF", 178F, 72F, 1990))
//            arrayListPlayer.add(Player("ディエゴ・オリヴェイラ", "でぃえごおりゔぇいら", 9, "FW", 179F, 78F, 1990))
//
//            for (playerNum in arrayListPlayer) {
//                clubRef.child("fctokyo").child(PATH_PLAYER).push().setValue(playerNum)
//            }
//
//            val club = Club("ヴィッセル神戸", null)
//            clubRef.child("visselkobe").setValue(club)
//
//            arrayListPlayer.add(Player("前川黛也", "まえかわだいや", 1, "GK", 191F, 86F, 1994))
//            arrayListPlayer.add(Player("キム・スンギュ", "きむすんぎゅ", 18, "GK", 187F, 84F, 1990))
//            arrayListPlayer.add(Player("渡辺博文", "わたなべひろふみ", 3, "DF", 186F, 77F, 1987))
//            arrayListPlayer.add(Player("ダンクレー", "だんくれー", 33, "DF", 187F, 80F, 1992))
//            arrayListPlayer.add(Player("西大伍", "にしだいご", 22, "DF", 178F, 74F, 1987))
//            arrayListPlayer.add(Player("山口蛍", "やまぐちほたる", 5, "MF", 173F, 72F, 1990))
//            arrayListPlayer.add(Player("セルジ・サンペール", "せるじさんぺーる", 6, "MF", 182F, 73F, 1995))
//            arrayListPlayer.add(Player("アンドレス・イニエスタ", "あんどれすいにえすた", 8, "MF", 171F, 68F, 1984))
//            arrayListPlayer.add(Player("三田啓貴",  "みたひろたか", 14, "MF", 173F, 63F, 1990))
//            arrayListPlayer.add(Player("ダビド・ビジャ", "だびどびじゃ", 7, "FW", 174F, 68F, 1981))
//            arrayListPlayer.add(Player("ルーカス・ポドルスキ", "るーかすぽどるすき", 10, "FW", 180F, 80F, 1985))
//            arrayListPlayer.add(Player("古橋亨梧", "ふるはしきょうご", 16, "FW", 170F, 63F, 1995))
//            arrayListPlayer.add(Player("ウェリントン", "うぇりんとん", 17, "FW", 188F, 90F, 1988))
//
//            for (playerNum in arrayListPlayer) {
//                clubRef.child("visselkobe").child(PATH_PLAYER).push().setValue(playerNum)
//            }

            // push/ sync players in PATH_PLAYER n players in PATH_CLUB
//            val clubRef = FirebaseDatabase.getInstance().reference.child(PATH_CLUB)
//            clubRef.addValueEventListener(valueEventListenerForPlayerRef)
        }

        buttonBackToHome.setOnClickListener {
            finish()
        }
    }

    val valueEventListenerForPlayerRef = object : ValueEventListener {
        override fun onDataChange(p0: DataSnapshot) {
            for (childClub in p0.children) {
                val mapClub = childClub.value as Map<*, *>?
                val clubName = mapClub!!["clubName"] as String
                // you have to change this part: "FC東京", "ヴィッセル神戸", or so
                if (clubName == "FC東京") {
                    val clubPath = childClub.key!!
                    val playerRef = FirebaseDatabase.getInstance().reference
                        .child(PATH_CLUB).child(clubPath).child(PATH_PLAYER)
                    playerRef.addValueEventListener(valueEventListenerForPushingToPlayerPath)
                }
            }
        }
        override fun onCancelled(p0: DatabaseError) {

        }
    }

    val valueEventListenerForPushingToPlayerPath = object : ValueEventListener {
        override fun onDataChange(p0: DataSnapshot) {
            // you have to sync clubName above/ below
            val clubName = "FC東京"
            for (childPlayer in p0.children) {
                val mapPlayer = childPlayer.value as Map<*, *>?
                val playerUid = childPlayer.key
//                Log.d("key", childPlayer.key)
                if (mapPlayer != null) {
                    val playerName = mapPlayer["playerName"] as String
                    val playerPron = mapPlayer["playerPron"] as String
                    val playerEng = mapPlayer["playerEng"] as String
                    val squadNumber = mapPlayer["squadNumber"] as Long

                    val playerForSearch = PlayerForSearch(playerName, playerPron, playerEng, squadNumber.toInt(), clubName)

                    val playerForSearchRef = FirebaseDatabase.getInstance().reference
                        .child(PATH_ALL_PLAYERS).child(playerUid!!)
                    playerForSearchRef.setValue(playerForSearch)
                }
            }
        }

        override fun onCancelled(p0: DatabaseError) {

        }
    }

//    val childEventListener = object : ChildEventListener {
//        override fun onChildAdded(p0: DataSnapshot, p1: String?) {
//            val
//        }
//
//        override fun onChildChanged(p0: DataSnapshot, p1: String?) {
//
//        }
//
//        override fun onChildMoved(p0: DataSnapshot, p1: String?) {
//
//        }
//
//        override fun onChildRemoved(p0: DataSnapshot) {
//
//        }
//
//        override fun onCancelled(p0: DatabaseError) {
//
//        }
//
//    }
}
