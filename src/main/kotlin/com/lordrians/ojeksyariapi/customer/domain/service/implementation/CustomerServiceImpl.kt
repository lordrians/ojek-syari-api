package com.lordrians.ojeksyariapi.customer.domain.service.implementation

import com.lordrians.ojeksyariapi.authentication.JwtConfig
import com.lordrians.ojeksyariapi.customer.data.entity.request.CustLoginReq
import com.lordrians.ojeksyariapi.customer.data.entity.request.CustRegisterReq
import com.lordrians.ojeksyariapi.customer.domain.entity.CustLoginRes
import com.lordrians.ojeksyariapi.customer.domain.entity.CustRegisterRes
import com.lordrians.ojeksyariapi.customer.domain.repository.CustomerRepository
import com.lordrians.ojeksyariapi.customer.domain.service.CustomerService
import com.lordrians.ojeksyariapi.utils.Result
import com.lordrians.ojeksyariapi.utils.data
import com.lordrians.ojeksyariapi.utils.encryption.EncryptionUseCase
import com.lordrians.ojeksyariapi.utils.tryCatch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CustomerServiceImpl(
    @Autowired
    private val repository: CustomerRepository,
    @Autowired
    private val encryptionUseCase: EncryptionUseCase
): CustomerService{
    override fun register(custRegisterReq: CustRegisterReq): Result<CustRegisterRes> = tryCatch {
        val encryptedPass = encryptionUseCase.encryptPass(
            custRegisterReq.password
        ).data
        val registerRes = repository.register(
            custRegisterReq.copy(
                password = encryptedPass
            )
        ).data
        val token = JwtConfig.generateToken(
            registerRes.customer?.id,
            registerRes.customer?.username
        )
        val customer = registerRes.customer?.apply {
            this.password = null
        }
        Result.Success(CustRegisterRes(
            customer,
            token
        ))
    }

    override fun login(custLoginReq: CustLoginReq): Result<CustLoginRes> = tryCatch {
        val loginResDTO = repository.login(custLoginReq).data
        val isUserAuthorized = encryptionUseCase.isPassAuthorized(
            password = custLoginReq.password,
            encryptedData = loginResDTO.password.toString()
        ).data
        if (isUserAuthorized){
            val token = JwtConfig.generateToken(
                loginResDTO.id,
                loginResDTO.username
            )
            Result.Success(CustLoginRes(token = token))
        } else {
            throw Exception("Username / Password salah")
        }
    }
}