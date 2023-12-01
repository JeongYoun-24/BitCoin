package com.example.my_bit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.my_bit.adpter.BitAdapter
import com.example.my_bit.data.UpBitData
import com.example.my_bit.databinding.ActivityBitBinding
import com.example.my_bit.databinding.ActivityLoginBinding
import com.example.my_bit.`object`.BitLogin
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset

class BitActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener  {
    private val binding by lazy { ActivityBitBinding.inflate(layoutInflater) }
    private val mRequestAddress = "https://bittrex.com/api/v1.1/public/getticker?market=USDT"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        binding.button.setOnClickListener(){
            val thread = NetworkThread()
            thread.start()
            thread.join()

        }

        // 네이게이션 이벤트
        binding.navi.setOnClickListener(){
            binding.layoutDrawer.openDrawer(GravityCompat.START) // START : left  END : right
        }
        binding.navieView.setNavigationItemSelectedListener(this)


    }


    // 네이게이션 메뉴 아이템 클릭시 수행 메서드
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        val intent2 = Intent(this, LoginActivity::class.java) // 메인 액티비티
        val intent3 = Intent(this, MainActivity::class.java) //
        val intent4 = Intent(this, BitActivity::class.java) //
        val intent5 = Intent(this, SaleActivity::class.java) //
        val intent6 = Intent(this, UserActivity::class.java) //

        when (item.itemId) {
            R.id.logout -> {
                FirebaseAuth.getInstance().signOut()
                BitLogin.clearUser(this)
                val intent : Intent = Intent(this,LoginActivity::class.java)
                startActivity(intent)
                val pref = getSharedPreferences("Prefs", 0)
                var editor = pref.edit();
                editor.clear();
                editor.commit();
            }
            R.id.user -> {
                val userUID = intent.getStringExtra("id")
                val pref = getSharedPreferences("Prefs", 0)
                //shared에 있는 'userEmail'이란 데이터를 불러온다는 뜻. 0 대신 MODE_PRIVATE라고 입력하셔도 됩니다.
                val savedEmail =pref.getString("email", "").toString()
                val savedPwd =pref.getString("pwd", "").toString()

                intent6.putExtra("id",userUID)
                intent6.putExtra("email",savedEmail)
                intent6.putExtra("password",savedPwd)

                startActivity(intent6)

            }
            R.id.main -> {
                val userUID = intent.getStringExtra("id")

                Log.d("1234",userUID.toString())
                intent3.putExtra("id",userUID.toString())

                startActivity(intent3)
            }
            R.id.Bit -> {
                val userUID = intent.getStringExtra("id")

                Log.d("1234",userUID.toString())
                intent4.putExtra("id",userUID.toString())
                startActivity(intent4)
            }

            R.id.Sale -> {
                val userUID = intent.getStringExtra("id")

                Log.d("1234",userUID.toString())
                intent5.putExtra("id",userUID.toString())

                startActivity(intent5)

            }

        }
        binding.layoutDrawer.closeDrawers() //네이게이션 닫기
        return false
    }


    fun onRequestCoinPrice(): Double {
        val url = URL(mRequestAddress)
        val conn = url.openConnection() as HttpURLConnection

        BufferedReader(InputStreamReader (conn.inputStream, Charset.forName("UTF-8"))).use { reader ->
            // Bid : 살 때,
            // Ask : 팔 때,
            // Last : 최근 거래 가격
            // {"success":true,"message":"","result":{"Bid":0.65900000,"Ask":0.65993000,"Last":0.65993000}}
            val response = reader.readLine()
            val json = JSONObject(response)
            val exchangePrice = (json["result"] as JSONObject).get("Last")

            return exchangePrice as Double
        }
    }

    inner class NetworkThread(): Thread(){ // 스레드로 url api 정보 받아오기
        override fun run() {



            val id = intent.getStringExtra("id")
            Log.d("sdasdsad",id.toString())

            // API 정보를 가지고 있는 주소
//            https://api.coinpaprika.com/v1/tickers?quotes=USD  //원화
            val site = "https://api.coinpaprika.com/v1/tickers?quotes=USD"  // 달러
            val url = URL(site)
            val conn = url.openConnection()
            val input = conn.getInputStream()
            val isr = InputStreamReader(input)
            // br: 라인 단위로 데이터를 읽어오기 위해서 만듦
            val br = BufferedReader(isr)
            // Json 문서는 일단 문자열로 데이터를 모두 읽어온 후, Json에 관련된 객체를 만들어서 데이터를 가져옴
            var str: String? = null
            val buf = StringBuffer()
            do{
                str = br.readLine()
                if(str!=null){ buf.append(str) }
            }while (str!=null)
            // 전체가 객체로 묶여있기 때문에 객체형태로 가져옴
            val root = JSONArray(buf.toString())
            Log.d("코인 데이터",root.toString())

            var profileList = arrayListOf(  // 리사이클뷰 더비 데이터
                UpBitData("",0,"","",0,0,"","","","" ,"","","")

            )
            val price :String = root.getJSONObject(0).getJSONObject("quotes").getJSONObject("USD").getInt("price").toString()+"원"
            Log.d("String 변환 ",price)
            val one : Int



            for(i in 0 until 5){
                var response = root.getJSONObject(i)
                val price :String = root.getJSONObject(0).getJSONObject("quotes").getJSONObject("USD").getInt("price").toString()+"$"
                val price2 :String =  root.getJSONObject(i).getJSONObject("quotes").getJSONObject("USD").getString("price").substring(0 until 7)
                Log.d("dsadasda",price2)
                profileList = arrayListOf(  // 리사이클뷰 더비 데이터
                    UpBitData("${root.getJSONObject(0).getString("id")}",1,"${root.getJSONObject(0).getString("name")}","${root.getJSONObject(0).getString("symbol")}", root.getJSONObject(0).getInt("circulating_supply"), root.getJSONObject(0).getInt("max_supply"), "${root.getJSONObject(0).getString("first_data_at")}", "${root.getJSONObject(0).getString("last_updated")}", price, root.getJSONObject(0).getJSONObject("quotes").getJSONObject("USD").getString("market_cap"), root.getJSONObject(0).getJSONObject("quotes").getJSONObject("USD").getString("volume_24h_change_24h"), root.getJSONObject(0).getJSONObject("quotes").getJSONObject("USD").getString("percent_change_24h")+"%", root.getJSONObject(0).getJSONObject("quotes").getJSONObject("USD").getString("percent_change_7d")+"%" ),
                    UpBitData("${root.getJSONObject(1).getString("id")}",2,"${root.getJSONObject(1).getString("name")}","${root.getJSONObject(1).getString("symbol")}", root.getJSONObject(1).getInt("circulating_supply"), root.getJSONObject(1).getInt("max_supply"), "${root.getJSONObject(1).getString("first_data_at")}", "${root.getJSONObject(1).getString("last_updated")}", root.getJSONObject(1).getJSONObject("quotes").getJSONObject("USD").getString("price").substring(0 until 7)+"$", root.getJSONObject(1).getJSONObject("quotes").getJSONObject("USD").getString("market_cap"), root.getJSONObject(1).getJSONObject("quotes").getJSONObject("USD").getString("volume_24h_change_24h"), root.getJSONObject(1).getJSONObject("quotes").getJSONObject("USD").getString("percent_change_24h")+"%", root.getJSONObject(1).getJSONObject("quotes").getJSONObject("USD").getString("percent_change_7d")+"%" ),
                    UpBitData("${root.getJSONObject(2).getString("id")}",3,"${root.getJSONObject(2).getString("name")}","${root.getJSONObject(2).getString("symbol")}", root.getJSONObject(2).getInt("circulating_supply"), root.getJSONObject(2).getInt("max_supply"), "${root.getJSONObject(2).getString("first_data_at")}", "${root.getJSONObject(2).getString("last_updated")}", root.getJSONObject(2).getJSONObject("quotes").getJSONObject("USD").getString("price").substring(0 until 7)+"$", root.getJSONObject(2).getJSONObject("quotes").getJSONObject("USD").getString("market_cap"), root.getJSONObject(2).getJSONObject("quotes").getJSONObject("USD").getString("volume_24h_change_24h"), root.getJSONObject(2).getJSONObject("quotes").getJSONObject("USD").getString("percent_change_24h")+"%", root.getJSONObject(2).getJSONObject("quotes").getJSONObject("USD").getString("percent_change_7d")+"%" ),
                    UpBitData("${root.getJSONObject(3).getString("id")}",4,"${root.getJSONObject(3).getString("name")}","${root.getJSONObject(3).getString("symbol")}", root.getJSONObject(3).getInt("circulating_supply"), root.getJSONObject(3).getInt("max_supply"), "${root.getJSONObject(3).getString("first_data_at")}", "${root.getJSONObject(3).getString("last_updated")}", root.getJSONObject(3).getJSONObject("quotes").getJSONObject("USD").getString("price").substring(0 until 7)+"$", root.getJSONObject(3).getJSONObject("quotes").getJSONObject("USD").getString("market_cap"), root.getJSONObject(3).getJSONObject("quotes").getJSONObject("USD").getString("volume_24h_change_24h"), root.getJSONObject(3).getJSONObject("quotes").getJSONObject("USD").getString("percent_change_24h")+"%", root.getJSONObject(3).getJSONObject("quotes").getJSONObject("USD").getString("percent_change_7d")+"%" ),
                    UpBitData("${root.getJSONObject(4).getString("id")}",5,"${root.getJSONObject(4).getString("name")}","${root.getJSONObject(4).getString("symbol")}", root.getJSONObject(4).getInt("circulating_supply"), root.getJSONObject(4).getInt("max_supply"), "${root.getJSONObject(4).getString("first_data_at")}", "${root.getJSONObject(4).getString("last_updated")}", root.getJSONObject(4).getJSONObject("quotes").getJSONObject("USD").getString("price").substring(0 until 7)+"$", root.getJSONObject(4).getJSONObject("quotes").getJSONObject("USD").getString("market_cap"), root.getJSONObject(4).getJSONObject("quotes").getJSONObject("USD").getString("volume_24h_change_24h"), root.getJSONObject(4).getJSONObject("quotes").getJSONObject("USD").getString("percent_change_24h")+"%", root.getJSONObject(4).getJSONObject("quotes").getJSONObject("USD").getString("percent_change_7d")+"%" ),
                    UpBitData("${root.getJSONObject(5).getString("id")}",6,"${root.getJSONObject(5).getString("name")}","${root.getJSONObject(5).getString("symbol")}", root.getJSONObject(5).getInt("circulating_supply"), root.getJSONObject(5).getInt("max_supply"), "${root.getJSONObject(5).getString("first_data_at")}", "${root.getJSONObject(5).getString("last_updated")}", root.getJSONObject(5).getJSONObject("quotes").getJSONObject("USD").getString("price").substring(0 until 7)+"$", root.getJSONObject(5).getJSONObject("quotes").getJSONObject("USD").getString("market_cap"), root.getJSONObject(5).getJSONObject("quotes").getJSONObject("USD").getString("volume_24h_change_24h"), root.getJSONObject(5).getJSONObject("quotes").getJSONObject("USD").getString("percent_change_24h")+"%", root.getJSONObject(5).getJSONObject("quotes").getJSONObject("USD").getString("percent_change_7d")+"%" ),
                    UpBitData("${root.getJSONObject(6).getString("id")}",7,"${root.getJSONObject(6).getString("name")}","${root.getJSONObject(6).getString("symbol")}", root.getJSONObject(6).getInt("circulating_supply"), root.getJSONObject(6).getInt("max_supply"), "${root.getJSONObject(6).getString("first_data_at")}", "${root.getJSONObject(6).getString("last_updated")}", root.getJSONObject(6).getJSONObject("quotes").getJSONObject("USD").getString("price").substring(0 until 7)+"$", root.getJSONObject(6).getJSONObject("quotes").getJSONObject("USD").getString("market_cap"), root.getJSONObject(6).getJSONObject("quotes").getJSONObject("USD").getString("volume_24h_change_24h"), root.getJSONObject(6).getJSONObject("quotes").getJSONObject("USD").getString("percent_change_24h")+"%", root.getJSONObject(6).getJSONObject("quotes").getJSONObject("USD").getString("percent_change_7d")+"%" ),
                    UpBitData("${root.getJSONObject(7).getString("id")}",8,"${root.getJSONObject(7).getString("name")}","${root.getJSONObject(7).getString("symbol")}", root.getJSONObject(7).getInt("circulating_supply"), root.getJSONObject(7).getInt("max_supply"), "${root.getJSONObject(7).getString("first_data_at")}", "${root.getJSONObject(7).getString("last_updated")}", root.getJSONObject(7).getJSONObject("quotes").getJSONObject("USD").getString("price").substring(0 until 7)+"$", root.getJSONObject(7).getJSONObject("quotes").getJSONObject("USD").getString("market_cap"), root.getJSONObject(7).getJSONObject("quotes").getJSONObject("USD").getString("volume_24h_change_24h"), root.getJSONObject(7).getJSONObject("quotes").getJSONObject("USD").getString("percent_change_24h")+"%", root.getJSONObject(7).getJSONObject("quotes").getJSONObject("USD").getString("percent_change_7d")+"%" ),
                    UpBitData("${root.getJSONObject(8).getString("id")}",9,"${root.getJSONObject(8).getString("name")}","${root.getJSONObject(8).getString("symbol")}", root.getJSONObject(8).getInt("circulating_supply"), root.getJSONObject(8).getInt("max_supply"), "${root.getJSONObject(8).getString("first_data_at")}", "${root.getJSONObject(8).getString("last_updated")}", root.getJSONObject(8).getJSONObject("quotes").getJSONObject("USD").getString("price").substring(0 until 7)+"$", root.getJSONObject(8).getJSONObject("quotes").getJSONObject("USD").getString("market_cap"), root.getJSONObject(8).getJSONObject("quotes").getJSONObject("USD").getString("volume_24h_change_24h"), root.getJSONObject(8).getJSONObject("quotes").getJSONObject("USD").getString("percent_change_24h")+"%", root.getJSONObject(8).getJSONObject("quotes").getJSONObject("USD").getString("percent_change_7d")+"%" ),
                    UpBitData("${root.getJSONObject(9).getString("id")}",10,"${root.getJSONObject(9).getString("name")}","${root.getJSONObject(9).getString("symbol")}", root.getJSONObject(9).getInt("circulating_supply"), root.getJSONObject(9).getInt("max_supply"), "${root.getJSONObject(9).getString("first_data_at")}", "${root.getJSONObject(9).getString("last_updated")}", root.getJSONObject(9).getJSONObject("quotes").getJSONObject("USD").getString("price").substring(0 until 7)+"$", root.getJSONObject(9).getJSONObject("quotes").getJSONObject("USD").getString("market_cap"), root.getJSONObject(9).getJSONObject("quotes").getJSONObject("USD").getString("volume_24h_change_24h"), root.getJSONObject(9).getJSONObject("quotes").getJSONObject("USD").getString("percent_change_24h")+"%", root.getJSONObject(9).getJSONObject("quotes").getJSONObject("USD").getString("percent_change_7d")+"%" ),
                    //  UpBitData("${root.getJSONObject(1).getString("id")}",1,"${root.getJSONObject(1).getString("name")}","${root.getJSONObject(1).getString("symbol")}", root.getJSONObject(1).getInt("circulating_supply"), root.getJSONObject(1).getInt("max_supply"), "${root.getJSONObject(1).getString("first_data_at")}", "${root.getJSONObject(1).getString("last_updated")}", root.getJSONObject(1).getJSONObject("quotes").getJSONObject("USD").getInt("price"), root.getJSONObject(1).getJSONObject("quotes").getJSONObject("USD").getInt("market_cap"), root.getJSONObject(1).getJSONObject("quotes").getJSONObject("USD").getInt("volume_24h"), root.getJSONObject(1).getJSONObject("quotes").getJSONObject("USD").getInt("percent_change_24h"), root.getJSONObject(1).getJSONObject("quotes").getJSONObject("USD").getInt("percent_change_7d") ),
                    //  UpBitData("${root.getJSONObject(1).getString("id")}",1,"${root.getJSONObject(1).getString("name")}","${root.getJSONObject(1).getString("symbol")}", root.getJSONObject(1).getInt("circulating_supply"), root.getJSONObject(1).getInt("max_supply"), "${root.getJSONObject(1).getString("first_data_at")}", "${root.getJSONObject(1).getString("last_updated")}", root.getJSONObject(1).getJSONObject("quotes").getJSONObject("USD").getInt("price"), root.getJSONObject(1).getJSONObject("quotes").getJSONObject("USD").getInt("market_cap"), root.getJSONObject(1).getJSONObject("quotes").getJSONObject("USD").getInt("volume_24h"), root.getJSONObject(1).getJSONObject("quotes").getJSONObject("USD").getInt("percent_change_24h"), root.getJSONObject(1).getJSONObject("quotes").getJSONObject("USD").getInt("percent_change_7d") ),

                )

            }
//            Log.d("데이터확인",root.getJSONObject(0).getJSONObject("quotes").getJSONObject("USD").getString("volume_24h").toString())
            Log.d("데이터확인",root.getJSONObject(0).getJSONObject("quotes").getJSONObject("USD").getInt("price").toString())
            Log.d("데이터확인",root.getJSONObject(0).getJSONObject("quotes").getJSONObject("USD").getString("volume_24h").toString())
            Log.d("데이터확인2",root.getJSONObject(0).getJSONObject("quotes").getJSONObject("USD").getString("volume_24h_change_24h").toString())
            Log.d("데이터확인3",root.getJSONObject(0).getJSONObject("quotes").getJSONObject("USD").getString("market_cap").toString())
            Log.d("데이터확인4",root.getJSONObject(0).getJSONObject("quotes").getJSONObject("USD").getString("market_cap_change_24h").toString())

            var response = JSONObject()
            var response2 = JSONObject()
            var response3 = JSONObject()
            // 화면에 출력
            runOnUiThread {

                Log.d("코인데이터3 name",profileList.toString())
                binding.BiyList.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL,false)
                binding.BiyList.setHasFixedSize(true)
                binding.BiyList.adapter = BitAdapter(profileList)



            }
        }
    }
    // 함수를 통해 데이터를 불러온다.
    fun JSON_Parse(obj: JSONObject, data : String): String {
        // 원하는 정보를 불러와 리턴받고 없는 정보는 캐치하여 "없습니다."로 리턴받는다.
        return try { obj.getString(data)
        } catch (e: Exception) {
            "없습니다."
        }
    }
}