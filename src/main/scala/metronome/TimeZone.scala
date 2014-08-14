package metronome

import metronome.temporal.{TemporalAdjuster, TemporalAccessor}

/**
 * A time-zone ID, such as {@code Europe/Paris}.
 * <p>
 * A {@code ZoneId} is used to identify the rules used to convert between
 * an {@link Instant} and a {@link DateTime}.
 * There are two distinct types of ID:
 * <p><ul>
 * <li>Fixed offsets - a fully resolved offset from UTC/Greenwich, that uses
 * the same offset for all local date-times
 * <li>Geographical regions - an area where a specific set of rules for finding
 * the offset from UTC/Greenwich apply
 * </ul><p>
 * Most fixed offsets are represented by {@link ZoneOffset}.
 * Calling {@link #normalized()} on any {@code ZoneId} will ensure that a
 * fixed offset ID will be represented as a {@code ZoneOffset}.
 * <p>
 * The actual rules, describing when and how the offset changes, are defined by {@link ZoneRules}.
 * This class is simply an ID used to obtain the underlying rules.
 * This approach is taken because rules are defined by governments and change
 * frequently, whereas the ID is stable.
 * <p>
 * The distinction has other effects. Serializing the {@code ZoneId} will only send
 * the ID, whereas serializing the rules sends the entire data set.
 * Similarly, a comparison of two IDs only examines the ID, whereas
 * a comparison of two rules examines the entire data set.
 *
 * <h3>Time-zone IDs</h3>
 * The ID is unique within the system.
 * There are three types of ID.
 * <p>
 * The simplest type of ID is that from {@code ZoneOffset}.
 * This consists of 'Z' and IDs starting with '+' or '-'.
 * <p>
 * The next type of ID are offset-style IDs with some form of prefix,
 * such as 'GMT+2' or 'UTC+01:00'.
 * The recognised prefixes are 'UTC', 'GMT' and 'UT'.
 * The offset is the suffix and will be normalized during creation.
 * These IDs can be normalized to a {@code ZoneOffset} using {@code normalized()}.
 * <p>
 * The third type of ID are region-based IDs. A region-based ID must be of
 * two or more characters, and not start with 'UTC', 'GMT', 'UT' '+' or '-'.
 * Region-based IDs are defined by configuration, see {@link ZoneRulesProvider}.
 * The configuration focuses on providing the lookup from the ID to the
 * underlying {@code ZoneRules}.
 * <p>
 * Time-zone rules are defined by governments and change frequently.
 * There are a number of organizations, known here as groups, that monitor
 * time-zone changes and collate them.
 * The default group is the IANA Time Zone Database (TZDB).
 * Other organizations include IATA (the airline industry body) and Microsoft.
 * <p>
 * Each group defines its own format for the region ID it provides.
 * The TZDB group defines IDs such as 'Europe/London' or 'America/New_York'.
 * TZDB IDs take precedence over other groups.
 * <p>
 * It is strongly recommended that the group name is included in all IDs supplied by
 * groups other than TZDB to avoid conflicts. For example, IATA airline time-zone
 * region IDs are typically the same as the three letter airport code.
 * However, the airport of Utrecht has the code 'UTC', which is obviously a conflict.
 * The recommended format for region IDs from groups other than TZDB is 'group~region'.
 * Thus if IATA data were defined, Utrecht airport would be 'IATA~UTC'.
 *
 * <h3>Serialization</h3>
 * This class can be serialized and stores the string zone ID in the external form.
 * The {@code ZoneOffset} subclass uses a dedicated format that only stores the
 * offset from UTC/Greenwich.
 * <p>
 * A {@code ZoneId} can be deserialized in a Java Runtime where the ID is unknown.
 * For example, if a server-side Java Runtime has been updated with a new zone ID, but
 * the client-side Java Runtime has not been updated. In this case, the {@code ZoneId}
 * object will exist, and can be queried using {@code getId}, {@code equals},
 * {@code hashCode}, {@code toString}, {@code getDisplayName} and {@code normalized}.
 * However, any call to {@code getRules} will fail with {@code ZoneRulesException}.
 * This approach is designed to allow a {@link ZonedDateTime} to be loaded and
 * queried, but not modified, on a Java Runtime with incomplete time-zone information.
 *
 * @implSpec
 * This abstract class has two implementations, both of which are immutable and thread-safe.
 * One implementation models region-based IDs, the other is { @code ZoneOffset} modelling
 *                                                                  offset-based IDs. This difference is visible in serialization.
 *
 * @since 1.8
 */
object ZoneId {
  /**
   * Gets the system default time-zone.
   * <p>
   * This queries {@link TimeZone#getDefault()} to find the default time-zone
   * and converts it to a {@code ZoneId}. If the system default time-zone is changed,
   * then the result of this method will also change.
   *
   * @return the zone ID, not null
   * @throws DateTimeException if the converted zone ID has an invalid format
   * @throws ZoneRulesException if the converted zone region ID cannot be found
   */
  def systemDefault: ZoneId = {
     ZoneId.of(TimeZone.getDefault.getID, SHORT_IDS)
  }

  /**
   * Gets the set of available zone IDs.
   * <p>
   * This set includes the string form of all available region-based IDs.
   * Offset-based zone IDs are not included in the returned set.
   * The ID can be passed to {@link #of(String)} to create a {@code ZoneId}.
   * <p>
   * The set of zone IDs can increase over time, although in a typical application
   * the set of IDs is fixed. Each call to this method is thread-safe.
   *
   * @return a modifiable copy of the set of zone IDs, not null
   */
  def getAvailableZoneIds: Set[String] = {
     ZoneRulesProvider.getAvailableZoneIds
  }

  /**
   * Obtains an instance of {@code ZoneId} using its ID using a map
   * of aliases to supplement the standard zone IDs.
   * <p>
   * Many users of time-zones use short abbreviations, such as PST for
   * 'Pacific Standard Time' and PDT for 'Pacific Daylight Time'.
   * These abbreviations are not unique, and so cannot be used as IDs.
   * This method allows a map of string to time-zone to be setup and reused
   * within an application.
   *
   * @param zoneId  the time-zone ID, not null
   * @param aliasMap  a map of alias zone IDs (typically abbreviations) to real zone IDs, not null
   * @return the zone ID, not null
   * @throws DateTimeException if the zone ID has an invalid format
   * @throws ZoneRulesException if the zone ID is a region ID that cannot be found
   */
  def of(zoneId: String, aliasMap: Map[String, String]): ZoneId = {
    object
    object
    var id: String = aliasMap.get(zoneId)
    id = (if (id != null) id else zoneId)
     of(id)
  }

