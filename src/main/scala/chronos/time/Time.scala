package chronos

import chronos.calendar.Ser
import chronos.format.DateTimeFormatter
import chronos.temporal._


/**
 * A time without time-zone in the ISO-8601 calendar system,
 * such as {@code 10:15:30}.
 * <p>
 * {@code Time} is an immutable date-time object that represents a time,
 * often viewed as hour-minute-second.
 * Time is represented to nanosecond precision.
 * For example, the value "13:45.30.123456789" can be stored in a {@code Time}.
 * <p>
 * It does not store or represent a date or time-zone.
 * Instead, it is a description of the local time as seen on a wall clock.
 * It cannot represent an instant on the time-line without additional information
 * such as an offset or time-zone.
 * <p>
 * The ISO-8601 calendar system is the modern civil calendar system used today
 * in most of the world. This API assumes that all calendar systems use the same
 * representation, this class, for time-of-day.
 *
 * @implSpec
 * This class is immutable and thread-safe.
 *
 * @since 1.8
 */
object Time {
  /**
   * Obtains the current time from the system clock in the default time-zone.
   * <p>
   * This will query the {@link Clock#systemDefaultZone() system clock} in the default
   * time-zone to obtain the current time.
   * <p>
   * Using this method will prevent the ability to use an alternate clock for testing
   * because the clock is hard-coded.
   *
   * @return the current time using the system clock and default time-zone, not null
   */
  def now: Time = {
    now(Clock.systemDefaultZone)
  }

  /**
   * Obtains the current time from the system clock in the specified time-zone.
   * <p>
   * This will query the {@link Clock#system(ZoneId) system clock} to obtain the current time.
   * Specifying the time-zone avoids dependence on the default time-zone.
   * <p>
   * Using this method will prevent the ability to use an alternate clock for testing
   * because the clock is hard-coded.
   *
   * @param zone  the zone ID to use, not null
   * @return the current time using the system clock, not null
   */
  def now(zone: ZoneId): Time = {
    now(Clock.system(zone))
  }

  /**
   * Obtains the current time from the specified clock.
   * <p>
   * This will query the specified clock to obtain the current time.
   * Using this method allows the use of an alternate clock for testing.
   * The alternate clock may be introduced using {@link Clock dependency injection}.
   *
   * @param clock  the clock to use, not null
   * @return the current time, not null
   */
  def now(clock: Clock): Time = {
    object
    val now: Instant = clock.instant
    val offset: ZoneOffset = clock.getZone.getRules.getOffset(now)
    val localSecond: Long = now.getEpochSecond + offset.getTotalSeconds
    val secsOfDay: Int = Math.floorMod(localSecond, SECONDS_PER_DAY).asInstanceOf[Int]
    ofNanoOfDay(secsOfDay * NANOS_PER_SECOND + now.getNano)
  }

  /**
   * Obtains an instance of {@code Time} from an hour and minute.
   * <p>
   * This returns a {@code Time} with the specified hour and minute.
   * The second and nanosecond fields will be set to zero.
   *
   * @param hour  the hour-of-day to represent, from 0 to 23
   * @param minute  the minute-of-hour to represent, from 0 to 59
   * @return the local time, not null
   * @throws DateTimeException if the value of any field is out of range
   */
  def of(hour: Int, minute: Int): Time = {
    HOUR_OF_DAY.checkValidValue(hour)
    if (minute == 0) {
      HOURS(hour)
    }
    MINUTE_OF_HOUR.checkValidValue(minute)
    new Time(hour, minute, 0, 0)
  }

  /**
   * Obtains an instance of {@code Time} from an hour, minute and second.
   * <p>
   * This returns a {@code Time} with the specified hour, minute and second.
   * The nanosecond field will be set to zero.
   *
   * @param hour  the hour-of-day to represent, from 0 to 23
   * @param minute  the minute-of-hour to represent, from 0 to 59
   * @param second  the second-of-minute to represent, from 0 to 59
   * @return the local time, not null
   * @throws DateTimeException if the value of any field is out of range
   */
  def of(hour: Int, minute: Int, second: Int): Time = {
    HOUR_OF_DAY.checkValidValue(hour)
    if ((minute | second) == 0) {
      HOURS(hour)
    }
    MINUTE_OF_HOUR.checkValidValue(minute)
    SECOND_OF_MINUTE.checkValidValue(second)
    new Time(hour, minute, second, 0)
  }

  /**
   * Obtains an instance of {@code Time} from an hour, minute, second and nanosecond.
   * <p>
   * This returns a {@code Time} with the specified hour, minute, second and nanosecond.
   *
   * @param hour  the hour-of-day to represent, from 0 to 23
   * @param minute  the minute-of-hour to represent, from 0 to 59
   * @param second  the second-of-minute to represent, from 0 to 59
   * @param nanoOfSecond  the nano-of-second to represent, from 0 to 999,999,999
   * @return the local time, not null
   * @throws DateTimeException if the value of any field is out of range
   */
  def of(hour: Int, minute: Int, second: Int, nanoOfSecond: Int): Time = {
    HOUR_OF_DAY.checkValidValue(hour)
    MINUTE_OF_HOUR.checkValidValue(minute)
    SECOND_OF_MINUTE.checkValidValue(second)
    NANO_OF_SECOND.checkValidValue(nanoOfSecond)
    create(hour, minute, second, nanoOfSecond)
  }

  /**
   * Obtains an instance of {@code Time} from a second-of-day value.
   * <p>
   * This returns a {@code Time} with the specified second-of-day.
   * The nanosecond field will be set to zero.
   *
   * @param secondOfDay  the second-of-day, from { @code 0} to { @code 24 * 60 * 60 - 1}
   * @return the local time, not null
   * @throws DateTimeException if the second-of-day value is invalid
   */
  def ofSecondOfDay(secondOfDay: Long): Time = {
    SECOND_OF_DAY.checkValidValue(secondOfDay)
    val hours: Int = (secondOfDay / SECONDS_PER_HOUR).asInstanceOf[Int]
    secondOfDay -= hours * SECONDS_PER_HOUR
    val minutes: Int = (secondOfDay / SECONDS_PER_MINUTE).asInstanceOf[Int]
    secondOfDay -= minutes * SECONDS_PER_MINUTE
    create(hours, minutes, secondOfDay.asInstanceOf[Int], 0)
  }

  /**
   * Obtains an instance of {@code Time} from a nanos-of-day value.
   * <p>
   * This returns a {@code Time} with the specified nanosecond-of-day.
   *
   * @param nanoOfDay  the nano of day, from { @code 0} to { @code 24 * 60 * 60 * 1,000,000,000 - 1}
   * @return the local time, not null
   * @throws DateTimeException if the nanos of day value is invalid
   */
  def ofNanoOfDay(nanoOfDay: Long): Time = {
    NANO_OF_DAY.checkValidValue(nanoOfDay)
    val hours: Int = (nanoOfDay / NANOS_PER_HOUR).asInstanceOf[Int]
    nanoOfDay -= hours * NANOS_PER_HOUR
    val minutes: Int = (nanoOfDay / NANOS_PER_MINUTE).asInstanceOf[Int]
    nanoOfDay -= minutes * NANOS_PER_MINUTE
    val seconds: Int = (nanoOfDay / NANOS_PER_SECOND).asInstanceOf[Int]
    nanoOfDay -= seconds * NANOS_PER_SECOND
    create(hours, minutes, seconds, nanoOfDay.asInstanceOf[Int])
  }

  /**
   * Obtains an instance of {@code Time} from a temporal object.
   * <p>
   * This obtains a local time based on the specified temporal.
   * A {@code TemporalAccessor} represents an arbitrary set of date and time information,
   * which this factory converts to an instance of {@code Time}.
   * <p>
   * The conversion uses the {@link TemporalQuery#localTime()} query, which relies
   * on extracting the {@link ChronoField#NANO_OF_DAY NANO_OF_DAY} field.
   * <p>
   * This method matches the signature of the functional interface {@link TemporalQuery}
   * allowing it to be used in queries via method reference, {@code Time::from}.
   *
   * @param temporal  the temporal object to convert, not null
   * @return the local time, not null
   * @throws DateTimeException if unable to convert to a { @code Time}
   */
  def from(temporal: TemporalAccessor): Time = {
    object
    val time: Time = temporal.query(TemporalQuery.localTime)
    if (time == null) {
      throw new DateTimeException("Unable to obtain Time from TemporalAccessor: " + temporal.getClass)
    }
    time
  }

  /**
   * Obtains an instance of {@code Time} from a text string such as {@code 10:15}.
   * <p>
   * The string must represent a valid time and is parsed using
   * {@link java.time.format.DateTimeFormatter#ISO_LOCAL_TIME}.
   *
   * @param text the text to parse such as "10:15:30", not null
   * @return the parsed local time, not null
   * @throws DateTimeParseException if the text cannot be parsed
   */
  def parse(text: CharSequence): Time = {
    parse(text, DateTimeFormatter.ISO_LOCAL_TIME)
  }

  /**
   * Obtains an instance of {@code Time} from a text string using a specific formatter.
   * <p>
   * The text is parsed using the formatter, returning a time.
   *
   * @param text  the text to parse, not null
   * @param formatter  the formatter to use, not null
   * @return the parsed local time, not null
   * @throws DateTimeParseException if the text cannot be parsed
   */
  def parse(text: CharSequence, formatter: DateTimeFormatter): Time = {
    object
    formatter.parse(text, Time.from)
  }

  /**
   * Creates a local time from the hour, minute, second and nanosecond fields.
   * <p>
   * This factory may  a cached value, but applications must not rely on this.
   *
   * @param hour  the hour-of-day to represent, validated from 0 to 23
   * @param minute  the minute-of-hour to represent, validated from 0 to 59
   * @param second  the second-of-minute to represent, validated from 0 to 59
   * @param nanoOfSecond  the nano-of-second to represent, validated from 0 to 999,999,999
   * @return the local time, not null
   */
  private def create(hour: Int, minute: Int, second: Int, nanoOfSecond: Int): Time = {
    if ((minute | second | nanoOfSecond) == 0) {
      HOURS(hour)
    }
    new Time(hour, minute, second, nanoOfSecond)
  }

  private[time] def readExternal(in: DataInput): Time = {
    var hour: Int = in.readByte
    var minute: Int = 0
    var second: Int = 0
    var nano: Int = 0
    if (hour < 0) {
      hour = ~hour
    }
    else {
      minute = in.readByte
      if (minute < 0) {
        minute = ~minute
      }
      else {
        second = in.readByte
        if (second < 0) {
          second = ~second
        }
        else {
          nano = in.readInt
        }
      }
    }
    Time.of(hour, minute, second, nano)
  }

  /**
   * The minimum supported {@code Time}, '00:00'.
   * This is the time of midnight at the start of the day.
   */
  final val MIN: Time = null
  /**
   * The maximum supported {@code Time}, '23:59:59.999999999'.
   * This is the time just before midnight at the end of the day.
   */
  final val MAX: Time = null
  /**
   * The time of midnight at the start of the day, '00:00'.
   */
  final val MIDNIGHT: Time = null
  /**
   * The time of noon in the middle of the day, '12:00'.
   */
  final val NOON: Time = null
  /**
   * Constants for the local time of each hour.
   */
  private final val HOURS: Array[Time] = new Array[Time](24)
  /**
   * Hours per day.
   */
  private[time] final val HOURS_PER_DAY: Int = 24
  /**
   * Minutes per hour.
   */
  private[time] final val MINUTES_PER_HOUR: Int = 60
  /**
   * Minutes per day.
   */
  private[time] final val MINUTES_PER_DAY: Int = MINUTES_PER_HOUR * HOURS_PER_DAY
  /**
   * Seconds per minute.
   */
  private[time] final val SECONDS_PER_MINUTE: Int = 60
  /**
   * Seconds per hour.
   */
  private[time] final val SECONDS_PER_HOUR: Int = SECONDS_PER_MINUTE * MINUTES_PER_HOUR
  /**
   * Seconds per day.
   */
  private[time] final val SECONDS_PER_DAY: Int = SECONDS_PER_HOUR * HOURS_PER_DAY
  /**
   * Milliseconds per day.
   */
  private[time] final val MILLIS_PER_DAY: Long = SECONDS_PER_DAY * 1000L
  /**
   * Microseconds per day.
   */
  private[time] final val MICROS_PER_DAY: Long = SECONDS_PER_DAY * 1000 _000L
  /**
   * Nanos per second.
   */
  private[time] final val NANOS_PER_SECOND: Long = 1000 _000_000L
  /**
   * Nanos per minute.
   */
  private[time] final val NANOS_PER_MINUTE: Long = NANOS_PER_SECOND * SECONDS_PER_MINUTE
  /**
   * Nanos per hour.
   */
  private[time] final val NANOS_PER_HOUR: Long = NANOS_PER_MINUTE * MINUTES_PER_HOUR
  /**
   * Nanos per day.
   */
  private[time] final val NANOS_PER_DAY: Long = NANOS_PER_HOUR * HOURS_PER_DAY
  /**
   * Serialization version.
   */
  private final val serialVersionUID: Long = 6414437269572265201L
}

