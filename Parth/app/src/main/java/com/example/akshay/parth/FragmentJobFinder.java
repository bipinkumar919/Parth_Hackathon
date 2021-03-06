package com.example.akshay.parth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by akshay on 10/9/17.
 */

public class FragmentJobFinder extends Fragment implements AdapterJobs.ListItemClickListener{
    private RecyclerView mJobsRecyclerView;
    ArrayList<JobSeekerInfo> infolist;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mNotifyDatabaseReference;

    private ChildEventListener mChildEventListener;


    private FloatingActionButton addButton;



    public  FragmentJobFinder(){
        infolist=new ArrayList<>();



    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.jobs_fragment,container,false);


        mJobsRecyclerView=rootView.findViewById(R.id.Jobs_RecyclerView);
        LinearLayoutManager linearlayout=new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false);
        mJobsRecyclerView.setLayoutManager(linearlayout);
        AdapterJobs mJobsAdapter=new AdapterJobs(infolist, this);
        mJobsRecyclerView.setAdapter(mJobsAdapter);



        for(JobSeekerInfo i:infolist ){
            Log.i("message",i.getNameUser());
            Log.i("message",String.valueOf(infolist.size()));
        }

        mFirebaseDatabase=FirebaseDatabase.getInstance();

        mNotifyDatabaseReference=mFirebaseDatabase.getReference().child("JobSeekers");

        addButton=(FloatingActionButton)rootView.findViewById(R.id.FAB);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(),JobDescription.class);
                startActivity(i);
            }
        });


        return rootView;
    }

    @Override
    public void OnListItemClick(String phnNumber, int index) {
        if(index==0){
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:"+phnNumber));
            startActivity(intent);

        }
        else if(index==1){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("sms:"+phnNumber));
            startActivity(intent);



        }
        else{
            Intent i=new Intent(getActivity(),JobDescription.class);
            startActivity(i);

        }

    }

    @Override
    public void onPause() {
        super.onPause();
        detachDatabaseReadListener();
    }

    public void OnsignInInitialize(){
        if(mChildEventListener==null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Log.i("bafila rocks","chal bhosdike chup");
                    JobSeekerInfo notification=dataSnapshot.getValue(JobSeekerInfo.class);
                    Log.i("bafila rocks","chal bhosdike chup2");
                    infolist.add(notification);
                    Log.i("bafila rocks","chal bhosdike chup3");

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            mNotifyDatabaseReference.addChildEventListener(mChildEventListener);

        }
//        Collections.reverse(infolist);


    }
    private void detachDatabaseReadListener() {
        if (mChildEventListener!=null){
            mNotifyDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener=null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        OnsignInInitialize();
    }
}
