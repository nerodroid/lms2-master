package project.com.lms;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import project.com.lms.models.Note;
import project.com.lms.models.User;

public class ProfileActivity extends AppCompatActivity {
    private DocumentSnapshot mLastQueriedDocument;
    private ArrayList<User> mNotes = new ArrayList<>();
    private TextView nameText,emailText,yearText,degreeText;
    private ImageView profileImage;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);

        nameText = findViewById(R.id.user_name);
        emailText = findViewById(R.id.email_text);
        yearText = findViewById(R.id.year_text);
        degreeText = findViewById(R.id.degree_text);
        profileImage =findViewById(R.id.profile_img);

        getUserDetails();

    }
    private void getUserDetails(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference CollectionRef = db.collection("users");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = user.getUid();
        System.out.println("......................................................");
        System.out.println(userId);

        Query sQuery = null;
        if(mLastQueriedDocument != null){
            sQuery = CollectionRef
                    .whereEqualTo("user_id", userId)
                    //.orderBy("timestamp", Query.Direction.ASCENDING)
                    .startAfter(mLastQueriedDocument);
        }
        else{
            sQuery = CollectionRef
                    .whereEqualTo("user_id", userId);
            //.orderBy("timestamp", Query.Direction.ASCENDING);
        }
        sQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    User user = new User();
                    for(QueryDocumentSnapshot document: task.getResult()){

                        System.out.println(document.getData());

                        Picasso.get().load(document.getData().get("profile_img").toString()).into(profileImage);
                        //profileImage.setImageResource(document.getData().get("name").toString());
                        nameText.setText(document.getData().get("name").toString());
                        yearText.setText(document.getData().get("year").toString());
                        emailText.setText(document.getData().get("email").toString());
                        degreeText.setText(document.getData().get("course").toString());

                    }
                    if(task.getResult().size() != 0){
                        mLastQueriedDocument = task.getResult().getDocuments()
                                .get(task.getResult().size() -1);
                    }
                    // mNoteRecyclerViewAdapter.notifyDataSetChanged();
                }
                else{
                    System.out.println("Query Failed. Check Logs.");
                }
            }
        });


    }
    private void getNotes(){


        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference notesCollectionRef = db.collection("users");

        System.out.println(notesCollectionRef.document().get());
        //DocumentReference doc1 = db.document("users");
        System.out.println("......................................................");

        Query notesQuery = null;
        notesQuery = notesCollectionRef
                .whereEqualTo("user_id", FirebaseAuth.getInstance().getCurrentUser().getUid());

        notesQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){

                    for(QueryDocumentSnapshot document: task.getResult()){
                        User user = document.toObject(User.class);

                        //System.out.println(user.getName());
                        System.out.println(document.contains("attendence"));
                        System.out.println(document.getData());

                    }

                    if(task.getResult().size() != 0){
                        mLastQueriedDocument = task.getResult().getDocuments()
                                .get(task.getResult().size() -1);
                    }

                }
                else{
                    System.out.println("Query Failed. Check Logs.");
                }
            }
        });
    }




}
