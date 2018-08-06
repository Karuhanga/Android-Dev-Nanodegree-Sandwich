package com.udacity.sandwichclub;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.DebugUtils;
import com.udacity.sandwichclub.utils.JsonUtils;
import com.udacity.sandwichclub.utils.ViewUtils.OnSwipeTouchListener;

import static com.udacity.sandwichclub.utils.StringUtils.stringify;
import static com.udacity.sandwichclub.utils.ViewUtils.ViewUtils.hide;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ingredientsIv = findViewById(R.id.image_iv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json;
        try {
            json = sandwiches[position];
        }catch (IndexOutOfBoundsException e){
            Log.e(DebugUtils.TAG, "DetailActivity#onCreate: Reached the end", e);
            closeOnReachedEnd();
            return;
        }
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());

        setupSwipeListeners(position);
    }

    private void setupSwipeListeners(final int position) {
        findViewById(R.id.image_iv).setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeLeft() {
                finish();
                launchDetailActivity(DetailActivity.this, position + 1);
            }

            @Override
            public void onSwipeRight() {
                finish();
                launchDetailActivity(DetailActivity.this, position - 1);
            }
        });
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void closeOnReachedEnd() {
        finish();
    }

    // populates UI based on what data is available
    private void populateUI(Sandwich sandwich) {
        TextView alsoKnownAs = findViewById(R.id.also_known_tv);
        TextView placeOfOrigin = findViewById(R.id.origin_tv);
        TextView description = findViewById(R.id.description_tv);
        TextView ingredients = findViewById(R.id.ingredients_tv);

        View alsoKnownAsHolder = findViewById(R.id.label_holder_also_known);
        View placeOfOriginHolder = findViewById(R.id.label_holder_origin);
        View ingredientsHolder = findViewById(R.id.label_holder_ingredients);

        if (sandwich.getAlsoKnownAs().isEmpty()){
            hide(alsoKnownAsHolder);
            hide(alsoKnownAs);
        }
        else{
            alsoKnownAs.setText(stringify(sandwich.getAlsoKnownAs()));
        }

        if (sandwich.getPlaceOfOrigin().isEmpty()){
            hide(placeOfOriginHolder);
            hide(placeOfOrigin);
        }
        else{
            placeOfOrigin.setText(sandwich.getPlaceOfOrigin());
        }

        if (sandwich.getDescription().isEmpty()){
            hide(description);
        }
        else{
            description.setText(sandwich.getDescription());
        }

        if (sandwich.getIngredients().isEmpty()){
            hide(ingredientsHolder);
            hide(ingredients);
        }
        else{
            ingredients.setText(stringify(sandwich.getIngredients()));
        }
    }

    public static void launchDetailActivity(Context context, int position) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_POSITION, position);
        context.startActivity(intent);
    }
}
