package com.example.my_bit.data


import com.squareup.moshi.Json

class BitCoinData : ArrayList<BitCoinData.BitCoinDataItem>(){
    data class BitCoinDataItem(
        @Json(name = "beta_value")
        val betaValue: Double?,
        @Json(name = "circulating_supply")
        val circulatingSupply: Long?,
        @Json(name = "first_data_at")
        val firstDataAt: String?,
        @Json(name = "id")
        val id: String?,
        @Json(name = "last_updated")
        val lastUpdated: String?,
        @Json(name = "max_supply")
        val maxSupply: Long?,
        @Json(name = "name")
        val name: String?,
        @Json(name = "quotes")
        val quotes: Quotes?,
        @Json(name = "rank")
        val rank: Int?,
        @Json(name = "symbol")
        val symbol: String?,
        @Json(name = "total_supply")
        val totalSupply: Long?
    ) {
        data class Quotes(
            @Json(name = "KRW")
            val kRW: KRW?
        ) {
            data class KRW(
                @Json(name = "ath_date")
                val athDate: String?,
                @Json(name = "ath_price")
                val athPrice: Double?,
                @Json(name = "market_cap")
                val marketCap: Double?,
                @Json(name = "market_cap_change_24h")
                val marketCapChange24h: Double?,
                @Json(name = "percent_change_12h")
                val percentChange12h: Double?,
                @Json(name = "percent_change_15m")
                val percentChange15m: Double?,
                @Json(name = "percent_change_1h")
                val percentChange1h: Double?,
                @Json(name = "percent_change_1y")
                val percentChange1y: Double?,
                @Json(name = "percent_change_24h")
                val percentChange24h: Double?,
                @Json(name = "percent_change_30d")
                val percentChange30d: Double?,
                @Json(name = "percent_change_30m")
                val percentChange30m: Double?,
                @Json(name = "percent_change_6h")
                val percentChange6h: Double?,
                @Json(name = "percent_change_7d")
                val percentChange7d: Double?,
                @Json(name = "percent_from_price_ath")
                val percentFromPriceAth: Double?,
                @Json(name = "price")
                val price: Double?,
                @Json(name = "volume_24h")
                val volume24h: Double?,
                @Json(name = "volume_24h_change_24h")
                val volume24hChange24h: Double?
            )
        }
    }
}