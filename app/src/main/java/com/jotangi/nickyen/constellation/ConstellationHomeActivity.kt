package com.jotangi.nickyen.constellation

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.jotangi.nickyen.R
import com.jotangi.nickyen.databinding.ActivityMyQuestionnaireBinding
import com.jotangi.nickyen.home.myquestionnaire.MyQuestionModel
import com.jotangi.nickyen.home.myquestionnaire.MyQuestionnaireViewModel
import com.jotangi.nickyen.utils.SharedPreferencesUtil


class ConstellationHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyQuestionnaireBinding
    private lateinit var viewModel: MyQuestionnaireViewModel
    private lateinit var constellationHomeAdapter: ConstellationHomeAdapter
    private var dataList = mutableListOf<MyQuestionModel>()

    private var isConstellation: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyQuestionnaireBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.typeRed)
        binding.ibGoBack.setOnClickListener { finish() }

        binding.textview.text = "星座運勢"

        getConstellationSwitch() // 有玩過星座要鎖起來

        dataList.add(MyQuestionModel("2021歲末年終星座運勢", "測驗送好康"))
        initRecycleView(dataList)
//        initViewModel()
        binding.refreshLayout.setOnRefreshListener {
//            myQuestionnaireList.clear()
//            viewModel.getMyQuestionnaire()
            binding.refreshLayout.isRefreshing = true
            binding.refreshLayout.isRefreshing = false
        }
    }

    private fun initRecycleView(mutableList: MutableList<MyQuestionModel>?) {
        constellationHomeAdapter =
            ConstellationHomeAdapter(R.layout.item_my_questionnaire, mutableList)
        constellationHomeAdapter.setOnItemClickListener(OnItemClickListener() { adapter, view, position ->

            if (isConstellation != null && isConstellation == "off") {
                Toast.makeText(this, "您已填寫過此運勢", Toast.LENGTH_SHORT).show()
            } else {
                startActivity(Intent(this, ConstellationQuizActivity::class.java))
            }
        })
        binding.recycleView.apply {
            layoutManager = LinearLayoutManager(this@ConstellationHomeActivity)
            adapter = constellationHomeAdapter;
        }
    }

    private fun getConstellationSwitch()
    {
        SharedPreferencesUtil(this, "constellation")
        isConstellation = SharedPreferencesUtil.getData("switch", "") as String
    }
}