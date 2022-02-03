package com.github.njustus.stockdashboard.dtos

import java.time.OffsetDateTime

case class ExchangeData(isin: String,
                        val price: BigDecimal,
                        val time: OffsetDateTime)