  /**
   * Obtains an instance of {@code ZoneId} from an ID ensuring that the
   * ID is valid and available for use.
   * <p>
   * This method parses the ID producing a {@code ZoneId} or {@code ZoneOffset}.
   * A {@code ZoneOffset} is returned if the ID is 'Z', or starts with '+' or '-'.
   * The result will always be a valid ID for which {@link ZoneRules} can be obtained.
   * <p>
   * Parsing matches the zone ID step by step as follows.
   * <ul>
   * <li>If the zone ID equals 'Z', the result is {@code ZoneOffset.UTC}.
   * <li>If the zone ID consists of a single letter, the zone ID is invalid
   * and {@code DateTimeException} is thrown.
   * <li>If the zone ID starts with '+' or '-', the ID is parsed as a
   * {@code ZoneOffset} using {@link ZoneOffset#of(String)}.
   * <li>If the zone ID equals 'GMT', 'UTC' or 'UT' then the result is a {@code ZoneId}
   * with the same ID and rules equivalent to {@code ZoneOffset.UTC}.
   * <li>If the zone ID starts with 'UTC+', 'UTC-', 'GMT+', 'GMT-', 'UT+' or 'UT-'
   * then the ID is a prefixed offset-based ID. The ID is split in two, with
   * a two or three letter prefix and a suffix starting with the sign.
   * The suffix is parsed as a {@link ZoneOffset#of(String) ZoneOffset}.
   * The result will be a {@code ZoneId} with the specified UTC/GMT/UT prefix
   * and the normalized offset ID as per {@link ZoneOffset#getId()}.
   * The rules of the returned {@code ZoneId} will be equivalent to the
   * parsed {@code ZoneOffset}.
   * <li>All other IDs are parsed as region-based zone IDs. Region IDs must
   * match the regular expression <code>[A-Za-z][A-Za-z0-9~/._+-]+</code>
   * otherwise a {@code DateTimeException} is thrown. If the zone ID is not
   * in the configured set of IDs, {@code ZoneRulesException} is thrown.
   * The detailed format of the region ID depends on the group supplying the data.
   * The default set of data is supplied by the IANA Time Zone Database (TZDB).
   * This has region IDs of the form '{area}/{city}', such as 'Europe/Paris' or 'America/New_York'.
   * This is compatible with most IDs from {@link java.util.TimeZone}.
   * </ul>
   *
   * @param zoneId  the time-zone ID, not null
   * @return the zone ID, not null
   * @throws DateTimeException if the zone ID has an invalid format
   * @throws ZoneRulesException if the zone ID is a region ID that cannot be found
   */
  def of(zoneId: String): ZoneId = {
     of(zoneId, true)
  }

  /**
   * Obtains an instance of {@code ZoneId} wrapping an offset.
   * <p>
   * If the prefix is "GMT", "UTC", or "UT" a {@code ZoneId}
   * with the prefix and the non-zero offset is returned.
   * If the prefix is empty {@code ""} the {@code ZoneOffset} is returned.
   *
   * @param prefix  the time-zone ID, not null
   * @param offset  the offset, not null
   * @return the zone ID, not null
   * @throws IllegalArgumentException if the prefix is not one of
   *                                  "GMT", "UTC", or "UT", or ""
   */
  def ofOffset(prefix: String, offset: ZoneOffset): ZoneId = {
    object
    object
    if (prefix.length == 0) {
       offset
    }
    if (!(prefix == "GMT") && !(prefix == "UTC") && !(prefix == "UT")) {
      throw new IllegalArgumentException("prefix should be GMT, UTC or UT, is: " + prefix)
    }
    if (offset.getTotalSeconds != 0) {
      prefix = prefix.concat(offset.getId)
    }
     new ZoneRegion(prefix, offset.getRules)
  }

  /**
   * Parses the ID, taking a flag to indicate whether {@code ZoneRulesException}
   * should be thrown or not, used in deserialization.
   *
   * @param zoneId  the time-zone ID, not null
   * @param checkAvailable  whether to check if the zone ID is available
   * @return the zone ID, not null
   * @throws DateTimeException if the ID format is invalid
   * @throws ZoneRulesException if checking availability and the ID cannot be found
   */
  private[time] def of(zoneId: String, checkAvailable: Boolean): ZoneId = {
    object
    if (zoneId.length <= 1 || zoneId.startsWith("+") || zoneId.startsWith("-")) {
       ZoneOffset.of(zoneId)
    }
    else if (zoneId.startsWith("UTC") || zoneId.startsWith("GMT")) {
       ofWithPrefix(zoneId, 3, checkAvailable)
    }
    else if (zoneId.startsWith("UT")) {
       ofWithPrefix(zoneId, 2, checkAvailable)
    }
     ZoneRegion.ofId(zoneId, checkAvailable)
  }

  /**
   * Parse once a prefix is established.
   *
   * @param zoneId  the time-zone ID, not null
   * @param prefixLength  the length of the prefix, 2 or 3
   * @return the zone ID, not null
   * @throws DateTimeException if the zone ID has an invalid format
   */
  private def ofWithPrefix(zoneId: String, prefixLength: Int, checkAvailable: Boolean): ZoneId = {
    val prefix: String = zoneId.substring(0, prefixLength)
    if (zoneId.length == prefixLength) {
       ofOffset(prefix, ZoneOffset.UTC)
    }
    if (zoneId.charAt(prefixLength) != '+' && zoneId.charAt(prefixLength) != '-') {
       ZoneRegion.ofId(zoneId, checkAvailable)
    }
    try {
      val offset: ZoneOffset = ZoneOffset.of(zoneId.substring(prefixLength))
      if (offset eq ZoneOffset.UTC) {
         ofOffset(prefix, offset)
      }
       ofOffset(prefix, offset)
    }
    catch {
      case ex: DateTimeException => {
        throw new DateTimeException("Invalid ID for offset-based ZoneId: " + zoneId, ex)
      }
    }
  }

