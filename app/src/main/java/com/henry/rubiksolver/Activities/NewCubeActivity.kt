package com.henry.rubiksolver.Activities

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.ColorFilter
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.children
import com.henry.rubiksolver.R
import com.henry.rubiksolver.faces
import org.w3c.dom.Text
import kotlin.math.log

class NewCubeActivity : AppCompatActivity() {
    val colourIds: IntArray = intArrayOf(R.color.white,R.color.blue,R.color.red,R.color.green,R.color.orange,R.color.yellow)
    var displaySquareArray: Array<ImageView> = arrayOf()
    var cubeFace: Array<CharArray> = arrayOf(charArrayOf(), charArrayOf(),charArrayOf(),charArrayOf(),charArrayOf(),charArrayOf())

    val resultIntent: Intent = Intent()

    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
        if (result.resultCode == Activity.RESULT_OK){
            val returnData = result.data?.getSerializableExtra("cubeFacePic") as? Array<CharArray>

            try {
            }
            catch(e: Error){
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_new_cube)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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
                resultIntent.putExtra("newCubeFace", cubeFace)
                setResult(Activity.RESULT_OK, resultIntent)
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
