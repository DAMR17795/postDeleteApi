package www.iesmurgi.postdeleteapi

interface MainActivityListener {
    fun onSomeEvent(position:Int)
    fun onSomeEvent2(position: Int, peliculaId:String, pelicula:Movies)
}