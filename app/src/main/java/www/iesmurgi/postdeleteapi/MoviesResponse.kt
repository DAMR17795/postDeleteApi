package www.iesmurgi.postdeleteapi

import com.google.gson.annotations.SerializedName


data class MoviesResponse(@SerializedName ("pelicula") val movies: List<Movies>)

data class Movies(
    val id: String,
    val nombre: String,
    val fechaLanzamiento: String,
)
