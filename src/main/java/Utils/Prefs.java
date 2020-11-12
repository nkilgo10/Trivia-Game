package Utils;

import android.app.Activity;
import android.content.SharedPreferences;



public class Prefs {
    private SharedPreferences sharedPreferences;
    private String key = "highScore";

    public Prefs(Activity activity) {
        this.sharedPreferences = activity.getPreferences(activity.MODE_PRIVATE);
    }

    public void saveHighScore(int score) {
        int currentScore = score;
        int lastScore = sharedPreferences.getInt(key, 0);

        if (currentScore > lastScore) {
            sharedPreferences.edit().putInt(key, currentScore).apply();
        }
    }

    public int getHighScore() {
        return sharedPreferences.getInt(key, 0);
    }

    public void saveState(int index) {
        sharedPreferences.edit().putInt("index", index).apply();
    }

    public int getState() {
        return sharedPreferences.getInt("index", 0);
    }
}


