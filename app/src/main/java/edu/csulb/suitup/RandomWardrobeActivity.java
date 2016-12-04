package edu.csulb.suitup;

import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

public class RandomWardrobeActivity extends AppCompatActivity {

    private Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_wardrobe);

        WardrobeDbHelper dbhelper = new WardrobeDbHelper(getApplicationContext());

        List<Wardrobe> topList = dbhelper.getTop();
        List<Wardrobe> bottomList = dbhelper.getBottom();
        List<Wardrobe> shoesList = dbhelper.getShoes();

        // Ends activity if user does not have anything in one of the categories
        if(topList.size() < 1){
            Toast.makeText(getApplicationContext(), "Please add a top apparel", Toast.LENGTH_LONG).show();
            finish();
        }
        else if(bottomList.size() < 1){
            Toast.makeText(getApplicationContext(), "Please add a bottom apparel", Toast.LENGTH_LONG).show();
            finish();
        }
        else if(shoesList.size() < 1){
            Toast.makeText(getApplicationContext(), "Please add a pair of shoes", Toast.LENGTH_LONG).show();
            finish();
        }
        else {
            Wardrobe randtop = generateRandom(topList);
            Wardrobe randbottom = generateRandom(bottomList);
            Wardrobe randshoes = generateRandom(shoesList);

            ImageView topview = (ImageView) findViewById(R.id.top_view);
            ImageView bottomview = (ImageView) findViewById(R.id.bottom_view);
            ImageView shoesview = (ImageView) findViewById(R.id.shoes_view);

            Toast.makeText(getApplicationContext(), randtop.getFilepath(), Toast.LENGTH_LONG).show();

            // Set View's Images
            topview.setImageBitmap(BitmapFactory.decodeFile(randtop.getFilepath()));
            bottomview.setImageBitmap(BitmapFactory.decodeFile(randbottom.getFilepath()));
            shoesview.setImageBitmap(BitmapFactory.decodeFile(randshoes.getFilepath()));
        }
    }

    // Will return a random wardrobe from a list of wardrobe
    private Wardrobe generateRandom(List<Wardrobe> list){
        int size = list.size();
        return list.get(rand.nextInt(size));
    }
}
