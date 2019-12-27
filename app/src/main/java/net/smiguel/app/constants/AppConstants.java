package net.smiguel.app.constants;

public class AppConstants {

    public static class View {
        public static final int NOTIFICATION_ID = 1002;
    }

    public static class SharedData {
        public static final String SHARED_PREF_FILE_NAME = "smiguel_net_pref_file";
        public static final String SHARED_PREF_TOKEN = "token";
    }

    public static class Database {
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "SmiguelNet.db";
        public static final String TABLE_CUSTOMER = "tb_customer";

        public static final String COL_ID = "id";
        public static final String COL_ID_REF = "id_ref";
    }

    public static class Network {
        public static final long HTTP_CONN_SECONDS_TIMEOUT = 60;
        public static final long HTTP_READ_SECONDS_TIMEOUT = 15;
        public static final long HTTP_WRITE_SECONDS_TIMEOUT = 15;
        public static final String GSON_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    }

    public static class Jobs {
        public static final long EXECUTE_SYNC_JOB_MAX_ATTEMPT = 3;
        public static final long EXECUTE_SYNC_JOB_MILLISECONDS = 5000;
    }
}
