package com.example.purepawapp.data.repository;

import com.example.purepawapp.data.model.Booking;
import com.example.purepawapp.data.model.SpaService;

import java.util.List;

public interface SpaRepository {
    void getSpaServices(RepoCallback<List<SpaService>> callback);

    void getBookedSlots(String date, RepoCallback<List<String>> callback);

    void createBooking(Booking booking, RepoCallback<String> callback);

    void getBookings(String userId, RepoCallback<List<Booking>> callback);

    void submitRating(String bookingId, int rating, String review, RepoCallback<Void> callback);

    void getAllBookings(RepoCallback<List<Booking>> callback);

    void updateBookingStatus(String bookingId, String status, RepoCallback<Void> callback);
}
