package com.example.my_bit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.GravityCompat
import com.example.my_bit.databinding.ActivityMainBinding
import com.example.my_bit.databinding.ActivityUserBinding
import com.example.my_bit.`object`.BitLogin
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UserActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val binding by lazy { ActivityUserBinding.inflate(layoutInflater) }

    lateinit var mAutn : FirebaseAuth
    private lateinit var mDBRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val uId = intent.getStringExtra("id")
        val email = intent.getStringExtra("email")
        val password = intent.getStringExtra("password")

        // 인증 초기화
        mAutn = Firebase.auth
        // DB 초기화
        mDBRef = Firebase.database.reference

        binding.email.text = email.toString()





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
            R.id.main -> startActivity(intent3)
            R.id.Bit -> startActivity(intent4)

        }
        binding.layoutDrawer.closeDrawers() //네이게이션 닫기
        return false
    }
}