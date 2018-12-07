package com.jason.avengers.common.test;

/**
 * Created by jason on 2018/3/30.
 */

public class TestUtils {

    protected static final String userJson = "{\"id\":1, \"name\":\"Jason\", \"sex\":1, \"age\":33, \"workAge\":10, \"avatar\":\"https://tvax4.sinaimg.cn/crop.0.0.1080.1080.180/64b3acc7ly8fqmet0ddb1j20u00u0jw2.jpg\", \"email\":\"ravegenius1985@126.com\", \"phone\":\"13401073452\"}";

    protected static final String educationJson = "[" +
            "{\"school\":\"东北大学\", \"startDate\":\"2004/09\", \"endDate\":\"2008/07\", \"degree\":\"学士\", \"serial\":1}" +
            "]";

    protected static final String resumeJson = "[" +
            "{\"company\":\"网易\", \"startDate\":\"2016/11\", \"endDate\":\"now\", \"position\":\"Android Senior Engineer\", \"jobContent\":\"网易新闻、羽毛、薄荷\"" +
            ", \"jobDescription\":\"负责新闻产品的架构开发，性能优化\n负责新闻产品的新技术预研和沉淀，跟踪国内外的最新技术动向，及时为公司引进新技术\n通过研究，分析与产品结合的可行性，提前解决技术使用的疑难点\n对现有产品进行深入分析，找出优化方向\"" +
            ", \"serial\":1}" +
            "," +
            "{\"company\":\"问吧科技\", \"startDate\":\"2016/03\", \"endDate\":\"2016/11\", \"position\":\"Mobile Platform Manager\", \"jobContent\":\"学霸君、不二课堂、青蓝头条、家长汇\"" +
            ", \"jobDescription\":\"日常工作量评估，管理分配需求，统筹规划产品方向，顺利完成移动端Android，iOS产品开发需求\n产品架构设计，编写产品功能代码，并做Team代码审核\n构建Base Common Library 能够快速开发应用，节省开发成本\n基于Bilibili开源Src开发直播组件\n协调配合其他部门共同完成各项产品需求\n制定团队、个人KPI，保证产品进度\n与团队共同学习最新技术并快速运用在工作中\"" +
            ", \"serial\":2}" +
            "," +
            "{\"company\":\"蜜蜂汇金\", \"startDate\":\"2015/04\", \"endDate\":\"2016/03\", \"position\":\"Mobile Platform Manager\", \"jobContent\":\"蜜银、贷贷熊、分期商城、鹰眼\"" +
            ", \"jobDescription\":\"日常工作量评估，管理分配需求，统筹规划产品方向，顺利完成移动端Android，iOS产品开发需求\n搭建并使用Git Gerrit Jenkins，实现持续集成迭代开发\n产品架构设计，编写产品功能代码，并做Team代码审核\n构建Base Common Library 能够快速开发应用，节省开发成本\n完成鹰眼的录像和拍照同时进行的功能\n学习并推广React Native开发\n协调配合其他部门共同完成各项产品需求\n制定团队、个人KPI，保证产品进度\n与团队共同学习最新技术并快速运用在工作中\"" +
            ", \"serial\":3}" +
            "," +
            "{\"company\":\"锤子科技\", \"startDate\":\"2013/10\", \"endDate\":\"2015/04\", \"position\":\"Team Leader\", \"jobContent\":\"应用商店、录音机、Email、其他\"" +
            ", \"jobDescription\":\"日常工作量评估，管理分配需求，统筹规划产品方向，代码审核\n完成App下载，网络读取，图片加载，通用UI组件和基础工具包模块的开发。完成类似DownloadManager组件用于AppStore下载，断点续传，取消，安装的功能；使用Volley完成网络读取模块，设计cache完成refresh机制；使用DiskLruCache和ImageMemoryCache的机制完成图片加载的二级缓存\n负责录音模块开发。调用底层media库实现录音与播放，使用AIDL与media库通信。完成实时显示音频频谱的UI组件\n负责Email发送邮件，显示邮件内容的模块开发。需要掌握G-mail发送及接受邮件的流程，熟悉Email发送协议。修改了原生Gmail的UI样式，解决了邮件内容的Html文本可自适应在Webview中的折行显示；解决了无法显示Emoji的问题；结合联系人显示邮件收件人发件人\n其他应用的代码审核、bug修改\"" +
            ", \"serial\":4}" +
            "," +
            "{\"company\":\"中搜\", \"startDate\":\"2012/03\", \"endDate\":\"2013/10\", \"position\":\"Java Senior Engineer\", \"jobContent\":\"通用版B2C移动商城（Android）、\nO2O商城（Android）、\n通用O2O商城（Android）、\n通用版B2C商城（Web）、\n个性化B2C商城（Web）\"" +
            ", \"jobDescription\":\"敏捷模式下开发，负责基础框架开发搭建、银联支付组件开发、Scala+Aquery网络异步获取数据框架、图片下载组件开发，开发了首页模块、会员中心、搜索商品模块、设置模块\n敏捷模式下开发，负责高德地图组件、图片二级缓存组件、通用EditView控件开发，开发了分类模块、会员中心、设置模块、商家展示模块\n敏捷模式下开发，负责基础框架开发搭建、退出Manager组件开发，重构代码、分享组件、通用下拉上提组件开发，开发了分类模块、快讯模块、会员中心、设置模块、商家展示模块\n敏捷模式下开发，负责移动终端数据API接口，使用groovy编写测试用例，订单模块，物流配送模块，会用模块开发，使用redis缓存配送范围和商品分类信息\n敏捷模式下开发，以通用B2C商城为基础代码，根据个性化需求，设计、开发功能，开发包括了买卖提分站式平台型商城、中品家博家具商城、婚博商城、眼镜商城\"" +
            ", \"serial\":5}" +
            "," +
            "{\"company\":\"北控软件\", \"startDate\":\"2009/08\", \"endDate\":\"2012/03\", \"position\":\"Java Senior Engineer\", \"jobContent\":\"大兴区移动终端展示系统、\n交换共享数据平台、\nAndroid手持终端系统、\n角色访问控制系统\"" +
            ", \"jobDescription\":\"带领团队需求调研、数据库设计、核心API代码设计编写、文档归纳、测试验收等。主要编写了通过HTML5生成图例组件，修改Sencha-touch组件衍生成公司在iPad展示组件\n带领团队完成两个系统的demo设计，需求调研，数据库设计，合理分配工作量 \n自学Android开发， 短时间完成项目，基于Android2.2平台的手持终端设备开发一款采集信息上报事件的终端平台。\"" +
            ", \"serial\":6}" +
            "]";

