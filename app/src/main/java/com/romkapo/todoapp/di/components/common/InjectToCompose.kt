package com.romkapo.todoapp.di.components.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.compositionLocalOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun Inject(
    composeViewModelFactory: ComposeViewModelFactory,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalViewModelFactory provides composeViewModelFactory,
        content = content,
    )
}

@Composable
inline fun <reified VM : ViewModel> daggerViewModel(): VM {
    val factory = getViewModelFactory()
    return viewModel {
        val savedStateHandle = createSavedStateHandle()
        factory.create(VM::class.java, savedStateHandle)
    }
}

@Composable
@PublishedApi
internal fun getViewModelFactory(): ComposeViewModelFactory {
    return checkNotNull(LocalViewModelFactory.current) {
        "No ComposeViewModelFactory was provided via LocalComposeViewModelFactory"
    }
}

object LocalViewModelFactory {
    private val LocalComposeViewModelFactory =
        compositionLocalOf<ComposeViewModelFactory?> { null }

    val current: ComposeViewModelFactory?
        @Composable
        get() = LocalComposeViewModelFactory.current

    infix fun provides(composeViewModelFactory: ComposeViewModelFactory): ProvidedValue<ComposeViewModelFactory?> {
        return LocalComposeViewModelFactory.provides(composeViewModelFactory)
    }
}
