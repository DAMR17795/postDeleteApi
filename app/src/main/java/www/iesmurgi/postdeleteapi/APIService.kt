package www.iesmurgi.postdeleteapi

import android.content.ClipData
import retrofit2.Call
import retrofit2.http.*

interface APIService {
    @GET
    fun getMovies(@Url url:String): Call<MoviesResponse>

    @DELETE
    fun deleteMovie(@Url url:String): Call<Void>

    @PUT
    fun updatePelicula(@Url url:String, @Body pelicula: Movies): Call<Movies>

}