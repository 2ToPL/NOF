package io.github.kuroka3.nof.manager.classes

import io.github.kuroka3.nof.manager.interfaces.Readable
import io.github.kuroka3.nof.manager.utils.APIManager
import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser

data class Novel(val id: Int, val title: String, val ep: String) : Readable {
    val url = "https://novelpia.com/proc/viewer_data/$id"

    override fun read(): String {
        val parser = JSONParser()

        // After logging in, get the text from the page
        val getTextResponse = APIManager.reqNovel(url) ?: return "ERR: 소설을 불러올 수 없습니다."

        val builder = StringBuilder()

        val text = (parser.parse(getTextResponse) as JSONObject)["s"] as JSONArray


        for (element in text) {
            val jObject = element as JSONObject
            builder.append(" ${jObject["text"]}")
        }

        var builderstring = builder.toString()
        builderstring = builderstring.replace("커버보기", "")
        builderstring = builderstring.replace("&nbsp;", "")
        builderstring = builderstring.replace("&lt;", "<")
        builderstring = builderstring.replace("&gt;", ">")
        builderstring = builderstring.replace("&amp;", "&")
        builderstring = builderstring.replace("&quot;", "\"")
        builderstring = removeTextInParentheses(builderstring)

        return "$ep : ${title}\n\n\n\n\n$builderstring"
    }

    private fun removeTextInParentheses(input: String): String {
        var inpu = input
        for(i in 1..50) {
            val startIndex = inpu.indexOf("<")
            val endIndex = inpu.indexOf(">")
            if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
                val textToRemove = inpu.substring(startIndex, endIndex + 1)
                inpu = inpu.replace(textToRemove, "")
            }
            if(!inpu.contains("<") || !inpu.contains(">")) {
                break
            }
        }
        return inpu
    }
}