package comp5216.sydney.edu.au.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NewToDoItemActivity extends AppCompatActivity {

    EditText editText;
    FloatingActionButton submit;
    MainActivity mainActivity = new MainActivity();
    String msg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_add_item);

        editText = (EditText) findViewById(R.id.add_item_sub);
        submit = (FloatingActionButton) findViewById(R.id.submit_new_item);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                msg = editText.getText().toString();

                // Prepare data intent for sending it back
                Intent data = new Intent();
//
//        // Pass relevant data back as a result
                data.putExtra("item",msg);
//
//
//        // Activity finished ok, return the data
                setResult(RESULT_OK, data); // set result code and bundle data for response
                finish(); // closes the activity, pass data to parent

            }
        });

    }

    public void onNewItemSubmit(View view){
        Toast.makeText(NewToDoItemActivity.this, msg, Toast.LENGTH_LONG).show();
        //mainActivity.setupList(msg);
        startActivity(new Intent(NewToDoItemActivity.this, MainActivity.class));
    }
     public void onnewcanItemClick(View view) {
                 finish();
    }
}
