package metronome.chrono

/**
 * The Hijrah calendar is a lunar calendar supporting Islamic calendars.
 * <p>
 * The HijrahChronology follows the rules of the Hijrah calendar system. The Hijrah
 * calendar has several variants based on differences in when the new moon is
 * determined to have occurred and where the observation is made.
 * In some variants the length of each month is
 * computed algorithmically from the astronomical data for the moon and earth and
 * in others the length of the month is determined by an authorized sighting
 * of the new moon. For the algorithmically based calendars the calendar
 * can project into the future.
 * For sighting based calendars only historical data from past
 * sightings is available.
 * <p>
 * The length of each month is 29 or 30 days.
 * Ordinary years have 354 days; leap years have 355 days.
 *
 * <p>
 * CLDR and LDML identify variants:
 * <table cellpadding="2" summary="Variants of Hijrah Calendars">
 * <thead>
 * <tr class="tableSubHeadingColor">
 * <th class="colFirst" align="left" >Chronology ID</th>
 * <th class="colFirst" align="left" >Calendar Type</th>
 * <th class="colFirst" align="left" >Locale extension, see {@link java.util.Locale}</th>
 * <th class="colLast" align="left" >Description</th>
 * </tr>
 * </thead>
 * <tbody>
 * <tr class="altColor">
 * <td>Hijrah-umalqura</td>
 * <td>islamic-umalqura</td>
 * <td>ca-islamic-umalqura</td>
 * <td>Islamic - Umm Al-Qura calendar of Saudi Arabia</td>
 * </tr>
 * </tbody>
 * </table>
 * <p>Additional variants may be available through {@link Chronology#getAvailableChronologies()}.
 *
 * <p>Example</p>
 * <p>
 * Selecting the chronology from the locale uses {@link Chronology#ofLocale}
 * to find the Chronology based on Locale supported BCP 47 extension mechanism
 * to request a specific calendar ("ca"). For example,
 * </p>
 * <pre>
 * Locale locale = Locale.forLanguageTag("en-US-u-ca-islamic-umalqura");
 * Chronology chrono = Chronology.ofLocale(locale);
 * </pre>
 *
 * @implSpec
 * This class is immutable and thread-safe.
 *
 * @implNote
 * Each Hijrah variant is configured individually. Each variant is defined by a
 * property resource that defines the { @code ID}, the { @code calendar type},
 *                                                             the start of the calendar, the alignment with the
 *                                                             ISO calendar, and the length of each month for a range of years.
 *                                                             The variants are identified in the { @code calendars.properties} file.
 *                                                                                                        The new properties are prefixed with { @code "calendars.hijrah."}:
 *                                                                                                                                                     <table cellpadding="2" border="0" summary="Configuration of Hijrah Calendar Variants">
 *                                                                                                                                                     <thead>
 *                                                                                                                                                     <tr class="tableSubHeadingColor">
 *                                                                                                                                                     <th class="colFirst" align="left">Property Name</th>
 *                                                                                                                                                     <th class="colFirst" align="left">Property value</th>
 *                                                                                                                                                     <th class="colLast" align="left">Description </th>
 *                                                                                                                                                     </tr>
 *                                                                                                                                                     </thead>
 *                                                                                                                                                     <tbody>
 *                                                                                                                                                     <tr class="altColor">
 *                                                                                                                                                     <td>calendars.hijrah.{ID}</td>
 *                                                                                                                                                     <td>The property resource defining the { @code {ID}} variant</td>
 *                                                                                                                                                                                                    <td>The property resource is located with the { @code calendars.properties} file</td>
 *                                                                                                                                                                                                                                                          </tr>
 *                                                                                                                                                                                                                                                          <tr class="rowColor">
 *                                                                                                                                                                                                                                                          <td>calendars.hijrah.{ID}.type</td>
 *                                                                                                                                                                                                                                                          <td>The calendar type</td>
 *                                                                                                                                                                                                                                                          <td>LDML defines the calendar type names</td>
 *                                                                                                                                                                                                                                                          </tr>
 *                                                                                                                                                                                                                                                          </tbody>
 *                                                                                                                                                                                                                                                          </table>
 *                                                                                                                                                                                                                                                          <p>
 *                                                                                                                                                                                                                                                          The Hijrah property resource is a set of properties that describe the calendar.
 *                                                                                                                                                                                                                                                          The syntax is defined by { @code java.util.Properties#load(Reader)}.
 *                                                                                                                                                                                                                                                                                           <table cellpadding="2" summary="Configuration of Hijrah Calendar">
 *                                                                                                                                                                                                                                                                                           <thead>
 *                                                                                                                                                                                                                                                                                           <tr class="tableSubHeadingColor">
 *                                                                                                                                                                                                                                                                                           <th class="colFirst" align="left" > Property Name</th>
 *                                                                                                                                                                                                                                                                                           <th class="colFirst" align="left" > Property value</th>
 *                                                                                                                                                                                                                                                                                           <th class="colLast" align="left" > Description </th>
 *                                                                                                                                                                                                                                                                                           </tr>
 *                                                                                                                                                                                                                                                                                           </thead>
 *                                                                                                                                                                                                                                                                                           <tbody>
 *                                                                                                                                                                                                                                                                                           <tr class="altColor">
 *                                                                                                                                                                                                                                                                                           <td>id</td>
 *                                                                                                                                                                                                                                                                                           <td>Chronology Id, for example, "Hijrah-umalqura"</td>
 *                                                                                                                                                                                                                                                                                           <td>The Id of the calendar in common usage</td>
 *                                                                                                                                                                                                                                                                                           </tr>
 *                                                                                                                                                                                                                                                                                           <tr class="rowColor">
 *                                                                                                                                                                                                                                                                                           <td>type</td>
 *                                                                                                                                                                                                                                                                                           <td>Calendar type, for example, "islamic-umalqura"</td>
 *                                                                                                                                                                                                                                                                                           <td>LDML defines the calendar types</td>
 *                                                                                                                                                                                                                                                                                           </tr>
 *                                                                                                                                                                                                                                                                                           <tr class="altColor">
 *                                                                                                                                                                                                                                                                                           <td>version</td>
 *                                                                                                                                                                                                                                                                                           <td>Version, for example: "1.8.0_1"</td>
 *                                                                                                                                                                                                                                                                                           <td>The version of the Hijrah variant data</td>
 *                                                                                                                                                                                                                                                                                           </tr>
 *                                                                                                                                                                                                                                                                                           <tr class="rowColor">
 *                                                                                                                                                                                                                                                                                           <td>iso-start</td>
 *                                                                                                                                                                                                                                                                                           <td>ISO start date, formatted as { @code yyyy-MM-dd}, for example: "1900-04-30"</td>
 *                                                                                                                                                                                                                                                                                                                                    <td>The ISO date of the first day of the minimum Hijrah year.</td>
 *                                                                                                                                                                                                                                                                                                                                    </tr>
 *                                                                                                                                                                                                                                                                                                                                    <tr class="altColor">
 *                                                                                                                                                                                                                                                                                                                                    <td>yyyy - a numeric 4 digit year, for example "1434"</td>
 *                                                                                                                                                                                                                                                                                                                                    <td>The value is a sequence of 12 month lengths,
 *                                                                                                                                                                                                                                                                                                                                    for example: "29 30 29 30 29 30 30 30 29 30 29 29"</td>
 *                                                                                                                                                                                                                                                                                                                                    <td>The lengths of the 12 months of the year separated by whitespace.
 *                                                                                                                                                                                                                                                                                                                                    A numeric year property must be present for every year without any gaps.
 *                                                                                                                                                                                                                                                                                                                                    The month lengths must be between 29-32 inclusive.
 *                                                                                                                                                                                                                                                                                                                                    </td>
 *                                                                                                                                                                                                                                                                                                                                    </tr>
 *                                                                                                                                                                                                                                                                                                                                    </tbody>
 *                                                                                                                                                                                                                                                                                                                                    </table>
 *
 * @since 1.8
 */
