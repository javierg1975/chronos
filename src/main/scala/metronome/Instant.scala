package metronome

/**
 * An instantaneous point on the time-line.
 * <p>
 * This class models a single instantaneous point on the time-line.
 * This might be used to record event time-stamps in the application.
 * <p>
 * For practicality, the instant is stored with some constraints.
 * The measurable time-line is restricted to the number of seconds that can be held
 * in a {@code long}. This is greater than the current estimated age of the universe.
 * The instant is stored to nanosecond resolution.
 * <p>
 * The range of an instant requires the storage of a number larger than a {@code long}.
 * To achieve this, the class stores a {@code long} representing epoch-seconds and an
 * {@code int} representing nanosecond-of-second, which will always be between 0 and 999,999,999.
 * The epoch-seconds are measured from the standard Java epoch of {@code 1970-01-01T00:00:00Z}
 * where instants after the epoch have positive values, and earlier instants have negative values.
 * For both the epoch-second and nanosecond parts, a larger value is always later on the time-line
 * than a smaller value.
 *
 * <h3>Time-scale</h3>
 * <p>
 * The length of the solar day is the standard way that humans measure time.
 * This has traditionally been subdivided into 24 hours of 60 minutes of 60 seconds,
 * forming a 86400 second day.
 * <p>
 * Modern timekeeping is based on atomic clocks which precisely define an SI second
 * relative to the transitions of a Caesium atom. The length of an SI second was defined
 * to be very close to the 86400th fraction of a day.
 * <p>
 * Unfortunately, as the Earth rotates the length of the day varies.
 * In addition, over time the average length of the day is getting longer as the Earth slows.
 * As a result, the length of a solar day in 2012 is slightly longer than 86400 SI seconds.
 * The actual length of any given day and the amount by which the Earth is slowing
 * are not predictable and can only be determined by measurement.
 * The UT1 time-scale captures the accurate length of day, but is only available some
 * time after the day has completed.
 * <p>
 * The UTC time-scale is a standard approach to bundle up all the additional fractions
 * of a second from UT1 into whole seconds, known as <i>leap-seconds</i>.
 * A leap-second may be added or removed depending on the Earth's rotational changes.
 * As such, UTC permits a day to have 86399 SI seconds or 86401 SI seconds where
 * necessary in order to keep the day aligned with the Sun.
 * <p>
 * The modern UTC time-scale was introduced in 1972, introducing the concept of whole leap-seconds.
 * Between 1958 and 1972, the definition of UTC was complex, with minor sub-second leaps and
 * alterations to the length of the notional second. As of 2012, discussions are underway
 * to change the definition of UTC again, with the potential to remove leap seconds or
 * introduce other changes.
 * <p>
 * Given the complexity of accurate timekeeping described above, this Java API defines
 * its own time-scale, the <i>Java Time-Scale</i>.
 * <p>
 * The Java Time-Scale divides each calendar day into exactly 86400
 * subdivisions, known as seconds.  These seconds may differ from the
 * SI second.  It closely matches the de facto international civil time
 * scale, the definition of which changes from time to time.
 * <p>
 * The Java Time-Scale has slightly different definitions for different
 * segments of the time-line, each based on the consensus international
 * time scale that is used as the basis for civil time. Whenever the
 * internationally-agreed time scale is modified or replaced, a new
 * segment of the Java Time-Scale must be defined for it.  Each segment
 * must meet these requirements:
 * <p><ul>
 * <li>the Java Time-Scale shall closely match the underlying international
 * civil time scale;</li>
 * <li>the Java Time-Scale shall exactly match the international civil
 * time scale at noon each day;</li>
 * <li>the Java Time-Scale shall have a precisely-defined relationship to
 * the international civil time scale.</li>
 * </ul><p>
 * There are currently, as of 2013, two segments in the Java time-scale.
 * <p>
 * For the segment from 1972-11-03 (exact boundary discussed below) until
 * further notice, the consensus international time scale is UTC (with
 * leap seconds).  In this segment, the Java Time-Scale is identical to
 * <a href="http://www.cl.cam.ac.uk/~mgk25/time/utc-sls/">UTC-SLS</a>.
 * This is identical to UTC on days that do not have a leap second.
 * On days that do have a leap second, the leap second is spread equally
 * over the last 1000 seconds of the day, maintaining the appearance of
 * exactly 86400 seconds per day.
 * <p>
 * For the segment prior to 1972-11-03, extending back arbitrarily far,
 * the consensus international time scale is defined to be UT1, applied
 * proleptically, which is equivalent to the (mean) solar time on the
 * prime meridian (Greenwich). In this segment, the Java Time-Scale is
 * identical to the consensus international time scale. The exact
 * boundary between the two segments is the instant where UT1 = UTC
 * between 1972-11-03T00:00 and 1972-11-04T12:00.
 * <p>
 * Implementations of the Java time-scale using the JSR-310 API are not
 * required to provide any clock that is sub-second accurate, or that
 * progresses monotonically or smoothly. Implementations are therefore
 * not required to actually perform the UTC-SLS slew or to otherwise be
 * aware of leap seconds. JSR-310 does, however, require that
 * implementations must document the approach they use when defining a
 * clock representing the current instant.
 * See {@link Clock} for details on the available clocks.
 * <p>
 * The Java time-scale is used for all date-time classes.
 * This includes {@code Instant}, {@code Date}, {@code Time}, {@code OffsetDateTime},
 * {@code ZonedDateTime} and {@code Duration}.
 *
 * @implSpec
 * This class is immutable and thread-safe.
 *
 * @since 1.8
 */

