package com.thdz.ywqx.util;

import java.util.List;

/**
 * desc:    测试数据
 * author:  Administrator
 * date:    2018/8/6  11:38
 */
public class TestUtil {

    public static int getCodeFromList(List<Byte> list) {
        return list.get(2) + list.get(2) << 8;

    }
}
