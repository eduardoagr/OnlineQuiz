package com.example.ed.onlinequiz.Model

import android.graphics.Bitmap
import java.io.*
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

class DownloadingData {

    @Throws(IOException::class)
    fun downloadJSON(link: String): String {
        val stringBuilder = StringBuilder()

        val url = URL(link)
        val urlConnection = url.openConnection() as HttpURLConnection
        try {
            val bufferedInputStream = BufferedInputStream(urlConnection.inputStream)
            val bufferedReader = BufferedReader(InputStreamReader(bufferedInputStream))
            var inputLine: String?
            inputLine = bufferedReader.readLine()
            while (inputLine != null) {
                stringBuilder.append(inputLine)
                inputLine = bufferedReader.readLine()
            }
        } finally {
            urlConnection.disconnect()
        }
        return stringBuilder.toString()
    }
}
