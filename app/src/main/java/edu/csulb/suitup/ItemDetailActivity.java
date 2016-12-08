package edu.csulb.suitup;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;

/*
This activity is used to display the images when user clicks in gridView
 */
public class ItemDetailActivity extends AppCompatActivity implements View.OnClickListener{
    private WardrobeDbHelper dbHelper = new WardrobeDbHelper(this);
    String title = "";
    Bitmap bitmap;
    String tags = "";
    int id = 0;
    ImageItemUtil util = new ImageItemUtil(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        title = getIntent().getStringExtra("title");
        //bitmap = getIntent().getParcelableExtra("image");
        String path = getIntent().getStringExtra("path");
        tags = getIntent().getStringExtra("tags");
        id = getIntent().getIntExtra("id",1);

        TextView titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(title);

        ImageView imageView = (ImageView) findViewById(R.id.image);
        File f = new File(path);
        bitmap = util.decodeSampledBitmapFromResource(f, 400, 400);
        imageView.setImageBitmap(bitmap);

        TextView tagsTextView = (TextView)findViewById(R.id.tags);
        tagsTextView.setText(tags);

        Button edit_button = (Button)findViewById(R.id.edit_record_btn);
        Button delete_button = (Button) findViewById(R.id.delete_record_btn);

        edit_button.setOnClickListener(this);
        delete_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()== R.id.edit_record_btn){
            Intent intent = new Intent(this, ItemEditActivity.class);
            intent.putExtra("description", title);
            intent.putExtra("id", id);
            intent.putExtra("tags", tags);
            startActivity(intent);
        }
        if (v.getId()==R.id.delete_record_btn){
            dbHelper.removeWardrobe(id);
            //will need to delete from disk as well.
            startActivity(new Intent(this, WardrobeMgmtActivity.class));
            finish();
        }
    }
}
