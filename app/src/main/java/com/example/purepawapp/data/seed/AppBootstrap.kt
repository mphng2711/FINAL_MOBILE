package com.example.purepawapp.data.seed

import com.example.purepawapp.di.ServiceLocator

/**
 * One-time debug-only setup: seeds Firestore demo data and creates two ready-to-use
 * accounts (customer + admin) so the app can be exercised without manual data entry.
 * Runs once per install, gated by [com.example.purepawapp.data.session.SessionManager].
 */
object AppBootstrap {

    const val CUSTOMER_EMAIL = "customer@purepaw.vn"
    const val CUSTOMER_PASSWORD = "Customer123"
    const val ADMIN_EMAIL = "admin@purepaw.vn"
    const val ADMIN_PASSWORD = "Admin123"

    suspend fun runIfNeeded() {
        val sessionManager = ServiceLocator.sessionManager
        if (sessionManager.isBootstrapDoneOnce()) return

        runCatching { FirestoreSeeder(ServiceLocator.firestore).seedAll() }

        createAccountIfMissing(
            name = "Nguyễn Văn Khách",
            email = CUSTOMER_EMAIL,
            password = CUSTOMER_PASSWORD,
            phone = "0901234567",
            role = "user"
        )
        createAccountIfMissing(
            name = "Quản Trị Viên",
            email = ADMIN_EMAIL,
            password = ADMIN_PASSWORD,
            phone = "0909999999",
            role = "admin"
        )

        sessionManager.markBootstrapDone()
    }

    private suspend fun createAccountIfMissing(
        name: String,
        email: String,
        password: String,
        phone: String,
        role: String
    ) {
        // Failures (e.g. the account already exists from a previous install) are
        // ignored — this is a best-effort debug convenience, not a critical path.
        ServiceLocator.authRepository.signUp(name, email, password, phone, role)
            .onSuccess { ServiceLocator.authRepository.logout() }
    }
}
