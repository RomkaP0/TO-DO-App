package com.romkapo.todoapp.di.components.list

import com.romkapo.todoapp.di.components.app.ViewModelKeys
import com.romkapo.todoapp.di.components.common.ViewModelAssistedFactory
import com.romkapo.todoapp.ui.screen.todolistitems.TodoItemListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface TodoListItemModule {
    @Binds
    @IntoMap
    @ViewModelKeys(TodoItemListViewModel::class)
    fun provideTodoListViewModelFactory(factory: TodoItemListViewModel.Factory): ViewModelAssistedFactory<*>
}
