package io.github.kuroka3.nof.manager.classes

import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.net.URL

class Book(val id: Int) {
    val url = "https://novelpia.com/novel/$id"
    var title: String?
    var author: String?
    var desc: String?
    var novels: JSONArray?

    init {
        val url = URL("https://b-p.msub.kr/novelp/list/?p=all&last=0&id=$id")
        val obj: JSONObject
        try {
            obj = JSONParser().parse(url.readText()) as JSONObject
            novels = obj["result"] as JSONArray
        } catch (e: Exception) {
            e.printStackTrace()
            novels = null
        }

        val url2 = URL("https://b-p.msub.kr/novelp/info/?id=$id")
        val obj2: JSONObject
        try {
            obj2 = JSONParser().parse(url2.readText()) as JSONObject
            val tmpobj = obj2["result"] as JSONObject
            title = tmpobj["title"] as String
            author = tmpobj["author"] as String
            desc = tmpobj["summary"] as String
        } catch (e: Exception) {
            e.printStackTrace()
            title = null
            author = null
            desc = null
        }
    }

    operator fun get(num: Int): Novel? {
        return try {
            val jobj = novels!![num] as JSONObject
            val ep = jobj["ep"] as String
            Novel((jobj["link"] as String).toInt(), jobj["title"] as String, if(ep == "0") "Prologue" else ep)
        } catch (e: Exception) {
            null
        }
    }
}