package com.example.ctello.clientehenrys;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ctello.clientehenrys.Utilidades.Utilidades;
import com.example.ctello.clientehenrys.entidades.ConexionSQLiteHelper;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import ServerFood.ProductoTransmision;
import ServerFood.ProductosDomicilio;

public class Inicio extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String nombre;
    int id;

    ArrayList<ProductoTransmision> ListaProductosAgregados = new ArrayList<>();
    ArrayList<ProductosDomicilio> todosProductos = new ArrayList<>();
    ArrayList<String> producto_name = new ArrayList();
    ArrayList<Integer> producto_id = new ArrayList();
    ArrayList<Integer> producto_precio = new ArrayList();
    ArrayList<Integer> producto_id_categoria = new ArrayList<>();

    ArrayList<String> item_name = new ArrayList();
    ArrayList<String>  item_cant = new ArrayList();
    ArrayList<String>  item_observ = new ArrayList();
    ArrayList<String>  item_mesa = new ArrayList();
    ArrayList<ProductoTransmision> productosTransmision = new ArrayList<>();

    ArrayList<String> producto_name_agregado = new ArrayList();
    ArrayList<String>  producto_cantidad_agregado = new ArrayList();
    ArrayList<String>  producto_total_agregado = new ArrayList();
    ArrayList<String>  producto_precio_agregado = new ArrayList();

    Button btndom;
    TextView tvnompro,etcanpro,etobspro;
    ListView listado,listadoProductos,listadoProductosAgregados;
    Integer p;
    AdminHilo adminHilo;

    RadioButton Rsalchipapa,Rplancha,Rhorno,Rcocina,Rdomicilio;
    RadioGroup radioGroup,radioCategorias;

    Spinner spinner_direcciones;
    ArrayAdapter spinner_adapter;
    TextView campoInicio;
    EditText campoDireccion;
    String direccion = "";
    ConexionSQLiteHelper conn;
    ArrayList<String> listaIP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        btndom = (Button) findViewById(R.id.btnDom);
        tvnompro = (TextView)findViewById(R.id.tvNomPro);
        etcanpro = (EditText)findViewById(R.id.etCanPro);
        etobspro = (EditText)findViewById(R.id.etObsPro);

        Rsalchipapa = (RadioButton)findViewById(R.id.rbSalchipapa);
        Rplancha = (RadioButton)findViewById(R.id.rbPlancha);
        Rhorno = (RadioButton)findViewById(R.id.rbHorno);
        Rcocina = (RadioButton)findViewById(R.id.rbCocina);
        Rdomicilio = (RadioButton)findViewById(R.id.rbDomicilio);
        radioGroup = (RadioGroup)findViewById(R.id.RGroup);
        radioCategorias = (RadioGroup) findViewById(R.id.RgroupCat);
        spinner_direcciones = (Spinner) findViewById(R.id.spinnerDirecciones);
        consultarDirecciones();
        adminHilo = new AdminHilo();

        btndom.setVisibility(View.INVISIBLE);
    }
    public void consultarDirecciones()
    {
        listaIP = new ArrayList<>();
        conn = new ConexionSQLiteHelper(getApplicationContext(),"bd_prueba2",null,1);
        SQLiteDatabase db = conn.getReadableDatabase();
        try{
            Cursor cursor = db.rawQuery("select ip from "+Utilidades.TABLA_IPSERVIDOR,null);
            if (cursor.moveToFirst()) {
                //Recorremos el cursor hasta que no haya más registros
                do {
                    listaIP.add(cursor.getString(0));
                } while(cursor.moveToNext());
            }
            db.close();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Error en lectura de datos",Toast.LENGTH_LONG).show();
        }
        spinner_adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,listaIP);
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_direcciones.setAdapter(spinner_adapter);
    }
    public void eliminarDireccion(String direccion)
    {
        conn = new ConexionSQLiteHelper(getApplicationContext(),"bd_prueba2",null,1);
        SQLiteDatabase db = conn.getReadableDatabase();
        try{
            db.delete(Utilidades.TABLA_IPSERVIDOR,Utilidades.CAMPO_DIRECCION+ " = '"+direccion+"'",null);
            Toast.makeText(getApplicationContext(),"Eliminado: "+direccion,Toast.LENGTH_LONG).show();
            consultarDirecciones();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Error Eliminando: "+direccion,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        if (id == R.id.nav_configIP) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new IpFragment()).commit();
        }
        /*else if (id == R.id.nav_principal) {
            fragmentManager.beginTransaction().replace(R.id.contenedor, new PrincipalFragment()).commit();
        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void RegistrarIP(View view){
        campoDireccion = (EditText) findViewById(R.id.campoDireccionIP);
        direccion = campoDireccion.getText().toString();

        if(direccion.compareTo("") != 0)
        {
            ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this,"bd_prueba2",null,1);
            SQLiteDatabase db = conn.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(Utilidades.CAMPO_DIRECCION,direccion);
            Long idResultante = db.insert(Utilidades.TABLA_IPSERVIDOR,Utilidades.CAMPO_DIRECCION,values);
            db.close();
            Toast.makeText(getApplicationContext(),"Registro Satisfactorio",Toast.LENGTH_LONG).show();
            campoDireccion.setText("");
            consultarDirecciones();

        }

    }
    public void EliminarIP(View view){
        String seleccion = spinner_direcciones.getSelectedItem().toString();
        eliminarDireccion(seleccion);
        //Toast.makeText(getApplicationContext(),"Eliminando: "+seleccion,Toast.LENGTH_LONG).show();
    }
    public void btnConectarPresionado(View view){
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

        //****************************************************************************************************************
        //****************************************************************************************************************
        //*****************************Validacion de Estacion Seleccionada************************************************
        int seleccionado = radioGroup.getCheckedRadioButtonId();
        String value = "";
        try {
            value =((RadioButton)findViewById(seleccionado)).getText().toString();

        }catch (Exception e){
            value = "Error";
        }

        if(value.compareTo("Salchipapa")==0)
        {
            nombre = "SALCHIPAPA";
            id = 1;

        }else if(value.compareTo("Plancha")==0)
        {
            nombre = "PLANCHA";
            id = 2;

        }else if(value.compareTo("Horno")==0)
        {
            nombre = "HORNO";
            id = 3;

        }else if(value.compareTo("Cocina")==0)
        {
            nombre = "COCINA";
            id = 4;

        }else if(value.compareTo("Domicilio")==0)
        {
            nombre = "DOMICILIO";
            id = 5;

        }else{Toast.makeText(getApplicationContext(),"ERROR ",Toast.LENGTH_LONG).show();return; }
        //****************************************************************************************************************
        //****************************************************************************************************************
        if(adminHilo.getEstadoConexion() == 0)
        {
            adminHilo.setVentanaPrincipal(this);
            adminHilo.execute();
            System.out.println("Boton presionado");
        }

        else
        {
            Toast toast1 = Toast.makeText(getApplicationContext(), "Error, ya estas Conectado", Toast.LENGTH_SHORT);
            toast1.show();
        }
    }
    public void cambiarFragment(){
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        if(nombre.compareTo("DOMICILIO")==0){
            fragmentManager.beginTransaction().replace(R.id.contenedor, new DomicilioFragment()).commit();
            //Toast.makeText(getApplicationContext(),"SELECCIONADO...: "+nombre,Toast.LENGTH_LONG).show();
        }
        else{
            fragmentManager.beginTransaction().replace(R.id.contenedor, new PrincipalFragment()).commit();
            //Toast.makeText(getApplicationContext(),"SELECCIONADO...: "+nombre,Toast.LENGTH_LONG).show();
        }
    }

    public void eliminarHilo()
    {
        adminHilo = new AdminHilo();
    }
    public String getIP()
    {
        String direccion = spinner_direcciones.getSelectedItem().toString();
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
    public void llenarListView()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listado = (ListView) findViewById(R.id.vPedido);
                CustomListAdapter adaptador = new CustomListAdapter(Inicio.this, item_name, item_cant, item_observ, item_mesa);
                listado.setAdapter(adaptador);
                listado.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        p = position;
                        new AlertDialog.Builder(Inicio.this).setTitle("Confirmacion Finalizacion").setMessage("Desea Finalizar Pedido?").setPositiveButton("SI", new DialogInterface.OnClickListener() {
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
    //************************************************************************************************
    //************************************************************************************************
    public void btnAgregarPresionado(View view)
    {
        ProductoTransmision productoTransmision = new ProductoTransmision();
        final EditText etcanpro = (EditText)findViewById(R.id.etCanPro);
        final EditText etobspro = (EditText)findViewById(R.id.etObsPro);
        final TextView tvtotal = (TextView)findViewById(R.id.tvTotal);
        final EditText edtnombre = (EditText)findViewById(R.id.edtNombre);
        final EditText edttelefono = (EditText)findViewById(R.id.edtTelefono);
        final EditText edtdireccion = (EditText)findViewById(R.id.edtDireccion);

        productoTransmision.setId_producto(producto_id.get(p));
        productoTransmision.setNombre_producto(producto_name.get(p));
        productoTransmision.setCantidad(Integer.parseInt(etcanpro.getText().toString()));
        productoTransmision.setObservacion(etobspro.getText().toString());
        productoTransmision.setPrecio(producto_precio.get(p));
        productoTransmision.setNombreCliente(edtnombre.getText().toString());
        productoTransmision.setTelefonoCliente(edttelefono.getText().toString());
        productoTransmision.setDireccionCliente(edtdireccion.getText().toString());

        Locale locale = new Locale("es","CO");
        NumberFormat nf = DecimalFormat.getCurrencyInstance(locale);

        int precio = productoTransmision.getPrecio();
        String precioFinal = nf.format(precio);//.substring(0,nf.format(precio).length());

        int precioTotal = productoTransmision.getPrecio() * productoTransmision.getCantidad();
        String precioFinalTotal = nf.format(precioTotal);//.substring(0,nf.format(precio).length());

        producto_name_agregado.add(productoTransmision.getNombre_producto());
        producto_cantidad_agregado.add(String.valueOf(productoTransmision.getCantidad()));
        producto_total_agregado.add(precioFinalTotal);
        producto_precio_agregado.add(precioFinal);

        ListaProductosAgregados.add(productoTransmision);

        int totalPagar = 0;
        for (int i=0; i<producto_total_agregado.size(); i++)
        {
            totalPagar = totalPagar + ListaProductosAgregados.get(i).getPrecio() * ListaProductosAgregados.get(i).getCantidad();
        }

        tvtotal.setText("TOTAL PEDIDO: "+nf.format(totalPagar)+"  + DOMICILIO");
        llenarListViewProductosAgregados();
    }
    public void radioGroupPresionado(View view)
    {
        int seleccionado = view.getId();
        String value =((RadioButton)findViewById(seleccionado)).getText().toString();

        if(value.compareTo("SALCHIPAPA")==0)
        {
            mostrarProductos(1);
        }else if(value.compareTo("CARNE")==0)
        {
            mostrarProductos(2);
        }else if(value.compareTo("ALITAS")==0)
        {
            mostrarProductos(3);
        }else if(value.compareTo("SANDWICH")==0)
        {
            mostrarProductos(4);
        }else if(value.compareTo("AREPAS")==0)
        {
            mostrarProductos(5);
        }else if(value.compareTo("PIZZA")==0)
        {
            mostrarProductos(6);
        }else if(value.compareTo("LASAGNA")==0)
        {
            mostrarProductos(7);
        }else if(value.compareTo("PERRO CALIENTE")==0)
        {
            mostrarProductos(8);
        }else if(value.compareTo("MAICITO")==0)
        {
            mostrarProductos(9);
        }else if(value.compareTo("HAMBURGUESA RES")==0)
        {
            mostrarProductos(10);
        }else if(value.compareTo("HAMBURGUESA POLLO")==0)
        {
            mostrarProductos(11);
        }else if(value.compareTo("JUGOS NATURALES")==0)
        {
            mostrarProductos(12);
        }else if(value.compareTo("BEBIDAS")==0)
        {
            mostrarProductos(13);
        }else if(value.compareTo("ADICIONALES")==0)
        {
            mostrarProductos(14);
        }
    }

    public void llenarListViewProductos()
    {
        final TextView tvnompro = (TextView) findViewById(R.id.tvNomPro);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listadoProductos = (ListView) findViewById(R.id.vProductos);
                CustomListProductosAdapter adaptador = new CustomListProductosAdapter(Inicio.this, producto_name);
                listadoProductos.setAdapter(adaptador);
                listadoProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        p = position;
                        tvnompro.setText(producto_name.get(p)+"id: "+producto_id.get(p));
                    }
                });
            }
        });
    }
    public void mostrarProductos(int cat)
    {
        producto_name = new ArrayList<>();
        producto_id = new ArrayList<>();
        producto_id_categoria = new ArrayList<>();
        producto_precio = new ArrayList<>();

        for(int i=0; i<todosProductos.size(); i++)
        {
            if(todosProductos.get(i).getIdCategoria() == cat)
            {
                producto_name.add(todosProductos.get(i).getNombre());
                producto_id.add(todosProductos.get(i).getId());
                producto_id_categoria.add(todosProductos.get(i).getIdCategoria());
                producto_precio.add(todosProductos.get(i).getPrecio());
            }
        }
        llenarListViewProductos();
    }
    public void ingresarListaProducto(ProductosDomicilio producto)
    {
        todosProductos.add(producto);
    }
    //************************************************************************************************
    //************************************************************************************************
    public void llenarListViewProductosAgregados()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listadoProductosAgregados = (ListView) findViewById(R.id.vProductosAgr);
                CustomListProductosAgregadosAdapter adaptador = new CustomListProductosAgregadosAdapter(Inicio.this, producto_name_agregado, producto_cantidad_agregado, producto_total_agregado, producto_precio_agregado);
                listadoProductosAgregados.setAdapter(adaptador);
                listadoProductosAgregados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        p = position;
                        new AlertDialog.Builder(Inicio.this).setTitle("Confirmacion Finalizacion").setMessage("Desea Eliminar Producto?").setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                System.out.println("******   Selecciono Posicion: "+p+"   *******");
                                eliminarElementosAgregados(p);

                                llenarListViewProductosAgregados();

                            }
                        }).setNegativeButton("No!",null).show();
                    }
                });
            }
        });
    }
    public void enviarPedidoDomicilio(View view)
    {
        adminHilo.enviarPedidoDomicilio(ListaProductosAgregados);

        producto_name_agregado = new ArrayList<>();
        producto_cantidad_agregado = new ArrayList<>();
        producto_total_agregado = new ArrayList<>();
        producto_precio_agregado = new ArrayList<>();

        ListaProductosAgregados = new ArrayList<>();
        llenarListViewProductosAgregados();


        final EditText etcanpro = (EditText)findViewById(R.id.etCanPro);
        final EditText etobspro = (EditText)findViewById(R.id.etObsPro);
        final TextView tvtotal = (TextView)findViewById(R.id.tvTotal);
        final EditText edtnombre = (EditText)findViewById(R.id.edtNombre);
        final EditText edttelefono = (EditText)findViewById(R.id.edtTelefono);
        final EditText edtdireccion = (EditText)findViewById(R.id.edtDireccion);

        edtnombre.setText("");
        edtdireccion.setText("");
        edttelefono.setText("");
        etcanpro.setText("");
    }
    public void eliminarElementosAgregados(int indice)
    {
        //System.out.println("Enviando respuesta al servidor......");
        //adminHilo.enviarRespuesta(productosTransmision.get(indice));
        producto_name_agregado.remove(indice);
        producto_cantidad_agregado.remove(indice);
        producto_total_agregado.remove(indice);
        producto_precio_agregado.remove(indice);

        ListaProductosAgregados.remove(indice);
    }
    //************************************************************************************************
    //************************************************************************************************
    public void imp()
    {
        for (int i = 0; i< item_name.size();i++)
        {
            System.out.println("Item :"+item_name.get(i));
        }
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
}
