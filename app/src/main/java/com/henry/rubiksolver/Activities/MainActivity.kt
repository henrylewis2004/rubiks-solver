package com.henry.rubiksolver.Activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
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

    var debugMode: Boolean = false
    var cube: Cube = Cube()
    val solverAgent: Solver = Solver()

    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
            if (result.resultCode == Activity.RESULT_OK){
                val returnData = result.data?.getSerializableExtra("newCubeFace") as? Array<CharArray>

                try {
                    cube.setCubeFaces(returnData!!)
                    findViewById<TextView>(R.id.cubeText).text = getCubeString(cube)
                    solveCube()
                } catch (e: Error) {
                    Log.d("Errors", e.message.toString())
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        solveCube()


        findViewById<Button>(R.id.newCubeButton).setOnClickListener { //goto make new camera activity
            val newCubeIntent = Intent(this, NewCubeActivity::class.java)
            startForResult.launch(newCubeIntent)
        }

        findViewById<Button>(R.id.settingsButton).setOnClickListener { //goto settings
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }




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

    private fun solveCube(){
        findViewById<ImageView>(R.id.playButton).isVisible = true
        val cubeFace = arrayOf(
            charArrayOf('g','o','w','b','w','b','o','w','r'),
            charArrayOf('r','r','g','g','b','y','o','g','g'),
            charArrayOf('o','o','y','r','r','g','g','r','b'),
            charArrayOf('y','o','w','o','g','y','w','b','r'),
            charArrayOf('w','y','y','w','o','w','b','b','b'),
            charArrayOf('r','w','b','r','y','g','y','y','o')
        )
        val cubeFace2 = arrayOf(
            charArrayOf('y','y','r','r','w','g','o','r','r'),
            charArrayOf('r','g','b','g','b','y','b','o','y'),
            charArrayOf('g','r','y','r','r','b','g','w','o'),
            charArrayOf('b','b','w','o','g','b','o','g','g'),
            charArrayOf('w','o','r','o','o','w','y','b','w'),
            charArrayOf('o','y','b','y','y','w','w','w','g')
        )
        val crashCube = arrayOf(
            charArrayOf('b','y','o','r','w','g','r','o','r'),
            charArrayOf('b','b','y','g','b','y','w','r','b'),
            charArrayOf('w','g','g','o','r','w','y','b','b'),
            charArrayOf('g','y','g','g','g','w','o','o','y'),
            charArrayOf('y','r','r','r','o','w','g','w','w'),
            charArrayOf('o','y','r','o','y','b','o','b','w')
        )
        val finalSolveCube = arrayOf(
            charArrayOf('w','w','w','w','w','w','w','w','w'),
            charArrayOf('b','r','r','b','b','b','b','b','b'),
            charArrayOf('r','r','g','r','r','o','r','r','g'),
            charArrayOf('g','g','g','g','g','g','b','g','o'),
            charArrayOf('o','o','o','b','o','o','r','o','o'),
            charArrayOf('y','y','y','y','y','y','y','y','y'),
        )
        val finalSolveCube2 = arrayOf(
            charArrayOf('w','w','w','w','w','w','w','w','w'),
            charArrayOf('b','b','b','b','b','b','b','b','b'),
            charArrayOf('r','r','r','r','r','g','r','r','r'),
            charArrayOf('g','g','g','g','g','g','g','o','g'),
            charArrayOf('o','o','o','r','o','o','o','o','o'),
            charArrayOf('y','y','y','y','y','y','y','y','y'),
        )
        val al: Array<String> = solverAgent.getAlgorithm(cubeFace)
        //findViewById<TextView>(R.id.cubeText).text = s

    }


    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

}