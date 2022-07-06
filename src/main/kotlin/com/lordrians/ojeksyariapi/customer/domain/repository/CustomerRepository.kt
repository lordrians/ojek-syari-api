package com.lordrians.ojeksyariapi.customer.domain.repository

import com.lordrians.ojeksyariapi.customer.data.entity.request.CustLoginReq
import com.lordrians.ojeksyariapi.customer.data.entity.request.CustRegisterReq
import com.lordrians.ojeksyariapi.customer.data.entity.response.CustLoginResDTO
import com.lordrians.ojeksyariapi.customer.data.entity.response.CustRegisterResDTO
import com.lordrians.ojeksyariapi.utils.Result

interface CustomerRepository {
    fun register(custRegisterReq: CustRegisterReq): Result<CustRegisterResDTO>
    fun login(custLoginReq: CustLoginReq): Result<CustLoginResDTO>
}