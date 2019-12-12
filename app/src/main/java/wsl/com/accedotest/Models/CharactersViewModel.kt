package wsl.com.accedotest.Models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import wsl.com.accedotest.Models.Repositorys.CharacterRepository
import java.lang.ClassCastException

class CharactersViewModel( application: Application ): AndroidViewModel( application ) {

    private val repository = CharacterRepository()
    var charactersList: List<CharacterModel> = emptyList()

    var characterSelected: CharacterModel? = null

    var limit = 10

    fun getMoreCharacters(function:(List<CharacterModel>?) -> Unit ) {

        repository.getCharacters( oldNumber = this.charactersList.size, requiredNumber = this.limit ){

            try {
                ( this.charactersList as ArrayList ).addAll( it )
            }catch ( e: ClassCastException ){
                this.charactersList = it
            }

            function( this.charactersList )

        }

    }

    fun getCharacterWith( characterId: String, function: (List<ComicModel>?) -> Unit )
            = repository.getCharacterWith( characterId, function )

}