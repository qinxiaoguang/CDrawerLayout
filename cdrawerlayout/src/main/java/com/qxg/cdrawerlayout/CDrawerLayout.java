package com.qxg.cdrawerlayout;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Looper;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by Qking on 2016/10/17.
 * 继承DrawerLayout，并修改其中一部分代码
 * 使用方式简单
 * 使用时，不能设置fitsSystemWindow设为true
 */
public class CDrawerLayout extends DrawerLayout {

    private View homeLayout;  //主布局
    private View navDrawerLayout; //用户设置抽屉布局
    private FrameLayout drawerLayout;  //抽屉总布局
    private LinearLayout mainHomeLayout; //主页总布局
    private LinearLayout childDrawerLayout;

    private LinearLayout headerLayout;
    private LinearLayout menuLayout;
    private LinearLayout footerLayout;

    private Toolbar toolbar;

    //状态栏
    private View statusBar;

    //默认DrawerLayout的头部，菜单部，和脚部的View
    private View headerView;
    private View menuView;
    private View footerView;

    private Context context;
    private int toolbarColor = TOOL_BAR_NO_COLOR;

    final int version = Build.VERSION.SDK_INT;

    //自定义属性
    private boolean navigationFlag = false;
    private boolean custom = false; //该标志表示用户不想用你的CDrawerLayout,用户想要自己搞，不过会用你的statusBar而已，所以不用initLayout了
    private boolean contextFlag = false; //判断context是否属于AppcompatActivity

    private static final int TOOL_BAR_NO_COLOR = -2;
    private int homeFlag = FLAG_NO_LAYOUT;  //主布局标志
    private int navFlag = FLAG_NO_LAYOUT; //抽屉布局标志
    private int drawerFlag = SYSTEM_LAYOUT; //使用自定义布局，还是NavigationView,还是预设好的布局
    private int barColor = NO_STATUS_COLOR;

    private static final int NAVIGATION_VIEW_LAYOUT = 11;
    private static final int CUSTOM_LAYOUT = 12;
    private static final int SYSTEM_LAYOUT = 13;

    private static final int FLAG_RELATIVE_LAYOUT = 1;
    private static final int FLAG_LINEAR_LAYOUT = 2;
    private static final int FLAG_FRAME_LAYOUT = 3;
    private static final int FLAG_GRID_LAYOUT = 4;
    private static final int FLAG_NO_LAYOUT = -1;

    private static final int NO_STATUS_COLOR = -1;


    private static final ViewGroup.LayoutParams FULL_SCREEN_PARAMS =
            new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


    private LayoutInflater getLayoutInflater(){
        LayoutInflater inflater = LayoutInflater.from(context);
        if(inflater == null){
            throw new RuntimeException("LayoutInflater must not be null");
        }
        return inflater;
    }

    /**
     * 传入View的参数，返回对应的FLAG值，用该值判断该View的布局
     */
    private int getFlag(View layout){
        if(layout instanceof RelativeLayout)return FLAG_RELATIVE_LAYOUT;
        if(layout instanceof LinearLayout) return FLAG_LINEAR_LAYOUT;
        if(layout instanceof FrameLayout) return FLAG_FRAME_LAYOUT;
        if(layout instanceof GridLayout) return FLAG_GRID_LAYOUT;
        else return -1;
    }


    private void getCustom(Context context,AttributeSet attrs)
    {
        //取得自定义属性
        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.CDrawerLayout);

        navigationFlag = ta.getBoolean(R.styleable.CDrawerLayout_navigation,false);
        custom = ta.getBoolean(R.styleable.CDrawerLayout_custom,false);

