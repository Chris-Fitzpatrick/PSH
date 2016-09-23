package com.lunaandchris.projstudenthousing.psh;

import android.util.AttributeSet;
import android.view.View;
import android.content.Context;

/**
 * Created by CFitzpatrick on 9/23/16.
 */

public class view1 extends View{
    public view1(Context context){
        super (context);
    }

    public view1(Context context, AttributeSet attribs){
        super (context, attribs);
        init (attribs, 0);
    }

    public view1(Context context, AttributeSet attribs, int defStyle){
        super(context, attribs, defStyle);
        init (attribs, defStyle);
    }

    private void init(AttributeSet attribs, int defStyle){

    }
}