case class Time(hour: Int, minute: Int, second: Int, nanoOfSecond: Int) extends Temporal with TemporalAdjuster with Comparable[Time]  {
  /**
   * Constructor, previously validated.
   *
   * @param hour  the hour-of-day to represent, validated from 0 to 23
   * @param minute  the minute-of-hour to represent, validated from 0 to 59
   * @param second  the second-of-minute to represent, validated from 0 to 59
   * @param nanoOfSecond  the nano-of-second to represent, validated from 0 to 999,999,999
   */


  /**
   * Checks if the specified field is supported.
   * <p>
   * This checks if this time can be queried for the specified field.
   * If false, then calling the {@link #range(TemporalField) range},
   * {@link #get(TemporalField) get} and {@link #with(TemporalField, long)}
   * methods will throw an exception.
   * <p>
   * If the field is a {@link ChronoField} then the query is implemented here.
   * The supported fields are:
   * <ul>
   * <li>{@code NANO_OF_SECOND}
   * <li>{@code NANO_OF_DAY}
   * <li>{@code MICRO_OF_SECOND}
   * <li>{@code MICRO_OF_DAY}
   * <li>{@code MILLI_OF_SECOND}
   * <li>{@code MILLI_OF_DAY}
   * <li>{@code SECOND_OF_MINUTE}
   * <li>{@code SECOND_OF_DAY}
   * <li>{@code MINUTE_OF_HOUR}
   * <li>{@code MINUTE_OF_DAY}
   * <li>{@code HOUR_OF_AMPM}
   * <li>{@code CLOCK_HOUR_OF_AMPM}
   * <li>{@code HOUR_OF_DAY}
   * <li>{@code CLOCK_HOUR_OF_DAY}
   * <li>{@code AMPM_OF_DAY}
   * </ul>
   * All other {@code ChronoField} instances will  false.
   * <p>
   * If the field is not a {@code ChronoField}, then the result of this method
   * is obtained by invoking {@code TemporalField.isSupportedBy(TemporalAccessor)}
   * passing {@code this} as the argument.
   * Whether the field is supported is determined by the field.
   *
   * @param field  the field to check, null returns false
   * @return true if the field is supported on this time, false if not
   */
  def isSupported(field: TemporalField): Boolean = {
    if (field.isInstanceOf[ChronoField]) {
      field.isTimeBased
    }
    field != null && field.isSupportedBy(this)
  }

  /**
   * Checks if the specified unit is supported.
   * <p>
   * This checks if the specified unit can be added to, or subtracted from, this date-time.
   * If false, then calling the {@link #plus(long, TemporalUnit)} and
   * {@link #minus(long, TemporalUnit) minus} methods will throw an exception.
   * <p>
   * If the unit is a {@link ChronoUnit} then the query is implemented here.
   * The supported units are:
   * <ul>
   * <li>{@code NANOS}
   * <li>{@code MICROS}
   * <li>{@code MILLIS}
   * <li>{@code SECONDS}
   * <li>{@code MINUTES}
   * <li>{@code HOURS}
   * <li>{@code HALF_DAYS}
   * </ul>
   * All other {@code ChronoUnit} instances will  false.
   * <p>
   * If the unit is not a {@code ChronoUnit}, then the result of this method
   * is obtained by invoking {@code TemporalUnit.isSupportedBy(Temporal)}
   * passing {@code this} as the argument.
   * Whether the unit is supported is determined by the unit.
   *
   * @param unit  the unit to check, null returns false
   * @return true if the unit can be added/subtracted, false if not
   */
  def isSupported(unit: TemporalUnit): Boolean = {
    if (unit.isInstanceOf[ChronoUnit]) {
      unit.isTimeBased
    }
    unit != null && unit.isSupportedBy(this)
  }

  /**
   * Gets the range of valid values for the specified field.
   * <p>
   * The range object expresses the minimum and maximum valid values for a field.
   * This time is used to enhance the accuracy of the returned range.
   * If it is not possible to  the range, because the field is not supported
   * or for some other reason, an exception is thrown.
   * <p>
   * If the field is a {@link ChronoField} then the query is implemented here.
   * The {@link #isSupported(TemporalField) supported fields} will
   * appropriate range instances.
   * All other {@code ChronoField} instances will throw an {@code UnsupportedTemporalTypeException}.
   * <p>
   * If the field is not a {@code ChronoField}, then the result of this method
   * is obtained by invoking {@code TemporalField.rangeRefinedBy(TemporalAccessor)}
   * passing {@code this} as the argument.
   * Whether the range can be obtained is determined by the field.
   *
   * @param field  the field to query the range for, not null
   * @return the range of valid values for the field, not null
   * @throws DateTimeException if the range for the field cannot be obtained
   * @throws UnsupportedTemporalTypeException if the field is not supported
   */
  override def range(field: TemporalField): ValueRange = {
    Temporal.super.range(field)
  }

  /**
   * Gets the value of the specified field from this time as an {@code int}.
   * <p>
   * This queries this time for the value for the specified field.
   * The returned value will always be within the valid range of values for the field.
   * If it is not possible to  the value, because the field is not supported
   * or for some other reason, an exception is thrown.
   * <p>
   * If the field is a {@link ChronoField} then the query is implemented here.
   * The {@link #isSupported(TemporalField) supported fields} will  valid
   * values based on this time, except {@code NANO_OF_DAY} and {@code MICRO_OF_DAY}
   * which are too large to fit in an {@code int} and throw a {@code DateTimeException}.
   * All other {@code ChronoField} instances will throw an {@code UnsupportedTemporalTypeException}.
   * <p>
   * If the field is not a {@code ChronoField}, then the result of this method
   * is obtained by invoking {@code TemporalField.getFrom(TemporalAccessor)}
   * passing {@code this} as the argument. Whether the value can be obtained,
   * and what the value represents, is determined by the field.
   *
   * @param field  the field to get, not null
   * @return the value for the field
   * @throws DateTimeException if a value for the field cannot be obtained or
   *                           the value is outside the range of valid values for the field
   * @throws UnsupportedTemporalTypeException if the field is not supported or
   *                                          the range of values exceeds an { @code int}
   * @throws ArithmeticException if numeric overflow occurs
   */
  override def get(field: TemporalField): Int = {
    if (field.isInstanceOf[ChronoField]) {
      get0(field)
    }
    Temporal.super.get(field)
  }

  /**
   * Gets the value of the specified field from this time as a {@code long}.
   * <p>
   * This queries this time for the value for the specified field.
   * If it is not possible to  the value, because the field is not supported
   * or for some other reason, an exception is thrown.
   * <p>
   * If the field is a {@link ChronoField} then the query is implemented here.
   * The {@link #isSupported(TemporalField) supported fields} will  valid
   * values based on this time.
   * All other {@code ChronoField} instances will throw an {@code UnsupportedTemporalTypeException}.
   * <p>
   * If the field is not a {@code ChronoField}, then the result of this method
   * is obtained by invoking {@code TemporalField.getFrom(TemporalAccessor)}
   * passing {@code this} as the argument. Whether the value can be obtained,
   * and what the value represents, is determined by the field.
   *
   * @param field  the field to get, not null
   * @return the value for the field
   * @throws DateTimeException if a value for the field cannot be obtained
   * @throws UnsupportedTemporalTypeException if the field is not supported
   * @throws ArithmeticException if numeric overflow occurs
   */
  def getLong(field: TemporalField): Long = {
    if (field.isInstanceOf[ChronoField]) {
      if (field eq NANO_OF_DAY) {
        toNanoOfDay
      }
      if (field eq MICRO_OF_DAY) {
        toNanoOfDay / 1000
      }
      get0(field)
    }
    field.getFrom(this)
  }

  private def get0(field: TemporalField): Int = {
    field.asInstanceOf[ChronoField] match {
      case NANO_OF_SECOND =>
        nano
      case NANO_OF_DAY =>
        throw new UnsupportedTemporalTypeException("Invalid field 'NanoOfDay' for get() method, use getLong() instead")
      case MICRO_OF_SECOND =>
        nano / 1000
      case MICRO_OF_DAY =>
        throw new UnsupportedTemporalTypeException("Invalid field 'MicroOfDay' for get() method, use getLong() instead")
      case MILLI_OF_SECOND =>
        nano / 1000 _000
      case MILLI_OF_DAY =>
        (toNanoOfDay / 1000 _000).asInstanceOf[Int]
      case SECOND_OF_MINUTE =>
        second
      case SECOND_OF_DAY =>
        toSecondOfDay
      case MINUTE_OF_HOUR =>
        minute
      case MINUTE_OF_DAY =>
        hour * 60 + minute
      case HOUR_OF_AMPM =>
        hour % 12
      case CLOCK_HOUR_OF_AMPM =>
        val ham: Int = hour % 12
        (if (ham % 12 == 0) 12 else ham)
      case HOUR_OF_DAY =>
        hour
      case CLOCK_HOUR_OF_DAY =>
        (if (hour == 0) 24 else hour)
      case AMPM_OF_DAY =>
        hour / 12
    }
    throw new UnsupportedTemporalTypeException("Unsupported field: " + field)
  }

  /**
   * Gets the hour-of-day field.
   *
   * @return the hour-of-day, from 0 to 23
   */
  def getHour: Int = {
    hour
  }

  /**
   * Gets the minute-of-hour field.
   *
   * @return the minute-of-hour, from 0 to 59
   */
  def getMinute: Int = {
    minute
  }

  /**
   * Gets the second-of-minute field.
   *
   * @return the second-of-minute, from 0 to 59
   */
  def getSecond: Int = {
    second
  }

  /**
   * Gets the nano-of-second field.
   *
   * @return the nano-of-second, from 0 to 999,999,999
   */
  def getNano: Int = {
    nano
  }

  /**
   * Returns an adjusted copy of this time.
   * <p>
   * This returns a {@code Time}, based on this one, with the time adjusted.
   * The adjustment takes place using the specified adjuster strategy object.
   * Read the documentation of the adjuster to understand what adjustment will be made.
   * <p>
   * A simple adjuster might simply set the one of the fields, such as the hour field.
   * A more complex adjuster might set the time to the last hour of the day.
   * <p>
   * The result of this method is obtained by invoking the
   * {@link TemporalAdjuster#adjustInto(Temporal)} method on the
   * specified adjuster passing {@code this} as the argument.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param adjuster the adjuster to use, not null
   * @return a { @code Time} based on { @code this} with the adjustment made, not null
   * @throws DateTimeException if the adjustment cannot be made
   * @throws ArithmeticException if numeric overflow occurs
   */
  override def `with`(adjuster: TemporalAdjuster): Time = {
    if (adjuster.isInstanceOf[Time]) {
      adjuster.asInstanceOf[Time]
    }
    adjuster.adjustInto(this).asInstanceOf[Time]
  }

  /**
   * Returns a copy of this time with the specified field set to a new value.
   * <p>
   * This returns a {@code Time}, based on this one, with the value
   * for the specified field changed.
   * This can be used to change any supported field, such as the hour, minute or second.
   * If it is not possible to set the value, because the field is not supported or for
   * some other reason, an exception is thrown.
   * <p>
   * If the field is a {@link ChronoField} then the adjustment is implemented here.
   * The supported fields behave as follows:
   * <ul>
   * <li>{@code NANO_OF_SECOND} -
   * Returns a {@code Time} with the specified nano-of-second.
   * The hour, minute and second will be unchanged.
   * <li>{@code NANO_OF_DAY} -
   * Returns a {@code Time} with the specified nano-of-day.
   * This completely replaces the time and is equivalent to {@link #ofNanoOfDay(long)}.
   * <li>{@code MICRO_OF_SECOND} -
   * Returns a {@code Time} with the nano-of-second replaced by the specified
   * micro-of-second multiplied by 1,000.
   * The hour, minute and second will be unchanged.
   * <li>{@code MICRO_OF_DAY} -
   * Returns a {@code Time} with the specified micro-of-day.
   * This completely replaces the time and is equivalent to using {@link #ofNanoOfDay(long)}
   * with the micro-of-day multiplied by 1,000.
   * <li>{@code MILLI_OF_SECOND} -
   * Returns a {@code Time} with the nano-of-second replaced by the specified
   * milli-of-second multiplied by 1,000,000.
   * The hour, minute and second will be unchanged.
   * <li>{@code MILLI_OF_DAY} -
   * Returns a {@code Time} with the specified milli-of-day.
   * This completely replaces the time and is equivalent to using {@link #ofNanoOfDay(long)}
   * with the milli-of-day multiplied by 1,000,000.
   * <li>{@code SECOND_OF_MINUTE} -
   * Returns a {@code Time} with the specified second-of-minute.
   * The hour, minute and nano-of-second will be unchanged.
   * <li>{@code SECOND_OF_DAY} -
   * Returns a {@code Time} with the specified second-of-day.
   * The nano-of-second will be unchanged.
   * <li>{@code MINUTE_OF_HOUR} -
   * Returns a {@code Time} with the specified minute-of-hour.
   * The hour, second-of-minute and nano-of-second will be unchanged.
   * <li>{@code MINUTE_OF_DAY} -
   * Returns a {@code Time} with the specified minute-of-day.
   * The second-of-minute and nano-of-second will be unchanged.
   * <li>{@code HOUR_OF_AMPM} -
   * Returns a {@code Time} with the specified hour-of-am-pm.
   * The AM/PM, minute-of-hour, second-of-minute and nano-of-second will be unchanged.
   * <li>{@code CLOCK_HOUR_OF_AMPM} -
   * Returns a {@code Time} with the specified clock-hour-of-am-pm.
   * The AM/PM, minute-of-hour, second-of-minute and nano-of-second will be unchanged.
   * <li>{@code HOUR_OF_DAY} -
   * Returns a {@code Time} with the specified hour-of-day.
   * The minute-of-hour, second-of-minute and nano-of-second will be unchanged.
   * <li>{@code CLOCK_HOUR_OF_DAY} -
   * Returns a {@code Time} with the specified clock-hour-of-day.
   * The minute-of-hour, second-of-minute and nano-of-second will be unchanged.
   * <li>{@code AMPM_OF_DAY} -
   * Returns a {@code Time} with the specified AM/PM.
   * The hour-of-am-pm, minute-of-hour, second-of-minute and nano-of-second will be unchanged.
   * </ul>
   * <p>
   * In all cases, if the new value is outside the valid range of values for the field
   * then a {@code DateTimeException} will be thrown.
   * <p>
   * All other {@code ChronoField} instances will throw an {@code UnsupportedTemporalTypeException}.
   * <p>
   * If the field is not a {@code ChronoField}, then the result of this method
   * is obtained by invoking {@code TemporalField.adjustInto(Temporal, long)}
   * passing {@code this} as the argument. In this case, the field determines
   * whether and how to adjust the instant.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param field  the field to set in the result, not null
   * @param newValue  the new value of the field in the result
   * @return a { @code Time} based on { @code this} with the specified field set, not null
   * @throws DateTimeException if the field cannot be set
   * @throws UnsupportedTemporalTypeException if the field is not supported
   * @throws ArithmeticException if numeric overflow occurs
   */
  def `with`(field: TemporalField, newValue: Long): Time = {
    if (field.isInstanceOf[ChronoField]) {
      val f: ChronoField = field.asInstanceOf[ChronoField]
      f.checkValidValue(newValue)
      f match {
        case NANO_OF_SECOND =>
          withNano(newValue.asInstanceOf[Int])
        case NANO_OF_DAY =>
          Time.ofNanoOfDay(newValue)
        case MICRO_OF_SECOND =>
          withNano(newValue.asInstanceOf[Int] * 1000)
        case MICRO_OF_DAY =>
          plusNanos((newValue - toNanoOfDay / 1000) * 1000)
        case MILLI_OF_SECOND =>
          withNano(newValue.asInstanceOf[Int] * 1000 _000)
        case MILLI_OF_DAY =>
          plusNanos((newValue - toNanoOfDay / 1000 _000) * 1000 _000)
        case SECOND_OF_MINUTE =>
          withSecond(newValue.asInstanceOf[Int])
        case SECOND_OF_DAY =>
          plusSeconds(newValue - toSecondOfDay)
        case MINUTE_OF_HOUR =>
          withMinute(newValue.asInstanceOf[Int])
        case MINUTE_OF_DAY =>
          plusMinutes(newValue - (hour * 60 + minute))
        case HOUR_OF_AMPM =>
          plusHours(newValue - (hour % 12))
        case CLOCK_HOUR_OF_AMPM =>
          plusHours((if (newValue == 12) 0 else newValue) - (hour % 12))
        case HOUR_OF_DAY =>
          withHour(newValue.asInstanceOf[Int])
        case CLOCK_HOUR_OF_DAY =>
          withHour((if (newValue == 24) 0 else newValue).asInstanceOf[Int])
        case AMPM_OF_DAY =>
          plusHours((newValue - (hour / 12)) * 12)
      }
      throw new UnsupportedTemporalTypeException("Unsupported field: " + field)
    }
    field.adjustInto(this, newValue)
  }

  /**
   * Returns a copy of this {@code Time} with the hour-of-day value altered.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param hour  the hour-of-day to set in the result, from 0 to 23
   * @return a { @code Time} based on this time with the requested hour, not null
   * @throws DateTimeException if the hour value is invalid
   */
  def withHour(hour: Int): Time = {
    if (this.hour == hour) {
      this
    }
    HOUR_OF_DAY.checkValidValue(hour)
    create(hour, minute, second, nano)
  }

  /**
   * Returns a copy of this {@code Time} with the minute-of-hour value altered.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param minute  the minute-of-hour to set in the result, from 0 to 59
   * @return a { @code Time} based on this time with the requested minute, not null
   * @throws DateTimeException if the minute value is invalid
   */
  def withMinute(minute: Int): Time = {
    if (this.minute == minute) {
      this
    }
    MINUTE_OF_HOUR.checkValidValue(minute)
    create(hour, minute, second, nano)
  }

  /**
   * Returns a copy of this {@code Time} with the second-of-minute value altered.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param second  the second-of-minute to set in the result, from 0 to 59
   * @return a { @code Time} based on this time with the requested second, not null
   * @throws DateTimeException if the second value is invalid
   */
  def withSecond(second: Int): Time = {
    if (this.second == second) {
      this
    }
    SECOND_OF_MINUTE.checkValidValue(second)
    create(hour, minute, second, nano)
  }

  /**
   * Returns a copy of this {@code Time} with the nano-of-second value altered.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param nanoOfSecond  the nano-of-second to set in the result, from 0 to 999,999,999
   * @return a { @code Time} based on this time with the requested nanosecond, not null
   * @throws DateTimeException if the nanos value is invalid
   */
  def withNano(nanoOfSecond: Int): Time = {
    if (this.nano == nanoOfSecond) {
      this
    }
    NANO_OF_SECOND.checkValidValue(nanoOfSecond)
    create(hour, minute, second, nanoOfSecond)
  }

  /**
   * Returns a copy of this {@code Time} with the time truncated.
   * <p>
   * Truncating the time returns a copy of the original time with fields
   * smaller than the specified unit set to zero.
   * For example, truncating with the {@link ChronoUnit#MINUTES minutes} unit
   * will set the second-of-minute and nano-of-second field to zero.
   * <p>
   * The unit must have a {@linkplain TemporalUnit#getDuration() duration}
   * that divides into the length of a standard day without remainder.
   * This includes all supplied time units on {@link ChronoUnit} and
   * {@link ChronoUnit#DAYS DAYS}. Other units throw an exception.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param unit  the unit to truncate to, not null
   * @return a { @code Time} based on this time with the time truncated, not null
   * @throws DateTimeException if unable to truncate
   * @throws UnsupportedTemporalTypeException if the unit is not supported
   */
  def truncatedTo(unit: TemporalUnit): Time = {
    if (unit eq ChronoUnit.NANOS) {
      this
    }
    val unitDur: Duration = unit.getDuration
    if (unitDur.getSeconds > SECONDS_PER_DAY) {
      throw new UnsupportedTemporalTypeException("Unit is too large to be used for truncation")
    }
    val dur: Long = unitDur.toNanos
    if ((NANOS_PER_DAY % dur) != 0) {
      throw new UnsupportedTemporalTypeException("Unit must divide into a standard day without remainder")
    }
    val nod: Long = toNanoOfDay
    ofNanoOfDay((nod / dur) * dur)
  }

  /**
   * Returns a copy of this time with the specified amount added.
   * <p>
   * This returns a {@code Time}, based on this one, with the specified amount added.
   * The amount is typically {@link Duration} but may be any other type implementing
   * the {@link TemporalAmount} interface.
   * <p>
   * The calculation is delegated to the amount object by calling
   * {@link TemporalAmount#addTo(Temporal)}. The amount implementation is free
   * to implement the addition in any way it wishes, however it typically
   * calls back to {@link #plus(long, TemporalUnit)}. Consult the documentation
   * of the amount implementation to determine if it can be successfully added.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param amountToAdd  the amount to add, not null
   * @return a { @code Time} based on this time with the addition made, not null
   * @throws DateTimeException if the addition cannot be made
   * @throws ArithmeticException if numeric overflow occurs
   */
  override def plus(amountToAdd: TemporalAmount): Time = {
    amountToAdd.addTo(this).asInstanceOf[Time]
  }

  /**
   * Returns a copy of this time with the specified amount added.
   * <p>
   * This returns a {@code Time}, based on this one, with the amount
   * in terms of the unit added. If it is not possible to add the amount, because the
   * unit is not supported or for some other reason, an exception is thrown.
   * <p>
   * If the field is a {@link ChronoUnit} then the addition is implemented here.
   * The supported fields behave as follows:
   * <ul>
   * <li>{@code NANOS} -
   * Returns a {@code Time} with the specified number of nanoseconds added.
   * This is equivalent to {@link #plusNanos(long)}.
   * <li>{@code MICROS} -
   * Returns a {@code Time} with the specified number of microseconds added.
   * This is equivalent to {@link #plusNanos(long)} with the amount
   * multiplied by 1,000.
   * <li>{@code MILLIS} -
   * Returns a {@code Time} with the specified number of milliseconds added.
   * This is equivalent to {@link #plusNanos(long)} with the amount
   * multiplied by 1,000,000.
   * <li>{@code SECONDS} -
   * Returns a {@code Time} with the specified number of seconds added.
   * This is equivalent to {@link #plusSeconds(long)}.
   * <li>{@code MINUTES} -
   * Returns a {@code Time} with the specified number of minutes added.
   * This is equivalent to {@link #plusMinutes(long)}.
   * <li>{@code HOURS} -
   * Returns a {@code Time} with the specified number of hours added.
   * This is equivalent to {@link #plusHours(long)}.
   * <li>{@code HALF_DAYS} -
   * Returns a {@code Time} with the specified number of half-days added.
   * This is equivalent to {@link #plusHours(long)} with the amount
   * multiplied by 12.
   * </ul>
   * <p>
   * All other {@code ChronoUnit} instances will throw an {@code UnsupportedTemporalTypeException}.
   * <p>
   * If the field is not a {@code ChronoUnit}, then the result of this method
   * is obtained by invoking {@code TemporalUnit.addTo(Temporal, long)}
   * passing {@code this} as the argument. In this case, the unit determines
   * whether and how to perform the addition.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param amountToAdd  the amount of the unit to add to the result, may be negative
   * @param unit  the unit of the amount to add, not null
   * @return a { @code Time} based on this time with the specified amount added, not null
   * @throws DateTimeException if the addition cannot be made
   * @throws UnsupportedTemporalTypeException if the unit is not supported
   * @throws ArithmeticException if numeric overflow occurs
   */
  def plus(amountToAdd: Long, unit: TemporalUnit): Time = {
    if (unit.isInstanceOf[ChronoUnit]) {
      unit.asInstanceOf[ChronoUnit] match {
        case NANOS =>
          plusNanos(amountToAdd)
        case MICROS =>
          plusNanos((amountToAdd % MICROS_PER_DAY) * 1000)
        case MILLIS =>
          plusNanos((amountToAdd % MILLIS_PER_DAY) * 1000 _000)
        case SECONDS =>
          plusSeconds(amountToAdd)
        case MINUTES =>
          plusMinutes(amountToAdd)
        case HOURS =>
          plusHours(amountToAdd)
        case HALF_DAYS =>
          plusHours((amountToAdd % 2) * 12)
      }
      throw new UnsupportedTemporalTypeException("Unsupported unit: " + unit)
    }
    unit.addTo(this, amountToAdd)
  }

  /**
   * Returns a copy of this {@code Time} with the specified period in hours added.
   * <p>
   * This adds the specified number of hours to this time, returning a new time.
   * The calculation wraps around midnight.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param hoursToAdd  the hours to add, may be negative
   * @return a { @code Time} based on this time with the hours added, not null
   */
  def plusHours(hoursToAdd: Long): Time = {
    if (hoursToAdd == 0) {
      this
    }
    val newHour: Int = ((hoursToAdd % HOURS_PER_DAY).asInstanceOf[Int] + hour + HOURS_PER_DAY) % HOURS_PER_DAY
    create(newHour, minute, second, nano)
  }

  /**
   * Returns a copy of this {@code Time} with the specified period in minutes added.
   * <p>
   * This adds the specified number of minutes to this time, returning a new time.
   * The calculation wraps around midnight.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param minutesToAdd  the minutes to add, may be negative
   * @return a { @code Time} based on this time with the minutes added, not null
   */
  def plusMinutes(minutesToAdd: Long): Time = {
    if (minutesToAdd == 0) {
      this
    }
    val mofd: Int = hour * MINUTES_PER_HOUR + minute
    val newMofd: Int = ((minutesToAdd % MINUTES_PER_DAY).asInstanceOf[Int] + mofd + MINUTES_PER_DAY) % MINUTES_PER_DAY
    if (mofd == newMofd) {
      this
    }
    val newHour: Int = newMofd / MINUTES_PER_HOUR
    val newMinute: Int = newMofd % MINUTES_PER_HOUR
    create(newHour, newMinute, second, nano)
  }

  /**
   * Returns a copy of this {@code Time} with the specified period in seconds added.
   * <p>
   * This adds the specified number of seconds to this time, returning a new time.
   * The calculation wraps around midnight.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param secondstoAdd  the seconds to add, may be negative
   * @return a { @code Time} based on this time with the seconds added, not null
   */
  def plusSeconds(secondstoAdd: Long): Time = {
    if (secondstoAdd == 0) {
      this
    }
    val sofd: Int = hour * SECONDS_PER_HOUR + minute * SECONDS_PER_MINUTE + second
    val newSofd: Int = ((secondstoAdd % SECONDS_PER_DAY).asInstanceOf[Int] + sofd + SECONDS_PER_DAY) % SECONDS_PER_DAY
    if (sofd == newSofd) {
      this
    }
    val newHour: Int = newSofd / SECONDS_PER_HOUR
    val newMinute: Int = (newSofd / SECONDS_PER_MINUTE) % MINUTES_PER_HOUR
    val newSecond: Int = newSofd % SECONDS_PER_MINUTE
    create(newHour, newMinute, newSecond, nano)
  }

  /**
   * Returns a copy of this {@code Time} with the specified period in nanoseconds added.
   * <p>
   * This adds the specified number of nanoseconds to this time, returning a new time.
   * The calculation wraps around midnight.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param nanosToAdd  the nanos to add, may be negative
   * @return a { @code Time} based on this time with the nanoseconds added, not null
   */
  def plusNanos(nanosToAdd: Long): Time = {
    if (nanosToAdd == 0) {
      this
    }
    val nofd: Long = toNanoOfDay
    val newNofd: Long = ((nanosToAdd % NANOS_PER_DAY) + nofd + NANOS_PER_DAY) % NANOS_PER_DAY
    if (nofd == newNofd) {
      this
    }
    val newHour: Int = (newNofd / NANOS_PER_HOUR).asInstanceOf[Int]
    val newMinute: Int = ((newNofd / NANOS_PER_MINUTE) % MINUTES_PER_HOUR).asInstanceOf[Int]
    val newSecond: Int = ((newNofd / NANOS_PER_SECOND) % SECONDS_PER_MINUTE).asInstanceOf[Int]
    val newNano: Int = (newNofd % NANOS_PER_SECOND).asInstanceOf[Int]
    create(newHour, newMinute, newSecond, newNano)
  }

  /**
   * Returns a copy of this time with the specified amount subtracted.
   * <p>
   * This returns a {@code Time}, based on this one, with the specified amount subtracted.
   * The amount is typically {@link Duration} but may be any other type implementing
   * the {@link TemporalAmount} interface.
   * <p>
   * The calculation is delegated to the amount object by calling
   * {@link TemporalAmount#subtractFrom(Temporal)}. The amount implementation is free
   * to implement the subtraction in any way it wishes, however it typically
   * calls back to {@link #minus(long, TemporalUnit)}. Consult the documentation
   * of the amount implementation to determine if it can be successfully subtracted.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param amountToSubtract  the amount to subtract, not null
   * @return a { @code Time} based on this time with the subtraction made, not null
   * @throws DateTimeException if the subtraction cannot be made
   * @throws ArithmeticException if numeric overflow occurs
   */
  override def minus(amountToSubtract: TemporalAmount): Time = {
    amountToSubtract.subtractFrom(this).asInstanceOf[Time]
  }

  /**
   * Returns a copy of this time with the specified amount subtracted.
   * <p>
   * This returns a {@code Time}, based on this one, with the amount
   * in terms of the unit subtracted. If it is not possible to subtract the amount,
   * because the unit is not supported or for some other reason, an exception is thrown.
   * <p>
   * This method is equivalent to {@link #plus(long, TemporalUnit)} with the amount negated.
   * See that method for a full description of how addition, and thus subtraction, works.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param amountToSubtract  the amount of the unit to subtract from the result, may be negative
   * @param unit  the unit of the amount to subtract, not null
   * @return a { @code Time} based on this time with the specified amount subtracted, not null
   * @throws DateTimeException if the subtraction cannot be made
   * @throws UnsupportedTemporalTypeException if the unit is not supported
   * @throws ArithmeticException if numeric overflow occurs
   */
  override def minus(amountToSubtract: Long, unit: TemporalUnit): Time = {
    (if (amountToSubtract == Long.MIN_VALUE) plus(Long.MAX_VALUE, unit).plus(1, unit) else plus(-amountToSubtract, unit))
  }

  /**
   * Returns a copy of this {@code Time} with the specified period in hours subtracted.
   * <p>
   * This subtracts the specified number of hours from this time, returning a new time.
   * The calculation wraps around midnight.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param hoursToSubtract  the hours to subtract, may be negative
   * @return a { @code Time} based on this time with the hours subtracted, not null
   */
  def minusHours(hoursToSubtract: Long): Time = {
    plusHours(-(hoursToSubtract % HOURS_PER_DAY))
  }

  /**
   * Returns a copy of this {@code Time} with the specified period in minutes subtracted.
   * <p>
   * This subtracts the specified number of minutes from this time, returning a new time.
   * The calculation wraps around midnight.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param minutesToSubtract  the minutes to subtract, may be negative
   * @return a { @code Time} based on this time with the minutes subtracted, not null
   */
  def minusMinutes(minutesToSubtract: Long): Time = {
    plusMinutes(-(minutesToSubtract % MINUTES_PER_DAY))
  }

  /**
   * Returns a copy of this {@code Time} with the specified period in seconds subtracted.
   * <p>
   * This subtracts the specified number of seconds from this time, returning a new time.
   * The calculation wraps around midnight.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param secondsToSubtract  the seconds to subtract, may be negative
   * @return a { @code Time} based on this time with the seconds subtracted, not null
   */
  def minusSeconds(secondsToSubtract: Long): Time = {
    plusSeconds(-(secondsToSubtract % SECONDS_PER_DAY))
  }

  /**
   * Returns a copy of this {@code Time} with the specified period in nanoseconds subtracted.
   * <p>
   * This subtracts the specified number of nanoseconds from this time, returning a new time.
   * The calculation wraps around midnight.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param nanosToSubtract  the nanos to subtract, may be negative
   * @return a { @code Time} based on this time with the nanoseconds subtracted, not null
   */
  def minusNanos(nanosToSubtract: Long): Time = {
    plusNanos(-(nanosToSubtract % NANOS_PER_DAY))
  }

  /**
   * Queries this time using the specified query.
   * <p>
   * This queries this time using the specified query strategy object.
   * The {@code TemporalQuery} object defines the logic to be used to
   * obtain the result. Read the documentation of the query to understand
   * what the result of this method will be.
   * <p>
   * The result of this method is obtained by invoking the
   * {@link TemporalQuery#queryFrom(TemporalAccessor)} method on the
   * specified query passing {@code this} as the argument.
   *
   * @param <R> the type of the result
   * @param query  the query to invoke, not null
   * @return the query result, null may be returned (defined by the query)
   * @throws DateTimeException if unable to query (defined by the query)
   * @throws ArithmeticException if numeric overflow occurs (defined by the query)
   */
  override def query(query: TemporalQuery[R]): R = {
    if (query eq TemporalQuery.chronology || query eq TemporalQuery.zoneId || query eq TemporalQuery.zone || query eq TemporalQuery.offset) {
      null
    }
    else if (query eq TemporalQuery.localTime) {
      this.asInstanceOf[R]
    }
    else if (query eq TemporalQuery.localDate) {
      null
    }
    else if (query eq TemporalQuery.precision) {
      NANOS.asInstanceOf[R]
    }
    query.queryFrom(this)
  }

  /**
   * Adjusts the specified temporal object to have the same time as this object.
   * <p>
   * This returns a temporal object of the same observable type as the input
   * with the time changed to be the same as this.
   * <p>
   * The adjustment is equivalent to using {@link Temporal#with(TemporalField, long)}
   * passing {@link ChronoField#NANO_OF_DAY} as the field.
   * <p>
   * In most cases, it is clearer to reverse the calling pattern by using
   * {@link Temporal#with(TemporalAdjuster)}:
   * {{{
   * // these two lines are equivalent, but the second approach is recommended
   * temporal = thisLocalTime.adjustInto(temporal);
   * temporal = temporal.with(thisLocalTime);
   * }}}
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param temporal  the target object to be adjusted, not null
   * @return the adjusted object, not null
   * @throws DateTimeException if unable to make the adjustment
   * @throws ArithmeticException if numeric overflow occurs
   */
  def adjustInto(temporal: Temporal): Temporal = {
    temporal.`with`(NANO_OF_DAY, toNanoOfDay)
  }

  /**
   * Calculates the amount of time until another time in terms of the specified unit.
   * <p>
   * This calculates the amount of time between two {@code Time}
   * objects in terms of a single {@code TemporalUnit}.
   * The start and end points are {@code this} and the specified time.
   * The result will be negative if the end is before the start.
   * The {@code Temporal} passed to this method is converted to a
   * {@code Time} using {@link #from(TemporalAccessor)}.
   * For example, the amount in hours between two times can be calculated
   * using {@code startTime.until(endTime, HOURS)}.
   * <p>
   * The calculation returns a whole number, representing the number of
   * complete units between the two times.
   * For example, the amount in hours between 11:30 and 13:29 will only
   * be one hour as it is one minute short of two hours.
   * <p>
   * There are two equivalent ways of using this method.
   * The first is to invoke this method.
   * The second is to use {@link TemporalUnit#between(Temporal, Temporal)}:
   * {{{
   * // these two lines are equivalent
   * amount = start.until(end, MINUTES);
   * amount = MINUTES.between(start, end);
   * }}}
   * The choice should be made based on which makes the code more readable.
   * <p>
   * The calculation is implemented in this method for {@link ChronoUnit}.
   * The units {@code NANOS}, {@code MICROS}, {@code MILLIS}, {@code SECONDS},
   * {@code MINUTES}, {@code HOURS} and {@code HALF_DAYS} are supported.
   * Other {@code ChronoUnit} values will throw an exception.
   * <p>
   * If the unit is not a {@code ChronoUnit}, then the result of this method
   * is obtained by invoking {@code TemporalUnit.between(Temporal, Temporal)}
   * passing {@code this} as the first argument and the converted input temporal
   * as the second argument.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param endExclusive  the end time, which is converted to a { @code Time}, not null
   * @param unit  the unit to measure the amount in, not null
   * @return the amount of time between this time and the end time
   * @throws DateTimeException if the amount cannot be calculated, or the end
   *                           temporal cannot be converted to a { @code Time}
   * @throws UnsupportedTemporalTypeException if the unit is not supported
   * @throws ArithmeticException if numeric overflow occurs
   */
  def until(endExclusive: Temporal, unit: TemporalUnit): Long = {
    val end: Time = Time.from(endExclusive)
    if (unit.isInstanceOf[ChronoUnit]) {
      val nanosUntil: Long = end.toNanoOfDay - toNanoOfDay
      unit.asInstanceOf[ChronoUnit] match {
        case NANOS =>
          nanosUntil
        case MICROS =>
          nanosUntil / 1000
        case MILLIS =>
          nanosUntil / 1000 _000
        case SECONDS =>
          nanosUntil / NANOS_PER_SECOND
        case MINUTES =>
          nanosUntil / NANOS_PER_MINUTE
        case HOURS =>
          nanosUntil / NANOS_PER_HOUR
        case HALF_DAYS =>
          nanosUntil / (12 * NANOS_PER_HOUR)
      }
      throw new UnsupportedTemporalTypeException("Unsupported unit: " + unit)
    }
    unit.between(this, end)
  }

  /**
   * Formats this time using the specified formatter.
   * <p>
   * This time will be passed to the formatter to produce a string.
   *
   * @param formatter  the formatter to use, not null
   * @return the formatted time string, not null
   * @throws DateTimeException if an error occurs during printing
   */
  def format(formatter: DateTimeFormatter): String = {
    object
    formatter.format(this)
  }

  /**
   * Combines this time with a date to create a {@code DateTime}.
   * <p>
   * This returns a {@code DateTime} formed from this time at the specified date.
   * All possible combinations of date and time are valid.
   *
   * @param date  the date to combine with, not null
   * @return the local date-time formed from this time and the specified date, not null
   */
  def atDate(date: Date): DateTime = {
    DateTime.of(date, this)
  }

  /**
   * Combines this time with an offset to create an {@code OffsetTime}.
   * <p>
   * This returns an {@code OffsetTime} formed from this time at the specified offset.
   * All possible combinations of time and offset are valid.
   *
   * @param offset  the offset to combine with, not null
   * @return the offset time formed from this time and the specified offset, not null
   */
  def atOffset(offset: ZoneOffset): OffsetTime = {
    OffsetTime.of(this, offset)
  }

  /**
   * Extracts the time as seconds of day,
   * from {@code 0} to {@code 24 * 60 * 60 - 1}.
   *
   * @return the second-of-day equivalent to this time
   */
  def toSecondOfDay: Int = {
    var total: Int = hour * SECONDS_PER_HOUR
    total += minute * SECONDS_PER_MINUTE
    total += second
    total
  }

  /**
   * Extracts the time as nanos of day,
   * from {@code 0} to {@code 24 * 60 * 60 * 1,000,000,000 - 1}.
   *
   * @return the nano of day equivalent to this time
   */
  def toNanoOfDay: Long = {
    var total: Long = hour * NANOS_PER_HOUR
    total += minute * NANOS_PER_MINUTE
    total += second * NANOS_PER_SECOND
    total += nano
    total
  }

  /**
   * Compares this {@code Time} to another time.
   * <p>
   * The comparison is based on the time-line position of the local times within a day.
   * It is "consistent with equals", as defined by {@link Comparable}.
   *
   * @param other  the other time to compare to, not null
   * @return the comparator value, negative if less, positive if greater
   * @throws NullPointerException if { @code other} is null
   */
  def compareTo(other: Time): Int = {
    var cmp: Int = Integer.compare(hour, other.hour)
    if (cmp == 0) {
      cmp = Integer.compare(minute, other.minute)
      if (cmp == 0) {
        cmp = Integer.compare(second, other.second)
        if (cmp == 0) {
          cmp = Integer.compare(nano, other.nano)
        }
      }
    }
    cmp
  }

  /**
   * Checks if this {@code Time} is after the specified time.
   * <p>
   * The comparison is based on the time-line position of the time within a day.
   *
   * @param other  the other time to compare to, not null
   * @return true if this is after the specified time
   * @throws NullPointerException if { @code other} is null
   */
  def isAfter(other: Time): Boolean = {
    compareTo(other) > 0
  }

  /**
   * Checks if this {@code Time} is before the specified time.
   * <p>
   * The comparison is based on the time-line position of the time within a day.
   *
   * @param other  the other time to compare to, not null
   * @return true if this point is before the specified time
   * @throws NullPointerException if { @code other} is null
   */
  def isBefore(other: Time): Boolean = {
    compareTo(other) < 0
  }

  /**
   * Checks if this time is equal to another time.
   * <p>
   * The comparison is based on the time-line position of the time within a day.
   * <p>
   * Only objects of type {@code Time} are compared, other types  false.
   * To compare the date of two {@code TemporalAccessor} instances, use
   * {@link ChronoField#NANO_OF_DAY} as a comparator.
   *
   * @param obj  the object to check, null returns false
   * @return true if this is equal to the other time
   */
  override def equals(obj: AnyRef): Boolean = {
    if (this eq obj) {
      true
    }
    if (obj.isInstanceOf[Time]) {
      val other: Time = obj.asInstanceOf[Time]
      hour == other.hour && minute == other.minute && second == other.second && nano == other.nano
    }
    false
  }

  /**
   * A hash code for this time.
   *
   * @return a suitable hash code
   */
  override def hashCode: Int = {
    val nod: Long = toNanoOfDay
    (nod ^ (nod >>> 32)).asInstanceOf[Int]
  }

  /**
   * Outputs this time as a {@code String}, such as {@code 10:15}.
   * <p>
   * The output will be one of the following ISO-8601 formats:
   * <p><ul>
   * <li>{@code HH:mm}</li>
   * <li>{@code HH:mm:ss}</li>
   * <li>{@code HH:mm:ss.SSS}</li>
   * <li>{@code HH:mm:ss.SSSSSS}</li>
   * <li>{@code HH:mm:ss.SSSSSSSSS}</li>
   * </ul><p>
   * The format used will be the shortest that outputs the full value of
   * the time where the omitted parts are implied to be zero.
   *
   * @return a string representation of this time, not null
   */
  override def toString: String = {
    val buf: StringBuilder = new StringBuilder(18)
    val hourValue: Int = hour
    val minuteValue: Int = minute
    val secondValue: Int = second
    val nanoValue: Int = nano
    buf.append(if (hourValue < 10) "0" else "").append(hourValue).append(if (minuteValue < 10) ":0" else ":").append(minuteValue)
    if (secondValue > 0 || nanoValue > 0) {
      buf.append(if (secondValue < 10) ":0" else ":").append(secondValue)
      if (nanoValue > 0) {
        buf.append('.')
        if (nanoValue % 1000 _000 == 0)
        {
          buf.append(Integer.toString((nanoValue / 1000 _000) + 1000).substring(1))
        }
        else if (nanoValue % 1000 == 0) {
          buf.append(Integer.toString((nanoValue / 1000) + 1000 _000).substring(1))
        }
        else {
          buf.append(Integer.toString((nanoValue) + 1000 _000_000).substring(1))
        }
      }
    }
    buf.toString
  }

  /**
   * Writes the object using a
   * <a href="../../serialized-form.html#java.time.Ser">dedicated serialized form</a>.
   * {{{
   * out.writeByte(4);  // identifies this as a Time
   * if (nano == 0) {
   * if (second == 0) {
   * if (minute == 0) {
   * out.writeByte(~hour);
   * } else {
   * out.writeByte(hour);
   * out.writeByte(~minute);
   * }
   * } else {
   * out.writeByte(hour);
   * out.writeByte(minute);
   * out.writeByte(~second);
   * }
   * } else {
   * out.writeByte(hour);
   * out.writeByte(minute);
   * out.writeByte(second);
   * out.writeInt(nano);
   * }
   * }}}
   *
   * @return the instance of { @code Ser}, not null
   */
  private def writeReplace: AnyRef = {
    new Ser(Ser.LOCAL_TIME_TYPE, this)
  }

  /**
   * Defend against malicious streams.
   * @return never
   * @throws InvalidObjectException always
   */
  private def readResolve: AnyRef = {
    throw new InvalidObjectException("Deserialization via serialization delegate")
  }

  private[time] def writeExternal(out: DataOutput) {
    if (nano == 0) {
      if (second == 0) {
        if (minute == 0) {
          out.writeByte(~hour)
        }
        else {
          out.writeByte(hour)
          out.writeByte(~minute)
        }
      }
      else {
        out.writeByte(hour)
        out.writeByte(minute)
        out.writeByte(~second)
      }
    }
    else {
      out.writeByte(hour)
      out.writeByte(minute)
      out.writeByte(second)
      out.writeInt(nano)
    }
  }

  /**
   * The hour.
   */
  private final val hour: Byte = 0
  /**
   * The minute.
   */
  private final val minute: Byte = 0
  /**
   * The second.
   */
  private final val second: Byte = 0
  /**
   * The nanosecond.
   */
  private final val nano: Int = 0
}