        if(navigationFlag){
            drawerFlag = NAVIGATION_VIEW_LAYOUT;
        }
        ta.recycle();
    }


    public CDrawerLayout(Context context) {
        this(context,null);
    }

    public CDrawerLayout(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public CDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        getCustom(context,attrs);
        contextFlag = getContextFlag();
        if(!custom){
            initLayout();
        }else{
            initStatusBar();
        }
    }

    private void initStatusBar(){
        //设置布局透明状态栏
        if(context instanceof Activity && version >= Build.VERSION_CODES.KITKAT){
            ((Activity)context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //((Activity)context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    private void initMainHomeLayout(){
        //初始化主页布局
        mainHomeLayout = new LinearLayout(context);
        mainHomeLayout.setOrientation(LinearLayout.VERTICAL);
        mainHomeLayout.setLayoutParams(FULL_SCREEN_PARAMS);

        //如果版本大于API19，就添加一个View取代状态栏
        if(version >= Build.VERSION_CODES.KITKAT)
        {
            statusBar = new View(context);
            ViewGroup.LayoutParams vlp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,getStatusBarSize());
            statusBar.setLayoutParams(vlp);
            statusBar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

            mainHomeLayout.addView(statusBar);
        }
    }

    private void initDrawerLayout(){
        //初始化抽屉布局
        drawerLayout = new FrameLayout(context);
        //这里必须要用DrawerLayout中的LayoutParams。
        CDrawerLayout.LayoutParams fllp =
                new CDrawerLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        fllp.gravity = Gravity.LEFT;
        drawerLayout.setLayoutParams(fllp);
        childDrawerLayout = new LinearLayout(context);
        childDrawerLayout.setOrientation(LinearLayout.VERTICAL);
        childDrawerLayout.setBackgroundColor(getResources().getColor(R.color.white));
        drawerLayout.addView(childDrawerLayout);

        //初始化headerLayout,menuLayout,footerLayout;
        headerLayout = new LinearLayout(context);
        menuLayout = new LinearLayout(context);
        footerLayout = new LinearLayout(context);

        LinearLayout.LayoutParams headerLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0);
        headerLp.weight = 3;
        LinearLayout.LayoutParams menuLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0);
        menuLp.weight = 8;
        LinearLayout.LayoutParams footerLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0);
        footerLp.weight = 1;

        headerLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        footerLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        headerLayout.setLayoutParams(headerLp);
        menuLayout.setLayoutParams(menuLp);
        footerLayout.setLayoutParams(footerLp);

        setMainDrawerLayout();
    }

    private void initLayout(){
        initStatusBar();
        initMainHomeLayout();
        initDrawerLayout();
        initToolbar();

        addView(mainHomeLayout);

        if(!navigationFlag){
            addView(drawerLayout);
        }
    }

    private void initToolbar(){
        if(contextFlag){
            toolbar = new Toolbar(context);
            toolbar.setTitle(getApplicationName());
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            mainHomeLayout.addView(toolbar);
        }
    }

    /**
     * 如果Activity继承的是AppcompatActivity，并且使用的是android.support.v7.widget.Toolbar
     * 就可以调用该方法取得Toolbar，取得Toobar后，可以将Toolbar通过相应的setActionBar来进行设置
     */
    public Toolbar getAppcompatToolbar(){
        return toolbar;
    }


    private View getDrawerLayout(){
        int count = getChildCount();
        for(int i=0;i<count;i++){
            View child = getChildAt(i);
            if(((LayoutParams)child.getLayoutParams()).gravity == Gravity.LEFT){
                return child;
            }
        }
        return null;
    }

    private void setMainDrawerLayout(){
        if(drawerLayout==null){
            throw new RuntimeException("drawerLayout must not be null");
        }

        switch(drawerFlag){
            case SYSTEM_LAYOUT:
                //使用系统布局
                removeView(getNavigationView());
                setSystemDrawerLayout();
                break;
            case CUSTOM_LAYOUT:
                removeView(getNavigationView());
                childDrawerLayout.removeAllViews();
                childDrawerLayout.addView(navDrawerLayout);
                //使用自定义布局
                break;
            case NAVIGATION_VIEW_LAYOUT:
                //使用NavigationView
                removeView(drawerLayout);
                //将nav的fitsSystemWindow设置为false,因为默认是true
                setNavFitsWindow(false);
                break;
            default:
                throw new RuntimeException("drawerFlag must be {SYSTEM_LAYOTU , CUSTOM_LAYOUT, NAVIGATION_VIEW_LAYOUT}");
        }
    }

    private void setSystemDrawerLayout(){
        //系统布局
        //判断homeLayout等等是否为空

        if(headerView == null && headerView == footerView && footerView == menuView){
            //全空，只需要将来layout加入即可
            childDrawerLayout.removeAllViews();
            childDrawerLayout.addView(headerLayout);
            childDrawerLayout.addView(menuLayout);
            childDrawerLayout.addView(footerLayout);
        }
        else if(headerView == null && headerView == menuView){
            //两空
            drawerAddOneView(footerView);
        }else if(headerView == null && headerView == footerView){
            //两空
            drawerAddOneView(menuView);
        }else if(menuView == null && menuView == footerView){
            //两空
            drawerAddOneView(headerView);
        }else if(headerView == null){
            //一空
            drawerAddTwoView(HEADER_VIEW_NULL);
        }else if(footerView == null){
            //一空
            drawerAddTwoView(FOOTER_VIEW_NULL);
        }else if(menuView == null){
            //一空
            drawerAddTwoView(MENU_VIEW_NULL);
        }else{
            //0空
            headerView.setLayoutParams(FULL_SCREEN_PARAMS);
            menuView.setLayoutParams(FULL_SCREEN_PARAMS);
            footerView.setLayoutParams(FULL_SCREEN_PARAMS);

            childDrawerLayout.removeAllViews();
            headerLayout.addView(headerView);
            menuLayout.addView(menuView);
            footerLayout.addView(footerView);

            childDrawerLayout.addView(headerLayout);
            childDrawerLayout.addView(menuLayout);
            childDrawerLayout.addView(footerLayout);
        }
    }


    private static final int FOOTER_VIEW_NULL = 111;
    private static final int MENU_VIEW_NULL = 112;
    private static final int HEADER_VIEW_NULL = 113;

    private void drawerAddTwoView(int flag){
        if(headerView!=null)headerView.setLayoutParams(FULL_SCREEN_PARAMS);
        if(footerView!=null)footerView.setLayoutParams(FULL_SCREEN_PARAMS);
        if(menuView!=null)menuView.setLayoutParams(FULL_SCREEN_PARAMS);
        childDrawerLayout.removeAllViews();
        switch(flag){
            case FOOTER_VIEW_NULL:
                headerLayout.addView(headerView);
                menuLayout.addView(menuView);
                childDrawerLayout.addView(headerLayout);
                childDrawerLayout.addView(menuLayout);
                break;
            case MENU_VIEW_NULL:
                headerLayout.addView((headerView));
                footerLayout.addView(footerView);
                childDrawerLayout.addView(headerLayout);
                childDrawerLayout.addView(footerLayout);
                break;
            case HEADER_VIEW_NULL:
                menuLayout.addView((menuView));
                footerLayout.addView(footerView);
                childDrawerLayout.addView(menuLayout);
                childDrawerLayout.addView(footerLayout);
                break;
        }
    }

    //两空
    private void drawerAddOneView(View view){
        childDrawerLayout.removeAllViews();
        //ViewGroup.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(FULL_SCREEN_PARAMS);
        childDrawerLayout.addView(view);
    }

    private View getNavigationView(){
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if(child instanceof NavigationView){
                return child;
            }
        }
        return null;
    }

    private void setMainHomeLayout(){
        if(homeLayout!=null){
            //首先移除之前存在的homeLayout
            mainHomeLayout.removeView(homeLayout);
            homeLayout.setLayoutParams(FULL_SCREEN_PARAMS);
            mainHomeLayout.addView(homeLayout);
        }
        //一定要做的是设置statusBar的颜色
        if(barColor!=NO_STATUS_COLOR && statusBar!=null){
            statusBar.setBackgroundColor(getResources().getColor(barColor));
        }

        if(toolbarColor!=TOOL_BAR_NO_COLOR){
            if(toolbar!=null)
                toolbar.setBackgroundColor(getResources().getColor(toolbarColor));
        }
    }

    private boolean getContextFlag(){
        if(context instanceof AppCompatActivity)return true;
        else return false;
    }

    /**
     * 设置使用自定义DrawerLayout,这些设置全部返回的this,也就是说，您可以通过链式来对该类进行设置
     * 但是到最后一定要调用commit才能使得设置有效。
     */
    public CDrawerLayout setCustomNavLayout(int layout){

        navDrawerLayout = LayoutInflater.from(context).inflate(layout,null);
        if(navDrawerLayout == null){
            throw new RuntimeException("in CDrawerLayout the drawerLayout is null");
        }else{
            //ViewGroup.LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            navDrawerLayout.setLayoutParams(FULL_SCREEN_PARAMS);
        }

        drawerFlag = CUSTOM_LAYOUT;
        return this;
    }


    /**
     * 设置headerLayout,menuLayout,footerLayout等
     * 一旦设置了headerLayout等布局，就不会使用NavigationView
     */
    public CDrawerLayout setHeaderLayout(int layout){

        headerView = getLayout(layout);
        drawerFlag = SYSTEM_LAYOUT;
        return this;
    }
    public CDrawerLayout setMenuLayout(int layout){
        menuView = getLayout(layout);
        drawerFlag = SYSTEM_LAYOUT;
        return this;
    }
    public CDrawerLayout setFooterLayout(int layout){
        footerView = getLayout(layout);
        drawerFlag = SYSTEM_LAYOUT;
        return this;
    }

    /**
     * 设置Toolbar，默认是true，如果传入false，则是不会显示toolbar的
     */
    public CDrawerLayout setToolbar(boolean flag){
        if(flag){
            if(contextFlag){
                mainHomeLayout.removeView(toolbar);
                mainHomeLayout.addView(toolbar);
            }
        }else{
            if(contextFlag){
                mainHomeLayout.removeView(toolbar);
            }
        }
        return this;
    }

    /**
     * 设置Toolbar的颜色
     */
    public CDrawerLayout setToolbarColor(int color){
        toolbarColor = color;
        return this;
    }

    /**
     *  设置statusBar颜色
     */
    public CDrawerLayout setStatusBarColor(int color){
        //设置statusBar的颜色
        barColor = color;
        return this;
    }

    /**
     * 设置主页布局，默认情况下是有Toolbar的，设置的主页布局应当是不包含Toolbar的
     */
    public CDrawerLayout setHomeLayout(int layout){
        homeLayout = getLayoutInflater().inflate(layout,null);
        if(homeLayout == null){
            throw new RuntimeException("the param in setHomeLayout is wrong");
        }

        homeFlag = getFlag(homeLayout);
        return this;
    }


    /**
     * 如果只想把该类当作DrawerLayout使用，则需要调用该方法
     */
    public void setCustom(){
        //用户想要自己搞事
        custom = true;
        commit();
    }


    /**
     * 如果通过setXX等方法设置相应的属性后，到最后一定要调用commit()方法，用于更新界面布局
     * 否则系统是不会响应您通过setXX方法做的相应改动
     */
    public void commit(){
        refreshLayout();
        invalidate();
    }

    public CDrawerLayout setNavFitsWindow(boolean flag){
        NavigationView nav = (NavigationView) getNavigationView();
        if(nav!=null) nav.setFitsSystemWindows(flag);
        return this;
    }


    private void refreshLayout(){
        //主页布局不用改变，只有抽屉布局需要改变
        if(!custom){
            setMainDrawerLayout();
            setMainHomeLayout();
        }else{
            removeView(homeLayout);
            removeView(drawerLayout);
            setNavFitsWindow(false);
        }
    }

    private View getLayout(int layout){
        return getLayoutInflater().inflate(layout,null);
    }


    private boolean isMainThread(){
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    private int getStatusBarSize(){
        //取得状态栏高度
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }


    @Override
    public void invalidate() {
        if(isMainThread()){
            super.invalidate();
        }else{
            postInvalidate();
        }
    }

    private String getApplicationName(){
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try{
            packageManager = ((Activity)context).getApplicationContext().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(((Activity)context).getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        String applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
        return applicationName;
    }

}
