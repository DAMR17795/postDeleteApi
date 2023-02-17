package www.iesmurgi.postdeleteapi


import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MoviesAdapter (private var datos: List<Movies>, private val listener: MainActivityListener): RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {


    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.bind(datos[position])

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val builder = AlertDialog.Builder(context)

            builder.setTitle("Borrado de Pelicula")
                .setMessage("Va a borrar la pelicula seleccionada cuyo ID es " + (position+1) + ". ¿Desea Continuar?")

            builder.setPositiveButton("OK") {_,_ ->
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://apiejemplo-2a720-default-rtdb.europe-west1.firebasedatabase.app/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val myApi = retrofit.create(APIService::class.java)

                myApi.deleteMovie("peliculas/pelicula/"+position+".json").enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        notifyDataSetChanged()
                        listener.onSomeEvent(position)
                        Toast.makeText(context, "Pelicula Borrada", Toast.LENGTH_SHORT).show()

                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        //no.visibility = View.VISIBLE
                        //no.text="No hay peliculas"
                        Log.e("MainActivity", "Error al cargar los datos de la API", t)
                    }
                })
            }

            builder.setNegativeButton("Cancelar") {dialog, _ ->
                dialog.cancel()
            }

            builder.show()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_movie, parent, false))
    }

    override fun getItemCount(): Int {
        return datos.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(dato: Movies) {
            itemView.findViewById<TextView>(R.id.tvNombre).text = dato.nombre
            itemView.findViewById<TextView>(R.id.tvFecha).text = dato.fechaLanzamiento
            itemView.findViewById<TextView>(R.id.tvID).text = dato.id
        }

    }

    fun setList(lista2:MutableList<Movies>){
        this.datos = lista2
        notifyDataSetChanged()
    }

}