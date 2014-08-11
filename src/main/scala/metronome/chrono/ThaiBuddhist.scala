package metronome.chrono

/*
 * Copyright (c) 2012, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
/*
 * Copyright (c) 2012, Stephen Colebourne & Michael Nascimento Santos
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of JSR-310 nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


/**
 * The Thai Buddhist calendar system.
 * <p>
 * This chronology defines the rules of the Thai Buddhist calendar system.
 * This calendar system is primarily used in Thailand.
 * Dates are aligned such that {@code 2484-01-01 (Buddhist)} is {@code 1941-01-01 (ISO)}.
 * <p>
 * The fields are defined as follows:
 * <p><ul>
 * <li>era - There are two eras, the current 'Buddhist' (ERA_BE) and the previous era (ERA_BEFORE_BE).
 * <li>year-of-era - The year-of-era for the current era increases uniformly from the epoch at year one.
 * For the previous era the year increases from one as time goes backwards.
 * The value for the current era is equal to the ISO proleptic-year plus 543.
 * <li>proleptic-year - The proleptic year is the same as the year-of-era for the
 * current era. For the previous era, years have zero, then negative values.
 * The value is equal to the ISO proleptic-year plus 543.
 * <li>month-of-year - The ThaiBuddhist month-of-year exactly matches ISO.
 * <li>day-of-month - The ThaiBuddhist day-of-month exactly matches ISO.
 * <li>day-of-year - The ThaiBuddhist day-of-year exactly matches ISO.
 * <li>leap-year - The ThaiBuddhist leap-year pattern exactly matches ISO, such that the two calendars
 * are never out of step.
 * </ul><p>
 *
 * @implSpec
 * This class is immutable and thread-safe.
 *
 * @since 1.8
 */
final object ThaiBuddhistChronology {
  /**
   * Singleton instance of the Buddhist chronology.
   */
  final val INSTANCE: ThaiBuddhistChronology = new ThaiBuddhistChronology
  /**
   * Serialization version.
   */
  private final val serialVersionUID: Long = 2775954514031616474L
  /**
   * Containing the offset to add to the ISO year.
   */
  private[chrono] final val YEARS_DIFFERENCE: Int = 543
  /**
   * Narrow names for eras.
   */
  private final val ERA_NARROW_NAMES: HashMap[String, Array[String]] = new HashMap[String, Array[String]]
  /**
   * Short names for eras.
   */
  private final val ERA_SHORT_NAMES: HashMap[String, Array[String]] = new HashMap[String, Array[String]]
  /**
   * Full names for eras.
   */
  private final val ERA_FULL_NAMES: HashMap[String, Array[String]] = new HashMap[String, Array[String]]
  /**
   * Fallback language for the era names.
   */
  private final val FALLBACK_LANGUAGE: String = "en"
  /**
   * Language that has the era names.
   */
  private final val TARGET_LANGUAGE: String = "th"
}

final class ThaiBuddhistChronology extends Chronology with Serializable {
  /**
   * Restricted constructor.
   */
  private def this() {
    this()
  }

  /**
   * Gets the ID of the chronology - 'ThaiBuddhist'.
   * <p>
   * The ID uniquely identifies the {@code Chronology}.
   * It can be used to lookup the {@code Chronology} using {@link #of(String)}.
   *
   * @return the chronology ID - 'ThaiBuddhist'
   * @see #getCalendarType()
   */
  def getId: String = {
    return "ThaiBuddhist"
  }

  /**
   * Gets the calendar type of the underlying calendar system - 'buddhist'.
   * <p>
   * The calendar type is an identifier defined by the
   * <em>Unicode Locale Data Markup Language (LDML)</em> specification.
   * It can be used to lookup the {@code Chronology} using {@link #of(String)}.
   * It can also be used as part of a locale, accessible via
   * {@link Locale#getUnicodeLocaleType(String)} with the key 'ca'.
   *
   * @return the calendar system type - 'buddhist'
   * @see #getId()
   */
  def getCalendarType: String = {
    return "buddhist"
  }

