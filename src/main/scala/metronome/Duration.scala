package metronome

import metronome.format.DateTimeParseException
import metronome.temporal.{TemporalUnit, TemporalAmount}

/**
 * A time-based amount of time, such as '34.5 seconds'.
 * <p>
 * This class models a quantity or amount of time in terms of seconds and nanoseconds.
 * It can be accessed using other duration-based units, such as minutes and hours.
 * In addition, the {@link ChronoUnit#DAYS DAYS} unit can be used and is treated as
 * exactly equal to 24 hours, thus ignoring daylight savings effects.
 * See {@link Period} for the date-based equivalent to this class.
 * <p>
 * A physical duration could be of infinite length.
 * For practicality, the duration is stored with constraints similar to {@link Instant}.
 * The duration uses nanosecond resolution with a maximum value of the seconds that can
 * be held in a {@code long}. This is greater than the current estimated age of the universe.
 * <p>
 * The range of a duration requires the storage of a number larger than a {@code long}.
 * To achieve this, the class stores a {@code long} representing seconds and an {@code int}
 * representing nanosecond-of-second, which will always be between 0 and 999,999,999.
 * The model is of a directed duration, meaning that the duration may be negative.
 * <p>
 * The duration is measured in "seconds", but these are not necessarily identical to
 * the scientific "SI second" definition based on atomic clocks.
 * This difference only impacts durations measured near a leap-second and should not affect
 * most applications.
 * See {@link Instant} for a discussion as to the meaning of the second and time-scales.
 *
 * @implSpec
 * This class is immutable and thread-safe.
 *
 * @since 1.8
 */
object Duration {
  /**
   * Obtains a {@code Duration} representing a number of standard 24 hour days.
   * <p>
   * The seconds are calculated based on the standard definition of a day,
   * where each day is 86400 seconds which implies a 24 hour day.
   * The nanosecond in second field is set to zero.
   *
   * @param days  the number of days, positive or negative
   * @return a { @code Duration}, not null
   * @throws ArithmeticException if the input days exceeds the capacity of { @code Duration}
   */
  def ofDays(days: Long): Duration = {
     create(Math.multiplyExact(days, SECONDS_PER_DAY), 0)
  }

  /**
   * Obtains a {@code Duration} representing a number of standard hours.
   * <p>
   * The seconds are calculated based on the standard definition of an hour,
   * where each hour is 3600 seconds.
   * The nanosecond in second field is set to zero.
   *
   * @param hours  the number of hours, positive or negative
   * @return a { @code Duration}, not null
   * @throws ArithmeticException if the input hours exceeds the capacity of { @code Duration}
   */
  def ofHours(hours: Long): Duration = {
     create(Math.multiplyExact(hours, SECONDS_PER_HOUR), 0)
  }

  /**
   * Obtains a {@code Duration} representing a number of standard minutes.
   * <p>
   * The seconds are calculated based on the standard definition of a minute,
   * where each minute is 60 seconds.
   * The nanosecond in second field is set to zero.
   *
   * @param minutes  the number of minutes, positive or negative
   * @return a { @code Duration}, not null
   * @throws ArithmeticException if the input minutes exceeds the capacity of { @code Duration}
   */
  def ofMinutes(minutes: Long): Duration = {
     create(Math.multiplyExact(minutes, SECONDS_PER_MINUTE), 0)
  }

  /**
   * Obtains a {@code Duration} representing a number of seconds.
   * <p>
   * The nanosecond in second field is set to zero.
   *
   * @param seconds  the number of seconds, positive or negative
   * @return a { @code Duration}, not null
   */
  def ofSeconds(seconds: Long): Duration = {
     create(seconds, 0)
  }

  /**
   * Obtains a {@code Duration} representing a number of seconds and an
   * adjustment in nanoseconds.
   * <p>
   * This method allows an arbitrary number of nanoseconds to be passed in.
   * The factory will alter the values of the second and nanosecond in order
   * to ensure that the stored nanosecond is in the range 0 to 999,999,999.
   * For example, the following will result in the exactly the same duration:
   * {{{
   * Duration.ofSeconds(3, 1);
   * Duration.ofSeconds(4, -999_999_999);
   * Duration.ofSeconds(2, 1000_000_001);
   * }}}
   *
   * @param seconds  the number of seconds, positive or negative
   * @param nanoAdjustment  the nanosecond adjustment to the number of seconds, positive or negative
   * @return a { @code Duration}, not null
   * @throws ArithmeticException if the adjustment causes the seconds to exceed the capacity of { @code Duration}
   */
  def ofSeconds(seconds: Long, nanoAdjustment: Long): Duration = {
    val secs: Long = Math.addExact(seconds, Math.floorDiv(nanoAdjustment, NANOS_PER_SECOND))
    val nos: Int = Math.floorMod(nanoAdjustment, NANOS_PER_SECOND).asInstanceOf[Int]
     create(secs, nos)
  }

  /**
   * Obtains a {@code Duration} representing a number of milliseconds.
   * <p>
   * The seconds and nanoseconds are extracted from the specified milliseconds.
   *
   * @param millis  the number of milliseconds, positive or negative
   * @return a { @code Duration}, not null
   */
  def ofMillis(millis: Long): Duration = {
    var secs: Long = millis / 1000
    var mos: Int = (millis % 1000).asInstanceOf[Int]
    if (mos < 0) {
      mos += 1000
      secs -= 1
    }
     create(secs, mos * 1000 _000)
  }

  /**
   * Obtains a {@code Duration} representing a number of nanoseconds.
   * <p>
   * The seconds and nanoseconds are extracted from the specified nanoseconds.
   *
   * @param nanos  the number of nanoseconds, positive or negative
   * @return a { @code Duration}, not null
   */
  def ofNanos(nanos: Long): Duration = {
    var secs: Long = nanos / NANOS_PER_SECOND
    var nos: Int = (nanos % NANOS_PER_SECOND).asInstanceOf[Int]
    if (nos < 0) {
      nos += NANOS_PER_SECOND
      secs -= 1
    }
     create(secs, nos)
  }

  /**
   * Obtains a {@code Duration} representing an amount in the specified unit.
   * <p>
   * The parameters represent the two parts of a phrase like '6 Hours'. For example:
   * {{{
   * Duration.of(3, SECONDS);
   * Duration.of(465, HOURS);
   * }}}
   * Only a subset of units are accepted by this method.
   * The unit must either have an {@linkplain TemporalUnit#isDurationEstimated() exact duration} or
   * be {@link ChronoUnit#DAYS} which is treated as 24 hours. Other units throw an exception.
   *
   * @param amount  the amount of the duration, measured in terms of the unit, positive or negative
   * @param unit  the unit that the duration is measured in, must have an exact duration, not null
   * @return a { @code Duration}, not null
   * @throws DateTimeException if the period unit has an estimated duration
   * @throws ArithmeticException if a numeric overflow occurs
   */
  def of(amount: Long, unit: TemporalUnit): Duration = {
     ZERO.plus(amount, unit)
  }

  /**
   * Obtains an instance of {@code Duration} from a temporal amount.
   * <p>
   * This obtains a duration based on the specified amount.
   * A {@code TemporalAmount} represents an  amount of time, which may be
   * date-based or time-based, which this factory extracts to a duration.
   * <p>
   * The conversion loops around the set of units from the amount and uses
   * the {@linkplain TemporalUnit#getDuration() duration} of the unit to
   * calculate the total {@code Duration}.
   * Only a subset of units are accepted by this method. The unit must either
   * have an {@linkplain TemporalUnit#isDurationEstimated() exact duration}
   * or be {@link ChronoUnit#DAYS} which is treated as 24 hours.
   * If any other units are found then an exception is thrown.
   *
   * @param amount  the temporal amount to convert, not null
   * @return the equivalent duration, not null
   * @throws DateTimeException if unable to convert to a { @code Duration}
   * @throws ArithmeticException if numeric overflow occurs
   */
  def from(amount: TemporalAmount): Duration = {
    object
    var duration: Duration = ZERO
    for (unit <- amount.getUnits) {
      duration = duration.plus(amount.get(unit), unit)
    }
     duration
  }

