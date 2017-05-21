package dagger.extension.example;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import javax.inject.Inject;

import injector.Injector;

public class MainActivity extends AppCompatActivity {

    @Inject
    SomeService someService;

    private ActivityComponent component;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        component = Injector.get(getApplication()).activityComponent(this.getParentComponent());
        component.inject(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        someService.call();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action",
                Snackbar.LENGTH_LONG).setAction("Action", null).show());
    }

    private ApplicationComponent getParentComponent() {
        return app().getApplicationComponent();
    }

    protected ExampleApplication app() {
        return (ExampleApplication) getApplication();
    }
}
