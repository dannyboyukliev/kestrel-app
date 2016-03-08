package com.boyukliev.kestrel_app.ui.ui.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.boyukliev.kestrel_app.R;
import com.boyukliev.kestrel_app.ui.di.ApplicationComponent;
import com.boyukliev.kestrel_app.ui.presenter.MainPresenter;
import com.boyukliev.kestrel_app.ui.ui.activity.base.BaseActivity;
import com.boyukliev.kestrel_app.ui.ui.view.MainView;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements MainView {

    @Bind(R.id.db_text_view)
    public TextView textView;

    @Inject
    public MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        presenter.bindView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }

    @Override
    protected void injectComponent(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    private View.OnClickListener sendCommandButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void addToast(String text, int length) {
        Toast.makeText(this, text, length).show();
    }

    @Override
    public void displayText(String text) {
        textView.setText(text);
    }
}
