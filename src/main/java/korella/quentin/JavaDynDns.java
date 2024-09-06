package korella.quentin;

import java.util.Arrays;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JavaDynDns implements Runnable {
    public static boolean stopProcess = false;
    public static Logger logger = LogManager.getLogger();
    public static Cloudflare cloudflare = new Cloudflare(logger);

    public static void main(String[] args) throws Exception {
        JavaDynDns javaDynDns = new JavaDynDns();
        Thread thread = new Thread(javaDynDns);
        Scanner scanner = new Scanner(System.in);
       
        thread.setName("updateRecordsThread");
        thread.start();
        logger.debug("updateRecordsThread started");

        while (!stopProcess) {
            String input = scanner.nextLine();
            logger.debug("scanner started");

            if (Arrays.asList("stop", "kill", "end").contains(input)) {
                logger.info("Shutting down....");
                stopProcess = true;
            }
        }

        thread.interrupt();
        logger.debug("updateRecordsThread stopped");
        scanner.close();
        logger.debug("scanner stopped");
    }

    @Override
    public void run() {
        while (!stopProcess) {
            try {
                cloudflare.updateRecordsByName();

                logger.info("updated Records");

                Thread.sleep(cloudflare.getRefreshRateInMinutes() * 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
