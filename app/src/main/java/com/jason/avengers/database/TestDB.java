package com.jason.avengers.database;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.jason.avengers.BuildConfig;
import com.jason.avengers.base.BaseApplication;
import com.jason.avengers.database.entity.ResumeDBEntity;
import com.jason.avengers.database.entity.UserDBEntity;
import com.jason.avengers.database.entity.UserDBEntity_;
import com.jason.avengers.database.simple.Customer;
import com.jason.avengers.database.simple.Order;
import com.jason.avengers.database.simple.Student;
import com.jason.avengers.database.simple.Teacher;
import com.jason.avengers.resume.beans.ResumeBean;
import com.jason.avengers.test.TestUtils;
import com.jason.avengers.user.beans.UserBean;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.query.LazyList;
import io.objectbox.query.Query;
import io.objectbox.reactive.DataObserver;
import io.objectbox.reactive.DataSubscription;
import io.objectbox.reactive.DataSubscriptionList;
import io.objectbox.reactive.ErrorObserver;


/**
 * Created by jason on 2018/7/24.
 */

public class TestDB {

    public static final String TAG = "TestDB";

    public static void testUserInsert() {
        UserBean userBean = TestUtils.initUserTestData();
        UserDBEntity userDBEntity = new UserDBEntity();
        userDBEntity.setUserId(userBean.getUserId());
        userDBEntity.setUsername(userBean.getUsername());
        userDBEntity.setSex(userBean.getSex());
        userDBEntity.setAge(userBean.getAge());
        userDBEntity.setWorkAge(userBean.getWorkAge());
        userDBEntity.setAvatar(userBean.getAvatar());
        userDBEntity.setEmail(userBean.getEmail());
        userDBEntity.setPhone(userBean.getPhone());
        userDBEntity.setMark(userBean.getMark());
        ObjectBoxBuilder.getInstance().getBoxStore().boxFor(UserDBEntity.class).put(userDBEntity);
    }

    public static void testUserUpdate() {
        UserBean userBean = TestUtils.initUserTestData();
        UserDBEntity userDBEntity = new UserDBEntity();
        userDBEntity.setId(1L);
        userDBEntity.setUserId(userBean.getUserId());
        userDBEntity.setUsername("Jackson");
        userDBEntity.setSex(userBean.getSex());
        userDBEntity.setAge(userBean.getAge());
        userDBEntity.setWorkAge(userBean.getWorkAge());
        userDBEntity.setAvatar(userBean.getAvatar());
        userDBEntity.setEmail(userBean.getEmail());
        userDBEntity.setPhone(userBean.getPhone());
        userDBEntity.setMark(userBean.getMark());
        ObjectBoxBuilder.getInstance().getBoxStore().boxFor(UserDBEntity.class).put(userDBEntity);
    }

    public static void testUserDelete() {
        ObjectBoxBuilder.getInstance().getBoxStore().boxFor(UserDBEntity.class).remove(1);
    }

    public static void testUserDeleteAll() {
        ObjectBoxBuilder.getInstance().getBoxStore().boxFor(UserDBEntity.class).removeAll();
    }

    public static void testUserSelect() {
        Box<UserDBEntity> box = ObjectBoxBuilder.getInstance().getBoxStore().boxFor(UserDBEntity.class);
        Query<UserDBEntity> userDBEntityQuery = box.query().equal(UserDBEntity_.username, "Jason").build();

        testUserSelectFind(userDBEntityQuery);
//        testUserSelectFindFirst(userDBEntityQuery);
//        testUserSelectFindUnique(userDBEntityQuery);
//
//        testUserSelectFindSetParameter(userDBEntityQuery, "Jason");
//        testUserSelectFindSetParameter(userDBEntityQuery, "Jackson");
//
//        testUserSelectFindWithOffsetLimit(userDBEntityQuery, 10, 5);
//
//        testUserSelectFindLazy(userDBEntityQuery);
//        testUserSelectFindLazyCached(userDBEntityQuery);
//
//        testUserSelectPropertyQuery(userDBEntityQuery, "min");
//        testUserSelectPropertyQuery(userDBEntityQuery, "max");
//        testUserSelectPropertyQuery(userDBEntityQuery, "sum");
//        testUserSelectPropertyQuery(userDBEntityQuery, "avg");
//
//        testUserSelectRemove(userDBEntityQuery);
    }

