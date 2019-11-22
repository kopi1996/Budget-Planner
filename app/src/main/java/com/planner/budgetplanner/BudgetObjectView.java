package com.planner.budgetplanner;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import com.planner.budgetplanner.Adapters.MyItemAdapter;
import com.planner.budgetplanner.Model.BudgetObject;
import com.planner.budgetplanner.Utility.MyUtility;

import java.util.ArrayList;

public class BudgetObjectView extends AppCompatActivity {

    private static final String TAG = "BudgetObjectView";
    protected SearchView searchView;
    protected RecyclerView searchRecyclerView;
    protected ImageView searchClose;
    protected boolean isSearchEnabled;
    protected View homeView;


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.searchBtn);
        searchView = (SearchView) item.getActionView();
        int searchCloseButtonId = searchView.getContext().getResources()
                .getIdentifier("android:id/search_close_btn", null, null);
        searchClose = searchView.findViewById(searchCloseButtonId);
        searchClose.setColorFilter(Color.argb(150, 255, 255, 255));

        return true;
    }

    protected void onCloseSearchView()
    {
        isSearchEnabled = false;
        homeView.setVisibility(View.VISIBLE);
        findViewById(R.id.searchViewLayout).setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (isSearchEnabled) {
            searchView.setIconified(true);
            searchView.onActionViewCollapsed();
            onCloseSearchView();
        } else {
            super.onBackPressed();
        }
    }

    protected <T extends MyItemAdapter<T2>, T2 extends BudgetObject> void setupSearchView(final T adapter, final ArrayList<T2> list, final ISearchListner<T2> searchListner) {
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSearchEnabled = true;
                searchRecyclerView.setAdapter(adapter);
                homeView.setVisibility(View.INVISIBLE);
                findViewById(R.id.searchViewLayout).setVisibility(View.VISIBLE);
                searchRecyclerView.setVisibility(View.VISIBLE);
                findViewById(R.id.catEmptyMsgTxt).setVisibility(View.INVISIBLE);
            }
        });


        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                onCloseSearchView();
                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.isEmpty()) {
                    searchClose.setAlpha(150);
                } else {
                    searchClose.setAlpha(255);
                }

                ArrayList<T2> newList = MyUtility.filterWithName(list, newText);
                if (newList.isEmpty()) {
                    findViewById(R.id.catEmptyMsgTxt).setVisibility(View.VISIBLE);
                    searchRecyclerView.setVisibility(View.INVISIBLE);
                } else {
                    findViewById(R.id.catEmptyMsgTxt).setVisibility(View.INVISIBLE);
                    searchRecyclerView.setVisibility(View.VISIBLE);
                    searchListner.onResult(newList);
                }
                return false;
            }
        });
    }

    public interface ISearchListner<T>
    {
        void onResult(ArrayList<T> result);
    }
}
