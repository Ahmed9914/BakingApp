package nanodegree.udacity.bakingapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import nanodegree.udacity.bakingapp.R;
import nanodegree.udacity.bakingapp.adapters.RecipeComponentsAdapter;
import nanodegree.udacity.bakingapp.model.Ingredient;
import nanodegree.udacity.bakingapp.model.Recipe;
import nanodegree.udacity.bakingapp.model.Step;
import nanodegree.udacity.bakingapp.widget.IngredientsWidgetService;

public class RecipeComponentsListFragment extends Fragment implements RecipeComponentsAdapter.OnStepAdapterClickListener{
    @Override
    public void onStepSelected(Step step) {
        mCallback.onStepSelected(step);
    }

    @BindView(R.id.recipe_steps_rv) RecyclerView stepsRV;

    // Define a new interface OnImageClickListener that triggers a callback in the host activity
    OnStepClickListener mCallback;
    // OnImageClickListener interface, calls a method in the host activity named onImageSelected
    public interface OnStepClickListener {
        void onStepSelected(Step step);
    }

    //public RecipeComponentsAdapter.OnStepClickListener stepsListener;
    //implements RecipeComponentsAdapter.OnStepClickListener
    Recipe currentRecipe;
    private int mPosition = RecyclerView.NO_POSITION;

    public RecipeComponentsListFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_recipe_steps_list, container, false);
        ButterKnife.bind(this, rootView);

        String ingredientsText = "Ingredients:\n";
        for (Ingredient ingredient:currentRecipe.getIngredients()) {
            ingredientsText += Math.round(ingredient.getQuantity()) + " "
                    + ingredient.getMeasure() + " of " + ingredient.getIngredient() + "\n";
        }

        IngredientsWidgetService.startIngredientsWidgetService(getContext(), ingredientsText);

        stepsRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        stepsRV.setAdapter(new RecipeComponentsAdapter((RecipeComponentsListActivity)getActivity(),
                 currentRecipe.getSteps(), ingredientsText, this));
                //, ((RecipeComponentsListActivity) getActivity()).stepsListener));
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;

        // Return the root view
        return rootView;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            currentRecipe = ((RecipeComponentsListActivity)getActivity()).recipe;
            mCallback = (OnStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnImageClickListener");
        }

    }
}
