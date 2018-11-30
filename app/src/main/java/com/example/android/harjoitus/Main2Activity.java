package com.example.android.harjoitus;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;


import com.example.android.harjoitus.data.DatabaseContentAdapter;
import com.example.android.harjoitus.data.DatabaseContract;
import com.example.android.harjoitus.data.OmaSQLiteHelper;

public class Main2Activity extends AppCompatActivity  implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private DatabaseContentAdapter mAdapter;
    private RecyclerView mNumbersList;
    private static final String TAG = "Child";
    private SQLiteDatabase mDb;
    private OmaSQLiteHelper dbHelper;
    private static final int SQLITE_SEARCH_LOADER = 22;
    private static final int SQLITE_INSERT_LOADER = 23;
    private static final int SQLITE_DELETE_LOADER = 24;
    private static final int SQLITE_DELETE_BY_ID_LOADER = 25;
    private static final int SQLITE_SORT_LOADER = 26;
    private static final String QUERY_EXTRA = "query";
    private static final String SORT_EXTRA = "sort";
    private static final String INSERT_NAME_EXTRA = "insert_name";
    private static final String INSERT_NUMBER_EXTRA = "insert_number";
    private static final String INSERT_PAINOS_EXTRA = "insert_painos";
    private static final String INSERT_HANKINTAPVM_EXTRA = "insert_hankintapvm";
    private static final String DELETE_EXTRA = "delete";
    private static final String DELETE_BY_ID_EXTRA = "delete_by_id";
    private ProgressBar mLoadingIndicator;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);



            mNumbersList = (RecyclerView) findViewById(R.id.rv_numbers);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            mNumbersList.setLayoutManager(layoutManager);
            mNumbersList.setHasFixedSize(true);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mNumbersList.getContext(), layoutManager.getOrientation());
            mNumbersList.addItemDecoration(dividerItemDecoration);


            dbHelper = new OmaSQLiteHelper(this);
            mDb = dbHelper.getWritableDatabase();

            android.support.v4.app.LoaderManager.getInstance(this).initLoader(SQLITE_SEARCH_LOADER, null, this).forceLoad();
            getAllAkus();
            mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);


        /*
         Add a touch helper to the RecyclerView to recognize when a user swipes to delete an item.
         An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
         and uses callbacks to signal when a user is performing these actions.
         */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Here is where you'll implement swipe to delete

                //[Hint] Use getTag (from the adapter code) to get the id of the swiped item
                // Retrieve the id of the task to delete
                int id = (int) viewHolder.itemView.getTag();
                deleteById(id);

            }
        }).attachToRecyclerView(mNumbersList);

    }


    private void sort(){

        Bundle sortBundle = new Bundle();
        sortBundle.putString(SORT_EXTRA,"sort");

        LoaderManager loaderManager =  android.support.v4.app.LoaderManager.getInstance(this);
        Loader<Cursor> sortLoader = loaderManager.getLoader(SQLITE_SORT_LOADER);

        if (sortLoader == null) {
            loaderManager.initLoader(SQLITE_SORT_LOADER, sortBundle, this);
        } else {
            loaderManager.restartLoader(SQLITE_SORT_LOADER, sortBundle, this);
        }

    }



    private void getAllAkus(){

        Bundle queryBundle = new Bundle();
        queryBundle.putString(QUERY_EXTRA,"getAllAkus");

        LoaderManager loaderManager =  android.support.v4.app.LoaderManager.getInstance(this);
        Loader<Cursor> queryLoader = loaderManager.getLoader(SQLITE_SEARCH_LOADER);

        if (queryLoader == null) {
            loaderManager.initLoader(SQLITE_SEARCH_LOADER, queryBundle, this);
        } else {
            loaderManager.restartLoader(SQLITE_SEARCH_LOADER, queryBundle, this);
        }

    }

    private void addNewAku(String nimi, String numero, String painos, String hankintapvm) {

        Bundle bundle = new Bundle();
        bundle.putString(INSERT_NAME_EXTRA,nimi);
        bundle.putString(INSERT_NUMBER_EXTRA,numero);
        bundle.putString(INSERT_PAINOS_EXTRA,painos);
        bundle.putString(INSERT_HANKINTAPVM_EXTRA,hankintapvm);

        LoaderManager loaderManager =  android.support.v4.app.LoaderManager.getInstance(this);
        Loader<Cursor> queryLoader = loaderManager.getLoader(SQLITE_INSERT_LOADER);

        if (queryLoader == null) {
            loaderManager.initLoader(SQLITE_INSERT_LOADER, bundle, this);
        } else {
            loaderManager.restartLoader(SQLITE_INSERT_LOADER, bundle, this);
        }

    }

    private void deleteFirst(){

        Bundle bundle = new Bundle();
        bundle.putString(DELETE_EXTRA,"remove");

        LoaderManager loaderManager =  android.support.v4.app.LoaderManager.getInstance(this);
        Loader<Cursor> queryLoader = loaderManager.getLoader(SQLITE_DELETE_LOADER);

        if (queryLoader == null) {
            loaderManager.initLoader(SQLITE_DELETE_LOADER, bundle, this);
        } else {
            loaderManager.restartLoader(SQLITE_DELETE_LOADER, bundle, this);
        }


    }

    private void deleteById(int id){

        Bundle bundle = new Bundle();
        bundle.putInt(DELETE_BY_ID_EXTRA,id);

        LoaderManager loaderManager =  android.support.v4.app.LoaderManager.getInstance(this);
        Loader<Cursor> queryLoader = loaderManager.getLoader(SQLITE_DELETE_BY_ID_LOADER);

        if (queryLoader == null) {
            loaderManager.initLoader(SQLITE_DELETE_BY_ID_LOADER, bundle, this);
        } else {
            loaderManager.restartLoader(SQLITE_DELETE_BY_ID_LOADER, bundle, this);
        }


    }

    protected Cursor cursor(String clause){

        switch (clause){

            case DatabaseContract.DatabaseEntry._ID:

                return mDb.query(
                        DatabaseContract.DatabaseEntry.TABLE_AKUT,
                        null,
                        null,
                        null,
                        null,
                        null,
                        DatabaseContract.DatabaseEntry._ID
                );


            case DatabaseContract.DatabaseEntry.COLUMN_NRO:

                return mDb.query(
                        DatabaseContract.DatabaseEntry.TABLE_AKUT,
                        null,
                        null,
                        null,
                        null,
                        null,
                        DatabaseContract.DatabaseEntry.COLUMN_NRO

                );
        }
    return null;
    }



    public void onClick(View view){


        switch (view.getId()) {
            case R.id.add:

                EditText nimi=findViewById(R.id.editText2);
                EditText  nro=findViewById(R.id.editText3);
                EditText painos=findViewById(R.id.editText);
                EditText  hankintapvm=findViewById(R.id.editText4);

                Log.d("Aku's name ",""+nimi.getText().toString());

                if(!checkIfEmptyOrNull(nimi.getText().toString()) && !checkIfEmptyOrNull(nro.getText().toString())) {
                    addNewAku(nimi.getText().toString(), nro.getText().toString(), painos.getText().toString(), hankintapvm.getText().toString());
                    getAllAkus();
                }

                break;

            case R.id.delete:

                deleteFirst();

                break;

            case R.id.sort:

                sort();

                break;

        }

    }

    public boolean checkIfEmptyOrNull(String fieldValue){

        if(!TextUtils.isEmpty(fieldValue)){

            if(fieldValue!=null){
                return false;
            }
        }

        return true;
    }

    public void open() throws SQLException {
        mDb = dbHelper.getWritableDatabase();
        getAllAkus();
    }

    public void close() {
        dbHelper.close();
    }

    @Override
    protected void onResume() {
        open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        close();
        super.onPause();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable final Bundle bundle) {
        return new AsyncTaskLoader<Cursor>(this){


            // Initialize a Cursor, this will hold all the task data
            Cursor mTaskData = null;

            @Override
            public void onStartLoading(){
                super.onStartLoading();


                if(mTaskData != null){
                    deliverResult(mTaskData);
                }else {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }


            @Override
            public Cursor loadInBackground() {

                String QueryString = bundle.getString(QUERY_EXTRA);
                String InsertNameString = bundle.getString(INSERT_NAME_EXTRA);
                String InsertNumberString = bundle.getString(INSERT_NUMBER_EXTRA);
                String InsertPainosString = bundle.getString(INSERT_PAINOS_EXTRA);
                String InsertHankintaPvmString = bundle.getString(INSERT_HANKINTAPVM_EXTRA);
                String DeleteString = bundle.getString(DELETE_EXTRA);
                int DeleteByIdInt = bundle.getInt(DELETE_BY_ID_EXTRA);
                String SortString = bundle.getString(SORT_EXTRA);
                boolean isNull = SortString==null;
                boolean isEmpty = TextUtils.isEmpty(SortString);
                Log.d("isNull? ",""+isNull);
                Log.d("isEmpty? ",""+isEmpty);
                Log.d("SortString!! ",""+SortString);




                if(QueryString!=null && !TextUtils.isEmpty(QueryString)){

                    try {
                        return cursor(DatabaseContract.DatabaseEntry._ID);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        return null;
                    }

                }else if(InsertNameString!=null && InsertNumberString!=null && !TextUtils.isEmpty(InsertNameString) && !TextUtils.isEmpty(InsertNumberString) &&
                        InsertPainosString!=null && InsertHankintaPvmString!=null && !TextUtils.isEmpty(InsertPainosString) && !TextUtils.isEmpty(InsertHankintaPvmString)){

                    try {

                        ContentValues cv = new ContentValues();
                        cv.put(DatabaseContract.DatabaseEntry.COLUMN_NIMI, InsertNameString);
                        cv.put(DatabaseContract.DatabaseEntry.COLUMN_NRO, InsertNumberString);
                        cv.put(DatabaseContract.DatabaseEntry.COLUMN_PAINOS, InsertPainosString);
                        cv.put(DatabaseContract.DatabaseEntry.COLUMN_HANKINTAPVM, InsertHankintaPvmString);

                        mDb.insert(DatabaseContract.DatabaseEntry.TABLE_AKUT, null, cv);

                        return cursor(DatabaseContract.DatabaseEntry._ID);





                    }catch (NullPointerException e){
                        e.printStackTrace();
                        return null;
                    }


                }else if(DeleteString!=null && !TextUtils.isEmpty(DeleteString) ){

                    try {

                Cursor cursor = cursor(DatabaseContract.DatabaseEntry._ID);

                cursor.moveToFirst();

                String id = cursor.getString(cursor.getColumnIndex(DatabaseContract.DatabaseEntry._ID));
                mDb.delete(DatabaseContract.DatabaseEntry.TABLE_AKUT, DatabaseContract.DatabaseEntry._ID
                        + " = " + id, null);
                cursor.close();

                        return cursor(DatabaseContract.DatabaseEntry._ID);

                    }catch(NullPointerException e){
                        e.printStackTrace();
                        return null;
                    }

                }else if(SortString!=null && !TextUtils.isEmpty(SortString)){


                    try {
                        return cursor(DatabaseContract.DatabaseEntry.COLUMN_NRO);
                    }catch(NullPointerException e){
                        e.printStackTrace();
                        return null;
                    }
                }else if(DeleteByIdInt>=0){

                    try{

                        mDb.delete(DatabaseContract.DatabaseEntry.TABLE_AKUT, DatabaseContract.DatabaseEntry._ID
                                + " = " + DeleteByIdInt, null);

                        return cursor(DatabaseContract.DatabaseEntry._ID);

                    }catch(NullPointerException e){
                        e.printStackTrace();
                        return null;
                    }

                }

             return null;
            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                mTaskData = data;
                super.deliverResult(data);
            }

        };
    }


    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {

        mAdapter = new DatabaseContentAdapter(this, cursor);
        mNumbersList.setAdapter(mAdapter);
        mLoadingIndicator.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }


}
