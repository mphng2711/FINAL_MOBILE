package com.example.purepawapp.data.repository;

import com.example.purepawapp.data.model.Booking;
import com.example.purepawapp.data.model.SpaService;
import com.example.purepawapp.util.FirestoreCollections;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.SetOptions;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpaRepositoryImpl implements SpaRepository {

    private final FirebaseFirestore firestore;

    public SpaRepositoryImpl(FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    @Override
    public void getSpaServices(RepoCallback<List<SpaService>> callback) {
        firestore.collection(FirestoreCollections.SPA_SERVICES).get()
                .addOnSuccessListener(snapshot -> callback.onSuccess(snapshot.toObjects(SpaService.class)))
                .addOnFailureListener(callback::onError);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void getBookedSlots(String date, RepoCallback<List<String>> callback) {
        firestore.collection(FirestoreCollections.BOOKED_SLOTS).document(date).get()
                .addOnSuccessListener(doc -> {
                    Object slots = doc.get("slots");
                    if (slots instanceof List) {
                        callback.onSuccess((List<String>) slots);
                    } else {
                        callback.onSuccess(Collections.emptyList());
                    }
                })
                .addOnFailureListener(callback::onError);
    }

    @Override
    public void createBooking(Booking booking, RepoCallback<String> callback) {
        var docRef = firestore.collection(FirestoreCollections.BOOKINGS).document();
        booking.setId(docRef.getId());
        docRef.set(booking)
                .addOnSuccessListener(v -> {
                    Map<String, Object> update = new HashMap<>();
                    update.put("slots", FieldValue.arrayUnion(booking.getTimeSlot()));
                    firestore.collection(FirestoreCollections.BOOKED_SLOTS)
                            .document(booking.getBookingDate())
                            .set(update, SetOptions.merge())
                            .addOnSuccessListener(v2 -> callback.onSuccess(docRef.getId()))
                            .addOnFailureListener(callback::onError);
                })
                .addOnFailureListener(callback::onError);
    }

    @Override
    public void getBookings(String userId, RepoCallback<List<Booking>> callback) {
        firestore.collection(FirestoreCollections.BOOKINGS)
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(snapshot -> {
                    List<Booking> bookings = snapshot.toObjects(Booking.class);
                    Collections.sort(bookings, Comparator.comparingLong(Booking::getCreatedAt).reversed());
                    callback.onSuccess(bookings);
                })
                .addOnFailureListener(callback::onError);
    }

    @Override
    public ListenerRegistration listenToBookings(String userId, RepoCallback<List<Booking>> callback) {
        return firestore.collection(FirestoreCollections.BOOKINGS)
                .whereEqualTo("userId", userId)
                .addSnapshotListener((snapshot, error) -> {
                    if (error != null) {
                        callback.onError(error);
                        return;
                    }
                    if (snapshot != null) {
                        List<Booking> bookings = snapshot.toObjects(Booking.class);
                        Collections.sort(bookings, Comparator.comparingLong(Booking::getCreatedAt).reversed());
                        callback.onSuccess(bookings);
                    }
                });
    }

    @Override
    public void submitRating(String bookingId, int rating, String review, RepoCallback<Void> callback) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("rating", rating);
        updates.put("review", review);
        firestore.collection(FirestoreCollections.BOOKINGS).document(bookingId)
                .update(updates)
                .addOnSuccessListener(v -> callback.onSuccess(null))
                .addOnFailureListener(callback::onError);
    }

    @Override
    public void getAllBookings(RepoCallback<List<Booking>> callback) {
        firestore.collection(FirestoreCollections.BOOKINGS).get()
                .addOnSuccessListener(snapshot -> {
                    List<Booking> bookings = snapshot.toObjects(Booking.class);
                    Collections.sort(bookings, Comparator.comparingLong(Booking::getCreatedAt).reversed());
                    callback.onSuccess(bookings);
                })
                .addOnFailureListener(callback::onError);
    }

    @Override
    public void updateBookingStatus(String bookingId, String status, RepoCallback<Void> callback) {
        firestore.collection(FirestoreCollections.BOOKINGS).document(bookingId)
                .update("status", status)
                .addOnSuccessListener(v -> callback.onSuccess(null))
                .addOnFailureListener(callback::onError);
    }
}
