package com.jotangi.nickyen.beautySalons.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.jotangi.nickyen.R
import com.jotangi.nickyen.base.BaseFragment
import com.jotangi.nickyen.beautySalons.CheckDesignerFragment.shopBean
import com.jotangi.nickyen.beautySalons.OrderCheckFragment
import com.jotangi.nickyen.beautySalons.model.ServiceBean
import com.jotangi.nickyen.databinding.CalendarDayBinding
import com.jotangi.nickyen.databinding.FragmentCheckCalendarBinding
import com.jotangi.nickyen.utils.makeGone
import com.jotangi.nickyen.utils.makeInVisible
import com.jotangi.nickyen.utils.makeVisible
import com.jotangi.nickyen.utils.setTextColorRes
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.next
import com.kizitonwose.calendarview.utils.previous
import timber.log.Timber
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.*

private const val ARG_PARAM_HID = "designer_bean_hid"

class CheckCalendarFragment : BaseFragment() {

    companion object {
        lateinit var serviceArrayList: ArrayList<ServiceBean>

        @JvmStatic
        fun newInstance(hid: String, dataList: ArrayList<ServiceBean>): CheckCalendarFragment {
            serviceArrayList = dataList

            return CheckCalendarFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM_HID, hid)
                }
            }
        }
    }

    private var _binding: FragmentCheckCalendarBinding? = null
    val binding get() = _binding!!

    private lateinit var viewModel: CheckCalendarViewModel

    private var designerBeanHid: String? = null

    private var selectedDate: LocalDate? = null
    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")
    private val today = LocalDate.now()

    private lateinit var checkCalendarAdapter: CheckCalendarAdapter
    private var reservationList = mutableListOf<Reservation>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            designerBeanHid = it.getString(ARG_PARAM_HID)
        }
        viewModel = ViewModelProvider(this).get(CheckCalendarViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCheckCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivToolbarBack.setOnClickListener { activity?.onBackPressed() }

        initReservationList()

        designerBeanHid?.let {
            if (designerBeanHid == ("0")) {
                viewModel.getWorkingDay(it, shopBean.sid)
            } else {
                viewModel.getWorkingDay(it,"")
            }

            viewModel.listWorkingDayBean.observe(viewLifecycleOwner) {
                Timber.e("listWorkingDayBean -> $it")
                setupCalendar()
            }

            viewModel.listReservation.observe(viewLifecycleOwner) { listReservation ->
                Timber.e("listIsBookingBean -> $it")
                binding.progressBar.makeGone()

                reservationList.clear()
                if (listReservation.isNullOrEmpty()) {
                    binding.tvNoAppointment.makeVisible()
                } else {
                    binding.tvNoAppointment.makeGone()
                    reservationList.addAll(listReservation)
                }
                checkCalendarAdapter?.notifyDataSetChanged()
            }
        }
    }

    private fun setupCalendar() {

        binding.clCalendar.makeVisible()

        val currentMonth = YearMonth.now()
        val firstMonth = currentMonth.minusMonths(0)
        val lastMonth = currentMonth.plusMonths(20)
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        binding.calendarView.setup(firstMonth, lastMonth, firstDayOfWeek)
        binding.calendarView.scrollToMonth(currentMonth)

        binding.calendarView.post {
            selectDate(today)
        }

        binding.calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            // Called only when a new container is needed.
            override fun create(view: View) = DayViewContainer(view)

            // Called every time we need to reuse a container.
            override fun bind(container: DayViewContainer, day: CalendarDay) {

                container.binding.tvDayText.apply {
                    text = day.date.dayOfMonth.toString()
                    setOnClickListener {
                        Timber.e("DayViewContainer(OnClickListener) -> $day")
                        if (day.date >= today && day.owner == DayOwner.THIS_MONTH) {
                            selectDate(day.date)
                        }
                    }

                    if (day.owner != DayOwner.THIS_MONTH) {
                        makeInVisible()
                    } else {
                        makeVisible()

                        if (day.date == selectedDate) {
                            setBackgroundResource(R.drawable.calendar_selected_bg)

                            setTextColorRes(R.color.white)
                        } else {
                            background = null

                            if (day.date < today) {
                                setTextColorRes(R.color.c_cecece_100)
                            } else {
                                viewModel.listWorkingDayBean.value?.firstOrNull {
                                    (day.date.toString() == it.workingdate) && ((it.workingtype == "H") || (it.workingtype == "C"))
                                }?.let {
                                    setTextColorRes(R.color.typeRed)
                                } ?: setTextColorRes(R.color.black)
                            }
                        }
                    }
                }
                Timber.e("day: $day")
            }
        }

        binding.calendarView.monthHeaderBinder = object : MonthHeaderFooterBinder<ViewContainer> {
            override fun create(view: View) = ViewContainer(view)
            override fun bind(container: ViewContainer, month: CalendarMonth) {
                Timber.e("month: $month")
            }
        }

        binding.calendarView.monthScrollListener = { month ->
            Timber.e("monthScrollListener -> $month")
            val title = "${monthTitleFormatter.format(month.yearMonth)} ${month.yearMonth.year}"
            binding.tvMonthYears.text = title
        }

        binding.ivCalendarNext.setOnClickListener {
            binding.calendarView.findFirstVisibleMonth()?.let {
                binding.calendarView.smoothScrollToMonth(it.yearMonth.next)
            }
        }

        binding.ivCalendarPrevious.setOnClickListener {
            binding.calendarView.findFirstVisibleMonth()?.let {
                binding.calendarView.smoothScrollToMonth(it.yearMonth.previous)
            }
        }
    }

    fun selectDate(date: LocalDate) {
        if (selectedDate != date) {
            val oldDate = selectedDate
            selectedDate = date
            oldDate?.let { binding.calendarView.notifyDateChanged(it) }
            binding.calendarView.notifyDateChanged(date)

            viewModel.listWorkingDayBean.value?.firstOrNull {
                (selectedDate.toString() == it.workingdate) && ((it.workingtype == "H") || it.workingtype == "C")
            }?.let {
                binding.tvNoAppointment.makeVisible()
            } ?: kotlin.run {
                binding.progressBar.makeVisible()
                binding.tvNoAppointment.makeGone()
                viewModel.getBookingDay(
                    hid = designerBeanHid.toString(),
                    workingDate = date.toString()
                )
            }
            reservationList.clear()
            checkCalendarAdapter?.notifyDataSetChanged()
        }
    }

    private fun initReservationList() {
        checkCalendarAdapter = CheckCalendarAdapter(reservationList)
        checkCalendarAdapter.onItemClick = {
            Timber.e("$it")
            if (!it.isBooked) {
                val orderCheckFragment = OrderCheckFragment.newInstance(
                    selectedDate.toString(),
                    it.reserveTime,
                    serviceArrayList,
                    designerBeanHid
                )
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.calendarCheckLayout, orderCheckFragment, null)
                    ?.addToBackStack(null)?.commit()
            }
        }
        binding.rvReservation.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = checkCalendarAdapter
        }
    }

    class DayViewContainer(view: View) : ViewContainer(view) {
        val binding = CalendarDayBinding.bind(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}