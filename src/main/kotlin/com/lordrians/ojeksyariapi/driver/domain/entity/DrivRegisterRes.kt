package com.lordrians.ojeksyariapi.driver.domain.entity

import com.lordrians.ojeksyariapi.customer.data.entity.Customer
import com.lordrians.ojeksyariapi.driver.data.entity.Driver

data class DrivRegisterRes(
    var driver: Driver? = null,
    var accessToken: String? = null
)
