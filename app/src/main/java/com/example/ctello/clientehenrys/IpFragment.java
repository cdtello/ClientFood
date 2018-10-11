package com.example.ctello.clientehenrys;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ctello.clientehenrys.Utilidades.Utilidades;
import com.example.ctello.clientehenrys.entidades.ConexionSQLiteHelper;


public class IpFragment extends Fragment{


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_ip, container, false);


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ip, container, false);
    }


}
