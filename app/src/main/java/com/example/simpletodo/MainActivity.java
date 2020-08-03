package com.example.simpletodo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;

    List<String> itemList;

    Button addButton;
    EditText textItem;
    RecyclerView items;
    ItemsAdapter itemsAdapter;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addButton = findViewById(R.id.addButton);
        textItem = findViewById(R.id.textItem);
        items = findViewById(R.id.items);
        loadItems();

        ItemsAdapter.OnLongClickListener onLongClickListener =new ItemsAdapter.OnLongClickListener(){
            @Override
            public void OnItemLongClicked(int position) {

                //Delete the item from the model
                itemList.remove(position);
                //notify the adapter
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };
        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                Log.d("MainActivity","Single click at position "+ position );
                //create a new activity
                Intent i = new Intent(MainActivity.this,EditActivity.class);
                //pass the data being edited
                i.putExtra(KEY_ITEM_TEXT, itemList.get(position));
                i.putExtra(KEY_ITEM_POSITION, position);

                startActivityForResult(i,EDIT_TEXT_CODE);
            }
        };
         itemsAdapter = new ItemsAdapter(itemList, onLongClickListener, onClickListener);
        items.setAdapter(itemsAdapter);
        items.setLayoutManager(new LinearLayoutManager(this));
    addButton.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v){
            String todoItem = textItem.getText().toString();
            //Add item to the model
            itemList.add(todoItem);
            //notify adapter that an item is inserted
            itemsAdapter.notifyItemInserted(itemList.size()-1);
            textItem.setText("");
            Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
            saveItems();
        }
    });
    }
    //handle the result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE) {
            assert data != null;
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);

            int position = data.getExtras().getInt(KEY_ITEM_POSITION);

            itemList.set(position, itemText);
            itemsAdapter.notifyItemChanged(position);
            saveItems();

        } else {
            Log.w("MainActivity", "Unknown call to onActivityResult");
        }
    }

    private File getDataFile(){
        return new File(getFilesDir(), "data.txt");
    }
    //This function will load items by reading every line of the data file
    private void loadItems(){
        try {
            itemList = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        }catch (IOException e) {
          Log.e("MainActivity", "Error reading items", e);
          itemList = new ArrayList<>();
        }
    }
    //this function saves items by writing them into the data file
    private void saveItems(){
        try {
            FileUtils.writeLines(getDataFile(), itemList);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);

        }
    }
}