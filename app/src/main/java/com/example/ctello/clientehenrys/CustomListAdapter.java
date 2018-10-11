package com.example.ctello.clientehenrys;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter<String>{

    private final Activity context;
    private final ArrayList<String> item_name;
    private final ArrayList<String> item_cant;
    private final ArrayList<String> item_observ;
    private final ArrayList<String> item_mesa;

    public CustomListAdapter(Activity context, ArrayList<String> item_name, ArrayList<String> item_desc, ArrayList<String> item_precio, ArrayList<String> item_id) {
        super(context, R.layout.listamenu, item_name);
        this.context = context;
        this.item_cant = item_desc;
        this.item_name = item_name;
        this.item_observ = item_precio;
        this.item_mesa = item_id;
    }
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listamenu, null, true);


        TextView txtitem = (TextView) rowView.findViewById(R.id.pNombre);
        TextView txtcant = (TextView) rowView.findViewById(R.id.pCantidad);
        TextView txtobser = (TextView) rowView.findViewById(R.id.pObservacion);
        TextView txtmesa = (TextView) rowView.findViewById(R.id.pMesa);

        txtitem.setText(item_name.get(position));
        txtcant.setText(item_cant.get(position));
        txtobser.setText(item_observ.get(position));
        txtmesa.setText(item_mesa.get(position));
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
