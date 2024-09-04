package com.cd.statussaver.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cd.statussaver.R;
import com.cd.statussaver.databinding.ActivityGetContactBinding;
import com.cd.statussaver.util.Const;
import com.cd.statussaver.util.SharedPreference;

import java.util.ArrayList;
import java.util.List;

public class GetContactActivity extends AppCompatActivity implements View.OnClickListener {

    public List<String> cntList;
    private Cursor cursor;

    public SharedPreference preference;

    public List<String> searchList;

    public int textLength = 0;
    ActivityGetContactBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGetContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(getResources().getColor(R.color.green));

        binding = (ActivityGetContactBinding) DataBindingUtil.setContentView(this, R.layout.activity_get_contact);
        preference = new SharedPreference(this);
        cntList = new ArrayList();
        searchList = new ArrayList();
        getContactList();
        binding.imgCntBack.setOnClickListener(this);
        binding.imgCntSave.setOnClickListener(this);
        binding.imgCntSearch.setOnClickListener(this);
        binding.imgCntClose.setOnClickListener(this);
    }
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.imgCntBack || viewId == R.id.imgCntSave) {
            finish();
        } else if (viewId == R.id.imgCntClose) {
            binding.edCntSearch.setText("");
            binding.imgCntClose.setVisibility(View.GONE);
            getContactList();
        } else if (viewId == R.id.imgCntSearch) {
            binding.txtCntSearch.setVisibility(View.GONE);
            binding.imgCntSearch.setVisibility(View.GONE);
            binding.edCntSearch.setVisibility(View.VISIBLE);
            binding.edCntSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    textLength = binding.edCntSearch.getText().toString().length();
                    cntList.clear();
                    for (int i4 = 0; i4 < searchList.size(); i4++) {
                        if (textLength <= ((String) searchList.get(i4)).length() && ((String) searchList.get(i4)).toLowerCase().contains(binding.edCntSearch.getText().toString().toLowerCase().trim())) {
                            cntList.add((String) searchList.get(i4));
                        }
                    }

                    AppendList(cntList);
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (binding.edCntSearch.getText().toString().length() > 0) {
                        binding.imgCntClose.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    @SuppressLint("Range")
    private void getContactList() {


       /*  cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, (String[]) null, (String) null, (String[]) null, "display_name ASC");
         cntList=new ArrayList<>();
        while (cursor.moveToNext()) {

        }
        cursor.close();*/

        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, "display_name ASC");
        String temp_name="";
        if (phones != null) {
            while (phones.moveToNext())
            {
                @SuppressLint("Range") String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                if (name.equals(temp_name))
                    continue;
                temp_name=name;
                cntList.add(phones.getString(phones.getColumnIndex("display_name")));
                searchList.add(phones.getString(phones.getColumnIndex("display_name")));

                //add name to your list or adapter here`enter code here`
            }
        }
        if (phones != null) {
            phones.close();
        }
        binding.getContactRecycleView.setAdapter(new GetContactAdapter(this, cntList));
    }

    public void AppendList(List<String> list) {
        binding.getContactRecycleView.setAdapter(new GetContactAdapter(this, list));
    }

    class GetContactAdapter extends RecyclerView.Adapter<GetContactAdapter.ViewHolder> {
        private Context context;

        public List<String> listitem;

        public GetContactAdapter(Context context2, List<String> list) {
            context = context2;
            listitem = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.get_contact_design_layout, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            viewHolder.txtContact.setText(listitem.get(i));
            viewHolder.txtCntFName.setText(String.valueOf(listitem.get(i).charAt(0)));
            if (Const.contactList.contains(listitem.get(i))) {
                viewHolder.chkContact.setChecked(true);
            } else {
                viewHolder.chkContact.setChecked(false);
            }
            viewHolder.chkContact.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        Const.contactList.add((String) listitem.get(i));
                        preference.setContactList("ContactList", Const.contactList);
                        return;
                    }
                    else
                    {
                        Const.contactList.remove((String) listitem.get(i));
                        preference.setContactList("ContactList", Const.contactList);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return listitem.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public CheckBox chkContact;
            private LinearLayout linearContacts;

            public TextView txtCntFName;

            public TextView txtContact;

            public ViewHolder(View view) {
                super(view);
                chkContact = (CheckBox) view.findViewById(R.id.chkContact);

                txtContact = (TextView) view.findViewById(R.id.txtContact);
                txtCntFName = (TextView) view.findViewById(R.id.txtCntFname);
                linearContacts = (LinearLayout) view.findViewById(R.id.linearContacts);
             /*   linearContacts.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!chkContact.isChecked()) {
                            chkContact.setChecked(true);
                            Const.contactList.add((String) listitem.get(getAdapterPosition()));
                            preference.setContactList("ContactList", Const.contactList);
                            return;
                        }
                        chkContact.setChecked(false);
                        Const.contactList.remove((String) listitem.get(getAdapterPosition()));
                        preference.setContactList("ContactList", Const.contactList);
                    }
                });*/
            }
        }
    }
}