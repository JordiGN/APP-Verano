package com.example.kisspk.app_verano;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
    String[] wifis2;
    String[] wifistemp;
    int contar=1;
    Button btn;//botón para volver a hacer un escaneo
    Button btn2;//botón para guardar el scanresult actual
    Button btn3;//botón para reiniciar el contador de ubicación y de instancia
    WifiScanReceiver wifiReciever;
    int cont=1;
    String ub;
    int j=0;
    int tam1,tam2;
    String[] wf2;
    EditText text;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lectura__wifi);
        lv = (ListView) findViewById(R.id.listView);
        btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(this);
        btn2 = (Button) findViewById(R.id.button2);
        btn2.setOnClickListener(this);
        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiReciever = new WifiScanReceiver();

       /* wifi.startScan();*/
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

            /*contar++;*/
            Toast.makeText(getBaseContext(), "El contar esta en "+contar +" !!", Toast.LENGTH_SHORT).show();
            if(contar==2){
                wifis2= new String[wifiScanList.size()];
            }else{
                wifis = new String[wifiScanList.size()];
            }

           for (int i = 0; i < wifiScanList.size(); i++) {
                if(contar==2){

                    wifis2[i] = ((wifiScanList.get(i).BSSID).toString() + "," + (wifiScanList.get(i).level));
                    //Toast.makeText(getBaseContext(), "en wifi2", Toast.LENGTH_SHORT).show();


                }else{

                    wifis[i] = ((wifiScanList.get(i).BSSID).toString() + "," + (wifiScanList.get(i).level));
                    //Toast.makeText(getBaseContext(), "en wifi1", Toast.LENGTH_SHORT).show();

                }
            }
            if(contar== 2) {
                //Mostrar la lista
                lv.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, wifis2));
                tam2=wifis2.length;
                if(tam1>tam2){
                    wifistemp= new String[tam2];
                    for(int b=0;b<tam2;b++){
                        wifistemp[b]=wifis2[b];
                    }

                    wf2= new String[tam1];
                    for(int n=0;n<tam1;n++){
                        wf2[n]=wifis[n];
                    }

                }else{
                    wifistemp= new String[tam1];
                    for(int a=0;a<tam1;a++){
                        wifistemp[a]=wifis[a];
                    }
                    //String t1=wifis[0];
                    //String t2=wifis2[0];
                    wf2= new String[tam2];
                    for(int n=0;n<tam2;n++){
                        wf2[n]=wifis2[n];
                    }
                }
                while(j<(wifistemp.length)){
                    String t1=wifistemp[j];
                    String [] ttemp=t1.split(",");
                    String mac1=ttemp[0];
                    int inten1=Integer.parseInt(ttemp[1]);

                    for(int x=0;x<(wf2.length);x++){
                        String t2=wf2[x];
                        String[] ttemp2=t2.split(",");
                        String mac2=ttemp2[0];
                        int inte2=Integer.parseInt(ttemp2[1]);
                        if(mac1.equals(mac2)){
                            int intetemp=((inten1+inte2)/2);
                            String it=Integer.toString(intetemp);
                            wifistemp[j]=mac1+it;
                            x=wf2.length;
                        }
                    }
                    j++;
                }
                Toast.makeText(getBaseContext(),
                        "La instancia "+cont+" está lista, presiona guardar",
                        Toast.LENGTH_LONG).show();
                handler.removeCallbacks(runnable);
            }else {
                //Mostrar la lista
                lv.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, wifis));
                tam1=wifis.length;
                contar=2;
            }
        }
    }

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
      /* do what you need to do */
            wifi.startScan();
      /* and here comes the "trick" */
            handler.postDelayed(this, 1000);
        }
    };

    public void onClick(View arg0) {
    // TODO Auto-generated method stub
        File sdCard, directory, file = null;
        if (arg0.equals(btn)) {
            handler.postDelayed(runnable, 1000);
        }
            try {
                EditText text = (EditText)findViewById(R.id.editText);
                ub = text.getText().toString().trim();
                text.setFocusable(false);
                // validamos si se encuentra montada nuestra memoria externa
                if (Environment.getExternalStorageState().equals("mounted")) {
                    // Obtenemos el directorio de la memoria externa
                    sdCard = Environment.getExternalStorageDirectory();

                    if (arg0.equals(btn2)) {
                        Toast.makeText(getBaseContext(), "Entró al boton !!", Toast.LENGTH_SHORT).show();

                        Toast.makeText(getBaseContext(), "La ubicación es: "+ub +" !!", Toast.LENGTH_SHORT).show();

                        if (contar == 2) {
                            contar=1;
                            // Clase que permite grabar texto en un archivo
                            FileOutputStream fout = null;
                            try {
                                // instanciamos un objeto File para crear un nuevo
                                // directorio
                                // la memoria externa
                                directory = new File(sdCard.getAbsolutePath() + "/Instancias");
                                // se crea el nuevo directorio donde se cerara el
                                // archivo
                                directory.mkdirs();


                                // creamos el archivo en el nuevo directorio creado
                                file = new File(directory, (ub) + "_" + (cont) + ".txt");
                                cont=cont+1;
                                fout = new FileOutputStream(file);
                                // Convierte un stream de caracteres en un stream de bytes
                                OutputStreamWriter ows = new OutputStreamWriter(fout);
                                //se crea un arraylist para poder hacer las iteraciones

                                for (String nombre : wifistemp) {
                                    ows.write(nombre.toString() + "\n");
                                }

                                // Escribe en el buffer la cadena de texto
                                ows.flush(); // Volca lo que hay en el buffer al archivo
                                ows.close(); // Cierra el archivo de texto

                                Toast.makeText(getBaseContext(),
                                        "La instancia " + (cont - 1) + " de la ubicación " + ub + " se ha almacenado!!!",
                                        Toast.LENGTH_SHORT).show();
                                if (cont==4){
                                    cont=1;
                                    Toast.makeText(getBaseContext(),
                                            "La instancias de la ubicación " + ub + " se han completado, ingresa una nueva ubicación",
                                            Toast.LENGTH_SHORT).show();
                                    text.setFocusableInTouchMode(true);
                                    handler.removeCallbacks(runnable);
                                }

                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(getBaseContext(),
                                    "Refresca una vez mas",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(getBaseContext(),
                            "El almacenamineto externo no se encuentra disponible",
                            Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                // TODO: handle exception

            }
        }
    }
