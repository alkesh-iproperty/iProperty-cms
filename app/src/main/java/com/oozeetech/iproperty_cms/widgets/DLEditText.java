package com.oozeetech.iproperty_cms.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;


/**
 * Created by root on 16/2/16.
 */
public class DLEditText extends EditText {

    public DLEditText(Context context) {
        super(context);

        EditTextHelper.setTypeface(context, this);
    }

    public DLEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        EditTextHelper.setTypeface(context, this);
    }

    public DLEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        EditTextHelper.setTypeface(context, this);
    }

    public static class EditTextHelper {

        private static Typeface typeface = null;

        public static void setTypeface(Context context, TextView textView) {

            if (typeface == null) {

                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
            }

            textView.setTypeface(typeface);
        }
    }
}