    private static void testUserSelectFind(Query<UserDBEntity> userDBEntityQuery) {
        // return all entities matching the query
        List<UserDBEntity> userDBEntities = userDBEntityQuery.find();
        for (UserDBEntity userDBEntity : userDBEntities) {
            Log.d(TAG, "Func.testUserSelectFind userDBEntity.id==>>" + userDBEntity.getId());
        }
    }

    private static void testUserSelectFindFirst(Query<UserDBEntity> userDBEntityQuery) {
        // return only the first result or null if none
        UserDBEntity userDBEntity = userDBEntityQuery.findFirst();
        if (userDBEntity == null) return;
        Log.d(TAG, "Func.testUserSelectFindFirst userDBEntity.id==>>" + userDBEntity.getId());
    }

    private static void testUserSelectFindUnique(Query<UserDBEntity> userDBEntityQuery) {
        // return the only result or null if none, throw if more than one result
        UserDBEntity userDBEntity = userDBEntityQuery.findUnique();
        if (userDBEntity == null) return;
        Log.d(TAG, "Func.testUserSelectFindUnique userDBEntity.id==>>" + userDBEntity.getId());
    }

    private static void testUserSelectFindSetParameter(Query<UserDBEntity> userDBEntityQuery, String username) {
        // query parameter reset
        List<UserDBEntity> userDBEntities = userDBEntityQuery.setParameter(UserDBEntity_.username, username).find();
        for (UserDBEntity userDBEntity : userDBEntities) {
            Log.d(TAG, "Func.testUserSelectFind userDBEntity.id==>>" + userDBEntity.getId());
        }
    }

    private static void testUserSelectFindWithOffsetLimit(Query<UserDBEntity> userDBEntityQuery, int offset, int limit) {
        // offset and limit
        List<UserDBEntity> userDBEntities = userDBEntityQuery.find(offset, limit);
        for (UserDBEntity userDBEntity : userDBEntities) {
            Log.d(TAG, "Func.testUserSelectFindWithOffsetLimit userDBEntity.id==>>" + userDBEntity.getId());
        }
    }

    private static void testUserSelectFindLazy(Query<UserDBEntity> userDBEntityQuery) {
        // lazy loading results
        LazyList<UserDBEntity> lazyList = userDBEntityQuery.findLazy();
        lazyList.get(0);
        lazyList.subList(0, 1);
    }

    private static void testUserSelectFindLazyCached(Query<UserDBEntity> userDBEntityQuery) {
        // lazy loading results with cache
        LazyList<UserDBEntity> lazyListCached = userDBEntityQuery.findLazyCached();
        lazyListCached.get(0);
        lazyListCached.subList(0, 1);
    }

    private static void testUserSelectPropertyQuery(Query<UserDBEntity> userDBEntityQuery, String action) {
        if (TextUtils.equals("min", action)) {
            long min = userDBEntityQuery.property(UserDBEntity_.sex).min();
            Log.d(TAG, "Func.testUserSelectPropertyQuery min(sex)==>>" + min);
        } else if (TextUtils.equals("max", action)) {
            long max = userDBEntityQuery.property(UserDBEntity_.sex).max();
            Log.d(TAG, "Func.testUserSelectPropertyQuery max(sex)==>>" + max);
        } else if (TextUtils.equals("sum", action)) {
            long sum = userDBEntityQuery.property(UserDBEntity_.sex).sum();
            Log.d(TAG, "Func.testUserSelectPropertyQuery sum(sex)==>>" + sum);
        } else if (TextUtils.equals("avg", action)) {
            double avg = userDBEntityQuery.property(UserDBEntity_.sex).avg();
            Log.d(TAG, "Func.testUserSelectPropertyQuery avg(sex)==>>" + avg);
        }
    }

    private static void testUserSelectRemove(Query<UserDBEntity> userDBEntityQuery) {
        // query and remove
        userDBEntityQuery.remove();
    }

