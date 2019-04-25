package project.com.lms;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import project.com.lms.Utils.Config;
import project.com.lms.models.Course;
import project.com.lms.models.Module;
import project.com.lms.models.User;


public class CourseActivity extends AppCompatActivity {
    private Context mContext;
    private Activity mActivity;

    private CoordinatorLayout mCLayout;
    private ListView mListView;
    private ArrayAdapter mAdapter;
    private Random mRandom = new Random();
    private TextView courseTitle;

    private DocumentSnapshot mLastQueriedDocument;
    private ArrayList<User> mNotes = new ArrayList<>();
    List<String> ModuleList = new ArrayList<String>();

    List<String> Module = new ArrayList<String>();
    public ArrayList<String> ModulesArray = new ArrayList<>();

    private static final String TAG = "FireLog";
    String TheTitle;
    private TextView course_title;
    ArrayList<Course> CourseArr = new ArrayList<Course>();

    ArrayList<Module> ModuleArrayList = new ArrayList<Module>();

    String title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_layout);

        // Get the application context
        mContext = getApplicationContext();
        mActivity = CourseActivity.this;

        // Get the widget reference from XML layout
        mCLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        mListView = (ListView) findViewById(R.id.list_view);

        courseTitle = findViewById(R.id.course_title);


        Module module = new Module();
        module.setCourseTitle("aaaaa");

        //ModuleArrayList.add(module);

        FirebaseFirestore mFireStore = FirebaseFirestore.getInstance();
        System.out.println("Doc ID :"+Config.DOC_ID);
        mFireStore.collection("users").document(Config.DOC_ID).collection("modules").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(e != null){
                    Log.d(TAG, "Error: " + e.getMessage());
                }

                for(DocumentChange doc : documentSnapshots.getDocumentChanges()){
                    if(doc.getType() == DocumentChange.Type.ADDED){
                        String title = doc.getDocument().getString("title");
                        String id = doc.getDocument().getId();
                       // Toast.makeText(getApplicationContext(), "Title: " + title , Toast.LENGTH_LONG).show();
                        ModuleList.add(title);
                        Module module = new Module();
                        module.setCourseTitle(title);
                        module.setCouseId(id);
                        ModuleArrayList.add(module);

                        System.out.print("aaaaaaa");
                        Log.v("aaaa", "asd");
                        //Toast.makeText(getApplicationContext(), "Title: " + ModuleList.get(1) , Toast.LENGTH_LONG).show();
                    }
                }

                CourseAdapter courseAdapter = new CourseAdapter(CourseActivity.this, ModuleArrayList);
                mListView.setAdapter(courseAdapter);
                //Toast.makeText(getApplicationContext(), NameString, Toast.LENGTH_LONG).show();
            }
        });



    }



    private void GetModule(){
        FirebaseFirestore mFireStore = FirebaseFirestore.getInstance();

        mFireStore.collection("users").document("data").collection("modules").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(e != null){
                    Log.d(TAG, "Error: " + e.getMessage());
                }

                for(DocumentChange doc : documentSnapshots.getDocumentChanges()){
                    if(doc.getType() == DocumentChange.Type.ADDED){
                        String title = doc.getDocument().getString("title");
                        //Toast.makeText(getApplicationContext(),  title, Toast.LENGTH_SHORT).show();
                        Module m = new Module();

                        //ModuleList.add(title);
                        //ModulesArray.add(title);
                        //System.out.println(ModulesArray.get(0));

                        m.setCourseTitle(title);
                        ModuleList.add(m.getCouseTitle());
                        System.out.println("Title :"+ m.getCouseTitle());
                    }
                }


                //Toast.makeText(getApplicationContext(), NameString, Toast.LENGTH_LONG).show();
            }
        });

        //for(int i=0; i<ModuleList.size(); i++){
         //   System.out.println("print" + ModuleList.get(i)); }

//        CourseAdapter courseAdapter = new CourseAdapter(CourseActivity.this, ModuleList);
//        mListView.setAdapter(courseAdapter);
    }



    //adapter is used to bind the data from above arrays to respective UI components
    private class CourseAdapter extends ArrayAdapter<Module> {

       // private String[] Title;
        //private List<String> Title;
//        private List<String> DiscussionBody;
//        private List<String> DocumentID;
       ArrayList<Module> TitleList = new ArrayList<Module>();

        private Activity context;

        //adapter constructor
        private CourseAdapter(Activity context, ArrayList<Module> TitleList) {
            super(context, R.layout.course_layout, TitleList);
            this.context = context;
            this.TitleList = TitleList;
        }

        @NonNull
        @Override

        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent){
            View r = convertView;
            ViewHolder viewHolder = null;

            //things to do onclick of an item
            if(r==null){
                LayoutInflater layoutInflater = context.getLayoutInflater();
                r = layoutInflater.inflate(R.layout.list_item,null,true);

                viewHolder = new ViewHolder(r);
                r.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder)r.getTag();
            }

            viewHolder.ModuleTitle.setText(TitleList.get(position).getCouseTitle());

//            //bind data to UI components
//            viewHolder.user_name.setText(UserName.get(position));
//            viewHolder.discussion_type.setText(DiscussionType.get(position));
//            viewHolder.discussion_body.setText(DiscussionBody.get(position));
            viewHolder.Card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Go to  " + TitleList.get(position).getCouseId(), Toast.LENGTH_SHORT).show();
                    Config.MOD_ID = TitleList.get(position).getCouseId();
                    System.out.println("Atendence Clicked");
                    Intent moduleIntent = new Intent(getApplicationContext(), ModuleActivity.class);
                    startActivity(moduleIntent);
                }
            });

            return r;

        }

        //Defining UI components
        class ViewHolder{
            TextView ModuleTitle;
            TextView discussion_type;
            TextView discussion_body;
            CardView Card;


            ViewHolder(View v){

                ModuleTitle = (TextView)v.findViewById(R.id.modules);
                Card = (CardView)v.findViewById(R.id.card_view);
            }


        }
    }

}