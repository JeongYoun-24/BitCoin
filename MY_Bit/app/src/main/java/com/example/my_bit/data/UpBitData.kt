package com.example.my_bit.data

data class UpBitData(
    val id :String,  // id
    val rank : Int,  // 순위
    val name : String,  // 이름
    val symbol : String, // 기호
    val circulating_supply : Int, // 공급량
    val max_supply : Int, // 최대 공급량
    val first_data_at : String, // 생성일자
    val last_updated : String,  // 업데이트 일자

    val price : String,  // 가격
    val market_cap  :String, // 총시가
    val volume_24h : String, // 최근 24시간 거래량
    val percent_change_24h : String, // 최근 24시간 변동
    val percent_change_7d : String  // 최근 7일 변동

){
    data class KRW(
        val price : Int,  // 가격
        val market_cap  :Int, // 총시가
        val volume_24h : Int, // 최근 24시간 거래량
        val percent_change_24h : Int, // 최근 24시간 변동
        val percent_change_7d : Int  // 최근 7일 변동
    )


}

