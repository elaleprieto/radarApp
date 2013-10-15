package org.radarcultural;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MainActivity extends FragmentActivity implements LocationListener {
	private static final long MIN_TIME_BW_UPDATES = 0;
	private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
	private GoogleMap gMap;
	private boolean canGetLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setUpMapIfNeeded();
		System.out.println(getLocation());
//		gMap.animateCamera(CameraUpdateFactory
//				.newCameraPosition(new CameraPosition(new LatLng(9.491327,
//						76.571404), 10, 30, 0)));
		getCurrentLocation();

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
		// mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
	}

	private void getCurrentLocation() {

		double[] d = getLocation();
		double lat = d[0];
		double lng = d[1];

		gMap.addMarker(new MarkerOptions()
				.position(new LatLng(lat, lng))
				.title("Current Location")
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)));

		gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
				lat, lng), 16));
	}

	public double[] getLocation() {
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		List<String> providers = lm.getProviders(true);

		Location l = null;
		for (int i = 0; i < providers.size(); i++) {
			l = lm.getLastKnownLocation(providers.get(i));
			if (l != null)
				break;
		}
		double[] gps = new double[2];

		if (l != null) {
			gps[0] = l.getLatitude();
			gps[1] = l.getLongitude();
		}
		return gps;
	}

	// private Location getLocation() {
	// Location location = null;
	//
	// try {
	// Context mContext = null;
	// LocationManager locationManager = (LocationManager) mContext
	// .getSystemService(LOCATION_SERVICE);
	//
	// // getting GPS status
	// boolean isGPSEnabled = locationManager
	// .isProviderEnabled(LocationManager.GPS_PROVIDER);
	//
	// System.out.println(isGPSEnabled);
	//
	// // getting network status
	// boolean isNetworkEnabled = locationManager
	// .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	//
	// if (!isGPSEnabled && !isNetworkEnabled) {
	// // no network provider is enabled
	// } else {
	// this.canGetLocation = true;
	// double latitude;
	// double longitude;
	// if (isNetworkEnabled) {
	// locationManager.requestLocationUpdates(
	// LocationManager.NETWORK_PROVIDER,
	// MIN_TIME_BW_UPDATES,
	// MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
	// Log.d("Network", "Network Enabled");
	// if (locationManager != null) {
	// location = locationManager
	// .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	// if (location != null) {
	// latitude = location.getLatitude();
	// longitude = location.getLongitude();
	// }
	// }
	// }
	// // if GPS Enabled get lat/long using GPS Services
	// if (isGPSEnabled) {
	// if (location == null) {
	// locationManager.requestLocationUpdates(
	// LocationManager.GPS_PROVIDER,
	// MIN_TIME_BW_UPDATES,
	// MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
	// Log.d("GPS", "GPS Enabled");
	// if (locationManager != null) {
	// location = locationManager
	// .getLastKnownLocation(LocationManager.GPS_PROVIDER);
	// if (location != null) {
	// latitude = location.getLatitude();
	// longitude = location.getLongitude();
	// }
	// }
	// }
	// }
	// }
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// return location;
	// }

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}

	// protected void updateLocation(Location location) {
	// MapView mapView = (MapView) findViewById(R.id.map);
	// MapController mapController = mapView.getController();
	//
	// GeoPoint point = new GeoPoint((int) (location.getLatitude() * 1E6),
	// (int) (location.getLongitude() * 1E6));
	//
	// mapController.animateTo(point);
	// mapController.setZoom(15);
	//
	// Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
	//
	// try {
	// List<Address> addresses = geoCoder.getFromLocation(
	// point.getLatitudeE6() / 1E6,
	// point.getLongitudeE6() / 1E6, 1);
	// String address = "";
	// if (addresses.size() > 0) {
	// for (int i = 0; i < addresses.get(0)
	// .getMaxAddressLineIndex(); i++)
	// address += addresses.get(0).getAddressLine(i) + "\n";
	// }
	// Toast.makeText(this, address, Toast.LENGTH_SHORT).show();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	//
	// List<Overlay> mapOverlays = mapView.getOverlays();
	// MyOverlay marker = new MyOverlay(point);
	// mapOverlays.add(marker);
	// mapView.invalidate();
	// }

	// class MyOverlay extends Overlay {
	// com.google.android.maps.GeoPoint point;
	//
	// /* El constructor recibe el punto donde se dibujará el marker */
	// public MyOverlay(GeoPoint point) {
	// super();
	// this.point = point;
	// }
	//
	// public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
	// long when) {
	// super.draw(canvas, mapView, shadow);
	//
	// // se traduce el punto geo localizado a un punto en la pantalla
	// Point scrnPoint = new Point();
	// mapView.getProjection().toPixels(this.point, scrnPoint);
	//
	// // se construye un bitmap a partir de la imagen
	// Bitmap marker = BitmapFactory.decodeResource(getResources(),
	// R.drawable.ic_launcher);
	//
	// // se dibuja la imagen del marker
	// canvas.drawBitmap(marker, scrnPoint.x - marker.getWidth() / 2,
	// scrnPoint.y - marker.getHeight() / 2, null);
	//
	// return true;
	// }
	//
	// }

}
