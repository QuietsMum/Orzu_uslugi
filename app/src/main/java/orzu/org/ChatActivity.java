package orzu.org;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import orzu.org.chat.ChattingAdapter;
import orzu.org.chat.message_interface;
import orzu.org.chat.my_message;
import orzu.org.chat.their_message;

public class ChatActivity extends AppCompatActivity {

    RecyclerView messages_view;
    EditText editText;
    ImageButton send;
    List<message_interface> messages = new ArrayList<>();
    ChattingAdapter adapter;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_back));
        getSupportActionBar().setTitle(Common.nameOfChat);
        getSupportActionBar().setElevation(0);

        messages_view = findViewById(R.id.messages_view);
        editText = findViewById(R.id.editText);
        send = findViewById(R.id.chat_send_button);

        messages_view.setHasFixedSize(true);
        messages_view.setLayoutManager(new LinearLayoutManager(this));
        messages_view.setNestedScrollingEnabled(false);

        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM orzuchat where id = '" + Common.chatId + "' Order by date", null);
        while (cursor.moveToNext()) {
            int their_text = cursor.getColumnIndex("their_text");
            int my_text = cursor.getColumnIndex("my_text");
            int date = cursor.getColumnIndex("date");

            if(cursor.getString(my_text)!=null){
                messages.add(new my_message(cursor.getString(my_text), cursor.getString(date)));
            }else{
                messages.add(new their_message(cursor.getString(their_text), cursor.getString(date)));
            }
        }
        cursor.close();
        db.close();
        dbHelper.close();
        adapter = new ChattingAdapter(this, messages);
        messages_view.setAdapter(adapter);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        messages_view.smoothScrollToPosition(messages_view.getAdapter().getItemCount() - 1);
                    }
                }, 200);
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentTimeStamp = dateFormat.format(new Date());
                messages.add(new my_message(editText.getText().toString(), currentTimeStamp));
                adapter.notifyDataSetChanged();
                messages_view.smoothScrollToPosition(messages_view.getAdapter().getItemCount() - 1);
                editText.setText("");
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
}
