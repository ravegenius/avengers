package com.jason.avengers.other.views;

import com.jason.avengers.common.base.BaseView;
import com.jason.avengers.other.beans.OwnerBean;

import java.util.List;

/**
 * @author Jason
 */
public interface OwnerView extends BaseView {

    void notifyView(List<OwnerBean> ownerBeans);

    void notifyInserted(int adapterPosition, OwnerBean ownerBean);

    void notifyItemChanged(int position);

    void notifyRemove(int position);
}
