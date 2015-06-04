package de.project.ice.utils;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

public class FakePerspectiveCamera extends OrthographicCamera {
    /**
     * x-axis dimension from far left to far right
     */
    public static final float FRUSTUM_WIDTH = 16f;
    /**
     * y-axis dimension from bottom to top
     */
    public static final float FRUSTUM_HEIGHT = 9f;
    public  static final float PIXELS_TO_METRES = 1.0f / 128.0f;

    /**
     * configure the horizon line from top to x percent coverage to bottom;
     * e.g. 0.5f equals a horizon line at the screen center
     */
    private float scaleHorizontPercentage = 0.7f;

    public FakePerspectiveCamera() {
        super(FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
    }

    public float calcDistanceScaling(float y)
    {
        float scalePart = ((FRUSTUM_HEIGHT * scaleHorizontPercentage) - y);
        //Gdx.app.log("Scale", "" + scalePart);
        float scaleDistance = 1f / (FRUSTUM_HEIGHT * scaleHorizontPercentage);

        return scaleDistance * scalePart;
    }

    /**
     * @param percentage between 0.1 and 1.0
     */
    public void setScaleHorizonPercentage(float percentage)
    {
        if(percentage >= 0.1f && percentage <= 1f)
            scaleHorizontPercentage = percentage;
    }

    public float getScaleHorizonPercentage()
    {
        return scaleHorizontPercentage;
    }

    public float getHorizonPosition() {
        return (FRUSTUM_HEIGHT * scaleHorizontPercentage);
    }
}
