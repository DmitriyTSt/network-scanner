package ru.dmitriyt.networkscanner.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.dmitriyt.networkscanner.data.DiffUtilCallbackFactory
import ru.dmitriyt.networkscanner.data.DiffUtilItemCallbackFactory

@Module
@InstallIn(SingletonComponent::class)
class DiffUtilModule {

    @Provides
    fun provideDiffUtilItemCallbackFactory(): DiffUtilItemCallbackFactory {
        return DiffUtilItemCallbackFactory()
    }

    @Provides
    fun provideDiffUtilCallbackFactory(
        diffUtilItemCallbackFactory: DiffUtilItemCallbackFactory,
    ): DiffUtilCallbackFactory {
        return DiffUtilCallbackFactory(diffUtilItemCallbackFactory)
    }
}