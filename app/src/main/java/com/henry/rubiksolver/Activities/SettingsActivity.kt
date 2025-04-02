package com.henry.rubiksolver.Activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.Switch
import android.widget.Toast
import android.widget.ToggleButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.henry.rubiksolver.R

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)


        findViewById<Button>(R.id.settingsBackButton).setOnClickListener{ //back button
               finish() //close activity
        }


        val darkModeButton: Switch = findViewById<Switch>(R.id.darkModeButton)


        darkModeButton.setOnCheckedChangeListener{_, isChecked->
            if (darkModeButton.isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                darkModeButton.text = "Disable dark mode"
            }
            else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                darkModeButton.text = "Enable dark mode"
            }
        }

        val permissionButton: ToggleButton = findViewById<ToggleButton>(R.id.permissionsButton)
        permissionButton.isChecked = hasRequiredPermissions()

        permissionButton.setOnClickListener {
            if(!hasRequiredPermissions()){
                requestPermissions()
            }
            permissionButton.isChecked = hasRequiredPermissions()
        }


    }

    private fun hasRequiredPermissions(): Boolean{
        return SettingsActivity.CAMERAX_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(applicationContext,it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermissions(): Unit{
        ActivityCompat.requestPermissions(this, SettingsActivity.CAMERAX_PERMISSIONS,0)

    }

    companion object{
        private val CAMERAX_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA
        )
    }
}