    public static DataSubscription testWithObserver0() {
        DataSubscription dataSubscription = ObjectBoxBuilder.getInstance().getBoxStore()
                .subscribe()
                // ObjectBox - rx
//                .transform(new DataTransformer<Class, Class>() {
//                    @Override
//                    public Class transform(Class source) throws Exception {
//                        return source;
//                    }
//                })
//                .weak()
//                .single()
//                .onlyChanges()
                .onError(new ErrorObserver() {
                    @Override
                    public void onError(Throwable th) {
                        Log.e(TAG, "Func.testWithObserver0 " + th.getMessage());
                    }
                })
                .observer(new DataObserver<Class>() {
                    @Override
                    public void onData(Class data) {
                        Log.i(TAG, "Func.testWithObserver0 " + data);
                    }
                });

        return dataSubscription;
        // dataSubscription.cancel();
    }

    public static DataSubscription testWithObserver1() {
        DataSubscription dataSubscription = ObjectBoxBuilder.getInstance().getBoxStore()
                .subscribe(UserDBEntity.class)
                .onError(new ErrorObserver() {
                    @Override
                    public void onError(Throwable th) {
                        Log.e(TAG, "Func.testWithObserver1 " + th.getMessage());
                    }
                })
                .observer(new DataObserver<Class<UserDBEntity>>() {
                    @Override
                    public void onData(Class<UserDBEntity> data) {
                        Log.i(TAG, "Func.testWithObserver1 " + data);
                    }
                });

        return dataSubscription;
        // dataSubscription.cancel();
    }

    public static DataSubscription testWithObserver2() {
        DataSubscriptionList subscriptions = new DataSubscriptionList();
        DataSubscription dataSubscription = ObjectBoxBuilder.getInstance().getBoxStore()
                .boxFor(UserDBEntity.class)
                .query().build()
                .subscribe(subscriptions)
                .onError(new ErrorObserver() {
                    @Override
                    public void onError(Throwable th) {
                        Log.e(TAG, "Func.testWithObserver2 " + th.getMessage());
                    }
                })
                .observer(new DataObserver<List<UserDBEntity>>() {
                    @Override
                    public void onData(List<UserDBEntity> data) {
                        Log.i(TAG, "Func.testWithObserver2 " + new Gson().toJson(data));
                    }
                });

        return dataSubscription;
        // dataSubscription.cancel();
    }

    public static void testUserWithResumeInsert() {
        UserDBEntity userDBEntity = new UserDBEntity();

        UserBean userBean = TestUtils.initUserTestData();
        userDBEntity.setUserId(userBean.getUserId());
        userDBEntity.setUsername(userBean.getUsername());
        userDBEntity.setSex(userBean.getSex());
        userDBEntity.setAge(userBean.getAge());
        userDBEntity.setWorkAge(userBean.getWorkAge());
        userDBEntity.setAvatar(userBean.getAvatar());
        userDBEntity.setEmail(userBean.getEmail());
        userDBEntity.setPhone(userBean.getPhone());
        userDBEntity.setMark(userBean.getMark());

        List<ResumeBean> resumeBeans = TestUtils.initResumeInfosTestData();
        List<ResumeDBEntity> resumeDBEntities = new ArrayList<>();
        ResumeDBEntity resumeDBEntity;
        for (ResumeBean resumeBean : resumeBeans) {
            resumeDBEntity = new ResumeDBEntity();
            resumeDBEntity.setCompanyName(resumeBean.getCompanyName());
            resumeDBEntity.setStartDate(resumeBean.getStartDate());
            resumeDBEntity.setEndDate(resumeBean.getEndDate());
            resumeDBEntity.setPosition(resumeBean.getPosition());
            resumeDBEntity.setJobContent(resumeBean.getJobContent());
            resumeDBEntity.setJobDescription(resumeBean.getJobDescription());
            resumeDBEntity.setSerial(resumeBean.getSerial());
            resumeDBEntities.add(resumeDBEntity);
        }
        if (resumeDBEntities.size() > 0) {
            userDBEntity.getResumes().addAll(resumeDBEntities);
        }
        ObjectBoxBuilder.getInstance().getBoxStore().boxFor(UserDBEntity.class).put(userDBEntity);
    }

