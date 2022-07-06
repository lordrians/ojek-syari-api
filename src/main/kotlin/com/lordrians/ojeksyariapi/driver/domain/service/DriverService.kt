package com.lordrians.ojeksyariapi.driver.domain.service

import com.lordrians.ojeksyariapi.customer.data.entity.request.CustLoginReq
import com.lordrians.ojeksyariapi.customer.data.entity.request.CustRegisterReq
import com.lordrians.ojeksyariapi.customer.domain.entity.CustLoginRes
import com.lordrians.ojeksyariapi.customer.domain.entity.CustRegisterRes
import com.lordrians.ojeksyariapi.driver.data.entity.request.DrivLoginReq
import com.lordrians.ojeksyariapi.driver.data.entity.request.DrivRegisterReq
import com.lordrians.ojeksyariapi.driver.domain.entity.DrivLoginRes
import com.lordrians.ojeksyariapi.driver.domain.entity.DrivRegisterRes
import com.lordrians.ojeksyariapi.utils.Result

interface DriverService {
    fun register(drivRegisterReq: DrivRegisterReq): Result<DrivRegisterRes>
    fun login(drivLoginReq: DrivLoginReq): Result<DrivLoginRes>
}