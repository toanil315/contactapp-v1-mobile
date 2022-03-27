package com.example.contactapp;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder>  {

    private List<Contact> contactsAll;
    private List<Contact> contacts;

    public ContactAdapter(List<Contact> contacts) {
        this.contacts = contacts;
        this.contactsAll = new ArrayList<Contact>(contacts);
    }

    @NonNull
    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.ViewHolder holder, int position) {
        byte[] byteImage = contacts.get(position).getImage();
        holder.tvName.setText(contacts.get(position).getName());
        holder.ivAvatar.setImageBitmap(BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length));

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if(isLongClick) {

                }
                else {
                    Log.d("DEBUG1", "click item: " + Integer.toString(contacts.get(position).getId()));
                    Intent intentDetail = new Intent(view.getContext(), DetailActivity.class);
                    intentDetail.putExtra("idContact", Integer.toString(contacts.get(position).getId()));
                    view.getContext().startActivity(intentDetail);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {
        public TextView tvName;
        public ImageView ivAvatar;
        private ItemClickListener itemClickListener;

        public ViewHolder(View view) {
            super(view);

            tvName = view.findViewById(R.id.tv_name);
            ivAvatar = view.findViewById(R.id.iv_avatar);

            view.setOnClickListener(this); // set event OnClick for view
            view.setOnLongClickListener(this); //set event OnLongClick for view
        }

        public void setItemClickListener(ItemClickListener itemClickListener)
        {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),false);
        }

        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),true);
            return true;
        }
    }

}
