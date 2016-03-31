package sel.mobile.application.distance.zenk.seleksi;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZenK on 3/23/2016.
 */
public class Map extends FragmentActivity implements LocationListener {

    GoogleMap googleMap;
    Context context;
    EditText searchtext;
    private ArrayAdapter<String> listAdapter;
    private ArrayAdapter<String> listAdapterempty;
    ListView itemList;
    MarkerOptions marker;
    LatLng latLng_tujuan;
    LatLng latLng_mylocation;
    TextView from,to,dis;

    ArrayList<LatLng> points;
    int id=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Bundle extras = getIntent().getExtras();
        id = extras.getInt("iduser");
        points = new ArrayList<LatLng>();
        searchtext = (EditText)findViewById(R.id.editText);

        from=(TextView)findViewById(R.id.from);
        to=(TextView)findViewById(R.id.to);
        dis=(TextView)findViewById(R.id.dis);

        //show error dialog if GoolglePlayServices not available
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }

        MapFragment supportMapFragment =
                (MapFragment) getFragmentManager().findFragmentById(R.id.maps);
        googleMap = supportMapFragment.getMap();

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);

        // check permission
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return  ;
        }

        Location location = locationManager.getLastKnownLocation(bestProvider);
        if (location != null) {
            mylocation(location);
        }
        locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);





    }


    @Override
    public void onLocationChanged(Location location) {



    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }

    public void search(View view){
        String location = searchtext.getText().toString();
        if(location.equalsIgnoreCase("")|| location.equalsIgnoreCase(null)) {
            Toast.makeText(this, "Unknown Location", Toast.LENGTH_LONG).show();
        }else {
            googleMap.clear();
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String bestProvider = locationManager.getBestProvider(criteria, true);

            // check permission
            if (Build.VERSION.SDK_INT >= 23 &&
                    ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            Location loc = locationManager.getLastKnownLocation(bestProvider);
            if (loc != null) {
                mylocation(loc);
            }
            locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);


            List<Address> addressList = null;


            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }

            if (addressList.size() > 0) {
                Address address = addressList.get(0);
                latLng_tujuan = new LatLng(address.getLatitude(), address.getLongitude());
                String addresline = "";
                addresline = address.getAddressLine(0);

                String kota = " ";
                kota = address.getLocality();

                String area = " ";
                area = address.getAdminArea();

                String negara = address.getCountryName();


                to.setText(addresline + " " + kota + " " + area + " " + negara);
                marker = new MarkerOptions().position(
                        new LatLng(address.getLatitude(), address.getLongitude()))
                        .title(" Destination Location ");
                marker.icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

                googleMap.addMarker(marker);
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng_tujuan));
                //googleMap.addMarker(New MarkerOptions().posisition(latLng).title("Tujuan"));

                //}
            } else {
                Toast.makeText(context, "Unknown Location", Toast.LENGTH_LONG).show();
            }
        }
    }
    // perhitungan jarak eucledian
    public void calculate(View view){

        if(to.getText().toString().equals(null) || to.getText().toString().equals("")) {
            AlertDialog alertDialog = new AlertDialog.Builder(
                    this).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage("You must search destination location");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Write your code here to execute after dialog closed


                }
            });
            alertDialog.show();
        }else {
            // derajat bumi
            final double decDegree= 111.319;

            double getEliminteLatitude = Math.pow((latLng_mylocation.latitude - latLng_tujuan.latitude),2);
            double getEliminteLongitude = Math.pow((latLng_mylocation.longitude - latLng_tujuan.longitude), 2);

            double jarak = Math.sqrt(getEliminteLatitude+getEliminteLongitude)*decDegree;
            double save = Math.pow(10, 3);
            double convjarak = (double) Math.round(jarak*save)/save;
            //Toast.makeText(Map.this,""+jarak,Toast.LENGTH_LONG).show();
            new Record(id, from.getText().toString(), to.getText().toString(),convjarak, this).execute();
        }
    }
    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }
    public void mylocation(Location location){
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        latLng_mylocation = new LatLng(latitude, longitude);
        Geocoder geocoder = new Geocoder(this);

        List<Address> addresses  = null;
        try {
            addresses = geocoder.getFromLocation(latitude,longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String addresline = "";
        addresline = addresses.get(0).getAddressLine(0);

        String kota = " ";
        kota = addresses.get(0).getLocality();

        String area = " ";
        area= addresses.get(0).getAdminArea();

        String negara = addresses.get(0).getCountryName();


        from.setText(addresline+" "+kota + " " + area + " " + negara);

        // set type map
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // cek permission
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return  ;
        }

        googleMap.setMyLocationEnabled(true);
        // Enable  my location button
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

        // Enable Compass icon
        googleMap.getUiSettings().setCompassEnabled(true);

        // Enable / Disable Rotate gesture
        googleMap.getUiSettings().setRotateGesturesEnabled(true);

        // Enable / Disable zooming functionality
        googleMap.getUiSettings().setZoomGesturesEnabled(true);

        marker = new MarkerOptions().position(
                new LatLng(latitude, longitude))
                .title("My Location ");

        marker.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));

        googleMap.addMarker(marker);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng_mylocation));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));



    }

    public class Record extends AsyncTask<Void, Void, String> {
        private static final String url = "jdbc:mysql://85.10.205.173:3306/damtest";
        private static final String user = "muadz";
        private static final String pass = "daytona999";
        Context context;
        ProgressDialog pDialog;
        Statement st;
        ResultSet rs;
        int hasil = 0;
        int txt_iduser;
        String txt_loc;
        String txt_tujuan;
        double txt_distance;

        public Record(int iduser, String loc, String tujuan, double distance, Context cnt) {
            this.txt_iduser = iduser;
            this.txt_loc = loc;
            this.txt_tujuan = tujuan;
            this.txt_distance = distance;
            this.context = cnt;
        }

        protected void onPreExecute() {

            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Calculating");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(Void... params) {

            try {

                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(url, user, pass);
                st = con.createStatement();
                String sql2 = "insert into record(id_user,rec_location,rec_tujuan,rec_distance) values(" + txt_iduser + ",'" + txt_loc + "','" + txt_tujuan + "'," + txt_distance + ")";
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
                points.clear();
                Toast.makeText(context, "Succes Calculate", Toast.LENGTH_LONG).show();
                dis.setText("" + txt_distance + " km");
                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.color(Color.BLUE);
                polylineOptions.width(3);
                points.add(latLng_mylocation);
                points.add(latLng_tujuan);
                polylineOptions.addAll(points);
                googleMap.addPolyline(polylineOptions);


            } else {
                Toast.makeText(context, "Error Calculate", Toast.LENGTH_LONG).show();
                Toast.makeText(context, "Check your Connection", Toast.LENGTH_LONG).show();

            }

        }
    }

}
