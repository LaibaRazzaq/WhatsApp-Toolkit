package com.cd.statussaver.fragment;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cd.statussaver.R;
import com.cd.statussaver.activity.WhatsAppCleaner;
import com.cd.statussaver.adapter.WhatsappStatusAdapter;
import com.cd.statussaver.adapter.whatsAppCleanerAdatpter;
import com.cd.statussaver.databinding.FragmentWhatsappImageBinding;
import com.cd.statussaver.interfaces.FileListWhatsappClickInterface;
import com.cd.statussaver.model.WhatsappStatusModel;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static androidx.databinding.DataBindingUtil.inflate;

public class WhatsappImageFragment extends Fragment implements FileListWhatsappClickInterface {
    FragmentWhatsappImageBinding binding;


    ArrayList<Uri> statusModelArrayList;
    private whatsAppCleanerAdatpter whatsappStatusAdapter;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = inflate(inflater, R.layout.fragment_whatsapp_image, container, false);

            initViews();
            whatsappStatusAdapter = new whatsAppCleanerAdatpter(statusModelArrayList, getActivity(), "whatsapp");
            binding.rvFileList.setAdapter(whatsappStatusAdapter);


        return binding.getRoot();
    }

    private void initViews() {
        statusModelArrayList = new ArrayList<>();
        //getData();
        binding.swiperefresh.setOnRefreshListener(() -> {
            statusModelArrayList = new ArrayList<>();
           // getData();
            binding.swiperefresh.setRefreshing(false);
        });

    }


    @Override
    public void getPosition(int position) {

    }
}
