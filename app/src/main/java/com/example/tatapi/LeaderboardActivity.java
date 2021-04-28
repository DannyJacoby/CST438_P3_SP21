package com.example.tatapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseQuery;
import com.parse.ParseUser;

public class LeaderboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        // Set up array adapter
        ListView leaderboardView =findViewById(R.id.leaderboardList);
        LeaderboardListAdapter listAdapter = new LeaderboardListAdapter(this);
        leaderboardView.setAdapter(listAdapter);

        // Get top 10 players
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.orderByDescending("highScore");
        query.addAscendingOrder("updatedAt");
        query.setLimit(10);
        query.findInBackground((objects, e) -> {
            if (e == null) {
                for (ParseUser user : objects) {
                    listAdapter.add(user);
                }
            }
            else {
                Log.d("Leaderboard", "Query failed: " + e.getMessage());
            }
        });
    }

    public class LeaderboardListAdapter extends ArrayAdapter<ParseUser> {

        public LeaderboardListAdapter(Activity context) {
            super(context, R.layout.leaderboard_layout);
        }

        @NonNull
        @Override
        public View getView(int position, View view, @NonNull ViewGroup parent) {
            LayoutInflater inflater = LeaderboardActivity.this.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.leaderboard_layout, null, true);
            TextView rowField = rowView.findViewById(R.id.leaderboard_row);

            ParseUser user = getItem(position);

            rowField.setText(String.format("level %d - %s", user.getInt("highScore"), user.getString("username")));

            return rowView;
        }
    }

    public static Intent intent_factory(Context context) {
        return new Intent(context, LeaderboardActivity.class);
    }
}