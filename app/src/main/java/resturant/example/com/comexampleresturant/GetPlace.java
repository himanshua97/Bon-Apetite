package resturant.example.com.comexampleresturant;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;



import java.util.ArrayList;
import java.util.HashMap;

import resturant.example.com.comexampleresturant.location.AlertDialogManager;
import resturant.example.com.comexampleresturant.location.ConnectionDetector;
import resturant.example.com.comexampleresturant.location.GPSTracker;
import resturant.example.com.comexampleresturant.location.GooglePlaces;
import resturant.example.com.comexampleresturant.location.Place;
import resturant.example.com.comexampleresturant.location.PlacesList;


public class GetPlace extends Activity {
	// flag for Internet connection status
		Boolean isInternetPresent = false;

		// Connection detector class
		ConnectionDetector cd;
		
		// Alert Dialog Manager
		AlertDialogManager alert = new AlertDialogManager();

		// Google Places
		GooglePlaces googlePlaces;

		// Places List
		PlacesList nearPlaces;

		// GPS Location
		GPSTracker gps;

		// Button
		Button btnShowOnMap;

		// Progress dialog
		ProgressDialog pDialog;
		
		// Places Listview
		ListView lv;
		
		// ListItems data
		ArrayList<HashMap<String, String>> placesListItems = new ArrayList<HashMap<String,String>>();
		String strRadius="";
		String types="";// Listing places only cafes, restaurants
		// KEY Strings
		public static String KEY_REFERENCE = "reference"; // id of the place
		public static String KEY_NAME = "name"; // name of the place
		public static String KEY_VICINITY = "vicinity"; // Place area name
		String latitude,longitude;
		EditText txtSearch;
		String userid;
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.adrian);
			Intent i=getIntent();
			userid=getIntent().getStringExtra("userid");
			latitude=getIntent().getStringExtra("LAT");
			longitude=getIntent().getStringExtra("LON");
			strRadius=i.getStringExtra("radius");
			types=i.getStringExtra("type");
			System.out.println("types="+types);
			txtSearch=(EditText) findViewById(R.id.txtSearch);
			cd = new ConnectionDetector(GetPlace.this);

			// Check if Internet present
			isInternetPresent = cd.isConnectingToInternet();
			if (!isInternetPresent) {
				// Internet Connection is not present
				pDialog=ProgressDialog.show(GetPlace.this, "Internet Connection Error",
						"Please connect to working Internet connection", false);

				// stop executing code by return
				return;
			}

			// creating GPS Class object
			gps = new GPSTracker(this);

			// check if GPS location can get
			if (gps.canGetLocation()) {
				Log.d("Your Location", "latitude:" + gps.getLatitude() + ", longitude: " + gps.getLongitude());
			} else {
				pDialog=ProgressDialog.show(GetPlace.this, "GPS Status",
						"Couldn't get location information. Please enable GPS",
						false);


				// stop executing code by return
				return;
			}

			// Getting listview
			lv = (ListView) findViewById(R.id.list);
			
			
			

			
			/**
			 * ListItem click event
			 * On selecting a listitem SinglePlaceActivity is launched
			 * */
			lv.setOnItemClickListener(new OnItemClickListener() {
	 
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view,
	                    int position, long id) {
	            	// getting values from selected ListItem
	                String reference = ((TextView) view.findViewById(R.id.reference)).getText().toString();
	                
	                // Starting new intent
	                Intent in = new Intent(getApplicationContext(),
	                        SinglePlaceActivity.class);
	                in.putExtra("LAT", latitude);
	                in.putExtra("LON", longitude);
	                in.putExtra("userid", userid);
	                // Sending place refrence id to single place activity
	                // place refrence id used to get "Place full details"
	                in.putExtra(KEY_REFERENCE, reference);
	                startActivity(in);
	            }
	        });
		}
		String keyword=null;
public  void search(View v){
			 keyword=txtSearch.getText().toString();

	// calling background Async task to load Google Places
	// After getting places from Google all the data is shown in listview
	new LoadPlaces().execute();


}
		/**
		 * Background Async Task to Load Google places
		 * */
		class LoadPlaces extends AsyncTask<String, String, String> {

			/**
			 * Before starting background thread Show Progress Dialog
			 * */
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				placesListItems.clear();
				pDialog=ProgressDialog.show(GetPlace.this, "Searching places",
						"please wait...",	false);
			}

			/**
			 * getting Places JSON
			 * */
			protected String doInBackground(String... args) {
				// creating Places class object
				googlePlaces = new GooglePlaces();
				
				try {
					// Separeate your place types by PIPE symbol "|"
					// If you want all types places make it as null
					// Check list of types supported by google
					// 
					
					
					// Radius in meters - increase this value if you don't find any places
					
					double radius = Double.parseDouble(strRadius); // 1000 meters 
					
					// get nearest places
					nearPlaces = googlePlaces.search(gps.getLatitude(),
							gps.getLongitude(), radius, types,keyword);




				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			/**
			 * After completing background task Dismiss the progress dialog
			 * and show the data in UI
			 * Always use runOnUiThread(new Runnable()) to update UI from background
			 * thread, otherwise you will get error
			 * **/
			protected void onPostExecute(String file_url) {
				// dismiss the dialog after getting all products
				pDialog.dismiss();
				// updating UI from Background Thread
				runOnUiThread(new Runnable() {
					public void run() {
						/**
						 * Updating parsed Places into LISTVIEW
						 * */
						// Get json response status
						String status = nearPlaces.status;
						
						// Check for all possible status
						if(status.equals("OK")){
							// Successfully got places details
							if (nearPlaces.results != null) {
								// loop through each place
								for (Place p : nearPlaces.results) {
									HashMap<String, String> map = new HashMap<String, String>();
									
									// Place reference won't display in listview - it will be hidden
									// Place reference is used to get "place full details"
									map.put(KEY_REFERENCE, p.reference);
									
									// Place name
									map.put(KEY_NAME, p.name);
									
									
									// adding HashMap to ArrayList
									placesListItems.add(map);
								}
								// list adapter
								ListAdapter adapter = new SimpleAdapter(GetPlace.this, placesListItems,
						                R.layout.list_item,
						                new String[] { KEY_REFERENCE, KEY_NAME}, new int[] {
						                        R.id.reference, R.id.name });
								
								// Adding data into listview
								lv.setAdapter(adapter);
							}
						}
						else
						{
							Toast.makeText(GetPlace.this,"no values found",Toast.LENGTH_SHORT).show();
						}
					}
				});

			}

		}

		

		

	}
