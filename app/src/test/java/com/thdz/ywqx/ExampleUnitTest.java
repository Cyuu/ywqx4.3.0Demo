package com.thdz.ywqx;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        double x = Math.floor(12.1);
        double y = Math.floor(12.6);

        assertEquals(x, y);
    }


    @Test
    public void testShort() {
        byte b1 = 0x09;
        byte b2 = 0x10;
        int ppp = b1 + (b2 << 8);

        assertEquals(ppp, 24);
    }

    @Test
    public void testPackage() {
        String json = "{\"id\":\"1\",\"name\":\"123\",\"age\":\"44\",\"oth\":\"1231211\"}";
        Foo bean = com.alibaba.fastjson.JSONObject.parseObject(json, Foo.class);

        assertEquals(bean.getId(), "123");
    }

}