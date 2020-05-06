package com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Utility;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.Disaster;
import com.example.appproject_group25_itsmap_f20_master_disaster_broadcaster.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Repository {
    //firebase
    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageRef;
    FirebaseUser currentUser;
    Context context;

    public interface GetAllUsersCallBack
    {
        void onDataReceived(User user);
    }
    public Repository(FirebaseFirestore _db, FirebaseStorage _storage, StorageReference _storageRef, FirebaseUser _currentUser, Context _context)
    {
        db = _db;
        storage = _storage;
        context = _context;
        storageRef = _storageRef;
        currentUser = _currentUser;
    }

//Disasters methodes
    public void InsertDisaster(Disaster disaster)
    {
        CollectionReference disasterCollRef = db.collection("users").document(currentUser.getUid()).collection("disasters");
        // Add a new document with a generated ID
        disasterCollRef
                .add(disaster)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("FIREBASE", "DocumentSnapshot added with ID: " + documentReference.getId());

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("FIREBASE", "Error adding document", e);
                    }
                });
    }

    public List<Disaster> GetAllDisasters(String userID){
        List<Disaster> disasters = new ArrayList<Disaster>();

        db.collection("users").document(userID).collection("disasters")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.wtf("FIREBASE", document.getId() + " => " + document.getData());


                                if (document.toObject(Disaster.class).getId() != null)
                                {
                                    disasters.add(document.toObject(Disaster.class));
                                }
                                else{
                                    Disaster disaster = document.toObject(Disaster.class);
                                    disaster.setId(document.getId());
                                    disasters.add(disaster);
                                }
                            }
                        } else {
                            Log.wtf("FIREBASE", "Error getting documents.", task.getException());
                        }
                    }
                });
       // Intent intent = new Intent("GetALLDB");
       // LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        return disasters;
    }

    public Disaster GetDisaster(String userId, String disasterId)
    {

        final Disaster[] disaster = new Disaster[1];
        db.collection("users").document(userId).collection("disasters").document(disasterId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            Log.wtf("FIREBASE", document.getId() + " => " + document.getData());
                            disaster[0] = document.toObject(Disaster.class);
                            Log.wtf("FIREBASE", "ID: "+document.getId()+" title: "+disaster[0].getTitle());
                            //Intent intent = new Intent("GetALLDB");
                            //LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                        } else {
                            Log.wtf("FIREBASE", "Error getting documents.", task.getException());
                        }
                    }

                });


        return disaster[0];
    }

    //Upload Image to storage
    public String UploadImage(String filePath){
        Uri file = Uri.fromFile(new File(filePath));
        StorageReference ref = storageRef.child(UUID.randomUUID().toString());
        UploadTask uploadTask = ref.putFile(file);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Log.wtf("Repository", "Image uploaded");
            }
        });
        return ref.getName();
    }

}
