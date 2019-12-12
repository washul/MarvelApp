package wsl.com.accedotest.Utils.Retrofit

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url
import wsl.com.accedotest.Models.CharacterModel
import wsl.com.accedotest.Models.ComicModel
import wsl.com.accedotest.Utils.PARAM_CHARACTERS


interface ServicesCharacters{

    @GET(PARAM_CHARACTERS)
    fun getCharacters( @Query("limit") limit: String = "10", @Query("offset") offset: String = "0" ): Observable<List<CharacterModel>>

    @GET
    fun getComics( @Url url: String ): Observable<List<ComicModel>>

}