object HijrahChronology {
  /**
   * For each Hijrah variant listed, create the HijrahChronology and register it.
   * Exceptions during initialization are logged but otherwise ignored.
   */
  private def registerVariants {
    import scala.collection.JavaConversions._
    for (name <- calendarProperties.stringPropertyNames) {
      if (name.startsWith(PROP_PREFIX)) {
        val id: String = name.substring(PROP_PREFIX.length)
        if (id.indexOf('.') >= 0) {
          continue //todo: continue is not supported
        }
        if (id == INSTANCE.getId) {
          continue //todo: continue is not supported
        }
        try {
          val chrono: HijrahChronology = new HijrahChronology(id)
          Chronology.registerChrono(chrono)
        }
        catch {
          case ex: DateTimeException => {
            val logger: PlatformLogger = PlatformLogger.getLogger("java.time.chrono")
            logger.severe("Unable to initialize Hijrah calendar: " + id, ex)
          }
        }
      }
    }
  }

  /**
   * Return the configuration properties from the resource.
   * <p>
   * The default location of the variant configuration resource is:
   * <pre>
   * "$java.home/lib/" + resource-name
   * </pre>
   *
   * @param resource the name of the calendar property resource
   * @return a Properties containing the properties read from the resource.
   * @throws Exception if access to the property resource fails
   */
  private def readConfigProperties(resource: String): Properties = {
    try {
       AccessController.doPrivileged(() -> {
        String libDir = System.getProperty("java.home") + File.separator + "lib";
        File file = new File(libDir, resource);
        Properties props = new Properties();
        try (InputStream is = new FileInputStream(file)) {
          props.load(is);
        }
         props;
      }.asInstanceOf[PrivilegedExceptionAction[Properties]])
    }
    catch {
      case pax: PrivilegedActionException => {
        throw pax.getException
      }
    }
  }

  /**
   * Serialization version.
   */
  private final val serialVersionUID: Long = 3127340209035924785L
  /**
   * Singleton instance of the Islamic Umm Al-Qura calendar of Saudi Arabia.
   * Other Hijrah chronology variants may be available from
   * {@link Chronology#getAvailableChronologies}.
   */
  final val INSTANCE: HijrahChronology = null
  /**
   * A reference to the properties stored in
   * ${java.home}/lib/calendars.properties
   */
  @transient
  private final val calendarProperties: Properties = null
  /**
   * Prefix of property names for Hijrah calendar variants.
   */
  private final val PROP_PREFIX: String = "calendar.hijrah."
  /**
   * Suffix of property names containing the calendar type of a variant.
   */
  private final val PROP_TYPE_SUFFIX: String = ".type"
  private final val KEY_ID: String = "id"
  private final val KEY_TYPE: String = "type"
  private final val KEY_VERSION: String = "version"
  private final val KEY_ISO_START: String = "iso-start"
}

final class HijrahChronology extends Chronology  {
  /**
   * Create a HijrahChronology for the named variant.
   * The resource and calendar type are retrieved from properties
   * in the {@code calendars.properties}.
   * The property names are {@code "calendar.hijrah." + id}
   * and  {@code "calendar.hijrah." + id + ".type"}
   * @param id the id of the calendar
   * @throws DateTimeException if the calendar type is missing from the properties file.
   * @throws IllegalArgumentException if the id is empty
   */
  private def this(id: String) {
    this()
    if (id.isEmpty) {
      throw new IllegalArgumentException("calendar id is empty")
    }
    val propName: String = PROP_PREFIX + id + PROP_TYPE_SUFFIX
    val calType: String = calendarProperties.getProperty(propName)
    if (calType == null || calType.isEmpty) {
      throw new DateTimeException("calendarType is missing or empty for: " + propName)
    }
    this.typeId = id
    this.calendarType = calType
  }

  /**
   * Check and ensure that the calendar data has been initialized.
   * The initialization check is performed at the boundary between
   * public and package methods.  If a public calls another public method
   * a check is not necessary in the caller.
   * The constructors of HijrahDate call {@link #getEpochDay} or
   * {@link #getHijrahDateInfo} so every call from HijrahDate to a
   * HijrahChronology via package private methods has been checked.
   *
   * @throws DateTimeException if the calendar data configuration is
   *                           malformed or IOExceptions occur loading the data
   */
  private def checkCalendarInit {
    if (initComplete == false) {
      loadCalendarData
      initComplete = true
    }
  }

  /**
   * Gets the ID of the chronology.
   * <p>
   * The ID uniquely identifies the {@code Chronology}. It can be used to
   * lookup the {@code Chronology} using {@link #of(String)}.
   *
   * @return the chronology ID, non-null
   * @see #getCalendarType()
   */
  def getId: String = {
     typeId
  }

  /**
   * Gets the calendar type of the Islamic calendar.
   * <p>
   * The calendar type is an identifier defined by the
   * <em>Unicode Locale Data Markup Language (LDML)</em> specification.
   * It can be used to lookup the {@code Chronology} using {@link #of(String)}.
   *
   * @return the calendar system type; non-null if the calendar has
   *         a standard type, otherwise null
   * @see #getId()
   */
  def getCalendarType: String = {
     calendarType
  }

  /**
   * Obtains a local date in Hijrah calendar system from the
   * era, year-of-era, month-of-year and day-of-month fields.
   *
   * @param era  the Hijrah era, not null
   * @param yearOfEra  the year-of-era
   * @param month  the month-of-year
   * @param dayOfMonth  the day-of-month
   * @return the Hijrah local date, not null
   * @throws DateTimeException if unable to create the date
   * @throws ClassCastException if the { @code era} is not a { @code HijrahEra}
   */
  override def date(era: Era, yearOfEra: Int, month: Int, dayOfMonth: Int): HijrahDate = {
     date(prolepticYear(era, yearOfEra), month, dayOfMonth)
  }

  /**
   * Obtains a local date in Hijrah calendar system from the
   * proleptic-year, month-of-year and day-of-month fields.
   *
   * @param prolepticYear  the proleptic-year
   * @param month  the month-of-year
   * @param dayOfMonth  the day-of-month
   * @return the Hijrah local date, not null
   * @throws DateTimeException if unable to create the date
   */
  def date(prolepticYear: Int, month: Int, dayOfMonth: Int): HijrahDate = {
     HijrahDate.of(this, prolepticYear, month, dayOfMonth)
  }

  /**
   * Obtains a local date in Hijrah calendar system from the
   * era, year-of-era and day-of-year fields.
   *
   * @param era  the Hijrah era, not null
   * @param yearOfEra  the year-of-era
   * @param dayOfYear  the day-of-year
   * @return the Hijrah local date, not null
   * @throws DateTimeException if unable to create the date
   * @throws ClassCastException if the { @code era} is not a { @code HijrahEra}
   */
  override def dateYearDay(era: Era, yearOfEra: Int, dayOfYear: Int): HijrahDate = {
     dateYearDay(prolepticYear(era, yearOfEra), dayOfYear)
  }

