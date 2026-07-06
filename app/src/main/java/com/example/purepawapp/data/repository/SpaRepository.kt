package com.example.purepawapp.data.repository

import com.example.purepawapp.data.model.Booking
import com.example.purepawapp.data.model.SpaService
import com.example.purepawapp.util.FirestoreCollections
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

interface SpaRepository {
    suspend fun getSpaServices(): Result<List<SpaService>>
    suspend fun getBookedSlots(date: String): Result<List<String>>
    suspend fun createBooking(booking: Booking): Result<String>
    suspend fun getBookings(userId: String): Result<List<Booking>>
    suspend fun submitRating(bookingId: String, rating: Int, review: String): Result<Unit>
    suspend fun getAllBookings(): Result<List<Booking>>
    suspend fun updateBookingStatus(bookingId: String, status: String): Result<Unit>
}

class SpaRepositoryImpl(
    private val firestore: FirebaseFirestore
) : SpaRepository {

    override suspend fun getSpaServices(): Result<List<SpaService>> = runCatching {
        val snapshot = firestore.collection(FirestoreCollections.SPA_SERVICES).get().await()
        snapshot.toObjects(SpaService::class.java)
    }

    override suspend fun getBookedSlots(date: String): Result<List<String>> = runCatching {
        val doc = firestore.collection(FirestoreCollections.BOOKED_SLOTS).document(date).get().await()
        @Suppress("UNCHECKED_CAST")
        (doc.get("slots") as? List<String>).orEmpty()
    }

    override suspend fun createBooking(booking: Booking): Result<String> = runCatching {
        val docRef = firestore.collection(FirestoreCollections.BOOKINGS).document()
        val bookingWithId = booking.copy(id = docRef.id)
        docRef.set(bookingWithId).await()

        firestore.collection(FirestoreCollections.BOOKED_SLOTS)
            .document(booking.bookingDate)
            .set(mapOf("slots" to FieldValue.arrayUnion(booking.timeSlot)), SetOptions.merge())
            .await()

        docRef.id
    }

    override suspend fun getBookings(userId: String): Result<List<Booking>> = runCatching {
        val snapshot = firestore.collection(FirestoreCollections.BOOKINGS)
            .whereEqualTo("userId", userId)
            .get()
            .await()
        snapshot.toObjects(Booking::class.java).sortedByDescending { it.createdAt }
    }

    override suspend fun submitRating(bookingId: String, rating: Int, review: String): Result<Unit> = runCatching {
        firestore.collection(FirestoreCollections.BOOKINGS).document(bookingId)
            .update(mapOf("rating" to rating, "review" to review))
            .await()
        Unit
    }

    override suspend fun getAllBookings(): Result<List<Booking>> = runCatching {
        val snapshot = firestore.collection(FirestoreCollections.BOOKINGS).get().await()
        snapshot.toObjects(Booking::class.java).sortedByDescending { it.createdAt }
    }

    override suspend fun updateBookingStatus(bookingId: String, status: String): Result<Unit> = runCatching {
        firestore.collection(FirestoreCollections.BOOKINGS).document(bookingId)
            .update("status", status)
            .await()
        Unit
    }
}