  /**
   * Obtains a {@code Duration} from a text string such as {@code PnDTnHnMn.nS}.
   * <p>
   * This will parse a textual representation of a duration, including the
   * string produced by {@code toString()}. The formats accepted are based
   * on the ISO-8601 duration format {@code PnDTnHnMn.nS} with days
   * considered to be exactly 24 hours.
   * <p>
   * The string starts with an optional sign, denoted by the ASCII negative
   * or positive symbol. If negative, the whole period is negated.
   * The ASCII letter "P" is next in upper or lower case.
   * There are then four sections, each consisting of a number and a suffix.
   * The sections have suffixes in ASCII of "D", "H", "M" and "S" for
   * days, hours, minutes and seconds, accepted in upper or lower case.
   * The suffixes must occur in order. The ASCII letter "T" must occur before
   * the first occurrence, if any, of an hour, minute or second section.
   * At least one of the four sections must be present, and if "T" is present
   * there must be at least one section after the "T".
   * The number part of each section must consist of one or more ASCII digits.
   * The number may be prefixed by the ASCII negative or positive symbol.
   * The number of days, hours and minutes must parse to an {@code long}.
   * The number of seconds must parse to an {@code long} with optional fraction.
   * The decimal point may be either a dot or a comma.
   * The fractional part may have from zero to 9 digits.
   * <p>
   * The leading plus/minus sign, and negative values for other units are
   * not part of the ISO-8601 standard.
   * <p>
   * Examples:
   * {{{
   * "PT20.345S" -- parses as "20.345 seconds"
   * "PT15M"     -- parses as "15 minutes" (where a minute is 60 seconds)
   * "PT10H"     -- parses as "10 hours" (where an hour is 3600 seconds)
   * "P2D"       -- parses as "2 days" (where a day is 24 hours or 86400 seconds)
   * "P2DT3H4M"  -- parses as "2 days, 3 hours and 4 minutes"
   * "P-6H3M"    -- parses as "-6 hours and +3 minutes"
   * "-P6H3M"    -- parses as "-6 hours and -3 minutes"
   * "-P-6H+3M"  -- parses as "+6 hours and -3 minutes"
   * }}}
   *
   * @param text  the text to parse, not null
   * @return the parsed duration, not null
   * @throws DateTimeParseException if the text cannot be parsed to a duration
   */
  def parse(text: CharSequence): Duration = {
    object
    val matcher: Matcher = PATTERN.matcher(text)
    if (matcher.matches) {
      if (("T" == matcher.group(3)) == false) {
        val negate: Boolean = "-" == matcher.group(1)
        val dayMatch: String = matcher.group(2)
        val hourMatch: String = matcher.group(4)
        val minuteMatch: String = matcher.group(5)
        val secondMatch: String = matcher.group(6)
        val fractionMatch: String = matcher.group(7)
        if (dayMatch != null || hourMatch != null || minuteMatch != null || secondMatch != null) {
          val daysAsSecs: Long = parseNumber(text, dayMatch, SECONDS_PER_DAY, "days")
          val hoursAsSecs: Long = parseNumber(text, hourMatch, SECONDS_PER_HOUR, "hours")
          val minsAsSecs: Long = parseNumber(text, minuteMatch, SECONDS_PER_MINUTE, "minutes")
          val seconds: Long = parseNumber(text, secondMatch, 1, "seconds")
          val nanos: Int = parseFraction(text, fractionMatch, if (seconds < 0) -1 else 1)
          try {
             create(negate, daysAsSecs, hoursAsSecs, minsAsSecs, seconds, nanos)
          }
          catch {
            case ex: ArithmeticException => {
              throw new DateTimeParseException("Text cannot be parsed to a Duration: overflow", text, 0).initCause(ex).asInstanceOf[DateTimeParseException]
            }
          }
        }
      }
    }
    throw new DateTimeParseException("Text cannot be parsed to a Duration", text, 0)
  }

  private def parseNumber(text: CharSequence, parsed: String, multiplier: Int, errorText: String): Long = {
    if (parsed == null) {
       0
    }
    try {
      val `val`: Long = Long.parseLong(parsed)
       Math.multiplyExact(`val`, multiplier)
    }
    catch {
      case ex: Any => {
        throw new DateTimeParseException("Text cannot be parsed to a Duration: " + errorText, text, 0).initCause(ex).asInstanceOf[DateTimeParseException]
      }
    }
  }

  private def parseFraction(text: CharSequence, parsed: String, negate: Int): Int = {
    if (parsed == null || parsed.length == 0) {
       0
    }
    try {
      parsed = (parsed + "000000000").substring(0, 9)
       Integer.parseInt(parsed) * negate
    }
    catch {
      case ex: Any => {
        throw new DateTimeParseException("Text cannot be parsed to a Duration: fraction", text, 0).initCause(ex).asInstanceOf[DateTimeParseException]
      }
    }
  }

  private def create(negate: Boolean, daysAsSecs: Long, hoursAsSecs: Long, minsAsSecs: Long, secs: Long, nanos: Int): Duration = {
    val seconds: Long = Math.addExact(daysAsSecs, Math.addExact(hoursAsSecs, Math.addExact(minsAsSecs, secs)))
    if (negate) {
       ofSeconds(seconds, nanos).negated
    }
     ofSeconds(seconds, nanos)
  }

  /**
   * Obtains a {@code Duration} representing the duration between two temporal objects.
   * <p>
   * This calculates the duration between two temporal objects. If the objects
   * are of different types, then the duration is calculated based on the type
   * of the first object. For example, if the first argument is a {@code Time}
   * then the second argument is converted to a {@code Time}.
   * <p>
   * The specified temporal objects must support the {@link ChronoUnit#SECONDS SECONDS} unit.
   * For full accuracy, either the {@link ChronoUnit#NANOS NANOS} unit or the
   * {@link ChronoField#NANO_OF_SECOND NANO_OF_SECOND} field should be supported.
   * <p>
   * The result of this method can be a negative period if the end is before the start.
   * To guarantee to obtain a positive duration call {@link #abs()} on the result.
   *
   * @param startInclusive  the start instant, inclusive, not null
   * @param endExclusive  the end instant, exclusive, not null
   * @return a { @code Duration}, not null
   * @throws DateTimeException if the seconds between the temporals cannot be obtained
   * @throws ArithmeticException if the calculation exceeds the capacity of { @code Duration}
   */
  def between(startInclusive: Temporal, endExclusive: Temporal): Duration = {
    try {
       ofNanos(startInclusive.until(endExclusive, NANOS))
    }
    catch {
      case ex: Any => {
        var secs: Long = startInclusive.until(endExclusive, SECONDS)
        var nanos: Long = 0L
        try {
          nanos = endExclusive.getLong(NANO_OF_SECOND) - startInclusive.getLong(NANO_OF_SECOND)
          if (secs > 0 && nanos < 0) {
            secs += 1
          }
          else if (secs < 0 && nanos > 0) {
            secs -= 1
          }
        }
        catch {
          case ex2: DateTimeException => {
            nanos = 0
          }
        }
         ofSeconds(secs, nanos)
      }
    }
  }

  /**
   * Obtains an instance of {@code Duration} using seconds and nanoseconds.
   *
   * @param seconds  the length of the duration in seconds, positive or negative
   * @param nanoAdjustment  the nanosecond adjustment within the second, from 0 to 999,999,999
   */
  private def create(seconds: Long, nanoAdjustment: Int): Duration = {
    if ((seconds | nanoAdjustment) == 0) {
       ZERO
    }
     new Duration(seconds, nanoAdjustment)
  }

  /**
   * Creates an instance of {@code Duration} from a number of seconds.
   *
   * @param seconds  the number of seconds, up to scale 9, positive or negative
   * @return a { @code Duration}, not null
   * @throws ArithmeticException if numeric overflow occurs
   */
  private def create(seconds: BigDecimal): Duration = {
    val nanos: BigInteger = seconds.movePointRight(9).toBigIntegerExact
    val divRem: Array[BigInteger] = nanos.divideAndRemainder(BI_NANOS_PER_SECOND)
    if (divRem(0).bitLength > 63) {
      throw new ArithmeticException("Exceeds capacity of Duration: " + nanos)
    }
     ofSeconds(divRem(0).longValue, divRem(1).intValue)
  }

  private[time] def readExternal(in: DataInput): Duration = {
    val seconds: Long = in.readLong
    val nanos: Int = in.readInt
     Duration.ofSeconds(seconds, nanos)
  }

  /**
   * Constant for a duration of zero.
   */
  final val ZERO: Duration = new Duration(0, 0)
  /**
   * Serialization version.
   */
  private final val serialVersionUID: Long = 3078945930695997490L
  /**
   * Constant for nanos per second.
   */
  private final val BI_NANOS_PER_SECOND: BigInteger = BigInteger.valueOf(NANOS_PER_SECOND)
  /**
   * The pattern for parsing.
   */
  private final val PATTERN: Pattern = Pattern.compile("([-+]?)P(?:([-+]?[0-9]+)D)?" + "(T(?:([-+]?[0-9]+)H)?(?:([-+]?[0-9]+)M)?(?:([-+]?[0-9]+)(?:[.,]([0-9]{0,9}))?S)?)?", Pattern.CASE_INSENSITIVE)

  /**
   * Private class to delay initialization of this list until needed.
   * The circular dependency between Duration and ChronoUnit prevents
   * the simple initialization in Duration.
   */
  private object DurationUnits {
    private[time] final val UNITS: List[TemporalUnit] = Collections.unmodifiableList(Arrays.asList[TemporalUnit](SECONDS, NANOS))
  }

}

final class Duration extends TemporalAmount with Comparable[Duration]  {
  /**
   * Constructs an instance of {@code Duration} using seconds and nanoseconds.
   *
   * @param seconds  the length of the duration in seconds, positive or negative
   * @param nanos  the nanoseconds within the second, from 0 to 999,999,999
   */
  private def this(seconds: Long, nanos: Int) {

    `super`
    this.seconds = seconds
    this.nanos = nanos
  }

  /**
   * Gets the value of the requested unit.
   * <p>
   * This returns a value for each of the two supported units,
   * {@link ChronoUnit#SECONDS SECONDS} and {@link ChronoUnit#NANOS NANOS}.
   * All other units throw an exception.
   *
   * @param unit the { @code TemporalUnit} for which to  the value
   * @return the long value of the unit
   * @throws DateTimeException if the unit is not supported
   * @throws UnsupportedTemporalTypeException if the unit is not supported
   */
  def get(unit: TemporalUnit): Long = {
    if (unit eq SECONDS) {
       seconds
    }
    else if (unit eq NANOS) {
       nanos
    }
    else {
      throw new UnsupportedTemporalTypeException("Unsupported unit: " + unit)
    }
  }

