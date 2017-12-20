package chronos.calendar

/**
 * A date-based amount of time, such as '3 years, 4 months and 5 days' in an
 * arbitrary chronology, intended for advanced globalization use cases.
 * <p>
 * This interface models a date-based amount of time in a calendar system.
 * While most calendar systems use years, months and days, some do not.
 * Therefore, this interface operates solely in terms of a set of supported
 * units that are defined by the {@code Chronology}.
 * The set of supported units is fixed for a given chronology.
 * The amount of a supported unit may be set to zero.
 * <p>
 * The period is modeled as a directed amount of time, meaning that individual
 * parts of the period may be negative.
 *
 * @implSpec
 * This interface must be implemented with care to ensure other classes operate correctly.
 * All implementations that can be instantiated must be final, immutable and thread-safe.
 * Subclasses should be Serializable wherever possible.
 *
 * @since 1.8
 */
object ChronoPeriod {
  /**
   * Obtains a {@code ChronoPeriod} consisting of amount of time between two dates.
   * <p>
   * The start date is included, but the end date is not.
   * The period is calculated using {@link ChronoLocalDate#until(ChronoLocalDate)}.
   * As such, the calculation is chronology specific.
   * <p>
   * The chronology of the first date is used.
   * The chronology of the second date is ignored, with the date being converted
   * to the target chronology system before the calculation starts.
   * <p>
   * The result of this method can be a negative period if the end is before the start.
   * In most cases, the positive/negative sign will be the same in each of the supported fields.
   *
   * @param startDateInclusive  the start date, inclusive, specifying the chronology of the calculation, not null
   * @param endDateExclusive  the end date, exclusive, in any chronology, not null
   * @return the period between this date and the end date, not null
   * @see ChronoLocalDate#until(ChronoLocalDate)
   */
  def between(startDateInclusive: ChronoLocalDate, endDateExclusive: ChronoLocalDate): ChronoPeriod = {


     startDateInclusive.until(endDateExclusive)
  }
}

trait ChronoPeriod extends TemporalAmount {
  /**
   * Gets the value of the requested unit.
   * <p>
   * The supported units are chronology specific.
   * They will typically be {@link ChronoUnit#YEARS YEARS},
   * {@link ChronoUnit#MONTHS MONTHS} and {@link ChronoUnit#DAYS DAYS}.
   * Requesting an unsupported unit will throw an exception.
   *
   * @param unit the { @code TemporalUnit} for which to return the value
   * @return the long value of the unit
   * @throws DateTimeException if the unit is not supported
   * @throws UnsupportedTemporalTypeException if the unit is not supported
   */
  def get(unit: TemporalUnit): Long

  /**
   * Gets the set of units supported by this period.
   * <p>
   * The supported units are chronology specific.
   * They will typically be {@link ChronoUnit#YEARS YEARS},
   * {@link ChronoUnit#MONTHS MONTHS} and {@link ChronoUnit#DAYS DAYS}.
   * They are returned in order from largest to smallest.
   * <p>
   * This set can be used in conjunction with {@link #get(TemporalUnit)}
   * to access the entire state of the period.
   *
   * @return a list containing the supported units, not null
   */
  def getUnits: List[TemporalUnit]

  /**
   * Gets the chronology that defines the meaning of the supported units.
   * <p>
   * The period is defined by the chronology.
   * It controls the supported units and restricts addition/subtraction
   * to {@code ChronoLocalDate} instances of the same chronology.
   *
   * @return the chronology defining the period, not null
   */
  def getChronology: Chronology

  /**
   * Checks if all the supported units of this period are zero.
   *
   * @return true if this period is zero-length
   */
  def isZero: Boolean = {
    import scala.collection.JavaConversions._
    for (unit <- getUnits) {
      if (get(unit) != 0) {
         false
      }
    }
     true
  }

  /**
   * Checks if any of the supported units of this period are negative.
   *
   * @return true if any unit of this period is negative
   */
  def isNegative: Boolean = {
    import scala.collection.JavaConversions._
    for (unit <- getUnits) {
      if (get(unit) < 0) {
         true
      }
    }
     false
  }

  /**
   * Returns a copy of this period with the specified period added.
   * <p>
   * If the specified amount is a {@code ChronoPeriod} then it must have
   * the same chronology as this period. Implementations may choose to
   * accept or reject other {@code TemporalAmount} implementations.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param amountToAdd  the period to add, not null
   * @return a { @code ChronoPeriod} based on this period with the requested period added, not null
   * @throws ArithmeticException if numeric overflow occurs
   */
  def plus(amountToAdd: TemporalAmount): ChronoPeriod

