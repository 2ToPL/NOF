package io.github.kuroka3.nof.manager.utils

import io.github.kuroka3.nof.manager.classes.LoginResult
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

object ClientManager {
    lateinit var loginKey: String
    var isLogined: Boolean = false

    fun login(login: JSONObject): LoginResult {
        val loginURL = "https://b-p.msub.kr/novelp/login?v=0.1.4"
        val data = login.toJSONString()

        try {
            val url = URL(loginURL)
            val conn = url.openConnection() as HttpURLConnection
            conn.doOutput = true
            conn.doInput = true
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            val writer = OutputStreamWriter(conn.outputStream)
            writer.write(data)
            writer.flush()

            val `in` = BufferedReader(InputStreamReader(conn.inputStream))
            val content = StringBuilder()
            var inputLine = `in`.readLine()
            while (inputLine != null) {
                content.append(inputLine)
                inputLine = `in`.readLine()
            }

            `in`.close()
            conn.disconnect()

            val parser = JSONParser()
            val jsonObject = parser.parse(content.toString()) as JSONObject
            val err: String? = jsonObject["err"] as String?

            if(err != null) {
                return LoginResult(false, err = err)
            }

            loginKey = jsonObject["result"] as String

            SettingsManager.logkey = loginKey

            isLogined = true

            return LoginResult(true, loginKey)
        } catch (e: Exception) {
            e.printStackTrace()
            return LoginResult(false, err = "${e}: ${e.message}")
        }
    }

    fun login(loginkey: String) {
        loginKey = loginkey
        isLogined = true
    }

    fun runInAnotherThread(runnable: Runnable) {
        Thread(runnable).start()
    }
}
