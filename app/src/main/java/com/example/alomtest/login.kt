package com.example.alomtest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import retrofit2.Call
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.alomtest.databinding.LoginLayoutBinding
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.json.JSONObject
import retrofit2.Callback
import retrofit2.Response

class login : AppCompatActivity() {
    lateinit var binding: LoginLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginbutton.setOnClickListener {
            val id = binding.loginemail.text.toString().trim()//trim : 문자열 공백제거
            val pw = binding.loginpassword.text.toString().trim()

            saveData(id, pw)//db (shared preference)에 데이터 저장 (자동 로그인 용)

            // == 백엔드 통신 부분 ==
            val api = Api.create()//
            val data = UserModel(id, pw)
            val jsonObject=JSONObject()
            jsonObject.put("email",data.id)
            jsonObject.put("password",data.pw)



            JsonParser.parseString(jsonObject.toString())


            println(JsonParser.parseString(jsonObject.toString()))


            api.userLogin(JsonParser.parseString(jsonObject.toString())).enqueue(object : Callback<LoginBackendResponse> {
                override fun onResponse(
                    call: Call<LoginBackendResponse>,
                    response: Response<LoginBackendResponse>
                ) {
                    Log.d("로그인 통신 성공",response.toString())
                    Log.d("로그인 통신 성공", response.body().toString())
                    Log.d("response코드",response.code().toString())
                    //Log.d("반환 메시지",response.body())


                    when (response.code()) {
                        200 -> {
                            saveData(id, pw)
                            Toast.makeText(this@login, "로그인 성공", Toast.LENGTH_LONG).show()
                            //인텐트를 이용하여 화면 전환
                            val intent = Intent(this@login,MainActivity::class.java)
                            startActivity(intent)
                            finish()

                        }
                        500 -> Toast.makeText(this@login, "로그인 실패 : 아이디나 비번이 올바르지 않습니다", Toast.LENGTH_LONG).show()
                        403->Toast.makeText(this@login,"로그인 실패 : 서버 접근 권한이 없습니다.",Toast.LENGTH_SHORT).show()

                        else -> Toast.makeText(this@login, "LOGIN ERROR", Toast.LENGTH_LONG).show()


                    }
                }

                override fun onFailure(call: Call<LoginBackendResponse>, t: Throwable) {
                    // 실패
                    Log.d("로그인 통신 실패",t.message.toString())
                    Log.d("로그인 통신 실패","fail")
                }
            })
        }
    }fun saveData( id : String, pw : String){
        val prefID = this.getSharedPreferences("userID", MODE_PRIVATE)
        val prefPW = this.getSharedPreferences("userPW", MODE_PRIVATE)
        val editID = prefID.edit()
        val editPW = prefPW.edit()
        editID.putString("id", id)
        editPW.putString("pw", pw)
        editID.apply()//save
        editPW.apply()//save
        Log.d("로그인 데이터", "saved")
    }


}
