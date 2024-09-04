package com.cd.statussaver.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.cd.statussaver.R;
import com.cd.statussaver.adapter.WhatsappStatusAdapter;
import com.cd.statussaver.databinding.FragmentWhatsappImageBinding;
import com.cd.statussaver.interfaces.FileListWhatsappClickInterface;

import java.io.File;
import java.util.ArrayList;

import static androidx.databinding.DataBindingUtil.inflate;

public class WhatsappQImageFragment extends Fragment implements FileListWhatsappClickInterface {
    FragmentWhatsappImageBinding binding;

    private File[] allfiles;
    private ArrayList<Uri> fileArrayList;
    ArrayList<Uri> statusModelArrayList;
    private WhatsappStatusAdapter whatsappStatusAdapter;

    public WhatsappQImageFragment(ArrayList<Uri> fileArrayList) {
        this.fileArrayList = fileArrayList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = inflate(inflater, R.layout.fragment_whatsapp_image, container, false);
        initViews();

        whatsappStatusAdapter = new WhatsappStatusAdapter(getActivity(), statusModelArrayList, WhatsappQImageFragment.this);
        binding.rvFileList.setAdapter(whatsappStatusAdapter);
        return binding.getRoot();
    }

    private void initViews() {

        statusModelArrayList = new ArrayList<Uri>();
        getData();


    }

    private void getData() {

        for (int i = 0; i < fileArrayList.size(); i++) {
            Uri uri = fileArrayList.get(i);
            if (uri.getPath()!=null){
                String uriPath = uri.getPath().toLowerCase(); // Convert URI to lowercase for case-insensitive check

                if (!uriPath.endsWith(".mp4")) {
                    statusModelArrayList.add(uri);
                }
            }

        }

        if (statusModelArrayList!=null&&statusModelArrayList.size() != 0) {
            binding.tvNoResult.setVisibility(View.GONE);
        } else {
            binding.tvNoResult.setVisibility(View.VISIBLE);
        }

    }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }




    @Override
    public void getPosition(int position) {
        /*Intent inNext = new Intent(getActivity(), FullViewHomeWAActivity.class);
        inNext.putExtra("ImageDataFile", fileArrayList);
        inNext.putExtra("Position", position);
        getActivity().startActivity(inNext);*/
    }
}
