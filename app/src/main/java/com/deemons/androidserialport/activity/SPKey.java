package com.deemons.androidserialport.activity;

/**
 * author： deemons
 * date:    2018/5/26
 * desc:
 */
public interface SPKey {

    String SU_PATH      = "su_path";
    String SERIAL_PORT  = "serial_port";
    String BAUD_RATE    = "baud_rate";
    String DATA_BITS    = "data_bits";
    String CHECK_DIGIT  = "check_digit";
    String STOP_BIT     = "stop_bit";
    String FLOW_CONTROL = "flow_control";

    String SETTING_RECEIVE_TYPE = "receive_type";
    String SETTING_RECEIVE_SHOW_TIME = "setting_receive_show_time";
    String SETTING_RECEIVE_SHOW_SEND = "setting_receive_show_send";

    String SETTING_SEND_TYPE   = "setting_send_type";
    String SETTING_SEND_REPEAT = "setting_send_repeat";
    String SETTING_SEND_DURING = "setting_send_during";
    String SEND_HISTORY        = "send_history";
}
