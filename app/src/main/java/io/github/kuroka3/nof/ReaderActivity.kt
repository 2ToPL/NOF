package io.github.kuroka3.nof

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import io.github.kuroka3.nof.manager.utils.JSONFile
import io.github.kuroka3.nof.manager.utils.SettingsManager
import java.io.File

class ReaderActivity : AppCompatActivity() {
    lateinit var scroll: ScrollView
    lateinit var goup: Button
    lateinit var go_prev: Button
    lateinit var reader: TextView
    lateinit var clickable: LinearLayout
    lateinit var go_next: Button
    var ep = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reader)

        scroll = findViewById(R.id.reader_view)
        goup = findViewById(R.id.goup)
        go_prev = findViewById(R.id.go_prev)
        reader = findViewById(R.id.reader_main)
        clickable = findViewById(R.id.clickable_center)
        go_next = findViewById(R.id.go_next)

        if (!SettingsManager.enable_goup) goup.visibility = View.GONE

        val book = intent.getStringExtra("book")!!
        ep = intent.getIntExtra("ep", 0)

        if (true) {
            val info = JSONFile("${filesDir.path}/$book/info.json")
            val obj = info.jsonObject!!

            if ((obj["ep"] as Long).toInt() == ep) {
                go_next.visibility = View.GONE
            } else if ((obj["ep"] as Long).toInt() < ep) {
                Toast.makeText(this@ReaderActivity, "Invalid episode", Toast.LENGTH_LONG).show()
                finish()
            } else if (ep < 0) {
                Toast.makeText(this@ReaderActivity, "Invalid episode", Toast.LENGTH_LONG).show()
                finish()
            } else if (ep == 0) {
                go_prev.visibility = View.GONE
                obj["last"] = ep
                info.saveJSONFile(obj)
            } else {
                obj["last"] = ep
                info.saveJSONFile(obj)
            }
        }

        val novel = File("${filesDir.path}/$book/$ep.txt")
        val value = novel.readText()

        reader.text = value

        clickable.setOnClickListener {
            if (SettingsManager.enable_goup) goup.visibility = if(goup.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }

        goup.setOnClickListener {
            scroll.smoothScrollTo(0, 0)
        }

        go_prev.setOnClickListener {
            ep--
            val novel2 = File("${filesDir.path}/$book/$ep.txt")
            val value2 = novel2.readText()
            reader.text = value2
            if(SettingsManager.goup_when_next) scroll.smoothScrollTo(0, 0)

            val info = JSONFile("${filesDir.path}/$book/info.json")
            val obj = info.jsonObject!!
            obj["last"] = ep
            info.saveJSONFile(obj)

            if (ep == 0) {
                go_prev.visibility = View.GONE
            }

            if(go_next.visibility == View.GONE) {
                go_next.visibility = View.VISIBLE
            }
        }

        go_next.setOnClickListener {
            ep++
            val novel2 = File("${filesDir.path}/$book/$ep.txt")
            val value2 = novel2.readText()
            reader.text = value2
            if(SettingsManager.goup_when_next) scroll.smoothScrollTo(0, 0)

            val info = JSONFile("${filesDir.path}/$book/info.json")
            val obj = info.jsonObject!!
            obj["last"] = ep
            info.saveJSONFile(obj)

            if ((obj["ep"] as Long).toInt() == ep) {
                go_next.visibility = View.GONE
            }

            if(go_prev.visibility == View.GONE) {
                go_prev.visibility = View.VISIBLE
            }
        }
    }
}