import org.apache.commons.cli.*;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import java.util.Properties;

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

        Integer port = Integer.valueOf(cl.getOptionValue("port"));
        String host = cl.getOptionValue("host");
        String user = cl.getOptionValue("user");
        String pwd = cl.getOptionValue("pwd");

        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");

            Session session = Session.getInstance(props);

            Transport transport = session.getTransport("smtp");
            transport.connect(host, port, user, pwd);
            transport.close();
            System.out.println("Connection established [" + host + "]");
        } catch (AuthenticationFailedException e) {
            System.out.println("Authentication failed");
        } catch (MessagingException e) {
            System.out.println("Unable to connect to server");
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