/**
 * A time with an offset from UTC/Greenwich in the ISO-8601 calendar system,
 * such as {@code 10:15:30+01:00}.
 * <p>
 * {@code OffsetTime} is an immutable date-time object that represents a time, often
 * viewed as hour-minute-second-offset.
 * This class stores all time fields, to a precision of nanoseconds,
 * as well as a zone offset.
 * For example, the value "13:45.30.123456789+02:00" can be stored
 * in an {@code OffsetTime}.
 *
 * @implSpec
 * This class is immutable and thread-safe.
 *
 * @since 1.8
 */
object OffsetTime {
  /**
   * Obtains the current time from the system clock in the default time-zone.
   * <p>
   * This will query the {@link java.time.Clock#systemDefaultZone() system clock} in the default
   * time-zone to obtain the current time.
   * The offset will be calculated from the time-zone in the clock.
   * <p>
   * Using this method will prevent the ability to use an alternate clock for testing
   * because the clock is hard-coded.
   *
   * @return the current time using the system clock, not null
   */
  def now: OffsetTime = {
    now(Clock.systemDefaultZone)
  }

  /**
   * Obtains the current time from the system clock in the specified time-zone.
   * <p>
   * This will query the {@link Clock#system(java.time.ZoneId) system clock} to obtain the current time.
   * Specifying the time-zone avoids dependence on the default time-zone.
   * The offset will be calculated from the specified time-zone.
   * <p>
   * Using this method will prevent the ability to use an alternate clock for testing
   * because the clock is hard-coded.
   *
   * @param zone  the zone ID to use, not null
   * @return the current time using the system clock, not null
   */
  def now(zone: ZoneId): OffsetTime = {
    now(Clock.system(zone))
  }

