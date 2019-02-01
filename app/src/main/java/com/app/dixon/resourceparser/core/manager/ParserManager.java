package com.app.dixon.resourceparser.core.manager;

import com.app.dixon.resourceparser.core.pub.parser.ParseQueue;
import com.app.dixon.resourceparser.core.pub.parser.Parser;

/**
 * Created by dixon.xu on 2019/2/1.
 */

public class ParserManager {

    private static ParseQueue queue;

    private ParserManager() {
    }

    public static void init() {
        queue = Parser.newParseQueue();
    }

    public static ParseQueue queue() {
        if (queue == null) {
            throw new NullPointerException("You must init parseQueue first!");
        }
        return queue;
    }
}
