package com.henry.rubiksolver.Activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.ImageDecoder
import android.graphics.drawable.AnimatedImageDrawable
import android.media.Image
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
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

        playGif(R.raw.cubehome)

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
            charArrayOf('b','b','r','w','w','g','o','w','b'),
            charArrayOf('r','b','y','o','b','o','y','y','g'),
            charArrayOf('w','y','g','r','r','r','w','y','r'),
            charArrayOf('w','r','o','g','g','g','y','r','b'),
            charArrayOf('y','w','o','o','o','g','b','o','g'),
            charArrayOf('o','w','g','b','y','b','w','y','r')
        )





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
        playGif(R.raw.glep_rubiks)
        try {
            mediaPlayer = MediaPlayer()
            algorithmHolder.setAlgorithm(solverAgent.getAlgorithm(cubeFace))
            algorithmText.text = "Cube Solved"
            algorithmText.text = algorithmHolder.getMove(0)
            algorithmInteraction()
        }
        catch(e:Exception){
            Log.d("solvecube",e.message.toString())
        }


    }

    private fun algorithmInteraction(): Unit{
        playSound(baseContext,algorithmHolder.getAbsoluteMove(algorithmHolder.getMoveCount()))

        findViewById<ImageButton>(R.id.playButton).setOnClickListener{
            if (!algorithmHolder.algorithmFinished()){
                if (!mediaPlayer.isPlaying){
                    playSound(baseContext,algorithmHolder.getAbsoluteMove(algorithmHolder.getMoveCount()))
                }
                else{
                    mediaPlayer.stop()
                    findViewById<ImageButton>(R.id.playButton).setImageResource(android.R.drawable.ic_media_play)
                    window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                    //drawable = resources.getDrawable(R.drawable.)
                    //findViewById<ImageButton>(R.id.playButton).
                }
            }
        }



        findViewById<ImageButton>(R.id.previousButton).setOnClickListener{
            algorithmText.text = algorithmHolder.getPreviousMove()
            playSound(baseContext,algorithmHolder.getAbsoluteMove(algorithmHolder.getMoveCount()))

        }

        findViewById<ImageButton>(R.id.nextButton).setOnClickListener{
            algorithmText.text = algorithmHolder.getNextMove()

            if (algorithmHolder.algorithmFinished()){
                finishedSolve()
            }
            else{
                playSound(baseContext,algorithmHolder.getAbsoluteMove(algorithmHolder.getMoveCount()))
            }
        }


    }





    private fun finishedSolve(): Unit{
        algorithmText.text = "SOLVED CUBE! CONGRATULATIONS!"
        findViewById<ImageButton>(R.id.playButton).isEnabled = false
        findViewById<ImageButton>(R.id.previousButton).isEnabled = false
        findViewById<ImageButton>(R.id.nextButton).isEnabled = false

        findViewById<ImageButton>(R.id.playButton).setImageResource(android.R.drawable.ic_media_play)
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        playGif(R.raw.rubiks_solved)



    }


    private fun playGif(gifId: Int): Unit{
        try{
            runOnUiThread{
                val source = ImageDecoder.createSource(resources,gifId)
                val drawable = ImageDecoder.decodeDrawable(source)

                findViewById<ImageView>(R.id.gifView).setImageDrawable(drawable)
                if (drawable is AnimatedImageDrawable) {
                    drawable.start()
                }


            }
        }
        catch(e:Exception){Toast.makeText(baseContext,e.message,Toast.LENGTH_SHORT).show()}
    }

    private fun addCompletitionListener(){
        if (algorithmHolder.algorithmFinished()) {
            finishedSolve()
            mediaPlayer.release()
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
        else {
            algorithmText.text = algorithmHolder.getNextMove()
            val move: String = algorithmHolder.getAbsoluteMove(algorithmHolder.getMoveCount())
            playSound(baseContext, move)
        }
    }


    private fun playSound(context: Context, move: String): Unit{
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        findViewById<ImageButton>(R.id.playButton).setImageResource(android.R.drawable.ic_media_pause)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val speakerName: String = "daniel"
        val audioFile: Int
        if (move.startsWith("rot_To_")){
            audioFile = baseContext.resources.getIdentifier("${speakerName}_${move.lowercase()}_${Random.nextInt(1,4)}", "raw", baseContext.packageName)
        }
        else{
            if (move.length>1){
                audioFile = baseContext.resources.getIdentifier("${speakerName}_${move[0]}_p_${Random.nextInt(1,6)}", "raw", baseContext.packageName)
            }
            else{
                audioFile = baseContext.resources.getIdentifier("${speakerName}_${move[0]}_${Random.nextInt(1,6)}", "raw", baseContext.packageName)
            }
        }

        try {
            mediaPlayer.reset()
            mediaPlayer = MediaPlayer.create(context,audioFile)
            mediaPlayer.setOnCompletionListener { addCompletitionListener() }
            mediaPlayer.start()
        }
        catch(e:Exception){Log.d("cubesolve",e.message.toString())}

    }



    override fun onDestroy() {
        mediaPlayer.release()
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        super.onDestroy()
    }

    override fun onStop() {
        mediaPlayer.reset()
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        super.onStop()
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.reset()
    }
}