package com.planner.budgetplanner.ViewDirectories;

import android.app.Dialog;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.TextView;

import com.planner.budgetplanner.Adapters.MyItemAdapter;
import com.planner.budgetplanner.CustomAppBarActivity;
import com.planner.budgetplanner.Model.BudgetObject;
import com.planner.budgetplanner.R;
import com.planner.budgetplanner.Utility.MyDialogWindow;
import com.planner.budgetplanner.Utility.MyUtility;
import com.planner.budgetplanner.Utility.SortType;
import com.planner.budgetplanner.Utility.Trash;

import java.util.ArrayList;

public class BudgetObjectView<T1 extends MyItemAdapter<T2>,T2 extends BudgetObject> extends CustomAppBarActivity implements MyItemAdapter.IItemSwipeListner<T2> {

    static final String TAG = "BudgetObjectView";
    protected SearchView searchView;
    protected RecyclerView searchRecyclerView;
    protected RecyclerView recyclerView;
    protected ImageView searchClose;
    protected boolean isSearchEnabled;
    protected View homeView;
    protected ArrayList<T2> list;
    protected T1 adapter;
    protected ImageButton sortBtn;
    protected Snackbar undoSnackBar;
    protected SortType lastSortType=SortType.Name;
    protected boolean isAscendant=true;

    protected void initialize(String title,View homeView, RecyclerView recyclerView) {
        super.initialize(title);
        enableBackBtn();
        sortBtn=findViewById(R.id.sortBtn);
        sortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortBtnClick();
            }
        });
        this.recyclerView = recyclerView;
        this.homeView = homeView;
        MyUtility.sortList(list,lastSortType,isAscendant);
        searchRecyclerView = findViewById(R.id.searhCateViewList);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchRecyclerView.setHasFixedSize(true);
        searchRecyclerView.setNestedScrollingEnabled(false);
        searchRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        searchRecyclerView.setItemAnimator(new DefaultItemAnimator());


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.enableSwipeToDeleteAndUndo(homeView, recyclerView,this);

        adapter.enableSwipeToDeleteAndUndo(homeView, searchRecyclerView, this);
    }

    protected void sortBtnClick() {
        final Dialog dialog = MyDialogWindow.defaultSortDialog(this, null, false);

        TextView okBtn=dialog.findViewById(R.id.okBtn);
        TextView cancelBtn=dialog.findViewById(R.id.cancelBtn);
        final RadioButton nameBtn=dialog.findViewById(R.id.nameRadioBtn);
        final RadioButton amountBtn=dialog.findViewById(R.id.amountRadioBtn);
        RadioButton dateBtn=dialog.findViewById(R.id.dateRadioBtn);
        final RadioButton ascenBtn=dialog.findViewById(R.id.ascendantBtn);
        RadioButton descendant=dialog.findViewById(R.id.descendantBtn);

        if (lastSortType == SortType.Name) {
            nameBtn.setChecked(true);
        }
        else if (lastSortType == SortType.Amount) {
            amountBtn.setChecked(true);
        }else {
            dateBtn.setChecked(true);
        }

        if(isAscendant)
            ascenBtn.setChecked(true);
        else
            descendant.setChecked(true);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastSortType=nameBtn.isChecked()?SortType.Name:amountBtn.isChecked()?SortType.Amount:SortType.Date;
                isAscendant=ascenBtn.isChecked();
                MyUtility.sortList(list,lastSortType,isAscendant);
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    protected void updateUI() {
        super.updateUI();

        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

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
        adapter.setList(list);
        recyclerView.setAdapter(adapter);
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

    protected void setupSearchView(final T1 adapter, final ArrayList<T2> list, final ISearchListner<T2> searchListner) {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onShowSnackBar(Snackbar snackbar) {
        undoSnackBar=snackbar;
    }

    @Override
    public void onRemove(T2 item, int pos) {
        list.remove(item);
    }

    @Override
    public void onRestore(T2 item, int pos) {
        if (!list.contains(item))
            list.add(item);
    }

    @Override
    public void onHideSnackBar(int event) {
        undoSnackBar=null;
        Trash.clearTrash();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (undoSnackBar != null)
            undoSnackBar.dismiss();
    }

    public interface ISearchListner<T>
    {
        void onResult(ArrayList<T> result);
    }
}
