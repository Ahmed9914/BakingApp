package nanodegree.udacity.bakingapp;

import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nanodegree.udacity.bakingapp.adapters.RecipesAdapter;
import nanodegree.udacity.bakingapp.masterListComponents.RecipeComponentsListActivity;
import nanodegree.udacity.bakingapp.model.Recipe;
import nanodegree.udacity.bakingapp.utils.NetworkUtils;

public class MainActivity extends AppCompatActivity implements RecipesAdapter.RecipesAdapterOnClickHandler{
    @BindView(R.id.error_message_display_tv) TextView errorMessageDisplay;
    @BindView(R.id.loading_indicator_pb) ProgressBar loadingIndicator;
    @BindView(R.id.recipes_RV) RecyclerView recipesRV;

    public RecipesAdapter recipesAdapter;
    int numberOfColumns;
    final static int RECIPES_LOADER_ID = 1;
    public static final String RECIPE_DETAILS_KEY = "recipe-details";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        ButterKnife.bind(this);

        if (getResources().getBoolean(R.bool.isTablet)) numberOfColumns = 3;
        else numberOfColumns = 1;

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, numberOfColumns);
        recipesRV.setLayoutManager(gridLayoutManager);
        recipesAdapter = new RecipesAdapter(this, this);
        recipesRV.setAdapter(recipesAdapter);
        getSupportLoaderManager().initLoader(RECIPES_LOADER_ID, null, recipesLoader);

    }
    private LoaderManager.LoaderCallbacks<List<Recipe>> recipesLoader =
            new LoaderManager.LoaderCallbacks<List<Recipe>>(){

                @Override
                public Loader<List<Recipe>> onCreateLoader(int id, Bundle args) {
                    return new AsyncTaskLoader<List<Recipe>>(MainActivity.this) {
                        List<Recipe> recipes = null;
                        @Override
                        protected void onStartLoading() {
                            if (recipes != null) {
                                deliverResult(recipes);
                            } else {
                                recipesRV.setVisibility(View.INVISIBLE);
                                loadingIndicator.setVisibility(View.VISIBLE);
                                forceLoad();
                            }
                        }

                        @Override
                        public List<Recipe> loadInBackground() {
                            try {
                                recipes = Arrays.asList(NetworkUtils
                                        .getAllRecipes());

                                return recipes;
                            } catch (Exception e) {
                                e.printStackTrace();
                                return null;
                            }
                        }

                        @Override
                        public void deliverResult(List<Recipe> data) {
                            recipes = data;
                            super.deliverResult(data);
                        }

                    };
                }

                @Override
                public void onLoadFinished(Loader<List<Recipe>> loader, List<Recipe> data) {
                    loadingIndicator.setVisibility(View.INVISIBLE);
                    recipesRV.setVisibility(View.VISIBLE);
                    recipesAdapter.loadRecipes(data);
                    if (data == null) {
                        showErrorMessage();
                    } else {
                        showRecipesView();
                    }
                }

                @Override
                public void onLoaderReset(Loader<List<Recipe>> loader) {

                }
            };

    @Override
    public void onClick(Recipe recipe) {
        Intent intentToStartDetailsActivity = new Intent(this, RecipeComponentsListActivity.class);
        intentToStartDetailsActivity.putExtra(RECIPE_DETAILS_KEY, recipe);
        startActivity(intentToStartDetailsActivity);

    }

    private void showErrorMessage() {
        recipesRV.setVisibility(View.INVISIBLE);
        errorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private void showRecipesView() {
        errorMessageDisplay.setVisibility(View.INVISIBLE);
        recipesRV.setVisibility(View.VISIBLE);
    }
}
