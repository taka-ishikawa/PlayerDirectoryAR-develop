package com.example.playerdirectoryar

import android.content.Intent
import android.graphics.Bitmap
import android.icu.text.SimpleDateFormat
import android.media.SoundPool
import android.net.Uri
import android.os.Bundle
import android.os.Environment.DIRECTORY_PICTURES
import android.os.Environment.getExternalStoragePublicDirectory
import android.os.Handler
import android.os.HandlerThread
import android.support.design.widget.Snackbar
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.PixelCopy
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.ar.core.Anchor
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.collision.Box
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.Renderable
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.activity_ar.*
import kotlinx.android.synthetic.main.content_ar.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.File.separator
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class ArActivity : AppCompatActivity() {

    private lateinit var fragment: ArFragment
    private var anchorPlayer: Anchor? = null
    private var anchorNodePlayer: AnchorNode? = null

    private var playerName: String? = null
    private var playerSquadNumber: Long = 0
    private var playerHeight: Long = 0
    private var playerWeight: Long = 0
    private var playerDominantFoot: String? = null
    private var playerClubPath: String? = null

    private lateinit var soundPool: SoundPool
    private var soundShutter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ar)
        setSupportActionBar(toolbarAr)

        toolbarAr.setNavigationOnClickListener {
            finish()
        }

        // get player info
        if (intent.extras == null) {
            playerName = "アンドレス・イニエスタ"
            playerSquadNumber = 8
            playerHeight = 171
            playerWeight = 68
            playerDominantFoot = "right"
            playerClubPath = "visselkobe"
        } else {
            playerName = intent.extras!![INTENT_KEY_PLAYER_NAME] as String
            playerSquadNumber = intent.extras!![INTENT_KEY_PLAYER_SQUAD_NUMBER] as Long
            playerHeight = intent.extras!![INTENT_KEY_PLAYER_HEIGHT] as Long
            playerWeight = intent.extras!![INTENT_KEY_PLAYER_WEIGHT] as Long
            playerDominantFoot = intent.extras!![INTENT_KEY_PLAYER_DOMINANT_FOOT] as String
            playerClubPath = intent.extras!![INTENT_KEY_PLAYER_CLUB_PATH] as String
        }

        fragment = arFragment as ArFragment
        var gateKeeperObject = 0
        fragment.setOnTapArPlaneListener { hitResult, plane, _ ->
            if (plane.type != Plane.Type.HORIZONTAL_UPWARD_FACING || gateKeeperObject > 0) {
                return@setOnTapArPlaneListener
            } else {
                anchorPlayer = hitResult.createAnchor()

                placeObject(fragment, anchorPlayer!!, Uri.parse("$playerClubPath.sfb"))
                gateKeeperObject += 1

                fabCard.show()
            }
        }

        soundPool = SoundPool.Builder().build()
        soundShutter = soundPool.load(this, R.raw.sound_shutter, 1)

        fabCamera.setOnClickListener {
            takePhoto()
            soundPool.play(soundShutter, 1.0f, 1.0f, 0, 0, 1.0f)
        }

        // setting flat card
        val cardHeight: Float = playerHeight / 173F
        fabCard.hide()
        fabCard.setOnClickListener {
            placeCardObject(anchorPlayer!!, cardHeight)
            fabCard.hide()
        }
    }

    private fun placeObject(fragment: ArFragment, anchor: Anchor, uriModel: Uri) {
        ModelRenderable.builder()
            .setSource(fragment.context, uriModel)
            .build()
            .thenAccept {
                addNodeToScene(fragment, anchor, it)
            }
            .exceptionally {
                val builder = AlertDialog.Builder(this)
                builder.setMessage(it.message).setTitle("Error")
                val dialog = builder.create()
                dialog.show()
                return@exceptionally null
            }
    }

    private fun placeCardObject(anchor: Anchor, cardHeight: Float) {
        val currentAnchor: Anchor = anchor  // できれば
        ViewRenderable.builder()
            .setView(fragment.context, R.layout.renderable_flat_card_simplized)
            .setSizer { Vector3(1.0F, 2.0F * cardHeight, 1.0F) }
            .build()
            .thenAccept { renderable ->
                val textViewPlayerName =
                    renderable.view.findViewById<android.view.View>(R.id.textViewPlayerName) as TextView
                textViewPlayerName.text = "$playerName  $playerSquadNumber"
                val textViewPlayerHeight =
                    renderable.view.findViewById<android.view.View>(R.id.textViewPlayerHeight) as TextView
                textViewPlayerHeight.text = "$playerHeight[cm]"
                val textViewPlayerWeight =
                    renderable.view.findViewById<android.view.View>(R.id.textViewPlayerWeight) as TextView
                textViewPlayerWeight.text = "$playerWeight[kg]"
                when (playerDominantFoot) {
                    "left" -> {
                        val imageViewFootLeft =
                            renderable.view.findViewById<android.view.View>(R.id.imageViewLeftFootCard) as ImageView
                        imageViewFootLeft.visibility = android.view.View.VISIBLE
                    }
                    "right" -> {
//                        val imageViewFootRight =
//                            renderable.view.findViewById<android.view.View>(R.id.imageViewRightFootCard) as ImageView
//                        imageViewFootRight.visibility = android.view.View.VISIBLE
                        val imageViewFootLeft =
                            renderable.view.findViewById<android.view.View>(R.id.imageViewLeftFootCard) as ImageView
                        imageViewFootLeft.visibility = android.view.View.VISIBLE
                        imageViewFootLeft.setImageResource(R.drawable.ic_foot_right)
                    }
                }
                addNodeToScene(fragment, currentAnchor, renderable)
            }
            .exceptionally { throwable ->
                val builder = AlertDialog.Builder(this)
                builder.setMessage(throwable.message)
                    .setTitle("error!")
                val dialog = builder.create()
                dialog.show()
                return@exceptionally null
            }
    }

    private fun addNodeToScene(fragment: ArFragment, anchor: Anchor, renderable: Renderable) {
        if (anchorNodePlayer == null) {
            anchorNodePlayer = AnchorNode(anchor)
            val nodePlayer = TransformableNode(fragment.transformationSystem)
            // setting scale of the model
            val boundingBox = renderable.collisionShape as Box
            val boundingBoxSize = boundingBox.size
            val maxExtent = Math.max(boundingBoxSize.x, Math.max(boundingBoxSize.y, boundingBoxSize.z))
            val targetSize = 1.8f * (playerHeight / 173f) // 1.8f is the base size: 1.8f -> 173[cm]
            val scale = targetSize / maxExtent
            nodePlayer.localScale = Vector3.one().scaled(scale)
            nodePlayer.renderable = renderable
            nodePlayer.setParent(anchorNodePlayer)
            fragment.arSceneView.scene.addChild(anchorNodePlayer)
        } else {
            val nodeCard = TransformableNode(fragment.transformationSystem)
            nodeCard.renderable = renderable
            nodeCard.setParent(anchorNodePlayer)
            fragment.arSceneView.scene.addChild(anchorNodePlayer)
        }
    }

    private fun generateFilename(): String {
        val date = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())
        return getExternalStoragePublicDirectory(DIRECTORY_PICTURES).toString() +
                separator + "Sceneform/" + date + "_screenshot.jpg"
    }

    @Throws(IOException::class)
    private fun saveBitmapToDisk(bitmap: Bitmap, filename: String) {

        val out = File(filename)
        if (!out.parentFile.exists()) {
            out.parentFile.mkdirs()
        }
        try {
            FileOutputStream(filename).use { outputStream ->
                ByteArrayOutputStream().use { outputData ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputData)
                    outputData.writeTo(outputStream)
                    outputStream.flush()
                    outputStream.close()
                }
            }
        } catch (ex: IOException) {
            throw IOException("Failed to save bitmap to disk", ex)
        }
    }

    private fun takePhoto() {
        val filename = generateFilename()
        val view = fragment.arSceneView

        // Create a bitmap the size of the scene view.
        val bitmap = Bitmap.createBitmap(
            view.width, view.height,
            Bitmap.Config.ARGB_8888
        )

        // Create a handler thread to offload the processing of the image.
        val handlerThread = HandlerThread("PixelCopier")
        handlerThread.start()
        // Make the request to copy.
        PixelCopy.request(view, bitmap, { copyResult ->
            if (copyResult == PixelCopy.SUCCESS) {
                try {
                    saveBitmapToDisk(bitmap, filename)
                } catch (e: IOException) {
                    val toast = Toast.makeText(
                        this, e.toString(),
                        Toast.LENGTH_LONG
                    )
                    toast.show()
                    return@request
                }

                val snackbar = Snackbar.make(
                    findViewById(android.R.id.content),
                    "Photo saved", Snackbar.LENGTH_LONG
                )
                snackbar.setAction("Open in Photos") { v ->
                    val photoFile = File(filename)

                    val photoURI = FileProvider.getUriForFile(
                        this,
                        this.packageName + ".android.fileprovider",
                        photoFile
                    )
                    val intent = Intent(Intent.ACTION_VIEW, photoURI)
                    intent.setDataAndType(photoURI, "image/*")
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    startActivity(intent)
                }
                snackbar.show()
            } else {
                val toast = Toast.makeText(
                    this,
                    "Failed to copyPixels: $copyResult", Toast.LENGTH_LONG
                )
                toast.show()
            }
            handlerThread.quitSafely()
        }, Handler(handlerThread.looper))
    }
}