package com.example.purepawapp.data.repository

import com.example.purepawapp.data.model.Order
import com.example.purepawapp.data.model.OrderStatusEvent
import com.example.purepawapp.util.FirestoreCollections
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

interface OrderRepository {
    suspend fun placeOrder(order: Order): Result<String>
    suspend fun getOrders(userId: String): Result<List<Order>>
    suspend fun getOrder(orderId: String): Result<Order>
    fun listenToOrder(orderId: String): Flow<Order>
    suspend fun getAllOrders(): Result<List<Order>>
    suspend fun updateOrderStatus(orderId: String, status: String): Result<Unit>
}

class OrderRepositoryImpl(
    private val firestore: FirebaseFirestore
) : OrderRepository {

    override suspend fun placeOrder(order: Order): Result<String> = runCatching {
        val docRef = firestore.collection(FirestoreCollections.ORDERS).document()
        val orderWithId = order.copy(id = docRef.id)
        docRef.set(orderWithId).await()
        docRef.id
    }

    override suspend fun getOrders(userId: String): Result<List<Order>> = runCatching {
        val snapshot = firestore.collection(FirestoreCollections.ORDERS)
            .whereEqualTo("userId", userId)
            .get()
            .await()
        snapshot.toObjects(Order::class.java).sortedByDescending { it.createdAt }
    }

    override suspend fun getOrder(orderId: String): Result<Order> = runCatching {
        val doc = firestore.collection(FirestoreCollections.ORDERS).document(orderId).get().await()
        doc.toObject(Order::class.java) ?: error("Order not found")
    }

    override fun listenToOrder(orderId: String): Flow<Order> = callbackFlow {
        val registration = firestore.collection(FirestoreCollections.ORDERS)
            .document(orderId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                snapshot?.toObject(Order::class.java)?.let { trySend(it) }
            }
        awaitClose { registration.remove() }
    }

    override suspend fun getAllOrders(): Result<List<Order>> = runCatching {
        val snapshot = firestore.collection(FirestoreCollections.ORDERS).get().await()
        snapshot.toObjects(Order::class.java).sortedByDescending { it.createdAt }
    }

    override suspend fun updateOrderStatus(orderId: String, status: String): Result<Unit> = runCatching {
        firestore.collection(FirestoreCollections.ORDERS).document(orderId)
            .update(
                mapOf(
                    "status" to status,
                    "statusHistory" to FieldValue.arrayUnion(OrderStatusEvent(status = status))
                )
            )
            .await()
        Unit
    }
}
