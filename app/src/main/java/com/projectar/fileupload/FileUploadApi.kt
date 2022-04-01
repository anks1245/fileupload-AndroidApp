package com.projectar.fileupload

import retrofit2.http.Multipart
import retrofit2.http.POST
import okhttp3.RequestBody

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Part


interface FileUploadApi {
    @Multipart
    @POST("fileupload_api.php")
    fun uploadFile(@Part file: MultipartBody.Part?,
//                   @Part("file") fileName: RequestBody?,
                   @Part("name") name: RequestBody?,
                    @Part("desc") desc: RequestBody?
                   ): Call<Response>
}