package sel.mobile.application.distance.zenk.seleksi;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

/**
 * Created by ZenK on 3/25/2016.
 */
public class tabhos extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
       TabHost TabHostWindow = (TabHost)findViewById(android.R.id.tabhost);

        TabHost.TabSpec TabMenu1 = TabHostWindow.newTabSpec("Map");
        TabHost.TabSpec TabMenu2 = TabHostWindow.newTabSpec("Record");
        int id=0;
        Bundle extras = getIntent().getExtras();
            id = extras.getInt("iduser");
        //Toast.makeText(this,""+id,Toast.LENGTH_LONG).show();

        TabMenu1.setIndicator("Map");

        Intent imap=new Intent(this, Map.class);
        imap.putExtra("iduser", id);
        TabMenu1.setContent(imap);

        TabMenu2.setIndicator("Record");

        Intent i=new Intent(this, Record.class);
        i.putExtra("iduser",id);
        TabMenu2.setContent(i);



        TabHostWindow.addTab(TabMenu1);
        TabHostWindow.addTab(TabMenu2);

    }
}
