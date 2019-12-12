package wsl.com.accedotest.Models

import com.google.gson.JsonObject

data class CharacterModel(

    val id: Int?,
    val name: String?,
    val description: String?,
    val thumbnail: Thumbnail?,
    var list: List<ComicModel>?

)

data class ComicModel(

    val id: String?,
    val thumbnail : Thumbnail?,
    val description: String?

)

data class Thumbnail(

    val path: String?,
    val extension: String?

)

data class ApiResponseModel(

    val code: String?,
    val status: String,
    val etag: String?,
    val data: JsonObject

)


