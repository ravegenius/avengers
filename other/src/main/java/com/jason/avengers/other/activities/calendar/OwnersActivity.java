package com.jason.avengers.other.activities.calendar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jason.avengers.common.base.BaseActivity;
import com.jason.avengers.common.router.RouterBuilder;
import com.jason.avengers.common.router.RouterPath;
import com.jason.avengers.other.R;
import com.jason.avengers.other.adapters.OwnersAdapter;
import com.jason.avengers.other.beans.OwnerBean;
import com.jason.avengers.other.holders.OwnerHolder;
import com.jason.avengers.other.listeners.OwnerClickListener;
import com.jason.avengers.other.presenters.OwnerPresenter;
import com.jason.avengers.other.views.OwnerView;
import com.jason.core.utils.SoftKeyboardUtils;
import com.jason.core.utils.ToastUtils;

import java.util.List;

/**
 * @author Jason
 */
@Route(path = RouterPath.OTHER_CALENDAR_OWNER)
public class OwnersActivity extends BaseActivity<OwnerPresenter, OwnerView> implements OwnerView {

    private RecyclerView mOwnersView;
    private OwnersAdapter mOwnersAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_activity_calendar_owners);
        initView();
    }

    private void initView() {
        mOwnersView = findViewById(R.id.owners_view);

        mOwnersView.setLayoutManager(new LinearLayoutManager(this));
        mOwnersView.setAdapter(mOwnersAdapter = new OwnersAdapter(getLayoutInflater(), new OwnerClickListener() {
            @Override
            public void onColorClickListener(OwnerHolder holder, View view) {
                // TODO 选择颜色CustomPickerView
            }

            @Override
            public void onDetailClickListener(OwnerHolder holder, View view) {
                OwnerBean ownerBean = holder.ownerBean;
                if (ownerBean == null || ownerBean.getId() <= 0) {
                    return;
                }
                RouterBuilder.INSTANCE.build(RouterPath.OTHER_CALENDAR_OWNER_DETAIL)
                        .withLong(OwnerDetailActivity.PARAMS_ID, ownerBean.getId())
                        .navigation(OwnersActivity.this);
            }

            @Override
            public void onSaveClickListener(OwnerHolder holder, View view) {
                OwnerBean ownerBean = holder.ownerBean;
                if (ownerBean == null) {
                    return;
                }

                if (TextUtils.isEmpty(ownerBean.getName())) {
                    ToastUtils.showShortToast("请输入名称！");
                    return;
                }
                if (TextUtils.isEmpty(ownerBean.getLocation())) {
                    ToastUtils.showShortToast("请输入地址！");
                    return;
                }

                getPresenter().saveData(ownerBean, holder.getAdapterPosition());
            }

            @Override
            public void onDeleteClickListener(final OwnerHolder holder, View view) {
                final OwnerBean ownerBean = holder.ownerBean;
                if (ownerBean == null) {
                    return;
                }
                new AlertDialog.Builder(OwnersActivity.this)
                        .setTitle(R.string.other_dialog_title_alter)
                        .setMessage(getString(R.string.other_dialog_msg, getString(R.string.app_delete)))
                        .setNegativeButton(R.string.other_dialog_negative_btn_label, null)
                        .setPositiveButton(R.string.other_dialog_positive_btn_label, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getPresenter().removeData(ownerBean, holder.getAdapterPosition());
                            }
                        })
                        .create().show();
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
        if (id == R.id.action_add) {
            getPresenter().addData(0);
            return true;
        } else if (id == R.id.action_init) {
            new AlertDialog.Builder(OwnersActivity.this)
                    .setTitle(R.string.other_dialog_title_alter)
                    .setMessage(getString(R.string.other_dialog_msg, item.getTitle()))
                    .setNegativeButton(R.string.other_dialog_negative_btn_label, null)
                    .setPositiveButton(R.string.other_dialog_positive_btn_label, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            initData();
                            queryData();
                        }
                    })
                    .create().show();
            return true;
        } else if (id == R.id.action_clear) {
            new AlertDialog.Builder(OwnersActivity.this)
                    .setTitle(R.string.other_dialog_title_alter)
                    .setMessage(getString(R.string.other_dialog_msg, item.getTitle()))
                    .setNegativeButton(R.string.other_dialog_negative_btn_label, null)
                    .setPositiveButton(R.string.other_dialog_positive_btn_label, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getPresenter().clear();
                            queryData();
                        }
                    })
                    .create().show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initData() {
        getPresenter().initData();
    }

    private void queryData() {
        getPresenter().queryData();
    }

    @Override
    protected OwnerPresenter initPresenter() {
        return new OwnerPresenter();
    }

    @Override
    protected OwnerView initAttachView() {
        return this;
    }

    @Override
    public void notifyView(List<OwnerBean> ownerBeans) {
        mOwnersAdapter.notifyData(ownerBeans);
        SoftKeyboardUtils.hide(OwnersActivity.this);
    }

    @Override
    public void notifyInserted(int position, OwnerBean ownerBean) {
        mOwnersAdapter.notifyInserted(position, ownerBean);
        mOwnersView.scrollToPosition(position);
        SoftKeyboardUtils.hide(OwnersActivity.this);
    }

    @Override
    public void notifyItemChanged(int position) {
        mOwnersAdapter.notifyItemChanged(position);
        SoftKeyboardUtils.hide(OwnersActivity.this);
    }

    @Override
    public void notifyRemove(int position) {
        mOwnersAdapter.notifyRemove(position);
        SoftKeyboardUtils.hide(OwnersActivity.this);
    }
}
