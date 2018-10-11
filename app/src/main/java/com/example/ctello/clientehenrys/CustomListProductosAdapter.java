package com.example.ctello.clientehenrys;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomListProductosAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> producto_name;

    public CustomListProductosAdapter(Activity context, ArrayList<String> producto_name) {
        super(context, R.layout.listaproductos, producto_name);
        this.context = context;
        this.producto_name = producto_name;

    }
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listaproductos, null, true);


        TextView txtproducto = (TextView) rowView.findViewById(R.id.nombreProducto);
        txtproducto.setText(producto_name.get(position));

        if(position%2==0)
        {
            rowView.setBackgroundColor(Color.argb(255,248,248,248));
        }
        else
        {
            rowView.setBackgroundColor(Color.argb(220,220,220,220));
        }

        return rowView;
    }
}