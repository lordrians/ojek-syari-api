package com.lordrians.ojeksyariapi.driver.domain.service.implementation

import com.lordrians.ojeksyariapi.authentication.JwtConfig
import com.lordrians.ojeksyariapi.customer.domain.entity.CustLoginRes
import com.lordrians.ojeksyariapi.customer.domain.entity.CustRegisterRes
import com.lordrians.ojeksyariapi.driver.data.entity.request.DrivLoginReq
import com.lordrians.ojeksyariapi.driver.data.entity.request.DrivRegisterReq
import com.lordrians.ojeksyariapi.driver.domain.entity.DrivLoginRes
import com.lordrians.ojeksyariapi.driver.domain.entity.DrivRegisterRes
import com.lordrians.ojeksyariapi.driver.domain.repository.DriverRepository
import com.lordrians.ojeksyariapi.driver.domain.service.DriverService
import com.lordrians.ojeksyariapi.utils.Result
import com.lordrians.ojeksyariapi.utils.data
import com.lordrians.ojeksyariapi.utils.encryption.EncryptionUseCase
import com.lordrians.ojeksyariapi.utils.tryCatch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DriverServiceImpl(
    @Autowired
    private val repository: DriverRepository,
    @Autowired
    private val encryptionUseCase: EncryptionUseCase
) : DriverService{
    override fun register(drivRegisterReq: DrivRegisterReq): Result<DrivRegisterRes> = tryCatch {
        val encryptedPass = encryptionUseCase.encryptPass(
            drivRegisterReq.password
        ).data
        val registerRes = repository.register(
            drivRegisterReq.copy(
                password = encryptedPass
            )
        ).data
        val token = JwtConfig.generateToken(
            registerRes.driver?.id,
            registerRes.driver?.username
        )
        val customer = registerRes.driver?.apply {
            this.password = null
        }
        Result.Success(
            DrivRegisterRes(
            customer,
            token ))
    }

    override fun login(drivLoginReq: DrivLoginReq): Result<DrivLoginRes> = tryCatch {
        val loginResDTO = repository.login(drivLoginReq).data
        val isUserAuthorized = encryptionUseCase.isPassAuthorized(
            password = drivLoginReq.password,
            encryptedData = loginResDTO.password.toString()
        ).data
        if (isUserAuthorized){
            val token = JwtConfig.generateToken(
                loginResDTO.id,
                loginResDTO.username
            )
            Result.Success(DrivLoginRes(token = token))
        } else {
            throw Exception("Username / Password salah")
        }
    }
}