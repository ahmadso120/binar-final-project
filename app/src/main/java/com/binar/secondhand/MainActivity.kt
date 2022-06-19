package com.binar.secondhand

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.binar.secondhand.databinding.ActivityMainBinding
import com.binar.secondhand.storage.AppLocalData
import com.binar.secondhand.ui.common.ConnectionViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val appLocalData: AppLocalData by inject()

    private val connectionViewModel by viewModel<ConnectionViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            banner.setLeftButtonAction { banner.dismiss() }
            banner.setRightButtonAction {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val panelIntent = Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY)
                    startActivity(panelIntent)
                } else {
                    val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
                    startActivity(intent)
                }
            }
            connectionViewModel.hasConnection.observe(this@MainActivity) {
                if (it) {
                    banner.isVisible = false
                } else {
                    banner.show()
                    banner.isVisible = true
                }
            }

            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController = navHostFragment.navController
            bottomNavigation.setupWithNavController(navController)

            val token = appLocalData.getAccessToken

            if (!token.isNullOrEmpty()) {
                navController.navigate(R.id.action_loginFragment_to_homeFragment)
            }
        }
    }

    fun setBottomNavigationVisibility(visibility: Int) {
        binding.bottomNavigation.visibility = visibility
    }
}