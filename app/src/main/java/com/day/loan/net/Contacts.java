package com.day.loan.net;

/**
 * Created by apple on 2018/5/17.
 */

public interface Contacts {
    String LEAN_KEY = "1Xractc2sikvNt5m6r9GP7sK";
    String LEAN_ID = "9zeHS4F8GAHi97Fkg0p80FYV-gzGzoHsz";
    String UMENG_KEY = "5b875cbea40fa30595000027";

    String PHONE = "phone";

    String TOKEN = "token";

    String MUN = "mun";

    String LIMIT = "limit";

    class Times {
        /**
         * 启动页显示时间
         **/
        public static final int LAUCHER_DIPLAY_MILLIS = 2000;
        /**
         * 倒计时时间
         **/
        public static final int MILLIS_IN_TOTAL = 60000;
        /**
         * 时间间隔
         **/
        public static final int COUNT_DOWN_INTERVAL = 1000;
    }


}
