package com.jason.avengers.other.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jason.avengers.common.base.BaseNoMVPActivity;
import com.jason.avengers.common.router.RouterPath;
import com.jason.avengers.common.widgets.label.LabelBuilder;
import com.jason.avengers.common.widgets.label.LabelSpanStore;
import com.jason.avengers.other.R;

@Route(path = RouterPath.OTHER_MUTILLABEL)
public class MutilLabelActivity extends BaseNoMVPActivity {
    TextView labelTextView;
    EditText labelEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_activity_label);
        labelTextView = findViewById(R.id.label_text_view);
        labelEditText = findViewById(R.id.label_edit_text);
        labelEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                new LabelBuilder()
                        .content(s.toString())
                        .addSpan(LabelSpanStore.LabelRectSpan(Color.GRAY, "专专", Color.RED, 10))
                        .build(labelTextView);
            }
        });

        new LabelBuilder()
                .addSpan(LabelSpanStore.LabelImageSpan(this, Color.RED, "专专", R.drawable.other_richeditor_txt_color))
                .build(labelTextView);
    }
}
