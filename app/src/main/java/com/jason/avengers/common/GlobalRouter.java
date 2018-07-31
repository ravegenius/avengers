package com.jason.avengers.common;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.jakewharton.rxbinding2.view.RxView;
import com.jason.avengers.skill.activities.SkillTabActivity;
import com.jason.avengers.skill.beans.SkillInfoBean;
import com.jason.avengers.user.beans.UserBean;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

/**
 * Created by jason on 2018/4/23.
 */

public class GlobalRouter {

    public static void startSystemCamera(final Activity activity) {
        RxPermissions rxPermissions = new RxPermissions(activity);
        rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if (granted) {
                            // 所有权限请求被同意
                            CameraUtils.startSystem(activity);
                        } else {
                            // 至少有一个权限没同意
                            RxPermissions rxPermissions = new RxPermissions(activity);
                            if (!rxPermissions.isGranted(Manifest.permission.CAMERA)) {
                                Toast.makeText(activity, "没有获得相机的权限", Toast.LENGTH_SHORT).show();
                            } else if (!rxPermissions.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                Toast.makeText(activity, "没有获得写SD卡的权限", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(activity, "没有获得权限", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    public static void startCall(final Fragment fragment, final View view, UserBean userBean) {
        RxPermissions rxPermissions = new RxPermissions(fragment.getActivity());
        final String phone = userBean.getPhone();
        RxView.clicks(view)
                .compose(rxPermissions.ensure(Manifest.permission.CALL_PHONE))
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean granted) throws Exception {
                        if (granted) {
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                            startByFragment(fragment, intent);
                        } else {
                            Toast.makeText(fragment.getContext(), "没有获得打电话的权限", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public static void startEmail(Fragment fragment, UserBean userBean) {
        final String email = userBean.getEmail();
        Uri uri = Uri.parse("mailto:" + email);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra(Intent.EXTRA_CC, new String[]{email}); // 抄送人
        intent.putExtra(Intent.EXTRA_SUBJECT, "这是邮件的主题部分"); // 主题
        intent.putExtra(Intent.EXTRA_TEXT, "这是邮件的正文部分"); // 正文
        startByFragment(fragment, Intent.createChooser(intent, "请选择邮件类应用"));
    }

    public static synchronized void startSkillTab(Activity activity, SkillInfoBean skillInfoBean) {
        Intent intent = new Intent(activity, SkillTabActivity.class);
        intent.putExtra(SkillTabActivity.PARAM_SKILL, skillInfoBean);
        startByActivity(activity, intent);
    }

    public static synchronized void startByActivity(Activity activity, Intent intent) {
        activity.startActivity(intent);
    }

    public static synchronized void startByFragment(Fragment fragment, Intent intent) {
        fragment.startActivity(intent);
    }
}
