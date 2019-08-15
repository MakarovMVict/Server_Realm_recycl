package com.kadnikovea.server_realm_recycl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MyAdapter.ClickRecyclerListener {
    //что связано с recyclerview
    List<String> titles = new ArrayList<>();
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    //Retrofit
    List<MyBook_Retof> bookList=new ArrayList<>();
    MyBook_Retof myBook_retof;
    //fragments
    Fragment frEmpty;

    Button save;
    Button load;
    Button clear;
    Button send;
    Button findByIdButt;
    EditText bookText;
    EditText findByIdText;

    //Realm
    Realm realm;
    RealmResults<MyBook> realmResults;
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
        //recyclerView
        recyclerView = findViewById(R.id.title_recyclerview_id);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(manager);
        myAdapter = new MyAdapter(titles, MainActivity.this, this);
        recyclerView.setAdapter(myAdapter);




        //действия с БД, и сетью списком при загрузке активити
        firtActions();






        //buttons
        save = findViewById(R.id.save_butt);
        load = findViewById(R.id.load_butt);
        clear = findViewById(R.id.clearButt);
        send = findViewById(R.id.sendButt);
        findByIdButt=findViewById(R.id.findById_butt);
        //edittext
        bookText = findViewById(R.id.text_book_id);
        findByIdText=findViewById(R.id.find_by_id_text_id);


        //listeners
        addSaveListener();
        addLoadListener();
        addClearListener();
        addSendListener();
        addFindListener();
    }

    private void addFindListener() {
        System.out.println("from server search  ");
        //тут создаю ретрофит и в нем уже обновляю адаптер рецайклера
        //Retrofit
        ApiInterface apiInterface=ClientApi.getClient().create(ApiInterface.class);

        findByIdButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiInterface apiInterface=ClientApi.getClient().create(ApiInterface.class);
                Call<List<MyBook_Retof>> call =apiInterface.getName();
                call.enqueue(new Callback<List<MyBook_Retof>>() {
                    @Override
                    public void onResponse(Call<List<MyBook_Retof>> call, Response<List<MyBook_Retof>> response) {
                        bookList=response.body();


                            if (Integer.parseInt(findByIdText.getText().toString())<bookList.size()){

                                bookText.setText(bookList.get(Integer.parseInt(findByIdText
                                        .getText().toString().trim())).getTitle());
                            }else{
                                Toast.makeText(MainActivity.this
                                        , "NO ELEMENT FOUND", Toast.LENGTH_LONG).show();
                            }
                    }

                    @Override
                    public void onFailure(Call<List<MyBook_Retof>> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "something went wrong "
                                +t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }



    private void addSendListener() {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendToNetwork();
            }
        });
    }
    private void sendToNetwork(){

        ApiInterface apiInterface=ClientApi.getClient().create(ApiInterface.class);
        myBook_retof=new MyBook_Retof();
        System.out.println("myBook_Retrof is created! "+myBook_retof.toString());
        myBook_retof.setTitle(bookText.getText().toString().trim());
        System.out.println("title is : "+myBook_retof.getTitle());
        Call<MyBook_Retof> myBook_retofCall=apiInterface.createName(myBook_retof);
        myBook_retofCall.enqueue(new Callback<MyBook_Retof>() {
            @Override
            public void onResponse(Call<MyBook_Retof> call, Response<MyBook_Retof> response) {
                Toast.makeText(MainActivity.this, "name: ("+myBook_retof.getTitle()
                        + ") sent successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<MyBook_Retof> call, Throwable t) {
                Toast.makeText(MainActivity.this, "something went wrong ", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void addSaveListener() {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                realm.executeTransaction(realm -> {
                    MyBook book = realm.createObject(MyBook.class);
                    book.setTitle(bookText.getText().toString().trim());
                    titles.add(realmResults.get(realmResults.size() - 1).getTitle());
                });

                //проверить есть ли в списке данные и показать нужный фрагмент после этого
                checkTitlesAreEmpty();
                //это надо чтобы данные в списке в активити обновились и отобразились при сохранении!!!!!

                recyclerView.setAdapter(myAdapter);


            }
        });

    }

    private String getTrimmedTitle() {
        return bookText.getText().toString().trim();
    }

    private void firtActions() {
        //если в бд  не пусто?то всзять вс] оттуда а не с сервера
        realm.executeTransaction(realm1 -> {
            realmResults = realm.where(MyBook.class).findAll();
            if (!realmResults.isEmpty()) {
                for (int i = 0; i < realmResults.size(); i++) {
                    titles.add(realmResults.get(i).getTitle());
                    System.out.println("realmresult" + i + titles.get(i));
                }
            }else {
                //загрузить данные с сервера в коллекцию
                loadNamesFromServer();
                //recyclerView.setAdapter(myAdapter);

            }
        });

        //checkTitlesAreEmpty();
        //recyclerView.setAdapter(myAdapter);
    }

    private void loadNamesFromServer() {
        System.out.println("from inet starting");
        //тут создаю ретрофит и в нем уже обновляю адаптер рецайклера
        //Retrofit
        ApiInterface apiInterface=ClientApi.getClient().create(ApiInterface.class);
        Call<List<MyBook_Retof>> call =apiInterface.getName();
        call.enqueue(new Callback<List<MyBook_Retof>>() {
            @Override
            public void onResponse(Call<List<MyBook_Retof>> call, Response<List<MyBook_Retof>> response) {
                //тут проблема с получением данных в коллекцию
                bookList=response.body();
                //System.out.println("retrofit response + " + bookList.toString()+"\n");
                for (MyBook_Retof mb:bookList) {
                    titles.add(mb.getTitle());
                    System.out.println("title from inet"+titles.get(titles.size()-1  ));
                    System.out.println("\n");
                }

                //сделать видимыми titles
                recyclerView.setAdapter(myAdapter);
                //проверить есть ли в списке данные и показать нужный фрагмент после этого
                checkTitlesAreEmpty();

            }




            @Override
            public void onFailure(Call<List<MyBook_Retof>> call, Throwable t) {
                System.out.println("retrofit response failure + "+t.toString());

            }
        });
    }

    private void addLoadListener() {
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                realm.beginTransaction();
                System.out.println("load!");
                if (!realmResults.isEmpty()) {
                    bookText.setText(realmResults.get(realmResults.size() - 1).getTitle());
                }
                realm.commitTransaction();
            }
        });
    }

    //показать окошко/dialog  для сохранения*********************************


    private void addClearListener() {
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
        System.out.println("name in booktext " + name);

    }

    //*****если список имен пустой то запустить фрагмент с надписью - нет сохр имен
    private void checkTitlesAreEmpty() {
        if (titles.isEmpty()) {
            //if (frEmpty==null){
            Fragment frEmpty = new BlankFragment();//}
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.recycler_frag_main_id, frEmpty);
            transaction.commit();
        } else {
            //if(recyclerV_fragment==null){
            RecyclerV_Fragment recyclerV_fragment = new RecyclerV_Fragment();//}
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.recycler_frag_main_id, recyclerV_fragment);
            transaction.commit();
        }
        System.out.println("из адаптера +");
        for (String n : titles) {
            System.out.println(n + "\n");
        }
        System.out.println("из БД + ");
        for (int i = 0; i < realmResults.size(); i++) {
            System.out.println(realmResults.get(i).getTitle() + "\n");
        }

    }

}