  /**
   * Obtains the current time from the specified clock.
   * <p>
   * This will query the specified clock to obtain the current time.
   * The offset will be calculated from the time-zone in the clock.
   * <p>
   * Using this method allows the use of an alternate clock for testing.
   * The alternate clock may be introduced using {@link Clock dependency injection}.
   *
   * @param clock  the clock to use, not null
   * @return the current time, not null
   */
  def now(clock: Clock): OffsetTime = {
    object
    val now: Instant = clock.instant
    ofInstant(now, clock.getZone.getRules.getOffset(now))
  }

  /**
   * Obtains an instance of {@code OffsetTime} from a local time and an offset.
   *
   * @param time  the local time, not null
   * @param offset  the zone offset, not null
   * @return the offset time, not null
   */
  def of(time: Time, offset: ZoneOffset): OffsetTime = {
    new OffsetTime(time, offset)
  }

  /**
   * Obtains an instance of {@code OffsetTime} from an hour, minute, second and nanosecond.
   * <p>
   * This creates an offset time with the four specified fields.
   * <p>
   * This method exists primarily for writing test cases.
   * Non test-code will typically use other methods to create an offset time.
   * {@code Time} has two additional convenience variants of the
   * equivalent factory method taking fewer arguments.
   * They are not provided here to reduce the footprint of the API.
   *
   * @param hour  the hour-of-day to represent, from 0 to 23
   * @param minute  the minute-of-hour to represent, from 0 to 59
   * @param second  the second-of-minute to represent, from 0 to 59
   * @param nanoOfSecond  the nano-of-second to represent, from 0 to 999,999,999
   * @param offset  the zone offset, not null
   * @return the offset time, not null
   * @throws DateTimeException if the value of any field is out of range
   */
  def of(hour: Int, minute: Int, second: Int, nanoOfSecond: Int, offset: ZoneOffset): OffsetTime = {
    new OffsetTime(Time.of(hour, minute, second, nanoOfSecond), offset)
  }