  /**
   * Gets the set of units supported by this duration.
   * <p>
   * The supported units are {@link ChronoUnit#SECONDS SECONDS},
   * and {@link ChronoUnit#NANOS NANOS}.
   * They are returned in the order seconds, nanos.
   * <p>
   * This set can be used in conjunction with {@link #get(TemporalUnit)}
   * to access the entire state of the period.
   *
   * @return a list containing the seconds and nanos units, not null
   */
  def getUnits: List[TemporalUnit] = {
     DurationUnits.UNITS
  }

  /**
   * Checks if this duration is zero length.
   * <p>
   * A {@code Duration} represents a directed distance between two points on
   * the time-line and can therefore be positive, zero or negative.
   * This method checks whether the length is zero.
   *
   * @return true if this duration has a total length equal to zero
   */
  def isZero: Boolean = {
     (seconds | nanos) == 0
  }

  /**
   * Checks if this duration is negative, excluding zero.
   * <p>
   * A {@code Duration} represents a directed distance between two points on
   * the time-line and can therefore be positive, zero or negative.
   * This method checks whether the length is less than zero.
   *
   * @return true if this duration has a total length less than zero
   */
  def isNegative: Boolean = {
     seconds < 0
  }

  /**
   * Gets the number of seconds in this duration.
   * <p>
   * The length of the duration is stored using two fields - seconds and nanoseconds.
   * The nanoseconds part is a value from 0 to 999,999,999 that is an adjustment to
   * the length in seconds.
   * The total duration is defined by calling this method and {@link #getNano()}.
   * <p>
   * A {@code Duration} represents a directed distance between two points on the time-line.
   * A negative duration is expressed by the negative sign of the seconds part.
   * A duration of -1 nanosecond is stored as -1 seconds plus 999,999,999 nanoseconds.
   *
   * @return the whole seconds part of the length of the duration, positive or negative
   */
  def getSeconds: Long = {
     seconds
  }

  /**
   * Gets the number of nanoseconds within the second in this duration.
   * <p>
   * The length of the duration is stored using two fields - seconds and nanoseconds.
   * The nanoseconds part is a value from 0 to 999,999,999 that is an adjustment to
   * the length in seconds.
   * The total duration is defined by calling this method and {@link #getSeconds()}.
   * <p>
   * A {@code Duration} represents a directed distance between two points on the time-line.
   * A negative duration is expressed by the negative sign of the seconds part.
   * A duration of -1 nanosecond is stored as -1 seconds plus 999,999,999 nanoseconds.
   *
   * @return the nanoseconds within the second part of the length of the duration, from 0 to 999,999,999
   */
  def getNano: Int = {
     nanos
  }

  /**
   * Returns a copy of this duration with the specified amount of seconds.
   * <p>
   * This returns a duration with the specified seconds, retaining the
   * nano-of-second part of this duration.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param seconds  the seconds to represent, may be negative
   * @return a { @code Duration} based on this period with the requested seconds, not null
   */
  def withSeconds(seconds: Long): Duration = {
     create(seconds, nanos)
  }

  /**
   * Returns a copy of this duration with the specified nano-of-second.
   * <p>
   * This returns a duration with the specified nano-of-second, retaining the
   * seconds part of this duration.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param nanoOfSecond  the nano-of-second to represent, from 0 to 999,999,999
   * @return a { @code Duration} based on this period with the requested nano-of-second, not null
   * @throws DateTimeException if the nano-of-second is invalid
   */
  def withNanos(nanoOfSecond: Int): Duration = {
    NANO_OF_SECOND.checkValidIntValue(nanoOfSecond)
     create(seconds, nanoOfSecond)
  }

  /**
   * Returns a copy of this duration with the specified duration added.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param duration  the duration to add, positive or negative, not null
   * @return a { @code Duration} based on this duration with the specified duration added, not null
   * @throws ArithmeticException if numeric overflow occurs
   */
  def plus(duration: Duration): Duration = {
     plus(duration.getSeconds, duration.getNano)
  }

  /**
   * Returns a copy of this duration with the specified duration added.
   * <p>
   * The duration amount is measured in terms of the specified unit.
   * Only a subset of units are accepted by this method.
   * The unit must either have an {@linkplain TemporalUnit#isDurationEstimated() exact duration} or
   * be {@link ChronoUnit#DAYS} which is treated as 24 hours. Other units throw an exception.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param amountToAdd  the amount of the period, measured in terms of the unit, positive or negative
   * @param unit  the unit that the period is measured in, must have an exact duration, not null
   * @return a { @code Duration} based on this duration with the specified duration added, not null
   * @throws UnsupportedTemporalTypeException if the unit is not supported
   * @throws ArithmeticException if numeric overflow occurs
   */
  def plus(amountToAdd: Long, unit: TemporalUnit): Duration = {
    object
    if (unit eq DAYS) {
       plus(Math.multiplyExact(amountToAdd, SECONDS_PER_DAY), 0)
    }
    if (unit.isDurationEstimated) {
      throw new UnsupportedTemporalTypeException("Unit must not have an estimated duration")
    }
    if (amountToAdd == 0) {
       this
    }
    if (unit.isInstanceOf[ChronoUnit]) {
      unit.asInstanceOf[ChronoUnit] match {
        case NANOS =>
           plusNanos(amountToAdd)
        case MICROS =>
           plusSeconds((amountToAdd / (1000 _000L * 1000) ) * 1000).plusNanos ((amountToAdd % (1000 _000L * 1000) ) * 1000)
        case MILLIS =>
           plusMillis(amountToAdd)
        case SECONDS =>
           plusSeconds(amountToAdd)
      }
       plusSeconds(Math.multiplyExact(unit.getDuration.seconds, amountToAdd))
    }
    val duration: Duration = unit.getDuration.multipliedBy(amountToAdd)
     plusSeconds(duration.getSeconds).plusNanos(duration.getNano)
  }

  /**
   * Returns a copy of this duration with the specified duration in standard 24 hour days added.
   * <p>
   * The number of days is multiplied by 86400 to obtain the number of seconds to add.
   * This is based on the standard definition of a day as 24 hours.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param daysToAdd  the days to add, positive or negative
   * @return a { @code Duration} based on this duration with the specified days added, not null
   * @throws ArithmeticException if numeric overflow occurs
   */
  def plusDays(daysToAdd: Long): Duration = {
     plus(Math.multiplyExact(daysToAdd, SECONDS_PER_DAY), 0)
  }

  /**
   * Returns a copy of this duration with the specified duration in hours added.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param hoursToAdd  the hours to add, positive or negative
   * @return a { @code Duration} based on this duration with the specified hours added, not null
   * @throws ArithmeticException if numeric overflow occurs
   */
  def plusHours(hoursToAdd: Long): Duration = {
     plus(Math.multiplyExact(hoursToAdd, SECONDS_PER_HOUR), 0)
  }

  /**
   * Returns a copy of this duration with the specified duration in minutes added.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param minutesToAdd  the minutes to add, positive or negative
   * @return a { @code Duration} based on this duration with the specified minutes added, not null
   * @throws ArithmeticException if numeric overflow occurs
   */
  def plusMinutes(minutesToAdd: Long): Duration = {
     plus(Math.multiplyExact(minutesToAdd, SECONDS_PER_MINUTE), 0)
  }

  /**
   * Returns a copy of this duration with the specified duration in seconds added.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param secondsToAdd  the seconds to add, positive or negative
   * @return a { @code Duration} based on this duration with the specified seconds added, not null
   * @throws ArithmeticException if numeric overflow occurs
   */
  def plusSeconds(secondsToAdd: Long): Duration = {
     plus(secondsToAdd, 0)
  }

  /**
   * Returns a copy of this duration with the specified duration in milliseconds added.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param millisToAdd  the milliseconds to add, positive or negative
   * @return a { @code Duration} based on this duration with the specified milliseconds added, not null
   * @throws ArithmeticException if numeric overflow occurs
   */
  def plusMillis(millisToAdd: Long): Duration = {
     plus(millisToAdd / 1000, (millisToAdd % 1000) * 1000 _000)
  }

  /**
   * Returns a copy of this duration with the specified duration in nanoseconds added.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param nanosToAdd  the nanoseconds to add, positive or negative
   * @return a { @code Duration} based on this duration with the specified nanoseconds added, not null
   * @throws ArithmeticException if numeric overflow occurs
   */
  def plusNanos(nanosToAdd: Long): Duration = {
     plus(0, nanosToAdd)
  }

  /**
   * Returns a copy of this duration with the specified duration added.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param secondsToAdd  the seconds to add, positive or negative
   * @param nanosToAdd  the nanos to add, positive or negative
   * @return a { @code Duration} based on this duration with the specified seconds added, not null
   * @throws ArithmeticException if numeric overflow occurs
   */
  private def plus(secondsToAdd: Long, nanosToAdd: Long): Duration = {
    if ((secondsToAdd | nanosToAdd) == 0) {
       this
    }
    var epochSec: Long = Math.addExact(seconds, secondsToAdd)
    epochSec = Math.addExact(epochSec, nanosToAdd / NANOS_PER_SECOND)
    nanosToAdd = nanosToAdd % NANOS_PER_SECOND
    val nanoAdjustment: Long = nanos + nanosToAdd
     ofSeconds(epochSec, nanoAdjustment)
  }

