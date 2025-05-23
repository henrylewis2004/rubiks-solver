package com.henry.rubiksolver.Activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.henry.rubiksolver.R
import com.henry.rubiksolver.faces

class NewCubeActivity : AppCompatActivity() {
    val colourIds: IntArray = intArrayOf(R.color.white,R.color.blue,R.color.red,R.color.green,R.color.orange,R.color.yellow)
    var displaySquareArray: Array<ImageView> = arrayOf()
    var cubeFace: Array<CharArray> = arrayOf(charArrayOf(), charArrayOf(),charArrayOf(),charArrayOf(),charArrayOf(),charArrayOf())

    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if (result.resultCode == Activity.RESULT_OK){
            val returnData = result.data?.getIntArrayExtra("cubeFaceResult")

            try {
                val x = returnData
                fillFace(returnData!!)

            }
            catch(e: Exception){
                Toast.makeText(baseContext,"exception: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_new_cube)

        val nextFaceButton: Button = findViewById<Button>(R.id.nextFaceButton)
        val instructionText: TextView = findViewById<TextView>(R.id.newCubeInstructionText)
        nextFaceButton.isEnabled = false

        displaySquareArray = arrayOf()
        var faceCount: Int = 0

        setCubeSqaureButton(findViewById<ImageView>(R.id.cubeFace00))
        setCubeSqaureButton(findViewById<ImageView>(R.id.cubeFace01))
        setCubeSqaureButton(findViewById<ImageView>(R.id.cubeFace02))
        setCubeSqaureButton(findViewById<ImageView>(R.id.cubeFace10))
        setCubeSqaureButton(findViewById<ImageView>(R.id.cubeFace11))
        setCubeSqaureButton(findViewById<ImageView>(R.id.cubeFace12))
        setCubeSqaureButton(findViewById<ImageView>(R.id.cubeFace20))
        setCubeSqaureButton(findViewById<ImageView>(R.id.cubeFace21))
        setCubeSqaureButton(findViewById<ImageView>(R.id.cubeFace22))


        nextFaceButton.setOnClickListener{

            var faceArray: CharArray = charArrayOf()
            for (square in displaySquareArray){
                faceArray += getColour(square.getTag(square.id) as Int)
                square.setColorFilter(ContextCompat.getColor(this,R.color.default_background))
                square.setTag(square.id,-1)
            }

            cubeFace[faceCount] = faceArray

            if (faceCount.equals(5)){
                setResult(Activity.RESULT_OK, Intent().putExtra("newCubeFace", cubeFace))
                finish()
            }

            faceCount += 1

            if (faceCount.equals(5)){
                nextFaceButton.setText("Solve!")
                instructionText.setText("")
            }

            when (faceCount){
                0 -> instructionText.setText("WHITE face with GREEN face on bottom")
                1 -> instructionText.setText("BLUE face with WHITE face on bottom")
                2 -> instructionText.setText("RED face with GREEN face on bottom")
                3 -> instructionText.setText("GREEN face with YELLOW face on bottom")
                4 -> instructionText.setText("ORANGE face with GREEN face on bottom")
                5 -> instructionText.setText("YELLOW face with GREEN face on bottom")
            }
            nextFaceButton.isEnabled = false
        }

        findViewById<Button>(R.id.newCubeBackButton).setOnClickListener{
            finish()
        }

        findViewById<Button>(R.id.newCubeCamButton).setOnClickListener {
            val camIntent = Intent(this, NewCubeCamActivity::class.java)
            startForResult.launch(camIntent)
        }
    }


    private fun setCubeSqaureButton(square: ImageView): Unit{
        square.setTag(square.id,-1)

        square.setColorFilter(ContextCompat.getColor(this,R.color.default_background))

        square.setOnClickListener {
            when(square.getTag(square.id)){
                5 -> square.setTag(square.id, faces.WHITE.ordinal)
                else -> square.setTag(square.id,square.getTag(square.id) as Int + 1)
            }

            square.setColorFilter(ContextCompat.getColor(this,colourIds[square.getTag(square.id) as Int]))
            findViewById<Button>(R.id.nextFaceButton).isEnabled = faceFull()

        }


        displaySquareArray += square
    }

    private fun fillFace(face: IntArray): Unit{
        for (i in 0..8){
            if(face[i] > -1) {
                val square = displaySquareArray[i]
                square.setTag(square.id, face[i])
                square.setColorFilter(ContextCompat.getColor(this, colourIds[face[i]]))
            }

        }

        findViewById<Button>(R.id.nextFaceButton).isEnabled = faceFull()

    }


    private fun faceFull(): Boolean{
        for (square in displaySquareArray){
            if (square.getTag(square.id) as Int == -1){
                return false
            }
        }
        return true
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
