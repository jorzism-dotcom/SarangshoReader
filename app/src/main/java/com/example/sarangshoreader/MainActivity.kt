package com.example.sarangshoreader

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class MainActivity : AppCompatActivity() {
    private lateinit var output: TextView
    private val collected = StringBuilder()

    private val pickImages = registerForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        if (uris.isNullOrEmpty()) return@registerForActivityResult
        output.text = "ছবি থেকে লেখা পড়া হচ্ছে…"
        processImages(uris)
    }

    private val createTextFile = registerForActivityResult(
        ActivityResultContracts.CreateDocument("text/plain")
    ) { uri ->
        if (uri != null) saveText(uri)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 24, 24, 24)
        }

        val intro = TextView(this).apply {
            text = "Sarangsho Reader\\n\\nনিজের ফোনে তোলা/সংরক্ষিত স্ক্রিনশট থেকে বাংলা ও ইংরেজি লেখা OCR করে টেক্সট বানান। প্রতিটি স্ক্রিনশট নিজে নির্বাচন করুন।"
            textSize = 17f
        }
        root.addView(intro)

        val pick = Button(this).apply {
            text = "স্ক্রিনশট নির্বাচন করে লেখা পড়ুন"
            setOnClickListener { pickImages.launch("image/*") }
        }
        root.addView(pick)

        val export = Button(this).apply {
            text = "TXT ফাইল হিসেবে সংরক্ষণ"
            setOnClickListener {
                if (collected.isEmpty()) {
                    Toast.makeText(this@MainActivity, "আগে ছবি থেকে লেখা পড়ুন", Toast.LENGTH_SHORT).show()
                } else {
                    createTextFile.launch("sarangsho-news-notes.txt")
                }
            }
        }
        root.addView(export)

        val clear = Button(this).apply {
            text = "লেখা মুছুন"
            setOnClickListener {
                collected.clear()
                output.text = "এখানে OCR করা লেখা দেখা যাবে।"
            }
        }
        root.addView(clear)

        output = TextView(this).apply {
            text = "এখানে OCR করা লেখা দেখা যাবে।"
            textSize = 15f
            setTextIsSelectable(true)
        }
        val scroll = ScrollView(this).apply { addView(output) }
        root.addView(scroll, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f
        ))
        setContentView(root)
    }

    private fun processImages(uris: List<Uri>) {
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        val results = mutableListOf<String>()
        var remaining = uris.size

        uris.forEachIndexed { index, uri ->
            try {
                val image = InputImage.fromFilePath(this, uri)
                recognizer.process(image)
                    .addOnSuccessListener { visionText ->
                        val text = visionText.text.trim()
                        if (text.isNotBlank()) {
                            results.add("----- ছবি ${index + 1} -----\\n$text")
                        }
                    }
                    .addOnFailureListener {
                        results.add("----- ছবি ${index + 1}: পড়া যায়নি -----")
                    }
                    .addOnCompleteListener {
                        remaining--
                        if (remaining == 0) {
                            val batch = results.joinToString("\\n\\n")
                            if (batch.isNotBlank()) {
                                if (collected.isNotEmpty()) collected.append("\\n\\n")
                                collected.append(batch)
                            }
                            output.text = collected.toString().ifBlank { "কোনো লেখা শনাক্ত হয়নি।" }
                            recognizer.close()
                        }
                    }
            } catch (e: Exception) {
                remaining--
                results.add("ছবি ${index + 1} খোলা যায়নি: ${e.localizedMessage}")
                if (remaining == 0) {
                    collected.append(results.joinToString("\\n\\n"))
                    output.text = collected.toString()
                    recognizer.close()
                }
            }
        }
    }

    private fun saveText(uri: Uri) {
        try {
            contentResolver.openOutputStream(uri)?.use { stream ->
                stream.write(collected.toString().toByteArray(Charsets.UTF_8))
            }
            Toast.makeText(this, "TXT ফাইল সংরক্ষিত হয়েছে", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(this, "সংরক্ষণ করা যায়নি: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }
}
