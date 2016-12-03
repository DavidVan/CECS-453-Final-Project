package edu.csulb.suitup;

import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.List;
import java.util.Random;

public class RandomWardrobeActivity extends AppCompatActivity {

    private Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_wardrobe);

        WardrobeDbHelper dbhelper = new WardrobeDbHelper(getApplicationContext());

        Wardrobe randtop = generateRandom(dbhelper.getTop());
        Wardrobe randbottom = generateRandom(dbhelper.getBottom());
        Wardrobe randshoes = generateRandom(dbhelper.getShoes());

        ImageView topview = (ImageView) findViewById(R.id.top_view);
        ImageView bottomview = (ImageView) findViewById(R.id.bottom_view);
        ImageView shoesview = (ImageView) findViewById(R.id.shoes_view);

        // Set View's Images
        topview.setImageBitmap(BitmapFactory.decodeFile(randtop.getFilepath()));
        bottomview.setImageBitmap(BitmapFactory.decodeFile(randbottom.getFilepath()));
        shoesview.setImageBitmap(BitmapFactory.decodeFile(randshoes.getFilepath()));

    }

    // Will return a random wardrobe from a list of wardrobe
    private Wardrobe generateRandom(List<Wardrobe> list){
        int size = list.size();
        return list.get(rand.nextInt(size));
    }
}
