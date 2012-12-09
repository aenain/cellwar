/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.nanowar.view;

import com.test.nanowar.R;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.test.nanowar.model.Level;

/**
 *
 * @author artur
 */
public class LevelListAdapter extends ArrayAdapter<Level> {
    private final Activity context;
    private final Level[] levels;

    static class ViewHolder {
        public TextView name;
        public ImageView thumbnail;
    }

    public LevelListAdapter(Activity context, Level[] levels) {
        super(context, R.layout.level, levels);
        this.context = context;
        this.levels = levels;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        ViewHolder holder;

        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.level, null);
            holder = new ViewHolder();
            holder.name = (TextView) rowView.findViewById(R.id.level_name);
            Log.d("ADAPTER", "holder.name:" + holder.name);
            // holder.thumbnail = (ImageView) rowView.findViewById(R.id.level_thumbnail);
            rowView.setTag(holder);
        }
        else {
            holder = (ViewHolder) rowView.getTag();
            Log.d("ADAPTER", "holder.name:" + holder.name);
        }

        Level level = levels[position];
        Log.d("ADAPTER", "level" + level);
        Log.d("ADAPTER", "levelName" + level.getLevelName());
        holder.name.setText(level.getLevelName());

        return rowView;
    }
}
