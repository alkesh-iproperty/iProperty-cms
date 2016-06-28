package com.oozeetech.iproperty_cms.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;


/**
 * @author VaViAn Labs.
 */
public class DButtonMaterial extends com.rey.material.widget.Button {

    public DButtonMaterial(Context context) {
        super(context);

        DButtonMaterialHelper.setTypeface(context,this);
    }

    public DButtonMaterial(Context context, AttributeSet attrs) {
        super(context, attrs);

        DButtonMaterialHelper.setTypeface(context,this);
    }

    public DButtonMaterial(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        DButtonMaterialHelper.setTypeface(context,this);
    }

    public static class DButtonMaterialHelper{

        private static Typeface typeface = null;

        public static void setTypeface(Context context,TextView textView){

            if(typeface==null){

                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
            }

            textView.setTypeface(typeface);
        }
    }

};