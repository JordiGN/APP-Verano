package com.example.shates.appveranov2;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Environment;
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
import android.widget.Toast;

import junit.framework.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;


//public class Test_set extends ActionBarActivity {
public class Test_set extends Activity{
    ListView lv;
    String[][] nomI;
    WifiManager wifi;
    String[] wifis;
    ArrayList<String> wifistemp=new ArrayList<String>();
    WifiScanReceiver wifiReciever;
    int cont=1;
    String ub;
    int j=0;
    int tam1,tam2;
    int suma=0;int s=0;
    ArrayList<String> wf2= new ArrayList<String>();
    Context v =Test_set.this;
    View vis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_set);
        lv = (ListView) findViewById(R.id.listView2);
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

            //Toast.makeText(getBaseContext(), "El contar esta en "+contar +" !!", Toast.LENGTH_SHORT).show();
            wifis = new String[wifiScanList.size()];

            for (int i = 0; i < wifiScanList.size(); i++)
            {
                wifis[i] = ((wifiScanList.get(i).BSSID).toString() + "," + (wifiScanList.get(i).level));
                //Toast.makeText(getBaseContext(), "en wifi1", Toast.LENGTH_SHORT).show();
            }
            //Mostrar la lista
            lv.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, wifis));
            //Manda leer otro archivo
            //olarch(vis);
        }

    }

    public void olarch(View view){
        String path = Environment.getExternalStorageDirectory().toString()+"/Instancias";//Obtiene los archivos de la carpeta

        File f = new File(path);//Vector para guardar los archivos
        File file[] = f.listFiles();

        nomI= new String[file.length][2];//Vector para guardar el nombre de los archivos

        for (int i=0; i < file.length; i++)//Guardar los nombres en un vector/matriz
        {
            nomI[i][0]=file[i].getName();
            nomI[i][1]="0";
            //nomI[i][2]="0";
            //nomI[i][3]="0";
        }

        //Leer el archivo
        try
        {
            //int conta=0;
            for (int na=0;na<nomI.length;na++){//Ciclo para acceder a la lista de archivos
                File ff = new File(path, nomI[na][0].toString()); //Asignar archivo de ubicacin
                //Toast.makeText(getBaseContext(), "Direccion"+ff, Toast.LENGTH_LONG).show();

                BufferedReader fin = new BufferedReader(new InputStreamReader(new FileInputStream(ff))); //Para leer el archivo

                String linea;//Variable para guarda la lista del archivo


                while((linea=fin.readLine())!=null){

                    //Toast.makeText(getBaseContext(), "Linea"+linea, Toast.LENGTH_LONG).show();
                    wf2.add(linea);// Agregar a un Array list los valores del archivo
                    String [] l=linea.split(",");
                    int intes=Integer.parseInt((l[1]));
                    s=s+(intes*intes);

                }
                nomI[na][1]=Integer.toString(s/wf2.size());

                lv.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, wf2));
                for(int x=0; x<wifis.length;x++){
                    String t1=wifis[x];
                    String [] ttemp=t1.split(",");
                    String mac1=ttemp[0];
                    int inten1=Integer.parseInt(ttemp[1]);
                    for(String t2: wf2){
                        String [] ttemp2=t2.split(",");
                        String mac2=ttemp2[0];
                        int inten2=Integer.parseInt(ttemp2[1]);
                        //s=s+inten2;
                        if(mac1.equals(mac2)){
                            int it=(inten2-inten1);
                            it=it*it;
                            suma=suma+it;
                            String ln=mac1+","+it;
                            wifistemp.add(ln);
                        }
                    }

                }
                int promedio=suma/wifistemp.size();
                //nomI[na][2]=Integer.toString(promedio);

                wf2.clear();
                wifistemp.clear();

                fin.close();
                //conta++;
                //Toast.makeText(getBaseContext(), ff+"-- "+conta +"!!", Toast.LENGTH_LONG).show();
                //dif(vis);
            }
        }
        catch (Exception e)
        {
            Toast.makeText(getBaseContext(), "Error al leer fichero desde memoria",Toast.LENGTH_LONG).show();
        }

    }
    public void dif(View view){
        int num=0;
        for(int w=0; w<nomI.length;w++){
            int iarc=Integer.parseInt(nomI[w][1]);
            int iact=Integer.parseInt(nomI[w][2]);
            nomI[w][3]=Integer.toString((iarc-iact));
        }
        /*for(int w=0;w<nomI.length;w++){
            num=Integer.parseInt(nomI[w][3]);


        }*/

    }

    public void goHome(View v){
        Intent act = new Intent(this, MenuPrincipal.class);
        startActivity(act);
    }
}
