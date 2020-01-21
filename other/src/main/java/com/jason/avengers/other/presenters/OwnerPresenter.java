package com.jason.avengers.other.presenters;

import android.text.TextUtils;

import com.jason.avengers.common.base.BasePresenter;
import com.jason.avengers.common.database.ObjectBoxBuilder;
import com.jason.avengers.common.database.entity.other.calendar.CalendarOwnerDBEntity;
import com.jason.avengers.common.database.entity.other.calendar.CalendarOwnerDBEntity_;
import com.jason.avengers.other.beans.OwnerBean;
import com.jason.avengers.other.common.CalendarCommon;
import com.jason.avengers.other.views.OwnerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.objectbox.Box;

/**
 * @author Jason
 */
public class OwnerPresenter extends BasePresenter<OwnerView> {

    private OwnerView mView;
    private Box<CalendarOwnerDBEntity> mOwnerBox = ObjectBoxBuilder.INSTANCE.getBoxStore().boxFor(CalendarOwnerDBEntity.class);

    @Override
    protected void attach(OwnerView view) {
        mView = view;
    }

    @Override
    protected void detach() {
        mView = null;

        if (mOwnerBox != null) {
            mOwnerBox.closeThreadResources();
        }
    }

    public void initData() {
        mOwnerBox.removeAll();
        long ownerId = System.currentTimeMillis();
        int index = 0;
        List<CalendarOwnerDBEntity> entities = new ArrayList<>();
        CalendarOwnerDBEntity entity;
        for (Map.Entry<String, List<String>> entry : CalendarCommon.OwnersMap.entrySet()) {
            String location = entry.getKey();
            for (String owner : entry.getValue()) {
                entity = new CalendarOwnerDBEntity();
                entity.setOwnerId(ownerId++);
                entity.setOwner(owner);
                entity.setLocation(location);
                entity.setColor(CalendarCommon.Colors.get((index++) % CalendarCommon.Colors.size()));
                entities.add(entity);
            }
        }
        mOwnerBox.put(entities);
    }

    public void queryData() {
        List<CalendarOwnerDBEntity> entities = mOwnerBox.query().orderDesc(CalendarOwnerDBEntity_.id).build().find();
        List<OwnerBean> ownerBeans = new ArrayList<>();
        if (!entities.isEmpty()) {
            OwnerBean ownerBean;
            for (CalendarOwnerDBEntity entity : entities) {
                ownerBean = new OwnerBean();
                ownerBean.setId(entity.getId());
                ownerBean.setOwnerId(entity.getOwnerId());
                ownerBean.setName(entity.getOwner());
                ownerBean.setLocation(entity.getLocation());
                ownerBean.setColor(entity.getColor());
                ownerBeans.add(ownerBean);
            }
        }
        mView.notifyView(ownerBeans);
    }

    public void addData(int adapterPosition) {
        int index = (int) (System.currentTimeMillis() % CalendarCommon.Colors.size());
        OwnerBean ownerBean = new OwnerBean();
        ownerBean.setColor(CalendarCommon.Colors.get(index));
        mView.notifyInserted(adapterPosition, ownerBean);
    }

    public void saveData(OwnerBean ownerBean, int adapterPosition) {
        if (ownerBean.getOwnerId() == 0) {
            ownerBean.setOwnerId(System.currentTimeMillis());
        }
        CalendarOwnerDBEntity entity = new CalendarOwnerDBEntity();
        entity.setId(ownerBean.getId());
        entity.setOwnerId(ownerBean.getOwnerId());
        entity.setOwner(ownerBean.getName());
        entity.setLocation(ownerBean.getLocation());
        entity.setColor(ownerBean.getColor());
        mOwnerBox.put(entity);
        mView.notifyItemChanged(adapterPosition);
    }

    public void removeData(OwnerBean ownerBean, int adapterPosition) {
        if (ownerBean.getId() > 0) {
            mOwnerBox.remove(ownerBean.getId());
        }
        mView.notifyRemove(adapterPosition);
    }

    public void saveData(long id, String name, String location) {
        CalendarOwnerDBEntity entity = mOwnerBox.get(id);
        if (entity == null) {
            return;
        }
        if (TextUtils.equals(entity.getOwner(), name)
                && TextUtils.equals(entity.getLocation(), location)) {
            return;
        }
        entity.setOwner(name);
        entity.setLocation(location);
        mOwnerBox.put(entity);
    }

    public void removeData(long id) {
        mOwnerBox.remove(id);
    }

    public String getOwnerName(long id) {
        CalendarOwnerDBEntity entity = mOwnerBox.get(id);
        if (entity == null) {
            return null;
        }
        return entity.getOwner();
    }

    public CalendarOwnerDBEntity queryData(long id) {
        return mOwnerBox.get(id);
    }
}
