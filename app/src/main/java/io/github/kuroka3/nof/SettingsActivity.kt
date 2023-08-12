package io.github.kuroka3.nof

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import io.github.kuroka3.nof.manager.utils.ClientManager
import io.github.kuroka3.nof.manager.utils.SettingsManager
import org.json.simple.JSONObject
import java.lang.NumberFormatException

class SettingsActivity : AppCompatActivity() {
    lateinit var enable_goup: SwitchCompat
    lateinit var goup_when: SwitchCompat
    lateinit var maxAPIReq: EditText
    lateinit var confirm: Button
    lateinit var logout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        enable_goup = findViewById(R.id.enable_goup)
        goup_when = findViewById(R.id.goup_when)
        maxAPIReq = findViewById(R.id.maxAPIReq)
        confirm = findViewById(R.id.confirm_setting)
        logout = findViewById(R.id.logout)

        maxAPIReq.setText("${SettingsManager.maxAPIReq}")
        enable_goup.isChecked = SettingsManager.enable_goup
        goup_when.isChecked = SettingsManager.goup_when_next

        if(!ClientManager.isLogined) {
            logout.visibility = View.GONE
        }

        confirm.setOnClickListener {
            try {
                if (maxAPIReq.text.toString().toInt() < 1) {
                    Toast.makeText(this@SettingsActivity, "최대 API Request 횟수는 1 이상이여야 합니다.", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                } else if (maxAPIReq.text.toString().toInt() > 15) {
                    Toast.makeText(this@SettingsActivity, "최대 API Request 횟수는 15 이하여야 합니다.", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }

                SettingsManager.enable_goup = enable_goup.isChecked
                SettingsManager.goup_when_next = goup_when.isChecked
                SettingsManager.maxAPIReq = maxAPIReq.text.toString().toInt()

                setResult(RESULT_CANCELED)
                finish()
            } catch (e: NumberFormatException) {
                Toast.makeText(this@SettingsActivity, "최대 API Request 횟수 형식이 잘못되었습니다.", Toast.LENGTH_LONG).show()
            }
        }

        logout.setOnClickListener {
            AlertDialog.Builder(this@SettingsActivity)
                .setTitle("로그아웃")
                .setMessage("로그인 후 10분이내 재로그인이 불가능합니다.\n정말 로그아웃 하시겠습니까?")
                .setPositiveButton(android.R.string.yes) { dialog, whichButton -> // 확인시 처리 로직
                    val login = JSONObject()
                    login["id"] = null; login["pw"] = null
                    SettingsManager.login = login
                    SettingsManager.logkey = null

                    Toast.makeText(this@SettingsActivity, "로그아웃 완료", Toast.LENGTH_LONG).show()

                    setResult(RESULT_OK)
                    finish()
                }
                .setNegativeButton(android.R.string.cancel) { dialog, whichButton -> // 확인시 처리 로직
                    Toast.makeText(this@SettingsActivity, "로그아웃 취소됨", Toast.LENGTH_LONG).show()
                }.show()
        }
    }
}