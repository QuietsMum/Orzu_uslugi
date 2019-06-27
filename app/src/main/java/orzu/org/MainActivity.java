package orzu.org;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {

    private ActionBar toolbar;
    Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        toolbar = getSupportActionBar();
        toolbar.setTitle("Задания");
        //toolbar.setTitleTextColor(Color.WHITE);
        loadFragment(new Fragment1());




        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    toolbar.setTitle("Задания");
                    fragment = new Fragment1();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_add:
                    toolbar.setTitle("Добавить");
                    fragment = new Fragment2();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_user:
                    toolbar.setTitle("Пользователь");
                    fragment = new Fragment3();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }


    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.up_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.new_game:
                Intent intent = new Intent(this, FiltersActivity.class);
                startActivityForResult(intent, 1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            loadFragment(new Fragment1());
            return;
        }

    }

}
