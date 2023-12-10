package com.example.my_bit

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.my_bit.adpter.FriendAdapter
import com.example.my_bit.adpter.UserAdapter
import com.example.my_bit.data.FriendData
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
import java.util.Timer
import java.util.TimerTask
import kotlin.concurrent.timer
import kotlin.concurrent.timerTask

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val mRequestAddress = "https://bittrex.com/api/v1.1/public/getticker?market=USDT"

    lateinit var countDownTime : CountDownTimer
    lateinit var mTimer : Timer

    private var time = 0
    private var timerTask :Timer? = null


    lateinit var mAutn : FirebaseAuth
    private lateinit var mDBRef: DatabaseReference

    lateinit var adapter : FriendAdapter
    lateinit var userList : ArrayList<FriendData>


//    private val dlg = Dialog(this)


//    var uId = intent.getStringExtra("id")
    var count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 인증 초기화
        mAutn = Firebase.auth
        // DB 초기화
        mDBRef = Firebase.database.reference

        var userUID = intent.getStringExtra("id")

        Log.d("받은데이터 ",userUID.toString())

        userList = ArrayList()

        adapter = FriendAdapter(this,userList)

        binding.friendList.layoutManager  = LinearLayoutManager(this)
        binding.friendList.adapter = adapter


        if( mDBRef.child("friend").child("${userUID.toString()}") == null){
            binding.friendList.setVisibility(View.INVISIBLE) // 감추기
            binding.request2.setVisibility(View.INVISIBLE)

        }


        if( mDBRef.child("friend").child("${userUID.toString()}") != null){
            binding.friendList.setVisibility(View.INVISIBLE) // 감추기
            binding.request.setVisibility(View.INVISIBLE)

        }

        binding.request2.setOnClickListener(){
            val intent : Intent = Intent(this,FriendRequestActivity::class.java)

            binding.friendList.setVisibility(View.VISIBLE)  // 보이기

                binding.request.setVisibility(View.VISIBLE)
                binding.request2.setVisibility(View.INVISIBLE)

                mDBRef.child("friend").addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for(postSnapshot in snapshot.children){
                            // 유저 정보
                            val currentUser = postSnapshot.getValue(FriendData::class.java)
                            Log.d("친구추가 요청 uId", currentUser?.uid.toString())

                            Log.d("친구요청 받는사람 uId", mAutn?.uid.toString())


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





            binding.request.setOnClickListener(){
                binding.friendList.setVisibility(View.INVISIBLE)
                binding.request2.setVisibility(View.VISIBLE)
            }




//            val mDialogView = LayoutInflater.from(this).inflate(R.layout.friend_request, null)
//            val mBuilder = AlertDialog.Builder(this).setView(mDialogView).setTitle("최근 알림")
//            intent.putExtra("id",userUID)
//            startActivity(intent)

//            Log.d("","${intent.getStringExtra("id")}")
//            mBuilder.show()


           /* val mDialogView = LayoutInflater.from(this).inflate(R.layout.friend_request, null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
                .setTitle("최근 알림")
//            mBuilder.show()
            val  mAlertDialog = mBuilder.show()

            val mDialogView2 = mDialogView

            val noButton = mDialogView.findViewById<Button>(R.id.but1)
            noButton.setOnClickListener {
                mAlertDialog.dismiss()
            }
            userList = ArrayList()
            adapter = FriendAdapter(this,userList)*/







        }






        val pref = getSharedPreferences("Prefs", 0)
        //shared에 있는 'userEmail'이란 데이터를 불러온다는 뜻. 0 대신 MODE_PRIVATE라고 입력하셔도 됩니다.
        val savedEmail =pref.getString("email", "").toString()
        val savedPwd =pref.getString("pwd", "").toString()


        // 상점 액티비티 이동
        binding.shopBut.setOnClickListener(){
            val intent : Intent = Intent(this,ShopActivity::class.java)
            intent.putExtra("id",userUID.toString())
            startActivity(intent)

        }

        // 메인 홈 버튼
        binding.MainBut.setOnClickListener(){
            val intent : Intent = Intent(this,MainActivity::class.java)
            intent.putExtra("id",userUID.toString())
            startActivity(intent)

        }

        // 개인정보 액티비티로 연결
        binding.UserBut.setOnClickListener(){
            val intent : Intent = Intent(this,UserActivity::class.java)
            intent.putExtra("id",userUID)
            intent.putExtra("email",savedEmail)
            intent.putExtra("password",savedPwd)

            startActivity(intent)
        }

        binding.userId.setText(BitLogin.getUserId(this))
        // 파이베이스에서 데이터 불러오기


        if(userUID != null){
            mDBRef.child("user").child(userUID.toString()).get().addOnSuccessListener {
                var name =     it.child("name").value

                binding.userId.text = name.toString()
                Log.d("데이터받은결과값 1", "${name}")
                Log.d("데이터받은결과값 ㅕㄴㄷㄱ", "Got value ${it.value}")

                mDBRef.child("coin").child(userUID.toString()).get().addOnSuccessListener {
                    var bit = it.child("coin").value

                    binding.BitCoin.text = Editable.Factory.getInstance().newEditable(bit.toString())

                }
            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }

        }






        // 코인 채굴
        binding.BitBut.setOnClickListener(){
            startsTime(userUID.toString())
            binding.BitBut.isEnabled = false
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

    //복원시키는 함수
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        count = savedInstanceState.getInt("count")
       binding.BitCoin.setText("$count")
//        editView.setText(savedInstanceState.getString("edit"))
    }

    //데이터를 저장시키는 함수
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("count", count)
        outState.putString("edit", binding.BitCoin.text.toString())
    }



    var hour = 1  // 시
    var minute = 10 // 분
    var seconu = 59  // 초

    var hour2 = 1  // 시
    var minute2 = 10 // 분
    var seconu2 = 59  // 초

    var coin : Int = 0


    var dogi = ""
    var total :Int =0
    