  /**
   * Obtains an instance of {@code ZoneId} from a temporal object.
   * <p>
   * This obtains a zone based on the specified temporal.
   * A {@code TemporalAccessor} represents an arbitrary set of date and time information,
   * which this factory converts to an instance of {@code ZoneId}.
   * <p>
   * A {@code TemporalAccessor} represents some form of date and time information.
   * This factory converts the arbitrary temporal object to an instance of {@code ZoneId}.
   * <p>
   * The conversion will try to obtain the zone in a way that favours region-based
   * zones over offset-based zones using {@link TemporalQuery#zone()}.
   * <p>
   * This method matches the signature of the functional interface {@link TemporalQuery}
   * allowing it to be used in queries via method reference, {@code ZoneId::from}.
   *
   * @param temporal  the temporal object to convert, not null
   * @return the zone ID, not null
   * @throws DateTimeException if unable to convert to a { @code ZoneId}
   */
  def from(temporal: TemporalAccessor): ZoneId = {
    val obj: ZoneId = temporal.query(TemporalQuery.zone)
    if (obj == null) {
      throw new DateTimeException("Unable to obtain ZoneId from TemporalAccessor: " + temporal.getClass)
    }
     obj
  }

  /**
   * A map of zone overrides to enable the older short time-zone names to be used.
   * <p>
   * Use of short zone IDs has been deprecated in {@code java.util.TimeZone}.
   * This map allows the IDs to continue to be used via the
   * {@link #of(String, Map)} factory method.
   * <p>
   * This map contains an older mapping of the IDs, where 'EST', 'MST' and 'HST'
   * map to IDs which include daylight savings.
   * This is in line with versions of TZDB before 2005r.
   * <p>
   * This maps as follows:
   * <p><ul>
   * <li>EST - America/New_York</li>
   * <li>MST - America/Denver</li>
   * <li>HST - Pacific/Honolulu</li>
   * <li>ACT - Australia/Darwin</li>
   * <li>AET - Australia/Sydney</li>
   * <li>AGT - America/Argentina/Buenos_Aires</li>
   * <li>ART - Africa/Cairo</li>
   * <li>AST - America/Anchorage</li>
   * <li>BET - America/Sao_Paulo</li>
   * <li>BST - Asia/Dhaka</li>
   * <li>CAT - Africa/Harare</li>
   * <li>CNT - America/St_Johns</li>
   * <li>CST - America/Chicago</li>
   * <li>CTT - Asia/Shanghai</li>
   * <li>EAT - Africa/Addis_Ababa</li>
   * <li>ECT - Europe/Paris</li>
   * <li>IET - America/Indiana/Indianapolis</li>
   * <li>IST - Asia/Kolkata</li>
   * <li>JST - Asia/Tokyo</li>
   * <li>MIT - Pacific/Apia</li>
   * <li>NET - Asia/Yerevan</li>
   * <li>NST - Pacific/Auckland</li>
   * <li>PLT - Asia/Karachi</li>
   * <li>PNT - America/Phoenix</li>
   * <li>PRT - America/Puerto_Rico</li>
   * <li>PST - America/Los_Angeles</li>
   * <li>SST - Pacific/Guadalcanal</li>
   * <li>VST - Asia/Ho_Chi_Minh</li>
   * </ul><p>
   * The map is unmodifiable.
   */
  final val OLD_SHORT_IDS: Map[String, String] = null
  /**
   * A map of zone overrides to enable the short time-zone names to be used.
   * <p>
   * Use of short zone IDs has been deprecated in {@code java.util.TimeZone}.
   * This map allows the IDs to continue to be used via the
   * {@link #of(String, Map)} factory method.
   * <p>
   * This map contains a newer mapping of the IDs, where 'EST', 'MST' and 'HST'
   * map to IDs which do not include daylight savings
   * This is in line with TZDB 2005r and later.
   * <p>
   * This maps as follows:
   * <p><ul>
   * <li>EST - -05:00</li>
   * <li>HST - -10:00</li>
   * <li>MST - -07:00</li>
   * <li>ACT - Australia/Darwin</li>
   * <li>AET - Australia/Sydney</li>
   * <li>AGT - America/Argentina/Buenos_Aires</li>
   * <li>ART - Africa/Cairo</li>
   * <li>AST - America/Anchorage</li>
   * <li>BET - America/Sao_Paulo</li>
   * <li>BST - Asia/Dhaka</li>
   * <li>CAT - Africa/Harare</li>
   * <li>CNT - America/St_Johns</li>
   * <li>CST - America/Chicago</li>
   * <li>CTT - Asia/Shanghai</li>
   * <li>EAT - Africa/Addis_Ababa</li>
   * <li>ECT - Europe/Paris</li>
   * <li>IET - America/Indiana/Indianapolis</li>
   * <li>IST - Asia/Kolkata</li>
   * <li>JST - Asia/Tokyo</li>
   * <li>MIT - Pacific/Apia</li>
   * <li>NET - Asia/Yerevan</li>
   * <li>NST - Pacific/Auckland</li>
   * <li>PLT - Asia/Karachi</li>
   * <li>PNT - America/Phoenix</li>
   * <li>PRT - America/Puerto_Rico</li>
   * <li>PST - America/Los_Angeles</li>
   * <li>SST - Pacific/Guadalcanal</li>
   * <li>VST - Asia/Ho_Chi_Minh</li>
   * </ul><p>
   * The map is unmodifiable.
   */
  final val SHORT_IDS: Map[String, String] = null
  /**
   * Serialization version.
   */
  private final val serialVersionUID: Long = 8352817235686L
}

abstract class ZoneId extends Serializable {
  /**
   * Constructor only accessible within the package.
   */
  private[time] def this() {
    this()
    if (getClass ne classOf[ZoneOffset] && getClass ne classOf[ZoneRegion]) {
      throw new AssertionError("Invalid subclass")
    }
  }

  /**
   * Gets the unique time-zone ID.
   * <p>
   * This ID uniquely defines this object.
   * The format of an offset based ID is defined by {@link ZoneOffset#getId()}.
   *
   * @return the time-zone unique ID, not null
   */
  def getId: String

  /**
   * Gets the textual representation of the zone, such as 'British Time' or
   * '+02:00'.
   * <p>
   * This returns the textual name used to identify the time-zone ID,
   * suitable for presentation to the user.
   * The parameters control the style of the returned text and the locale.
   * <p>
   * If no textual mapping is found then the {@link #getId() full ID} is returned.
   *
   * @param style  the length of the text required, not null
   * @param locale  the locale to use, not null
   * @return the text value of the zone, not null
   */
  def getDisplayName(style: TextStyle, locale: Locale): String = {
     new DateTimeFormatterBuilder().appendZoneText(style).toFormatter(locale).format(toTemporal)
  }

