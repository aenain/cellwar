/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.nanowar;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;
import com.test.nanowar.model.Level;
import com.test.nanowar.model.MainGamePanel;
import com.test.nanowar.model.Player;
import com.test.nanowar.utils.ResourceResolver;

/**
 *
 * @author artur
 */
public class GameActivity extends Activity {

    protected MainLayout layout;
    protected MainGamePanel gamePanel;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // settings of layout and window
        layout = new MainLayout(this);
        RelativeLayout.LayoutParams mainParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layout.setLayoutParams(mainParams);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(layout);

        // background        
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundColor(getResources().getColor(R.color.background));
        layout.addView(imageView, mainParams);

        // models
        Bundle bundle = getIntent().getExtras();
        int levelNumber = bundle.getInt("level", 1);

        final MainGamePanel gamePanel = new MainGamePanel(layout, levelNumber);
        this.gamePanel = gamePanel;
        gamePanel.initPlayers();

        // controls
        LinearLayout controls = new LinearLayout(this);
        RelativeLayout.LayoutParams controlListParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        controlListParams.alignWithParent = true;
        controlListParams.topMargin = 0;
        layout.addView(controls, controlListParams);

        // choose selection
        LinearLayout.LayoutParams controlParams;
        Button chooseSelection = new Button(this);
        chooseSelection.setText(R.string.choose_selection);
        chooseSelection.setTextColor(Color.WHITE);
        chooseSelection.setBackgroundColor(Color.argb(50, 0, 0, 0));

        controlParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        chooseSelection.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                gamePanel.getUserPlayer().setSelectionMode(Player.SelectionMode.SELECT);
            }
        });
        controls.addView(chooseSelection, controlParams);

        // choose transport
        Button chooseTransport = new Button(this);
        chooseTransport.setText(R.string.choose_transport);
        chooseTransport.setTextColor(Color.WHITE);
        chooseTransport.setBackgroundColor(Color.argb(50, 0, 0, 0));

        controlParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        controlParams.leftMargin = 5;

        chooseTransport.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                gamePanel.getUserPlayer().setSelectionMode(Player.SelectionMode.TRANSPORT);
            }
        });
        controls.addView(chooseTransport, controlParams);

        // start game
        gamePanel.initLevel();
        gamePanel.startGame();
    }

    // gra sie zakonczyla, bo ktorys gracz wygral
    public void onGameFinished() {
        final Dialog dialog = new Dialog(this);
        final GameActivity self = this;

        if (gamePanel.getUserPlayer().equals(gamePanel.getWinner())) {
            dialog.setContentView(R.layout.victory);
            dialog.setTitle("Victory!");
            ImageView score = (ImageView) dialog.findViewById(R.id.score_image_victory);
            Integer resourceId = ResourceResolver.raw("score" + Integer.toString(gamePanel.getLevel().getScore()));
            SVG stars = SVGParser.getSVGFromResource(getResources(), resourceId);
            score.setImageDrawable(stars.createPictureDrawable());
            Button button;
            button = (Button) dialog.findViewById(R.id.next_level_button);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    self.openNextLevel(v);
                }
            });
            button = (Button) dialog.findViewById(R.id.main_menu_button);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    self.openMainMenu(v);
                }
            });
        } else {
            dialog.setContentView(R.layout.defeat);
            dialog.setTitle("Defeat...");
            ImageView score = (ImageView) dialog.findViewById(R.id.score_image_defeat);
            Integer resourceId = ResourceResolver.raw("score" + Integer.toString(gamePanel.getLevel().getScore()));
            SVG stars = SVGParser.getSVGFromResource(getResources(), resourceId);
            score.setImageDrawable(stars.createPictureDrawable());
            Button button;
            button = (Button) dialog.findViewById(R.id.retry_button);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    self.retryCurrentLevel(v);
                }
            });
            button = (Button) dialog.findViewById(R.id.main_menu_button);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    self.retryCurrentLevel(v);
                }
            });
        }

        dialog.show();
    }

    public void retryCurrentLevel(View view) {
        int currentLevelNumber = gamePanel.getLevel().getLevelNumber();
        Intent intent = new Intent(GameActivity.this, GameActivity.class);
        intent.putExtra("level", currentLevelNumber);
        startActivity(intent);
        finish();
    }

    public void openNextLevel(View view) {
        Intent intent;
        int currentLevelNumber = gamePanel.getLevel().getLevelNumber();

        if (currentLevelNumber < Level.COUNT) {
            intent = new Intent(GameActivity.this, GameActivity.class);
            intent.putExtra("level", currentLevelNumber + 1);
        } else {
            intent = new Intent(GameActivity.this, LvlChooserActivity.class);
        }

        startActivity(intent);
        finish();
    }

    public void openMainMenu(View view) {
        Intent intent = new Intent(GameActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onStop() {
        gamePanel.stopGame();
        super.onStop();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