  /**
   * Returns a copy of this period with the specified period subtracted.
   * <p>
   * If the specified amount is a {@code ChronoPeriod} then it must have
   * the same chronology as this period. Implementations may choose to
   * accept or reject other {@code TemporalAmount} implementations.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param amountToSubtract  the period to subtract, not null
   * @return a { @code ChronoPeriod} based on this period with the requested period subtracted, not null
   * @throws ArithmeticException if numeric overflow occurs
   */
  def minus(amountToSubtract: TemporalAmount): ChronoPeriod

  /**
   * Returns a new instance with each amount in this period in this period
   * multiplied by the specified scalar.
   * <p>
   * This returns a period with each supported unit individually multiplied.
   * For example, a period of "2 years, -3 months and 4 days" multiplied by
   * 3 will return "6 years, -9 months and 12 days".
   * No normalization is performed.
   *
   * @param scalar  the scalar to multiply by, not null
   * @return a { @code ChronoPeriod} based on this period with the amounts multiplied
   *                   by the scalar, not null
   * @throws ArithmeticException if numeric overflow occurs
   */
  def multipliedBy(scalar: Int): ChronoPeriod

  /**
   * Returns a new instance with each amount in this period negated.
   * <p>
   * This returns a period with each supported unit individually negated.
   * For example, a period of "2 years, -3 months and 4 days" will be
   * negated to "-2 years, 3 months and -4 days".
   * No normalization is performed.
   *
   * @return a { @code ChronoPeriod} based on this period with the amounts negated, not null
   * @throws ArithmeticException if numeric overflow occurs, which only happens if
   *                             one of the units has the value { @code Long.MIN_VALUE}
   */
  def negated: ChronoPeriod = {
     multipliedBy(-1)
  }

  /**
   * Returns a copy of this period with the amounts of each unit normalized.
   * <p>
   * The process of normalization is specific to each calendar system.
   * For example, in the ISO calendar system, the years and months are
   * normalized but the days are not, such that "15 months" would be
   * normalized to "1 year and 3 months".
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @return a { @code ChronoPeriod} based on this period with the amounts of each
   *                   unit normalized, not null
   * @throws ArithmeticException if numeric overflow occurs
   */
  def normalized: ChronoPeriod

  /**
   * Adds this period to the specified temporal object.
   * <p>
   * This returns a temporal object of the same observable type as the input
   * with this period added.
   * <p>
   * In most cases, it is clearer to reverse the calling pattern by using
   * {@link Temporal#plus(TemporalAmount)}.
   * {{{
   * // these two lines are equivalent, but the second approach is recommended
   * dateTime = thisPeriod.addTo(dateTime);
   * dateTime = dateTime.plus(thisPeriod);
   * }}}
   * <p>
   * The specified temporal must have the same chronology as this period.
   * This returns a temporal with the non-zero supported units added.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param temporal  the temporal object to adjust, not null
   * @return an object of the same type with the adjustment made, not null
   * @throws DateTimeException if unable to add
   * @throws ArithmeticException if numeric overflow occurs
   */
  def addTo(temporal: Temporal): Temporal

  /**
   * Subtracts this period from the specified temporal object.
   * <p>
   * This returns a temporal object of the same observable type as the input
   * with this period subtracted.
   * <p>
   * In most cases, it is clearer to reverse the calling pattern by using
   * {@link Temporal#minus(TemporalAmount)}.
   * {{{
   * // these two lines are equivalent, but the second approach is recommended
   * dateTime = thisPeriod.subtractFrom(dateTime);
   * dateTime = dateTime.minus(thisPeriod);
   * }}}
   * <p>
   * The specified temporal must have the same chronology as this period.
   * This returns a temporal with the non-zero supported units subtracted.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param temporal  the temporal object to adjust, not null
   * @return an object of the same type with the adjustment made, not null
   * @throws DateTimeException if unable to subtract
   * @throws ArithmeticException if numeric overflow occurs
   */
  def subtractFrom(temporal: Temporal): Temporal

