package com.lordrians.ojeksyariapi.driver.controller

import com.lordrians.ojeksyariapi.customer.data.entity.request.CustLoginReq
import com.lordrians.ojeksyariapi.customer.data.entity.request.CustRegisterReq
import com.lordrians.ojeksyariapi.customer.domain.entity.CustLoginRes
import com.lordrians.ojeksyariapi.customer.domain.entity.CustRegisterRes
import com.lordrians.ojeksyariapi.driver.data.entity.request.DrivLoginReq
import com.lordrians.ojeksyariapi.driver.data.entity.request.DrivRegisterReq
import com.lordrians.ojeksyariapi.driver.domain.entity.DrivLoginRes
import com.lordrians.ojeksyariapi.driver.domain.entity.DrivRegisterRes
import com.lordrians.ojeksyariapi.driver.domain.service.DriverService
import com.lordrians.ojeksyariapi.utils.BaseResponse
import com.lordrians.ojeksyariapi.utils.toBaseRes
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/driver")
class DriverController {
    @Autowired
    private lateinit var driverService: DriverService

    @PostMapping("/register")
    fun register(
        @RequestBody drivRegisterReq: DrivRegisterReq
    ): BaseResponse<DrivRegisterRes> {
        return driverService.register(drivRegisterReq).toBaseRes()
    }

    @PostMapping("/login")
    fun login(
        @RequestBody drivLoginReq: DrivLoginReq
    ): BaseResponse<DrivLoginRes> {
        return driverService.login(drivLoginReq).toBaseRes()
    }
}