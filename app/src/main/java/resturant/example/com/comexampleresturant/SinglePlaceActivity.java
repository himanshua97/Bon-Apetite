package resturant.example.com.comexampleresturant;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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
import resturant.example.com.comexampleresturant.location.AlertDialogManager;
import resturant.example.com.comexampleresturant.location.ConnectionDetector;
import resturant.example.com.comexampleresturant.location.GooglePlaces;
import resturant.example.com.comexampleresturant.location.PlaceDetails;


public class SinglePlaceActivity extends Activity {
	// flag for Internet connection status
	Boolean isInternetPresent = false;

	// Connection detector class
	ConnectionDetector cd;
	
	// Alert Dialog Manager
	AlertDialogManager alert = new AlertDialogManager();

	// Google Places
	GooglePlaces googlePlaces;
	
	// Place Details
	PlaceDetails placeDetails;
	
	// Progress dialog
	ProgressDialog pDialog;
	
	// KEY Strings
	public static String KEY_REFERENCE = "reference"; // id of the place
	String phone_no="";
	String latitude="";
	String longitude="";
	String lat,lon;
	ArrayList<String>lstComments=new ArrayList<>();
	ArrayList<String>lstID=new ArrayList<>();
	ListView listView;
	Context context;
	String userid;
	Adapter1 adapter1;
	EditText txtCOmment;
	String reference;
	String cafeid;
	private String name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.single_place);		
		Intent i = getIntent();
		context=this;
		userid=getIntent().getStringExtra("userid");
		listView=findViewById(R.id.list);
		adapter1=new Adapter1(this,R.layout.custum_list,lstComments);
		listView.setAdapter(adapter1);
		latitude=getIntent().getStringExtra("LAT");
		longitude=getIntent().getStringExtra("LON");
		txtCOmment=findViewById(R.id.txtComment);
		// Place referece id
		 reference = i.getStringExtra(KEY_REFERENCE);
		
		// Calling a Async Background thread
		new LoadSinglePlaceDetails().execute(reference);
		Button btnCall=(Button)findViewById(R.id.btnCall);
		Button btnViewMap=(Button)findViewById(R.id.btnViewMap);
		btnCall.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (ContextCompat.checkSelfPermission(SinglePlaceActivity.this, Manifest.permission.CALL_PHONE) ==
						PackageManager.PERMISSION_GRANTED &&
						ContextCompat.checkSelfPermission(SinglePlaceActivity.this, Manifest.permission.INTERNET) ==
								PackageManager.PERMISSION_GRANTED) {
					//googleMap.setMyLocationEnabled(true);
					//googleMap.getUiSettings().setMyLocationButtonEnabled(true);
				} else {
					ActivityCompat.requestPermissions(SinglePlaceActivity.this, new String[]{
									Manifest.permission.CALL_PHONE,
									Manifest.permission.INTERNET},
							1);
				}
				Intent callIntent = new Intent(Intent.ACTION_CALL);  
				 callIntent.setData(Uri.parse("tel:"+phone_no));  
				 startActivity(callIntent); 
			}
		});
		btnViewMap.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getBaseContext(), Navigate.class);
				intent.putExtra("LAT", latitude);
				intent.putExtra("LON", longitude);
				intent.putExtra("tolat", lat);
				intent.putExtra("tolon", lon);
				startActivity(intent);	
			}
		});

	}
	public void addToFab(View view){
		dialog = ProgressDialog.show(context,
				"Please Wait ...",
				"Adding To Your Favourite places", true, false);
		RequestParams params = new RequestParams();
		params.add("userid", userid);
		params.add("resid", cafeid);
		params.add("name", name);
		params.add("reference", reference);

		AsyncHttpClient client = new AsyncHttpClient();
		// Don't forget to change the IP address to
		// your LAN address. Port no as
		// well.

		client.post(Constant.url_save_favorite, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				try {
					dialog.dismiss();
					String str = new String(responseBody, "UTF-8");
					JSONObject jsonObject=new JSONObject(str);
					Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();

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


					Toast.makeText(context, str, Toast.LENGTH_SHORT).show();

				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		});
	}
	public void getComments(){
		dialog = ProgressDialog.show(context,
				"Please Wait ...",
				"Getting feedbacks", true, false);
		RequestParams params = new RequestParams();
		params.add("userid", userid);
		params.add("resid", cafeid);

		AsyncHttpClient client = new AsyncHttpClient();
		// Don't forget to change the IP address to
		// your LAN address. Port no as
		// well.

		client.post(Constant.url_getcomments, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				try {
					dialog.dismiss();
					String str = new String(responseBody, "UTF-8");
					JSONArray jsonArray=new JSONArray(str);
					int count=0;
					while (count<jsonArray.length()){
						lstComments.add(jsonArray.getJSONObject(count).getString("comment")+"\n"+jsonArray.getJSONObject(count).getString("output"));
					count++;
					}

				if(lstComments.size()>0){
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


					Toast.makeText(context, str, Toast.LENGTH_SHORT).show();

				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		});

	}

	ProgressDialog dialog;
	public void saveReview(View v){
		String comment=txtCOmment.getText().toString();
		dialog = ProgressDialog.show(context,
				"Please Wait ...",
				"Saving  feedbacks", true, false);
		com.loopj.android.http.RequestParams params = new com.loopj.android.http.RequestParams();
		params.add("userid", userid);
		params.add("comment", comment);
		params.add("resid", cafeid);
		com.loopj.android.http.AsyncHttpClient client = new com.loopj.android.http.AsyncHttpClient();
		// Don't forget to change the IP address to
		// your LAN address. Port no as
		// well.

		client.post(Constant.postComment, params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				try {
					dialog.dismiss();
					String str = new String(responseBody, "UTF-8");
					JSONObject jsonObject = new JSONObject(str);

					Toast.makeText(context, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();

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


					Toast.makeText(context, str, Toast.LENGTH_SHORT).show();

				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		});

	}
	/**
	 * Background Async Task to Load Google places
	 * */
	class LoadSinglePlaceDetails extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(SinglePlaceActivity.this);
			pDialog.setMessage("Loading profile ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting Profile JSON
		 * */
		protected String doInBackground(String... args) {
			String reference = args[0];
			
			// creating Places class object
			googlePlaces = new GooglePlaces();

			// Check if used is connected to Internet
			try {
				placeDetails = googlePlaces.getPlaceDetails(reference);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
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
					if(placeDetails != null){
						String status = placeDetails.status;
						
						// check place deatils status
						// Check for all possible status
						if(status.equals("OK")){
							if (placeDetails.result != null) {
								cafeid=placeDetails.result.id;
								 name = placeDetails.result.name;
								String address = placeDetails.result.formatted_address;
								String phone = placeDetails.result.formatted_phone_number;
								String latitude = Double.toString(placeDetails.result.geometry.location.lat);
								String longitude = Double.toString(placeDetails.result.geometry.location.lng);
								
								Log.d("Place ", name + address + phone + latitude + longitude);
								phone_no=phone;//to set values for latitude & longitude. along with phone no. for future use.
								lat=latitude;
								lon=longitude;
								// Displaying all the details in the view
								// single_place.xml
								TextView lbl_name = (TextView) findViewById(R.id.name);
								TextView lbl_address = (TextView) findViewById(R.id.address);
								TextView lbl_phone = (TextView) findViewById(R.id.phone);
								TextView lbl_location = (TextView) findViewById(R.id.location);
								
								// Check for null data from google
								// Sometimes place details might missing
								name = name == null ? "Not present" : name; // if name is null display as "Not present"
								address = address == null ? "Not present" : address;
								phone = phone == null ? "Not present" : phone;
								latitude = latitude == null ? "Not present" : latitude;
								longitude = longitude == null ? "Not present" : longitude;
								
								lbl_name.setText(name);
								lbl_address.setText(address);
								lbl_phone.setText(Html.fromHtml("<b>Phone:</b> " + phone));
								lbl_location.setText(Html.fromHtml("<b>Latitude:</b> " + latitude + ", <b>Longitude:</b> " + longitude));
								getComments();
							}
						}
						else if(status.equals("ZERO_RESULTS")){
							alert.showAlertDialog(SinglePlaceActivity.this, "Near Places",
									"Sorry no place found.",
									false);
						}
						else if(status.equals("UNKNOWN_ERROR"))
						{
							alert.showAlertDialog(SinglePlaceActivity.this, "Places Error",
									"Sorry unknown error occured.",
									false);
						}
						else if(status.equals("OVER_QUERY_LIMIT"))
						{
							alert.showAlertDialog(SinglePlaceActivity.this, "Places Error",
									"Sorry query limit to google places is reached",
									false);
						}
						else if(status.equals("REQUEST_DENIED"))
						{
							alert.showAlertDialog(SinglePlaceActivity.this, "Places Error",
									"Sorry error occured. Request is denied",
									false);
						}
						else if(status.equals("INVALID_REQUEST"))
						{
							alert.showAlertDialog(SinglePlaceActivity.this, "Places Error",
									"Sorry error occured. Invalid Request",
									false);
						}
						else
						{
							alert.showAlertDialog(SinglePlaceActivity.this, "Places Error",
									"Sorry error occured.",
									false);
						}
					}else{
						alert.showAlertDialog(SinglePlaceActivity.this, "Places Error",
								"Sorry error occured.",
								false);
					}
					
					
				}
			});

		}

	}

}
