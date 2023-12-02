package com.example.my_bit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
import com.example.my_bit.databinding.ActivitySaleBinding
import com.example.my_bit.databinding.ActivitySignupBinding
import com.example.my_bit.`object`.BitLogin
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.Random
import java.util.Timer
import kotlin.concurrent.timer

class SaleActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener{
    private val binding by lazy { ActivitySaleBinding.inflate(layoutInflater) }

    lateinit var mAutn : FirebaseAuth
    private lateinit var mDBRef: DatabaseReference

    private var time = 0
    private var timerTask : Timer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 인증 초기화
        mAutn = Firebase.auth
        // DB 초기화
        mDBRef = Firebase.database.reference

        var userUID = intent.getStringExtra("id")

        Log.d("받은데이터값",userUID.toString())
        mDBRef.child("point").child("${userUID.toString()}").get().addOnSuccessListener {
            val count = it.child("count").value
            val point = it.child("point").value
            mDBRef.child("coin").child("${userUID.toString()}").get().addOnSuccessListener {
                val count = it.child("coin").value

                binding.CoinCount.text = count.toString()
            }


        }

        // 포인트 교환 버튼
        binding.saleBut.setOnClickListener(){
          val saleCoin=  binding.saleCoin.text.toString()  // 교환하는 코인량
            val  saleCoin2 : Int = saleCoin.toInt()
            Log.d("교환 갯수",saleCoin2.toString())

            val cointCount = binding.CoinCount.text.toString() // 들고있는 코인량
            val  cointCount2 : Int = cointCount.toInt()
            Log.d("코인량 ",cointCount2.toString())

            if(cointCount2 >saleCoin2){
                Log.d("교환 갯수","교환가능")
                var total :Int = 0
                val NewPoint = saleCoin2*100
                total = cointCount2-saleCoin2

                Log.d("코인량 변화","${total.toString()}")
                Log.d("포인트량","${NewPoint.toString()}")

                mDBRef.child("user").child("${userUID.toString()}").get().addOnSuccessListener {
                    val name = it.child("name").value

                    mDBRef.child("point").child("${userUID.toString()}").get().addOnSuccessListener {
                        val count = it.child("count").value
                        val count2 = count.toString()


                        val totalCount : Int
                        totalCount = count2.toInt()+saleCoin2.toInt()

                        Log.d("코인 카운터","${totalCount.toString()}")

                        val point = it.child("point").value
                        val point2 = point.toString()
                        val totalPoint : Int
                        totalPoint = point2.toInt()+NewPoint.toInt()

                        Log.d("포인트량2","${totalPoint.toString()}")
                        // 코인량 차감
                        mDBRef.child("coin").child("${userUID.toString()}").setValue(UserBit(total,"${name.toString()}"))
                        // 포인트 적립
                        mDBRef.child("point").child("${userUID.toString()}").setValue(BitPoint(totalCount,totalPoint,"${name.toString()}"))
                        finish();
                        // 액티비티 재시작
                        val intent5 = Intent(this, SaleActivity::class.java) //
                        val userUID = intent.getStringExtra("id")
                        intent5.putExtra("id",userUID.toString())
                        startActivity(intent5)
                        Toast.makeText(this, "${NewPoint.toString()}  포인트 전환 성공 ", Toast.LENGTH_LONG).show()
                    }




                }


            }else{
                Log.d("교환 갯수","교환가능한 코인 부족 ")
                Toast.makeText(this,"교환가능 코인 갯수 초과", Toast.LENGTH_LONG).show();
            }
            
            
        } // 버튼 End



        startsTime()






        // 네이게이션 이벤트
        binding.navi.setOnClickListener() {
            binding.layoutDrawer.openDrawer(GravityCompat.START) // START : left  END : right
        }
        binding.navieView.setNavigationItemSelectedListener(this)

    }

    var total :Int =0
    private fun startsTime() {

        timerTask = timer(period = 10){


            runOnUiThread{

                var random = Random()
                var num = random.nextInt(100)

                if(num >50){
                    total++
                    val number = 10000
                    Log.d("랜덤값2","${num}")
                    binding.dobak.text = total.toString()
                    val random = Random()
                    val num = random.nextInt(100)
                    if(num < 2){
                        total += 10
                        Log.d("럭키값2 ","${total}")
                        binding.dobak.text = total.toString()
                        val random = Random()
                        val num = random.nextInt( 5)
                        Log.d("랜덤값입니다 ","${num}")
                           if(num >=4){
                               total += 1000
                               Log.d("럭키값2 ","${total}")
                               binding.dobak.text = total.toString()

                           }


                    }else{

                        total --
                        Log.d("감소 ","${total}")
                        binding.dobak.text = total.toString()
                    }


                }else{
                    total--
                    val number = 10000

                    Log.d("랜덤값3","${num.toString()}")

                        if(total < 0){
                            total++
                            binding.dobak.text = total.toString()
                        }


                }





            }
        }

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

}