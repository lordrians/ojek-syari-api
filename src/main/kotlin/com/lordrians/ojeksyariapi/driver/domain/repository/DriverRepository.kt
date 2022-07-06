package com.lordrians.ojeksyariapi.driver.domain.repository

import com.lordrians.ojeksyariapi.driver.data.entity.request.DrivLoginReq
import com.lordrians.ojeksyariapi.driver.data.entity.request.DrivRegisterReq
import com.lordrians.ojeksyariapi.driver.data.entity.response.DrivLoginResDTO
import com.lordrians.ojeksyariapi.driver.data.entity.response.DrivRegisterResDTO
import com.lordrians.ojeksyariapi.utils.Result

interface DriverRepository {
    fun register(drivRegisterReq: DrivRegisterReq): Result<DrivRegisterResDTO>
    fun login(drivLoginReq: DrivLoginReq): Result<DrivLoginResDTO>
}