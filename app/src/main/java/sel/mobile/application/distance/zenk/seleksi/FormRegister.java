package sel.mobile.application.distance.zenk.seleksi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Created by ZenK on 3/22/2016.
 */
public class FormRegister extends Activity {


    EditText edtname;
    EditText edtemail;
    EditText edtnohp;
    EditText edtusername;
    EditText edtpassword;
    EditText edtconfirmpassword;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        edtname = (EditText) findViewById(R.id.edt_nama);
        edtemail = (EditText) findViewById(R.id.edt_email);
        edtnohp = (EditText) findViewById(R.id.edt_nohp);
        edtusername = (EditText) findViewById(R.id.edt_username);
        edtpassword = (EditText) findViewById(R.id.edt_password);
        edtconfirmpassword = (EditText) findViewById(R.id.edt_confirmpassword);


    }

    public void reg(View view){
        String Sname= edtname.getText().toString();
        String Semail= edtemail.getText().toString();
        String Snohp= edtnohp.getText().toString();
        String Susername=edtusername.getText().toString();
        String Spassword=edtpassword.getText().toString();
        String Sconfirm=edtconfirmpassword.getText().toString();
        context = this;
        if(validasiData(Sname,Semail,Snohp,Susername,Spassword,Sconfirm)){
            new register(Sname,Semail,Snohp,Susername,Spassword,context).execute();
        }



    }

    private boolean validasiData(String Sname,String Semail,String Snohp,String Susername,String Spassword,String SConfirmpassword) {
        boolean result = true;


        if (!ValidasiName(Sname)) {
            edtname.setError("Invalid Name min : 5 character");
            return false;
        }

        if (!ValidasiEmail(Semail)) {
            edtemail.setError("Invalid Email");
            return false;
        }

        if (!ValidasiNohp(Snohp)) {
           edtnohp.setError("Invalid PhoneNumber");
            return false;
        }
        if (!Validasiusername(Susername)) {
            edtusername.setError("Invalid Username min : 5 character");
            return false;
        }
        if (!Validasipassword(Spassword)) {
            edtpassword.setError("Invalid Password min : 8 character");
            return false;
        }
        if (!Spassword.equalsIgnoreCase(SConfirmpassword)) {
            edtconfirmpassword.setError("Invalid Password");
            return false;
        }


        return result;
    }
    private boolean ValidasiName(String name) {
        if (name != null && name.length() > 4) {
            return true;
        }
        return false;
    }
    private boolean ValidasiEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    private boolean ValidasiNohp(String nohp) {
        if (nohp != null && nohp.length() > 9) {
            String hp = "^[0-9]+$";

            Pattern pattern = Pattern.compile(hp);
            Matcher matcher = pattern.matcher(nohp);
            return matcher.matches();

        }
        return false;
    }
    private boolean Validasiusername(String user) {
        if (user != null && user.length() > 4) {
            return true;
        }
        return false;
    }
    private boolean Validasipassword(String pass) {
        if (pass != null && pass.length() > 7) {
            return true;
        }
        return false;
    }
    public class register extends AsyncTask<Void, Void, String> {
        private static final String url = "jdbc:mysql://85.10.205.173:3306/damtest";
        private static final String user = "muadz";
        private static final String pass = "daytona999";
        Context context;
        ProgressDialog pDialog;
        Statement st;
        ResultSet rs;
        int hasil = 0;
        String edt_name;
        String edt_email;
        String edt_nohp;
        String edt_username;
        String edt_password;
        FormRegister fr = new FormRegister();

        public register(String name, String email, String nohp, String username, String password, Context cnt) {
            this.edt_name = name;
            this.edt_email = email;
            this.edt_nohp = nohp;
            this.edt_username = username;
            this.edt_password = password;
            this.context = cnt;
        }

        protected void onPreExecute() {

            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Registering");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(Void... params) {

            try {

                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(url, user, pass);
                st = con.createStatement();
                String sql2 = "insert into register (reg_name,reg_email,reg_nohp,reg_username,reg_password) " +
                        "values('" + edt_name + "','" + edt_email + "','" + edt_nohp + "','" + edt_username + "','" + edt_password + "')";
                hasil = st.executeUpdate(sql2);
                st.close();
                con.close();


            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            return "finish";

        }

        protected void onPostExecute(String as) {

            pDialog.dismiss();
            if (hasil == 1) {
                Toast.makeText(context, "Register Succes", Toast.LENGTH_LONG).show();
                Intent i = new Intent(context, FormLogin.class);
                startActivity(i);
                overridePendingTransition(R.animator.right_in, R.animator.right_out);
                finish();
            } else {
                Toast.makeText(context, "Register Failed", Toast.LENGTH_LONG).show();
                Toast.makeText(context, "Check Your Connection", Toast.LENGTH_LONG).show();

            }

        }
    }

}
