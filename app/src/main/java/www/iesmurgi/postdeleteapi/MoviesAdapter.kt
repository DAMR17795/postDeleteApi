package www.iesmurgi.postdeleteapi


import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MoviesAdapter (private var datos: List<Movies>, private val listener: MainActivityListener): RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {


    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.bind(datos[position])

        holder.itemView.findViewById<Button>(R.id.btEliminar).setOnClickListener {
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

        holder.itemView.findViewById<Button>(R.id.btModificar).setOnClickListener {
            var nombre = holder.itemView.findViewById<TextView>(R.id.tvNombre).text.toString()
            var id = holder.itemView.findViewById<TextView>(R.id.tvID).text.toString()
            var fechaLanzamiento = holder.itemView.findViewById<TextView>(R.id.tvFecha).text.toString()

            val intent = Intent(holder.itemView.context, Insercion::class.java)
            intent.putExtra("ID", id)
            intent.putExtra("FECHA", fechaLanzamiento)
            intent.putExtra("NOMBRE", nombre)
            ContextCompat.startActivity(holder.itemView.context, intent, null)

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