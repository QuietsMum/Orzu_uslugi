package orzu.org;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

        messages.add(new my_message("Hello mazafaka","13:20"));
        messages.add(new their_message("Hello","13:24"));
        messages.add(new my_message("how are you doing?","13:30"));
        messages.add(new their_message("Fine","13:31"));
        messages.add(new their_message("How are you doing? This is a long message that should probably wrap.","13:31"));

        adapter = new ChattingAdapter(this,messages);
        messages_view.setAdapter(adapter);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.wtf("asd","sad");
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
                String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
                messages.add(new my_message(editText.getText().toString(),currentTime));
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
