package com.mobile.yan.contextawareness;

import android.Manifest;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.VoiceInteractor;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
//import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.fence.AwarenessFence;
import com.google.android.gms.awareness.fence.DetectedActivityFence;
import com.google.android.gms.awareness.fence.FenceState;
import com.google.android.gms.awareness.fence.FenceUpdateRequest;
import com.google.android.gms.awareness.fence.LocationFence;
import com.google.android.gms.awareness.snapshot.BeaconStateResult;
import com.google.android.gms.awareness.snapshot.DetectedActivityResult;
import com.google.android.gms.awareness.snapshot.HeadphoneStateResult;
import com.google.android.gms.awareness.snapshot.LocationResult;
import com.google.android.gms.awareness.snapshot.PlacesResult;
import com.google.android.gms.awareness.snapshot.WeatherResult;
import com.google.android.gms.awareness.state.BeaconState;
import com.google.android.gms.awareness.state.HeadphoneState;
import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihood;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener
{

    private GoogleApiClient mGoogleApiClient;
    private final static int REQUEST_PERMISSION_RESULT_CODE = 42;
    private double  lat;
    private double  lng;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<ListItem> listItems;
    private SearchView searchView;
    private String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up awareness api
        checkLocationPermission();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Awareness.API)
                .enableAutoManage(this, this)
                .build();
        mGoogleApiClient.connect();

        // Detect current location generate latitude and longitude
        // Give latitude and longitude value to URL
        setUrlLocation();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Setting up recyclerView
        recyclerView = (RecyclerView) findViewById( R.id.recyclerView );
        recyclerView.setHasFixedSize( true );
        recyclerView.setLayoutManager( new LinearLayoutManager( this ) );
        listItems = new ArrayList<>( );
    }

    private void loadRecyclerViewData (){
        final ProgressDialog progressDialog = new ProgressDialog( this );
        progressDialog.setMessage( "Loading data..." );
        progressDialog.show();

        StringRequest stringRequest = new StringRequest( Request.Method.GET,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject( response );
                            JSONArray array = jsonObject.getJSONArray( "deals" );

                            for(int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject( i );

                                ListItem item = new ListItem(
                                        object.getString( "announcementTitle" ),
                                        object.getString( "title" ),
                                        object.getString( "largeImageUrl" ),

                                        object.getString( "dealUrl" ),
                                        object.getString( "shortAnnouncementTitle" ),
                                        object.getString( "smallImageUrl" ),
                                        object.getString( "mediumImageUrl" ),
                                        object.getString( "finePrint" ),
                                        object.getString( "highlightsHtml" ),
                                        object.getString( "pitchHtml" ),

                                        object.getJSONArray("options")
                                                .getJSONObject(  0 )
                                                .getJSONObject( "value" )
                                                .getString( "formattedAmount" ),
                                        object.getJSONArray("options")
                                                .getJSONObject( 0 )
                                                .getJSONObject( "price" )
                                                .getString( "formattedAmount" ),
                                        defChannel(object)
                                );
                                listItems.add(item);
                                Log.d( "JSONfile", item.getChannels() );
                            }
                            adapter = new RecyclerViewAdapter(listItems, getApplicationContext());
                            recyclerView.setAdapter( adapter );
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError volleyError){
                        progressDialog.dismiss();
                        Toast.makeText( getApplicationContext(), volleyError.getMessage(), Toast.LENGTH_LONG ).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //Handle null value in channels

    private String defChannel(JSONObject object) throws JSONException {

        if (object.getJSONArray( "channels" ).isNull( 0 )){
            return "No chaneels is avialable";
        } else {
            return object.getJSONArray( "channels" )
                    .getJSONObject( 0 )
                    .getString( "id" );
        }
    }

    //Detect current location

    private void setUrlLocation() {
        if( !checkLocationPermission() ) {
            return;
        }

        Awareness.SnapshotApi.getLocation(mGoogleApiClient)
                .setResultCallback(new ResultCallback<LocationResult>()
                {
                    @Override
                    public void onResult(@NonNull LocationResult locationResult) {
                        Location location = locationResult.getLocation();
                        lat = location.getLatitude();
                        lng = location.getLongitude();
                        Log.d( "Location", location.toString() );
                        Log.d("lat", "value"+lat);

                        //Set up initial URL
                        URL = "https://partner-api.groupon.com/deals.json?tsToken=US_AFF_0_201236_212556_0"
                                + "&lat=" + lat
                                + "&lng=" + lng
                                + "&offset=0&limit=50";

                        loadRecyclerViewData();
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    private boolean checkLocationPermission() {
        if( !hasLocationPermission() ) {
            Log.e("ContextAwareness+", "Does not have location permission granted");
            requestLocationPermission();
            return false;
        }

        return true;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(
                MainActivity.this,
                new String[]{ Manifest.permission.ACCESS_FINE_LOCATION },
                REQUEST_PERMISSION_RESULT_CODE );
    }

    private boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION )
                == PackageManager.PERMISSION_GRANTED;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_RESULT_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //granted
                } else {
                    Log.e("ContextAwareness+", "Location permission denied.");
                }
            }
        }
    }

    //Create search option

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu_items, menu );

        final MenuItem menuItem = menu.findItem( R.id.search_button );
        searchView = (SearchView) menuItem.getActionView();
        changeSearchViewTextColor( searchView );
        ((EditText) searchView.findViewById(R.id.search_src_text))
                .setHintTextColor( getResources().getColor( R.color.white ) );

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                ArrayList<ListItem> newList = new ArrayList<>();
                for (ListItem item :  listItems){
                    String name = item.getHead().toLowerCase();
                    String price = item.getDiscountPrice().toLowerCase();
                    if (name.contains( newText )|| price.contains( newText ))
                        newList.add(item);
                }
                ((RecyclerViewAdapter)adapter).setAdapterFilter(newList);
                return true;
            }
            });
          return true;
    }

    private void changeSearchViewTextColor (View view){
        if (view != null){
            if (view instanceof TextView){
                ((TextView) view).setTextColor( Color.WHITE  );
                return;
            }else if (view instanceof ViewGroup){
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++ ){
                    changeSearchViewTextColor( viewGroup.getChildAt( i ) );
                }
            }
        }
    }

    //Customize Api

    public void setURLChannel(String channel) {
        try {
            channel = URLEncoder.encode(channel, "UTF-8");
        } catch (Exception e) {
            channel = channel;
        }
        URL += "&channel_id=" + channel;
        Log.v("uRL", URL);
    }

    public void setURLCategories(String categories) {
        try {
            categories = URLEncoder.encode(categories, "UTF-8");
        } catch (Exception e) {
            categories = categories;
        }
        URL += "&filters=category:" + categories;
        Log.v("uRL", URL);
    }

}

















