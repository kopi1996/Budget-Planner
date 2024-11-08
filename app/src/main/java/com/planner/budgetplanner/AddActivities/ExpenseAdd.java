package com.planner.budgetplanner.AddActivities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.planner.budgetplanner.Adapters.BudgetObjectAdapter;
import com.planner.budgetplanner.Adapters.MyItemAdapter;
import com.planner.budgetplanner.FirebaseManager;
import com.planner.budgetplanner.MainActivity;
import com.planner.budgetplanner.Model.BudgetObject;
import com.planner.budgetplanner.Model.Category;
import com.planner.budgetplanner.Model.Expense;
import com.planner.budgetplanner.Model.Income;
import com.planner.budgetplanner.R;
import com.planner.budgetplanner.Utility.MyUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class ExpenseAdd extends BudgetObjectAdd<Expense> implements View.OnFocusChangeListener,OnSuccessListener<Date> {

    public static final String EXPENSE_EDIT="ExpenseEdit";
    public static final String EXPENSE_ID="ExpenseId";
    public static final String EXPENSE_CAT_ID="ExpenseCatId";

    private static final String TAG = "ExpenseAdd";
    private TextInputEditText pickCateBtn;
    private TextInputEditText titleTxt;
    private TextInputEditText amountTxt;
    private TextInputEditText descriptionTxt;

    private TextInputLayout pickCateBtnPar;
    private TextInputLayout titleTxtPar;
    private TextInputLayout amountTxtPar;
    private TextInputLayout descriptionTxtPar;

    private Button pickerBtn;
    private Expense expense;
    private Category pickedCategory;
    private Date pickedDate;

    private boolean isSavingProgress;
    private boolean isUpdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_add);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isUpdate = bundle.getBoolean(EXPENSE_EDIT, false);
            if (isUpdate) {
                String id = bundle.getString(EXPENSE_ID);
                if (id != null)
                    expense = MyUtility.currentUser.getExpenseForId(id);
            }
            String catId=bundle.getString(EXPENSE_CAT_ID);
            if(catId!=null)
               pickedCategory = MyUtility.currentUser.getCategoryForId(catId);
        }

        if (expense == null)
            isUpdate = false;

        initialize(isUpdate ? "Edit Expense" : "Add Expense", expense);
    }

    @Override
    protected void initialize(String title,Expense expense) {
        super.initialize(title,expense);
        pickerBtn = findViewById(R.id.datePickerBtn);

        pickCateBtn = findViewById(R.id.pickCateBtn);
        titleTxt = findViewById(R.id.expAddTitleTxt);
        amountTxt = findViewById(R.id.expAddAmountTxt);
        descriptionTxt = findViewById(R.id.expAddDesTxt);

        amountTxt.setHint("Amount("+MyUtility.currentUser.getCurrencyType()+")");

        pickCateBtnPar = findViewById(R.id.pickCateBtnPar);
        titleTxtPar = findViewById(R.id.expAddTitleTxtPar);
        amountTxtPar = findViewById(R.id.expAddAmountTxtPar);
        descriptionTxtPar = findViewById(R.id.expAddDesTxtPar);
        pickedDate = new Date();

        if (isUpdate) {
            pickCateBtn.setClickable(false);
            pickCateBtn.setAlpha(0.5f);
        }
        if(pickedCategory!=null) {
            pickCateBtn.setText(pickedCategory.getTitle());
        }
        if (expense != null) {
            pickCateBtn.setText(expense.getCategory().getTitle());
            titleTxt.setText(expense.getTitle());
            amountTxt.setText(Double.toString(expense.getAmount()));
            descriptionTxt.setText(expense.getDescription());
            if (expense.getTimestamp() != null)
                pickedDate = MyUtility.convertFromUtcToStamp(expense.getTimestamp().toDate()).toDate();
        }

        pickerBtn.setOnFocusChangeListener(this);
        titleTxt.setOnFocusChangeListener(this);
        amountTxt.setOnFocusChangeListener(this);
        descriptionTxt.setOnFocusChangeListener(this);
        pickerBtn.setText(MyUtility.convertDateToString(pickedDate));
        pickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayDatePicker(ExpenseAdd.this);
            }
        });
        if (!isUpdate)
            pickCateBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    displayHintDialog();
                }
            });
    }

    private void displayDatePicker(final OnSuccessListener<Date> listener) {
        final Dialog dialog = MyUtility.displayDatePickerWindow(this);
        final DatePicker picker = dialog.findViewById(R.id.datePicker);
        MyUtility.setDateForDatePicker(picker,pickedDate,null);
        final View okBtn = dialog.findViewById(R.id.okBtn);
        final View cancelBtn = dialog.findViewById(R.id.cancelBtn);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSuccess(MyUtility.getPickerDate(picker));
                //pickerBtn.setText(MyUtility.getPickerDate(picker));
                
                dialog.dismiss();
                okBtn.setOnClickListener(null);
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSuccess(null);
                dialog.dismiss();
                cancelBtn.setOnClickListener(null);
            }
        });
    }


    public void onFocusChange(View v, boolean hasFocus) {
        if(!hasFocus)
            return;
        switch (v.getId()) {
            case R.id.pickCateBtn:
                pickCateBtnPar.setError(null);
                break;
            case R.id.expAddTitleTxt:
                titleTxtPar.setError(null);
                break;
            case R.id.expAddAmountTxt:
                amountTxtPar.setError(null);
                break;
            default:
                return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.category_menu, menu);

        return true;
    }

    @Override
    public void onBackPressed() {
        if(!isSavingProgress)
            super.onBackPressed();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveBtn:
                if (!isUpdate)
                    addExpense();
                else
                    updateExpense();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addExpense() {
        if (getCurrentFocus() != null) {
            MyUtility.hideKeyboardFrom(this, getCurrentFocus());
            getCurrentFocus().clearFocus();
        }
        if (!checkEverythingReady())
            return;
        isSavingProgress = true;
        MyUtility.enableLoading(this);
        Timestamp timestamp = MyUtility.convDateToUtcTimeStamp(pickedDate);
        final Expense expense = new Expense(pickedCategory, titleTxt.getText().toString(), descriptionTxt.getText().toString(), timestamp, Double.parseDouble(amountTxt.getText().toString()));
        FirebaseManager.addExpenseIntoDB(expense, new OnSuccessListener<Expense>() {
            @Override
            public void onSuccess(Expense expense1) {
                MyUtility.disableLoading(ExpenseAdd.this);
                isSavingProgress = false;
                if (expense1 != null) {
                    MyUtility.currentUser.addExpenses(expense1);
                    finish();
                } else {

                }
            }
        });
    }

    private void updateExpense() {
        if (getCurrentFocus() != null) {
            MyUtility.hideKeyboardFrom(this, getCurrentFocus());
            getCurrentFocus().clearFocus();
        }
        if (!checkEverythingReady())
            return;
        isSavingProgress = true;
        MyUtility.enableLoading(this);
        Timestamp timestamp = MyUtility.convDateToUtcTimeStamp(pickedDate);
        expense.setTitle(titleTxt.getText().toString());
        expense.setDescription(descriptionTxt.getText().toString());
        expense.setAmount(Double.parseDouble(amountTxt.getText().toString()));
        expense.setTimestamp(timestamp);
        FirebaseManager.updateExpense(expense, new OnSuccessListener<Boolean>() {
            @Override
            public void onSuccess(Boolean success) {
                MyUtility.disableLoading(ExpenseAdd.this);
                isSavingProgress = false;
                if (success != null) {
                    finish();
                } else {

                }
            }
        });
    }

    private boolean checkEverythingReady()
    {
        if(pickCateBtn.getText().toString().isEmpty())
        {
            pickCateBtnPar.setError("Pick a category");
            return false;
        }
        if(titleTxt.getText().toString().isEmpty())
        {
            titleTxtPar.setError("Enter a title");
            return false;
        }
        if(amountTxt.getText().toString().isEmpty())
        {
            amountTxtPar.setError("Enter a amount");
            return false;
        }

        return true;
    }

    private boolean checkCatAddReady(TextInputLayout titleTxtPar2,TextInputLayout amountTxtPar2,TextInputEditText titleInp,TextInputEditText amountInp) {
        if (titleInp.getText().toString().isEmpty()) {
            Log.i(TAG, "checkCatAddReady: ");
            titleTxtPar2.setError("Enter title");
            return false;
        }
        Category[] categories = MyUtility.currentUser.getCategories();
        for (Category category1 : categories) {
            if(category1.getTitle().equals(titleInp.getText().toString())) {
                titleTxtPar2.setError("This title already exist");
                return false;
            }
        }
        if (amountInp.getText().toString().isEmpty()) {
            amountTxtPar2.setError("Enter a amount");
            return false;
        }

        return true;
    }

    private void displayHintDialog() {
        final ArrayList<BudgetObject> elements=new ArrayList<>();
        elements.addAll(Arrays.asList(MyUtility.currentUser.getCategories()));

        int width = 0;
        int height = 0;
        Point size = new Point();
        WindowManager w = this.getWindowManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            w.getDefaultDisplay().getSize(size);
            width = size.x;
            height = size.y;
        } else {
            Display d = w.getDefaultDisplay();
            width = d.getWidth();
            height = d.getHeight();
        }


        int dialogHeight = (int) ((height / 100.0) * 75);

        final Dialog dialog = new Dialog(this,R.style.DialogTitleTheme);
        dialog.setContentView(R.layout.category_pick_dialog);
        dialog.setTitle("Pick Category");
        final WindowManager.LayoutParams lWindowParams = new WindowManager.LayoutParams();
        lWindowParams.copyFrom(dialog.getWindow().getAttributes());
        lWindowParams.height = dialogHeight;

        final RecyclerView myListView = dialog.findViewById(R.id.catePickDiaViewList);
        myListView.setHasFixedSize(true);
        myListView.setNestedScrollingEnabled(false);
        myListView.setItemAnimator(new DefaultItemAnimator());

        final BudgetObjectAdapter adapter=new BudgetObjectAdapter(elements, new MyItemAdapter.IItemListner() {
            @Override
            public void onClick(View v, int pos) {
                dialog.dismiss();
                pickCateBtn.setText(elements.get(pos).getTitle());
                pickedCategory = (Category) elements.get(pos);
                Log.i(TAG, "onClick cate: " + pickedCategory.getBudget());
            }
        });

        dialog.findViewById(R.id.catPickDiaCancelBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.catPickDiaAddBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog1=new Dialog(ExpenseAdd.this,R.style.DialogTitleTheme);
                dialog1.setContentView(R.layout.category_add_dialog_box);
                dialog1.setTitle("Add Category");
                dialog1.show();

                final TextInputLayout titleTxt2Par=dialog1.findViewById(R.id.cateDialogTitlePar);
                final TextInputLayout amountTxt2Par=dialog1.findViewById(R.id.cateDialogAmountPar);

                final TextInputEditText titleTxt2=dialog1.findViewById(R.id.catDialogTitle);
                final TextInputEditText desTxt2=dialog1.findViewById(R.id.catDialogDes);
                final TextInputEditText amountTxt2=dialog1.findViewById(R.id.catDialogAmount);
                final View homeView2=dialog1.findViewById(R.id.homeView);
                View okBtn = dialog1.findViewById(R.id.catDialOkBtn);
                View cancelBtn = dialog1.findViewById(R.id.catDialCancelBtn);

                titleTxt2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(hasFocus)
                        {
                            titleTxt2Par.setError(null);
                        }
                    }
                });

                amountTxt2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(hasFocus)
                        {
                            amountTxt2Par.setError(null);
                        }
                    }
                });

                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (dialog1.getCurrentFocus() != null) {
                            MyUtility.hideKeyboardFrom(dialog1.getContext(), dialog1.getCurrentFocus());
                            dialog1.getCurrentFocus().clearFocus();
                        }
                        if(!checkCatAddReady(titleTxt2Par,amountTxt2Par,titleTxt2,amountTxt2))
                            return;
                        addCategory(dialog1,homeView2,titleTxt2, desTxt2, amountTxt2, new OnSuccessListener<Category>() {
                            @Override
                            public void onSuccess(Category category) {
                                if(category!=null)
                                {
                                    MyUtility.currentUser.addCategories(category);
                                    dialog1.dismiss();
                                    displayHintDialog();
                                }
                                else
                                    Toast.makeText(ExpenseAdd.this,"Something went wrong",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                       displayHintDialog();
                    }
                });

                dialog.dismiss();
            }
        });


        myListView.setAdapter(adapter);
        ViewGroup.LayoutParams layoutParams = myListView.getLayoutParams();

        int minListViewHeight = (int) ((dialogHeight));
        if(adapter.getItemCount()>9)
            layoutParams.height=minListViewHeight;
        myListView.setLayoutParams(layoutParams);
        dialog.show();

    }

    private void addCategory(final Dialog dialog, final View home, TextInputEditText title, TextInputEditText des, TextInputEditText amount, final OnSuccessListener<Category> listener) {

        MyUtility.enableLoading(dialog,home);
        Timestamp timestamp = MyUtility.convDateToUtcTimeStamp(new Date());
        Category category = new Category(title.getText().toString(), des.getText().toString(), Double.parseDouble(amount.getText().toString()), timestamp);
        FirebaseManager.addCategoryIntoDB(category, new OnSuccessListener<Category>() {
            @Override
            public void onSuccess(Category category1) {
                MyUtility.disableLoading(dialog,home);
                if (listener != null)
                    listener.onSuccess(category1);
            }
        });
    }

    @Override
    public void onSuccess(Date date) {
        if (date != null) {
            pickedDate=date;
            pickerBtn.setText(MyUtility.convertDateToString(date));
        }
    }
}