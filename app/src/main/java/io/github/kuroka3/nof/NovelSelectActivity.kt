package io.github.kuroka3.nof

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.TypedValue
import android.view.ViewGroup
import android.view.WindowManager.LayoutParams
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.marginLeft
import androidx.core.view.marginStart
import io.github.kuroka3.nof.manager.utils.JSONFile
import org.json.simple.JSONArray


class NovelSelectActivity : AppCompatActivity() {

    lateinit var scroll: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_novel_select)

        scroll = findViewById(R.id.scroll)

        val novelsFile = JSONFile("${filesDir.path}/novels.json")
        val novels = novelsFile.jsonObject!!["novels"] as JSONArray

        val marginPx = dipToPixels(this, 20.0f).toInt()

        for (bookid in novels) {

            val info = JSONFile("${filesDir.path}/${bookid.toString()}/info.json")
            val infobj = info.jsonObject ?: continue

            val titleValue = infobj["title"] as String
            val authorVaule = infobj["author"] as String

            val newLayout = LinearLayout(this)
            newLayout.orientation = LinearLayout.VERTICAL
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(marginPx, 0, marginPx, marginPx)
            newLayout.layoutParams = params
            newLayout.background = AppCompatResources.getDrawable(this, R.drawable.wrap_teduri)

            val params2 = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params2.setMargins(marginPx, marginPx, marginPx, 0)

            val title = TextView(this)
            title.layoutParams = params2
            title.paintFlags = title.paintFlags or Paint.FAKE_BOLD_TEXT_FLAG
            title.text = titleValue
            title.textSize = 25.0F
            newLayout.addView(title)

            val params3 = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params3.setMargins(marginPx, 0, marginPx, marginPx)

            val author = TextView(this)
            author.layoutParams = params3
            author.text = "작가 $authorVaule"
            author.textSize = 15.0F
            newLayout.addView(author)

            val params4 = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params4.setMargins(marginPx, marginPx, marginPx, marginPx)

            val button = Button(this)
            button.layoutParams = params4
            button.text = "자세히 보기"

            button.setOnClickListener {
                val intent = Intent(this@NovelSelectActivity, NovelInfoActivity::class.java)
                intent.putExtra("novid", bookid.toString())
                startActivity(intent)
            }

            newLayout.addView(button)

            scroll.addView(newLayout)
        }
    }

    fun dipToPixels(context: Context, dipValue: Float): Float {
        val metrics = context.resources.displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics)
    }
}