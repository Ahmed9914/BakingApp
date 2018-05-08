package nanodegree.udacity.bakingapp.widget;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import java.util.ArrayList;

public class IngredientsWidgetService extends IntentService {
    public static String INGREDIENTS_FROM_ACTIVITY_KEY ="ingredients-from-activity";

    public IngredientsWidgetService() {
        super("IngredientsWidgetService");
    }

    public static void startIngredientsWidgetService(Context context,String sentIngredients) {
        Intent intent = new Intent(context, IngredientsWidgetService.class);
        intent.putExtra(INGREDIENTS_FROM_ACTIVITY_KEY, sentIngredients);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String ingredientsList = intent
                    .getExtras()
                    .getString(INGREDIENTS_FROM_ACTIVITY_KEY);
            Intent sentIntentToWidget = new Intent("android.appwidget.action.APPWIDGET_UPDATE_INGREDIENTS");
            sentIntentToWidget.setAction("android.appwidget.action.APPWIDGET_UPDATE_INGREDIENTS");
            sentIntentToWidget.putExtra(INGREDIENTS_FROM_ACTIVITY_KEY, ingredientsList);
            sendBroadcast(sentIntentToWidget);

        }
    }

}
