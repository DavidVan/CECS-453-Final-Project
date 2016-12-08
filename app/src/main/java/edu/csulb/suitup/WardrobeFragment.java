package edu.csulb.suitup;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class WardrobeFragment extends Fragment {
    private GridView gridView;
    private GridViewAdapter gridAdapter;
    private ArrayList<ImageItem> imageItems = new ArrayList<>();;


    public WardrobeFragment() {
        // Required empty public constructor
    }

    public static final WardrobeFragment newInstance(String type)
    {
        WardrobeFragment wardrobeFragment = new WardrobeFragment();
        Bundle bd = new Bundle(1);
        bd.putString("Type", type);
        wardrobeFragment.setArguments(bd);
        return wardrobeFragment;
    }
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wardrobe, container, false);


        ImageItemUtil imageItemUtil = new ImageItemUtil(getActivity());
        imageItems.addAll(imageItemUtil.getData("Top"));
        gridView = (GridView) view.findViewById(R.id.gridViewFragment);
        gridAdapter = new GridViewAdapter(getActivity(), R.layout.wardrobe_item, imageItems);
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageItem item = (ImageItem) parent.getItemAtPosition(position);
                //Create intent
                Intent intent = new Intent(getActivity(), ItemDetailActivity.class);
                intent.putExtra("title", item.getDescription());
                intent.putExtra("image", item.getImage());
                String tags = "";
                for (int i = 0; i < item.getTags().size(); i++)
                {
                    tags += item.getTags().get(i) + ", ";
                }
                String tag = tags.substring(0, tags.length() - 2);
                intent.putExtra("tags", tag);
                intent.putExtra("category", item.getCategory());
                intent.putExtra("id",item.getId());
                intent.putExtra("path", item.getFilepath());
                //Start details activity
                startActivity(intent);
            }
        });

        return view;
    }

    public void setType(String type) {
        ImageItemUtil imageItemUtil = new ImageItemUtil(getActivity());
        switch (type) {
            case ("Top"):
                imageItems.clear();
                imageItems.addAll(imageItemUtil.getData("Top"));
                gridAdapter.notifyDataSetChanged();
                gridView.invalidateViews();
                break;
            case ("Bottom"):
                imageItems.clear();
                imageItems.addAll(imageItemUtil.getData("Bottom"));
                gridAdapter.notifyDataSetChanged();
                gridView.invalidateViews();
                break;
            case ("Shoes"):
                imageItems.clear();
                imageItems.addAll(imageItemUtil.getData("Shoes"));
                gridAdapter.notifyDataSetChanged();
                gridView.invalidateViews();
                break;
            case ("All"):
                imageItems.clear();
                imageItems.addAll(imageItemUtil.getData("All"));
                gridAdapter.notifyDataSetChanged();
                gridView.invalidateViews();
                break;
        }
    }

}
