package com.lordrians.ojeksyariapi.driver.data.entity.response

import com.lordrians.ojeksyariapi.customer.data.entity.Customer
import com.lordrians.ojeksyariapi.driver.data.entity.Driver

data class DrivRegisterResDTO(
    var driver: Driver? = null,
    var accessToken: String? = null
)
