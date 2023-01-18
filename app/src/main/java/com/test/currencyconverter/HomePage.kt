package com.test.currencyconverter

import android.os.Bundle
import android.content.Intent
import android.os.Handler
import android.widget.ArrayAdapter
import androidx.drawerlayout.widget.DrawerLayout
import com.google.gson.GsonBuilder

import kotlinx.android.synthetic.main.activity_home_page.*
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class HomePage : NavigationPane() {

    private var mDelayHandler: Handler? = null
    private val splashDelay: Long = 1000

    private val mRunnable = Runnable {

        val aa = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            homeFeed.rates.keySet().toList()
        )

        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        with(fromCurr) {
            adapter = aa
            setSelection(0, true)
            gravity = android.view.Gravity.CENTER
        }

        with(toCurr) {
            adapter = aa
            setSelection(0, true)
            gravity = android.view.Gravity.CENTER
        }
    }

    private fun fetchJson() {

        println("Attempting")

        val url = "http://data.fixer.io/api/latest?access_key=6ea1f933eac1b688ac6c36ff708c6324"

        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {

            override fun onResponse(call: okhttp3.Call?, response: okhttp3.Response?) {
                val body = response?.body()?.string()

                val gson = GsonBuilder().create()
                homeFeed = gson.fromJson(body, HomeFeed::class.java)

            }

            override fun onFailure(call: okhttp3.Call?, e: IOException?) {
                println("Failed to execute request")
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        val mDrawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        onCreateDrawer(mDrawerLayout)

        fetchJson()
        convertBtn.setOnClickListener {
            val resultIntent = Intent(this, ResultPage::class.java)
            if (enterAmt.text != null) {
                val amount = enterAmt.text.toString()
                val finalValue = conversion(
                    fromCurr.selectedItem.toString(),
                    toCurr.selectedItem.toString(),
                    amount
                )
                resultIntent.putExtra("key", finalValue.toString())
                startActivity(resultIntent)
            }
        }

        mDelayHandler = Handler()
        mDelayHandler!!.postDelayed(mRunnable, splashDelay)
    }

    private fun conversion(from: String, to: String, amount: String): Float {

        val fromCurrRate = homeFeed.rates[from].toString()
        val toCurrRate = homeFeed.rates[to].toString()
        return ((amount.toFloat() * toCurrRate.toFloat()) / fromCurrRate.toFloat())
    }
}