  /**
   * Obtains a local date in Thai Buddhist calendar system from the
   * era, year-of-era, month-of-year and day-of-month fields.
   *
   * @param era  the Thai Buddhist era, not null
   * @param yearOfEra  the year-of-era
   * @param month  the month-of-year
   * @param dayOfMonth  the day-of-month
   * @return the Thai Buddhist local date, not null
   * @throws DateTimeException if unable to create the date
   * @throws ClassCastException if the { @code era} is not a { @code ThaiBuddhistEra}
   */
  override def date(era: Era, yearOfEra: Int, month: Int, dayOfMonth: Int): ThaiBuddhistDate = {
    return date(prolepticYear(era, yearOfEra), month, dayOfMonth)
  }

  /**
   * Obtains a local date in Thai Buddhist calendar system from the
   * proleptic-year, month-of-year and day-of-month fields.
   *
   * @param prolepticYear  the proleptic-year
   * @param month  the month-of-year
   * @param dayOfMonth  the day-of-month
   * @return the Thai Buddhist local date, not null
   * @throws DateTimeException if unable to create the date
   */
  def date(prolepticYear: Int, month: Int, dayOfMonth: Int): ThaiBuddhistDate = {
    return new ThaiBuddhistDate(LocalDate.of(prolepticYear - YEARS_DIFFERENCE, month, dayOfMonth))
  }

  /**
   * Obtains a local date in Thai Buddhist calendar system from the
   * era, year-of-era and day-of-year fields.
   *
   * @param era  the Thai Buddhist era, not null
   * @param yearOfEra  the year-of-era
   * @param dayOfYear  the day-of-year
   * @return the Thai Buddhist local date, not null
   * @throws DateTimeException if unable to create the date
   * @throws ClassCastException if the { @code era} is not a { @code ThaiBuddhistEra}
   */
  override def dateYearDay(era: Era, yearOfEra: Int, dayOfYear: Int): ThaiBuddhistDate = {
    return dateYearDay(prolepticYear(era, yearOfEra), dayOfYear)
  }

  /**
   * Obtains a local date in Thai Buddhist calendar system from the
   * proleptic-year and day-of-year fields.
   *
   * @param prolepticYear  the proleptic-year
   * @param dayOfYear  the day-of-year
   * @return the Thai Buddhist local date, not null
   * @throws DateTimeException if unable to create the date
   */
  def dateYearDay(prolepticYear: Int, dayOfYear: Int): ThaiBuddhistDate = {
    return new ThaiBuddhistDate(LocalDate.ofYearDay(prolepticYear - YEARS_DIFFERENCE, dayOfYear))
  }

  /**
   * Obtains a local date in the Thai Buddhist calendar system from the epoch-day.
   *
   * @param epochDay  the epoch day
   * @return the Thai Buddhist local date, not null
   * @throws DateTimeException if unable to create the date
   */
  def dateEpochDay(epochDay: Long): ThaiBuddhistDate = {
    return new ThaiBuddhistDate(LocalDate.ofEpochDay(epochDay))
  }

  override def dateNow: ThaiBuddhistDate = {
    return dateNow(Clock.systemDefaultZone)
  }

  override def dateNow(zone: ZoneId): ThaiBuddhistDate = {
    return dateNow(Clock.system(zone))
  }

  override def dateNow(clock: Clock): ThaiBuddhistDate = {
    return date(LocalDate.now(clock))
  }

  def date(temporal: TemporalAccessor): ThaiBuddhistDate = {
    if (temporal.isInstanceOf[ThaiBuddhistDate]) {
      return temporal.asInstanceOf[ThaiBuddhistDate]
    }
    return new ThaiBuddhistDate(LocalDate.from(temporal))
  }

  @SuppressWarnings(Array("unchecked")) override def localDateTime(temporal: TemporalAccessor): ChronoLocalDateTime[ThaiBuddhistDate] = {
    return super.localDateTime(temporal).asInstanceOf[ChronoLocalDateTime[ThaiBuddhistDate]]
  }

