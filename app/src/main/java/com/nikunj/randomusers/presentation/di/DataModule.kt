package com.nikunj.randomusers.presentation.di

import android.content.Context
import androidx.paging.RxPagedListBuilder
import androidx.room.Room
import com.nikunj.randomusers.BuildConfig
import com.nikunj.randomusers.data.constants.RepositoryConstants
import com.nikunj.randomusers.data.repository.UsersRepositoryImpl
import com.nikunj.randomusers.data.source.UsersBoundaryCallback
import com.nikunj.randomusers.data.source.local.UsersLocalSource
import com.nikunj.randomusers.data.source.local.UsersLocalSourceImpl
import com.nikunj.randomusers.data.source.local.room.UsersDatabase
import com.nikunj.randomusers.data.source.remote.UsersRemoteSource
import com.nikunj.randomusers.data.source.remote.UsersRemoteSourceImpl
import com.nikunj.randomusers.data.source.remote.retrofit.UsersService
import com.nikunj.randomusers.domain.models.User
import com.nikunj.randomusers.domain.repository.UsersRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL: String = ""
const val DATABASE_NAME = "users_database"

val dataModule = module {
    single { provideOkHttpClient() }
    single { provideRetrofit(get()) }
    single { provideUsersService(get()) }
    single { provideUsersDatabase(androidContext()) }
    single { UsersLocalSourceImpl(get()) as UsersLocalSource }
    single { UsersRemoteSourceImpl(get()) as UsersRemoteSource }
    single { UsersBoundaryCallback(get(), get()) }
    single { provideRxPagedListBuilder(get(), get()) }
    single { UsersRepositoryImpl(get(), get()) as UsersRepository }
}

fun provideOkHttpClient(): OkHttpClient = OkHttpClient().newBuilder()
    .addInterceptor(HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
        else HttpLoggingInterceptor.Level.NONE
    })
    .build()

fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    .client(client)
    .build()

fun provideUsersService(retrofit: Retrofit): UsersService = retrofit.create(UsersService::class.java)

fun provideUsersDatabase(context: Context) = Room.databaseBuilder(
    context,
    UsersDatabase::class.java,
    DATABASE_NAME
).fallbackToDestructiveMigration().build()

fun provideRxPagedListBuilder(
    usersLocalSource: UsersLocalSource,
    usersBoundaryCallback: UsersBoundaryCallback
): RxPagedListBuilder<Int, User> =
    RxPagedListBuilder(
        usersLocalSource.getUsersFromDatabase(),
        RepositoryConstants.DEFAULT_PAGE_SIZE
    ).setBoundaryCallback(usersBoundaryCallback)