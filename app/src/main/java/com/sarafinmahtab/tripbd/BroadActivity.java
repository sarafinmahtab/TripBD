package com.sarafinmahtab.tripbd;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BroadActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    String pinPointListUrl = ServerAddress.getMyServerAddress().concat("pin_point_marker_loader.php");

    GoogleMap broadGoogleMap;
    LocationRequest broadLocationRequest;
    GoogleApiClient broadGoogleApiClient;
    Location broadLastLocation;

    String markerID, markerTitle;
    double latitude, longitude;
    float zoomLevel = 14;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broad);

        Toolbar toolbar = (Toolbar) findViewById(R.id.broad_toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(0xFFFFFFFF);

        Bundle bundle = getIntent().getExtras();

        markerID = bundle.getString("marker_id");
        markerTitle = bundle.getString("marker_title");

        latitude = Double.parseDouble(bundle.getString("latitude"));
        longitude = Double.parseDouble(bundle.getString("longitude"));

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        getSupportActionBar().setTitle(markerTitle);

        if (googleServiceAvailable()) {
//            Toast.makeText(this, "Perfect!!", Toast.LENGTH_LONG).show();
            initMap();
        } else {
            new AlertDialog.Builder(this).setTitle("Failed To Load Maps")
                    .setMessage("Can't load Google Maps. Please try again later.")
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            System.exit(0);
                        }
                    }).show();
        }
    }

    private void initMap() {
        MapFragment broadMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.broadMapFragment);
        broadMapFragment.getMapAsync(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (broadGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(broadGoogleApiClient, this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        broadGoogleMap = googleMap;

        goToMapLocation(latitude, longitude, zoomLevel);

//        homeGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                broadGoogleMap.setMyLocationEnabled(true);
                broadGoogleMap.getUiSettings().setZoomControlsEnabled(true);
                broadGoogleMap.setMinZoomPreference(6.0f);
                broadGoogleMap.setMaxZoomPreference(20.0f);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            buildGoogleApiClient();
            broadGoogleMap.setMyLocationEnabled(true);
            broadGoogleMap.getUiSettings().setZoomControlsEnabled(true);
            broadGoogleMap.setMinZoomPreference(6.0f);
            broadGoogleMap.setMaxZoomPreference(20.0f);
        }

        addMarker(googleMap);

        getInfoWindow();
    }

    private void goToMapLocation(double lat, double lng, float zoom) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        broadGoogleMap.animateCamera(update);
    }

    private void addMarker(final GoogleMap googleMap) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, pinPointListUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("pin_point_marker_list");

                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);

                        LatLng latLng = new LatLng(Double.parseDouble(obj.getString("latitude")),
                                Double.parseDouble(obj.getString("longitude")));
                        String makeAddition;

                        if(obj.getString("details_link").equals("")) {
                            makeAddition = obj.getString("pin_point_id");
                        } else {
                            makeAddition = obj.getString("pin_point_id") + " " + obj.getString("details_link");
                        }

                        googleMap
                                .addMarker(new MarkerOptions().position(latLng)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_red_pin))
                                        .title(obj.getString("pin_point_name") + " (" + obj.getString("pp_bangla_name") + ")")
                                ).setTag(makeAddition);

                        final Marker[] lastOpenned = {null};
                        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {

                                // Check if there is an open info window
                                if (lastOpenned[0] != null) {
                                    // Close the info window
                                    lastOpenned[0].hideInfoWindow();

                                    // Is the marker the same marker that was already open
                                    if (lastOpenned[0].equals(marker)) {
                                        // Nullify the lastOpenned object
                                        lastOpenned[0] = null;
                                        // Return so that the info window isn't openned again
                                        return true;
                                    }
                                }

                                // Open the info window for the marker
                                marker.showInfoWindow();
                                // Re-assign the last openned such that we can close it later
                                lastOpenned[0] = marker;

                                // Event was handled by our code do not launch default behaviour.
                                return true;
                            }
                        });
//                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                LatLng sylhet = new LatLng(24.904539, 91.861101);
//                googleMap.addMarker(new MarkerOptions().position(sylhet)
//                        .title("Sylhet"));
//                googleMap.moveCamera(CameraUpdateFactory.newLatLng(sylhet));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BroadActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("centre_point_id", markerID);

                return params;
            }
        };

        MySingleton.getMyInstance(BroadActivity.this).addToRequestQueue(stringRequest);
    }

    private void getInfoWindow() {
        broadGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View view = getLayoutInflater().inflate(R.layout.marker_info_window, null);

                TextView name = view.findViewById(R.id.place_name);
                TextView lat_long = view.findViewById(R.id.lat_long);

                LatLng ll = marker.getPosition();
                name.setText(marker.getTitle());
                lat_long.setText(ll.latitude + ", " + ll.longitude);

                return view;
            }
        });

        broadGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String part1, part2;
                String myStrProblem = String.valueOf(marker.getTag());
                boolean blank_link = true;

                for (int i = 0; i < myStrProblem.length(); i++) {
                    if (!(myStrProblem.charAt(i) >= '0' && myStrProblem.charAt(i) <= '9')) {
                        blank_link = false;
                        break;
                    }
                }

                if(blank_link) {
                    part1 = myStrProblem; // id
                    part2 = ""; // link
                } else {
                    String[] parts = myStrProblem.split(" ");
                    part1 = parts[0]; // id
                    part2 = parts[1]; // link
                }

                Intent intent = new Intent(BroadActivity.this, DetailsActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("pin_point_id", part1);
                bundle.putString("details_link", part2);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });
    }

    protected synchronized void buildGoogleApiClient() {
        broadGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        broadGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        broadLocationRequest = new LocationRequest();
//        broadLocationRequest.setInterval(10000);
//        broadLocationRequest.setFastestInterval(1000);
        broadLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(broadGoogleApiClient, broadLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(final Location location)
    {
        broadLastLocation = location;
        final LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        broadGoogleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener(){
            @Override
            public boolean onMyLocationButtonClick() {
                //move map camera
                broadGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                return false;
            }
        });
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new android.support.v7.app.AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(BroadActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (broadGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        broadGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public boolean googleServiceAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);

        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvailable)) {
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "Can't Connect to Play Services", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
