package com.example.my_bit.adpter



import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.my_bit.R
import com.example.my_bit.data.BitData
import com.example.my_bit.data.UpBitData


class BitAdapter(val profileList : ArrayList<UpBitData>) : RecyclerView.Adapter<BitAdapter.CustemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bit_list, parent, false)  // 연결될 액티비티

        return CustemViewHolder(view).apply {
//            val intent = Intent(parent.context, LoL_DetailActivity::class.java)

//            val gender = itemView.findViewById<ImageView>(R.id.iv_profile) // 성별

            // 리사이클 클릭시 이벤트
            itemView.setOnClickListener {
                val curPos : Int = adapterPosition // 현재 포지션
                val profile : UpBitData = profileList.get(curPos) // 객체형태로 전달

//                val bitmap = (gender.drawable as BitmapDrawable).bitmap

//                intent.putExtra("gender",bitmap)
//                intent.putExtra("id",profile.id)
//                intent.putExtra("name",profile.name)
//                intent.putExtra("lain",profile.lain)
//                intent.putExtra("detail",profile.detail)

//                ContextCompat.startActivity(parent.context,intent,null)
            }
        }
    }
    override fun getItemCount(): Int {
        return profileList.size
    }

    // 실제 연결해주는 메서드
    override fun onBindViewHolder(holder: CustemViewHolder, position: Int) {
        holder.BitId.text = profileList.get(position).id
        holder.BitName.text = profileList.get(position).name
        holder.symbol.text = profileList.get(position).symbol
        holder.Rank.text = profileList.get(position).rank.toString()
        holder.circulating_supply.text = profileList.get(position).circulating_supply.toString()
        holder.max_supply.text = profileList.get(position).max_supply.toString()
        holder.first_data_at.text = profileList.get(position).first_data_at
        holder.last_updated.text = profileList.get(position).last_updated

        holder.price.text = profileList.get(position).price.toString()
        holder.market_cap.text = profileList.get(position).market_cap.toString()
        holder.volume_24h.text = profileList.get(position).volume_24h.toString()
        holder.percent_change_24h.text = profileList.get(position).percent_change_24h.toString()
        holder.percent_change_7d.text = profileList.get(position).percent_change_7d.toString()

    }
    class CustemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val imgUser_icon = itemView.findViewById<ImageView>(R.id.imgUser_icon) // 이미지

        val BitId = itemView.findViewById<TextView>(R.id.id)        // 아이디
        val BitName = itemView.findViewById<TextView>(R.id.BitName) // 이름
        val symbol = itemView.findViewById<TextView>(R.id.symbol) // 기호
        val Rank = itemView.findViewById<TextView>(R.id.rank) // 순위
        val circulating_supply = itemView.findViewById<TextView>(R.id.circulating_supply) // 공급량
        val max_supply = itemView.findViewById<TextView>(R.id.max_supply) // 최대 공급량
        val first_data_at = itemView.findViewById<TextView>(R.id.first_data_at) // 생성일자
        val last_updated = itemView.findViewById<TextView>(R.id.last_updated) // 업데이트일자

        val price = itemView.findViewById<TextView>(R.id.price)        // 가격
        val market_cap = itemView.findViewById<TextView>(R.id.market_cap)   //총시가
        val volume_24h = itemView.findViewById<TextView>(R.id.volume_24h)  // 최근 24시간 거래량
        val percent_change_24h = itemView.findViewById<TextView>(R.id.percent_change_24h) // 변동 24시간
        val percent_change_7d = itemView.findViewById<TextView>(R.id.percent_change_7d)   // 변동 7일


    }

}