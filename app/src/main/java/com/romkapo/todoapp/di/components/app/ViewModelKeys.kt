package com.romkapo.todoapp.di.components.app

import androidx.lifecycle.ViewModel
import dagger.MapKey
import kotlin.reflect.KClass

/* Мапа ВьюМоделей для мультибайдинга*/

@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKeys(val value: KClass<out ViewModel>)
