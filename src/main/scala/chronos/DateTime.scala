package chronos

import chronos.calendar._
import chronos.format._
import chronos.temporal._


/**
 * Exception used to indicate a problem while calculating a date-time.
 * <p>
 * This exception is used to indicate problems with creating, querying
 * and manipulating date-time objects.
 *
 * @implSpec
 * This class is intended for use in a single thread.
 *
 * @since 1.8
 */
object DateTimeException {


  def apply(message: String, cause: Throwable) = new DateTimeException(message).initCause(cause)
}

case class DateTimeException(message: String) extends RuntimeException(message)


/**
 * A date-time without a time-zone in the ISO-8601 calendar system,
 * such as {@code 2007-12-03T10:15:30}.
 * <p>
 * {@code DateTime} is an immutable date-time object that represents a date-time,
 * often viewed as year-month-day-hour-minute-second. Other date and time fields,
 * such as day-of-year, day-of-week and week-of-year, can also be accessed.
 * Time is represented to nanosecond precision.
 * For example, the value "2nd October 2007 at 13:45.30.123456789" can be
 * stored in a {@code DateTime}.
 * <p>
 * This class does not store or represent a time-zone.
 * Instead, it is a description of the date, as used for birthdays, combined with
 * the local time as seen on a wall clock.
 * It cannot represent an instant on the time-line without additional information
 * such as an offset or time-zone.
 * <p>
 * The ISO-8601 calendar system is the modern civil calendar system used today
 * in most of the world. It is equivalent to the proleptic Gregorian calendar
 * system, in which today's rules for leap years are applied for all time.
 * For most applications written today, the ISO-8601 rules are entirely suitable.
 * However, any application that makes use of historical dates, and requires them
 * to be accurate will find the ISO-8601 approach unsuitable.
 *
 * @implSpec
 * This class is immutable and thread-safe.
 *
 * @since 1.8
 */
object DateTime {
  /**
   * Obtains the current date-time from the system clock in the default time-zone.
   * <p>
   * This will query the {@link Clock#systemDefaultZone() system clock} in the default
   * time-zone to obtain the current date-time.
   * <p>
   * Using this method will prevent the ability to use an alternate clock for testing
   * because the clock is hard-coded.
   *
   * @return the current date-time using the system clock and default time-zone, not null
   */
  def now: DateTime = {
     now(Clock.systemDefaultZone)
  }

  /**
   * Obtains the current date-time from the system clock in the specified time-zone.
   * <p>
   * This will query the {@link Clock#system(ZoneId) system clock} to obtain the current date-time.
   * Specifying the time-zone avoids dependence on the default time-zone.
   * <p>
   * Using this method will prevent the ability to use an alternate clock for testing
   * because the clock is hard-coded.
   *
   * @param zone  the zone ID to use, not null
   * @return the current date-time using the system clock, not null
   */
  def now(zone: ZoneId): DateTime = {
     now(Clock.system(zone))
  }

  /**
   * Obtains the current date-time from the specified clock.
   * <p>
   * This will query the specified clock to obtain the current date-time.
   * Using this method allows the use of an alternate clock for testing.
   * The alternate clock may be introduced using {@link Clock dependency injection}.
   *
   * @param clock  the clock to use, not null
   * @return the current date-time, not null
   */
  def now(clock: Clock): DateTime = {
    object
    val now: Instant = clock.instant
    val offset: ZoneOffset = clock.getZone.getRules.getOffset(now)
     ofEpochSecond(now.getEpochSecond, now.getNano, offset)
  }

  /**
   * Obtains an instance of {@code DateTime} from year, month,
   * day, hour and minute, setting the second and nanosecond to zero.
   * <p>
   * This returns a {@code DateTime} with the specified year, month,
   * day-of-month, hour and minute.
   * The day must be valid for the year and month, otherwise an exception will be thrown.
   * The second and nanosecond fields will be set to zero.
   *
   * @param year  the year to represent, from MIN_YEAR to MAX_YEAR
   * @param month  the month-of-year to represent, not null
   * @param dayOfMonth  the day-of-month to represent, from 1 to 31
   * @param hour  the hour-of-day to represent, from 0 to 23
   * @param minute  the minute-of-hour to represent, from 0 to 59
   * @return the local date-time, not null
   * @throws DateTimeException if the value of any field is out of range,
   *                           or if the day-of-month is invalid for the month-year
   */
  def of(year: Int, month: Month, dayOfMonth: Int, hour: Int, minute: Int): DateTime = {
    val date: Date = Date.of(year, month, dayOfMonth)
    val time: Time = Time.of(hour, minute)
     new DateTime(date, time)
  }

  /**
   * Obtains an instance of {@code DateTime} from year, month,
   * day, hour, minute and second, setting the nanosecond to zero.
   * <p>
   * This returns a {@code DateTime} with the specified year, month,
   * day-of-month, hour, minute and second.
   * The day must be valid for the year and month, otherwise an exception will be thrown.
   * The nanosecond field will be set to zero.
   *
   * @param year  the year to represent, from MIN_YEAR to MAX_YEAR
   * @param month  the month-of-year to represent, not null
   * @param dayOfMonth  the day-of-month to represent, from 1 to 31
   * @param hour  the hour-of-day to represent, from 0 to 23
   * @param minute  the minute-of-hour to represent, from 0 to 59
   * @param second  the second-of-minute to represent, from 0 to 59
   * @return the local date-time, not null
   * @throws DateTimeException if the value of any field is out of range,
   *                           or if the day-of-month is invalid for the month-year
   */
  def of(year: Int, month: Month, dayOfMonth: Int, hour: Int, minute: Int, second: Int): DateTime = {
    val date: Date = Date.of(year, month, dayOfMonth)
    val time: Time = Time.of(hour, minute, second)
     new DateTime(date, time)
  }

  /**
   * Obtains an instance of {@code DateTime} from year, month,
   * day, hour, minute, second and nanosecond.
   * <p>
   * This returns a {@code DateTime} with the specified year, month,
   * day-of-month, hour, minute, second and nanosecond.
   * The day must be valid for the year and month, otherwise an exception will be thrown.
   *
   * @param year  the year to represent, from MIN_YEAR to MAX_YEAR
   * @param month  the month-of-year to represent, not null
   * @param dayOfMonth  the day-of-month to represent, from 1 to 31
   * @param hour  the hour-of-day to represent, from 0 to 23
   * @param minute  the minute-of-hour to represent, from 0 to 59
   * @param second  the second-of-minute to represent, from 0 to 59
   * @param nanoOfSecond  the nano-of-second to represent, from 0 to 999,999,999
   * @return the local date-time, not null
   * @throws DateTimeException if the value of any field is out of range,
   *                           or if the day-of-month is invalid for the month-year
   */
  def of(year: Int, month: Month, dayOfMonth: Int, hour: Int, minute: Int, second: Int, nanoOfSecond: Int): DateTime = {
    val date: Date = Date.of(year, month, dayOfMonth)
    val time: Time = Time.of(hour, minute, second, nanoOfSecond)
     new DateTime(date, time)
  }

  /**
   * Obtains an instance of {@code DateTime} from year, month,
   * day, hour and minute, setting the second and nanosecond to zero.
   * <p>
   * This returns a {@code DateTime} with the specified year, month,
   * day-of-month, hour and minute.
   * The day must be valid for the year and month, otherwise an exception will be thrown.
   * The second and nanosecond fields will be set to zero.
   *
   * @param year  the year to represent, from MIN_YEAR to MAX_YEAR
   * @param month  the month-of-year to represent, from 1 (January) to 12 (December)
   * @param dayOfMonth  the day-of-month to represent, from 1 to 31
   * @param hour  the hour-of-day to represent, from 0 to 23
   * @param minute  the minute-of-hour to represent, from 0 to 59
   * @return the local date-time, not null
   * @throws DateTimeException if the value of any field is out of range,
   *                           or if the day-of-month is invalid for the month-year
   */
  def of(year: Int, month: Int, dayOfMonth: Int, hour: Int, minute: Int): DateTime = {
    val date: Date = Date.of(year, month, dayOfMonth)
    val time: Time = Time.of(hour, minute)
     new DateTime(date, time)
  }

  /**
   * Obtains an instance of {@code DateTime} from year, month,
   * day, hour, minute and second, setting the nanosecond to zero.
   * <p>
   * This returns a {@code DateTime} with the specified year, month,
   * day-of-month, hour, minute and second.
   * The day must be valid for the year and month, otherwise an exception will be thrown.
   * The nanosecond field will be set to zero.
   *
   * @param year  the year to represent, from MIN_YEAR to MAX_YEAR
   * @param month  the month-of-year to represent, from 1 (January) to 12 (December)
   * @param dayOfMonth  the day-of-month to represent, from 1 to 31
   * @param hour  the hour-of-day to represent, from 0 to 23
   * @param minute  the minute-of-hour to represent, from 0 to 59
   * @param second  the second-of-minute to represent, from 0 to 59
   * @return the local date-time, not null
   * @throws DateTimeException if the value of any field is out of range,
   *                           or if the day-of-month is invalid for the month-year
   */
  def of(year: Int, month: Int, dayOfMonth: Int, hour: Int, minute: Int, second: Int): DateTime = {
    val date: Date = Date.of(year, month, dayOfMonth)
    val time: Time = Time.of(hour, minute, second)
     new DateTime(date, time)
  }

  /**
   * Obtains an instance of {@code DateTime} from year, month,
   * day, hour, minute, second and nanosecond.
   * <p>
   * This returns a {@code DateTime} with the specified year, month,
   * day-of-month, hour, minute, second and nanosecond.
   * The day must be valid for the year and month, otherwise an exception will be thrown.
   *
   * @param year  the year to represent, from MIN_YEAR to MAX_YEAR
   * @param month  the month-of-year to represent, from 1 (January) to 12 (December)
   * @param dayOfMonth  the day-of-month to represent, from 1 to 31
   * @param hour  the hour-of-day to represent, from 0 to 23
   * @param minute  the minute-of-hour to represent, from 0 to 59
   * @param second  the second-of-minute to represent, from 0 to 59
   * @param nanoOfSecond  the nano-of-second to represent, from 0 to 999,999,999
   * @return the local date-time, not null
   * @throws DateTimeException if the value of any field is out of range,
   *                           or if the day-of-month is invalid for the month-year
   */
  def of(year: Int, month: Int, dayOfMonth: Int, hour: Int, minute: Int, second: Int, nanoOfSecond: Int): DateTime = {
    val date: Date = Date.of(year, month, dayOfMonth)
    val time: Time = Time.of(hour, minute, second, nanoOfSecond)
     new DateTime(date, time)
  }

  /**
   * Obtains an instance of {@code DateTime} from a date and time.
   *
   * @param date  the local date, not null
   * @param time  the local time, not null
   * @return the local date-time, not null
   */
  def of(date: Date, time: Time): DateTime = {
    object
    object
     new DateTime(date, time)
  }

  /**
   * Obtains an instance of {@code DateTime} from an {@code Instant} and zone ID.
   * <p>
   * This creates a local date-time based on the specified instant.
   * First, the offset from UTC/Greenwich is obtained using the zone ID and instant,
   * which is simple as there is only one valid offset for each instant.
   * Then, the instant and offset are used to calculate the local date-time.
   *
   * @param instant  the instant to create the date-time from, not null
   * @param zone  the time-zone, which may be an offset, not null
   * @return the local date-time, not null
   * @throws DateTimeException if the result exceeds the supported range
   */
  def ofInstant(instant: Instant, zone: ZoneId): DateTime = {
    object
    object
    val rules: ZoneRules = zone.getRules
    val offset: ZoneOffset = rules.getOffset(instant)
     ofEpochSecond(instant.getEpochSecond, instant.getNano, offset)
  }

  /**
   * Obtains an instance of {@code DateTime} using seconds from the
   * epoch of 1970-01-01T00:00:00Z.
   * <p>
   * This allows the {@link ChronoField#INSTANT_SECONDS epoch-second} field
   * to be converted to a local date-time. This is primarily intended for
   * low-level conversions rather than general application usage.
   *
   * @param epochSecond  the number of seconds from the epoch of 1970-01-01T00:00:00Z
   * @param nanoOfSecond  the nanosecond within the second, from 0 to 999,999,999
   * @param offset  the zone offset, not null
   * @return the local date-time, not null
   * @throws DateTimeException if the result exceeds the supported range,
   *                           or if the nano-of-second is invalid
   */
  def ofEpochSecond(epochSecond: Long, nanoOfSecond: Int, offset: ZoneOffset): DateTime = {
    object
    NANO_OF_SECOND.checkValidValue(nanoOfSecond)
    val localSecond: Long = epochSecond + offset.getTotalSeconds
    val localEpochDay: Long = Math.floorDiv(localSecond, SECONDS_PER_DAY)
    val secsOfDay: Int = Math.floorMod(localSecond, SECONDS_PER_DAY).asInstanceOf[Int]
    val date: Date = Date.ofEpochDay(localEpochDay)
    val time: Time = Time.ofNanoOfDay(secsOfDay * NANOS_PER_SECOND + nanoOfSecond)
     new DateTime(date, time)
  }

  /**
   * Obtains an instance of {@code DateTime} from a temporal object.
   * <p>
   * This obtains an offset time based on the specified temporal.
   * A {@code TemporalAccessor} represents an arbitrary set of date and time information,
   * which this factory converts to an instance of {@code DateTime}.
   * <p>
   * The conversion extracts and combines the {@code Date} and the
   * {@code Time} from the temporal object.
   * Implementations are permitted to perform optimizations such as accessing
   * those fields that are equivalent to the relevant objects.
   * <p>
   * This method matches the signature of the functional interface {@link TemporalQuery}
   * allowing it to be used as a query via method reference, {@code DateTime::from}.
   *
   * @param temporal  the temporal object to convert, not null
   * @return the local date-time, not null
   * @throws DateTimeException if unable to convert to a { @code DateTime}
   */
  def from(temporal: TemporalAccessor): DateTime = {
    if (temporal.isInstanceOf[DateTime]) {
       temporal.asInstanceOf[DateTime]
    }
    else if (temporal.isInstanceOf[ZonedDateTime]) {
       (temporal.asInstanceOf[ZonedDateTime]).toLocalDateTime
    }
    else if (temporal.isInstanceOf[OffsetDateTime]) {
       (temporal.asInstanceOf[OffsetDateTime]).toLocalDateTime
    }
    try {
      val date: Date = Date.from(temporal)
      val time: Time = Time.from(temporal)
       new DateTime(date, time)
    }
    catch {
      case ex: DateTimeException => {
        throw new DateTimeException("Unable to obtain DateTime from TemporalAccessor: " + temporal.getClass, ex)
      }
    }
  }

