package com.example.appproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.purepaw.app.models.Appointment;
import com.purepaw.app.models.DateItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DateAdapter dateAdapter;
    private final List<DateItem> dateItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupDateSelector();
        setupAppointmentsList();
    }

    private void setupDateSelector() {
        RecyclerView rvDates = findViewById(R.id.rvDates);
        rvDates.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Matches the week 15/06 - 21/06 shown in the screenshot.
        dateItems.add(new DateItem("CN", "15", true, true));
        dateItems.add(new DateItem("T2", "16", true, false));
        dateItems.add(new DateItem("T3", "17", true, false));
        dateItems.add(new DateItem("T4", "18", false, false));
        dateItems.add(new DateItem("T5", "19", false, false));
        dateItems.add(new DateItem("T6", "20", false, false));
        dateItems.add(new DateItem("T7", "21", false, false));

        dateAdapter = new DateAdapter(dateItems, position -> {
            for (int i = 0; i < dateItems.size(); i++) {
                dateItems.get(i).setSelected(i == position);
            }
            dateAdapter.notifyDataSetChanged();
        });

        rvDates.setAdapter(dateAdapter);
    }

    private void setupAppointmentsList() {
        RecyclerView rvAppointments = findViewById(R.id.rvAppointments);
        rvAppointments.setLayoutManager(new LinearLayoutManager(this));

        List<Appointment> appointments = new ArrayList<>();
        appointments.add(new Appointment(
                "09:00",
                R.drawable.ic_spa,
                "Spa toàn thân",
                Appointment.STATUS_CONFIRMED,
                "Đã xác nhận",
                "Nguyễn Thị Mai · Mèo Anh lông ngắn",
                "Mèo hay cắn, cần nhẹ nhàng"
        ));
        appointments.add(new Appointment(
                "10:30",
                R.drawable.ic_scissors,
                "Cắt lông",
                Appointment.STATUS_PENDING,
                "Chờ xác nhận",
                "Trần Văn Hùng · Chó Poodle",
                null
        ));
        appointments.add(new Appointment(
                "14:00",
                R.drawable.ic_shower,
                "Tắm & Sấy",
                Appointment.STATUS_DONE,
                "Hoàn thành",
                "Lê Thị Hoa · Chó Corgi",
                "Dùng sữa tắm hypo"
        ));

        rvAppointments.setAdapter(new AppointmentAdapter(appointments));
    }
}
