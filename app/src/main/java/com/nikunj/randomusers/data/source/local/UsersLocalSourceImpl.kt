package com.nikunj.randomusers.data.source.local

import androidx.paging.DataSource
import com.nikunj.randomusers.data.mapper.mapLocalUserToDomain
import com.nikunj.randomusers.data.source.local.room.UsersDatabase
import com.nikunj.randomusers.data.source.local.room.entity.UserEntity
import com.nikunj.randomusers.domain.models.User
import io.reactivex.Completable
import io.reactivex.Single

class UsersLocalSourceImpl(private val usersDatabase: UsersDatabase): UsersLocalSource {

    override fun getUsersCountFromDatabase(): Single<Int> = usersDatabase.usersDao().getUsersCount()

    override fun getUsersFromDatabase(): DataSource.Factory<Int, User> = usersDatabase.usersDao().getUsers()
        .mapByPage { users -> users.map(mapLocalUserToDomain) }

    override fun getFavoriteUsersFromDatabase(): Single<List<User>> = usersDatabase.usersDao().getFavoriteUsers()
        .map { users -> users.map(mapLocalUserToDomain) }

    override fun saveUsersInDatabase(users: List<UserEntity>): Completable = usersDatabase.usersDao().insertUsers(users)

    override fun updateUser(user: UserEntity): Completable = usersDatabase.usersDao().update(user)

    override fun deleteLocalUsers(): Completable = usersDatabase.usersDao().deleteAllUsers()
}