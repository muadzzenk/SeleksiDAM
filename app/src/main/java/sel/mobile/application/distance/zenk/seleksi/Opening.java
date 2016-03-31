package sel.mobile.application.distance.zenk.seleksi;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;



public class Opening extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening);
        Handler handler;
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent i = new Intent(Opening.this,FormLogin.class);
                startActivity(i);
                overridePendingTransition(R.animator.bottom_in, R.animator.bottom_out);
                finish();
            }
        }, 3000);
    }

    public void login(View view){
        Intent i = new Intent(this,FormLogin.class);
        startActivity(i);
        overridePendingTransition(R.animator.bottom_in, R.animator.bottom_out);
    }
    public class transisi extends AsyncTask<Void, Void, String> {


        protected void onPreExecute() {

        }

        protected String doInBackground(Void... params) {




            return "transisi";
        }

        protected void onPostExecute(String as) {



        }
    }
}
