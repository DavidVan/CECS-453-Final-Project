package edu.csulb.suitup;


import android.app.Fragment;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;


public class WardrobeFragment extends Fragment {
    private FileUtility fileUtility = new FileUtility();
    private ArrayList<ImageItem> imageItems;
    private GridView gridView;
    GridViewAdapter gridAdapter;

    public WardrobeFragment() {
        imageItems = fileUtility.getData();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wardobe, container, false);

        gridView = (GridView) view.findViewById(R.id.gridViewFragment);
        gridAdapter = new GridViewAdapter(getActivity(), R.layout.wardrobe_item, imageItems);
        System.out.println("I run again over here");
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageItem item = (ImageItem) parent.getItemAtPosition(position);
                //Create intent
                Intent intent = new Intent(getActivity(), ItemDetailActivity.class);
                intent.putExtra("title", item.getTitle());
                intent.putExtra("image", item.getImage());

                //Start details activity
                startActivity(intent);
            }
        });


        return view;
    }

    public void setSize(int size)
    {
        ArrayList<ImageItem> temp = fileUtility.getData();
        imageItems.clear();
        imageItems.addAll(temp.subList(0, size));
        for (int i = 0; i < imageItems.size(); i++)
        {
            System.out.println(imageItems.get(i).getTitle());
        }
        gridAdapter.notifyDataSetChanged();
        gridView.invalidateViews();
    }

}