  /**
   * Checks if this period is equal to another period, including the chronology.
   * <p>
   * Compares this period with another ensuring that the type, each amount and
   * the chronology are the same.
   * Note that this means that a period of "15 Months" is not equal to a period
   * of "1 Year and 3 Months".
   *
   * @param obj  the object to check, null returns false
   * @return true if this is equal to the other period
   */
  override def equals(obj: AnyRef): Boolean

  /**
   * A hash code for this period.
   *
   * @return a suitable hash code
   */
  override def hashCode: Int

  /**
   * Outputs this period as a {@code String}.
   * <p>
   * The output will include the period amounts and chronology.
   *
   * @return a string representation of this period, not null
   */
  override def toString: String
}

/**
 * A period expressed in terms of a standard year-month-day calendar system.
 * <p>
 * This class is used by applications seeking to handle dates in non-ISO calendar systems.
 * For example, the Japanese, Minguo, Thai Buddhist and others.
 *
 * @implSpec
 * This class is immutable nad thread-safe.
 *
 * @since 1.8
 */
object ChronoPeriodImpl {
  private[calendar] def readExternal(in: DataInput): ChronoPeriodImpl = {
    val chrono: Chronology = Chronology.of(in.readUTF)
    val years: Int = in.readInt
    val months: Int = in.readInt
    val days: Int = in.readInt
     new ChronoPeriodImpl(chrono, years, months, days)
  }

  /**
   * Serialization version.
   */
  private final val serialVersionUID: Long = 57387258289L
  /**
   * The set of supported units.
   */
  private final val SUPPORTED_UNITS: List[TemporalUnit] = Collections.unmodifiableList(Arrays.asList[TemporalUnit](YEARS, MONTHS, DAYS))
}

final class ChronoPeriodImpl extends ChronoPeriod  {
  /**
   * Creates an instance.
   */
  private[calendar] def this(chrono: Chronology, years: Int, months: Int, days: Int) {


    this.chrono = chrono
    this.years = years
    this.months = months
    this.days = days
  }

  def get(unit: TemporalUnit): Long = {
    if (unit eq ChronoUnit.YEARS) {
       years
    }
    else if (unit eq ChronoUnit.MONTHS) {
       months
    }
    else if (unit eq ChronoUnit.DAYS) {
       days
    }
    else {
      throw new UnsupportedTemporalTypeException("Unsupported unit: " + unit)
    }
  }

  def getUnits: List[TemporalUnit] = {
     ChronoPeriodImpl.SUPPORTED_UNITS
  }

  def getChronology: Chronology = {
     chrono
  }

  override def isZero: Boolean = {
     years == 0 && months == 0 && days == 0
  }

  override def isNegative: Boolean = {
     years < 0 || months < 0 || days < 0
  }

  def plus(amountToAdd: TemporalAmount): ChronoPeriod = {
    val amount: ChronoPeriodImpl = validateAmount(amountToAdd)
     new ChronoPeriodImpl(chrono, Math.addExact(years, amount.years), Math.addExact(months, amount.months), Math.addExact(days, amount.days))
  }

  def minus(amountToSubtract: TemporalAmount): ChronoPeriod = {
    val amount: ChronoPeriodImpl = validateAmount(amountToSubtract)
     new ChronoPeriodImpl(chrono, Math.subtractExact(years, amount.years), Math.subtractExact(months, amount.months), Math.subtractExact(days, amount.days))
  }

  /**
   * Obtains an instance of {@code ChronoPeriodImpl} from a temporal amount.
   *
   * @param amount  the temporal amount to convert, not null
   * @return the period, not null
   */
  private def validateAmount(amount: TemporalAmount): ChronoPeriodImpl = {

    if (amount.isInstanceOf[ChronoPeriodImpl] == false) {
      throw new DateTimeException("Unable to obtain ChronoPeriod from TemporalAmount: " + amount.getClass)
    }
    val period: ChronoPeriodImpl = amount.asInstanceOf[ChronoPeriodImpl]
    if ((chrono == period.getChronology) == false) {
      throw new ClassCastException("Chronology mismatch, expected: " + chrono.getId + ", actual: " + period.getChronology.getId)
    }
     period
  }

  def multipliedBy(scalar: Int): ChronoPeriod = {
    if (this.isZero || scalar == 1) {
       this
    }
     new ChronoPeriodImpl(chrono, Math.multiplyExact(years, scalar), Math.multiplyExact(months, scalar), Math.multiplyExact(days, scalar))
  }

