package com.sarafinmahtab.tripbd;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    GoogleMap homeGoogleMap;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;

    ListView list;
    SearchQueryListAdapter adapter;
    SearchView searchView;
    ImageView closeButton;
    EditText searchEditText;
    ArrayList<Place> arraylist = new ArrayList<>();

    String centrePoint, centrePointID, centrePointBang, latitude, longitude;
    boolean onQuery = false;

    String searchQueryRequest_url = "http://192.168.0.63/TripBD/searchview_place_name_query.php";
    String markerList_url = "http://192.168.0.63/TripBD/center_point_marker_loader.php";

//    String searchQueryRequest_url = "http://10.100.173.234/TripBD/searchview_place_name_query.php";
//    String markerList_url = "http://10.100.173.234/TripBD/center_point_marker_loader.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        //Navigation Drawer Parts
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        list = (ListView) findViewById(R.id.listView);

        searchView = (SearchView) findViewById(R.id.homeSearchView);
        searchEditText = (EditText) findViewById(R.id.search_src_text);
//        searchEditText.setHint("Find Tour Places");

        searchView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                return true;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
//                Toast.makeText(MainActivity.this, newText, Toast.LENGTH_LONG).show();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, searchQueryRequest_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
//                            Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
                            onQuery = true;
                            arraylist.clear();
                            int len;

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("place_query_list");

                            if(jsonArray.length() > 8) {
                                len = 8;
                            } else {
                                len = jsonArray.length();
                            }

                            for (int i = 0; i < len; i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                Place placeObj = new Place(obj.getString("pin_point_id"),
                                        obj.getString("pin_point_name"), obj.getString("pp_bangla_name"),
                                        obj.getString("details_link"), obj.getString("centre_point_id"), obj.getString("lat_long_id"));

                                // Binds all strings into an array
                                arraylist.add(placeObj);
                            }

                            // Pass results to ListViewAdapter Class
                            adapter = new SearchQueryListAdapter(MainActivity.this, arraylist);
                            adapter.notifyDataSetChanged();

                            // Binds the Adapter to the ListView
                            list.setAdapter(adapter);

                            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    Place placeObj = adapter.getItem(i);

//                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(MainActivity.this, placeObj.getPinPointName(), Toast.LENGTH_LONG).show();
                                }
                            });
                        } catch (JSONException e) {
//                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();

                        if(newText.equals("")) {
                            arraylist.clear();
                            params.put("query_text_change", "DhakaChittagongSylhet");
                            return params;
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    adapter.notifyDataSetChanged();
//                                }
//                            });
                        }

                        params.put("query_text_change", newText);
                        return params;
                    }
                };

//                adapter.filter(newText);
                MySingleton.getMyInstance(MainActivity.this).addToRequestQueue(stringRequest);
                return false;
            }
        });

        closeButton = searchView.findViewById(R.id.search_close_btn);

        // Set on cross button click listener
        closeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(onQuery) {
                    //Clear the text from EditText view
                    searchEditText.setText("");

                    //Clear query
                    searchView.setQuery("", false);
                    adapter.notifyDataSetChanged();
                }

                searchView.clearFocus();
                arraylist.clear();
            }
        });
    }

    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.homeMapFragment);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        homeGoogleMap = googleMap;
        goToMapLocation(24.4100476,90.3309697,6.75f);

//        homeGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                homeGoogleMap.setMyLocationEnabled(true);
                homeGoogleMap.getUiSettings().setZoomControlsEnabled(true);
                homeGoogleMap.setMinZoomPreference(6.0f);
                homeGoogleMap.setMaxZoomPreference(19.0f);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            buildGoogleApiClient();
            homeGoogleMap.setMyLocationEnabled(true);
            homeGoogleMap.getUiSettings().setZoomControlsEnabled(true);
            homeGoogleMap.setMinZoomPreference(6.0f);
            homeGoogleMap.setMaxZoomPreference(19.0f);
        }

        addMarker(googleMap);

        getInfoWindow();
    }

    private void getInfoWindow() {
        homeGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
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

        homeGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(MainActivity.this, BroadActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("marker_id", String.valueOf(marker.getTag()));
                bundle.putString("marker_title", marker.getTitle());
                bundle.putString("latitude", String.valueOf(marker.getPosition().latitude));
                bundle.putString("longitude", String.valueOf(marker.getPosition().longitude));
                intent.putExtras(bundle);

                Toast.makeText(MainActivity.this, String.valueOf(marker.getTag()), Toast.LENGTH_LONG).show();

                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }

    private void addMarker(final GoogleMap googleMap) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, markerList_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("center_point_marker_list");

                    for(int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);

                        LatLng latLng = new LatLng(Double.parseDouble(obj.getString("latitude")),
                                Double.parseDouble(obj.getString("longitude")));
                        googleMap
                                .addMarker(new MarkerOptions().position(latLng)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_red_pin))
                                .title(obj.getString("centre_point_name") + " (" + obj.getString("cp_bangla_name") + ")")
                                ).setTag(obj.getString("centre_point_id"));

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
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });

        MySingleton.getMyInstance(MainActivity.this).addToRequestQueue(stringRequest);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(10000);
//        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
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
        mLastLocation = location;
        final LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        homeGoogleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener(){
            @Override
            public boolean onMyLocationButtonClick() {
                //move map camera
                homeGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
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
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
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

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        homeGoogleMap.setMyLocationEnabled(true);
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

//    private void goToMapLocation(double lat, double lng) {
//        LatLng ll = new LatLng(lat, lng);
//        CameraUpdate update = CameraUpdateFactory.newLatLng(ll);
//        homeGoogleMap.moveCamera(update);
//    }

    private void goToMapLocation(double lat, double lng, float zoom) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        homeGoogleMap.moveCamera(update);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem menuItem = menu.findItem(R.id.mySwitch);
        View view = menuItem.getActionView();
        ImageButton switchView = view.findViewById(R.id.switchButton);

        switchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainListActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_down_in,R.anim.push_down_out);
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.mySwitch:
                return true;
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_camera:
                // Handle the camera action
                break;
            case R.id.nav_gallery:

                break;
            case R.id.nav_slideshow:

                break;
            case R.id.nav_manage:

                break;
            case R.id.nav_share:

                break;
            case R.id.nav_send:

                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
