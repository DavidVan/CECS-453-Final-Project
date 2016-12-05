package edu.csulb.suitup;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.List;
import java.util.Random;

public class RandomWardrobeActivity extends AppCompatActivity implements View.OnClickListener{

    private Random mRand = new Random();
    private WardrobeCombination mCurrentCombination; 
    private List<Wardrobe> mTopList;
    private List<Wardrobe> mBottomList;
    private List<Wardrobe> mShoesList;

    private List<WardrobeCombination> mExclusions;

    private ImageView topview;
    private ImageView bottomview;
    private ImageView shoesview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_wardrobe);

        //Initialize Views
        topview = (ImageView) findViewById(R.id.top_view);
        bottomview  = (ImageView) findViewById(R.id.bottom_view);
        shoesview = (ImageView) findViewById(R.id.shoes_view);

        // Get list of wardrobe from database
        WardrobeDbHelper dbhelper = new WardrobeDbHelper(getApplicationContext());

        mTopList = dbhelper.getTop();
        mBottomList = dbhelper.getBottom();
        mShoesList = dbhelper.getShoes();

        // get list of exclusions from database
        mExclusions = dbhelper.getExclusions();

        // Ends activity if user does not have anything in one of the categories
        if(mTopList.size() < 1){
            Toast.makeText(getApplicationContext(), "Please add a top apparel", Toast.LENGTH_LONG).show();
            finish();
        }
        else if(mBottomList.size() < 1){
            Toast.makeText(getApplicationContext(), "Please add a bottom apparel", Toast.LENGTH_LONG).show();
            finish();
        }
        else if(mShoesList.size() < 1){
            Toast.makeText(getApplicationContext(), "Please add a pair of shoes", Toast.LENGTH_LONG).show();
            finish();
        }
        else {
            generateCombination();
        }

        // Event Listeners for Buttons
        Button generateButton= (Button) findViewById(R.id.generate_button);
        Button excludeButton = (Button) findViewById(R.id.exclude_button);

        generateButton.setOnClickListener(this);
        excludeButton.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if(v.getId()== R.id.generate_button){
            generateCombination();
        }
        if(v.getId()== R.id.exclude_button){
            excludeCombination();
        }
    }

    // Will return a random wardrobe chosen from a list of wardrobes
    private Wardrobe generateRandom(List<Wardrobe> list){
        int size = list.size();
        return list.get(mRand.nextInt(size));
    }

    private void generateCombination(){
        // Make sure you have enough wardrobe in store, taking in account the exclusions.
        int possibleCombinations = mTopList.size() * mBottomList.size() * mShoesList.size();
        if (possibleCombinations <= mExclusions.size()){
            Toast.makeText(getApplicationContext(), "Not enough Apparel added to make a combination",
                    Toast.LENGTH_SHORT).show();
            finish();
        }

        WardrobeCombination wardcombo;
        Wardrobe randtop;
        Wardrobe randbottom;
        Wardrobe randshoes;

        boolean uniquefound = false;

        // keep looking for a combination until you find one that's not excluded
        while(!uniquefound) {
            randtop = generateRandom(mTopList);
            randbottom = generateRandom(mBottomList);
            randshoes = generateRandom(mShoesList);

            wardcombo = new WardrobeCombination(randtop.getId(), randbottom.getId(), randshoes.getId());
            // If new generated combo is not the same as the current one
            if(mCurrentCombination == null || !mCurrentCombination.equals(wardcombo)) {
                // if the new generated combo is not in the exclude list
                if (!mExclusions.contains(wardcombo)) {
                    uniquefound = true;
                    mCurrentCombination = wardcombo;

                    // Set View's Images
                    topview.setImageBitmap(BitmapFactory.decodeFile(randtop.getFilepath()));
                    bottomview.setImageBitmap(BitmapFactory.decodeFile(randbottom.getFilepath()));
                    shoesview.setImageBitmap(BitmapFactory.decodeFile(randshoes.getFilepath()));

                    Animation myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);
                    topview.startAnimation(myFadeInAnimation); //Set animation to your ImageView
                    bottomview.startAnimation(myFadeInAnimation);
                    shoesview.startAnimation(myFadeInAnimation);
                }
            }
        }
    }

    // Adds the current combination displayed to the database as excluded.
    public void excludeCombination(){
        mExclusions.add(mCurrentCombination);

        WardrobeDbHelper dbHelper = new WardrobeDbHelper(getApplicationContext());
        dbHelper.addExclusion(mCurrentCombination);

        // Make a new combination to replace current one on the GUI
        generateCombination();
    }
}