object Instant {
  /**
   * Obtains the current instant from the system clock.
   * <p>
   * This will query the {@link Clock#systemUTC() system UTC clock} to
   * obtain the current instant.
   * <p>
   * Using this method will prevent the ability to use an alternate time-source for
   * testing because the clock is effectively hard-coded.
   *
   * @return the current instant using the system clock, not null
   */
  def now: Instant = {
     Clock.systemUTC.instant
  }

  /**
   * Obtains the current instant from the specified clock.
   * <p>
   * This will query the specified clock to obtain the current time.
   * <p>
   * Using this method allows the use of an alternate clock for testing.
   * The alternate clock may be introduced using {@link Clock dependency injection}.
   *
   * @param clock  the clock to use, not null
   * @return the current instant, not null
   */
  def now(clock: Clock): Instant = {
    object
     clock.instant
  }

  /**
   * Obtains an instance of {@code Instant} using seconds from the
   * epoch of 1970-01-01T00:00:00Z.
   * <p>
   * The nanosecond field is set to zero.
   *
   * @param epochSecond  the number of seconds from 1970-01-01T00:00:00Z
   * @return an instant, not null
   * @throws DateTimeException if the instant exceeds the maximum or minimum instant
   */
  def ofEpochSecond(epochSecond: Long): Instant = {
     create(epochSecond, 0)
  }

  /**
   * Obtains an instance of {@code Instant} using seconds from the
   * epoch of 1970-01-01T00:00:00Z and nanosecond fraction of second.
   * <p>
   * This method allows an arbitrary number of nanoseconds to be passed in.
   * The factory will alter the values of the second and nanosecond in order
   * to ensure that the stored nanosecond is in the range 0 to 999,999,999.
   * For example, the following will result in the exactly the same instant:
   * {{{
   * Instant.ofEpochSecond(3, 1);
   * Instant.ofEpochSecond(4, -999_999_999);
   * Instant.ofEpochSecond(2, 1000_000_001);
   * }}}
   *
   * @param epochSecond  the number of seconds from 1970-01-01T00:00:00Z
   * @param nanoAdjustment  the nanosecond adjustment to the number of seconds, positive or negative
   * @return an instant, not null
   * @throws DateTimeException if the instant exceeds the maximum or minimum instant
   * @throws ArithmeticException if numeric overflow occurs
   */
  def ofEpochSecond(epochSecond: Long, nanoAdjustment: Long): Instant = {
    val secs: Long = Math.addExact(epochSecond, Math.floorDiv(nanoAdjustment, NANOS_PER_SECOND))
    val nos: Int = Math.floorMod(nanoAdjustment, NANOS_PER_SECOND).asInstanceOf[Int]
     create(secs, nos)
  }

  /**
   * Obtains an instance of {@code Instant} using milliseconds from the
   * epoch of 1970-01-01T00:00:00Z.
   * <p>
   * The seconds and nanoseconds are extracted from the specified milliseconds.
   *
   * @param epochMilli  the number of milliseconds from 1970-01-01T00:00:00Z
   * @return an instant, not null
   * @throws DateTimeException if the instant exceeds the maximum or minimum instant
   */
  def ofEpochMilli(epochMilli: Long): Instant = {
    val secs: Long = Math.floorDiv(epochMilli, 1000)
    val mos: Int = Math.floorMod(epochMilli, 1000).asInstanceOf[Int]
     create(secs, mos * 1000 _000)
  }

  /**
   * Obtains an instance of {@code Instant} from a temporal object.
   * <p>
   * This obtains an instant based on the specified temporal.
   * A {@code TemporalAccessor} represents an arbitrary set of date and time information,
   * which this factory converts to an instance of {@code Instant}.
   * <p>
   * The conversion extracts the {@link ChronoField#INSTANT_SECONDS INSTANT_SECONDS}
   * and {@link ChronoField#NANO_OF_SECOND NANO_OF_SECOND} fields.
   * <p>
   * This method matches the signature of the functional interface {@link TemporalQuery}
   * allowing it to be used as a query via method reference, {@code Instant::from}.
   *
   * @param temporal  the temporal object to convert, not null
   * @return the instant, not null
   * @throws DateTimeException if unable to convert to an { @code Instant}
   */
  def from(temporal: TemporalAccessor): Instant = {
    if (temporal.isInstanceOf[Instant]) {
       temporal.asInstanceOf[Instant]
    }
    object
    val instantSecs: Long = temporal.getLong(INSTANT_SECONDS)
    val nanoOfSecond: Int = temporal.get(NANO_OF_SECOND)
     Instant.ofEpochSecond(instantSecs, nanoOfSecond)
  }

  /**
   * Obtains an instance of {@code Instant} from a text string such as
   * {@code 2007-12-03T10:15:30:00}.
   * <p>
   * The string must represent a valid instant in UTC and is parsed using
   * {@link DateTimeFormatter#ISO_INSTANT}.
   *
   * @param text  the text to parse, not null
   * @return the parsed instant, not null
   * @throws DateTimeParseException if the text cannot be parsed
   */
  def parse(text: CharSequence): Instant = {
     DateTimeFormatter.ISO_INSTANT.parse(text, Instant.from)
  }

