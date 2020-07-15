package com.github.raphcal.logdorak;

import java.util.Objects;
import java.util.regex.Pattern;

import org.slf4j.LoggerFactory;

/**
 * Wrap SLF4J logger to provide a simpler format mechanism.
 *
 * <p>Line endings in logged messages are stripped to avoid log injection.</p>
 *
 * <strong>Usage example:</strong><br>
 * <pre>
 * public class Thermostat {
 *   private static final Logger LOGGER = new Logger(Thermostat.class);
 *
 *   public void setTemperature(int temperature) {
 *     LOGGER.trace("Will set the temperature to ", temperature, "°C.");
 *     try (Connector connector = new Connector()) {
 *       connector.setTemperature(temperature);
 *       LOGGER.info("Temperature has been set to ", temperature, "°C.")
 *     } catch (IOException e) {
 *       LOGGER.error("Unable to set the temperature to ", temperature, "°C", e);
 *     }
 *   }
 * }
 * </pre>
 *
 * @author Raphaël Calabro (ddaeke-github@yahoo.fr)
 */
public class Logger {

    /**
     * Pattern to detect line endings.
     */
    private static final Pattern LINE_ENDINGS = Pattern.compile("[\r\n]");

    /**
     * Logger delegate.
     */
    private final org.slf4j.Logger delegate;

    /**
     * Creates a new instance for the given class.
     */
    public Logger(Class<?> clazz) {
        this.delegate = LoggerFactory.getLogger(clazz);
    }

    /**
     * Log a message and optionally a throwable at the ERROR level.
     * <p>Throwable should be the last argument.</p>
     *
     * @param arguments Message parts with a throwable.
     */
    public void error(Object... arguments) {
        final Object maybeThrowable = arguments[arguments.length - 1];
        if (maybeThrowable instanceof Throwable) {
            delegate.error(concatStringValuesOf(arguments, arguments.length - 1), (Throwable) maybeThrowable);
        } else {
            delegate.error(concatStringValuesOf(arguments));
        }
    }

    /**
     * Log a message and optionally a throwable at the WARN level.
     * <p>Throwable should be the last argument.</p>
     *
     * @param arguments Message parts with a throwable.
     */
    public void warn(Object... arguments) {
        final Object maybeThrowable = arguments[arguments.length - 1];
        if (maybeThrowable instanceof Throwable) {
            delegate.warn(concatStringValuesOf(arguments, arguments.length - 1), (Throwable) maybeThrowable);
        } else {
            delegate.warn(concatStringValuesOf(arguments));
        }
    }

    /**
     * Log a message and optionally a throwable at the INFO level.
     * <p>Throwable should be the last argument.</p>
     *
     * @param arguments Message parts with a throwable.
     */
    public void info(Object... arguments) {
        final Object maybeThrowable = arguments[arguments.length - 1];
        if (maybeThrowable instanceof Throwable) {
            delegate.info(concatStringValuesOf(arguments, arguments.length - 1), (Throwable) maybeThrowable);
        } else {
            delegate.info(concatStringValuesOf(arguments));
        }
    }

    /**
     * Log a message and optionally a throwable at the DEBUG level.
     * <p>Throwable should be the last argument.</p>
     *
     * @param arguments Message parts with a throwable.
     */
    public void debug(Object... arguments) {
        final Object maybeThrowable = arguments[arguments.length - 1];
        if (maybeThrowable instanceof Throwable) {
            delegate.debug(concatStringValuesOf(arguments, arguments.length - 1), (Throwable) maybeThrowable);
        } else {
            delegate.debug(concatStringValuesOf(arguments));
        }
    }

    /**
     * Log a message and optionally a throwable at the TRACE level.
     * <p>Throwable should be the last argument.</p>
     *
     * @param arguments Message parts with a throwable.
     */
    public void trace(Object... arguments) {
        final Object maybeThrowable = arguments[arguments.length - 1];
        if (maybeThrowable instanceof Throwable) {
            delegate.trace(concatStringValuesOf(arguments, arguments.length - 1), (Throwable) maybeThrowable);
        } else {
            delegate.trace(concatStringValuesOf(arguments));
        }
    }

    /**
     * Creates a new string joining every string value from the given objects.
     * Line endings are stripped from the result.
     *
     * @param objects Parts of the message to build.
     * @return A string.
     */
    private String concatStringValuesOf(Object[] objects) {
        return concatStringValuesOf(objects, objects.length);
    }

    /**
     * Creates a new string joining the string value of the first <code>length</code> given objects.
     *
     * @param objects Parts of the message to build.
     * @param length Number of parts to use.
     * @return A string.
     */
    private String concatStringValuesOf(Object[] objects, int length) {
        final String[] strings = new String[length];
        int totalLength = 0;
        for (int index = 0; index < length; index++) {
            final String string = LINE_ENDINGS.matcher(Objects.toString(objects[index]))
                .replaceAll("");
            strings[index] = string;
            totalLength += string.length();
        }
        final char[] characters = new char[totalLength];
        int start = 0;
        for (final String string : strings) {
            final char[] source = string.toCharArray();
            System.arraycopy(source, 0, characters, start, source.length);
            start += source.length;
        }
        return new String(characters);
    }
}