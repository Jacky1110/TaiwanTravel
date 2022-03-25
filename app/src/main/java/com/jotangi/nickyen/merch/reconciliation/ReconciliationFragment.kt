package com.jotangi.nickyen.merch.reconciliation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.jotangi.nickyen.AppUtility
import com.jotangi.nickyen.KTUtils
import com.jotangi.nickyen.R
import com.jotangi.nickyen.databinding.FragmentReconciliationBinding
import com.jotangi.nickyen.databinding.FragmentReconciliationBinding.inflate
import com.jotangi.nickyen.merch.reconciliation.adapter.ReconciliationAdapter
import com.jotangi.nickyen.merch.reconciliation.model.Reconciliation
import com.jotangi.nickyen.merch.reconciliation.viewmodel.ReconciliationViewModel
import com.jotangi.nickyen.utils.makeGone
import com.jotangi.nickyen.utils.makeVisible
import com.kizitonwose.calendarview.utils.yearMonth
import com.loper7.date_time_picker.DateTimeConfig
import com.loper7.date_time_picker.dialog.CardDatePickerDialog
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

class ReconciliationFragment : Fragment() {

    private var _binding: FragmentReconciliationBinding? = null
    val binding get() = _binding!!
    private lateinit var viewModel: ReconciliationViewModel

    private lateinit var reconciliationAdapter: ReconciliationAdapter
    private var reconciliationList = mutableListOf<Reconciliation>()

    private val today = LocalDate.now()
    private var yearMonth: String =
        today.yearMonth.format(DateTimeFormatter.ofPattern("yyyy-MM"))
    private var status: String = "1"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        inflate(inflater, container, false).also { _binding = it }
        initRecyclerView()
        initViewModel()
        onClick()
        return binding.root
    }

    private fun initRecyclerView() {
        reconciliationAdapter = ReconciliationAdapter(requireContext(), reconciliationList, "1")
        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = reconciliationAdapter
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(ReconciliationViewModel::class.java)
        val strYearMonth: String =
            today.yearMonth.format(DateTimeFormatter.ofPattern(getString(R.string.formatter_tw)))
        binding.text.text = strYearMonth
        getRecordAll(yearMonth, status)
    }

    private fun getRecordAll(yearMonth: String, status: String) {
        binding.progressBar.makeVisible()
        viewModel.getProfitInfo(yearMonth, status)
        viewModel.liveReconciliationList.observe(viewLifecycleOwner, {
            reconciliationList.clear()
            var intAmount = 0 // 交易總金額
            var intOrder = 0 // 交易筆數
            if (it != null) {
                reconciliationList.addAll(it)
                reconciliationAdapter.setReconciliationList(reconciliationList, status)
                binding.tvNoData.text = ""
                reconciliationList.forEach { item ->
                    intAmount += item.total_amount?.toInt()!!
                    intOrder += item.total_order?.toInt()!!
                }

                binding.tvAmount.text = AppUtility.strAddComma(intAmount.toString())
                binding.tvCount.text = intOrder.toString()

            } else {
                reconciliationAdapter.setReconciliationList(reconciliationList, status)
                binding.tvNoData.text = getString(R.string.no_record_found)
                binding.tvAmount.text = intAmount.toString()
                binding.tvCount.text = intOrder.toString()
            }
            reconciliationAdapter.notifyDataSetChanged() // 必須指向同一個list才能生效
        })
        binding.progressBar.makeGone()
    }

    private fun onClick() {
        binding.ibGoBack.setOnClickListener {
            Navigation.findNavController(requireView()).popBackStack()
        }
        val tabView: TabLayout = binding.tabLayout
        tabView.run {
            addOnTabSelectedListener(object : OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    when (Objects.requireNonNull(tab.text).toString()) {
                        getString(R.string.all) -> {
//                            創建一個方法 處理 list的計算回傳list 需要判斷狀態
                            status = "1"
                            getRecordAll(yearMonth, status)
                        }
                        getString(R.string.pending_payment) -> {
                            status = "2"
                            getRecordAll(yearMonth, status)
                        }
                        getString(R.string.end_payment) -> {
                            status = "3"
                            getRecordAll(yearMonth, status)
                        }
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab) {}
                override fun onTabReselected(tab: TabLayout.Tab) {}
            })
        }
        binding.btnDate.setOnClickListener {
            val displayList = mutableListOf(DateTimeConfig.YEAR, DateTimeConfig.MONTH)

            val dialog = CardDatePickerDialog.builder(requireContext())
                .setTitle(getString(R.string.select_reconciliation_month))
                .setDisplayType(displayList)
                .setBackGroundModel(CardDatePickerDialog.STACK)
                .showBackNow(false)
                .setMaxTime(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli())
                .setPickerLayout(0)
                .setMinTime(0L)
                .setDefaultTime(0L)
                .setWrapSelectorWheel(false)
                .setThemeColor(ContextCompat.getColor(requireContext(), R.color.typeRed))
                .showDateLabel(true)
                .showFocusDateInfo(false)
                .setOnChoose(getString(R.string.text_confirm)) {
                    binding.text.text = KTUtils.conversionTime(
                        it,
                        getString(R.string.formatter_tw)
                    )
                    yearMonth = KTUtils.conversionTime(
                        it,
                        getString(R.string.formatter_year_month)
                    )
                    getRecordAll(
                        yearMonth, status
                    )
                }
                .setOnCancel(getString(R.string.text_cancel)) {
                }.build()
            dialog.show()
            (dialog as BottomSheetDialog).behavior.isHideable = false
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        reconciliationList.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}