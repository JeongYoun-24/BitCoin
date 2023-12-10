package com.example.my_bit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.my_bit.adpter.FriendAdapter
import com.example.my_bit.adpter.FriendListAdapter
import com.example.my_bit.data.FriendData
import com.example.my_bit.data.FriendListData
import com.example.my_bit.databinding.ActivityMainBinding
import com.example.my_bit.databinding.ActivityUserBinding
import com.example.my_bit.`object`.BitLogin
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UserActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val binding by lazy { ActivityUserBinding.inflate(layoutInflater) }

    lateinit var mAutn : FirebaseAuth
    private lateinit var mDBRef: DatabaseReference

    lateinit var adapter : FriendListAdapter
    lateinit var userList : ArrayList<FriendListData>
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


        userList = ArrayList()

        adapter = FriendListAdapter(this,userList)

        binding.FriendUser.layoutManager  = LinearLayoutManager(this)
        binding.FriendUser.adapter = adapter


        mDBRef.child("user").child("${uId.toString()}").child("friend").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(postSnapshot in snapshot.children){
                    // 유저 정보
                    val currentUser = postSnapshot.getValue(FriendListData::class.java)

                    Log.d("???","${currentUser!!}")

//                    userList.add(currentUser!!)
                    if(mAutn.currentUser?.uid != currentUser?.uid){

                        userList.add(currentUser!!)
                        Log.d("갑확인요", "${userList}")
                    }

                }
                adapter.notifyDataSetChanged()

            }
            override fun onCancelled(error: DatabaseError) {
            }

        })




        mDBRef.child("point").child("${uId.toString()}").get().addOnSuccessListener {
            val count = it.child("count").value
            val name = it.child("name").value
            val point = it.child("point").value

            binding.CoinCount.text = count.toString()
            binding.name.text = name.toString()
            binding.point.text = point.toString()
            binding.email.text = email.toString()

            mDBRef.child("coin").child("${uId.toString()}").get().addOnSuccessListener {
                val coin = it.child("coin").value

                binding.Coin.text = coin.toString()
            }

        }



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