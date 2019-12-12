package wsl.com.accedotest.Models.Repositorys

import android.util.Log
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import wsl.com.accedotest.Models.CharacterModel
import wsl.com.accedotest.Models.ComicModel
import wsl.com.accedotest.Utils.PARAM_CHARACTERS
import wsl.com.accedotest.Utils.PARAM_COMICS
import wsl.com.accedotest.Utils.Retrofit.RetrofitClient
import wsl.com.accedotest.Utils.Retrofit.ServicesCharacters
import wsl.com.accedotest.Utils.URL_BASE
import java.lang.Exception

class CharacterRepository {

    private val retrofit = RetrofitClient.instance
    private val service = retrofit.create( ServicesCharacters::class.java )
    private val compositeDisposable = CompositeDisposable()

    fun getCharacters( oldNumber: Int, requiredNumber: Int, function: ( List<CharacterModel> ) -> Unit ){

        try {

            compositeDisposable.add( service.getCharacters( offset = oldNumber.toString(), limit = requiredNumber.toString() )
                .subscribeOn( Schedulers.io() )
                .unsubscribeOn( Schedulers.computation() )
                .observeOn( AndroidSchedulers.mainThread() )
                .subscribe({ list ->

                    Log.e("WSL", list.toString() )
                    function( list )

                },
                    {
                        it.printStackTrace()
                        function(emptyList())
                    }
                )

            )

        }catch ( e: Exception){
            e.printStackTrace()
            function(emptyList())
        }

    }

    fun getCharacterWith( characterId: String, function: (List<ComicModel>?) -> Unit){

        try {

            compositeDisposable.add( service.getComics( "$PARAM_CHARACTERS/$characterId/$PARAM_COMICS" )
                .subscribeOn( Schedulers.io() )
                .unsubscribeOn( Schedulers.computation() )
                .observeOn( AndroidSchedulers.mainThread() )
                .subscribe({ list ->

                    Log.e("WSL", list.toString() )
                    function( list )

                },
                    {
                        it.printStackTrace()
                        function(null)
                    }
                )

            )

        }catch ( e: Exception){
            e.printStackTrace()
            function(null)
        }

    }

}