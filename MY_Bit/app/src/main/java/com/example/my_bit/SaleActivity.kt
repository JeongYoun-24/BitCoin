package com.example.my_bit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

class SaleActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener{
    private val binding by lazy { ActivitySaleBinding.inflate(layoutInflater) }

    lateinit var mAutn : FirebaseAuth
    private lateinit var mDBRef: DatabaseReference

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

        binding.saleBut.setOnClickListener(){
          val saleCoin=  binding.saleCoin.text.toString()  // 교환하는 코인량
            val  saleCoin2 : Int = saleCoin.toInt()
            Log.d("교환 갯수",saleCoin2.toString())

            val cointCount = binding.CoinCount.text.toString() // 들고있는 코인량
            val  cointCount2 : Int = cointCount.toInt()
            Log.d("코인량 ",cointCount2.toString())

            if(cointCount2 >saleCoin2){
                Log.d("교환 갯수","교환가능")
               /* val

                mDBRef.child("user").child("${userUID.toString()}").get().addOnSuccessListener {
                    val name = it.child("name").value

                    mDBRef.child("coin").child("${userUID.toString()}").setValue(UserBit(name.toString(),""))
                    mDBRef.child("point").child("${userUID.toString()}").setValue(BitPoint(0,"","${name.toString()}")*/
                }



            }else{
                Log.d("교환 갯수","교환가능한 코인 부족 ")
                Toast.makeText(this,"교환가능 코인 갯수 초과", Toast.LENGTH_LONG).show();
            }
            
            
        }

//        mDBRef.child("coin").child("${userId.toString()}").setValue(UserBit(total.toString(),""))




        // 네이게이션 이벤트
        binding.navi.setOnClickListener() {
            binding.layoutDrawer.openDrawer(GravityCompat.START) // START : left  END : right
        }
        binding.navieView.setNavigationItemSelectedListener(this)

    }

    // 네이게이션 메뉴 아이템 클릭시 수행 메서드
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        val intent2 = Intent(this, LoginActivity::class.java) // 메인 액티비티
        val intent3 = Intent(this, MainActivity::class.java) //
        val intent4 = Intent(this, BitActivity::class.java) //
        val intent5 = Intent(this, UserActivity::class.java) //
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
            R.id.main -> {
                val userUID = intent.getStringExtra("id")

                Log.d("1234",userUID.toString())
                intent3.putExtra("id",userUID.toString())

                startActivity(intent3)
            }
            R.id.Bit -> startActivity(intent4)

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