  @SuppressWarnings(Array("unchecked")) override def zonedDateTime(temporal: TemporalAccessor): ChronoZonedDateTime[ThaiBuddhistDate] = {
    return super.zonedDateTime(temporal).asInstanceOf[ChronoZonedDateTime[ThaiBuddhistDate]]
  }

  @SuppressWarnings(Array("unchecked")) override def zonedDateTime(instant: Instant, zone: ZoneId): ChronoZonedDateTime[ThaiBuddhistDate] = {
    return super.zonedDateTime(instant, zone).asInstanceOf[ChronoZonedDateTime[ThaiBuddhistDate]]
  }

  /**
   * Checks if the specified year is a leap year.
   * <p>
   * Thai Buddhist leap years occur exactly in line with ISO leap years.
   * This method does not validate the year passed in, and only has a
   * well-defined result for years in the supported range.
   *
   * @param prolepticYear  the proleptic-year to check, not validated for range
   * @return true if the year is a leap year
   */
  def isLeapYear(prolepticYear: Long): Boolean = {
    return IsoChronology.INSTANCE.isLeapYear(prolepticYear - YEARS_DIFFERENCE)
  }

  def prolepticYear(era: Era, yearOfEra: Int): Int = {
    if (era.isInstanceOf[ThaiBuddhistEra] == false) {
      throw new ClassCastException("Era must be BuddhistEra")
    }
    return (if (era eq ThaiBuddhistEra.BE) yearOfEra else 1 - yearOfEra)
  }

  def eraOf(eraValue: Int): ThaiBuddhistEra = {
    return ThaiBuddhistEra.of(eraValue)
  }

  def eras: List[Era] = {
    return Arrays.asList[Era](ThaiBuddhistEra.values)
  }

  def range(field: ChronoField): ValueRange = {
    field match {
      case PROLEPTIC_MONTH => {
        val range: ValueRange = PROLEPTIC_MONTH.range
        return ValueRange.of(range.getMinimum + YEARS_DIFFERENCE * 12L, range.getMaximum + YEARS_DIFFERENCE * 12L)
      }
      case YEAR_OF_ERA => {
        val range: ValueRange = YEAR.range
        return ValueRange.of(1, -(range.getMinimum + YEARS_DIFFERENCE) + 1, range.getMaximum + YEARS_DIFFERENCE)
      }
      case YEAR => {
        val range: ValueRange = YEAR.range
        return ValueRange.of(range.getMinimum + YEARS_DIFFERENCE, range.getMaximum + YEARS_DIFFERENCE)
      }
    }
    return field.range
  }

  def resolveDate(fieldValues: Map[TemporalField, Long], resolverStyle: ResolverStyle): ThaiBuddhistDate = {
    return super.resolveDate(fieldValues, resolverStyle).asInstanceOf[ThaiBuddhistDate]
  }
}

/*
 * Copyright (c) 2012, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
/*
 * Copyright (c) 2012, Stephen Colebourne & Michael Nascimento Santos
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of JSR-310 nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


/**
 * A date in the Thai Buddhist calendar system.
 * <p>
 * This date operates using the {@linkplain ThaiBuddhistChronology Thai Buddhist calendar}.
 * This calendar system is primarily used in Thailand.
 * Dates are aligned such that {@code 2484-01-01 (Buddhist)} is {@code 1941-01-01 (ISO)}.
 *
 * @implSpec
 * This class is immutable and thread-safe.
 *
 * @since 1.8
 */
final object ThaiBuddhistDate {
  /**
   * Obtains the current {@code ThaiBuddhistDate} from the system clock in the default time-zone.
   * <p>
   * This will query the {@link Clock#systemDefaultZone() system clock} in the default
   * time-zone to obtain the current date.
   * <p>
   * Using this method will prevent the ability to use an alternate clock for testing
   * because the clock is hard-coded.
   *
   * @return the current date using the system clock and default time-zone, not null
   */
  def now: ThaiBuddhistDate = {
    return now(Clock.systemDefaultZone)
  }

