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
    lateinit var algorithm: Array<String>
    var moveCount: Int = 0


    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
            if (result.resultCode == Activity.RESULT_OK){
                val returnData = result.data?.getSerializableExtra("newCubeFace") as? Array<CharArray>

                try {
                    Log.d("cubesolve", "mainHere")
                    solveCube(returnData!!)
                    Log.d("cubesolve", "mainHere2")
                } catch (e: Error) {
                    Log.d("Errors", e.message.toString())
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        findViewById<Button>(R.id.newCubeButton).setOnClickListener { //goto make new camera activity
            val newCubeIntent = Intent(this, NewCubeActivity::class.java)
            startForResult.launch(newCubeIntent)
        }

        findViewById<Button>(R.id.settingsButton).setOnClickListener { //goto settings
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }

        algorithmText = findViewById(R.id.cubeText)

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
        algorithm = solverAgent.getAlgorithm(cubeFace)
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