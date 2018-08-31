package com.etsdk.app.huov7.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.etsdk.app.huov7.R;
import com.etsdk.app.huov7.adapter.GroupHomeAdapter;
import com.etsdk.app.huov7.base.ImmerseActivity;
import com.etsdk.app.huov7.http.AppApi;
import com.etsdk.app.huov7.model.AddressList;
import com.etsdk.app.huov7.model.ListRequestBean;
import com.etsdk.app.huov7.model.MessageRequestBean;
import com.etsdk.app.huov7.model.ShowMsg;
import com.etsdk.app.huov7.model.StartupResultBean;
import com.etsdk.app.huov7.model.UserInfoResultBean;
import com.etsdk.app.huov7.ui.fragment.FindFragment;
import com.etsdk.app.huov7.ui.fragment.GameTestNewFragmentNew;
import com.etsdk.app.huov7.ui.fragment.HomeFragment;
import com.etsdk.app.huov7.ui.fragment.MainGameFragment;
import com.etsdk.app.huov7.ui.fragment.MainMineFragmentNew2;
import com.etsdk.app.huov7.update.UpdateVersionDialog;
import com.etsdk.app.huov7.update.UpdateVersionService;
import com.etsdk.app.huov7.util.ImgUtil;
import com.etsdk.app.huov7.util.StringUtils;
import com.game.sdk.domain.BaseRequestBean;
import com.game.sdk.http.HttpNoLoginCallbackDecode;
import com.game.sdk.http.HttpParamsBuild;
import com.game.sdk.log.L;
import com.game.sdk.util.GsonUtil;
import com.jaeger.library.StatusBarUtil;
import com.jude.swipbackhelper.SwipeBackHelper;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpParams;
import com.liang530.log.T;
import com.liang530.rxvolley.HttpJsonCallBackDialog;
import com.liang530.rxvolley.NetRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.liulishuo.filedownloader.model.FileDownloadStatus.error;

/**
 * Created by Administrator on 2018\3\14 0014.
 */

