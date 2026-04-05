package com.example.twdist_android.core.network

import android.content.Context
import android.content.SharedPreferences
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import org.json.JSONObject
import java.util.concurrent.ConcurrentHashMap

/**
 * A persistent CookieJar implementation using SharedPreferences.
 */
class CookieJarImpl(context: Context) : CookieJar {
    private val prefs: SharedPreferences = context.getSharedPreferences("cookie_prefs", Context.MODE_PRIVATE)
    private val cookieStore = ConcurrentHashMap<String, MutableMap<String, Cookie>>()

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        val host = url.host
        
        val hostCookies = cookieStore.getOrPut(host) {
            loadFromPrefs(url).associateBy { it.name }.toMutableMap()
        }
        
        for (cookie in cookies) {
            if (cookie.expiresAt <= System.currentTimeMillis()) {
                hostCookies.remove(cookie.name)
            } else {
                hostCookies[cookie.name] = cookie
            }
        }
        
        val cookieStrings = hostCookies.values.map { it.toJson() }.toSet()
        prefs.edit().putStringSet(host, cookieStrings).apply()
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        val host = url.host
        
        val hostCookies = cookieStore.getOrPut(host) {
            loadFromPrefs(url).associateBy { it.name }.toMutableMap()
        }
        
        val validCookies = hostCookies.values.filter { it.expiresAt > System.currentTimeMillis() }
        
        if (validCookies.size != hostCookies.size) {
            for (cookie in hostCookies.values.toList()) {
                if (cookie.expiresAt <= System.currentTimeMillis()) {
                    hostCookies.remove(cookie.name)
                }
            }
            val cookieStrings = hostCookies.values.map { it.toJson() }.toSet()
            prefs.edit().putStringSet(host, cookieStrings).apply()
        }
        
        return validCookies
    }
    
    private fun loadFromPrefs(url: HttpUrl): List<Cookie> {
        val host = url.host
        val cookieStrings = prefs.getStringSet(host, emptySet()) ?: emptySet()
        return cookieStrings.mapNotNull { cookieFromJson(it) }
    }

    private fun Cookie.toJson(): String {
        return JSONObject().apply {
            put("name", name)
            put("value", value)
            put("domain", domain)
            put("path", path)
            put("expiresAt", expiresAt)
            put("httpOnly", httpOnly)
            put("secure", secure)
        }.toString()
    }

    private fun cookieFromJson(json: String): Cookie? {
        return try {
            val obj = JSONObject(json)
            Cookie.Builder()
                .name(obj.getString("name"))
                .value(obj.getString("value"))
                .domain(obj.getString("domain"))
                .path(obj.getString("path"))
                .expiresAt(obj.getLong("expiresAt"))
                .apply {
                    if (obj.getBoolean("httpOnly")) httpOnly()
                    if (obj.getBoolean("secure")) secure()
                }
                .build()
        } catch (e: Exception) {
            null
        }
    }
}
