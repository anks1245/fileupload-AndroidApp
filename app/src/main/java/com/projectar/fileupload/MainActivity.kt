package com.projectar.fileupload

import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import java.io.File
import android.provider.MediaStore
import java.io.FileInputStream
import java.io.FileOutputStream


class MainActivity : AppCompatActivity() {
    private val RESPONSE_CODE = 100
    private var nameEditText: EditText ?= null
    private var descEditText: EditText ?= null
    private lateinit var chooseFileButton: Button
    private lateinit var fileTextView: TextView
    private lateinit var submitButton: Button
    private lateinit var fileuri : Uri
    private var filePath : String = ""
    private lateinit var selectedImageView : ImageView
    private lateinit var bitmap: Bitmap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nameEditText = findViewById(R.id.name)
        descEditText = findViewById(R.id.description)
        chooseFileButton = findViewById(R.id.choose_file)
        fileTextView = findViewById(R.id.file_name)
        submitButton = findViewById(R.id.submit)
        selectedImageView = findViewById(R.id.selected_image)

        chooseFileButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent,RESPONSE_CODE)
        }
        submitButton.setOnClickListener {
            if(filePath == ""){
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            upload(fileuri)
        }
    }

    private fun upload(fileUri: Uri) {

        val parcelFileDescriptor = contentResolver.openFileDescriptor(fileUri, "r", null)?: return
        val file = File(cacheDir, contentResolver.getFileName(fileUri))
        val inputstreamimage = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val outputStream = FileOutputStream(file)
        inputstreamimage.copyTo(outputStream)

        val requestBody = RequestBody.create(MediaType.parse("image/*"),file)
        val fileupload = MultipartBody.Part.createFormData("file",file.name,requestBody)
        val filename = MultipartBody.create(MediaType.parse("multipart/form-data"),nameEditText?.text.toString())
        val desc = MultipartBody.create(MediaType.parse("multipart/form-data"),descEditText?.text.toString())

        val getResponse: FileUploadApi = RetrofitConfig().getRetrofit().create(FileUploadApi::class.java)

        val call = getResponse.uploadFile(fileupload,filename,desc)
        call.enqueue(object : Callback<Response>{
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                Log.e("error",response.message() +' '+response)
                Toast.makeText(this@MainActivity, "$response", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("error",t.message.toString())
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Toast.makeText(this@MainActivity, "$resultCode $requestCode", Toast.LENGTH_SHORT).show()
        if (requestCode == RESPONSE_CODE){
            if(resultCode == -1){
                fileuri = data?.data!!
                fileTextView.text = contentResolver.getFileName(fileuri)
                filePath = contentResolver.getFileName(fileuri)
                Log.d("Image",fileuri.path.toString())
                selectedImageView.setImageURI(fileuri)
            }
        }
    }



}