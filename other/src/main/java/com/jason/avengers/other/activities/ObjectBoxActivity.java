package com.jason.avengers.other.activities;

import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.jason.avengers.common.base.BaseNoMVPActivity;
import com.jason.avengers.common.router.RouterPath;
import com.jason.avengers.other.R;
// import com.jason.avengers.common.database.TestDB;

// import io.objectbox.reactive.DataSubscription;

/**
 * Created by jason on 2018/7/25.
 */

@Route(path = RouterPath.OTHER_OBJECTBOX)
public class ObjectBoxActivity extends BaseNoMVPActivity implements View.OnClickListener {

//     private DataSubscription mDataSubscription0, mDataSubscription1, mDataSubscription2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_activity_objectbox);
        initView();
//         mDataSubscription0 = TestDB.testWithObserver0();
//         mDataSubscription1 = TestDB.testWithObserver1();
//         mDataSubscription2 = TestDB.testWithObserver2();
//         TestDB.testJsonToMany();
    }

    private void initView() {
        findViewById(R.id.objectbox_insert).setOnClickListener(this);
        findViewById(R.id.objectbox_delete).setOnClickListener(this);
        findViewById(R.id.objectbox_delete_all).setOnClickListener(this);
        findViewById(R.id.objectbox_update).setOnClickListener(this);
        findViewById(R.id.objectbox_select).setOnClickListener(this);
        findViewById(R.id.objectbox_insert_many).setOnClickListener(this);
        findViewById(R.id.objectbox_testAddToOne).setOnClickListener(this);
        findViewById(R.id.objectbox_testGetToOne).setOnClickListener(this);
        findViewById(R.id.objectbox_testDelToOne).setOnClickListener(this);
        findViewById(R.id.objectbox_testAddOneToMany).setOnClickListener(this);
        findViewById(R.id.objectbox_testGetOneToMany).setOnClickListener(this);
        findViewById(R.id.objectbox_testDelOneToMany).setOnClickListener(this);
        findViewById(R.id.objectbox_testAddManyToMany).setOnClickListener(this);
        findViewById(R.id.objectbox_testGetManyToMany).setOnClickListener(this);
        findViewById(R.id.objectbox_testDelManyToMany).setOnClickListener(this);
        findViewById(R.id.objectbox_testDropAllData).setOnClickListener(this);
        findViewById(R.id.objectbox_testDelAllFiles).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.objectbox_insert:
//                TestDB.testUserInsert();
//                break;
//            case R.id.objectbox_update:
//                TestDB.testUserUpdate();
//                break;
//            case R.id.objectbox_delete:
//                TestDB.testUserDelete();
//                break;
//            case R.id.objectbox_delete_all:
//                TestDB.testUserDeleteAll();
//                break;
//            case R.id.objectbox_select:
//                TestDB.testUserSelect();
//                break;
//            case R.id.objectbox_insert_many:
//                TestDB.testUserWithResumeInsert();
//                break;
//            case R.id.objectbox_testAddToOne:
//                TestDB.testAddToOne();
//                break;
//            case R.id.objectbox_testGetToOne:
//                TestDB.testGetToOne();
//                break;
//            case R.id.objectbox_testDelToOne:
//                TestDB.testDelToOne();
//                break;
//            case R.id.objectbox_testAddOneToMany:
//                TestDB.testAddOneToMany();
//                break;
//            case R.id.objectbox_testGetOneToMany:
//                TestDB.testGetOneToMany();
//                break;
//            case R.id.objectbox_testDelOneToMany:
//                TestDB.testDelOneToMany();
//                break;
//            case R.id.objectbox_testAddManyToMany:
//                TestDB.testAddManyToMany();
//                break;
//            case R.id.objectbox_testGetManyToMany:
//                TestDB.testGetManyToMany();
//                break;
//            case R.id.objectbox_testDelManyToMany:
//                TestDB.testDelManyToMany();
//                break;
//            case R.id.objectbox_testDropAllData:
//                TestDB.testDropAllData();
//                break;
//            case R.id.objectbox_testDelAllFiles:
//                TestDB.testDelAllFiles();
//                break;
//        }
    }

    @Override
    protected void onDestroy() {
//        if (mDataSubscription0 != null) mDataSubscription0.cancel();
//        if (mDataSubscription1 != null) mDataSubscription1.cancel();
//        if (mDataSubscription2 != null) mDataSubscription2.cancel();
        super.onDestroy();
    }
}
