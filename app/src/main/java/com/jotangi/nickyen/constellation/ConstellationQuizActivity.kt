package com.jotangi.nickyen.constellation

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jotangi.nickyen.AppUtility
import com.jotangi.nickyen.AppUtility.OnBtnClickListener
import com.jotangi.nickyen.MainActivity
import com.jotangi.nickyen.R
import com.jotangi.nickyen.api.ApiConnection
import com.jotangi.nickyen.api.ApiConstant.API_IMAGE
import com.jotangi.nickyen.databinding.ActivityConstellationQuizBinding
import com.jotangi.nickyen.home.MyDiscountNew2Activity
import com.jotangi.nickyen.utils.SharedPreferencesUtil
import com.jotangi.nickyen.utils.makeGone
import com.jotangi.nickyen.utils.makeVisible
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class ConstellationQuizActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityConstellationQuizBinding

    private var strGender: String? = ""
    private var strDate: String? = ""
    private var strDrink: String? = ""
    private var strHousework: String? = ""
    private var strEmotion: String? = ""
    private var strHobby: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConstellationQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.typeRed)

        binding.ivToolbarBack.setOnClickListener(this)
        binding.tvSend.setOnClickListener(this)
        binding.tvDate.setOnClickListener(this)

        binding.ivCheckBoxMale.setOnClickListener(this)
        binding.ivCheckBoxFemale.setOnClickListener(this)

        binding.ivCheckBoxWater.setOnClickListener(this)
        binding.ivCheckBoxJuice.setOnClickListener(this)
        binding.ivCheckBoxSoda.setOnClickListener(this)
        binding.ivCheckBoxWine.setOnClickListener(this)
        binding.ivCheckBoxCoffee.setOnClickListener(this)
        binding.ivCheckBoxTea.setOnClickListener(this)

        binding.ivCheckBoxSweep.setOnClickListener(this)
        binding.ivCheckBoxTidy.setOnClickListener(this)
        binding.ivCheckBoxWash.setOnClickListener(this)
        binding.ivCheckBoxCook.setOnClickListener(this)

        binding.ivCheckBoxFirstLove.setOnClickListener(this)
        binding.ivCheckBoxNoFirstLove.setOnClickListener(this)

        binding.ivCheckBoxShopping.setOnClickListener(this)
        binding.ivCheckBoxMovie.setOnClickListener(this)
        binding.ivCheckBoxStudy.setOnClickListener(this)
        binding.ivCheckBoxVideo.setOnClickListener(this)
        binding.ivCheckBoxOuting.setOnClickListener(this)
        binding.ivCheckBoxSport.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivToolbarBack -> {
                finish()
            }
            R.id.tvSend -> {
                if (checkAnswers()) {
                    sendQuestionnaire()
                } else {
                    Toast.makeText(this, R.string.questionnaire_no_finish, Toast.LENGTH_SHORT)
                        .show()
                }
            }
            R.id.tvDate -> {
                selectDate()
            }
            // == 性別 ==
            R.id.ivCheckBoxMale -> {
                binding.ivCheckBoxMale.setImageResource(R.drawable.ic_baseline_check_24)
                binding.ivCheckBoxFemale.setImageResource(android.R.color.transparent)
                strGender = "1"
            }
            R.id.ivCheckBoxFemale -> {
                binding.ivCheckBoxFemale.setImageResource(R.drawable.ic_baseline_check_24)
                binding.ivCheckBoxMale.setImageResource(android.R.color.transparent)
                strGender = "2"
            }
            // == 飲品 ==
            R.id.ivCheckBoxWater -> {
                // 單選
                binding.ivCheckBoxWater.setImageResource(R.drawable.ic_baseline_check_24)
                binding.ivCheckBoxJuice.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxSoda.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxWine.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxCoffee.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxTea.setImageResource(android.R.color.transparent)
                strDrink = "1"
                // 複選
//                isWater = if (!isWater) {
//                    binding.ivCheckBoxWater.setImageResource(R.drawable.ic_baseline_check_24)
//                    true
//                }else{
//                    binding.ivCheckBoxWater.setImageResource(android.R.color.transparent)
//                    false
//                }
            }
            R.id.ivCheckBoxJuice -> {
                binding.ivCheckBoxWater.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxJuice.setImageResource(R.drawable.ic_baseline_check_24)
                binding.ivCheckBoxSoda.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxWine.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxCoffee.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxTea.setImageResource(android.R.color.transparent)
                strDrink = "2"
            }
            R.id.ivCheckBoxSoda -> {
                binding.ivCheckBoxWater.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxJuice.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxSoda.setImageResource(R.drawable.ic_baseline_check_24)
                binding.ivCheckBoxWine.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxCoffee.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxTea.setImageResource(android.R.color.transparent)
                strDrink = "3"
            }
            R.id.ivCheckBoxWine -> {
                binding.ivCheckBoxWater.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxJuice.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxSoda.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxWine.setImageResource(R.drawable.ic_baseline_check_24)
                binding.ivCheckBoxCoffee.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxTea.setImageResource(android.R.color.transparent)
                strDrink = "4"
            }
            R.id.ivCheckBoxCoffee -> {
                binding.ivCheckBoxWater.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxJuice.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxSoda.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxWine.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxCoffee.setImageResource(R.drawable.ic_baseline_check_24)
                binding.ivCheckBoxTea.setImageResource(android.R.color.transparent)
                strDrink = "5"
            }
            R.id.ivCheckBoxTea -> {
                binding.ivCheckBoxWater.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxJuice.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxSoda.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxWine.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxCoffee.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxTea.setImageResource(R.drawable.ic_baseline_check_24)
                strDrink = "6"
            }
            // == 家事 ==
            R.id.ivCheckBoxSweep -> {
                binding.ivCheckBoxSweep.setImageResource(R.drawable.ic_baseline_check_24)
                binding.ivCheckBoxTidy.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxWash.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxCook.setImageResource(android.R.color.transparent)
                strHousework = "1"
            }
            R.id.ivCheckBoxTidy -> {
                binding.ivCheckBoxSweep.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxTidy.setImageResource(R.drawable.ic_baseline_check_24)
                binding.ivCheckBoxWash.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxCook.setImageResource(android.R.color.transparent)
                strHousework = "2"
            }
            R.id.ivCheckBoxWash -> {
                binding.ivCheckBoxSweep.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxTidy.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxWash.setImageResource(R.drawable.ic_baseline_check_24)
                binding.ivCheckBoxCook.setImageResource(android.R.color.transparent)
                strHousework = "3"
            }
            R.id.ivCheckBoxCook -> {
                binding.ivCheckBoxSweep.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxTidy.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxWash.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxCook.setImageResource(R.drawable.ic_baseline_check_24)
                strHousework = "4"
            }
            // == 感情 ==
            R.id.ivCheckBoxFirstLove -> {
                binding.ivCheckBoxFirstLove.setImageResource(R.drawable.ic_baseline_check_24)
                binding.ivCheckBoxNoFirstLove.setImageResource(android.R.color.transparent)
                strEmotion = "1"
            }
            R.id.ivCheckBoxNoFirstLove -> {
                binding.ivCheckBoxFirstLove.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxNoFirstLove.setImageResource(R.drawable.ic_baseline_check_24)
                strEmotion = "2"
            }
            // == 嗜好 ==
            R.id.ivCheckBoxShopping -> {
                binding.ivCheckBoxShopping.setImageResource(R.drawable.ic_baseline_check_24)
                binding.ivCheckBoxMovie.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxStudy.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxVideo.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxOuting.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxSport.setImageResource(android.R.color.transparent)
                strHobby = "1"
            }
            R.id.ivCheckBoxMovie -> {
                binding.ivCheckBoxShopping.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxMovie.setImageResource(R.drawable.ic_baseline_check_24)
                binding.ivCheckBoxStudy.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxVideo.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxOuting.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxSport.setImageResource(android.R.color.transparent)
                strHobby = "2"
            }
            R.id.ivCheckBoxStudy -> {
                binding.ivCheckBoxShopping.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxMovie.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxStudy.setImageResource(R.drawable.ic_baseline_check_24)
                binding.ivCheckBoxVideo.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxOuting.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxSport.setImageResource(android.R.color.transparent)
                strHobby = "3"
            }
            R.id.ivCheckBoxVideo -> {
                binding.ivCheckBoxShopping.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxMovie.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxStudy.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxVideo.setImageResource(R.drawable.ic_baseline_check_24)
                binding.ivCheckBoxOuting.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxSport.setImageResource(android.R.color.transparent)
                strHobby = "4"
            }
            R.id.ivCheckBoxOuting -> {
                binding.ivCheckBoxShopping.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxMovie.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxStudy.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxVideo.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxOuting.setImageResource(R.drawable.ic_baseline_check_24)
                binding.ivCheckBoxSport.setImageResource(android.R.color.transparent)
                strHobby = "5"
            }
            R.id.ivCheckBoxSport -> {
                binding.ivCheckBoxShopping.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxMovie.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxStudy.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxVideo.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxOuting.setImageResource(android.R.color.transparent)
                binding.ivCheckBoxSport.setImageResource(R.drawable.ic_baseline_check_24)
                strHobby = "6"
            }
        }
    }

    private fun checkAnswers(): Boolean {
        var isCheckAnswer = false
        isCheckAnswer =
            !(strGender?.isNotBlank() == false || strDate?.isNotBlank() == false || strDrink?.isNotBlank() == false|| strHousework?.isNotBlank() == false|| strEmotion?.isNotBlank() == false|| strHobby?.isNotBlank() == false)

        return isCheckAnswer
    }

    private fun sendQuestionnaire() {
        binding.progressBar.makeVisible()
        ApiConnection.saveConstellation(
            strGender,
            strDate,
            strDrink,
            strHousework,
            strEmotion,
            strHobby,
            object : ApiConnection.OnConnectResultListener {
                override fun onSuccess(jsonString: String) {
                    Timber.e("onSuccess -> $jsonString")
                    GlobalScope.launch(Dispatchers.Main) {
                        binding.progressBar.makeGone()
                        val type = object : TypeToken<ConstellationModel?>() {}.type
                        Gson().fromJson<ConstellationModel>(jsonString, type)?.let {
                            Timber.e("onSuccess -> $it")
                            GlobalScope.launch(Dispatchers.Main) {
                                dialogQuestionnaireFinish(it)
                            }
                        } ?: onFailure("發現一些問題，請退出重新")
                    }
                }

                override fun onFailure(message: String) {
                    GlobalScope.launch(Dispatchers.Main) {
                        binding.progressBar.makeGone()
                        Toast.makeText(this@ConstellationQuizActivity, message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            })
    }

    private fun dialogQuestionnaireFinish(c: ConstellationModel) {

        Dialog(this).apply {
            setContentView(R.layout.dialog_constellation)
            window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val params: ViewGroup.LayoutParams = attributes
                params.width = ConstraintLayout.LayoutParams.MATCH_PARENT
                params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
                attributes = params as WindowManager.LayoutParams
                setCanceledOnTouchOutside(false) // 空白區域點擊消失設置為否
            }

            var img: ImageView = findViewById(R.id.iv_content)
            Picasso.with(this@ConstellationQuizActivity).load(API_IMAGE + c.picture).into(img)
            findViewById<TextView>(R.id.tv_title).text = "${c.cname} ${c.startday}~${c.endday}"
            findViewById<TextView>(R.id.tv_content).text = c.description ?: ""

            findViewById<Button>(R.id.btn_add).setOnClickListener {
                GlobalScope.launch(Dispatchers.Main) {
                    AppUtility.showMyDialog(
                        this@ConstellationQuizActivity,
                        "運勢好禮\n優惠券已發送，請至會員專區的優惠券頁面查看",
                        "確定",
                        null,
                        object :
                            OnBtnClickListener {
                            override fun onCheck() {
                                save()
                                toNextPage(true)
                            }

                            override fun onCancel() {
                            }
                        }
                    )
                }
            }
            show()
        }
    }

    private fun save() {
        SharedPreferencesUtil(this, "constellation")
        SharedPreferencesUtil.putData("switch", "off")
    }

    private fun selectDate() {
        var c = Calendar.getInstance()
        var cDay = c.get(Calendar.DAY_OF_MONTH)
        var cMonth = c.get(Calendar.MONTH)
        var cYear = c.get(Calendar.YEAR)
        val calendarDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                cDay = dayOfMonth
                cMonth = month
                cYear = year
                binding.tvDate.text = "西元：$cYear 年 ${cMonth + 1} 月 $cDay 日"
                strDate = "$cYear/${cMonth + 1}/$cDay"
                Timber.e("selectDate -> $strDate")
            }, cYear, cMonth, cDay
        )
        calendarDialog.show()
    }

    private fun toNextPage(isOpenCoupon: Boolean? = null) {
        startActivity(
            Intent(this@ConstellationQuizActivity, MainActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        )
        if (isOpenCoupon == true)
            startActivity(
                Intent(
                    this@ConstellationQuizActivity,
                    MyDiscountNew2Activity::class.java
                )
            )
        finish()
    }
}