  /**
   * Converts this zone to a {@code TemporalAccessor}.
   * <p>
   * A {@code ZoneId} can be fully represented as a {@code TemporalAccessor}.
   * However, the interface is not implemented by this class as most of the
   * methods on the interface have no meaning to {@code ZoneId}.
   * <p>
   * The returned temporal has no supported fields, with the query method
   * supporting the  of the zone using {@link TemporalQuery#zoneId()}.
   *
   * @return a temporal equivalent to this zone, not null
   */
  private def toTemporal: TemporalAccessor = {
     new TemporalAccessor {
      def isSupported(field: TemporalField): Boolean = {
         false
      }

      def getLong(field: TemporalField): Long = {
        throw new UnsupportedTemporalTypeException("Unsupported field: " + field)
      }

      override def query(query: TemporalQuery[R]): R = {
        if (query eq TemporalQuery.zoneId) {
           ZoneId.this.asInstanceOf[R]
        }
         TemporalAccessor.super.query(query)
      }
    }
  }

  /**
   * Gets the time-zone rules for this ID allowing calculations to be performed.
   * <p>
   * The rules provide the functionality associated with a time-zone,
   * such as finding the offset for a given instant or local date-time.
   * <p>
   * A time-zone can be invalid if it is deserialized in a Java Runtime which
   * does not have the same rules loaded as the Java Runtime that stored it.
   * In this case, calling this method will throw a {@code ZoneRulesException}.
   * <p>
   * The rules are supplied by {@link ZoneRulesProvider}. An advanced provider may
   * support dynamic updates to the rules without restarting the Java Runtime.
   * If so, then the result of this method may change over time.
   * Each individual call will be still remain thread-safe.
   * <p>
   * {@link ZoneOffset} will always  a set of rules where the offset never changes.
   *
   * @return the rules, not null
   * @throws ZoneRulesException if no rules are available for this ID
   */
  def getRules: ZoneRules

  /**
   * Normalizes the time-zone ID, returning a {@code ZoneOffset} where possible.
   * <p>
   * The returns a normalized {@code ZoneId} that can be used in place of this ID.
   * The result will have {@code ZoneRules} equivalent to those returned by this object,
   * however the ID returned by {@code getId()} may be different.
   * <p>
   * The normalization checks if the rules of this {@code ZoneId} have a fixed offset.
   * If they do, then the {@code ZoneOffset} equal to that offset is returned.
   * Otherwise {@code this} is returned.
   *
   * @return the time-zone unique ID, not null
   */
  def normalized: ZoneId = {
    try {
      val rules: ZoneRules = getRules
      if (rules.isFixedOffset) {
         rules.getOffset(Instant.EPOCH)
      }
    }
    catch {
      case ex: ZoneRulesException => {
      }
    }
     this
  }

  /**
   * Checks if this time-zone ID is equal to another time-zone ID.
   * <p>
   * The comparison is based on the ID.
   *
   * @param obj  the object to check, null returns false
   * @return true if this is equal to the other time-zone ID
   */
  override def equals(obj: AnyRef): Boolean = {
    if (this eq obj) {
       true
    }
    if (obj.isInstanceOf[ZoneId]) {
      val other: ZoneId = obj.asInstanceOf[ZoneId]
       getId == other.getId
    }
     false
  }

  /**
   * A hash code for this time-zone ID.
   *
   * @return a suitable hash code
   */
  override def hashCode: Int = {
     getId.hashCode
  }

  /**
   * Outputs this zone as a {@code String}, using the ID.
   *
   * @return a string representation of this time-zone ID, not null
   */
  override def toString: String = {
     getId
  }

  /**
   * Writes the object using a
   * <a href="../../serialized-form.html#java.time.Ser">dedicated serialized form</a>.
   * <pre>
   * out.writeByte(7);  // identifies this as a ZoneId (not ZoneOffset)
   * out.writeUTF(zoneId);
   * </pre>
   * <p>
   * When read back in, the {@code ZoneId} will be created as though using
   * {@link #of(String)}, but without any exception in the case where the
   * ID has a valid format, but is not in the known set of region-based IDs.
   *
   * @return the instance of { @code Ser}, not null
   */
  private def writeReplace: AnyRef = {
     new Ser(Ser.ZONE_REGION_TYPE, this)
  }

  private[time] def write(out: DataOutput)
}


/**
 * A time-zone offset from Greenwich/UTC, such as {@code +02:00}.
 * <p>
 * A time-zone offset is the period of time that a time-zone differs from Greenwich/UTC.
 * This is usually a fixed number of hours and minutes.
 * <p>
 * Different parts of the world have different time-zone offsets.
 * The rules for how offsets vary by place and time of year are captured in the
 * {@link ZoneId} class.
 * <p>
 * For example, Paris is one hour ahead of Greenwich/UTC in winter and two hours
 * ahead in summer. The {@code ZoneId} instance for Paris will reference two
 * {@code ZoneOffset} instances - a {@code +01:00} instance for winter,
 * and a {@code +02:00} instance for summer.
 * <p>
 * In 2008, time-zone offsets around the world extended from -12:00 to +14:00.
 * To prevent any problems with that range being extended, yet still provide
 * validation, the range of offsets is restricted to -18:00 to 18:00 inclusive.
 * <p>
 * This class is designed for use with the ISO calendar system.
 * The fields of hours, minutes and seconds make assumptions that are valid for the
 * standard ISO definitions of those fields. This class may be used with other
 * calendar systems providing the definition of the time fields matches those
 * of the ISO calendar system.
 * <p>
 * Instances of {@code ZoneOffset} must be compared using {@link #equals}.
 * Implementations may choose to cache certain common offsets, however
 * applications must not rely on such caching.
 *
 * @implSpec
 * This class is immutable and thread-safe.
 *
 * @since 1.8
 */
