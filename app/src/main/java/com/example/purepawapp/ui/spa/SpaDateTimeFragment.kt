package com.example.purepawapp.ui.spa

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.purepawapp.R
import com.example.purepawapp.databinding.FragmentSpaDatetimeBinding
import com.example.purepawapp.ui.common.BaseFragment
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

private const val QUICK_PICK_DAYS = 7

class SpaDateTimeFragment : BaseFragment<FragmentSpaDatetimeBinding>(FragmentSpaDatetimeBinding::inflate) {

    private var selectedDateIndex = 0
    private var selectedDate: Calendar = todayAtMidnight()
    private var selectedTimeChip: Chip? = null
    private lateinit var quickPickDates: List<Calendar>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        quickPickDates = (0 until QUICK_PICK_DAYS).map { offset ->
            (todayAtMidnight().clone() as Calendar).apply { add(Calendar.DAY_OF_MONTH, offset) }
        }

        val dateChips = listOf(
            binding.chipDate0,
            binding.chipDate1,
            binding.chipDate2,
            binding.chipDate3,
            binding.chipDate4,
            binding.chipDate5,
            binding.chipDate6
        )
        dateChips.forEachIndexed { index, chip ->
            chip.text = if (index == 0) "Hôm nay" else shortWeekdayLabel(quickPickDates[index])
            chip.setOnClickListener {
                selectedDateIndex = index
                selectedDate = quickPickDates[index]
                updateDateChips(dateChips)
                updateSelectedDateLabel()
            }
        }
        updateDateChips(dateChips)
        updateSelectedDateLabel()

        binding.btnOpenCalendar.setOnClickListener {
            openCalendarPicker(dateChips)
        }

        val timeChips = listOf(
            binding.chipTime0900,
            binding.chipTime1300,
            binding.chipTime1430,
            binding.chipTime1730
        )
        selectedTimeChip = binding.chipTime0900
        timeChips.forEach { chip ->
            chip.setOnClickListener {
                selectedTimeChip = chip
                updateTimeChips(timeChips)
            }
        }

        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_spaDateTimeFragment_to_spaPetInfoFragment)
        }
    }

    private fun openCalendarPicker(dateChips: List<TextView>) {
        val constraints = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.now())
            .build()
        val picker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Chọn ngày hẹn")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setCalendarConstraints(constraints)
            .build()
        picker.addOnPositiveButtonClickListener { utcMillis ->
            selectedDate = utcMillisToLocalCalendar(utcMillis)
            selectedDateIndex = quickPickDates.indexOfFirst { isSameDay(it, selectedDate) }
            updateDateChips(dateChips)
            updateSelectedDateLabel()
        }
        picker.show(parentFragmentManager, "spa_date_picker")
    }

    private fun updateSelectedDateLabel() {
        val label = if (selectedDateIndex == 0) {
            "Đã chọn: Hôm nay, ${fullDateFormat(selectedDate)}"
        } else {
            "Đã chọn: ${fullWeekdayLabel(selectedDate)}, ${fullDateFormat(selectedDate)}"
        }
        binding.tvSelectedDateFull.text = label
    }

    private fun updateDateChips(chips: List<TextView>) {
        chips.forEachIndexed { index, chip ->
            val isSelected = index == selectedDateIndex
            chip.backgroundTintList = resources.getColorStateList(
                if (isSelected) R.color.pp_primary else R.color.white,
                null
            )
            chip.setTextColor(
                resources.getColor(
                    if (isSelected) R.color.white else R.color.pp_text_primary,
                    null
                )
            )
        }
    }

    private fun updateTimeChips(chips: List<Chip>) {
        chips.forEach { chip ->
            val isSelected = chip == selectedTimeChip
            chip.chipBackgroundColor = resources.getColorStateList(
                if (isSelected) R.color.pp_primary else R.color.pp_surface,
                null
            )
            chip.setTextColor(
                resources.getColor(
                    if (isSelected) R.color.white else R.color.pp_text_primary,
                    null
                )
            )
            chip.chipStrokeWidth = if (isSelected) 0f else 1f
        }
    }
}

private fun todayAtMidnight(): Calendar = Calendar.getInstance().apply {
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
}

private fun isSameDay(a: Calendar, b: Calendar): Boolean =
    a.get(Calendar.YEAR) == b.get(Calendar.YEAR) && a.get(Calendar.DAY_OF_YEAR) == b.get(Calendar.DAY_OF_YEAR)

private fun utcMillisToLocalCalendar(utcMillis: Long): Calendar {
    val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply { timeInMillis = utcMillis }
    return todayAtMidnight().apply {
        set(utc.get(Calendar.YEAR), utc.get(Calendar.MONTH), utc.get(Calendar.DAY_OF_MONTH))
    }
}

private fun shortWeekdayLabel(date: Calendar): String = when (date.get(Calendar.DAY_OF_WEEK)) {
    Calendar.SUNDAY -> "CN"
    Calendar.MONDAY -> "Thứ 2"
    Calendar.TUESDAY -> "Thứ 3"
    Calendar.WEDNESDAY -> "Thứ 4"
    Calendar.THURSDAY -> "Thứ 5"
    Calendar.FRIDAY -> "Thứ 6"
    else -> "Thứ 7"
}

private fun fullWeekdayLabel(date: Calendar): String = when (date.get(Calendar.DAY_OF_WEEK)) {
    Calendar.SUNDAY -> "Chủ Nhật"
    Calendar.MONDAY -> "Thứ Hai"
    Calendar.TUESDAY -> "Thứ Ba"
    Calendar.WEDNESDAY -> "Thứ Tư"
    Calendar.THURSDAY -> "Thứ Năm"
    Calendar.FRIDAY -> "Thứ Sáu"
    else -> "Thứ Bảy"
}

private fun fullDateFormat(date: Calendar): String =
    SimpleDateFormat("dd/MM/yyyy", Locale("vi", "VN")).format(date.time)
