package com.example.my_bit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.my_bit.databinding.ActivityShopBinding
import com.example.my_bit.databinding.ActivityUserBinding
import com.example.my_bit.fragment.FragmentService
import com.example.my_bit.fragment.FragmentService2
import com.example.my_bit.fragment.FragmentService3

class ShopActivity : AppCompatActivity() { // 상점 액티비티

    private val binding by lazy { ActivityShopBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setFrag(0)

        binding.FragBut1.setOnClickListener(){
            Log.d("","버튼누름1")
            setFrag(0)
        }
        binding.FragBut2.setOnClickListener(){
            Log.d("","버튼누름2")
            setFrag(1)
        }
        binding.FragBut3.setOnClickListener(){
            Log.d("","버튼누름3")
            setFrag(2)
        }




    }


    private  fun setFrag(fragNum : Int){
        val ft = supportFragmentManager.beginTransaction()

        when(fragNum){
            0-> {
                ft.replace(R.id.shopFrag, FragmentService()).commit()
            }
            1-> {
                ft.replace(R.id.shopFrag, FragmentService2()).commit()
            }
            2-> {
                ft.replace(R.id.shopFrag, FragmentService3()).commit()
            }
        }
    }

}
