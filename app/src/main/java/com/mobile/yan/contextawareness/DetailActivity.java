package com.mobile.yan.contextawareness;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
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
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    Button btn_purchase, btn_sharing;
    CallbackManager callbackManager;
    ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        FacebookSdk.sdkInitialize( this.getApplicationContext() );
        setContentView( R.layout.activity_detail );

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

        //Initial describtion view

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById( R.id.collapsingToolbarLayout );
        collapsingToolbarLayout.setTitleEnabled( true );

        TextView detail_desc = findViewById( R.id.detail_desc );
        TextView detail_initialPrice = findViewById( R.id.detail_initialPrice );
        TextView detail_discountPrice = findViewById( R.id.detail_discountPrice );
        //TextView detail_finePrint = findViewById( R.id.detail_finePrint );
        ImageView detail_image = findViewById( R.id.detail_imageView );

        //TextView detail_highlightHtml =  findViewById( R.id.highlightHtml );
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
        //detail_finePrint.setText( finePrint );

        //Seting html view
        //detail_highlightHtml.setText( Html.fromHtml(highlightHtml) );
        detail_info.setText( Html.fromHtml(pitchHtml) );
        //RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);

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
}
