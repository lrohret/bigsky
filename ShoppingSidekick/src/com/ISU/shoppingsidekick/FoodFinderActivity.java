package com.ISU.shoppingsidekick;
import com.Database.API.*;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

/**
 * Food Finder page that allows the user to search for a particular food item
 * or food group in our database
 * @author Jeremy Curtiss
 *
 */

public class FoodFinderActivity extends Activity {

	EditText searchField;
	ListView resultsList;
	Button searchBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_food_finder);
		
		resultsList = (ListView) findViewById(R.id.searchResultsList);
		
		//Search button and search field
        searchBtn = (Button) findViewById(R.id.searchButton);
        searchBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Thread thread = new Thread()
				{
					@Override
		            public void run()
					{
						synchronized(this)
						{
							String st = getSearchFieldText();
							List<Food> list = getSearchResults(st);
							populateResultsList(list);
						};
					}
				};
				thread.start();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.food_finder, menu);
		return true;
	}

	/**
	 * Gets the text entered in the searchField
	 * @return String of text from searchField
	 */
	private String getSearchFieldText(){
		searchField = (EditText)findViewById(R.id.searchField);
		return searchField.getText().toString();
	}
	
	/**
	 * Searches database for food entries matching the searchText
	 * @param searchText the text input from the searchField
	 * @return list of food items from database based off of our search text
	 */
	private List<Food> getSearchResults(String searchText){
		DatabaseAPI d = new DatabaseAPI();
		List<Food> setFromName = d.getFoodByFuzzyNameMatch(searchText);
		List<Food> setFromGroup = d.getFoodByFuzzyFoodGroupMatch(searchText);
		for(int i = 0; i < setFromGroup.size(); i++){
			if(!setFromName.contains(setFromGroup.get(i)))
				setFromName.add((Food) setFromGroup.get(i));
		}
		return setFromName;
	}
	
	private void populateResultsList(List<Food> results){
		final ArrayAdapter<Food> adapter;
		adapter = new ArrayAdapter<Food>(this, R.layout.activity_food_finder,
										R.id.searchResultsList, results);
	}
}
