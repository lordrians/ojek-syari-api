package com.lordrians.ojeksyariapi.utils

import com.google.gson.Gson
import java.util.*
import javax.crypto.spec.IvParameterSpec

fun String.jsonToBase64() = String(Base64.getEncoder().encode(this.toByteArray()))

fun ByteArray.ivToParamSpec() = IvParameterSpec(this)
