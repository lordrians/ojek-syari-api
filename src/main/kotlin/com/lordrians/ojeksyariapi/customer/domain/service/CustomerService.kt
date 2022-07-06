package com.lordrians.ojeksyariapi.customer.domain.service

import com.lordrians.ojeksyariapi.customer.data.entity.request.CustLoginReq
import com.lordrians.ojeksyariapi.customer.data.entity.request.CustRegisterReq
import com.lordrians.ojeksyariapi.customer.domain.entity.CustLoginRes
import com.lordrians.ojeksyariapi.customer.domain.entity.CustRegisterRes
import com.lordrians.ojeksyariapi.utils.Result

interface CustomerService {
    fun register(custRegisterReq: CustRegisterReq): Result<CustRegisterRes>
    fun login(custLoginReq: CustLoginReq): Result<CustLoginRes>
}