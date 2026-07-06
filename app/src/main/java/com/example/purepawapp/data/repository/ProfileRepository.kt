package com.example.purepawapp.data.repository

import com.example.purepawapp.data.model.User
import com.example.purepawapp.util.FirestoreCollections
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

interface ProfileRepository {
    suspend fun getUser(uid: String): Result<User>
    suspend fun updateUser(user: User): Result<Unit>
    suspend fun getAllUsers(): Result<List<User>>
}

class ProfileRepositoryImpl(
    private val firestore: FirebaseFirestore
) : ProfileRepository {

    override suspend fun getUser(uid: String): Result<User> = runCatching {
        val doc = firestore.collection(FirestoreCollections.USERS).document(uid).get().await()
        doc.toObject(User::class.java) ?: error("User not found")
    }

    override suspend fun updateUser(user: User): Result<Unit> = runCatching {
        firestore.collection(FirestoreCollections.USERS).document(user.uid).set(user).await()
        Unit
    }

    override suspend fun getAllUsers(): Result<List<User>> = runCatching {
        val snapshot = firestore.collection(FirestoreCollections.USERS).get().await()
        snapshot.toObjects(User::class.java).sortedByDescending { it.createdAt }
    }
}
