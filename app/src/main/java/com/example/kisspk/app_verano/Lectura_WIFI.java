package com.example.kisspk.app_verano;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

//clases importadas para guardar datos
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import android.os.Environment;
import android.widget.Toast;


/*public class Lectura_WIFI extends ActionBarActivity*/
public class Lectura_WIFI extends Activity implements View.OnClickListener {
    ListView lv;
    WifiManager wifi;
    String[] wifis;
    Button btn;//botón para volver a hacer un escaneo
    Button btn2;//botón para guardar el scanresult actual
    Button btn3;//botón para reiniciar el contador de ubicación y de instancia
    WifiScanReceiver wifiReciever;
    int cont=1;
    int ub=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lectura__wifi);
        lv = (ListView) findViewById(R.id.listView);
        btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(this);
        btn2 = (Button) findViewById(R.id.button2);
        btn2.setOnClickListener(this);
        btn3 = (Button) findViewById(R.id.button3);
        btn3.setOnClickListener(this);

        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiReciever = new WifiScanReceiver();
        wifi.startScan();
    }

    protected void onPause() {
        unregisterReceiver(wifiReciever);
        super.onPause();
    }

    protected void onResume() {
        registerReceiver(wifiReciever, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lectura__wifi, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class WifiScanReceiver extends BroadcastReceiver {
        public void onReceive(Context c, Intent intent) {
            List<ScanResult> wifiScanList = wifi.getScanResults();
            wifis = new String[wifiScanList.size()];

            for (int i = 0; i < wifiScanList.size(); i++) {
                wifis[i] = ((wifiScanList.get(i).BSSID).toString() + "," + (wifiScanList.get(i).level));
            }
            lv.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, wifis));
        }

    }

    public void onClick(View arg0) {

        if (arg0.equals(btn))
        {wifi.startScan();}

        // TODO Auto-generated method stub
        File sdCard, directory, file = null;

        if (arg0.equals(btn3)){
            cont=1;
            ub=1;
        }

        try {
            // validamos si se encuentra montada nuestra memoria externa
            if (Environment.getExternalStorageState().equals("mounted")) {
                // Obtenemos el directorio de la memoria externa
                sdCard = Environment.getExternalStorageDirectory();
                if (arg0.equals(btn2)) {
                    // Clase que permite grabar texto en un archivo
                    FileOutputStream fout = null;
                    try {
                        // instanciamos un objeto File para crear un nuevo
                        // directorio
                        // la memoria externa
                        directory = new File(sdCard.getAbsolutePath()
                                + "/Instancias");
                        // se crea el nuevo directorio donde se cerara el
                        // archivo
                        directory.mkdirs();

                        // creamos el archivo en el nuevo directorio creado
                        file = new File(directory, "Ubicación"+(ub)+"_"+(cont++)+".txt");
                        fout = new FileOutputStream(file);
                        // Convierte un stream de caracteres en un stream de
                        // bytes
                       OutputStreamWriter ows = new OutputStreamWriter(fout);
                        //se crea un arraylist para poder hacer las iteraciones

                        for (String nombre : wifis) {
                            ows.write(nombre.toString()+"\n");
                        }
                        // Escribe en el buffer la cadena de texto
                        ows.flush(); // Volca lo que hay en el buffer al archivo
                        ows.close(); // Cierra el archivo de texto


                        Toast.makeText(getBaseContext(),
                                "La instancia "+(cont-1)+" de la ubicación "+ub+" se ha almacenado!!!",
                                Toast.LENGTH_SHORT).show();
                        if (cont==4){
                            cont=1;
                            ub++;
                        }

                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }else{
                Toast.makeText(getBaseContext(),
                        "El almacenamineto externo no se encuentra disponible",
                        Toast.LENGTH_LONG).show();
            }

        }catch (Exception e) {
            // TODO: handle exception

        }
    }
}