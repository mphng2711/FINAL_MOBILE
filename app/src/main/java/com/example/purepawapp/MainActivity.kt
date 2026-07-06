package com.example.purepawapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.purepawapp.di.ServiceLocator
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val topLevelGraphIds = setOf(
        R.id.home_nav_graph,
        R.id.product_nav_graph,
        R.id.spa_nav_graph,
        R.id.cart_nav_graph,
        R.id.account_nav_graph
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val rootView = findViewById<androidx.coordinatorlayout.widget.CoordinatorLayout>(R.id.root_coordinator)
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, 0, systemBars.right, 0)
            insets
        }

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNav.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val parentId = destination.parent?.id
            val showBottomNav = parentId in topLevelGraphIds || destination.id == R.id.productDetailFragment
            bottomNav.visibility = if (showBottomNav) {
                android.view.View.VISIBLE
            } else {
                android.view.View.GONE
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                ServiceLocator.cartRepository.items.collect { items ->
                    val count = items.sumOf { it.quantity }
                    val badge = bottomNav.getOrCreateBadge(R.id.cart_nav_graph)
                    badge.isVisible = count > 0
                    badge.number = count
                }
            }
        }
    }
}
