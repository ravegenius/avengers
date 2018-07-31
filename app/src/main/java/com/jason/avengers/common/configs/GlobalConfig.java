package com.jason.avengers.common.configs;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.jason.avengers.R;

/**
 * Created by jason on 2018/4/23.
 */

public class GlobalConfig {

    public static final RequestOptions AvatarOptions = new RequestOptions()
            .placeholder(R.mipmap.ic_launcher)
            .error(R.mipmap.ic_launcher)
            .transform(new RoundedCorners(15));
}
