package com.example.my_bit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
import com.example.my_bit.databinding.ActivityLoginBinding
import com.example.my_bit.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class LoginActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    lateinit var mAutn : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 인증 초기화
        mAutn = Firebase.auth

        // 로그인 버튼 이벤트
        binding.loginChkBtn.setOnClickListener(){
            val email  = binding.userEmail.text.toString()
            val password = binding.userPwd.text.toString()

            login(email,password)
        }



        // 회원가입 액티비티 로 이동
        binding.registerBtn.setOnClickListener(){
            val intent : Intent = Intent(this,SignupActivity::class.java)
            startActivity(intent)

        }

        // 네이게이션 이벤트
        binding.navi.setOnClickListener(){
            binding.layoutDrawer.openDrawer(GravityCompat.START) // START : left  END : right
        }
        binding.navieView.setNavigationItemSelectedListener(this)


    }
    // 로그인
        private fun login(email : String, password : String){
            mAutn.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                      // 성공시 실행
                        val intent : Intent = Intent(this@LoginActivity,MainActivity::class.java)
                        startActivity(intent)
                        finish() // 액티비티 닫기

                        Toast.makeText(this,"로그인 성공",Toast.LENGTH_SHORT).show()

                    } else {
                    // 실패시 실행
                        Toast.makeText(this,"로그인 실패", Toast.LENGTH_SHORT).show()
                        Log.d("","Error${task.exception}")

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
        when(item.itemId){
            R.id.login -> startActivity(intent)
            R.id.Bit -> startActivity(intent2)

        }
        binding.layoutDrawer.closeDrawers() //네이게이션 닫기
        return false
    }
}