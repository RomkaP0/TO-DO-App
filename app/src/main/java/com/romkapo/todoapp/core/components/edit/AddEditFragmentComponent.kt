package com.romkapo.todoapp.core.components.edit

import com.romkapo.todoapp.presentation.screen.addedititem.AddEditItemFragment
import dagger.Subcomponent

@Subcomponent(modules = [AddEditFragmentModule::class])
@AddEditFragmentScope
interface AddEditFragmentComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): AddEditFragmentComponent
    }
    fun inject(addEditItemFragment: AddEditItemFragment)
}
