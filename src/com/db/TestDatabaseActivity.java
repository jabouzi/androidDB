package com.db;

import java.util.List;
import java.util.Random;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.util.Log;

public class TestDatabaseActivity extends ListActivity {
	private CommentsDataSource datasource;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		datasource = new CommentsDataSource(this);
		datasource.open();
		Log.i("DB ", "Created" + datasource.getComment(1).getComment());
		List<Comment> values = datasource.getAllComments();

		// Use the SimpleCursorAdapter to show the
		// elements in a ListView
		ArrayAdapter<Comment> adapter = new ArrayAdapter<Comment>(this,
				android.R.layout.simple_list_item_1, values);
		setListAdapter(adapter);
	}

	// Will be called via the onClick attribute
	// of the buttons in main.xml
	public void onClick(View view) {
		@SuppressWarnings("unchecked")
		ArrayAdapter<Comment> adapter = (ArrayAdapter<Comment>) getListAdapter();
		Comment comment = null;
		switch (view.getId()) {
		case R.id.add:
			String[] comments = new String[] { "Cool", "Very nice", "Hate it" };
			int nextInt = new Random().nextInt(3);
			// Save the new comment to the database
			comment = datasource.createComment(comments[nextInt]);
			adapter.add(comment);
			break;
		case R.id.delete:
			if (getListAdapter().getCount() > 0) {
				comment = (Comment) getListAdapter().getItem(0);
				datasource.deleteComment(comment);
				adapter.remove(comment);
			}
			break;
		}
		adapter.notifyDataSetChanged();
		Log.i("DB ", "COUNT" + String.valueOf(datasource.getCommentsCount()));
	}

	@Override
	protected void onResume() {
		super.onResume();
		datasource.open();
	}

	@Override
	protected void onPause() {
		super.onPause();
		datasource.close();
	}

}
