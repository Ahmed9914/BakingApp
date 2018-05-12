package nanodegree.udacity.bakingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import nanodegree.udacity.bakingapp.ui.RecipeComponentsListActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;

@RunWith(AndroidJUnit4.class)
public class IntentsTest {

    @Rule
    public IntentsTestRule<MainActivity> mActivityTestRule =
            new IntentsTestRule<>(MainActivity.class);

    @Test
    public void checkIntent_RecipeDetailsActivity() {
        onView(ViewMatchers.withId(R.id.recipes_RV))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1,click()));
        intended(hasComponent(RecipeComponentsListActivity.class.getName()));

    }
}
