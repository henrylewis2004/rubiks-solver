package com.henry.rubiksolver

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.henry.rubiksolver.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var cube: Cube = Cube()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        findViewById<Button>(R.id.newCubeButton).setOnClickListener {
            cube.newCubeFace(9)
            val s: String = getCubeFaceString(cube)
            findViewById<TextView>(R.id.cubeText).text = s
        }
        findViewById<Button>(R.id.rTurn).setOnClickListener {
            cube.rTurn(false,0,1)
            val s: String = getCubeFaceString(cube)
            findViewById<TextView>(R.id.cubeText).text = s
        }



    }

    private fun getCubeFaceString(cube: Cube): String{
        var s: String = ""

        for (face in cube.getCube()){
            for (square in face) {
                s += square
            }
            s += "\n"
        }
        return s

    }

    override fun onStart() {
        super.onStart()
        Log.d("test", "hello")
    }

    override fun onResume() {
        super.onResume()
        print("here")
    }

}