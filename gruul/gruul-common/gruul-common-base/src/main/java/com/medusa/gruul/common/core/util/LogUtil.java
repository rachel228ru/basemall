package com.medusa.gruul.common.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

/**
 * <p>
 * 请求用户信息工具类
 * </p>
 *
 * @author 王鹏
 * @since 2019-1206-23
 */
public class LogUtil {
    private static Logger logger;
    private static final String NO_EXCEPTION_TEMPLATE  = "{0}.{1}({2}:{3}) desc is => {4} ; info is => ";

    private static final String HAS_EXCEPTION_TEMPLATE = "{0}.{1}({2}:{3}) desc is => {6} ; error is => ({4}:{5}) ; info is => ";


    public static void info(Throwable e, String methodDesc, String... info) {
        StackTraceElement stackTraceElement = getStackTraceElement();
        Logger log = getLogger(stackTraceElement.getClassName());
        info(log, stackTraceElement, e, methodDesc, "", info);
    }

    public static void info(String methodDesc, String infoFormat, String... info) {
        StackTraceElement stackTraceElement = getStackTraceElement();
        Logger log = getLogger(stackTraceElement.getClassName());
        info(log, stackTraceElement, null, methodDesc, infoFormat, info);
    }

    private static void info(Logger log, StackTraceElement stackTraceElement, Throwable throwable, String methodDesc,
                             String infoFormat, String... info) {
        StackTraceElement showSte = stackTraceElement;
        if (throwable == null) {
            String infoMsg = MessageFormat.format(NO_EXCEPTION_TEMPLATE, showSte.getClassName(),
                    showSte.getMethodName(), showSte.getFileName(), showSte.getLineNumber() + "", methodDesc)
                    + infoFormat;
            log.info(infoMsg, info);
        } else {
            StackTraceElement[] stackTraceElements = throwable.getStackTrace();
            int steLen = stackTraceElements.length;
            for (int i = steLen - 1; i >= 0; i--) {
                StackTraceElement eSte = stackTraceElements[i];
                if (eSte.getClassName().contains(".lifewallet.")) {
                    showSte = eSte;
                }
            }
            String infoMsg = MessageFormat.format(HAS_EXCEPTION_TEMPLATE, showSte.getClassName(),
                    showSte.getMethodName(), showSte.getFileName(), showSte.getLineNumber() + "",
                    throwable.getClass().getName(), throwable.getMessage(), methodDesc)
                    + StringUtil.arrayToStr(info, ";");
            log.info(infoMsg, throwable.getCause());
        }
    }

    private static void debug(Logger log, StackTraceElement stackTraceElement, Throwable throwable, String methodDesc,
                              String infoFormat, String... info) {
        StackTraceElement showSte = stackTraceElement;
        if (throwable == null) {
            String infoMsg = MessageFormat.format(NO_EXCEPTION_TEMPLATE, showSte.getClassName(),
                    showSte.getMethodName(), showSte.getFileName(), showSte.getLineNumber() + "", methodDesc)
                    + infoFormat;
            log.debug(infoMsg, info);
        } else {
            StackTraceElement[] stackTraceElements = throwable.getStackTrace();
            int steLen = stackTraceElements.length;
            for (int i = steLen - 1; i >= 0; i--) {
                StackTraceElement eSte = stackTraceElements[i];
                if (eSte.getClassName().contains(".lifewallet.")) {
                    showSte = eSte;
                }
            }
            String infoMsg = MessageFormat.format(HAS_EXCEPTION_TEMPLATE, showSte.getClassName(),
                    showSte.getMethodName(), showSte.getFileName(), showSte.getLineNumber() + "",
                    throwable.getClass().getName(), throwable.getMessage(), methodDesc)
                    + StringUtil.arrayToStr(info, ";");
            log.debug(infoMsg, throwable.getCause());
        }
    }

    public static void debug(Throwable e, String methodDesc, String... info) {
        StackTraceElement stackTraceElement = getStackTraceElement();
        Logger log = getLogger(stackTraceElement.getClassName());
        debug(log, stackTraceElement, e, methodDesc, "", info);
    }

    public static void debug(String methodDesc, String infoFormat, String... info) {
        StackTraceElement stackTraceElement = getStackTraceElement();
        Logger log = getLogger(stackTraceElement.getClassName());
        debug(log, stackTraceElement, null, methodDesc, infoFormat, info);
    }

    public static void error(Throwable e, String methodDesc, String... info) {
        StackTraceElement stackTraceElement = getStackTraceElement();
        Logger logger = getLogger(stackTraceElement.getClassName());
        error(logger, stackTraceElement, e, methodDesc, "", info);
    }

    public static void error(String methodDesc, String infoFormat, String... info) {
        StackTraceElement stackTraceElement = getStackTraceElement();
        Logger logger = getLogger(stackTraceElement.getClassName());
        error(logger, stackTraceElement, null, methodDesc, infoFormat, info);
    }

    private static void error(Logger log, StackTraceElement stackTraceElement, Throwable throwable, String methodDesc,
                              String infoFormat, String... info) {
        StackTraceElement showSte = stackTraceElement;
        if (throwable == null) {
            String errorMsg = MessageFormat.format(NO_EXCEPTION_TEMPLATE, showSte.getClassName(),
                    showSte.getMethodName(), showSte.getFileName(), showSte.getLineNumber() + "", methodDesc)
                    + infoFormat;
            log.error(errorMsg, (Object[]) info);
        } else {
            StackTraceElement[] eStes = throwable.getStackTrace();
            int steLen = eStes.length;
            for (int i = steLen - 1; i >= 0; i--) {
                StackTraceElement eSte = eStes[i];
                if (eSte.getClassName().contains(".lifewallet.")) {
                    showSte = eSte;
                }
            }
            String errorMsg = MessageFormat.format(HAS_EXCEPTION_TEMPLATE, showSte.getClassName(),
                    showSte.getMethodName(), showSte.getFileName(), showSte.getLineNumber() + "",
                    throwable.getClass().getName(), throwable.getMessage(), methodDesc)
					+ StringUtil.arrayToStr(info, ";");
            log.error(errorMsg, throwable.getCause());
        }
    }

    private static Logger getLogger(String className) {
        Logger log;
        try {
            log = LoggerFactory.getLogger(className);
        } catch (Exception e) {
            log = logger;
        }
        return log;
    }

    private static StackTraceElement getStackTraceElement() {
        StackTraceElement[] temp = Thread.currentThread().getStackTrace();
        return temp[3];
    }



}