  /**
   * Obtains an instance of {@code Instant} using seconds and nanoseconds.
   *
   * @param seconds  the length of the duration in seconds
   * @param nanoOfSecond  the nano-of-second, from 0 to 999,999,999
   * @throws DateTimeException if the instant exceeds the maximum or minimum instant
   */
  private def create(seconds: Long, nanoOfSecond: Int): Instant = {
    if ((seconds | nanoOfSecond) == 0) {
       EPOCH
    }
    if (seconds < MIN_SECOND || seconds > MAX_SECOND) {
      throw new DateTimeException("Instant exceeds minimum or maximum instant")
    }
     new Instant(seconds, nanoOfSecond)
  }

  private[time] def readExternal(in: DataInput): Instant = {
    val seconds: Long = in.readLong
    val nanos: Int = in.readInt
     Instant.ofEpochSecond(seconds, nanos)
  }

  /**
   * Constant for the 1970-01-01T00:00:00Z epoch instant.
   */
  final val EPOCH: Instant = new Instant(0, 0)
  /**
   * The minimum supported epoch second.
   */
  private final val MIN_SECOND: Long = -31557014167219200L
  /**
   * The maximum supported epoch second.
   */
  private final val MAX_SECOND: Long = 31556889864403199L
  /**
   * The minimum supported {@code Instant}, '-1000000000-01-01T00:00Z'.
   * This could be used by an application as a "far past" instant.
   * <p>
   * This is one year earlier than the minimum {@code DateTime}.
   * This provides sufficient values to handle the range of {@code ZoneOffset}
   * which affect the instant in addition to the local date-time.
   * The value is also chosen such that the value of the year fits in
   * an {@code int}.
   */
  final val MIN: Instant = Instant.ofEpochSecond(MIN_SECOND, 0)
  /**
   * The maximum supported {@code Instant}, '1000000000-12-31T23:59:59.999999999Z'.
   * This could be used by an application as a "far future" instant.
   * <p>
   * This is one year later than the maximum {@code DateTime}.
   * This provides sufficient values to handle the range of {@code ZoneOffset}
   * which affect the instant in addition to the local date-time.
   * The value is also chosen such that the value of the year fits in
   * an {@code int}.
   */
  final val MAX: Instant = Instant.ofEpochSecond(MAX_SECOND, 999 _999_999)

}

final class Instant extends Temporal with TemporalAdjuster with Comparable[Instant]  {
  /**
   * Constructs an instance of {@code Instant} using seconds from the epoch of
   * 1970-01-01T00:00:00Z and nanosecond fraction of second.
   *
   * @param epochSecond  the number of seconds from 1970-01-01T00:00:00Z
   * @param nanos  the nanoseconds within the second, must be positive
   */
  private def this(epochSecond: Long, nanos: Int) {

    `super`
    this.seconds = epochSecond
    this.nanos = nanos
  }

  /**
   * Checks if the specified field is supported.
   * <p>
   * This checks if this instant can be queried for the specified field.
   * If false, then calling the {@link #range(TemporalField) range},
   * {@link #get(TemporalField) get} and {@link #with(TemporalField, long)}
   * methods will throw an exception.
   * <p>
   * If the field is a {@link ChronoField} then the query is implemented here.
   * The supported fields are:
   * <ul>
   * <li>{@code NANO_OF_SECOND}
   * <li>{@code MICRO_OF_SECOND}
   * <li>{@code MILLI_OF_SECOND}
   * <li>{@code INSTANT_SECONDS}
   * </ul>
   * All other {@code ChronoField} instances will  false.
   * <p>
   * If the field is not a {@code ChronoField}, then the result of this method
   * is obtained by invoking {@code TemporalField.isSupportedBy(TemporalAccessor)}
   * passing {@code this} as the argument.
   * Whether the field is supported is determined by the field.
   *
   * @param field  the field to check, null returns false
   * @return true if the field is supported on this instant, false if not
   */
  def isSupported(field: TemporalField): Boolean = {
    if (field.isInstanceOf[ChronoField]) {
       field eq INSTANT_SECONDS || field eq NANO_OF_SECOND || field eq MICRO_OF_SECOND || field eq MILLI_OF_SECOND
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
       unit.isTimeBased || unit eq DAYS
    }
     unit != null && unit.isSupportedBy(this)
  }