    protected static final String skillJson = "[" +
            "{\"kind\":\"Android\", \"level\":\"熟练\", \"skillPoints\":[" +
            "\"Activity\", \"Service\", \"ContentProvider\", \"BroadcastReceiver\", \"Fragment\", \"Intent\", \"Handler\", \"Toast\", \"Menu\", \"ViewGroup & View\", \"ViewStub\", \"RecyclerView\", \"Permission\", " +
            "\"多级缓存\", \"文件存储\", \"SharedPreferences\", \"数据库\", \"LitePal操作数据库\", \"多媒体-视频&相机\", \"网络技术\", \"WebView\", \"多线程\", \"Material Design UI\", \"Log\", \"Nine-Patch\", " +
            "\"第三方组件\", \"源码解析\", \"热更新\", \"逆向工程\", \"其他\"" +
            "]}" +
            "," +
            "{\"kind\":\"Java\", \"level\":\"熟练\", \"skillPoints\":[\"内存管理\", \"GC算法\", \"Java 反射\", \"Java容器类\", \"Java AOP\", \"ASM 框架\", \"AspectJ\", \"Javaassist\", \"Java 动态代理\", \"Java 注解\", \"Java copy\", \"Java 泛型和泛型擦除\", \"代码生成与代码生成框架\", \"Java 8, 9, 10新特性\"]}" +
            "," +
            "{\"kind\":\"其他\", \"level\":\"\", \"skillPoints\":[\"Js 入门\", \"前端与客户端的交互\", \"Node\", \"React Native\", \"微信小程序\", \"shell指令\", \"python 脚本\", \"Groovy 脚本\", \"Kotlin\", \"Go语言\", \"Git命令\"]}" +
            "]";
}
