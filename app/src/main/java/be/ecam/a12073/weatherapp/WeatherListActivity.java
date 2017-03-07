package be.ecam.a12073.weatherapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;

public class WeatherListActivity extends AppCompatActivity implements ItemAdapter.ItemAdapterOnClickHandler, LoaderManager.LoaderCallbacks<String>, SharedPreferences.OnSharedPreferenceChangeListener {
    private static final int QUERY_LOADER = 22;
    private ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        setContentView(R.layout.activity_weather_list);

        RecyclerView resultView = (RecyclerView) findViewById(R.id.resultView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        resultView.setLayoutManager(layoutManager);
        resultView.setHasFixedSize(true);

        itemAdapter = new ItemAdapter(this);
        resultView.setAdapter(itemAdapter);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        String textToSave = "";
        outState.putString("weather_json", textToSave);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.weather_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();

        switch (itemThatWasClickedId) {
            case R.id.menu:
                Log.i("WeatherListActivity", "Menu clicked");
                Toast.makeText(this, "Weather charging...", Toast.LENGTH_LONG).show();

                Bundle queryUrl = new Bundle();
                queryUrl.putString("URL", "https://andfun-weather.udacity.com/weather");

                LoaderManager loaderManager = getSupportLoaderManager();
                loaderManager.restartLoader(QUERY_LOADER, queryUrl, this);

//                new QueryTask().execute("https://andfun-weather.udacity.com/weather");
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public  void onClick(int index) {
        Context context = this;
        Class<WeatherInfoActivity> infoActivity = WeatherInfoActivity.class;
        Intent intent = new Intent(context, infoActivity);
        intent.putExtra(Intent.EXTRA_INDEX, index);
        startActivity(intent);
    }

//    public class QueryTask extends AsyncTask<String, Void, String[]> {
//
//        @Override
//        protected String[] doInBackground(String... params) {
//            String searchUrl = params[0];
//            String json;
//            String[] queryResults = null;
//            try {
//                json = NetworkUtils.getResponseFromHttpUrl(searchUrl);
//                Weather.parse(json);
//
//                queryResults = Weather.getMaxTemps();
//            } catch (IOException | JSONException e) {
//                e.printStackTrace();
//            }
//
//            return queryResults;
//        }
//
//        @Override
//        public void onPostExecute(String[] queryResults) {
//            if (queryResults != null) {
//                itemAdapter.setData(queryResults);
//            }
//        }
//    }

    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {
            String json = null;

            @Override
            protected void onStartLoading() {
                if (json != null) {
                    deliverResult(json);
                } else {
                    forceLoad();
                }
            }

            @Override
            public String loadInBackground() {
                String searchUrl = args.getString("URL");
                try {
                    Log.i("ASyncTaskLoader", "Start query");
                    return NetworkUtils.getResponseFromHttpUrl(searchUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(String data) {
                json = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        try {
            Log.i("WeatherListActivity", "Load finished");
            Weather.parse(data);
            itemAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("my_name")) {
            Toast.makeText(this, "Test", Toast.LENGTH_SHORT).show();
        }
    }
}
