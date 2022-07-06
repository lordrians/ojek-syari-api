package com.lordrians.ojeksyariapi.customer.data.entity.response

import com.lordrians.ojeksyariapi.customer.data.entity.Customer

data class CustRegisterResDTO(
    var customer: Customer? = null,
    var accessToken: String? = null
)
