package com.deemons.androidserialport.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.blankj.utilcode.util.KeyboardUtils;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.deemons.androidserialport.R;
import com.deemons.androidserialport.adapter.LeftAdapter;
import com.deemons.androidserialport.adapter.MsgAdapter;
import com.deemons.androidserialport.bean.MessageBean;
import com.deemons.serialportlib.SerialPort;
import com.oushangfeng.pinnedsectionitemdecoration.PinnedHeaderItemDecoration;
import com.oushangfeng.pinnedsectionitemdecoration.callback.OnHeaderClickListener;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements MainContract.IView {

    private MainPresenter mPresenter;
    private RecyclerView  mMainRv;
    private MsgAdapter    mMsgAdapter;
    private LeftAdapter   mLeftAdapter;
    private MenuItem      mMenuItem;
    private boolean       isOpen;
    private boolean       isShowBottom;
    private boolean       willShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPresenter = new MainPresenter(this);

        initView();

        initRv();

        initListener();

        KeyboardUtils.registerSoftInputChangedListener(this, height -> {
            if (isShowBottom && height != 0) {
                showBottom(false);
            } else if (height == 0 && willShow) {
                showBottom(true);
                willShow = false;
            }
        });
    }

    private void initListener() {
        RadioGroup receiveRg = findViewById(R.id.receive_rg);
        receiveRg.setOnCheckedChangeListener((group, checkedId) -> {
            mPresenter.refreshReceiveType(checkedId==R.id.receive_hex);
        });

        RadioGroup sendRg = findViewById(R.id.send_rg);
        sendRg.setOnCheckedChangeListener((group, checkedId) -> {
            mPresenter.refreshSendType(checkedId==R.id.send_hex);
        });

        CheckBox showSend = findViewById(R.id.receive_show_send);
        showSend.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mPresenter.refreshShowSend(isChecked);
        });

        CheckBox showTime = findViewById(R.id.receive_show_time);
        showTime.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mPresenter.refreshShowTime(isChecked);
        });

        CheckBox sendRepeat = findViewById(R.id.send_repeat);
        sendRepeat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mPresenter.refreshSendRepeat(isChecked);
        });
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle =
            new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initRv() {
        mMainRv = findViewById(R.id.main_rv);

        ArrayList<MessageBean> list = new ArrayList<>();
        list.add(new MessageBean(MessageBean.TYPE_RECEIVE, "08:00:00:123", "receive"));
        list.add(new MessageBean(MessageBean.TYPE_SEND, "08:00:00:123", "send"));
        mMsgAdapter = new MsgAdapter(list);
        mMainRv.setLayoutManager(new LinearLayoutManager(this));
        mMainRv.setAdapter(mMsgAdapter);
        GestureDetector gestureDetector =
            new GestureDetector(this, new GestureDetector.OnGestureListener() {
                @Override
                public boolean onDown(MotionEvent e) {
                    return false;
                }

                @Override
                public void onShowPress(MotionEvent e) {

                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    if (KeyboardUtils.isSoftInputVisible(MainActivity.this)) {
                        KeyboardUtils.hideSoftInput(MainActivity.this);
                    }
                    if (isShowBottom) {
                        showBottom(false);
                    }
                    return true;
                }

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                    float distanceY) {
                    return false;
                }

                @Override
                public void onLongPress(MotionEvent e) {

                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                    float velocityY) {
                    return false;
                }
            });
        mMainRv.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));

        RecyclerView leftRv = findViewById(R.id.left_rv);
        mLeftAdapter = new LeftAdapter(new ArrayList<>());

        leftRv.addItemDecoration(
            new PinnedHeaderItemDecoration.Builder(LeftAdapter.TYPE_HEAD).disableHeaderClick(false)
                .setHeaderClickListener(new OnHeaderClickListener() {
                    @Override
                    public void onHeaderClick(View view, int id, int position) {
                        mLeftAdapter.collapseOrExpand(mLeftAdapter.getItem(position));
                    }

                    @Override
                    public void onHeaderLongClick(View view, int id, int position) {

                    }

                    @Override
                    public void onHeaderDoubleClick(View view, int id, int position) {

                    }
                })
                .create());
        leftRv.setLayoutManager(new LinearLayoutManager(this));
        leftRv.setAdapter(mLeftAdapter);
        mPresenter.getLeftData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_delete) {
            mMsgAdapter.setNewData(new ArrayList<>());
            return true;
        } else if (id == R.id.action_run) {
            mMenuItem = item;
            if (isOpen) {
                mPresenter.close();
            } else {
                mPresenter.open();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setOpen(boolean isOpen) {
        if (mMenuItem != null) {
            mMenuItem.setIcon(isOpen ? R.mipmap.ic_launcher : R.mipmap.ic_close);
        }
        this.isOpen = isOpen;
    }

    @Override
    public void setLeftData(ArrayList<MultiItemEntity> list) {
        mLeftAdapter.setNewData(list);
    }

    @Override
    public void addData(MessageBean messageBean) {
        mMsgAdapter.addData(messageBean);
    }

    @SuppressLint("StringFormatInvalid")
    @Override
    public void showPermissionDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_permission, null);
        TextView textView = view.findViewById(R.id.dialog_text);
        String string = getResources().getString(R.string.permission_error);

        textView.setText(String.format(Locale.getDefault(), string, SerialPort.getSuPath()));
        AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle("提示")
            .setView(view)
            .setNegativeButton("取消", (dialog, which) -> {
                dialog.dismiss();
            })
            .setPositiveButton("确定", (dialog, which) -> {
                EditText editText = view.findViewById(R.id.dialog_path);
                if (editText != null && !TextUtils.isEmpty(editText.getText().toString())) {
                    SerialPort.setSuPath(editText.getText().toString());
                    dialog.dismiss();
                }
            })
            .create();

        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (isOpen) {
            mPresenter.close();
        } else if (isShowBottom) {
            showBottom(false);
        } else {
            super.onBackPressed();
        }
    }

    private void showBottom(boolean isShow) {
        View view = findViewById(R.id.main_bottom_contain);
        view.setVisibility(isShow ? View.VISIBLE : View.GONE);
        isShowBottom = isShow;
    }

    public void onClickHistory(View view) {
        mMsgAdapter.setNewData(new ArrayList<>());
    }

    public void onClickMore(View view) {
        if (KeyboardUtils.isSoftInputVisible(this)) {
            KeyboardUtils.hideSoftInput(this);
            willShow = true;
        } else {
            showBottom(!isShowBottom);
        }
    }

    public void onClickSend(View view) {
        EditText editText = findViewById(R.id.main_edit);
        String contain = editText.getText().toString();
        if (!TextUtils.isEmpty(contain)) {
            mPresenter.sendMsg(contain);
        }
        editText.getText().clear();
        KeyboardUtils.hideSoftInput(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KeyboardUtils.fixSoftInputLeaks(this);
    }

    public void onClickRepeatDuring(View view) {
        View inflateView = getLayoutInflater().inflate(R.layout.dialog_during, null);
        AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle("输入时间")
            .setView(inflateView)
            .setNegativeButton("取消", (dialog, which) -> {
                dialog.dismiss();
            })
            .setPositiveButton("确定", (dialog, which) -> {
                EditText editText = inflateView.findViewById(R.id.during_edit);
                if (editText != null && !TextUtils.isEmpty(editText.getText().toString())) {
                    TextView textView = findViewById(R.id.send_repeat_during);
                    int result = Integer.parseInt(editText.getText().toString().trim());
                    int hour_12 = 1000 * 60 * 60 * 12;
                    textView.setText(String.valueOf(
                        result > hour_12 ? hour_12 : result));
                    mPresenter.refreshSendDuring(result);
                    dialog.dismiss();
                }
            })
            .setOnDismissListener(dialog -> { })
            .create();

        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }
}
