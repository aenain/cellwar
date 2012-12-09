/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.nanowar;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import com.test.nanowar.model.Level;
import com.test.nanowar.view.LevelListAdapter;

/**
 *
 * @author artur
 */
public class LvlChooserActivity extends ListActivity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Log.d("CHOOSER", "create");
        setContentView(R.layout.levels);

        ListView listView = (ListView) findViewById(android.R.id.list);

        Level level;
        Level[] levels = new Level[Level.COUNT];
        for (int i = 0; i < levels.length; i++) {
            level = new Level(i + 1);
            level.readData(this);
            level.readUserData(this);
            levels[i] = level;
            Log.d("CHOOSER", "list" + Integer.toString(i));
        }

        listView.setAdapter(new LevelListAdapter(this, levels));   
    }

    @Override
    protected void onListItemClick(ListView list, View view, int position, long id) {
        Intent intent = new Intent(LvlChooserActivity.this, GameActivity.class);
        intent.putExtra("level", position + 1);
        startActivity(intent);
    }
}
