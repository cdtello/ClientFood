package com.example.ctello.clientehenrys;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import ServerFood.Estacion;
import ServerFood.ProductoTransmision;
import ServerFood.ProductosDomicilio;


public class AdminHilo extends AsyncTask<Void,Void,Void> implements Runnable{
    //MediaPlayer mp;
    Thread hilo;
    Inicio ventanaPrincipal;
    //MainActivity ventanaPrincipal;
    private Socket socket;
    ObjectInputStream objectInputStream;
    ObjectOutputStream objectOutputStream;
    private int estadoConexion = 0;
    Estacion estacion;

    public int getEstadoConexion() {
        return estadoConexion;
    }

    public void setEstadoConexion(int estadoInicial) {
        this.estadoConexion = estadoInicial;
    }

    public AdminHilo()
    {
        hilo = new Thread(this);
    }
    public void setVentanaPrincipal(Inicio ventana)
    {
        ventanaPrincipal = ventana;
        estacion = new Estacion(ventanaPrincipal.nombre,ventanaPrincipal.id);
        //mp =  MediaPlayer.create(ventanaPrincipal,R.raw.sonido);
    }

    public int conectar(){
        System.out.println("*****************************Valindando Conexion*********************************");

        try {
            System.out.println("*****************************Esperando Conexion*********************************");
            String ip = ventanaPrincipal.getIP();
            socket = new Socket(ip, 5050);
            System.out.println("Estamos Conectados");
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream()); //Salida de Objetos
            objectOutputStream.flush(); // Limpia Bufer
            objectInputStream = new ObjectInputStream(socket.getInputStream()); //Entrada de Objetos

            ventanaPrincipal.cambiarFragment();

            return 1;
        } catch (IOException e) {
            System.out.println("*****************************No se Encontro Servidor  3*********************************");
            //ventanaPrincipal.finish();
            return 0;
        }
    }


    @Override
    public void run() {

        try{
        while (true)
        {

            if(estadoConexion == 0)
            {
                try {
                    Estacion estacionRespuesta = (Estacion) objectInputStream.readObject();
                    estacion.setEstado(estacionRespuesta.getEstado());
                    if (estacion.getEstado() == 1)
                    {
                        ventanaPrincipal.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast toast1 = Toast.makeText(ventanaPrincipal.getApplicationContext(), "Conexion Establecida Correctamente", Toast.LENGTH_SHORT);
                                toast1.show();
                            }
                        });
                        estadoConexion = 1;
                        //*********************Codigo para recibir los productos de Domicilios*******
                        if(this.estacion.getId() == 5)
                        {
                            ArrayList<ProductosDomicilio> productosDomicilios = (ArrayList<ProductosDomicilio>) objectInputStream.readObject();

                            System.out.println("Se recibieron todos los productos");
                            for(int i=0; i<productosDomicilios.size();i++)
                            {
                                System.out.println("Nombre Producto:  "+productosDomicilios.get(i).getNombre());
                                ventanaPrincipal.ingresarListaProducto(productosDomicilios.get(i));
                            }
                        }



                    }
                    else if(estacion.getEstado() == 0)
                    {
                        ventanaPrincipal.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast toast1 = Toast.makeText(ventanaPrincipal.getApplicationContext(), "Conexion Rechazada", Toast.LENGTH_SHORT);
                                toast1.show();
                            }
                        });
                        ventanaPrincipal.eliminarHilo();
                        estadoConexion = 0;
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else if(estadoConexion == 1)
            {
                System.out.println("Esperando Recibir....");
                try {
                    ProductoTransmision producto = (ProductoTransmision) objectInputStream.readObject();
                    ventanaPrincipal.ingresarProducto(producto);
                    ventanaPrincipal.llenarListView();
                    //mp.start();

                }catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }}catch (IOException ex) {
            ventanaPrincipal.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast toast1 = Toast.makeText(ventanaPrincipal.getApplicationContext(), "Servidor Desconectado", Toast.LENGTH_SHORT);
                    toast1.show();
                    ventanaPrincipal.finish();
                }
            });
            estadoConexion = 0;
            ventanaPrincipal.eliminarHilo();
        }
    }
    //***Conecta, Autentica, y ejecuta el metodo Run()***

    @Override
    protected Void doInBackground(Void... params) {
        if(conectar() == 1){
            autenticar();
            hilo.start();
            System.out.println("Hilo Receptor Iniciado");
            return null;
        }
        else{
            ventanaPrincipal.finish();
            return null;
        }
    }

    public void autenticar(){
        try{
            objectOutputStream.writeObject(estacion);
            objectOutputStream.flush();
            System.out.println("Se envio: ");
            System.out.println(estacion.getNombre());
            System.out.println(estacion.getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void enviarRespuesta(ProductoTransmision producto)
    {
        try{
            objectOutputStream.writeObject(producto);
            objectOutputStream.flush();
            System.out.println("Se finalizo: ");
            System.out.println("Producto: "+producto.getNombre_producto());
            System.out.println("Cantidad: "+producto.getCantidad());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void enviarPedidoDomicilio(ArrayList<ProductoTransmision> pedidoDomicilio)
    {
        try{
            objectOutputStream.writeObject(pedidoDomicilio);
            objectOutputStream.flush();
            System.out.println("*****************************SE ENVIO TODO EL PEDIDO**************************************");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
