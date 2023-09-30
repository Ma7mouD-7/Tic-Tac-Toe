package com.ma7moud27.tictactoe

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.HandlerCompat.postDelayed
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit
import java.util.logging.Handler

class PlaygroundActivity : AppCompatActivity() {
    private lateinit var playGround: List<TextView>
    private lateinit var playerNameTextView: TextView
    private lateinit var opponentNameTextView: TextView
    private lateinit var playerScoreTextView: TextView
    private lateinit var opponentScoreTextView: TextView
    private lateinit var resultTextView: TextView
    private lateinit var newRoundButton: Button


    private var isMyTurn = true
    private var isComputer: Boolean = false
    private lateinit var opponentName: String
    private var playerScore: Int = 0
    private var opponentScore: Int = 0
    private lateinit var playerName :String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playground)
        initComponents()
        setupGame()

        playGround.forEachIndexed { index, cell ->
            cell.setOnClickListener { handleClick(index) }
        }
        newRoundButton.setOnClickListener { setupGame() }
    }

    private fun initComponents() {
        playGround = listOf(
            findViewById(R.id.play_layout_1_tv),
            findViewById(R.id.play_layout_2_tv),
            findViewById(R.id.play_layout_3_tv),
            findViewById(R.id.play_layout_4_tv),
            findViewById(R.id.play_layout_5_tv),
            findViewById(R.id.play_layout_6_tv),
            findViewById(R.id.play_layout_7_tv),
            findViewById(R.id.play_layout_8_tv),
            findViewById(R.id.play_layout_9_tv)
        )
        playerNameTextView = findViewById(R.id.play_player_name_tv)
        opponentNameTextView = findViewById(R.id.play_opponent_name_tv)
        playerScoreTextView = findViewById(R.id.play_player_score_tv)
        opponentScoreTextView = findViewById(R.id.play_opponent_score_tv)
        resultTextView = findViewById(R.id.play_result_tv)
        newRoundButton = findViewById(R.id.play_new_btn)

        playerName = this.intent.getStringExtra(Constants.PLAYER_NAME.name) ?: "Unknown"
        opponentName = this.intent.getStringExtra(Constants.OPPONENT_NAME.name) ?: "Unknown"
        isComputer = this.intent.getBooleanExtra(Constants.COMPUTER.name,false)

        playerNameTextView.text = playerName
        opponentNameTextView.text = opponentName
        playerScoreTextView.text = playerScore.toString()
        opponentScoreTextView.text = opponentScore.toString()

        playerScore = 0
        opponentScore = 0
    }

    private fun setupGame() {
        playGround.forEach{
            it.text = " "
            it.isEnabled = true
        }
        resultTextView.text = "Draw!"
        resultTextView.setTextColor(ContextCompat.getColor(this,R.color.font))
        newRoundButton.visibility = View.INVISIBLE
        resultTextView.visibility = View.INVISIBLE
        toggleName(isMyTurn)

        if(isComputer && !isMyTurn)computerMove()
    }

    private fun handleClick(index: Int) {
        playGround[index].text = if (isMyTurn) "X" else "O"
        playGround[index].setTextColor(if (isMyTurn) ContextCompat.getColor(this,R.color.player_1_font) else ContextCompat.getColor(this,R.color.player_2_font))
        playGround[index].isEnabled = false
        isMyTurn = !isMyTurn
        toggleName(isMyTurn)

        val endGame = endGame(isEnd(), playGround.none { it.isEnabled })

        if(isComputer && !isMyTurn &&!endGame)computerMove()

    }

    private fun computerMove() {
        android.os.Handler(Looper.getMainLooper()).postDelayed({
            handleClick((playGround.mapIndexed { index, textView -> index to textView }.filter { it.second.isEnabled }.map { it.first }).random())
            TimeUnit.MILLISECONDS.sleep(300)
        }, 10)
    }

    private fun toggleName(isMyTurn: Boolean) {
        if (isMyTurn) {
            playerNameTextView.setTextColor(ContextCompat.getColor(this,R.color.player_1_font))
            opponentNameTextView.setTextColor(ContextCompat.getColor(this,R.color.font))
        } else {
            opponentNameTextView.setTextColor(ContextCompat.getColor(this,R.color.player_2_font))
            playerNameTextView.setTextColor(ContextCompat.getColor(this,R.color.font))
        }
    }
    
    private fun isEnd():Boolean {
        val rows = playGround.chunked(3)
            .fold(false) {acc,list -> acc or list.all { it.text == "X" } or  list.all { it.text == "O" } }

        val cols = playGround.chunked(3)
            .transpose()
            .fold(false) {acc,list -> acc or list.all { it.text == "X" } or  list.all { it.text == "O" } }

        val dia = playGround.chunked(3)
            .let { listOf(it.indices.map { i -> it[i][i] }, it.indices.map { i -> it[i][3 - i - 1] }) }
            .fold(false) {acc,list -> acc or list.all { it.text == "X" } or  list.all { it.text == "O" } }

        return rows or cols or dia
    }

    private fun <T> List<List<T>>.transpose(): List<List<T>> {
        return if (isEmpty()) emptyList()
        else List(this[0].size) { i -> this.map { it[i] } }
    }


    private fun endGame(isEnd:Boolean, isFinished: Boolean) :Boolean {
        if(isEnd){
            if (!isMyTurn){
                resultTextView.text = if(isComputer) "$playerName Win" else "$playerName Wins"
                playerScoreTextView.text = (++playerScore).toString()
                resultTextView.setTextColor(ContextCompat.getColor(this,R.color.player_1_font))
            }
            else {
                resultTextView.text = if(isComputer) "$playerName Lose" else "$opponentName Wins"
                opponentScoreTextView.text = (++opponentScore).toString()
                resultTextView.setTextColor(ContextCompat.getColor(this,R.color.player_2_font))

            }
            playGround.forEach{ it.isEnabled = false }
        }
        if (isFinished || isEnd) {
            newRoundButton.visibility = View.VISIBLE
            resultTextView.visibility = View.VISIBLE
            playerNameTextView.setTextColor(Color.GRAY)
            opponentNameTextView.setTextColor(Color.GRAY)
            return true
        }
        return false
    }
}