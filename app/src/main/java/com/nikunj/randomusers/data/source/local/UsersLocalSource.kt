package com.nikunj.randomusers.data.source.local

import androidx.paging.DataSource
import com.nikunj.randomusers.data.source.local.room.entity.UserEntity
import com.nikunj.randomusers.domain.models.User
import io.reactivex.Completable
import io.reactivex.Single

interface UsersLocalSource {
    fun getUsersCountFromDatabase(): Single<Int>
    fun getUsersFromDatabase(): DataSource.Factory<Int, User>
    fun getFavoriteUsersFromDatabase(): Single<List<User>>
    fun saveUsersInDatabase(users: List<UserEntity>): Completable
    fun updateUser(user: UserEntity): Completable
    fun deleteLocalUsers(): Completable
}