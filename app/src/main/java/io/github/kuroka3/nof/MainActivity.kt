package io.github.kuroka3.nof

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.github.kuroka3.nof.manager.classes.Novel
import io.github.kuroka3.nof.manager.utils.ClientManager
import io.github.kuroka3.nof.manager.utils.JSONFile
import io.github.kuroka3.nof.manager.utils.SettingsManager
import io.github.kuroka3.nof.utils.NetworkManager
import org.json.simple.JSONArray
import org.json.simple.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var goto_down: Button
    private lateinit var goto_read: Button
    private lateinit var goto_login: Button
    private lateinit var goto_settings: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        SettingsManager.load(filesDir)

        val novels = JSONFile("${filesDir.path}/novels.json")
        if(!novels.isFile || novels.isEmpty) {
            novels.createNewFile()
            val tmpary = JSONArray()
            val tmpobj = JSONObject()
            tmpobj["novels"] = tmpary
            novels.saveJSONFile(tmpobj)
        }

        goto_down = findViewById(R.id.goto_download)
        goto_read = findViewById(R.id.goto_read)
        goto_login = findViewById(R.id.login)
        goto_settings = findViewById(R.id.settings)

        if(!NetworkManager.checkNetworkState(this)) {
            goto_down.visibility = View.GONE
            goto_login.visibility = View.GONE
        }

        if(SettingsManager.logkey == null) {
            if (SettingsManager.login["id"] != null) {
                ClientManager.runInAnotherThread {
                    val result = ClientManager.login(SettingsManager.login)
                    if (!result.success) {
                        runOnUiThread {
                            Toast.makeText(this.applicationContext, result.err, Toast.LENGTH_LONG).show()
                            goto_down.visibility = View.GONE
                            goto_settings.visibility = View.GONE
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(this.applicationContext, "LOGIN SUCCESS", Toast.LENGTH_LONG).show()
                            goto_login.visibility = View.GONE
                        }
                    }
                }
            } else {
                goto_down.visibility = View.GONE
                goto_settings.visibility = View.GONE
            }
        } else {
            ClientManager.login(SettingsManager.logkey!!)

            ClientManager.runInAnotherThread {
                val novel = Novel(2053119, "test", "test")

                if(novel.read() == "ERR: 소설을 불러올 수 없습니다.") {
                    runOnUiThread {
                        Toast.makeText(this.applicationContext, "LOGIN FAILED", Toast.LENGTH_LONG).show()
                        goto_down.visibility = View.GONE
                        goto_settings.visibility = View.GONE
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this.applicationContext, "LOGIN SUCCESS", Toast.LENGTH_LONG).show()
                        goto_login.visibility = View.GONE
                    }
                }
            }
        }

        goto_down.setOnClickListener {
            val intent = Intent(this@MainActivity, SearchActivity::class.java)
            startActivity(intent)
        }

        goto_read.setOnClickListener {
            val intent = Intent(this@MainActivity, NovelSelectActivity::class.java)
            startActivity(intent)
        }

        goto_login.setOnClickListener {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivityForResult(intent, 0)
        }

        goto_settings.setOnClickListener {
            val intent = Intent(this@MainActivity, SettingsActivity::class.java)
            startActivityForResult(intent, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 || requestCode == 1) {
            if (resultCode != RESULT_OK) return

            finish()
            overridePendingTransition(0, 0)
            val intent = intent
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
    }
}