  /**
   * Returns a copy of this duration with the specified duration subtracted.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param duration  the duration to subtract, positive or negative, not null
   * @return a { @code Duration} based on this duration with the specified duration subtracted, not null
   * @throws ArithmeticException if numeric overflow occurs
   */
  def minus(duration: Duration): Duration = {
    val secsToSubtract: Long = duration.getSeconds
    val nanosToSubtract: Int = duration.getNano
    if (secsToSubtract == Long.MIN_VALUE) {
       plus(Long.MAX_VALUE, -nanosToSubtract).plus(1, 0)
    }
     plus(-secsToSubtract, -nanosToSubtract)
  }

  /**
   * Returns a copy of this duration with the specified duration subtracted.
   * <p>
   * The duration amount is measured in terms of the specified unit.
   * Only a subset of units are accepted by this method.
   * The unit must either have an {@linkplain TemporalUnit#isDurationEstimated() exact duration} or
   * be {@link ChronoUnit#DAYS} which is treated as 24 hours. Other units throw an exception.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param amountToSubtract  the amount of the period, measured in terms of the unit, positive or negative
   * @param unit  the unit that the period is measured in, must have an exact duration, not null
   * @return a { @code Duration} based on this duration with the specified duration subtracted, not null
   * @throws ArithmeticException if numeric overflow occurs
   */
  def minus(amountToSubtract: Long, unit: TemporalUnit): Duration = {
     (if (amountToSubtract == Long.MIN_VALUE) plus(Long.MAX_VALUE, unit).plus(1, unit) else plus(-amountToSubtract, unit))
  }

  /**
   * Returns a copy of this duration with the specified duration in standard 24 hour days subtracted.
   * <p>
   * The number of days is multiplied by 86400 to obtain the number of seconds to subtract.
   * This is based on the standard definition of a day as 24 hours.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param daysToSubtract  the days to subtract, positive or negative
   * @return a { @code Duration} based on this duration with the specified days subtracted, not null
   * @throws ArithmeticException if numeric overflow occurs
   */
  def minusDays(daysToSubtract: Long): Duration = {
     (if (daysToSubtract == Long.MIN_VALUE) plusDays(Long.MAX_VALUE).plusDays(1) else plusDays(-daysToSubtract))
  }

  /**
   * Returns a copy of this duration with the specified duration in hours subtracted.
   * <p>
   * The number of hours is multiplied by 3600 to obtain the number of seconds to subtract.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param hoursToSubtract  the hours to subtract, positive or negative
   * @return a { @code Duration} based on this duration with the specified hours subtracted, not null
   * @throws ArithmeticException if numeric overflow occurs
   */
  def minusHours(hoursToSubtract: Long): Duration = {
     (if (hoursToSubtract == Long.MIN_VALUE) plusHours(Long.MAX_VALUE).plusHours(1) else plusHours(-hoursToSubtract))
  }

  /**
   * Returns a copy of this duration with the specified duration in minutes subtracted.
   * <p>
   * The number of hours is multiplied by 60 to obtain the number of seconds to subtract.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param minutesToSubtract  the minutes to subtract, positive or negative
   * @return a { @code Duration} based on this duration with the specified minutes subtracted, not null
   * @throws ArithmeticException if numeric overflow occurs
   */
  def minusMinutes(minutesToSubtract: Long): Duration = {
     (if (minutesToSubtract == Long.MIN_VALUE) plusMinutes(Long.MAX_VALUE).plusMinutes(1) else plusMinutes(-minutesToSubtract))
  }

  /**
   * Returns a copy of this duration with the specified duration in seconds subtracted.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param secondsToSubtract  the seconds to subtract, positive or negative
   * @return a { @code Duration} based on this duration with the specified seconds subtracted, not null
   * @throws ArithmeticException if numeric overflow occurs
   */
  def minusSeconds(secondsToSubtract: Long): Duration = {
     (if (secondsToSubtract == Long.MIN_VALUE) plusSeconds(Long.MAX_VALUE).plusSeconds(1) else plusSeconds(-secondsToSubtract))
  }

  /**
   * Returns a copy of this duration with the specified duration in milliseconds subtracted.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param millisToSubtract  the milliseconds to subtract, positive or negative
   * @return a { @code Duration} based on this duration with the specified milliseconds subtracted, not null
   * @throws ArithmeticException if numeric overflow occurs
   */
  def minusMillis(millisToSubtract: Long): Duration = {
     (if (millisToSubtract == Long.MIN_VALUE) plusMillis(Long.MAX_VALUE).plusMillis(1) else plusMillis(-millisToSubtract))
  }

  /**
   * Returns a copy of this duration with the specified duration in nanoseconds subtracted.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param nanosToSubtract  the nanoseconds to subtract, positive or negative
   * @return a { @code Duration} based on this duration with the specified nanoseconds subtracted, not null
   * @throws ArithmeticException if numeric overflow occurs
   */
  def minusNanos(nanosToSubtract: Long): Duration = {
     (if (nanosToSubtract == Long.MIN_VALUE) plusNanos(Long.MAX_VALUE).plusNanos(1) else plusNanos(-nanosToSubtract))
  }

  /**
   * Returns a copy of this duration multiplied by the scalar.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param multiplicand  the value to multiply the duration by, positive or negative
   * @return a { @code Duration} based on this duration multiplied by the specified scalar, not null
   * @throws ArithmeticException if numeric overflow occurs
   */
  def multipliedBy(multiplicand: Long): Duration = {
    if (multiplicand == 0) {
       ZERO
    }
    if (multiplicand == 1) {
       this
    }
     create(toSeconds.multiply(BigDecimal.valueOf(multiplicand)))
  }

  /**
   * Returns a copy of this duration divided by the specified value.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param divisor  the value to divide the duration by, positive or negative, not zero
   * @return a { @code Duration} based on this duration divided by the specified divisor, not null
   * @throws ArithmeticException if the divisor is zero or if numeric overflow occurs
   */
  def dividedBy(divisor: Long): Duration = {
    if (divisor == 0) {
      throw new ArithmeticException("Cannot divide by zero")
    }
    if (divisor == 1) {
       this
    }
     create(toSeconds.divide(BigDecimal.valueOf(divisor), RoundingMode.DOWN))
  }

  /**
   * Converts this duration to the total length in seconds and
   * fractional nanoseconds expressed as a {@code BigDecimal}.
   *
   * @return the total length of the duration in seconds, with a scale of 9, not null
   */
  private def toSeconds: BigDecimal = {
     BigDecimal.valueOf(seconds).add(BigDecimal.valueOf(nanos, 9))
  }

  /**
   * Returns a copy of this duration with the length negated.
   * <p>
   * This method swaps the sign of the total length of this duration.
   * For example, {@code PT1.3S} will be returned as {@code PT-1.3S}.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @return a { @code Duration} based on this duration with the amount negated, not null
   * @throws ArithmeticException if numeric overflow occurs
   */
  def negated: Duration = {
     multipliedBy(-1)
  }

  /**
   * Returns a copy of this duration with a positive length.
   * <p>
   * This method returns a positive duration by effectively removing the sign from any negative total length.
   * For example, {@code PT-1.3S} will be returned as {@code PT1.3S}.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @return a { @code Duration} based on this duration with an absolute length, not null
   * @throws ArithmeticException if numeric overflow occurs
   */
  def abs: Duration = {
     if (isNegative) negated else this
  }

  /**
   * Adds this duration to the specified temporal object.
   * <p>
   * This returns a temporal object of the same observable type as the input
   * with this duration added.
   * <p>
   * In most cases, it is clearer to reverse the calling pattern by using
   * {@link Temporal#plus(TemporalAmount)}.
   * {{{
   * // these two lines are equivalent, but the second approach is recommended
   * dateTime = thisDuration.addTo(dateTime);
   * dateTime = dateTime.plus(thisDuration);
   * }}}
   * <p>
   * The calculation will add the seconds, then nanos.
   * Only non-zero amounts will be added.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param temporal  the temporal object to adjust, not null
   * @return an object of the same type with the adjustment made, not null
   * @throws DateTimeException if unable to add
   * @throws ArithmeticException if numeric overflow occurs
   */
  def addTo(temporal: Temporal): Temporal = {
    if (seconds != 0) {
      temporal = temporal.plus(seconds, SECONDS)
    }
    if (nanos != 0) {
      temporal = temporal.plus(nanos, NANOS)
    }
     temporal
  }

  /**
   * Subtracts this duration from the specified temporal object.
   * <p>
   * This returns a temporal object of the same observable type as the input
   * with this duration subtracted.
   * <p>
   * In most cases, it is clearer to reverse the calling pattern by using
   * {@link Temporal#minus(TemporalAmount)}.
   * {{{
   * // these two lines are equivalent, but the second approach is recommended
   * dateTime = thisDuration.subtractFrom(dateTime);
   * dateTime = dateTime.minus(thisDuration);
   * }}}
   * <p>
   * The calculation will subtract the seconds, then nanos.
   * Only non-zero amounts will be added.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param temporal  the temporal object to adjust, not null
   * @return an object of the same type with the adjustment made, not null
   * @throws DateTimeException if unable to subtract
   * @throws ArithmeticException if numeric overflow occurs
   */
  def subtractFrom(temporal: Temporal): Temporal = {
    if (seconds != 0) {
      temporal = temporal.minus(seconds, SECONDS)
    }
    if (nanos != 0) {
      temporal = temporal.minus(nanos, NANOS)
    }
     temporal
  }

