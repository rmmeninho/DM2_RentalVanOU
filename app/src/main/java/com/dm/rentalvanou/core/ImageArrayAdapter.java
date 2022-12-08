package com.dm.rentalvanou.core;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dm.rentalvanou.iu.R;

import java.util.List;

public class ImageArrayAdapter extends ArrayAdapter<Item> {

    Context context;

    public ImageArrayAdapter(Context context, int resourceId, List<Item> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final Context CONTEXT = this.getContext();
        final LayoutInflater INFLATER = LayoutInflater.from( CONTEXT );
        final Item ITEM = (Item) this.getItem( position );
// Crear la vista si no existe
        if ( view == null ) {
            view = INFLATER.inflate( R.layout.list_vans_layout, null );
        }
// Rellenar los datos

        final ImageView lblImage = view.findViewById( R.id.imageM);
        final TextView lblMarca = view.findViewById( R.id.textViewMM);

        lblImage.setImageResource(ITEM.getImg());
        lblMarca.setText( ITEM.getMarca());

        return view;
    }

}