  /**
   * Obtains the current {@code ThaiBuddhistDate} from the system clock in the specified time-zone.
   * <p>
   * This will query the {@link Clock#system(ZoneId) system clock} to obtain the current date.
   * Specifying the time-zone avoids dependence on the default time-zone.
   * <p>
   * Using this method will prevent the ability to use an alternate clock for testing
   * because the clock is hard-coded.
   *
   * @param zone  the zone ID to use, not null
   * @return the current date using the system clock, not null
   */
  def now(zone: ZoneId): ThaiBuddhistDate = {
    return now(Clock.system(zone))
  }

  /**
   * Obtains the current {@code ThaiBuddhistDate} from the specified clock.
   * <p>
   * This will query the specified clock to obtain the current date - today.
   * Using this method allows the use of an alternate clock for testing.
   * The alternate clock may be introduced using {@linkplain Clock dependency injection}.
   *
   * @param clock  the clock to use, not null
   * @return the current date, not null
   * @throws DateTimeException if the current date cannot be obtained
   */
  def now(clock: Clock): ThaiBuddhistDate = {
    return new ThaiBuddhistDate(LocalDate.now(clock))
  }

  /**
   * Obtains a {@code ThaiBuddhistDate} representing a date in the Thai Buddhist calendar
   * system from the proleptic-year, month-of-year and day-of-month fields.
   * <p>
   * This returns a {@code ThaiBuddhistDate} with the specified fields.
   * The day must be valid for the year and month, otherwise an exception will be thrown.
   *
   * @param prolepticYear  the Thai Buddhist proleptic-year
   * @param month  the Thai Buddhist month-of-year, from 1 to 12
   * @param dayOfMonth  the Thai Buddhist day-of-month, from 1 to 31
   * @return the date in Thai Buddhist calendar system, not null
   * @throws DateTimeException if the value of any field is out of range,
   *                           or if the day-of-month is invalid for the month-year
   */
  def of(prolepticYear: Int, month: Int, dayOfMonth: Int): ThaiBuddhistDate = {
    return new ThaiBuddhistDate(LocalDate.of(prolepticYear - YEARS_DIFFERENCE, month, dayOfMonth))
  }

  /**
   * Obtains a {@code ThaiBuddhistDate} from a temporal object.
   * <p>
   * This obtains a date in the Thai Buddhist calendar system based on the specified temporal.
   * A {@code TemporalAccessor} represents an arbitrary set of date and time information,
   * which this factory converts to an instance of {@code ThaiBuddhistDate}.
   * <p>
   * The conversion typically uses the {@link ChronoField#EPOCH_DAY EPOCH_DAY}
   * field, which is standardized across calendar systems.
   * <p>
   * This method matches the signature of the functional interface {@link TemporalQuery}
   * allowing it to be used as a query via method reference, {@code ThaiBuddhistDate::from}.
   *
   * @param temporal  the temporal object to convert, not null
   * @return the date in Thai Buddhist calendar system, not null
   * @throws DateTimeException if unable to convert to a { @code ThaiBuddhistDate}
   */
  def from(temporal: TemporalAccessor): ThaiBuddhistDate = {
    return ThaiBuddhistChronology.INSTANCE.date(temporal)
  }

  private[chrono] def readExternal(in: DataInput): ThaiBuddhistDate = {
    val year: Int = in.readInt
    val month: Int = in.readByte
    val dayOfMonth: Int = in.readByte
    return ThaiBuddhistChronology.INSTANCE.date(year, month, dayOfMonth)
  }

  /**
   * Serialization version.
   */
  private final val serialVersionUID: Long = -8722293800195731463L
}

final class ThaiBuddhistDate extends ChronoLocalDateImpl[ThaiBuddhistDate] with ChronoLocalDate with Serializable {
  /**
   * Creates an instance from an ISO date.
   *
   * @param isoDate  the standard local date, validated not null
   */
  private[chrono] def this(isoDate: LocalDate) {
    this()
    Objects.requireNonNull(isoDate, "isoDate")
    this.isoDate = isoDate
  }

