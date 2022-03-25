package com.jotangi.nickyen.questionnaire

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jotangi.nickyen.R
import com.jotangi.nickyen.api.ApiConnection
import com.jotangi.nickyen.api.ApiConnection.OnConnectResultListener
import com.jotangi.nickyen.databinding.ActivityQuestionnaireBinding
import com.jotangi.nickyen.questionnaire.adapter.QuestionnaireAdapter
import com.jotangi.nickyen.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.gson.JsonArray
import com.jotangi.nickyen.MainActivity
import com.jotangi.nickyen.home.MyDiscountNew2Activity
import com.google.gson.JsonObject


class QuestionnaireActivity : AppCompatActivity() {

    companion object{
        const val BUNDLE_KEY_SID = "sid"
        const val BUNDLE_KEY_STORE_NAME = "storeName"

        @JvmStatic
        fun start(context: Context?, sid: String, storeName: String){
            context?.also {
                val intent = Intent(it, QuestionnaireActivity::class.java)
                intent.putExtra(BUNDLE_KEY_SID, sid)
                intent.putExtra(BUNDLE_KEY_STORE_NAME, storeName)
                it.startActivity(intent)
            }
        }
    }

    private var sid: String? = null
    private var storeName: String? = null

    private lateinit var binding: ActivityQuestionnaireBinding
    private lateinit var questionnaireAdapter: QuestionnaireAdapter
    private var questionnaireList = mutableListOf<Questionnaire>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionnaireBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.typeRed)

        intent?.apply {
            sid = getStringExtra(BUNDLE_KEY_SID)
            storeName = getStringExtra(BUNDLE_KEY_STORE_NAME)
        }

        binding.ivToolbarBack.setOnClickListener { toNextPage() }

        binding.tvSend.setOnClickListener {
            if (checkAnswers()){
                LoadingDialog.show(this)
                sendQuestionnaires()
            }else {
                Toast.makeText(this, R.string.questionnaire_no_finish, Toast.LENGTH_SHORT).show()
            }
        }

        initQuestionnaireList()
        getQuestionnaireList()
    }

    private fun initQuestionnaireList(){
        questionnaireAdapter = QuestionnaireAdapter(this, questionnaireList)
        questionnaireAdapter.onAnswerClick = {questionnaire, answer ->
            Timber.e("onAnswerClick: $questionnaire, $answer")

            questionnaireList.find {
                it == questionnaire
            }?.answers?.forEach {
                if(it == answer){
                    it.isSelected = !it.isSelected
                }else{
                    if(questionnaire.chooseType == Constants.Choose_Type.Single){
                        it.isSelected = false
                    }
                }
            }
            questionnaireAdapter?.notifyDataSetChanged()
        }
        questionnaireAdapter.onOtherClick = {questionnaire, answer ->
            Timber.e("onOtherClick: $questionnaire, $answer")
            questionnaireList.find {
                it == questionnaire
            }?.answers?.find {
                it == answer
            }?.let { answer ->
                if (answer.isOther){
                    dialogQuestionnaireOther(this, answer.other) {
                        answer.other = it
                        questionnaireAdapter?.notifyDataSetChanged()
                    }
                }
            }
        }

        binding.rvQuestionnaire.apply {
            layoutManager = LinearLayoutManager(this@QuestionnaireActivity)
            adapter = questionnaireAdapter
        }
    }

    private fun checkAnswers(): Boolean{
        questionnaireList.forEach { question ->
            if (question.isRequired){
                question.answers?.filter {
                    it.isSelected
                }.let {
                    if (it.isNullOrEmpty())
                        return false
                }
            }
        }
        return true
    }

    private fun getQuestionnaireList() {
        binding.progressBar.makeVisible()
        ApiConnection.getQuestionnaireList(sid, object : OnConnectResultListener {
            override fun onSuccess(jsonString: String) {
                Timber.e("onSuccess -> $jsonString")
                GlobalScope.launch(Dispatchers.Main){
                    binding.progressBar.makeGone()
                    binding.tvSend.makeVisible()

                    try {
                        val type = object : TypeToken<ArrayList<QuestionnaireList?>?>() {}.type
                        Gson().fromJson<ArrayList<QuestionnaireList>>(jsonString, type)?.let { list ->
                            list.forEach {
                                val answers = mutableListOf<QuestionnaireAnswer>()
                                answers.addAnswer(arrayListOf(it.q01, it.q02, it.q03, it.q04, it.q05, it.q06, it.q07, it.q08, it.q09, it.q10))

                                questionnaireList.add(
                                    Questionnaire(
                                        question = it.question,
                                        chooseType = it.choose_type,
                                        answers = answers,
                                        qid = it.qid))
                            }
                            questionnaireAdapter?.notifyDataSetChanged()
                        }
                    }catch (e: Exception) {}
                }
            }

            override fun onFailure(message: String) {
                GlobalScope.launch(Dispatchers.Main){
                    binding.progressBar.makeGone()
                }
                Timber.e("onFailure -> $message")
            }
        })
    }

    private fun MutableList<QuestionnaireAnswer>.addAnswer(answers: ArrayList<String?>?) {
        answers?.forEach { answer ->
            answer?.let {
                add(QuestionnaireAnswer(
                    answer = it,
                    isOther = it == getString(R.string.questionnaire_other)))
            }
        }
    }

    private fun sendQuestionnaires(){
        questionnaireList.find {
            !it.isSent
        }?.let {
            sendQuestionnaire(it)
        }?: kotlin.run {
            LoadingDialog.cancel()
            Toast.makeText(this, R.string.questionnaire_finish, Toast.LENGTH_SHORT).show()
            dialogQuestionnaireFinish("")
//            fetchCouponId()
        }
    }

    private fun sendQuestionnaire(questionnaire: Questionnaire){

        val answer = questionnaire.answers?.joinToString(",") {
            if (it.isSelected) "1" else "0"
        }
        var other: String? = null
        val hasOther = questionnaire.answers?.find {
            it.isOther
        }?.let {
            other = it.other
            true
        }?: false

        Timber.e("sendQuestionnaire -> ${questionnaire.qid} / $answer / $hasOther / $other")

        ApiConnection.sendQuestionnaire(
            questionnaire.qid,
            answer,
            hasOther,
            other?:"",
            object : OnConnectResultListener {
                override fun onSuccess(jsonString: String) {
                    GlobalScope.launch(Dispatchers.Main) {
                        Timber.e("onSuccess -> $jsonString")

                        questionnaireList.find {
                            it == questionnaire
                        }?.apply {
                            isSent = true
                        }
                        sendQuestionnaires()
                    }
                }

                override fun onFailure(message: String) {
                    GlobalScope.launch(Dispatchers.Main) {
                        LoadingDialog.cancel()
                        Toast.makeText(this@QuestionnaireActivity, message, Toast.LENGTH_SHORT).show()
                        Timber.e("onFailure -> $message")
                    }
                }
            })
    }

    private var tmpCouponPosition = 0
    private fun fetchCouponId(){
        if (Constants.Questionnaire.coupons.size > tmpCouponPosition){
            getCoupon(Constants.Questionnaire.coupons[tmpCouponPosition])
            tmpCouponPosition++
        }else{
            toNextPage()
        }
    }

    private fun getCoupon(couponId: String){
        LoadingDialog.show(this)
        ApiConnection.getCoupon(couponId, object : OnConnectResultListener {
            override fun onSuccess(jsonString: String) {
                Timber.e("onSuccess -> $jsonString")
                GlobalScope.launch(Dispatchers.Main) {
                    LoadingDialog.cancel()
                    var couponName: String? =null
                    try {
                        val couponInfo = Gson().fromJson(jsonString, JsonObject::class.java)?.get("coupon_info")?.asString
                        couponName = Gson().fromJson(couponInfo, JsonArray::class.java)?.get(0)?.asJsonObject?.get("coupon_name")?.asString
                    }catch (e: Exception) { }
                    dialogQuestionnaireFinish(couponName)
                }
            }

            override fun onFailure(message: String) {
                GlobalScope.launch(Dispatchers.Main) {
                    LoadingDialog.cancel()
                    Timber.e("onFailure -> $message")
                    fetchCouponId()
                }
            }
        })
    }

    private fun dialogQuestionnaireFinish(couponName: String?){

        Dialog(this).apply {
            setContentView(R.layout.dialog_questionnaire)
            window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                val params: ViewGroup.LayoutParams = attributes
                params.width = ConstraintLayout.LayoutParams.MATCH_PARENT
                params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
                attributes = params as WindowManager.LayoutParams
            }

            findViewById<TextView>(R.id.tvStoreName).text = storeName?:""
            findViewById<TextView>(R.id.tvContent).text = getString(R.string.questionnaire_dlg_content, couponName?:"")

            findViewById<Button>(R.id.btnCoupon).setOnClickListener {
                toNextPage(true)
            }

            findViewById<Button>(R.id.btnHome).setOnClickListener {
                toNextPage()
            }
            show()
        }
    }

    private fun toNextPage(isOpenCoupon: Boolean? = null){
        startActivity(
            Intent(this@QuestionnaireActivity, MainActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
        if (isOpenCoupon == true)
            startActivity(Intent(this@QuestionnaireActivity, MyDiscountNew2Activity::class.java))
        finish()
    }
}