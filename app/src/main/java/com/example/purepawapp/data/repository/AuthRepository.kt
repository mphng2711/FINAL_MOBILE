package com.example.purepawapp.data.repository

import com.example.purepawapp.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.purepawapp.util.FirestoreCollections
import kotlinx.coroutines.tasks.await

interface AuthRepository {
    val currentUserId: String?
    suspend fun signUp(
        name: String,
        email: String,
        password: String,
        phone: String,
        role: String = "user"
    ): Result<String>
    suspend fun login(email: String, password: String): Result<String>
    fun logout()
}

class AuthRepositoryImpl(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override val currentUserId: String?
        get() = auth.currentUser?.uid

    override suspend fun signUp(
        name: String,
        email: String,
        password: String,
        phone: String,
        role: String
    ): Result<String> = runCatching {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val uid = result.user?.uid ?: error("Sign up failed: no user id returned")
        val user = User(uid = uid, fullName = name, email = email, phone = phone, role = role)
        firestore.collection(FirestoreCollections.USERS).document(uid).set(user).await()
        uid
    }

    override suspend fun login(email: String, password: String): Result<String> = runCatching {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        result.user?.uid ?: error("Login failed: no user id returned")
    }

    override fun logout() {
        auth.signOut()
    }
}
