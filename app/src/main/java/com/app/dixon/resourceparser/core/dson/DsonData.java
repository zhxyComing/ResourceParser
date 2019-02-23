package com.app.dixon.resourceparser.core.dson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dixon.xu on 2019/2/21.
 * <p>
 * 代表一行参数集合
 * <p>
 * 1.Dson以一行为一Model;
 * 2.Model内参数个数、类型不限;
 * 3.以'|'为KV对分割符，以':'为KV分割符。
 */

public class DsonData {

    private Map<String, String> map;

    public DsonData() {
        map = new HashMap<>();
    }

    public void put(String key, String value) {
        map.put(key, value);
    }

    public String get(String key) {
        return map.get(key);
    }
}