object ZoneOffset {
  /**
   * Obtains an instance of {@code ZoneOffset} using the ID.
   * <p>
   * This method parses the string ID of a {@code ZoneOffset} to
   *  an instance. The parsing accepts all the formats generated by
   * {@link #getId()}, plus some additional formats:
   * <p><ul>
   * <li>{@code Z} - for UTC
   * <li>{@code +h}
   * <li>{@code +hh}
   * <li>{@code +hh:mm}
   * <li>{@code -hh:mm}
   * <li>{@code +hhmm}
   * <li>{@code -hhmm}
   * <li>{@code +hh:mm:ss}
   * <li>{@code -hh:mm:ss}
   * <li>{@code +hhmmss}
   * <li>{@code -hhmmss}
   * </ul><p>
   * Note that &plusmn; means either the plus or minus symbol.
   * <p>
   * The ID of the returned offset will be normalized to one of the formats
   * described by {@link #getId()}.
   * <p>
   * The maximum supported range is from +18:00 to -18:00 inclusive.
   *
   * @param offsetId  the offset ID, not null
   * @return the zone-offset, not null
   * @throws DateTimeException if the offset ID is invalid
   */
  @SuppressWarnings(Array("fallthrough")) def of(offsetId: String): ZoneOffset = {
    object
    val offset: ZoneOffset = ID_CACHE.get(offsetId)
    if (offset != null) {
       offset
    }
    val hours: Int = 0
    val minutes: Int = 0
    val seconds: Int = 0
    offsetId.length match {
      case 2 =>
        offsetId = offsetId.charAt(0) + "0" + offsetId.charAt(1)
      case 3 =>
        hours = parseNumber(offsetId, 1, false)
        minutes = 0
        seconds = 0
        break //todo: break is not supported
      case 5 =>
        hours = parseNumber(offsetId, 1, false)
        minutes = parseNumber(offsetId, 3, false)
        seconds = 0
        break //todo: break is not supported
      case 6 =>
        hours = parseNumber(offsetId, 1, false)
        minutes = parseNumber(offsetId, 4, true)
        seconds = 0
        break //todo: break is not supported
      case 7 =>
        hours = parseNumber(offsetId, 1, false)
        minutes = parseNumber(offsetId, 3, false)
        seconds = parseNumber(offsetId, 5, false)
        break //todo: break is not supported
      case 9 =>
        hours = parseNumber(offsetId, 1, false)
        minutes = parseNumber(offsetId, 4, true)
        seconds = parseNumber(offsetId, 7, true)
        break //todo: break is not supported
      case _ =>
        throw new DateTimeException("Invalid ID for ZoneOffset, invalid format: " + offsetId)
    }
    val first: Char = offsetId.charAt(0)
    if (first != '+' && first != '-') {
      throw new DateTimeException("Invalid ID for ZoneOffset, plus/minus not found when expected: " + offsetId)
    }
    if (first == '-') {
       ofHoursMinutesSeconds(-hours, -minutes, -seconds)
    }
    else {
       ofHoursMinutesSeconds(hours, minutes, seconds)
    }
  }

  /**
   * Parse a two digit zero-prefixed number.
   *
   * @param offsetId  the offset ID, not null
   * @param pos  the position to parse, valid
   * @param precededByColon  should this number be prefixed by a precededByColon
   * @return the parsed number, from 0 to 99
   */
  private def parseNumber(offsetId: CharSequence, pos: Int, precededByColon: Boolean): Int = {
    if (precededByColon && offsetId.charAt(pos - 1) != ':') {
      throw new DateTimeException("Invalid ID for ZoneOffset, colon not found when expected: " + offsetId)
    }
    val ch1: Char = offsetId.charAt(pos)
    val ch2: Char = offsetId.charAt(pos + 1)
    if (ch1 < '0' || ch1 > '9' || ch2 < '0' || ch2 > '9') {
      throw new DateTimeException("Invalid ID for ZoneOffset, non numeric characters found: " + offsetId)
    }
     (ch1 - 48) * 10 + (ch2 - 48)
  }

  /**
   * Obtains an instance of {@code ZoneOffset} using an offset in hours.
   *
   * @param hours  the time-zone offset in hours, from -18 to +18
   * @return the zone-offset, not null
   * @throws DateTimeException if the offset is not in the required range
   */
  def ofHours(hours: Int): ZoneOffset = {
     ofHoursMinutesSeconds(hours, 0, 0)
  }

  /**
   * Obtains an instance of {@code ZoneOffset} using an offset in
   * hours and minutes.
   * <p>
   * The sign of the hours and minutes components must match.
   * Thus, if the hours is negative, the minutes must be negative or zero.
   * If the hours is zero, the minutes may be positive, negative or zero.
   *
   * @param hours  the time-zone offset in hours, from -18 to +18
   * @param minutes  the time-zone offset in minutes, from 0 to &plusmn;59, sign matches hours
   * @return the zone-offset, not null
   * @throws DateTimeException if the offset is not in the required range
   */
  def ofHoursMinutes(hours: Int, minutes: Int): ZoneOffset = {
     ofHoursMinutesSeconds(hours, minutes, 0)
  }

  /**
   * Obtains an instance of {@code ZoneOffset} using an offset in
   * hours, minutes and seconds.
   * <p>
   * The sign of the hours, minutes and seconds components must match.
   * Thus, if the hours is negative, the minutes and seconds must be negative or zero.
   *
   * @param hours  the time-zone offset in hours, from -18 to +18
   * @param minutes  the time-zone offset in minutes, from 0 to &plusmn;59, sign matches hours and seconds
   * @param seconds  the time-zone offset in seconds, from 0 to &plusmn;59, sign matches hours and minutes
   * @return the zone-offset, not null
   * @throws DateTimeException if the offset is not in the required range
   */
  def ofHoursMinutesSeconds(hours: Int, minutes: Int, seconds: Int): ZoneOffset = {
    validate(hours, minutes, seconds)
    val totalSeconds: Int = totalSeconds(hours, minutes, seconds)
     ofTotalSeconds(totalSeconds)
  }

  /**
   * Obtains an instance of {@code ZoneOffset} from a temporal object.
   * <p>
   * This obtains an offset based on the specified temporal.
   * A {@code TemporalAccessor} represents an arbitrary set of date and time information,
   * which this factory converts to an instance of {@code ZoneOffset}.
   * <p>
   * A {@code TemporalAccessor} represents some form of date and time information.
   * This factory converts the arbitrary temporal object to an instance of {@code ZoneOffset}.
   * <p>
   * The conversion uses the {@link TemporalQuery#offset()} query, which relies
   * on extracting the {@link ChronoField#OFFSET_SECONDS OFFSET_SECONDS} field.
   * <p>
   * This method matches the signature of the functional interface {@link TemporalQuery}
   * allowing it to be used in queries via method reference, {@code ZoneOffset::from}.
   *
   * @param temporal  the temporal object to convert, not null
   * @return the zone-offset, not null
   * @throws DateTimeException if unable to convert to an { @code ZoneOffset}
   */
  def from(temporal: TemporalAccessor): ZoneOffset = {
    object
    val offset: ZoneOffset = temporal.query(TemporalQuery.offset)
    if (offset == null) {
      throw new DateTimeException("Unable to obtain ZoneOffset from TemporalAccessor: " + temporal.getClass)
    }
     offset
  }

