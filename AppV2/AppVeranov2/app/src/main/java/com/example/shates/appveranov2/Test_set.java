package com.example.shates.appveranov2;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import junit.framework.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Test_set extends Activity{
    String[][] nomI;
    WifiManager wifi;
    String[] wifis;
    TextView Texto;

    WifiScanReceiver wifiReciever;

    View vis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_set);
        Texto=(TextView) findViewById(R.id.textView4);
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
        getMenuInflater().inflate(R.menu.menu_test_set, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class WifiScanReceiver extends BroadcastReceiver {
        public void onReceive(Context c, Intent intent) {
            List<ScanResult> wifiScanList = wifi.getScanResults();

            wifis = new String[wifiScanList.size()];

            for (int i = 0; i < wifiScanList.size(); i++)
            {
                wifis[i] = ((wifiScanList.get(i).BSSID).toString() + "," + (wifiScanList.get(i).level));
            }
            //Mostrar la lista
            //lv.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, wifis));
            //Manda leer otro archivo
            olarch(vis);
        }

    }

    public void olarch(View view){
        String path = Environment.getExternalStorageDirectory().toString()+"/Instancias";//Obtiene los archivos de la carpeta

        File f = new File(path);//Vector para guardar los archivos
        File file[] = f.listFiles();

        nomI= new String[file.length][4];//Vector para guardar el nombre de los archivos

        for (int i=0; i < file.length; i++)//Guardar los nombres en un vector/matriz
        {
            //n[i]=file[i].getName();

            nomI[i][0]=file[i].getName();
            nomI[i][1]="0";
            nomI[i][2]="0";
            nomI[i][3]="0";
        }

        //Leer el archivo
        try
        {
            for (int na=0;na<nomI.length;na++)//Ciclo para acceder a la lista de archivos
            {
                //Siempre aqui
                File ff = new File(path, nomI[na][0].toString()); //Forever here
                BufferedReader fin = new BufferedReader(new InputStreamReader(new FileInputStream(ff))); //Para leer el archivo
                //Declaracion de variables
                String linea;//Variable para guarda la lista del archivo
                ArrayList<String> wifistemp =new ArrayList<String>(); //Lista de arrays guardar coincidencias
                ArrayList<String> wf2 = new ArrayList<String>(); //Lista para guardar las lineas del archivo
                int suma=0,s=0; //Sumatorias para obtener promedios
                int promedio=0, p=0; //Promedios

                /**/
                //Repetir mientras no sea el final del archivo
                while((linea=fin.readLine())!=null)
                {
                    wf2.add(linea);// Agregar a un Array list los valores del archivo
                    String [] tupla=linea.split(",");
                    int intes=Integer.parseInt((tupla[1]));
                    intes=(intes*intes);
                    s=s+intes;
                }
                p=s/wf2.size();
                nomI[na][1]=Integer.toString(p);
                fin.close();

                /*Funciona de aqui hacia arriba*/
                int c=0;
                for(int x=0; x<wifis.length;x++)
                {

                    String t1=wifis[x];
                    String [] ttemp=t1.split(",");
                    String mac1=ttemp[0];
                    int inten1=Integer.parseInt(ttemp[1]);
                    for(String t2: wf2)
                    {
                        String [] ttemp2=t2.split(",");
                        String mac2=ttemp2[0];
                        if(mac1.equals(mac2))
                        {
                            int it= inten1*inten1;
                            suma=suma+it;
                            String ln=mac1+","+it;
                            wifistemp.add(ln);
                            c++;
                            break;
                        }
                    }

                }
                if(wifistemp.size()==0){
                    suma=0;
                    wifistemp.add("0");
                }
                promedio=suma/wifistemp.size();
                nomI[na][2]=Integer.toString(promedio);
            }
            dif(vis);

        }
        catch (Exception e)
        {
            Toast.makeText(getBaseContext(), "Error al leer fichero desde memoria",Toast.LENGTH_LONG).show();
        }

    }

    public void dif(View view){
        int [] vaux;
        String ubicacion="";
        vaux= new int[nomI.length];

        for(int w=0; w<nomI.length;w++){
            int iarc=Integer.parseInt(nomI[w][1]);
            int iact=Integer.parseInt(nomI[w][2]);
            nomI[w][3]=Integer.toString(Math.abs((iarc - iact)));
            vaux[w]=(iarc-iact);
        }

        Arrays.sort(vaux);
        int varax;

        String [] nom= new String[4];
        List<String> ubicaciones= new ArrayList<String>();

        for(int ca=0; ca<4;ca++ ){
            varax=vaux[ca];
            String val=Integer.toString(varax);
            for(int k=0;k<nomI.length;k++){
                if(val.equals(nomI[k][3])){
                    String [] nu=nomI[k][0].split("_");
                    ubicaciones.add(nu[0]);
                    k=nomI.length;
                }
            }

        }

        int mayFrec=0;
        Set<String> quipu = new HashSet<String>(ubicaciones);
        for (String key : quipu) {
            if (Collections.frequency(ubicaciones, key)>mayFrec)
            {
                mayFrec=Collections.frequency(ubicaciones, key);
                ubicacion=key;
            }
        }
        if(mayFrec==1){
            ubicacion=ubicaciones.get(0);
        }

        if(ubicaciones.size()==0){
            Texto.setText("No encontro ubicacion");
        }else{
            Texto.setText(ubicacion);
        }
        wifi.startScan();

    }

    public void goHome(View v){
        Intent act = new Intent(this, MenuPrincipal.class);
        startActivity(act);
    }
}
