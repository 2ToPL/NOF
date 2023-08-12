package io.github.kuroka3.nof

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import io.github.kuroka3.nof.manager.utils.JSONFile
import org.json.simple.JSONObject

class NovelInfoActivity : AppCompatActivity() {
    lateinit var title: TextView
    lateinit var author: TextView
    lateinit var summary: TextView
    lateinit var stfrl: Button
    lateinit var startfrthis: Button
    lateinit var startfrthisedit: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_novel_info)

        title = findViewById(R.id.title_view)
        author = findViewById(R.id.author_view)
        summary = findViewById(R.id.summary)
        stfrl = findViewById(R.id.start_from_last)
        startfrthis = findViewById(R.id.start_from_this)
        startfrthisedit = findViewById(R.id.start_from_text)

        val id = intent.getStringExtra("novid")
        val info: JSONObject = JSONFile("${filesDir.path}/$id/info.json").jsonObject ?: JSONObject()
        if (info["title"] == null) {
            Toast.makeText(this@NovelInfoActivity, "info.json 파일이 손상되었습니다", Toast.LENGTH_LONG).show()
            finish()
        }

        val titlev = info["title"] as String
        val authorv = info["author"] as String
        val summaryv = info["desc"] as String
        val last = (info["last"] as Long).toInt()

        title.text = titlev
        author.text = "작가 $authorv"
        summary.text = summaryv
        stfrl.text = "${last}화 이어보기"
        startfrthisedit.hint = last.toString()

        stfrl.setOnClickListener {
            val intent = Intent(this@NovelInfoActivity, ReaderActivity::class.java)
            intent.putExtra("book", id)
            intent.putExtra("ep", last)
            startActivity(intent)
        }

        startfrthis.setOnClickListener {
            val intent = Intent(this@NovelInfoActivity, ReaderActivity::class.java)
            intent.putExtra("book", id)
            intent.putExtra("ep", startfrthisedit.text.toString().toInt())
            startActivity(intent)
        }
    }
}