package project.com.lms;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import project.com.lms.Utils.Config;
import project.com.lms.models.Note;
import project.com.lms.models.User;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener
{

    private static final String TAG = "MainActivity";
    //Firebase
    private FirebaseAuth.AuthStateListener mAuthListener;

    //widgets
    //private FloatingActionButton mFab;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    //vars
    private View mParentLayout;
    private ArrayList<Note> mNotes = new ArrayList<>();
    private NoteRecyclerViewAdapter mNoteRecyclerViewAdapter;
    private DocumentSnapshot mLastQueriedDocument;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //mFab = findViewById(R.id.fab);
        mParentLayout = findViewById(android.R.id.content);
        //mRecyclerView = findViewById(R.id.recycler_view);
        //mSwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        //mSwipeRefreshLayout.setOnRefreshListener(this);

        Button btnProfile = (Button) findViewById(R.id.btnProfile);
        Button btnCourse = (Button) findViewById(R.id.btnCourse);
        Button btnAttendence = (Button) findViewById(R.id.btnAttendence);

        setupFirebaseAuth();
        //initRecyclerView();
        //getNotes();
        //GetDocID();

        btnProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Profile Clicked");
                Intent activity2Intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(activity2Intent);
            }
        });

        btnCourse.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Course Clicked");
                Intent activity2Intent = new Intent(getApplicationContext(), CourseActivity.class);
                startActivity(activity2Intent);
            }
        });

        btnAttendence.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("Atendence Clicked");
                Intent activity2Intent = new Intent(getApplicationContext(), AttendenceActivity.class);
                startActivity(activity2Intent);
            }
        });

        //if(Config.DOC_ID!=null){
            GetDocID();
        //}



    }



    @Override
    public void onRefresh() {
        getNotes();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void getNotes(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference notesCollectionRef = db.collection("notes");

        System.out.println("......................................................");

        Query notesQuery = null;
        if(mLastQueriedDocument != null){
           notesQuery = notesCollectionRef
                    .whereEqualTo("user_id", FirebaseAuth.getInstance().getCurrentUser().getUid())
                    //.orderBy("timestamp", Query.Direction.ASCENDING)
                    .startAfter(mLastQueriedDocument);
        }
        else{
            notesQuery = notesCollectionRef
                    .whereEqualTo("user_id", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    //.orderBy("timestamp", Query.Direction.ASCENDING);
        }
        notesQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){

                    for(QueryDocumentSnapshot document: task.getResult()){
                        Note note = document.toObject(Note.class);
                        mNotes.add(note);
                        //System.out.println(note.getContent());
                        //System.out.println(note.getTitle());
                        //System.out.println(note.getContent());
                        //Log.d(TAG, "onComplete: got a new note. Position: " + (mNotes.size() - 1));
                    }
                    if(task.getResult().size() != 0){
                        mLastQueriedDocument = task.getResult().getDocuments()
                                .get(task.getResult().size() -1);
                    }
                   // mNoteRecyclerViewAdapter.notifyDataSetChanged();
                }
                else{
                    makeSnackBarMessage("Query Failed. Check Logs.");
                }
            }
        });
    }

    private void initRecyclerView(){
        if(mNoteRecyclerViewAdapter == null){
            mNoteRecyclerViewAdapter = new NoteRecyclerViewAdapter(this, mNotes);
        }
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //mRecyclerView.setAdapter(mNoteRecyclerViewAdapter);
    }

    private void makeSnackBarMessage(String message){
        Snackbar.make(mParentLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        Intent i;
        switch(view.getId()){
            case R.id.btnProfile:
                i  = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(i);
                break;
            case R.id.btnCourse:
                i = new Intent(MainActivity.this, CourseActivity.class);
                startActivity(i);
                break;

            case R.id.btnAttendence:
                i = new Intent(MainActivity.this, AttendenceActivity.class);
                startActivity(i);
                break;

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.optionSignOut:
                signOut();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void signOut(){
        Log.d(TAG, "signOut: signing out");
        FirebaseAuth.getInstance().signOut();
    }

    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: started.");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        };
    }

    private void GetDocID(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference CollectionRef = db.collection("users");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



        Config.USER_ID = user.getUid();
        String userId =user.getUid();


        System.out.println("......................................................");


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

                        if(document.getId()!=null){
                            Config.DOC_ID = document.getId();
                            //Config.COURSE_ID = document.getData().keySet();
                            System.out.println("doc_id   :"+document.getId());
                            System.out.println("course_id:"+document.getData().get("course_id"));
                            Toast.makeText(getApplicationContext(), Config.DOC_ID, Toast.LENGTH_SHORT).show();
                        }

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

    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
        }
    }

}










