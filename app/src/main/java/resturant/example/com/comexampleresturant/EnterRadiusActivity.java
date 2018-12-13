package resturant.example.com.comexampleresturant;





import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EnterRadiusActivity extends Activity {

	String radius = "hiii";
	String latitude,longitude;
String userid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enter_radius);
		latitude=getIntent().getStringExtra("LAT");
		longitude=getIntent().getStringExtra("LON");
		Button btnRadius=(Button) findViewById(R.id.btnViewMap);
		userid=getIntent().getStringExtra("userid");
		System.out.println(radius);
		btnRadius.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				EditText et = (EditText) findViewById(R.id.etPoliceRadius);
				radius = et.getText().toString();
				Intent intent = new Intent(getBaseContext(), GetPlace.class);
				System.out.println(radius);
				intent.putExtra("LAT", latitude);
				intent.putExtra("userid", userid);
				intent.putExtra("LON", longitude);
				intent.putExtra("radius", radius);
				intent.putExtra("type", "cafe||resturant");
				startActivity(intent);
			}
		});
	
		
		// Show the Up button in the action bar
		// getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	

}
