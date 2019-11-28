package com.planner.budgetplanner;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.planner.budgetplanner.Model.Category;
import com.planner.budgetplanner.Model.Expense;
import com.planner.budgetplanner.Model.Income;
import com.planner.budgetplanner.Model.User;
import com.planner.budgetplanner.Utility.MyUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FirebaseManager {

    private static final String USERS_REF="Users";
    private static final String INCOMES_REF="Incomes";
    private static final String CATEGORIES_REF="Categories";
    private static final String EXPENSES_REF="Expenses";

    public static void initialize()
    {
        getDBInstance().disableNetwork()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.i(TAG, "onComplete internet disable: ");
                    }
                });
        getDBInstance().enableNetwork()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.i(TAG, "onComplete internet enable: ");
                    }
                });
    }

    public static void initializeStateListner(final OnSuccessListener<Boolean> listener)
    {
        final FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(listener!=null)
                    listener.onSuccess(user==null);
            }
        };
        getAuth().addAuthStateListener(authStateListener);
    }

    private static final ArrayList<OnLogoutListner> logOutCallbackListners = new ArrayList<>();

    public static final String TAG="FirebaseManager";

    public static FirebaseFirestore getDBInstance()
    {
        return FirebaseFirestore.getInstance();
    }

    public static FirebaseAuth getAuth()
    {
        return FirebaseAuth.getInstance();
    }

    public static void isUserExist(String key, final OnSuccessListener<Boolean> onSuccessListener, final OnFailureListener onFailureListener) {

        getDBInstance().collection("users").document(key).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    if (onSuccessListener != null)
                        onSuccessListener.onSuccess(task.getResult().exists());
                } else {
                    if (onFailureListener != null)
                        onFailureListener.onFailure(new NullPointerException());
                }
            }
        });
    }

    public static void getUser(final String key, final OnSuccessListener<User> listener) {
        getDBInstance().collection(USERS_REF).document(key).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        String emailString = document.get("email").toString();

                        String fName = document.get("name").toString();

                        User user = new User(key, fName, emailString, LoginType.valueOf(document.get("type").toString()));
                        if (listener != null)
                            listener.onSuccess(user);
                    }
                } else {
                    if (listener != null)
                        listener.onSuccess(null);
                }
            }
        });
    }

    public static void addUserIntoDB(String key, User user, final OnSuccessListener<Boolean> listener) {

        getDBInstance().collection(USERS_REF).document(key).set(user.toJson()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (listener == null)
                    return;
                listener.onSuccess(task.isComplete() && task.isSuccessful());
            }
        });
    }

    public static void loginWithId(Activity activity, String email, String password, final IUserLogin onFinished) {
        getAuth().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.i(TAG, "onComplete login: "+task.isSuccessful());
                        if (task.isSuccessful()) {
                            DocumentReference docRef = getDBInstance().collection(USERS_REF).document(getAuth().getCurrentUser().getUid()).collection("profile").document("basic-info");
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {

                                        DocumentSnapshot document = task.getResult();
                                        Log.i(TAG, "onComplete login result: "+getAuth().getCurrentUser().getUid());
                                        if (document.exists()) {
                                            String fName = document.get("name").toString();
                                            String emailString = document.get("email").toString();
                                            final User user = new User(getAuth().getUid(), fName, emailString, LoginType.Email);
                                            FirebaseManager.fetchAllDataFromDB(user, new OnSuccessListener<Boolean>() {
                                                @Override
                                                public void onSuccess(Boolean success) {
                                                    onFinished.onSuccess(success ? user : null);
                                                }
                                            });
                                        }
                                    } else {
                                        onFinished.onFailure(task.getException().getMessage());
                                    }
                                }
                            });
                        } else {
                            onFinished.onFailure("Account login failed");
                        }

                    }
                });
    }

    public static void loginWithCredential(String token, final LoginType type, final IUserLogin listner) {
        if(type==LoginType.Email)
            return;

        AuthCredential credential =type==LoginType.Google? GoogleAuthProvider.getCredential(token,null): FacebookAuthProvider.getCredential(token);

        getAuth().signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser currentUser = getAuth().getCurrentUser();
                            Log.i(TAG, "onComplete: current user: "+ currentUser.getDisplayName());
                            final User user=new User(currentUser.getUid(),currentUser.getDisplayName(),currentUser.getEmail(),type);
                            isUserExist(user.getId(), new OnSuccessListener<Boolean>() {
                                @Override
                                public void onSuccess(Boolean result) {
                                    if (!result) {
                                        addUserIntoDB(getAuth().getUid(), user, new OnSuccessListener<Boolean>() {
                                            @Override
                                            public void onSuccess(Boolean isSuccess) {
                                                if (listner == null)
                                                    return;
                                                if(isSuccess)
                                                    listner.onSuccess(user);
                                                else
                                                    listner.onFailure("Something went wrong");
                                            }
                                        });
                                    } else {
                                        Log.i(TAG, "onSuccess check exist: " + result);
                                        listner.onSuccess(user);
                                    }
                                }
                            }, new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    listner.onFailure("Something went wrong");
                                }
                            });
                        } else {
                            Log.i(TAG, "onComplete: failure: "+task.getException());
                            if(listner!=null)
                                listner.onFailure("Null");
                        }
                    }
                });
    }

    public static void logOut() {

        getAuth().signOut();
    }

    // Add Budget Items into database methods

    public static void addCategoryIntoDB(final Category category, final OnSuccessListener<Category> listener)
    {
        category.setUserId(MyUtility.currentUser.getId());
        getDBInstance().collection(CATEGORIES_REF).add(category.toJson()).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull final Task<DocumentReference> categoryAddTask) {
                if(categoryAddTask.isSuccessful()&&listener!=null)
                {
                    category.setId(categoryAddTask.getResult().getId());
                    listener.onSuccess(category);
                }
            }
        });
    }

    public static void addExpenseIntoDB(final Expense expense,final OnSuccessListener<Expense> listener) {
        expense.setUserId(MyUtility.currentUser.getId());
        getDBInstance().collection(EXPENSES_REF).add(expense.toJson()).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull final Task<DocumentReference> expenseAddTask) {
                if (expenseAddTask.isSuccessful() && listener != null) {
                    expense.setId(expenseAddTask.getResult().getId());
                    listener.onSuccess(expense);
                }
            }
        });
    }

    public static void addIncomeIntoDB(final Income income, final OnSuccessListener<Income> listener) {
        income.setUserId(MyUtility.currentUser.getId());
        getDBInstance().collection(INCOMES_REF).add(income.toJson()).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull final Task<DocumentReference> incomeAddTask) {
                if (incomeAddTask.isSuccessful() && listener != null) {
                    income.setId(incomeAddTask.getResult().getId());
                    listener.onSuccess(income);
                }
            }
        });
    }

    // Get Data From Database

    public static void getCategoriesFromDB(final OnSuccessListener<Category[]> listener) {
        getDBInstance().collection(CATEGORIES_REF).whereEqualTo("userId", MyUtility.currentUser.getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ArrayList<Category> categories = new ArrayList<>();
                for (QueryDocumentSnapshot snapshot : task.getResult()) {
                    Category category = Category.jsonToObject(snapshot);
                    category.setUserId(MyUtility.currentUser.getId());
                    categories.add(category);
                }
                Category[] categories1=new Category[categories.size()];
                categories1= (Category[]) categories.toArray(categories1);
                listener.onSuccess(categories1);
            }
        });
    }

    public static void getIncomesFromDB(final OnSuccessListener<Income[]> listner) {
        getDBInstance().collection(INCOMES_REF).whereEqualTo("userId", MyUtility.currentUser.getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ArrayList<Income> incomes = new ArrayList<>();
                for (QueryDocumentSnapshot snapshot : task.getResult()) {
                    Income income = Income.jsonToObject(snapshot);
                    income.setUserId(MyUtility.currentUser.getId());
                    incomes.add(income);
                }

                Income[] incomes1 = new Income[incomes.size()];
                incomes1=incomes.toArray(incomes1);

                listner.onSuccess(incomes1);
            }
        });
    }

    public static void getExpensesFromDB(final OnSuccessListener<Expense[]> listener) {
        getDBInstance().collection(EXPENSES_REF).whereEqualTo("userId", MyUtility.currentUser.getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ArrayList<Expense> expenses = new ArrayList<>();
                for (QueryDocumentSnapshot snapshot : task.getResult()) {
                    Expense expense = Expense.jsonToObject(snapshot);
                    expense.setUserId(MyUtility.currentUser.getId());
                    expenses.add(expense);
                }
                Expense[] expenses1 = new Expense[expenses.size()];
                expenses1 = (Expense[]) expenses.toArray(expenses1);
                listener.onSuccess(expenses1);
            }
        });
    }

    public static void fetchAllDataFromDB(final User user, final OnSuccessListener<Boolean> listener) {
        getCategoriesFromDB(new OnSuccessListener<Category[]>() {
            @Override
            public void onSuccess(Category[] categories) {
                if (categories != null)
                    user.addCategories(categories);
                getExpensesFromDB(new OnSuccessListener<Expense[]>() {
                    @Override
                    public void onSuccess(Expense[] expenses) {
                        if (expenses != null) {
                            for (Expense expens : expenses) {
                                expens.setCategory(user.getCategoryForId(expens.getCategoryId()));
                            }
                            user.addExpenses(expenses);
                        }
                        getIncomesFromDB(new OnSuccessListener<Income[]>() {
                            @Override
                            public void onSuccess(Income[] incomes) {
                                if (incomes != null) {
                                    user.addIncomes(incomes);
                                }
                                if (listener != null)
                                    listener.onSuccess(true);
                            }
                        });
                    }
                });
            }
        });
    }

    // Update Data from database
    public static void updateCategories(Category category,final OnSuccessListener<Boolean> listener)
    {
        getDBInstance().collection(CATEGORIES_REF).document(category.getId()).update(category.toJson()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(listener!=null)
                    listener.onSuccess(task.isComplete()&&task.isSuccessful());
            }
        });
    }

    public static void updateExpense(Expense expense, final OnSuccessListener<Boolean> listener) {
        getDBInstance().collection(EXPENSES_REF).document(expense.getId()).update(expense.toJson()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (listener != null)
                    listener.onSuccess(task.isComplete() && task.isSuccessful());
            }
        });
    }

    public static void updateIncome(Income income, final OnSuccessListener<Boolean> listener)
    {
        getDBInstance().collection(INCOMES_REF).document(income.getId()).update(income.toJson()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(listener!=null)
                    listener.onSuccess(task.isComplete()&&task.isSuccessful());
            }
        });
    }

    // Delete data from database
    public static void deleteCategory(final User user, final Category category, final OnSuccessListener<Boolean> listener) {
        getDBInstance().collection(CATEGORIES_REF).document(category.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    final Expense[] expensesForCategory = MyUtility.currentUser.getExpensesForCategory(category.getId());
                    for (int i = 0; i < expensesForCategory.length; i++) {
                        final int finalI = i;
                        deleteExpense(expensesForCategory[i], new OnSuccessListener<Boolean>() {
                            @Override
                            public void onSuccess(Boolean aBoolean) {
                                if(aBoolean)
                                    MyUtility.currentUser.removeExpenses(expensesForCategory[finalI]);
                                if (finalI >= expensesForCategory.length - 1 && listener != null) {
                                    listener.onSuccess(aBoolean);
                                }
                            }
                        });
                    }
                } else {
                    if (listener != null) {
                        listener.onSuccess(false);
                    }
                }
            }
        });
    }

    public static void deleteExpense(final Expense expense, final OnSuccessListener<Boolean> listener) {
        getDBInstance().collection(EXPENSES_REF).document(expense.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (listener != null)
                    listener.onSuccess(task.isComplete() && task.isSuccessful());
            }
        });
    }

    public static void deleteIncome(final User user, final Income income, final OnSuccessListener<Boolean> listener) {
        getDBInstance().collection(INCOMES_REF).document(income.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (listener != null) {
                    listener.onSuccess(task.isComplete() && task.isSuccessful());
                }
            }
        });
    }

    public enum LoginType
    {
        Email,Facebook,Google
    }

    public interface OnLogoutListner
    {
        void onSuccess(boolean isLogOut);
    }

    public interface IUserLogin {
        public void onSuccess(User user);
        public void onFailure(String error);
    }
}
