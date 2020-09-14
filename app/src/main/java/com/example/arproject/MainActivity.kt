package com.example.arproject

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View


import androidx.appcompat.app.AppCompatActivity
import com.google.ar.core.HitResult
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.assets.RenderableSource
import com.google.ar.sceneform.collision.Plane
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.activity_main.*

/*
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.assets.RenderableSource
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.ViewRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.activity_main.*
*/
class MainActivity : AppCompatActivity() {
    private lateinit var fragment: ArFragment
    private var testRenderable: ModelRenderable? = null
    lateinit var modelUri: Uri


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fragment = supportFragmentManager.findFragmentById(R.id.sceneform_fragmenttt) as ArFragment
      //  modelUri = Uri.parse("https://github.com/KhronosGroup/glTF-Sample-Models/tree/master/2.0/Duck/glTF/Duck.gltf")
        modelUri = Uri.parse("https://github.com/KhronosGroup/glTF-Sample-Models/blob/master/2.0/CesiumMan/glTF/CesiumMan.gltf")

        mainBTN.setOnClickListener {
            addObject()
        }



        val renderableFuture = ModelRenderable.builder()
            .setSource(this, RenderableSource.builder().setSource(
                this,
                modelUri,
                RenderableSource.SourceType.GLTF2)
                .setScale(0.2f) // Scale the original mode l to 20%.
                .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                .build())
            .setRegistryId("CesiumMan")
            .build()
    renderableFuture.thenAccept { testRenderable = it }
        renderableFuture.exceptionally {
            Log.d("faill","$it")
            return@exceptionally null

        }


    }
    private fun addObject() {
        val frame = fragment.arSceneView.arFrame
        val pt = getScreenCenter()
        val hits: List<HitResult>
        if (frame != null && testRenderable != null) {
            hits = frame.hitTest(pt.x.toFloat(), pt.y.toFloat())
            for (hit in hits) {
                val trackable = hit.trackable
                if (trackable is Plane) {
                    val anchor = hit!!.createAnchor()
                    val anchorNode = AnchorNode(anchor)
                    anchorNode.setParent(fragment.arSceneView.scene)
                    val mNode = TransformableNode(fragment.transformationSystem)
                    mNode.setParent(anchorNode)
                    mNode.renderable = testRenderable
                    mNode.select()
                    break
                } } } }

    private fun getScreenCenter(): android.graphics.Point {
        val vw = findViewById<View>(android.R.id.content)
        return android.graphics.Point(vw.width / 2, vw.height / 2)
    }
}