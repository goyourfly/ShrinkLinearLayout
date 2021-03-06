# ShrinkLinearLayout

[![](https://jitpack.io/v/goyourfly/ShrinkLinearLayout.svg)](https://jitpack.io/#goyourfly/ShrinkLinearLayout)


ShrinkLinearLayout 是一个自定 LinearLayout，它的目的是当 LinearLayout 的子 View 高度超过 LinearLayout 的高度时，让子 View 按照一定的比例缩放

### Demo
 ![](./screenshot.gif)

### Download

Step 1. Add the JitPack repository to your build file
````
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
````

Step 2. Add the dependency
````
dependencies {
    implementation 'com.github.goyourfly:ShrinkLinearLayout:1.1'
}
````

### Usage
一般和 LinearLayout 使用方法完全一致，但是多了一个 `shrink` 属性，通过 shrink 配置缩放强度，如果是 `0` 或者 `froze`，则不缩放

````
    <com.goyourfly.view.ShrinkLinearLayout
        android:id="@+id/linearLayout"
        android:layout_width="match_parent"
        android:layout_height="500dp"
        android:background="#ddd"
        android:orientation="vertical">

        <TextView
            android:layout_width="match_parent"
            android:layout_height="200dp"
            android:background="#673AB7"
            android:gravity="center"
            android:text="Shrink"
            android:textColor="@android:color/white"
            android:textSize="40dp"
            app:shrink="1" />

        <ImageView
            android:layout_width="match_parent"
            android:layout_height="80dp"
            android:background="#9C27B0"
            android:gravity="center"
            android:src="@drawable/ic_launcher_foreground"
            android:text="A"
            android:textColor="@android:color/white"
            android:textSize="40dp"
            app:shrink="2" />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="40dp"
            android:adjustViewBounds="true"
            android:background="#03A9F0"
            android:gravity="center"
            android:src="@drawable/ic_launcher_foreground"
            android:text="Froze"
            android:textColor="@android:color/white"
            app:shrink="frozen" />

    </com.goyourfly.view.ShrinkLinearLayout>
````
