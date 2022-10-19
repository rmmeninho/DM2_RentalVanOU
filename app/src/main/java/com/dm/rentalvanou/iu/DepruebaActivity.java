package com.dm.rentalvanou.iu;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;

public class DepruebaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deprueba);

        Button btn_Volve = (Button) this.findViewById(R.id.button);
        btn_Volve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DepruebaActivity.this, RegistroActivity.class));
            }
        });
    }
    // Ejemplos en clase sobre menus
     // menu contextual


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        this.getMenuInflater().inflate( R.menu.main_menu, menu );
        return true;
    }

    @Override
     public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
     super.onCreateContextMenu(menu, v, menuInfo);
     getMenuInflater().inflate(R.menu.main_menu, menu);
     }

   /*  @Override
     public boolean onOptionsItemSelected(MenuItem item) {

     boolean toret = false;
     switch(item.getItemId()){
     case R.id.te:

     default: super.onOptionsItemSelected(item);

     }
     }
*/

}
