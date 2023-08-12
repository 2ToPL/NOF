package io.github.kuroka3.nof.manager.utils

import org.json.simple.JSONObject
import java.io.File

object SettingsManager {
    lateinit var file: JSONFile
    lateinit var setobj: JSONObject
    var maxAPIReq: Int = 0
        set(value) {
            field = value
            setobj["maxAPIReq"] = value
            file.saveJSONFile(setobj)
        }
    var login: JSONObject = JSONObject()
        set(value) {
            field = value
            setobj["login"] = value
            file.saveJSONFile(setobj)
        }
    var logkey: String? = null
        set(value) {
            field = value
            setobj["logkey"] = value
            file.saveJSONFile(setobj)
        }
    var enable_goup: Boolean = true
        set(value) {
            field = value
            setobj["enable_goup"] = value
            file.saveJSONFile(setobj)
        }
    var goup_when_next: Boolean = true
        set(value) {
            field = value
            setobj["goup_when_next"] = value
            file.saveJSONFile(setobj)
        }

    fun load(filesdir: File) {
        file = JSONFile("${filesdir.path}/settings.json")

        if(!file.isFile) {
            file.createNewFile()
            val obj = JSONObject()
            obj["logkey"] = null
            obj["enable_goup"] = true
            obj["goup_when_next"] = true
            obj["maxAPIReq"] = 5
            val lg = JSONObject()
            lg["id"] = null
            lg["pw"] = null
            obj["login"] = lg
            file.saveJSONFile(obj)
        }

        setobj = file.jsonObject!!

        maxAPIReq = (setobj["maxAPIReq"] as Long).toInt()
        login = setobj["login"] as JSONObject
        logkey = setobj["logkey"] as String?
        goup_when_next = setobj["goup_when_next"] as Boolean
        enable_goup = setobj["enable_goup"] as Boolean
    }
}