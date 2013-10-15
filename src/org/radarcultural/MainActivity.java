package org.radarcultural;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

public class MainActivity extends FragmentActivity {
    private GoogleMap gMap;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setUpMapIfNeeded();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void setUpMapIfNeeded() {
		if (gMap != null) {
			return;
		}
		gMap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		if (gMap == null) {
			return;
		}
		// Initialize map options. For example:
//		 mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		 gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
	}

}
