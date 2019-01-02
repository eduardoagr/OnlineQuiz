package com.example.ed.onlinequiz.Controller

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.ed.onlinequiz.Model.ParseUtility
import com.example.ed.onlinequiz.Model.Plant
import com.example.ed.onlinequiz.R
import com.squareup.picasso.Callback
import com.squareup.picasso.MemoryPolicy.NO_CACHE
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var correctAnswerIndex: Int = 0
    private var correctPlant: Plant? = null

//    private val OPEN_CAMERA_REQUEST1_CODE = 100
//    private val OPEN_GALLERY_REQUEST_CODE = 200

    private var numberOfAnswerCorrectly = 0
    private var numberOfAnswerIncorrectly = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        pBarVisibility(false)

        displayUIWidget(false)
//        main_btnOpenCamera?.setOnClickListener(this)
//        main_btnViewGallry?.setOnClickListener(this)

        main_button1?.setOnClickListener(this)
        main_button2?.setOnClickListener(this)
        main_button3?.setOnClickListener(this)
        main_button4?.setOnClickListener(this)
        main_flb?.setOnClickListener(this)

        YoYo.with(Techniques.Pulse)
            .duration(700)
            .repeat(5)
            .playOn(main_flb)

    }

    override fun onClick(v: View) = when (v.id) {
        R.id.main_button1 -> specifyAnswer(0)
        R.id.main_button2 -> specifyAnswer(1)
        R.id.main_button3 -> specifyAnswer(2)
        R.id.main_button4 -> specifyAnswer(3)
        R.id.main_flb -> reset()

        else -> Toast.makeText(this, "this should never be hit", Toast.LENGTH_LONG).show()
    }