  /**
   * Gets the range of valid values for the specified field.
   * <p>
   * The range object expresses the minimum and maximum valid values for a field.
   * This instant is used to enhance the accuracy of the returned range.
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
   * Gets the value of the specified field from this instant as an {@code int}.
   * <p>
   * This queries this instant for the value for the specified field.
   * The returned value will always be within the valid range of values for the field.
   * If it is not possible to  the value, because the field is not supported
   * or for some other reason, an exception is thrown.
   * <p>
   * If the field is a {@link ChronoField} then the query is implemented here.
   * The {@link #isSupported(TemporalField) supported fields} will  valid
   * values based on this date-time, except {@code INSTANT_SECONDS} which is too
   * large to fit in an {@code int} and throws a {@code DateTimeException}.
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
        case NANO_OF_SECOND =>
           nanos
        case MICRO_OF_SECOND =>
           nanos / 1000
        case MILLI_OF_SECOND =>
           nanos / 1000 _000
        case INSTANT_SECONDS =>
          INSTANT_SECONDS.checkValidIntValue(seconds)
      }
      throw new UnsupportedTemporalTypeException("Unsupported field: " + field)
    }
     range(field).checkValidIntValue(field.getFrom(this), field)
  }

  /**
   * Gets the value of the specified field from this instant as a {@code long}.
   * <p>
   * This queries this instant for the value for the specified field.
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
        case NANO_OF_SECOND =>
           nanos
        case MICRO_OF_SECOND =>
           nanos / 1000
        case MILLI_OF_SECOND =>
           nanos / 1000 _000
        case INSTANT_SECONDS =>
           seconds
      }
      throw new UnsupportedTemporalTypeException("Unsupported field: " + field)
    }
     field.getFrom(this)
  }

  /**
   * Gets the number of seconds from the Java epoch of 1970-01-01T00:00:00Z.
   * <p>
   * The epoch second count is a simple incrementing count of seconds where
   * second 0 is 1970-01-01T00:00:00Z.
   * The nanosecond part of the day is returned by {@code getNanosOfSecond}.
   *
   * @return the seconds from the epoch of 1970-01-01T00:00:00Z
   */
  def getEpochSecond: Long = {
     seconds
  }

  /**
   * Gets the number of nanoseconds, later along the time-line, from the start
   * of the second.
   * <p>
   * The nanosecond-of-second value measures the total number of nanoseconds from
   * the second returned by {@code getEpochSecond}.
   *
   * @return the nanoseconds within the second, always positive, never exceeds 999,999,999
   */
  def getNano: Int = {
     nanos
  }

  /**
   * Returns an adjusted copy of this instant.
   * <p>
   * This returns an {@code Instant}, based on this one, with the instant adjusted.
   * The adjustment takes place using the specified adjuster strategy object.
   * Read the documentation of the adjuster to understand what adjustment will be made.
   * <p>
   * The result of this method is obtained by invoking the
   * {@link TemporalAdjuster#adjustInto(Temporal)} method on the
   * specified adjuster passing {@code this} as the argument.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param adjuster the adjuster to use, not null
   * @return an { @code Instant} based on { @code this} with the adjustment made, not null
   * @throws DateTimeException if the adjustment cannot be made
   * @throws ArithmeticException if numeric overflow occurs
   */
  override def `with`(adjuster: TemporalAdjuster): Instant = {
     adjuster.adjustInto(this).asInstanceOf[Instant]
  }

  /**
   * Returns a copy of this instant with the specified field set to a new value.
   * <p>
   * This returns an {@code Instant}, based on this one, with the value
   * for the specified field changed.
   * If it is not possible to set the value, because the field is not supported or for
   * some other reason, an exception is thrown.
   * <p>
   * If the field is a {@link ChronoField} then the adjustment is implemented here.
   * The supported fields behave as follows:
   * <ul>
   * <li>{@code NANO_OF_SECOND} -
   * Returns an {@code Instant} with the specified nano-of-second.
   * The epoch-second will be unchanged.
   * <li>{@code MICRO_OF_SECOND} -
   * Returns an {@code Instant} with the nano-of-second replaced by the specified
   * micro-of-second multiplied by 1,000. The epoch-second will be unchanged.
   * <li>{@code MILLI_OF_SECOND} -
   * Returns an {@code Instant} with the nano-of-second replaced by the specified
   * milli-of-second multiplied by 1,000,000. The epoch-second will be unchanged.
   * <li>{@code INSTANT_SECONDS} -
   * Returns an {@code Instant} with the specified epoch-second.
   * The nano-of-second will be unchanged.
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
   * @return an { @code Instant} based on { @code this} with the specified field set, not null
   * @throws DateTimeException if the field cannot be set
   * @throws UnsupportedTemporalTypeException if the field is not supported
   * @throws ArithmeticException if numeric overflow occurs
   */
  def `with`(field: TemporalField, newValue: Long): Instant = {
    if (field.isInstanceOf[ChronoField]) {
      val f: ChronoField = field.asInstanceOf[ChronoField]
      f.checkValidValue(newValue)
      f match {
        case MILLI_OF_SECOND => {
          val nval: Int = newValue.asInstanceOf[Int] * 1000 _000
           (if (nval != nanos) create(seconds, nval) else this)
        }
        case MICRO_OF_SECOND => {
          val nval: Int = newValue.asInstanceOf[Int] * 1000
           (if (nval != nanos) create(seconds, nval) else this)
        }
        case NANO_OF_SECOND =>
           (if (newValue != nanos) create(seconds, newValue.asInstanceOf[Int]) else this)
        case INSTANT_SECONDS =>
           (if (newValue != seconds) create(newValue, nanos) else this)
      }
      throw new UnsupportedTemporalTypeException("Unsupported field: " + field)
    }
     field.adjustInto(this, newValue)
  }

