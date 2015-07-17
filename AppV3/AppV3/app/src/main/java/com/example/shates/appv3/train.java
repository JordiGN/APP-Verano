package com.example.shates.appv3;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


public class train extends Activity {
    private RadioGroup radioGroup;
    CharSequence texto=" ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train);
        /* Initialize Radio Group and attach click handler */
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.clearCheck();

        /* Attach CheckedChangeListener to radio group */
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {
                    texto=rb.getText();
                    //Toast.makeText(train.this, rb.getText(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_train, menu);
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

    public void onClear(View v) {
        /* Clears all selected radio buttons to default */
        radioGroup.clearCheck();
    }

    public void onSubmit(View v) {
        Intent i=new Intent(this, train.class);
        if(texto.equals("Gestos")){
            i = new Intent(this, Gestos.class);
        }
        if(texto.equals("Ubicacion")){
            i = new Intent(this, ubicacion.class);

        }
        if(texto.equals(" ")){
            Toast.makeText(train.this, "No seleccionaste ninguna opcion", Toast.LENGTH_SHORT).show();
        }else{
            startActivity(i);
        }


        /*RadioButton rb = (RadioButton) radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
        Toast.makeText(train.this, rb.getText(), Toast.LENGTH_SHORT).show();*/
    }
    public void inicio(View v) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
