package com.jason.avengers.other.activities;

import android.os.Bundle;
import android.view.View;

import com.jason.avengers.R;
import com.jason.avengers.base.BaseActivity;
import com.jason.avengers.database.TestDB;

/**
 * Created by jason on 2018/7/25.
 */

public class ObjectBoxActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_objectbox);
        initView();
    }

    private void initView() {
        findViewById(R.id.objectbox_insert).setOnClickListener(this);
        findViewById(R.id.objectbox_delete).setOnClickListener(this);
        findViewById(R.id.objectbox_delete_all).setOnClickListener(this);
        findViewById(R.id.objectbox_update).setOnClickListener(this);
        findViewById(R.id.objectbox_select).setOnClickListener(this);
        findViewById(R.id.objectbox_testAddToOne).setOnClickListener(this);
        findViewById(R.id.objectbox_testGetToOne).setOnClickListener(this);
        findViewById(R.id.objectbox_testDelToOne).setOnClickListener(this);
        findViewById(R.id.objectbox_testAddOneToMany).setOnClickListener(this);
        findViewById(R.id.objectbox_testGetOneToMany).setOnClickListener(this);
        findViewById(R.id.objectbox_testDelOneToMany).setOnClickListener(this);
        findViewById(R.id.objectbox_testAddManyToMany).setOnClickListener(this);
        findViewById(R.id.objectbox_testGetManyToMany).setOnClickListener(this);
        findViewById(R.id.objectbox_testDelManyToMany).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.objectbox_insert:
                TestDB.testUserInsert();
                break;
            case R.id.objectbox_delete:
                TestDB.testUserDelete();
                break;
            case R.id.objectbox_delete_all:
                TestDB.testUserDeleteAll();
                break;
            case R.id.objectbox_update:
                TestDB.testUserUpdate();
                break;
            case R.id.objectbox_select:
                TestDB.testUserSelect();
                break;
            case R.id.objectbox_testAddToOne:
                TestDB.testAddToOne();
                break;
            case R.id.objectbox_testGetToOne:
                TestDB.testGetToOne();
                break;
            case R.id.objectbox_testDelToOne:
                TestDB.testDelToOne();
                break;
            case R.id.objectbox_testAddOneToMany:
                TestDB.testAddOneToMany();
                break;
            case R.id.objectbox_testGetOneToMany:
                TestDB.testGetOneToMany();
                break;
            case R.id.objectbox_testDelOneToMany:
                TestDB.testDelOneToMany();
                break;
            case R.id.objectbox_testAddManyToMany:
                TestDB.testAddManyToMany();
                break;
            case R.id.objectbox_testGetManyToMany:
                TestDB.testGetManyToMany();
                break;
            case R.id.objectbox_testDelManyToMany:
                TestDB.testDelManyToMany();
                break;
        }
    }
}
