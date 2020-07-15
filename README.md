# Logdorak

Wrap SLF4J logger to provide a simpler format mechanism.

Line endings in logged messages are stripped to avoid log injection.

## Usage
Logdorak Logger methods takes an object array as argument.

If the last argument is a `Throwable`, it will be given as is to SLF4J.
Every other argument will be turned into a `String` using `Objects.toString`, stripped from any `\r` or `\n` character and joined together to be used as the message.

## Example
~~~java
public class Thermostat {
  private static final Logger LOGGER = new Logger(Thermostat.class);

  public void setTemperature(int temperature) {
    LOGGER.trace("Will set the temperature to ", temperature, "°C.");
    try (Connector connector = new Connector()) {
      connector.setTemperature(temperature);
      LOGGER.info("Temperature has been set to ", temperature, "°C.")
    } catch (IOException e) {
      LOGGER.error("Unable to set the temperature to ", temperature, "°C", e);
    }
  }
}
~~~
