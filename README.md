# CDrawerLayout
一款使用简单的DrawerLayout，不用在布局文件中写一大堆的DrawerLayout，NavigationView等复杂的操作，也不用因为沉浸式状态栏而苦恼，只需要几步操作即可。

# 使用方法
布局文件只需写以下代码即可，再也不需要写一大堆繁杂的代码啦！
```xml
<?xml version="1.0" encoding="utf-8"?>
<com.qxg.cdrawerlayout.CDrawerLayout
    android:id="@+id/drawerlayout"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    xmlns:android="http://schemas.android.com/apk/res/android" />
```

在MainActivity中获取到CDrawerLayout后，即可对其进行操作：
`CDrawerLayout drawerLayout = (CDrawerLayout)findViewById(R.id.drawerlayout);`

更换主界面的操作如下:

* `setHomeLayout(int layout)` 更换主界面为layout
* `setCustomNavLayout(int layout)` 设置自定义抽屉界面为layout
* `setHeaderLayout(int layout)`,`setFooterLayout(int layout)`,`setMenuLayout(int layout)`使用默认抽屉界面，并分别设置头部布局，中间布局，以及底部布局。
* `setToolBarColor(int color)`设置Toolbar的颜色为color
* `setStatusBarColor(int color)`设置状态栏颜色为color
* `setToolBar(boolean flag)` 是否需要标题栏
* `getAppcompatToolbar()`取得布局中的Toolbar，之后，您可以对Toolbar进行一系列的操作
* `commit()`所有设置选项采用链式操作，并且设置完成的最后要调用`commit()`方法进行提交，这样所有的设置才会生效。


自定义属性：
* custome:该属性含义为，如果您想要把CDrawerLayout纯粹当作DrawerLayout来使用，就设置该值为true即可。
* navigation:该属性表示，如果您不想要使用默认的抽屉布局，而是想要使用NavigationView，那么将该属性设置为true，并且在布局的CDrawerLayout中加上NavigationView即可。

示例代码：
```
CDrawerLayout drawerLayout = (CDrawerLayout)findViewById(R.id.drawerlayout);
drawerLayout.setStatusBarColor(R.color.colorPrimary)
			.setCustomNavLayout(R.layout.nav_layout)
            .setHomeLayout(R.layout.home)
            .commit();
```
# 注意事项
* 确保您的Activity继承的是AppcompatActivity
* 确保您的界面使用的主题是NoActionBar
* 确保在每次操作之后使用commit提交，否则界面不会进行改变
* 在commit()提交之后，您可以通过findViewById()方法来获取相应布局中的一些View。
* 因为该项目是继承的DrawerLaoyout，您依然可以使用DrawerLayout的一些方法，比如`setSCrimColor()`来设置阴影的颜色等。
* API19以上为沉浸式状态栏

# 效果
使用CDrawerLayout实现的一些效果：
![](http://www.qxgzone.com/images/custom.gif)
![](http://www.qxgzone.com/images/comproj.gif)

# 导入方式
```
	compile 'com.qxg:cdrawerlayout:1.0.0'
```