//package com.romkapo.todoapp.presentation.screen.auth
//
//import android.app.Application
//import android.content.Context
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Toast
//import androidx.activity.result.ActivityResultLauncher
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Divider
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedButton
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.drawBehind
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.em
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.viewModels
//import androidx.lifecycle.ViewModelProvider
//import androidx.navigation.fragment.findNavController
//import com.romkapo.todoapp.R
//import com.romkapo.todoapp.appComponent
//import com.romkapo.todoapp.databinding.FragmentAuthBinding
//import com.romkapo.todoapp.di.components.auth.AuthFragmentComponent
//import com.romkapo.todoapp.ui.theme.Black
//import com.romkapo.todoapp.ui.theme.LightGrey
//import com.romkapo.todoapp.ui.theme.Typography
//import com.yandex.authsdk.YandexAuthException
//import com.yandex.authsdk.YandexAuthOptions
//import com.yandex.authsdk.YandexAuthSdk
//import javax.inject.Inject
//
//class AuthFragment : Fragment() {
//
//    private lateinit var launcher: ActivityResultLauncher<YandexAuthSdk>
//    private lateinit var sdk: YandexAuthSdk
//
//    private var _binding: FragmentAuthBinding? = null
//    private val binding get() = _binding!!
//
//    @Inject
//    lateinit var viewModelFactory: ViewModelProvider.Factory
//    private lateinit var authFragmentComponent: AuthFragmentComponent
//    private val viewModel: AuthViewModel by viewModels { viewModelFactory }
//
//    override fun onAttach(context: Context) {
//        authFragmentComponent =
//            (requireContext().applicationContext as Application).appComponent.authFragmentComponentFactory()
//                .create()
//        authFragmentComponent.inject(this)
//        super.onAttach(context)
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?,
//    ): View {
//        sdk = YandexAuthSdk(requireContext(), YandexAuthOptions(requireContext()))
//
//        launcher = registerForActivityResult(YandexSignInActivityResultContract()) { pair ->
//            val token = sdk.extractToken(pair.first, pair.second)
//            try {
//                if (token != null) {
//                    viewModel.putToken(token.value)
//                    findNavController().navigate(R.id.action_authFragment_to_todoListFragment)
//                } else {
//                    cantAuthToast()
//                }
//            } catch (e: YandexAuthException) {
//                cantAuthToast()
//            }
//        }
//
//        _binding = FragmentAuthBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        viewModel.token?.let {
//            if (it != "") {
//                findNavController().navigate(R.id.action_authFragment_to_todoListFragment)
//            }
//        }
//
//        binding.authYandexButton.setOnClickListener {
//            launcher.launch(sdk)
//        }
//
//        binding.authOfflineButton.setOnClickListener {
//            findNavController().navigate(R.id.action_authFragment_to_todoListFragment)
//        }
//    }
//
//    private fun cantAuthToast() {
//        Toast.makeText(
//            requireContext(),
//            getText(R.string.failed_to_login),
//            Toast.LENGTH_LONG,
//        ).show()
//    }
//
//    override fun onDestroyView() {
//        _binding = null
//        super.onDestroyView()
//    }
//}
//
//@Preview(showSystemUi = true, showBackground = true)
//@Composable
//fun AuthScreen() {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        Text(text = stringResource(id = R.string.auth), style = Typography.titleLarge)
//        Column(
//            modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            OutlinedButton(
//                modifier = Modifier.size(298.dp, 44.dp),
//                onClick = { /*TODO*/ }) {
//                Text(text = stringResource(id = R.string.auth_guest))
//            }
//            Box(
//                contentAlignment = Alignment.Center, modifier = Modifier
//                    .width(270.dp)
//                    .padding(vertical = 16.dp)
//            ) {
//                Divider()
//                Text(
//                    modifier = Modifier
//                        .padding(horizontal = 16.dp).fillMaxWidth(0.2f)
//                        .drawBehind { drawRect(color = LightGrey) },
//                    textAlign = TextAlign.Center,
//                    text = stringResource(id = R.string.or)
//                )
//            }
//            Button(
//                colors = ButtonDefaults.buttonColors(containerColor = Black),
//                modifier = Modifier.size(298.dp, 44.dp),
//                onClick = { /*TODO*/ }) {
//
//                Image(
//                    painter = painterResource(id = R.drawable.yandex_logo),
//                    contentDescription = "icon",
//                    modifier = Modifier
//                        .size(40.dp)
//                )
//                Text(
//                    text = "Войти с Яндекс ID",
//                    color = Color.White,
//                    textAlign = TextAlign.Center,
//                    lineHeight = 1.43.em,
//                    style = MaterialTheme.typography.labelLarge,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 8.dp)
//                )
//
//            }
//        }
//    }
//}