  /**
   * Obtains a local date in Hijrah calendar system from the
   * proleptic-year and day-of-year fields.
   *
   * @param prolepticYear  the proleptic-year
   * @param dayOfYear  the day-of-year
   * @return the Hijrah local date, not null
   * @throws DateTimeException if the value of the year is out of range,
   *                           or if the day-of-year is invalid for the year
   */
  def dateYearDay(prolepticYear: Int, dayOfYear: Int): HijrahDate = {
    val date: HijrahDate = HijrahDate.of(this, prolepticYear, 1, 1)
    if (dayOfYear > date.lengthOfYear) {
      throw new DateTimeException("Invalid dayOfYear: " + dayOfYear)
    }
     date.plusDays(dayOfYear - 1)
  }

  /**
   * Obtains a local date in the Hijrah calendar system from the epoch-day.
   *
   * @param epochDay  the epoch day
   * @return the Hijrah local date, not null
   * @throws DateTimeException if unable to create the date
   */
  def dateEpochDay(epochDay: Long): HijrahDate = {
     HijrahDate.ofEpochDay(this, epochDay)
  }

  override def dateNow: HijrahDate = {
     dateNow(Clock.systemDefaultZone)
  }

  override def dateNow(zone: ZoneId): HijrahDate = {
     dateNow(Clock.system(zone))
  }

  override def dateNow(clock: Clock): HijrahDate = {
     date(LocalDate.now(clock))
  }

  def date(temporal: TemporalAccessor): HijrahDate = {
    if (temporal.isInstanceOf[HijrahDate]) {
       temporal.asInstanceOf[HijrahDate]
    }
     HijrahDate.ofEpochDay(this, temporal.getLong(EPOCH_DAY))
  }

  override def localDateTime(temporal: TemporalAccessor): ChronoLocalDateTime[HijrahDate] = {
     super.localDateTime(temporal).asInstanceOf[ChronoLocalDateTime[HijrahDate]]
  }

  override def zonedDateTime(temporal: TemporalAccessor): ChronoZonedDateTime[HijrahDate] = {
     super.zonedDateTime(temporal).asInstanceOf[ChronoZonedDateTime[HijrahDate]]
  }

  override def zonedDateTime(instant: Instant, zone: ZoneId): ChronoZonedDateTime[HijrahDate] = {
     super.zonedDateTime(instant, zone).asInstanceOf[ChronoZonedDateTime[HijrahDate]]
  }

  def isLeapYear(prolepticYear: Long): Boolean = {
    checkCalendarInit
    val epochMonth: Int = yearToEpochMonth(prolepticYear.asInstanceOf[Int])
    if (epochMonth < 0 || epochMonth > maxEpochDay) {
      throw new DateTimeException("Hijrah date out of range")
    }
    val len: Int = getYearLength(prolepticYear.asInstanceOf[Int])
     (len > 354)
  }

  def prolepticYear(era: Era, yearOfEra: Int): Int = {
    if (era.isInstanceOf[HijrahEra] == false) {
      throw new ClassCastException("Era must be HijrahEra")
    }
     yearOfEra
  }

  def eraOf(eraValue: Int): HijrahEra = {
    eraValue match {
      case 1 =>
         HijrahEra.AH
      case _ =>
        throw new DateTimeException("invalid Hijrah era")
    }
  }

  def eras: List[Era] = {
     Arrays.asList[Era](HijrahEra.values)
  }

  def range(field: ChronoField): ValueRange = {
    checkCalendarInit
    if (field.isInstanceOf[ChronoField]) {
      val f: ChronoField = field
      f match {
        case DAY_OF_MONTH =>
           ValueRange.of(1, 1, getMinimumMonthLength, getMaximumMonthLength)
        case DAY_OF_YEAR =>
           ValueRange.of(1, getMaximumDayOfYear)
        case ALIGNED_WEEK_OF_MONTH =>
           ValueRange.of(1, 5)
        case YEAR =>
        case YEAR_OF_ERA =>
           ValueRange.of(getMinimumYear, getMaximumYear)
        case ERA =>
           ValueRange.of(1, 1)
        case _ =>
           field.range
      }
    }
     field.range
  }

  def resolveDate(fieldValues: Map[TemporalField, Long], resolverStyle: ResolverStyle): HijrahDate = {
     super.resolveDate(fieldValues, resolverStyle).asInstanceOf[HijrahDate]
  }

  /**
   * Check the validity of a year.
   *
   * @param prolepticYear the year to check
   */
  private[chrono] def checkValidYear(prolepticYear: Long): Int = {
    if (prolepticYear < getMinimumYear || prolepticYear > getMaximumYear) {
      throw new DateTimeException("Invalid Hijrah year: " + prolepticYear)
    }
     prolepticYear.asInstanceOf[Int]
  }

  private[chrono] def checkValidDayOfYear(dayOfYear: Int) {
    if (dayOfYear < 1 || dayOfYear > getMaximumDayOfYear) {
      throw new DateTimeException("Invalid Hijrah day of year: " + dayOfYear)
    }
  }

  private[chrono] def checkValidMonth(month: Int) {
    if (month < 1 || month > 12) {
      throw new DateTimeException("Invalid Hijrah month: " + month)
    }
  }

  /**
   * Returns an array containing the Hijrah year, month and day
   * computed from the epoch day.
   *
   * @param epochDay  the EpochDay
   * @return int[0] = YEAR, int[1] = MONTH, int[2] = DATE
   */
  private[chrono] def getHijrahDateInfo(epochDay: Int): Array[Int] = {
    checkCalendarInit
    if (epochDay < minEpochDay || epochDay >= maxEpochDay) {
      throw new DateTimeException("Hijrah date out of range")
    }
    val epochMonth: Int = epochDayToEpochMonth(epochDay)
    val year: Int = epochMonthToYear(epochMonth)
    val month: Int = epochMonthToMonth(epochMonth)
    val day1: Int = epochMonthToEpochDay(epochMonth)
    val date: Int = epochDay - day1
    val dateInfo: Int = new Array[Int](3)
    dateInfo(0) = year
    dateInfo(1) = month + 1
    dateInfo(2) = date + 1
     dateInfo
  }

  /**
   * Return the epoch day computed from Hijrah year, month, and day.
   *
   * @param prolepticYear the year to represent, 0-origin
   * @param monthOfYear the month-of-year to represent, 1-origin
   * @param dayOfMonth the day-of-month to represent, 1-origin
   * @return the epoch day
   */
  private[chrono] def getEpochDay(prolepticYear: Int, monthOfYear: Int, dayOfMonth: Int): Long = {
    checkCalendarInit
    checkValidMonth(monthOfYear)
    val epochMonth: Int = yearToEpochMonth(prolepticYear) + (monthOfYear - 1)
    if (epochMonth < 0 || epochMonth >= hijrahEpochMonthStartDays.length) {
      throw new DateTimeException("Invalid Hijrah date, year: " + prolepticYear + ", month: " + monthOfYear)
    }
    if (dayOfMonth < 1 || dayOfMonth > getMonthLength(prolepticYear, monthOfYear)) {
      throw new DateTimeException("Invalid Hijrah day of month: " + dayOfMonth)
    }
     epochMonthToEpochDay(epochMonth) + (dayOfMonth - 1)
  }

