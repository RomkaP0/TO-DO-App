package com.romkapo.todoapp.di.components.list

import androidx.lifecycle.ViewModel
import com.romkapo.todoapp.di.components.app.ViewModelKeys
import com.romkapo.todoapp.presentation.screen.todolistitems.TodoItemListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface TodoListItemModule {
    @Binds
    @IntoMap
    @ViewModelKeys(TodoItemListViewModel::class)
    fun provideTodoListViewModel(todoItemListViewModel: TodoItemListViewModel): ViewModel
}
