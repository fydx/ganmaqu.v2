package com.xstrikers.ganmaquv2.ui.widget;

import android.content.Context;
import android.media.Image;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.xstrikers.ganmaquv2.R;

import org.w3c.dom.Text;

/**
 * Created by LB on 14-2-15.
 */
public class typeSelectItem extends LinearLayout {
    private ImageView imageView;
    private CheckBox checkBox;
    private TextView textView;

    public typeSelectItem(Context context) {
        super(context);
    }
    public typeSelectItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        //想自定义 字体的颜色、大小可以在这实现，具体请参考前一篇文章

        init(context);
    }
    private void init(Context context) {
        // 导入布局
        LayoutInflater mInflater = LayoutInflater.from(context);
        mInflater.inflate(R.layout.viewgroup_type, this, true); //注意此次最后一个参数为 true
        imageView = (ImageView) findViewById(R.id.imageView_typeSelect);
        textView = (TextView) findViewById(R.id.textview_typeSelect);
        checkBox = (CheckBox) findViewById(R.id.checkbox_typeSelect);

    }
}
