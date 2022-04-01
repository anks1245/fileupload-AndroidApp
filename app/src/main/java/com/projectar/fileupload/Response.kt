package com.projectar.fileupload

import com.google.gson.annotations.SerializedName

class Response {
    @SerializedName("errors")
    var errors: String ?= null
    @SerializedName("status")
    var status: Boolean ?= null
}