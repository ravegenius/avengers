package com.jason.avengers.other.calendar.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jason.avengers.common.base.BaseActivity;
import com.jason.avengers.common.database.ObjectBoxBuilder;
import com.jason.avengers.common.database.entity.other.calendar.CalendarOwnerDBEntity;
import com.jason.avengers.common.database.entity.other.calendar.CalendarOwnerDBEntity_;
import com.jason.avengers.common.router.RouterBuilder;
import com.jason.avengers.common.router.RouterPath;
import com.jason.avengers.other.R;
import com.jason.avengers.other.calendar.CalendarCommon;
import com.jason.avengers.other.calendar.CalendarPresenter;
import com.jason.avengers.other.calendar.CalendarView;
import com.jason.avengers.other.calendar.adapters.OwnersAdapter;
import com.jason.avengers.other.calendar.holders.OwnersHolder;
import com.jason.avengers.other.calendar.listeners.OwnersClickListener;
import com.jason.core.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.objectbox.Box;

/**
 * @author Jason
 */
@Route(path = RouterPath.OTHER_CALENDAR_OWNER)
public class OwnersActivity extends BaseActivity<CalendarPresenter, CalendarView> {

    private RecyclerView mOwnersView;
    private OwnersAdapter mOwnersAdapter;

    private Box<CalendarOwnerDBEntity> mOwnerBox = ObjectBoxBuilder.INSTANCE.getBoxStore().boxFor(CalendarOwnerDBEntity.class);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_activity_calendar_owners);

        initView();
    }

    private void initView() {
        mOwnersView = findViewById(R.id.calendar_owners_view);

        mOwnersView.setLayoutManager(new LinearLayoutManager(this));
        mOwnersView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mOwnersView.setAdapter(mOwnersAdapter = new OwnersAdapter(getLayoutInflater(), new OwnersClickListener() {
            @Override
            public void onColorClickListener(OwnersHolder holder, View view) {
                // TODO 选择颜色CustomPickerView
            }

            @Override
            public void onDetailClickListener(OwnersHolder holder, View view) {
                CalendarOwnerDBEntity entity = holder.entity;
                if (entity == null || entity.getId() <= 0) {
                    return;
                }
                RouterBuilder.INSTANCE.build(RouterPath.OTHER_CALENDAR_OWNER_DETAIL)
                        .withLong(OwnerDetailActivity.PARAMS_ID, entity.getId())
                        .navigation(OwnersActivity.this);
            }

            @Override
            public void onSaveClickListener(OwnersHolder holder, View view) {
                CalendarOwnerDBEntity entity = holder.entity;
                if (entity == null) {
                    return;
                }

                if (TextUtils.isEmpty(entity.getOwner())) {
                    ToastUtils.showShortToast("请输入名称！");
                    return;
                }
                if (TextUtils.isEmpty(entity.getLocaltion())) {
                    ToastUtils.showShortToast("请输入地址！");
                    return;
                }

                if (entity.getOwnerId() == 0) {
                    entity.setOwnerId(System.currentTimeMillis());
                }
                mOwnerBox.put(entity);
                mOwnersAdapter.notifyItemChanged(holder.getAdapterPosition());
            }

            @Override
            public void onDeleteClickListener(final OwnersHolder holder, View view) {
                final CalendarOwnerDBEntity entity = holder.entity;
                if (entity == null) {
                    return;
                }
                new AlertDialog.Builder(OwnersActivity.this)
                        .setTitle("提示")
                        .setMessage("确认是否删除")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (entity.getId() > 0) {
                                    mOwnerBox.remove(entity.getId());
                                }
                                mOwnersAdapter.notifyRemove(holder.getAdapterPosition());
                            }
                        }).create().show();
            }
        }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.calendar_owners, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_init) {
            new AlertDialog.Builder(OwnersActivity.this)
                    .setTitle("提示")
                    .setMessage("确认是否初始化")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            initData();
                            queryData();
                        }
                    }).create().show();
            return true;
        } else if (id == R.id.action_add) {
            if (mOwnersAdapter != null) {
                CalendarOwnerDBEntity entity = new CalendarOwnerDBEntity();
                int index = (int) (System.currentTimeMillis() % CalendarCommon.Colors.size());
                entity.setColor(CalendarCommon.Colors.get(index));
                mOwnersAdapter.notifyInserted(0, entity);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initData() {
        mOwnerBox.removeAll();
        long ownerId = System.currentTimeMillis();
        int index = (int) (ownerId % CalendarCommon.Colors.size());
        List<CalendarOwnerDBEntity> entities = new ArrayList<>();
        CalendarOwnerDBEntity entity;
        for (Map.Entry<String, List<String>> entry : CalendarCommon.OwnersMap.entrySet()) {
            String location = entry.getKey();
            for (String owner : entry.getValue()) {
                entity = new CalendarOwnerDBEntity();
                entity.setOwnerId(ownerId++);
                entity.setOwner(owner);
                entity.setLocaltion(location);
                entity.setColor(CalendarCommon.Colors.get((index++) % CalendarCommon.Colors.size()));
                entities.add(entity);
            }
        }
        mOwnerBox.put(entities);
    }

    private void queryData() {
        List<CalendarOwnerDBEntity> entities = mOwnerBox.query().orderDesc(CalendarOwnerDBEntity_.id).build().find();
        mOwnersAdapter.notifyData(entities);
    }

    @Override
    protected CalendarPresenter initPresenter() {
        return null;
    }

    @Override
    protected CalendarView initAttachView() {
        return null;
    }
}
