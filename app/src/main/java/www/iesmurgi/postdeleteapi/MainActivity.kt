package www.iesmurgi.postdeleteapi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), MainActivityListener{

    private lateinit var miAdapter: MoviesAdapter
    private var allMovies = mutableListOf<Movies>()
    private lateinit var rvMain: RecyclerView
    private lateinit var btn: Button
    private lateinit var no:TextView

    private lateinit var layoutManager: LinearLayoutManager

    override fun onSomeEvent(position: Int) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://apiejemplo-2a720-default-rtdb.europe-west1.firebasedatabase.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val myApi = retrofit.create(APIService::class.java)
        myApi.deleteMovie("peliculas/pelicula/"+position+".json").enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                recreate()
                // Aquí actualizas la interfaz de usuario con los datos cargados de la API.
                // Por ejemplo:
                // myTextView.text = myData.someValue
                miAdapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                //no.visibility = View.VISIBLE
                //no.text="No hay peliculas"
                Log.e("MainActivity", "Error al cargar los datos de la API", t)
            }
        })
    }
    override fun onSomeEvent2(position: Int, peliculaId:String, pelicula:Movies) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://apiejemplo-2a720-default-rtdb.europe-west1.firebasedatabase.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val myApi = retrofit.create(APIService::class.java)
        val peliculaId = peliculaId
        val pelicula = Movies(peliculaId, "Nuevo nombre de película", "30/12/2022")

        myApi.updatePelicula("peliculas/pelicula/"+position+".json", pelicula).enqueue(object : Callback<Movies> {
            override fun onResponse(call: Call<Movies>, response: Response<Movies>) {
                recreate()
            }

            override fun onFailure(call: Call<Movies>, t: Throwable) {
                // Manejo de errores en la conexión o petición
            }
        })

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        no = findViewById<TextView>(R.id.tvNo)
        no.text = "Cargando..."

        //RecyclerView
        rvMain = findViewById(R.id.rvMain)
        //Linear Layout
        layoutManager = LinearLayoutManager(applicationContext)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://apiejemplo-2a720-default-rtdb.europe-west1.firebasedatabase.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val myApi = retrofit.create(APIService::class.java)

        miAdapter = MoviesAdapter(allMovies, this)

        myApi.getMovies("peliculas.json/").enqueue(object : Callback<MoviesResponse> {
            override fun onResponse(call: Call<MoviesResponse>, response: Response<MoviesResponse>) {
                val movies = response.body()?.movies
                allMovies.addAll(movies!!)

                if (response.isSuccessful) {
                    no.visibility = View.INVISIBLE

                    miAdapter.setList(allMovies)

                    rvMain.layoutManager = layoutManager
                    rvMain.adapter = miAdapter

                }
                // Aquí actualizas la interfaz de usuario con los datos cargados de la API.
                // Por ejemplo:
                // myTextView.text = myData.someValue
            }

            override fun onFailure(call: Call<MoviesResponse>, t: Throwable) {
                    no.visibility = View.VISIBLE
                    no.text="No hay peliculas"
                Log.e("MainActivity", "Error al cargar los datos de la API", t)
            }
        })

        btn = findViewById(R.id.button)
        btn.setOnClickListener {
            val enviar= Intent(this, Insercion::class.java)
            startActivity(enviar)
        }
    }

    fun metodoPut() {
        val peliculaId = "12"
        val pelicula = Movies(peliculaId, "Nuevo nombre de película", "30/12/2022")

        /*myApi.updatePelicula("peliculas/pelicula/11.json", pelicula).enqueue(object : Callback<Movies> {
            override fun onResponse(call: Call<Movies>, response: Response<Movies>) {
                recreate()
            }

            override fun onFailure(call: Call<Movies>, t: Throwable) {
                // Manejo de errores en la conexión o petición
            }
        })*/

    }
}