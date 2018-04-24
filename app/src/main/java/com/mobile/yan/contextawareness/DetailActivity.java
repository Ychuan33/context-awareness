package com.mobile.yan.contextawareness;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.WeatherResult;
import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;

public class DetailActivity extends AppCompatActivity {

    private GoogleApiClient mGoogleApiClient;
    private final static int REQUEST_PERMISSION_RESULT_CODE = 42;

    Button btn_purchase, btn_sharing;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    String weatherInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        FacebookSdk.sdkInitialize( this.getApplicationContext() );
        setContentView( R.layout.activity_detail );

        // Set up awareness api
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi( Awareness.API)
                .build();
        mGoogleApiClient.connect();

        // Hide actionbar

        getSupportActionBar().hide();

        //Receiving data

        String head = getIntent().getExtras().getString( "detail_name" );
        String desc = getIntent().getExtras().getString( "detail_desc" );
        String img = getIntent().getExtras().getString( "detail_image" );
        final String dealUrl = getIntent().getExtras().getString( "detail_dealUrl" );
        String shortAnnouncementTitle = getIntent().getExtras().getString( "detail_shortAnnouncementTitle" );
        String smallImageUrl = getIntent().getExtras().getString( "detail_smallImageUrl" );
        String mediumImageUrl = getIntent().getExtras().getString( "detail_mediumImageUrl" );
        String finePrint = getIntent().getExtras().getString( "detail_finePrint" );
        String highlightHtml = getIntent().getExtras().getString( "detail_highlightHtml" );
        String pitchHtml = getIntent().getExtras().getString( "detail_pitchHtml" );
        String initialPrice = getIntent().getExtras().getString( "detail_initialPrice" );
        String discountPrice = getIntent().getExtras().getString( "detail_discountPrice" );
        String channels = getIntent().getExtras().getString( "detail_channels" );

        showWeather(channels);

        //Initial describtion view

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById( R.id.collapsingToolbarLayout );
        collapsingToolbarLayout.setTitleEnabled( true );

        TextView detail_desc = findViewById( R.id.detail_desc );
        TextView detail_initialPrice = findViewById( R.id.detail_initialPrice );
        TextView detail_discountPrice = findViewById( R.id.detail_discountPrice );
        ImageView detail_image = findViewById( R.id.detail_imageView );
        TextView detail_info =  findViewById( R.id.detail_information );

        //Setting the image
        Picasso.get()
                .load(img)
                .into(detail_image);

        //Setting views
        collapsingToolbarLayout.setTitle( shortAnnouncementTitle );
        detail_desc.setText( desc );
        detail_initialPrice.setText( initialPrice );
        detail_discountPrice.setText( discountPrice );
        detail_info.setText( Html.fromHtml(pitchHtml) );

        //Setting up purchase buttion
        btn_purchase = (Button) findViewById(R.id.detail_purchase);
        btn_purchase.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myWebLink = new Intent(android.content.Intent.ACTION_VIEW);
                myWebLink.setData( Uri.parse(dealUrl));
                startActivity(myWebLink);
            }
        });

        //Setting up facebook share button
        btn_sharing = (Button) findViewById( R.id.detail_shareLink );

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog( this );
        btn_sharing.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setQuote( "This is the deal link" )
                        .setContentUrl( Uri.parse(dealUrl) )
                        .build();
                if(ShareDialog.canShow( ShareLinkContent.class )){
                    shareDialog.show( linkContent );
                }
            }
        } );
    }

    //Show weather

    private void showWeather(String show_weather){
        if (show_weather.contains( "No chaneels is avialable")||show_weather.contains("getaways")){
            setWeatherInfo();
        }
    }


    //Detect current weather and set the view

    private void setWeatherInfo() {
        if( !checkLocationPermission() ) {
            return;
        }
        Awareness.SnapshotApi.getWeather(mGoogleApiClient)
                .setResultCallback(new ResultCallback<WeatherResult>() {
                    @Override
                    public void onResult(@NonNull WeatherResult weatherResult) {
                        Weather weather = weatherResult.getWeather();

                        weatherInfo ="Temperature today:"
                                +"  "
                                + Math.round(weather.getTemperature( Weather.CELSIUS ))
                                +"\n"+
                                "Feels like:"
                                +" "
                                + Math.round(weather.getFeelsLikeTemperature( Weather.CELSIUS ));
                        if (weather.getConditions()[0] == Weather.CONDITION_CLOUDY){
                            weatherInfo += "\n"+"Looks like there's some clouds out there.";
                        }else if (weather.getConditions()[0] == Weather.CONDITION_CLEAR){
                            weatherInfo += "\n"+"Enjoy the good weather!";
                        }else if (weather.getConditions()[0] == Weather.CONDITION_RAINY){
                            weatherInfo += "\n"+"Looks like the is raining outside.";
                        }
                        TextView detail_weather = findViewById( R.id.detail_weather );
                        detail_weather.setText( weatherInfo );
                        Log.e("ContextAwareness+", weatherInfo);
                    }
                });
    }

    // Ensure persimssion is granted

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
                DetailActivity.this,
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
}
