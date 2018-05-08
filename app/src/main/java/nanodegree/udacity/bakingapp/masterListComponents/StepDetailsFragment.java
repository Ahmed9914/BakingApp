package nanodegree.udacity.bakingapp.masterListComponents;

//import android.support.design.widget.CollapsingToolbarLayout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import nanodegree.udacity.bakingapp.R;
import nanodegree.udacity.bakingapp.model.Step;


/**
 * A fragment representing a single RecipeStep detail screen.
 * This fragment is either contained in a {@link RecipeComponentsListActivity}
 * in two-pane mode (on tablets) or a {@link StepDetailsActivity}
 * on handsets.
 */
public class StepDetailsFragment extends Fragment {

    @BindView(R.id.step_full_description_tv) TextView stepDescriptionTV;
    @BindView(R.id.video_view) SimpleExoPlayerView videoPlayerView;
    @BindView(R.id.next_button) Button next_button;
    @BindView(R.id.prev_button) Button prev_button;

    ArrayList<Step> steps;
    int stepIndex;
    Step currentStep;
    public static boolean areButtonsVisible = true;
    public static final String STEPS_SAVE = "steps-save";
    public static final String STEPS_INDEX_SAVE = "step-index";
    private SimpleExoPlayer player;
    private long playbackPosition;
    private int currentWindow;
    private boolean playWhenReady = true;
    String stepVideoUrl;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StepDetailsFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(STEPS_SAVE, steps);
        outState.putInt(STEPS_INDEX_SAVE, stepIndex);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            steps = savedInstanceState.getParcelableArrayList(STEPS_SAVE);
            stepIndex = savedInstanceState.getInt(STEPS_INDEX_SAVE);
        }
        View rootView = inflater.inflate(R.layout.fragment_recipe_step_detail, container, false);
        ButterKnife.bind(this, rootView);

        currentStep = steps.get(stepIndex);
        if (currentStep != null) {
            stepDescriptionTV.setText(currentStep.getDescription());
            stepVideoUrl = currentStep.getVideoURL();

            if (areButtonsVisible) {
                next_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (stepIndex < steps.size() - 1) {
                            stepIndex++;
                            currentStep = steps.get(stepIndex);
                            StepDetailsActivity.ab.setTitle(currentStep.getShortDescription());
                            stepDescriptionTV.setText(currentStep.getDescription());
                            stepVideoUrl = currentStep.getVideoURL();
                            initializePlayer(stepVideoUrl);

                        } else {
                            stepIndex = steps.size() - 1;
                        }


                    }
                });
                prev_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (stepIndex > 0) {
                            stepIndex--;
                            currentStep = steps.get(stepIndex);
                            StepDetailsActivity.ab.setTitle(currentStep.getShortDescription());
                            stepDescriptionTV.setText(currentStep.getDescription());
                            stepVideoUrl = currentStep.getVideoURL();
                            initializePlayer(stepVideoUrl);

                        } else {
                            stepIndex = 0;
                        }
                    }
                });
            } else {
                next_button.setVisibility(View.GONE);
                prev_button.setVisibility(View.GONE);
            }
        }

        return rootView;
    }

    public void setStepIndex(int index) {
        stepIndex = index;
    }

    public void setStepsList(ArrayList<Step> stepsList) {
        steps = stepsList;
    }

    private void initializePlayer(String videoUrl) {
        if (videoUrl != null && !videoUrl.equals("")) {
            videoPlayerView.setVisibility(View.VISIBLE);
            player = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(getContext()),
                    new DefaultTrackSelector(), new DefaultLoadControl());

            videoPlayerView.setPlayer(player);

            player.setPlayWhenReady(playWhenReady);
            player.seekTo(currentWindow, playbackPosition);

            Uri uri = Uri.parse(videoUrl);
            MediaSource mediaSource = buildMediaSource(uri);
            player.prepare(mediaSource, true, false);
        } else {
            videoPlayerView.setVisibility(View.GONE);
        }

    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("Baking-App")).
                createMediaSource(uri);
    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer(stepVideoUrl);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer(stepVideoUrl);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }



}
