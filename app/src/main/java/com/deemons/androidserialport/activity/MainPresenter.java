package com.deemons.androidserialport.activity;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.deemons.androidserialport.R;
import com.deemons.androidserialport.bean.LeftDetailBean;
import com.deemons.androidserialport.bean.LeftHeadBean;
import com.deemons.androidserialport.bean.MessageBean;
import com.deemons.serialportlib.ByteUtils;
import com.deemons.serialportlib.SerialPort;
import com.deemons.serialportlib.SerialPortFinder;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.disposables.Disposable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import org.joda.time.DateTime;

/**
 * author： deemons
 * date:    2018/5/26
 * desc:
 */
public class MainPresenter implements MainContract.IPresenter {

    MainContract.IView mView;

    private String mPath;
    private int    mBaudRate;
    private int    mCheckDigit;
    private int    mDataBits;
    private int    mStopBit;

    private SerialPort mSerialPort;
    private boolean    isInterrupted;
    private Disposable mDisposable;

    public MainPresenter(MainContract.IView view) {
        mView = view;
    }

    private void refreshValueFormSp() {
        mPath = SPUtils.getInstance().getString(SPKey.SERIAL_PORT, "");
        mBaudRate = Integer.parseInt(SPUtils.getInstance().getString(SPKey.BAUD_RATE, "9600"));
        mCheckDigit = Integer.parseInt(SPUtils.getInstance().getString(SPKey.CHECK_DIGIT, "0"));
        mDataBits = Integer.parseInt(SPUtils.getInstance().getString(SPKey.DATA_BITS, "8"));
        mStopBit = Integer.parseInt(SPUtils.getInstance().getString(SPKey.STOP_BIT, "1"));
    }

    public void getLeftData() {
        refreshValueFormSp();

        ArrayList<MultiItemEntity> list = new ArrayList<>();

        list.add(getLeftSerialPortBean());
        list.add(getLeftBaudRateBean());
        list.add(getLeftCheckDigitBean());
        list.add(getLeftDataBitsBean());
        list.add(getLeftStopBitsBean());

        mView.setLeftData(list);
    }

    @NonNull
    private LeftHeadBean getLeftSerialPortBean() {
        ArrayList<LeftDetailBean> list = new ArrayList<>();

        SerialPortFinder finder = new SerialPortFinder();
        String[] path = finder.getAllDevicesPath();
        for (String s : path) {
            if (TextUtils.isEmpty(mPath)) {
                mPath = s;
            }

            if (s.equals(mPath)) {
                list.add(new LeftDetailBean(s, true));
            } else {
                list.add(new LeftDetailBean(s));
            }
        }

        LeftHeadBean bean = new LeftHeadBean();
        bean.imageRes = R.mipmap.ic_serial_port;
        bean.title = "串口";
        bean.spKey =SPKey.SERIAL_PORT;
        bean.value = mPath;

        for (LeftDetailBean detailBean : list) {
            bean.addSubItem(detailBean);
        }

        return bean;
    }

    private LeftHeadBean getLeftBaudRateBean() {
        ArrayList<LeftDetailBean> list = new ArrayList<>();

        int[] array = Utils.getApp().getResources().getIntArray(R.array.baud_rate);
        for (int i : array) {
            if (i == mBaudRate) {
                list.add(new LeftDetailBean(String.valueOf(i), true));
            } else {
                list.add(new LeftDetailBean(String.valueOf(i)));
            }
        }

        LeftHeadBean bean = new LeftHeadBean();
        bean.imageRes = R.mipmap.ic_baud;
        bean.title = "波特率";
        bean.spKey =SPKey.BAUD_RATE;
        bean.value = String.valueOf(mBaudRate);

        for (LeftDetailBean leftDetailBean : list) {
            bean.addSubItem(leftDetailBean);
        }

        return bean;
    }