public class MainActivity2 extends ImmerseActivity {
    //    @BindView(R.id.sticky)
//    StickyNavLayout sticky;
    @BindView(R.id.pager_view)
    ViewPager mViewPager;
    @BindView(R.id.group_ll)
    LinearLayout group_ll;
    @BindView(R.id.group_tv)
    TextView group_tv;
    @BindView(R.id.group_iv)
    ImageView group_iv;
    @BindView(R.id.event_ll)
    LinearLayout event_ll;
    @BindView(R.id.event_tv)
    TextView event_tv;
    @BindView(R.id.event_iv)
    ImageView event_iv;
    @BindView(R.id.chat_ll)
    LinearLayout chat_ll;
    @BindView(R.id.chat_tv)
    TextView chat_tv;
    @BindView(R.id.chat_iv)
    ImageView chat_iv;
    @BindView(R.id.house_ll)
    LinearLayout house_ll;
    @BindView(R.id.house_tv)
    TextView house_tv;
    @BindView(R.id.house_iv)
    ImageView house_iv;
    @BindView(R.id.mine_ll)
    LinearLayout mine_ll;
    @BindView(R.id.mine_tv)
    TextView mine_tv;
    @BindView(R.id.mine_iv)
    ImageView mine_iv;
    List<Fragment> fragmentList = new ArrayList<>();
    private GroupHomeAdapter mAdapter;
    private List<TextView> textViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            L.i("333", "透明状态栏");
            setTranslucentStatus(true);
        }
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(false);
        initDate();
        getUserInfoData();
        getPageData();
    }

    /**
     * 设置状态栏透明
     *
     * @param on
     */
    public void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * 处理版本更新信息
     */
    private void handleUpdate() {
        final boolean showCancel;
        final StartupResultBean.UpdateInfo updateInfo = EventBus.getDefault().getStickyEvent(StartupResultBean.UpdateInfo.class);
        if (updateInfo != null) {//有更新
            if ("1".equals(updateInfo.getUp_status())) {//强制更新
                showCancel = false;
            } else if ("2".equals(updateInfo.getUp_status())) {//选择更新
                showCancel = true;
            } else {
                return;
            }
            if (TextUtils.isEmpty(updateInfo.getUrl()) ||
                    (!updateInfo.getUrl().startsWith("http") && !updateInfo.getUrl().startsWith("https"))) {
                return;//url不可用
            }
            new UpdateVersionDialog().showDialog(mContext, showCancel, updateInfo.getContent(), new UpdateVersionDialog.ConfirmDialogListener() {
                @Override
                public void ok() {
                    Intent intent = new Intent(mContext, UpdateVersionService.class);
                    intent.putExtra("url", updateInfo.getUrl());
                    mContext.startService(intent);
                    T.s(mContext, "开始下载,请在下载完成后确认安装！");
                    if (!showCancel) {//是强更则关闭界面
                        finish();
                    }
                }

                @Override
                public void cancel() {
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initDate() {
        handleUpdate();
        textViews = new ArrayList<>();
        textViews.add(group_tv);
        textViews.add(house_tv);
        textViews.add(chat_tv);
        textViews.add(event_tv);
        textViews.add(mine_tv);
        fragmentList.add(new HomeFragment());
        fragmentList.add(new MainGameFragment());
        fragmentList.add(new FindFragment());
        fragmentList.add(new GameTestNewFragmentNew());
        fragmentList.add(new MainMineFragmentNew2());
        mAdapter = new GroupHomeAdapter(getSupportFragmentManager(), fragmentList);
        clear();
        show(0);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                clear();
                show(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.group_ll, R.id.event_ll, R.id.chat_ll, R.id.house_ll, R.id.mine_ll})
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.search_iv:
//                SearchActivity.start(mContext);
//                break;
//            case iv_title_down:
//                DownloadManagerActivity.start(mContext);
//                break;
            case R.id.group_ll:
                clear();
                show(0);
                break;
            case R.id.event_ll:
                clear();
                show(3);
                break;
            case R.id.chat_ll:
                clear();
                show(2);
                break;
            case R.id.house_ll:
                clear();
                show(1);
                break;
            case R.id.mine_ll:
                clear();
                show(4);
                break;
            default:
                break;
        }
    }

    private void clear() {
        for (int i = 0; i < textViews.size(); i++) {
            textViews.get(i).setTextColor(getResources().getColor(R.color.black));
        }
        group_iv.setImageResource(R.mipmap.tab_icon_tj_us);
        house_iv.setImageResource(R.mipmap.tab_icon_game_us);
//        chat_iv.setImageResource(R.mipmap.tab_icon_find_us);
        event_iv.setImageResource(R.mipmap.zixun_us);
        mine_iv.setImageResource(R.mipmap.tab_icon_my_us);
    }

    public void show(int position) {
        getPageData();
        mViewPager.setCurrentItem(position);
        textViews.get(position).setTextColor(getResources().getColor(R.color.text_green));
        switch (position) {
            case 0:
                group_iv.setImageResource(R.mipmap.tab_icon_tj_s);
                break;
            case 1:
                house_iv.setImageResource(R.mipmap.tab_icon_game_s);
                break;
            case 2:
//                chat_iv.setImageResource(R.mipmap.tab_icon_find_s);
                break;
            case 3:
                event_iv.setImageResource(R.mipmap.zixun_s);
                break;
            case 4:
                mine_iv.setImageResource(R.mipmap.tab_icon_my_s);
                break;
        }
    }

    // 退出时间
    private long currentBackPressedTime = 0;
    // 退出间隔
    private static final int BACK_PRESSED_INTERVAL = 2000;

    //重写onBackPressed()方法,继承自退出的方法
    @Override
    public void onBackPressed() {
        // 判断时间间隔
        if (System.currentTimeMillis() - currentBackPressedTime > BACK_PRESSED_INTERVAL) {
            currentBackPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
        } else {
            // 退出
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }


    public void getPageData() {
        final ListRequestBean baseRequestBean = new ListRequestBean();
        baseRequestBean.setPage(1 + "");
        baseRequestBean.setOffset("20");
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(baseRequestBean));
        HttpNoLoginCallbackDecode httpCallbackDecode = new HttpNoLoginCallbackDecode<MessageRequestBean>(this, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(MessageRequestBean data) {
                int num = 0;
                if (data != null && data.getList() != null) {
                    for (int i = 0; i < data.getList().size(); i++) {
                        if ("1".equals(data.getList().get(i).getReaded())) {
                            num++;
                        } else if (i == data.getList().size() - 1) {
                        }
                    }
                }
                if (num == 0) {
                    L.i("333", "没有消息");
                    EventBus.getDefault().postSticky(new ShowMsg(false, false));
                } else {
                    L.i("333", "新消息");
                    EventBus.getDefault().postSticky(new ShowMsg(true, false, String.valueOf(num)));
                }
            }

            @Override
            public void onFailure(String code, String msg) {
//                super.onFailure(code, msg);
                EventBus.getDefault().postSticky(new ShowMsg(false, false));
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(AppApi.getUrl(AppApi.userMsgListApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMsgShow(ShowMsg showMsg) {
        if (showMsg.isUp()) {
            L.i("333", "更新信息");
            getPageData();
        }
        if (showMsg.isShow()) {
        } else {
        }
    }

    public void getUserInfoData() {
        final BaseRequestBean baseRequestBean = new BaseRequestBean();
        HttpParamsBuild httpParamsBuild = new HttpParamsBuild(GsonUtil.getGson().toJson(baseRequestBean));
        HttpNoLoginCallbackDecode httpCallbackDecode = new HttpNoLoginCallbackDecode<UserInfoResultBean>(mContext, httpParamsBuild.getAuthkey()) {
            @Override
            public void onDataSuccess(UserInfoResultBean data) {
                if (data != null) {
                    if (StringUtils.isEmpty(data.getPortrait()) || data.getPortrait().equals("http://static.idielian.com")) {
                        Bitmap bmp = BitmapFactory.decodeResource(getResources(), ImgUtil.getError());
                        L.i("333", "开始上传头像：");
                        updateHeadImage(savePhoto(bmp));
                    }
                }
            }

            @Override
            public void onFailure(String code, String msg) {
                if (CODE_SESSION_ERROR.equals(code)) {
                } else {
                    super.onFailure(code, msg);
                }
            }
        };
        httpCallbackDecode.setShowTs(true);
        httpCallbackDecode.setLoadingCancel(false);
        httpCallbackDecode.setShowLoading(false);
        RxVolley.post(AppApi.getUrl(AppApi.userDetailApi), httpParamsBuild.getHttpParams(), httpCallbackDecode);
    }

    private File savePhoto(Bitmap bitmap) {
        //进行图片保存
        try {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, bitmap.getWidth(), bitmap.getHeight(),
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
            File file = new File(mContext.getCacheDir(), System.currentTimeMillis() + "temp.jpg");
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void updateHeadImage(File file) {
        HttpParams httpParams = AppApi.getCommonHttpParams(AppApi.userHeadImgApi);
        httpParams.put("portrait", file);
        //成功，失败，null数据
        NetRequest.request(this).setParams(httpParams).post(AppApi.getUrl(AppApi.userHeadImgApi), new HttpJsonCallBackDialog<AddressList>() {
            @Override
            public void onDataSuccess(AddressList data) {
//                T.s(getContext(), "上传成功");
                L.i("333", "上传头像成功");
            }
        });
    }
}
