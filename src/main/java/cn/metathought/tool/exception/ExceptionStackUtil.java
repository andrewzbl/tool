package cn.metathought.tool.exception;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhoubinglong
 */
@Slf4j
public class ExceptionStackUtil {

    public static String getExceptionStackTrace(Throwable anexcepObj) {
        if (anexcepObj != null) {
            try (StringWriter sw = new StringWriter();
                PrintWriter printWriter = new PrintWriter(sw)) {
                anexcepObj.printStackTrace(printWriter);
                printWriter.flush();
                sw.flush();
                return sw.toString();
            } catch (IOException e) {
                log.error("getExceptionStackTrace error", e);
            }
        }
        return null;
    }
}