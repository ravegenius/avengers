package com.jason.avengers.other.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jason.avengers.accessibility.OAAccessibilityService;
import com.jason.avengers.common.base.BaseNoMVPActivity;
import com.jason.avengers.common.database.ObjectBoxBuilder;
import com.jason.avengers.common.database.entity.accessibility.LogDBEntity;
import com.jason.avengers.common.database.entity.accessibility.LogDBEntity_;
import com.jason.avengers.common.router.RouterPath;
import com.jason.avengers.other.R;

import java.util.List;

import io.objectbox.Box;
import io.objectbox.query.Query;

@Route(path = RouterPath.OTHER_OACLOCK)
public class OAClockActivity extends BaseNoMVPActivity implements View.OnClickListener {

    private static final int LIMIT = 100;

    Box<LogDBEntity> mLogBox;
    Query<LogDBEntity> mQuery;
    LinearLayoutManager mLogLayoutManager;
    LogAdapter mLogAdapter;
    int pageNo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLogBox = ObjectBoxBuilder.INSTANCE.getBoxStore().boxFor(LogDBEntity.class);
        mQuery = mLogBox.query().orderDesc(LogDBEntity_.__ID_PROPERTY).build();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_activity_oaclock);
        initView();
    }

    private void initView() {
        TextView developerStateView = findViewById(R.id.oaclock_developer_state);
        developerStateView.setText("伪开发者状态 <" + String.valueOf(OAAccessibilityService.IS_CLOCK_ADB_ENABLED).toUpperCase() + ">");
        developerStateView.setOnClickListener(this);

        TextView logClearView = findViewById(R.id.oaclock_log_clear);
        logClearView.setText("清除数据");
        logClearView.setOnClickListener(this);

        TextView developerModelView = findViewById(R.id.oaclock_developer_model);
        developerModelView.setText("开发者模式");
        developerModelView.setOnClickListener(this);

        TextView accessibilityModelView = findViewById(R.id.oaclock_accessibility_model);
        accessibilityModelView.setText("无障碍模式");
        accessibilityModelView.setOnClickListener(this);

        RecyclerView logView = findViewById(R.id.oaclock_log);
        logView.setLayoutManager(mLogLayoutManager = new LinearLayoutManager(this));
        logView.setAdapter(mLogAdapter = new LogAdapter(null));
        logView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int position = mLogLayoutManager.findFirstVisibleItemPosition();
                if (position == LIMIT / 2 && mLogAdapter.getItemCount() <= pageNo * LIMIT) {
                    recyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mLogAdapter.getItemCount() > pageNo * LIMIT){
                                return;
                            }
                            int newPageNo = pageNo + 1;
                            List<LogDBEntity> data = mQuery.find(newPageNo * LIMIT, LIMIT);
                            if (!data.isEmpty()) {
                                pageNo = newPageNo;
                                mLogAdapter.notifyAddData(data);
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<LogDBEntity> data = mQuery.find(pageNo * LIMIT, LIMIT);
        mLogAdapter.notifyData(data);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.oaclock_developer_state) {
            OAAccessibilityService.IS_CLOCK_ADB_ENABLED = !OAAccessibilityService.IS_CLOCK_ADB_ENABLED;
            ((TextView) view).setText("伪开发者状态 <" + String.valueOf(OAAccessibilityService.IS_CLOCK_ADB_ENABLED).toUpperCase() + ">");
        } else if (id == R.id.oaclock_log_clear) {
            pageNo = 0;
            mLogBox.removeAll();
            List<LogDBEntity> data = mQuery.find(pageNo * LIMIT, LIMIT);
            mLogAdapter.notifyData(data);
        } else if (id == R.id.oaclock_developer_model) {
            OAAccessibilityService.startDevelopmentActivity(this);
        } else if (id == R.id.oaclock_accessibility_model) {
            OAAccessibilityService.startAccessibilityServiceSettings(this);
        }
    }

    class LogAdapter extends RecyclerView.Adapter<LogViewHolder> {

        private List<LogDBEntity> mData;

        LogAdapter(List<LogDBEntity> data) {
            this.mData = data;
        }

        @Override
        public LogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = getLayoutInflater().inflate(R.layout.other_layout_item_log, parent, false);
            return new LogViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(LogViewHolder holder, int position) {
            ((TextView) holder.itemView).setText(mData.get(position).getMsg());
        }

        @Override
        public int getItemCount() {
            return mData == null ? 0 : mData.size();
        }

        public void notifyData(List<LogDBEntity> data) {
            mData = data;
            notifyDataSetChanged();
        }

        public void notifyAddData(List<LogDBEntity> data) {
            if (mData == null) {
                mData = data;
            } else {
                mData.addAll(data);
            }
            notifyDataSetChanged();
        }
    }

    class LogViewHolder extends RecyclerView.ViewHolder {

        LogViewHolder(View itemView) {
            super(itemView);
        }
    }
}
