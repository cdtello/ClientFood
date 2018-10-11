package com.example.ctello.clientehenrys;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomListProductosAgregadosAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> producto_name;
    private final ArrayList<String> producto_precio;
    private final ArrayList<String> producto_cantidad;
    private final ArrayList<String> producto_total;

    public CustomListProductosAgregadosAdapter(Activity context, ArrayList<String> producto_name,ArrayList<String> producto_cantidad,ArrayList<String> producto_total, ArrayList<String> producto_precio) {
        super(context, R.layout.listaproductos, producto_name);
        this.context = context;
        this.producto_name = producto_name;
        this.producto_cantidad = producto_cantidad;
        this.producto_total = producto_total;
        this.producto_precio = producto_precio;

    }
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listaproductos_agregados, null, true);


        TextView txtproducto = (TextView) rowView.findViewById(R.id.nombreProductoAg);
        TextView txtcantidad = (TextView) rowView.findViewById(R.id.CantidadProductoAg);
        TextView txttotal = (TextView) rowView.findViewById(R.id.TotalProductoAg);
        TextView txtprecio = (TextView) rowView.findViewById(R.id.precioProductoAg);

        txtproducto.setText(producto_name.get(position));
        txtcantidad.setText(producto_cantidad.get(position));
        txttotal.setText(producto_total.get(position));
        txtprecio.setText(producto_precio.get(position));

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