//        R.id.main_btnOpenCamera -> {
//            startActivityForResult(
//                Intent(MediaStore.ACTION_IMAGE_CAPTURE),
//                OPEN_CAMERA_REQUEST_CODE
//            )
//        }
//        R.id.main_btnViewGallry -> {
//            startActivityForResult(
//                Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI),
//                OPEN_GALLERY_REQUEST_CODE
//            )
//        }
//        else -> Toast.makeText(this, "this should never be hit", Toast.LENGTH_LONG).show()
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        when (requestCode) {
//            OPEN_CAMERA_REQUEST_CODE -> {
//                if (resultCode == Activity.RESULT_OK) {
//                    val cameraContent = data?.extras?.get("data") as Bitmap
//                    main_img?.setImageBitmap(cameraContent)
//                }
//            }
//            OPEN_GALLERY_REQUEST_CODE -> {
//                if (resultCode == Activity.RESULT_OK) {
//                    val contentURI = data?.data
//                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
//                    main_img?.setImageBitmap(bitmap)
//                }
//            }
//        }
//    }

    private fun checkConnection(): Boolean {

        val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo

        if (networkInfo != null && networkInfo.isConnected) {
            return true
        } else {
            createAlert()
            return false
        }
    }

    private fun createAlert() {

        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle("Network Error")
        alertDialog.setMessage("Please check your internet connection")

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK") { dialog: DialogInterface?, which: Int ->
            startActivity(Intent(Settings.ACTION_SETTINGS))
        }
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel") { dialog: DialogInterface?, which: Int ->
            Toast.makeText(this, "you must be connected to the internet", Toast.LENGTH_LONG).show()
            finish()
        }

        alertDialog.show()
    }

    override fun onResume() {

        checkConnection()

        super.onResume()
    }

    private fun pBarVisibility(show: Boolean){

        if (show){
            LL.visibility = View.VISIBLE
            pBar.visibility = View.VISIBLE
            window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        } else if (!show) {
            LL.visibility = View.GONE
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            pBar.visibility = View.GONE
        }
    }

    inner class DownloadTask : AsyncTask<String, Int, ArrayList<Plant>>() {

        override fun doInBackground(vararg params: String?): ArrayList<Plant>? {

            val parsePlant = ParseUtility()

            return parsePlant.parseJsonObj()
        }

        override fun onPostExecute(result: ArrayList<Plant>?) {

            val numberOfPlants = result?.size ?: 0
            val pictureImageLink = "http://www.plantplaces.com/photos/"

            if (numberOfPlants > 0) {

                var randomIndexBtn1 = (Math.random() * result!!.size).toInt()
                var randomIndexBtn2 = (Math.random() * result.size).toInt()
                var randomIndexBtn3 = (Math.random() * result.size).toInt()
                var randomIndexBtn4 = (Math.random() * result.size).toInt()

                var randomPlants = ArrayList<Plant>()

                randomPlants.add(result.get(randomIndexBtn1))
                randomPlants.add(result.get(randomIndexBtn2))
                randomPlants.add(result.get(randomIndexBtn3))
                randomPlants.add(result.get(randomIndexBtn4))

                main_button1.text = result.get(randomIndexBtn1).toString()
                main_button2.text = result.get(randomIndexBtn2).toString()
                main_button3.text = result.get(randomIndexBtn3).toString()
                main_button4.text = result.get(randomIndexBtn4).toString()

                correctAnswerIndex = (Math.random() * randomPlants.size).toInt()
                correctPlant = randomPlants.get(correctAnswerIndex)

                Picasso.get()
                    .load(pictureImageLink + randomPlants.get(correctAnswerIndex).picture_name).fit()
                    .memoryPolicy(NO_CACHE)
                    .into(main_img, object : Callback {
                    override fun onError(e: Exception?) {
                        Log.e("ed", "error")
                    }

                    override fun onSuccess() {
                       pBarVisibility(false)
                       animationOnView(main_img, Techniques.Tada)
                       animationOnView(main_button1, Techniques.RollIn)
                       animationOnView(main_button2, Techniques.RollIn)
                       animationOnView(main_button3, Techniques.RollIn)
                       animationOnView(main_button4, Techniques.RollIn)
                       animationOnView(main_txt, Techniques.Swing)
                       animationOnView(txtRightAnswer, Techniques.FlipInX)
                       animationOnView(txtWrongAnswer, Techniques.Landing)

                       displayUIWidget(true)
                    }

                })
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun specifyAnswer(userGuess: Int) {

        when (correctAnswerIndex) {
            0 -> main_button1.setBackgroundColor(Color.GREEN)
            1 -> main_button2.setBackgroundColor(Color.GREEN)
            2 -> main_button3.setBackgroundColor(Color.GREEN)
            3 -> main_button4.setBackgroundColor(Color.GREEN)
        }

        if (userGuess == correctAnswerIndex) {
            main_txt.text = "Right"
            numberOfAnswerCorrectly++
            txtRightAnswer.setText("$numberOfAnswerCorrectly")
        } else {
            var correctPlant = correctPlant.toString()
            numberOfAnswerIncorrectly++
            txtWrongAnswer.setText("$numberOfAnswerIncorrectly")
            main_txt.text = "the correct answer is $correctPlant"
        }
    }

    private fun reset() {
        if (checkConnection()) {
            pBarVisibility(true)
            displayUIWidget(false)
            val t = DownloadTask()
            t.execute()

            txtWrongAnswer.setText("")
            txtRightAnswer.setText("")
            numberOfAnswerIncorrectly = 0
            numberOfAnswerCorrectly = 0
            main_button1.setBackgroundResource(R.drawable.btn_corners)
            main_button2.setBackgroundResource(R.drawable.btn_corners)
            main_button3.setBackgroundResource(R.drawable.btn_corners)
            main_button4.setBackgroundResource(R.drawable.btn_corners)
            main_txt.setText("")
        }
    }

    private fun displayUIWidget(display: Boolean){

        if (!display){

            main_img.visibility = View.INVISIBLE
            main_button1.visibility = View.INVISIBLE
            main_button2.visibility = View.INVISIBLE
            main_button3.visibility = View.INVISIBLE
            main_button4.visibility = View.INVISIBLE
            main_txt.visibility = View.INVISIBLE
            txtRightAnswer.visibility = View.INVISIBLE
            txtWrongAnswer.visibility = View.INVISIBLE
        }
        else if (display){

            main_img.visibility = View.VISIBLE
            main_button1.visibility = View.VISIBLE
            main_button2.visibility = View.VISIBLE
            main_button3.visibility = View.VISIBLE
            main_button4.visibility = View.VISIBLE
            main_txt.visibility = View.VISIBLE
            txtRightAnswer.visibility = View.VISIBLE
            txtWrongAnswer.visibility = View.VISIBLE
        }
    }

    private fun animationOnView(v: View, T: Techniques){

        YoYo.with(T)
            .duration(700)
            .repeat(0)
            .playOn(v)
    }
}
