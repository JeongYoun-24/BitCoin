package com.example.my_bit

import android.content.Intent
import android.content.IntentSender.OnFinished
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.support.test.espresso.Espresso
import android.text.Editable
import android.util.Log
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.my_bit.adpter.BitAdapter
import com.example.my_bit.data.BitData
import com.example.my_bit.data.UpBitData
import com.example.my_bit.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset
import java.util.Timer
import java.util.TimerTask
import kotlin.concurrent.timer
import kotlin.concurrent.timerTask

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val mRequestAddress = "https://bittrex.com/api/v1.1/public/getticker?market=USDT"

    lateinit var countDownTime : CountDownTimer
    lateinit var mTimer : Timer

//    var timeRunning = false
//    var firstState = false
//
//    var time = 0L
//    var tempTime = 0L

    private var time = 0
    private var timerTask :Timer? = null
    private var inputData : String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        binding.BitBut.setOnClickListener(){
            startsTime()
        }

        binding.BitCoin.isClickable =false
        binding.BitCoin.isFocusable = false



      var timer =  Timer();
      var time = timerTask {  }

        timer.schedule(time, 0, 1000);







        // 네이게이션 이벤트
        binding.navi.setOnClickListener() {
            binding.layoutDrawer.openDrawer(GravityCompat.START) // START : left  END : right
        }
        binding.navieView.setNavigationItemSelectedListener(this)


    }
    var hour = 1  // 시
    var minute = 2 // 분
    var seconu = 59  // 초
    var dogi = 1
    
//    스레드로 타임어택
    private fun startsTime(){
        timerTask = timer(period = 10){
            time++
            hour
            minute
            seconu--
            val sec = time/60
            val milli = -time %60


            runOnUiThread{
//                Log.d("타이머",seconu.toString())
                if(seconu<0){
                    binding.BitBut.isEnabled = false
                    seconu = 60
                    minute --
//                    Log.d("sasadas",seconu.toString())
                    binding.Time.text =" ${hour} :  ${minute} : ${seconu}"
                        if(minute<1){
                            seconu = 60
                            minute = 10
                            hour--
                            binding.Time.text =" ${hour} :  ${minute} : ${seconu}"
                        }
                            else if(hour<1){
                                seconu = 60
                                minute --
                                hour = 0
                                binding.Time.text =" ${hour} :  ${minute} : ${seconu}"

                            }
                                if( hour<1 ||minute <0){
                                    seconu = 60
                                    minute = 0
                                    hour = 0
                                    binding.Time.text =" ${hour} :  ${minute} : ${seconu}"
                                    cancel()
                                }
                                    if( hour<1 ||minute <1||seconu<1){
                                        seconu = 59
                                        minute = 10
                                        hour = 1
                                        binding.Time.text =" ${hour} :  ${minute} : ${seconu}"
                                        timerTask?.cancel()
                                        dogi ++
                                        binding.BitCoin.text = Editable.Factory.getInstance().newEditable(dogi.toString())
                                        binding.BitBut.isEnabled = true
                                    }

                }else{
                    binding.Time.text =" ${hour} :  ${minute} : ${seconu}"
                }


            }
        }

    }



    // 네이게이션 메뉴 아이템 클릭시 수행 메서드
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val intent = Intent(this, LoginActivity::class.java) // 로그인 액티비티
        val intent2 = Intent(this, MainActivity::class.java) // 메인 액티비티
//        val intent3 = Intent(this, My_Champion::class.java) // 챔피언 스펠 액티비티
//        val intent4 = Intent(this, My_RuneActivity::class.java) // 챔피언 룬 액티비티
//        val intent5 = Intent(this, My_ItemListActivity::class.java) // 챔피언 룬 액티비티
        when (item.itemId) {
            R.id.login -> startActivity(intent)
            R.id.Bit -> startActivity(intent2)

        }
        binding.layoutDrawer.closeDrawers() //네이게이션 닫기
        return false
    }

    private fun funTime(){
        mTimer = kotlin.concurrent.timer(period = 2000){
            runOnUiThread{
                binding.Time.text = "1"
            }

        }

    }


   /* private fun startTime(){
        if(firstState){
            val Hour = binding.Time.text.toString()

            time = (Hour.toLong()*360000)
        }else{

        }

        countDownTime = object  : CountDownTimer(time, 1000){
            override fun onTick(millisUntilFinished: Long) {
                tempTime = millisUntilFinished
            }

            override fun onFinish() {}
        }.start()


    }*/






}


