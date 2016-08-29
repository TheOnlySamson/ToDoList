package comp5216.sydney.edu.au.todolist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class AddToDoItemActivity extends Activity {
    ListView listview;
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    EditText addItemEditText;
    public int position=0;
    EditText etItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //use "activity_main.xml" as the layout
        setContentView(R.layout.edit_add_item);
        //reference the "listview" variable to the id-"listview" in the layout
        listview = (ListView) findViewById(R.id.listview);
        addItemEditText = (EditText) findViewById(R.id.txtNewItem);
        //create an ArrayList of String
        items = new ArrayList<String>();
        items.add("item one");
        items.add("item two");

        //must call it befer creating the adapter, because it references the right item list
        //readItemsFromFile();

        //Create an adapter for the list view using Android's built-in item layout
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        //connect the listview and the adapter
        listview.setAdapter(itemsAdapter);

        //setupListViewListener();
    }

    public void onAddItemClick(View view){
        String toAddString = addItemEditText.getText().toString();
        if(toAddString != null && toAddString.length() > 0){
            itemsAdapter.add(toAddString);
            addItemEditText.setText("");
        }
    }

}