  /**
   * Obtains an instance of {@code OffsetTime} from an {@code Instant} and zone ID.
   * <p>
   * This creates an offset time with the same instant as that specified.
   * Finding the offset from UTC/Greenwich is simple as there is only one valid
   * offset for each instant.
   * <p>
   * The date component of the instant is dropped during the conversion.
   * This means that the conversion can never fail due to the instant being
   * out of the valid range of dates.
   *
   * @param instant  the instant to create the time from, not null
   * @param zone  the time-zone, which may be an offset, not null
   * @return the offset time, not null
   */
  def ofInstant(instant: Instant, zone: ZoneId): OffsetTime = {
    object
    object
    val rules: ZoneRules = zone.getRules
    val offset: ZoneOffset = rules.getOffset(instant)
    val localSecond: Long = instant.getEpochSecond + offset.getTotalSeconds
    val secsOfDay: Int = Math.floorMod(localSecond, SECONDS_PER_DAY).asInstanceOf[Int]
    val time: Time = Time.ofNanoOfDay(secsOfDay * NANOS_PER_SECOND + instant.getNano)
    new OffsetTime(time, offset)
  }

  /**
   * Obtains an instance of {@code OffsetTime} from a temporal object.
   * <p>
   * This obtains an offset time based on the specified temporal.
   * A {@code TemporalAccessor} represents an arbitrary set of date and time information,
   * which this factory converts to an instance of {@code OffsetTime}.
   * <p>
   * The conversion extracts and combines the {@code ZoneOffset} and the
   * {@code Time} from the temporal object.
   * Implementations are permitted to perform optimizations such as accessing
   * those fields that are equivalent to the relevant objects.
   * <p>
   * This method matches the signature of the functional interface {@link TemporalQuery}
   * allowing it to be used in queries via method reference, {@code OffsetTime::from}.
   *
   * @param temporal  the temporal object to convert, not null
   * @return the offset time, not null
   * @throws DateTimeException if unable to convert to an { @code OffsetTime}
   */
  def from(temporal: TemporalAccessor): OffsetTime = {
    if (temporal.isInstanceOf[OffsetTime]) {
      temporal.asInstanceOf[OffsetTime]
    }
    try {
      val time: Time = Time.from(temporal)
      val offset: ZoneOffset = ZoneOffset.from(temporal)
      new OffsetTime(time, offset)
    }
    catch {
      case ex: DateTimeException => {
        throw new DateTimeException("Unable to obtain OffsetTime from TemporalAccessor: " + temporal.getClass, ex)
      }
    }
  }

  /**
   * Obtains an instance of {@code OffsetTime} from a text string such as {@code 10:15:30+01:00}.
   * <p>
   * The string must represent a valid time and is parsed using
   * {@link java.time.format.DateTimeFormatter#ISO_OFFSET_TIME}.
   *
   * @param text  the text to parse such as "10:15:30+01:00", not null
   * @return the parsed local time, not null
   * @throws DateTimeParseException if the text cannot be parsed
   */
  def parse(text: CharSequence): OffsetTime = {
    parse(text, DateTimeFormatter.ISO_OFFSET_TIME)
  }

  /**
   * Obtains an instance of {@code OffsetTime} from a text string using a specific formatter.
   * <p>
   * The text is parsed using the formatter, returning a time.
   *
   * @param text  the text to parse, not null
   * @param formatter  the formatter to use, not null
   * @return the parsed offset time, not null
   * @throws DateTimeParseException if the text cannot be parsed
   */
  def parse(text: CharSequence, formatter: DateTimeFormatter): OffsetTime = {
    object
    formatter.parse(text, OffsetTime.from)
  }

