package com.jason.avengers.skill.beans;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.annotations.SerializedName;
import com.jason.avengers.R;
import com.jason.avengers.base.BaseItemBean;
import com.jason.avengers.base.BaseItemViewHolder;
import com.jason.avengers.common.configs.ItemViewTypeConfig;
import com.jason.avengers.widgets.linebreak.LineBreakAdapter;
import com.jason.avengers.widgets.linebreak.LineBreakLayout;
import com.jason.avengers.widgets.linebreak.LineBreakListener;

import java.util.List;

/**
 * Created by jason on 2018/4/4.
 */

public class SkillInfoBean extends BaseItemBean {

    public static final int ItemViewType = ItemViewTypeConfig.SKILL;

    @SerializedName("kind")
    private String kind;

    @SerializedName("level")
    private String level;

    @SerializedName("skillPoints")
    private List<String> skillPoints;

    // 当前点击了的技能点
    private String currentClickedPoint;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public List<String> getSkillPoints() {
        return skillPoints;
    }

    public void setSkillPoints(List<String> skillPoints) {
        this.skillPoints = skillPoints;
    }

    public String getCurrentClickedPoint() {
        return currentClickedPoint;
    }

    public void setCurrentClickedPoint(String currentClickedPoint) {
        this.currentClickedPoint = currentClickedPoint;
    }

    @Override
    public int getItemViewType() {
        return ItemViewType;
    }

    @Override
    public void onBindViewHolder(final BaseItemViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        ((TextView) holder.getView(R.id.skill_info_kind)).setText(getKind());
        holder.getView(R.id.skill_info_level).setVisibility(TextUtils.isEmpty(getLevel()) ? View.GONE : View.VISIBLE);
        ((TextView) holder.getView(R.id.skill_info_level)).setText(getLevel());
        ((LineBreakLayout) holder.getView(R.id.skill_info_points)).setAdapter(
                new LineBreakAdapter(holder.getContext(), getSkillPoints(),
                        new LineBreakListener() {
                            @Override
                            public void onClick(View view, int position, String data) {
                                setCurrentClickedPoint(data);
                                holder.doAction(R.id.skill_info_points, SkillInfoBean.this);
                            }
                        }));
    }

    public static BaseItemViewHolder onCreateViewHolder(ViewGroup parent) {
        return new BaseItemViewHolder(parent, R.layout.layout_item_view_skill_info);
    }
}