  /**
   * Returns day of year for the year and month.
   *
   * @param prolepticYear a proleptic year
   * @param month a month, 1-origin
   * @return the day of year, 1-origin
   */
  private[chrono] def getDayOfYear(prolepticYear: Int, month: Int): Int = {
     yearMonthToDayOfYear(prolepticYear, (month - 1))
  }

  /**
   * Returns month length for the year and month.
   *
   * @param prolepticYear a proleptic year
   * @param monthOfYear a month, 1-origin.
   * @return the length of the month
   */
  private[chrono] def getMonthLength(prolepticYear: Int, monthOfYear: Int): Int = {
    val epochMonth: Int = yearToEpochMonth(prolepticYear) + (monthOfYear - 1)
    if (epochMonth < 0 || epochMonth >= hijrahEpochMonthStartDays.length) {
      throw new DateTimeException("Invalid Hijrah date, year: " + prolepticYear + ", month: " + monthOfYear)
    }
     epochMonthLength(epochMonth)
  }

  /**
   * Returns year length.
   * Note: The 12th month must exist in the data.
   *
   * @param prolepticYear a proleptic year
   * @return year length in days
   */
  private[chrono] def getYearLength(prolepticYear: Int): Int = {
     yearMonthToDayOfYear(prolepticYear, 12)
  }

  /**
   * Return the minimum supported Hijrah year.
   *
   * @return the minimum
   */
  private[chrono] def getMinimumYear: Int = {
     epochMonthToYear(0)
  }

  /**
   * Return the maximum supported Hijrah ear.
   *
   * @return the minimum
   */
  private[chrono] def getMaximumYear: Int = {
     epochMonthToYear(hijrahEpochMonthStartDays.length - 1) - 1
  }

  /**
   * Returns maximum day-of-month.
   *
   * @return maximum day-of-month
   */
  private[chrono] def getMaximumMonthLength: Int = {
     maxMonthLength
  }

  /**
   * Returns smallest maximum day-of-month.
   *
   * @return smallest maximum day-of-month
   */
  private[chrono] def getMinimumMonthLength: Int = {
     minMonthLength
  }

  /**
   * Returns maximum day-of-year.
   *
   * @return maximum day-of-year
   */
  private[chrono] def getMaximumDayOfYear: Int = {
     maxYearLength
  }

  /**
   * Returns smallest maximum day-of-year.
   *
   * @return smallest maximum day-of-year
   */
  private[chrono] def getSmallestMaximumDayOfYear: Int = {
     minYearLength
  }

  /**
   * Returns the epochMonth found by locating the epochDay in the table. The
   * epochMonth is the index in the table
   *
   * @param epochDay
   * @return The index of the element of the start of the month containing the
   *         epochDay.
   */
  private def epochDayToEpochMonth(epochDay: Int): Int = {
    var ndx: Int = Arrays.binarySearch(hijrahEpochMonthStartDays, epochDay)
    if (ndx < 0) {
      ndx = -ndx - 2
    }
     ndx
  }

  /**
   * Returns the year computed from the epochMonth
   *
   * @param epochMonth the epochMonth
   * @return the Hijrah Year
   */
  private def epochMonthToYear(epochMonth: Int): Int = {
     (epochMonth + hijrahStartEpochMonth) / 12
  }

  /**
   * Returns the epochMonth for the Hijrah Year.
   *
   * @param year the HijrahYear
   * @return the epochMonth for the beginning of the year.
   */
  private def yearToEpochMonth(year: Int): Int = {
     (year * 12) - hijrahStartEpochMonth
  }

  /**
   * Returns the Hijrah month from the epochMonth.
   *
   * @param epochMonth the epochMonth
   * @return the month of the Hijrah Year
   */
  private def epochMonthToMonth(epochMonth: Int): Int = {
     (epochMonth + hijrahStartEpochMonth) % 12
  }

  /**
   * Returns the epochDay for the start of the epochMonth.
   *
   * @param epochMonth the epochMonth
   * @return the epochDay for the start of the epochMonth.
   */
  private def epochMonthToEpochDay(epochMonth: Int): Int = {
     hijrahEpochMonthStartDays(epochMonth)
  }

  /**
   * Returns the day of year for the requested HijrahYear and month.
   *
   * @param prolepticYear the Hijrah year
   * @param month the Hijrah month
   * @return the day of year for the start of the month of the year
   */
  private def yearMonthToDayOfYear(prolepticYear: Int, month: Int): Int = {
    val epochMonthFirst: Int = yearToEpochMonth(prolepticYear)
     epochMonthToEpochDay(epochMonthFirst + month) - epochMonthToEpochDay(epochMonthFirst)
  }

  /**
   * Returns the length of the epochMonth. It is computed from the start of
   * the following month minus the start of the requested month.
   *
   * @param epochMonth the epochMonth; assumed to be within range
   * @return the length in days of the epochMonth
   */
  private def epochMonthLength(epochMonth: Int): Int = {
     hijrahEpochMonthStartDays(epochMonth + 1) - hijrahEpochMonthStartDays(epochMonth)
  }

  /**
   * Loads and processes the Hijrah calendar properties file for this calendarType.
   * The starting Hijrah date and the corresponding ISO date are
   * extracted and used to calculate the epochDate offset.
   * The version number is identified and ignored.
   * Everything else is the data for a year with containing the length of each
   * of 12 months.
   *
   * @throws DateTimeException if initialization of the calendar data from the
   *                           resource fails
   */
  private def loadCalendarData {
    try {
      val resourceName: String = calendarProperties.getProperty(PROP_PREFIX + typeId)

      val props: Properties = readConfigProperties(resourceName)
      val years: Map[Integer, Array[Int]] = new HashMap[Integer, Array[Int]]
      var minYear: Int = Integer.MAX_VALUE
      var maxYear: Int = Integer.MIN_VALUE
      var id: String = null
      var `type`: String = null
      var version: String = null
      var isoStart: Int = 0
      import scala.collection.JavaConversions._
      for (entry <- props.entrySet) {
        val key: String = entry.getKey.asInstanceOf[String]
        key match {
          case KEY_ID =>
            id = entry.getValue.asInstanceOf[String]
            break //todo: break is not supported
          case KEY_TYPE =>
            `type` = entry.getValue.asInstanceOf[String]
            break //todo: break is not supported
          case KEY_VERSION =>
            version = entry.getValue.asInstanceOf[String]
            break //todo: break is not supported
          case KEY_ISO_START => {
            val ymd: Array[Int] = parseYMD(entry.getValue.asInstanceOf[String])
            isoStart = LocalDate.of(ymd(0), ymd(1), ymd(2)).toEpochDay.asInstanceOf[Int]
            break //todo: break is not supported
          }
          case _ =>
            try {
              val year: Int = Integer.valueOf(key)
              val months: Array[Int] = parseMonths(entry.getValue.asInstanceOf[String])
              years.put(year, months)
              maxYear = Math.max(maxYear, year)
              minYear = Math.min(minYear, year)
            }
            catch {
              case nfe: NumberFormatException => {
                throw new IllegalArgumentException("bad key: " + key)
              }
            }
        }
      }
      if (!(getId == id)) {
        throw new IllegalArgumentException("Configuration is for a different calendar: " + id)
      }
      if (!(getCalendarType == `type`)) {
        throw new IllegalArgumentException("Configuration is for a different calendar type: " + `type`)
      }
      if (version == null || version.isEmpty) {
        throw new IllegalArgumentException("Configuration does not contain a version")
      }
      if (isoStart == 0) {
        throw new IllegalArgumentException("Configuration does not contain a ISO start date")
      }
      hijrahStartEpochMonth = minYear * 12
      minEpochDay = isoStart
      hijrahEpochMonthStartDays = createEpochMonths(minEpochDay, minYear, maxYear, years)
      maxEpochDay = hijrahEpochMonthStartDays(hijrahEpochMonthStartDays.length - 1)
      {
        var year: Int = minYear
        while (year < maxYear) {
          {
            val length: Int = getYearLength(year)
            minYearLength = Math.min(minYearLength, length)
            maxYearLength = Math.max(maxYearLength, length)
          }
          ({
            year += 1; year - 1
          })
        }
      }
    }
    catch {
      case ex: Exception => {
        val logger: PlatformLogger = PlatformLogger.getLogger("java.time.chrono")
        logger.severe("Unable to initialize Hijrah calendar proxy: " + typeId, ex)
        throw new DateTimeException("Unable to initialize HijrahCalendar: " + typeId, ex)
      }
    }
  }