    public static void testAddToOne() {
        BoxStore boxStore = ObjectBoxBuilder.getInstance().getBoxStore();
        // add
        Customer customer = new Customer();
        Order order = new Order();
        order.customer.setTarget(customer);
        // puts order and customer
        long orderId = boxStore.boxFor(Order.class).put(order);
    }

    public static void testGetToOne() {
        BoxStore boxStore = ObjectBoxBuilder.getInstance().getBoxStore();
        // get
        long orderId = 1L;
        Order order = boxStore.boxFor(Order.class).get(orderId);
        Customer customer = order.customer.getTarget();
        Log.d(TAG, "Func.testGetToOne customer.id==>>" + customer.id);
    }

    public static void testDelToOne() {
        BoxStore boxStore = ObjectBoxBuilder.getInstance().getBoxStore();
        long orderId = 1L;
        Order order = boxStore.boxFor(Order.class).get(orderId);
        long customerId = order.customer.getTarget().id;

        order.customer.setTarget(null);
        boxStore.boxFor(Order.class).put(order);

        // delete customer
        // boxStore.boxFor(Customer.class).remove(customerId);
    }

    public static void testAddOneToMany() {
        BoxStore boxStore = ObjectBoxBuilder.getInstance().getBoxStore();

        Customer customer = new Customer();
        customer.orders.add(new Order());
        customer.orders.add(new Order());
        // puts customer and orders
        long customerId = boxStore.boxFor(Customer.class).put(customer);
    }

    public static void testGetOneToMany() {
        BoxStore boxStore = ObjectBoxBuilder.getInstance().getBoxStore();
        long customerId = 1L;
        Customer customer = boxStore.boxFor(Customer.class).get(customerId);
        for (Order order : customer.orders) {
            Log.d(TAG, "Func.testGetOneToMany order.id==>>" + order.id);
        }
    }

    public static void testDelOneToMany() {
        BoxStore boxStore = ObjectBoxBuilder.getInstance().getBoxStore();
        long customerId = 1L;
        Customer customer = boxStore.boxFor(Customer.class).get(customerId);

        Order order = customer.orders.remove(0);
        boxStore.boxFor(Customer.class).put(customer);
    }

    public static void testAddManyToMany() {
        BoxStore boxStore = ObjectBoxBuilder.getInstance().getBoxStore();

        Teacher teacher1 = new Teacher();
        Teacher teacher2 = new Teacher();

        Student student1 = new Student();
        student1.teachers.add(teacher1);
        student1.teachers.add(teacher2);

        Student student2 = new Student();
        student2.teachers.add(teacher2);

        // puts students and teachers
        boxStore.boxFor(Student.class).put(student1, student2);
    }

    public static void testGetManyToMany() {
        BoxStore boxStore = ObjectBoxBuilder.getInstance().getBoxStore();
        long studentId = 1L;
        Student student = boxStore.boxFor(Student.class).get(studentId);
        for (Teacher teacher : student.teachers) {
            Log.d(TAG, "Func.testGetManyToMany teacher.id==>>" + teacher.id);
        }
    }

    public static void testDelManyToMany() {
        BoxStore boxStore = ObjectBoxBuilder.getInstance().getBoxStore();
        long studentId = 1L;
        Student student = boxStore.boxFor(Student.class).get(studentId);
        student.teachers.remove(0);
        // boxStore.boxFor(Student.class).put(student);
        // more efficient than using put:
        student.teachers.applyChangesToDb();
    }

    public static void testDropAllData() {
        BoxStore boxStore = ObjectBoxBuilder.getInstance().getBoxStore();
        for (Class clazz : boxStore.getAllEntityClasses()) {
            boxStore.boxFor(clazz).removeAll();
        }
    }

    public static void testDelAllFiles() {
        BoxStore boxStore = ObjectBoxBuilder.getInstance().getBoxStore();
        boxStore.close();
        boxStore.deleteAllFiles();
        // 重新创建
        ObjectBoxBuilder.getInstance().build(BaseApplication.getInstance(), BuildConfig.DEBUG);
    }
}
