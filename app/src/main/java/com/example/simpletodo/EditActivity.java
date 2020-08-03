package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    EditText editedItem;
    Button buttonSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editedItem= findViewById(R.id.editedItem);
        buttonSave = findViewById(R.id.buttonSave);

        getSupportActionBar().setTitle("Edit Item");
        editedItem.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //create an intent which will contain the results
                Intent intent = new Intent();
                //pass the data(results of editing)
                intent.putExtra(MainActivity.KEY_ITEM_TEXT, editedItem.getText().toString());
                intent.putExtra(MainActivity.KEY_ITEM_POSITION, getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));
                //set the result of the intent
                setResult(RESULT_OK, intent);
                //finish activity, close screen and go back
                finish();
            }
        });
    }
}