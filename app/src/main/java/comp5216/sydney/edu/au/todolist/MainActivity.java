package comp5216.sydney.edu.au.todolist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

public class MainActivity extends AppCompatActivity {
    //define variables
    ListView listview;
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    EditText addItemEditText;

    public final int EDIT_ITEM_REQUEST_CODE = 647;
    public final int NEW_ITEM_REQUEST_CODE = 648;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //use "activity_main.xml" as the layout
        setContentView(R.layout.activity_main);
        //reference the "listview" variable to the id-"listview" in the layout
        listview = (ListView) findViewById(R.id.listview);
        addItemEditText = (EditText) findViewById(R.id.add_item_sub);
        //create an ArrayList of String
        items = new ArrayList<String>();

        //must call it befer creating the adapter, because it references the right item list
        readItemsFromDatabase();

        //Create an adapter for the list view using Android's built-in item layout
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        //connect the listview and the adapter
        listview.setAdapter(itemsAdapter);

        setupListViewListener();
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
//        super.onActivityReenter(resultCode, data);
        Toast.makeText(this, data.getStringExtra("item"), Toast.LENGTH_SHORT).show();

    }

    public void onAddItemClick(View view) {
        Intent editItemIntent = new Intent(this, EditToDoItemActivity.class);
        startActivity(editItemIntent);
        //finish();
        String toAddString = addItemEditText.getText().toString();
        itemsAdapter.add(toAddString);
        addItemEditText.setText("");
        saveItemsToDatabase();
        /*if (toAddString != null && toAddString.length() > 0) {
            itemsAdapter.add(toAddString);
            addItemEditText.setText("");
            saveItemsToDatabase();
        }*/
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_ITEM_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Extract name value from result extras
                String editedItem = data.getExtras().getString("item");
                int position = data.getIntExtra("position", -1);
                items.set(position, editedItem);
                Log.i("Updated Item in list:", editedItem + ",position:"
                        + position);

                Toast.makeText(this, "updated:" + editedItem, Toast.LENGTH_SHORT).show();
                itemsAdapter.notifyDataSetChanged();

                saveItemsToDatabase();

            }
        }
        else if (requestCode == NEW_ITEM_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                String newitem = data.getExtras().getString("item");
                items.add(newitem);
                itemsAdapter.notifyDataSetChanged();

                saveItemsToDatabase();
            }
        }
    }
    public void newItem(View view) {

        Intent intent = new Intent(MainActivity.this, NewToDoItemActivity.class);
        if (intent != null) {
            // put "extras" into the bundle for access in the edit activity
            intent.putExtra("item", "");
            // brings up the second activity
            startActivityForResult(intent, NEW_ITEM_REQUEST_CODE);
            itemsAdapter.notifyDataSetChanged();
        }
    }
    private void setupListViewListener() {
//        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            public boolean onItemLongClick(AdapterView<?> parent, View view,int position,long rowId)
//            {
//                Log.i("MainActivity", "Long Clicked item " + position);
//                return true;
//            }
//        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // @Override
            // public void onItemClick(AdapterView <? > parent, View view,
            //                         int position, long id) {
            //     String updateItem = (String) itemsAdapter.getItem(position);
            //     Log.i("MainActivity", "Clicked item " + position + ": " + updateItem);
            // }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                String updateItem = (String) itemsAdapter.getItem(position);
                Log.i("MainActivity", "Clicked item " + position + ": " + updateItem);

                Intent intent = new Intent(MainActivity.this, EditToDoItemActivity.class);
                if (intent != null) {
                    // put "extras" into the bundle for access in the edit activity
                    intent.putExtra("item", updateItem);
                    intent.putExtra("position", position);
                    // brings up the second activity
                    startActivityForResult(intent, EDIT_ITEM_REQUEST_CODE);
                    itemsAdapter.notifyDataSetChanged();
                }
            }
        });

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long rowId) {
                Log.i("MainActivity", "Long Clicked item " + position);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.dialog_delete_title)
                        .setMessage(R.string.dialog_delete_msg)
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //delete the item
                                items.remove(position);
                                itemsAdapter.notifyDataSetChanged();
                                saveItemsToDatabase();

                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //User cancelled the dialog
                                //nothing happens
                            }
                        });

                builder.create().show();
                return true;
            }
        });
    }

    private void readItemsFromFile() {
        //retrieve the app's private folder.
        //this folder cannot be accessed by other apps
        File filesDir = getFilesDir();
        //prepare a file to read the data
        File todoFile = new File(filesDir, "todo.txt");
        //if file does not exist, create an empty list
        if (!todoFile.exists()) {
            items = new ArrayList<String>();
        } else {
            try {
                //read data and put it into the ArrayList
                items = new ArrayList<String>(FileUtils.readLines(todoFile));
            } catch (IOException ex) {
                items = new ArrayList<String>();
            }
        }
    }

    private void saveItemsToFile() {
        File filesDir = getFilesDir();
        //using the same file for reading. Should use define a global string instead.
        File todoFile = new File(filesDir, "todo.txt");
        try {
            //write list to file
            FileUtils.writeLines(todoFile, items);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    private void readItemsFromDatabase() {
        //read items from database
        List<ToDoItem> itemsFromORM = ToDoItem.listAll(ToDoItem.class);
        items = new ArrayList<String>();
        if (itemsFromORM != null & itemsFromORM.size() > 0) {
            for (ToDoItem item : itemsFromORM) {
                items.add(item.todo);
            }
        }
    }

    private void saveItemsToDatabase() {
        ToDoItem.deleteAll(ToDoItem.class);
        for (String todo:items){
            ToDoItem item = new ToDoItem(todo);
            item.save();
            Log.i("SQL saved item: ",todo);
        }
    }
}