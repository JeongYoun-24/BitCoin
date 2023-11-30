package com.example.my_bit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
import com.example.my_bit.databinding.ActivitySignupBinding
import com.google.android.material.navigation.NavigationView

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignupActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySignupBinding.inflate(layoutInflater) }

    lateinit var mAutn : FirebaseAuth
    private lateinit var mDBRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        // 인증 초기화
        mAutn = Firebase.auth
        // DB 초기화
        mDBRef = Firebase.database.reference


        binding.SignUpBtn.setOnClickListener(){

            val name = binding.SignUpName.text.toString().trim() // 공백제거
            val email = binding.SignUpEmail.text.toString().trim() // 공백 제거
            val password = binding.SignUpPwd.text.toString().trim() // 공백 제거
            val password2 = binding.SignUpPwd2.text.toString().trim() // 공백 제거

            if(password == password2){
                signUp(name,email,password)
            }

            signUp(name,email,password)
            Log.d("회원가입 데이터 값 ","들어왔는지 확인")
        }





    }
    // 회원 가입
    private fun signUp (name : String,email :String,password : String){
        mAutn.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                   // 성공시 실행
                    Log.d("회원가입 데이터 값 ","들어왔는지 확인")
                    val intent : Intent = Intent(this@SignupActivity,LoginActivity::class.java)
                    startActivity(intent)
                    addUserDatabase(name,email,password,mAutn.currentUser?.uid.toString())

                    mDBRef.child("coin").child("${ mAutn.currentUser?.uid.toString()}").setValue(BitPoint(0,0,"${name.toString()}"))

                    Log.d("회원가입 데이터 값 ","${mAutn.currentUser?.uid!!}")
                } else {
                   // 실패시 실행
                    Log.d("실패","실패??")
                    Toast.makeText(this,"회원가입 실패",Toast.LENGTH_SHORT).show()

                }
            }

    }
    private fun addUserDatabase(name :String,email: String, uId:String,password: String){
        val user = User(name,email,uId)
        mDBRef.child("user").child(uId).setValue(User(name,email,password))
        Log.d("값확인","들어왔나요")
    }




}