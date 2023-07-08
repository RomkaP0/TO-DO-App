package com.romkapo.todoapp.core.components.list

import androidx.lifecycle.ViewModel
import com.romkapo.todoapp.core.components.app.ViewModelKeys
import com.romkapo.todoapp.presentation.screen.todolistitems.TodoItemListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface TodoListItemModule{
    @Binds
    @IntoMap
    @ViewModelKeys(TodoItemListViewModel::class)
    fun provideTodoListViewModel(todoItemListViewModel: TodoItemListViewModel): ViewModel
}
