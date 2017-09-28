package com.founder.serverapi;


public interface ServerCons {
    String ACTION_ADD = "com.founder.server.action.ADD";
    String ACTION_PLUS = "com.founder.server.action.plus";
    String ACTION_REVERSE = "com.founder.server.action.reverse";

    String RECEIVER_TAG = "receiverTag";
    String MESSENGER_TAG = "messengerTag";
    String EXTRA_PARAM1 = "com.founder.server.extra.PARAM1";
    String EXTRA_PARAM2 = "com.founder.server.extra.PARAM2";
    
    String RESULT_TAG = "resultTag";

    int MSG_SUM = 0x110;

}
