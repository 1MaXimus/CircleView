package ru.pakhotin.circleview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements Button.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Resources res = getResources();
        int color_w,color_g;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            color_w = res.getColor(R.color.water_color, getTheme());
            color_g = res.getColor(R.color.gas_color, getTheme());
        } else {
            color_w = res.getColor(R.color.water_color);
            color_g = res.getColor(R.color.gas_color);
        }
        ConstraintLayout main = (ConstraintLayout) findViewById(R.id.mainLayout);
        Button button_w = main.findViewById(R.id.water);
        button_w.setTag(R.string.tag_color, color_w);
        button_w.setTag(R.string.tag_state,false);
        button_w.setOnClickListener(this);
        Button button_g = main.findViewById(R.id.gas);
        button_g.setTag(R.string.tag_color,color_g);
        button_g.setTag(R.string.tag_state,false);
        button_g.setOnClickListener(this);
        button_g.setTag(R.string.tag_button,button_w);
        button_w.setTag(R.string.tag_button,button_g);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View v) {
        final Button cButton = (Button) v ;
        final Button otherButton = (Button) cButton.getTag(R.string.tag_button);
        final ConstraintLayout main = (ConstraintLayout) findViewById(R.id.mainLayout);

        if (!(boolean)cButton.getTag(R.string.tag_state)) {
            cButton.setTag(R.string.tag_state, true);
            cButton.bringToFront();

            final ConstraintLayout.LayoutParams cButtonLayoutParams = (ConstraintLayout.LayoutParams) cButton.getLayoutParams();
            final ConstraintLayout.LayoutParams otherButtonLayoutParams = (ConstraintLayout.LayoutParams) otherButton.getLayoutParams();
            final float cdelt = (cButtonLayoutParams.horizontalBias - 0.5f);
            final float odelt = ( otherButtonLayoutParams.horizontalBias - 0.5f);
            ValueAnimator animation = ValueAnimator.ofFloat(1f, 0f);
            animation.setDuration(500);
            animation.start();
            animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator updatedAnimation) {

                    float animatedValue = (float)updatedAnimation.getAnimatedValue();

                    cButtonLayoutParams.horizontalBias = 0.5f + cdelt * animatedValue;
                    cButton.setLayoutParams(cButtonLayoutParams);
                    otherButtonLayoutParams.horizontalBias = 0.5f + odelt * animatedValue;
                    otherButton.setLayoutParams(otherButtonLayoutParams);
                }
            });
            animation.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    final View cView = new CircleView(MainActivity.this, (int) cButton.getTag(R.string.tag_color));
                    cView.setId(R.id.circleViewId);

                    ConstraintLayout.LayoutParams cPa = new ConstraintLayout.LayoutParams(0, 0);
                    cView.setLayoutParams(cPa);
                    cView.setAlpha(0);
                    main.addView(cView, 0);
                    ConstraintSet constraintSet = new ConstraintSet();
                    constraintSet.clone(main);
                    constraintSet.connect(R.id.circleViewId, ConstraintSet.START, main.getId(), ConstraintSet.START, 0);
                    constraintSet.connect(R.id.circleViewId, ConstraintSet.END, main.getId(), ConstraintSet.END, 0);
                    constraintSet.connect(R.id.circleViewId, ConstraintSet.TOP, main.getId(), ConstraintSet.TOP, 0);
                    constraintSet.connect(R.id.circleViewId, ConstraintSet.BOTTOM, main.getId(), ConstraintSet.BOTTOM, 0);
                    constraintSet.setDimensionRatio(R.id.circleViewId, "1:1");

                    constraintSet.applyTo(main);
                    cView.animate().alpha(1).setDuration(500).start();
                    main.removeView(otherButton);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }


            });

        } else {
            View cView = main.findViewById(R.id.circleViewId);
            cView.clearAnimation();
            cView.setPivotX(cView.getWidth() / 2);
            cView.setPivotY(cView.getHeight() / 2);
            ViewPropertyAnimator va = cView.animate();
            va.cancel();
            va.rotationBy(360);
            va.setDuration(2000);
            va.start();
        }
    }
}