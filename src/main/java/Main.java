import org.apache.commons.cli.*;

import javax.mail.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    private static String appName = "malooer";

    /**
     * Required parameters:
     * -port <PORT_NUMBER>
     * -host <HOST>
     * -user <USER>
     * -pwd <PASSWORD>
     *
     * @param args
     */
    public static void main(String[] args) {
        HelpFormatter hFormatter = new HelpFormatter();
        CommandLine cl;

        if (args.length < 1) {
            hFormatter.printHelp(appName, getOptions());
            return;
        }

        try {
            cl = getCommandline(args);
        } catch (ParseException e) {
            hFormatter.printHelp(appName, getOptions());
            return;
        }

        final Integer port = Integer.valueOf(cl.getOptionValue("port"));
        final String host = cl.getOptionValue("host");
        final String user = cl.getOptionValue("user");
        final String pwd = cl.getOptionValue("pwd");


        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(props);

        try {
            final Transport transport = session.getTransport("smtp");
            Runnable runnable = new Runnable() {
                public void run() {
                    Date date = new Date();
                    Timestamp timestamp = new Timestamp(date.getTime());
                    try {
                        transport.connect(host, port, user, pwd);
                        transport.close();
                        System.out.println(timestamp + ": [OK] Connection established - " + host);
                    } catch (AuthenticationFailedException e) {
                        System.out.println(timestamp + ": [FAIL] Authentication failed");
                    } catch (MessagingException e) {
                        System.out.println(timestamp + ": [FAIL] Unable to connect to server");
                    }
                }
            };

            ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
            service.scheduleAtFixedRate(runnable, 0, 5, TimeUnit.MINUTES);
        } catch (NoSuchProviderException e) {
            System.out.println("No such provider");
            return;
        }
    }

    private static CommandLine getCommandline(String[] args) throws ParseException {
        Options options = getOptions();
        CommandLineParser clParser = new DefaultParser();

        return clParser.parse(options, args);
    }

    private static Options getOptions() {
        Options options = new Options();

        Option portOption = new Option("port", true, "port of server");
        Option hostOption = new Option("host", true, "the host");
        Option userOption = new Option("user", true, "the user");
        Option passOption = new Option("pwd", true, "the password");

        options.addOption(portOption);
        options.addOption(hostOption);
        options.addOption(userOption);
        options.addOption(passOption);

        return options;
    }
}