package com.algotrading.dutytodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 114;

    List<String> items;

    Button btnAdd;
    EditText etItem;
    RecyclerView rvItems;
    itemsAdapter ItemsAdapter;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);


        loadItems();


        itemsAdapter.OnLongClickListener onLongClickListener = new itemsAdapter.OnLongClickListener(){

            @Override
            public void onItemLongClicked(int i) {
                // remove the item from the model
                items.remove(i);
                // notify the adapter
                ItemsAdapter.notifyItemRemoved(i);
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_LONG).show();
                saveItems();

            }
        };
        itemsAdapter.onClickListener onClickListener = new itemsAdapter.onClickListener(){
            @Override
            public void onItemClicked(int i){
                Log.d("MainActivity", "Single click at position" + i);
                // create a new activity
                Intent intent = new Intent( MainActivity.this, editActivity.class);
                // pass the data being edited
                intent.putExtra(KEY_ITEM_TEXT, items.get(i));
                intent.putExtra(KEY_ITEM_POSITION, i);
                // display the activity
                startActivityForResult(intent, EDIT_TEXT_CODE);


            }

        };

        final itemsAdapter ItemsAdapter = new itemsAdapter(items, onLongClickListener, onClickListener);
        rvItems.setAdapter(ItemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this ));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String todoItem = etItem.getText().toString();
                // Adding items to model
                items.add(todoItem);
                // notify adapter that item is inserted
                ItemsAdapter.notifyItemInserted( items.size() - 1);
                etItem.setText("");
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_LONG).show();
                saveItems();
            }
        });
    }
    // handle the result of the edit activity

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        if(resultCode == RESULT_OK && resultCode == EDIT_TEXT_CODE){
            // retrieve the updated text value
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);
            // Extract the original position of the edited item from the position key
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);

            // Update the model at the right position with new item text
            items.set(position, itemText);
            // notify the adapter
            ItemsAdapter.notifyItemChanged(position);
            // persist the changes
            saveItems();
            Toast.makeText(getApplicationContext(), "Item updated successfully!", Toast.LENGTH_SHORT).show();


        }else{
            Log.w("MainActivity", "unknown call to onActivityResult");
        }

    }

    private File getDataFile(){
        return new File(getFilesDir(), "data.txt");

    }
    // this function will load items by reading every line of the date file
    private void loadItems(){
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }
    // this function save items by writing to the data file
    private void saveItems(){
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);
        }
    }
}