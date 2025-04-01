package com.henry.rubiksolver.Activities

import android.app.Activity
import android.content.Intent
import android.graphics.ImageDecoder
import android.graphics.drawable.AnimatedImageDrawable
import android.media.Image
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.henry.rubiksolver.Algorithm
import com.henry.rubiksolver.Cube
import com.henry.rubiksolver.R
import com.henry.rubiksolver.Solver
import com.henry.rubiksolver.databinding.ActivityMainBinding
import org.w3c.dom.Text
import kotlin.math.log
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private var cube: Cube = Cube()
    private val solverAgent: Solver = Solver()
    private val algorithmHolder: Algorithm = Algorithm()
    lateinit var algorithmText: TextView
    private var mediaPlayer: MediaPlayer = MediaPlayer()
    private var moveCount: Int = 0



    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
            if (result.resultCode == Activity.RESULT_OK){
                val returnData = result.data?.getSerializableExtra("newCubeFace") as? Array<CharArray>

                try {
                    solveCube(returnData!!)
                } catch (e: Error) {
                    Log.d("Errors", e.message.toString())
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


       // playGif(R.raw.homecube)


        findViewById<Button>(R.id.newCubeButton).setOnClickListener { //goto make new camera activity
            val newCubeIntent = Intent(this, NewCubeActivity::class.java)
            startForResult.launch(newCubeIntent)
        }

        findViewById<Button>(R.id.settingsButton).setOnClickListener { //goto settings
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }

        algorithmText = findViewById(R.id.cubeText)


        val s = arrayOf(
            charArrayOf('o','b','g','w','w','o','r','r','r'),
            charArrayOf('y','o','b','y','b','b','g','o','y'),
            charArrayOf('o','y','w','w','r','g','g','r','o'),
            charArrayOf('y','y','w','g','g','b','b','b','w'),
            charArrayOf('r','o','w','y','o','r','y','w','g'),
            charArrayOf('r','g','b','r','y','g','b','w','o')
        )

        solveCube(s)

    }

    private fun getCubeString(cube: Cube): String{
        var s: String = ""
        val facesChar: CharArray = charArrayOf('w','b','r','g','o','y')

        for (face in cube.getCube()){
            for (square in face) {
                s += square
            }
            s += "\n"
        }
        return s

    }

    private fun solveCube(cubeFace: Array<CharArray>){
        algorithmHolder.setAlgorithm(solverAgent.getAlgorithm(cubeFace))
        algorithmText.text = algorithmHolder.getMove(0)
        algorithmInteraction()

    }

    private fun algorithmInteraction(): Unit{


        mediaPlayer.setOnCompletionListener {
            addCompletitionListener()
        }




        findViewById<ImageButton>(R.id.playButton).setOnClickListener{
            if (!mediaPlayer.isPlaying){
                val speakerName: String = "daniel"

                val move: String = algorithmHolder.getAbsoluteMove(algorithmHolder.getMoveCount())
                var audioFile: Int
                if (move.startsWith("rot_To_")){
                    audioFile = baseContext.resources.getIdentifier("${speakerName}_${move.lowercase()}_${Random.nextInt(1,4)}", "raw", baseContext.packageName)
                }
                else{
                    if(move.length>1) {
                        audioFile = baseContext.resources.getIdentifier("${speakerName}_${move[0]}_p_${Random.nextInt(1,6)}", "raw", baseContext.packageName)
                    }
                    else{
                        audioFile = baseContext.resources.getIdentifier("${speakerName}_${move[0]}_${Random.nextInt(1,6)}", "raw", baseContext.packageName)
                    }
                }

                try {
                    mediaPlayer.reset()
                    mediaPlayer = MediaPlayer.create(baseContext,audioFile)
                    mediaPlayer.setOnCompletionListener {addCompletitionListener()}
                    mediaPlayer.start()
                }
                catch(e:Exception){Log.d("cubesolve",e.message.toString())}

            }
            else{
                mediaPlayer.stop()
            }
        }



        findViewById<ImageButton>(R.id.previousButton).setOnClickListener{
            algorithmText.text = algorithmHolder.getPreviousMove()
        }

        findViewById<ImageButton>(R.id.nextButton).setOnClickListener{
            algorithmText.text = algorithmHolder.getNextMove()

            if (algorithmHolder.algorithmFinished()){
                finishedSolve()
            }
        }


    }





    private fun finishedSolve(): Unit{

    }


    private fun playGif(gifId: Int): Unit{
        runOnUiThread{
            val source = ImageDecoder.createSource(resources,R.id.gifView)
            val drawable = ImageDecoder.decodeDrawable(source)

            findViewById<ImageView>(R.id.gifView).setImageDrawable(drawable)
            if (drawable is AnimatedImageDrawable) {
                drawable.start()
            }


        }
    }

    private fun addCompletitionListener(){
        algorithmText.text = algorithmHolder.getNextMove()


        if (algorithmHolder.algorithmFinished()) {
            finishedSolve()
            mediaPlayer.release()
        }
        else{
            val speakerName: String = "daniel"
            val move: String = algorithmHolder.getAbsoluteMove(algorithmHolder.getMoveCount())
            val audioFile: Int
            if (move.startsWith("rot_To_")){
                audioFile = baseContext.resources.getIdentifier("${speakerName}_${move.lowercase()}_${Random.nextInt(1,4)}", "raw", baseContext.packageName)
            }
            else{
                Log.d("move",move)
                if (move.length>1){
                    audioFile = baseContext.resources.getIdentifier("${speakerName}_${move[0]}_p_${Random.nextInt(1,6)}", "raw", baseContext.packageName)
                }
                else{
                    audioFile = baseContext.resources.getIdentifier("${speakerName}_${move[0]}_${Random.nextInt(1,6)}", "raw", baseContext.packageName)
                }
            }

            try {
                mediaPlayer.reset()
                mediaPlayer = MediaPlayer.create(baseContext,audioFile)
                mediaPlayer.setOnCompletionListener { addCompletitionListener() }
                mediaPlayer.start()
            }
            catch(e:Exception){Log.d("cubesolve",e.message.toString())}
        }

    }



    override fun onDestroy() {
        mediaPlayer.release()
        super.onDestroy()
    }

    override fun onStop() {
        mediaPlayer.release()
        super.onStop()
    }

}