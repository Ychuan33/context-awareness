package com.mobile.yan.contextawareness;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_detail );

        getSupportActionBar().hide();

        //Receiving data

        String head = getIntent().getExtras().getString( "detail_name" );
        String desc = getIntent().getExtras().getString( "detail_desc" );
        String img = getIntent().getExtras().getString( "detail_image" );

        //Initial describtion view

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById( R.id.collapsingToolbarLayout );
        collapsingToolbarLayout.setTitleEnabled( true );

        TextView detail_head = findViewById( R.id.detail_textViewHead );
        TextView detail_desc = findViewById( R.id.detail_textViewDes );
        ImageView detail_image = findViewById( R.id.detail_imageView );
        TextView detail_info =  findViewById( R.id.detail_information );

        //Setting views
        collapsingToolbarLayout.setTitle( head );
        detail_head.setText( head );
        detail_desc.setText( desc );
        detail_info.setText( desc );

        //RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.loading_shape).error(R.drawable.loading_shape);

        //Setting the image
        Picasso.get()
                .load(img)
                .into(detail_image);


    }
}
