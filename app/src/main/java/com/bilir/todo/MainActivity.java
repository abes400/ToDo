package com.bilir.todo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    Button button;
    ListView list;

    ArrayAdapter adapter;

    ArrayList<String> items = new ArrayList<>();
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.newItem);
        button = findViewById(R.id.button);
        list = findViewById(R.id.list);

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1, items);

        list.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items.add(editText.getText().toString());
                adapter.notifyDataSetChanged();
                editText.setText("");
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("Delete?").setMessage("You cannot revert it if you do so.")
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                items.remove(position);
                                adapter.notifyDataSetChanged();
                            }
                        }).show();

                alertDialog.create();

            }
        });

        read();
    }

    @Override
    protected void onPause() {
        super.onPause();
        write();
    }
// You found me :D


    public void write() {
        sharedPreferences = getSharedPreferences("list.dat", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        HashSet<String> hashSet = new HashSet<>(items);
        editor.putStringSet("notes", hashSet);

        editor.commit();
    }

    public void read() {
        sharedPreferences = getSharedPreferences("list.dat", Context.MODE_PRIVATE);
        HashSet<String> hashSet = (HashSet<String>) sharedPreferences.getStringSet("notes", null);
        if(hashSet != null)
            items.addAll(hashSet);
        adapter.notifyDataSetChanged();
    }
}//