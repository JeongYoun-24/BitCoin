package com.example.my_bit

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
import com.example.my_bit.databinding.ActivityLoginBinding
import com.example.my_bit.databinding.ActivityMainBinding
import com.example.my_bit.`object`.BitLogin
import com.google.android.material.navigation.NavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class LoginActivity : AppCompatActivity(){
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    lateinit var mAutn : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 인증 초기화
        mAutn = Firebase.auth


        val prefs : SharedPreferences = this.getSharedPreferences("Prefs", Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit() // 데이터 기록을 위한 editor

        val pref = getSharedPreferences("Prefs", 0)
        val savedEmail =pref.getString("email", "").toString()
        val savedPwd =pref.getString("pwd", "").toString()
        Log.d("데이터확인ㅕㄴㄷㄱ",savedEmail)
        

        if(savedEmail.isEmpty()){

            Log.d("시시시시",savedEmail)
            Log.d("발바바바발",savedPwd)
        }else{
            val pref = getSharedPreferences("Prefs", 0)
            val savedEmail =pref.getString("email", "").toString()
            val savedPwd =pref.getString("pwd", "").toString()
            Log.d("12312",savedEmail)
            Log.d("12312",savedPwd)
            Login(savedEmail,savedPwd)

        }

        // 로그인 버튼 이벤트
        binding.loginChkBtn.setOnClickListener(){
            BitLogin.setUserId(this, binding.userEmail.text.toString())
            BitLogin.setUserPass(this, binding.userPwd.text.toString())
            val email  = binding.userEmail.text.toString()
            val password = binding.userPwd.text.toString()
            editor.putString("email", "${email}");
            editor.putString("pwd", "${password}");
            editor.commit();

            Login(email,password)
        }




        // 회원가입 액티비티 로 이동
        binding.registerBtn.setOnClickListener(){
            val intent : Intent = Intent(this,SignupActivity::class.java)
            startActivity(intent)

        }



    }
    fun Login(email : String, password : String) {
                mAutn.signInWithEmailAndPassword(email.toString(), password.toString()).addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // 성공시 실행
                            val intent: Intent =
                                Intent(this@LoginActivity, MainActivity::class.java)
                            intent.putExtra("id", mAutn.currentUser?.uid)
                            Log.d("UUID값 확인","${mAutn.currentUser?.uid}")
                            startActivity(intent)

                            Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()

                        } else {
                            // 실패시 실행
                            Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
                            Log.d("", "Error${task.exception}")

                        }



//                Toast.makeText(this, "${BitLogin.getUserId(this)}님 로그인 되었습니다.", Toast.LENGTH_SHORT).show()
//                    var intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)


        }
    }

    // 로그인
        private fun login(email : String, password : String){
        binding.loginChkBtn.setOnClickListener(){
            mAutn.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // 성공시 실행
                        val intent : Intent = Intent(this@LoginActivity,MainActivity::class.java)
                        intent.putExtra("id",mAutn.currentUser?.uid)
                        startActivity(intent)

                        Toast.makeText(this,"로그인 성공",Toast.LENGTH_SHORT).show()

                    } else {
                        // 실패시 실행
                        Toast.makeText(this,"로그인 실패", Toast.LENGTH_SHORT).show()
                        Log.d("","Error${task.exception}")

                    }
                }

        }


        }




}