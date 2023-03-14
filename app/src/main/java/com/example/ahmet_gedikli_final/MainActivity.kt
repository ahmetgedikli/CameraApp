package com.example.ahmet_gedikli_final

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.database.sqlite.SQLiteDatabase

import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues

import android.database.sqlite.SQLiteException
import android.graphics.BitmapFactory

import android.graphics.Bitmap.CompressFormat
import android.widget.EditText
import android.widget.TextView
import java.io.ByteArrayOutputStream


class MainActivity : AppCompatActivity() {
    private val cameraRequest = 1888
    lateinit var imageView: ImageView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "KotlinApp"

        //Dao?
        var dao_object=ImageDatabase.getInstance(application).ImageDao()

        var textInput:EditText=findViewById(R.id.textbox)
        var lastText :TextView=findViewById(R.id.textView)
        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), cameraRequest)
        imageView = findViewById(R.id.imageView)
        imageView.isDrawingCacheEnabled=true
        val photoButton: Button = findViewById(R.id.button)
        photoButton.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, cameraRequest)
        }
        val saveButton: Button= findViewById(R.id.saveButton)
        saveButton.setOnClickListener{
            val saveImage: Bitmap= imageView.drawingCache
            dao_object.addImage(Image(textInput.text.toString(),DbBitmapUtility.getBytes(saveImage)))
            lastText.setText("Image successfully saved with tag -> ${textInput.text.toString()}")

        }
        val loadButton: Button =findViewById(R.id.loadButton)
        loadButton.setOnClickListener{
            var foundphoto: ByteArray = dao_object.getImageIdByTag(textInput.text.toString())
            imageView.setImageBitmap(DbBitmapUtility.getImage(foundphoto))

        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == cameraRequest) {
            val photo: Bitmap = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(photo)
        }
    }


    }

    object DbBitmapUtility {
        // convert from bitmap to byte array
        fun getBytes(bitmap: Bitmap): ByteArray {
            val stream = ByteArrayOutputStream()
            bitmap.compress(CompressFormat.PNG, 0, stream)
            return stream.toByteArray()
        }

        // convert from byte array to bitmap
        fun getImage(image: ByteArray): Bitmap {
            return BitmapFactory.decodeByteArray(image, 0, image.size)
        }
    }

    object TagProcesser{
        fun seperateTag(tag: String): Array<String> {
            return tag.split(";").toTypedArray()
        }
    }
