package org.radarcultural;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.google.android.gms.maps.model.VisibleRegionCreator;

public class MainActivity extends FragmentActivity implements LocationListener {
	private static final long MIN_TIME_BW_UPDATES = 0;
	private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 0;
	private GoogleMap gMap;
	private Location location = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setUpMapIfNeeded();
		getCurrentLocation();
		
		if (location != null) {
			updateMapItems();
		}
//		} else {
//			new ReadWeatherJSONFeedTask()
//					.execute("
//							http://www.radarcultural.com.ar/events/get?eventCategory=%5B%5D&eventInterval=1&neLat=-31.598035689038454&neLong=-60.63508608364259&swLat=-31.666729827847984&swLong=-60.763832116357435");
//							http://www.radarcultural.com.ar/events/get?eventCategory=%5B%5D&eventInterval=1&neLat=-31.616974940104598&neLong=-60.72560034692287&swLat=-31.654195071234096&swLong=-60.697447545826435
//		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.action_settings:
	            return true;
	        case R.id.action_update:
	        	updateMapItems();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
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
		
		
		gMap.setOnCameraChangeListener(new OnCameraChangeListener() {
			
            public void onCameraChange(CameraPosition cameraPosition) {
              updateMapItems();
            }

        });
	}

	public void updateMapItems(View view) {
		updateMapItems();
	}
	
	private void updateMapItems() {
		String url = "http://www.radarcultural.com.ar/events/get?";
		Integer eventInterval = 1;
		VisibleRegion visibleRegion = gMap.getProjection()
				.getVisibleRegion();

//		double neLat = visibleRegion.farLeft.latitude;
//		double neLong = visibleRegion.farLeft.longitude;
//		double swLat = visibleRegion.nearRight.latitude;
//		double swLong = visibleRegion.nearRight.longitude;
		double neLat = visibleRegion.latLngBounds.northeast.latitude;
		double neLong = visibleRegion.latLngBounds.northeast.longitude;
		double swLat = visibleRegion.latLngBounds.southwest.latitude;
		double swLong = visibleRegion.latLngBounds.southwest.longitude;

//		url += "eventCategory=%5B%5D";
		url += "eventCategory=[]";
		url += "&eventInterval=" + eventInterval;
		url += "&neLat=" + neLat;
		url += "&neLong=" + neLong;
		url += "&swLat=" + swLat;
		url += "&swLong=" + swLong;
		
		System.out.println(url);

		new ReadWeatherJSONFeedTask().execute(url);		
	}

	private void getCurrentLocation() {

		Location location = getLocation();

		if (location != null) {
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			LatLng position = new LatLng(latitude, longitude);
			Integer zoom = 14;

			gMap.addMarker(new MarkerOptions()
					.position(position)
					.title("Tu ubicación actual")
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.launcher)));

			gMap.animateCamera(CameraUpdateFactory
					.newLatLngZoom(position, zoom));
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	// public double[] getLocation() {
	// LocationManager lm = (LocationManager)
	// getSystemService(Context.LOCATION_SERVICE);
	// List<String> providers = lm.getProviders(true);
	//
	// Location l = null;
	// for (int i = 0; i < providers.size(); i++) {
	// l = lm.getLastKnownLocation(providers.get(i));
	// if (l != null)
	// break;
	// }
	// double[] gps = new double[2];
	//
	// if (l != null) {
	// gps[0] = l.getLatitude();
	// gps[1] = l.getLongitude();
	// }
	// return gps;
	// }

	public Location getLocation() {
		Location location = null;
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		List<String> providers = locationManager.getProviders(true);

		// getting GPS status
		boolean isGPSEnabled = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);

		// getting network status
		boolean isNetworkEnabled = locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

		if (!isGPSEnabled && !isNetworkEnabled) {
			// no network provider is enabled
			for (int i = 0; i < providers.size(); i++) {
				location = locationManager.getLastKnownLocation(providers
						.get(i));
				if (location != null)
					break;
			}
		} else {
			if (isNetworkEnabled) {
				locationManager.requestLocationUpdates(
						LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES,
						MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
				Log.d("Network", "Network Enabled");
				if (locationManager != null) {
					location = locationManager
							.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				}
			}

			// if GPS Enabled get lat/long using GPS Services
			if (isGPSEnabled) {
				locationManager.requestLocationUpdates(
						LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
						MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
				Log.d("GPS", "GPS Enabled");
				if (locationManager != null) {
					location = locationManager
							.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				}
			}
		}

		return location;
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

	public String readJSONFeed(String URL) {
		StringBuilder stringBuilder = new StringBuilder();
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(URL);
		try {
			HttpResponse response = httpClient.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream inputStream = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(inputStream));
				String line;
				while ((line = reader.readLine()) != null) {
					stringBuilder.append(line);
				}
				inputStream.close();
			} else {
				Log.d("JSON", "Failed to download file");
			}
		} catch (Exception e) {
			Log.d("readJSONFeed", e.getLocalizedMessage());
		}
		return stringBuilder.toString();
	}

	private class ReadWeatherJSONFeedTask extends
			AsyncTask<String, Void, String> {

		protected String doInBackground(String... urls) {
			System.out.println(urls[0]);
			return readJSONFeed(urls[0]);
		}

		protected void onPostExecute(String result) {
			try {
				JSONArray eventos = new JSONArray(result);

				for (int i = 0; i <= eventos.length(); i++) {
					JSONObject evento = eventos.getJSONObject(i);
					JSONObject category = evento.getJSONObject("Category");
					JSONObject event = evento.getJSONObject("Event");

					String title = event.getString("title");
					String categoryIcon = category.getString("icon");

					// Se obtiene la posición del evento y se parsea a double
					double latitude = Double
							.parseDouble(event.getString("lat"));
					double longitude = Double.parseDouble(event
							.getString("long"));
					LatLng latlong = new LatLng(latitude, longitude);

					System.out.println(evento.toString());
					System.out.println(category.toString());
					System.out.println(categoryIcon);
					System.out.println(categoryIcon.substring(0,
							categoryIcon.length() - 4));

					int icon = getResources()
							.getIdentifier(
									categoryIcon.substring(0,
											categoryIcon.length() - 4),
									"drawable", getPackageName());

					System.out.println(icon);

					// Se setea un icono por defecto si no se pudo encontrar el
					// icono correspondiente a la categoría
					if (icon == 0) {
						icon = R.drawable.musica;
					}

					gMap.addMarker(new MarkerOptions().position(latlong)
							.title(title).icon(BitmapDescriptorFactory
							// .fromResource(R.drawable.musica)));
									.fromResource(icon)));

				}

				// JSONObject weatherObservationItems = new JSONObject(
				// jsonObject.getString("weatherObservation"));
				//
				// Toast.makeText(
				// getBaseContext(),
				// weatherObservationItems.getString("clouds")
				// + " - "
				// + weatherObservationItems
				// .getString("stationName"),
				// Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				Log.d("ReadWeatherJSONFeedTask", e.getLocalizedMessage());
			}
		}
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
