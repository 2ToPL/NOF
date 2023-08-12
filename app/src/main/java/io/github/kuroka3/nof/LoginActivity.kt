package io.github.kuroka3.nof

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import io.github.kuroka3.nof.manager.utils.ClientManager
import io.github.kuroka3.nof.manager.utils.SettingsManager
import org.json.simple.JSONObject

class LoginActivity : AppCompatActivity() {
    lateinit var lgid: EditText
    lateinit var pw: EditText
    lateinit var confirm: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        lgid = findViewById(R.id.login_id)
        pw = findViewById(R.id.login_pw)
        confirm = findViewById(R.id.login_confirm)

        if(SettingsManager.login["id"] != null) lgid.setText(SettingsManager.login["id"].toString())
        if(SettingsManager.login["pw"] != null) pw.setText(SettingsManager.login["pw"].toString())

        confirm.setOnClickListener {
            val login = JSONObject()
            login["id"] = lgid.text.toString()
            login["pw"] = pw.text.toString()

            ClientManager.runInAnotherThread {
                val result = ClientManager.login(login)

                if(result.success) {
                    SettingsManager.login = login
                    runOnUiThread { Toast.makeText(this@LoginActivity.applicationContext, "LOGIN SUCCESS", Toast.LENGTH_LONG).show() }
                    setResult(RESULT_OK)
                    finish()
                } else {
                    runOnUiThread { Toast.makeText(this@LoginActivity.applicationContext, result.err, Toast.LENGTH_LONG).show() }
                }
            }
        }
    }
}