  /**
   * Gets the chronology of this date, which is the Thai Buddhist calendar system.
   * <p>
   * The {@code Chronology} represents the calendar system in use.
   * The era and other fields in {@link ChronoField} are defined by the chronology.
   *
   * @return the Thai Buddhist chronology, not null
   */
  def getChronology: ThaiBuddhistChronology = {
    return ThaiBuddhistChronology.INSTANCE
  }

  /**
   * Gets the era applicable at this date.
   * <p>
   * The Thai Buddhist calendar system has two eras, 'BE' and 'BEFORE_BE',
   * defined by {@link ThaiBuddhistEra}.
   *
   * @return the era applicable at this date, not null
   */
  override def getEra: ThaiBuddhistEra = {
    return (if (getProlepticYear >= 1) ThaiBuddhistEra.BE else ThaiBuddhistEra.BEFORE_BE)
  }

  /**
   * Returns the length of the month represented by this date.
   * <p>
   * This returns the length of the month in days.
   * Month lengths match those of the ISO calendar system.
   *
   * @return the length of the month in days
   */
  def lengthOfMonth: Int = {
    return isoDate.lengthOfMonth
  }

  override def range(field: TemporalField): ValueRange = {
    if (field.isInstanceOf[ChronoField]) {
      if (isSupported(field)) {
        val f: ChronoField = field.asInstanceOf[ChronoField]
        f match {
          case DAY_OF_MONTH =>
          case DAY_OF_YEAR =>
          case ALIGNED_WEEK_OF_MONTH =>
            return isoDate.range(field)
          case YEAR_OF_ERA => {
            val range: ValueRange = YEAR.range
            val max: Long = (if (getProlepticYear <= 0) -(range.getMinimum + YEARS_DIFFERENCE) + 1 else range.getMaximum + YEARS_DIFFERENCE)
            return ValueRange.of(1, max)
          }
        }
        return getChronology.range(f)
      }
      throw new UnsupportedTemporalTypeException("Unsupported field: " + field)
    }
    return field.rangeRefinedBy(this)
  }

  def getLong(field: TemporalField): Long = {
    if (field.isInstanceOf[ChronoField]) {
      field.asInstanceOf[ChronoField] match {
        case PROLEPTIC_MONTH =>
          return getProlepticMonth
        case YEAR_OF_ERA => {
          val prolepticYear: Int = getProlepticYear
          return (if (prolepticYear >= 1) prolepticYear else 1 - prolepticYear)
        }
        case YEAR =>
          return getProlepticYear
        case ERA =>
          return (if (getProlepticYear >= 1) 1 else 0)
      }
      return isoDate.getLong(field)
    }
    return field.getFrom(this)
  }

  private def getProlepticMonth: Long = {
    return getProlepticYear * 12L + isoDate.getMonthValue - 1
  }

  private def getProlepticYear: Int = {
    return isoDate.getYear + YEARS_DIFFERENCE
  }

  override def `with`(field: TemporalField, newValue: Long): ThaiBuddhistDate = {
    if (field.isInstanceOf[ChronoField]) {
      val f: ChronoField = field.asInstanceOf[ChronoField]
      if (getLong(f) == newValue) {
        return this
      }
      f match {
        case PROLEPTIC_MONTH =>
          getChronology.range(f).checkValidValue(newValue, f)
          return plusMonths(newValue - getProlepticMonth)
        case YEAR_OF_ERA =>
        case YEAR =>
        case ERA => {
          val nvalue: Int = getChronology.range(f).checkValidIntValue(newValue, f)
          f match {
            case YEAR_OF_ERA =>
              return `with`(isoDate.withYear((if (getProlepticYear >= 1) nvalue else 1 - nvalue) - YEARS_DIFFERENCE))
            case YEAR =>
              return `with`(isoDate.withYear(nvalue - YEARS_DIFFERENCE))
            case ERA =>
              return `with`(isoDate.withYear((1 - getProlepticYear) - YEARS_DIFFERENCE))
          }
        }
      }
      return `with`(isoDate.`with`(field, newValue))
    }
    return super.`with`(field, newValue)
  }

