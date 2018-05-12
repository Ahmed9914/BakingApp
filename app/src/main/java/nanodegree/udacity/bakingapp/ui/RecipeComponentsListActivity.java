package nanodegree.udacity.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import nanodegree.udacity.bakingapp.R;
import nanodegree.udacity.bakingapp.MainActivity;
import nanodegree.udacity.bakingapp.model.Recipe;
import nanodegree.udacity.bakingapp.model.Step;

/**
 * An activity representing a list of RecipeSteps. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link StepDetailsActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeComponentsListActivity extends AppCompatActivity implements RecipeComponentsListFragment.OnStepClickListener {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    public Recipe recipe;

    public static final String STEPS_LIST_KEY = "steps-list";
    public static final String STEP_INDEX_KEY = "step-index";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recipe = getIntent().getParcelableExtra(MainActivity.RECIPE_DETAILS_KEY);
        setContentView(R.layout.activity_recipe_steps_list);

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(recipe.getName());
            ab.setDisplayHomeAsUpEnabled(true);
        }

        if (getResources().getBoolean(R.bool.isTablet)) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                StepDetailsFragment fragment = new StepDetailsFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.recipe_step_detail_container, fragment)
                        .commit();
                fragment.setStepIndex(0);
                fragment.setStepsList(recipe.getSteps());
                fragment.areButtonsVisible = false;
            }
        } else {
            mTwoPane = false;
        }
    }


    @Override
    public void onStepSelected(Step step) {
        if (mTwoPane) {
            StepDetailsFragment newFragment = new StepDetailsFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_step_detail_container, newFragment)
                    .commit();
            newFragment.setStepIndex(step.getId());
            newFragment.setStepsList(recipe.getSteps());
            newFragment.areButtonsVisible = false;
        } else {
            final Intent intent = new Intent(this, StepDetailsActivity.class);
            intent.putExtra(STEP_INDEX_KEY, step.getId());
            intent.putParcelableArrayListExtra(STEPS_LIST_KEY, recipe.getSteps());
            startActivity(intent);
        }

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
