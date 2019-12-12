package wsl.com.accedotest.Fragments


import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.squareup.picasso.Picasso
import wsl.com.accedotest.MainActivity
import wsl.com.accedotest.Models.CharacterModel
import wsl.com.accedotest.R
import wsl.com.accedotest.Utils.getLayoutManagerToDetailsFragment

class CharacterDetailsFragment : DialogFragment() {

    private lateinit var uIView: View

    companion object{
        const val TAG = "DetailsFragment"
    }

    override fun onStart() {
        super.onStart()

        val dialog = dialog

        if ( dialog != null ) {

            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window.setLayout(width, height)

        }


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle( STYLE_NORMAL, R.style.FullScreenDialogStyle )
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        uIView = inflater.inflate(R.layout.fragment_character_details, container, false)

        InitUI().run()

        return uIView
    }

//    override fun onDestroy() {
//        super.onDestroy()
//
//        fragmentManager!!.beginTransaction().remove(this).commit()
//
//    }

    inner class InitUI: Thread(){

        private val picasso = Picasso.with( this@CharacterDetailsFragment.context )
        private val charactersViewModel = ( activity!! as MainActivity ).charactersViewModel
        private lateinit var adapterComics: AdapterComics

        private val image_size = context?.resources?.getString( R.string.image_size_details )
        private val _swipeToRefresh = uIView.findViewById<SwipeRefreshLayout>( R.id._swipeToRefresh_details )

        override fun run() {
            super.run()

            _swipeToRefresh.isRefreshing = true

            initToolbar()

            if ( charactersViewModel.characterSelected?.id != null ) {

                initRecycler()
                initImage()
                initDescription()

                if ( charactersViewModel.characterSelected?.list == null ) {
                    downloadComics()
                }else {
                    updateListOfComics()
                }

                _swipeToRefresh.setOnRefreshListener {

                    charactersViewModel.characterSelected?.list = null
                    this.run()

                }

            }

        }

        private fun updateListOfComics(){

            adapterComics.updateList( charactersViewModel.characterSelected?.list!! )
            _swipeToRefresh.isRefreshing = false

        }

        private fun downloadComics(){

            charactersViewModel.getCharacterWith( charactersViewModel.characterSelected?.id.toString()) {

                if (it == null)
                    return@getCharacterWith

                charactersViewModel.characterSelected?.list = it

                activity!!.runOnUiThread {
                    updateListOfComics()
                }

            }

        }

        private fun initToolbar(){

            val toolbar = uIView.findViewById<Toolbar>(R.id.toolbar)

            toolbar.setNavigationIcon(R.drawable.ic_arrow_back )
            toolbar.title = charactersViewModel.characterSelected?.name
            toolbar.setNavigationOnClickListener {
                dismiss()
            }

        }

        private fun initImage(){

            picasso
                .load("${charactersViewModel.characterSelected?.thumbnail?.path}/$image_size.${charactersViewModel.characterSelected?.thumbnail?.extension}")
                .into( uIView.findViewById<ImageView>(R.id._image_details ) )

        }

        private fun initDescription(){

            val description = uIView.findViewById<TextView>(R.id._description )

            description.text =
                if ( charactersViewModel.characterSelected?.description != "")
                    charactersViewModel.characterSelected?.description
                else
                    context!!.getString(R.string.no_description)

            if ( description.text.toString().length > 100 )
                description.movementMethod = ScrollingMovementMethod()

        }

        private fun initRecycler(){

            val recycler = uIView.findViewById<RecyclerView>( R.id._recycler_details )
            adapterComics = AdapterComics( context!! )

            recycler.apply {

                this.layoutManager = activity!!.getLayoutManagerToDetailsFragment()
                this.adapter = adapterComics

            }

        }

    }

}