  /**
   * {@inheritDoc}
   * @throws DateTimeException { @inheritDoc}
   * @throws ArithmeticException { @inheritDoc}
   */
  override def `with`(adjuster: TemporalAdjuster): ThaiBuddhistDate = {
    return super.`with`(adjuster)
  }

  /**
   * {@inheritDoc}
   * @throws DateTimeException { @inheritDoc}
   * @throws ArithmeticException { @inheritDoc}
   */
  override def plus(amount: TemporalAmount): ThaiBuddhistDate = {
    return super.plus(amount)
  }

  /**
   * {@inheritDoc}
   * @throws DateTimeException { @inheritDoc}
   * @throws ArithmeticException { @inheritDoc}
   */
  override def minus(amount: TemporalAmount): ThaiBuddhistDate = {
    return super.minus(amount)
  }

  private[chrono] def plusYears(years: Long): ThaiBuddhistDate = {
    return `with`(isoDate.plusYears(years))
  }

  private[chrono] def plusMonths(months: Long): ThaiBuddhistDate = {
    return `with`(isoDate.plusMonths(months))
  }

  private[chrono] override def plusWeeks(weeksToAdd: Long): ThaiBuddhistDate = {
    return super.plusWeeks(weeksToAdd)
  }

  private[chrono] def plusDays(days: Long): ThaiBuddhistDate = {
    return `with`(isoDate.plusDays(days))
  }

  override def plus(amountToAdd: Long, unit: TemporalUnit): ThaiBuddhistDate = {
    return super.plus(amountToAdd, unit)
  }

  override def minus(amountToAdd: Long, unit: TemporalUnit): ThaiBuddhistDate = {
    return super.minus(amountToAdd, unit)
  }

  private[chrono] override def minusYears(yearsToSubtract: Long): ThaiBuddhistDate = {
    return super.minusYears(yearsToSubtract)
  }

  private[chrono] override def minusMonths(monthsToSubtract: Long): ThaiBuddhistDate = {
    return super.minusMonths(monthsToSubtract)
  }

  private[chrono] override def minusWeeks(weeksToSubtract: Long): ThaiBuddhistDate = {
    return super.minusWeeks(weeksToSubtract)
  }

  private[chrono] override def minusDays(daysToSubtract: Long): ThaiBuddhistDate = {
    return super.minusDays(daysToSubtract)
  }

  private def `with`(newDate: LocalDate): ThaiBuddhistDate = {
    return (if ((newDate == isoDate)) this else new ThaiBuddhistDate(newDate))
  }

  @SuppressWarnings(Array("unchecked")) final override def atTime(localTime: LocalTime): ChronoLocalDateTime[ThaiBuddhistDate] = {
    return super.atTime(localTime).asInstanceOf[ChronoLocalDateTime[ThaiBuddhistDate]]
  }

  def until(endDate: ChronoLocalDate): ChronoPeriod = {
    val period: Period = isoDate.until(endDate)
    return getChronology.period(period.getYears, period.getMonths, period.getDays)
  }

  override def toEpochDay: Long = {
    return isoDate.toEpochDay
  }

  override def equals(obj: AnyRef): Boolean = {
    if (this eq obj) {
      return true
    }
    if (obj.isInstanceOf[ThaiBuddhistDate]) {
      val otherDate: ThaiBuddhistDate = obj.asInstanceOf[ThaiBuddhistDate]
      return this.isoDate == otherDate.isoDate
    }
    return false
  }

  override def hashCode: Int = {
    return getChronology.getId.hashCode ^ isoDate.hashCode
  }

  private def writeReplace: AnyRef = {
    return new Ser(Ser.THAIBUDDHIST_DATE_TYPE, this)
  }

  private[chrono] def writeExternal(out: DataOutput) {
    out.writeInt(this.get(YEAR))
    out.writeByte(this.get(MONTH_OF_YEAR))
    out.writeByte(this.get(DAY_OF_MONTH))
  }

