package de.ocker.olfitracker.commons;

import org.apache.log4j.Logger;

/**
 * Encapsulates the whole logging mechanism<br>
 * generates log files and deletes them on no exception program shutdown
 * 
 * @author Florian J. Ocker
 */
public class Print {
  private static int line = 0;
  private static Logger logger;

  public static void line(String formattedMsg, Object... args) {
    if (logger == null) {
      initLogger();
    }
    line++;
    logger.info(line + ": " + String.format(formattedMsg, args));
    System.out.println(line + ": " + String.format(formattedMsg, args));
  }

  public static void warn(String formattedMsg, Object... args) {
    if (logger == null) {
      initLogger();
    }
    line++;
    logger.debug(line + ": " + String.format(formattedMsg, args));
    System.out.println(line + ": " + "WARN: " + String.format(formattedMsg, args));

  }

  public static void err(String formattedMsg, Object... args) {
    if (logger == null) {
      initLogger();
    }
    line++;
    logger.error(line + ": " + String.format(formattedMsg, args));
    System.err.println(line + ": " + String.format(formattedMsg, args));
  }

  public static void err(Throwable throwable) {
    if (logger == null) {
      initLogger();
    }
    line++;
    logger.error(line + ": " + throwable);
    for (StackTraceElement elem : throwable.getStackTrace()) {
      line++;
      logger.error("\t" + elem);
    }
    throwable.printStackTrace();
  }

  private static void initLogger() {
    try {
      logger = Logger.getRootLogger();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

}
