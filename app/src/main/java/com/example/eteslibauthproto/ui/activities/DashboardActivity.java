package com.example.eteslibauthproto.ui.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.eteslibauthproto.R;
import com.example.eteslibauthproto.databinding.ActivityDashboardBinding;
import com.example.eteslibauthproto.ui.fragments.HomeFragment;
import com.example.eteslibauthproto.ui.fragments.ProfileFragment;
import com.example.eteslibauthproto.ui.fragments.SavedBooksFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

// TODO: Add the red dot above bottom navigation icons when there is unread notifications/update

public class DashboardActivity extends BaseActivity {

    private ActivityDashboardBinding binding;
    private BottomNavigationView bottomNavView;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bottomNavView = binding.navView;

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_dashboard);

        bottomNavView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                // Do nothing :D
            }
        });

        bottomNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        while(navController.getCurrentDestination().getId() != R.id.navigation_home) {
                            navController.navigateUp();
                        }
                        return true;
                    case R.id.navigation_profile:
                        navController.navigate(R.id.navigation_profile);
                        return true;
                    case R.id.navigation_saved_books:
                        navController.navigate(R.id.navigation_saved_books);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(navController.getCurrentDestination().getId() == R.id.navigation_home) {
            doubleBackToExit();
        } else {
            while(navController.getCurrentDestination().getId() != R.id.navigation_home) {
                navController.navigateUp();
                bottomNavView.getMenu().getItem(0).setChecked(true);
                navController.popBackStack();
            }
        }
    }
}