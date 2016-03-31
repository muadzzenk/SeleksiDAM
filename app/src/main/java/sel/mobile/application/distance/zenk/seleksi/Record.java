package sel.mobile.application.distance.zenk.seleksi;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import entitas.list;

/**
 * Created by ZenK on 3/26/2016.
 */
public class Record extends Activity {
    ArrayList<list> rec = new ArrayList<list>();
    SwipeRefreshLayout SwipeRefresh;
    ProgressBar pb;
    GridView grid_record;
    int id=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getInt("iduser");
        }
        grid_record = (GridView)findViewById(R.id.gridView);
        SwipeRefresh = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        pb = (ProgressBar)findViewById(R.id.progressBar);

        new getRecord().execute();

    }

    private class getRecord extends AsyncTask<Void, Void, ArrayList<list>> {

        private static final String url = "jdbc:mysql://85.10.205.173:3306/damtest";
        private static final String user = "muadz";
        private static final String pass = "daytona999";
        

        protected void onPreExecute() {

        }

        protected ArrayList<list> doInBackground(Void... params) {
            rec.clear();
            try {

                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(url, user, pass);
                Statement st = con.createStatement();

                ResultSet rs = st.executeQuery("select * from record where id_user="+id+"");
                while (rs.next()) {
                    list data = new list();
                    data.setDatetime(rs.getString(6));
                    data.setDistance(rs.getDouble(5));
                    data.setLokasi_tujuan(rs.getString(4));
                    data.setLokasi(rs.getString(3));

                    rec.add(data);

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


            return rec;

        }

        protected void onPostExecute(ArrayList<list> dta) {
            SwipeRefresh.setRefreshing(false);
            pb.setVisibility(View.GONE);
            ListRecord adapter=new ListRecord(Record.this,dta);
            grid_record.setAdapter(adapter);
            grid_record.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {


                }
            });
            grid_record.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    boolean enable = true;
                    //if (grid_record != null && grid_record.getChildCount() > 0) {
                        // check if the first item of the list is visible
                      //  boolean firstItemVisible = grid_record.getFirstVisiblePosition() == 0;
                        // check if the top of the first item is visible
                       // boolean topOfFirstItemVisible = grid_record.getChildAt(0).getTop() == 0;
                        // enabling / disabling refresh layout
                       // enable = firstItemVisible && topOfFirstItemVisible;
                    //}
                    SwipeRefresh.setEnabled(enable);
                }
            });

            SwipeRefresh.setColorSchemeResources(R.color.wallet_link_text_light, R.color.wallet_holo_blue_light, R.color.wallet_dim_foreground_holo_dark);
            SwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {

                    new getRecord().execute();

                }
            });


        }
    }

}





