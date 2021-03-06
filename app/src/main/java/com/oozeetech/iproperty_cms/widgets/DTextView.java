package com.oozeetech.iproperty_cms.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;


/**
 * Created by root on 17/6/16.
 */
public class DTextView extends TextView {

    public DTextView(Context context) {
        super(context);

        TextViewHelper.setTypeface(context, this);
    }

    public DTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TextViewHelper.setTypeface(context, this);
    }

    public DTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TextViewHelper.setTypeface(context, this);
    }

    public static class TextViewHelper {

        private static Typeface typeface = null;

        public static void setTypeface(Context context, TextView textView) {

            if (typeface == null) {

                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
            }

            textView.setTypeface(typeface);
        }
    }
}


