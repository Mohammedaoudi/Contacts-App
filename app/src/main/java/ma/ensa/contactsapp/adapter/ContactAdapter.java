package ma.ensa.contactsapp.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import ma.ensa.contactsapp.R;
import ma.ensa.contactsapp.beans.Contact;

public class ContactAdapter extends ArrayAdapter<Contact> implements Filterable {
    private final Context context;
    private final List<Contact> originalContacts;
    private List<Contact> filteredContacts;
    private ContactFilter filter;

    public ContactAdapter(Context context, List<Contact> contacts) {
        super(context, R.layout.contact_item, contacts);
        this.context = context;
        this.originalContacts = contacts;
        this.filteredContacts = contacts;
    }

    @Override
    public int getCount() {
        return filteredContacts.size();
    }

    @Override
    public Contact getItem(int position) {
        return filteredContacts.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.contact_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.nameTextView = convertView.findViewById(R.id.contactName);
            viewHolder.numberTextView = convertView.findViewById(R.id.contactNumber);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Contact contact = filteredContacts.get(position);
        viewHolder.nameTextView.setText(contact.getName());
        viewHolder.numberTextView.setText(contact.getPhoneNumber());


        return convertView;
    }



    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new ContactFilter();
        }
        return filter;
    }

    private class ContactFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0) {
                results.values = originalContacts;
                results.count = originalContacts.size();
            } else {
                List<Contact> filteredList = new ArrayList<>();
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Contact contact : originalContacts) {
                    if (contact.getName().toLowerCase().contains(filterPattern) ||
                            contact.getPhoneNumber().contains(filterPattern)) {
                        filteredList.add(contact);
                    }
                }

                results.values = filteredList;
                results.count = filteredList.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredContacts = (List<Contact>) results.values;
            notifyDataSetChanged();
        }
    }

    private static class ViewHolder {
        TextView nameTextView;
        TextView numberTextView;
        ImageView avatarImageView;
    }
}
