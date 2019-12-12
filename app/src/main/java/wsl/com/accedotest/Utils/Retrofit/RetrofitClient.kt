package wsl.com.accedotest.Utils.Retrofit

import com.google.gson.Gson
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import wsl.com.accedotest.Models.ApiResponseModel
import wsl.com.accedotest.Utils.API_KEY
import wsl.com.accedotest.Utils.HASH
import wsl.com.accedotest.Utils.TIME_STAMP
import wsl.com.accedotest.Utils.URL_BASE

object RetrofitClient {

    private var retrofit: Retrofit? = null

    val instance: Retrofit
        get() {
            
            if ( retrofit == null ) {

                val client = OkHttpClient
                    .Builder()
                    .addInterceptor( RequestInterceptorAddHeaders() )
                    .addInterceptor( RequestInterceptorSetJsonModel() )
                    .build()

                retrofit = Retrofit.Builder()
                    .baseUrl("http://$URL_BASE")
                    .client( client )
                    .addConverterFactory( GsonConverterFactory.create() )
                    .addCallAdapterFactory( RxJava2CallAdapterFactory.create() )
                    .build()

            }

            return retrofit!!

        }

}

private class RequestInterceptorSetJsonModel : Interceptor {

    val JSON = MediaType.parse("application/json; charset=utf-8")
    val GSON = Gson()

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val response = chain.proceed(request)
        val body = response.body()

        val apiResponse: ApiResponseModel = GSON.fromJson(body!!.string(), ApiResponseModel::class.java)
        body.close()

        val newResponse = response.newBuilder()
            .body(ResponseBody.create(JSON, ( apiResponse.data.getAsJsonArray("results") ).toString()))

        return newResponse.build()
    }

}

private class RequestInterceptorAddHeaders : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalHttpUrl = original.url()

        val url = originalHttpUrl.newBuilder()
            .addQueryParameter("apikey", API_KEY)
            .addQueryParameter("hash", HASH)
            .addQueryParameter("ts", TIME_STAMP)
            .build()

        val requestBuilder = original.newBuilder()
            .url(url)

        val request = requestBuilder.build()

        return chain.proceed(request)
    }

}