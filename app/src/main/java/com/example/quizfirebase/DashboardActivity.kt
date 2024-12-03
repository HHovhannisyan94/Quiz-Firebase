package com.example.quizfirebase

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.quizfirebase.databinding.ActivityDashboardBinding
import com.google.firebase.database.*

class DashboardActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var countDownTimer: CountDownTimer
    var timeValue = 80
    lateinit var binding: ActivityDashboardBinding
    var questions: MutableList<Quiz> = ArrayList()
    lateinit var quiz: Quiz
    private var index = 0
    private var correctCount = 0
    private var wrongCount = 0
    private lateinit var databaseReference: DatabaseReference
    private var mTimerRunning = false
    private val START_TIME: Long = 20000
    private var mTimeLeftInMillis = START_TIME


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(
            layoutInflater
        )
        setContentView(binding.root)
        val layoutInflater = findViewById<View>(R.id.networkError)

        val checkNetworkConnection = NetworkConnection(this)
        checkNetworkConnection.observe(this) { isConnected: Boolean ->
            if (isConnected) {
                layoutInflater.visibility = View.GONE
                binding.mainContainer.visibility = View.VISIBLE
            } else {
                Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show()
                layoutInflater.visibility = View.VISIBLE
                binding.mainContainer.visibility = View.INVISIBLE
                if (mTimerRunning) {
                    pauseTimer()
                }
            }
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("Quiz")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children) {
                    quiz = dataSnapshot.getValue(Quiz::class.java)!!
                    questions.add(quiz)
                    setAllData()
                    startTimer()
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })



        binding.apply {
            card1.setOnClickListener(this@DashboardActivity)
            card2.setOnClickListener(this@DashboardActivity)
            card3.setOnClickListener(this@DashboardActivity)
            card4.setOnClickListener(this@DashboardActivity)
        }
    }

    fun setAllData() {
        binding.apply {
            txtQuestion.text = quiz.question
            cardOption1.text = quiz.option1
            cardOption2.text = quiz.option2
            cardOption3.text = quiz.option3
            cardOption4.text = quiz.option4

        }
    }

    private fun correct(cardView: CardView) {
        cardView.setCardBackgroundColor(Color.GREEN)
        binding.bttnNext.setOnClickListener {
            index++
            correctCount++
            quiz = questions[index]
            resetColor()
            setAllData()
            enableButton()
        }
    }

    private fun wrong(cardView: CardView) {
        cardView.setCardBackgroundColor(Color.RED)
        binding.bttnNext.setOnClickListener {
            wrongCount++
            if (index < questions.size - 1) {
                index++
                quiz = questions[index]
                resetColor()
                setAllData()
                enableButton()
            } else {
                result
            }
        }
    }

    private fun enableButton() {
        binding.apply {
            card1.isEnabled = true
            card2.isEnabled = true
            card3.isEnabled = true
            card4.isEnabled = true
        }
    }

    private fun disableButton() {
        binding.apply {
            card1.isEnabled = false
            card2.isEnabled = false
            card3.isEnabled = false
            card4.isEnabled = false
        }
    }

    private fun resetColor() {
        binding.bttnNext.isClickable = false

        binding.apply {
            card1.setCardBackgroundColor(resources.getColor(R.color.light_magenta))
            card2.setCardBackgroundColor(resources.getColor(R.color.light_magenta))
            card3.setCardBackgroundColor(resources.getColor(R.color.light_magenta))
            card4.setCardBackgroundColor(resources.getColor(R.color.light_magenta))
        }
    }

    private val result: Unit
        get() {
            if (mTimerRunning) {
                pauseTimer()
            }
            val intent = Intent(this@DashboardActivity, ResultActivity::class.java)
            intent.putExtra("correctCount", correctCount)
            intent.putExtra("wrongCount", wrongCount)
            startActivity(intent)
        }

    private fun checkAnswer(cardView: CardView, option: String) {
        disableButton()
        binding.bttnNext.isClickable = true
        if (option == quiz.answer) {
            cardView.setCardBackgroundColor(Color.GREEN)
            if (index < questions.size - 1) {
                correct(cardView)
            } else {
                correctCount++
                result
            }
        } else {
            wrong(cardView)
        }
    }


    private fun startTimer() {
        countDownTimer = object : CountDownTimer(START_TIME, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                mTimeLeftInMillis = millisUntilFinished
                timeValue -= 1
                binding.progressBar.progress = timeValue
            }

            override fun onFinish() {
                mTimerRunning = false
                val dialogFragment = GaveOverFragment()
                try {
                    val ft = supportFragmentManager.beginTransaction()
                    ft.add(dialogFragment, "GaveOver")
                    ft.commitAllowingStateLoss()
                } catch (ignored: IllegalStateException) {
                    ignored.printStackTrace()
                }
            }
        }.start()

        mTimerRunning = true
    }

    private fun pauseTimer() {
        countDownTimer.cancel()
        mTimerRunning = false
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.card_1 -> checkAnswer(binding.card1, quiz.option1)
            R.id.card_2 -> checkAnswer(binding.card2, quiz.option2)
            R.id.card_3 -> checkAnswer(binding.card3, quiz.option3)
            R.id.card_4 -> checkAnswer(binding.card4, quiz.option4)
        }
    }
}