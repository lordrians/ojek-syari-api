package com.lordrians.ojeksyariapi.utils.encryption

data class EncryptDataClass(
    val salt: String,
    val initializationVector: String,
    val encrypt: String
)