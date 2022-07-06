package com.lordrians.ojeksyariapi.utils.encryption

import com.lordrians.ojeksyariapi.utils.Result

interface EncryptionUseCase {
    fun encryptPass(password: String): Result<String>
    fun isPassAuthorized(
        password: String,
        encryptedData: String
    ): Result<Boolean>
}