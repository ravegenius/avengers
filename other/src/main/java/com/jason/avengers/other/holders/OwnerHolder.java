package com.jason.avengers.other.holders;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jason.avengers.other.R;
import com.jason.avengers.other.beans.OwnerBean;
import com.jason.avengers.other.listeners.OwnerClickListener;

/**
 * @author Jason
 */
public class OwnerHolder extends RecyclerView.ViewHolder {

    public OwnerBean ownerBean;
    public final EditText ownerName;
    public final EditText ownerLocation;
    public final TextView ownerColor;
    public final Button ownerDetail, ownerSave, ownerDelete;

    public OwnerHolder(LayoutInflater layoutInflater, ViewGroup parent, final OwnerClickListener listener) {
        super(layoutInflater.inflate(R.layout.other_layout_item_owner, parent, false));
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
                ownerBean.setName(ownerName.getText().toString());
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
                ownerBean.setLocation(ownerLocation.getText().toString());
            }
        });

        ownerColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onColorClickListener(OwnerHolder.this, view);

            }
        });

        ownerDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDetailClickListener(OwnerHolder.this, view);
            }
        });

        ownerSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ownerName.isFocusable()) {
                    ownerName.setFocusable(false);
                    ownerName.setFocusableInTouchMode(false);
                    ownerLocation.setFocusable(false);
                    ownerLocation.setFocusableInTouchMode(false);
                    if (ownerBean.getId() > 0) {
                        ownerSave.setText(R.string.other_owners_action_update);
                    } else {
                        ownerSave.setText(R.string.other_owners_action_add);
                    }

                    listener.onSaveClickListener(OwnerHolder.this, view);
                } else {
                    ownerName.setFocusable(true);
                    ownerName.setFocusableInTouchMode(true);
                    ownerLocation.setFocusable(true);
                    ownerLocation.setFocusableInTouchMode(true);
                    ownerSave.setText(R.string.other_owners_action_save);
                }
            }
        });

        ownerDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDeleteClickListener(OwnerHolder.this, view);
            }
        });
    }

    public void bindView(OwnerBean ownerBean) {
        this.ownerBean = ownerBean;

        if (ownerBean == null) {
            return;
        }

        ownerName.setText(ownerBean.getName());
        ownerName.setFocusable(false);
        ownerName.setFocusableInTouchMode(false);

        ownerLocation.setText(ownerBean.getLocation());
        ownerLocation.setFocusable(false);
        ownerLocation.setFocusableInTouchMode(false);

        Context context = itemView.getContext();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            itemView.setBackgroundColor(context.getResources().getColor(ownerBean.getColor(), null));
        } else {
            itemView.setBackgroundColor(context.getResources().getColor(ownerBean.getColor()));
        }
        if (ownerBean.getId() > 0) {
            ownerSave.setText(R.string.other_owners_action_update);
        } else {
            ownerSave.setText(R.string.other_owners_action_add);
        }
    }
}