package edu.csulb.suitup;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class ItemEditActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText tagsEditText;
    private EditText descEditText;
    private String desc;
    private String tags;
    private int id;
    private WardrobeDbHelper dbHelper = new WardrobeDbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_edit);

        desc = getIntent().getStringExtra("description");
        tags = getIntent().getStringExtra("tags");
        id = getIntent().getIntExtra("id",10);

        tagsEditText = (EditText)findViewById(R.id.editTagsEditText);
        descEditText = (EditText)findViewById(R.id.editDescEditText);

        tagsEditText.setText(tags);
        descEditText.setText(desc);

        Button saveBtn = (Button)findViewById(R.id.save_record_btn);
        Button cancelBtn = (Button)findViewById(R.id.cancel_record_btn);
        saveBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()== R.id.save_record_btn) {
            String currentTags = tagsEditText.getText().toString();
            String currentDesc = descEditText.getText().toString();
            System.out.println(id);
            if (!currentDesc.equalsIgnoreCase(desc))
                dbHelper.updateDescription(id, currentDesc);
            if (!currentTags.equalsIgnoreCase(tags)) {
                String[] newTags = currentTags.split(",");
                dbHelper.removeTag(id);
                for (int i = 0; i< newTags.length; i ++)
                {
                    dbHelper.addTag(id, newTags[i]);
                }
                //Toast.makeText(this, newTags[2], Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, WardrobeMgmtActivity.class));
            finish();
        }
        if (v.getId()==R.id.cancel_record_btn) {
            startActivity(new Intent(this, WardrobeMgmtActivity.class));
            finish();
        }
    }
}