  private[time] def readExternal(in: ObjectInput): OffsetTime = {
    val time: Time = in.readObject.asInstanceOf[Time]
    val offset: ZoneOffset = in.readObject.asInstanceOf[ZoneOffset]
    OffsetTime.of(time, offset)
  }

  /**
   * The minimum supported {@code OffsetTime}, '00:00:00+18:00'.
   * This is the time of midnight at the start of the day in the maximum offset
   * (larger offsets are earlier on the time-line).
   * This combines {@link Time#MIN} and {@link ZoneOffset#MAX}.
   * This could be used by an application as a "far past" date.
   */
  final val MIN: OffsetTime = Time.MIN.atOffset(ZoneOffset.MAX)
  /**
   * The maximum supported {@code OffsetTime}, '23:59:59.999999999-18:00'.
   * This is the time just before midnight at the end of the day in the minimum offset
   * (larger negative offsets are later on the time-line).
   * This combines {@link Time#MAX} and {@link ZoneOffset#MIN}.
   * This could be used by an application as a "far future" date.
   */
  final val MAX: OffsetTime = Time.MAX.atOffset(ZoneOffset.MIN)
  /**
   * Serialization version.
   */
  private final val serialVersionUID: Long = 7264499704384272492L
}

final class OffsetTime extends Temporal with TemporalAdjuster with Comparable[OffsetTime]  {
  /**
   * Constructor.
   *
   * @param time  the local time, not null
   * @param offset  the zone offset, not null
   */
  private def this(time: Time, offset: ZoneOffset) {

    this.time = object
    this.offset = object
  }

  /**
   * Returns a new time based on this one, returning {@code this} where possible.
   *
   * @param time  the time to create with, not null
   * @param offset  the zone offset to create with, not null
   */
  private def `with`(time: Time, offset: ZoneOffset): OffsetTime = {
    if (this.time eq time && (this.offset == offset)) {
      this
    }
    new OffsetTime(time, offset)
  }

  /**
   * Checks if the specified field is supported.
   * <p>
   * This checks if this time can be queried for the specified field.
   * If false, then calling the {@link #range(TemporalField) range},
   * {@link #get(TemporalField) get} and {@link #with(TemporalField, long)}
   * methods will throw an exception.
   * <p>
   * If the field is a {@link ChronoField} then the query is implemented here.
   * The supported fields are:
   * <ul>
   * <li>{@code NANO_OF_SECOND}
   * <li>{@code NANO_OF_DAY}
   * <li>{@code MICRO_OF_SECOND}
   * <li>{@code MICRO_OF_DAY}
   * <li>{@code MILLI_OF_SECOND}
   * <li>{@code MILLI_OF_DAY}
   * <li>{@code SECOND_OF_MINUTE}
   * <li>{@code SECOND_OF_DAY}
   * <li>{@code MINUTE_OF_HOUR}
   * <li>{@code MINUTE_OF_DAY}
   * <li>{@code HOUR_OF_AMPM}
   * <li>{@code CLOCK_HOUR_OF_AMPM}
   * <li>{@code HOUR_OF_DAY}
   * <li>{@code CLOCK_HOUR_OF_DAY}
   * <li>{@code AMPM_OF_DAY}
   * <li>{@code OFFSET_SECONDS}
   * </ul>
   * All other {@code ChronoField} instances will  false.
   * <p>
   * If the field is not a {@code ChronoField}, then the result of this method
   * is obtained by invoking {@code TemporalField.isSupportedBy(TemporalAccessor)}
   * passing {@code this} as the argument.
   * Whether the field is supported is determined by the field.
   *
   * @param field  the field to check, null returns false
   * @return true if the field is supported on this time, false if not
   */
  def isSupported(field: TemporalField): Boolean = {
    if (field.isInstanceOf[ChronoField]) {
      field.isTimeBased || field eq OFFSET_SECONDS
    }
    field != null && field.isSupportedBy(this)
  }

  /**
   * Checks if the specified unit is supported.
   * <p>
   * This checks if the specified unit can be added to, or subtracted from, this date-time.
   * If false, then calling the {@link #plus(long, TemporalUnit)} and
   * {@link #minus(long, TemporalUnit) minus} methods will throw an exception.
   * <p>
   * If the unit is a {@link ChronoUnit} then the query is implemented here.
   * The supported units are:
   * <ul>
   * <li>{@code NANOS}
   * <li>{@code MICROS}
   * <li>{@code MILLIS}
   * <li>{@code SECONDS}
   * <li>{@code MINUTES}
   * <li>{@code HOURS}
   * <li>{@code HALF_DAYS}
   * </ul>
   * All other {@code ChronoUnit} instances will  false.
   * <p>
   * If the unit is not a {@code ChronoUnit}, then the result of this method
   * is obtained by invoking {@code TemporalUnit.isSupportedBy(Temporal)}
   * passing {@code this} as the argument.
   * Whether the unit is supported is determined by the unit.
   *
   * @param unit  the unit to check, null returns false
   * @return true if the unit can be added/subtracted, false if not
   */
  def isSupported(unit: TemporalUnit): Boolean = {
    if (unit.isInstanceOf[ChronoUnit]) {
      unit.isTimeBased
    }
    unit != null && unit.isSupportedBy(this)
  }

  /**
   * Gets the range of valid values for the specified field.
   * <p>
   * The range object expresses the minimum and maximum valid values for a field.
   * This time is used to enhance the accuracy of the returned range.
   * If it is not possible to  the range, because the field is not supported
   * or for some other reason, an exception is thrown.
   * <p>
   * If the field is a {@link ChronoField} then the query is implemented here.
   * The {@link #isSupported(TemporalField) supported fields} will
   * appropriate range instances.
   * All other {@code ChronoField} instances will throw an {@code UnsupportedTemporalTypeException}.
   * <p>
   * If the field is not a {@code ChronoField}, then the result of this method
   * is obtained by invoking {@code TemporalField.rangeRefinedBy(TemporalAccessor)}
   * passing {@code this} as the argument.
   * Whether the range can be obtained is determined by the field.
   *
   * @param field  the field to query the range for, not null
   * @return the range of valid values for the field, not null
   * @throws DateTimeException if the range for the field cannot be obtained
   * @throws UnsupportedTemporalTypeException if the field is not supported
   */
  override def range(field: TemporalField): ValueRange = {
    if (field.isInstanceOf[ChronoField]) {
      if (field eq OFFSET_SECONDS) {
        field.range
      }
      time.range(field)
    }
    field.rangeRefinedBy(this)
  }

  /**
   * Gets the value of the specified field from this time as an {@code int}.
   * <p>
   * This queries this time for the value for the specified field.
   * The returned value will always be within the valid range of values for the field.
   * If it is not possible to  the value, because the field is not supported
   * or for some other reason, an exception is thrown.
   * <p>
   * If the field is a {@link ChronoField} then the query is implemented here.
   * The {@link #isSupported(TemporalField) supported fields} will  valid
   * values based on this time, except {@code NANO_OF_DAY} and {@code MICRO_OF_DAY}
   * which are too large to fit in an {@code int} and throw a {@code DateTimeException}.
   * All other {@code ChronoField} instances will throw an {@code UnsupportedTemporalTypeException}.
   * <p>
   * If the field is not a {@code ChronoField}, then the result of this method
   * is obtained by invoking {@code TemporalField.getFrom(TemporalAccessor)}
   * passing {@code this} as the argument. Whether the value can be obtained,
   * and what the value represents, is determined by the field.
   *
   * @param field  the field to get, not null
   * @return the value for the field
   * @throws DateTimeException if a value for the field cannot be obtained or
   *                           the value is outside the range of valid values for the field
   * @throws UnsupportedTemporalTypeException if the field is not supported or
   *                                          the range of values exceeds an { @code int}
   * @throws ArithmeticException if numeric overflow occurs
   */
  override def get(field: TemporalField): Int = {
    Temporal.super.get(field)
  }

  /**
   * Gets the value of the specified field from this time as a {@code long}.
   * <p>
   * This queries this time for the value for the specified field.
   * If it is not possible to  the value, because the field is not supported
   * or for some other reason, an exception is thrown.
   * <p>
   * If the field is a {@link ChronoField} then the query is implemented here.
   * The {@link #isSupported(TemporalField) supported fields} will  valid
   * values based on this time.
   * All other {@code ChronoField} instances will throw an {@code UnsupportedTemporalTypeException}.
   * <p>
   * If the field is not a {@code ChronoField}, then the result of this method
   * is obtained by invoking {@code TemporalField.getFrom(TemporalAccessor)}
   * passing {@code this} as the argument. Whether the value can be obtained,
   * and what the value represents, is determined by the field.
   *
   * @param field  the field to get, not null
   * @return the value for the field
   * @throws DateTimeException if a value for the field cannot be obtained
   * @throws UnsupportedTemporalTypeException if the field is not supported
   * @throws ArithmeticException if numeric overflow occurs
   */
  def getLong(field: TemporalField): Long = {
    if (field.isInstanceOf[ChronoField]) {
      if (field eq OFFSET_SECONDS) {
        offset.getTotalSeconds
      }
      time.getLong(field)
    }
    field.getFrom(this)
  }

  /**
   * Gets the zone offset, such as '+01:00'.
   * <p>
   * This is the offset of the local time from UTC/Greenwich.
   *
   * @return the zone offset, not null
   */
  def getOffset: ZoneOffset = {
    offset
  }

  /**
   * Returns a copy of this {@code OffsetTime} with the specified offset ensuring
   * that the result has the same local time.
   * <p>
   * This method returns an object with the same {@code Time} and the specified {@code ZoneOffset}.
   * No calculation is needed or performed.
   * For example, if this time represents {@code 10:30+02:00} and the offset specified is
   * {@code +03:00}, then this method will  {@code 10:30+03:00}.
   * <p>
   * To take into account the difference between the offsets, and adjust the time fields,
   * use {@link #withOffsetSameInstant}.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param offset  the zone offset to change to, not null
   * @return an { @code OffsetTime} based on this time with the requested offset, not null
   */
  def withOffsetSameLocal(offset: ZoneOffset): OffsetTime = {
    if (offset != null && (offset == this.offset)) this else new OffsetTime(time, offset)
  }

  /**
   * Returns a copy of this {@code OffsetTime} with the specified offset ensuring
   * that the result is at the same instant on an implied day.
   * <p>
   * This method returns an object with the specified {@code ZoneOffset} and a {@code Time}
   * adjusted by the difference between the two offsets.
   * This will result in the old and new objects representing the same instant an an implied day.
   * This is useful for finding the local time in a different offset.
   * For example, if this time represents {@code 10:30+02:00} and the offset specified is
   * {@code +03:00}, then this method will  {@code 11:30+03:00}.
   * <p>
   * To change the offset without adjusting the local time use {@link #withOffsetSameLocal}.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param offset  the zone offset to change to, not null
   * @return an { @code OffsetTime} based on this time with the requested offset, not null
   */
  def withOffsetSameInstant(offset: ZoneOffset): OffsetTime = {
    if (offset == this.offset) {
      this
    }
    val difference: Int = offset.getTotalSeconds - this.offset.getTotalSeconds
    val adjusted: Time = time.plusSeconds(difference)
    new OffsetTime(adjusted, offset)
  }

  /**
   * Gets the {@code Time} part of this date-time.
   * <p>
   * This returns a {@code Time} with the same hour, minute, second and
   * nanosecond as this date-time.
   *
   * @return the time part of this date-time, not null
   */
  def toLocalTime: Time = {
    time
  }

  /**
   * Gets the hour-of-day field.
   *
   * @return the hour-of-day, from 0 to 23
   */
  def getHour: Int = {
    time.getHour
  }

  /**
   * Gets the minute-of-hour field.
   *
   * @return the minute-of-hour, from 0 to 59
   */
  def getMinute: Int = {
    time.getMinute
  }

  /**
   * Gets the second-of-minute field.
   *
   * @return the second-of-minute, from 0 to 59
   */
  def getSecond: Int = {
    time.getSecond
  }

  /**
   * Gets the nano-of-second field.
   *
   * @return the nano-of-second, from 0 to 999,999,999
   */
  def getNano: Int = {
    time.getNano
  }

