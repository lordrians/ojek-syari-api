package com.lordrians.ojeksyariapi.customer.domain.entity

import com.lordrians.ojeksyariapi.customer.data.entity.Customer

data class CustRegisterRes(
    var customer: Customer? = null,
    var accessToken: String? = null
)
