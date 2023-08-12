package io.github.kuroka3.nof

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import io.github.kuroka3.nof.manager.classes.Book
import io.github.kuroka3.nof.manager.utils.APIManager
import io.github.kuroka3.nof.manager.utils.ClientManager
import io.github.kuroka3.nof.manager.utils.JSONFile
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import java.io.File
import java.io.FileWriter
import kotlin.math.roundToInt
import kotlin.system.exitProcess

class DownloadActivity : AppCompatActivity() {
    lateinit var progress: ProgressBar
    lateinit var nokori: TextView
    lateinit var tohome: Button
    lateinit var cancel: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)

        progress = findViewById(R.id.progress)
        nokori = findViewById(R.id.view_nokori)
        tohome = findViewById(R.id.tohome)
        cancel = findViewById(R.id.cancel_download)

        cancel.setOnClickListener {
            finishAndRemoveTask()
            exitProcess(1)
        }

        val novId = intent.getStringExtra("selNov")!!.toInt()

        ClientManager.runInAnotherThread {
            val book = Book(novId)

            val dir = "${filesDir.path}/$novId"

            val fold = File(dir)
            fold.mkdir()

            val info = JSONFile("$dir/info.json")
            info.createNewFile()

            val infobj = JSONObject()
            infobj["title"] = book.title
            infobj["author"] = book.author
            infobj["desc"] = book.desc
            infobj["ep"] = book.novels!!.size-1
            infobj["last"] = 0

            info.saveJSONFile(infobj)

            for (i in 0..<book.novels!!.size) {
                val novel = book[i] ?: break
                val novString = novel.read()
                val file = File("$dir/${i}.txt")
                val writer = FileWriter(file)
                writer.write(novString)
                writer.flush()
                writer.close()

                runOnUiThread {
                    nokori.text = "${i+1}/${book.novels!!.size}"
                    val db = (i+1).toDouble()
                    val novdb = book.novels!!.size.toDouble()
                    progress.progress = ((db / novdb) * 100.0).roundToInt()
                }
            }

            val novels = JSONFile("${filesDir.path}/novels.json")
            val obj = novels.jsonObject!!
            val ary = obj["novels"] as JSONArray? ?: JSONArray()
            ary.add(novId.toString())
            obj["novels"] = ary
            novels.saveJSONFile(obj)

            runOnUiThread {
                tohome.visibility = View.VISIBLE
            }
        }

        tohome.setOnClickListener {
            finish()
        }
    }
}