  /**
   * Gets the number of minutes in this duration.
   * <p>
   * This returns the total number of minutes in the duration by dividing the
   * number of seconds by 86400.
   * This is based on the standard definition of a day as 24 hours.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @return the number of minutes in the duration, may be negative
   */
  def toDays: Long = {
     seconds / SECONDS_PER_DAY
  }

  /**
   * Gets the number of minutes in this duration.
   * <p>
   * This returns the total number of minutes in the duration by dividing the
   * number of seconds by 3600.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @return the number of minutes in the duration, may be negative
   */
  def toHours: Long = {
     seconds / SECONDS_PER_HOUR
  }

  /**
   * Gets the number of minutes in this duration.
   * <p>
   * This returns the total number of minutes in the duration by dividing the
   * number of seconds by 60.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @return the number of minutes in the duration, may be negative
   */
  def toMinutes: Long = {
     seconds / SECONDS_PER_MINUTE
  }

  /**
   * Converts this duration to the total length in milliseconds.
   * <p>
   * If this duration is too large to fit in a {@code long} milliseconds, then an
   * exception is thrown.
   * <p>
   * If this duration has greater than millisecond precision, then the conversion
   * will drop any excess precision information as though the amount in nanoseconds
   * was subject to integer division by one million.
   *
   * @return the total length of the duration in milliseconds
   * @throws ArithmeticException if numeric overflow occurs
   */
  def toMillis: Long = {
    var millis: Long = Math.multiplyExact(seconds, 1000)
    millis = Math.addExact(millis, nanos / 1000 _000)
     millis
  }

  /**
   * Converts this duration to the total length in nanoseconds expressed as a {@code long}.
   * <p>
   * If this duration is too large to fit in a {@code long} nanoseconds, then an
   * exception is thrown.
   *
   * @return the total length of the duration in nanoseconds
   * @throws ArithmeticException if numeric overflow occurs
   */
  def toNanos: Long = {
    var totalNanos: Long = Math.multiplyExact(seconds, NANOS_PER_SECOND)
    totalNanos = Math.addExact(totalNanos, nanos)
     totalNanos
  }

  /**
   * Compares this duration to the specified {@code Duration}.
   * <p>
   * The comparison is based on the total length of the durations.
   * It is "consistent with equals", as defined by {@link Comparable}.
   *
   * @param otherDuration  the other duration to compare to, not null
   * @return the comparator value, negative if less, positive if greater
   */
  def compareTo(otherDuration: Duration): Int = {
    val cmp: Int = Long.compare(seconds, otherDuration.seconds)
    if (cmp != 0) {
       cmp
    }
     nanos - otherDuration.nanos
  }

  /**
   * Checks if this duration is equal to the specified {@code Duration}.
   * <p>
   * The comparison is based on the total length of the durations.
   *
   * @param otherDuration  the other duration, null returns false
   * @return true if the other duration is equal to this one
   */
  override def equals(otherDuration: AnyRef): Boolean = {
    if (this eq otherDuration) {
       true
    }
    if (otherDuration.isInstanceOf[Duration]) {
      val other: Duration = otherDuration.asInstanceOf[Duration]
       this.seconds == other.seconds && this.nanos == other.nanos
    }
     false
  }

  /**
   * A hash code for this duration.
   *
   * @return a suitable hash code
   */
  override def hashCode: Int = {
     ((seconds ^ (seconds >>> 32)).asInstanceOf[Int]) + (51 * nanos)
  }

