package com.example.twdist_android.core.network

import android.content.Context
import android.content.SharedPreferences
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
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
        
        val cookieStrings = hostCookies.values.map { it.toString() }.toSet()
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
            val cookieStrings = hostCookies.values.map { it.toString() }.toSet()
            prefs.edit().putStringSet(host, cookieStrings).apply()
        }
        
        return validCookies
    }
    
    private fun loadFromPrefs(url: HttpUrl): List<Cookie> {
        val host = url.host
        val cookieStrings = prefs.getStringSet(host, emptySet()) ?: emptySet()
        return cookieStrings.mapNotNull { Cookie.parse(url, it) }
    }
}
