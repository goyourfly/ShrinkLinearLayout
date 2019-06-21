# ShirkLinearLayout

ShirkLinearLayout 是一个自定 LinearLayout，它的目的是当 LinearLayout 的子 View 高度超过 LinearLayout 的高度时，让子 View 按照一定的比例缩放

### Demo
 ![](./screenshot.gif)

### Usage
一般和 LinearLayout 使用方法完全一致，但是多了一个 `shirk` 属性，通过 shirk 配置缩放强度，如果是 `0` 或者 `froze`，则不缩放

````
    <com.goyourfly.view.ShirkLinearLayout
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
            android:text="Shirk"
            android:textColor="@android:color/white"
            android:textSize="40dp"
            app:shirk="1" />

        <ImageView
            android:layout_width="match_parent"
            android:layout_height="80dp"
            android:background="#9C27B0"
            android:gravity="center"
            android:src="@drawable/ic_launcher_foreground"
            android:text="A"
            android:textColor="@android:color/white"
            android:textSize="40dp"
            app:shirk="2" />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="40dp"
            android:adjustViewBounds="true"
            android:background="#03A9F0"
            android:gravity="center"
            android:src="@drawable/ic_launcher_foreground"
            android:text="Froze"
            android:textColor="@android:color/white"
            app:shirk="frozen" />

    </com.goyourfly.view.ShirkLinearLayout>
````