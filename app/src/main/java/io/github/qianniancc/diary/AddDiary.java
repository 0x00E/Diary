package io.github.qianniancc.diary;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddDiary extends AppCompatActivity {

    String title;

    public static void DisplayToast(String msg,Context context) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finish() {
        super.finish();
        MainActivity.getAdapter().notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);
        Toolbar tb=(Toolbar)findViewById(R.id.toolbar);
        Button b=(Button) findViewById(R.id.add);
        final EditText titleET=(EditText) findViewById(R.id.titleET);

        titleET.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
                return(event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });

        final EditText et=(EditText) findViewById(R.id.editText);
        Intent getIntent = getIntent();

        title = getIntent.getStringExtra("title");
        if(title!=null){
            tb.setTitle("修改日记");
            titleET.setText(title);
            et.setText(MainActivity.getmPref().getString(title,""));
        }else{
            tb.setTitle("写日记");
        }
        setSupportActionBar(tb);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(titleET.getText().toString().equalsIgnoreCase("")){
                    DisplayToast("错误：标题不能为空！",getApplicationContext());
                    return;
                }
                if(et.getText().toString().equalsIgnoreCase("")){
                    DisplayToast("错误：日记不能为空！",getApplicationContext());
                    return;
               }
                SharedPreferences.Editor ed= MainActivity.getmPref().edit();
                if(title!=null) {
                    ed.remove(title).apply();
                }
                Time time = new Time();
                time.setToNow();
                ed.putString(titleET.getText().toString(),et.getText().toString()).apply();
                finish();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(title==null){
            return true;
        }
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(title==null){
            return true;
        }
        int id = item.getItemId();
        if (id == R.id.delete) {
           MainActivity.getmPref().edit().remove(title).apply();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}