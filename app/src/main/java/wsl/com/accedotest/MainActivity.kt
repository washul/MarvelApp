package wsl.com.accedotest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import wsl.com.accedotest.Fragments.CharacterDetailsFragment
import wsl.com.accedotest.Models.CharacterModel
import wsl.com.accedotest.Models.CharactersViewModel
import wsl.com.accedotest.Utils.*

class MainActivity : AppCompatActivity() {

    lateinit var charactersViewModel: CharactersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        charactersViewModel = ViewModelProviders.of( this@MainActivity ).get( CharactersViewModel::class.java )
        InitUI().run()

    }

    inner class InitUI: Thread() {

        private lateinit var recycler: RecyclerView
        private lateinit var adapter: AdapterCharacter

        private val _swipeToRefresh = findViewById<SwipeRefreshLayout>( R.id._swipeToRefresh )

        override fun run() {
            super.run()

            _swipeToRefresh.isRefreshing = true

            initRecyclerView()

            if ( isScreenBig() )
                charactersViewModel.limit = 20

            downloadItems()

        }

        private fun downloadItems() {

            charactersViewModel.getMoreCharacters {

                if ( it != null && it.isNotEmpty() ) {

                    this@MainActivity.runOnUiThread {

                        adapter.updateList( it )


                    }

                }else{

                    Toast.makeText( applicationContext, "No se a podido actualizar la lista.", Toast.LENGTH_LONG ).show()

                }

                _swipeToRefresh.isRefreshing = false

            }

        }

        private fun initRecyclerView(){

            val delegate = object : IAdapterCharacter {
                override fun itemSelected(item: CharacterModel) {

                    charactersViewModel.characterSelected = item

                    val ft = supportFragmentManager.beginTransaction()
                    CharacterDetailsFragment().show( ft, CharacterDetailsFragment.TAG)

                }
            }

            recycler = this@MainActivity.findViewById( R.id._recycler )
            adapter = AdapterCharacter(  this@MainActivity.applicationContext, delegate )

            recycler.apply {

                this.layoutManager = GridLayoutManager( this@MainActivity, this@MainActivity.getColumnsToGridLayout() )
                this.adapter = this@InitUI.adapter

            }

            recycler.addOnScrollListener( object : RecyclerView.OnScrollListener(){

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                    if (!recyclerView.canScrollVertically(1)) {

                        _swipeToRefresh.isRefreshing = true
                        downloadItems()

                    }

                }

            })

            _swipeToRefresh.setOnRefreshListener {

                charactersViewModel.charactersList = emptyList()
                downloadItems()

            }

        }

    }

}
