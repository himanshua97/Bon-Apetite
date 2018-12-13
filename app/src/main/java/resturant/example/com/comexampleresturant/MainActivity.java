package resturant.example.com.comexampleresturant;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import resturant.example.com.comexampleresturant.notepad.Simple_NotepadActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SharedPreferences preferences;
    String userid;
    String name;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        userid = getIntent().getStringExtra("userid");
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        context = this;
        preferences = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());

        Button btnMylocation = (Button) findViewById(R.id.btnMyLocation);
       // tvLocation = (TextView) findViewById(R.id.tvLocation);

        btnMylocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getBaseContext(), MyLocation.class);

                intent.putExtra("LAT", latitude);
                intent.putExtra("LON", longitude);
                startActivity(intent);
            }
        });
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            //googleMap.setMyLocationEnabled(true);
            //googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Things To Do", Snackbar.LENGTH_LONG)
                        .setAction("Click Here", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getBaseContext(), Simple_NotepadActivity.class);
                                intent.putExtra("LAT", latitude);
                                intent.putExtra("LON", longitude);
                                intent.putExtra("userid", userid);
                                startActivity(intent);
                            }
                        }).show();
            }
        });

        View hView = navigationView.getHeaderView(0);
        // ImageView nav_user = hView.findViewById(R.id.imageView);
        TextView txtEmail = hView.findViewById(R.id.txtEmail);
        TextView txtName = hView.findViewById(R.id.txtName);
        txtEmail.setText(email);
        txtName.setText(name);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_nearby_resturant) {
            Intent intent = new Intent(this, EnterRadiusActivity.class);
            intent.putExtra("LAT", latitude);
            intent.putExtra("LON", longitude);
            intent.putExtra("userid", userid);
            startActivity(intent);
        } else if (id == R.id.nav_fab_places) {
            Intent intent = new Intent(this, ListFabPlaces.class);
            intent.putExtra("LAT", latitude);
            intent.putExtra("LON", longitude);
            intent.putExtra("userid", userid);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(this, Simple_NotepadActivity.class);
            intent.putExtra("LAT", latitude);
            intent.putExtra("LON", longitude);
            intent.putExtra("userid", userid);
            startActivity(intent);
        } else if (id == R.id.nav_aboutus) {
            Intent intent = new Intent(this, Aboutus.class);
            intent.putExtra("LAT", latitude);
            intent.putExtra("LON", longitude);
            intent.putExtra("userid", userid);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void refresh(View v) {
        FetchLocation fd = new FetchLocation();
        fd.execute();


    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        FetchLocation fd = new FetchLocation();
        fd.execute();

        GetCurrentAddress currentadd = new GetCurrentAddress();
        currentadd.execute();

    }

    public void enterResturant(View view) {
        Intent intent = new Intent(this, EnterRadiusActivity.class);
        intent.putExtra("LAT", latitude);
        intent.putExtra("LON", longitude);
        intent.putExtra("userid", userid);
        startActivity(intent);
    }

    private Context context = null;
    //private ProgressDialog dialog = null;
    String latitude = "0";
    String longitude = "0";
    TextView tvLocation = null;


    public String getAddress(Context ctx, double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(ctx, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude,
                    longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);

                String locality = address.getLocality();
                String region_code = address.getCountryCode();
                result.append(""
                        + address.getAddressLine(0) + " "
                        //	+ address.getAddressLine(1) + " "
                        + address.getPostalCode() + " ");
                result.append(locality + " ");
                result.append(region_code);

            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        return result.toString();
    }

    String address1 = "Current Address: ";

    private class GetCurrentAddress extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            // this lat and log we can get from current location but here we
            // given hard coded
            address1 = "Current Address: ";
            System.out.println("lati=" + latitude + " longi=" + longitude);

            String address = getAddress(context, Double.parseDouble(latitude), Double.parseDouble(longitude));
            System.out.println("address=" + address);

            return address;
        }

        @Override
        protected void onPostExecute(String resultString) {
            // dialog.dismiss();
            address1 = address1 + resultString;
          //  tvLocation.setText(address1 + latlon);

        }
    }

    String latlon;

    LocationGetter location;

    class FetchLocation extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            location = new LocationGetter();
            runOnUiThread(new Runnable() {
                public void run() {
                    try {
                        latitude = Double.toString(location.getLocation(MainActivity.this).getLatitude());
                        longitude = Double.toString(location.getLocation(MainActivity.this).getLongitude());
                        GetCurrentAddress currentadd = new GetCurrentAddress();
                        currentadd.execute();
                    } catch (Exception e) {
                    }

                }
            });

			/*latitude= Double.toString(newlat());
            longitude= Double.toString(newlong());*/
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            runOnUiThread(new Runnable() {
                public void run() {
                    if (latitude == null || longitude == null) {

                    } else {
                        Toast.makeText(getBaseContext(), "Latitude,Longitude " + latitude + "," + longitude, Toast.LENGTH_SHORT).show();
                        latlon = "\nCurrent Location: " + latitude + "," + longitude;
                      //  tvLocation.setText(address1 + latlon);
                    }
                }
            });

        }
    }

}
