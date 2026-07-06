package com.example.purepawapp.data.repository;

import com.example.purepawapp.data.model.Order;
import com.example.purepawapp.data.model.OrderStatusEvent;
import com.example.purepawapp.util.FirestoreCollections;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderRepositoryImpl implements OrderRepository {

    private final FirebaseFirestore firestore;

    public OrderRepositoryImpl(FirebaseFirestore firestore) {
        this.firestore = firestore;
    }

    @Override
    public void placeOrder(Order order, RepoCallback<String> callback) {
        var docRef = firestore.collection(FirestoreCollections.ORDERS).document();
        order.setId(docRef.getId());
        docRef.set(order)
                .addOnSuccessListener(v -> callback.onSuccess(docRef.getId()))
                .addOnFailureListener(callback::onError);
    }

    @Override
    public void getOrders(String userId, RepoCallback<List<Order>> callback) {
        firestore.collection(FirestoreCollections.ORDERS)
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(snapshot -> {
                    List<Order> orders = snapshot.toObjects(Order.class);
                    Collections.sort(orders, Comparator.comparingLong(Order::getCreatedAt).reversed());
                    callback.onSuccess(orders);
                })
                .addOnFailureListener(callback::onError);
    }

    @Override
    public void getOrder(String orderId, RepoCallback<Order> callback) {
        firestore.collection(FirestoreCollections.ORDERS).document(orderId).get()
                .addOnSuccessListener(doc -> {
                    Order order = doc.toObject(Order.class);
                    if (order == null) {
                        callback.onError(new IllegalStateException("Order not found"));
                    } else {
                        callback.onSuccess(order);
                    }
                })
                .addOnFailureListener(callback::onError);
    }

    @Override
    public ListenerRegistration listenToOrder(String orderId, RepoCallback<Order> callback) {
        return firestore.collection(FirestoreCollections.ORDERS)
                .document(orderId)
                .addSnapshotListener((snapshot, error) -> {
                    if (error != null) {
                        callback.onError(error);
                        return;
                    }
                    if (snapshot != null) {
                        Order order = snapshot.toObject(Order.class);
                        if (order != null) callback.onSuccess(order);
                    }
                });
    }

    @Override
    public void getAllOrders(RepoCallback<List<Order>> callback) {
        firestore.collection(FirestoreCollections.ORDERS).get()
                .addOnSuccessListener(snapshot -> {
                    List<Order> orders = snapshot.toObjects(Order.class);
                    Collections.sort(orders, Comparator.comparingLong(Order::getCreatedAt).reversed());
                    callback.onSuccess(orders);
                })
                .addOnFailureListener(callback::onError);
    }

    @Override
    public void updateOrderStatus(String orderId, String status, RepoCallback<Void> callback) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("status", status);
        updates.put("statusHistory", FieldValue.arrayUnion(new OrderStatusEvent(status, System.currentTimeMillis())));
        firestore.collection(FirestoreCollections.ORDERS).document(orderId)
                .update(updates)
                .addOnSuccessListener(v -> callback.onSuccess(null))
                .addOnFailureListener(callback::onError);
    }
}
