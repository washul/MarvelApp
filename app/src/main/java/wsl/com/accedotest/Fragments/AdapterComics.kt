package wsl.com.accedotest.Fragments

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import wsl.com.accedotest.Models.ComicModel
import wsl.com.accedotest.R

class AdapterComics(private val context: Context) :
    RecyclerView.Adapter<AdapterComics.MyViewHolder>() {

    private var list: List<ComicModel> = emptyList()
    private var piccaso = Picasso.with( context )

    inner class MyViewHolder(private val view: View) : RecyclerView.ViewHolder(view){

        private val image: ImageView = view.findViewById( R.id._image_comic )

        fun bind(item: ComicModel){

            val image_size = context.resources.getString( R.string.image_size )

            piccaso
                .load("${item.thumbnail?.path}/portrait_uncanny.${item.thumbnail?.extension}")
                .into( image )

            setIsRecyclable( false )

        }

    }

    fun updateList( itemList: List<ComicModel> ){

        this.list = itemList
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from( context )
        return MyViewHolder( layoutInflater.inflate( R.layout.cardview_comic, parent, false ) )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) = holder.bind(list[position])


}
