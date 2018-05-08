package nanodegree.udacity.bakingapp.adapters;

import android.content.Context;
import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import nanodegree.udacity.bakingapp.R;
import nanodegree.udacity.bakingapp.model.Ingredient;
import nanodegree.udacity.bakingapp.model.Recipe;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipesViewHolder>{
    Context context;
    List<Recipe> recipes;

    public interface RecipesAdapterOnClickHandler {
        void onClick(Recipe recipe);
    }
    private RecipesAdapterOnClickHandler clickHandler;

    public RecipesAdapter(Context mContext, RecipesAdapterOnClickHandler mClickHandler){
        context = mContext;
        clickHandler = mClickHandler;
    }

    @Override
    public RecipesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutIdForGridItem = R.layout.recipes_grid_item;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(layoutIdForGridItem, parent, false);

        return new RecipesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipesViewHolder holder, int position) {
        holder.recipeTV.setText(recipes.get(position).getName());
        holder.servingsTV.setText(
                context.getString(R.string.recipe_servings_description, recipes.get(position).getServings())
        );
    }

    @Override
    public int getItemCount() {
        if (recipes == null || recipes.size() == 0) return 0;
        return recipes.size();
    }

    public void loadRecipes(List<Recipe> mRecipes){
        recipes = mRecipes;
        notifyDataSetChanged();
    }

    public class RecipesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.recipe_name_tv) TextView recipeTV;
        @BindView(R.id.recipe_servings_tv) TextView servingsTV;

    public RecipesViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int adapterPosition = getAdapterPosition();
        Recipe currentRecipe = recipes.get(adapterPosition);
        clickHandler.onClick(currentRecipe);
    }

}


}
