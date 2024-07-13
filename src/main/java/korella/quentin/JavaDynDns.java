package korella.quentin;

import java.util.Arrays;
import java.util.Scanner;

public class JavaDynDns implements Runnable {
    public static boolean stopProcess = false;
    public static Cloudflare cloudflare;

    public static void main(String[] args) throws Exception {
        JavaDynDns javaDynDns = new JavaDynDns();
        Thread thread = new Thread(javaDynDns);
        Scanner scanner = new Scanner(System.in);
        cloudflare = new Cloudflare();
       
        thread.start();

        while (!stopProcess) {
            String input = scanner.nextLine();

            if (Arrays.asList("stop", "kill", "end").contains(input)) {
                stopProcess = true;
            }
        }

        thread.interrupt();
        scanner.close();
    }

    @Override
    public void run() {
        while (!stopProcess) {
            try {
                cloudflare.updateRecordsByName();

                Thread.sleep(cloudflare.getRefreshRateInMinutes() * 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
