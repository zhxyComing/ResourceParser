package com.app.dixon.resourceparser.core.dson;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dixon.xu on 2019/2/21.
 * <p>
 * 自定义Dson格式数据，方便本地调用、存储
 */

public class Dson {

    //返回model集合（以行为model，参数不限）
    public static List<DsonData> parse(String dson) throws IOException {
        List<DsonData> dataList = new ArrayList<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(dson.getBytes(Charset.forName("utf8"))), Charset.forName("utf8")));
        String line;
        while ((line = br.readLine()) != null) {
            if (!line.trim().equals("")) {
                dataList.add(parseLine(line));
            }
        }
        return dataList;
    }

    private static DsonData parseLine(String line) throws IOException {
        DsonData dd = new DsonData();
        String[] params = line.split("\\|");
        for (String param : params) {
            String[] kv = param.split(":");
            if (kv.length != 2) {
                throw new IOException("param is error");
            }
            dd.put(kv[0], kv[1]);
        }
        return dd;
    }
}
