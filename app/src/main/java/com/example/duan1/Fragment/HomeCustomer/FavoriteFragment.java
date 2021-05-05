package com.example.duan1.Fragment.HomeCustomer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.duan1.Activity.HomeUserActivity;
import com.example.duan1.Adapter.FavoriteAdapter;
import com.example.duan1.DAO.FavoriteDAO;
import com.example.duan1.Model.Favorite;
import com.example.duan1.R;

import java.util.ArrayList;

public class FavoriteFragment extends Fragment implements FavoriteDAO.GetAllFavoriteInterface {
    RecyclerView rcvFavorite;
    SwipeRefreshLayout swlFavorite;
    FavoriteDAO favoriteDAO;
    ArrayList<Favorite> favorites;
    FavoriteAdapter favoriteAdapter;

    public FavoriteFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);


        rcvFavorite = view.findViewById(R.id.rcvFavorite);
        swlFavorite = view.findViewById(R.id.swlFavorite);
        rcvFavorite.setLayoutManager(new LinearLayoutManager(getContext()));

        favoriteDAO = new FavoriteDAO(getContext(), this);

        favorites = new ArrayList<>();
        favoriteAdapter = new FavoriteAdapter(getContext(), favorites);
        rcvFavorite.setAdapter(favoriteAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getFavorite();
        swlFavorite.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFavorite();
                swlFavorite.setRefreshing(false);
            }
        });
    }

    private void getFavorite() {
        favoriteDAO.getAllByUserId(HomeUserActivity.user.getId());
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeUserActivity) getActivity()).changeBackButton();
        ((HomeUserActivity) getActivity()).getSupportActionBar().setTitle("Yêu thích");
    }

    @Override
    public void getAllFavorite(ArrayList<Favorite> favoriteList) {
        favorites.clear();
        favorites.addAll(favoriteList);
        favoriteAdapter.notifyDataSetChanged();
    }
}