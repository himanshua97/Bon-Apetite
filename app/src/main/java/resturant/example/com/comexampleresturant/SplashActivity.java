package resturant.example.com.comexampleresturant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;


public class SplashActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);
//		TextView imageView = (TextView) findViewById(R.id.tvtimer);
//		Animation animation = AnimationUtils.loadAnimation(
//				getApplicationContext(), R.anim.abc);
//		imageView.startAnimation(animation);

		Thread thread = new Thread() {
			public void run() {
				try {
					sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				SplashActivity.this.finish();
			//	Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://google.com"));
			/*	Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.putExtra(android.content.Intent.EXTRA_TEXT, "News for you!");
				startActivity(intent); */
				Intent i = new Intent(SplashActivity.this, LoginActivity.class);
				startActivity(i);

			}

		};

		thread.start();
	}
}