  /**
   * Returns an adjusted copy of this time.
   * <p>
   * This returns an {@code OffsetTime}, based on this one, with the time adjusted.
   * The adjustment takes place using the specified adjuster strategy object.
   * Read the documentation of the adjuster to understand what adjustment will be made.
   * <p>
   * A simple adjuster might simply set the one of the fields, such as the hour field.
   * A more complex adjuster might set the time to the last hour of the day.
   * <p>
   * The classes {@link Time} and {@link ZoneOffset} implement {@code TemporalAdjuster},
   * thus this method can be used to change the time or offset:
   * {{{
   * result = offsetTime.with(time);
   * result = offsetTime.with(offset);
   * }}}
   * <p>
   * The result of this method is obtained by invoking the
   * {@link TemporalAdjuster#adjustInto(Temporal)} method on the
   * specified adjuster passing {@code this} as the argument.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param adjuster the adjuster to use, not null
   * @return an { @code OffsetTime} based on { @code this} with the adjustment made, not null
   * @throws DateTimeException if the adjustment cannot be made
   * @throws ArithmeticException if numeric overflow occurs
   */
  override def `with`(adjuster: TemporalAdjuster): OffsetTime = {
    if (adjuster.isInstanceOf[Time]) {
      `with`(adjuster.asInstanceOf[Time], offset)
    }
    else if (adjuster.isInstanceOf[ZoneOffset]) {
      `with`(time, adjuster.asInstanceOf[ZoneOffset])
    }
    else if (adjuster.isInstanceOf[OffsetTime]) {
      adjuster.asInstanceOf[OffsetTime]
    }
    adjuster.adjustInto(this).asInstanceOf[OffsetTime]
  }

  /**
   * Returns a copy of this time with the specified field set to a new value.
   * <p>
   * This returns an {@code OffsetTime}, based on this one, with the value
   * for the specified field changed.
   * This can be used to change any supported field, such as the hour, minute or second.
   * If it is not possible to set the value, because the field is not supported or for
   * some other reason, an exception is thrown.
   * <p>
   * If the field is a {@link ChronoField} then the adjustment is implemented here.
   * <p>
   * The {@code OFFSET_SECONDS} field will  a time with the specified offset.
   * The local time is unaltered. If the new offset value is outside the valid range
   * then a {@code DateTimeException} will be thrown.
   * <p>
   * The other {@link #isSupported(TemporalField) supported fields} will behave as per
   * the matching method on {@link Time#with(TemporalField, long)} Time}.
   * In this case, the offset is not part of the calculation and will be unchanged.
   * <p>
   * All other {@code ChronoField} instances will throw an {@code UnsupportedTemporalTypeException}.
   * <p>
   * If the field is not a {@code ChronoField}, then the result of this method
   * is obtained by invoking {@code TemporalField.adjustInto(Temporal, long)}
   * passing {@code this} as the argument. In this case, the field determines
   * whether and how to adjust the instant.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param field  the field to set in the result, not null
   * @param newValue  the new value of the field in the result
   * @return an { @code OffsetTime} based on { @code this} with the specified field set, not null
   * @throws DateTimeException if the field cannot be set
   * @throws UnsupportedTemporalTypeException if the field is not supported
   * @throws ArithmeticException if numeric overflow occurs
   */
  def `with`(field: TemporalField, newValue: Long): OffsetTime = {
    if (field.isInstanceOf[ChronoField]) {
      if (field eq OFFSET_SECONDS) {
        val f: ChronoField = field.asInstanceOf[ChronoField]
        `with`(time, ZoneOffset.ofTotalSeconds(f.checkValidIntValue(newValue)))
      }
      `with`(time.`with`(field, newValue), offset)
    }
    field.adjustInto(this, newValue)
  }

  /**
   * Returns a copy of this {@code OffsetTime} with the hour-of-day value altered.
   * <p>
   * The offset does not affect the calculation and will be the same in the result.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param hour  the hour-of-day to set in the result, from 0 to 23
   * @return an { @code OffsetTime} based on this time with the requested hour, not null
   * @throws DateTimeException if the hour value is invalid
   */
  def withHour(hour: Int): OffsetTime = {
    `with`(time.withHour(hour), offset)
  }

  /**
   * Returns a copy of this {@code OffsetTime} with the minute-of-hour value altered.
   * <p>
   * The offset does not affect the calculation and will be the same in the result.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param minute  the minute-of-hour to set in the result, from 0 to 59
   * @return an { @code OffsetTime} based on this time with the requested minute, not null
   * @throws DateTimeException if the minute value is invalid
   */
  def withMinute(minute: Int): OffsetTime = {
    `with`(time.withMinute(minute), offset)
  }

  /**
   * Returns a copy of this {@code OffsetTime} with the second-of-minute value altered.
   * <p>
   * The offset does not affect the calculation and will be the same in the result.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param second  the second-of-minute to set in the result, from 0 to 59
   * @return an { @code OffsetTime} based on this time with the requested second, not null
   * @throws DateTimeException if the second value is invalid
   */
  def withSecond(second: Int): OffsetTime = {
    `with`(time.withSecond(second), offset)
  }

  /**
   * Returns a copy of this {@code OffsetTime} with the nano-of-second value altered.
   * <p>
   * The offset does not affect the calculation and will be the same in the result.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param nanoOfSecond  the nano-of-second to set in the result, from 0 to 999,999,999
   * @return an { @code OffsetTime} based on this time with the requested nanosecond, not null
   * @throws DateTimeException if the nanos value is invalid
   */
  def withNano(nanoOfSecond: Int): OffsetTime = {
    `with`(time.withNano(nanoOfSecond), offset)
  }

  /**
   * Returns a copy of this {@code OffsetTime} with the time truncated.
   * <p>
   * Truncation returns a copy of the original time with fields
   * smaller than the specified unit set to zero.
   * For example, truncating with the {@link ChronoUnit#MINUTES minutes} unit
   * will set the second-of-minute and nano-of-second field to zero.
   * <p>
   * The unit must have a {@linkplain TemporalUnit#getDuration() duration}
   * that divides into the length of a standard day without remainder.
   * This includes all supplied time units on {@link ChronoUnit} and
   * {@link ChronoUnit#DAYS DAYS}. Other units throw an exception.
   * <p>
   * The offset does not affect the calculation and will be the same in the result.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param unit  the unit to truncate to, not null
   * @return an { @code OffsetTime} based on this time with the time truncated, not null
   * @throws DateTimeException if unable to truncate
   * @throws UnsupportedTemporalTypeException if the unit is not supported
   */
  def truncatedTo(unit: TemporalUnit): OffsetTime = {
    `with`(time.truncatedTo(unit), offset)
  }

  /**
   * Returns a copy of this time with the specified amount added.
   * <p>
   * This returns an {@code OffsetTime}, based on this one, with the specified amount added.
   * The amount is typically {@link Duration} but may be any other type implementing
   * the {@link TemporalAmount} interface.
   * <p>
   * The calculation is delegated to the amount object by calling
   * {@link TemporalAmount#addTo(Temporal)}. The amount implementation is free
   * to implement the addition in any way it wishes, however it typically
   * calls back to {@link #plus(long, TemporalUnit)}. Consult the documentation
   * of the amount implementation to determine if it can be successfully added.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param amountToAdd  the amount to add, not null
   * @return an { @code OffsetTime} based on this time with the addition made, not null
   * @throws DateTimeException if the addition cannot be made
   * @throws ArithmeticException if numeric overflow occurs
   */
  override def plus(amountToAdd: TemporalAmount): OffsetTime = {
    amountToAdd.addTo(this).asInstanceOf[OffsetTime]
  }

  /**
   * Returns a copy of this time with the specified amount added.
   * <p>
   * This returns an {@code OffsetTime}, based on this one, with the amount
   * in terms of the unit added. If it is not possible to add the amount, because the
   * unit is not supported or for some other reason, an exception is thrown.
   * <p>
   * If the field is a {@link ChronoUnit} then the addition is implemented by
   * {@link Time#plus(long, TemporalUnit)}.
   * The offset is not part of the calculation and will be unchanged in the result.
   * <p>
   * If the field is not a {@code ChronoUnit}, then the result of this method
   * is obtained by invoking {@code TemporalUnit.addTo(Temporal, long)}
   * passing {@code this} as the argument. In this case, the unit determines
   * whether and how to perform the addition.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param amountToAdd  the amount of the unit to add to the result, may be negative
   * @param unit  the unit of the amount to add, not null
   * @return an { @code OffsetTime} based on this time with the specified amount added, not null
   * @throws DateTimeException if the addition cannot be made
   * @throws UnsupportedTemporalTypeException if the unit is not supported
   * @throws ArithmeticException if numeric overflow occurs
   */
  def plus(amountToAdd: Long, unit: TemporalUnit): OffsetTime = {
    if (unit.isInstanceOf[ChronoUnit]) {
      `with`(time.plus(amountToAdd, unit), offset)
    }
    unit.addTo(this, amountToAdd)
  }

  /**
   * Returns a copy of this {@code OffsetTime} with the specified period in hours added.
   * <p>
   * This adds the specified number of hours to this time, returning a new time.
   * The calculation wraps around midnight.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param hours  the hours to add, may be negative
   * @return an { @code OffsetTime} based on this time with the hours added, not null
   */
  def plusHours(hours: Long): OffsetTime = {
    `with`(time.plusHours(hours), offset)
  }

  /**
   * Returns a copy of this {@code OffsetTime} with the specified period in minutes added.
   * <p>
   * This adds the specified number of minutes to this time, returning a new time.
   * The calculation wraps around midnight.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param minutes  the minutes to add, may be negative
   * @return an { @code OffsetTime} based on this time with the minutes added, not null
   */
  def plusMinutes(minutes: Long): OffsetTime = {
    `with`(time.plusMinutes(minutes), offset)
  }

  /**
   * Returns a copy of this {@code OffsetTime} with the specified period in seconds added.
   * <p>
   * This adds the specified number of seconds to this time, returning a new time.
   * The calculation wraps around midnight.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param seconds  the seconds to add, may be negative
   * @return an { @code OffsetTime} based on this time with the seconds added, not null
   */
  def plusSeconds(seconds: Long): OffsetTime = {
    `with`(time.plusSeconds(seconds), offset)
  }

  /**
   * Returns a copy of this {@code OffsetTime} with the specified period in nanoseconds added.
   * <p>
   * This adds the specified number of nanoseconds to this time, returning a new time.
   * The calculation wraps around midnight.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param nanos  the nanos to add, may be negative
   * @return an { @code OffsetTime} based on this time with the nanoseconds added, not null
   */
  def plusNanos(nanos: Long): OffsetTime = {
    `with`(time.plusNanos(nanos), offset)
  }

  /**
   * Returns a copy of this time with the specified amount subtracted.
   * <p>
   * This returns an {@code OffsetTime}, based on this one, with the specified amount subtracted.
   * The amount is typically {@link Duration} but may be any other type implementing
   * the {@link TemporalAmount} interface.
   * <p>
   * The calculation is delegated to the amount object by calling
   * {@link TemporalAmount#subtractFrom(Temporal)}. The amount implementation is free
   * to implement the subtraction in any way it wishes, however it typically
   * calls back to {@link #minus(long, TemporalUnit)}. Consult the documentation
   * of the amount implementation to determine if it can be successfully subtracted.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param amountToSubtract  the amount to subtract, not null
   * @return an { @code OffsetTime} based on this time with the subtraction made, not null
   * @throws DateTimeException if the subtraction cannot be made
   * @throws ArithmeticException if numeric overflow occurs
   */
  override def minus(amountToSubtract: TemporalAmount): OffsetTime = {
    amountToSubtract.subtractFrom(this).asInstanceOf[OffsetTime]
  }

  /**
   * Returns a copy of this time with the specified amount subtracted.
   * <p>
   * This returns an {@code OffsetTime}, based on this one, with the amount
   * in terms of the unit subtracted. If it is not possible to subtract the amount,
   * because the unit is not supported or for some other reason, an exception is thrown.
   * <p>
   * This method is equivalent to {@link #plus(long, TemporalUnit)} with the amount negated.
   * See that method for a full description of how addition, and thus subtraction, works.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param amountToSubtract  the amount of the unit to subtract from the result, may be negative
   * @param unit  the unit of the amount to subtract, not null
   * @return an { @code OffsetTime} based on this time with the specified amount subtracted, not null
   * @throws DateTimeException if the subtraction cannot be made
   * @throws UnsupportedTemporalTypeException if the unit is not supported
   * @throws ArithmeticException if numeric overflow occurs
   */
  override def minus(amountToSubtract: Long, unit: TemporalUnit): OffsetTime = {
    (if (amountToSubtract == Long.MIN_VALUE) plus(Long.MAX_VALUE, unit).plus(1, unit) else plus(-amountToSubtract, unit))
  }

  /**
   * Returns a copy of this {@code OffsetTime} with the specified period in hours subtracted.
   * <p>
   * This subtracts the specified number of hours from this time, returning a new time.
   * The calculation wraps around midnight.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param hours  the hours to subtract, may be negative
   * @return an { @code OffsetTime} based on this time with the hours subtracted, not null
   */
  def minusHours(hours: Long): OffsetTime = {
    `with`(time.minusHours(hours), offset)
  }

  /**
   * Returns a copy of this {@code OffsetTime} with the specified period in minutes subtracted.
   * <p>
   * This subtracts the specified number of minutes from this time, returning a new time.
   * The calculation wraps around midnight.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param minutes  the minutes to subtract, may be negative
   * @return an { @code OffsetTime} based on this time with the minutes subtracted, not null
   */
  def minusMinutes(minutes: Long): OffsetTime = {
    `with`(time.minusMinutes(minutes), offset)
  }

