package com.henry.rubiksolver.Activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.henry.rubiksolver.Cube
import com.henry.rubiksolver.R
import com.henry.rubiksolver.databinding.ActivityMainBinding
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var cube: Cube = Cube()

    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
            if (result.resultCode == Activity.RESULT_OK){
                val returnData = result.data?.getSerializableExtra("newCubeFace") as? Array<CharArray>

                try {
                    cube.setCubeFaces(returnData!!)
                    findViewById<TextView>(R.id.cubeText).text = getCubeString(cube)
                }
                catch(e: Error){
                    Log.d("resultData", e.message!!)
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

        findViewById<Button>(R.id.rTurn).setOnClickListener {
            cube.rTurn(false,0,1)
            val s: String = getCubeString(cube)
            findViewById<TextView>(R.id.cubeText).text = s
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

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

}