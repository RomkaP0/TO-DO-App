package com.romkapo.todoapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import com.romkapo.todoapp.data.repository.MainRepository
import com.romkapo.todoapp.databinding.ActivityMainBinding
import com.romkapo.todoapp.presentation.screen.MainViewModel
import com.romkapo.todoapp.presentation.screen.MyState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private var isFirstOpen = true

    @Inject
    lateinit var repository: MainRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initInternetMonitoring()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun onResume() {
        super.onResume()
        if (!isFirstOpen) {
            viewModel.updateRepository()
            Snackbar.make(binding.navHostFragment, "Update data from remote", Snackbar.LENGTH_SHORT)
                .show()
        }else{
            isFirstOpen=false
        }
    }

    private fun initInternetMonitoring() {
        viewModel.state.observe(this) {
            val text = when (it) {
                MyState.Fetched -> {
                    viewModel.updateRepository()
                    getString(R.string.available_network_state)
                }

                MyState.Error -> getString(R.string.no_internet_connection)
            }
            Snackbar.make(binding.navHostFragment, text, Snackbar.LENGTH_SHORT).show()
        }
    }
}