  /**
   * Validates the offset fields.
   *
   * @param hours  the time-zone offset in hours, from -18 to +18
   * @param minutes  the time-zone offset in minutes, from 0 to &plusmn;59
   * @param seconds  the time-zone offset in seconds, from 0 to &plusmn;59
   * @throws DateTimeException if the offset is not in the required range
   */
  private def validate(hours: Int, minutes: Int, seconds: Int) {
    if (hours < -18 || hours > 18) {
      throw new DateTimeException("Zone offset hours not in valid range: value " + hours + " is not in the range -18 to 18")
    }
    if (hours > 0) {
      if (minutes < 0 || seconds < 0) {
        throw new DateTimeException("Zone offset minutes and seconds must be positive because hours is positive")
      }
    }
    else if (hours < 0) {
      if (minutes > 0 || seconds > 0) {
        throw new DateTimeException("Zone offset minutes and seconds must be negative because hours is negative")
      }
    }
    else if ((minutes > 0 && seconds < 0) || (minutes < 0 && seconds > 0)) {
      throw new DateTimeException("Zone offset minutes and seconds must have the same sign")
    }
    if (Math.abs(minutes) > 59) {
      throw new DateTimeException("Zone offset minutes not in valid range: abs(value) " + Math.abs(minutes) + " is not in the range 0 to 59")
    }
    if (Math.abs(seconds) > 59) {
      throw new DateTimeException("Zone offset seconds not in valid range: abs(value) " + Math.abs(seconds) + " is not in the range 0 to 59")
    }
    if (Math.abs(hours) == 18 && (Math.abs(minutes) > 0 || Math.abs(seconds) > 0)) {
      throw new DateTimeException("Zone offset not in valid range: -18:00 to +18:00")
    }
  }

  /**
   * Calculates the total offset in seconds.
   *
   * @param hours  the time-zone offset in hours, from -18 to +18
   * @param minutes  the time-zone offset in minutes, from 0 to &plusmn;59, sign matches hours and seconds
   * @param seconds  the time-zone offset in seconds, from 0 to &plusmn;59, sign matches hours and minutes
   * @return the total in seconds
   */
  private def totalSeconds(hours: Int, minutes: Int, seconds: Int): Int = {
     hours * SECONDS_PER_HOUR + minutes * SECONDS_PER_MINUTE + seconds
  }

  /**
   * Obtains an instance of {@code ZoneOffset} specifying the total offset in seconds
   * <p>
   * The offset must be in the range {@code -18:00} to {@code +18:00}, which corresponds to -64800 to +64800.
   *
   * @param totalSeconds  the total time-zone offset in seconds, from -64800 to +64800
   * @return the ZoneOffset, not null
   * @throws DateTimeException if the offset is not in the required range
   */
  def ofTotalSeconds(totalSeconds: Int): ZoneOffset = {
    if (Math.abs(totalSeconds) > MAX_SECONDS) {
      throw new DateTimeException("Zone offset not in valid range: -18:00 to +18:00")
    }
    if (totalSeconds % (15 * SECONDS_PER_MINUTE) == 0) {
      val totalSecs: Integer = totalSeconds
      var result: ZoneOffset = SECONDS_CACHE.get(totalSecs)
      if (result == null) {
        result = new ZoneOffset(totalSeconds)
        SECONDS_CACHE.putIfAbsent(totalSecs, result)
        result = SECONDS_CACHE.get(totalSecs)
        ID_CACHE.putIfAbsent(result.getId, result)
      }
       result
    }
    else {
       new ZoneOffset(totalSeconds)
    }
  }

  private def buildId(totalSeconds: Int): String = {
    if (totalSeconds == 0) {
       "Z"
    }
    else {
      val absTotalSeconds: Int = Math.abs(totalSeconds)
      val buf: StringBuilder = new StringBuilder
      val absHours: Int = absTotalSeconds / SECONDS_PER_HOUR
      val absMinutes: Int = (absTotalSeconds / SECONDS_PER_MINUTE) % MINUTES_PER_HOUR
      buf.append(if (totalSeconds < 0) "-" else "+").append(if (absHours < 10) "0" else "").append(absHours).append(if (absMinutes < 10) ":0" else ":").append(absMinutes)
      val absSeconds: Int = absTotalSeconds % SECONDS_PER_MINUTE
      if (absSeconds != 0) {
        buf.append(if (absSeconds < 10) ":0" else ":").append(absSeconds)
      }
       buf.toString
    }
  }

  private[time] def readExternal(in: DataInput): ZoneOffset = {
    val offsetByte: Int = in.readByte
     (if (offsetByte == 127) ZoneOffset.ofTotalSeconds(in.readInt) else ZoneOffset.ofTotalSeconds(offsetByte * 900))
  }

  /** Cache of time-zone offset by offset in seconds. */
  private final val SECONDS_CACHE: ConcurrentMap[Integer, ZoneOffset] = new ConcurrentHashMap[Integer, ZoneOffset](16, 0.75f, 4)
  /** Cache of time-zone offset by ID. */
  private final val ID_CACHE: ConcurrentMap[String, ZoneOffset] = new ConcurrentHashMap[String, ZoneOffset](16, 0.75f, 4)
  /**
   * The abs maximum seconds.
   */
  private final val MAX_SECONDS: Int = 18 * SECONDS_PER_HOUR
  /**
   * Serialization version.
   */
  private final val serialVersionUID: Long = 2357656521762053153L
  /**
   * The time-zone offset for UTC, with an ID of 'Z'.
   */
  final val UTC: ZoneOffset = ZoneOffset.ofTotalSeconds(0)
  /**
   * Constant for the maximum supported offset.
   */
  final val MIN: ZoneOffset = ZoneOffset.ofTotalSeconds(-MAX_SECONDS)
  /**
   * Constant for the maximum supported offset.
   */
  final val MAX: ZoneOffset = ZoneOffset.ofTotalSeconds(MAX_SECONDS)
}

final class ZoneOffset extends ZoneId with TemporalAccessor with TemporalAdjuster with Comparable[ZoneOffset]  {
  /**
   * Constructor.
   *
   * @param totalSeconds  the total time-zone offset in seconds, from -64800 to +64800
   */
  private def this(totalSeconds: Int) {
    this()
    `super`
    this.totalSeconds = totalSeconds
    id = buildId(totalSeconds)
  }

