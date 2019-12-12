package wsl.com.accedotest.Utils

import android.app.Activity
import android.content.res.Configuration
import android.view.Surface
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun Activity.isScreenBig(): Boolean =

    when {
        resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK == Configuration.SCREENLAYOUT_SIZE_LARGE -> true
        resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK == Configuration.SCREENLAYOUT_SIZE_NORMAL -> false
        resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK == Configuration.SCREENLAYOUT_SIZE_SMALL -> true
        else -> resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK == Configuration.SCREENLAYOUT_SIZE_XLARGE
    }

fun Activity.getOrientation(): Int {

    val orientation = this.resources.configuration.orientation

    return if (orientation == Surface.ROTATION_0 || orientation == Surface.ROTATION_180) {

        ORIENTACION_LANDSCAPE

    } else {

        ORIENTACION_PORTRAIT

    }

}

fun Activity.getColumnsToGridLayout(): Int {

    return if ( this.isScreenBig() ) {

        if (this.getOrientation() == ORIENTACION_PORTRAIT) {

            3

        }else {

            5

        }

    }else {
        1
    }

}

fun Activity.getLayoutManagerToDetailsFragment(): RecyclerView.LayoutManager {

    if ( isScreenBig() && getOrientation() == ORIENTACION_LANDSCAPE ){

        return LinearLayoutManager( this.applicationContext )

    }


    return LinearLayoutManager( this.applicationContext, LinearLayoutManager.HORIZONTAL, false)

}

//TODO: Falta bloquear la rotacion de pantalla en telefonos
//TODO: Colocar 'loadings' en vista 'details'
//TODO: colocar Placeholder a cada imagen
//TODO: material design a vista details con animacion