package com.planner.budgetplanner.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.planner.budgetplanner.Model.Category;
import com.planner.budgetplanner.R;

import java.util.ArrayList;

public class CategoryAdapter extends ArrayAdapter<Category> implements View.OnClickListener {

    private ArrayList<Category> list;
    private Context context;

    public CategoryAdapter(Context context,ArrayList<Category> list)
    {
        super(context, R.layout.category_item,list);
        this.list=list;
        this.context=context;
    }

    @Override
    public void onClick(View v) {
        int position=(Integer) v.getTag();
        Object object= getItem(position);
        Category dataModel=(Category) object;

        switch (v.getId())
        {
            case R.id.cateItem:
                Toast.makeText(context,"Clck",Toast.LENGTH_LONG).show();
                break;
        }
    }


}
