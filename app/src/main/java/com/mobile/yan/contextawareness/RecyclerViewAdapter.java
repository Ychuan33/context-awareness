package com.mobile.yan.contextawareness;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private ArrayList<ListItem> listItems;
    private Context context;

    public RecyclerViewAdapter(ArrayList<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewHead;
        public TextView textViewDesc;
        public ImageView imageView;
        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super( itemView );
            textViewHead = (TextView) itemView.findViewById( R.id.textViewHead );
            textViewDesc = (TextView) itemView.findViewById( R.id.textViewPrice );
            imageView = (ImageView) itemView.findViewById( R.id.imageView );
            linearLayout = (LinearLayout) itemView.findViewById( R.id.linearDealList );
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.list_item, parent, false );
        final ViewHolder viewHolder = new ViewHolder(v);

        viewHolder.linearLayout.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, DetailActivity.class);
                i.putExtra( "detail_name", listItems.get( viewHolder.getAdapterPosition() ).getHead());
                i.putExtra( "detail_desc", listItems.get( viewHolder.getAdapterPosition() ).getDesc());
                i.putExtra( "detail_image", listItems.get( viewHolder.getAdapterPosition() ).getImageUrl());
                i.putExtra( "detail_dealUrl", listItems.get( viewHolder.getAdapterPosition() ).getDealUrl());
                i.putExtra( "detail_shortAnnouncementTitle", listItems.get( viewHolder.getAdapterPosition() ).getShortAnnouncementTitle());
                i.putExtra( "detail_smallImageUrl", listItems.get( viewHolder.getAdapterPosition() ).getSmallImageUrl());
                i.putExtra( "detail_mediumImageUrl", listItems.get( viewHolder.getAdapterPosition() ).getMediumImageUrl());
                i.putExtra( "detail_finePrint", listItems.get( viewHolder.getAdapterPosition() ).getFinePrint());
                i.putExtra( "detail_highlightHtml", listItems.get( viewHolder.getAdapterPosition() ).getHighlightsHtml());
                i.putExtra( "detail_pitchHtml", listItems.get( viewHolder.getAdapterPosition() ).getPitchHtml());
                i.putExtra( "detail_initialPrice", listItems.get( viewHolder.getAdapterPosition() ).getInitialPrice());
                i.putExtra( "detail_discountPrice", listItems.get( viewHolder.getAdapterPosition() ).getDiscountPrice());
                i.putExtra( "detail_channels", listItems.get(viewHolder.getAdapterPosition()).getChannels() );

                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity( i );
            }
        } );

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ListItem listItem = listItems.get( position );
        Picasso.get()
            .load(listItem.getImageUrl())
            .into(holder.imageView);
        holder.textViewHead.setText( listItem.getHead() );
        holder.textViewDesc.setText( listItem.getDiscountPrice() );
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    //Enable search
    public void setAdapterFilter (ArrayList<ListItem> newList){
        listItems = new ArrayList<>();
        listItems.addAll( newList );
        notifyDataSetChanged();
    }

}
