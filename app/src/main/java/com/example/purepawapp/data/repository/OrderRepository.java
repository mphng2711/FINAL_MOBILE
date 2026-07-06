package com.example.purepawapp.data.repository;

import com.example.purepawapp.data.model.Order;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.List;

public interface OrderRepository {
    void placeOrder(Order order, RepoCallback<String> callback);

    void getOrders(String userId, RepoCallback<List<Order>> callback);

    void getOrder(String orderId, RepoCallback<Order> callback);

    ListenerRegistration listenToOrder(String orderId, RepoCallback<Order> callback);

    void getAllOrders(RepoCallback<List<Order>> callback);

    void updateOrderStatus(String orderId, String status, RepoCallback<Void> callback);
}
