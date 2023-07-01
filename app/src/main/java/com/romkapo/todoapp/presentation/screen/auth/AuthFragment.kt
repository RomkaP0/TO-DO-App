package com.romkapo.todoapp.presentation.screen.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.romkapo.todoapp.R
import com.romkapo.todoapp.databinding.FragmentAuthBinding
import com.yandex.authsdk.YandexAuthException
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthFragment : Fragment() {

    private lateinit var launcher: ActivityResultLauncher<YandexAuthSdk>
    private lateinit var sdk: YandexAuthSdk

    private val viewModel: AuthViewModel by viewModels()

    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

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