package com.cd.statussaver.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cd.statussaver.R;
import com.cd.statussaver.databinding.ActivityContactBinding;
import com.cd.statussaver.util.Admenager;
import com.cd.statussaver.util.Const;
import com.cd.statussaver.util.SharedPreference;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.List;

public class ContactActivity extends AppCompatActivity implements View.OnClickListener {
    private static int GETCONTACT = 100;
    private boolean fullScreen = false;

    public SharedPreference preference;
    ActivityContactBinding binding;
    Admenager admenager;
    AdView adView;
    private boolean initialLayoutComplete = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.hide();
        }
        admenager=new Admenager(this,this);
        admenager.load_InterstitialAd(getString(R.string.admobe_intertesial_contact));
        adView=new AdView(this);
        binding.adviewContainner.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (!initialLayoutComplete) {
                            initialLayoutComplete = true;
                            loadBanner();
                        }
                    }
                });
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                binding.adviewContainner.setVisibility(View.VISIBLE);
                binding.adviewContainner.removeAllViews();
                binding.adviewContainner.addView(adView);
            }


        });



        preference = new SharedPreference(getApplicationContext());
        binding.imgChooseCont.setOnClickListener(this);
        binding.imgFullScreen.setOnClickListener(this);

        binding.imgDirectBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.contactRdtGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = i;

                if (id == R.id.rbtnContactList) {
                    ContactList();
                    preference.addToPref_String("ContactType", "ContactList");
                    binding.imgChooseCont.setVisibility(View.VISIBLE);
                    binding.imgFullScreen.setVisibility(View.VISIBLE);
                } else if (id == R.id.rbtnEverone) {
                    preference.addToPref_String("ContactType", "Everyone");
                    binding.imgChooseCont.setVisibility(View.GONE);
                    binding.imgFullScreen.setVisibility(View.GONE);
                    binding.linearEmpty.setVisibility(View.VISIBLE);
                    binding.contactRecycleView.setVisibility(View.GONE);
                    binding.imgEmpty.setImageDrawable(getResources().getDrawable(R.drawable.ic_group));
                    binding.txtEmpty.setText("Auto reply to everyone.");
                } else if (id == R.id.rbtnExceptContList) {
                    preference.addToPref_String("ContactType", "ExceptContList");
                    binding.imgChooseCont.setVisibility(View.VISIBLE);
                    binding.imgFullScreen.setVisibility(View.VISIBLE);
                    ContactList();
                } else if (id == R.id.rbtnExceptPhoneCont) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        if (ContactActivity.this.checkSelfPermission("android.permission.READ_CONTACTS") == PackageManager.PERMISSION_GRANTED) {
                            preference.addToPref_String("ContactType", "ExceptPhoneList");
                            binding.imgChooseCont.setVisibility(View.GONE);
                            binding.imgFullScreen.setVisibility(View.GONE);
                            binding.linearEmpty.setVisibility(View.VISIBLE);
                            binding.contactRecycleView.setVisibility(View.GONE);
                            binding.imgEmpty.setImageDrawable(getResources().getDrawable(R.drawable.ic_contact_page));
                            binding.txtEmpty.setText("Auto reply to everyone except your phone contacts.");
                        } else {
                            ActivityCompat.requestPermissions(ContactActivity.this, new String[]{"android.permission.READ_CONTACTS"}, 100);
                        }
                    }
                }

            }
        });

    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id != R.id.imgChooseCont) {
            if (id == R.id.imgFullScreen) {
                if (!fullScreen) {
                    binding.linearRadioBtn.setVisibility(View.GONE);
                    fullScreen = true;
                    binding.imgFullScreen.setImageDrawable(getResources().getDrawable(R.drawable.ic_fullscreen_exit));
                    return;
                }
                binding.linearRadioBtn.setVisibility(View.VISIBLE);
                fullScreen = false;
                binding.imgFullScreen.setImageDrawable(getResources().getDrawable(R.drawable.ic_fullscreen));
            }
        } else if (Build.VERSION.SDK_INT < 23) {
        } else {
            if (ContactActivity.this.checkSelfPermission("android.permission.READ_CONTACTS") == PackageManager.PERMISSION_GRANTED) {
                startActivity(new Intent(ContactActivity.this, GetContactActivity.class));
            } else {
                ActivityCompat.requestPermissions(ContactActivity.this, new String[]{"android.permission.READ_CONTACTS"}, 100);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Const.contactList = preference.getContactList("ContactList");
        if (preference.getFromPref_String("ContactType").equals("ContactList") || preference.getFromPref_String("ContactType").equals("ExceptContList")) {
            ContactList();
        }
        String fromPref_String = preference.getFromPref_String("ContactType");
        if (fromPref_String.equals("EveryOne")) {
            binding.imgChooseCont.setVisibility(View.GONE);
            binding.rbtnEverone.setChecked(true);
        } else if (fromPref_String.equals("ContactList")) {
            binding.imgChooseCont.setVisibility(View.VISIBLE);
            binding.rbtnContactList.setChecked(true);
        } else if (fromPref_String.equals("ExceptContList")) {
            binding.imgChooseCont.setVisibility(View.VISIBLE);
            binding.rbtnExceptContList.setChecked(true);
        } else if (fromPref_String.equals("ExceptPhoneList")) {
            binding.imgChooseCont.setVisibility(View.GONE);
            binding.rbtnExceptPhoneCont.setChecked(true);
        } else {
            binding.imgChooseCont.setVisibility(View.GONE);
            binding.rbtnEverone.setChecked(true);
        }
    }


    public void ContactList() {
        try {
            if (Const.contactList.isEmpty()) {
                Const.contactList = new ArrayList();
                binding.linearEmpty.setVisibility(View.VISIBLE);
                binding.contactRecycleView.setVisibility(View.GONE);
                binding.imgEmpty.setImageDrawable(getResources().getDrawable(R.drawable.ic_contacts));
                binding.txtEmpty.setText("Add one or more contact to the list.");
                return;
            }
            binding.linearEmpty.setVisibility(View.GONE);
            binding.contactRecycleView.setVisibility(View.VISIBLE);
            binding.contactRecycleView.setAdapter(new ContactListAdapter(ContactActivity.this, Const.contactList));
        } catch (Exception e) {
            Const.contactList = new ArrayList();
            Log.e("Contact List", e.getMessage());
        }
    }


    class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> {
        private Context context;
        private List<String> listItem;

        public ContactListAdapter(Context context2, List<String> list) {
            context = context2;
            listItem = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.contactlist_design_layout, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") int i) {
            viewHolder.txtFirstLater.setText(String.valueOf(listItem.get(i).charAt(0)));
            viewHolder.txtConctName.setText(listItem.get(i));
            viewHolder.imgClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listItem.remove(i);
                    preference.setContactList("ContactList", listItem);
                    notifyDataSetChanged();
                }
            });
        }


        public int getItemCount() {
            return listItem.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView imgClose;

            public TextView txtConctName;

            public TextView txtFirstLater;

            public ViewHolder(View view) {
                super(view);
                txtFirstLater = (TextView) view.findViewById(R.id.txtFirstLater);
                txtConctName = (TextView) view.findViewById(R.id.txtConctName);
                imgClose = (TextView) view.findViewById(R.id.imgConctClose);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        admenager.showadmobeInterstitialAd();
    }
    private void loadBanner()
    {
        adView.setAdUnitId(getString(R.string.admobe_banner_auto_reply_to));

        AdSize adSize = getAdSize();
        adView.setAdSize(adSize);

        // Create an ad request. Check your logcat output for the hashed device ID
        // to get test ads on a physical device, e.g.,
        // "Use AdRequest.Builder.addTestDevice("ABCDE0123") to get test ads on this
        // device."
        AdRequest adRequest =
                new AdRequest.Builder()
                        .build();

        // Start loading the ad in the background.
        adView.loadAd(adRequest);

    }
    private AdSize getAdSize() {
        // Determine the screen width (less decorations) to use for the ad width.
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = outMetrics.density;

        float adWidthPixels = binding.adviewContainner.getWidth();

        // If the ad hasn't been laid out, default to the full screen width.
        if (adWidthPixels == 0) {
            adWidthPixels = outMetrics.widthPixels;
        }

        int adWidth = (int) (adWidthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }
}