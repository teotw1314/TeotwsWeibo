package com.skyland.sky_data.result;

import com.skyland.sky_data.bean.StatusInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by skyland on 2017/7/10
 */

public class StatusResult {

    public List<StatusInfo> statuses = new ArrayList<>();
    public String next_cursor;
    public int total_number;


}
