package com.cd.statussaver.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.cd.statussaver.R;
import com.cd.statussaver.adapter.AudioAdapter;
import com.cd.statussaver.interfaces.FileSizeListener;
import com.cd.statussaver.model.WhatsappStatusModel;

import java.util.ArrayList;
import java.util.List;

public class voiceFragment extends Fragment  implements FileSizeListener {
    List<Uri> reciveArrayList = new ArrayList<>();
    ArrayList<WhatsappStatusModel> statusModelArrayList;
    AudioAdapter whatsappStatusAdapter;
    RecyclerView reciveRcv;
    TextView selectAll,sizeSort,deslect;
    String from;
    LottieAnimationView noTvresult;
    Menu menu;

    public voiceFragment(List<Uri> recvList, String from) {
        this.reciveArrayList = recvList;
        this.from = from;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =   inflater.inflate(R.layout.fragment_voice, container, false);
        intt(view);

        selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whatsappStatusAdapter.selectAllItems();
                selectAll.setVisibility(View.GONE);
                deslect.setVisibility(View.VISIBLE);
                menu.getItem(0).setIcon(ContextCompat.getDrawable(requireContext(),R.drawable.baseline_delete));

            }
        });

        deslect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whatsappStatusAdapter.clearAllItems();
                deslect.setVisibility(View.GONE);
                selectAll.setVisibility(View.VISIBLE);
                menu.getItem(0).setIcon(ContextCompat.getDrawable(requireContext(),R.drawable.baseline_delete_outline_24));

            }
        });

        return view;
    }

    private void intt(View view) {
        noTvresult = view.findViewById(R.id.noTvresult);
        reciveRcv = view.findViewById(R.id.rcv_DOCList);
        sizeSort = view.findViewById(R.id.text);
        selectAll = view.findViewById(R.id.selecAll);
        deslect = view.findViewById(R.id.deselecAll);
        statusModelArrayList = new ArrayList<>();


        getData();



    }



    private void getData() {
        if (reciveArrayList!=null&&reciveArrayList.size() != 0) {
            whatsappStatusAdapter = new AudioAdapter(reciveArrayList,getActivity(),from);
            reciveRcv.setAdapter(whatsappStatusAdapter);
            whatsappStatusAdapter.setFileSizeListener(this);
            noTvresult.setVisibility(View.GONE);
        } else {
            noTvresult.setVisibility(View.VISIBLE);
            selectAll.setVisibility(View.GONE);
            sizeSort.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_menu, menu);
        this.menu=menu;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            boolean anySelected = false;
            for (int i = 0; i < whatsappStatusAdapter.getItemCount(); i++) {
                if (whatsappStatusAdapter.isSelected(i)) {
                    anySelected = true;
                    break;
                }
            }

            if (whatsappStatusAdapter.getItemCount() == 0) {
                // Show a message that there are no items to delete
                Toast.makeText(getContext(), "No items to delete", Toast.LENGTH_SHORT).show();
            } else if (anySelected) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirmation Message");
                builder.setMessage("Are you sure to delete videos?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something when the positive button is clicked
                        ArrayList<Uri> selectedItems = new ArrayList<>();
                        for (int i = whatsappStatusAdapter.getItemCount() - 1; i >= 0; i--) {
                            if (whatsappStatusAdapter.isSelected(i)) {
                                selectedItems.add(whatsappStatusAdapter.getItem(i));
                                whatsappStatusAdapter.AudremoveItem(i);

                            }
                        }
                        Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something when the negative button is clicked
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                whatsappStatusAdapter.notifyDataSetChanged();
            } else {
                // Show a message that no items are selected
                Toast.makeText(getContext(), "Please select items to delete", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (whatsappStatusAdapter != null) {
            whatsappStatusAdapter.stopMediaPlayer();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (whatsappStatusAdapter != null) {
            whatsappStatusAdapter.stopMediaPlayer();
        }
    }

    @Override
    public void selectImageSize(String size) {
        sizeSort.setText(size);
    }
}