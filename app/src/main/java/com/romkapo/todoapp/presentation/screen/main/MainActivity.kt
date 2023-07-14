package com.romkapo.todoapp.presentation.screen.main

import android.app.Application
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.romkapo.todoapp.R
import com.romkapo.todoapp.appComponent
import com.romkapo.todoapp.data.model.BadRequestException
import com.romkapo.todoapp.data.model.ClientSideException
import com.romkapo.todoapp.data.model.ItemNotFoundException
import com.romkapo.todoapp.data.model.NetworkException
import com.romkapo.todoapp.data.model.Resource
import com.romkapo.todoapp.data.model.Resource.Error
import com.romkapo.todoapp.data.model.SyncFailedException
import com.romkapo.todoapp.data.model.UpdateFailedException
import com.romkapo.todoapp.di.components.main.MainActivityComponent
import com.romkapo.todoapp.ui.theme.TodoAppTheme
import com.romkapo.todoapp.utils.navigation.AppNavHost

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
//    private lateinit var binding: ActivityMainBinding

//    @Inject
//    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var mainComponent: MainActivityComponent
//    private val viewModel: MainViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        injections()

        super.onCreate(savedInstanceState)
        setContent {
            TodoAppTheme {
                val navState = rememberNavController()
                AppNavHost(navController = navState)
            }
        }

//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        initInternetMonitoring()
//        viewModel.listenStateRequest()
//
//        val navHostFragment =
//            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
//        navController = navHostFragment.navController
    }

//    private fun initInternetMonitoring() {
//        lifecycleScope.launch {
//            viewModel.state.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).collect {
//                if (it is Success) {
//                    showToast(getString(R.string.available_network_state))
//                    viewModel.updateRepository()
//                } else {
//                    showToast(getString(R.string.loading_failed_showing_local_data))
//                }
//            }
//        }
//        lifecycleScope.launch {
//            viewModel.stateRequest.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
//                .collectLatest {
//                    showToast(classifyResult(it))
//                }
//        }
//    }

    private fun classifyResult(result: Resource): String {
        return if (result is Error) {
            when (result.exception) {
                SyncFailedException -> getString(R.string.sync_exc)
                ItemNotFoundException -> getString(R.string.not_found_exc)
                BadRequestException -> getString(R.string.bad_exc)
                ClientSideException -> getString(R.string.client_exc)
                NetworkException -> getString(R.string.net_exc)
                UpdateFailedException -> getString(R.string.update_exc)
            }
        } else {
            getString(R.string.success_update)
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun injections() {
        mainComponent = (applicationContext as Application)
            .appComponent
            .mainActivityComponentFactory()
            .create()

        mainComponent.inject(this)
    }
}
