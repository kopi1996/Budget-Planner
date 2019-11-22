package com.planner.budgetplanner;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.planner.budgetplanner.Adapters.MyItemAdapter;
import com.planner.budgetplanner.Model.BudgetObject;
import com.planner.budgetplanner.Model.Category;
import com.planner.budgetplanner.Utility.MyUtility;

import java.util.ArrayList;

public class CategoryAdd extends BudgetObjectAdd<Category> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_add);


        initialize("Add Category");

        findViewById(R.id.cateHintBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayHintCateTitle();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displayHintCateTitle()
    {
        final ArrayList<BudgetObject> elements=new ArrayList<>();
        elements.add(new BudgetObject("1",""));

        elements.add(new BudgetObject("1",""));

        elements.add(new BudgetObject("1",""));

        elements.add(new BudgetObject("1",""));

        elements.add(new BudgetObject("1",""));

        elements.add(new BudgetObject("1",""));

        elements.add(new BudgetObject("1",""));
        elements.add(new BudgetObject("1",""));

        elements.add(new BudgetObject("1",""));

        elements.add(new BudgetObject("1",""));

        elements.add(new BudgetObject("1",""));

        elements.add(new BudgetObject("1",""));

        elements.add(new BudgetObject("1",""));

        elements.add(new BudgetObject("1",""));

        elements.add(new BudgetObject("1",""));

        elements.add(new BudgetObject("1",""));

        elements.add(new BudgetObject("1",""));

        elements.add(new BudgetObject("1",""));

        elements.add(new BudgetObject("1",""));

        elements.add(new BudgetObject("1",""));
        Dialog dialog = MyUtility.displayHintDialog(this, elements, new MyItemAdapter.IItemListner() {
            @Override
            public void onClick(View v, int pos) {
                Toast.makeText(CategoryAdd.this, elements.get(pos).getTitle(), Toast.LENGTH_LONG).show();
            }
        } ,true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.category_menu,menu);

        return true;
    }
}
