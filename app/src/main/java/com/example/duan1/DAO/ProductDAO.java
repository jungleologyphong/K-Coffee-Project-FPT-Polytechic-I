package com.example.duan1.DAO;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.duan1.Model.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;

public class ProductDAO {
    Context context;
    GetAllProductInterface getAllProductInterface;
    GetRecommendInterface getRecommendInterface;
    GetProductInterface getProductInterface;
    InsertProductInterface insertProductInterface;
    UpdateProductInterface updateProductInterface;
    StorageTask storageTask;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference("Uploads/Product");
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Product");

    public ProductDAO(Context context, UpdateProductInterface updateProductInterface) {
        this.context = context;
        this.updateProductInterface = updateProductInterface;
    }

    public ProductDAO(Context context, InsertProductInterface insertProductInterface) {
        this.context = context;
        this.insertProductInterface = insertProductInterface;
    }

    public ProductDAO(Context context, GetAllProductInterface getAllProductInterface) {
        this.context = context;
        this.getAllProductInterface = getAllProductInterface;
    }

    public ProductDAO(Context context, GetRecommendInterface getRecommendInterface) {
        this.context = context;
        this.getRecommendInterface = getRecommendInterface;
    }

    public ProductDAO(Context context, GetProductInterface getProductInterface) {
        this.context = context;
        this.getProductInterface = getProductInterface;
    }

    public void getRecommend() {
        final ArrayList<Product> products = new ArrayList<>();
        databaseReference.limitToLast(10).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    Product product = item.getValue(Product.class);
                    if (product != null) {
                        product.setId(item.getKey());
                        products.add(0, product);
                    }
                }
                getRecommendInterface.getRecommend(products);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getAll() {
        final ArrayList<Product> products = new ArrayList<>();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    Product product = item.getValue(Product.class);
                    if (product != null) {
                        product.setId(item.getKey());
                        products.add(product);
                    }
                }
                getAllProductInterface.getAllProduct(products);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getAllByTypeNoRealtime(String type) {
        final ArrayList<Product> products = new ArrayList<>();
        databaseReference.orderByChild("type").equalTo(type).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    Product product = item.getValue(Product.class);
                    if (product != null) {
                        product.setId(item.getKey());
                        products.add(product);
                    }
                }
                getAllProductInterface.getAllProduct(products);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getAllByType(String type) {
        databaseReference.orderByChild("type").equalTo(type)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Product product = snapshot.getValue(Product.class);
                        if (product != null) {
                            product.setId(snapshot.getKey());
                            getAllProductInterface.addProduct(product);
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Product product = snapshot.getValue(Product.class);
                        if (product != null) {
                            product.setId(snapshot.getKey());
                            getAllProductInterface.changeProduct(product);
                        }
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        Product product = snapshot.getValue(Product.class);
                        if (product != null) {
                            product.setId(snapshot.getKey());
                            getAllProductInterface.removeProduct(product);
                        }
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void getProductById(String productId) {
        databaseReference.child(productId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Product product = snapshot.getValue(Product.class);
                if (product != null) {
                    product.setId(snapshot.getKey());
                    getProductInterface.getProduct(product);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    public void insertProduct(final Product product, Uri uri) {
        if (uri != null) {
            MediaManager.get().upload(uri)
                    .option("resource_type", "image")
                    .callback(new UploadCallback() {
                        @Override
                        public void onStart(String requestId) {
                            // Bắt đầu upload, có thể show progress
                        }

                        @Override
                        public void onProgress(String requestId, long bytes, long totalBytes) {
                            // Cập nhật tiến trình nếu cần
                        }

                        @Override
                        public void onSuccess(String requestId, Map resultData) {
                            // Upload thành công, lấy URL ảnh trả về
                            String imageUrl = (String) resultData.get("secure_url");
                            product.setImage(imageUrl);

                            // Lưu product vào database (Firebase Realtime Database hoặc Firestore)
                            databaseReference.push().setValue(product)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(context, "Thành công", Toast.LENGTH_SHORT).show();
                                        insertProductInterface.insertSuccess();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(context, "Thất bại", Toast.LENGTH_SHORT).show();
                                    });
                        }

                        @Override
                        public void onError(String requestId, ErrorInfo error) {
                            Toast.makeText(context, "Upload thất bại: " + error.getDescription(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onReschedule(String requestId, ErrorInfo error) {
                            // Upload bị hoãn lại
                        }
                    }).dispatch();
        } else {
            // Không có ảnh, chỉ lưu product
            databaseReference.push().setValue(product)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(context, "Thành công", Toast.LENGTH_SHORT).show();
                        insertProductInterface.insertSuccess();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Thất bại", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    public void updateProduct(final Product product, Uri uri) {
        if (uri != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtention(uri));
            storageTask = fileReference.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful()) ;
                            Uri downloadUrl = urlTask.getResult();
                            product.setImage(downloadUrl.toString());
                            databaseReference.child(product.getId()).setValue(product)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(context, "Thanh cong", Toast.LENGTH_SHORT).show();
                                            updateProductInterface.upadeSuccess();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "That bai", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "That bai", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            databaseReference.child(product.getId()).setValue(product)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context, "Thanh cong", Toast.LENGTH_SHORT).show();
                            updateProductInterface.upadeSuccess();
                        }
                    });
        }
    }

    public String getFileExtention(Uri uri) {
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public interface GetAllProductInterface {
        void getAllProduct(ArrayList<Product> productList);

        void addProduct(Product product);

        void changeProduct(Product product);

        void removeProduct(Product product);
    }

    public interface GetRecommendInterface {
        void getRecommend(ArrayList<Product> productList);
    }

    public interface GetProductInterface {
        void getProduct(Product product);
    }

    public interface InsertProductInterface {
        void insertSuccess();
    }

    public interface UpdateProductInterface {
        void upadeSuccess();
    }


}
