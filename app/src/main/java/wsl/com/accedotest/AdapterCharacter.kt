package wsl.com.accedotest

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import wsl.com.accedotest.Models.CharacterModel

interface IAdapterCharacter{

    fun itemSelected( item: CharacterModel )

}

class AdapterCharacter( private val context: Context, private val delegate: IAdapterCharacter ) :
    RecyclerView.Adapter<AdapterCharacter.MyViewHolder>() {

    private var list: ArrayList<CharacterModel> = ArrayList()
    private var piccaso = Picasso.with( context )

    inner class MyViewHolder(private val view: View) : RecyclerView.ViewHolder(view){

        private val image: ImageView = view.findViewById( R.id._image )
        private val name: TextView = view.findViewById( R.id._name )

        fun bind(item: CharacterModel){

            val image_size = context.resources.getString( R.string.image_size )

            piccaso
                .load("${item.thumbnail?.path}/$image_size.${item.thumbnail?.extension}")
                .into( image )

            name.text = if ( item.name != "") item.name else context.getString(R.string.no_description)

            view.setOnClickListener {

                delegate.itemSelected( item )

            }

            setIsRecyclable( false )

        }

    }

    fun updateList( itemList: List<CharacterModel> ){

        this.list = itemList as ArrayList<CharacterModel>
        notifyItemRangeInserted( this.list.size, itemList.size )

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from( context )
        return MyViewHolder( layoutInflater.inflate( R.layout.cardview_character, parent, false ) )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) = holder.bind(list[position])


}
