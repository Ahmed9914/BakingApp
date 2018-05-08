package nanodegree.udacity.bakingapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nanodegree.udacity.bakingapp.R;
import nanodegree.udacity.bakingapp.masterListComponents.RecipeComponentsListActivity;
import nanodegree.udacity.bakingapp.model.Step;

public class RecipeComponentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final RecipeComponentsListActivity mParentActivity;
    private final List<Step> steps;

    String mIngredientsText;

    private static final int VIEW_TYPE_INGREDIENT = 0;
    private static final int VIEW_TYPE_STEP = 1;

    OnStepAdapterClickListener mStepClickListener;
    public interface OnStepAdapterClickListener {
        void onStepSelected(Step step);
    }

    public RecipeComponentsAdapter(RecipeComponentsListActivity parent,
                                   List<Step> steps, String ingredientsText
            ,OnStepAdapterClickListener stepClickListener) {
        this.steps = steps;
        mIngredientsText = ingredientsText;
        mParentActivity = parent;
        mStepClickListener  = stepClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {

            case VIEW_TYPE_INGREDIENT: {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.ingredients_tv, parent, false);
                return new IngredientsViewHolder(view);
            }

            case VIEW_TYPE_STEP: {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recipe_steps_list_content, parent, false);
                return new StepViewHolder(view);
            }

            default:
                throw new IllegalArgumentException("Invalid view type, value of " + viewType);
        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {

            case VIEW_TYPE_INGREDIENT:
                IngredientsViewHolder ingredientsViewHolderHolder = (IngredientsViewHolder) holder;
                ingredientsViewHolderHolder.ingredientsTV.setText(mIngredientsText);
                break;

            case VIEW_TYPE_STEP:
                StepViewHolder stepViewHolder = (StepViewHolder) holder;
                stepViewHolder.mIdView.setText(String.valueOf(steps.get(position-1).getId()));
                stepViewHolder.mContentView.setText(steps.get(position-1).getShortDescription());
                break;

            default:
                throw new IllegalArgumentException("Invalid view type, value of " + viewType);
        }

    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_INGREDIENT;
        }
        return VIEW_TYPE_STEP;
    }

    class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.content) TextView mContentView;
        @BindView(R.id.step_id_tv) TextView mIdView;

        StepViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            if (adapterPosition > 0) {
                Step currentStep = steps.get(adapterPosition-1);
                mStepClickListener.onStepSelected(currentStep);
            }
        }
    }

    class IngredientsViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.ingredients_text) TextView ingredientsTV;

        IngredientsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
