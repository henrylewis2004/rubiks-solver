package com.henry.rubiksolver.Activities

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.ColorFilter
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.children
import com.henry.rubiksolver.R
import com.henry.rubiksolver.faces
import kotlin.math.log

class NewCubeActivity : AppCompatActivity() {
    val colourIds: IntArray = intArrayOf(R.color.white,R.color.blue,R.color.red,R.color.green,R.color.orange,R.color.yellow)
    var buttonArray: Array<ImageButton> = arrayOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_new_cube)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        buttonArray = arrayOf()
        var faceCount: Int = 0






        setCubeSqaureButton(findViewById<ImageButton>(R.id.cubeFace00))

        findViewById<Button>(R.id.newCubeBackButton).setOnClickListener{
            finish()
        }
        findViewById<Button>(R.id.nextFaceButton).setOnClickListener{
            if (faceCount.equals(5)){
                finish()
            }

            faceCount += 1
            var face: CharArray = charArrayOf()

            for (button in buttonArray){
                face += getColour(button.getTag(button.id) as Int)
            }

            if (faceCount.equals(5)){
                findViewById<Button>(R.id.nextFaceButton).setText("Solve!")
            }


        }
    }


    private fun setCubeSqaureButton(button: ImageButton): Unit{
        button.setTag(button.id,faces.WHITE.ordinal)

        button.setOnClickListener {
            if (button.getTag(button.id) as Int > 4){button.setTag(button.id,faces.WHITE.ordinal)}
            else{button.setTag(button.id, button.getTag(button.id) as Int + 1)}

            button.setColorFilter(ContextCompat.getColor(this,findNextColour(button.getTag(button.id) as Int)))
        }
        buttonArray += button
    }


    private fun findNextColour(colour: Int): Int{
       when(colour){
           faces.YELLOW.ordinal -> return colourIds[0]
           else -> return colourIds[colour + 1]
       }
    }

    private fun getColour(colour: Int): Char{
        when(colour){
            faces.WHITE.ordinal -> return 'w'
            faces.BLUE.ordinal -> return 'b'
            faces.RED.ordinal -> return 'r'
            faces.GREEN.ordinal -> return 'g'
            faces.ORANGE.ordinal -> return 'o'
            faces.YELLOW.ordinal -> return 'y'
        }
        return 'x'
    }


}