  /**
   * Gets the total zone offset in seconds.
   * <p>
   * This is the primary way to access the offset amount.
   * It returns the total of the hours, minutes and seconds fields as a
   * single offset that can be added to a time.
   *
   * @return the total zone offset amount in seconds
   */
  def getTotalSeconds: Int = {
     totalSeconds
  }

  /**
   * Gets the normalized zone offset ID.
   * <p>
   * The ID is minor variation to the standard ISO-8601 formatted string
   * for the offset. There are three formats:
   * <p><ul>
   * <li>{@code Z} - for UTC (ISO-8601)
   * <li>{@code +hh:mm} or {@code -hh:mm} - if the seconds are zero (ISO-8601)
   * <li>{@code +hh:mm:ss} or {@code -hh:mm:ss} - if the seconds are non-zero (not ISO-8601)
   * </ul><p>
   *
   * @return the zone offset ID, not null
   */
  def getId: String = {
     id
  }

  /**
   * Gets the associated time-zone rules.
   * <p>
   * The rules will always  this offset when queried.
   * The implementation class is immutable, thread-safe and serializable.
   *
   * @return the rules, not null
   */
  def getRules: ZoneRules = {
     ZoneRules.of(this)
  }

  /**
   * Checks if the specified field is supported.
   * <p>
   * This checks if this offset can be queried for the specified field.
   * If false, then calling the {@link #range(TemporalField) range} and
   * {@link #get(TemporalField) get} methods will throw an exception.
   * <p>
   * If the field is a {@link ChronoField} then the query is implemented here.
   * The {@code OFFSET_SECONDS} field returns true.
   * All other {@code ChronoField} instances will  false.
   * <p>
   * If the field is not a {@code ChronoField}, then the result of this method
   * is obtained by invoking {@code TemporalField.isSupportedBy(TemporalAccessor)}
   * passing {@code this} as the argument.
   * Whether the field is supported is determined by the field.
   *
   * @param field  the field to check, null returns false
   * @return true if the field is supported on this offset, false if not
   */
  def isSupported(field: TemporalField): Boolean = {
    if (field.isInstanceOf[ChronoField]) {
       field eq OFFSET_SECONDS
    }
     field != null && field.isSupportedBy(this)
  }

  /**
   * Gets the range of valid values for the specified field.
   * <p>
   * The range object expresses the minimum and maximum valid values for a field.
   * This offset is used to enhance the accuracy of the returned range.
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
     TemporalAccessor.super.range(field)
  }

  /**
   * Gets the value of the specified field from this offset as an {@code int}.
   * <p>
   * This queries this offset for the value for the specified field.
   * The returned value will always be within the valid range of values for the field.
   * If it is not possible to  the value, because the field is not supported
   * or for some other reason, an exception is thrown.
   * <p>
   * If the field is a {@link ChronoField} then the query is implemented here.
   * The {@code OFFSET_SECONDS} field returns the value of the offset.
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
    if (field eq OFFSET_SECONDS) {
       totalSeconds
    }
    else if (field.isInstanceOf[ChronoField]) {
      throw new UnsupportedTemporalTypeException("Unsupported field: " + field)
    }
     range(field).checkValidIntValue(getLong(field), field)
  }

  /**
   * Gets the value of the specified field from this offset as a {@code long}.
   * <p>
   * This queries this offset for the value for the specified field.
   * If it is not possible to  the value, because the field is not supported
   * or for some other reason, an exception is thrown.
   * <p>
   * If the field is a {@link ChronoField} then the query is implemented here.
   * The {@code OFFSET_SECONDS} field returns the value of the offset.
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
    if (field eq OFFSET_SECONDS) {
       totalSeconds
    }
    else if (field.isInstanceOf[ChronoField]) {
      throw new UnsupportedTemporalTypeException("Unsupported field: " + field)
    }
     field.getFrom(this)
  }

  /**
   * Queries this offset using the specified query.
   * <p>
   * This queries this offset using the specified query strategy object.
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
       this.asInstanceOf[R]
    }
     TemporalAccessor.super.query(query)
  }

  /**
   * Adjusts the specified temporal object to have the same offset as this object.
   * <p>
   * This returns a temporal object of the same observable type as the input
   * with the offset changed to be the same as this.
   * <p>
   * The adjustment is equivalent to using {@link Temporal#with(TemporalField, long)}
   * passing {@link ChronoField#OFFSET_SECONDS} as the field.
   * <p>
   * In most cases, it is clearer to reverse the calling pattern by using
   * {@link Temporal#with(TemporalAdjuster)}:
   * <pre>
   * // these two lines are equivalent, but the second approach is recommended
   * temporal = thisOffset.adjustInto(temporal);
   * temporal = temporal.with(thisOffset);
   * </pre>
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param temporal  the target object to be adjusted, not null
   * @return the adjusted object, not null
   * @throws DateTimeException if unable to make the adjustment
   * @throws ArithmeticException if numeric overflow occurs
   */
  def adjustInto(temporal: Temporal): Temporal = {
     temporal.`with`(OFFSET_SECONDS, totalSeconds)
  }

  /**
   * Compares this offset to another offset in descending order.
   * <p>
   * The offsets are compared in the order that they occur for the same time
   * of day around the world. Thus, an offset of {@code +10:00} comes before an
   * offset of {@code +09:00} and so on down to {@code -18:00}.
   * <p>
   * The comparison is "consistent with equals", as defined by {@link Comparable}.
   *
   * @param other  the other date to compare to, not null
   * @return the comparator value, negative if less, postive if greater
   * @throws NullPointerException if { @code other} is null
   */
  def compareTo(other: ZoneOffset): Int = {
     other.totalSeconds - totalSeconds
  }

  /**
   * Checks if this offset is equal to another offset.
   * <p>
   * The comparison is based on the amount of the offset in seconds.
   * This is equivalent to a comparison by ID.
   *
   * @param obj  the object to check, null returns false
   * @return true if this is equal to the other offset
   */
  override def equals(obj: AnyRef): Boolean = {
    if (this eq obj) {
       true
    }
    if (obj.isInstanceOf[ZoneOffset]) {
       totalSeconds == (obj.asInstanceOf[ZoneOffset]).totalSeconds
    }
     false
  }

  /**
   * A hash code for this offset.
   *
   * @return a suitable hash code
   */
  override def hashCode: Int = {
     totalSeconds
  }

