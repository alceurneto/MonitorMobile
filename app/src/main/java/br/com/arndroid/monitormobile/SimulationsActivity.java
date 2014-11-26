package br.com.arndroid.monitormobile;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import br.com.arndroid.monitormobile.sqlite.DBOpenHelper;
import br.com.arndroid.monitormobile.utils.PreferencesUtils;
public class SimulationsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulations);

        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int itemId = item.getItemId();

        switch (itemId) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void dbReset(View view) {
        DBOpenHelper.resetDB(this);
        Toast.makeText(this, "Database reset realizado com sucesso.", Toast.LENGTH_SHORT).show();
    }

    public void userReset(View view) {
        PreferencesUtils.setUserShortName(this, null);
        Toast.makeText(this, "Usuário reset realizado com sucesso.", Toast.LENGTH_SHORT).show();
    }
}