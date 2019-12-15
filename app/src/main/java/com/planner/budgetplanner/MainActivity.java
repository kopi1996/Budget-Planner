package com.planner.budgetplanner;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.planner.budgetplanner.AddActivities.CategoryAdd;
import com.planner.budgetplanner.AddActivities.ExpenseAdd;
import com.planner.budgetplanner.AddActivities.IncomeAdd;
import com.planner.budgetplanner.Managers.AuthenticationManager;
import com.planner.budgetplanner.Managers.MoneyManager;
import com.planner.budgetplanner.Utility.MyUtility;
import com.planner.budgetplanner.ViewDirectories.CategoryView;
import com.planner.budgetplanner.ViewDirectories.IncomeView;

public class MainActivity extends CustomAppBarActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String TAG="MainActivity";
    private TextView overviewRouPerTxt;
    private TextView overviewTotIncTxt;
    private TextView overviewToExpTxt;
    private TextView overviewProPerTxt;
    private TextView overviewTotSavTxt;
    private TextView totalBudgetedTxt;
    private TextView remianSpentTxt;
    private TextView provBalanceTxt;
    private TextView dispTncBtnTxt;
    private TextView expBtnTxt;

    private ProgressBar overviewProg;
    private ProgressBar budgetedProg;

    int dangerColId;
    int normalColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize("Budget Planner");
    }

    @Override
    protected void initialize(String title) {
        super.initialize(title);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        overviewRouPerTxt=findViewById(R.id.overviewRouPerTxt);
        overviewProg=findViewById(R.id.homeOverviewProgress);
        overviewProPerTxt=findViewById(R.id.overviewProPerTxt);
        overviewTotIncTxt=findViewById(R.id.overviewTotIncTxt);
        overviewToExpTxt=findViewById(R.id.overviewToExpTxt);
        overviewTotSavTxt=findViewById(R.id.overviewTotSaveTxt);
        totalBudgetedTxt=findViewById(R.id.overviewTotBudgetedTxt);
        provBalanceTxt=findViewById(R.id.overviewToProvBalaTxt);
        remianSpentTxt=findViewById(R.id.overviewTotRemSpentTxt);
        budgetedProg=findViewById(R.id.budgetedProg);
        dispTncBtnTxt=findViewById(R.id.dispoIncBtnTxt);
        expBtnTxt=findViewById(R.id.expBtnTxt);

        dangerColId=getResources().getColor(R.color.dangerColor);
        normalColor=totalBudgetedTxt.getTextColors().getDefaultColor();

        TextView name = navigationView.getHeaderView(0).findViewById(R.id.navName);
        name.setText(MyUtility.currentUser.getName());

        TextView email = navigationView.getHeaderView(0).findViewById(R.id.navEmail);
        email.setText(MyUtility.currentUser.getEmail());

        findViewById(R.id.overviewCard).setBackgroundResource(R.drawable.my_cardview);

        findViewById(R.id.netDisposableBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, IncomeView.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.totalExpBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CategoryView.class);
                startActivity(intent);
            }
        });

        addBottomBarEvent();
    }

    private void gotoExpenseAct()
    {
        Intent intent=new Intent(MainActivity.this,ExpenseAdd.class);
        startActivity(intent);
    }

    private void gotoIncomeAct()
    {
        Intent intent=new Intent(MainActivity.this, IncomeAdd.class);
        startActivity(intent);
    }

    private void gotoChartAct()
    {
        Intent intent=new Intent(MainActivity.this, PieChartActivity.class);
        startActivity(intent);
    }

    private void gotoAnalticsAct()
    {
        Intent intent=new Intent(MainActivity.this, ChartActivity.class);
        startActivity(intent);
    }

    private void gotoCatrgotyAct()
    {
        Intent intent=new Intent(MainActivity.this,CategoryAdd.class);
        startActivity(intent);
    }

    private void addBottomBarEvent()
    {
        View viewById = findViewById(R.id.my_bottom_nav);
        viewById.findViewById(R.id.expenseAddBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoExpenseAct();
            }
        });

        viewById.findViewById(R.id.incomeAddBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               gotoIncomeAct();
            }
        });

        viewById.findViewById(R.id.cateAddBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoCatrgotyAct();
            }
        });

        viewById.findViewById(R.id.chartBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoChartAct();
            }
        });
    }

    @Override
    protected void updateUI() {
        super.updateUI();
        String currencyType = "rs";


        double totalIncome = MoneyManager.totalIncome(MyUtility.currentUser);
        double totalExpense = MoneyManager.totalExpenditure(MyUtility.currentUser);
        double totalSaving = totalIncome - totalExpense;
        double percentage = totalIncome == 0 ? 0 : (totalExpense / totalIncome) * 100;

        double totalBudgeted = MoneyManager.totalBudgeted(MyUtility.currentUser);
        double provBalance = totalIncome - totalBudgeted;
        double remainSpent = totalBudgeted - totalExpense;
        double budgetedPer = totalBudgeted == 0 ? 0 : (totalExpense / totalBudgeted) * 100;

        overviewTotIncTxt.setText(totalIncome + currencyType);
        overviewToExpTxt.setText(totalExpense + currencyType);
        overviewTotSavTxt.setText(totalSaving + currencyType);
        overviewProg.setProgress((int) percentage);
        overviewRouPerTxt.setText(MyUtility.wrapDecPointDouble(percentage) + " %");

        totalBudgetedTxt.setText(totalBudgeted + currencyType);
        provBalanceTxt.setText(provBalance + currencyType);
        remianSpentTxt.setText(remainSpent + currencyType);
        overviewProPerTxt.setText(MyUtility.wrapDecPointDouble(budgetedPer) + " %");
        budgetedProg.setProgress((int) budgetedPer);

        Log.i(TAG, "updateUI: " + budgetedPer);

        dispTncBtnTxt.setText(totalIncome + currencyType);
        expBtnTxt.setText(totalExpense + currencyType);

        overviewTotSavTxt.setTextColor(normalColor);
        overviewRouPerTxt.setTextColor(normalColor);

        Drawable overProDrawable = overviewProg.getProgressDrawable();
        overProDrawable.setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);


        if (totalSaving < 0) {
            overviewTotSavTxt.setTextColor(dangerColId);
            overviewRouPerTxt.setTextColor(dangerColId);
            overProDrawable.setColorFilter(dangerColId, PorterDuff.Mode.SRC_IN);
        }
        overviewProg.setProgressDrawable(overProDrawable);

        LayerDrawable budgetedProgDrawable = (LayerDrawable) budgetedProg.getProgressDrawable();
        budgetedProgDrawable.getDrawable(1).setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);

        if (budgetedPer > 100) {
              budgetedProgDrawable.getDrawable(1).setColorFilter(dangerColId, PorterDuff.Mode.SRC_IN);
        }
        budgetedProg.setProgressDrawable(budgetedProgDrawable);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_category) {
            gotoCatrgotyAct();
        } else if (id == R.id.nav_expense) {
            gotoExpenseAct();
        } else if (id == R.id.nav_income) {
            gotoIncomeAct();
        }
        else if(id==R.id.nav_chart)
        {
            gotoAnalticsAct();
        }
        else if (id == R.id.logOutBtn) {
            MyUtility.enableLoading(this);
            AuthenticationManager.logOut(new OnSuccessListener<Boolean>() {
                @Override
                public void onSuccess(Boolean aBoolean) {
                    MyUtility.disableLoading(MainActivity.this);
                    Intent intent = new Intent(MainActivity.this, LoginScreen.class);
                    startActivity(intent);
                    finish();
                }
            });
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
