package com.jeyendroid.sciflare_assessment_app

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@InstallIn(ViewModelComponent::class)
@Module
class UserDetailModule {

    @ViewModelScoped
    @Provides
    fun providesUserDetailUseCase(repository: UserDetailRepository) : UserDetailUseCase = UserDetailUseCase(repository)

    @ViewModelScoped
    @Provides
    fun providesUserDetailRepository() : UserDetailRepository {
        val db = UserDetailDatabase.getDatabase(MyApplication.instance)
        return  UserDetailRepositoryImpl(db.userDetail())
    }

    @ViewModelScoped
    @Provides
    fun providesUserViewModel(useCase: UserDetailUseCase): UserViewModel = UserViewModel(useCase)

}