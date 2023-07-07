package com.romkapo.todoapp.presentation.screen.auth

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.romkapo.todoapp.R
import com.romkapo.todoapp.appComponent
import com.romkapo.todoapp.core.components.auth.AuthFragmentComponent
import com.romkapo.todoapp.databinding.FragmentAuthBinding
import com.yandex.authsdk.YandexAuthException
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk
import javax.inject.Inject

class AuthFragment : Fragment() {

    private lateinit var launcher: ActivityResultLauncher<YandexAuthSdk>
    private lateinit var sdk: YandexAuthSdk

    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: AuthViewModelFactory
    private lateinit var authFragmentComponent: AuthFragmentComponent
    private lateinit var viewModel: AuthViewModel

    override fun onAttach(context: Context) {
        authFragmentComponent = (requireContext().applicationContext as Application).appComponent.authFragmentComponentFactory().create()
        authFragmentComponent.inject(this)
        super.onAttach(context)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this, viewModelFactory)[AuthViewModel::class.java]


        sdk = YandexAuthSdk(requireContext(), YandexAuthOptions(requireContext()))

        launcher = registerForActivityResult(YandexSignInActivityResultContract()) { pair ->
            val token = sdk.extractToken(pair.first, pair.second)
            try {
                if (token != null) {
                    viewModel.putToken(token.value)
                    findNavController().navigate(R.id.action_authFragment_to_todoListFragment)
                } else {
                    cantAuthToast()
                }
            } catch (e: YandexAuthException) {
                cantAuthToast()
            }
        }

        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.token?.let {
            if (it != "") {
                findNavController().navigate(R.id.action_authFragment_to_todoListFragment)
            }
        }

        binding.authYandexButton.setOnClickListener {
            launcher.launch(sdk)
        }

        binding.authOfflineButton.setOnClickListener {
            findNavController().navigate(R.id.action_authFragment_to_todoListFragment)
        }
    }

    private fun cantAuthToast() {
        Toast.makeText(
            requireContext(),
            getText(R.string.failed_to_login),
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}