  /**
   * Obtains an instance of {@code DateTime} from a text string such as {@code 2007-12-03T10:15:30}.
   * <p>
   * The string must represent a valid date-time and is parsed using
   * {@link java.time.format.DateTimeFormatter#ISO_LOCAL_DATE_TIME}.
   *
   * @param text  the text to parse such as "2007-12-03T10:15:30", not null
   * @return the parsed local date-time, not null
   * @throws DateTimeParseException if the text cannot be parsed
   */
  def parse(text: CharSequence): DateTime = {
     parse(text, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
  }

  /**
   * Obtains an instance of {@code DateTime} from a text string using a specific formatter.
   * <p>
   * The text is parsed using the formatter, returning a date-time.
   *
   * @param text  the text to parse, not null
   * @param formatter  the formatter to use, not null
   * @return the parsed local date-time, not null
   * @throws DateTimeParseException if the text cannot be parsed
   */
  def parse(text: CharSequence, formatter: DateTimeFormatter): DateTime = {
    object
     formatter.parse(text, DateTime.from)
  }

  private[time] def readExternal(in: DataInput): DateTime = {
    val date: Date = Date.readExternal(in)
    val time: Time = Time.readExternal(in)
     DateTime.of(date, time)
  }

  /**
   * The minimum supported {@code DateTime}, '-999999999-01-01T00:00:00'.
   * This is the local date-time of midnight at the start of the minimum date.
   * This combines {@link Date#MIN} and {@link Time#MIN}.
   * This could be used by an application as a "far past" date-time.
   */
  final val MIN: DateTime = DateTime.of(Date.MIN, Time.MIN)
  /**
   * The maximum supported {@code DateTime}, '+999999999-12-31T23:59:59.999999999'.
   * This is the local date-time just before midnight at the end of the maximum date.
   * This combines {@link Date#MAX} and {@link Time#MAX}.
   * This could be used by an application as a "far future" date-time.
   */
  final val MAX: DateTime = DateTime.of(Date.MAX, Time.MAX)
  /**
   * Serialization version.
   */
}

case class DateTime(date: Date, time: Time, zone: ZoneId = ZoneId.systemDefault, zoneOffset: ZoneOffset = 0) extends Temporal with TemporalAdjuster with ChronoLocalDateTime[Date]  {


  /**
   * Returns a copy of this date-time with the new date and time, checking
   * to see if a new object is in fact required.
   *
   * @param newDate  the date of the new date-time, not null
   * @param newTime  the time of the new date-time, not null
   * @return the date-time, not null
   */
  private def `with`(newDate: Date, newTime: Time): DateTime = {
    if (date eq newDate && time eq newTime) {
       this
    }
     new DateTime(newDate, newTime)
  }

  /**
   * Checks if the specified field is supported.
   * <p>
   * This checks if this date-time can be queried for the specified field.
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
   * <li>{@code DAY_OF_WEEK}
   * <li>{@code ALIGNED_DAY_OF_WEEK_IN_MONTH}
   * <li>{@code ALIGNED_DAY_OF_WEEK_IN_YEAR}
   * <li>{@code DAY_OF_MONTH}
   * <li>{@code DAY_OF_YEAR}
   * <li>{@code EPOCH_DAY}
   * <li>{@code ALIGNED_WEEK_OF_MONTH}
   * <li>{@code ALIGNED_WEEK_OF_YEAR}
   * <li>{@code MONTH_OF_YEAR}
   * <li>{@code PROLEPTIC_MONTH}
   * <li>{@code YEAR_OF_ERA}
   * <li>{@code YEAR}
   * <li>{@code ERA}
   * </ul>
   * All other {@code ChronoField} instances will  false.
   * <p>
   * If the field is not a {@code ChronoField}, then the result of this method
   * is obtained by invoking {@code TemporalField.isSupportedBy(TemporalAccessor)}
   * passing {@code this} as the argument.
   * Whether the field is supported is determined by the field.
   *
   * @param field  the field to check, null returns false
   * @return true if the field is supported on this date-time, false if not
   */
  def isSupported(field: TemporalField): Boolean = {
    if (field.isInstanceOf[ChronoField]) {
      val f: ChronoField = field.asInstanceOf[ChronoField]
       f.isDateBased || f.isTimeBased
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
   * <li>{@code DAYS}
   * <li>{@code WEEKS}
   * <li>{@code MONTHS}
   * <li>{@code YEARS}
   * <li>{@code DECADES}
   * <li>{@code CENTURIES}
   * <li>{@code MILLENNIA}
   * <li>{@code ERAS}
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
  override def isSupported(unit: TemporalUnit): Boolean = {
     ChronoLocalDateTime.super.isSupported(unit)
  }

  /**
   * Gets the range of valid values for the specified field.
   * <p>
   * The range object expresses the minimum and maximum valid values for a field.
   * This date-time is used to enhance the accuracy of the returned range.
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
      val f: ChronoField = field.asInstanceOf[ChronoField]
       (if (f.isTimeBased) time.range(field) else date.range(field))
    }
     field.rangeRefinedBy(this)
  }

  /**
   * Gets the value of the specified field from this date-time as an {@code int}.
   * <p>
   * This queries this date-time for the value for the specified field.
   * The returned value will always be within the valid range of values for the field.
   * If it is not possible to  the value, because the field is not supported
   * or for some other reason, an exception is thrown.
   * <p>
   * If the field is a {@link ChronoField} then the query is implemented here.
   * The {@link #isSupported(TemporalField) supported fields} will  valid
   * values based on this date-time, except {@code NANO_OF_DAY}, {@code MICRO_OF_DAY},
   * {@code EPOCH_DAY} and {@code PROLEPTIC_MONTH} which are too large to fit in
   * an {@code int} and throw a {@code DateTimeException}.
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
      val f: ChronoField = field.asInstanceOf[ChronoField]
       (if (f.isTimeBased) time.get(field) else date.get(field))
    }
     ChronoLocalDateTime.super.get(field)
  }

  /**
   * Gets the value of the specified field from this date-time as a {@code long}.
   * <p>
   * This queries this date-time for the value for the specified field.
   * If it is not possible to  the value, because the field is not supported
   * or for some other reason, an exception is thrown.
   * <p>
   * If the field is a {@link ChronoField} then the query is implemented here.
   * The {@link #isSupported(TemporalField) supported fields} will  valid
   * values based on this date-time.
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
      val f: ChronoField = field.asInstanceOf[ChronoField]
       (if (f.isTimeBased) time.getLong(field) else date.getLong(field))
    }
     field.getFrom(this)
  }

  /**
   * Gets the year field.
   * <p>
   * This method returns the primitive {@code int} value for the year.
   * <p>
   * The year returned by this method is proleptic as per {@code get(YEAR)}.
   * To obtain the year-of-era, use {@code get(YEAR_OF_ERA)}.
   *
   * @return the year, from MIN_YEAR to MAX_YEAR
   */
  def year: Int = {
     date.getYear
  }

  /**
   * Gets the month-of-year field from 1 to 12.
   * <p>
   * This method returns the month as an {@code int} from 1 to 12.
   * Application code is frequently clearer if the enum {@link Month}
   * is used by calling {@link #getMonth()}.
   *
   * @return the month-of-year, from 1 to 12
   * @see #getMonth()
   */
  def getMonthValue: Int = {
     date.getMonthValue
  }

  /**
   * Gets the month-of-year field using the {@code Month} enum.
   * <p>
   * This method returns the enum {@link Month} for the month.
   * This avoids confusion as to what {@code int} values mean.
   * If you need access to the primitive {@code int} value then the enum
   * provides the {@link Month#getValue() int value}.
   *
   * @return the month-of-year, not null
   * @see #getMonthValue()
   */
  def getMonth: Month = {
     date.getMonth
  }

  /**
   * Gets the day-of-month field.
   * <p>
   * This method returns the primitive {@code int} value for the day-of-month.
   *
   * @return the day-of-month, from 1 to 31
   */
  def getDayOfMonth: Int = {
     date.getDayOfMonth
  }

  /**
   * Gets the day-of-year field.
   * <p>
   * This method returns the primitive {@code int} value for the day-of-year.
   *
   * @return the day-of-year, from 1 to 365, or 366 in a leap year
   */
  def getDayOfYear: Int = {
     date.getDayOfYear
  }

  /**
   * Gets the day-of-week field, which is an enum {@code DayOfWeek}.
   * <p>
   * This method returns the enum {@link DayOfWeek} for the day-of-week.
   * This avoids confusion as to what {@code int} values mean.
   * If you need access to the primitive {@code int} value then the enum
   * provides the {@link DayOfWeek#getValue() int value}.
   * <p>
   * Additional information can be obtained from the {@code DayOfWeek}.
   * This includes textual names of the values.
   *
   * @return the day-of-week, not null
   */
  def getDayOfWeek: DayOfWeek = {
     date.getDayOfWeek
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
   * Returns an adjusted copy of this date-time.
   * <p>
   * This returns a {@code DateTime}, based on this one, with the date-time adjusted.
   * The adjustment takes place using the specified adjuster strategy object.
   * Read the documentation of the adjuster to understand what adjustment will be made.
   * <p>
   * A simple adjuster might simply set the one of the fields, such as the year field.
   * A more complex adjuster might set the date to the last day of the month.
   * A selection of common adjustments is provided in {@link TemporalAdjuster}.
   * These include finding the "last day of the month" and "next Wednesday".
   * Key date-time classes also implement the {@code TemporalAdjuster} interface,
   * such as {@link Month} and {@link java.time.MonthDay MonthDay}.
   * The adjuster is responsible for handling special cases, such as the varying
   * lengths of month and leap years.
   * <p>
   * For example this code returns a date on the last day of July:
   * {{{
   * import static java.time.Month.*;
   * import static java.time.temporal.Adjusters.*;
   *
   * result = localDateTime.with(JULY).with(lastDayOfMonth());
   * }}}
   * <p>
   * The classes {@link Date} and {@link Time} implement {@code TemporalAdjuster},
   * thus this method can be used to change the date, time or offset:
   * {{{
   * result = localDateTime.with(date);
   * result = localDateTime.with(time);
   * }}}
   * <p>
   * The result of this method is obtained by invoking the
   * {@link TemporalAdjuster#adjustInto(Temporal)} method on the
   * specified adjuster passing {@code this} as the argument.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param adjuster the adjuster to use, not null
   * @return a { @code DateTime} based on { @code this} with the adjustment made, not null
   * @throws DateTimeException if the adjustment cannot be made
   * @throws ArithmeticException if numeric overflow occurs
   */
  override def `with`(adjuster: TemporalAdjuster): DateTime = {
    if (adjuster.isInstanceOf[Date]) {
       `with`(adjuster.asInstanceOf[Date], time)
    }
    else if (adjuster.isInstanceOf[Time]) {
       `with`(date, adjuster.asInstanceOf[Time])
    }
    else if (adjuster.isInstanceOf[DateTime]) {
       adjuster.asInstanceOf[DateTime]
    }
     adjuster.adjustInto(this).asInstanceOf[DateTime]
  }

  /**
   * Returns a copy of this date-time with the specified field set to a new value.
   * <p>
   * This returns a {@code DateTime}, based on this one, with the value
   * for the specified field changed.
   * This can be used to change any supported field, such as the year, month or day-of-month.
   * If it is not possible to set the value, because the field is not supported or for
   * some other reason, an exception is thrown.
   * <p>
   * In some cases, changing the specified field can cause the resulting date-time to become invalid,
   * such as changing the month from 31st January to February would make the day-of-month invalid.
   * In cases like this, the field is responsible for resolving the date. Typically it will choose
   * the previous valid date, which would be the last valid day of February in this example.
   * <p>
   * If the field is a {@link ChronoField} then the adjustment is implemented here.
   * The {@link #isSupported(TemporalField) supported fields} will behave as per
   * the matching method on {@link Date#with(TemporalField, long) Date}
   * or {@link Time#with(TemporalField, long) Time}.
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
   * @return a { @code DateTime} based on { @code this} with the specified field set, not null
   * @throws DateTimeException if the field cannot be set
   * @throws UnsupportedTemporalTypeException if the field is not supported
   * @throws ArithmeticException if numeric overflow occurs
   */
  def `with`(field: TemporalField, newValue: Long): DateTime = {
    if (field.isInstanceOf[ChronoField]) {
      val f: ChronoField = field.asInstanceOf[ChronoField]
      if (f.isTimeBased) {
         `with`(date, time.`with`(field, newValue))
      }
      else {
         `with`(date.`with`(field, newValue), time)
      }
    }
     field.adjustInto(this, newValue)
  }

  /**
   * Returns a copy of this {@code DateTime} with the year altered.
   * The time does not affect the calculation and will be the same in the result.
   * If the day-of-month is invalid for the year, it will be changed to the last valid day of the month.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param year  the year to set in the result, from MIN_YEAR to MAX_YEAR
   * @return a { @code DateTime} based on this date-time with the requested year, not null
   * @throws DateTimeException if the year value is invalid
   */
  def withYear(year: Int): DateTime = {
     `with`(date.withYear(year), time)
  }

  /**
   * Returns a copy of this {@code DateTime} with the month-of-year altered.
   * The time does not affect the calculation and will be the same in the result.
   * If the day-of-month is invalid for the year, it will be changed to the last valid day of the month.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param month  the month-of-year to set in the result, from 1 (January) to 12 (December)
   * @return a { @code DateTime} based on this date-time with the requested month, not null
   * @throws DateTimeException if the month-of-year value is invalid
   */
  def withMonth(month: Int): DateTime = {
     `with`(date.withMonth(month), time)
  }

  /**
   * Returns a copy of this {@code DateTime} with the day-of-month altered.
   * If the resulting {@code DateTime} is invalid, an exception is thrown.
   * The time does not affect the calculation and will be the same in the result.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param dayOfMonth  the day-of-month to set in the result, from 1 to 28-31
   * @return a { @code DateTime} based on this date-time with the requested day, not null
   * @throws DateTimeException if the day-of-month value is invalid,
   *                           or if the day-of-month is invalid for the month-year
   */
  def withDayOfMonth(dayOfMonth: Int): DateTime = {
     `with`(date.withDayOfMonth(dayOfMonth), time)
  }

  /**
   * Returns a copy of this {@code DateTime} with the day-of-year altered.
   * If the resulting {@code DateTime} is invalid, an exception is thrown.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param dayOfYear  the day-of-year to set in the result, from 1 to 365-366
   * @return a { @code DateTime} based on this date with the requested day, not null
   * @throws DateTimeException if the day-of-year value is invalid,
   *                           or if the day-of-year is invalid for the year
   */
  def withDayOfYear(dayOfYear: Int): DateTime = {
     `with`(date.withDayOfYear(dayOfYear), time)
  }

  /**
   * Returns a copy of this {@code DateTime} with the hour-of-day value altered.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param hour  the hour-of-day to set in the result, from 0 to 23
   * @return a { @code DateTime} based on this date-time with the requested hour, not null
   * @throws DateTimeException if the hour value is invalid
   */
  def withHour(hour: Int): DateTime = {
    val newTime: Time = time.withHour(hour)
     `with`(date, newTime)
  }

  /**
   * Returns a copy of this {@code DateTime} with the minute-of-hour value altered.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param minute  the minute-of-hour to set in the result, from 0 to 59
   * @return a { @code DateTime} based on this date-time with the requested minute, not null
   * @throws DateTimeException if the minute value is invalid
   */
  def withMinute(minute: Int): DateTime = {
    val newTime: Time = time.withMinute(minute)
     `with`(date, newTime)
  }

  /**
   * Returns a copy of this {@code DateTime} with the second-of-minute value altered.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param second  the second-of-minute to set in the result, from 0 to 59
   * @return a { @code DateTime} based on this date-time with the requested second, not null
   * @throws DateTimeException if the second value is invalid
   */
  def withSecond(second: Int): DateTime = {
    val newTime: Time = time.withSecond(second)
     `with`(date, newTime)
  }

  /**
   * Returns a copy of this {@code DateTime} with the nano-of-second value altered.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param nanoOfSecond  the nano-of-second to set in the result, from 0 to 999,999,999
   * @return a { @code DateTime} based on this date-time with the requested nanosecond, not null
   * @throws DateTimeException if the nano value is invalid
   */
  def withNano(nanoOfSecond: Int): DateTime = {
    val newTime: Time = time.withNano(nanoOfSecond)
     `with`(date, newTime)
  }

  /**
   * Returns a copy of this {@code DateTime} with the time truncated.
   * <p>
   * Truncation returns a copy of the original date-time with fields
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
   * @return a { @code DateTime} based on this date-time with the time truncated, not null
   * @throws DateTimeException if unable to truncate
   * @throws UnsupportedTemporalTypeException if the field is not supported
   */
  def truncatedTo(unit: TemporalUnit): DateTime = {
     `with`(date, time.truncatedTo(unit))
  }

  /**
   * Returns a copy of this date-time with the specified amount added.
   * <p>
   * This returns a {@code DateTime}, based on this one, with the specified amount added.
   * The amount is typically {@link Period} or {@link Duration} but may be
   * any other type implementing the {@link TemporalAmount} interface.
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
   * @return a { @code DateTime} based on this date-time with the addition made, not null
   * @throws DateTimeException if the addition cannot be made
   * @throws ArithmeticException if numeric overflow occurs
   */
  override def plus(amountToAdd: TemporalAmount): DateTime = {
    object
    if (amountToAdd.isInstanceOf[Period]) {
      val periodToAdd: Period = amountToAdd.asInstanceOf[Period]
       `with`(date.plus(periodToAdd), time)
    }
     amountToAdd.addTo(this).asInstanceOf[DateTime]
  }

  /**
   * Returns a copy of this date-time with the specified amount added.
   * <p>
   * This returns a {@code DateTime}, based on this one, with the amount
   * in terms of the unit added. If it is not possible to add the amount, because the
   * unit is not supported or for some other reason, an exception is thrown.
   * <p>
   * If the field is a {@link ChronoUnit} then the addition is implemented here.
   * Date units are added as per {@link Date#plus(long, TemporalUnit)}.
   * Time units are added as per {@link Time#plus(long, TemporalUnit)} with
   * any overflow in days added equivalent to using {@link #plusDays(long)}.
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
   * @return a { @code DateTime} based on this date-time with the specified amount added, not null
   * @throws DateTimeException if the addition cannot be made
   * @throws UnsupportedTemporalTypeException if the unit is not supported
   * @throws ArithmeticException if numeric overflow occurs
   */
  def plus(amountToAdd: Long, unit: TemporalUnit): DateTime = {
    if (unit.isInstanceOf[ChronoUnit]) {
      val f: ChronoUnit = unit.asInstanceOf[ChronoUnit]
      f match {
        case NANOS =>
           plusNanos(amountToAdd)
        case MICROS =>
           plusDays(amountToAdd / MICROS_PER_DAY).plusNanos((amountToAdd % MICROS_PER_DAY) * 1000)
        case MILLIS =>
           plusDays(amountToAdd / MILLIS_PER_DAY).plusNanos((amountToAdd % MILLIS_PER_DAY) * 1000 _000)
        case SECONDS =>
           plusSeconds(amountToAdd)
        case MINUTES =>
           plusMinutes(amountToAdd)
        case HOURS =>
           plusHours(amountToAdd)
        case HALF_DAYS =>
           plusDays(amountToAdd / 256).plusHours((amountToAdd % 256) * 12)
      }
       `with`(date.plus(amountToAdd, unit), time)
    }
     unit.addTo(this, amountToAdd)
  }

  /**
   * Returns a copy of this {@code DateTime} with the specified period in years added.
   * <p>
   * This method adds the specified amount to the years field in three steps:
   * <ol>
   * <li>Add the input years to the year field</li>
   * <li>Check if the resulting date would be invalid</li>
   * <li>Adjust the day-of-month to the last valid day if necessary</li>
   * </ol>
   * <p>
   * For example, 2008-02-29 (leap year) plus one year would result in the
   * invalid date 2009-02-29 (standard year). Instead of returning an invalid
   * result, the last valid day of the month, 2009-02-28, is selected instead.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param years  the years to add, may be negative
   * @return a { @code DateTime} based on this date-time with the years added, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def plusYears(years: Long): DateTime = {
    val newDate: Date = date.plusYears(years)
     `with`(newDate, time)
  }

  /**
   * Returns a copy of this {@code DateTime} with the specified period in months added.
   * <p>
   * This method adds the specified amount to the months field in three steps:
   * <ol>
   * <li>Add the input months to the month-of-year field</li>
   * <li>Check if the resulting date would be invalid</li>
   * <li>Adjust the day-of-month to the last valid day if necessary</li>
   * </ol>
   * <p>
   * For example, 2007-03-31 plus one month would result in the invalid date
   * 2007-04-31. Instead of returning an invalid result, the last valid day
   * of the month, 2007-04-30, is selected instead.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param months  the months to add, may be negative
   * @return a { @code DateTime} based on this date-time with the months added, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def plusMonths(months: Long): DateTime = {
    val newDate: Date = date.plusMonths(months)
     `with`(newDate, time)
  }

  /**
   * Returns a copy of this {@code DateTime} with the specified period in weeks added.
   * <p>
   * This method adds the specified amount in weeks to the days field incrementing
   * the month and year fields as necessary to ensure the result remains valid.
   * The result is only invalid if the maximum/minimum year is exceeded.
   * <p>
   * For example, 2008-12-31 plus one week would result in 2009-01-07.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param weeks  the weeks to add, may be negative
   * @return a { @code DateTime} based on this date-time with the weeks added, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def plusWeeks(weeks: Long): DateTime = {
    val newDate: Date = date.plusWeeks(weeks)
     `with`(newDate, time)
  }

  /**
   * Returns a copy of this {@code DateTime} with the specified period in days added.
   * <p>
   * This method adds the specified amount to the days field incrementing the
   * month and year fields as necessary to ensure the result remains valid.
   * The result is only invalid if the maximum/minimum year is exceeded.
   * <p>
   * For example, 2008-12-31 plus one day would result in 2009-01-01.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param days  the days to add, may be negative
   * @return a { @code DateTime} based on this date-time with the days added, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def plusDays(days: Long): DateTime = {
    val newDate: Date = date.plusDays(days)
     `with`(newDate, time)
  }

  /**
   * Returns a copy of this {@code DateTime} with the specified period in hours added.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param hours  the hours to add, may be negative
   * @return a { @code DateTime} based on this date-time with the hours added, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def plusHours(hours: Long): DateTime = {
     plusWithOverflow(date, hours, 0, 0, 0, 1)
  }

  /**
   * Returns a copy of this {@code DateTime} with the specified period in minutes added.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param minutes  the minutes to add, may be negative
   * @return a { @code DateTime} based on this date-time with the minutes added, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def plusMinutes(minutes: Long): DateTime = {
     plusWithOverflow(date, 0, minutes, 0, 0, 1)
  }

  /**
   * Returns a copy of this {@code DateTime} with the specified period in seconds added.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param seconds  the seconds to add, may be negative
   * @return a { @code DateTime} based on this date-time with the seconds added, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def plusSeconds(seconds: Long): DateTime = {
     plusWithOverflow(date, 0, 0, seconds, 0, 1)
  }

  /**
   * Returns a copy of this {@code DateTime} with the specified period in nanoseconds added.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param nanos  the nanos to add, may be negative
   * @return a { @code DateTime} based on this date-time with the nanoseconds added, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def plusNanos(nanos: Long): DateTime = {
     plusWithOverflow(date, 0, 0, 0, nanos, 1)
  }

  /**
   * Returns a copy of this date-time with the specified amount subtracted.
   * <p>
   * This returns a {@code DateTime}, based on this one, with the specified amount subtracted.
   * The amount is typically {@link Period} or {@link Duration} but may be
   * any other type implementing the {@link TemporalAmount} interface.
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
   * @return a { @code DateTime} based on this date-time with the subtraction made, not null
   * @throws DateTimeException if the subtraction cannot be made
   * @throws ArithmeticException if numeric overflow occurs
   */
  override def minus(amountToSubtract: TemporalAmount): DateTime = {
    object
    if (amountToSubtract.isInstanceOf[Period]) {
      val periodToSubtract: Period = amountToSubtract.asInstanceOf[Period]
       `with`(date.minus(periodToSubtract), time)
    }
     amountToSubtract.subtractFrom(this).asInstanceOf[DateTime]
  }

  /**
   * Returns a copy of this date-time with the specified amount subtracted.
   * <p>
   * This returns a {@code DateTime}, based on this one, with the amount
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
   * @return a { @code DateTime} based on this date-time with the specified amount subtracted, not null
   * @throws DateTimeException if the subtraction cannot be made
   * @throws UnsupportedTemporalTypeException if the unit is not supported
   * @throws ArithmeticException if numeric overflow occurs
   */
  override def minus(amountToSubtract: Long, unit: TemporalUnit): DateTime = {
     (if (amountToSubtract == Long.MIN_VALUE) plus(Long.MAX_VALUE, unit).plus(1, unit) else plus(-amountToSubtract, unit))
  }

  /**
   * Returns a copy of this {@code DateTime} with the specified period in years subtracted.
   * <p>
   * This method subtracts the specified amount from the years field in three steps:
   * <ol>
   * <li>Subtract the input years from the year field</li>
   * <li>Check if the resulting date would be invalid</li>
   * <li>Adjust the day-of-month to the last valid day if necessary</li>
   * </ol>
   * <p>
   * For example, 2008-02-29 (leap year) minus one year would result in the
   * invalid date 2009-02-29 (standard year). Instead of returning an invalid
   * result, the last valid day of the month, 2009-02-28, is selected instead.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param years  the years to subtract, may be negative
   * @return a { @code DateTime} based on this date-time with the years subtracted, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def minusYears(years: Long): DateTime = {
     (if (years == Long.MIN_VALUE) plusYears(Long.MAX_VALUE).plusYears(1) else plusYears(-years))
  }

  /**
   * Returns a copy of this {@code DateTime} with the specified period in months subtracted.
   * <p>
   * This method subtracts the specified amount from the months field in three steps:
   * <ol>
   * <li>Subtract the input months from the month-of-year field</li>
   * <li>Check if the resulting date would be invalid</li>
   * <li>Adjust the day-of-month to the last valid day if necessary</li>
   * </ol>
   * <p>
   * For example, 2007-03-31 minus one month would result in the invalid date
   * 2007-04-31. Instead of returning an invalid result, the last valid day
   * of the month, 2007-04-30, is selected instead.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param months  the months to subtract, may be negative
   * @return a { @code DateTime} based on this date-time with the months subtracted, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def minusMonths(months: Long): DateTime = {
     (if (months == Long.MIN_VALUE) plusMonths(Long.MAX_VALUE).plusMonths(1) else plusMonths(-months))
  }

  /**
   * Returns a copy of this {@code DateTime} with the specified period in weeks subtracted.
   * <p>
   * This method subtracts the specified amount in weeks from the days field decrementing
   * the month and year fields as necessary to ensure the result remains valid.
   * The result is only invalid if the maximum/minimum year is exceeded.
   * <p>
   * For example, 2009-01-07 minus one week would result in 2008-12-31.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param weeks  the weeks to subtract, may be negative
   * @return a { @code DateTime} based on this date-time with the weeks subtracted, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def minusWeeks(weeks: Long): DateTime = {
     (if (weeks == Long.MIN_VALUE) plusWeeks(Long.MAX_VALUE).plusWeeks(1) else plusWeeks(-weeks))
  }

  /**
   * Returns a copy of this {@code DateTime} with the specified period in days subtracted.
   * <p>
   * This method subtracts the specified amount from the days field incrementing the
   * month and year fields as necessary to ensure the result remains valid.
   * The result is only invalid if the maximum/minimum year is exceeded.
   * <p>
   * For example, 2009-01-01 minus one day would result in 2008-12-31.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param days  the days to subtract, may be negative
   * @return a { @code DateTime} based on this date-time with the days subtracted, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def minusDays(days: Long): DateTime = {
     (if (days == Long.MIN_VALUE) plusDays(Long.MAX_VALUE).plusDays(1) else plusDays(-days))
  }

  /**
   * Returns a copy of this {@code DateTime} with the specified period in hours subtracted.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param hours  the hours to subtract, may be negative
   * @return a { @code DateTime} based on this date-time with the hours subtracted, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def minusHours(hours: Long): DateTime = {
     plusWithOverflow(date, hours, 0, 0, 0, -1)
  }

  /**
   * Returns a copy of this {@code DateTime} with the specified period in minutes subtracted.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param minutes  the minutes to subtract, may be negative
   * @return a { @code DateTime} based on this date-time with the minutes subtracted, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def minusMinutes(minutes: Long): DateTime = {
     plusWithOverflow(date, 0, minutes, 0, 0, -1)
  }

  /**
   * Returns a copy of this {@code DateTime} with the specified period in seconds subtracted.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param seconds  the seconds to subtract, may be negative
   * @return a { @code DateTime} based on this date-time with the seconds subtracted, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def minusSeconds(seconds: Long): DateTime = {
     plusWithOverflow(date, 0, 0, seconds, 0, -1)
  }

  /**
   * Returns a copy of this {@code DateTime} with the specified period in nanoseconds subtracted.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param nanos  the nanos to subtract, may be negative
   * @return a { @code DateTime} based on this date-time with the nanoseconds subtracted, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def minusNanos(nanos: Long): DateTime = {
     plusWithOverflow(date, 0, 0, 0, nanos, -1)
  }

  /**
   * Returns a copy of this {@code DateTime} with the specified period added.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param newDate  the new date to base the calculation on, not null
   * @param hours  the hours to add, may be negative
   * @param minutes the minutes to add, may be negative
   * @param seconds the seconds to add, may be negative
   * @param nanos the nanos to add, may be negative
   * @param sign  the sign to determine add or subtract
   * @return the combined result, not null
   */
  private def plusWithOverflow(newDate: Date, hours: Long, minutes: Long, seconds: Long, nanos: Long, sign: Int): DateTime = {
    if ((hours | minutes | seconds | nanos) == 0) {
       `with`(newDate, time)
    }
    var totDays: Long = nanos / NANOS_PER_DAY + seconds / SECONDS_PER_DAY + minutes / MINUTES_PER_DAY + hours / HOURS_PER_DAY
    totDays *= sign
    var totNanos: Long = nanos % NANOS_PER_DAY + (seconds % SECONDS_PER_DAY) * NANOS_PER_SECOND + (minutes % MINUTES_PER_DAY) * NANOS_PER_MINUTE + (hours % HOURS_PER_DAY) * NANOS_PER_HOUR
    val curNoD: Long = time.toNanoOfDay
    totNanos = totNanos * sign + curNoD
    totDays += Math.floorDiv(totNanos, NANOS_PER_DAY)
    val newNoD: Long = Math.floorMod(totNanos, NANOS_PER_DAY)
    val newTime: Time = (if (newNoD == curNoD) time else Time.ofNanoOfDay(newNoD))
     `with`(newDate.plusDays(totDays), newTime)
  }

  /**
   * Queries this date-time using the specified query.
   * <p>
   * This queries this date-time using the specified query strategy object.
   * The {@code TemporalQuery} object defines the logic to be used to
   * obtain the result. Read the documentation of the query to understand
   * what the result of this method will be.
   * <p>
   * The result of this method is obtained by invoking the
   * {@link java.time.temporal.TemporalQuery#queryFrom(TemporalAccessor)} method on the
   * specified query passing {@code this} as the argument.
   *
   * @param <R> the type of the result
   * @param query  the query to invoke, not null
   * @return the query result, null may be returned (defined by the query)
   * @throws DateTimeException if unable to query (defined by the query)
   * @throws ArithmeticException if numeric overflow occurs (defined by the query)
   */
  override def query(query: TemporalQuery[R]): R = {
    if (query eq TemporalQuery.localDate) {
       date.asInstanceOf[R]
    }
     ChronoLocalDateTime.super.query(query)
  }

  /**
   * Adjusts the specified temporal object to have the same date and time as this object.
   * <p>
   * This returns a temporal object of the same observable type as the input
   * with the date and time changed to be the same as this.
   * <p>
   * The adjustment is equivalent to using {@link Temporal#with(TemporalField, long)}
   * twice, passing {@link ChronoField#EPOCH_DAY} and
   * {@link ChronoField#NANO_OF_DAY} as the fields.
   * <p>
   * In most cases, it is clearer to reverse the calling pattern by using
   * {@link Temporal#with(TemporalAdjuster)}:
   * {{{
   * // these two lines are equivalent, but the second approach is recommended
   * temporal = thisLocalDateTime.adjustInto(temporal);
   * temporal = temporal.with(thisLocalDateTime);
   * }}}
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param temporal  the target object to be adjusted, not null
   * @return the adjusted object, not null
   * @throws DateTimeException if unable to make the adjustment
   * @throws ArithmeticException if numeric overflow occurs
   */
  override def adjustInto(temporal: Temporal): Temporal = {
     ChronoLocalDateTime.super.adjustInto(temporal)
  }

  /**
   * Calculates the amount of time until another date-time in terms of the specified unit.
   * <p>
   * This calculates the amount of time between two {@code DateTime}
   * objects in terms of a single {@code TemporalUnit}.
   * The start and end points are {@code this} and the specified date-time.
   * The result will be negative if the end is before the start.
   * The {@code Temporal} passed to this method is converted to a
   * {@code DateTime} using {@link #from(TemporalAccessor)}.
   * For example, the amount in days between two date-times can be calculated
   * using {@code startDateTime.until(endDateTime, DAYS)}.
   * <p>
   * The calculation returns a whole number, representing the number of
   * complete units between the two date-times.
   * For example, the amount in months between 2012-06-15T00:00 and 2012-08-14T23:59
   * will only be one month as it is one minute short of two months.
   * <p>
   * There are two equivalent ways of using this method.
   * The first is to invoke this method.
   * The second is to use {@link TemporalUnit#between(Temporal, Temporal)}:
   * {{{
   * // these two lines are equivalent
   * amount = start.until(end, MONTHS);
   * amount = MONTHS.between(start, end);
   * }}}
   * The choice should be made based on which makes the code more readable.
   * <p>
   * The calculation is implemented in this method for {@link ChronoUnit}.
   * The units {@code NANOS}, {@code MICROS}, {@code MILLIS}, {@code SECONDS},
   * {@code MINUTES}, {@code HOURS} and {@code HALF_DAYS}, {@code DAYS},
   * {@code WEEKS}, {@code MONTHS}, {@code YEARS}, {@code DECADES},
   * {@code CENTURIES}, {@code MILLENNIA} and {@code ERAS} are supported.
   * Other {@code ChronoUnit} values will throw an exception.
   * <p>
   * If the unit is not a {@code ChronoUnit}, then the result of this method
   * is obtained by invoking {@code TemporalUnit.between(Temporal, Temporal)}
   * passing {@code this} as the first argument and the converted input temporal
   * as the second argument.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param endExclusive  the end date, which is converted to a { @code DateTime}, not null
   * @param unit  the unit to measure the amount in, not null
   * @return the amount of time between this date-time and the end date-time
   * @throws DateTimeException if the amount cannot be calculated, or the end
   *                           temporal cannot be converted to a { @code DateTime}
   * @throws UnsupportedTemporalTypeException if the unit is not supported
   * @throws ArithmeticException if numeric overflow occurs
   */
  def until(endExclusive: Temporal, unit: TemporalUnit): Long = {
    val end: DateTime = DateTime.from(endExclusive)
    if (unit.isInstanceOf[ChronoUnit]) {
      if (unit.isTimeBased) {
        var amount: Long = date.daysUntil(end.date)
        if (amount == 0) {
           time.until(end.time, unit)
        }
        var timePart: Long = end.time.toNanoOfDay - time.toNanoOfDay
        if (amount > 0) {
          amount -= 1
          timePart += NANOS_PER_DAY
        }
        else {
          amount += 1
          timePart -= NANOS_PER_DAY
        }
        unit.asInstanceOf[ChronoUnit] match {
          case NANOS =>
            amount = Math.multiplyExact(amount, NANOS_PER_DAY)
            break //todo: break is not supported
          case MICROS =>
            amount = Math.multiplyExact(amount, MICROS_PER_DAY)
            timePart = timePart / 1000
            break //todo: break is not supported
          case MILLIS =>
            amount = Math.multiplyExact(amount, MILLIS_PER_DAY)
            timePart = timePart / 1 _000_000
              break //todo: break is not supported
          case SECONDS =>
            amount = Math.multiplyExact(amount, SECONDS_PER_DAY)
            timePart = timePart / NANOS_PER_SECOND
            break //todo: break is not supported
          case MINUTES =>
            amount = Math.multiplyExact(amount, MINUTES_PER_DAY)
            timePart = timePart / NANOS_PER_MINUTE
            break //todo: break is not supported
          case HOURS =>
            amount = Math.multiplyExact(amount, HOURS_PER_DAY)
            timePart = timePart / NANOS_PER_HOUR
            break //todo: break is not supported
          case HALF_DAYS =>
            amount = Math.multiplyExact(amount, 2)
            timePart = timePart / (NANOS_PER_HOUR * 12)
            break //todo: break is not supported
        }
         Math.addExact(amount, timePart)
      }
      var endDate: Date = end.date
      if (endDate.isAfter(date) && end.time.isBefore(time)) {
        endDate = endDate.minusDays(1)
      }
      else if (endDate.isBefore(date) && end.time.isAfter(time)) {
        endDate = endDate.plusDays(1)
      }
       date.until(endDate, unit)
    }
     unit.between(this, end)
  }

  /**
   * Formats this date-time using the specified formatter.
   * <p>
   * This date-time will be passed to the formatter to produce a string.
   *
   * @param formatter  the formatter to use, not null
   * @return the formatted date-time string, not null
   * @throws DateTimeException if an error occurs during printing
   */
  override def format(formatter: DateTimeFormatter): String = {
    object
     formatter.format(this)
  }

  /**
   * Combines this date-time with an offset to create an {@code OffsetDateTime}.
   * <p>
   * This returns an {@code OffsetDateTime} formed from this date-time at the specified offset.
   * All possible combinations of date-time and offset are valid.
   *
   * @param offset  the offset to combine with, not null
   * @return the offset date-time formed from this date-time and the specified offset, not null
   */
  def atOffset(offset: ZoneOffset): OffsetDateTime = {
     OffsetDateTime.of(this, offset)
  }

  /**
   * Combines this date-time with a time-zone to create a {@code ZonedDateTime}.
   * <p>
   * This returns a {@code ZonedDateTime} formed from this date-time at the
   * specified time-zone. The result will match this date-time as closely as possible.
   * Time-zone rules, such as daylight savings, mean that not every local date-time
   * is valid for the specified zone, thus the local date-time may be adjusted.
   * <p>
   * The local date-time is resolved to a single instant on the time-line.
   * This is achieved by finding a valid offset from UTC/Greenwich for the local
   * date-time as defined by the {@link ZoneRules rules} of the zone ID.
   * <p>
   * In most cases, there is only one valid offset for a local date-time.
   * In the case of an overlap, where clocks are set back, there are two valid offsets.
   * This method uses the earlier offset typically corresponding to "summer".
   * <p>
   * In the case of a gap, where clocks jump forward, there is no valid offset.
   * Instead, the local date-time is adjusted to be later by the length of the gap.
   * For a typical one hour daylight savings change, the local date-time will be
   * moved one hour later into the offset typically corresponding to "summer".
   * <p>
   * To obtain the later offset during an overlap, call
   * {@link ZonedDateTime#withLaterOffsetAtOverlap()} on the result of this method.
   * To throw an exception when there is a gap or overlap, use
   * {@link ZonedDateTime#ofStrict(DateTime, ZoneOffset, ZoneId)}.
   *
   * @param zone  the time-zone to use, not null
   * @return the zoned date-time formed from this date-time, not null
   */
  def atZone(zone: ZoneId): ZonedDateTime = {
     ZonedDateTime.of(this, zone)
  }

  /**
   * Compares this date-time to another date-time.
   * <p>
   * The comparison is primarily based on the date-time, from earliest to latest.
   * It is "consistent with equals", as defined by {@link Comparable}.
   * <p>
   * If all the date-times being compared are instances of {@code DateTime},
   * then the comparison will be entirely based on the date-time.
   * If some dates being compared are in different chronologies, then the
   * chronology is also considered, see {@link ChronoLocalDateTime#compareTo}.
   *
   * @param other  the other date-time to compare to, not null
   * @return the comparator value, negative if less, positive if greater
   */
  override def compareTo(other: ChronoLocalDateTime[_]): Int = {
    if (other.isInstanceOf[DateTime]) {
       compareTo0(other.asInstanceOf[DateTime])
    }
     ChronoLocalDateTime.super.compareTo(other)
  }

  private def compareTo0(other: DateTime): Int = {
    var cmp: Int = date.compareTo0(other.toLocalDate)
    if (cmp == 0) {
      cmp = time.compareTo(other.toLocalTime)
    }
     cmp
  }

  /**
   * Checks if this date-time is after the specified date-time.
   * <p>
   * This checks to see if this date-time represents a point on the
   * local time-line after the other date-time.
   * {{{
   * Date a = DateTime.of(2012, 6, 30, 12, 00);
   * Date b = DateTime.of(2012, 7, 1, 12, 00);
   * a.isAfter(b) == false
   * a.isAfter(a) == false
   * b.isAfter(a) == true
   * }}}
   * <p>
   * This method only considers the position of the two date-times on the local time-line.
   * It does not take into account the chronology, or calendar system.
   * This is different from the comparison in {@link #compareTo(ChronoLocalDateTime)},
   * but is the same approach as {@link ChronoLocalDateTime#timeLineOrder()}.
   *
   * @param other  the other date-time to compare to, not null
   * @return true if this date-time is after the specified date-time
   */
  override def isAfter(other: ChronoLocalDateTime[_]): Boolean = {
    if (other.isInstanceOf[DateTime]) {
       compareTo0(other.asInstanceOf[DateTime]) > 0
    }
     ChronoLocalDateTime.super.isAfter(other)
  }

  /**
   * Checks if this date-time is before the specified date-time.
   * <p>
   * This checks to see if this date-time represents a point on the
   * local time-line before the other date-time.
   * {{{
   * Date a = DateTime.of(2012, 6, 30, 12, 00);
   * Date b = DateTime.of(2012, 7, 1, 12, 00);
   * a.isBefore(b) == true
   * a.isBefore(a) == false
   * b.isBefore(a) == false
   * }}}
   * <p>
   * This method only considers the position of the two date-times on the local time-line.
   * It does not take into account the chronology, or calendar system.
   * This is different from the comparison in {@link #compareTo(ChronoLocalDateTime)},
   * but is the same approach as {@link ChronoLocalDateTime#timeLineOrder()}.
   *
   * @param other  the other date-time to compare to, not null
   * @return true if this date-time is before the specified date-time
   */
  override def isBefore(other: ChronoLocalDateTime[_]): Boolean = {
    if (other.isInstanceOf[DateTime]) {
       compareTo0(other.asInstanceOf[DateTime]) < 0
    }
     ChronoLocalDateTime.super.isBefore(other)
  }

  /**
   * Checks if this date-time is equal to the specified date-time.
   * <p>
   * This checks to see if this date-time represents the same point on the
   * local time-line as the other date-time.
   * {{{
   * Date a = DateTime.of(2012, 6, 30, 12, 00);
   * Date b = DateTime.of(2012, 7, 1, 12, 00);
   * a.isEqual(b) == false
   * a.isEqual(a) == true
   * b.isEqual(a) == false
   * }}}
   * <p>
   * This method only considers the position of the two date-times on the local time-line.
   * It does not take into account the chronology, or calendar system.
   * This is different from the comparison in {@link #compareTo(ChronoLocalDateTime)},
   * but is the same approach as {@link ChronoLocalDateTime#timeLineOrder()}.
   *
   * @param other  the other date-time to compare to, not null
   * @return true if this date-time is equal to the specified date-time
   */
  override def isEqual(other: ChronoLocalDateTime[_]): Boolean = {
    if (other.isInstanceOf[DateTime]) {
       compareTo0(other.asInstanceOf[DateTime]) == 0
    }
     ChronoLocalDateTime.super.isEqual(other)
  }

  /**
   * Checks if this date-time is equal to another date-time.
   * <p>
   * Compares this {@code DateTime} with another ensuring that the date-time is the same.
   * Only objects of type {@code DateTime} are compared, other types  false.
   *
   * @param obj  the object to check, null returns false
   * @return true if this is equal to the other date-time
   */
  override def equals(obj: AnyRef): Boolean = {
    if (this eq obj) {
       true
    }
    if (obj.isInstanceOf[DateTime]) {
      val other: DateTime = obj.asInstanceOf[DateTime]
       (date == other.date) && (time == other.time)
    }
     false
  }

  /**
   * A hash code for this date-time.
   *
   * @return a suitable hash code
   */
  override def hashCode: Int = {
     date.hashCode ^ time.hashCode
  }

  /**
   * Outputs this date-time as a {@code String}, such as {@code 2007-12-03T10:15:30}.
   * <p>
   * The output will be one of the following ISO-8601 formats:
   * <p><ul>
   * <li>{@code uuuu-MM-dd'T'HH:mm}</li>
   * <li>{@code uuuu-MM-dd'T'HH:mm:ss}</li>
   * <li>{@code uuuu-MM-dd'T'HH:mm:ss.SSS}</li>
   * <li>{@code uuuu-MM-dd'T'HH:mm:ss.SSSSSS}</li>
   * <li>{@code uuuu-MM-dd'T'HH:mm:ss.SSSSSSSSS}</li>
   * </ul><p>
   * The format used will be the shortest that outputs the full value of
   * the time where the omitted parts are implied to be zero.
   *
   * @return a string representation of this date-time, not null
   */
  override def toString: String = {
     date.toString + 'T' + time.toString
  }

  /**
   * Writes the object using a
   * <a href="../../serialized-form.html#java.time.Ser">dedicated serialized form</a>.
   * {{{
   * out.writeByte(5);  // identifies this as a DateTime
   * // the <a href="../../serialized-form.html#java.time.Date">date</a> excluding the one byte header
   * // the <a href="../../serialized-form.html#java.time.Time">time</a> excluding the one byte header
   * }}}
   *
   * @return the instance of { @code Ser}, not null
   */
  private def writeReplace: AnyRef = {
     new Ser(Ser.LOCAL_DATE_TIME_TYPE, this)
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
    date.writeExternal(out)
    time.writeExternal(out)
  }

}


/**
 * A date-time with a time-zone in the ISO-8601 calendar system,
 * such as {@code 2007-12-03T10:15:30+01:00 Europe/Paris}.
 * <p>
 * {@code ZonedDateTime} is an immutable representation of a date-time with a time-zone.
 * This class stores all date and time fields, to a precision of nanoseconds,
 * and a time-zone, with a zone offset used to handle ambiguous local date-times.
 * For example, the value
 * "2nd October 2007 at 13:45.30.123456789 +02:00 in the Europe/Paris time-zone"
 * can be stored in a {@code ZonedDateTime}.
 * <p>
 * This class handles conversion from the local time-line of {@code DateTime}
 * to the instant time-line of {@code Instant}.
 * The difference between the two time-lines is the offset from UTC/Greenwich,
 * represented by a {@code ZoneOffset}.
 * <p>
 * Converting between the two time-lines involves calculating the offset using the
 * {@link ZoneRules rules} accessed from the {@code ZoneId}.
 * Obtaining the offset for an instant is simple, as there is exactly one valid
 * offset for each instant. By contrast, obtaining the offset for a local date-time
 * is not straightforward. There are three cases:
 * <p><ul>
 * <li>Normal, with one valid offset. For the vast majority of the year, the normal
 * case applies, where there is a single valid offset for the local date-time.</li>
 * <li>Gap, with zero valid offsets. This is when clocks jump forward typically
 * due to the spring daylight savings change from "winter" to "summer".
 * In a gap there are local date-time values with no valid offset.</li>
 * <li>Overlap, with two valid offsets. This is when clocks are set back typically
 * due to the autumn daylight savings change from "summer" to "winter".
 * In an overlap there are local date-time values with two valid offsets.</li>
 * </ul><p>
 * <p>
 * Any method that converts directly or implicitly from a local date-time to an
 * instant by obtaining the offset has the potential to be complicated.
 * <p>
 * For Gaps, the general strategy is that if the local date-time falls in the
 * middle of a Gap, then the resulting zoned date-time will have a local date-time
 * shifted forwards by the length of the Gap, resulting in a date-time in the later
 * offset, typically "summer" time.
 * <p>
 * For Overlaps, the general strategy is that if the local date-time falls in the
 * middle of an Overlap, then the previous offset will be retained. If there is no
 * previous offset, or the previous offset is invalid, then the earlier offset is
 * used, typically "summer" time.. Two additional methods,
 * {@link #withEarlierOffsetAtOverlap()} and {@link #withLaterOffsetAtOverlap()},
 * help manage the case of an overlap.
 * <p>
 * In terms of design, this class should be viewed primarily as the combination
 * of a {@code DateTime} and a {@code ZoneId}. The {@code ZoneOffset} is
 * a vital, but secondary, piece of information, used to ensure that the class
 * represents an instant, especially during a daylight savings overlap.
 *
 * @implSpec
 * A { @code ZonedDateTime} holds state equivalent to three separate objects,
 *           a { @code DateTime}, a { @code ZoneId} and the resolved { @code ZoneOffset}.
 *                                                                                The offset and local date-time are used to define an instant when necessary.
 *                                                                                The zone ID is used to obtain the rules for how and when the offset changes.
 *                                                                                The offset cannot be freely set, as the zone controls which offsets are valid.
 *                                                                                <p>
 *                                                                                This class is immutable and thread-safe.
 *
 * @since 1.8
 */
object ZonedDateTime {
  /**
   * Obtains the current date-time from the system clock in the default time-zone.
   * <p>
   * This will query the {@link Clock#systemDefaultZone() system clock} in the default
   * time-zone to obtain the current date-time.
   * The zone and offset will be set based on the time-zone in the clock.
   * <p>
   * Using this method will prevent the ability to use an alternate clock for testing
   * because the clock is hard-coded.
   *
   * @return the current date-time using the system clock, not null
   */
  def now: ZonedDateTime = {
     now(Clock.systemDefaultZone)
  }

  /**
   * Obtains the current date-time from the system clock in the specified time-zone.
   * <p>
   * This will query the {@link Clock#system(ZoneId) system clock} to obtain the current date-time.
   * Specifying the time-zone avoids dependence on the default time-zone.
   * The offset will be calculated from the specified time-zone.
   * <p>
   * Using this method will prevent the ability to use an alternate clock for testing
   * because the clock is hard-coded.
   *
   * @param zone  the zone ID to use, not null
   * @return the current date-time using the system clock, not null
   */
  def now(zone: ZoneId): ZonedDateTime = {
     now(Clock.system(zone))
  }

  /**
   * Obtains the current date-time from the specified clock.
   * <p>
   * This will query the specified clock to obtain the current date-time.
   * The zone and offset will be set based on the time-zone in the clock.
   * <p>
   * Using this method allows the use of an alternate clock for testing.
   * The alternate clock may be introduced using {@link Clock dependency injection}.
   *
   * @param clock  the clock to use, not null
   * @return the current date-time, not null
   */
  def now(clock: Clock): ZonedDateTime = {
    object
    val now: Instant = clock.instant
     ofInstant(now, clock.getZone)
  }

  /**
   * Obtains an instance of {@code ZonedDateTime} from a local date and time.
   * <p>
   * This creates a zoned date-time matching the input local date and time as closely as possible.
   * Time-zone rules, such as daylight savings, mean that not every local date-time
   * is valid for the specified zone, thus the local date-time may be adjusted.
   * <p>
   * The local date time and first combined to form a local date-time.
   * The local date-time is then resolved to a single instant on the time-line.
   * This is achieved by finding a valid offset from UTC/Greenwich for the local
   * date-time as defined by the {@link ZoneRules rules} of the zone ID.
   * <p>
   * In most cases, there is only one valid offset for a local date-time.
   * In the case of an overlap, when clocks are set back, there are two valid offsets.
   * This method uses the earlier offset typically corresponding to "summer".
   * <p>
   * In the case of a gap, when clocks jump forward, there is no valid offset.
   * Instead, the local date-time is adjusted to be later by the length of the gap.
   * For a typical one hour daylight savings change, the local date-time will be
   * moved one hour later into the offset typically corresponding to "summer".
   *
   * @param date  the local date, not null
   * @param time  the local time, not null
   * @param zone  the time-zone, not null
   * @return the offset date-time, not null
   */
  def of(date: Date, time: Time, zone: ZoneId): ZonedDateTime = {
     of(DateTime.of(date, time), zone)
  }

  /**
   * Obtains an instance of {@code ZonedDateTime} from a local date-time.
   * <p>
   * This creates a zoned date-time matching the input local date-time as closely as possible.
   * Time-zone rules, such as daylight savings, mean that not every local date-time
   * is valid for the specified zone, thus the local date-time may be adjusted.
   * <p>
   * The local date-time is resolved to a single instant on the time-line.
   * This is achieved by finding a valid offset from UTC/Greenwich for the local
   * date-time as defined by the {@link ZoneRules rules} of the zone ID.
   * <p>
   * In most cases, there is only one valid offset for a local date-time.
   * In the case of an overlap, when clocks are set back, there are two valid offsets.
   * This method uses the earlier offset typically corresponding to "summer".
   * <p>
   * In the case of a gap, when clocks jump forward, there is no valid offset.
   * Instead, the local date-time is adjusted to be later by the length of the gap.
   * For a typical one hour daylight savings change, the local date-time will be
   * moved one hour later into the offset typically corresponding to "summer".
   *
   * @param localDateTime  the local date-time, not null
   * @param zone  the time-zone, not null
   * @return the zoned date-time, not null
   */
  def of(localDateTime: DateTime, zone: ZoneId): ZonedDateTime = {
     ofLocal(localDateTime, zone, null)
  }

  /**
   * Obtains an instance of {@code ZonedDateTime} from a year, month, day,
   * hour, minute, second, nanosecond and time-zone.
   * <p>
   * This creates a zoned date-time matching the local date-time of the seven
   * specified fields as closely as possible.
   * Time-zone rules, such as daylight savings, mean that not every local date-time
   * is valid for the specified zone, thus the local date-time may be adjusted.
   * <p>
   * The local date-time is resolved to a single instant on the time-line.
   * This is achieved by finding a valid offset from UTC/Greenwich for the local
   * date-time as defined by the {@link ZoneRules rules} of the zone ID.
   * <p>
   * In most cases, there is only one valid offset for a local date-time.
   * In the case of an overlap, when clocks are set back, there are two valid offsets.
   * This method uses the earlier offset typically corresponding to "summer".
   * <p>
   * In the case of a gap, when clocks jump forward, there is no valid offset.
   * Instead, the local date-time is adjusted to be later by the length of the gap.
   * For a typical one hour daylight savings change, the local date-time will be
   * moved one hour later into the offset typically corresponding to "summer".
   * <p>
   * This method exists primarily for writing test cases.
   * Non test-code will typically use other methods to create an offset time.
   * {@code DateTime} has five additional convenience variants of the
   * equivalent factory method taking fewer arguments.
   * They are not provided here to reduce the footprint of the API.
   *
   * @param year  the year to represent, from MIN_YEAR to MAX_YEAR
   * @param month  the month-of-year to represent, from 1 (January) to 12 (December)
   * @param dayOfMonth  the day-of-month to represent, from 1 to 31
   * @param hour  the hour-of-day to represent, from 0 to 23
   * @param minute  the minute-of-hour to represent, from 0 to 59
   * @param second  the second-of-minute to represent, from 0 to 59
   * @param nanoOfSecond  the nano-of-second to represent, from 0 to 999,999,999
   * @param zone  the time-zone, not null
   * @return the offset date-time, not null
   * @throws DateTimeException if the value of any field is out of range, or
   *                           if the day-of-month is invalid for the month-year
   */
  def of(year: Int, month: Int, dayOfMonth: Int, hour: Int, minute: Int, second: Int, nanoOfSecond: Int, zone: ZoneId): ZonedDateTime = {
    val dt: DateTime = DateTime.of(year, month, dayOfMonth, hour, minute, second, nanoOfSecond)
     ofLocal(dt, zone, null)
  }

  /**
   * Obtains an instance of {@code ZonedDateTime} from a local date-time
   * using the preferred offset if possible.
   * <p>
   * The local date-time is resolved to a single instant on the time-line.
   * This is achieved by finding a valid offset from UTC/Greenwich for the local
   * date-time as defined by the {@link ZoneRules rules} of the zone ID.
   * <p>
   * In most cases, there is only one valid offset for a local date-time.
   * In the case of an overlap, where clocks are set back, there are two valid offsets.
   * If the preferred offset is one of the valid offsets then it is used.
   * Otherwise the earlier valid offset is used, typically corresponding to "summer".
   * <p>
   * In the case of a gap, where clocks jump forward, there is no valid offset.
   * Instead, the local date-time is adjusted to be later by the length of the gap.
   * For a typical one hour daylight savings change, the local date-time will be
   * moved one hour later into the offset typically corresponding to "summer".
   *
   * @param localDateTime  the local date-time, not null
   * @param zone  the time-zone, not null
   * @param preferredOffset  the zone offset, null if no preference
   * @return the zoned date-time, not null
   */
  def ofLocal(localDateTime: DateTime, zone: ZoneId, preferredOffset: ZoneOffset): ZonedDateTime = {
    object
    object
    if (zone.isInstanceOf[ZoneOffset]) {
       new ZonedDateTime(localDateTime, zone.asInstanceOf[ZoneOffset], zone)
    }
    val rules: ZoneRules = zone.getRules
    val validOffsets: List[ZoneOffset] = rules.getValidOffsets(localDateTime)
    var offset: ZoneOffset = null
    if (validOffsets.size == 1) {
      offset = validOffsets.get(0)
    }
    else if (validOffsets.size == 0) {
      val trans: ZoneOffsetTransition = rules.getTransition(localDateTime)
      localDateTime = localDateTime.plusSeconds(trans.getDuration.getSeconds)
      offset = trans.getOffsetAfter
    }
    else {
      if (preferredOffset != null && validOffsets.contains(preferredOffset)) {
        offset = preferredOffset
      }
      else {
        offset = object
      }
    }
     new ZonedDateTime(localDateTime, offset, zone)
  }

  /**
   * Obtains an instance of {@code ZonedDateTime} from an {@code Instant}.
   * <p>
   * This creates a zoned date-time with the same instant as that specified.
   * Calling {@link #toInstant()} will  an instant equal to the one used here.
   * <p>
   * Converting an instant to a zoned date-time is simple as there is only one valid
   * offset for each instant.
   *
   * @param instant  the instant to create the date-time from, not null
   * @param zone  the time-zone, not null
   * @return the zoned date-time, not null
   * @throws DateTimeException if the result exceeds the supported range
   */
  def ofInstant(instant: Instant, zone: ZoneId): ZonedDateTime = {
    object
    object
     create(instant.getEpochSecond, instant.getNano, zone)
  }

  /**
   * Obtains an instance of {@code ZonedDateTime} from the instant formed by combining
   * the local date-time and offset.
   * <p>
   * This creates a zoned date-time by {@link DateTime#toInstant(ZoneOffset) combining}
   * the {@code DateTime} and {@code ZoneOffset}.
   * This combination uniquely specifies an instant without ambiguity.
   * <p>
   * Converting an instant to a zoned date-time is simple as there is only one valid
   * offset for each instant. If the valid offset is different to the offset specified,
   * the the date-time and offset of the zoned date-time will differ from those specified.
   * <p>
   * If the {@code ZoneId} to be used is a {@code ZoneOffset}, this method is equivalent
   * to {@link #of(DateTime, ZoneId)}.
   *
   * @param localDateTime  the local date-time, not null
   * @param offset  the zone offset, not null
   * @param zone  the time-zone, not null
   * @return the zoned date-time, not null
   */
  def ofInstant(localDateTime: DateTime, offset: ZoneOffset, zone: ZoneId): ZonedDateTime = {
    object
    object
    object
    if (zone.getRules.isValidOffset(localDateTime, offset)) {
       new ZonedDateTime(localDateTime, offset, zone)
    }
     create(localDateTime.toEpochSecond(offset), localDateTime.getNano, zone)
  }

  /**
   * Obtains an instance of {@code ZonedDateTime} using seconds from the
   * epoch of 1970-01-01T00:00:00Z.
   *
   * @param epochSecond  the number of seconds from the epoch of 1970-01-01T00:00:00Z
   * @param nanoOfSecond  the nanosecond within the second, from 0 to 999,999,999
   * @param zone  the time-zone, not null
   * @return the zoned date-time, not null
   * @throws DateTimeException if the result exceeds the supported range
   */
  private def create(epochSecond: Long, nanoOfSecond: Int, zone: ZoneId): ZonedDateTime = {
    val rules: ZoneRules = zone.getRules
    val instant: Instant = Instant.ofEpochSecond(epochSecond, nanoOfSecond)
    val offset: ZoneOffset = rules.getOffset(instant)
    val ldt: DateTime = DateTime.ofEpochSecond(epochSecond, nanoOfSecond, offset)
     new ZonedDateTime(ldt, offset, zone)
  }

  /**
   * Obtains an instance of {@code ZonedDateTime} strictly validating the
   * combination of local date-time, offset and zone ID.
   * <p>
   * This creates a zoned date-time ensuring that the offset is valid for the
   * local date-time according to the rules of the specified zone.
   * If the offset is invalid, an exception is thrown.
   *
   * @param localDateTime  the local date-time, not null
   * @param offset  the zone offset, not null
   * @param zone  the time-zone, not null
   * @return the zoned date-time, not null
   */
  def ofStrict(localDateTime: DateTime, offset: ZoneOffset, zone: ZoneId): ZonedDateTime = {
    object
    object
    object
    val rules: ZoneRules = zone.getRules
    if (rules.isValidOffset(localDateTime, offset) == false) {
      val trans: ZoneOffsetTransition = rules.getTransition(localDateTime)
      if (trans != null && trans.isGap) {
        throw new DateTimeException("DateTime '" + localDateTime + "' does not exist in zone '" + zone + "' due to a gap in the local time-line, typically caused by daylight savings")
      }
      throw new DateTimeException("ZoneOffset '" + offset + "' is not valid for DateTime '" + localDateTime + "' in zone '" + zone + "'")
    }
     new ZonedDateTime(localDateTime, offset, zone)
  }

  /**
   * Obtains an instance of {@code ZonedDateTime} leniently, for advanced use cases,
   * allowing any combination of local date-time, offset and zone ID.
   * <p>
   * This creates a zoned date-time with no checks other than no nulls.
   * This means that the resulting zoned date-time may have an offset that is in conflict
   * with the zone ID.
   * <p>
   * This method is intended for advanced use cases.
   * For example, consider the case where a zoned date-time with valid fields is created
   * and then stored in a database or serialization-based store. At some later point,
   * the object is then re-loaded. However, between those points in time, the government
   * that defined the time-zone has changed the rules, such that the originally stored
   * local date-time now does not occur. This method can be used to create the object
   * in an "invalid" state, despite the change in rules.
   *
   * @param localDateTime  the local date-time, not null
   * @param offset  the zone offset, not null
   * @param zone  the time-zone, not null
   * @return the zoned date-time, not null
   */
  private def ofLenient(localDateTime: DateTime, offset: ZoneOffset, zone: ZoneId): ZonedDateTime = {
    object
    object
    object
    if (zone.isInstanceOf[ZoneOffset] && (offset == zone) == false) {
      throw new IllegalArgumentException("ZoneId must match ZoneOffset")
    }
     new ZonedDateTime(localDateTime, offset, zone)
  }

  /**
   * Obtains an instance of {@code ZonedDateTime} from a temporal object.
   * <p>
   * This obtains a zoned date-time based on the specified temporal.
   * A {@code TemporalAccessor} represents an arbitrary set of date and time information,
   * which this factory converts to an instance of {@code ZonedDateTime}.
   * <p>
   * The conversion will first obtain a {@code ZoneId} from the temporal object,
   * falling back to a {@code ZoneOffset} if necessary. It will then try to obtain
   * an {@code Instant}, falling back to a {@code DateTime} if necessary.
   * The result will be either the combination of {@code ZoneId} or {@code ZoneOffset}
   * with {@code Instant} or {@code DateTime}.
   * Implementations are permitted to perform optimizations such as accessing
   * those fields that are equivalent to the relevant objects.
   * <p>
   * This method matches the signature of the functional interface {@link TemporalQuery}
   * allowing it to be used in queries via method reference, {@code ZonedDateTime::from}.
   *
   * @param temporal  the temporal object to convert, not null
   * @return the zoned date-time, not null
   * @throws DateTimeException if unable to convert to an { @code ZonedDateTime}
   */
  def from(temporal: TemporalAccessor): ZonedDateTime = {
    if (temporal.isInstanceOf[ZonedDateTime]) {
       temporal.asInstanceOf[ZonedDateTime]
    }
    try {
      val zone: ZoneId = ZoneId.from(temporal)
      try {
        val epochSecond: Long = temporal.getLong(INSTANT_SECONDS)
        val nanoOfSecond: Int = temporal.get(NANO_OF_SECOND)
         create(epochSecond, nanoOfSecond, zone)
      }
      catch {
        case ex1: DateTimeException => {
          val ldt: DateTime = DateTime.from(temporal)
           of(ldt, zone)
        }
      }
    }
    catch {
      case ex: DateTimeException => {
        throw new DateTimeException("Unable to create ZonedDateTime from TemporalAccessor: " + temporal.getClass, ex)
      }
    }
  }

  /**
   * Obtains an instance of {@code ZonedDateTime} from a text string such as
   * {@code 2007-12-03T10:15:30+01:00[Europe/Paris]}.
   * <p>
   * The string must represent a valid date-time and is parsed using
   * {@link java.time.format.DateTimeFormatter#ISO_ZONED_DATE_TIME}.
   *
   * @param text  the text to parse such as "2007-12-03T10:15:30+01:00[Europe/Paris]", not null
   * @return the parsed zoned date-time, not null
   * @throws DateTimeParseException if the text cannot be parsed
   */
  def parse(text: CharSequence): ZonedDateTime = {
     parse(text, DateTimeFormatter.ISO_ZONED_DATE_TIME)
  }

  /**
   * Obtains an instance of {@code ZonedDateTime} from a text string using a specific formatter.
   * <p>
   * The text is parsed using the formatter, returning a date-time.
   *
   * @param text  the text to parse, not null
   * @param formatter  the formatter to use, not null
   * @return the parsed zoned date-time, not null
   * @throws DateTimeParseException if the text cannot be parsed
   */
  def parse(text: CharSequence, formatter: DateTimeFormatter): ZonedDateTime = {
    object
     formatter.parse(text, ZonedDateTime.from)
  }

  private[time] def readExternal(in: ObjectInput): ZonedDateTime = {
    val dateTime: DateTime = DateTime.readExternal(in)
    val offset: ZoneOffset = ZoneOffset.readExternal(in)
    val zone: ZoneId = Ser.read(in).asInstanceOf[ZoneId]
     ZonedDateTime.ofLenient(dateTime, offset, zone)
  }


}

final class ZonedDateTime extends Temporal with ChronoZonedDateTime[Date]  {
  /**
   * Constructor.
   *
   * @param dateTime  the date-time, validated as not null
   * @param offset  the zone offset, validated as not null
   * @param zone  the time-zone, validated as not null
   */
  private def this(dateTime: DateTime, offset: ZoneOffset, zone: ZoneId) {

    this.dateTime = dateTime
    this.offset = offset
    this.zone = zone
  }

  /**
   * Resolves the new local date-time using this zone ID, retaining the offset if possible.
   *
   * @param newDateTime  the new local date-time, not null
   * @return the zoned date-time, not null
   */
  private def resolveLocal(newDateTime: DateTime): ZonedDateTime = {
     ofLocal(newDateTime, zone, offset)
  }

  /**
   * Resolves the new local date-time using the offset to identify the instant.
   *
   * @param newDateTime  the new local date-time, not null
   * @return the zoned date-time, not null
   */
  private def resolveInstant(newDateTime: DateTime): ZonedDateTime = {
     ofInstant(newDateTime, offset, zone)
  }

  /**
   * Resolves the offset into this zoned date-time for the with methods.
   * <p>
   * This typically ignores the offset, unless it can be used to switch offset in a DST overlap.
   *
   * @param offset  the offset, not null
   * @return the zoned date-time, not null
   */
  private def resolveOffset(offset: ZoneOffset): ZonedDateTime = {
    if ((offset == this.offset) == false && zone.getRules.isValidOffset(dateTime, offset)) {
       new ZonedDateTime(dateTime, offset, zone)
    }
     this
  }

  /**
   * Checks if the specified field is supported.
   * <p>
   * This checks if this date-time can be queried for the specified field.
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
   * <li>{@code DAY_OF_WEEK}
   * <li>{@code ALIGNED_DAY_OF_WEEK_IN_MONTH}
   * <li>{@code ALIGNED_DAY_OF_WEEK_IN_YEAR}
   * <li>{@code DAY_OF_MONTH}
   * <li>{@code DAY_OF_YEAR}
   * <li>{@code EPOCH_DAY}
   * <li>{@code ALIGNED_WEEK_OF_MONTH}
   * <li>{@code ALIGNED_WEEK_OF_YEAR}
   * <li>{@code MONTH_OF_YEAR}
   * <li>{@code PROLEPTIC_MONTH}
   * <li>{@code YEAR_OF_ERA}
   * <li>{@code YEAR}
   * <li>{@code ERA}
   * <li>{@code INSTANT_SECONDS}
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
   * @return true if the field is supported on this date-time, false if not
   */
  def isSupported(field: TemporalField): Boolean = {
     field.isInstanceOf[ChronoField] || (field != null && field.isSupportedBy(this))
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
   * <li>{@code DAYS}
   * <li>{@code WEEKS}
   * <li>{@code MONTHS}
   * <li>{@code YEARS}
   * <li>{@code DECADES}
   * <li>{@code CENTURIES}
   * <li>{@code MILLENNIA}
   * <li>{@code ERAS}
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
  override def isSupported(unit: TemporalUnit): Boolean = {
     ChronoZonedDateTime.super.isSupported(unit)
  }

  /**
   * Gets the range of valid values for the specified field.
   * <p>
   * The range object expresses the minimum and maximum valid values for a field.
   * This date-time is used to enhance the accuracy of the returned range.
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
      if (field eq INSTANT_SECONDS || field eq OFFSET_SECONDS) {
         field.range
      }
       dateTime.range(field)
    }
     field.rangeRefinedBy(this)
  }

  /**
   * Gets the value of the specified field from this date-time as an {@code int}.
   * <p>
   * This queries this date-time for the value for the specified field.
   * The returned value will always be within the valid range of values for the field.
   * If it is not possible to  the value, because the field is not supported
   * or for some other reason, an exception is thrown.
   * <p>
   * If the field is a {@link ChronoField} then the query is implemented here.
   * The {@link #isSupported(TemporalField) supported fields} will  valid
   * values based on this date-time, except {@code NANO_OF_DAY}, {@code MICRO_OF_DAY},
   * {@code EPOCH_DAY}, {@code PROLEPTIC_MONTH} and {@code INSTANT_SECONDS} which are too
   * large to fit in an {@code int} and throw a {@code DateTimeException}.
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
      field.asInstanceOf[ChronoField] match {
        case INSTANT_SECONDS =>
          throw new UnsupportedTemporalTypeException("Invalid field 'InstantSeconds' for get() method, use getLong() instead")
        case OFFSET_SECONDS =>
           getOffset.getTotalSeconds
      }
       dateTime.get(field)
    }
     ChronoZonedDateTime.super.get(field)
  }

  /**
   * Gets the value of the specified field from this date-time as a {@code long}.
   * <p>
   * This queries this date-time for the value for the specified field.
   * If it is not possible to  the value, because the field is not supported
   * or for some other reason, an exception is thrown.
   * <p>
   * If the field is a {@link ChronoField} then the query is implemented here.
   * The {@link #isSupported(TemporalField) supported fields} will  valid
   * values based on this date-time.
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
  override def getLong(field: TemporalField): Long = {
    if (field.isInstanceOf[ChronoField]) {
      field.asInstanceOf[ChronoField] match {
        case INSTANT_SECONDS =>
           toEpochSecond
        case OFFSET_SECONDS =>
           getOffset.getTotalSeconds
      }
       dateTime.getLong(field)
    }
     field.getFrom(this)
  }

  /**
   * Gets the zone offset, such as '+01:00'.
   * <p>
   * This is the offset of the local date-time from UTC/Greenwich.
   *
   * @return the zone offset, not null
   */
  def getOffset: ZoneOffset = {
     offset
  }

  /**
   * Returns a copy of this date-time changing the zone offset to the
   * earlier of the two valid offsets at a local time-line overlap.
   * <p>
   * This method only has any effect when the local time-line overlaps, such as
   * at an autumn daylight savings cutover. In this scenario, there are two
   * valid offsets for the local date-time. Calling this method will
   * a zoned date-time with the earlier of the two selected.
   * <p>
   * If this method is called when it is not an overlap, {@code this}
   * is returned.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @return a { @code ZonedDateTime} based on this date-time with the earlier offset, not null
   */
  def withEarlierOffsetAtOverlap: ZonedDateTime = {
    val trans: ZoneOffsetTransition = getZone.getRules.getTransition(dateTime)
    if (trans != null && trans.isOverlap) {
      val earlierOffset: ZoneOffset = trans.getOffsetBefore
      if ((earlierOffset == offset) == false) {
         new ZonedDateTime(dateTime, earlierOffset, zone)
      }
    }
     this
  }

  /**
   * Returns a copy of this date-time changing the zone offset to the
   * later of the two valid offsets at a local time-line overlap.
   * <p>
   * This method only has any effect when the local time-line overlaps, such as
   * at an autumn daylight savings cutover. In this scenario, there are two
   * valid offsets for the local date-time. Calling this method will
   * a zoned date-time with the later of the two selected.
   * <p>
   * If this method is called when it is not an overlap, {@code this}
   * is returned.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @return a { @code ZonedDateTime} based on this date-time with the later offset, not null
   */
  def withLaterOffsetAtOverlap: ZonedDateTime = {
    val trans: ZoneOffsetTransition = getZone.getRules.getTransition(toLocalDateTime)
    if (trans != null) {
      val laterOffset: ZoneOffset = trans.getOffsetAfter
      if ((laterOffset == offset) == false) {
         new ZonedDateTime(dateTime, laterOffset, zone)
      }
    }
     this
  }

  /**
   * Gets the time-zone, such as 'Europe/Paris'.
   * <p>
   * This returns the zone ID. This identifies the time-zone {@link ZoneRules rules}
   * that determine when and how the offset from UTC/Greenwich changes.
   * <p>
   * The zone ID may be same as the {@linkplain #getOffset() offset}.
   * If this is true, then any future calculations, such as addition or subtraction,
   * have no complex edge cases due to time-zone rules.
   * See also {@link #withFixedOffsetZone()}.
   *
   * @return the time-zone, not null
   */
  def getZone: ZoneId = {
     zone
  }

  /**
   * Returns a copy of this date-time with a different time-zone,
   * retaining the local date-time if possible.
   * <p>
   * This method changes the time-zone and retains the local date-time.
   * The local date-time is only changed if it is invalid for the new zone,
   * determined using the same approach as
   * {@link #ofLocal(DateTime, ZoneId, ZoneOffset)}.
   * <p>
   * To change the zone and adjust the local date-time,
   * use {@link #withZoneSameInstant(ZoneId)}.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param zone  the time-zone to change to, not null
   * @return a { @code ZonedDateTime} based on this date-time with the requested zone, not null
   */
  def withZoneSameLocal(zone: ZoneId): ZonedDateTime = {
    object
     if ((this.zone == zone)) this else ofLocal(dateTime, zone, offset)
  }

  /**
   * Returns a copy of this date-time with a different time-zone,
   * retaining the instant.
   * <p>
   * This method changes the time-zone and retains the instant.
   * This normally results in a change to the local date-time.
   * <p>
   * This method is based on retaining the same instant, thus gaps and overlaps
   * in the local time-line have no effect on the result.
   * <p>
   * To change the offset while keeping the local time,
   * use {@link #withZoneSameLocal(ZoneId)}.
   *
   * @param zone  the time-zone to change to, not null
   * @return a { @code ZonedDateTime} based on this date-time with the requested zone, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def withZoneSameInstant(zone: ZoneId): ZonedDateTime = {
    object
     if ((this.zone == zone)) this else create(dateTime.toEpochSecond(offset), dateTime.getNano, zone)
  }

  /**
   * Returns a copy of this date-time with the zone ID set to the offset.
   * <p>
   * This returns a zoned date-time where the zone ID is the same as {@link #getOffset()}.
   * The local date-time, offset and instant of the result will be the same as in this date-time.
   * <p>
   * Setting the date-time to a fixed single offset means that any future
   * calculations, such as addition or subtraction, have no complex edge cases
   * due to time-zone rules.
   * This might also be useful when sending a zoned date-time across a network,
   * as most protocols, such as ISO-8601, only handle offsets,
   * and not region-based zone IDs.
   * <p>
   * This is equivalent to {@code ZonedDateTime.of(zdt.toLocalDateTime(), zdt.getOffset())}.
   *
   * @return a { @code ZonedDateTime} with the zone ID set to the offset, not null
   */
  def withFixedOffsetZone: ZonedDateTime = {
     if ((this.zone == offset)) this else new ZonedDateTime(dateTime, offset, offset)
  }

  /**
   * Gets the {@code DateTime} part of this date-time.
   * <p>
   * This returns a {@code DateTime} with the same year, month, day and time
   * as this date-time.
   *
   * @return the local date-time part of this date-time, not null
   */
  def toLocalDateTime: DateTime = {
     dateTime
  }

  /**
   * Gets the {@code Date} part of this date-time.
   * <p>
   * This returns a {@code Date} with the same year, month and day
   * as this date-time.
   *
   * @return the date part of this date-time, not null
   */
  override def toLocalDate: Date = {
     dateTime.toLocalDate
  }

  /**
   * Gets the year field.
   * <p>
   * This method returns the primitive {@code int} value for the year.
   * <p>
   * The year returned by this method is proleptic as per {@code get(YEAR)}.
   * To obtain the year-of-era, use {@code get(YEAR_OF_ERA)}.
   *
   * @return the year, from MIN_YEAR to MAX_YEAR
   */
  def getYear: Int = {
     dateTime.getYear
  }

  /**
   * Gets the month-of-year field from 1 to 12.
   * <p>
   * This method returns the month as an {@code int} from 1 to 12.
   * Application code is frequently clearer if the enum {@link Month}
   * is used by calling {@link #getMonth()}.
   *
   * @return the month-of-year, from 1 to 12
   * @see #getMonth()
   */
  def getMonthValue: Int = {
     dateTime.getMonthValue
  }

  /**
   * Gets the month-of-year field using the {@code Month} enum.
   * <p>
   * This method returns the enum {@link Month} for the month.
   * This avoids confusion as to what {@code int} values mean.
   * If you need access to the primitive {@code int} value then the enum
   * provides the {@link Month#getValue() int value}.
   *
   * @return the month-of-year, not null
   * @see #getMonthValue()
   */
  def getMonth: Month = {
     dateTime.getMonth
  }

  /**
   * Gets the day-of-month field.
   * <p>
   * This method returns the primitive {@code int} value for the day-of-month.
   *
   * @return the day-of-month, from 1 to 31
   */
  def getDayOfMonth: Int = {
     dateTime.getDayOfMonth
  }

  /**
   * Gets the day-of-year field.
   * <p>
   * This method returns the primitive {@code int} value for the day-of-year.
   *
   * @return the day-of-year, from 1 to 365, or 366 in a leap year
   */
  def getDayOfYear: Int = {
     dateTime.getDayOfYear
  }

  /**
   * Gets the day-of-week field, which is an enum {@code DayOfWeek}.
   * <p>
   * This method returns the enum {@link DayOfWeek} for the day-of-week.
   * This avoids confusion as to what {@code int} values mean.
   * If you need access to the primitive {@code int} value then the enum
   * provides the {@link DayOfWeek#getValue() int value}.
   * <p>
   * Additional information can be obtained from the {@code DayOfWeek}.
   * This includes textual names of the values.
   *
   * @return the day-of-week, not null
   */
  def getDayOfWeek: DayOfWeek = {
     dateTime.getDayOfWeek
  }

  /**
   * Gets the {@code Time} part of this date-time.
   * <p>
   * This returns a {@code Time} with the same hour, minute, second and
   * nanosecond as this date-time.
   *
   * @return the time part of this date-time, not null
   */
  override def toLocalTime: Time = {
     dateTime.toLocalTime
  }

  /**
   * Gets the hour-of-day field.
   *
   * @return the hour-of-day, from 0 to 23
   */
  def getHour: Int = {
     dateTime.getHour
  }

  /**
   * Gets the minute-of-hour field.
   *
   * @return the minute-of-hour, from 0 to 59
   */
  def getMinute: Int = {
     dateTime.getMinute
  }

  /**
   * Gets the second-of-minute field.
   *
   * @return the second-of-minute, from 0 to 59
   */
  def getSecond: Int = {
     dateTime.getSecond
  }

  /**
   * Gets the nano-of-second field.
   *
   * @return the nano-of-second, from 0 to 999,999,999
   */
  def getNano: Int = {
     dateTime.getNano
  }

  /**
   * Returns an adjusted copy of this date-time.
   * <p>
   * This returns a {@code ZonedDateTime}, based on this one, with the date-time adjusted.
   * The adjustment takes place using the specified adjuster strategy object.
   * Read the documentation of the adjuster to understand what adjustment will be made.
   * <p>
   * A simple adjuster might simply set the one of the fields, such as the year field.
   * A more complex adjuster might set the date to the last day of the month.
   * A selection of common adjustments is provided in {@link TemporalAdjuster}.
   * These include finding the "last day of the month" and "next Wednesday".
   * Key date-time classes also implement the {@code TemporalAdjuster} interface,
   * such as {@link Month} and {@link java.time.MonthDay MonthDay}.
   * The adjuster is responsible for handling special cases, such as the varying
   * lengths of month and leap years.
   * <p>
   * For example this code returns a date on the last day of July:
   * {{{
   * import static java.time.Month.*;
   * import static java.time.temporal.Adjusters.*;
   *
   * result = zonedDateTime.with(JULY).with(lastDayOfMonth());
   * }}}
   * <p>
   * The classes {@link Date} and {@link Time} implement {@code TemporalAdjuster},
   * thus this method can be used to change the date, time or offset:
   * {{{
   * result = zonedDateTime.with(date);
   * result = zonedDateTime.with(time);
   * }}}
   * <p>
   * {@link ZoneOffset} also implements {@code TemporalAdjuster} however using it
   * as an argument typically has no effect. The offset of a {@code ZonedDateTime} is
   * controlled primarily by the time-zone. As such, changing the offset does not generally
   * make sense, because there is only one valid offset for the local date-time and zone.
   * If the zoned date-time is in a daylight savings overlap, then the offset is used
   * to switch between the two valid offsets. In all other cases, the offset is ignored.
   * <p>
   * The result of this method is obtained by invoking the
   * {@link TemporalAdjuster#adjustInto(Temporal)} method on the
   * specified adjuster passing {@code this} as the argument.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param adjuster the adjuster to use, not null
   * @return a { @code ZonedDateTime} based on { @code this} with the adjustment made, not null
   * @throws DateTimeException if the adjustment cannot be made
   * @throws ArithmeticException if numeric overflow occurs
   */
  override def `with`(adjuster: TemporalAdjuster): ZonedDateTime = {
    if (adjuster.isInstanceOf[Date]) {
       resolveLocal(DateTime.of(adjuster.asInstanceOf[Date], dateTime.toLocalTime))
    }
    else if (adjuster.isInstanceOf[Time]) {
       resolveLocal(DateTime.of(dateTime.toLocalDate, adjuster.asInstanceOf[Time]))
    }
    else if (adjuster.isInstanceOf[DateTime]) {
       resolveLocal(adjuster.asInstanceOf[DateTime])
    }
    else if (adjuster.isInstanceOf[OffsetDateTime]) {
      val odt: OffsetDateTime = adjuster.asInstanceOf[OffsetDateTime]
       ofLocal(odt.toLocalDateTime, zone, odt.getOffset)
    }
    else if (adjuster.isInstanceOf[Instant]) {
      val instant: Instant = adjuster.asInstanceOf[Instant]
       create(instant.getEpochSecond, instant.getNano, zone)
    }
    else if (adjuster.isInstanceOf[ZoneOffset]) {
       resolveOffset(adjuster.asInstanceOf[ZoneOffset])
    }
     adjuster.adjustInto(this).asInstanceOf[ZonedDateTime]
  }

  /**
   * Returns a copy of this date-time with the specified field set to a new value.
   * <p>
   * This returns a {@code ZonedDateTime}, based on this one, with the value
   * for the specified field changed.
   * This can be used to change any supported field, such as the year, month or day-of-month.
   * If it is not possible to set the value, because the field is not supported or for
   * some other reason, an exception is thrown.
   * <p>
   * In some cases, changing the specified field can cause the resulting date-time to become invalid,
   * such as changing the month from 31st January to February would make the day-of-month invalid.
   * In cases like this, the field is responsible for resolving the date. Typically it will choose
   * the previous valid date, which would be the last valid day of February in this example.
   * <p>
   * If the field is a {@link ChronoField} then the adjustment is implemented here.
   * <p>
   * The {@code INSTANT_SECONDS} field will  a date-time with the specified instant.
   * The zone and nano-of-second are unchanged.
   * The result will have an offset derived from the new instant and original zone.
   * If the new instant value is outside the valid range then a {@code DateTimeException} will be thrown.
   * <p>
   * The {@code OFFSET_SECONDS} field will typically be ignored.
   * The offset of a {@code ZonedDateTime} is controlled primarily by the time-zone.
   * As such, changing the offset does not generally make sense, because there is only
   * one valid offset for the local date-time and zone.
   * If the zoned date-time is in a daylight savings overlap, then the offset is used
   * to switch between the two valid offsets. In all other cases, the offset is ignored.
   * If the new offset value is outside the valid range then a {@code DateTimeException} will be thrown.
   * <p>
   * The other {@link #isSupported(TemporalField) supported fields} will behave as per
   * the matching method on {@link DateTime#with(TemporalField, long) DateTime}.
   * The zone is not part of the calculation and will be unchanged.
   * When converting back to {@code ZonedDateTime}, if the local date-time is in an overlap,
   * then the offset will be retained if possible, otherwise the earlier offset will be used.
   * If in a gap, the local date-time will be adjusted forward by the length of the gap.
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
   * @return a { @code ZonedDateTime} based on { @code this} with the specified field set, not null
   * @throws DateTimeException if the field cannot be set
   * @throws UnsupportedTemporalTypeException if the field is not supported
   * @throws ArithmeticException if numeric overflow occurs
   */
  def `with`(field: TemporalField, newValue: Long): ZonedDateTime = {
    if (field.isInstanceOf[ChronoField]) {
      val f: ChronoField = field.asInstanceOf[ChronoField]
      f match {
        case INSTANT_SECONDS =>
           create(newValue, getNano, zone)
        case OFFSET_SECONDS =>
          val offset: ZoneOffset = ZoneOffset.ofTotalSeconds(f.checkValidIntValue(newValue))
           resolveOffset(offset)
      }
       resolveLocal(dateTime.`with`(field, newValue))
    }
     field.adjustInto(this, newValue)
  }

  /**
   * Returns a copy of this {@code ZonedDateTime} with the year value altered.
   * <p>
   * This operates on the local time-line,
   * {@link DateTime#withYear(int) changing the year} of the local date-time.
   * This is then converted back to a {@code ZonedDateTime}, using the zone ID
   * to obtain the offset.
   * <p>
   * When converting back to {@code ZonedDateTime}, if the local date-time is in an overlap,
   * then the offset will be retained if possible, otherwise the earlier offset will be used.
   * If in a gap, the local date-time will be adjusted forward by the length of the gap.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param year  the year to set in the result, from MIN_YEAR to MAX_YEAR
   * @return a { @code ZonedDateTime} based on this date-time with the requested year, not null
   * @throws DateTimeException if the year value is invalid
   */
  def withYear(year: Int): ZonedDateTime = {
     resolveLocal(dateTime.withYear(year))
  }

  /**
   * Returns a copy of this {@code ZonedDateTime} with the month-of-year value altered.
   * <p>
   * This operates on the local time-line,
   * {@link DateTime#withMonth(int) changing the month} of the local date-time.
   * This is then converted back to a {@code ZonedDateTime}, using the zone ID
   * to obtain the offset.
   * <p>
   * When converting back to {@code ZonedDateTime}, if the local date-time is in an overlap,
   * then the offset will be retained if possible, otherwise the earlier offset will be used.
   * If in a gap, the local date-time will be adjusted forward by the length of the gap.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param month  the month-of-year to set in the result, from 1 (January) to 12 (December)
   * @return a { @code ZonedDateTime} based on this date-time with the requested month, not null
   * @throws DateTimeException if the month-of-year value is invalid
   */
  def withMonth(month: Int): ZonedDateTime = {
     resolveLocal(dateTime.withMonth(month))
  }

  /**
   * Returns a copy of this {@code ZonedDateTime} with the day-of-month value altered.
   * <p>
   * This operates on the local time-line,
   * {@link DateTime#withDayOfMonth(int) changing the day-of-month} of the local date-time.
   * This is then converted back to a {@code ZonedDateTime}, using the zone ID
   * to obtain the offset.
   * <p>
   * When converting back to {@code ZonedDateTime}, if the local date-time is in an overlap,
   * then the offset will be retained if possible, otherwise the earlier offset will be used.
   * If in a gap, the local date-time will be adjusted forward by the length of the gap.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param dayOfMonth  the day-of-month to set in the result, from 1 to 28-31
   * @return a { @code ZonedDateTime} based on this date-time with the requested day, not null
   * @throws DateTimeException if the day-of-month value is invalid,
   *                           or if the day-of-month is invalid for the month-year
   */
  def withDayOfMonth(dayOfMonth: Int): ZonedDateTime = {
     resolveLocal(dateTime.withDayOfMonth(dayOfMonth))
  }

  /**
   * Returns a copy of this {@code ZonedDateTime} with the day-of-year altered.
   * <p>
   * This operates on the local time-line,
   * {@link DateTime#withDayOfYear(int) changing the day-of-year} of the local date-time.
   * This is then converted back to a {@code ZonedDateTime}, using the zone ID
   * to obtain the offset.
   * <p>
   * When converting back to {@code ZonedDateTime}, if the local date-time is in an overlap,
   * then the offset will be retained if possible, otherwise the earlier offset will be used.
   * If in a gap, the local date-time will be adjusted forward by the length of the gap.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param dayOfYear  the day-of-year to set in the result, from 1 to 365-366
   * @return a { @code ZonedDateTime} based on this date with the requested day, not null
   * @throws DateTimeException if the day-of-year value is invalid,
   *                           or if the day-of-year is invalid for the year
   */
  def withDayOfYear(dayOfYear: Int): ZonedDateTime = {
     resolveLocal(dateTime.withDayOfYear(dayOfYear))
  }

  /**
   * Returns a copy of this {@code ZonedDateTime} with the hour-of-day value altered.
   * <p>
   * This operates on the local time-line,
   * {@linkplain DateTime#withHour(int) changing the time} of the local date-time.
   * This is then converted back to a {@code ZonedDateTime}, using the zone ID
   * to obtain the offset.
   * <p>
   * When converting back to {@code ZonedDateTime}, if the local date-time is in an overlap,
   * then the offset will be retained if possible, otherwise the earlier offset will be used.
   * If in a gap, the local date-time will be adjusted forward by the length of the gap.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param hour  the hour-of-day to set in the result, from 0 to 23
   * @return a { @code ZonedDateTime} based on this date-time with the requested hour, not null
   * @throws DateTimeException if the hour value is invalid
   */
  def withHour(hour: Int): ZonedDateTime = {
     resolveLocal(dateTime.withHour(hour))
  }

  /**
   * Returns a copy of this {@code ZonedDateTime} with the minute-of-hour value altered.
   * <p>
   * This operates on the local time-line,
   * {@linkplain DateTime#withMinute(int) changing the time} of the local date-time.
   * This is then converted back to a {@code ZonedDateTime}, using the zone ID
   * to obtain the offset.
   * <p>
   * When converting back to {@code ZonedDateTime}, if the local date-time is in an overlap,
   * then the offset will be retained if possible, otherwise the earlier offset will be used.
   * If in a gap, the local date-time will be adjusted forward by the length of the gap.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param minute  the minute-of-hour to set in the result, from 0 to 59
   * @return a { @code ZonedDateTime} based on this date-time with the requested minute, not null
   * @throws DateTimeException if the minute value is invalid
   */
  def withMinute(minute: Int): ZonedDateTime = {
     resolveLocal(dateTime.withMinute(minute))
  }

  /**
   * Returns a copy of this {@code ZonedDateTime} with the second-of-minute value altered.
   * <p>
   * This operates on the local time-line,
   * {@linkplain DateTime#withSecond(int) changing the time} of the local date-time.
   * This is then converted back to a {@code ZonedDateTime}, using the zone ID
   * to obtain the offset.
   * <p>
   * When converting back to {@code ZonedDateTime}, if the local date-time is in an overlap,
   * then the offset will be retained if possible, otherwise the earlier offset will be used.
   * If in a gap, the local date-time will be adjusted forward by the length of the gap.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param second  the second-of-minute to set in the result, from 0 to 59
   * @return a { @code ZonedDateTime} based on this date-time with the requested second, not null
   * @throws DateTimeException if the second value is invalid
   */
  def withSecond(second: Int): ZonedDateTime = {
     resolveLocal(dateTime.withSecond(second))
  }

  /**
   * Returns a copy of this {@code ZonedDateTime} with the nano-of-second value altered.
   * <p>
   * This operates on the local time-line,
   * {@linkplain DateTime#withNano(int) changing the time} of the local date-time.
   * This is then converted back to a {@code ZonedDateTime}, using the zone ID
   * to obtain the offset.
   * <p>
   * When converting back to {@code ZonedDateTime}, if the local date-time is in an overlap,
   * then the offset will be retained if possible, otherwise the earlier offset will be used.
   * If in a gap, the local date-time will be adjusted forward by the length of the gap.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param nanoOfSecond  the nano-of-second to set in the result, from 0 to 999,999,999
   * @return a { @code ZonedDateTime} based on this date-time with the requested nanosecond, not null
   * @throws DateTimeException if the nano value is invalid
   */
  def withNano(nanoOfSecond: Int): ZonedDateTime = {
     resolveLocal(dateTime.withNano(nanoOfSecond))
  }

  /**
   * Returns a copy of this {@code ZonedDateTime} with the time truncated.
   * <p>
   * Truncation returns a copy of the original date-time with fields
   * smaller than the specified unit set to zero.
   * For example, truncating with the {@link ChronoUnit#MINUTES minutes} unit
   * will set the second-of-minute and nano-of-second field to zero.
   * <p>
   * The unit must have a {@linkplain TemporalUnit#getDuration() duration}
   * that divides into the length of a standard day without remainder.
   * This includes all supplied time units on {@link ChronoUnit} and
   * {@link ChronoUnit#DAYS DAYS}. Other units throw an exception.
   * <p>
   * This operates on the local time-line,
   * {@link DateTime#truncatedTo(java.time.temporal.TemporalUnit) truncating}
   * the underlying local date-time. This is then converted back to a
   * {@code ZonedDateTime}, using the zone ID to obtain the offset.
   * <p>
   * When converting back to {@code ZonedDateTime}, if the local date-time is in an overlap,
   * then the offset will be retained if possible, otherwise the earlier offset will be used.
   * If in a gap, the local date-time will be adjusted forward by the length of the gap.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param unit  the unit to truncate to, not null
   * @return a { @code ZonedDateTime} based on this date-time with the time truncated, not null
   * @throws DateTimeException if unable to truncate
   * @throws UnsupportedTemporalTypeException if the unit is not supported
   */
  def truncatedTo(unit: TemporalUnit): ZonedDateTime = {
     resolveLocal(dateTime.truncatedTo(unit))
  }

  /**
   * Returns a copy of this date-time with the specified amount added.
   * <p>
   * This returns a {@code ZonedDateTime}, based on this one, with the specified amount added.
   * The amount is typically {@link Period} or {@link Duration} but may be
   * any other type implementing the {@link TemporalAmount} interface.
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
   * @return a { @code ZonedDateTime} based on this date-time with the addition made, not null
   * @throws DateTimeException if the addition cannot be made
   * @throws ArithmeticException if numeric overflow occurs
   */
  override def plus(amountToAdd: TemporalAmount): ZonedDateTime = {
    object
    if (amountToAdd.isInstanceOf[Period]) {
      val periodToAdd: Period = amountToAdd.asInstanceOf[Period]
       resolveLocal(dateTime.plus(periodToAdd))
    }
     amountToAdd.addTo(this).asInstanceOf[ZonedDateTime]
  }

  /**
   * Returns a copy of this date-time with the specified amount added.
   * <p>
   * This returns a {@code ZonedDateTime}, based on this one, with the amount
   * in terms of the unit added. If it is not possible to add the amount, because the
   * unit is not supported or for some other reason, an exception is thrown.
   * <p>
   * If the field is a {@link ChronoUnit} then the addition is implemented here.
   * The zone is not part of the calculation and will be unchanged in the result.
   * The calculation for date and time units differ.
   * <p>
   * Date units operate on the local time-line.
   * The period is first added to the local date-time, then converted back
   * to a zoned date-time using the zone ID.
   * The conversion uses {@link #ofLocal(DateTime, ZoneId, ZoneOffset)}
   * with the offset before the addition.
   * <p>
   * Time units operate on the instant time-line.
   * The period is first added to the local date-time, then converted back to
   * a zoned date-time using the zone ID.
   * The conversion uses {@link #ofInstant(DateTime, ZoneOffset, ZoneId)}
   * with the offset before the addition.
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
   * @return a { @code ZonedDateTime} based on this date-time with the specified amount added, not null
   * @throws DateTimeException if the addition cannot be made
   * @throws UnsupportedTemporalTypeException if the unit is not supported
   * @throws ArithmeticException if numeric overflow occurs
   */
  def plus(amountToAdd: Long, unit: TemporalUnit): ZonedDateTime = {
    if (unit.isInstanceOf[ChronoUnit]) {
      if (unit.isDateBased) {
         resolveLocal(dateTime.plus(amountToAdd, unit))
      }
      else {
         resolveInstant(dateTime.plus(amountToAdd, unit))
      }
    }
     unit.addTo(this, amountToAdd)
  }

  /**
   * Returns a copy of this {@code ZonedDateTime} with the specified period in years added.
   * <p>
   * This operates on the local time-line,
   * {@link DateTime#plusYears(long) adding years} to the local date-time.
   * This is then converted back to a {@code ZonedDateTime}, using the zone ID
   * to obtain the offset.
   * <p>
   * When converting back to {@code ZonedDateTime}, if the local date-time is in an overlap,
   * then the offset will be retained if possible, otherwise the earlier offset will be used.
   * If in a gap, the local date-time will be adjusted forward by the length of the gap.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param years  the years to add, may be negative
   * @return a { @code ZonedDateTime} based on this date-time with the years added, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def plusYears(years: Long): ZonedDateTime = {
     resolveLocal(dateTime.plusYears(years))
  }

  /**
   * Returns a copy of this {@code ZonedDateTime} with the specified period in months added.
   * <p>
   * This operates on the local time-line,
   * {@link DateTime#plusMonths(long) adding months} to the local date-time.
   * This is then converted back to a {@code ZonedDateTime}, using the zone ID
   * to obtain the offset.
   * <p>
   * When converting back to {@code ZonedDateTime}, if the local date-time is in an overlap,
   * then the offset will be retained if possible, otherwise the earlier offset will be used.
   * If in a gap, the local date-time will be adjusted forward by the length of the gap.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param months  the months to add, may be negative
   * @return a { @code ZonedDateTime} based on this date-time with the months added, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def plusMonths(months: Long): ZonedDateTime = {
     resolveLocal(dateTime.plusMonths(months))
  }

  /**
   * Returns a copy of this {@code ZonedDateTime} with the specified period in weeks added.
   * <p>
   * This operates on the local time-line,
   * {@link DateTime#plusWeeks(long) adding weeks} to the local date-time.
   * This is then converted back to a {@code ZonedDateTime}, using the zone ID
   * to obtain the offset.
   * <p>
   * When converting back to {@code ZonedDateTime}, if the local date-time is in an overlap,
   * then the offset will be retained if possible, otherwise the earlier offset will be used.
   * If in a gap, the local date-time will be adjusted forward by the length of the gap.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param weeks  the weeks to add, may be negative
   * @return a { @code ZonedDateTime} based on this date-time with the weeks added, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def plusWeeks(weeks: Long): ZonedDateTime = {
     resolveLocal(dateTime.plusWeeks(weeks))
  }

  /**
   * Returns a copy of this {@code ZonedDateTime} with the specified period in days added.
   * <p>
   * This operates on the local time-line,
   * {@link DateTime#plusDays(long) adding days} to the local date-time.
   * This is then converted back to a {@code ZonedDateTime}, using the zone ID
   * to obtain the offset.
   * <p>
   * When converting back to {@code ZonedDateTime}, if the local date-time is in an overlap,
   * then the offset will be retained if possible, otherwise the earlier offset will be used.
   * If in a gap, the local date-time will be adjusted forward by the length of the gap.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param days  the days to add, may be negative
   * @return a { @code ZonedDateTime} based on this date-time with the days added, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def plusDays(days: Long): ZonedDateTime = {
     resolveLocal(dateTime.plusDays(days))
  }

  /**
   * Returns a copy of this {@code ZonedDateTime} with the specified period in hours added.
   * <p>
   * This operates on the instant time-line, such that adding one hour will
   * always be a duration of one hour later.
   * This may cause the local date-time to change by an amount other than one hour.
   * Note that this is a different approach to that used by days, months and years,
   * thus adding one day is not the same as adding 24 hours.
   * <p>
   * For example, consider a time-zone where the spring DST cutover means that the
   * local times 01:00 to 01:59 occur twice changing from offset +02:00 to +01:00.
   * <p><ul>
   * <li>Adding one hour to 00:30+02:00 will result in 01:30+02:00
   * <li>Adding one hour to 01:30+02:00 will result in 01:30+01:00
   * <li>Adding one hour to 01:30+01:00 will result in 02:30+01:00
   * <li>Adding three hours to 00:30+02:00 will result in 02:30+01:00
   * </ul><p>
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param hours  the hours to add, may be negative
   * @return a { @code ZonedDateTime} based on this date-time with the hours added, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def plusHours(hours: Long): ZonedDateTime = {
     resolveInstant(dateTime.plusHours(hours))
  }

  /**
   * Returns a copy of this {@code ZonedDateTime} with the specified period in minutes added.
   * <p>
   * This operates on the instant time-line, such that adding one minute will
   * always be a duration of one minute later.
   * This may cause the local date-time to change by an amount other than one minute.
   * Note that this is a different approach to that used by days, months and years.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param minutes  the minutes to add, may be negative
   * @return a { @code ZonedDateTime} based on this date-time with the minutes added, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def plusMinutes(minutes: Long): ZonedDateTime = {
     resolveInstant(dateTime.plusMinutes(minutes))
  }

  /**
   * Returns a copy of this {@code ZonedDateTime} with the specified period in seconds added.
   * <p>
   * This operates on the instant time-line, such that adding one second will
   * always be a duration of one second later.
   * This may cause the local date-time to change by an amount other than one second.
   * Note that this is a different approach to that used by days, months and years.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param seconds  the seconds to add, may be negative
   * @return a { @code ZonedDateTime} based on this date-time with the seconds added, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def plusSeconds(seconds: Long): ZonedDateTime = {
     resolveInstant(dateTime.plusSeconds(seconds))
  }

  /**
   * Returns a copy of this {@code ZonedDateTime} with the specified period in nanoseconds added.
   * <p>
   * This operates on the instant time-line, such that adding one nano will
   * always be a duration of one nano later.
   * This may cause the local date-time to change by an amount other than one nano.
   * Note that this is a different approach to that used by days, months and years.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param nanos  the nanos to add, may be negative
   * @return a { @code ZonedDateTime} based on this date-time with the nanoseconds added, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def plusNanos(nanos: Long): ZonedDateTime = {
     resolveInstant(dateTime.plusNanos(nanos))
  }

  /**
   * Returns a copy of this date-time with the specified amount subtracted.
   * <p>
   * This returns a {@code ZonedDateTime}, based on this one, with the specified amount subtracted.
   * The amount is typically {@link Period} or {@link Duration} but may be
   * any other type implementing the {@link TemporalAmount} interface.
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
   * @return a { @code ZonedDateTime} based on this date-time with the subtraction made, not null
   * @throws DateTimeException if the subtraction cannot be made
   * @throws ArithmeticException if numeric overflow occurs
   */
  override def minus(amountToSubtract: TemporalAmount): ZonedDateTime = {
    object
    if (amountToSubtract.isInstanceOf[Period]) {
      val periodToSubtract: Period = amountToSubtract.asInstanceOf[Period]
       resolveLocal(dateTime.minus(periodToSubtract))
    }
     amountToSubtract.subtractFrom(this).asInstanceOf[ZonedDateTime]
  }

  /**
   * Returns a copy of this date-time with the specified amount subtracted.
   * <p>
   * This returns a {@code ZonedDateTime}, based on this one, with the amount
   * in terms of the unit subtracted. If it is not possible to subtract the amount,
   * because the unit is not supported or for some other reason, an exception is thrown.
   * <p>
   * The calculation for date and time units differ.
   * <p>
   * Date units operate on the local time-line.
   * The period is first subtracted from the local date-time, then converted back
   * to a zoned date-time using the zone ID.
   * The conversion uses {@link #ofLocal(DateTime, ZoneId, ZoneOffset)}
   * with the offset before the subtraction.
   * <p>
   * Time units operate on the instant time-line.
   * The period is first subtracted from the local date-time, then converted back to
   * a zoned date-time using the zone ID.
   * The conversion uses {@link #ofInstant(DateTime, ZoneOffset, ZoneId)}
   * with the offset before the subtraction.
   * <p>
   * This method is equivalent to {@link #plus(long, TemporalUnit)} with the amount negated.
   * See that method for a full description of how addition, and thus subtraction, works.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param amountToSubtract  the amount of the unit to subtract from the result, may be negative
   * @param unit  the unit of the amount to subtract, not null
   * @return a { @code ZonedDateTime} based on this date-time with the specified amount subtracted, not null
   * @throws DateTimeException if the subtraction cannot be made
   * @throws UnsupportedTemporalTypeException if the unit is not supported
   * @throws ArithmeticException if numeric overflow occurs
   */
  override def minus(amountToSubtract: Long, unit: TemporalUnit): ZonedDateTime = {
     (if (amountToSubtract == Long.MIN_VALUE) plus(Long.MAX_VALUE, unit).plus(1, unit) else plus(-amountToSubtract, unit))
  }

  /**
   * Returns a copy of this {@code ZonedDateTime} with the specified period in years subtracted.
   * <p>
   * This operates on the local time-line,
   * {@link DateTime#minusYears(long) subtracting years} to the local date-time.
   * This is then converted back to a {@code ZonedDateTime}, using the zone ID
   * to obtain the offset.
   * <p>
   * When converting back to {@code ZonedDateTime}, if the local date-time is in an overlap,
   * then the offset will be retained if possible, otherwise the earlier offset will be used.
   * If in a gap, the local date-time will be adjusted forward by the length of the gap.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param years  the years to subtract, may be negative
   * @return a { @code ZonedDateTime} based on this date-time with the years subtracted, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def minusYears(years: Long): ZonedDateTime = {
     (if (years == Long.MIN_VALUE) plusYears(Long.MAX_VALUE).plusYears(1) else plusYears(-years))
  }

  /**
   * Returns a copy of this {@code ZonedDateTime} with the specified period in months subtracted.
   * <p>
   * This operates on the local time-line,
   * {@link DateTime#minusMonths(long) subtracting months} to the local date-time.
   * This is then converted back to a {@code ZonedDateTime}, using the zone ID
   * to obtain the offset.
   * <p>
   * When converting back to {@code ZonedDateTime}, if the local date-time is in an overlap,
   * then the offset will be retained if possible, otherwise the earlier offset will be used.
   * If in a gap, the local date-time will be adjusted forward by the length of the gap.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param months  the months to subtract, may be negative
   * @return a { @code ZonedDateTime} based on this date-time with the months subtracted, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def minusMonths(months: Long): ZonedDateTime = {
     (if (months == Long.MIN_VALUE) plusMonths(Long.MAX_VALUE).plusMonths(1) else plusMonths(-months))
  }

  /**
   * Returns a copy of this {@code ZonedDateTime} with the specified period in weeks subtracted.
   * <p>
   * This operates on the local time-line,
   * {@link DateTime#minusWeeks(long) subtracting weeks} to the local date-time.
   * This is then converted back to a {@code ZonedDateTime}, using the zone ID
   * to obtain the offset.
   * <p>
   * When converting back to {@code ZonedDateTime}, if the local date-time is in an overlap,
   * then the offset will be retained if possible, otherwise the earlier offset will be used.
   * If in a gap, the local date-time will be adjusted forward by the length of the gap.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param weeks  the weeks to subtract, may be negative
   * @return a { @code ZonedDateTime} based on this date-time with the weeks subtracted, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def minusWeeks(weeks: Long): ZonedDateTime = {
     (if (weeks == Long.MIN_VALUE) plusWeeks(Long.MAX_VALUE).plusWeeks(1) else plusWeeks(-weeks))
  }

  /**
   * Returns a copy of this {@code ZonedDateTime} with the specified period in days subtracted.
   * <p>
   * This operates on the local time-line,
   * {@link DateTime#minusDays(long) subtracting days} to the local date-time.
   * This is then converted back to a {@code ZonedDateTime}, using the zone ID
   * to obtain the offset.
   * <p>
   * When converting back to {@code ZonedDateTime}, if the local date-time is in an overlap,
   * then the offset will be retained if possible, otherwise the earlier offset will be used.
   * If in a gap, the local date-time will be adjusted forward by the length of the gap.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param days  the days to subtract, may be negative
   * @return a { @code ZonedDateTime} based on this date-time with the days subtracted, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def minusDays(days: Long): ZonedDateTime = {
     (if (days == Long.MIN_VALUE) plusDays(Long.MAX_VALUE).plusDays(1) else plusDays(-days))
  }

  /**
   * Returns a copy of this {@code ZonedDateTime} with the specified period in hours subtracted.
   * <p>
   * This operates on the instant time-line, such that subtracting one hour will
   * always be a duration of one hour earlier.
   * This may cause the local date-time to change by an amount other than one hour.
   * Note that this is a different approach to that used by days, months and years,
   * thus subtracting one day is not the same as adding 24 hours.
   * <p>
   * For example, consider a time-zone where the spring DST cutover means that the
   * local times 01:00 to 01:59 occur twice changing from offset +02:00 to +01:00.
   * <p><ul>
   * <li>Subtracting one hour from 02:30+01:00 will result in 01:30+02:00
   * <li>Subtracting one hour from 01:30+01:00 will result in 01:30+02:00
   * <li>Subtracting one hour from 01:30+02:00 will result in 00:30+01:00
   * <li>Subtracting three hours from 02:30+01:00 will result in 00:30+02:00
   * </ul><p>
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param hours  the hours to subtract, may be negative
   * @return a { @code ZonedDateTime} based on this date-time with the hours subtracted, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def minusHours(hours: Long): ZonedDateTime = {
     (if (hours == Long.MIN_VALUE) plusHours(Long.MAX_VALUE).plusHours(1) else plusHours(-hours))
  }

  /**
   * Returns a copy of this {@code ZonedDateTime} with the specified period in minutes subtracted.
   * <p>
   * This operates on the instant time-line, such that subtracting one minute will
   * always be a duration of one minute earlier.
   * This may cause the local date-time to change by an amount other than one minute.
   * Note that this is a different approach to that used by days, months and years.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param minutes  the minutes to subtract, may be negative
   * @return a { @code ZonedDateTime} based on this date-time with the minutes subtracted, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def minusMinutes(minutes: Long): ZonedDateTime = {
     (if (minutes == Long.MIN_VALUE) plusMinutes(Long.MAX_VALUE).plusMinutes(1) else plusMinutes(-minutes))
  }

  /**
   * Returns a copy of this {@code ZonedDateTime} with the specified period in seconds subtracted.
   * <p>
   * This operates on the instant time-line, such that subtracting one second will
   * always be a duration of one second earlier.
   * This may cause the local date-time to change by an amount other than one second.
   * Note that this is a different approach to that used by days, months and years.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param seconds  the seconds to subtract, may be negative
   * @return a { @code ZonedDateTime} based on this date-time with the seconds subtracted, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def minusSeconds(seconds: Long): ZonedDateTime = {
     (if (seconds == Long.MIN_VALUE) plusSeconds(Long.MAX_VALUE).plusSeconds(1) else plusSeconds(-seconds))
  }

  /**
   * Returns a copy of this {@code ZonedDateTime} with the specified period in nanoseconds subtracted.
   * <p>
   * This operates on the instant time-line, such that subtracting one nano will
   * always be a duration of one nano earlier.
   * This may cause the local date-time to change by an amount other than one nano.
   * Note that this is a different approach to that used by days, months and years.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param nanos  the nanos to subtract, may be negative
   * @return a { @code ZonedDateTime} based on this date-time with the nanoseconds subtracted, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def minusNanos(nanos: Long): ZonedDateTime = {
     (if (nanos == Long.MIN_VALUE) plusNanos(Long.MAX_VALUE).plusNanos(1) else plusNanos(-nanos))
  }

  /**
   * Queries this date-time using the specified query.
   * <p>
   * This queries this date-time using the specified query strategy object.
   * The {@code TemporalQuery} object defines the logic to be used to
   * obtain the result. Read the documentation of the query to understand
   * what the result of this method will be.
   * <p>
   * The result of this method is obtained by invoking the
   * {@link java.time.temporal.TemporalQuery#queryFrom(TemporalAccessor)} method on the
   * specified query passing {@code this} as the argument.
   *
   * @param <R> the type of the result
   * @param query  the query to invoke, not null
   * @return the query result, null may be returned (defined by the query)
   * @throws DateTimeException if unable to query (defined by the query)
   * @throws ArithmeticException if numeric overflow occurs (defined by the query)
   */
  override def query(query: TemporalQuery[R]): R = {
     ChronoZonedDateTime.super.query(query)
  }

  /**
   * Calculates the amount of time until another date-time in terms of the specified unit.
   * <p>
   * This calculates the amount of time between two {@code ZonedDateTime}
   * objects in terms of a single {@code TemporalUnit}.
   * The start and end points are {@code this} and the specified date-time.
   * The result will be negative if the end is before the start.
   * For example, the period in days between two date-times can be calculated
   * using {@code startDateTime.until(endDateTime, DAYS)}.
   * <p>
   * The {@code Temporal} passed to this method is converted to a
   * {@code ZonedDateTime} using {@link #from(TemporalAccessor)}.
   * If the time-zone differs between the two zoned date-times, the specified
   * end date-time is normalized to have the same zone as this date-time.
   * <p>
   * The calculation returns a whole number, representing the number of
   * complete units between the two date-times.
   * For example, the period in months between 2012-06-15T00:00Z and 2012-08-14T23:59Z
   * will only be one month as it is one minute short of two months.
   * <p>
   * There are two equivalent ways of using this method.
   * The first is to invoke this method.
   * The second is to use {@link TemporalUnit#between(Temporal, Temporal)}:
   * {{{
   * // these two lines are equivalent
   * amount = start.until(end, MONTHS);
   * amount = MONTHS.between(start, end);
   * }}}
   * The choice should be made based on which makes the code more readable.
   * <p>
   * The calculation is implemented in this method for {@link ChronoUnit}.
   * The units {@code NANOS}, {@code MICROS}, {@code MILLIS}, {@code SECONDS},
   * {@code MINUTES}, {@code HOURS} and {@code HALF_DAYS}, {@code DAYS},
   * {@code WEEKS}, {@code MONTHS}, {@code YEARS}, {@code DECADES},
   * {@code CENTURIES}, {@code MILLENNIA} and {@code ERAS} are supported.
   * Other {@code ChronoUnit} values will throw an exception.
   * <p>
   * The calculation for date and time units differ.
   * <p>
   * Date units operate on the local time-line, using the local date-time.
   * For example, the period from noon on day 1 to noon the following day
   * in days will always be counted as exactly one day, irrespective of whether
   * there was a daylight savings change or not.
   * <p>
   * Time units operate on the instant time-line.
   * The calculation effectively converts both zoned date-times to instants
   * and then calculates the period between the instants.
   * For example, the period from noon on day 1 to noon the following day
   * in hours may be 23, 24 or 25 hours (or some other amount) depending on
   * whether there was a daylight savings change or not.
   * <p>
   * If the unit is not a {@code ChronoUnit}, then the result of this method
   * is obtained by invoking {@code TemporalUnit.between(Temporal, Temporal)}
   * passing {@code this} as the first argument and the converted input temporal
   * as the second argument.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param endExclusive  the end date, which is converted to a { @code ZonedDateTime}, not null
   * @param unit  the unit to measure the amount in, not null
   * @return the amount of time between this date-time and the end date-time
   * @throws DateTimeException if the amount cannot be calculated, or the end
   *                           temporal cannot be converted to a { @code ZonedDateTime}
   * @throws UnsupportedTemporalTypeException if the unit is not supported
   * @throws ArithmeticException if numeric overflow occurs
   */
  def until(endExclusive: Temporal, unit: TemporalUnit): Long = {
    var end: ZonedDateTime = ZonedDateTime.from(endExclusive)
    if (unit.isInstanceOf[ChronoUnit]) {
      end = end.withZoneSameInstant(zone)
      if (unit.isDateBased) {
         dateTime.until(end.dateTime, unit)
      }
      else {
         toOffsetDateTime.until(end.toOffsetDateTime, unit)
      }
    }
     unit.between(this, end)
  }

  /**
   * Formats this date-time using the specified formatter.
   * <p>
   * This date-time will be passed to the formatter to produce a string.
   *
   * @param formatter  the formatter to use, not null
   * @return the formatted date-time string, not null
   * @throws DateTimeException if an error occurs during printing
   */
  override def format(formatter: DateTimeFormatter): String = {
    object
     formatter.format(this)
  }

  /**
   * Converts this date-time to an {@code OffsetDateTime}.
   * <p>
   * This creates an offset date-time using the local date-time and offset.
   * The zone ID is ignored.
   *
   * @return an offset date-time representing the same local date-time and offset, not null
   */
  def toOffsetDateTime: OffsetDateTime = {
     OffsetDateTime.of(dateTime, offset)
  }

  /**
   * Checks if this date-time is equal to another date-time.
   * <p>
   * The comparison is based on the offset date-time and the zone.
   * Only objects of type {@code ZonedDateTime} are compared, other types  false.
   *
   * @param obj  the object to check, null returns false
   * @return true if this is equal to the other date-time
   */
  override def equals(obj: AnyRef): Boolean = {
    if (this eq obj) {
       true
    }
    if (obj.isInstanceOf[ZonedDateTime]) {
      val other: ZonedDateTime = obj.asInstanceOf[ZonedDateTime]
       (dateTime == other.dateTime) && (offset == other.offset) && (zone == other.zone)
    }
     false
  }

  /**
   * A hash code for this date-time.
   *
   * @return a suitable hash code
   */
  override def hashCode: Int = {
     dateTime.hashCode ^ offset.hashCode ^ Integer.rotateLeft(zone.hashCode, 3)
  }

  /**
   * Outputs this date-time as a {@code String}, such as
   * {@code 2007-12-03T10:15:30+01:00[Europe/Paris]}.
   * <p>
   * The format consists of the {@code DateTime} followed by the {@code ZoneOffset}.
   * If the {@code ZoneId} is not the same as the offset, then the ID is output.
   * The output is compatible with ISO-8601 if the offset and ID are the same.
   *
   * @return a string representation of this date-time, not null
   */
  override def toString: String = {
    var str: String = dateTime.toString + offset.toString
    if (offset ne zone) {
      str += '[' + zone.toString + ']'
    }
     str
  }

  /**
   * Writes the object using a
   * <a href="../../serialized-form.html#java.time.Ser">dedicated serialized form</a>.
   * {{{
   * out.writeByte(6);  // identifies this as a ZonedDateTime
   * // the <a href="../../serialized-form.html#java.time.DateTime">date-time</a> excluding the one byte header
   * // the <a href="../../serialized-form.html#java.time.ZoneOffset">offset</a> excluding the one byte header
   * // the <a href="../../serialized-form.html#java.time.ZoneId">zone ID</a> excluding the one byte header
   * }}}
   *
   * @return the instance of { @code Ser}, not null
   */
  private def writeReplace: AnyRef = {
     new Ser(Ser.ZONE_DATE_TIME_TYPE, this)
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
    dateTime.writeExternal(out)
    offset.writeExternal(out)
    zone.write(out)
  }

  /**
   * The local date-time.
   */
  private final val dateTime: DateTime = null
  /**
   * The offset from UTC/Greenwich.
   */
  private final val offset: ZoneOffset = null
  /**
   * The time-zone.
   */
  private final val zone: ZoneId = null
}

/**
 * A date-time with an offset from UTC/Greenwich in the ISO-8601 calendar system,
 * such as {@code 2007-12-03T10:15:30+01:00}.
 * <p>
 * {@code OffsetDateTime} is an immutable representation of a date-time with an offset.
 * This class stores all date and time fields, to a precision of nanoseconds,
 * as well as the offset from UTC/Greenwich. For example, the value
 * "2nd October 2007 at 13:45.30.123456789 +02:00" can be stored in an {@code OffsetDateTime}.
 * <p>
 * {@code OffsetDateTime}, {@link java.time.ZonedDateTime} and {@link java.time.Instant} all store an instant
 * on the time-line to nanosecond precision.
 * {@code Instant} is the simplest, simply representing the instant.
 * {@code OffsetDateTime} adds to the instant the offset from UTC/Greenwich, which allows
 * the local date-time to be obtained.
 * {@code ZonedDateTime} adds full time-zone rules.
 * <p>
 * It is intended that {@code ZonedDateTime} or {@code Instant} is used to model data
 * in simpler applications. This class may be used when modeling date-time concepts in
 * more detail, or when communicating to a database or in a network protocol.
 *
 * @implSpec
 * This class is immutable and thread-safe.
 *
 * @since 1.8
 */
object OffsetDateTime {
  /**
   * Gets a comparator that compares two {@code OffsetDateTime} instances
   * based solely on the instant.
   * <p>
   * This method differs from the comparison in {@link #compareTo} in that it
   * only compares the underlying instant.
   *
   * @return a comparator that compares in time-line order
   *
   * @see #isAfter
   * @see #isBefore
   * @see #isEqual
   */
  def timeLineOrder: Comparator[OffsetDateTime] = {
     OffsetDateTime.compareInstant
  }

  /**
   * Compares this {@code OffsetDateTime} to another date-time.
   * The comparison is based on the instant.
   *
   * @param datetime1  the first date-time to compare, not null
   * @param datetime2  the other date-time to compare to, not null
   * @return the comparator value, negative if less, positive if greater
   */
  private def compareInstant(datetime1: OffsetDateTime, datetime2: OffsetDateTime): Int = {
    if (datetime1.getOffset == datetime2.getOffset) {
       datetime1.toLocalDateTime.compareTo(datetime2.toLocalDateTime)
    }
    var cmp: Int = Long.compare(datetime1.toEpochSecond, datetime2.toEpochSecond)
    if (cmp == 0) {
      cmp = datetime1.toLocalTime.getNano - datetime2.toLocalTime.getNano
    }
     cmp
  }

  /**
   * Obtains the current date-time from the system clock in the default time-zone.
   * <p>
   * This will query the {@link java.time.Clock#systemDefaultZone() system clock} in the default
   * time-zone to obtain the current date-time.
   * The offset will be calculated from the time-zone in the clock.
   * <p>
   * Using this method will prevent the ability to use an alternate clock for testing
   * because the clock is hard-coded.
   *
   * @return the current date-time using the system clock, not null
   */
  def now: OffsetDateTime = {
     now(Clock.systemDefaultZone)
  }

  /**
   * Obtains the current date-time from the system clock in the specified time-zone.
   * <p>
   * This will query the {@link Clock#system(java.time.ZoneId) system clock} to obtain the current date-time.
   * Specifying the time-zone avoids dependence on the default time-zone.
   * The offset will be calculated from the specified time-zone.
   * <p>
   * Using this method will prevent the ability to use an alternate clock for testing
   * because the clock is hard-coded.
   *
   * @param zone  the zone ID to use, not null
   * @return the current date-time using the system clock, not null
   */
  def now(zone: ZoneId): OffsetDateTime = {
     now(Clock.system(zone))
  }

  /**
   * Obtains the current date-time from the specified clock.
   * <p>
   * This will query the specified clock to obtain the current date-time.
   * The offset will be calculated from the time-zone in the clock.
   * <p>
   * Using this method allows the use of an alternate clock for testing.
   * The alternate clock may be introduced using {@link Clock dependency injection}.
   *
   * @param clock  the clock to use, not null
   * @return the current date-time, not null
   */
  def now(clock: Clock): OffsetDateTime = {
    object
    val now: Instant = clock.instant
     ofInstant(now, clock.getZone.getRules.getOffset(now))
  }

  /**
   * Obtains an instance of {@code OffsetDateTime} from a date, time and offset.
   * <p>
   * This creates an offset date-time with the specified local date, time and offset.
   *
   * @param date  the local date, not null
   * @param time  the local time, not null
   * @param offset  the zone offset, not null
   * @return the offset date-time, not null
   */
  def of(date: Date, time: Time, offset: ZoneOffset): OffsetDateTime = {
    val dt: DateTime = DateTime.of(date, time)
     new OffsetDateTime(dt, offset)
  }

  /**
   * Obtains an instance of {@code OffsetDateTime} from a date-time and offset.
   * <p>
   * This creates an offset date-time with the specified local date-time and offset.
   *
   * @param dateTime  the local date-time, not null
   * @param offset  the zone offset, not null
   * @return the offset date-time, not null
   */
  def of(dateTime: DateTime, offset: ZoneOffset): OffsetDateTime = {
     new OffsetDateTime(dateTime, offset)
  }

  /**
   * Obtains an instance of {@code OffsetDateTime} from a year, month, day,
   * hour, minute, second, nanosecond and offset.
   * <p>
   * This creates an offset date-time with the seven specified fields.
   * <p>
   * This method exists primarily for writing test cases.
   * Non test-code will typically use other methods to create an offset time.
   * {@code DateTime} has five additional convenience variants of the
   * equivalent factory method taking fewer arguments.
   * They are not provided here to reduce the footprint of the API.
   *
   * @param year  the year to represent, from MIN_YEAR to MAX_YEAR
   * @param month  the month-of-year to represent, from 1 (January) to 12 (December)
   * @param dayOfMonth  the day-of-month to represent, from 1 to 31
   * @param hour  the hour-of-day to represent, from 0 to 23
   * @param minute  the minute-of-hour to represent, from 0 to 59
   * @param second  the second-of-minute to represent, from 0 to 59
   * @param nanoOfSecond  the nano-of-second to represent, from 0 to 999,999,999
   * @param offset  the zone offset, not null
   * @return the offset date-time, not null
   * @throws DateTimeException if the value of any field is out of range, or
   *                           if the day-of-month is invalid for the month-year
   */
  def of(year: Int, month: Int, dayOfMonth: Int, hour: Int, minute: Int, second: Int, nanoOfSecond: Int, offset: ZoneOffset): OffsetDateTime = {
    val dt: DateTime = DateTime.of(year, month, dayOfMonth, hour, minute, second, nanoOfSecond)
     new OffsetDateTime(dt, offset)
  }

  /**
   * Obtains an instance of {@code OffsetDateTime} from an {@code Instant} and zone ID.
   * <p>
   * This creates an offset date-time with the same instant as that specified.
   * Finding the offset from UTC/Greenwich is simple as there is only one valid
   * offset for each instant.
   *
   * @param instant  the instant to create the date-time from, not null
   * @param zone  the time-zone, which may be an offset, not null
   * @return the offset date-time, not null
   * @throws DateTimeException if the result exceeds the supported range
   */
  def ofInstant(instant: Instant, zone: ZoneId): OffsetDateTime = {
    object
    object
    val rules: ZoneRules = zone.getRules
    val offset: ZoneOffset = rules.getOffset(instant)
    val ldt: DateTime = DateTime.ofEpochSecond(instant.getEpochSecond, instant.getNano, offset)
     new OffsetDateTime(ldt, offset)
  }

  /**
   * Obtains an instance of {@code OffsetDateTime} from a temporal object.
   * <p>
   * This obtains an offset date-time based on the specified temporal.
   * A {@code TemporalAccessor} represents an arbitrary set of date and time information,
   * which this factory converts to an instance of {@code OffsetDateTime}.
   * <p>
   * The conversion will first obtain a {@code ZoneOffset} from the temporal object.
   * It will then try to obtain a {@code DateTime}, falling back to an {@code Instant} if necessary.
   * The result will be the combination of {@code ZoneOffset} with either
   * with {@code DateTime} or {@code Instant}.
   * Implementations are permitted to perform optimizations such as accessing
   * those fields that are equivalent to the relevant objects.
   * <p>
   * This method matches the signature of the functional interface {@link TemporalQuery}
   * allowing it to be used in queries via method reference, {@code OffsetDateTime::from}.
   *
   * @param temporal  the temporal object to convert, not null
   * @return the offset date-time, not null
   * @throws DateTimeException if unable to convert to an { @code OffsetDateTime}
   */
  def from(temporal: TemporalAccessor): OffsetDateTime = {
    if (temporal.isInstanceOf[OffsetDateTime]) {
       temporal.asInstanceOf[OffsetDateTime]
    }
    val offset: ZoneOffset = ZoneOffset.from(temporal)
    try {
      try {
        val ldt: DateTime = DateTime.from(temporal)
         OffsetDateTime.of(ldt, offset)
      }
      catch {
        case ignore: DateTimeException => {
          val instant: Instant = Instant.from(temporal)
           OffsetDateTime.ofInstant(instant, offset)
        }
      }
    }
    catch {
      case ex: DateTimeException => {
        throw new DateTimeException("Unable to obtain OffsetDateTime from TemporalAccessor: " + temporal.getClass, ex)
      }
    }
  }

  /**
   * Obtains an instance of {@code OffsetDateTime} from a text string
   * such as {@code 2007-12-03T10:15:30+01:00}.
   * <p>
   * The string must represent a valid date-time and is parsed using
   * {@link java.time.format.DateTimeFormatter#ISO_OFFSET_DATE_TIME}.
   *
   * @param text  the text to parse such as "2007-12-03T10:15:30+01:00", not null
   * @return the parsed offset date-time, not null
   * @throws DateTimeParseException if the text cannot be parsed
   */
  def parse(text: CharSequence): OffsetDateTime = {
     parse(text, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
  }

  /**
   * Obtains an instance of {@code OffsetDateTime} from a text string using a specific formatter.
   * <p>
   * The text is parsed using the formatter, returning a date-time.
   *
   * @param text  the text to parse, not null
   * @param formatter  the formatter to use, not null
   * @return the parsed offset date-time, not null
   * @throws DateTimeParseException if the text cannot be parsed
   */
  def parse(text: CharSequence, formatter: DateTimeFormatter): OffsetDateTime = {
    object
     formatter.parse(text, OffsetDateTime.from)
  }

  private[time] def readExternal(in: ObjectInput): OffsetDateTime = {
    val dateTime: DateTime = in.readObject.asInstanceOf[DateTime]
    val offset: ZoneOffset = in.readObject.asInstanceOf[ZoneOffset]
     OffsetDateTime.of(dateTime, offset)
  }

  /**
   * The minimum supported {@code OffsetDateTime}, '-999999999-01-01T00:00:00+18:00'.
   * This is the local date-time of midnight at the start of the minimum date
   * in the maximum offset (larger offsets are earlier on the time-line).
   * This combines {@link DateTime#MIN} and {@link ZoneOffset#MAX}.
   * This could be used by an application as a "far past" date-time.
   */
  final val MIN: OffsetDateTime = DateTime.MIN.atOffset(ZoneOffset.MAX)
  /**
   * The maximum supported {@code OffsetDateTime}, '+999999999-12-31T23:59:59.999999999-18:00'.
   * This is the local date-time just before midnight at the end of the maximum date
   * in the minimum offset (larger negative offsets are later on the time-line).
   * This combines {@link DateTime#MAX} and {@link ZoneOffset#MIN}.
   * This could be used by an application as a "far future" date-time.
   */
  final val MAX: OffsetDateTime = DateTime.MAX.atOffset(ZoneOffset.MIN)
  /**
   * Serialization version.
   */
  private final val serialVersionUID: Long = 2287754244819255394L
}

final class OffsetDateTime extends Temporal with TemporalAdjuster with Comparable[OffsetDateTime]  {
  /**
   * Constructor.
   *
   * @param dateTime  the local date-time, not null
   * @param offset  the zone offset, not null
   */
  private def this(dateTime: DateTime, offset: ZoneOffset) {

    this.dateTime = object
    this.offset = object
  }

  /**
   * Returns a new date-time based on this one, returning {@code this} where possible.
   *
   * @param dateTime  the date-time to create with, not null
   * @param offset  the zone offset to create with, not null
   */
  private def `with`(dateTime: DateTime, offset: ZoneOffset): OffsetDateTime = {
    if (this.dateTime eq dateTime && (this.offset == offset)) {
       this
    }
     new OffsetDateTime(dateTime, offset)
  }

  /**
   * Checks if the specified field is supported.
   * <p>
   * This checks if this date-time can be queried for the specified field.
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
   * <li>{@code DAY_OF_WEEK}
   * <li>{@code ALIGNED_DAY_OF_WEEK_IN_MONTH}
   * <li>{@code ALIGNED_DAY_OF_WEEK_IN_YEAR}
   * <li>{@code DAY_OF_MONTH}
   * <li>{@code DAY_OF_YEAR}
   * <li>{@code EPOCH_DAY}
   * <li>{@code ALIGNED_WEEK_OF_MONTH}
   * <li>{@code ALIGNED_WEEK_OF_YEAR}
   * <li>{@code MONTH_OF_YEAR}
   * <li>{@code PROLEPTIC_MONTH}
   * <li>{@code YEAR_OF_ERA}
   * <li>{@code YEAR}
   * <li>{@code ERA}
   * <li>{@code INSTANT_SECONDS}
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
   * @return true if the field is supported on this date-time, false if not
   */
  def isSupported(field: TemporalField): Boolean = {
     field.isInstanceOf[ChronoField] || (field != null && field.isSupportedBy(this))
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
   * <li>{@code DAYS}
   * <li>{@code WEEKS}
   * <li>{@code MONTHS}
   * <li>{@code YEARS}
   * <li>{@code DECADES}
   * <li>{@code CENTURIES}
   * <li>{@code MILLENNIA}
   * <li>{@code ERAS}
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
       unit ne FOREVER
    }
     unit != null && unit.isSupportedBy(this)
  }

  /**
   * Gets the range of valid values for the specified field.
   * <p>
   * The range object expresses the minimum and maximum valid values for a field.
   * This date-time is used to enhance the accuracy of the returned range.
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
      if (field eq INSTANT_SECONDS || field eq OFFSET_SECONDS) {
         field.range
      }
       dateTime.range(field)
    }
     field.rangeRefinedBy(this)
  }

  /**
   * Gets the value of the specified field from this date-time as an {@code int}.
   * <p>
   * This queries this date-time for the value for the specified field.
   * The returned value will always be within the valid range of values for the field.
   * If it is not possible to  the value, because the field is not supported
   * or for some other reason, an exception is thrown.
   * <p>
   * If the field is a {@link ChronoField} then the query is implemented here.
   * The {@link #isSupported(TemporalField) supported fields} will  valid
   * values based on this date-time, except {@code NANO_OF_DAY}, {@code MICRO_OF_DAY},
   * {@code EPOCH_DAY}, {@code PROLEPTIC_MONTH} and {@code INSTANT_SECONDS} which are too
   * large to fit in an {@code int} and throw a {@code DateTimeException}.
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
      field.asInstanceOf[ChronoField] match {
        case INSTANT_SECONDS =>
          throw new UnsupportedTemporalTypeException("Invalid field 'InstantSeconds' for get() method, use getLong() instead")
        case OFFSET_SECONDS =>
           getOffset.getTotalSeconds
      }
       dateTime.get(field)
    }
     Temporal.super.get(field)
  }

  /**
   * Gets the value of the specified field from this date-time as a {@code long}.
   * <p>
   * This queries this date-time for the value for the specified field.
   * If it is not possible to  the value, because the field is not supported
   * or for some other reason, an exception is thrown.
   * <p>
   * If the field is a {@link ChronoField} then the query is implemented here.
   * The {@link #isSupported(TemporalField) supported fields} will  valid
   * values based on this date-time.
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
      field.asInstanceOf[ChronoField] match {
        case INSTANT_SECONDS =>
           toEpochSecond
        case OFFSET_SECONDS =>
           getOffset.getTotalSeconds
      }
       dateTime.getLong(field)
    }
     field.getFrom(this)
  }

  /**
   * Gets the zone offset, such as '+01:00'.
   * <p>
   * This is the offset of the local date-time from UTC/Greenwich.
   *
   * @return the zone offset, not null
   */
  def getOffset: ZoneOffset = {
     offset
  }

  /**
   * Returns a copy of this {@code OffsetDateTime} with the specified offset ensuring
   * that the result has the same local date-time.
   * <p>
   * This method returns an object with the same {@code DateTime} and the specified {@code ZoneOffset}.
   * No calculation is needed or performed.
   * For example, if this time represents {@code 2007-12-03T10:30+02:00} and the offset specified is
   * {@code +03:00}, then this method will  {@code 2007-12-03T10:30+03:00}.
   * <p>
   * To take into account the difference between the offsets, and adjust the time fields,
   * use {@link #withOffsetSameInstant}.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param offset  the zone offset to change to, not null
   * @return an { @code OffsetDateTime} based on this date-time with the requested offset, not null
   */
  def withOffsetSameLocal(offset: ZoneOffset): OffsetDateTime = {
     `with`(dateTime, offset)
  }

  /**
   * Returns a copy of this {@code OffsetDateTime} with the specified offset ensuring
   * that the result is at the same instant.
   * <p>
   * This method returns an object with the specified {@code ZoneOffset} and a {@code DateTime}
   * adjusted by the difference between the two offsets.
   * This will result in the old and new objects representing the same instant.
   * This is useful for finding the local time in a different offset.
   * For example, if this time represents {@code 2007-12-03T10:30+02:00} and the offset specified is
   * {@code +03:00}, then this method will  {@code 2007-12-03T11:30+03:00}.
   * <p>
   * To change the offset without adjusting the local time use {@link #withOffsetSameLocal}.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param offset  the zone offset to change to, not null
   * @return an { @code OffsetDateTime} based on this date-time with the requested offset, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def withOffsetSameInstant(offset: ZoneOffset): OffsetDateTime = {
    if (offset == this.offset) {
       this
    }
    val difference: Int = offset.getTotalSeconds - this.offset.getTotalSeconds
    val adjusted: DateTime = dateTime.plusSeconds(difference)
     new OffsetDateTime(adjusted, offset)
  }

  /**
   * Gets the {@code DateTime} part of this offset date-time.
   * <p>
   * This returns a {@code DateTime} with the same year, month, day and time
   * as this date-time.
   *
   * @return the local date-time part of this date-time, not null
   */
  def toLocalDateTime: DateTime = {
     dateTime
  }

  /**
   * Gets the {@code Date} part of this date-time.
   * <p>
   * This returns a {@code Date} with the same year, month and day
   * as this date-time.
   *
   * @return the date part of this date-time, not null
   */
  def toLocalDate: Date = {
     dateTime.toLocalDate
  }

  /**
   * Gets the year field.
   * <p>
   * This method returns the primitive {@code int} value for the year.
   * <p>
   * The year returned by this method is proleptic as per {@code get(YEAR)}.
   * To obtain the year-of-era, use {@code get(YEAR_OF_ERA)}.
   *
   * @return the year, from MIN_YEAR to MAX_YEAR
   */
  def getYear: Int = {
     dateTime.getYear
  }

  /**
   * Gets the month-of-year field from 1 to 12.
   * <p>
   * This method returns the month as an {@code int} from 1 to 12.
   * Application code is frequently clearer if the enum {@link Month}
   * is used by calling {@link #getMonth()}.
   *
   * @return the month-of-year, from 1 to 12
   * @see #getMonth()
   */
  def getMonthValue: Int = {
     dateTime.getMonthValue
  }

  /**
   * Gets the month-of-year field using the {@code Month} enum.
   * <p>
   * This method returns the enum {@link Month} for the month.
   * This avoids confusion as to what {@code int} values mean.
   * If you need access to the primitive {@code int} value then the enum
   * provides the {@link Month#getValue() int value}.
   *
   * @return the month-of-year, not null
   * @see #getMonthValue()
   */
  def getMonth: Month = {
     dateTime.getMonth
  }

  /**
   * Gets the day-of-month field.
   * <p>
   * This method returns the primitive {@code int} value for the day-of-month.
   *
   * @return the day-of-month, from 1 to 31
   */
  def getDayOfMonth: Int = {
     dateTime.getDayOfMonth
  }

  /**
   * Gets the day-of-year field.
   * <p>
   * This method returns the primitive {@code int} value for the day-of-year.
   *
   * @return the day-of-year, from 1 to 365, or 366 in a leap year
   */
  def getDayOfYear: Int = {
     dateTime.getDayOfYear
  }

  /**
   * Gets the day-of-week field, which is an enum {@code DayOfWeek}.
   * <p>
   * This method returns the enum {@link java.time.DayOfWeek} for the day-of-week.
   * This avoids confusion as to what {@code int} values mean.
   * If you need access to the primitive {@code int} value then the enum
   * provides the {@link java.time.DayOfWeek#getValue() int value}.
   * <p>
   * Additional information can be obtained from the {@code DayOfWeek}.
   * This includes textual names of the values.
   *
   * @return the day-of-week, not null
   */
  def getDayOfWeek: DayOfWeek = {
     dateTime.getDayOfWeek
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
     dateTime.toLocalTime
  }

  /**
   * Gets the hour-of-day field.
   *
   * @return the hour-of-day, from 0 to 23
   */
  def getHour: Int = {
     dateTime.getHour
  }

  /**
   * Gets the minute-of-hour field.
   *
   * @return the minute-of-hour, from 0 to 59
   */
  def getMinute: Int = {
     dateTime.getMinute
  }

  /**
   * Gets the second-of-minute field.
   *
   * @return the second-of-minute, from 0 to 59
   */
  def getSecond: Int = {
     dateTime.getSecond
  }

  /**
   * Gets the nano-of-second field.
   *
   * @return the nano-of-second, from 0 to 999,999,999
   */
  def getNano: Int = {
     dateTime.getNano
  }

  /**
   * Returns an adjusted copy of this date-time.
   * <p>
   * This returns an {@code OffsetDateTime}, based on this one, with the date-time adjusted.
   * The adjustment takes place using the specified adjuster strategy object.
   * Read the documentation of the adjuster to understand what adjustment will be made.
   * <p>
   * A simple adjuster might simply set the one of the fields, such as the year field.
   * A more complex adjuster might set the date to the last day of the month.
   * A selection of common adjustments is provided in {@link TemporalAdjuster}.
   * These include finding the "last day of the month" and "next Wednesday".
   * Key date-time classes also implement the {@code TemporalAdjuster} interface,
   * such as {@link Month} and {@link java.time.MonthDay MonthDay}.
   * The adjuster is responsible for handling special cases, such as the varying
   * lengths of month and leap years.
   * <p>
   * For example this code returns a date on the last day of July:
   * {{{
   * import static java.time.Month.*;
   * import static java.time.temporal.Adjusters.*;
   *
   * result = offsetDateTime.with(JULY).with(lastDayOfMonth());
   * }}}
   * <p>
   * The classes {@link Date}, {@link Time} and {@link ZoneOffset} implement
   * {@code TemporalAdjuster}, thus this method can be used to change the date, time or offset:
   * {{{
   * result = offsetDateTime.with(date);
   * result = offsetDateTime.with(time);
   * result = offsetDateTime.with(offset);
   * }}}
   * <p>
   * The result of this method is obtained by invoking the
   * {@link TemporalAdjuster#adjustInto(Temporal)} method on the
   * specified adjuster passing {@code this} as the argument.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param adjuster the adjuster to use, not null
   * @return an { @code OffsetDateTime} based on { @code this} with the adjustment made, not null
   * @throws DateTimeException if the adjustment cannot be made
   * @throws ArithmeticException if numeric overflow occurs
   */
  override def `with`(adjuster: TemporalAdjuster): OffsetDateTime = {
    if (adjuster.isInstanceOf[Date] || adjuster.isInstanceOf[Time] || adjuster.isInstanceOf[DateTime]) {
       `with`(dateTime.`with`(adjuster), offset)
    }
    else if (adjuster.isInstanceOf[Instant]) {
       ofInstant(adjuster.asInstanceOf[Instant], offset)
    }
    else if (adjuster.isInstanceOf[ZoneOffset]) {
       `with`(dateTime, adjuster.asInstanceOf[ZoneOffset])
    }
    else if (adjuster.isInstanceOf[OffsetDateTime]) {
       adjuster.asInstanceOf[OffsetDateTime]
    }
     adjuster.adjustInto(this).asInstanceOf[OffsetDateTime]
  }

  /**
   * Returns a copy of this date-time with the specified field set to a new value.
   * <p>
   * TThis returns an {@code OffsetDateTime}, based on this one, with the value
   * for the specified field changed.
   * This can be used to change any supported field, such as the year, month or day-of-month.
   * If it is not possible to set the value, because the field is not supported or for
   * some other reason, an exception is thrown.
   * <p>
   * In some cases, changing the specified field can cause the resulting date-time to become invalid,
   * such as changing the month from 31st January to February would make the day-of-month invalid.
   * In cases like this, the field is responsible for resolving the date. Typically it will choose
   * the previous valid date, which would be the last valid day of February in this example.
   * <p>
   * If the field is a {@link ChronoField} then the adjustment is implemented here.
   * <p>
   * The {@code INSTANT_SECONDS} field will  a date-time with the specified instant.
   * The offset and nano-of-second are unchanged.
   * If the new instant value is outside the valid range then a {@code DateTimeException} will be thrown.
   * <p>
   * The {@code OFFSET_SECONDS} field will  a date-time with the specified offset.
   * The local date-time is unaltered. If the new offset value is outside the valid range
   * then a {@code DateTimeException} will be thrown.
   * <p>
   * The other {@link #isSupported(TemporalField) supported fields} will behave as per
   * the matching method on {@link DateTime#with(TemporalField, long) DateTime}.
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
   * @return an { @code OffsetDateTime} based on { @code this} with the specified field set, not null
   * @throws DateTimeException if the field cannot be set
   * @throws UnsupportedTemporalTypeException if the field is not supported
   * @throws ArithmeticException if numeric overflow occurs
   */
  def `with`(field: TemporalField, newValue: Long): OffsetDateTime = {
    if (field.isInstanceOf[ChronoField]) {
      val f: ChronoField = field.asInstanceOf[ChronoField]
      f match {
        case INSTANT_SECONDS =>
           ofInstant(Instant.ofEpochSecond(newValue, getNano), offset)
        case OFFSET_SECONDS => {
           `with`(dateTime, ZoneOffset.ofTotalSeconds(f.checkValidIntValue(newValue)))
        }
      }
       `with`(dateTime.`with`(field, newValue), offset)
    }
     field.adjustInto(this, newValue)
  }

  /**
   * Returns a copy of this {@code OffsetDateTime} with the year altered.
   * The offset does not affect the calculation and will be the same in the result.
   * If the day-of-month is invalid for the year, it will be changed to the last valid day of the month.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param year  the year to set in the result, from MIN_YEAR to MAX_YEAR
   * @return an { @code OffsetDateTime} based on this date-time with the requested year, not null
   * @throws DateTimeException if the year value is invalid
   */
  def withYear(year: Int): OffsetDateTime = {
     `with`(dateTime.withYear(year), offset)
  }

  /**
   * Returns a copy of this {@code OffsetDateTime} with the month-of-year altered.
   * The offset does not affect the calculation and will be the same in the result.
   * If the day-of-month is invalid for the year, it will be changed to the last valid day of the month.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param month  the month-of-year to set in the result, from 1 (January) to 12 (December)
   * @return an { @code OffsetDateTime} based on this date-time with the requested month, not null
   * @throws DateTimeException if the month-of-year value is invalid
   */
  def withMonth(month: Int): OffsetDateTime = {
     `with`(dateTime.withMonth(month), offset)
  }

  /**
   * Returns a copy of this {@code OffsetDateTime} with the day-of-month altered.
   * If the resulting {@code OffsetDateTime} is invalid, an exception is thrown.
   * The offset does not affect the calculation and will be the same in the result.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param dayOfMonth  the day-of-month to set in the result, from 1 to 28-31
   * @return an { @code OffsetDateTime} based on this date-time with the requested day, not null
   * @throws DateTimeException if the day-of-month value is invalid,
   *                           or if the day-of-month is invalid for the month-year
   */
  def withDayOfMonth(dayOfMonth: Int): OffsetDateTime = {
     `with`(dateTime.withDayOfMonth(dayOfMonth), offset)
  }

  /**
   * Returns a copy of this {@code OffsetDateTime} with the day-of-year altered.
   * If the resulting {@code OffsetDateTime} is invalid, an exception is thrown.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param dayOfYear  the day-of-year to set in the result, from 1 to 365-366
   * @return an { @code OffsetDateTime} based on this date with the requested day, not null
   * @throws DateTimeException if the day-of-year value is invalid,
   *                           or if the day-of-year is invalid for the year
   */
  def withDayOfYear(dayOfYear: Int): OffsetDateTime = {
     `with`(dateTime.withDayOfYear(dayOfYear), offset)
  }

  /**
   * Returns a copy of this {@code OffsetDateTime} with the hour-of-day value altered.
   * <p>
   * The offset does not affect the calculation and will be the same in the result.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param hour  the hour-of-day to set in the result, from 0 to 23
   * @return an { @code OffsetDateTime} based on this date-time with the requested hour, not null
   * @throws DateTimeException if the hour value is invalid
   */
  def withHour(hour: Int): OffsetDateTime = {
     `with`(dateTime.withHour(hour), offset)
  }

  /**
   * Returns a copy of this {@code OffsetDateTime} with the minute-of-hour value altered.
   * <p>
   * The offset does not affect the calculation and will be the same in the result.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param minute  the minute-of-hour to set in the result, from 0 to 59
   * @return an { @code OffsetDateTime} based on this date-time with the requested minute, not null
   * @throws DateTimeException if the minute value is invalid
   */
  def withMinute(minute: Int): OffsetDateTime = {
     `with`(dateTime.withMinute(minute), offset)
  }

  /**
   * Returns a copy of this {@code OffsetDateTime} with the second-of-minute value altered.
   * <p>
   * The offset does not affect the calculation and will be the same in the result.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param second  the second-of-minute to set in the result, from 0 to 59
   * @return an { @code OffsetDateTime} based on this date-time with the requested second, not null
   * @throws DateTimeException if the second value is invalid
   */
  def withSecond(second: Int): OffsetDateTime = {
     `with`(dateTime.withSecond(second), offset)
  }

  /**
   * Returns a copy of this {@code OffsetDateTime} with the nano-of-second value altered.
   * <p>
   * The offset does not affect the calculation and will be the same in the result.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param nanoOfSecond  the nano-of-second to set in the result, from 0 to 999,999,999
   * @return an { @code OffsetDateTime} based on this date-time with the requested nanosecond, not null
   * @throws DateTimeException if the nanos value is invalid
   */
  def withNano(nanoOfSecond: Int): OffsetDateTime = {
     `with`(dateTime.withNano(nanoOfSecond), offset)
  }

  /**
   * Returns a copy of this {@code OffsetDateTime} with the time truncated.
   * <p>
   * Truncation returns a copy of the original date-time with fields
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
   * @return an { @code OffsetDateTime} based on this date-time with the time truncated, not null
   * @throws DateTimeException if unable to truncate
   * @throws UnsupportedTemporalTypeException if the unit is not supported
   */
  def truncatedTo(unit: TemporalUnit): OffsetDateTime = {
     `with`(dateTime.truncatedTo(unit), offset)
  }

  /**
   * Returns a copy of this date-time with the specified amount added.
   * <p>
   * This returns an {@code OffsetDateTime}, based on this one, with the specified amount added.
   * The amount is typically {@link Period} or {@link Duration} but may be
   * any other type implementing the {@link TemporalAmount} interface.
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
   * @return an { @code OffsetDateTime} based on this date-time with the addition made, not null
   * @throws DateTimeException if the addition cannot be made
   * @throws ArithmeticException if numeric overflow occurs
   */
  override def plus(amountToAdd: TemporalAmount): OffsetDateTime = {
     amountToAdd.addTo(this).asInstanceOf[OffsetDateTime]
  }

  /**
   * Returns a copy of this date-time with the specified amount added.
   * <p>
   * This returns an {@code OffsetDateTime}, based on this one, with the amount
   * in terms of the unit added. If it is not possible to add the amount, because the
   * unit is not supported or for some other reason, an exception is thrown.
   * <p>
   * If the field is a {@link ChronoUnit} then the addition is implemented by
   * {@link DateTime#plus(long, TemporalUnit)}.
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
   * @return an { @code OffsetDateTime} based on this date-time with the specified amount added, not null
   * @throws DateTimeException if the addition cannot be made
   * @throws UnsupportedTemporalTypeException if the unit is not supported
   * @throws ArithmeticException if numeric overflow occurs
   */
  def plus(amountToAdd: Long, unit: TemporalUnit): OffsetDateTime = {
    if (unit.isInstanceOf[ChronoUnit]) {
       `with`(dateTime.plus(amountToAdd, unit), offset)
    }
     unit.addTo(this, amountToAdd)
  }

  /**
   * Returns a copy of this {@code OffsetDateTime} with the specified period in years added.
   * <p>
   * This method adds the specified amount to the years field in three steps:
   * <ol>
   * <li>Add the input years to the year field</li>
   * <li>Check if the resulting date would be invalid</li>
   * <li>Adjust the day-of-month to the last valid day if necessary</li>
   * </ol>
   * <p>
   * For example, 2008-02-29 (leap year) plus one year would result in the
   * invalid date 2009-02-29 (standard year). Instead of returning an invalid
   * result, the last valid day of the month, 2009-02-28, is selected instead.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param years  the years to add, may be negative
   * @return an { @code OffsetDateTime} based on this date-time with the years added, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def plusYears(years: Long): OffsetDateTime = {
     `with`(dateTime.plusYears(years), offset)
  }

  /**
   * Returns a copy of this {@code OffsetDateTime} with the specified period in months added.
   * <p>
   * This method adds the specified amount to the months field in three steps:
   * <ol>
   * <li>Add the input months to the month-of-year field</li>
   * <li>Check if the resulting date would be invalid</li>
   * <li>Adjust the day-of-month to the last valid day if necessary</li>
   * </ol>
   * <p>
   * For example, 2007-03-31 plus one month would result in the invalid date
   * 2007-04-31. Instead of returning an invalid result, the last valid day
   * of the month, 2007-04-30, is selected instead.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param months  the months to add, may be negative
   * @return an { @code OffsetDateTime} based on this date-time with the months added, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def plusMonths(months: Long): OffsetDateTime = {
     `with`(dateTime.plusMonths(months), offset)
  }

  /**
   * Returns a copy of this OffsetDateTime with the specified period in weeks added.
   * <p>
   * This method adds the specified amount in weeks to the days field incrementing
   * the month and year fields as necessary to ensure the result remains valid.
   * The result is only invalid if the maximum/minimum year is exceeded.
   * <p>
   * For example, 2008-12-31 plus one week would result in the 2009-01-07.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param weeks  the weeks to add, may be negative
   * @return an { @code OffsetDateTime} based on this date-time with the weeks added, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def plusWeeks(weeks: Long): OffsetDateTime = {
     `with`(dateTime.plusWeeks(weeks), offset)
  }

  /**
   * Returns a copy of this OffsetDateTime with the specified period in days added.
   * <p>
   * This method adds the specified amount to the days field incrementing the
   * month and year fields as necessary to ensure the result remains valid.
   * The result is only invalid if the maximum/minimum year is exceeded.
   * <p>
   * For example, 2008-12-31 plus one day would result in the 2009-01-01.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param days  the days to add, may be negative
   * @return an { @code OffsetDateTime} based on this date-time with the days added, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def plusDays(days: Long): OffsetDateTime = {
     `with`(dateTime.plusDays(days), offset)
  }

  /**
   * Returns a copy of this {@code OffsetDateTime} with the specified period in hours added.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param hours  the hours to add, may be negative
   * @return an { @code OffsetDateTime} based on this date-time with the hours added, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def plusHours(hours: Long): OffsetDateTime = {
     `with`(dateTime.plusHours(hours), offset)
  }

  /**
   * Returns a copy of this {@code OffsetDateTime} with the specified period in minutes added.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param minutes  the minutes to add, may be negative
   * @return an { @code OffsetDateTime} based on this date-time with the minutes added, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def plusMinutes(minutes: Long): OffsetDateTime = {
     `with`(dateTime.plusMinutes(minutes), offset)
  }

  /**
   * Returns a copy of this {@code OffsetDateTime} with the specified period in seconds added.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param seconds  the seconds to add, may be negative
   * @return an { @code OffsetDateTime} based on this date-time with the seconds added, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def plusSeconds(seconds: Long): OffsetDateTime = {
     `with`(dateTime.plusSeconds(seconds), offset)
  }

  /**
   * Returns a copy of this {@code OffsetDateTime} with the specified period in nanoseconds added.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param nanos  the nanos to add, may be negative
   * @return an { @code OffsetDateTime} based on this date-time with the nanoseconds added, not null
   * @throws DateTimeException if the unit cannot be added to this type
   */
  def plusNanos(nanos: Long): OffsetDateTime = {
     `with`(dateTime.plusNanos(nanos), offset)
  }

  /**
   * Returns a copy of this date-time with the specified amount subtracted.
   * <p>
   * This returns an {@code OffsetDateTime}, based on this one, with the specified amount subtracted.
   * The amount is typically {@link Period} or {@link Duration} but may be
   * any other type implementing the {@link TemporalAmount} interface.
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
   * @return an { @code OffsetDateTime} based on this date-time with the subtraction made, not null
   * @throws DateTimeException if the subtraction cannot be made
   * @throws ArithmeticException if numeric overflow occurs
   */
  override def minus(amountToSubtract: TemporalAmount): OffsetDateTime = {
     amountToSubtract.subtractFrom(this).asInstanceOf[OffsetDateTime]
  }

  /**
   * Returns a copy of this date-time with the specified amount subtracted.
   * <p>
   * This returns an {@code OffsetDateTime}, based on this one, with the amount
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
   * @return an { @code OffsetDateTime} based on this date-time with the specified amount subtracted, not null
   * @throws DateTimeException if the subtraction cannot be made
   * @throws UnsupportedTemporalTypeException if the unit is not supported
   * @throws ArithmeticException if numeric overflow occurs
   */
  override def minus(amountToSubtract: Long, unit: TemporalUnit): OffsetDateTime = {
     (if (amountToSubtract == Long.MIN_VALUE) plus(Long.MAX_VALUE, unit).plus(1, unit) else plus(-amountToSubtract, unit))
  }

  /**
   * Returns a copy of this {@code OffsetDateTime} with the specified period in years subtracted.
   * <p>
   * This method subtracts the specified amount from the years field in three steps:
   * <ol>
   * <li>Subtract the input years to the year field</li>
   * <li>Check if the resulting date would be invalid</li>
   * <li>Adjust the day-of-month to the last valid day if necessary</li>
   * </ol>
   * <p>
   * For example, 2008-02-29 (leap year) minus one year would result in the
   * invalid date 2009-02-29 (standard year). Instead of returning an invalid
   * result, the last valid day of the month, 2009-02-28, is selected instead.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param years  the years to subtract, may be negative
   * @return an { @code OffsetDateTime} based on this date-time with the years subtracted, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def minusYears(years: Long): OffsetDateTime = {
     (if (years == Long.MIN_VALUE) plusYears(Long.MAX_VALUE).plusYears(1) else plusYears(-years))
  }

  /**
   * Returns a copy of this {@code OffsetDateTime} with the specified period in months subtracted.
   * <p>
   * This method subtracts the specified amount from the months field in three steps:
   * <ol>
   * <li>Subtract the input months to the month-of-year field</li>
   * <li>Check if the resulting date would be invalid</li>
   * <li>Adjust the day-of-month to the last valid day if necessary</li>
   * </ol>
   * <p>
   * For example, 2007-03-31 minus one month would result in the invalid date
   * 2007-04-31. Instead of returning an invalid result, the last valid day
   * of the month, 2007-04-30, is selected instead.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param months  the months to subtract, may be negative
   * @return an { @code OffsetDateTime} based on this date-time with the months subtracted, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def minusMonths(months: Long): OffsetDateTime = {
     (if (months == Long.MIN_VALUE) plusMonths(Long.MAX_VALUE).plusMonths(1) else plusMonths(-months))
  }

  /**
   * Returns a copy of this {@code OffsetDateTime} with the specified period in weeks subtracted.
   * <p>
   * This method subtracts the specified amount in weeks from the days field decrementing
   * the month and year fields as necessary to ensure the result remains valid.
   * The result is only invalid if the maximum/minimum year is exceeded.
   * <p>
   * For example, 2008-12-31 minus one week would result in the 2009-01-07.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param weeks  the weeks to subtract, may be negative
   * @return an { @code OffsetDateTime} based on this date-time with the weeks subtracted, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def minusWeeks(weeks: Long): OffsetDateTime = {
     (if (weeks == Long.MIN_VALUE) plusWeeks(Long.MAX_VALUE).plusWeeks(1) else plusWeeks(-weeks))
  }

  /**
   * Returns a copy of this {@code OffsetDateTime} with the specified period in days subtracted.
   * <p>
   * This method subtracts the specified amount from the days field incrementing the
   * month and year fields as necessary to ensure the result remains valid.
   * The result is only invalid if the maximum/minimum year is exceeded.
   * <p>
   * For example, 2008-12-31 minus one day would result in the 2009-01-01.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param days  the days to subtract, may be negative
   * @return an { @code OffsetDateTime} based on this date-time with the days subtracted, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def minusDays(days: Long): OffsetDateTime = {
     (if (days == Long.MIN_VALUE) plusDays(Long.MAX_VALUE).plusDays(1) else plusDays(-days))
  }

  /**
   * Returns a copy of this {@code OffsetDateTime} with the specified period in hours subtracted.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param hours  the hours to subtract, may be negative
   * @return an { @code OffsetDateTime} based on this date-time with the hours subtracted, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def minusHours(hours: Long): OffsetDateTime = {
     (if (hours == Long.MIN_VALUE) plusHours(Long.MAX_VALUE).plusHours(1) else plusHours(-hours))
  }

  /**
   * Returns a copy of this {@code OffsetDateTime} with the specified period in minutes subtracted.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param minutes  the minutes to subtract, may be negative
   * @return an { @code OffsetDateTime} based on this date-time with the minutes subtracted, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def minusMinutes(minutes: Long): OffsetDateTime = {
     (if (minutes == Long.MIN_VALUE) plusMinutes(Long.MAX_VALUE).plusMinutes(1) else plusMinutes(-minutes))
  }

  /**
   * Returns a copy of this {@code OffsetDateTime} with the specified period in seconds subtracted.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param seconds  the seconds to subtract, may be negative
   * @return an { @code OffsetDateTime} based on this date-time with the seconds subtracted, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def minusSeconds(seconds: Long): OffsetDateTime = {
     (if (seconds == Long.MIN_VALUE) plusSeconds(Long.MAX_VALUE).plusSeconds(1) else plusSeconds(-seconds))
  }

  /**
   * Returns a copy of this {@code OffsetDateTime} with the specified period in nanoseconds subtracted.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param nanos  the nanos to subtract, may be negative
   * @return an { @code OffsetDateTime} based on this date-time with the nanoseconds subtracted, not null
   * @throws DateTimeException if the result exceeds the supported date range
   */
  def minusNanos(nanos: Long): OffsetDateTime = {
     (if (nanos == Long.MIN_VALUE) plusNanos(Long.MAX_VALUE).plusNanos(1) else plusNanos(-nanos))
  }

  /**
   * Queries this date-time using the specified query.
   * <p>
   * This queries this date-time using the specified query strategy object.
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
       getOffset.asInstanceOf[R]
    }
    else if (query eq TemporalQuery.zoneId) {
       null
    }
    else if (query eq TemporalQuery.localDate) {
       toLocalDate.asInstanceOf[R]
    }
    else if (query eq TemporalQuery.localTime) {
       toLocalTime.asInstanceOf[R]
    }
    else if (query eq TemporalQuery.chronology) {
       IsoChronology.INSTANCE.asInstanceOf[R]
    }
    else if (query eq TemporalQuery.precision) {
       NANOS.asInstanceOf[R]
    }
     query.queryFrom(this)
  }

  /**
   * Adjusts the specified temporal object to have the same offset, date
   * and time as this object.
   * <p>
   * This returns a temporal object of the same observable type as the input
   * with the offset, date and time changed to be the same as this.
   * <p>
   * The adjustment is equivalent to using {@link Temporal#with(TemporalField, long)}
   * three times, passing {@link ChronoField#EPOCH_DAY},
   * {@link ChronoField#NANO_OF_DAY} and {@link ChronoField#OFFSET_SECONDS} as the fields.
   * <p>
   * In most cases, it is clearer to reverse the calling pattern by using
   * {@link Temporal#with(TemporalAdjuster)}:
   * {{{
   * // these two lines are equivalent, but the second approach is recommended
   * temporal = thisOffsetDateTime.adjustInto(temporal);
   * temporal = temporal.with(thisOffsetDateTime);
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
     temporal.`with`(EPOCH_DAY, toLocalDate.toEpochDay).`with`(NANO_OF_DAY, toLocalTime.toNanoOfDay).`with`(OFFSET_SECONDS, getOffset.getTotalSeconds)
  }

  /**
   * Calculates the amount of time until another date-time in terms of the specified unit.
   * <p>
   * This calculates the amount of time between two {@code OffsetDateTime}
   * objects in terms of a single {@code TemporalUnit}.
   * The start and end points are {@code this} and the specified date-time.
   * The result will be negative if the end is before the start.
   * For example, the period in days between two date-times can be calculated
   * using {@code startDateTime.until(endDateTime, DAYS)}.
   * <p>
   * The {@code Temporal} passed to this method is converted to a
   * {@code OffsetDateTime} using {@link #from(TemporalAccessor)}.
   * If the offset differs between the two date-times, the specified
   * end date-time is normalized to have the same offset as this date-time.
   * <p>
   * The calculation returns a whole number, representing the number of
   * complete units between the two date-times.
   * For example, the period in months between 2012-06-15T00:00Z and 2012-08-14T23:59Z
   * will only be one month as it is one minute short of two months.
   * <p>
   * There are two equivalent ways of using this method.
   * The first is to invoke this method.
   * The second is to use {@link TemporalUnit#between(Temporal, Temporal)}:
   * {{{
   * // these two lines are equivalent
   * amount = start.until(end, MONTHS);
   * amount = MONTHS.between(start, end);
   * }}}
   * The choice should be made based on which makes the code more readable.
   * <p>
   * The calculation is implemented in this method for {@link ChronoUnit}.
   * The units {@code NANOS}, {@code MICROS}, {@code MILLIS}, {@code SECONDS},
   * {@code MINUTES}, {@code HOURS} and {@code HALF_DAYS}, {@code DAYS},
   * {@code WEEKS}, {@code MONTHS}, {@code YEARS}, {@code DECADES},
   * {@code CENTURIES}, {@code MILLENNIA} and {@code ERAS} are supported.
   * Other {@code ChronoUnit} values will throw an exception.
   * <p>
   * If the unit is not a {@code ChronoUnit}, then the result of this method
   * is obtained by invoking {@code TemporalUnit.between(Temporal, Temporal)}
   * passing {@code this} as the first argument and the converted input temporal
   * as the second argument.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param endExclusive  the end date, which is converted to an { @code OffsetDateTime}, not null
   * @param unit  the unit to measure the amount in, not null
   * @return the amount of time between this date-time and the end date-time
   * @throws DateTimeException if the amount cannot be calculated, or the end
   *                           temporal cannot be converted to an { @code OffsetDateTime}
   * @throws UnsupportedTemporalTypeException if the unit is not supported
   * @throws ArithmeticException if numeric overflow occurs
   */
  def until(endExclusive: Temporal, unit: TemporalUnit): Long = {
    var end: OffsetDateTime = OffsetDateTime.from(endExclusive)
    if (unit.isInstanceOf[ChronoUnit]) {
      end = end.withOffsetSameInstant(offset)
       dateTime.until(end.dateTime, unit)
    }
     unit.between(this, end)
  }

  /**
   * Formats this date-time using the specified formatter.
   * <p>
   * This date-time will be passed to the formatter to produce a string.
   *
   * @param formatter  the formatter to use, not null
   * @return the formatted date-time string, not null
   * @throws DateTimeException if an error occurs during printing
   */
  def format(formatter: DateTimeFormatter): String = {
    object
     formatter.format(this)
  }

  /**
   * Combines this date-time with a time-zone to create a {@code ZonedDateTime}
   * ensuring that the result has the same instant.
   * <p>
   * This returns a {@code ZonedDateTime} formed from this date-time and the specified time-zone.
   * This conversion will ignore the visible local date-time and use the underlying instant instead.
   * This avoids any problems with local time-line gaps or overlaps.
   * The result might have different values for fields such as hour, minute an even day.
   * <p>
   * To attempt to retain the values of the fields, use {@link #atZoneSimilarLocal(ZoneId)}.
   * To use the offset as the zone ID, use {@link #toZonedDateTime()}.
   *
   * @param zone  the time-zone to use, not null
   * @return the zoned date-time formed from this date-time, not null
   */
  def atZoneSameInstant(zone: ZoneId): ZonedDateTime = {
     ZonedDateTime.ofInstant(dateTime, offset, zone)
  }

  /**
   * Combines this date-time with a time-zone to create a {@code ZonedDateTime}
   * trying to keep the same local date and time.
   * <p>
   * This returns a {@code ZonedDateTime} formed from this date-time and the specified time-zone.
   * Where possible, the result will have the same local date-time as this object.
   * <p>
   * Time-zone rules, such as daylight savings, mean that not every time on the
   * local time-line exists. If the local date-time is in a gap or overlap according to
   * the rules then a resolver is used to determine the resultant local time and offset.
   * This method uses {@link ZonedDateTime#ofLocal(DateTime, ZoneId, ZoneOffset)}
   * to retain the offset from this instance if possible.
   * <p>
   * Finer control over gaps and overlaps is available in two ways.
   * If you simply want to use the later offset at overlaps then call
   * {@link ZonedDateTime#withLaterOffsetAtOverlap()} immediately after this method.
   * <p>
   * To create a zoned date-time at the same instant irrespective of the local time-line,
   * use {@link #atZoneSameInstant(ZoneId)}.
   * To use the offset as the zone ID, use {@link #toZonedDateTime()}.
   *
   * @param zone  the time-zone to use, not null
   * @return the zoned date-time formed from this date and the earliest valid time for the zone, not null
   */
  def atZoneSimilarLocal(zone: ZoneId): ZonedDateTime = {
     ZonedDateTime.ofLocal(dateTime, zone, offset)
  }

  /**
   * Converts this date-time to an {@code OffsetTime}.
   * <p>
   * This returns an offset time with the same local time and offset.
   *
   * @return an OffsetTime representing the time and offset, not null
   */
  def toOffsetTime: OffsetTime = {
     OffsetTime.of(dateTime.toLocalTime, offset)
  }

  /**
   * Converts this date-time to a {@code ZonedDateTime} using the offset as the zone ID.
   * <p>
   * This creates the simplest possible {@code ZonedDateTime} using the offset
   * as the zone ID.
   * <p>
   * To control the time-zone used, see {@link #atZoneSameInstant(ZoneId)} and
   * {@link #atZoneSimilarLocal(ZoneId)}.
   *
   * @return a zoned date-time representing the same local date-time and offset, not null
   */
  def toZonedDateTime: ZonedDateTime = {
     ZonedDateTime.of(dateTime, offset)
  }

  /**
   * Converts this date-time to an {@code Instant}.
   * <p>
   * This returns an {@code Instant} representing the same point on the
   * time-line as this date-time.
   *
   * @return an { @code Instant} representing the same instant, not null
   */
  def toInstant: Instant = {
     dateTime.toInstant(offset)
  }

  /**
   * Converts this date-time to the number of seconds from the epoch of 1970-01-01T00:00:00Z.
   * <p>
   * This allows this date-time to be converted to a value of the
   * {@link ChronoField#INSTANT_SECONDS epoch-seconds} field. This is primarily
   * intended for low-level conversions rather than general application usage.
   *
   * @return the number of seconds from the epoch of 1970-01-01T00:00:00Z
   */
  def toEpochSecond: Long = {
     dateTime.toEpochSecond(offset)
  }

  /**
   * Compares this {@code OffsetDateTime} to another date-time.
   * <p>
   * The comparison is based on the instant then on the local date-time.
   * It is "consistent with equals", as defined by {@link Comparable}.
   * <p>
   * For example, the following is the comparator order:
   * <ol>
   * <li>{@code 2008-12-03T10:30+01:00}</li>
   * <li>{@code 2008-12-03T11:00+01:00}</li>
   * <li>{@code 2008-12-03T12:00+02:00}</li>
   * <li>{@code 2008-12-03T11:30+01:00}</li>
   * <li>{@code 2008-12-03T12:00+01:00}</li>
   * <li>{@code 2008-12-03T12:30+01:00}</li>
   * </ol>
   * Values #2 and #3 represent the same instant on the time-line.
   * When two values represent the same instant, the local date-time is compared
   * to distinguish them. This step is needed to make the ordering
   * consistent with {@code equals()}.
   *
   * @param other  the other date-time to compare to, not null
   * @return the comparator value, negative if less, positive if greater
   */
  def compareTo(other: OffsetDateTime): Int = {
    var cmp: Int = compareInstant(this, other)
    if (cmp == 0) {
      cmp = toLocalDateTime.compareTo(other.toLocalDateTime)
    }
     cmp
  }

  /**
   * Checks if the instant of this date-time is after that of the specified date-time.
   * <p>
   * This method differs from the comparison in {@link #compareTo} and {@link #equals} in that it
   * only compares the instant of the date-time. This is equivalent to using
   * {@code dateTime1.toInstant().isAfter(dateTime2.toInstant());}.
   *
   * @param other  the other date-time to compare to, not null
   * @return true if this is after the instant of the specified date-time
   */
  def isAfter(other: OffsetDateTime): Boolean = {
    val thisEpochSec: Long = toEpochSecond
    val otherEpochSec: Long = other.toEpochSecond
     thisEpochSec > otherEpochSec || (thisEpochSec == otherEpochSec && toLocalTime.getNano > other.toLocalTime.getNano)
  }

  /**
   * Checks if the instant of this date-time is before that of the specified date-time.
   * <p>
   * This method differs from the comparison in {@link #compareTo} in that it
   * only compares the instant of the date-time. This is equivalent to using
   * {@code dateTime1.toInstant().isBefore(dateTime2.toInstant());}.
   *
   * @param other  the other date-time to compare to, not null
   * @return true if this is before the instant of the specified date-time
   */
  def isBefore(other: OffsetDateTime): Boolean = {
    val thisEpochSec: Long = toEpochSecond
    val otherEpochSec: Long = other.toEpochSecond
     thisEpochSec < otherEpochSec || (thisEpochSec == otherEpochSec && toLocalTime.getNano < other.toLocalTime.getNano)
  }

  /**
   * Checks if the instant of this date-time is equal to that of the specified date-time.
   * <p>
   * This method differs from the comparison in {@link #compareTo} and {@link #equals}
   * in that it only compares the instant of the date-time. This is equivalent to using
   * {@code dateTime1.toInstant().equals(dateTime2.toInstant());}.
   *
   * @param other  the other date-time to compare to, not null
   * @return true if the instant equals the instant of the specified date-time
   */
  def isEqual(other: OffsetDateTime): Boolean = {
     toEpochSecond == other.toEpochSecond && toLocalTime.getNano == other.toLocalTime.getNano
  }

  /**
   * Checks if this date-time is equal to another date-time.
   * <p>
   * The comparison is based on the local date-time and the offset.
   * To compare for the same instant on the time-line, use {@link #isEqual}.
   * Only objects of type {@code OffsetDateTime} are compared, other types  false.
   *
   * @param obj  the object to check, null returns false
   * @return true if this is equal to the other date-time
   */
  override def equals(obj: AnyRef): Boolean = {
    if (this eq obj) {
       true
    }
    if (obj.isInstanceOf[OffsetDateTime]) {
      val other: OffsetDateTime = obj.asInstanceOf[OffsetDateTime]
       (dateTime == other.dateTime) && (offset == other.offset)
    }
     false
  }

  /**
   * A hash code for this date-time.
   *
   * @return a suitable hash code
   */
  override def hashCode: Int = {
     dateTime.hashCode ^ offset.hashCode
  }

  /**
   * Outputs this date-time as a {@code String}, such as {@code 2007-12-03T10:15:30+01:00}.
   * <p>
   * The output will be one of the following ISO-8601 formats:
   * <p><ul>
   * <li>{@code uuuu-MM-dd'T'HH:mmXXXXX}</li>
   * <li>{@code uuuu-MM-dd'T'HH:mm:ssXXXXX}</li>
   * <li>{@code uuuu-MM-dd'T'HH:mm:ss.SSSXXXXX}</li>
   * <li>{@code uuuu-MM-dd'T'HH:mm:ss.SSSSSSXXXXX}</li>
   * <li>{@code uuuu-MM-dd'T'HH:mm:ss.SSSSSSSSSXXXXX}</li>
   * </ul><p>
   * The format used will be the shortest that outputs the full value of
   * the time where the omitted parts are implied to be zero.
   *
   * @return a string representation of this date-time, not null
   */
  override def toString: String = {
     dateTime.toString + offset.toString
  }

  /**
   * Writes the object using a
   * <a href="../../../serialized-form.html#java.time.temporal.Ser">dedicated serialized form</a>.
   * {{{
   * out.writeByte(10);  // identifies this as a OffsetDateTime
   * out.writeObject(dateTime);
   * out.writeObject(offset);
   * }}}
   *
   * @return the instance of { @code Ser}, not null
   */
  private def writeReplace: AnyRef = {
     new Ser(Ser.OFFSET_DATE_TIME_TYPE, this)
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
    out.writeObject(dateTime)
    out.writeObject(offset)
  }

  /**
   * The local date-time.
   */
  private final val dateTime: DateTime = null
  /**
   * The offset from UTC/Greenwich.
   */
  private final val offset: ZoneOffset = null
}
