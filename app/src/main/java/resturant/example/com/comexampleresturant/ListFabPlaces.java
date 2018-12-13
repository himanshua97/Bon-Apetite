package resturant.example.com.comexampleresturant;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ListFabPlaces extends AppCompatActivity {
    Adapter1 adapter1;
    ArrayList<String> lstItems = new ArrayList<>();
    ArrayList<String> lstRefrence = new ArrayList<>();

    ListView listView;
    ProgressDialog dialog;
    String userid;
    String latitude, longitude;
Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_fab_places);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        userid = getIntent().getStringExtra("userid");
        listView = findViewById(R.id.list_item);
        context=this;
        adapter1 = new Adapter1(this, R.layout.custum_list, lstItems);
        listView.setAdapter(adapter1);
        userid = getIntent().getStringExtra("userid");
        latitude = getIntent().getStringExtra("LAT");
        longitude = getIntent().getStringExtra("LON");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog1, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                dialog = ProgressDialog.show(context,
                                        "Please Wait ...",
                                        "Deleting Item..", true, false);
                                RequestParams params = new RequestParams();
                                params.add("userid", userid);
                                params.add("reference", lstRefrence.get(position));
                                AsyncHttpClient client = new AsyncHttpClient();
                                // Don't forget to change the IP address to
                                // your LAN address. Port no as
                                // well.

                                client.post(Constant.url_delete_fab, params, new AsyncHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                        try {
                                            dialog.dismiss();

                                            String str = new String(responseBody, "UTF-8");
                                            JSONObject jsonArray = new JSONObject(str);
                                            Toast.makeText(ListFabPlaces.this, jsonArray.getString("msg"), Toast.LENGTH_SHORT).show();
                                            lstItems.remove(position);
                                            lstRefrence.remove(position);
                                            if (lstItems.size() > 0) {
                                                adapter1.notifyDataSetChanged();
                                            }

                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                        try {
                                            dialog.dismiss();
                                            String str = new String(responseBody, "UTF-8");


                                            Toast.makeText(getBaseContext(), str, Toast.LENGTH_SHORT).show();

                                        } catch (UnsupportedEncodingException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                Intent in = new Intent(getApplicationContext(),
                                        SinglePlaceActivity.class);
                                in.putExtra("LAT", latitude);
                                in.putExtra("LON", longitude);
                                in.putExtra("userid", userid);
                                // Sending place refrence id to single place activity
                                // place refrence id used to get "Place full details"
                                in.putExtra("reference", lstRefrence.get(position));
                                startActivity(in);

                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("What do you want?").setPositiveButton("Delete!",
                        dialogClickListener)
                        .setNegativeButton("View!", dialogClickListener).show();

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        dialog = ProgressDialog.show(this,
                "Please Wait ...",
                "Getting Details", true, false);
        RequestParams params = new RequestParams();
        params.add("userid", userid);

        AsyncHttpClient client = new AsyncHttpClient();
        // Don't forget to change the IP address to
        // your LAN address. Port no as
        // well.

        client.post(Constant.url_fab_list, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    dialog.dismiss();
                    lstItems.clear();
                    lstRefrence.clear();
                    String str = new String(responseBody, "UTF-8");
                    JSONArray jsonArray = new JSONArray(str);
                    int count = 0;
                    while (count < jsonArray.length()) {
                        lstItems.add(jsonArray.getJSONObject(count).getString("name"));
                        lstRefrence.add(jsonArray.getJSONObject(count).getString("reference"));
                        count++;
                    }

                    if (lstItems.size() > 0) {
                        adapter1.notifyDataSetChanged();
                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                try {
                    dialog.dismiss();
                    String str = new String(responseBody, "UTF-8");


                    Toast.makeText(getBaseContext(), str, Toast.LENGTH_SHORT).show();

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });


    }

}