  /**
   * Returns a copy of this {@code Instant} truncated to the specified unit.
   * <p>
   * Truncating the instant returns a copy of the original with fields
   * smaller than the specified unit set to zero.
   * The fields are calculated on the basis of using a UTC offset as seen
   * in {@code toString}.
   * For example, truncating with the {@link ChronoUnit#MINUTES MINUTES} unit will
   * round down to the nearest minute, setting the seconds and nanoseconds to zero.
   * <p>
   * The unit must have a {@linkplain TemporalUnit#getDuration() duration}
   * that divides into the length of a standard day without remainder.
   * This includes all supplied time units on {@link ChronoUnit} and
   * {@link ChronoUnit#DAYS DAYS}. Other units throw an exception.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param unit  the unit to truncate to, not null
   * @return an { @code Instant} based on this instant with the time truncated, not null
   * @throws DateTimeException if the unit is invalid for truncation
   * @throws UnsupportedTemporalTypeException if the unit is not supported
   */
  def truncatedTo(unit: TemporalUnit): Instant = {
    if (unit eq ChronoUnit.NANOS) {
       this
    }
    val unitDur: Duration = unit.getDuration
    if (unitDur.getSeconds > Time.SECONDS_PER_DAY) {
      throw new UnsupportedTemporalTypeException("Unit is too large to be used for truncation")
    }
    val dur: Long = unitDur.toNanos
    if ((Time.NANOS_PER_DAY % dur) != 0) {
      throw new UnsupportedTemporalTypeException("Unit must divide into a standard day without remainder")
    }
    val nod: Long = (seconds % Time.SECONDS_PER_DAY) * Time.NANOS_PER_SECOND + nanos
    val result: Long = (nod / dur) * dur
     plusNanos(result - nod)
  }

  /**
   * Returns a copy of this instant with the specified amount added.
   * <p>
   * This returns an {@code Instant}, based on this one, with the specified amount added.
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
   * @return an { @code Instant} based on this instant with the addition made, not null
   * @throws DateTimeException if the addition cannot be made
   * @throws ArithmeticException if numeric overflow occurs
   */
  override def plus(amountToAdd: TemporalAmount): Instant = {
     amountToAdd.addTo(this).asInstanceOf[Instant]
  }

  /**
   * Returns a copy of this instant with the specified amount added.
   * <p>
   * This returns an {@code Instant}, based on this one, with the amount
   * in terms of the unit added. If it is not possible to add the amount, because the
   * unit is not supported or for some other reason, an exception is thrown.
   * <p>
   * If the field is a {@link ChronoUnit} then the addition is implemented here.
   * The supported fields behave as follows:
   * <ul>
   * <li>{@code NANOS} -
   * Returns a {@code Instant} with the specified number of nanoseconds added.
   * This is equivalent to {@link #plusNanos(long)}.
   * <li>{@code MICROS} -
   * Returns a {@code Instant} with the specified number of microseconds added.
   * This is equivalent to {@link #plusNanos(long)} with the amount
   * multiplied by 1,000.
   * <li>{@code MILLIS} -
   * Returns a {@code Instant} with the specified number of milliseconds added.
   * This is equivalent to {@link #plusNanos(long)} with the amount
   * multiplied by 1,000,000.
   * <li>{@code SECONDS} -
   * Returns a {@code Instant} with the specified number of seconds added.
   * This is equivalent to {@link #plusSeconds(long)}.
   * <li>{@code MINUTES} -
   * Returns a {@code Instant} with the specified number of minutes added.
   * This is equivalent to {@link #plusSeconds(long)} with the amount
   * multiplied by 60.
   * <li>{@code HOURS} -
   * Returns a {@code Instant} with the specified number of hours added.
   * This is equivalent to {@link #plusSeconds(long)} with the amount
   * multiplied by 3,600.
   * <li>{@code HALF_DAYS} -
   * Returns a {@code Instant} with the specified number of half-days added.
   * This is equivalent to {@link #plusSeconds(long)} with the amount
   * multiplied by 43,200 (12 hours).
   * <li>{@code DAYS} -
   * Returns a {@code Instant} with the specified number of days added.
   * This is equivalent to {@link #plusSeconds(long)} with the amount
   * multiplied by 86,400 (24 hours).
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
   * @return an { @code Instant} based on this instant with the specified amount added, not null
   * @throws DateTimeException if the addition cannot be made
   * @throws UnsupportedTemporalTypeException if the unit is not supported
   * @throws ArithmeticException if numeric overflow occurs
   */
  def plus(amountToAdd: Long, unit: TemporalUnit): Instant = {
    if (unit.isInstanceOf[ChronoUnit]) {
      unit.asInstanceOf[ChronoUnit] match {
        case NANOS =>
           plusNanos(amountToAdd)
        case MICROS =>
           plus(amountToAdd / 1000 _000, (amountToAdd % 1000 _000) * 1000)
        case MILLIS =>
           plusMillis(amountToAdd)
        case SECONDS =>
           plusSeconds(amountToAdd)
        case MINUTES =>
           plusSeconds(Math.multiplyExact(amountToAdd, SECONDS_PER_MINUTE))
        case HOURS =>
           plusSeconds(Math.multiplyExact(amountToAdd, SECONDS_PER_HOUR))
        case HALF_DAYS =>
           plusSeconds(Math.multiplyExact(amountToAdd, SECONDS_PER_DAY / 2))
        case DAYS =>
           plusSeconds(Math.multiplyExact(amountToAdd, SECONDS_PER_DAY))
      }
      throw new UnsupportedTemporalTypeException("Unsupported unit: " + unit)
    }
     unit.addTo(this, amountToAdd)
  }

