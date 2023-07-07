package com.romkapo.todoapp.presentation.screen.main

import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import com.romkapo.todoapp.R
import com.romkapo.todoapp.appComponent
import com.romkapo.todoapp.core.components.main.MainActivityComponent
import com.romkapo.todoapp.databinding.ActivityMainBinding
import com.romkapo.todoapp.utils.ViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private var isFirstOpen = true

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var mainComponent: MainActivityComponent
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        mainComponent = (applicationContext as Application).appComponent.mainActivityComponentFactory().create()
        mainComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
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
        lifecycleScope.launch {
            viewModel.state.collect {
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
}}