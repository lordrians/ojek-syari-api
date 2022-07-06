package com.lordrians.ojeksyariapi.driver.data.repository

import com.lordrians.ojeksyariapi.DatabaseComponent
import com.lordrians.ojeksyariapi.customer.data.entity.Customer
import com.lordrians.ojeksyariapi.customer.data.entity.response.CustLoginResDTO
import com.lordrians.ojeksyariapi.customer.data.entity.response.CustRegisterResDTO
import com.lordrians.ojeksyariapi.driver.data.entity.Driver
import com.lordrians.ojeksyariapi.driver.data.entity.request.DrivLoginReq
import com.lordrians.ojeksyariapi.driver.data.entity.request.DrivRegisterReq
import com.lordrians.ojeksyariapi.driver.data.entity.response.DrivLoginResDTO
import com.lordrians.ojeksyariapi.driver.data.entity.response.DrivRegisterResDTO
import com.lordrians.ojeksyariapi.driver.domain.repository.DriverRepository
import com.lordrians.ojeksyariapi.utils.Constant
import com.lordrians.ojeksyariapi.utils.Result
import com.lordrians.ojeksyariapi.utils.data
import com.lordrians.ojeksyariapi.utils.tryCatch
import com.mongodb.client.MongoCollection
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.getCollection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class DriverRepositoryImpl(
    @Autowired
    private val databaseCom: DatabaseComponent
) : DriverRepository{

    private fun getCollection(): MongoCollection<Driver> =
        databaseCom.database.getDatabase(Constant.DB_NAME).getCollection()

    override fun login(drivLoginReq: DrivLoginReq): Result<DrivLoginResDTO> = tryCatch {
        val driver = getByUsername(drivLoginReq.username).data
        if (driver != null){
            DrivLoginResDTO(
                id = driver.id,
                username = driver.username,
                password = driver.password
            ).let {
                Result.Success(it)
            }
        } else {
            throw Exception("Username / Password tidak sesuai")
        }
    }

    override fun register(drivRegisterReq: DrivRegisterReq): Result<DrivRegisterResDTO> = tryCatch {
        val isExist = getByUsername(drivRegisterReq.username).data != null
        if (isExist){
            throw Exception("User sudah terdaftar")
        } else {
            val driver = Driver(
                id = UUID.randomUUID().toString(),
                username = drivRegisterReq.username,
                password = drivRegisterReq.password
            )
            val driverResDTO = DrivRegisterResDTO(
                driver = driver.apply { getCollection().insertOne(driver) }
            )
            Result.Success(driverResDTO)
        }
    }

    private fun getByUsername(username: String): Result<Driver?> = tryCatch {
        getCollection().findOne(Driver::username eq username).let { driver ->
            Result.Success(driver)
        }
    }
}