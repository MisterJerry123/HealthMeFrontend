package com.example.alomtest.exercise.startpage

import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.example.alomtest.databinding.DoingExerciseSetItemFooterBinding
import com.example.alomtest.databinding.DoingExerciseSetListBinding
import com.example.alomtest.exercise.custompage01.set_list_item


class set_list_adapter( val context: Context, val setlist:ArrayList<set_list_item>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    //private lateinit var viewmodel : MyViewModel
    private val ITEM_VIEW_TYPE_NORMAL = 0
    private val ITEM_VIEW_TYPE_FOOTER = 1
    private var remainingTimeMillis: Long = 0
    private var isTimerRunning = false

    var onFooterClickListener: (() -> Unit)? = null
    var onMinusClickListener: (() -> Unit)?=null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_NORMAL -> {
                val binding = DoingExerciseSetListBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                set_list_adapter.set_list_viewholder(setlist,binding)
            }
            ITEM_VIEW_TYPE_FOOTER -> {
                val binding = DoingExerciseSetItemFooterBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                set_list_adapter.set_list_footer_viewholder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1) {
            ITEM_VIEW_TYPE_FOOTER
        } else {
            ITEM_VIEW_TYPE_NORMAL
        }
    }

    override fun getItemCount(): Int {
        return setlist.size+1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //viewmodel = ViewModelProvider(this).get(MyViewModel::class.java)

        when (getItemViewType(position)) {
            ITEM_VIEW_TYPE_NORMAL -> {
                val setlistHolder = holder as set_list_adapter.set_list_viewholder


                setlistHolder.binding.completeBtn.setOnCheckedChangeListener { buttonView, isChecked ->

                    if(isChecked){
                        startTimer()
                    }

                }



                setlistHolder.bind(setlist[position],position)

//                setlistHolder.itemView.setOnClickListener {
//                    onMinusClickListener?.invoke()
//                    Log.d("삭제직전", setlist.toString())
//                    Log.d("삭제할 인덱스",position.toString())
//                    setlist.removeAt(position)
//                    Log.d("삭제이후", setlist.toString())
//
//                    notifyDataSetChanged()
//                }
//                setlistHolder.binding.minusBtn.setOnClickListener {
//                    onMinusClickListener?.invoke()
//                    Log.d("삭제직전", setlist.toString())
//                    Log.d("삭제할 인덱스",position.toString())
//
//                    setlist.removeAt(position)
////
////                    for (i:Int in position until setlist.size-1){
////                        setlist[i]=setlist[i+1]
////
////
////
////                    }
//
//                    //setlist.removeAt(setlist.size-1)
//
//                    //notifyItemRemoved(position)
//                    notifyDataSetChanged()
//
//
//
//                    Log.d("삭제이후", setlist.toString())
//
////                    if(setlistHolder.setlist.size==0){
////
////                        viewmodel._myList.value?.removeAt(position)
////
////                    }
//
//
//
//                    Log.d("세트리스트 사이즈", setlist.size.toString())
//                    //for(i:Int in 0 until setlist.size){
//                    //setlistHolder.binding.setNo.text="* ${i+1}세트 | "
//                    //setlistHolder.binding.setNo.setText("* ${i+1}세트 | ")
//                    //setlistHolder.binding.weight.setText("${setlist[i].weight}")
//                    //setlistHolder.binding.cnt.setText("${setlist[i].cnt}")
//
//
//
//                    //}
//
//
//
//
//                    notifyDataSetChanged()
//                    //notifyItemRemoved(position)
//
//
//
//
//
//
//                }


                //setlist[position]
                //setlistHolder.bind(setlist[position].)
            }
            ITEM_VIEW_TYPE_FOOTER -> {
                val footerHolder = holder as set_list_adapter.set_list_footer_viewholder
                footerHolder.binding.restTimer.text = getFormattedTime(remainingTimeMillis)


//                footerHolder.itemView.setOnClickListener {
//                    onFooterClickListener?.invoke()
//                    setlist.add(set_list_item(1,1))
//                    notifyDataSetChanged()
//                }
//                footerHolder.binding.footerIcon.setOnClickListener {
//                    onFooterClickListener?.invoke()
//                    setlist.add(set_list_item(0,0))
//                    Log.d("add후 세트 리스트 출력", setlist.toString())
//                    //notifyItemInserted(setlist.size)
//
//
//                    notifyDataSetChanged()
//                }





            }
        }
    }
    private fun getFormattedTime(timeInMillis: Long): String {
        val hours = (timeInMillis / (3600 * 1000)).toInt()
        val minutes = ((timeInMillis - hours * (3600 * 1000)) / (60 * 1000)).toInt()
        val seconds = ((timeInMillis - hours * (3600 * 1000) - minutes * (60 * 1000)) / 1000).toInt()
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
    private lateinit var timer: CountDownTimer

    private fun startTimer() {

            timer = object : CountDownTimer(60000, 1000) { // 1분 타이머
                override fun onTick(millisUntilFinished: Long) {
                    remainingTimeMillis = millisUntilFinished
                    notifyItemChanged(itemCount - 1) // Footer를 갱신하여 남은 시간을 업데이트
                }

                override fun onFinish() {
                    // 타이머가 끝났을 때 수행할 작업
                }
            }.start()
            isTimerRunning = true

    }
    //}

    class set_list_viewholder(val setlist:ArrayList<set_list_item>, val binding: DoingExerciseSetListBinding) : RecyclerView.ViewHolder(binding.root){ // xml 아이템과 연결
        fun bind(setList: set_list_item, idx:Int){
            binding.setNo.text="ㆍ ${idx+1}세트 | "
            binding.weight.text = "${setlist[position].weight.toString()}kg"
            binding.cnt.text = "${setlist[position].cnt.toString()}회"
            //setlist[idx].set=idx+1
            






        }
    }

    class set_list_footer_viewholder(val binding: DoingExerciseSetItemFooterBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {

                Log.d("footer click", "footer클릭")

            }
        }

        // Footer에 필요한 뷰 바인딩 등을 처리
    }


}