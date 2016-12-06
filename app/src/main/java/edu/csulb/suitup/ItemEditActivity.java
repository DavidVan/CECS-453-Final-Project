package edu.csulb.suitup;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ItemEditActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText tagsEditText;
    private EditText descEditText;
    private String desc;
    private String tags;
    private int id;

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
    }

    @Override
    public void onClick(View v) {
        if(v.getId()== R.id.save_record_btn) {
            String currentTags = tagsEditText.getText().toString();
            String currentDesc = descEditText.getText().toString();

            if (!currentDesc.equalsIgnoreCase(desc))
                Toast.makeText(this, currentDesc + "----" + desc , Toast.LENGTH_SHORT).show();
            if (!currentTags.equalsIgnoreCase(tags)) {
                String[] newTags = currentTags.split(",");
                Toast.makeText(this, newTags[2], Toast.LENGTH_SHORT).show();
            }
        }

    }
}
