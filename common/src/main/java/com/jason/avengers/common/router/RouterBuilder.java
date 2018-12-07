package com.jason.avengers.common.router;

import android.Manifest;
import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.launcher.ARouter;
import com.jakewharton.rxbinding2.view.RxView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

/**
 * Created by jason on 2018/8/20.
 */

public enum RouterBuilder {

    INSTANCE;

    public void init(Application application, Boolean debug) {
        if (debug) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(application);
    }

    public Postcard build(String path) {
        return ARouter.getInstance().build(path);
    }

    public void startCall(final Fragment fragment, final View view, final String phone) {
        RxPermissions rxPermissions = new RxPermissions(fragment.getActivity());
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

    public void startEmail(Fragment fragment, final String email) {
        Uri uri = Uri.parse("mailto:" + email);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra(Intent.EXTRA_CC, new String[]{email}); // 抄送人
        intent.putExtra(Intent.EXTRA_SUBJECT, "这是邮件的主题部分"); // 主题
        intent.putExtra(Intent.EXTRA_TEXT, "这是邮件的正文部分"); // 正文
        startByFragment(fragment, Intent.createChooser(intent, "请选择邮件类应用"));
    }

    public void startByFragment(Fragment fragment, Intent intent) {
        fragment.startActivity(intent);
    }
}