    private LeftHeadBean getLeftCheckDigitBean() {
        ArrayList<LeftDetailBean> list = new ArrayList<>();

        int[] array = Utils.getApp().getResources().getIntArray(R.array.check_digit);
        for (int i : array) {
            if (i == mCheckDigit) {
                list.add(new LeftDetailBean(String.valueOf(i), true));
            } else {
                list.add(new LeftDetailBean(String.valueOf(i)));
            }
        }

        LeftHeadBean bean = new LeftHeadBean();
        bean.imageRes = R.mipmap.ic_check;
        bean.title = "校验位";
        bean.spKey =SPKey.CHECK_DIGIT;
        bean.value = String.valueOf(mCheckDigit);

        for (LeftDetailBean leftDetailBean : list) {
            bean.addSubItem(leftDetailBean);
        }

        return bean;
    }

    private LeftHeadBean getLeftDataBitsBean() {
        ArrayList<LeftDetailBean> list = new ArrayList<>();

        int[] array = Utils.getApp().getResources().getIntArray(R.array.data_bits);
        for (int i : array) {
            if (i == mStopBit) {
                list.add(new LeftDetailBean(String.valueOf(i), true));
            } else {
                list.add(new LeftDetailBean(String.valueOf(i)));
            }
        }

        LeftHeadBean bean = new LeftHeadBean();
        bean.imageRes = R.mipmap.ic_data;
        bean.title = "数据位";
        bean.spKey =SPKey.DATA_BITS;
        bean.value = String.valueOf(mDataBits);

        for (LeftDetailBean leftDetailBean : list) {
            bean.addSubItem(leftDetailBean);
        }

        return bean;
    }

    private LeftHeadBean getLeftStopBitsBean() {
        ArrayList<LeftDetailBean> list = new ArrayList<>();

        int[] array = Utils.getApp().getResources().getIntArray(R.array.stop_bits);
        for (int i : array) {
            if (i == mStopBit) {
                list.add(new LeftDetailBean(String.valueOf(i), true));
            } else {
                list.add(new LeftDetailBean(String.valueOf(i)));
            }
        }

        LeftHeadBean bean = new LeftHeadBean();
        bean.imageRes = R.mipmap.ic_stop;
        bean.title = "停止位";
        bean.spKey =SPKey.STOP_BIT;
        bean.value = String.valueOf(mStopBit);

        for (LeftDetailBean leftDetailBean : list) {
            bean.addSubItem(leftDetailBean);
        }

        return bean;
    }

    public void open() {
        refreshValueFormSp();
        try {
            mSerialPort =
                new SerialPort(new File(mPath), mBaudRate, mCheckDigit, mDataBits, mStopBit, 0);
        } catch (IOException e) {
            e.printStackTrace();
            ToastUtils.showLong("打开失败！请尝试其它串口！");
        } catch (SecurityException e) {
            e.printStackTrace();
            mView.showPermissionDialog();
        }

        if (mSerialPort != null) {
            isInterrupted = false;
            onReceiveSubscribe();
        }

        mView.setOpen(mSerialPort != null);
    }

    public void close() {
        isInterrupted = true;
        if (mDisposable != null) {
            mDisposable.dispose();
        }
    }

    private void onReceiveSubscribe() {
        mDisposable = Flowable.create((FlowableOnSubscribe<byte[]>) emitter -> {
            InputStream is = mSerialPort.getInputStream();
            byte[] buffer = new byte[64];
            while (!isInterrupted) {
                if (mSerialPort == null || is == null) {
                    close();
                    return;
                }
                is.read(buffer);
                emitter.onNext(buffer);
            }
        }, BackpressureStrategy.LATEST)
            .map(ByteUtils::bytesToHexString)
            .map(this::addSpace)
            .subscribe(it -> mView.addData(
                new MessageBean(MessageBean.TYPE_RECEIVE, DateTime.now().toString("HH:mm:ss:SSS"),
                    it)), Throwable::printStackTrace);
    }

    private String addSpace(String s) {
        if (s.length() % 2 == 0) {
            StringBuilder builder = new StringBuilder();
            char[] array = s.toCharArray();
            int length = array.length;
            for (int i = 0; i < length; i += 2) {
                if (i <= length - 2) {
                    builder.append(" ");
                }

                builder.append(array[0]);
                builder.append(array[1]);
            }

            return builder.toString();
        }
        return s;
    }

    public void sendMsg(String contain) {

    }
}
