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

class SignupActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener  {
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

            signUp(name,email,password)
            Log.d("회원가입 데이터 값 ","들어왔는지 확인")
        }


        // 네이게이션 이벤트
        binding.navi.setOnClickListener(){
            binding.layoutDrawer.openDrawer(GravityCompat.START) // START : left  END : right
        }
        binding.navieView.setNavigationItemSelectedListener(this)


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
                    addUserDatabase(name,email, mAutn.currentUser?.uid.toString())

                    Log.d("회원가입 데이터 값 ","${mAutn.currentUser?.uid!!}")
                } else {
                   // 실패시 실행
                    Log.d("실패","실패??")
                    Toast.makeText(this,"회원가입 실패",Toast.LENGTH_SHORT).show()

                }
            }

    }
    private fun addUserDatabase(name :String,email: String,uId: String){
        val user = User(name,email,uId)
        mDBRef.child("user").child(uId).setValue(User(name,email,uId))
        Log.d("값확인","들어왔나요")
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