package orzu.org;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;

import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.yandex.mapkit.GeoObjectCollection;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.BoundingBox;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraListener;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.CameraUpdateSource;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.VisibleRegionUtils;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.search.Response;
import com.yandex.mapkit.search.SearchFactory;
import com.yandex.mapkit.search.SearchManager;
import com.yandex.mapkit.search.SearchManagerType;
import com.yandex.mapkit.search.SearchOptions;
import com.yandex.mapkit.search.SearchType;
import com.yandex.mapkit.search.Session;
import com.yandex.mapkit.search.SuggestItem;
import com.yandex.runtime.Error;
import com.yandex.runtime.image.ImageProvider;
import com.yandex.runtime.network.NetworkError;
import com.yandex.runtime.network.RemoteError;

import java.util.ArrayList;
import java.util.List;


public class CreateTaskPlace extends AppCompatActivity implements View.OnClickListener, SearchManager.SuggestListener {
    TextView buttonCreate;
    TextView buttonCreateLeft;
    TextView buttonCreateRight;
    TextView textFar;
    public static Activity fa;
    int counter;
    int placetype;
    final String MAPKIT_API_KEY = "your_api_key";
    final int RESULT_NUMBER_LIMIT = 5;

    SearchManager searchManager;
    RecyclerView suggestResultView;
    ArrayAdapterMy resultAdapter;
    List<String> suggestResult;
    EditText queryEdit;
    final Point CENTER = new Point(55.75, 37.62);
    final double BOX_SIZE = 0.2;
    final BoundingBox BOUNDING_BOX = new BoundingBox(
            new Point(CENTER.getLatitude() - BOX_SIZE, CENTER.getLongitude() - BOX_SIZE),
            new Point(CENTER.getLatitude() + BOX_SIZE, CENTER.getLongitude() + BOX_SIZE));
    final SearchOptions SEARCH_OPTIONS =  new SearchOptions().setSearchTypes(
            SearchType.GEO.value |
                    SearchType.BIZ.value |
                    SearchType.TRANSIT.value);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MapKitFactory.setApiKey("38c4c9bc-766a-4574-8088-18a4e7583a90");
        MapKitFactory.initialize(this);
        SearchFactory.initialize(this);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_back));
        setContentView(R.layout.activity_create_task_place);
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Создать задание");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        counter = 1;
        placetype = 1;
        //searchEdit = findViewById(R.id.editCreatePlace);
        textFar = findViewById(R.id.text_far);
        textFar.setVisibility(View.INVISIBLE);
        buttonCreate = findViewById(R.id.createPlace);
        buttonCreateLeft = findViewById(R.id.createPlace_buttonleft);
        buttonCreateRight = findViewById(R.id.createPlace_buttonright);
        buttonCreate.setOnClickListener(this);
        buttonCreateLeft.setOnClickListener(this);
        buttonCreateRight.setOnClickListener(this);
        fa = this;

        searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED);
        queryEdit = (EditText)findViewById(R.id.suggest_query);
        suggestResultView = findViewById(R.id.suggest_result);
        suggestResultView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        suggestResultView.setLayoutManager(layoutManager);
        suggestResult = new ArrayList<>();
        resultAdapter = new ArrayAdapterMy(this, suggestResult);
        suggestResultView.setAdapter(resultAdapter);





        queryEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                requestSuggest(editable.toString());
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.createPlace:
                final SharedPreferences prefs = getSharedPreferences(" ", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                if (counter == 1) {
                    placetype = 1;
                    editor.putString(Util.TASK_PLACE, String.valueOf(queryEdit.getText()));
                    editor.putString(Util.TASK_PLACETYPE, String.valueOf(placetype));
                } else if (counter == 2) {
                    placetype = 2;
                    editor.putString(Util.TASK_PLACE, "Удаленно");
                    editor.putString(Util.TASK_PLACETYPE, String.valueOf(placetype));
                }
                editor.apply();
                Intent intent = new Intent(this, CreateTaskTerm.class);
                startActivity(intent);
                break;

            case R.id.createPlace_buttonleft:
                counter = 1;
                textFar.setVisibility(View.INVISIBLE);
                queryEdit.setVisibility(View.VISIBLE);
                buttonCreateLeft.setBackgroundResource(R.drawable.circle_button_left_solid);
                buttonCreateLeft.setTextColor(getResources().getColor(R.color.colorBackgrndFrg));
                buttonCreateRight.setBackgroundResource(R.drawable.circle_button_right);
                buttonCreateRight.setTextColor(getResources().getColor(R.color.colorAccent));
                break;

            case R.id.createPlace_buttonright:
                counter = 2;
                textFar.setVisibility(View.VISIBLE);
                queryEdit.setVisibility(View.INVISIBLE);
                buttonCreateLeft.setBackgroundResource(R.drawable.circle_button_left);
                buttonCreateLeft.setTextColor(getResources().getColor(R.color.colorAccent));
                buttonCreateRight.setBackgroundResource(R.drawable.circle_button_right_solid);
                buttonCreateRight.setTextColor(getResources().getColor(R.color.colorBackgrndFrg));
                break;

        }
    }

    @Override
    protected void onStop() {
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
    }

    @Override
    public void onSuggestResponse(List<SuggestItem> suggest) {
        suggestResult.clear();
        for (int i = 0; i < Math.min(RESULT_NUMBER_LIMIT, suggest.size()); i++) {
            suggestResult.add(suggest.get(i).getDisplayText());
        }
        resultAdapter.notifyDataSetChanged();
        suggestResultView.setVisibility(View.VISIBLE);

        ArrayAdapterMy.setSelect(new NameItemSelect() {
            @Override
            public void onItemSelectedListener(View view, int position) {
                queryEdit.setText(suggest.get(position).getDisplayText());
                queryEdit.setSelection(queryEdit.getText().length());
            }

            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public void onSuggestError(Error error) {
        String errorMessage = "unknown_error_message";
        if (error instanceof RemoteError) {
            errorMessage = "remote_error_message";
        } else if (error instanceof NetworkError) {
            errorMessage = "network_error_message";
        }

        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    private void requestSuggest(String query) {
        suggestResultView.setVisibility(View.INVISIBLE);
        searchManager.suggest(query, BOUNDING_BOX, SEARCH_OPTIONS, this);
    }


}
