package com.cd.statussaver.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.cd.statussaver.R;
import com.cd.statussaver.databinding.ActivityDirectSendBinding;
import com.cd.statussaver.model.PhoneNumberModel;
import com.cd.statussaver.util.Admenager;
import com.cd.statussaver.util.Const;
import com.cd.statussaver.util.SharedPreference;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

import java.util.ArrayList;
import java.util.List;

public class DirectSendActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityDirectSendBinding binding;
    public UnifiedNativeAd nativeAd;
    private List numberList;
    private String phonenumber;
    public Uri photoURI;
    private SharedPreference preference;
    public Uri shareUri = Uri.EMPTY;
    public int chatType = 1;
    Admenager admenager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        binding =(ActivityDirectSendBinding) DataBindingUtil.setContentView(this, R.layout.activity_direct_send);

        preference = new SharedPreference(this) {
        };
        admenager=new Admenager(this,this);
        admenager.load_InterstitialAd(getString(R.string.admobe_intertesial_direct_send_message));
        admenager.loadBannerAd(binding.adView,getString(R.string.admobe_banner_direct_reply));
        numberList = new ArrayList();

        binding.imgDirectBack.setOnClickListener(this);
        binding.imgChoose.setOnClickListener(this);
        binding.btnSendMsg.setOnClickListener(this);
        numberList= preference.getNumberList("NumberList");
  binding.countryCode.setAutoDetectedCountry(true);

        try {
            if (numberList.isEmpty()) {
                numberList = new ArrayList();
            }
        } catch (Exception e) {

            numberList = new ArrayList();
        }
        binding.phoneNumberRecycleview.setAdapter(new PhoneNumberAdapter(this, numberList));
        binding.directChatRadioGroup.check(R.id.rb_direct_chat_whatsapp);
        binding.directChatRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                chatType = ((RadioButton) binding.directChatRadioGroup.findViewById(binding.directChatRadioGroup.getCheckedRadioButtonId())).getId() == R.id.rb_direct_chat_whatsapp ? 1 : 2;
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.imgDirectBack) {
            finish();
        }
        if (id== R.id.btnSendMsg){
            String obj =  binding.edMessage.getText().toString().toString();
            String obj2 = binding.edPhoneNumber.getText().toString();
            String str =binding.countryCode.getSelectedCountryCode() + obj2;
            if (obj.length() == 0) {
                Toast.makeText(DirectSendActivity.this, "Please enter message", Toast.LENGTH_SHORT).show();
            } else if (obj2.length() == 0) {
                binding.edPhoneNumber.setError("Please enter phone number");
                binding.edPhoneNumber.requestFocus();
            } else if (obj2.length() < 7 || obj.length() <= 0) {
                binding.edPhoneNumber.setError("Please write correct phone number");
                binding.edPhoneNumber.requestFocus();
            } else {
                try {
                    PackageManager packageManager = DirectSendActivity.this.getPackageManager();
                    Intent intent = new Intent("android.intent.action.VIEW");

                    try {
                        intent.setPackage(chatType == 1 ? "com.whatsapp" : "com.whatsapp.w4b");
                        intent.setData(Uri.parse("https://api.whatsapp.com/send?phone=" + str + "&text=" + obj));
                        if (intent.resolveActivity(packageManager) != null) {
                            DirectSendActivity.this.startActivity(intent);
                        }
                        else{
                            Toast.makeText(this, chatType == 1 ? "Whatsapp" : "Whatsapp Business"+" Not install", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e2) {
                    Toast.makeText(DirectSendActivity.this, "Error/n" + e2.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 != -1) {
            return;
        }
        if (i == 1 && intent != null) {
            Uri data = intent.getData();
            binding.txtNoImage.setVisibility(View.GONE);
            binding.imgSetImage.setVisibility(View.VISIBLE);
            binding.imgSetImage.setImageURI(data);
            shareUri = data;
        } else if (i == 1000) {
            binding.txtNoImage.setVisibility(View.GONE);
            binding.imgSetImage.setVisibility(View.VISIBLE);
            binding.imgSetImage.setImageURI(photoURI);
            shareUri = photoURI;
        }
    }

//    private void chooseImage(Context context) {
//        if (Build.VERSION.SDK_INT < 23) {
//            return;
//        }
//        if (ContextCompat.checkSelfPermission(this, "android.permission.CAMERA") == 0 && ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0 && ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") == 0) {
//            final Dialog dialog = new Dialog(context);
//            dialog.setContentView(R.layout.choose_image_layout);
//            ((LinearLayout) dialog.findViewById(R.id.layourGallery)).setOnClickListener(new View.OnClickListener() {
//                public void onClick(View view) {
//                    Intent intent = new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    intent.setType("image/*");
//                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
//                    dialog.dismiss();
//                }
//            });
//            ((LinearLayout) dialog.findViewById(R.id.layoutCamera)).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                    if (intent.resolveActivity(context.getPackageManager()) != null) {
//                        File file2 = null;
//                        try {
//                            file2 = createImageFile();
//                        } catch (IOException e) {
//                            e.getMessage();
//                            Log.e("Dir Error", e.getMessage());
//                        }
//                        if (file2 != null) {
//                            Uri uriForFile = FileProvider.getUriForFile(context, "com.akweb.whatsautoreplybuzz.provider", file2);
//                            photoURI = uriForFile;
//                            intent.putExtra("output", uriForFile);
//                            startActivityForResult(intent, 1000);
//                            dialog.dismiss();
//                        }
//                    }
//                }
//            });
//            dialog.show();
//            return;
//        }
//        ActivityCompat.requestPermissions(this, new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"}, 100);
//    }

//    private File createImageFile() throws IOException {
//        String format = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        File createTempFile = File.createTempFile(getResources().getString(R.string.app_name) + format + "_", ".jpg", getExternalFilesDir(Environment.DIRECTORY_PICTURES));
//        currentPhotoPath = createTempFile.getAbsolutePath();
//        return createTempFile;
//    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }






    public class PhoneNumberAdapter extends RecyclerView.Adapter<PhoneNumberAdapter.ViewHolder> {
        private Context context;
        private List<PhoneNumberModel> listItem;

        public PhoneNumberAdapter(Context context2, List<PhoneNumberModel> list) {
            context = context2;
            listItem = list;
        }

        @NonNull
        @Override
        public PhoneNumberAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.phone_number_design_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull PhoneNumberAdapter.ViewHolder holder, int position) {
            holder.txtNumber.setText(listItem.get(position).getNumber());
            holder.txtTime.setText(Const.getTimeAgo(listItem.get(position).getTime()));
            holder.linearPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    binding.edPhoneNumber.setText(listItem.get(position).getNumber());
                }
            });
        }

        @Override
        public int getItemCount() {
            return listItem.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public LinearLayout linearPhone;

            public TextView txtNumber;

            public TextView txtTime;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                txtNumber = (TextView) itemView.findViewById(R.id.txtPhoneNumber);
                txtTime = (TextView) itemView.findViewById(R.id.txtPhoneTime);
                linearPhone = (LinearLayout) itemView.findViewById(R.id.linearPhoneNumber);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        admenager.showadmobeInterstitialAd();
    }
}