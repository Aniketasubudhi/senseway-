package com.senseway.karnataka

import android.graphics.Bitmap
import android.util.Log
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

data class GeminiResponse(val text: String?, val error: String? = null)

class GeminiPro {
    suspend fun getResponse(bitmap: Bitmap, prompt: String): GeminiResponse {
        if (BuildConfig.apiKey == "YOUR_API_KEY") {
            return GeminiResponse(null, error = "API key not set. Please add your API key to build.gradle.")
        }

        val generativeModel = GenerativeModel(
            modelName = "gemini-pro-vision",
            apiKey = BuildConfig.apiKey
        )

        val inputContent = content {
            image(bitmap)
            text(prompt)
        }

        return withContext(Dispatchers.IO) {
            try {
                val response = generativeModel.generateContent(inputContent)
                GeminiResponse(response.text)
            } catch (e: Exception) {
                Log.e("GeminiPro", "Error getting response from Gemini API: ${e.localizedMessage}")
                GeminiResponse(null, error = "Something unexpected happened. Please try again.")
            }
        }
    }
}