package ru.dmitriyt.networkscanner.di.module

import dagger.Module
import ru.dmitriyt.networkscanner.di.module.viewmodel.ViewModelFactoryModule

@Module(includes = [ViewModelFactoryModule::class])
abstract class ViewModelModule {
}