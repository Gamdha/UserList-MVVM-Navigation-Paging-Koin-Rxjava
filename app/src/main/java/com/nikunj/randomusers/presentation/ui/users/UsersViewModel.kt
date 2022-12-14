package com.nikunj.randomusers.presentation.ui.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.nikunj.randomusers.domain.models.User
import com.nikunj.randomusers.domain.usecase.GetUserListUseCase
import timber.log.Timber

class UsersViewModel(private val getUserListUseCase: GetUserListUseCase): ViewModel() {

    private val _users: MutableLiveData<PagedList<User>> = MutableLiveData()
    val users: LiveData<PagedList<User>>
        get() = _users

    fun getUsers() {
        Timber.i("Executing GetUserListUseCase...")
        getUserListUseCase.execute(
            { users -> _users.value = users },
            { throwable ->  Timber.e(throwable) },
            Unit)
    }

    override fun onCleared() {
        super.onCleared()
        getUserListUseCase.dispose()
        Timber.i("Use cases disposed")
    }
}