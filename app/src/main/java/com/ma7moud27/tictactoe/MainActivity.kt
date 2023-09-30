package com.ma7moud27.tictactoe

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import com.google.android.material.textfield.TextInputEditText


class MainActivity : AppCompatActivity() {

    private lateinit var singleButton: Button
    private lateinit var multiButton: Button
    private lateinit var settingsButton: Button
    private lateinit var playerName:String
    private lateinit var opponentName:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initComponents()

        singleButton.setOnClickListener { startPlay(true) }
        multiButton.setOnClickListener { startPlay(false) }
        settingsButton.setOnClickListener { startActivity(Intent(this,SettingActivity::class.java)) }
    }

    private fun initComponents() {
        singleButton = findViewById(R.id.main_single_btn)
        multiButton = findViewById(R.id.main_multi_btn)
        settingsButton = findViewById(R.id.main_setting_btn)
    }


    private fun startPlay(computer: Boolean) {
        if (computer){
            playerName = getString(R.string.you)
            opponentName = getString(R.string.com)
            navigateToPlay(computer)
        }
        else{
            Dialog(this).apply {
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window?.setGravity(Gravity.CENTER);
                setContentView(R.layout.name_dialog)


                val yesBtn = this.findViewById(R.id.name_save_button) as Button
                yesBtn.setOnClickListener {
                    playerName =
                        this.findViewById<TextInputEditText>(R.id.name_player1_et).text.toString()
                    opponentName =
                        this.findViewById<TextInputEditText>(R.id.name_player2_et).text.toString()
                    this.dismiss()
                    navigateToPlay(computer)
                }
                show()
            }
        }
    }
    private fun navigateToPlay(computer:Boolean) {
        startActivity(Intent(this, PlaygroundActivity::class.java).apply {
            putExtra(Constants.PLAYER_NAME.name, playerName)
            putExtra(Constants.OPPONENT_NAME.name, opponentName)
            putExtra(Constants.COMPUTER.name, computer)
        })
    }
}