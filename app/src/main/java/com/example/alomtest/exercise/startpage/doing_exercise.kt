package com.example.alomtest.exercise.custompage01

import SharedPreferenceUtils
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alomtest.MyViewModel
import com.example.alomtest.R
import com.example.alomtest.databinding.FragmentAddRoutinePageBinding
import com.example.alomtest.databinding.FragmentDoingExercisePageBinding
import com.example.alomtest.exercise.mainpage.exercise_main_copy
import com.example.alomtest.exerciseData
import com.example.alomtest.exercise.startpage.doing_exercise_list_adapter
import com.example.alomtest.presetDtoelement
import com.example.alomtest.retrofit.Api
import com.example.alomtest.retrofit.exercise_routine_list
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class doing_exercise : Fragment() {

    private lateinit var binding: FragmentDoingExercisePageBinding
    private lateinit var viewmodel : MyViewModel
    lateinit var userRoutine :  exercise_routine_list
    lateinit var exercise_recycler_view : RecyclerView
    var custom_cnt:Int =0
    var receive_data: String? = " " //공백으로 하면 되고 왜 null로 하면 안되는거지?

    lateinit var final_list:ArrayList<exerciseData>

    var startTime: Long = 0



    private var elapsedTime: Long = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= FragmentDoingExercisePageBinding.inflate(layoutInflater)
        viewmodel = ViewModelProvider(requireActivity()).get(MyViewModel::class.java)






        if(SharedPreferenceUtils.loadData(requireContext(),"exercise_uptime").isEmpty()){
            elapsedTime = 0L
        }
        else{
            elapsedTime = SharedPreferenceUtils.loadData(requireContext(),"exercise_uptime").toLong()
        }




        startStopwatch()


        //stopwatch부


        ///


        //뒤로가기 처리
        val callback = object : OnBackPressedCallback(true /* enabled by default */) {
            override fun handleOnBackPressed() {
                // 뒤로가기 이벤트가 발생했을 때 수행할 작업
                // 예를 들어 특정 상황에서만 뒤로가기를 처리하고 싶은 경우 여기에 작성

                replaceFragment(exercise_main_copy())

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)



        val backicon=binding.cancelicon


        val gson = Gson()
//
        val bundle = arguments
//        receive_data = bundle?.getString("selectied_exercise_routine").toString()
//
//        Log.d("번들을 통해 받은 데이터", receive_data.toString())

        val json = bundle?.getString("selectied_exercise_routine")
        val userRoutine = gson.fromJson(json,exercise_routine_list::class.java)

        Log.d("json형태로 출력test", userRoutine.toString())


        binding.presetName.text = userRoutine.presetName

        val extractedList = ArrayList<exerciseData>()
        val extract_set = ArrayList<set_list_item>()
        for(i:Int in 0 until userRoutine.splitPresetDto.size){




            for(j:Int in 0 until userRoutine.splitPresetDto[i].splitExerciseDetailDto.size){
                extract_set.add(set_list_item(userRoutine.splitPresetDto[i].splitExerciseDetailDto[j].weight.toInt(),userRoutine.splitPresetDto[i].splitExerciseDetailDto[j].repetitionCount))


            }

            Log.d("for문 나간 후 extract_set출력", extract_set.toString())


            extractedList.add(exerciseData("부제목테스트",userRoutine.splitPresetDto[i].exerciseName, set_list = ArrayList(extract_set)))

            extract_set.clear()


            //extractedList.add(exerciseData(userRoutine.splitPresetDto[i].exerciseName,))
        }


        final_list = ArrayList(extractedList)
        Log.d("추출리스트 테스트", extractedList.toString())


//TODO 버튼을 눌러도 넘어가지 않는 경우도 고려(예외처리, 가령 운동을 추가하지 않았는데 저장 버튼을 누른 경우)


        backicon.setOnClickListener {
            replaceFragment(exercise_main_copy())
        }



        //backicon은 나중에 구현




        //View.findViewById(R.id.exercise_view)










    }
    private fun startStopwatch() {

        startTime = SystemClock.elapsedRealtime() - elapsedTime
        stopwatchHandler.postDelayed(stopwatchRunnable, REFRESH_RATE)
    }

    private val stopwatchHandler = android.os.Handler()
    private val stopwatchRunnable = object : Runnable {
        override fun run() {
            elapsedTime = SystemClock.elapsedRealtime() - startTime
            val hours = (elapsedTime / (3600 * 1000)).toInt()
            val minutes = ((elapsedTime - hours * (3600 * 1000)) / (60 * 1000)).toInt()
            val seconds = ((elapsedTime - hours * (3600 * 1000) - minutes * (60 * 1000)) / 1000).toInt()
            val milliseconds = (elapsedTime - hours * (3600 * 1000) - minutes * (60 * 1000) - seconds * 1000).toInt()
            val formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds)
            binding.exerciseUptime.text = formattedTime

            stopwatchHandler.postDelayed(this, REFRESH_RATE)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)





        //정규식을 이용한 presetnumber추출
//        val regex = Regex("presetNumber=(\\d+)")
//        val matchResult = regex.find(receivedData)
//        val presetNumber = matchResult?.groupValues?.get(1)?.toIntOrNull()


        //Log.d("리시브 번들 테스트",arguments?.getString("selectedRoutine").toString())


//        val customList = arrayListOf(
//            exerciseData("대흉근 발달, 3대 운동","01 바벨 벤치 프레스")
//
//            )
        //data class ExerciseData(val title: String, val description: String)

        var customList = ArrayList<exerciseData>()
        viewmodel = ViewModelProvider(requireActivity()).get(MyViewModel::class.java)

        Log.d("size출력", viewmodel._myList.value?.size.toString())

        //TODO 나중에 viewmodel이 0일땐 저장 안되게 invisible하는 코드 추가할 것
//        if(viewmodel._myList.value?.size!! <1){
//            binding.saveBtn.visibility = View.INVISIBLE
//        }
//        else{
//            binding.saveBtn.visibility = View.VISIBLE
//        }

        //customList.add(exerciseData("대흉근 발달, 3대 운동","01 바벨 벤치 프레스"))



        exercise_recycler_view = view.findViewById(R.id.doing_exercise_view)
        exercise_recycler_view.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        exercise_recycler_view.setHasFixedSize(true)

        //exercise_recycler_view.adapter = viewmodel._myList.value?.let { exercise_list_adpater(it) }


        //exerciseData형으로 변경
        //userRoutine


        exercise_recycler_view.adapter = doing_exercise_list_adapter(final_list)

        Log.d("viewmodel출력", viewmodel._myList.value!!.toString())

        Log.d("if문 진입 전 receive data 확인", receive_data.toString())
        Log.d("true or false", (receive_data!!.isNotBlank()).toString())
        if(receive_data!!.isNotBlank()){

            Log.d("exerciseData 추가하는 곧 진입",receive_data.toString())
            customList.add(exerciseData("부제목테스트",receive_data.toString(),ArrayList<set_list_item>()))
            viewmodel.addItem(exerciseData("부제목테스트",receive_data.toString(),ArrayList<set_list_item>()))
            viewmodel._myList.value!!.get(viewmodel._myList.value!!.size-1).set_list.add(0,set_list_item(0,1))

//            if(viewmodel._myList.value?.size==0){
//                viewmodel.addItem(exerciseData("부제목테스트",receive_data.toString(),ArrayList<set_list_item>()))
//                viewmodel._myList.value[0].set_list.add()
//            }
//            else{
//                viewmodel.addItem(exerciseData("부제목테스트",receive_data.toString(),ArrayList<set_list_item>()))
//
//            }


            //Log.d("커스텀 리스트 출력", customList.toString())
            Log.d("커스텀 리스트 출력", viewmodel._myList.value.toString())
            receive_data=""

//            val ad=exercise_recycler_view.adapter
//            ad?.notifyDataSetChanged()


        }

        for(i:Int in 0 until viewmodel._myList.value!!.size){
            Log.d("세트리스트 크기 출력", (viewmodel._myList.value!![i].set_list.size).toString())
            if(viewmodel._myList.value!![i].set_list.size==0){
                viewmodel._myList.value!!.removeAt(i)
                viewmodel.deleteItem(i)

            }

        }

    }
    private var _binding: FragmentAddRoutinePageBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        _binding = FragmentAddRoutinePageBinding.inflate(inflater,container,false)
        return binding.root
        //return inflater.inflate(R.layout.fragment_mypage_body_measurement_editmode, container, false)
    }

    override fun onPause() {
        super.onPause()

        SharedPreferenceUtils.saveData(requireContext(),"exercise_uptime", elapsedTime.toString())
        Log.d("퍼즈 저장",elapsedTime.toString())
    }

    override fun onResume() {
        super.onResume()

        if(SharedPreferenceUtils.loadData(requireContext(),"exercise_uptime").isEmpty()){
            elapsedTime=0L
        }
        else{
            elapsedTime = SharedPreferenceUtils.loadData(requireContext(),"exercise_uptime").toString().toLong()

        }


        Log.d("리즈메 로드", elapsedTime.toString())

    }

    override fun onDestroy() {
        super.onDestroy()
        SharedPreferenceUtils.saveData(requireContext(),"exercise_uptime", "0")

    }
    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
        println("success")

    }



    //    private fun saveExerciseCustomPreset(){