  /**
   * Converts the map of year to month lengths ranging from minYear to maxYear
   * into a linear contiguous array of epochDays. The index is the hijrahMonth
   * computed from year and month and offset by minYear. The value of each
   * entry is the epochDay corresponding to the first day of the month.
   *
   * @param minYear The minimum year for which data is provided
   * @param maxYear The maximum year for which data is provided
   * @param years a Map of year to the array of 12 month lengths
   * @return array of epochDays for each month from min to max
   */
  private def createEpochMonths(epochDay: Int, minYear: Int, maxYear: Int, years: Map[Integer, Array[Int]]): Array[Int] = {
    val numMonths: Int = (maxYear - minYear + 1) * 12 + 1
    val epochMonth: Int = 0
    val epochMonths: Array[Int] = new Array[Int](numMonths)
    minMonthLength = Integer.MAX_VALUE
    maxMonthLength = Integer.MIN_VALUE
    {
      var year: Int = minYear
      while (year <= maxYear) {
        {
          val months: Array[Int] = years.get(year)
          {
            var month: Int = 0
            while (month < 12) {
              {
                val length: Int = months(month)
                epochMonths(({
                  epochMonth += 1; epochMonth - 1
                })) = epochDay
                if (length < 29 || length > 32) {
                  throw new IllegalArgumentException("Invalid month length in year: " + minYear)
                }
                epochDay += length
                minMonthLength = Math.min(minMonthLength, length)
                maxMonthLength = Math.max(maxMonthLength, length)
              }
              ({
                month += 1; month - 1
              })
            }
          }
        }
        ({
          year += 1; year - 1
        })
      }
    }
    epochMonths(({
      epochMonth += 1; epochMonth - 1
    })) = epochDay
    if (epochMonth != epochMonths.length) {
      throw new IllegalStateException("Did not fill epochMonths exactly: ndx = " + epochMonth + " should be " + epochMonths.length)
    }
     epochMonths
  }

  /**
   * Parses the 12 months lengths from a property value for a specific year.
   *
   * @param line the value of a year property
   * @return an array of int[12] containing the 12 month lengths
   * @throws IllegalArgumentException if the number of months is not 12
   * @throws NumberFormatException if the 12 tokens are not numbers
   */
  private def parseMonths(line: String): Array[Int] = {
    val months: Array[Int] = new Array[Int](12)
    val numbers: Array[String] = line.split("\\s")
    if (numbers.length != 12) {
      throw new IllegalArgumentException("wrong number of months on line: " + Arrays.toString(numbers) + "; count: " + numbers.length)
    }
    {
      var i: Int = 0
      while (i < 12) {
        {
          try {
            months(i) = Integer.valueOf(numbers(i))
          }
          catch {
            case nfe: NumberFormatException => {
              throw new IllegalArgumentException("bad key: " + numbers(i))
            }
          }
        }
        ({
          i += 1; i - 1
        })
      }
    }
     months
  }

  /**
   * Parse yyyy-MM-dd into a 3 element array [yyyy, mm, dd].
   *
   * @param string the input string
   * @return the 3 element array with year, month, day
   */
  private def parseYMD(string: String): Array[Int] = {
    string = string.trim
    try {
      if (string.charAt(4) != '-' || string.charAt(7) != '-') {
        throw new IllegalArgumentException("date must be yyyy-MM-dd")
      }
      val ymd: Array[Int] = new Array[Int](3)
      ymd(0) = Integer.valueOf(string.substring(0, 4))
      ymd(1) = Integer.valueOf(string.substring(5, 7))
      ymd(2) = Integer.valueOf(string.substring(8, 10))
       ymd
    }
    catch {
      case ex: NumberFormatException => {
        throw new IllegalArgumentException("date must be yyyy-MM-dd", ex)
      }
    }
  }

  /**
   * The Hijrah Calendar id.
   */
  private final val typeId: String = null
  /**
   * The Hijrah calendarType.
   */
  @transient
  private final val calendarType: String = null
  /**
   * Flag to indicate the initialization of configuration data is complete.
   * @see #checkCalendarInit()
   */
  @volatile
  private var initComplete: Boolean = false
  /**
   * Array of epoch days indexed by Hijrah Epoch month.
   * Computed by {@link #loadCalendarData}.
   */
  @transient
  private var hijrahEpochMonthStartDays: Array[Int] = null
  /**
   * The minimum epoch day of this Hijrah calendar.
   * Computed by {@link #loadCalendarData}.
   */
  @transient
  private var minEpochDay: Int = 0
  /**
   * The maximum epoch day for which calendar data is available.
   * Computed by {@link #loadCalendarData}.
   */
  @transient
  private var maxEpochDay: Int = 0
  /**
   * The minimum epoch month.
   * Computed by {@link #loadCalendarData}.
   */
  @transient
  private var hijrahStartEpochMonth: Int = 0
  /**
   * The minimum length of a month.
   * Computed by {@link #createEpochMonths}.
   */
  @transient
  private var minMonthLength: Int = 0
  /**
   * The maximum length of a month.
   * Computed by {@link #createEpochMonths}.
   */
  @transient
  private var maxMonthLength: Int = 0
  /**
   * The minimum length of a year in days.
   * Computed by {@link #createEpochMonths}.
   */
  @transient
  private var minYearLength: Int = 0
  /**
   * The maximum length of a year in days.
   * Computed by {@link #createEpochMonths}.
   */
  @transient
  private var maxYearLength: Int = 0
}

/**
 * A date in the Hijrah calendar system.
 * <p>
 * This date operates using one of several variants of the
 * {@linkplain HijrahChronology Hijrah calendar}.
 * <p>
 * The Hijrah calendar has a different total of days in a year than
 * Gregorian calendar, and the length of each month is based on the period
 * of a complete revolution of the moon around the earth
 * (as between successive new moons).
 * Refer to the {@link HijrahChronology} for details of supported variants.
 * <p>
 * Each HijrahDate is created bound to a particular HijrahChronology,
 * The same chronology is propagated to each HijrahDate computed from the date.
 * To use a different Hijrah variant, its HijrahChronology can be used
 * to create new HijrahDate instances.
 * Alternatively, the {@link #withVariant} method can be used to convert
 * to a new HijrahChronology.
 * @implSpec
 * This class is immutable and thread-safe.
 *
 * @since 1.8
 */
