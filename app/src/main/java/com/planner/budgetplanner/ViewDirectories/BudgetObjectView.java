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
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.planner.budgetplanner.Adapters.MyItemAdapter;
import com.planner.budgetplanner.CustomAppBarActivity;
import com.planner.budgetplanner.Model.BudgetObject;
import com.planner.budgetplanner.R;
import com.planner.budgetplanner.Utility.FilterType;
import com.planner.budgetplanner.Utility.MyDialogWindow;
import com.planner.budgetplanner.Utility.MyUtility;
import com.planner.budgetplanner.Utility.SortType;
import com.planner.budgetplanner.Utility.Trash;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BudgetObjectView<T1 extends MyItemAdapter<T2>,T2 extends BudgetObject> extends CustomAppBarActivity implements MyItemAdapter.IItemSwipeListner<T2>, AdapterView.OnItemSelectedListener {

    static final String TAG = "BudgetObjectView";
    protected SearchView searchView;
    protected RecyclerView searchRecyclerView;
    protected RecyclerView recyclerView;
    protected ImageView searchClose;
    protected boolean isSearchEnabled;
    protected View homeView;
    protected ArrayList<T2> orginList;
    protected ArrayList<T2> tempList;
    protected T1 adapter;
    protected ImageButton sortBtn;
    protected Snackbar undoSnackBar;
    protected SortType lastSortType = SortType.Name;
    protected boolean isAscendant = true;

    protected Spinner spinner;
    protected View customPickerView;
    protected ImageButton fromDateBtn;
    protected ImageButton toDateBtn;
    protected Date fromDate;
    protected Date toDate;
    protected FilterType type=FilterType.LIFETIME;

    protected void initialize(String title, View homeView, RecyclerView recyclerView) {
        super.initialize(title);
        enableBackBtn();
        sortBtn = findViewById(R.id.sortBtn);
        sortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortBtnClick();
            }
        });
        this.recyclerView = recyclerView;
        this.homeView = homeView;
        initFilterView();
        MyUtility.sortList(tempList, lastSortType, isAscendant);
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

        adapter.enableSwipeToDeleteAndUndo(homeView, recyclerView, this);
        adapter.enableSwipeToDeleteAndUndo(homeView, searchRecyclerView, this);
    }

    protected void initFilterView()
    {
        spinner = findViewById(R.id.budgetFilter);
        customPickerView = findViewById(R.id.customCalPickView);
        fromDateBtn = findViewById(R.id.caleFromPickBtn);
        toDateBtn = findViewById(R.id.caleToPickBtn);
        spinner.setSelection(idForFilterType(type));

        spinner.setOnItemSelectedListener(this);
        fromDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog fromDialog = MyUtility.displayDatePickerWindow(BudgetObjectView.this);

                TextView fromOkBtn=fromDialog.findViewById(R.id.okBtn);
                final DatePicker fromPicker=fromDialog.findViewById(R.id.datePicker);
                TextView fromCancelBtn=fromDialog.findViewById(R.id.cancelBtn);

                if(fromDate!=null)
                    MyUtility.setDateForDatePicker(fromPicker,fromDate,null);

                fromOkBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fromDate=MyUtility.getPickerDate(fromPicker);
                        if(fromDate!=null&&toDate!=null) {
                            tempList = (ArrayList<T2>) MyUtility.filterList(orginList, fromDate, toDate);
                            adapter.setList(tempList);
                            MyUtility.sortList(tempList, lastSortType, isAscendant);
                            updateOverView();
                        }
                        fromDialog.dismiss();
                    }
                });

                fromCancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fromDialog.dismiss();
                    }
                });
            }
        });
        toDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog toDialog = MyUtility.displayDatePickerWindow(BudgetObjectView.this);

                TextView toOkBtn=toDialog.findViewById(R.id.okBtn);
                TextView toCancelBtn=toDialog.findViewById(R.id.cancelBtn);
                final DatePicker toPicker=toDialog.findViewById(R.id.datePicker);


                if(toDate!=null)
                    MyUtility.setDateForDatePicker(toPicker,toDate,null);

                toOkBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toDate=MyUtility.getPickerDate(toPicker);
                        if(fromDate!=null&&toDate!=null) {
                            tempList = (ArrayList<T2>) MyUtility.filterList(orginList, fromDate, toDate);
                            adapter.setList(tempList);
                            MyUtility.sortList(tempList, lastSortType, isAscendant);
                            updateOverView();
                        }
                        toDialog.dismiss();
                    }
                });
                toCancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toDialog.dismiss();
                    }
                });
            }
        });
    }

    protected void sortBtnClick() {
        final Dialog dialog = MyDialogWindow.defaultSortDialog(this, null, false);

        TextView okBtn = dialog.findViewById(R.id.okBtn);
        TextView cancelBtn = dialog.findViewById(R.id.cancelBtn);
        final RadioButton nameBtn = dialog.findViewById(R.id.nameRadioBtn);
        final RadioButton amountBtn = dialog.findViewById(R.id.amountRadioBtn);
        RadioButton dateBtn = dialog.findViewById(R.id.dateRadioBtn);
        final RadioButton ascenBtn = dialog.findViewById(R.id.ascendantBtn);
        RadioButton descendant = dialog.findViewById(R.id.descendantBtn);

        if (lastSortType == SortType.Name) {
            nameBtn.setChecked(true);
        } else if (lastSortType == SortType.Amount) {
            amountBtn.setChecked(true);
        } else {
            dateBtn.setChecked(true);
        }

        if (isAscendant)
            ascenBtn.setChecked(true);
        else
            descendant.setChecked(true);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastSortType = nameBtn.isChecked() ? SortType.Name : amountBtn.isChecked() ? SortType.Amount : SortType.Date;
                isAscendant = ascenBtn.isChecked();
                MyUtility.sortList(tempList, lastSortType, isAscendant);
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
        if(isSearchEnabled)
        {
            searchView.setIconified(true);
            searchView.onActionViewCollapsed();
            onCloseSearchView();
        }
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

    protected void onCloseSearchView() {
        isSearchEnabled = false;
        homeView.setVisibility(View.VISIBLE);
        findViewById(R.id.searchViewLayout).setVisibility(View.INVISIBLE);
        adapter.setList(tempList);
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

    protected void setupSearchView(final T1 adapter, final ISearchListner<T2> searchListner) {
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

                ArrayList<T2> newList = MyUtility.filterWithName(tempList, newText);
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
        undoSnackBar = snackbar;
    }

    @Override
    public void onRemove(T2 item, int pos) {
        orginList.remove(item);
        tempList.remove(item);
    }

    @Override
    public void onRestore(T2 item, int pos) {
        if (!orginList.contains(item))
            orginList.add(item);
        if (!tempList.contains(item))
            tempList.add(item);
    }

    @Override
    public void onHideSnackBar(int event) {
        undoSnackBar = null;
        Trash.clearTrash();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (undoSnackBar != null)
            undoSnackBar.dismiss();
    }

    protected void updateOverView()
    {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        try {
            type = filterTypeForId(position);
            if(type==FilterType.CUSTOM)
                customPickerView.setVisibility(View.VISIBLE);
            else
                customPickerView.setVisibility(View.GONE);
            filterList(type);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void filterList(FilterType type)
    {
        if (type == FilterType.CUSTOM) {
            if(fromDate!=null&&toDate!=null) {
                tempList = (ArrayList<T2>) MyUtility.filterList(orginList, fromDate, toDate);
            }
        } else {
            tempList = (ArrayList<T2>) MyUtility.filterList(orginList, type);
        }
        adapter.setList(tempList);
        MyUtility.sortList(tempList, lastSortType, isAscendant);
        updateOverView();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public interface ISearchListner<T> {
        void onResult(ArrayList<T> result);
    }

    protected int idForFilterType(FilterType type) {
        switch (type) {
            case SEVENDAY:
                return 0;
            case THIRTYDAY:
                return 1;
            case HUNDREDEIGHTYDAY:
                return 2;
            case THREESIXTYDAY:
                return 3;
            case LIFETIME:
                return 4;
            case CUSTOM:
                return 5;
            default:
                return -1;
        }
    }

    protected FilterType filterTypeForId(int id) throws Exception {
        switch (id) {
            case 0:
                return FilterType.SEVENDAY;
            case 1:
                return FilterType.THIRTYDAY;
            case 2:
                return FilterType.HUNDREDEIGHTYDAY;
            case 3:
                return FilterType.THREESIXTYDAY;
            case 4:
                return FilterType.LIFETIME;
            case 5:
                return FilterType.CUSTOM;
            default:
                throw new Exception("Invalid Filter Id");
        }
    }
}