  /**
   * Returns a copy of this instant with the specified duration in seconds added.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param secondsToAdd  the seconds to add, positive or negative
   * @return an { @code Instant} based on this instant with the specified seconds added, not null
   * @throws DateTimeException if the result exceeds the maximum or minimum instant
   * @throws ArithmeticException if numeric overflow occurs
   */
  def plusSeconds(secondsToAdd: Long): Instant = {
     plus(secondsToAdd, 0)
  }

  /**
   * Returns a copy of this instant with the specified duration in milliseconds added.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param millisToAdd  the milliseconds to add, positive or negative
   * @return an { @code Instant} based on this instant with the specified milliseconds added, not null
   * @throws DateTimeException if the result exceeds the maximum or minimum instant
   * @throws ArithmeticException if numeric overflow occurs
   */
  def plusMillis(millisToAdd: Long): Instant = {
     plus(millisToAdd / 1000, (millisToAdd % 1000) * 1000 _000)
  }

  /**
   * Returns a copy of this instant with the specified duration in nanoseconds added.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param nanosToAdd  the nanoseconds to add, positive or negative
   * @return an { @code Instant} based on this instant with the specified nanoseconds added, not null
   * @throws DateTimeException if the result exceeds the maximum or minimum instant
   * @throws ArithmeticException if numeric overflow occurs
   */
  def plusNanos(nanosToAdd: Long): Instant = {
     plus(0, nanosToAdd)
  }

  /**
   * Returns a copy of this instant with the specified duration added.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param secondsToAdd  the seconds to add, positive or negative
   * @param nanosToAdd  the nanos to add, positive or negative
   * @return an { @code Instant} based on this instant with the specified seconds added, not null
   * @throws DateTimeException if the result exceeds the maximum or minimum instant
   * @throws ArithmeticException if numeric overflow occurs
   */
  private def plus(secondsToAdd: Long, nanosToAdd: Long): Instant = {
    if ((secondsToAdd | nanosToAdd) == 0) {
       this
    }
    var epochSec: Long = Math.addExact(seconds, secondsToAdd)
    epochSec = Math.addExact(epochSec, nanosToAdd / NANOS_PER_SECOND)
    nanosToAdd = nanosToAdd % NANOS_PER_SECOND
    val nanoAdjustment: Long = nanos + nanosToAdd
     ofEpochSecond(epochSec, nanoAdjustment)
  }

  /**
   * Returns a copy of this instant with the specified amount subtracted.
   * <p>
   * This returns an {@code Instant}, based on this one, with the specified amount subtracted.
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
   * @return an { @code Instant} based on this instant with the subtraction made, not null
   * @throws DateTimeException if the subtraction cannot be made
   * @throws ArithmeticException if numeric overflow occurs
   */
  override def minus(amountToSubtract: TemporalAmount): Instant = {
     amountToSubtract.subtractFrom(this).asInstanceOf[Instant]
  }

  /**
   * Returns a copy of this instant with the specified amount subtracted.
   * <p>
   * This returns a {@code Instant}, based on this one, with the amount
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
   * @return an { @code Instant} based on this instant with the specified amount subtracted, not null
   * @throws DateTimeException if the subtraction cannot be made
   * @throws UnsupportedTemporalTypeException if the unit is not supported
   * @throws ArithmeticException if numeric overflow occurs
   */
  override def minus(amountToSubtract: Long, unit: TemporalUnit): Instant = {
     (if (amountToSubtract == Long.MIN_VALUE) plus(Long.MAX_VALUE, unit).plus(1, unit) else plus(-amountToSubtract, unit))
  }

  /**
   * Returns a copy of this instant with the specified duration in seconds subtracted.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param secondsToSubtract  the seconds to subtract, positive or negative
   * @return an { @code Instant} based on this instant with the specified seconds subtracted, not null
   * @throws DateTimeException if the result exceeds the maximum or minimum instant
   * @throws ArithmeticException if numeric overflow occurs
   */
  def minusSeconds(secondsToSubtract: Long): Instant = {
    if (secondsToSubtract == Long.MIN_VALUE) {
       plusSeconds(Long.MAX_VALUE).plusSeconds(1)
    }
     plusSeconds(-secondsToSubtract)
  }

  /**
   * Returns a copy of this instant with the specified duration in milliseconds subtracted.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param millisToSubtract  the milliseconds to subtract, positive or negative
   * @return an { @code Instant} based on this instant with the specified milliseconds subtracted, not null
   * @throws DateTimeException if the result exceeds the maximum or minimum instant
   * @throws ArithmeticException if numeric overflow occurs
   */
  def minusMillis(millisToSubtract: Long): Instant = {
    if (millisToSubtract == Long.MIN_VALUE) {
       plusMillis(Long.MAX_VALUE).plusMillis(1)
    }
     plusMillis(-millisToSubtract)
  }

  /**
   * Returns a copy of this instant with the specified duration in nanoseconds subtracted.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param nanosToSubtract  the nanoseconds to subtract, positive or negative
   * @return an { @code Instant} based on this instant with the specified nanoseconds subtracted, not null
   * @throws DateTimeException if the result exceeds the maximum or minimum instant
   * @throws ArithmeticException if numeric overflow occurs
   */
  def minusNanos(nanosToSubtract: Long): Instant = {
    if (nanosToSubtract == Long.MIN_VALUE) {
       plusNanos(Long.MAX_VALUE).plusNanos(1)
    }
     plusNanos(-nanosToSubtract)
  }

