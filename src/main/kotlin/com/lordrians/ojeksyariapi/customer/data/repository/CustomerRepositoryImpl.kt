package com.lordrians.ojeksyariapi.customer.data.repository

import com.lordrians.ojeksyariapi.DatabaseComponent
import com.lordrians.ojeksyariapi.customer.data.entity.Customer
import com.lordrians.ojeksyariapi.customer.data.entity.request.CustLoginReq
import com.lordrians.ojeksyariapi.customer.data.entity.request.CustRegisterReq
import com.lordrians.ojeksyariapi.customer.data.entity.response.CustLoginResDTO
import com.lordrians.ojeksyariapi.customer.data.entity.response.CustRegisterResDTO
import com.lordrians.ojeksyariapi.customer.domain.repository.CustomerRepository
import com.lordrians.ojeksyariapi.utils.Constant.DB_NAME
import com.lordrians.ojeksyariapi.utils.Result
import com.lordrians.ojeksyariapi.utils.data
import com.lordrians.ojeksyariapi.utils.tryCatch
import com.mongodb.client.MongoCollection
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import org.litote.kmongo.getCollection
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class CustomerRepositoryImpl(
    @Autowired
    private val databaseCom: DatabaseComponent
) : CustomerRepository {

    private fun getCollection(): MongoCollection<Customer> =
        databaseCom.database.getDatabase(DB_NAME).getCollection()

    override fun register(custRegisterReq: CustRegisterReq): Result<CustRegisterResDTO> = tryCatch {
        val isExist = getByUsername(custRegisterReq.username).data != null
        if (isExist){
            throw Exception("User sudah terdaftar")
        } else {
            val customer = Customer(
                id = UUID.randomUUID().toString(),
                username = custRegisterReq.username,
                password = custRegisterReq.password
            )
            val customerResDTO = CustRegisterResDTO(
                customer = customer.apply { getCollection().insertOne(customer) }
            )
            Result.Success(customerResDTO)
        }
    }

    private fun getByUsername(username: String): Result<Customer?> = tryCatch {
        getCollection().findOne(Customer::username eq username).let { customer ->
            Result.Success(customer)
        }
    }

    override fun login(custLoginReq: CustLoginReq): Result<CustLoginResDTO> = tryCatch {
        val customer = getByUsername(custLoginReq.username).data
        if (customer != null){
            CustLoginResDTO(
                id = customer.id,
                username = customer.username,
                password = customer.password
            ).let {
                Result.Success(it)
            }
        } else {
            throw Exception("Username / Password tidak sesuai")
        }
    }
}