  /**
   * The underlying date.
   */
  private final val isoDate: LocalDate = null
}

/*
 * Copyright (c) 2012, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
/*
 * This file is available under and governed by the GNU General Public
 * License version 2 only, as published by the Free Software Foundation.
 * However, the following notice accompanied the original version of this
 * file:
 *
 * Copyright (c) 2012, Stephen Colebourne & Michael Nascimento Santos
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of JSR-310 nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


/**
 * An era in the Thai Buddhist calendar system.
 * <p>
 * The Thai Buddhist calendar system has two eras.
 * The current era, for years from 1 onwards, is known as the 'Buddhist' era.
 * All previous years, zero or earlier in the proleptic count or one and greater
 * in the year-of-era count, are part of the 'Before Buddhist' era.
 * <p>
 * <table summary="Buddhist years and eras" cellpadding="2" cellspacing="3" border="0" >
 * <thead>
 * <tr class="tableSubHeadingColor">
 * <th class="colFirst" align="left">year-of-era</th>
 * <th class="colFirst" align="left">era</th>
 * <th class="colFirst" align="left">proleptic-year</th>
 * <th class="colLast" align="left">ISO proleptic-year</th>
 * </tr>
 * </thead>
 * <tbody>
 * <tr class="rowColor">
 * <td>2</td><td>BE</td><td>2</td><td>-542</td>
 * </tr>
 * <tr class="altColor">
 * <td>1</td><td>BE</td><td>1</td><td>-543</td>
 * </tr>
 * <tr class="rowColor">
 * <td>1</td><td>BEFORE_BE</td><td>0</td><td>-544</td>
 * </tr>
 * <tr class="altColor">
 * <td>2</td><td>BEFORE_BE</td><td>-1</td><td>-545</td>
 * </tr>
 * </tbody>
 * </table>
 * <p>
 * <b>Do not use {@code ordinal()} to obtain the numeric representation of {@code ThaiBuddhistEra}.
 * Use {@code getValue()} instead.</b>
 *
 * @implSpec
 * This is an immutable and thread-safe enum.
 *
 * @since 1.8
 */
final object ThaiBuddhistEra {
  /**
   * Obtains an instance of {@code ThaiBuddhistEra} from an {@code int} value.
   * <p>
   * {@code ThaiBuddhistEra} is an enum representing the Thai Buddhist eras of BEFORE_BE/BE.
   * This factory allows the enum to be obtained from the {@code int} value.
   *
   * @param thaiBuddhistEra  the era to represent, from 0 to 1
   * @return the BuddhistEra singleton, never null
   * @throws DateTimeException if the era is invalid
   */
  def of(thaiBuddhistEra: Int): ThaiBuddhistEra = {
    thaiBuddhistEra match {
      case 0 =>
        return BEFORE_BE
      case 1 =>
        return BE
      case _ =>
        throw new DateTimeException("Invalid era: " + thaiBuddhistEra)
    }
  }

  private[chrono] def readExternal(in: DataInput): ThaiBuddhistEra = {
    val eraValue: Byte = in.readByte
    return ThaiBuddhistEra.of(eraValue)
  }

  /**
   * The singleton instance for the era before the current one, 'Before Buddhist Era',
   * which has the numeric value 0.
   */
  final val BEFORE_BE: = null
  /**
   * The singleton instance for the current era, 'Buddhist Era',
   * which has the numeric value 1.
   */
  final val BE: = null
}

final class ThaiBuddhistEra extends Era {
  /**
   * Gets the numeric era {@code int} value.
   * <p>
   * The era BEFORE_BE has the value 0, while the era BE has the value 1.
   *
   * @return the era value, from 0 (BEFORE_BE) to 1 (BE)
   */
  def getValue: Int = {
    return ordinal
  }

  private def writeReplace: AnyRef = {
    return new Ser(Ser.THAIBUDDHIST_ERA_TYPE, this)
  }

  private[chrono] def writeExternal(out: DataOutput) {
    out.writeByte(this.getValue)
  }
}