object HijrahDate {
  /**
   * Obtains an instance of {@code HijrahDate} from the Hijrah proleptic year,
   * month-of-year and day-of-month.
   *
   * @param prolepticYear  the proleptic year to represent in the Hijrah calendar
   * @param monthOfYear  the month-of-year to represent, from 1 to 12
   * @param dayOfMonth  the day-of-month to represent, from 1 to 30
   * @return the Hijrah date, never null
   * @throws DateTimeException if the value of any field is out of range
   */
  private[chrono] def of(chrono: HijrahChronology, prolepticYear: Int, monthOfYear: Int, dayOfMonth: Int): HijrahDate = {
     new HijrahDate(chrono, prolepticYear, monthOfYear, dayOfMonth)
  }

  /**
   * Returns a HijrahDate for the chronology and epochDay.
   * @param chrono The Hijrah chronology
   * @param epochDay the epoch day
   * @return a HijrahDate for the epoch day; non-null
   */
  private[chrono] def ofEpochDay(chrono: HijrahChronology, epochDay: Long): HijrahDate = {
     new HijrahDate(chrono, epochDay)
  }

  /**
   * Obtains the current {@code HijrahDate} of the Islamic Umm Al-Qura calendar
   * in the default time-zone.
   * <p>
   * This will query the {@link Clock#systemDefaultZone() system clock} in the default
   * time-zone to obtain the current date.
   * <p>
   * Using this method will prevent the ability to use an alternate clock for testing
   * because the clock is hard-coded.
   *
   * @return the current date using the system clock and default time-zone, not null
   */
  def now: HijrahDate = {
     now(Clock.systemDefaultZone)
  }

  /**
   * Obtains the current {@code HijrahDate} of the Islamic Umm Al-Qura calendar
   * in the specified time-zone.
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
  def now(zone: ZoneId): HijrahDate = {
     now(Clock.system(zone))
  }

  /**
   * Obtains the current {@code HijrahDate} of the Islamic Umm Al-Qura calendar
   * from the specified clock.
   * <p>
   * This will query the specified clock to obtain the current date - today.
   * Using this method allows the use of an alternate clock for testing.
   * The alternate clock may be introduced using {@linkplain Clock dependency injection}.
   *
   * @param clock  the clock to use, not null
   * @return the current date, not null
   * @throws DateTimeException if the current date cannot be obtained
   */
  def now(clock: Clock): HijrahDate = {
     HijrahDate.ofEpochDay(HijrahChronology.INSTANCE, LocalDate.now(clock).toEpochDay)
  }

  /**
   * Obtains a {@code HijrahDate} of the Islamic Umm Al-Qura calendar
   * from the proleptic-year, month-of-year and day-of-month fields.
   * <p>
   * This returns a {@code HijrahDate} with the specified fields.
   * The day must be valid for the year and month, otherwise an exception will be thrown.
   *
   * @param prolepticYear  the Hijrah proleptic-year
   * @param month  the Hijrah month-of-year, from 1 to 12
   * @param dayOfMonth  the Hijrah day-of-month, from 1 to 30
   * @return the date in Hijrah calendar system, not null
   * @throws DateTimeException if the value of any field is out of range,
   *                           or if the day-of-month is invalid for the month-year
   */
  def of(prolepticYear: Int, month: Int, dayOfMonth: Int): HijrahDate = {
     HijrahChronology.INSTANCE.date(prolepticYear, month, dayOfMonth)
  }

  /**
   * Obtains a {@code HijrahDate} of the Islamic Umm Al-Qura calendar from a temporal object.
   * <p>
   * This obtains a date in the Hijrah calendar system based on the specified temporal.
   * A {@code TemporalAccessor} represents an arbitrary set of date and time information,
   * which this factory converts to an instance of {@code HijrahDate}.
   * <p>
   * The conversion typically uses the {@link ChronoField#EPOCH_DAY EPOCH_DAY}
   * field, which is standardized across calendar systems.
   * <p>
   * This method matches the signature of the functional interface {@link TemporalQuery}
   * allowing it to be used as a query via method reference, {@code HijrahDate::from}.
   *
   * @param temporal  the temporal object to convert, not null
   * @return the date in Hijrah calendar system, not null
   * @throws DateTimeException if unable to convert to a { @code HijrahDate}
   */
  def from(temporal: TemporalAccessor): HijrahDate = {
     HijrahChronology.INSTANCE.date(temporal)
  }

  private[chrono] def readExternal(in: ObjectInput): HijrahDate = {
    val chrono: HijrahChronology = in.readObject.asInstanceOf[HijrahChronology]
    val year: Int = in.readInt
    val month: Int = in.readByte
    val dayOfMonth: Int = in.readByte
     chrono.date(year, month, dayOfMonth)
  }


}

final class HijrahDate extends ChronoLocalDateImpl[HijrahDate] with ChronoLocalDate  {
  /**
   * Constructs an {@code HijrahDate} with the proleptic-year, month-of-year and
   * day-of-month fields.
   *
   * @param chrono The chronology to create the date with
   * @param prolepticYear the proleptic year
   * @param monthOfYear the month of year
   * @param dayOfMonth the day of month
   */
  private def this(chrono: HijrahChronology, prolepticYear: Int, monthOfYear: Int, dayOfMonth: Int) {
    this()
    chrono.getEpochDay(prolepticYear, monthOfYear, dayOfMonth)
    this.chrono = chrono
    this.prolepticYear = prolepticYear
    this.monthOfYear = monthOfYear
    this.dayOfMonth = dayOfMonth
  }

  /**
   * Constructs an instance with the Epoch Day.
   *
   * @param epochDay  the epochDay
   */
  private def this(chrono: HijrahChronology, epochDay: Long) {
    this()
    val dateInfo: Array[Int] = chrono.getHijrahDateInfo(epochDay.asInstanceOf[Int])
    this.chrono = chrono
    this.prolepticYear = dateInfo(0)
    this.monthOfYear = dateInfo(1)
    this.dayOfMonth = dateInfo(2)
  }

  /**
   * Gets the chronology of this date, which is the Hijrah calendar system.
   * <p>
   * The {@code Chronology} represents the calendar system in use.
   * The era and other fields in {@link ChronoField} are defined by the chronology.
   *
   * @return the Hijrah chronology, not null
   */
  def getChronology: HijrahChronology = {
     chrono
  }

  /**
   * Gets the era applicable at this date.
   * <p>
   * The Hijrah calendar system has one era, 'AH',
   * defined by {@link HijrahEra}.
   *
   * @return the era applicable at this date, not null
   */
  override def getEra: HijrahEra = {
     HijrahEra.AH
  }

  /**
   * Returns the length of the month represented by this date.
   * <p>
   * This returns the length of the month in days.
   * Month lengths in the Hijrah calendar system vary between 29 and 30 days.
   *
   * @return the length of the month in days
   */
  def lengthOfMonth: Int = {
     chrono.getMonthLength(prolepticYear, monthOfYear)
  }

