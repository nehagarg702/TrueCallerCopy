package com.example.truecallercopy.dependencyInjection

import com.example.truecallercopy.data.global.CallerNetworkService
import com.example.truecallercopy.data.global.MockCallerApiService
import com.example.truecallercopy.data.model.CallerInfo
import com.google.gson.Gson
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


val networkModule = module {
    single { provideOkHttpClient() }
    single { provideRetrofit(get()) }
    single<CallerNetworkService> { MockCallerApiService() }  // Use MockCallerApiService instead of just creating it directly
}

fun provideOkHttpClient(): OkHttpClient {
    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val mockResponseInterceptor = Interceptor { chain ->
        val phoneNumber = chain.request().url.queryParameter("phoneNumber") ?: ""
        val mockResponse = getMockResponse(phoneNumber) // Get mock response based on phone number

        val response = Response.Builder()
            .request(chain.request())
            .protocol(Protocol.HTTP_1_1)
            .code(200) // Simulate a successful API response
            .message("OK")
            .body(ResponseBody.create(null, mockResponse))
            .build()
        response
    }

    return OkHttpClient.Builder()
        .addInterceptor(logging)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()
}

fun provideRetrofit(client: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://mock.api/")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}

fun provideApiService(retrofit: Retrofit): CallerNetworkService {
    return retrofit.create(CallerNetworkService::class.java)
}

fun getMockResponse(phoneNumber : String): String {
    val caller = CallerInfo(
        name = "John Doe",
        phoneNumber = phoneNumber,
        location = "New York, USA",
        isSpam = true
    )
    return Gson().toJson(caller)
}
