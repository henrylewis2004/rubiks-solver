package com.henry.rubiksolver.Activities

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.henry.rubiksolver.R

class NewCubeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_new_cube)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val colourList: CharArray = charArrayOf('w','b','r','g','o','y')

        for(i in 0..2 ){
            for (j in 0..2){
                val button: Button = (findViewById<TableLayout>(R.id.cubeFaceLayout).getChildAt(i) as TableRow).getChildAt(j) as Button
                button.setOnClickListener{
                    button.setBackgroundColor(Color.parseColor("#ffffff"))
                }
            }
        }

        findViewById<Button>(R.id.newCubeBackButton).setOnClickListener{
            finish()
        }
    }
}