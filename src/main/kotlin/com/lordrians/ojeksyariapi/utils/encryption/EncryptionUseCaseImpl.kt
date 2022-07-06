package com.lordrians.ojeksyariapi.utils.encryption

import com.google.gson.Gson
import com.lordrians.ojeksyariapi.utils.*
import org.apache.catalina.realm.SecretKeyCredentialHandler
import org.springframework.stereotype.Component
import java.nio.charset.Charset
import java.security.SecureRandom
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import kotlin.collections.ArrayList

@Component
class EncryptionUseCaseImpl : EncryptionUseCase {

    override fun encryptPass(password: String): Result<String> = tryCatch {
        val salt = getSalt()
        val iv = getIv()
        val encryptData = encrypt(
            password = password,
            salt = salt,
            ivSpec = IvParameterSpec(iv)
        )
        encryptedDataToJson(
            salt = salt,
            iv = iv,
            encryptedData = encryptData
        ).let { Result.Success(it.jsonToBase64()) }

    }
    override fun isPassAuthorized(
        password: String,
        encryptedData: String
    ): Result<Boolean> = tryCatch {
        val passEncryptDataClass = encryptedData
            .decodeBase64().jsonToEncryptDataClass()

        val passwordDecrypted = decrypt(
            encryptCode = passEncryptDataClass.encrypt.toByte(),
            salt = passEncryptDataClass.salt.toByte(),
            ivSpec = IvParameterSpec(passEncryptDataClass.initializationVector.toByte())
        )
        Result.Success(password == passwordDecrypted)
    }

    private fun encryptedDataToJson(salt: ByteArray, iv: ByteArray, encryptedData: ByteArray): String {
        hashMapOf(
            SALT to salt.toBase64(),
            IV to iv.toBase64(),
            ENCRYPT to encryptedData.toBase64()
        ).apply {
            return Gson().toJson(this)
        }
    }

    private fun getIv() : ByteArray {
        val ivRandom = SecureRandom()
        val iv = ByteArray(16)
        ivRandom.nextBytes(iv)
        return iv
    }

    private fun getSalt(): ByteArray {
        val random = SecureRandom()
        val salt = ByteArray(256)
        random.nextBytes(salt)
        return salt
    }

    private fun decrypt(encryptCode: ByteArray, salt: ByteArray, ivSpec: IvParameterSpec): String {
        Cipher.getInstance(TRANSFORMATION_KEY).apply {
            init(Cipher.DECRYPT_MODE, salt.saltToSecretKey(), ivSpec)
            return String(doFinal(encryptCode), Charset.forName(UTF_8))
        }
    }

    private fun encrypt(password: String, salt: ByteArray, ivSpec: IvParameterSpec): ByteArray {
        Cipher.getInstance(TRANSFORMATION_KEY).apply {
            init(Cipher.ENCRYPT_MODE, salt.saltToSecretKey() , ivSpec)
            return doFinal(password.toByteArray()) // 2
        }
    }

    private fun ByteArray.saltToSecretKey(): SecretKeySpec {
        val pbKeySpec = PBEKeySpec(ENCRYPT_PASS.toCharArray(), this, 1324, 256) // 1
        SecretKeyFactory.getInstance(SecretKeyCredentialHandler.DEFAULT_ALGORITHM).run {
            this.generateSecret(pbKeySpec).encoded.also {
                return SecretKeySpec(it, KEY_ALGORITHM_AES)
            }
        }
    }


    private fun ByteArray.toBase64(): String {
        val arrayInt = ArrayList<Int>()
        for (element in this)
            arrayInt.add(element.toInt())
        return ",${arrayInt.toString().filterNot { it == '[' || it == ']' || it == ' ' }},"
    }

    private fun String.toByte(): ByteArray {
        val formattedArr = this
        var tempData = ""
        val arrIntFormated = ArrayList<Int>()
        for (i in formattedArr.indices){
            if (formattedArr[i] == ','){
                if (tempData.isNotEmpty()) arrIntFormated.add(tempData.toInt())
                tempData = ""
                continue
            }
            tempData += formattedArr[i]
        }
        val byteArr = ByteArray(arrIntFormated.size)
        for (i in 0 until arrIntFormated.size){
            byteArr[i] = arrIntFormated[i].toByte()
        }
        return byteArr
    }

    private fun String.decodeBase64(): String = String(Base64.getDecoder().decode(this))
    private fun String.jsonToEncryptDataClass(): EncryptDataClass =
        Gson().fromJson(this,EncryptDataClass::class.java)

    companion object {
        const val UTF_8 = "UTF-8"
        const val KEY_ALGORITHM_AES = "AES"
        private const val BLOCK_MODE_CBC = "CBC"
        private const val ENCRYPTION_PADDING_PKCS5 = "PKCS5PADDING"
        const val ENCRYPT_PASS = "OjekSyariLhooo"
        const val SALT = "salt"
        const val IV = "initializationVector"
        const val ENCRYPT = "encrypt"
        const val TRANSFORMATION_KEY = "${KEY_ALGORITHM_AES}/" +
                "${BLOCK_MODE_CBC}/${ENCRYPTION_PADDING_PKCS5}"
    }

}