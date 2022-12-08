package com.dm.rentalvanou.core;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dm.rentalvanou.iu.R;

import java.util.List;

public class HistoricArrayAdapter extends ArrayAdapter<Item> {
    Context context;

    public HistoricArrayAdapter(Context context, int resourceId, List<Item> items) {
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
            view = INFLATER.inflate( R.layout.list_item_layout, null );
        }
// Rellenar los datos

        final TextView lblMarca = view.findViewById( R.id.textViewLMarca);
        final TextView lblModelo = view.findViewById( R.id.textViewLModelo );
        final TextView lblCombustible = view.findViewById(R.id.textViewLCombustible);
        final TextView lblFini = view.findViewById(R.id.textViewLFi);
        final TextView lblFf = view.findViewById(R.id.textViewLFf);
        final TextView lblCoste = view.findViewById(R.id.textViewLCoste);

        lblMarca.setText( ITEM.getMarca());
        lblModelo.setText( ITEM.getModelo());
        lblCombustible.setText( ITEM.getCombustible());
        lblFini.setText( ITEM.getF_ini());
        lblFf.setText( ITEM.getF_fin());
        lblCoste.setText( ITEM.getCoste());

        return view;
    }

}
