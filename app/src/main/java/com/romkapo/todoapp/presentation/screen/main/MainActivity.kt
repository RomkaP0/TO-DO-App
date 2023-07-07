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
import com.romkapo.todoapp.data.model.Resource
import com.romkapo.todoapp.databinding.ActivityMainBinding
import com.romkapo.todoapp.utils.ViewModelFactory
import kotlinx.coroutines.flow.collectLatest
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
        mainComponent =
            (applicationContext as Application).appComponent.mainActivityComponentFactory().create()
        mainComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initInternetMonitoring()

        viewModel.listenStateRequest()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun initInternetMonitoring() {
        lifecycleScope.launch {
            viewModel.state.collect {
                if (it is MyState.Fetched) {
                    showSnackBar(getString(R.string.available_network_state))
                    viewModel.updateRepository()
                } else {
                    showSnackBar(getString(R.string.loading_failed_showing_local_data))
                }
            }
        }
        lifecycleScope.launch {
            viewModel.stateRequest.collectLatest {
                if (it is Resource.Success){
                    showSnackBar(getString(R.string.success_update))}
                if (it is Resource.Error){
                    showSnackBar(it.message)
                }
            }
        }
    }

    private fun showSnackBar(text: String) {
        Snackbar.make(binding.navHostFragment, text, Snackbar.LENGTH_SHORT).show()
    }
}