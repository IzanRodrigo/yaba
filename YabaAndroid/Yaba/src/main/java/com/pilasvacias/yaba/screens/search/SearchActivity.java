package com.pilasvacias.yaba.screens.search;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.pilasvacias.yaba.R;
import com.pilasvacias.yaba.core.network.NetworkActivity;
import com.pilasvacias.yaba.core.widget.EmptyView;
import com.pilasvacias.yaba.modules.emt.models.EmtData;
import com.pilasvacias.yaba.modules.emt.persistence.EmtQueryManager;
import com.pilasvacias.yaba.modules.emt.pojos.Stop;
import com.pilasvacias.yaba.util.L;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.InjectView;
import butterknife.Views;

/**
 * Created by IzanRodrigo on 25/10/13.
 */
public class SearchActivity extends NetworkActivity implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {

    // Constants
    private static final long SEARCH_DELAY = 100;
    // Inject views
    @InjectView(R.id.search_listView)
    ListView listView;
    // Fields
    private SearchView searchView;
    private ArrayAdapter<Stop> arrayAdapter;
    private EmtData<Stop> nodes;
    private EmtQueryManager queryManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        Views.inject(this);

        configureListView();

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        handleIntent(intent);

        queryManager = new EmtQueryManager();
        queryManager.init(this);
    }

    private void configureListView() {
        EmptyView.makeText(R.string.empty_search_guide).into(listView);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(arrayAdapter);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // Because this activity has set launchMode="singleTop", the system calls this method
        // to deliver the intent if this activity is currently the foreground activity when
        // invoked again (when the user executes a search from this activity, we don't create
        // a new instance of this activity, so the system delivers the search intent here)
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            // handles a search query
            search(intent.getStringExtra(SearchManager.QUERY));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);

        // Configure SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        searchMenuItem.expandActionView();
        searchMenuItem.setOnActionExpandListener(this);
        searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setSearchableInfo(searchableInfo);
        searchView.setOnQueryTextListener(this);
        searchView.setIconified(false);

        return true;
    }

    private boolean search(String query) {
        if (query.isEmpty()) {
            arrayAdapter.clear();
            return false;
        }

        Pattern pattern = Pattern.compile("(\\d+)-(\\d+)-(\\d+).*");
        Matcher matcher = pattern.matcher("cosas");


        "0000313".replaceAll("\\G0", " ");

        arrayAdapter.clear();
        L.time.begin("query %s", query);
        List<Stop> data = queryManager.stops().query(query).execute();
        L.time.end();
        arrayAdapter.addAll(data);

        return true;
    }

    /**
     * Handle search when user click search button.
     *
     * @param query
     * @return
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        search(query);
        return true;
    }

    /**
     * Handle search while user enters text.
     * Delay search {@code SEARCH_DELAY} millis.
     * If user change search query after delay pass,
     * previous search is cancelled, and another is delayed.
     *
     * @param newText
     * @return
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        search(newText);
        return true;
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return false;
    }

    /**
     * Finish SearchActivity when close SearchView
     *
     * @param item
     * @return
     */
    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        finish();
        return false;
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        queryManager.release();
    }
}
