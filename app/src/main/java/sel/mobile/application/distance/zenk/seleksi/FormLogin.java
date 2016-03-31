package sel.mobile.application.distance.zenk.seleksi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by ZenK on 3/22/2016.
 */
public class FormLogin extends Activity {
    EditText username;
    EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
    }

    public void reg(View view){
        Intent i = new Intent(this,FormRegister.class);
        startActivity(i);
        overridePendingTransition(R.animator.left_in, R.animator.left_out);
    }
    public void log(View view){
        new ValidasiLogin(username.getText().toString(),password.getText().toString()).execute();
    }
    private class ValidasiLogin extends AsyncTask<Void, Void, String> {

        private static final String url = "jdbc:mysql://85.10.205.173:3306/damtest";
        private static final String user = "muadz";
        private static final String pass = "daytona999";
        ProgressDialog pDialog;
        String item;
        String us,pas;
        int id=0;



        public ValidasiLogin(String us, String pas) {
            this.us = us;
            this.pas = pas;


        }

        protected void onPreExecute() {


            pDialog = new ProgressDialog(FormLogin.this);

            pDialog.setMessage("Connecting");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(Void... params) {
            String data = "NO";


            try {

                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(url, user, pass);
                Statement st = con.createStatement();

                ResultSet rs = st.executeQuery("select * from register where reg_username='"+us+"' and reg_password='"+pas+"'");
                while (rs.next()) {
                    id = rs.getInt(1);
                    data = "YES";
                }

                rs.close();
                con.close();
                st.close();

            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            return data;

        }

        protected void onPostExecute(String dta) {

            pDialog.dismiss();
            if(dta.equalsIgnoreCase("YES")) {
                Intent i = new Intent(FormLogin.this, tabhos.class);
                i.putExtra("iduser",id);
                startActivity(i);
                overridePendingTransition(R.animator.left_in, R.animator.left_out);
                finish();
            }else{
                Toast.makeText(FormLogin.this,"Incorrect username or password",Toast.LENGTH_LONG).show();
                username.setText("");
                password.setText("");
            }
        }
    }


}
