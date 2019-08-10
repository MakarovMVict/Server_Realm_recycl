package com.kadnikovea.server_realm_recycl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements MyAdapter.ClickRecyclerListener {
    //что связано с recyclerview
    List<String>titles=new ArrayList<>();
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    //fragments
    Fragment frEmpty;

    Button save;
    Button load;
    Button clear;
    EditText bookText;
    //Realm
    Realm realm;
    RealmResults<MyBook>realmResults;
    //RecyclerV_Fragment recyclerV_fragment;

    //Strings
    //String result;
    //Fragments
    private FragmentManager manager;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //REALM
        Realm.init(this);
        realm=Realm.getDefaultInstance();
        //RealmConfiguration config=new RealmConfiguration.Builder(this).build();



        //init Fragment
        //RecyclerV_Fragment fr=new RecyclerV_Fragment();
        /*manager=getSupportFragmentManager();
        transaction=manager.beginTransaction();
        transaction.add(R.id.fragment_container_view_tag,fr);

        transaction.commit();*/
        //действия с БД и списком при загрузке активити
        firtActions();

        //recyclerView
        recyclerView=findViewById(R.id.title_recyclerview_id);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(manager);
        myAdapter=new MyAdapter(titles,MainActivity.this,this);
        recyclerView.setAdapter(myAdapter);




        //buttons
        save=findViewById(R.id.save_butt);
        load=findViewById(R.id.load_butt);
        clear=findViewById(R.id.clearButt);
        //edittext
        bookText=findViewById(R.id.text_book_id);

        //listeners
        addSaveListener();
        addLoadListener();
        addClearListener();
    }



    private void addSaveListener() {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                realm.executeTransaction(realm->{
                    MyBook book=realm.createObject(MyBook.class);
                    book.setTitle(bookText.getText().toString().trim());
                    titles.add(realmResults.get(realmResults.size()-1).getTitle());
                    });


                //это надо чтобы данные в списке в активити обновились и отобразились при сохранении!!!!!
                recyclerView.setAdapter(myAdapter);
                checkTitlesAreEmpty();

            }
        });

    }

    private String getTrimmedTitle() {
        return bookText.getText().toString().trim();
    }

    private void firtActions() {
        realm.executeTransaction(realm1 -> {
            realmResults=realm.where(MyBook.class).findAll();
            if(!realmResults.isEmpty()) {
            for (int i = 0; i < realmResults.size(); i++) {
                titles.add(realmResults.get(i).getTitle());
                System.out.println("realmresult"+i+titles.get(i));
            }
        }});

        checkTitlesAreEmpty();
        //recyclerView.setAdapter(myAdapter);
    }
    private void addLoadListener(){
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                realm.beginTransaction();
                System.out.println("load!");
                if(!realmResults.isEmpty()){
                    bookText.setText(realmResults.get(realmResults.size()-1).getTitle());
                }
                realm.commitTransaction();
            }
        });
    }

    //показать окошко/dialog  для сохранения*********************************


    private void addClearListener(){
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                realm.executeTransaction(realm1 -> {
                        realm.where(MyBook.class).findAll().deleteAllFromRealm();

                realmResults.deleteAllFromRealm();

              titles.clear();

                recyclerView.setAdapter(myAdapter);

                checkTitlesAreEmpty();
                });

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }




    @Override
    public void addViewListener(String name) {
        bookText.setText(name);
        System.out.println("name in booktext "+name);

    }
    //*****если список имен пустой то запустить фрагмент с надписью - нет сохр им]н
    private void checkTitlesAreEmpty(){
        if(titles.isEmpty()){
            //if (frEmpty==null){
            Fragment frEmpty=new BlankFragment();//}
            transaction=getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.recycler_frag_main_id,frEmpty);
            transaction.commit();
        }else{
            //if(recyclerV_fragment==null){
            RecyclerV_Fragment recyclerV_fragment=new RecyclerV_Fragment();//}
            transaction=getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.recycler_frag_main_id,recyclerV_fragment);
            transaction.commit();
        }
        //recyclerView.setAdapter(myAdapter);
        System.out.println("из адаптера +");
        for (String n:titles) {
            System.out.println(n+"\n");
        }
        System.out.println("из БД + ");
        for (int i=0;i<realmResults.size();i++) {
            System.out.println(realmResults.get(i).getTitle()+"\n");
        }

    }
}
