package com.jason.avengers.other.calendar.holders;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jason.avengers.common.database.entity.other.calendar.CalendarOwnerDBEntity;
import com.jason.avengers.other.R;
import com.jason.avengers.other.calendar.listeners.OwnersClickListener;

/**
 * @author Jason
 */
public class OwnersHolder extends RecyclerView.ViewHolder {

    public CalendarOwnerDBEntity entity;
    public final EditText ownerName;
    public final EditText ownerLocation;
    public final TextView ownerColor;
    public final Button ownerDetail, ownerSave, ownerDelete;

    public OwnersHolder(View itemView, final OwnersClickListener listener) {
        super(itemView);
        ownerName = itemView.findViewById(R.id.owner_name);
        ownerLocation = itemView.findViewById(R.id.owner_location);
        ownerColor = itemView.findViewById(R.id.owner_color);
        ownerDetail = itemView.findViewById(R.id.owner_action_detail);
        ownerSave = itemView.findViewById(R.id.owner_action_save);
        ownerDelete = itemView.findViewById(R.id.owner_action_delete);

        ownerName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                entity.setOwner(ownerName.getText().toString());
            }
        });

        ownerLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                entity.setLocaltion(ownerLocation.getText().toString());
            }
        });

        ownerColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onColorClickListener(OwnersHolder.this, view);

            }
        });

        ownerDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDetailClickListener(OwnersHolder.this, view);
            }
        });

        ownerSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSaveClickListener(OwnersHolder.this, view);
            }
        });

        ownerDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDeleteClickListener(OwnersHolder.this, view);
            }
        });
    }

    public void bindView(CalendarOwnerDBEntity entity) {
        this.entity = entity;

        if (entity == null) {
            return;
        }
        ownerName.setText(entity.getOwner());
        ownerLocation.setText(entity.getLocaltion());

        Context context = itemView.getContext();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ownerName.setBackgroundColor(context.getResources().getColor(entity.getColor(), null));
            ownerLocation.setBackgroundColor(context.getResources().getColor(entity.getColor(), null));
            ownerColor.setBackgroundColor(context.getResources().getColor(entity.getColor(), null));
        } else {
            ownerName.setBackgroundColor(context.getResources().getColor(entity.getColor()));
            ownerLocation.setBackgroundColor(context.getResources().getColor(entity.getColor()));
            ownerColor.setBackgroundColor(context.getResources().getColor(entity.getColor()));
        }

        if (entity.getId() > 0) {
            ownerSave.setText(R.string.other_calendar_owners_action_update);
        } else {
            ownerSave.setText(R.string.other_calendar_owners_action_save);
        }
    }
}