  /**
   * Returns the length of the year represented by this date.
   * <p>
   * This returns the length of the year in days.
   * A Hijrah calendar system year is typically shorter than
   * that of the ISO calendar system.
   *
   * @return the length of the year in days
   */
  override def lengthOfYear: Int = {
     chrono.getYearLength(prolepticYear)
  }

  override def range(field: TemporalField): ValueRange = {
    if (field.isInstanceOf[ChronoField]) {
      if (isSupported(field)) {
        val f: ChronoField = field.asInstanceOf[ChronoField]
        f match {
          case DAY_OF_MONTH =>
             ValueRange.of(1, lengthOfMonth)
          case DAY_OF_YEAR =>
             ValueRange.of(1, lengthOfYear)
          case ALIGNED_WEEK_OF_MONTH =>
             ValueRange.of(1, 5)
        }
         getChronology.range(f)
      }
      throw new UnsupportedTemporalTypeException("Unsupported field: " + field)
    }
     field.rangeRefinedBy(this)
  }

  def getLong(field: TemporalField): Long = {
    if (field.isInstanceOf[ChronoField]) {
      field.asInstanceOf[ChronoField] match {
        case DAY_OF_WEEK =>
           getDayOfWeek
        case ALIGNED_DAY_OF_WEEK_IN_MONTH =>
           ((getDayOfWeek - 1) % 7) + 1
        case ALIGNED_DAY_OF_WEEK_IN_YEAR =>
           ((getDayOfYear - 1) % 7) + 1
        case DAY_OF_MONTH =>
           this.dayOfMonth
        case DAY_OF_YEAR =>
           this.getDayOfYear
        case EPOCH_DAY =>
           toEpochDay
        case ALIGNED_WEEK_OF_MONTH =>
           ((dayOfMonth - 1) / 7) + 1
        case ALIGNED_WEEK_OF_YEAR =>
           ((getDayOfYear - 1) / 7) + 1
        case MONTH_OF_YEAR =>
           monthOfYear
        case PROLEPTIC_MONTH =>
           getProlepticMonth
        case YEAR_OF_ERA =>
           prolepticYear
        case YEAR =>
           prolepticYear
        case ERA =>
           getEraValue
      }
      throw new UnsupportedTemporalTypeException("Unsupported field: " + field)
    }
     field.getFrom(this)
  }

  private def getProlepticMonth: Long = {
     prolepticYear * 12L + monthOfYear - 1
  }

  override def `with`(field: TemporalField, newValue: Long): HijrahDate = {
    if (field.isInstanceOf[ChronoField]) {
      val f: ChronoField = field.asInstanceOf[ChronoField]
      chrono.range(f).checkValidValue(newValue, f)
      val nvalue: Int = newValue.asInstanceOf[Int]
      f match {
        case DAY_OF_WEEK =>
           plusDays(newValue - getDayOfWeek)
        case ALIGNED_DAY_OF_WEEK_IN_MONTH =>
           plusDays(newValue - getLong(ALIGNED_DAY_OF_WEEK_IN_MONTH))
        case ALIGNED_DAY_OF_WEEK_IN_YEAR =>
           plusDays(newValue - getLong(ALIGNED_DAY_OF_WEEK_IN_YEAR))
        case DAY_OF_MONTH =>
           resolvePreviousValid(prolepticYear, monthOfYear, nvalue)
        case DAY_OF_YEAR =>
           plusDays(Math.min(nvalue, lengthOfYear) - getDayOfYear)
        case EPOCH_DAY =>
           new HijrahDate(chrono, newValue)
        case ALIGNED_WEEK_OF_MONTH =>
           plusDays((newValue - getLong(ALIGNED_WEEK_OF_MONTH)) * 7)
        case ALIGNED_WEEK_OF_YEAR =>
           plusDays((newValue - getLong(ALIGNED_WEEK_OF_YEAR)) * 7)
        case MONTH_OF_YEAR =>
           resolvePreviousValid(prolepticYear, nvalue, dayOfMonth)
        case PROLEPTIC_MONTH =>
           plusMonths(newValue - getProlepticMonth)
        case YEAR_OF_ERA =>
           resolvePreviousValid(if (prolepticYear >= 1) nvalue else 1 - nvalue, monthOfYear, dayOfMonth)
        case YEAR =>
           resolvePreviousValid(nvalue, monthOfYear, dayOfMonth)
        case ERA =>
           resolvePreviousValid(1 - prolepticYear, monthOfYear, dayOfMonth)
      }
      throw new UnsupportedTemporalTypeException("Unsupported field: " + field)
    }
     super.`with`(field, newValue)
  }

  private def resolvePreviousValid(prolepticYear: Int, month: Int, day: Int): HijrahDate = {
    val monthDays: Int = chrono.getMonthLength(prolepticYear, month)
    if (day > monthDays) {
      day = monthDays
    }
     HijrahDate.of(chrono, prolepticYear, month, day)
  }

  /**
   * {@inheritDoc}
   * @throws DateTimeException if unable to make the adjustment.
   *                           For example, if the adjuster requires an ISO chronology
   * @throws ArithmeticException { @inheritDoc}
   */
  override def `with`(adjuster: TemporalAdjuster): HijrahDate = {
     super.`with`(adjuster)
  }

  /**
   * Returns a {@code HijrahDate} with the Chronology requested.
   * <p>
   * The year, month, and day are checked against the new requested
   * HijrahChronology.  If the chronology has a shorter month length
   * for the month, the day is reduced to be the last day of the month.
   *
   * @param chronology the new HijrahChonology, non-null
   * @return a HijrahDate with the requested HijrahChronology, non-null
   */
  def withVariant(chronology: HijrahChronology): HijrahDate = {
    if (chrono eq chronology) {
       this
    }
    val monthDays: Int = chronology.getDayOfYear(prolepticYear, monthOfYear)
     HijrahDate.of(chronology, prolepticYear, monthOfYear, if ((dayOfMonth > monthDays)) monthDays else dayOfMonth)
  }

  /**
   * {@inheritDoc}
   * @throws DateTimeException { @inheritDoc}
   * @throws ArithmeticException { @inheritDoc}
   */
  override def plus(amount: TemporalAmount): HijrahDate = {
     super.plus(amount)
  }

  /**
   * {@inheritDoc}
   * @throws DateTimeException { @inheritDoc}
   * @throws ArithmeticException { @inheritDoc}
   */
  override def minus(amount: TemporalAmount): HijrahDate = {
     super.minus(amount)
  }

  override def toEpochDay: Long = {
     chrono.getEpochDay(prolepticYear, monthOfYear, dayOfMonth)
  }

  /**
   * Gets the day-of-year field.
   * <p>
   * This method returns the primitive {@code int} value for the day-of-year.
   *
   * @return the day-of-year
   */
  private def getDayOfYear: Int = {
     chrono.getDayOfYear(prolepticYear, monthOfYear) + dayOfMonth
  }

  /**
   * Gets the day-of-week value.
   *
   * @return the day-of-week; computed from the epochday
   */
  private def getDayOfWeek: Int = {
    val dow0: Int = Math.floorMod(toEpochDay + 3, 7).asInstanceOf[Int]
     dow0 + 1
  }

  /**
   * Gets the Era of this date.
   *
   * @return the Era of this date; computed from epochDay
   */
  private def getEraValue: Int = {
     (if (prolepticYear > 1) 1 else 0)
  }

