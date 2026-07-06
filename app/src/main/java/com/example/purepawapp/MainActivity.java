package com.example.purepawapp;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.purepawapp.di.ServiceLocator;
import com.example.purepawapp.ui.common.LoadingHost;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Set;

public class MainActivity extends AppCompatActivity implements LoadingHost {

    private final Set<Integer> topLevelGraphIds = Set.of(
            R.id.home_nav_graph,
            R.id.product_nav_graph,
            R.id.spa_nav_graph,
            R.id.cart_nav_graph,
            R.id.account_nav_graph
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        CoordinatorLayout rootView = findViewById(R.id.root_coordinator);
        ViewCompat.setOnApplyWindowInsetsListener(rootView, (v, insets) -> {
            var systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, 0, systemBars.right, 0);
            return insets;
        });

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        var navController = navHostFragment.getNavController();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav_view);
        NavigationUI.setupWithNavController(bottomNav, navController);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            NavDestination parent = destination.getParent();
            boolean showBottomNav = (parent != null && topLevelGraphIds.contains(parent.getId()))
                    || destination.getId() == R.id.productDetailFragment;
            bottomNav.setVisibility(showBottomNav ? View.VISIBLE : View.GONE);
        });

        ServiceLocator.getCartRepository().getItemsLiveData().observe(this, items -> {
            int count = 0;
            for (var item : items) count += item.getQuantity();
            BadgeDrawable badge = bottomNav.getOrCreateBadge(R.id.cart_nav_graph);
            badge.setVisible(count > 0);
            badge.setNumber(count);
        });
    }

    @Override
    public void showLoading() {
        View overlay = findViewById(R.id.loading_overlay);
        if (overlay != null) overlay.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        View overlay = findViewById(R.id.loading_overlay);
        if (overlay != null) overlay.setVisibility(View.GONE);
    }
}
