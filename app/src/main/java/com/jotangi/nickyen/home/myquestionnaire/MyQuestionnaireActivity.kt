package com.jotangi.nickyen.home.myquestionnaire

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.jotangi.nickyen.R
import com.jotangi.nickyen.databinding.ActivityMyQuestionnaireBinding
import com.jotangi.nickyen.questionnaire.QuestionnaireActivity
import com.jotangi.nickyen.utils.makeInVisible
import com.jotangi.nickyen.utils.makeVisible
import timber.log.Timber

class MyQuestionnaireActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyQuestionnaireBinding
    private lateinit var viewModel: MyQuestionnaireViewModel
    private lateinit var myQuestionnaireAdapter: MyQuestionnaireAdapter
    private var myQuestionnaireList = mutableListOf<MyQuestionnaireModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyQuestionnaireBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this, R.color.typeRed)
        binding.ibGoBack.setOnClickListener { finish() }

//        myQuestionnaireList.add(MyQuestionModel("民族服飾穿戴體驗-預約成功通知", "蕊仔已成功送出預約單"))
//        myQuestionnaireList.add(MyQuestionModel("升旗體驗-預約取消通知", "蕊仔已取消預約單"))
        initViewModel()
        binding.refreshLayout.setOnRefreshListener {
            myQuestionnaireList.clear()
            viewModel.getMyQuestionnaire()
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(MyQuestionnaireViewModel::class.java)
        viewModel.getDataListObserve().observe(this, {
            binding.refreshLayout.isRefreshing = true
            if (it == null) {
                binding.progressBar.makeInVisible()
                binding.tvNoData.makeVisible()
                initRecycleView(null)
                binding.tvNoData.text = "目前無問券可填寫"
                Timber.e("initViewModel -> $it")
            } else {
                binding.progressBar.makeInVisible()
                binding.tvNoData.makeInVisible()
                initRecycleView(it as MutableList<MyQuestionnaireModel>)
                Timber.e("initViewModel -> $it")
            }
            binding.refreshLayout.isRefreshing = false
        })
        viewModel.getMyQuestionnaire()
    }

    private fun initRecycleView(myQuestionnaireList: MutableList<MyQuestionnaireModel>?) {
        myQuestionnaireAdapter =
            MyQuestionnaireAdapter(R.layout.item_my_questionnaire, myQuestionnaireList)
        myQuestionnaireAdapter.setOnItemClickListener(OnItemClickListener { adapter, view, position ->
            QuestionnaireActivity.start(this, (position+1).toString(), "");
        })
        binding.recycleView.apply {
            layoutManager = LinearLayoutManager(this@MyQuestionnaireActivity)
            adapter = myQuestionnaireAdapter
        }
    }
}