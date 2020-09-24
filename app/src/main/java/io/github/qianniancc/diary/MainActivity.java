package io.github.qianniancc.diary;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static SharedPreferences mPref;
    private Context  context;
    private static Adapter adapter;
    private static ListView lv;
    private Button cancle,delete;
    private static TextView txtcount;
    private static MainActivity me;
    private LinearLayout rootView;
    private RelativeLayout layout;
    private LinearLayout opreateView;
    private List<String> selectid = new ArrayList<String>();
    private boolean isMulChoice = false;
    private Intent intent;
    final boolean[] selected = new boolean[1];
    private LayoutInflater inflater;

    public static ListView getLv() {
        return lv;
    }

    public static Adapter getAdapter() {
        return adapter;
    }

    public static SharedPreferences getmPref() {
        return mPref;
    }

    public static MainActivity getMe(){
        if(me==null){
            me=new MainActivity();
        }
        return me;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancle:
                isMulChoice = false;
                selectid.clear();
                adapter = new Adapter(context,txtcount);
                lv.setAdapter(adapter);
                layout.setVisibility(View.INVISIBLE);
                break;
            case R.id.delete:

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setIcon(android.R.drawable.alert_light_frame);
                builder.setTitle("提示");
                builder.setMessage("您确定要删除吗，删除后将不可恢复！");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isMulChoice =false;
                        for(int i=0;i<selectid.size();i++){
                            for(int j=0;j<getData(getMe()).size();j++){
                                if(selectid.get(i).equals(getData(getMe()).get(j))){
                                    MainActivity.getmPref().edit().remove(getData(getMe()).get(j)).apply();
                                }
                            }
                        }
                        selectid.clear();
                        adapter = new Adapter(context,txtcount);
                        lv.setAdapter(adapter);
                        layout.setVisibility(View.INVISIBLE);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                AlertDialog ad = builder.create();
                ad.show();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        mPref =  getSharedPreferences("diary", Activity.MODE_PRIVATE);
        context = this;
        lv=(ListView)findViewById(R.id.diarys);
        layout = (RelativeLayout)findViewById(R.id.relative);
        txtcount = (TextView)findViewById(R.id.txtcount);
        cancle   = (Button)findViewById(R.id.cancle);
        delete   = (Button)findViewById(R.id.delete);
        cancle.setOnClickListener(this);
        delete.setOnClickListener(this);
        adapter = new Adapter(context,txtcount);
        lv.setAdapter(adapter);
       this.intent=new Intent(this , AddDiary.class);
        final Intent intent2 = new Intent(this , AddDiary.class);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent2);
            }
        });
    }
    private LinkedList<String> getData(Activity ac){

        LinkedList<String> data = new LinkedList<>();
        LinkedList<Integer> intList=new LinkedList<>();
        Map<String,?> m=mPref.getAll();
        for(Object obj:m.keySet().toArray()){
            intList.add(Integer.parseInt(obj.toString().split(";")[0]));
        }
        for(int i=0;i<intList.size()-1;i++){
            for(int j=0;j<intList.size()-1;j++){
                if(intList.get(j)>intList.get(j+1)){
                    int tmp=intList.get(j);
                    intList.set(j,intList.get(j+1));
                    intList.set(j+1,tmp);
                }
            }
        }
        while(!intList.isEmpty()){

        }
        return data;
    }

    class Adapter extends BaseAdapter {
        private Context context;
        private LayoutInflater inflater=null;
        private HashMap<Integer, View> mView ;
        public  HashMap<Integer, Integer> visiblecheck;
        public  HashMap<Integer, Boolean> ischeck;
        private TextView txtcount;
        public Adapter(Context context,TextView txtcount)
        {
            this.context = context;
            this.txtcount = txtcount;
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = new HashMap<Integer, View>();
            visiblecheck = new HashMap<Integer, Integer>();
            ischeck      = new HashMap<Integer, Boolean>();
            if(isMulChoice){
                for(int i=0;i<getData(getMe()).size();i++){
                    ischeck.put(i, false);
                    visiblecheck.put(i, CheckBox.VISIBLE);
                }
            }else{
                for(int i=0;i<getData(getMe()).size();i++)
                {
                    ischeck.put(i, false);
                    visiblecheck.put(i, CheckBox.INVISIBLE);
                }
            }
        }

        public int getCount() {
            return getData(getMe()).size();
        }

        public Object getItem(int position) {
            return getData(getMe()).get(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = mView.get(position);
            if(view==null)
            {
                view = inflater.inflate(R.layout.item, null);
                TextView txt = (TextView)view.findViewById(R.id.txtName);
                final CheckBox ceb = (CheckBox)view.findViewById(R.id.check);
                txt.setText(getData(getMe()).get(position));
                txt.setTextColor(getResources().getColor(R.color.white));
                if(ischeck.get(position)==null){
                    ceb.setChecked(false);
                }else{
                    ceb.setChecked(ischeck.get(position));
                }

                if(visiblecheck.get(position)==null){
                    ceb.setVisibility(View.GONE);
                }else if(visiblecheck.get(position)==0){
                    ceb.setVisibility(View.VISIBLE);
                }else if(visiblecheck.get(position)==4){
                    ceb.setVisibility(View.INVISIBLE);
                }else if(visiblecheck.get(position)==8){
                    ceb.setVisibility(View.GONE);
                }

                view.setOnLongClickListener(new Onlongclick());

                view.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        if(isMulChoice){
                            if(ceb.isChecked()){
                                ceb.setChecked(false);
                                selectid.remove(getData(getMe()).get(position));
                            }else{
                                ceb.setChecked(true);
                                selectid.add(getData(getMe()).get(position));
                            }
                            txtcount.setText("共选择了"+selectid.size()+"项");
                        }else {
                            intent.putExtra("title", getData(getMe()).get(position).toString());
                            startActivity(intent);
                        }
                    }
                });

                mView.put(position, view);
            }
            return view;
        }

        class Onlongclick implements View.OnLongClickListener {
            public boolean onLongClick(View v) {

                isMulChoice = true;
                selectid.clear();
                layout.setVisibility(View.VISIBLE);
                for(int i=0;i<getData(getMe()).size();i++)
                {
                    adapter.visiblecheck.put(i, CheckBox.VISIBLE);
                }
                adapter = new Adapter(context,txtcount);
                lv.setAdapter(adapter);
                return true;
            }
        }
    }
}