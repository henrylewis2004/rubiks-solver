package com.henry.rubiksolver.Activities

import android.app.Activity
import android.content.Intent
import android.media.Image
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
import com.henry.rubiksolver.Cube
import com.henry.rubiksolver.R
import com.henry.rubiksolver.Solver
import com.henry.rubiksolver.databinding.ActivityMainBinding
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    var cube: Cube = Cube()
    val solverAgent: Solver = Solver()
    lateinit var algorithmText: TextView
    var algorithm: Array<String> = arrayOf()
    var moveCount: Int = 0


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

        val cubeFacex: Array<CharArray> = arrayOf(
            charArrayOf('g','o','y','w','w','g','r','g','w'),
            charArrayOf('w','g','y','b','b','w','y','y','o'),
            charArrayOf('g','r','o','r','r','b','b','r','b'),
            charArrayOf('b','y','r','o','g','y','w','g','o'),
            charArrayOf('g','w','r','b','o','o','g','b','y'),
            charArrayOf('b','o','o','y','y','r','w','w','r'),
        )
        val s = solverAgent.getAlgorithm(cubeFacex)

        findViewById<Button>(R.id.newCubeButton).setOnClickListener { //goto make new camera activity
            val newCubeIntent = Intent(this, NewCubeActivity::class.java)
            startForResult.launch(newCubeIntent)
        }

        findViewById<Button>(R.id.settingsButton).setOnClickListener { //goto settings
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }

        algorithmText = findViewById(R.id.cubeText)
        algorithmText.isVisible = false

        findViewById<ImageButton>(R.id.playButton).isVisible = false
        findViewById<ImageButton>(R.id.nextButton).isVisible = false
        findViewById<ImageButton>(R.id.previousButton).isVisible = false

        findViewById<ImageButton>(R.id.playButton).isClickable = false
        findViewById<ImageButton>(R.id.nextButton).isClickable = false
        findViewById<ImageButton>(R.id.previousButton).isClickable = false


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
        val al: Array<String> = solverAgent.getAlgorithm(cubeFace)
        algorithmText.text = algorithm[0]
        moveCount = 1


        algorithmInteraction()

    }

    private fun algorithmInteraction(): Unit{
        val ui: Array<ImageButton> = arrayOf(findViewById<ImageButton>(R.id.playButton),findViewById<ImageButton>(R.id.previousButton),findViewById<ImageButton>(R.id.nextButton) )

        for (button in ui){
            button.isVisible = true
            button.isClickable = true
        }

        ui[1].setOnClickListener{
            moveCount -=1
            algorithmText.text = algorithm[moveCount]
        }

        ui[2].setOnClickListener{
            algorithmText.text = algorithm[moveCount]
            moveCount++
        }



    }



    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

}