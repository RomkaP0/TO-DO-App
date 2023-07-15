package com.romkapo.todoapp.presentation.screen.main

import android.app.Application
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
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
import com.romkapo.todoapp.utils.ThemeProvider
import com.romkapo.todoapp.utils.ThemeMode
import com.romkapo.todoapp.utils.navigation.AppNavHost
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalPermissionsApi::class)
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
//    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var mainComponent: MainActivityComponent
    private val viewModel: MainViewModel by viewModels { viewModelFactory }


    override fun onCreate(savedInstanceState: Bundle?) {
        injections()
        super.onCreate(savedInstanceState)

        setContent {

            TodoAppTheme(
                darkTheme = when (ThemeProvider.theme.intValue) {
                    ThemeMode.LIGHT.ordinal -> false
                    ThemeMode.DARK.ordinal -> true
                    else -> isSystemInDarkTheme()
                }
            ) {
                val notifyingState = rememberMultiplePermissionsState(
                    listOf(
                        android.Manifest.permission.POST_NOTIFICATIONS,
                        android.Manifest.permission.SCHEDULE_EXACT_ALARM
                    )
                )
                val skipState = rememberSaveable { mutableStateOf(false) }

                if (notifyingState.allPermissionsGranted || VERSION.SDK_INT < VERSION_CODES.S || skipState.value) {
                    viewModel.putStatusNotification(!skipState.value)
                    val navState = rememberNavController()
                    Scaffold(modifier = Modifier.fillMaxSize()) {
                        AppNavHost(navController = navState, paddingValues = it)
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val textToShow = if (notifyingState.shouldShowRationale) {
                            // If the user has denied the permission but the rationale can be shown,
                            // then gently explain why the app requires this permission
                            "The camera is important for this app. Please grant the permission."
                        } else {
                            // If it's the first time the user lands on this feature, or the user
                            // doesn't want to be asked again for this permission, explain that the
                            // permission is required
                            "Camera permission required for this feature to be available. " +
                                    "Please grant the permission"
                        }
                        Text(textToShow)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(onClick = { notifyingState.launchMultiplePermissionRequest() }) {
                                Text("Request permission")
                            }
                            Button(onClick = { skipState.value = true }) {
                                Text("Skip permission")
                            }
                        }
                    }
                }
            }
        }
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
        initInternetMonitoring()
        viewModel.listenStateRequest()
//
//        val navHostFragment =
//            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
//        navController = navHostFragment.navController
    }


    private fun initInternetMonitoring() {
        lifecycleScope.launch {
            viewModel.state.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).collect {
                if (it is Resource.Success) {
                    showToast(getString(R.string.available_network_state))
                    viewModel.updateRepository()
                } else {
                    showToast(getString(R.string.loading_failed_showing_local_data))
                }
            }
        }
        lifecycleScope.launch {
            viewModel.stateRequest.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collectLatest {
                    showToast(classifyResult(it))
                }
        }
    }

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
