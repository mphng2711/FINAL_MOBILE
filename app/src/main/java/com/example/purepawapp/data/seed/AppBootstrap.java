package com.example.purepawapp.data.seed;

import com.example.purepawapp.di.ServiceLocator;

public final class AppBootstrap {

    public static final String CUSTOMER_EMAIL = "customer@purepaw.vn";
    public static final String CUSTOMER_PASSWORD = "Customer123";
    public static final String ADMIN_EMAIL = "admin@purepaw.vn";
    public static final String ADMIN_PASSWORD = "Admin123";

    private AppBootstrap() {
    }

    public static void runIfNeeded() {
        var sessionManager = ServiceLocator.getSessionManager();
        if (sessionManager.isBootstrapDoneOnce()) return;

        try {
            new FirestoreSeeder(ServiceLocator.getFirestore()).seedAll();
        } catch (Exception ignored) {
        }

        createAccountIfMissing("Nguyễn Văn Khách", CUSTOMER_EMAIL, CUSTOMER_PASSWORD, "0901234567", "user");
        createAccountIfMissing("Quản Trị Viên", ADMIN_EMAIL, ADMIN_PASSWORD, "0909999999", "admin");

        sessionManager.markBootstrapDone();
    }

    private static void createAccountIfMissing(String name, String email, String password, String phone, String role) {
        // Failures (e.g. the account already exists from a previous install) are
        // ignored — this is a best-effort debug convenience, not a critical path.
        ServiceLocator.getAuthRepository().signUp(name, email, password, phone, role, new com.example.purepawapp.data.repository.RepoCallback<>() {
            @Override
            public void onSuccess(String result) {
                ServiceLocator.getAuthRepository().logout();
            }

            @Override
            public void onError(Exception error) {
            }
        });
    }
}