//    스레드로 타임어택
    private fun startsTime(userId: String) {
        coin = 0
        timerTask = timer(period = 10){
            time++
            hour
            minute
            seconu--
            val sec = time/60
            val milli = -time %60


            runOnUiThread{

                if(seconu<0){
                    binding.BitBut.isEnabled = false
                    seconu = 60
                    minute --
//                    Log.d("sasadas",seconu.toString())
                    binding.Time.text ="  ${minute}분 : ${seconu}초"
                    if(minute<1){
                        seconu = 59
                        minute = 10
//                        hour--
                        binding.Time.text ="  ${minute}분 : ${seconu}초"
                        timerTask?.cancel()
                        coin ++
                        dogi = coin.toString()
                        mDBRef.child("coin").child("${userId.toString()}").get().addOnSuccessListener {
                            var bit = it.child("coin").value
                            val bit2 = bit.toString()
                            val bit3 : Int =bit2.toInt()

                            total = bit3+coin


                            binding.BitCoin.text = Editable.Factory.getInstance().newEditable(total.toString())
                            mDBRef.child("coin").child("${userId.toString()}").setValue(UserBit(total,""))
                        }

//                        binding.BitCoin.text = Editable.Factory.getInstance().newEditable(dogi.toString())
                        binding.BitBut.isEnabled = true





                    }
                }else{
                    binding.Time.text ="  ${minute}분  : ${seconu}초"
                }



//                Log.d("타이머",seconu.toString())




                        /*     if(hour<1){ // 시간이 1보다 작으면
                                seconu = 59
//                                minute --
                                hour = 0
                                binding.Time.text =" ${hour} :  ${minute} : ${seconu}"
                                 Log.d("시간이 1보다 낮으면 ","지나갑니다.")
                            }
                                if( hour <1  || minute <1){
                                    seconu = 59
                                    minute = 10
                                    hour = 0
                                    binding.Time.text =" ${hour} :  ${minute} : ${seconu}"
                                    Log.d("시간이 과 분이 1보다 낮으면  ","지나갑니다.")
//                                    cancel()
                                }
                                    if( hour==0 || minute <=0 && seconu==0){
                                        seconu = 59
                                        minute = 10
                                        hour = 1
                                        binding.Time.text =" ${hour} :  ${minute} : ${seconu}"
                                        timerTask?.cancel()
                                        dogi ++
                                        binding.BitCoin.text = Editable.Factory.getInstance().newEditable(dogi.toString())
                                        binding.BitBut.isEnabled = true
                                        Log.d("시간이 0과같고 분이 1보다 낮으면  ","지나갑니다.")
                                    }*/




            }
        }

    }



    // 네이게이션 메뉴 아이템 클릭시 수행 메서드
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        val intent2 = Intent(this, LoginActivity::class.java) // 메인 액티비티
        val intent3 = Intent(this, MainActivity::class.java) //
        val intent4 = Intent(this, BitActivity::class.java) //
        val intent5 = Intent(this, SaleActivity::class.java) //
        val intent6 : Intent = Intent(this,UserActivity::class.java)
        val intent7 : Intent = Intent(this,UserListActivity::class.java)
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
            R.id.userList -> {
                val userUID = intent.getStringExtra("id")
                val pref = getSharedPreferences("Prefs", 0)
                //shared에 있는 'userEmail'이란 데이터를 불러온다는 뜻. 0 대신 MODE_PRIVATE라고 입력하셔도 됩니다.
                val savedEmail =pref.getString("email", "").toString()
                val savedPwd =pref.getString("pwd", "").toString()

                intent6.putExtra("id",userUID)
                intent6.putExtra("email",savedEmail)
                intent6.putExtra("password",savedPwd)

                startActivity(intent7)
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




