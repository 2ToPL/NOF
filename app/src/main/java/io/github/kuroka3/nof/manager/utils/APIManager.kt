package io.github.kuroka3.nof.manager.utils

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object APIManager {
    private var req = 0

    fun reqNovel(urlStr: String): String? {
        for (i in 1..SettingsManager.maxAPIReq) {
            req++
            if (req == 50) {
                req = 0
                try {
                    Thread.sleep(1000L)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }

            try {
                val url = URL(urlStr)
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "GET"
                conn.connectTimeout = 1000
                conn.readTimeout = 1000
                conn.setRequestProperty("Cookie", "LOGINKEY=${ClientManager.loginKey};")

                val `in` = BufferedReader(InputStreamReader(conn.inputStream))
                val content = StringBuilder()
                var inputLine = `in`.readLine()
                while (inputLine != null) {
                    content.append(inputLine)
                    inputLine = `in`.readLine()
                }

                `in`.close()
                conn.disconnect()

                return content.toString()
            } catch (e: Exception) {
                e.printStackTrace()
                continue
            }
        }
        return null
    }


}