  /**
   * Queries this instant using the specified query.
   * <p>
   * This queries this instant using the specified query strategy object.
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
    if (query eq TemporalQuery.precision) {
       NANOS.asInstanceOf[R]
    }
    if (query eq TemporalQuery.chronology || query eq TemporalQuery.zoneId || query eq TemporalQuery.zone || query eq TemporalQuery.offset) {
       null
    }
     query.queryFrom(this)
  }

  /**
   * Adjusts the specified temporal object to have this instant.
   * <p>
   * This returns a temporal object of the same observable type as the input
   * with the instant changed to be the same as this.
   * <p>
   * The adjustment is equivalent to using {@link Temporal#with(TemporalField, long)}
   * twice, passing {@link ChronoField#INSTANT_SECONDS} and
   * {@link ChronoField#NANO_OF_SECOND} as the fields.
   * <p>
   * In most cases, it is clearer to reverse the calling pattern by using
   * {@link Temporal#with(TemporalAdjuster)}:
   * {{{
   * // these two lines are equivalent, but the second approach is recommended
   * temporal = thisInstant.adjustInto(temporal);
   * temporal = temporal.with(thisInstant);
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
     temporal.`with`(INSTANT_SECONDS, seconds).`with`(NANO_OF_SECOND, nanos)
  }

  /**
   * Calculates the amount of time until another instant in terms of the specified unit.
   * <p>
   * This calculates the amount of time between two {@code Instant}
   * objects in terms of a single {@code TemporalUnit}.
   * The start and end points are {@code this} and the specified instant.
   * The result will be negative if the end is before the start.
   * The calculation returns a whole number, representing the number of
   * complete units between the two instants.
   * The {@code Temporal} passed to this method is converted to a
   * {@code Instant} using {@link #from(TemporalAccessor)}.
   * For example, the amount in days between two dates can be calculated
   * using {@code startInstant.until(endInstant, SECONDS)}.
   * <p>
   * There are two equivalent ways of using this method.
   * The first is to invoke this method.
   * The second is to use {@link TemporalUnit#between(Temporal, Temporal)}:
   * {{{
   * // these two lines are equivalent
   * amount = start.until(end, SECONDS);
   * amount = SECONDS.between(start, end);
   * }}}
   * The choice should be made based on which makes the code more readable.
   * <p>
   * The calculation is implemented in this method for {@link ChronoUnit}.
   * The units {@code NANOS}, {@code MICROS}, {@code MILLIS}, {@code SECONDS},
   * {@code MINUTES}, {@code HOURS}, {@code HALF_DAYS} and {@code DAYS}
   * are supported. Other {@code ChronoUnit} values will throw an exception.
   * <p>
   * If the unit is not a {@code ChronoUnit}, then the result of this method
   * is obtained by invoking {@code TemporalUnit.between(Temporal, Temporal)}
   * passing {@code this} as the first argument and the converted input temporal
   * as the second argument.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param endExclusive  the end date, which is converted to an { @code Instant}, not null
   * @param unit  the unit to measure the amount in, not null
   * @return the amount of time between this instant and the end instant
   * @throws DateTimeException if the amount cannot be calculated, or the end
   *                           temporal cannot be converted to an { @code Instant}
   * @throws UnsupportedTemporalTypeException if the unit is not supported
   * @throws ArithmeticException if numeric overflow occurs
   */
  def until(endExclusive: Temporal, unit: TemporalUnit): Long = {
    val end: Instant = Instant.from(endExclusive)
    if (unit.isInstanceOf[ChronoUnit]) {
      val f: ChronoUnit = unit.asInstanceOf[ChronoUnit]
      f match {
        case NANOS =>
           nanosUntil(end)
        case MICROS =>
           nanosUntil(end) / 1000
        case MILLIS =>
           Math.subtractExact(end.toEpochMilli, toEpochMilli)
        case SECONDS =>
           secondsUntil(end)
        case MINUTES =>
           secondsUntil(end) / SECONDS_PER_MINUTE
        case HOURS =>
           secondsUntil(end) / SECONDS_PER_HOUR
        case HALF_DAYS =>
           secondsUntil(end) / (12 * SECONDS_PER_HOUR)
        case DAYS =>
           secondsUntil(end) / (SECONDS_PER_DAY)
      }
      throw new UnsupportedTemporalTypeException("Unsupported unit: " + unit)
    }
     unit.between(this, end)
  }

  private def nanosUntil(end: Instant): Long = {
    val secsDiff: Long = Math.subtractExact(end.seconds, seconds)
    val totalNanos: Long = Math.multiplyExact(secsDiff, NANOS_PER_SECOND)
     Math.addExact(totalNanos, end.nanos - nanos)
  }

  private def secondsUntil(end: Instant): Long = {
    var secsDiff: Long = Math.subtractExact(end.seconds, seconds)
    val nanosDiff: Long = end.nanos - nanos
    if (secsDiff > 0 && nanosDiff < 0) {
      secsDiff -= 1
    }
    else if (secsDiff < 0 && nanosDiff > 0) {
      secsDiff += 1
    }
     secsDiff
  }

