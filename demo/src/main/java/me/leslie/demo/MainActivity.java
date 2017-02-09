package me.leslie.demo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import me.leslie.afv.base.BaseAnyRefreshViewFragment;

import static me.leslie.demo.MainActivity.Type.NewNormal;
import static me.leslie.demo.MainActivity.Type.NewWeb;
import static me.leslie.demo.MainActivity.Type.OldNormal;
import static me.leslie.demo.MainActivity.Type.OldWeb;

public class MainActivity extends AppCompatActivity {
    private Type type = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("It's a demo");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        setType(NewNormal);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        switch (type){
            case NewNormal:
                menu.findItem(R.id.new_1).setChecked(true);
                break;
            case NewWeb:
                menu.findItem(R.id.new_2).setChecked(true);
                break;
            case OldNormal:
                menu.findItem(R.id.old_1).setChecked(true);
                break;
            case OldWeb:
                menu.findItem(R.id.old_2).setChecked(true);
                break;
        }
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.new_1:
                setType(NewNormal);
                break;
            case R.id.new_2:
                setType(NewWeb);
                break;
            case R.id.old_1:
                setType(OldNormal);
                break;
            case R.id.old_2:
                setType(OldWeb);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setType(Type type) {
        if (this.type == type) return;
        this.type = type;
        Fragment f = null;
        if (Type.NewNormal == type){
            f = new M1Fragment();
        }else if (Type.NewWeb == type){
            f = new M2Fragment();
        }else if (Type.OldNormal == type){
            f = new PastM1Fragment();
        }else if (Type.OldWeb == type){
            f = new PastM2Fragment();
        }
        replace(f);
    }


    private void replace(Fragment f){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fold = fm.findFragmentByTag("demo");
        if (null != fold && fold instanceof BaseAnyRefreshViewFragment){
            ((BaseAnyRefreshViewFragment)fold).onLoadFinish();
        }
        ft.replace(R.id.replace, f, "demo").commit();
    }

    enum Type{
        NewNormal,
        NewWeb,
        OldNormal,
        OldWeb
    }
}
