package com.example.yellow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private HomeAdapter homeAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_home_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.home_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        List<HomeItem> itemList = new ArrayList<>();
        itemList.add(new HomeItem(R.drawable.application, "Application", "checks Responds Here"));
        itemList.add(new HomeItem(R.drawable.profile, "Profile", "check profile here"));
        itemList.add(new HomeItem(R.drawable.chat, "Chat", "Chat Here"));
        itemList.add(new HomeItem(R.drawable.job, "Jobs", "Check Jobs Here"));

        homeAdapter = new HomeAdapter(itemList,getContext());
        recyclerView.setAdapter(homeAdapter);
    }
}
