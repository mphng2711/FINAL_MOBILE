package com.example.purepawapp.ui.spa;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.navigation.fragment.NavHostFragment;

import com.example.purepawapp.R;
import com.example.purepawapp.databinding.FragmentSpaDatetimeBinding;
import com.example.purepawapp.di.ServiceLocator;
import com.example.purepawapp.ui.common.BaseFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class SpaDateTimeFragment extends BaseFragment<FragmentSpaDatetimeBinding> {

    private static final int QUICK_PICK_DAYS = 7;

    private int selectedDateIndex = 0;
    private Calendar selectedDate = todayAtMidnight();
    private Chip selectedTimeChip;
    private List<Calendar> quickPickDates;

    public SpaDateTimeFragment() {
        super(FragmentSpaDatetimeBinding::inflate);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getBinding().btnBack.setOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());

        quickPickDates = new ArrayList<>();
        for (int offset = 0; offset < QUICK_PICK_DAYS; offset++) {
            Calendar cal = (Calendar) todayAtMidnight().clone();
            cal.add(Calendar.DAY_OF_MONTH, offset);
            quickPickDates.add(cal);
        }

        List<TextView> dateChips = List.of(getBinding().chipDate0, getBinding().chipDate1, getBinding().chipDate2,
                getBinding().chipDate3, getBinding().chipDate4, getBinding().chipDate5, getBinding().chipDate6);
        for (int i = 0; i < dateChips.size(); i++) {
            int index = i;
            TextView chip = dateChips.get(i);
            chip.setText(index == 0 ? "Hôm nay" : shortWeekdayLabel(quickPickDates.get(index)));
            chip.setOnClickListener(v -> {
                selectedDateIndex = index;
                selectedDate = quickPickDates.get(index);
                updateDateChips(dateChips);
                updateSelectedDateLabel();
            });
        }
        updateDateChips(dateChips);
        updateSelectedDateLabel();

        getBinding().btnOpenCalendar.setOnClickListener(v -> openCalendarPicker(dateChips));

        List<Chip> timeChips = List.of(getBinding().chipTime0900, getBinding().chipTime1300,
                getBinding().chipTime1430, getBinding().chipTime1730);
        selectedTimeChip = getBinding().chipTime0900;
        for (Chip chip : timeChips) {
            chip.setOnClickListener(v -> {
                selectedTimeChip = chip;
                updateTimeChips(timeChips);
            });
        }

        getBinding().btnNext.setOnClickListener(v -> {
            String isoDate = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(selectedDate.getTime());
            String dateLabel = (selectedDateIndex == 0 ? "Hôm nay, " : fullWeekdayLabel(selectedDate) + ", ") + fullDateFormat(selectedDate);
            String timeSlot = selectedTimeChip.getText().toString();
            ServiceLocator.getBookingDraft().setDateTime(isoDate, dateLabel, timeSlot);
            NavHostFragment.findNavController(this).navigate(R.id.action_spaDateTimeFragment_to_spaPetInfoFragment);
        });
    }

    private void openCalendarPicker(List<TextView> dateChips) {
        CalendarConstraints constraints = new CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.now())
                .build();
        MaterialDatePicker<Long> picker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Chọn ngày hẹn")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(constraints)
                .build();
        picker.addOnPositiveButtonClickListener(utcMillis -> {
            selectedDate = utcMillisToLocalCalendar(utcMillis);
            selectedDateIndex = -1;
            for (int i = 0; i < quickPickDates.size(); i++) {
                if (isSameDay(quickPickDates.get(i), selectedDate)) {
                    selectedDateIndex = i;
                    break;
                }
            }
            updateDateChips(dateChips);
            updateSelectedDateLabel();
        });
        picker.show(getParentFragmentManager(), "spa_date_picker");
    }

    private void updateSelectedDateLabel() {
        String label = selectedDateIndex == 0
                ? "Đã chọn: Hôm nay, " + fullDateFormat(selectedDate)
                : "Đã chọn: " + fullWeekdayLabel(selectedDate) + ", " + fullDateFormat(selectedDate);
        getBinding().tvSelectedDateFull.setText(label);
    }

    private void updateDateChips(List<TextView> chips) {
        for (int i = 0; i < chips.size(); i++) {
            boolean isSelected = i == selectedDateIndex;
            TextView chip = chips.get(i);
            chip.setBackgroundTintList(getResources().getColorStateList(isSelected ? R.color.pp_primary : R.color.white, null));
            chip.setTextColor(getResources().getColor(isSelected ? R.color.white : R.color.pp_text_primary, null));
        }
    }

    private void updateTimeChips(List<Chip> chips) {
        for (Chip chip : chips) {
            boolean isSelected = chip == selectedTimeChip;
            chip.setChipBackgroundColor(getResources().getColorStateList(isSelected ? R.color.pp_primary : R.color.pp_surface, null));
            chip.setTextColor(getResources().getColor(isSelected ? R.color.white : R.color.pp_text_primary, null));
            chip.setChipStrokeWidth(isSelected ? 0f : 1f);
        }
    }

    private static Calendar todayAtMidnight() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

    private static boolean isSameDay(Calendar a, Calendar b) {
        return a.get(Calendar.YEAR) == b.get(Calendar.YEAR) && a.get(Calendar.DAY_OF_YEAR) == b.get(Calendar.DAY_OF_YEAR);
    }

    private static Calendar utcMillisToLocalCalendar(long utcMillis) {
        Calendar utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        utc.setTimeInMillis(utcMillis);
        Calendar result = todayAtMidnight();
        result.set(utc.get(Calendar.YEAR), utc.get(Calendar.MONTH), utc.get(Calendar.DAY_OF_MONTH));
        return result;
    }

    private static String shortWeekdayLabel(Calendar date) {
        switch (date.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY: return "CN";
            case Calendar.MONDAY: return "Thứ 2";
            case Calendar.TUESDAY: return "Thứ 3";
            case Calendar.WEDNESDAY: return "Thứ 4";
            case Calendar.THURSDAY: return "Thứ 5";
            case Calendar.FRIDAY: return "Thứ 6";
            default: return "Thứ 7";
        }
    }

    private static String fullWeekdayLabel(Calendar date) {
        switch (date.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY: return "Chủ Nhật";
            case Calendar.MONDAY: return "Thứ Hai";
            case Calendar.TUESDAY: return "Thứ Ba";
            case Calendar.WEDNESDAY: return "Thứ Tư";
            case Calendar.THURSDAY: return "Thứ Năm";
            case Calendar.FRIDAY: return "Thứ Sáu";
            default: return "Thứ Bảy";
        }
    }

    private static String fullDateFormat(Calendar date) {
        return new SimpleDateFormat("dd/MM/yyyy", new Locale("vi", "VN")).format(date.getTime());
    }
}
