package com.example.purepawapp.ui.admin;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.purepawapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Set;

public class AdminActivity extends AppCompatActivity {

    private final Set<Integer> topLevelDestinationIds = Set.of(
            R.id.adminDashboardFragment,
            R.id.adminOrdersFragment,
            R.id.adminProductsFragment,
            R.id.adminMoreFragment
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin);

        CoordinatorLayout rootView = findViewById(R.id.root_coordinator);
        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            var systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, 0, systemBars.right, 0);
            return insets;
        });

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.admin_nav_host_fragment);
        var navController = navHostFragment.getNavController();

        BottomNavigationView bottomNav = findViewById(R.id.admin_bottom_nav_view);
        NavigationUI.setupWithNavController(bottomNav, navController);

        navController.addOnDestinationChangedListener((controller, destination, arguments) ->
                bottomNav.setVisibility(topLevelDestinationIds.contains(destination.getId()) ? View.VISIBLE : View.GONE));
    }
}