  /**
   * Combines this instant with an offset to create an {@code OffsetDateTime}.
   * <p>
   * This returns an {@code OffsetDateTime} formed from this instant at the
   * specified offset from UTC/Greenwich. An exception will be thrown if the
   * instant is too large to fit into an offset date-time.
   * <p>
   * This method is equivalent to
   * {@link OffsetDateTime#ofInstant(Instant, ZoneId) OffsetDateTime.ofInstant(this, offset)}.
   *
   * @param offset  the offset to combine with, not null
   * @return the offset date-time formed from this instant and the specified offset, not null
   * @throws DateTimeException if the result exceeds the supported range
   */
  def atOffset(offset: ZoneOffset): OffsetDateTime = {
     OffsetDateTime.ofInstant(this, offset)
  }

  /**
   * Combines this instant with a time-zone to create a {@code ZonedDateTime}.
   * <p>
   * This returns an {@code ZonedDateTime} formed from this instant at the
   * specified time-zone. An exception will be thrown if the instant is too
   * large to fit into a zoned date-time.
   * <p>
   * This method is equivalent to
   * {@link ZonedDateTime#ofInstant(Instant, ZoneId) ZonedDateTime.ofInstant(this, zone)}.
   *
   * @param zone  the zone to combine with, not null
   * @return the zoned date-time formed from this instant and the specified zone, not null
   * @throws DateTimeException if the result exceeds the supported range
   */
  def atZone(zone: ZoneId): ZonedDateTime = {
     ZonedDateTime.ofInstant(this, zone)
  }

  /**
   * Converts this instant to the number of milliseconds from the epoch
   * of 1970-01-01T00:00:00Z.
   * <p>
   * If this instant represents a point on the time-line too far in the future
   * or past to fit in a {@code long} milliseconds, then an exception is thrown.
   * <p>
   * If this instant has greater than millisecond precision, then the conversion
   * will drop any excess precision information as though the amount in nanoseconds
   * was subject to integer division by one million.
   *
   * @return the number of milliseconds since the epoch of 1970-01-01T00:00:00Z
   * @throws ArithmeticException if numeric overflow occurs
   */
  def toEpochMilli: Long = {
    val millis: Long = Math.multiplyExact(seconds, 1000)
     millis + nanos / 1000 _000
  }

  /**
   * Compares this instant to the specified instant.
   * <p>
   * The comparison is based on the time-line position of the instants.
   * It is "consistent with equals", as defined by {@link Comparable}.
   *
   * @param otherInstant  the other instant to compare to, not null
   * @return the comparator value, negative if less, positive if greater
   * @throws NullPointerException if otherInstant is null
   */
  def compareTo(otherInstant: Instant): Int = {
    val cmp: Int = Long.compare(seconds, otherInstant.seconds)
    if (cmp != 0) {
       cmp
    }
     nanos - otherInstant.nanos
  }

  /**
   * Checks if this instant is after the specified instant.
   * <p>
   * The comparison is based on the time-line position of the instants.
   *
   * @param otherInstant  the other instant to compare to, not null
   * @return true if this instant is after the specified instant
   * @throws NullPointerException if otherInstant is null
   */
  def isAfter(otherInstant: Instant): Boolean = {
     compareTo(otherInstant) > 0
  }

  /**
   * Checks if this instant is before the specified instant.
   * <p>
   * The comparison is based on the time-line position of the instants.
   *
   * @param otherInstant  the other instant to compare to, not null
   * @return true if this instant is before the specified instant
   * @throws NullPointerException if otherInstant is null
   */
  def isBefore(otherInstant: Instant): Boolean = {
     compareTo(otherInstant) < 0
  }

  /**
   * Checks if this instant is equal to the specified instant.
   * <p>
   * The comparison is based on the time-line position of the instants.
   *
   * @param otherInstant  the other instant, null returns false
   * @return true if the other instant is equal to this one
   */
  override def equals(otherInstant: AnyRef): Boolean = {
    if (this eq otherInstant) {
       true
    }
    if (otherInstant.isInstanceOf[Instant]) {
      val other: Instant = otherInstant.asInstanceOf[Instant]
       this.seconds == other.seconds && this.nanos == other.nanos
    }
     false
  }

  /**
   * Returns a hash code for this instant.
   *
   * @return a suitable hash code
   */
  override def hashCode: Int = {
     ((seconds ^ (seconds >>> 32)).asInstanceOf[Int]) + 51 * nanos
  }

  /**
   * A string representation of this instant using ISO-8601 representation.
   * <p>
   * The format used is the same as {@link DateTimeFormatter#ISO_INSTANT}.
   *
   * @return an ISO-8601 representation of this instant, not null
   */
  override def toString: String = {
     DateTimeFormatter.ISO_INSTANT.format(this)
  }

  /**
   * Writes the object using a
   * <a href="../../serialized-form.html#java.time.Ser">dedicated serialized form</a>.
   * {{{
   * out.writeByte(2);  // identifies this as an Instant
   * out.writeLong(seconds);
   * out.writeInt(nanos);
   * }}}
   *
   * @return the instance of { @code Ser}, not null
   */
  private def writeReplace: AnyRef = {
     new Ser(Ser.INSTANT_TYPE, this)
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
    out.writeLong(seconds)
    out.writeInt(nanos)
  }

  /**
   * The number of seconds from the epoch of 1970-01-01T00:00:00Z.
   */
  private final val seconds: Long = 0L
  /**
   * The number of nanoseconds, later along the time-line, from the seconds field.
   * This is always positive, and never exceeds 999,999,999.
   */
  private final val nanos: Int = 0
}




