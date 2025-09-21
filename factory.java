public class factory {
    public static void main(String[] args) {
        demoFactoryMethod();
        demoAbstractFactory();
    }

    interface NotificationChannel {
        void send(String to, String message);
    }

    static final class EmailChannel implements NotificationChannel {
        @Override public void send(String to, String message) {
            System.out.println("Email -> " + to + ": " + message);
        }
    }
    static final class SmsChannel implements NotificationChannel {
        @Override public void send(String to, String message) {
            System.out.println("SMS   -> " + to + ": " + message);
        }
    }

    static abstract class Notifier {
        protected abstract NotificationChannel createChannel();
        public void notify(String to, String msg) {
            createChannel().send(to, msg);
        }
    }

    static final class EmailNotifier extends Notifier {
        @Override protected NotificationChannel createChannel() {
            return new EmailChannel();
        }
    }
    static final class SmsNotifier extends Notifier {
        @Override protected NotificationChannel createChannel() {
            return new SmsChannel();
        }
    }

    private static void demoFactoryMethod() {
        Notifier email = new EmailNotifier();
        Notifier sms   = new SmsNotifier();

        email.notify("alice@example.com", "добро пожаловать!!!!");
        sms.notify("+770012341488", "код подтверждения: поставьте 100 пожалуйста");
    }

    interface DbConnection {
        void connect();
    }
    interface DbCommand    {
        void execute(String sql);
    }

    interface DbFactory {
        DbConnection createConnection();
        DbCommand createCommand();
    }

    static final class PgConnection implements DbConnection {
        @Override public void connect() { System.out.println("Connect: PostgreSQL"); }
    }
    static final class PgCommand implements DbCommand {
        @Override public void execute(String sql) { System.out.println("PostgreSQL exec: " + sql); }
    }
    static final class PgFactory implements DbFactory {
        @Override public DbConnection createConnection() { return new PgConnection(); }
        @Override public DbCommand createCommand() { return new PgCommand(); }
    }

    static final class MyConnection implements DbConnection {
        @Override public void connect() { System.out.println("Connect: MySQL"); }
    }
    static final class MyCommand implements DbCommand {
        @Override public void execute(String sql) { System.out.println("MySQL exec: " + sql); }
    }
    static final class MyFactory implements DbFactory {
        @Override public DbConnection createConnection() { return new MyConnection(); }
        @Override public DbCommand createCommand() { return new MyCommand(); }
    }

    static final class DatabaseApp {
        private final DbConnection conn;
        private final DbCommand cmd;

        DatabaseApp(DbFactory factory) {
            this.conn = factory.createConnection();
            this.cmd  = factory.createCommand();
        }

        void run(String sql) {
            conn.connect();
            cmd.execute(sql);
        }
    }

    private static void demoAbstractFactory() {
        DatabaseApp pgApp = new DatabaseApp(new PgFactory());
        pgApp.run("SELECT * FROM users");

        DatabaseApp myApp = new DatabaseApp(new MyFactory());
        myApp.run("SELECT * FROM users");
    }
}
