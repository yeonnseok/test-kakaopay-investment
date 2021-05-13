package com.test.kakaopay.investment.common

import java.math.BigDecimal
import java.math.RoundingMode
import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter(autoApply = true)
class BigDecimalRoundingConverter : AttributeConverter<BigDecimal, String> {

    companion object {
        const val SCALE_ZERO = 0
        val ROUNDING_MODE = RoundingMode.HALF_EVEN
    }

    override fun convertToDatabaseColumn(attribute: BigDecimal): String {
        return when (attribute.scale()) {
            SCALE_ZERO -> attribute.toString()
            else -> attribute.setScale(SCALE_ZERO, ROUNDING_MODE).toString()
        }
    }

    override fun convertToEntityAttribute(dbData: String): BigDecimal {
        return BigDecimal(dbData)
    }
}