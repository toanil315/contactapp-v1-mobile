package com.example.contactapp;

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
        holder.tvName.setText(contacts.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public ImageView ivAvatar;

        public ViewHolder(View view) {
            super(view);

            tvName = view.findViewById(R.id.tv_name);
            ivAvatar = view.findViewById(R.id.iv_avatar);
        }
    }
//    @Override
//    public Filter getFilter() {
//        return new Filter() {
//            @Override
//            protected FilterResults performFiltering(CharSequence constraint) {
//                List<Contact> filterContacts = new ArrayList<>();
//                if(constraint.toString().isEmpty()){
//                    System.out.println("run");
//                    filterContacts.addAll(contactsAll);
//
//                }else{
//                    System.out.println("here");
//                    for(Contact record : contactsAll) {
//                        // name match condition. this might differ depending on your requirement
//                        // here we are looking for name or phone number match
//                        if (record.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
//                            filterContacts.add(record);
//                        }
//                    }
//                }
//                FilterResults filterResults = new FilterResults();
//                filterResults.values = filterContacts;
//                return filterResults;
//            }
//
//            @Override
//            protected void publishResults(CharSequence constraint, FilterResults results) {
//                contacts.clear();
//                contacts.addAll((Collection<? extends Contact>) results.values);
//                notifyDataSetChanged();
//            }
//        };
//    }


}
