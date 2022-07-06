package com.lordrians.ojeksyariapi.customer.controller

import com.lordrians.ojeksyariapi.customer.data.entity.request.CustLoginReq
import com.lordrians.ojeksyariapi.customer.data.entity.request.CustRegisterReq
import com.lordrians.ojeksyariapi.customer.domain.entity.CustLoginRes
import com.lordrians.ojeksyariapi.customer.domain.entity.CustRegisterRes
import com.lordrians.ojeksyariapi.customer.domain.service.CustomerService
import com.lordrians.ojeksyariapi.utils.BaseResponse
import com.lordrians.ojeksyariapi.utils.toBaseRes
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/customer")
class CustomerController {

    @Autowired
    private lateinit var customerService: CustomerService

    @PostMapping("/register")
    fun register(
        @RequestBody custRegisterReq: CustRegisterReq
    ):  BaseResponse<CustRegisterRes> {
        return customerService.register(custRegisterReq).toBaseRes()
    }

    @PostMapping("/login")
    fun login(
        @RequestBody custLoginReq: CustLoginReq
    ): BaseResponse<CustLoginRes> {
        return customerService.login(custLoginReq).toBaseRes()
    }
}