  /**
   * Outputs this offset as a {@code String}, using the normalized ID.
   *
   * @return a string representation of this offset, not null
   */
  override def toString: String = {
     id
  }

  /**
   * Writes the object using a
   * <a href="../../serialized-form.html#java.time.Ser">dedicated serialized form</a>.
   * <pre>
   * out.writeByte(8);  // identifies this as a ZoneOffset
   * int offsetByte = totalSeconds % 900 == 0 ? totalSeconds / 900 : 127;
   * out.writeByte(offsetByte);
   * if (offsetByte == 127) {
   * out.writeInt(totalSeconds);
   * }
   * </pre>
   *
   * @return the instance of { @code Ser}, not null
   */
  private def writeReplace: AnyRef = {
     new Ser(Ser.ZONE_OFFSET_TYPE, this)
  }

  /**
   * Defend against malicious streams.
   * @return never
   * @throws InvalidObjectException always
   */
  private def readResolve: AnyRef = {
    throw new InvalidObjectException("Deserialization via serialization delegate")
  }

  private[time] def write(out: DataOutput) {
    out.writeByte(Ser.ZONE_OFFSET_TYPE)
    writeExternal(out)
  }

  private[time] def writeExternal(out: DataOutput) {
    val offsetSecs: Int = totalSeconds
    val offsetByte: Int = if (offsetSecs % 900 == 0) offsetSecs / 900 else 127
    out.writeByte(offsetByte)
    if (offsetByte == 127) {
      out.writeInt(offsetSecs)
    }
  }

  /**
   * The total offset in seconds.
   */
  private final val totalSeconds: Int = 0
  /**
   * The string form of the time-zone offset.
   */
  @transient
  private final val id: String = null
}

/**
 * A geographical region where the same time-zone rules apply.
 * <p>
 * Time-zone information is categorized as a set of rules defining when and
 * how the offset from UTC/Greenwich changes. These rules are accessed using
 * identifiers based on geographical regions, such as countries or states.
 * The most common region classification is the Time Zone Database (TZDB),
 * which defines regions such as 'Europe/Paris' and 'Asia/Tokyo'.
 * <p>
 * The region identifier, modeled by this class, is distinct from the
 * underlying rules, modeled by {@link ZoneRules}.
 * The rules are defined by governments and change frequently.
 * By contrast, the region identifier is well-defined and long-lived.
 * This separation also allows rules to be shared between regions if appropriate.
 *
 * @implSpec
 * This class is immutable and thread-safe.
 *
 * @since 1.8
 */
object ZoneRegion {
  /**
   * Obtains an instance of {@code ZoneId} from an identifier.
   *
   * @param zoneId  the time-zone ID, not null
   * @param checkAvailable  whether to check if the zone ID is available
   * @return the zone ID, not null
   * @throws DateTimeException if the ID format is invalid
   * @throws ZoneRulesException if checking availability and the ID cannot be found
   */
  private[time] def ofId(zoneId: String, checkAvailable: Boolean): ZoneRegion = {
    object
    checkName(zoneId)
    var rules: ZoneRules = null
    try {
      rules = ZoneRulesProvider.getRules(zoneId, true)
    }
    catch {
      case ex: ZoneRulesException => {
        if (checkAvailable) {
          throw ex
        }
      }
    }
     new ZoneRegion(zoneId, rules)
  }

  /**
   * Checks that the given string is a legal ZondId name.
   *
   * @param zoneId  the time-zone ID, not null
   * @throws DateTimeException if the ID format is invalid
   */
  private def checkName(zoneId: String) {
    val n: Int = zoneId.length
    if (n < 2) {
      throw new DateTimeException("Invalid ID for region-based ZoneId, invalid format: " + zoneId)
    }
    {
      var i: Int = 0
      while (i < n) {
        {
          val c: Char = zoneId.charAt(i)
          if (c >= 'a' && c <= 'z') continue //todo: continue is not supported
          if (c >= 'A' && c <= 'Z') continue //todo: continue is not supported
          if (c == '/' && i != 0) continue //todo: continue is not supported
          if (c >= '0' && c <= '9' && i != 0) continue //todo: continue is not supported
          if (c == '~' && i != 0) continue //todo: continue is not supported
          if (c == '.' && i != 0) continue //todo: continue is not supported
          if (c == '_' && i != 0) continue //todo: continue is not supported
          if (c == '+' && i != 0) continue //todo: continue is not supported
          if (c == '-' && i != 0) continue //todo: continue is not supported
          throw new DateTimeException("Invalid ID for region-based ZoneId, invalid format: " + zoneId)
        }
        ({
          i += 1; i - 1
        })
      }
    }
  }

  private[time] def readExternal(in: DataInput): ZoneId = {
    val id: String = in.readUTF
     ZoneId.of(id, false)
  }

  /**
   * Serialization version.
   */
  private final val serialVersionUID: Long = 8386373296231747096L
}

final class ZoneRegion extends ZoneId  {
  /**
   * Constructor.
   *
   * @param id  the time-zone ID, not null
   * @param rules  the rules, null for lazy lookup
   */
  private[time] def this(id: String, rules: ZoneRules) {
    this()
    this.id = id
    this.rules = rules
  }

  def getId: String = {
     id
  }

  def getRules: ZoneRules = {
     (if (rules != null) rules else ZoneRulesProvider.getRules(id, false))
  }

  /**
   * Writes the object using a
   * <a href="../../serialized-form.html#java.time.Ser">dedicated serialized form</a>.
   * <pre>
   * out.writeByte(7);  // identifies this as a ZoneId (not ZoneOffset)
   * out.writeUTF(zoneId);
   * </pre>
   *
   * @return the instance of { @code Ser}, not null
   */
  private def writeReplace: AnyRef = {
     new Ser(Ser.ZONE_REGION_TYPE, this)
  }

  /**
   * Defend against malicious streams.
   * @return never
   * @throws InvalidObjectException always
   */
  private def readResolve: AnyRef = {
    throw new InvalidObjectException("Deserialization via serialization delegate")
  }

  private[time] def write(out: DataOutput) {
    out.writeByte(Ser.ZONE_REGION_TYPE)
    writeExternal(out)
  }

  private[time] def writeExternal(out: DataOutput) {
    out.writeUTF(id)
  }

  /**
   * The time-zone ID, not null.
   */
  private final val id: String = null
  /**
   * The time-zone rules, null if zone ID was loaded leniently.
   */
  @transient
  private final val rules: ZoneRules = null
}