  /**
   * A string representation of this duration using ISO-8601 seconds
   * based representation, such as {@code PT8H6M12.345S}.
   * <p>
   * The format of the returned string will be {@code PTnHnMnS}, where n is
   * the relevant hours, minutes or seconds part of the duration.
   * Any fractional seconds are placed after a decimal point i the seconds section.
   * If a section has a zero value, it is omitted.
   * The hours, minutes and seconds will all have the same sign.
   * <p>
   * Examples:
   * {{{
   * "20.345 seconds"                 -- "PT20.345S
   * "15 minutes" (15 * 60 seconds)   -- "PT15M"
   * "10 hours" (10 * 3600 seconds)   -- "PT10H"
   * "2 days" (2 * 86400 seconds)     -- "PT48H"
   * }}}
   * Note that multiples of 24 hours are not output as days to avoid confusion
   * with {@code Period}.
   *
   * @return an ISO-8601 representation of this duration, not null
   */
  override def toString: String = {
    if (this eq ZERO) {
       "PT0S"
    }
    val hours: Long = seconds / SECONDS_PER_HOUR
    val minutes: Int = ((seconds % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE).asInstanceOf[Int]
    val secs: Int = (seconds % SECONDS_PER_MINUTE).asInstanceOf[Int]
    val buf: StringBuilder = new StringBuilder(24)
    buf.append("PT")
    if (hours != 0) {
      buf.append(hours).append('H')
    }
    if (minutes != 0) {
      buf.append(minutes).append('M')
    }
    if (secs == 0 && nanos == 0 && buf.length > 2) {
       buf.toString
    }
    if (secs < 0 && nanos > 0) {
      if (secs == -1) {
        buf.append("-0")
      }
      else {
        buf.append(secs + 1)
      }
    }
    else {
      buf.append(secs)
    }
    if (nanos > 0) {
      val pos: Int = buf.length
      if (secs < 0) {
        buf.append(2 * NANOS_PER_SECOND - nanos)
      }
      else {
        buf.append(nanos + NANOS_PER_SECOND)
      }
      while (buf.charAt(buf.length - 1) == '0') {
        buf.setLength(buf.length - 1)
      }
      buf.setCharAt(pos, '.')
    }
    buf.append('S')
     buf.toString
  }

  /**
   * Writes the object using a
   * <a href="../../serialized-form.html#java.time.Ser">dedicated serialized form</a>.
   * {{{
   * out.writeByte(1);  // identifies this as a Duration
   * out.writeLong(seconds);
   * out.writeInt(nanos);
   * }}}
   *
   * @return the instance of { @code Ser}, not null
   */
  private def writeReplace: AnyRef = {
     new Ser(Ser.DURATION_TYPE, this)
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
   * The number of seconds in the duration.
   */
  private final val seconds: Long = 0L
  /**
   * The number of nanoseconds in the duration, expressed as a fraction of the
   * number of seconds. This is always positive, and never exceeds 999,999,999.
   */
  private final val nanos: Int = 0
}

/**
 * A date-based amount of time in the ISO-8601 calendar system,
 * such as '2 years, 3 months and 4 days'.
 * <p>
 * This class models a quantity or amount of time in terms of years, months and days.
 * See {@link Duration} for the time-based equivalent to this class.
 * <p>
 * Durations and periods differ in their treatment of daylight savings time
 * when added to {@link ZonedDateTime}. A {@code Duration} will add an exact
 * number of seconds, thus a duration of one day is always exactly 24 hours.
 * By contrast, a {@code Period} will add a conceptual day, trying to maintain
 * the local time.
 * <p>
 * For example, consider adding a period of one day and a duration of one day to
 * 18:00 on the evening before a daylight savings gap. The {@code Period} will add
 * the conceptual day and result in a {@code ZonedDateTime} at 18:00 the following day.
 * By contrast, the {@code Duration} will add exactly 24 hours, resulting in a
 * {@code ZonedDateTime} at 19:00 the following day (assuming a one hour DST gap).
 * <p>
 * The supported units of a period are {@link ChronoUnit#YEARS YEARS},
 * {@link ChronoUnit#MONTHS MONTHS} and {@link ChronoUnit#DAYS DAYS}.
 * All three fields are always present, but may be set to zero.
 * <p>
 * The ISO-8601 calendar system is the modern civil calendar system used today
 * in most of the world. It is equivalent to the proleptic Gregorian calendar
 * system, in which today's rules for leap years are applied for all time.
 * <p>
 * The period is modeled as a directed amount of time, meaning that individual parts of the
 * period may be negative.
 *
 * @implSpec
 * This class is immutable and thread-safe.
 *
 * @since 1.8
 */
object Period {
  /**
   * Obtains a {@code Period} representing a number of years.
   * <p>
   * The resulting period will have the specified years.
   * The months and days units will be zero.
   *
   * @param years  the number of years, positive or negative
   * @return the period of years, not null
   */
  def ofYears(years: Int): Period = {
     create(years, 0, 0)
  }

  /**
   * Obtains a {@code Period} representing a number of months.
   * <p>
   * The resulting period will have the specified months.
   * The years and days units will be zero.
   *
   * @param months  the number of months, positive or negative
   * @return the period of months, not null
   */
  def ofMonths(months: Int): Period = {
     create(0, months, 0)
  }

  /**
   * Obtains a {@code Period} representing a number of weeks.
   * <p>
   * The resulting period will be day-based, with the amount of days
   * equal to the number of weeks multiplied by 7.
   * The years and months units will be zero.
   *
   * @param weeks  the number of weeks, positive or negative
   * @return the period, with the input weeks converted to days, not null
   */
  def ofWeeks(weeks: Int): Period = {
     create(0, 0, Math.multiplyExact(weeks, 7))
  }

  /**
   * Obtains a {@code Period} representing a number of days.
   * <p>
   * The resulting period will have the specified days.
   * The years and months units will be zero.
   *
   * @param days  the number of days, positive or negative
   * @return the period of days, not null
   */
  def ofDays(days: Int): Period = {
     create(0, 0, days)
  }

  /**
   * Obtains a {@code Period} representing a number of years, months and days.
   * <p>
   * This creates an instance based on years, months and days.
   *
   * @param years  the amount of years, may be negative
   * @param months  the amount of months, may be negative
   * @param days  the amount of days, may be negative
   * @return the period of years, months and days, not null
   */
  def of(years: Int, months: Int, days: Int): Period = {
     create(years, months, days)
  }

  /**
   * Obtains an instance of {@code Period} from a temporal amount.
   * <p>
   * This obtains a period based on the specified amount.
   * A {@code TemporalAmount} represents an  amount of time, which may be
   * date-based or time-based, which this factory extracts to a {@code Period}.
   * <p>
   * The conversion loops around the set of units from the amount and uses
   * the {@link ChronoUnit#YEARS YEARS}, {@link ChronoUnit#MONTHS MONTHS}
   * and {@link ChronoUnit#DAYS DAYS} units to create a period.
   * If any other units are found then an exception is thrown.
   * <p>
   * If the amount is a {@code ChronoPeriod} then it must use the ISO chronology.
   *
   * @param amount  the temporal amount to convert, not null
   * @return the equivalent period, not null
   * @throws DateTimeException if unable to convert to a { @code Period}
   * @throws ArithmeticException if the amount of years, months or days exceeds an int
   */
  def from(amount: TemporalAmount): Period = {
    if (amount.isInstanceOf[Period]) {
       amount.asInstanceOf[Period]
    }
    if (amount.isInstanceOf[ChronoPeriod]) {
      if ((IsoChronology.INSTANCE == (amount.asInstanceOf[ChronoPeriod]).getChronology) == false) {
        throw new DateTimeException("Period requires ISO chronology: " + amount)
      }
    }
    object
    var years: Int = 0
    var months: Int = 0
    var days: Int = 0
    for (unit <- amount.getUnits) {
      val unitAmount: Long = amount.get(unit)
      if (unit eq ChronoUnit.YEARS) {
        years = Math.toIntExact(unitAmount)
      }
      else if (unit eq ChronoUnit.MONTHS) {
        months = Math.toIntExact(unitAmount)
      }
      else if (unit eq ChronoUnit.DAYS) {
        days = Math.toIntExact(unitAmount)
      }
      else {
        throw new DateTimeException("Unit must be Years, Months or Days, but was " + unit)
      }
    }
     create(years, months, days)
  }

  /**
   * Obtains a {@code Period} from a text string such as {@code PnYnMnD}.
   * <p>
   * This will parse the string produced by {@code toString()} which is
   * based on the ISO-8601 period formats {@code PnYnMnD} and {@code PnW}.
   * <p>
   * The string starts with an optional sign, denoted by the ASCII negative
   * or positive symbol. If negative, the whole period is negated.
   * The ASCII letter "P" is next in upper or lower case.
   * There are then four sections, each consisting of a number and a suffix.
   * At least one of the four sections must be present.
   * The sections have suffixes in ASCII of "Y", "M", "W" and "D" for
   * years, months, weeks and days, accepted in upper or lower case.
   * The suffixes must occur in order.
   * The number part of each section must consist of ASCII digits.
   * The number may be prefixed by the ASCII negative or positive symbol.
   * The number must parse to an {@code int}.
   * <p>
   * The leading plus/minus sign, and negative values for other units are
   * not part of the ISO-8601 standard. In addition, ISO-8601 does not
   * permit mixing between the {@code PnYnMnD} and {@code PnW} formats.
   * Any week-based input is multiplied by 7 and treated as a number of days.
   * <p>
   * For example, the following are valid inputs:
   * {{{
   * "P2Y"             -- Period.ofYears(2)
   * "P3M"             -- Period.ofMonths(3)
   * "P4W"             -- Period.ofWeeks(4)
   * "P5D"             -- Period.ofDays(5)
   * "P1Y2M3D"         -- Period.of(1, 2, 3)
   * "P1Y2M3W4D"       -- Period.of(1, 2, 25)
   * "P-1Y2M"          -- Period.of(-1, 2, 0)
   * "-P1Y2M"          -- Period.of(-1, -2, 0)
   * }}}
   *
   * @param text  the text to parse, not null
   * @return the parsed period, not null
   * @throws DateTimeParseException if the text cannot be parsed to a period
   */
  def parse(text: CharSequence): Period = {
    object
    val matcher: Matcher = PATTERN.matcher(text)
    if (matcher.matches) {
      val negate: Int = (if (("-" == matcher.group(1))) -1 else 1)
      val yearMatch: String = matcher.group(2)
      val monthMatch: String = matcher.group(3)
      val weekMatch: String = matcher.group(4)
      val dayMatch: String = matcher.group(5)
      if (yearMatch != null || monthMatch != null || dayMatch != null || weekMatch != null) {
        try {
          val years: Int = parseNumber(text, yearMatch, negate)
          val months: Int = parseNumber(text, monthMatch, negate)
          val weeks: Int = parseNumber(text, weekMatch, negate)
          var days: Int = parseNumber(text, dayMatch, negate)
          days = Math.addExact(days, Math.multiplyExact(weeks, 7))
           create(years, months, days)
        }
        catch {
          case ex: NumberFormatException => {
            throw new DateTimeParseException("Text cannot be parsed to a Period", text, 0, ex)
          }
        }
      }
    }
    throw new DateTimeParseException("Text cannot be parsed to a Period", text, 0)
  }

  private def parseNumber(text: CharSequence, str: String, negate: Int): Int = {
    if (str == null) {
       0
    }
    val `val`: Int = Integer.parseInt(str)
    try {
       Math.multiplyExact(`val`, negate)
    }
    catch {
      case ex: ArithmeticException => {
        throw new DateTimeParseException("Text cannot be parsed to a Period", text, 0, ex)
      }
    }
  }

  /**
   * Obtains a {@code Period} consisting of the number of years, months,
   * and days between two dates.
   * <p>
   * The start date is included, but the end date is not.
   * The period is calculated by removing complete months, then calculating
   * the remaining number of days, adjusting to ensure that both have the same sign.
   * The number of months is then split into years and months based on a 12 month year.
   * A month is considered if the end day-of-month is greater than or equal to the start day-of-month.
   * For example, from {@code 2010-01-15} to {@code 2011-03-18} is one year, two months and three days.
   * <p>
   * The result of this method can be a negative period if the end is before the start.
   * The negative sign will be the same in each of year, month and day.
   *
   * @param startDateInclusive  the start date, inclusive, not null
   * @param endDateExclusive  the end date, exclusive, not null
   * @return the period between this date and the end date, not null
   * @see ChronoLocalDate#until(ChronoLocalDate)
   */
  def between(startDateInclusive: Date, endDateExclusive: Date): Period = {
     startDateInclusive.until(endDateExclusive)
  }

  /**
   * Creates an instance.
   *
   * @param years  the amount
   * @param months  the amount
   * @param days  the amount
   */
  private def create(years: Int, months: Int, days: Int): Period = {
    if ((years | months | days) == 0) {
       ZERO
    }
     new Period(years, months, days)
  }

  private[time] def readExternal(in: DataInput): Period = {
    val years: Int = in.readInt
    val months: Int = in.readInt
    val days: Int = in.readInt
     Period.of(years, months, days)
  }

  /**
   * A constant for a period of zero.
   */
  final val ZERO: Period = new Period(0, 0, 0)

  /**
   * The pattern for parsing.
   */
  private final val PATTERN: Pattern = Pattern.compile("([-+]?)P(?:([-+]?[0-9]+)Y)?(?:([-+]?[0-9]+)M)?(?:([-+]?[0-9]+)W)?(?:([-+]?[0-9]+)D)?", Pattern.CASE_INSENSITIVE)
  /**
   * The set of supported units.
   */
  private final val SUPPORTED_UNITS: List[TemporalUnit] = Collections.unmodifiableList(Arrays.asList[TemporalUnit](YEARS, MONTHS, DAYS))
}

final class Period extends ChronoPeriod  {
  /**
   * Constructor.
   *
   * @param years  the amount
   * @param months  the amount
   * @param days  the amount
   */
  private def this(years: Int, months: Int, days: Int) {

    this.years = years
    this.months = months
    this.days = days
  }

  /**
   * Gets the value of the requested unit.
   * <p>
   * This returns a value for each of the three supported units,
   * {@link ChronoUnit#YEARS YEARS}, {@link ChronoUnit#MONTHS MONTHS} and
   * {@link ChronoUnit#DAYS DAYS}.
   * All other units throw an exception.
   *
   * @param unit the { @code TemporalUnit} for which to  the value
   * @return the long value of the unit
   * @throws DateTimeException if the unit is not supported
   * @throws UnsupportedTemporalTypeException if the unit is not supported
   */
  def get(unit: TemporalUnit): Long = {
    if (unit eq ChronoUnit.YEARS) {
       getYears
    }
    else if (unit eq ChronoUnit.MONTHS) {
       getMonths
    }
    else if (unit eq ChronoUnit.DAYS) {
       getDays
    }
    else {
      throw new UnsupportedTemporalTypeException("Unsupported unit: " + unit)
    }
  }

  /**
   * Gets the set of units supported by this period.
   * <p>
   * The supported units are {@link ChronoUnit#YEARS YEARS},
   * {@link ChronoUnit#MONTHS MONTHS} and {@link ChronoUnit#DAYS DAYS}.
   * They are returned in the order years, months, days.
   * <p>
   * This set can be used in conjunction with {@link #get(TemporalUnit)}
   * to access the entire state of the period.
   *
   * @return a list containing the years, months and days units, not null
   */
  def getUnits: List[TemporalUnit] = {
     SUPPORTED_UNITS
  }

  /**
   * Gets the chronology of this period, which is the ISO calendar system.
   * <p>
   * The {@code Chronology} represents the calendar system in use.
   * The ISO-8601 calendar system is the modern civil calendar system used today
   * in most of the world. It is equivalent to the proleptic Gregorian calendar
   * system, in which today's rules for leap years are applied for all time.
   *
   * @return the ISO chronology, not null
   */
  def getChronology: IsoChronology = {
     IsoChronology.INSTANCE
  }

  /**
   * Checks if all three units of this period are zero.
   * <p>
   * A zero period has the value zero for the years, months and days units.
   *
   * @return true if this period is zero-length
   */
  override def isZero: Boolean = {
     (this eq ZERO)
  }

  /**
   * Checks if any of the three units of this period are negative.
   * <p>
   * This checks whether the years, months or days units are less than zero.
   *
   * @return true if any unit of this period is negative
   */
  override def isNegative: Boolean = {
     years < 0 || months < 0 || days < 0
  }

  /**
   * Gets the amount of years of this period.
   * <p>
   * This returns the years unit.
   * <p>
   * The months unit is not automatically normalized with the years unit.
   * This means that a period of "15 months" is different to a period
   * of "1 year and 3 months".
   *
   * @return the amount of years of this period, may be negative
   */
  def getYears: Int = {
     years
  }

  /**
   * Gets the amount of months of this period.
   * <p>
   * This returns the months unit.
   * <p>
   * The months unit is not automatically normalized with the years unit.
   * This means that a period of "15 months" is different to a period
   * of "1 year and 3 months".
   *
   * @return the amount of months of this period, may be negative
   */
  def getMonths: Int = {
     months
  }

  /**
   * Gets the amount of days of this period.
   * <p>
   * This returns the days unit.
   *
   * @return the amount of days of this period, may be negative
   */
  def getDays: Int = {
     days
  }

  /**
   * Returns a copy of this period with the specified amount of years.
   * <p>
   * This sets the amount of the years unit in a copy of this period.
   * The months and days units are unaffected.
   * <p>
   * The months unit is not automatically normalized with the years unit.
   * This means that a period of "15 months" is different to a period
   * of "1 year and 3 months".
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param years  the years to represent, may be negative
   * @return a { @code Period} based on this period with the requested years, not null
   */
  def withYears(years: Int): Period = {
    if (years == this.years) {
       this
    }
     create(years, months, days)
  }

  /**
   * Returns a copy of this period with the specified amount of months.
   * <p>
   * This sets the amount of the months unit in a copy of this period.
   * The years and days units are unaffected.
   * <p>
   * The months unit is not automatically normalized with the years unit.
   * This means that a period of "15 months" is different to a period
   * of "1 year and 3 months".
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param months  the months to represent, may be negative
   * @return a { @code Period} based on this period with the requested months, not null
   */
  def withMonths(months: Int): Period = {
    if (months == this.months) {
       this
    }
     create(years, months, days)
  }

  /**
   * Returns a copy of this period with the specified amount of days.
   * <p>
   * This sets the amount of the days unit in a copy of this period.
   * The years and months units are unaffected.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param days  the days to represent, may be negative
   * @return a { @code Period} based on this period with the requested days, not null
   */
  def withDays(days: Int): Period = {
    if (days == this.days) {
       this
    }
     create(years, months, days)
  }

  /**
   * Returns a copy of this period with the specified period added.
   * <p>
   * This operates separately on the years, months and days.
   * No normalization is performed.
   * <p>
   * For example, "1 year, 6 months and 3 days" plus "2 years, 2 months and 2 days"
   * returns "3 years, 8 months and 5 days".
   * <p>
   * The specified amount is typically an instance of {@code Period}.
   * Other types are interpreted using {@link Period#from(TemporalAmount)}.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param amountToAdd  the period to add, not null
   * @return a { @code Period} based on this period with the requested period added, not null
   * @throws DateTimeException if the specified amount has a non-ISO chronology or
   *                           contains an invalid unit
   * @throws ArithmeticException if numeric overflow occurs
   */
  def plus(amountToAdd: TemporalAmount): Period = {
    val isoAmount: Period = Period.from(amountToAdd)
     create(Math.addExact(years, isoAmount.years), Math.addExact(months, isoAmount.months), Math.addExact(days, isoAmount.days))
  }

  /**
   * Returns a copy of this period with the specified years added.
   * <p>
   * This adds the amount to the years unit in a copy of this period.
   * The months and days units are unaffected.
   * For example, "1 year, 6 months and 3 days" plus 2 years returns "3 years, 6 months and 3 days".
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param yearsToAdd  the years to add, positive or negative
   * @return a { @code Period} based on this period with the specified years added, not null
   * @throws ArithmeticException if numeric overflow occurs
   */
  def plusYears(yearsToAdd: Long): Period = {
    if (yearsToAdd == 0) {
       this
    }
     create(Math.toIntExact(Math.addExact(years, yearsToAdd)), months, days)
  }

  /**
   * Returns a copy of this period with the specified months added.
   * <p>
   * This adds the amount to the months unit in a copy of this period.
   * The years and days units are unaffected.
   * For example, "1 year, 6 months and 3 days" plus 2 months returns "1 year, 8 months and 3 days".
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param monthsToAdd  the months to add, positive or negative
   * @return a { @code Period} based on this period with the specified months added, not null
   * @throws ArithmeticException if numeric overflow occurs
   */
  def plusMonths(monthsToAdd: Long): Period = {
    if (monthsToAdd == 0) {
       this
    }
     create(years, Math.toIntExact(Math.addExact(months, monthsToAdd)), days)
  }

  /**
   * Returns a copy of this period with the specified days added.
   * <p>
   * This adds the amount to the days unit in a copy of this period.
   * The years and months units are unaffected.
   * For example, "1 year, 6 months and 3 days" plus 2 days returns "1 year, 6 months and 5 days".
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param daysToAdd  the days to add, positive or negative
   * @return a { @code Period} based on this period with the specified days added, not null
   * @throws ArithmeticException if numeric overflow occurs
   */
  def plusDays(daysToAdd: Long): Period = {
    if (daysToAdd == 0) {
       this
    }
     create(years, months, Math.toIntExact(Math.addExact(days, daysToAdd)))
  }

  /**
   * Returns a copy of this period with the specified period subtracted.
   * <p>
   * This operates separately on the years, months and days.
   * No normalization is performed.
   * <p>
   * For example, "1 year, 6 months and 3 days" minus "2 years, 2 months and 2 days"
   * returns "-1 years, 4 months and 1 day".
   * <p>
   * The specified amount is typically an instance of {@code Period}.
   * Other types are interpreted using {@link Period#from(TemporalAmount)}.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param amountToSubtract  the period to subtract, not null
   * @return a { @code Period} based on this period with the requested period subtracted, not null
   * @throws DateTimeException if the specified amount has a non-ISO chronology or
   *                           contains an invalid unit
   * @throws ArithmeticException if numeric overflow occurs
   */
  def minus(amountToSubtract: TemporalAmount): Period = {
    val isoAmount: Period = Period.from(amountToSubtract)
     create(Math.subtractExact(years, isoAmount.years), Math.subtractExact(months, isoAmount.months), Math.subtractExact(days, isoAmount.days))
  }

  /**
   * Returns a copy of this period with the specified years subtracted.
   * <p>
   * This subtracts the amount from the years unit in a copy of this period.
   * The months and days units are unaffected.
   * For example, "1 year, 6 months and 3 days" minus 2 years returns "-1 years, 6 months and 3 days".
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param yearsToSubtract  the years to subtract, positive or negative
   * @return a { @code Period} based on this period with the specified years subtracted, not null
   * @throws ArithmeticException if numeric overflow occurs
   */
  def minusYears(yearsToSubtract: Long): Period = {
     (if (yearsToSubtract == Long.MIN_VALUE) plusYears(Long.MAX_VALUE).plusYears(1) else plusYears(-yearsToSubtract))
  }

  /**
   * Returns a copy of this period with the specified months subtracted.
   * <p>
   * This subtracts the amount from the months unit in a copy of this period.
   * The years and days units are unaffected.
   * For example, "1 year, 6 months and 3 days" minus 2 months returns "1 year, 4 months and 3 days".
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param monthsToSubtract  the years to subtract, positive or negative
   * @return a { @code Period} based on this period with the specified months subtracted, not null
   * @throws ArithmeticException if numeric overflow occurs
   */
  def minusMonths(monthsToSubtract: Long): Period = {
     (if (monthsToSubtract == Long.MIN_VALUE) plusMonths(Long.MAX_VALUE).plusMonths(1) else plusMonths(-monthsToSubtract))
  }

  /**
   * Returns a copy of this period with the specified days subtracted.
   * <p>
   * This subtracts the amount from the days unit in a copy of this period.
   * The years and months units are unaffected.
   * For example, "1 year, 6 months and 3 days" minus 2 days returns "1 year, 6 months and 1 day".
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param daysToSubtract  the months to subtract, positive or negative
   * @return a { @code Period} based on this period with the specified days subtracted, not null
   * @throws ArithmeticException if numeric overflow occurs
   */
  def minusDays(daysToSubtract: Long): Period = {
     (if (daysToSubtract == Long.MIN_VALUE) plusDays(Long.MAX_VALUE).plusDays(1) else plusDays(-daysToSubtract))
  }

  /**
   * Returns a new instance with each element in this period multiplied
   * by the specified scalar.
   * <p>
   * This returns a period with each of the years, months and days units
   * individually multiplied.
   * For example, a period of "2 years, -3 months and 4 days" multiplied by
   * 3 will  "6 years, -9 months and 12 days".
   * No normalization is performed.
   *
   * @param scalar  the scalar to multiply by, not null
   * @return a { @code Period} based on this period with the amounts multiplied by the scalar, not null
   * @throws ArithmeticException if numeric overflow occurs
   */
  def multipliedBy(scalar: Int): Period = {
    if (this eq ZERO || scalar == 1) {
       this
    }
     create(Math.multiplyExact(years, scalar), Math.multiplyExact(months, scalar), Math.multiplyExact(days, scalar))
  }

  /**
   * Returns a new instance with each amount in this period negated.
   * <p>
   * This returns a period with each of the years, months and days units
   * individually negated.
   * For example, a period of "2 years, -3 months and 4 days" will be
   * negated to "-2 years, 3 months and -4 days".
   * No normalization is performed.
   *
   * @return a { @code Period} based on this period with the amounts negated, not null
   * @throws ArithmeticException if numeric overflow occurs, which only happens if
   *                             one of the units has the value { @code Long.MIN_VALUE}
   */
  override def negated: Period = {
     multipliedBy(-1)
  }

  /**
   * Returns a copy of this period with the years and months normalized.
   * <p>
   * This normalizes the years and months units, leaving the days unit unchanged.
   * The months unit is adjusted to have an absolute value less than 11,
   * with the years unit being adjusted to compensate. For example, a period of
   * "1 Year and 15 months" will be normalized to "2 years and 3 months".
   * <p>
   * The sign of the years and months units will be the same after normalization.
   * For example, a period of "1 year and -25 months" will be normalized to
   * "-1 year and -1 month".
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @return a { @code Period} based on this period with excess months normalized to years, not null
   * @throws ArithmeticException if numeric overflow occurs
   */
  def normalized: Period = {
    val totalMonths: Long = toTotalMonths
    val splitYears: Long = totalMonths / 12
    val splitMonths: Int = (totalMonths % 12).asInstanceOf[Int]
    if (splitYears == years && splitMonths == months) {
       this
    }
     create(Math.toIntExact(splitYears), splitMonths, days)
  }

  /**
   * Gets the total number of months in this period.
   * <p>
   * This returns the total number of months in the period by multiplying the
   * number of years by 12 and adding the number of months.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @return the total number of months in the period, may be negative
   */
  def toTotalMonths: Long = {
     years * 12L + months
  }

  /**
   * Adds this period to the specified temporal object.
   * <p>
   * This returns a temporal object of the same observable type as the input
   * with this period added.
   * If the temporal has a chronology, it must be the ISO chronology.
   * <p>
   * In most cases, it is clearer to reverse the calling pattern by using
   * {@link Temporal#plus(TemporalAmount)}.
   * {{{
   * // these two lines are equivalent, but the second approach is recommended
   * dateTime = thisPeriod.addTo(dateTime);
   * dateTime = dateTime.plus(thisPeriod);
   * }}}
   * <p>
   * The calculation operates as follows.
   * First, the chronology of the temporal is checked to ensure it is ISO chronology or null.
   * Second, if the months are zero, the years are added if non-zero, otherwise
   * the combination of years and months is added if non-zero.
   * Finally, any days are added.
   * <p>
   * This approach ensures that a partial period can be added to a partial date.
   * For example, a period of years and/or months can be added to a {@code YearMonth},
   * but a period including days cannot.
   * The approach also adds years and months together when necessary, which ensures
   * correct behaviour at the end of the month.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param temporal  the temporal object to adjust, not null
   * @return an object of the same type with the adjustment made, not null
   * @throws DateTimeException if unable to add
   * @throws ArithmeticException if numeric overflow occurs
   */
  def addTo(temporal: Temporal): Temporal = {
    validateChrono(temporal)
    if (months == 0) {
      if (years != 0) {
        temporal = temporal.plus(years, YEARS)
      }
    }
    else {
      val totalMonths: Long = toTotalMonths
      if (totalMonths != 0) {
        temporal = temporal.plus(totalMonths, MONTHS)
      }
    }
    if (days != 0) {
      temporal = temporal.plus(days, DAYS)
    }
     temporal
  }

  /**
   * Subtracts this period from the specified temporal object.
   * <p>
   * This returns a temporal object of the same observable type as the input
   * with this period subtracted.
   * If the temporal has a chronology, it must be the ISO chronology.
   * <p>
   * In most cases, it is clearer to reverse the calling pattern by using
   * {@link Temporal#minus(TemporalAmount)}.
   * {{{
   * // these two lines are equivalent, but the second approach is recommended
   * dateTime = thisPeriod.subtractFrom(dateTime);
   * dateTime = dateTime.minus(thisPeriod);
   * }}}
   * <p>
   * The calculation operates as follows.
   * First, the chronology of the temporal is checked to ensure it is ISO chronology or null.
   * Second, if the months are zero, the years are subtracted if non-zero, otherwise
   * the combination of years and months is subtracted if non-zero.
   * Finally, any days are subtracted.
   * <p>
   * This approach ensures that a partial period can be subtracted from a partial date.
   * For example, a period of years and/or months can be subtracted from a {@code YearMonth},
   * but a period including days cannot.
   * The approach also subtracts years and months together when necessary, which ensures
   * correct behaviour at the end of the month.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param temporal  the temporal object to adjust, not null
   * @return an object of the same type with the adjustment made, not null
   * @throws DateTimeException if unable to subtract
   * @throws ArithmeticException if numeric overflow occurs
   */
  def subtractFrom(temporal: Temporal): Temporal = {
    validateChrono(temporal)
    if (months == 0) {
      if (years != 0) {
        temporal = temporal.minus(years, YEARS)
      }
    }
    else {
      val totalMonths: Long = toTotalMonths
      if (totalMonths != 0) {
        temporal = temporal.minus(totalMonths, MONTHS)
      }
    }
    if (days != 0) {
      temporal = temporal.minus(days, DAYS)
    }
     temporal
  }

  /**
   * Validates that the temporal has the correct chronology.
   */
  private def validateChrono(temporal: TemporalAccessor) {
    object
    val temporalChrono: Chronology = temporal.query(TemporalQuery.chronology)
    if (temporalChrono != null && (IsoChronology.INSTANCE == temporalChrono) == false) {
      throw new DateTimeException("Chronology mismatch, expected: ISO, actual: " + temporalChrono.getId)
    }
  }

  /**
   * Checks if this period is equal to another period.
   * <p>
   * The comparison is based on the type {@code Period} and each of the three amounts.
   * To be equal, the years, months and days units must be individually equal.
   * Note that this means that a period of "15 Months" is not equal to a period
   * of "1 Year and 3 Months".
   *
   * @param obj  the object to check, null returns false
   * @return true if this is equal to the other period
   */
  override def equals(obj: AnyRef): Boolean = {
    if (this eq obj) {
       true
    }
    if (obj.isInstanceOf[Period]) {
      val other: Period = obj.asInstanceOf[Period]
       years == other.years && months == other.months && days == other.days
    }
     false
  }

  /**
   * A hash code for this period.
   *
   * @return a suitable hash code
   */
  override def hashCode: Int = {
     years + Integer.rotateLeft(months, 8) + Integer.rotateLeft(days, 16)
  }

  /**
   * Outputs this period as a {@code String}, such as {@code P6Y3M1D}.
   * <p>
   * The output will be in the ISO-8601 period format.
   * A zero period will be represented as zero days, 'P0D'.
   *
   * @return a string representation of this period, not null
   */
  override def toString: String = {
    if (this eq ZERO) {
       "P0D"
    }
    else {
      val buf: StringBuilder = new StringBuilder
      buf.append('P')
      if (years != 0) {
        buf.append(years).append('Y')
      }
      if (months != 0) {
        buf.append(months).append('M')
      }
      if (days != 0) {
        buf.append(days).append('D')
      }
       buf.toString
    }
  }

  /**
   * Writes the object using a
   * <a href="../../serialized-form.html#java.time.Ser">dedicated serialized form</a>.
   * {{{
   * out.writeByte(14);  // identifies this as a Period
   * out.writeInt(years);
   * out.writeInt(months);
   * out.writeInt(seconds);
   * }}}
   *
   * @return the instance of { @code Ser}, not null
   */
  private def writeReplace: AnyRef = {
     new Ser(Ser.PERIOD_TYPE, this)
  }

  /**
   * Defend against malicious streams.
   * @return never
   * @throws java.io.InvalidObjectException always
   */
  private def readResolve: AnyRef = {
    throw new InvalidObjectException("Deserialization via serialization delegate")
  }

  private[time] def writeExternal(out: DataOutput) {
    out.writeInt(years)
    out.writeInt(months)
    out.writeInt(days)
  }

  /**
   * The number of years.
   */
  private final val years: Int = 0
  /**
   * The number of months.
   */
  private final val months: Int = 0
  /**
   * The number of days.
   */
  private final val days: Int = 0
}