  def normalized: ChronoPeriod = {
    val monthRange: Long = monthRange
    if (monthRange > 0) {
      val totalMonths: Long = years * monthRange + months
      val splitYears: Long = totalMonths / monthRange
      val splitMonths: Int = (totalMonths % monthRange).asInstanceOf[Int]
      if (splitYears == years && splitMonths == months) {
         this
      }
       new ChronoPeriodImpl(chrono, Math.toIntExact(splitYears), splitMonths, days)
    }
     this
  }

  /**
   * Calculates the range of months.
   *
   * @return the month range, -1 if not fixed range
   */
  private def monthRange: Long = {
    val startRange: ValueRange = chrono.range(MONTH_OF_YEAR)
    if (startRange.isFixed && startRange.isIntValue) {
       startRange.getMaximum - startRange.getMinimum + 1
    }
     -1
  }

  def addTo(temporal: Temporal): Temporal = {
    validateChrono(temporal)
    if (months == 0) {
      if (years != 0) {
        temporal = temporal.plus(years, YEARS)
      }
    }
    else {
      val monthRange: Long = monthRange
      if (monthRange > 0) {
        temporal = temporal.plus(years * monthRange + months, MONTHS)
      }
      else {
        if (years != 0) {
          temporal = temporal.plus(years, YEARS)
        }
        temporal = temporal.plus(months, MONTHS)
      }
    }
    if (days != 0) {
      temporal = temporal.plus(days, DAYS)
    }
     temporal
  }

  def subtractFrom(temporal: Temporal): Temporal = {
    validateChrono(temporal)
    if (months == 0) {
      if (years != 0) {
        temporal = temporal.minus(years, YEARS)
      }
    }
    else {
      val monthRange: Long = monthRange
      if (monthRange > 0) {
        temporal = temporal.minus(years * monthRange + months, MONTHS)
      }
      else {
        if (years != 0) {
          temporal = temporal.minus(years, YEARS)
        }
        temporal = temporal.minus(months, MONTHS)
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

    val temporalChrono: Chronology = temporal.query(TemporalQuery.chronology)
    if (temporalChrono != null && (chrono == temporalChrono) == false) {
      throw new DateTimeException("Chronology mismatch, expected: " + chrono.getId + ", actual: " + temporalChrono.getId)
    }
  }

  override def equals(obj: AnyRef): Boolean = {
    if (this eq obj) {
       true
    }
    if (obj.isInstanceOf[ChronoPeriodImpl]) {
      val other: ChronoPeriodImpl = obj.asInstanceOf[ChronoPeriodImpl]
       years == other.years && months == other.months && days == other.days && (chrono == other.chrono)
    }
     false
  }

  override def hashCode: Int = {
     (years + Integer.rotateLeft(months, 8) + Integer.rotateLeft(days, 16)) ^ chrono.hashCode
  }

  override def toString: String = {
    if (isZero) {
       getChronology.toString + " P0D"
    }
    else {
      val buf: StringBuilder = new StringBuilder
      buf.append(getChronology.toString).append(' ').append('P')
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
   * Writes the Chronology using a
   * <a href="../../../serialized-form.html#java.time.chrono.Ser">dedicated serialized form</a>.
   * {{{
   * out.writeByte(12);  // identifies this as a ChronoPeriodImpl
   * out.writeUTF(getId());  // the chronology
   * out.writeInt(years);
   * out.writeInt(months);
   * out.writeInt(days);
   * }}}
   *
   * @return the instance of { @code Ser}, not null
   */
  protected def writeReplace: AnyRef = {
     new Ser(Ser.CHRONO_PERIOD_TYPE, this)
  }

  /**
   * Defend against malicious streams.
   * @return never
   * @throws InvalidObjectException always
   */
  private def readResolve: AnyRef = {
    throw new InvalidObjectException("Deserialization via serialization delegate")
  }

  private[chrono] def writeExternal(out: DataOutput) {
    out.writeUTF(chrono.getId)
    out.writeInt(years)
    out.writeInt(months)
    out.writeInt(days)
  }

  /**
   * The chronology.
   */
  private final val chrono: Chronology = null
  /**
   * The number of years.
   */
  private[chrono] final val years: Int = 0
  /**
   * The number of months.
   */
  private[chrono] final val months: Int = 0
  /**
   * The number of days.
   */
  private[chrono] final val days: Int = 0
}

