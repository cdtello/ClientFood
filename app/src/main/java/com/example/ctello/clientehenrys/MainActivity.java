package com.example.ctello.clientehenrys;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ServerFood.ProductoTransmision;

public class MainActivity extends AppCompatActivity {

    String message = "";
    String nombre;
    int id;
    ListView listado;
    AdminHilo adminHilo;
    TextView produc,cant,descrip,obser;
    EditText direccionServidor;

    ArrayList<String> item_name = new ArrayList();
    ArrayList<String>  item_cant = new ArrayList();
    ArrayList<String>  item_observ = new ArrayList();
    ArrayList<String>  item_mesa = new ArrayList();
    ArrayList<ProductoTransmision> productosTransmision = new ArrayList<>();

    Spinner spinner_estaciones;
    ArrayAdapter spinner_adapter;


    Integer p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nombre = "";
        id = 0;

        /*spinner_estaciones = (Spinner) findViewById(R.id.spinner);
        spinner_adapter = ArrayAdapter.createFromResource(this,R.array.estaciones,android.R.layout.simple_spinner_item);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_estaciones.setAdapter(spinner_adapter);
        direccionServidor = (EditText) findViewById(R.id.editIP);
        adminHilo = new AdminHilo(); */
        //prueba();
        //llenarListView();
    }
    public void imp()
    {
        for (int i = 0; i< item_name.size();i++)
        {
            System.out.println("Item :"+item_name.get(i));
        }
    }
    public String getIP()
    {

        String direccion = direccionServidor.getText().toString();
        return direccion;
    }
    public void ingresarProducto(ProductoTransmision producto)
    {
        item_name.add(producto.getNombre_producto());
        item_cant.add(String.valueOf(producto.getCantidad()));
        item_observ.add(producto.getObservacion()+" Salsas: "+producto.getSalsas());
        item_mesa.add(producto.getEntregarA());
        productosTransmision.add(producto);
    }
    public void eliminarElementos(int indice)
    {
        System.out.println("Enviando respuesta al servidor......");
        adminHilo.enviarRespuesta(productosTransmision.get(indice));
        item_name.remove(indice);
        item_cant.remove(indice);
        item_observ.remove(indice);
        item_mesa.remove(indice);
        productosTransmision.remove(indice);
    }
    public void btnConectarPresionado(View view){
        if(adminHilo.getEstadoConexion() == 0)
        {
            nombre = spinner_estaciones.getSelectedItem().toString();

            id = spinner_estaciones.getSelectedItemPosition();
            if(id == 0)
            {
                Toast toast1 = Toast.makeText(getApplicationContext(), "Seleccione una estacion", Toast.LENGTH_SHORT);
                toast1.show();
            }
            else
            {
                //adminHilo.setVentanaPrincipal(this);
                adminHilo.execute();
                System.out.println("Boton presionado");
            }
        }
        else
        {
            Toast toast1 = Toast.makeText(getApplicationContext(), "Error, ya estas Conectado", Toast.LENGTH_SHORT);
            toast1.show();
        }

    }
    public void eliminarHilo()
    {
        adminHilo = new AdminHilo();
    }

    public void llenarListView()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listado = (ListView) findViewById(R.id.vPedido);
                CustomListAdapter adaptador = new CustomListAdapter(MainActivity.this, item_name, item_cant, item_observ, item_mesa);
                listado.setAdapter(adaptador);
                listado.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        p = position;
                        new AlertDialog.Builder(MainActivity.this).setTitle("Confirmacion Finalizacion").setMessage("Desea Finalizar Pedido?").setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                System.out.println("******   Eliminacion Posicion: "+p+"   *******");
                                eliminarElementos(p);
                                llenarListView();
                                imp();
                            }
                        }).setNegativeButton("No!",null).show();
                    }
                });
            }
        });
    }
}