//        val api= Api.create()
//
//        val jsonObject = JSONObject()
//
//        val email = SharedPreferenceUtils.loadData(requireContext(),"email")
//
//        jsonObject.put("email", email)
//
//        val current_routine_cnt = SharedPreferenceUtils.loadData(requireContext(),"routine_cnt") //현재 저장 된 루틴 개수 저장
//
//
//        //val presetDtoJsonObject = JSONObject()
//        val presetDtoList = ArrayList<JSONObject>()
//
//        //jsonObject.put("presetDto", ArrayList<presetDtoelement>())
//
//        for(i:Int in 0 until viewmodel._myList.value!!.size){
//            for(j:Int in 0 until viewmodel._myList.value!![i].set_list.size){
//
//                //val jsonObject2 = ArrayList<JSONObject>()
//                var jsonObject2=JSONObject()
//
//                jsonObject2.put("presetNumber", current_routine_cnt.toInt())
//                jsonObject2.put("exerciseNumber", i)
//                jsonObject2.put("presetTitle", binding.presetName.text.toString())
//                jsonObject2.put("weight", viewmodel._myList.value!![i].set_list[j].weight)
//                jsonObject2.put("setCount", j)
//                jsonObject2.put("repetitionCount",viewmodel._myList.value!![i].set_list[j].cnt)
//                jsonObject2.put("exerciseName", viewmodel._myList.value!![i].exerciseName)
//                jsonObject2.put("category", "notneed")
//                Log.d("jsono=옵젝2 출력", jsonObject2.toString())
//
//                presetDtoList.add(jsonObject2)
//
//                Log.d("presetDtoList출력", presetDtoList.toString())
//
//                jsonObject2=JSONObject()
//
//
////                presetDtoList.add(presetDtoelement(current_routine_cnt.toInt(),i,
////                    binding.presetName.text.toString(),
////                    viewmodel._myList.value!![i].set_list[j].weight,j,
////                    viewmodel._myList.value!![i].set_list[j].cnt,
////                    viewmodel._myList.value!![i].exerciseName,"not need"))
//            }
//        }
//        jsonObject.put("presetDto", presetDtoList)
//
//
//        Log.d("preset_save json출력", jsonObject.toString())
//
//
//        val usertoken = SharedPreferenceUtils.loadData(requireContext(),"accessToken")
//        val jsonStringWithoutBackslashes = jsonObject.toString()
//        //val jsonStringWithoutBackslashes = jsonObject.toString()
//
//        Log.d("\\제거 후 출력", jsonStringWithoutBackslashes)
//
//        val jsonObject3 = JSONObject(jsonStringWithoutBackslashes)
//
//        api.save_preset_exercise_list(
//            accessToken = "Bearer $usertoken",
//            JsonParser.parseString(jsonStringWithoutBackslashes)
//        ).enqueue(object : Callback<ArrayList<presetDtoelement>> {
//            override fun onResponse(
//                call: Call<ArrayList<presetDtoelement>>,
//                response: Response<ArrayList<presetDtoelement>>
//            ) {
//                Log.d("로그인 통신 성공", response.toString())
//                Log.d("로그인 통신 성공", response.body().toString())
//                Log.d("response코드", response.code().toString())
//
//                when (response.code()) {
//                    200 -> {
//                        Toast.makeText(requireContext(), "200OK", Toast.LENGTH_SHORT).show()
//                        Log.d("받은 정보 로그", response.toString())
//                        val result = response.body()!!
//                        //handleResult(result)
//                    }
//                    // 다른 상태 코드에 대한 처리
//                }
//            }
//
//            override fun onFailure(call: Call<ArrayList<presetDtoelement>>, t: Throwable) {
//                Log.d("로그인 통신 실패", t.message.toString())
//                Log.d("로그인 통신 실패", "fail")
//            }
//        })
//
//
//
//
//    }
    private fun saveExerciseCustomPreset() {
        val api = Api.create()

        val email = SharedPreferenceUtils.loadData(requireContext(), "email")
        val currentRoutineCnt = SharedPreferenceUtils.loadData(requireContext(), "routine_cnt").toInt()

        val presetDtoList = mutableListOf<presetDtoelement>()

        viewmodel._myList.value?.forEachIndexed { i, exerciseData ->
            exerciseData.set_list.forEachIndexed { j, setListItem ->
                val presetDto = presetDtoelement(
                    presetNumber = currentRoutineCnt,
                    exerciseNumber = i,
                    presetTitle = binding.presetName.text.toString(),
                    weight = setListItem.weight,
                    setCount = j,
                    repetitionCount = setListItem.cnt,
                    exerciseName = exerciseData.exerciseName,
                    category = "notneed"
                )
                presetDtoList.add(presetDto)
            }
        }

        val jsonObject = JsonObject().apply {
            addProperty("email", email)
            add("presetDto", JsonParser.parseString(Gson().toJson(presetDtoList)))
        }

        val userToken = SharedPreferenceUtils.loadData(requireContext(), "accessToken")
//TODO 추후 토큰 만료시 고려한 코드 작성
        //TODO 200말고 다른것도 추가
        api.save_preset_exercise_list("Bearer $userToken", jsonObject)
            .enqueue(object : Callback<ArrayList<presetDtoelement>> {
                override fun onResponse(
                    call: Call<ArrayList<presetDtoelement>>,
                    response: Response<ArrayList<presetDtoelement>>
                ) {
                    when (response.code()){
                        200->{
                            SharedPreferenceUtils.loadData(requireContext(),"routine_cnt",(currentRoutineCnt+1).toString())
                        }
                    }
                }

                override fun onFailure(call: Call<ArrayList<presetDtoelement>>, t: Throwable) {
                    // onFailure 처리
                }
            })
    }
    companion object {

        private const val REFRESH_RATE: Long = 100

    }

}