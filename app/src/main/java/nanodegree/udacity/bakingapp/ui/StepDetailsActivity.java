package nanodegree.udacity.bakingapp.ui;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.ArrayList;

import nanodegree.udacity.bakingapp.R;
import nanodegree.udacity.bakingapp.model.Step;

/**
 * An activity representing a single RecipeStep detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeComponentsListActivity}.
 */
public class StepDetailsActivity extends AppCompatActivity {

    ArrayList<Step> steps;
    int stepIndex;
    public static ActionBar ab;
    public static final String ACTIONBAR_TITLE = "actionbar-title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);

        if (savedInstanceState != null && savedInstanceState.getString(ACTIONBAR_TITLE) != null) {
            ab = getSupportActionBar();
            if (ab!=null) {
                ab.setTitle(savedInstanceState.getString(ACTIONBAR_TITLE));
                ab.setDisplayHomeAsUpEnabled(true);
            }
        } else {

            // savedInstanceState is non-null when there is fragment state
            // saved from previous configurations of this activity
            // (e.g. when rotating the screen from portrait to landscape).
            // In this case, the fragment will automatically be re-added
            // to its container so we don't need to manually add it.
            // For more information, see the Fragments API guide at:
            //
            // http://developer.android.com/guide/components/fragments.html
            //
            steps = getIntent().getParcelableArrayListExtra(RecipeComponentsListActivity.STEPS_LIST_KEY);
            stepIndex = getIntent().getIntExtra(RecipeComponentsListActivity.STEP_INDEX_KEY, 0);
            Step currentStep =  steps.get(stepIndex);
            ab = getSupportActionBar();
            if (ab!=null) {
                ab.setTitle(currentStep.getShortDescription());
                ab.setDisplayHomeAsUpEnabled(true);
            }

            StepDetailsFragment fragment = new StepDetailsFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipe_step_detail_container, fragment)
                    .commit();

            fragment.setStepIndex(stepIndex);
            fragment.setStepsList(steps);
        }


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ACTIONBAR_TITLE, ab.getTitle().toString());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
