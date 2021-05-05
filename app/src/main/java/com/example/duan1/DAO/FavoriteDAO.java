package com.example.duan1.DAO;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.duan1.Model.Favorite;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavoriteDAO {
    Context context;
    FavoriteInterface favoriteInterface;
    DatabaseReference databaseReference;
    GetAllFavoriteInterface getAllFavoriteInterface;
    RemoveFavoriteInterface removeFavoriteInterface;

    public FavoriteDAO(Context context, RemoveFavoriteInterface removeFavoriteInterface) {
        this.context = context;
        this.removeFavoriteInterface = removeFavoriteInterface;
        databaseReference = FirebaseDatabase.getInstance().getReference("Favorite");
    }

    public FavoriteDAO(Context context, GetAllFavoriteInterface getAllFavoriteInterface) {
        this.context = context;
        this.getAllFavoriteInterface = getAllFavoriteInterface;
        databaseReference = FirebaseDatabase.getInstance().getReference("Favorite");
    }

    public FavoriteDAO(Context context, FavoriteInterface favoriteInterface) {
        this.context = context;
        this.favoriteInterface = favoriteInterface;
        databaseReference = FirebaseDatabase.getInstance().getReference("Favorite");
    }

    public void favorite(String userId, String productId) {
        final Favorite favorite = new Favorite(userId, productId);
        final String key = databaseReference.push().getKey();
        databaseReference.child(key).setValue(favorite)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        favorite.setId(key);
                        favoriteInterface.getFavorite(favorite);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }

    public void unFavorite(String favoriteId) {
        databaseReference.child(favoriteId).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        favoriteInterface.getFavorite(null);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    public void getFavoriteStateByUserIdAndProductId(String userId, String productId) {
        databaseReference.orderByChild("userId_productId").equalTo(userId + "_" + productId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot item : snapshot.getChildren()) {
                            Favorite favorite = item.getValue(Favorite.class);
                            if (favorite != null) {
                                favorite.setId(item.getKey());
                                favoriteInterface.getFavorite(favorite);
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    public void getAllByUserId(String userId) {
        final ArrayList<Favorite> favorites = new ArrayList<>();
        databaseReference.orderByChild("userId").equalTo(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot item : snapshot.getChildren()) {
                            Favorite favorite = item.getValue(Favorite.class);
                            if (favorite != null) {
                                favorite.setId(item.getKey());
                                favorites.add(favorite);
                            }
                        }
                        getAllFavoriteInterface.getAllFavorite(favorites);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    public void removeFavorite(final int position, String id) {
        databaseReference.child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                removeFavoriteInterface.removeFavorite(position);
            }
        });
    }

    public interface FavoriteInterface {
        void getFavorite(Favorite favoritee);
    }

    public interface GetAllFavoriteInterface {
        void getAllFavorite(ArrayList<Favorite> favoriteList);
    }

    public interface RemoveFavoriteInterface {
        void removeFavorite(int position);
    }
}
