package project.com.lms;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import project.com.lms.Utils.Config;
import project.com.lms.models.Course;
import project.com.lms.models.Module;
import project.com.lms.models.User;

public class ModuleActivity extends AppCompatActivity {


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

    ArrayList<project.com.lms.models.Module> ModuleArrayList = new ArrayList<Module>();

    String title;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module);

        mContext = getApplicationContext();
        mActivity = ModuleActivity.this;

        // Get the widget reference from XML layout
        mCLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        mListView = (ListView) findViewById(R.id.list_view);

        courseTitle = findViewById(R.id.course_title);


        Module module = new Module();
        module.setCourseTitle("aaaaa");

        //ModuleArrayList.add(module);

        FirebaseFirestore mFireStore = FirebaseFirestore.getInstance();

        mFireStore.collection("users").document(Config.DOC_ID).collection("modules").document(Config.MOD_ID).collection("assesments").addSnapshotListener(new EventListener<QuerySnapshot>() {
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

                ModuleActivity.CourseAdapter courseAdapter = new ModuleActivity.CourseAdapter(ModuleActivity.this, ModuleArrayList);
                mListView.setAdapter(courseAdapter);
                //Toast.makeText(getApplicationContext(), NameString, Toast.LENGTH_LONG).show();
            }
        });




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
            ModuleActivity.CourseAdapter.ViewHolder viewHolder = null;

            //things to do onclick of an item
            if(r==null){
                LayoutInflater layoutInflater = context.getLayoutInflater();
                r = layoutInflater.inflate(R.layout.list_item,null,true);

                viewHolder = new ModuleActivity.CourseAdapter.ViewHolder(r);
                r.setTag(viewHolder);
            }else{
                viewHolder = (ModuleActivity.CourseAdapter.ViewHolder)r.getTag();
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