  /**
   * Returns a copy of this {@code OffsetTime} with the specified period in seconds subtracted.
   * <p>
   * This subtracts the specified number of seconds from this time, returning a new time.
   * The calculation wraps around midnight.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param seconds  the seconds to subtract, may be negative
   * @return an { @code OffsetTime} based on this time with the seconds subtracted, not null
   */
  def minusSeconds(seconds: Long): OffsetTime = {
    `with`(time.minusSeconds(seconds), offset)
  }

  /**
   * Returns a copy of this {@code OffsetTime} with the specified period in nanoseconds subtracted.
   * <p>
   * This subtracts the specified number of nanoseconds from this time, returning a new time.
   * The calculation wraps around midnight.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param nanos  the nanos to subtract, may be negative
   * @return an { @code OffsetTime} based on this time with the nanoseconds subtracted, not null
   */
  def minusNanos(nanos: Long): OffsetTime = {
    `with`(time.minusNanos(nanos), offset)
  }

  /**
   * Queries this time using the specified query.
   * <p>
   * This queries this time using the specified query strategy object.
   * The {@code TemporalQuery} object defines the logic to be used to
   * obtain the result. Read the documentation of the query to understand
   * what the result of this method will be.
   * <p>
   * The result of this method is obtained by invoking the
   * {@link TemporalQuery#queryFrom(TemporalAccessor)} method on the
   * specified query passing {@code this} as the argument.
   *
   * @param <R> the type of the result
   * @param query  the query to invoke, not null
   * @return the query result, null may be returned (defined by the query)
   * @throws DateTimeException if unable to query (defined by the query)
   * @throws ArithmeticException if numeric overflow occurs (defined by the query)
   */
  override def query(query: TemporalQuery[R]): R = {
    if (query eq TemporalQuery.offset || query eq TemporalQuery.zone) {
      offset.asInstanceOf[R]
    }
    else if (query eq TemporalQuery.zoneId | query eq TemporalQuery.chronology || query eq TemporalQuery.localDate) {
      null
    }
    else if (query eq TemporalQuery.localTime) {
      time.asInstanceOf[R]
    }
    else if (query eq TemporalQuery.precision) {
      NANOS.asInstanceOf[R]
    }
    query.queryFrom(this)
  }

  /**
   * Adjusts the specified temporal object to have the same offset and time
   * as this object.
   * <p>
   * This returns a temporal object of the same observable type as the input
   * with the offset and time changed to be the same as this.
   * <p>
   * The adjustment is equivalent to using {@link Temporal#with(TemporalField, long)}
   * twice, passing {@link ChronoField#NANO_OF_DAY} and
   * {@link ChronoField#OFFSET_SECONDS} as the fields.
   * <p>
   * In most cases, it is clearer to reverse the calling pattern by using
   * {@link Temporal#with(TemporalAdjuster)}:
   * {{{
   * // these two lines are equivalent, but the second approach is recommended
   * temporal = thisOffsetTime.adjustInto(temporal);
   * temporal = temporal.with(thisOffsetTime);
   * }}}
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param temporal  the target object to be adjusted, not null
   * @return the adjusted object, not null
   * @throws DateTimeException if unable to make the adjustment
   * @throws ArithmeticException if numeric overflow occurs
   */
  def adjustInto(temporal: Temporal): Temporal = {
    temporal.`with`(NANO_OF_DAY, time.toNanoOfDay).`with`(OFFSET_SECONDS, offset.getTotalSeconds)
  }

  /**
   * Calculates the amount of time until another time in terms of the specified unit.
   * <p>
   * This calculates the amount of time between two {@code OffsetTime}
   * objects in terms of a single {@code TemporalUnit}.
   * The start and end points are {@code this} and the specified time.
   * The result will be negative if the end is before the start.
   * For example, the period in hours between two times can be calculated
   * using {@code startTime.until(endTime, HOURS)}.
   * <p>
   * The {@code Temporal} passed to this method is converted to a
   * {@code OffsetTime} using {@link #from(TemporalAccessor)}.
   * If the offset differs between the two times, then the specified
   * end time is normalized to have the same offset as this time.
   * <p>
   * The calculation returns a whole number, representing the number of
   * complete units between the two times.
   * For example, the period in hours between 11:30Z and 13:29Z will only
   * be one hour as it is one minute short of two hours.
   * <p>
   * There are two equivalent ways of using this method.
   * The first is to invoke this method.
   * The second is to use {@link TemporalUnit#between(Temporal, Temporal)}:
   * {{{
   * // these two lines are equivalent
   * amount = start.until(end, MINUTES);
   * amount = MINUTES.between(start, end);
   * }}}
   * The choice should be made based on which makes the code more readable.
   * <p>
   * The calculation is implemented in this method for {@link ChronoUnit}.
   * The units {@code NANOS}, {@code MICROS}, {@code MILLIS}, {@code SECONDS},
   * {@code MINUTES}, {@code HOURS} and {@code HALF_DAYS} are supported.
   * Other {@code ChronoUnit} values will throw an exception.
   * <p>
   * If the unit is not a {@code ChronoUnit}, then the result of this method
   * is obtained by invoking {@code TemporalUnit.between(Temporal, Temporal)}
   * passing {@code this} as the first argument and the converted input temporal
   * as the second argument.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param endExclusive  the end date, which is converted to an { @code OffsetTime}, not null
   * @param unit  the unit to measure the amount in, not null
   * @return the amount of time between this time and the end time
   * @throws DateTimeException if the amount cannot be calculated, or the end
   *                           temporal cannot be converted to an { @code OffsetTime}
   * @throws UnsupportedTemporalTypeException if the unit is not supported
   * @throws ArithmeticException if numeric overflow occurs
   */
  def until(endExclusive: Temporal, unit: TemporalUnit): Long = {
    val end: OffsetTime = OffsetTime.from(endExclusive)
    if (unit.isInstanceOf[ChronoUnit]) {
      val nanosUntil: Long = end.toEpochNano - toEpochNano
      unit.asInstanceOf[ChronoUnit] match {
        case NANOS =>
          nanosUntil
        case MICROS =>
          nanosUntil / 1000
        case MILLIS =>
          nanosUntil / 1000 _000
        case SECONDS =>
          nanosUntil / NANOS_PER_SECOND
        case MINUTES =>
          nanosUntil / NANOS_PER_MINUTE
        case HOURS =>
          nanosUntil / NANOS_PER_HOUR
        case HALF_DAYS =>
          nanosUntil / (12 * NANOS_PER_HOUR)
      }
      throw new UnsupportedTemporalTypeException("Unsupported unit: " + unit)
    }
    unit.between(this, end)
  }

  /**
   * Formats this time using the specified formatter.
   * <p>
   * This time will be passed to the formatter to produce a string.
   *
   * @param formatter  the formatter to use, not null
   * @return the formatted time string, not null
   * @throws DateTimeException if an error occurs during printing
   */
  def format(formatter: DateTimeFormatter): String = {
    object
    formatter.format(this)
  }

  /**
   * Combines this time with a date to create an {@code OffsetDateTime}.
   * <p>
   * This returns an {@code OffsetDateTime} formed from this time and the specified date.
   * All possible combinations of date and time are valid.
   *
   * @param date  the date to combine with, not null
   * @return the offset date-time formed from this time and the specified date, not null
   */
  def atDate(date: Date): OffsetDateTime = {
    OffsetDateTime.of(date, time, offset)
  }

  /**
   * Converts this time to epoch nanos based on 1970-01-01Z.
   *
   * @return the epoch nanos value
   */
  private def toEpochNano: Long = {
    val nod: Long = time.toNanoOfDay
    val offsetNanos: Long = offset.getTotalSeconds * NANOS_PER_SECOND
    nod - offsetNanos
  }

  /**
   * Compares this {@code OffsetTime} to another time.
   * <p>
   * The comparison is based first on the UTC equivalent instant, then on the local time.
   * It is "consistent with equals", as defined by {@link Comparable}.
   * <p>
   * For example, the following is the comparator order:
   * <ol>
   * <li>{@code 10:30+01:00}</li>
   * <li>{@code 11:00+01:00}</li>
   * <li>{@code 12:00+02:00}</li>
   * <li>{@code 11:30+01:00}</li>
   * <li>{@code 12:00+01:00}</li>
   * <li>{@code 12:30+01:00}</li>
   * </ol>
   * Values #2 and #3 represent the same instant on the time-line.
   * When two values represent the same instant, the local time is compared
   * to distinguish them. This step is needed to make the ordering
   * consistent with {@code equals()}.
   * <p>
   * To compare the underlying local time of two {@code TemporalAccessor} instances,
   * use {@link ChronoField#NANO_OF_DAY} as a comparator.
   *
   * @param other  the other time to compare to, not null
   * @return the comparator value, negative if less, positive if greater
   * @throws NullPointerException if { @code other} is null
   */
  def compareTo(other: OffsetTime): Int = {
    if (offset == other.offset) {
      time.compareTo(other.time)
    }
    var compare: Int = Long.compare(toEpochNano, other.toEpochNano)
    if (compare == 0) {
      compare = time.compareTo(other.time)
    }
    compare
  }

  /**
   * Checks if the instant of this {@code OffsetTime} is after that of the
   * specified time applying both times to a common date.
   * <p>
   * This method differs from the comparison in {@link #compareTo} in that it
   * only compares the instant of the time. This is equivalent to converting both
   * times to an instant using the same date and comparing the instants.
   *
   * @param other  the other time to compare to, not null
   * @return true if this is after the instant of the specified time
   */
  def isAfter(other: OffsetTime): Boolean = {
    toEpochNano > other.toEpochNano
  }

  /**
   * Checks if the instant of this {@code OffsetTime} is before that of the
   * specified time applying both times to a common date.
   * <p>
   * This method differs from the comparison in {@link #compareTo} in that it
   * only compares the instant of the time. This is equivalent to converting both
   * times to an instant using the same date and comparing the instants.
   *
   * @param other  the other time to compare to, not null
   * @return true if this is before the instant of the specified time
   */
  def isBefore(other: OffsetTime): Boolean = {
    toEpochNano < other.toEpochNano
  }

  /**
   * Checks if the instant of this {@code OffsetTime} is equal to that of the
   * specified time applying both times to a common date.
   * <p>
   * This method differs from the comparison in {@link #compareTo} and {@link #equals}
   * in that it only compares the instant of the time. This is equivalent to converting both
   * times to an instant using the same date and comparing the instants.
   *
   * @param other  the other time to compare to, not null
   * @return true if this is equal to the instant of the specified time
   */
  def isEqual(other: OffsetTime): Boolean = {
    toEpochNano == other.toEpochNano
  }

  /**
   * Checks if this time is equal to another time.
   * <p>
   * The comparison is based on the local-time and the offset.
   * To compare for the same instant on the time-line, use {@link #isEqual(OffsetTime)}.
   * <p>
   * Only objects of type {@code OffsetTime} are compared, other types  false.
   * To compare the underlying local time of two {@code TemporalAccessor} instances,
   * use {@link ChronoField#NANO_OF_DAY} as a comparator.
   *
   * @param obj  the object to check, null returns false
   * @return true if this is equal to the other time
   */
  override def equals(obj: AnyRef): Boolean = {
    if (this eq obj) {
      true
    }
    if (obj.isInstanceOf[OffsetTime]) {
      val other: OffsetTime = obj.asInstanceOf[OffsetTime]
      (time == other.time) && (offset == other.offset)
    }
    false
  }

  /**
   * A hash code for this time.
   *
   * @return a suitable hash code
   */
  override def hashCode: Int = {
    time.hashCode ^ offset.hashCode
  }

  /**
   * Outputs this time as a {@code String}, such as {@code 10:15:30+01:00}.
   * <p>
   * The output will be one of the following ISO-8601 formats:
   * <p><ul>
   * <li>{@code HH:mmXXXXX}</li>
   * <li>{@code HH:mm:ssXXXXX}</li>
   * <li>{@code HH:mm:ss.SSSXXXXX}</li>
   * <li>{@code HH:mm:ss.SSSSSSXXXXX}</li>
   * <li>{@code HH:mm:ss.SSSSSSSSSXXXXX}</li>
   * </ul><p>
   * The format used will be the shortest that outputs the full value of
   * the time where the omitted parts are implied to be zero.
   *
   * @return a string representation of this time, not null
   */
  override def toString: String = {
    time.toString + offset.toString
  }

  /**
   * Writes the object using a
   * <a href="../../../serialized-form.html#java.time.temporal.Ser">dedicated serialized form</a>.
   * {{{
   * out.writeByte(9);  // identifies this as a OffsetTime
   * out.writeObject(time);
   * out.writeObject(offset);
   * }}}
   *
   * @return the instance of { @code Ser}, not null
   */
  private def writeReplace: AnyRef = {
    new Ser(Ser.OFFSET_TIME_TYPE, this)
  }

  /**
   * Defend against malicious streams.
   * @return never
   * @throws InvalidObjectException always
   */
  private def readResolve: AnyRef = {
    throw new InvalidObjectException("Deserialization via serialization delegate")
  }

  private[time] def writeExternal(out: ObjectOutput) {
    out.writeObject(time)
    out.writeObject(offset)
  }

  /**
   * The local date-time.
   */
  private final val time: Time = null
  /**
   * The offset from UTC/Greenwich.
   */
  private final val offset: ZoneOffset = null
}

