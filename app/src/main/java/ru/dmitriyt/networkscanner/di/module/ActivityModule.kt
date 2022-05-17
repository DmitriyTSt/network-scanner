package ru.dmitriyt.networkscanner.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import ru.dmitriyt.networkscanner.presentation.ui.mainscreen.MainActivity

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun mainActivity(): MainActivity
}