  /**
   * Checks if the year is a leap year, according to the Hijrah calendar system rules.
   *
   * @return true if this date is in a leap year
   */
  override def isLeapYear: Boolean = {
     chrono.isLeapYear(prolepticYear)
  }

  private[chrono] def plusYears(years: Long): HijrahDate = {
    if (years == 0) {
       this
    }
    val newYear: Int = Math.addExact(this.prolepticYear, years.asInstanceOf[Int])
     resolvePreviousValid(newYear, monthOfYear, dayOfMonth)
  }

  private[chrono] def plusMonths(monthsToAdd: Long): HijrahDate = {
    if (monthsToAdd == 0) {
       this
    }
    val monthCount: Long = prolepticYear * 12L + (monthOfYear - 1)
    val calcMonths: Long = monthCount + monthsToAdd
    val newYear: Int = chrono.checkValidYear(Math.floorDiv(calcMonths, 12L))
    val newMonth: Int = Math.floorMod(calcMonths, 12L).asInstanceOf[Int] + 1
     resolvePreviousValid(newYear, newMonth, dayOfMonth)
  }

  private[chrono] override def plusWeeks(weeksToAdd: Long): HijrahDate = {
     super.plusWeeks(weeksToAdd)
  }

  private[chrono] def plusDays(days: Long): HijrahDate = {
     new HijrahDate(chrono, toEpochDay + days)
  }

  override def plus(amountToAdd: Long, unit: TemporalUnit): HijrahDate = {
     super.plus(amountToAdd, unit)
  }

  override def minus(amountToSubtract: Long, unit: TemporalUnit): HijrahDate = {
     super.minus(amountToSubtract, unit)
  }

  private[chrono] override def minusYears(yearsToSubtract: Long): HijrahDate = {
     super.minusYears(yearsToSubtract)
  }

  private[chrono] override def minusMonths(monthsToSubtract: Long): HijrahDate = {
     super.minusMonths(monthsToSubtract)
  }

  private[chrono] override def minusWeeks(weeksToSubtract: Long): HijrahDate = {
     super.minusWeeks(weeksToSubtract)
  }

  private[chrono] override def minusDays(daysToSubtract: Long): HijrahDate = {
     super.minusDays(daysToSubtract)
  }

  final override def atTime(localTime: LocalTime): ChronoLocalDateTime[HijrahDate] = {
     super.atTime(localTime).asInstanceOf[ChronoLocalDateTime[HijrahDate]]
  }

  def until(endDate: ChronoLocalDate): ChronoPeriod = {
    val end: HijrahDate = getChronology.date(endDate)
    var totalMonths: Long = (end.prolepticYear - this.prolepticYear) * 12 + (end.monthOfYear - this.monthOfYear)
    var days: Int = end.dayOfMonth - this.dayOfMonth
    if (totalMonths > 0 && days < 0) {
      totalMonths -= 1
      val calcDate: HijrahDate = this.plusMonths(totalMonths)
      days = (end.toEpochDay - calcDate.toEpochDay).asInstanceOf[Int]
    }
    else if (totalMonths < 0 && days > 0) {
      totalMonths += 1
      days -= end.lengthOfMonth
    }
    val years: Long = totalMonths / 12
    val months: Int = (totalMonths % 12).asInstanceOf[Int]
     getChronology.period(Math.toIntExact(years), months, days)
  }

  private def writeReplace: AnyRef = {
     new Ser(Ser.HIJRAH_DATE_TYPE, this)
  }

  private[chrono] def writeExternal(out: ObjectOutput) {
    out.writeObject(chrono)
    out.writeInt(get(YEAR))
    out.writeByte(get(MONTH_OF_YEAR))
    out.writeByte(get(DAY_OF_MONTH))
  }

  /**
   * Replaces the date instance from the stream with a valid one.
   * ReadExternal has already read the fields and created a new instance
   * from the data.
   *
   * @return the resolved date, never null
   */
  private def readResolve: AnyRef = {
     this
  }

  /**
   * The Chronology of this HijrahDate.
   */
  private final val chrono: HijrahChronology = null
  /**
   * The proleptic year.
   */
  @transient
  private final val prolepticYear: Int = 0
  /**
   * The month-of-year.
   */
  @transient
  private final val monthOfYear: Int = 0
  /**
   * The day-of-month.
   */
  @transient
  private final val dayOfMonth: Int = 0
}

/**
 * An era in the Hijrah calendar system.
 * <p>
 * The Hijrah calendar system has only one era covering the
 * proleptic years greater than zero.
 * <p>
 * <b>Do not use {@code ordinal()} to obtain the numeric representation of {@code HijrahEra}.
 * Use {@code getValue()} instead.</b>
 *
 * @implSpec
 * This is an immutable and thread-safe enum.
 *
 * @since 1.8
 */
object HijrahEra {
  /**
   * Obtains an instance of {@code HijrahEra} from an {@code int} value.
   * <p>
   * The current era, which is the only accepted value, has the value 1
   *
   * @param hijrahEra  the era to represent, only 1 supported
   * @return the HijrahEra.AH singleton, not null
   * @throws DateTimeException if the value is invalid
   */
  def of(hijrahEra: Int): HijrahEra = {
    if (hijrahEra == 1) {
       AH
    }
    else {
      throw new DateTimeException("Invalid era: " + hijrahEra)
    }
  }

  private[chrono] def readExternal(in: DataInput): HijrahEra = {
    val eraValue: Byte = in.readByte
     HijrahEra.of(eraValue)
  }

  /**
   * The singleton instance for the current era, 'Anno Hegirae',
   * which has the numeric value 1.
   */
  final val AH: = null
}

final class HijrahEra extends Era {
  /**
   * Gets the numeric era {@code int} value.
   * <p>
   * The era AH has the value 1.
   *
   * @return the era value, 1 (AH)
   */
  def getValue: Int = {
     1
  }

  /**
   * Gets the range of valid values for the specified field.
   * <p>
   * The range object expresses the minimum and maximum valid values for a field.
   * This era is used to enhance the accuracy of the returned range.
   * If it is not possible to return the range, because the field is not supported
   * or for some other reason, an exception is thrown.
   * <p>
   * If the field is a {@link ChronoField} then the query is implemented here.
   * The {@code ERA} field returns the range.
   * All other {@code ChronoField} instances will throw an {@code UnsupportedTemporalTypeException}.
   * <p>
   * If the field is not a {@code ChronoField}, then the result of this method
   * is obtained by invoking {@code TemporalField.rangeRefinedBy(TemporalAccessor)}
   * passing {@code this} as the argument.
   * Whether the range can be obtained is determined by the field.
   * <p>
   * The {@code ERA} field returns a range for the one valid Hijrah era.
   *
   * @param field  the field to query the range for, not null
   * @return the range of valid values for the field, not null
   * @throws DateTimeException if the range for the field cannot be obtained
   * @throws UnsupportedTemporalTypeException if the unit is not supported
   */
  override def range(field: TemporalField): ValueRange = {
    if (field eq ERA) {
       ValueRange.of(1, 1)
    }
     Era.super.range(field)
  }

  private def writeReplace: AnyRef = {
     new Ser(Ser.HIJRAH_ERA_TYPE, this)
  }

  private[chrono] def writeExternal(out: DataOutput) {
    out.writeByte(this.getValue)
  }
}

