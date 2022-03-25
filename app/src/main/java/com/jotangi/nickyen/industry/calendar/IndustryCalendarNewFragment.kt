package com.jotangi.nickyen.industry.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.jotangi.nickyen.R
import com.jotangi.nickyen.base.BaseFragment
import com.jotangi.nickyen.beautySalons.calendar.Reservation
import com.jotangi.nickyen.databinding.CalendarDayBinding
import com.jotangi.nickyen.databinding.FragmentIndustryCalendarNewBinding
import com.jotangi.nickyen.industry.model.ClassBean
import com.jotangi.nickyen.industry.model.ProgramBean
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

class IndustryCalendarNewFragment : BaseFragment() {

    private var _binding: FragmentIndustryCalendarNewBinding? = null
    val binding get() = _binding!!

    private lateinit var viewModel: IndustryCalendarViewModel

    private var classBean: ClassBean? = null
    private var programBean: ProgramBean? = null
    private var numberOfPeople: String? = null
    private var remark: String? = null
//    private var programList = ArrayList<ProgramBean>()

    private var selectedDate: LocalDate? = null
    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")
    private val today = LocalDate.now().plusDays(4)

    private lateinit var industryCalendarAdapter: IndustryCalendarAdapter
    private var reservationList = mutableListOf<Reservation>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            classBean = Gson().fromJson(it.getString("classBean"), ClassBean::class.java)
            programBean = Gson().fromJson(it.getString("plan"), ProgramBean::class.java)
            numberOfPeople = it.getString("people")
            remark = it.getString("remark")

//            val type = object : TypeToken<ArrayList<ProgramBean?>?>() {}.type
//            Gson().fromJson<ArrayList<ProgramBean>>((it.getString("plan")), type)?.let { it ->
//                programList = it
//            }
        }
        viewModel = ViewModelProvider(this).get(IndustryCalendarViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentIndustryCalendarNewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivToolbarBack.setOnClickListener {
            findNavController(
                requireView()
            ).popBackStack()
        }

        initReservationList()

        programBean?.pid?.let { it ->
            viewModel.getWorkingDay(it)

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

                industryCalendarAdapter?.notifyDataSetChanged()
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
        binding.calendarView.post() {
            selectedDate(today)
        }
        binding.calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            // Called only when a new container is needed
            override fun create(view: View) = DayViewContainer(view)

            // Called every time we need to reuse a container
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.binding.tvDayText.apply {
                    text = day.date.dayOfMonth.toString()
                    setOnClickListener {
                        Timber.e("DayViewContainer(setOnClickListener) -> $day")
                        if (day.date >= today && day.owner == DayOwner.THIS_MONTH) {
                            selectedDate(day.date)
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

    private fun selectedDate(date: LocalDate) {
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
                classBean?.classCode?.let {
                    viewModel.getBookingDay(
                        pid = it,
                        workingDate = date.toString()
                    )
                }
            }
            reservationList.clear()
            industryCalendarAdapter?.notifyDataSetChanged()
        }
    }

    private fun initReservationList() {
        industryCalendarAdapter = IndustryCalendarAdapter(reservationList)
        industryCalendarAdapter.onItemClick = {
            Timber.e("$it")
            if (!it.isBooked) {

                val bundle = bundleOf(
                    "classBean" to Gson().toJson(classBean),
                    "plan" to Gson().toJson(programBean),
                    "count" to numberOfPeople,
                    "remark" to remark,
                    "date" to selectedDate.toString(),
                    "time" to it.reserveTime
                )
                //                controller = Navigation.findNavController(v);
                findNavController(requireView()).navigate(
                    R.id.action_industryCalendarNewFragment_to_industryOrderCheckFragment,
                    bundle
                )
            }
        }
        binding.rvReservation.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = industryCalendarAdapter
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