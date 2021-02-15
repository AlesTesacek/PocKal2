package com.example.pockal


import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    val MESTO: String = "Zlin"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val prvniCislo = findViewById<EditText>(R.id.prvniCislo)
        val druheCislo = findViewById<EditText>(R.id.druheCislo)
        val tlacitkoRovnaSe = findViewById<Button>(R.id.spocitej)
        val vysledek = findViewById<TextView>(R.id.vysledek)
        val operace = findViewById<Spinner>(R.id.operace)
        val mnozinaOperaci = listOf("+", "-", "÷", "X")
        operace.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, mnozinaOperaci)

        tlacitkoRovnaSe.setOnClickListener {
            val a = prvniCislo.text.toString().toInt()
            val b = druheCislo.text.toString().toInt()
            val o = operace.selectedItem as String

            var c = ""
            when(o) {
                "+" -> c = (a + b).toString()
                "-" -> c = (a - b).toString()
                "÷" -> c = (a.toFloat() / b).toString()
                "X" -> c = (a.toFloat() * b).toString()
            }

            vysledek.text = c
        }




        pocasi().execute()

    }

    inner class pocasi() : AsyncTask<String, Void, String>() {


        override fun doInBackground(vararg params: String?): String? {
            var response:String?
            try{
                response = URL("https://api.openweathermap.org/data/2.5/weather?q=$MESTO&units=metric&appid=2e584f09560c128f2127a70959725a28").readText(
                        Charsets.UTF_8
                )
            }catch (e: Exception){
                response = null
            }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {

                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val updatedAt:Long = jsonObj.getLong("dt")
                val updatedAtText = "Dnesni datum: "+ SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH ).format(Date(updatedAt*1000))
                val temp = main.getString("temp")+"°C"



                val address = jsonObj.getString("name")+", "+sys.getString("country")


                findViewById<TextView>(R.id.mesto).text = address
                findViewById<TextView>(R.id.aktualizovano).text =  updatedAtText
                findViewById<TextView>(R.id.teplota).text = temp
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.VISIBLE

            } catch (e: Exception) {
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<TextView>(R.id.errorText).visibility = View.VISIBLE
            }

        }
    }
}