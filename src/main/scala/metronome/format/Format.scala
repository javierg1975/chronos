package metronome.format

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
 * Copyright (c) 2008-2012, Stephen Colebourne & Michael Nascimento Santos
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
 * Formatter for printing and parsing date-time objects.
 * <p>
 * This class provides the main application entry point for printing and parsing
 * and provides common implementations of {@code DateTimeFormatter}:
 * <ul>
 * <li>Using predefined constants, such as {@link #ISO_LOCAL_DATE}</li>
 * <li>Using pattern letters, such as {@code uuuu-MMM-dd}</li>
 * <li>Using localized styles, such as {@code long} or {@code medium}</li>
 * </ul>
 * <p>
 * More complex formatters are provided by
 * {@link DateTimeFormatterBuilder DateTimeFormatterBuilder}.
 *
 * <p>
 * The main date-time classes provide two methods - one for formatting,
 * {@code format(DateTimeFormatter formatter)}, and one for parsing,
 * {@code parse(CharSequence text, DateTimeFormatter formatter)}.
 * <p>For example:
 * <blockquote>{{{
 * String text = date.toString(formatter);
 * Date date = Date.parse(text, formatter);
 * }}}</blockquote>
 * <p>
 * In addition to the format, formatters can be created with desired Locale,
 * Chronology, ZoneId, and DecimalStyle.
 * <p>
 * The {@link #withLocale withLocale} method returns a new formatter that
 * overrides the locale. The locale affects some aspects of formatting and
 * parsing. For example, the {@link #ofLocalizedDate ofLocalizedDate} provides a
 * formatter that uses the locale specific date format.
 * <p>
 * The {@link #withChronology withChronology} method returns a new formatter
 * that overrides the chronology. If overridden, the date-time value is
 * converted to the chronology before formatting. During parsing the date-time
 * value is converted to the chronology before it is returned.
 * <p>
 * The {@link #withZone withZone} method returns a new formatter that overrides
 * the zone. If overridden, the date-time value is converted to a ZonedDateTime
 * with the requested ZoneId before formatting. During parsing the ZoneId is
 * applied before the value is returned.
 * <p>
 * The {@link #withDecimalStyle withDecimalStyle} method returns a new formatter that
 * overrides the {@link DecimalStyle}. The DecimalStyle symbols are used for
 * formatting and parsing.
 * <p>
 * Some applications may need to use the older {@link Format java.text.Format}
 * class for formatting. The {@link #toFormat()} method returns an
 * implementation of {@code java.text.Format}.
 * <p>
 * <h3 id="predefined">Predefined Formatters</h3>
 * <table summary="Predefined Formatters" cellpadding="2" cellspacing="3" border="0" >
 * <thead>
 * <tr class="tableSubHeadingColor">
 * <th class="colFirst" align="left">Formatter</th>
 * <th class="colFirst" align="left">Description</th>
 * <th class="colLast" align="left">Example</th>
 * </tr>
 * </thead>
 * <tbody>
 * <tr class="rowColor">
 * <td>{@link #ofLocalizedDate ofLocalizedDate(dateStyle)} </td>
 * <td> Formatter with date style from the locale </td>
 * <td> '2011-12-03'</td>
 * </tr>
 * <tr class="altColor">
 * <td> {@link #ofLocalizedTime ofLocalizedTime(timeStyle)} </td>
 * <td> Formatter with time style from the locale </td>
 * <td> '10:15:30'</td>
 * </tr>
 * <tr class="rowColor">
 * <td> {@link #ofLocalizedDateTime ofLocalizedDateTime(dateTimeStyle)} </td>
 * <td> Formatter with a style for date and time from the locale</td>
 * <td> '3 Jun 2008 11:05:30'</td>
 * </tr>
 * <tr class="altColor">
 * <td> {@link #ofLocalizedDateTime ofLocalizedDateTime(dateStyle,timeStyle)}
 * </td>
 * <td> Formatter with date and time styles from the locale </td>
 * <td> '3 Jun 2008 11:05'</td>
 * </tr>
 * <tr class="rowColor">
 * <td> {@link #BASIC_ISO_DATE}</td>
 * <td>Basic ISO date </td> <td>'20111203'</td>
 * </tr>
 * <tr class="altColor">
 * <td> {@link #ISO_LOCAL_DATE}</td>
 * <td> ISO Local Date </td>
 * <td>'2011-12-03'</td>
 * </tr>
 * <tr class="rowColor">
 * <td> {@link #ISO_OFFSET_DATE}</td>
 * <td> ISO Date with offset </td>
 * <td>'2011-12-03+01:00'</td>
 * </tr>
 * <tr class="altColor">
 * <td> {@link #ISO_DATE}</td>
 * <td> ISO Date with or without offset </td>
 * <td> '2011-12-03+01:00'; '2011-12-03'</td>
 * </tr>
 * <tr class="rowColor">
 * <td> {@link #ISO_LOCAL_TIME}</td>
 * <td> Time without offset </td>
 * <td>'10:15:30'</td>
 * </tr>
 * <tr class="altColor">
 * <td> {@link #ISO_OFFSET_TIME}</td>
 * <td> Time with offset </td>
 * <td>'10:15:30+01:00'</td>
 * </tr>
 * <tr class="rowColor">
 * <td> {@link #ISO_TIME}</td>
 * <td> Time with or without offset </td>
 * <td>'10:15:30+01:00'; '10:15:30'</td>
 * </tr>
 * <tr class="altColor">
 * <td> {@link #ISO_LOCAL_DATE_TIME}</td>
 * <td> ISO Local Date and Time </td>
 * <td>'2011-12-03T10:15:30'</td>
 * </tr>
 * <tr class="rowColor">
 * <td> {@link #ISO_OFFSET_DATE_TIME}</td>
 * <td> Date Time with Offset
 * </td><td>2011-12-03T10:15:30+01:00'</td>
 * </tr>
 * <tr class="altColor">
 * <td> {@link #ISO_ZONED_DATE_TIME}</td>
 * <td> Zoned Date Time </td>
 * <td>'2011-12-03T10:15:30+01:00[Europe/Paris]'</td>
 * </tr>
 * <tr class="rowColor">
 * <td> {@link #ISO_DATE_TIME}</td>
 * <td> Date and time with ZoneId </td>
 * <td>'2011-12-03T10:15:30+01:00[Europe/Paris]'</td>
 * </tr>
 * <tr class="altColor">
 * <td> {@link #ISO_ORDINAL_DATE}</td>
 * <td> Year and day of year </td>
 * <td>'2012-337'</td>
 * </tr>
 * <tr class="rowColor">
 * <td> {@link #ISO_WEEK_DATE}</td>
 * <td> Year and Week </td>
 * <td>2012-W48-6'</td></tr>
 * <tr class="altColor">
 * <td> {@link #ISO_INSTANT}</td>
 * <td> Date and Time of an Instant </td>
 * <td>'2011-12-03T10:15:30Z' </td>
 * </tr>
 * <tr class="rowColor">
 * <td> {@link #RFC_1123_DATE_TIME}</td>
 * <td> RFC 1123 / RFC 822 </td>
 * <td>'Tue, 3 Jun 2008 11:05:30 GMT'</td>
 * </tr>
 * </tbody>
 * </table>
 *
 * <h3 id="patterns">Patterns for Formatting and Parsing</h3>
 * Patterns are based on a simple sequence of letters and symbols.
 * A pattern is used to create a Formatter using the
 * {@link #ofPattern(String)} and {@link #ofPattern(String, Locale)} methods.
 * For example,
 * {@code "d MMM uuuu"} will format 2011-12-03 as '3&nbsp;Dec&nbsp;2011'.
 * A formatter created from a pattern can be used as many times as necessary,
 * it is immutable and is thread-safe.
 * <p>
 * For example:
 * <blockquote>{{{
 * DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MM dd");
 * String text = date.toString(formatter);
 * Date date = Date.parse(text, formatter);
 * }}}</blockquote>
 * <p>
 * All letters 'A' to 'Z' and 'a' to 'z' are reserved as pattern letters. The
 * following pattern letters are defined:
 * {{{
 * Symbol  Meaning                     Presentation      Examples
 * ------  -------                     ------------      -------
 * G       era                         text              AD; Anno Domini; A
 * u       year                        year              2004; 04
 * y       year-of-era                 year              2004; 04
 * D       day-of-year                 number            189
 * M/L     month-of-year               number/text       7; 07; Jul; July; J
 * d       day-of-month                number            10
 *
 * Q/q     quarter-of-year             number/text       3; 03; Q3; 3rd quarter
 * Y       week-based-year             year              1996; 96
 * w       week-of-week-based-year     number            27
 * W       week-of-month               number            4
 * E       day-of-week                 text              Tue; Tuesday; T
 * e/c     localized day-of-week       number/text       2; 02; Tue; Tuesday; T
 * F       week-of-month               number            3
 *
 * a       am-pm-of-day                text              PM
 * h       clock-hour-of-am-pm (1-12)  number            12
 * K       hour-of-am-pm (0-11)        number            0
 * k       clock-hour-of-am-pm (1-24)  number            0
 *
 * H       hour-of-day (0-23)          number            0
 * m       minute-of-hour              number            30
 * s       second-of-minute            number            55
 * S       fraction-of-second          fraction          978
 * A       milli-of-day                number            1234
 * n       nano-of-second              number            987654321
 * N       nano-of-day                 number            1234000000
 *
 * V       time-zone ID                zone-id           America/Los_Angeles; Z; -08:30
 * z       time-zone name              zone-name         Pacific Standard Time; PST
 * O       localized zone-offset       offset-O          GMT+8; GMT+08:00; UTC-08:00;
 * X       zone-offset 'Z' for zero    offset-X          Z; -08; -0830; -08:30; -083015; -08:30:15;
 * x       zone-offset                 offset-x          +0000; -08; -0830; -08:30; -083015; -08:30:15;
 * Z       zone-offset                 offset-Z          +0000; -0800; -08:00;
 *
 * p       pad next                    pad modifier      1
 *
 * '       escape for text             delimiter
 * ''      single quote                literal           '
 * [       optional section start
 * ]       optional section end
 * #       reserved for future use
 * {       reserved for future use
 * }       reserved for future use
 * }}}
 * <p>
 * The count of pattern letters determines the format.
 * <p>
 * <b>Text</b>: The text style is determined based on the number of pattern
 * letters used. Less than 4 pattern letters will use the
 * {@link TextStyle#SHORT short form}. Exactly 4 pattern letters will use the
 * {@link TextStyle#FULL full form}. Exactly 5 pattern letters will use the
 * {@link TextStyle#NARROW narrow form}.
 * Pattern letters 'L', 'c', and 'q' specify the stand-alone form of the text styles.
 * <p>
 * <b>Number</b>: If the count of letters is one, then the value is output using
 * the minimum number of digits and without padding. Otherwise, the count of digits
 * is used as the width of the output field, with the value zero-padded as necessary.
 * The following pattern letters have constraints on the count of letters.
 * Only one letter of 'c' and 'F' can be specified.
 * Up to two letters of 'd', 'H', 'h', 'K', 'k', 'm', and 's' can be specified.
 * Up to three letters of 'D' can be specified.
 * <p>
 * <b>Number/Text</b>: If the count of pattern letters is 3 or greater, use the
 * Text rules above. Otherwise use the Number rules above.
 * <p>
 * <b>Fraction</b>: Outputs the nano-of-second field as a fraction-of-second.
 * The nano-of-second value has nine digits, thus the count of pattern letters
 * is from 1 to 9. If it is less than 9, then the nano-of-second value is
 * truncated, with only the most significant digits being output. When parsing
 * in strict mode, the number of parsed digits must match the count of pattern
 * letters. When parsing in lenient mode, the number of parsed digits must be at
 * least the count of pattern letters, up to 9 digits.
 * <p>
 * <b>Year</b>: The count of letters determines the minimum field width below
 * which padding is used. If the count of letters is two, then a
 * {@link DateTimeFormatterBuilder#appendValueReduced reduced} two digit form is
 * used. For printing, this outputs the rightmost two digits. For parsing, this
 * will parse using the base value of 2000, resulting in a year within the range
 * 2000 to 2099 inclusive. If the count of letters is less than four (but not
 * two), then the sign is only output for negative years as per
 * {@link SignStyle#NORMAL}. Otherwise, the sign is output if the pad width is
 * exceeded, as per {@link SignStyle#EXCEEDS_PAD}.
 * <p>
 * <b>ZoneId</b>: This outputs the time-zone ID, such as 'Europe/Paris'. If the
 * count of letters is two, then the time-zone ID is output. Any other count of
 * letters throws {@code IllegalArgumentException}.
 * <p>
 * <b>Zone names</b>: This outputs the display name of the time-zone ID. If the
 * count of letters is one, two or three, then the short name is output. If the
 * count of letters is four, then the full name is output. Five or more letters
 * throws {@code IllegalArgumentException}.
 * <p>
 * <b>Offset X and x</b>: This formats the offset based on the number of pattern
 * letters. One letter outputs just the hour, such as '+01', unless the minute
 * is non-zero in which case the minute is also output, such as '+0130'. Two
 * letters outputs the hour and minute, without a colon, such as '+0130'. Three
 * letters outputs the hour and minute, with a colon, such as '+01:30'. Four
 * letters outputs the hour and minute and optional second, without a colon,
 * such as '+013015'. Five letters outputs the hour and minute and optional
 * second, with a colon, such as '+01:30:15'. Six or more letters throws
 * {@code IllegalArgumentException}. Pattern letter 'X' (upper case) will output
 * 'Z' when the offset to be output would be zero, whereas pattern letter 'x'
 * (lower case) will output '+00', '+0000', or '+00:00'.
 * <p>
 * <b>Offset O</b>: This formats the localized offset based on the number of
 * pattern letters. One letter outputs the {@linkplain TextStyle#SHORT short}
 * form of the localized offset, which is localized offset text, such as 'GMT',
 * with hour without leading zero, optional 2-digit minute and second if
 * non-zero, and colon, for example 'GMT+8'. Four letters outputs the
 * {@linkplain TextStyle#FULL full} form, which is localized offset text,
 * such as 'GMT, with 2-digit hour and minute field, optional second field
 * if non-zero, and colon, for example 'GMT+08:00'. Any other count of letters
 * throws {@code IllegalArgumentException}.
 * <p>
 * <b>Offset Z</b>: This formats the offset based on the number of pattern
 * letters. One, two or three letters outputs the hour and minute, without a
 * colon, such as '+0130'. The output will be '+0000' when the offset is zero.
 * Four letters outputs the {@linkplain TextStyle#FULL full} form of localized
 * offset, equivalent to four letters of Offset-O. The output will be the
 * corresponding localized offset text if the offset is zero. Five
 * letters outputs the hour, minute, with optional second if non-zero, with
 * colon. It outputs 'Z' if the offset is zero.
 * Six or more letters throws {@code IllegalArgumentException}.
 * <p>
 * <b>Optional section</b>: The optional section markers work exactly like
 * calling {@link DateTimeFormatterBuilder#optionalStart()} and
 * {@link DateTimeFormatterBuilder#optionalEnd()}.
 * <p>
 * <b>Pad modifier</b>: Modifies the pattern that immediately follows to be
 * padded with spaces. The pad width is determined by the number of pattern
 * letters. This is the same as calling
 * {@link DateTimeFormatterBuilder#padNext(int)}.
 * <p>
 * For example, 'ppH' outputs the hour-of-day padded on the left with spaces to
 * a width of 2.
 * <p>
 * Any unrecognized letter is an error. Any non-letter character, other than
 * '[', ']', '{', '}', '#' and the single quote will be output directly.
 * Despite this, it is recommended to use single quotes around all characters
 * that you want to output directly to ensure that future changes do not break
 * your application.
 *
 * <h3 id="resolving">Resolving</h3>
 * Parsing is implemented as a two-phase operation.
 * First, the text is parsed using the layout defined by the formatter, producing
 * a {@code Map} of field to value, a {@code ZoneId} and a {@code Chronology}.
 * Second, the parsed data is <em>resolved</em>, by validating, combining and
 * simplifying the various fields into more useful ones.
 * <p>
 * Five parsing methods are supplied by this class.
 * Four of these perform both the parse and resolve phases.
 * The fifth method, {@link #parseUnresolved(CharSequence, ParsePosition)},
 * only performs the first phase, leaving the result unresolved.
 * As such, it is essentially a low-level operation.
 * <p>
 * The resolve phase is controlled by two parameters, set on this class.
 * <p>
 * The {@link ResolverStyle} is an enum that offers three different approaches,
 * strict, smart and lenient. The smart option is the default.
 * It can be set using {@link #withResolverStyle(ResolverStyle)}.
 * <p>
 * The {@link #withResolverFields(TemporalField...)} parameter allows the
 * set of fields that will be resolved to be filtered before resolving starts.
 * For example, if the formatter has parsed a year, month, day-of-month
 * and day-of-year, then there are two approaches to resolve a date:
 * (year + month + day-of-month) and (year + day-of-year).
 * The resolver fields allows one of the two approaches to be selected.
 * If no resolver fields are set then both approaches must result in the same date.
 * <p>
 * Resolving separate fields to form a complete date and time is a complex
 * process with behaviour distributed across a number of classes.
 * It follows these steps:
 * <ol>
 * <li>The chronology is determined.
 * The chronology of the result is either the chronology that was parsed,
 * or if no chronology was parsed, it is the chronology set on this class,
 * or if that is null, it is {@code IsoChronology}.
 * <li>The {@code ChronoField} date fields are resolved.
 * This is achieved using {@link Chronology#resolveDate(Map, ResolverStyle)}.
 * Documentation about field resolution is located in the implementation
 * of {@code Chronology}.
 * <li>The {@code ChronoField} time fields are resolved.
 * This is documented on {@link ChronoField} and is the same for all chronologies.
 * <li>Any fields that are not {@code ChronoField} are processed.
 * This is achieved using {@link TemporalField#resolve(Map, Chronology, ZoneId, ResolverStyle)}.
 * Documentation about field resolution is located in the implementation
 * of {@code TemporalField}.
 * <li>The {@code ChronoField} date and time fields are re-resolved.
 * This allows fields in step four to produce {@code ChronoField} values
 * and have them be processed into dates and times.
 * <li>A {@code Time} is formed if there is at least an hour-of-day available.
 * This involves providing default values for minute, second and fraction of second.
 * <li>Any remaining unresolved fields are cross-checked against any
 * date and/or time that was resolved. Thus, an earlier stage would resolve
 * (year + month + day-of-month) to a date, and this stage would check that
 * day-of-week was valid for the date.
 * <li>If an {@linkplain #parsedExcessDays() excess number of days}
 * was parsed then it is added to the date if a date is available.
 * </ol>
 *
 * @implSpec
 * This class is immutable and thread-safe.
 *
 * @since 1.8
 */
object DateTimeFormatter {
  /**
   * Creates a formatter using the specified pattern.
   * <p>
   * This method will create a formatter based on a simple
   * <a href="#patterns">pattern of letters and symbols</a>
   * as described in the class documentation.
   * For example, {@code d MMM uuuu} will format 2011-12-03 as '3 Dec 2011'.
   * <p>
   * The formatter will use the {@link Locale#getDefault(Locale.Category) default FORMAT locale}.
   * This can be changed using {@link DateTimeFormatter#withLocale(Locale)} on the returned formatter
   * Alternatively use the {@link #ofPattern(String, Locale)} variant of this method.
   * <p>
   * The returned formatter has no override chronology or zone.
   * It uses {@link ResolverStyle#SMART SMART} resolver style.
   *
   * @param pattern  the pattern to use, not null
   * @return the formatter based on the pattern, not null
   * @throws IllegalArgumentException if the pattern is invalid
   * @see DateTimeFormatterBuilder#appendPattern(String)
   */
  def ofPattern(pattern: String): DateTimeFormatter = {
     new DateTimeFormatterBuilder().appendPattern(pattern).toFormatter
  }

  /**
   * Creates a formatter using the specified pattern and locale.
   * <p>
   * This method will create a formatter based on a simple
   * <a href="#patterns">pattern of letters and symbols</a>
   * as described in the class documentation.
   * For example, {@code d MMM uuuu} will format 2011-12-03 as '3 Dec 2011'.
   * <p>
   * The formatter will use the specified locale.
   * This can be changed using {@link DateTimeFormatter#withLocale(Locale)} on the returned formatter
   * <p>
   * The returned formatter has no override chronology or zone.
   * It uses {@link ResolverStyle#SMART SMART} resolver style.
   *
   * @param pattern  the pattern to use, not null
   * @param locale  the locale to use, not null
   * @return the formatter based on the pattern, not null
   * @throws IllegalArgumentException if the pattern is invalid
   * @see DateTimeFormatterBuilder#appendPattern(String)
   */
  def ofPattern(pattern: String, locale: Locale): DateTimeFormatter = {
     new DateTimeFormatterBuilder().appendPattern(pattern).toFormatter(locale)
  }

  /**
   * Returns a locale specific date format for the ISO chronology.
   * <p>
   * This returns a formatter that will format or parse a date.
   * The exact format pattern used varies by locale.
   * <p>
   * The locale is determined from the formatter. The formatter returned directly by
   * this method will use the {@link Locale#getDefault(Locale.Category) default FORMAT locale}.
   * The locale can be controlled using {@link DateTimeFormatter#withLocale(Locale) withLocale(Locale)}
   * on the result of this method.
   * <p>
   * Note that the localized pattern is looked up lazily.
   * This {@code DateTimeFormatter} holds the style required and the locale,
   * looking up the pattern required on demand.
   * <p>
   * The returned formatter has a chronology of ISO set to ensure dates in
   * other calendar systems are correctly converted.
   * It has no override zone and uses the {@link ResolverStyle#SMART SMART} resolver style.
   *
   * @param dateStyle  the formatter style to obtain, not null
   * @return the date formatter, not null
   */
  def ofLocalizedDate(dateStyle: FormatStyle): DateTimeFormatter = {

     new DateTimeFormatterBuilder().appendLocalized(dateStyle, null).toFormatter(ResolverStyle.SMART, IsoChronology.INSTANCE)
  }

  /**
   * Returns a locale specific time format for the ISO chronology.
   * <p>
   * This returns a formatter that will format or parse a time.
   * The exact format pattern used varies by locale.
   * <p>
   * The locale is determined from the formatter. The formatter returned directly by
   * this method will use the {@link Locale#getDefault(Locale.Category) default FORMAT locale}.
   * The locale can be controlled using {@link DateTimeFormatter#withLocale(Locale) withLocale(Locale)}
   * on the result of this method.
   * <p>
   * Note that the localized pattern is looked up lazily.
   * This {@code DateTimeFormatter} holds the style required and the locale,
   * looking up the pattern required on demand.
   * <p>
   * The returned formatter has a chronology of ISO set to ensure dates in
   * other calendar systems are correctly converted.
   * It has no override zone and uses the {@link ResolverStyle#SMART SMART} resolver style.
   *
   * @param timeStyle  the formatter style to obtain, not null
   * @return the time formatter, not null
   */
  def ofLocalizedTime(timeStyle: FormatStyle): DateTimeFormatter = {

     new DateTimeFormatterBuilder().appendLocalized(null, timeStyle).toFormatter(ResolverStyle.SMART, IsoChronology.INSTANCE)
  }

  /**
   * Returns a locale specific date-time formatter for the ISO chronology.
   * <p>
   * This returns a formatter that will format or parse a date-time.
   * The exact format pattern used varies by locale.
   * <p>
   * The locale is determined from the formatter. The formatter returned directly by
   * this method will use the {@link Locale#getDefault(Locale.Category) default FORMAT locale}.
   * The locale can be controlled using {@link DateTimeFormatter#withLocale(Locale) withLocale(Locale)}
   * on the result of this method.
   * <p>
   * Note that the localized pattern is looked up lazily.
   * This {@code DateTimeFormatter} holds the style required and the locale,
   * looking up the pattern required on demand.
   * <p>
   * The returned formatter has a chronology of ISO set to ensure dates in
   * other calendar systems are correctly converted.
   * It has no override zone and uses the {@link ResolverStyle#SMART SMART} resolver style.
   *
   * @param dateTimeStyle  the formatter style to obtain, not null
   * @return the date-time formatter, not null
   */
  def ofLocalizedDateTime(dateTimeStyle: FormatStyle): DateTimeFormatter = {

     new DateTimeFormatterBuilder().appendLocalized(dateTimeStyle, dateTimeStyle).toFormatter(ResolverStyle.SMART, IsoChronology.INSTANCE)
  }

  /**
   * Returns a locale specific date and time format for the ISO chronology.
   * <p>
   * This returns a formatter that will format or parse a date-time.
   * The exact format pattern used varies by locale.
   * <p>
   * The locale is determined from the formatter. The formatter returned directly by
   * this method will use the {@link Locale#getDefault() default FORMAT locale}.
   * The locale can be controlled using {@link DateTimeFormatter#withLocale(Locale) withLocale(Locale)}
   * on the result of this method.
   * <p>
   * Note that the localized pattern is looked up lazily.
   * This {@code DateTimeFormatter} holds the style required and the locale,
   * looking up the pattern required on demand.
   * <p>
   * The returned formatter has a chronology of ISO set to ensure dates in
   * other calendar systems are correctly converted.
   * It has no override zone and uses the {@link ResolverStyle#SMART SMART} resolver style.
   *
   * @param dateStyle  the date formatter style to obtain, not null
   * @param timeStyle  the time formatter style to obtain, not null
   * @return the date, time or date-time formatter, not null
   */
  def ofLocalizedDateTime(dateStyle: FormatStyle, timeStyle: FormatStyle): DateTimeFormatter = {


     new DateTimeFormatterBuilder().appendLocalized(dateStyle, timeStyle).toFormatter(ResolverStyle.SMART, IsoChronology.INSTANCE)
  }

  /**
   * A query that provides access to the excess days that were parsed.
   * <p>
   * This returns a singleton {@linkplain TemporalQuery query} that provides
   * access to additional information from the parse. The query always returns
   * a non-null period, with a zero period returned instead of null.
   * <p>
   * There are two situations where this query may return a non-zero period.
   * <p><ul>
   * <li>If the {@code ResolverStyle} is {@code LENIENT} and a time is parsed
   * without a date, then the complete result of the parse consists of a
   * {@code Time} and an excess {@code Period} in days.
   * <p>
   * <li>If the {@code ResolverStyle} is {@code SMART} and a time is parsed
   * without a date where the time is 24:00:00, then the complete result of
   * the parse consists of a {@code Time} of 00:00:00 and an excess
   * {@code Period} of one day.
   * </ul>
   * <p>
   * In both cases, if a complete {@code ChronoLocalDateTime} or {@code Instant}
   * is parsed, then the excess days are added to the date part.
   * As a result, this query will return a zero period.
   * <p>
   * The {@code SMART} behaviour handles the common "end of day" 24:00 value.
   * Processing in {@code LENIENT} mode also produces the same result:
   * {{{
   * Text to parse        Parsed object                         Excess days
   * "2012-12-03T00:00"   DateTime.of(2012, 12, 3, 0, 0)   ZERO
   * "2012-12-03T24:00"   DateTime.of(2012, 12, 4, 0, 0)   ZERO
   * "00:00"              Time.of(0, 0)                    ZERO
   * "24:00"              Time.of(0, 0)                    Period.ofDays(1)
   * }}}
   * The query can be used as follows:
   * {{{
   * TemporalAccessor parsed = formatter.parse(str);
   * Time time = parsed.query(Time::from);
   * Period extraDays = parsed.query(DateTimeFormatter.parsedExcessDays());
   * }}}
   * @return a query that provides access to the excess days that were parsed
   */
  final def parsedExcessDays: TemporalQuery[Period] = {
     PARSED_EXCESS_DAYS
  }

  /**
   * A query that provides access to whether a leap-second was parsed.
   * <p>
   * This returns a singleton {@linkplain TemporalQuery query} that provides
   * access to additional information from the parse. The query always returns
   * a non-null boolean, true if parsing saw a leap-second, false if not.
   * <p>
   * Instant parsing handles the special "leap second" time of '23:59:60'.
   * Leap seconds occur at '23:59:60' in the UTC time-zone, but at other
   * local times in different time-zones. To avoid this potential ambiguity,
   * the handling of leap-seconds is limited to
   * {@link DateTimeFormatterBuilder#appendInstant()}, as that method
   * always parses the instant with the UTC zone offset.
   * <p>
   * If the time '23:59:60' is received, then a simple conversion is applied,
   * replacing the second-of-minute of 60 with 59. This query can be used
   * on the parse result to determine if the leap-second adjustment was made.
   * The query will return one second of excess if it did adjust to remove
   * the leap-second, and zero if not. Note that applying a leap-second
   * smoothing mechanism, such as UTC-SLS, is the responsibility of the
   * application, as follows:
   * {{{
   * TemporalAccessor parsed = formatter.parse(str);
   * Instant instant = parsed.query(Instant::from);
   * if (parsed.query(DateTimeFormatter.parsedLeapSecond())) {
   * // validate leap-second is correct and apply correct smoothing
   * }
   * }}}
   * @return a query that provides access to whether a leap-second was parsed
   */
  final def parsedLeapSecond: TemporalQuery[Boolean] = {
     PARSED_LEAP_SECOND
  }

  /**
   * The ISO date formatter that formats or parses a date without an
   * offset, such as '2011-12-03'.
   * <p>
   * This returns an immutable formatter capable of formatting and parsing
   * the ISO-8601 extended local date format.
   * The format consists of:
   * <p><ul>
   * <li>Four digits or more for the {@link ChronoField#YEAR year}.
   * Years in the range 0000 to 9999 will be pre-padded by zero to ensure four digits.
   * Years outside that range will have a prefixed positive or negative symbol.
   * <li>A dash
   * <li>Two digits for the {@link ChronoField#MONTH_OF_YEAR month-of-year}.
   * This is pre-padded by zero to ensure two digits.
   * <li>A dash
   * <li>Two digits for the {@link ChronoField#DAY_OF_MONTH day-of-month}.
   * This is pre-padded by zero to ensure two digits.
   * </ul>
   * <p>
   * The returned formatter has a chronology of ISO set to ensure dates in
   * other calendar systems are correctly converted.
   * It has no override zone and uses the {@link ResolverStyle#STRICT STRICT} resolver style.
   */
  final val ISO_LOCAL_DATE: DateTimeFormatter = null
  /**
   * The ISO date formatter that formats or parses a date with an
   * offset, such as '2011-12-03+01:00'.
   * <p>
   * This returns an immutable formatter capable of formatting and parsing
   * the ISO-8601 extended offset date format.
   * The format consists of:
   * <p><ul>
   * <li>The {@link #ISO_LOCAL_DATE}
   * <li>The {@link ZoneOffset#getId() offset ID}. If the offset has seconds then
   * they will be handled even though this is not part of the ISO-8601 standard.
   * Parsing is case insensitive.
   * </ul>
   * <p>
   * The returned formatter has a chronology of ISO set to ensure dates in
   * other calendar systems are correctly converted.
   * It has no override zone and uses the {@link ResolverStyle#STRICT STRICT} resolver style.
   */
  final val ISO_OFFSET_DATE: DateTimeFormatter = null
  /**
   * The ISO date formatter that formats or parses a date with the
   * offset if available, such as '2011-12-03' or '2011-12-03+01:00'.
   * <p>
   * This returns an immutable formatter capable of formatting and parsing
   * the ISO-8601 extended date format.
   * The format consists of:
   * <p><ul>
   * <li>The {@link #ISO_LOCAL_DATE}
   * <li>If the offset is not available then the format is complete.
   * <li>The {@link ZoneOffset#getId() offset ID}. If the offset has seconds then
   * they will be handled even though this is not part of the ISO-8601 standard.
   * Parsing is case insensitive.
   * </ul>
   * <p>
   * As this formatter has an optional element, it may be necessary to parse using
   * {@link DateTimeFormatter#parseBest}.
   * <p>
   * The returned formatter has a chronology of ISO set to ensure dates in
   * other calendar systems are correctly converted.
   * It has no override zone and uses the {@link ResolverStyle#STRICT STRICT} resolver style.
   */
  final val ISO_DATE: DateTimeFormatter = null
  /**
   * The ISO time formatter that formats or parses a time without an
   * offset, such as '10:15' or '10:15:30'.
   * <p>
   * This returns an immutable formatter capable of formatting and parsing
   * the ISO-8601 extended local time format.
   * The format consists of:
   * <p><ul>
   * <li>Two digits for the {@link ChronoField#HOUR_OF_DAY hour-of-day}.
   * This is pre-padded by zero to ensure two digits.
   * <li>A colon
   * <li>Two digits for the {@link ChronoField#MINUTE_OF_HOUR minute-of-hour}.
   * This is pre-padded by zero to ensure two digits.
   * <li>If the second-of-minute is not available then the format is complete.
   * <li>A colon
   * <li>Two digits for the {@link ChronoField#SECOND_OF_MINUTE second-of-minute}.
   * This is pre-padded by zero to ensure two digits.
   * <li>If the nano-of-second is zero or not available then the format is complete.
   * <li>A decimal point
   * <li>One to nine digits for the {@link ChronoField#NANO_OF_SECOND nano-of-second}.
   * As many digits will be output as required.
   * </ul>
   * <p>
   * The returned formatter has no override chronology or zone.
   * It uses the {@link ResolverStyle#STRICT STRICT} resolver style.
   */
  final val ISO_LOCAL_TIME: DateTimeFormatter = null
  /**
   * The ISO time formatter that formats or parses a time with an
   * offset, such as '10:15+01:00' or '10:15:30+01:00'.
   * <p>
   * This returns an immutable formatter capable of formatting and parsing
   * the ISO-8601 extended offset time format.
   * The format consists of:
   * <p><ul>
   * <li>The {@link #ISO_LOCAL_TIME}
   * <li>The {@link ZoneOffset#getId() offset ID}. If the offset has seconds then
   * they will be handled even though this is not part of the ISO-8601 standard.
   * Parsing is case insensitive.
   * </ul>
   * <p>
   * The returned formatter has no override chronology or zone.
   * It uses the {@link ResolverStyle#STRICT STRICT} resolver style.
   */
  final val ISO_OFFSET_TIME: DateTimeFormatter = null
  /**
   * The ISO time formatter that formats or parses a time, with the
   * offset if available, such as '10:15', '10:15:30' or '10:15:30+01:00'.
   * <p>
   * This returns an immutable formatter capable of formatting and parsing
   * the ISO-8601 extended offset time format.
   * The format consists of:
   * <p><ul>
   * <li>The {@link #ISO_LOCAL_TIME}
   * <li>If the offset is not available then the format is complete.
   * <li>The {@link ZoneOffset#getId() offset ID}. If the offset has seconds then
   * they will be handled even though this is not part of the ISO-8601 standard.
   * Parsing is case insensitive.
   * </ul>
   * <p>
   * As this formatter has an optional element, it may be necessary to parse using
   * {@link DateTimeFormatter#parseBest}.
   * <p>
   * The returned formatter has no override chronology or zone.
   * It uses the {@link ResolverStyle#STRICT STRICT} resolver style.
   */
  final val ISO_TIME: DateTimeFormatter = null
  /**
   * The ISO date-time formatter that formats or parses a date-time without
   * an offset, such as '2011-12-03T10:15:30'.
   * <p>
   * This returns an immutable formatter capable of formatting and parsing
   * the ISO-8601 extended offset date-time format.
   * The format consists of:
   * <p><ul>
   * <li>The {@link #ISO_LOCAL_DATE}
   * <li>The letter 'T'. Parsing is case insensitive.
   * <li>The {@link #ISO_LOCAL_TIME}
   * </ul>
   * <p>
   * The returned formatter has a chronology of ISO set to ensure dates in
   * other calendar systems are correctly converted.
   * It has no override zone and uses the {@link ResolverStyle#STRICT STRICT} resolver style.
   */
  final val ISO_LOCAL_DATE_TIME: DateTimeFormatter = null
  /**
   * The ISO date-time formatter that formats or parses a date-time with an
   * offset, such as '2011-12-03T10:15:30+01:00'.
   * <p>
   * This returns an immutable formatter capable of formatting and parsing
   * the ISO-8601 extended offset date-time format.
   * The format consists of:
   * <p><ul>
   * <li>The {@link #ISO_LOCAL_DATE_TIME}
   * <li>The {@link ZoneOffset#getId() offset ID}. If the offset has seconds then
   * they will be handled even though this is not part of the ISO-8601 standard.
   * Parsing is case insensitive.
   * </ul>
   * <p>
   * The returned formatter has a chronology of ISO set to ensure dates in
   * other calendar systems are correctly converted.
   * It has no override zone and uses the {@link ResolverStyle#STRICT STRICT} resolver style.
   */
  final val ISO_OFFSET_DATE_TIME: DateTimeFormatter = null
  /**
   * The ISO-like date-time formatter that formats or parses a date-time with
   * offset and zone, such as '2011-12-03T10:15:30+01:00[Europe/Paris]'.
   * <p>
   * This returns an immutable formatter capable of formatting and parsing
   * a format that extends the ISO-8601 extended offset date-time format
   * to add the time-zone.
   * The section in square brackets is not part of the ISO-8601 standard.
   * The format consists of:
   * <p><ul>
   * <li>The {@link #ISO_OFFSET_DATE_TIME}
   * <li>If the zone ID is not available or is a {@code ZoneOffset} then the format is complete.
   * <li>An open square bracket '['.
   * <li>The {@link ZoneId#getId() zone ID}. This is not part of the ISO-8601 standard.
   * Parsing is case sensitive.
   * <li>A close square bracket ']'.
   * </ul>
   * <p>
   * The returned formatter has a chronology of ISO set to ensure dates in
   * other calendar systems are correctly converted.
   * It has no override zone and uses the {@link ResolverStyle#STRICT STRICT} resolver style.
   */
  final val ISO_ZONED_DATE_TIME: DateTimeFormatter = null
  /**
   * The ISO-like date-time formatter that formats or parses a date-time with
   * the offset and zone if available, such as '2011-12-03T10:15:30',
   * '2011-12-03T10:15:30+01:00' or '2011-12-03T10:15:30+01:00[Europe/Paris]'.
   * <p>
   * This returns an immutable formatter capable of formatting and parsing
   * the ISO-8601 extended local or offset date-time format, as well as the
   * extended non-ISO form specifying the time-zone.
   * The format consists of:
   * <p><ul>
   * <li>The {@link #ISO_LOCAL_DATE_TIME}
   * <li>If the offset is not available to format or parse then the format is complete.
   * <li>The {@link ZoneOffset#getId() offset ID}. If the offset has seconds then
   * they will be handled even though this is not part of the ISO-8601 standard.
   * <li>If the zone ID is not available or is a {@code ZoneOffset} then the format is complete.
   * <li>An open square bracket '['.
   * <li>The {@link ZoneId#getId() zone ID}. This is not part of the ISO-8601 standard.
   * Parsing is case sensitive.
   * <li>A close square bracket ']'.
   * </ul>
   * <p>
   * As this formatter has an optional element, it may be necessary to parse using
   * {@link DateTimeFormatter#parseBest}.
   * <p>
   * The returned formatter has a chronology of ISO set to ensure dates in
   * other calendar systems are correctly converted.
   * It has no override zone and uses the {@link ResolverStyle#STRICT STRICT} resolver style.
   */
  final val ISO_DATE_TIME: DateTimeFormatter = null
  /**
   * The ISO date formatter that formats or parses the ordinal date
   * without an offset, such as '2012-337'.
   * <p>
   * This returns an immutable formatter capable of formatting and parsing
   * the ISO-8601 extended ordinal date format.
   * The format consists of:
   * <p><ul>
   * <li>Four digits or more for the {@link ChronoField#YEAR year}.
   * Years in the range 0000 to 9999 will be pre-padded by zero to ensure four digits.
   * Years outside that range will have a prefixed positive or negative symbol.
   * <li>A dash
   * <li>Three digits for the {@link ChronoField#DAY_OF_YEAR day-of-year}.
   * This is pre-padded by zero to ensure three digits.
   * <li>If the offset is not available to format or parse then the format is complete.
   * <li>The {@link ZoneOffset#getId() offset ID}. If the offset has seconds then
   * they will be handled even though this is not part of the ISO-8601 standard.
   * Parsing is case insensitive.
   * </ul>
   * <p>
   * As this formatter has an optional element, it may be necessary to parse using
   * {@link DateTimeFormatter#parseBest}.
   * <p>
   * The returned formatter has a chronology of ISO set to ensure dates in
   * other calendar systems are correctly converted.
   * It has no override zone and uses the {@link ResolverStyle#STRICT STRICT} resolver style.
   */
  final val ISO_ORDINAL_DATE: DateTimeFormatter = null
  /**
   * The ISO date formatter that formats or parses the week-based date
   * without an offset, such as '2012-W48-6'.
   * <p>
   * This returns an immutable formatter capable of formatting and parsing
   * the ISO-8601 extended week-based date format.
   * The format consists of:
   * <p><ul>
   * <li>Four digits or more for the {@link IsoFields#WEEK_BASED_YEAR week-based-year}.
   * Years in the range 0000 to 9999 will be pre-padded by zero to ensure four digits.
   * Years outside that range will have a prefixed positive or negative symbol.
   * <li>A dash
   * <li>The letter 'W'. Parsing is case insensitive.
   * <li>Two digits for the {@link IsoFields#WEEK_OF_WEEK_BASED_YEAR week-of-week-based-year}.
   * This is pre-padded by zero to ensure three digits.
   * <li>A dash
   * <li>One digit for the {@link ChronoField#DAY_OF_WEEK day-of-week}.
   * The value run from Monday (1) to Sunday (7).
   * <li>If the offset is not available to format or parse then the format is complete.
   * <li>The {@link ZoneOffset#getId() offset ID}. If the offset has seconds then
   * they will be handled even though this is not part of the ISO-8601 standard.
   * Parsing is case insensitive.
   * </ul>
   * <p>
   * As this formatter has an optional element, it may be necessary to parse using
   * {@link DateTimeFormatter#parseBest}.
   * <p>
   * The returned formatter has a chronology of ISO set to ensure dates in
   * other calendar systems are correctly converted.
   * It has no override zone and uses the {@link ResolverStyle#STRICT STRICT} resolver style.
   */
  final val ISO_WEEK_DATE: DateTimeFormatter = null
  /**
   * The ISO instant formatter that formats or parses an instant in UTC,
   * such as '2011-12-03T10:15:30Z'.
   * <p>
   * This returns an immutable formatter capable of formatting and parsing
   * the ISO-8601 instant format.
   * When formatting, the second-of-minute is always output.
   * The nano-of-second outputs zero, three, six or nine digits digits as necessary.
   * When parsing, time to at least the seconds field is required.
   * Fractional seconds from zero to nine are parsed.
   * The localized decimal style is not used.
   * <p>
   * This is a special case formatter intended to allow a human readable form
   * of an {@link java.time.Instant}. The {@code Instant} class is designed to
   * only represent a point in time and internally stores a value in nanoseconds
   * from a fixed epoch of 1970-01-01Z. As such, an {@code Instant} cannot be
   * formatted as a date or time without providing some form of time-zone.
   * This formatter allows the {@code Instant} to be formatted, by providing
   * a suitable conversion using {@code ZoneOffset.UTC}.
   * <p>
   * The format consists of:
   * <p><ul>
   * <li>The {@link #ISO_OFFSET_DATE_TIME} where the instant is converted from
   * {@link ChronoField#INSTANT_SECONDS} and {@link ChronoField#NANO_OF_SECOND}
   * using the {@code UTC} offset. Parsing is case insensitive.
   * </ul>
   * <p>
   * The returned formatter has no override chronology or zone.
   * It uses the {@link ResolverStyle#STRICT STRICT} resolver style.
   */
  final val ISO_INSTANT: DateTimeFormatter = null
  /**
   * The ISO date formatter that formats or parses a date without an
   * offset, such as '20111203'.
   * <p>
   * This returns an immutable formatter capable of formatting and parsing
   * the ISO-8601 basic local date format.
   * The format consists of:
   * <p><ul>
   * <li>Four digits for the {@link ChronoField#YEAR year}.
   * Only years in the range 0000 to 9999 are supported.
   * <li>Two digits for the {@link ChronoField#MONTH_OF_YEAR month-of-year}.
   * This is pre-padded by zero to ensure two digits.
   * <li>Two digits for the {@link ChronoField#DAY_OF_MONTH day-of-month}.
   * This is pre-padded by zero to ensure two digits.
   * <li>If the offset is not available to format or parse then the format is complete.
   * <li>The {@link ZoneOffset#getId() offset ID} without colons. If the offset has
   * seconds then they will be handled even though this is not part of the ISO-8601 standard.
   * Parsing is case insensitive.
   * </ul>
   * <p>
   * As this formatter has an optional element, it may be necessary to parse using
   * {@link DateTimeFormatter#parseBest}.
   * <p>
   * The returned formatter has a chronology of ISO set to ensure dates in
   * other calendar systems are correctly converted.
   * It has no override zone and uses the {@link ResolverStyle#STRICT STRICT} resolver style.
   */
  final val BASIC_ISO_DATE: DateTimeFormatter = null
  /**
   * The RFC-1123 date-time formatter, such as 'Tue, 3 Jun 2008 11:05:30 GMT'.
   * <p>
   * This returns an immutable formatter capable of formatting and parsing
   * most of the RFC-1123 format.
   * RFC-1123 updates RFC-822 changing the year from two digits to four.
   * This implementation requires a four digit year.
   * This implementation also does not handle North American or military zone
   * names, only 'GMT' and offset amounts.
   * <p>
   * The format consists of:
   * <p><ul>
   * <li>If the day-of-week is not available to format or parse then jump to day-of-month.
   * <li>Three letter {@link ChronoField#DAY_OF_WEEK day-of-week} in English.
   * <li>A comma
   * <li>A space
   * <li>One or two digits for the {@link ChronoField#DAY_OF_MONTH day-of-month}.
   * <li>A space
   * <li>Three letter {@link ChronoField#MONTH_OF_YEAR month-of-year} in English.
   * <li>A space
   * <li>Four digits for the {@link ChronoField#YEAR year}.
   * Only years in the range 0000 to 9999 are supported.
   * <li>A space
   * <li>Two digits for the {@link ChronoField#HOUR_OF_DAY hour-of-day}.
   * This is pre-padded by zero to ensure two digits.
   * <li>A colon
   * <li>Two digits for the {@link ChronoField#MINUTE_OF_HOUR minute-of-hour}.
   * This is pre-padded by zero to ensure two digits.
   * <li>If the second-of-minute is not available then jump to the next space.
   * <li>A colon
   * <li>Two digits for the {@link ChronoField#SECOND_OF_MINUTE second-of-minute}.
   * This is pre-padded by zero to ensure two digits.
   * <li>A space
   * <li>The {@link ZoneOffset#getId() offset ID} without colons or seconds.
   * An offset of zero uses "GMT". North American zone names and military zone names are not handled.
   * </ul>
   * <p>
   * Parsing is case insensitive.
   * <p>
   * The returned formatter has a chronology of ISO set to ensure dates in
   * other calendar systems are correctly converted.
   * It has no override zone and uses the {@link ResolverStyle#SMART SMART} resolver style.
   */
  final val RFC_1123_DATE_TIME: DateTimeFormatter = null
  private final val PARSED_EXCESS_DAYS: TemporalQuery[Period] = t -> {
    if (t instanceof Parsed) {
       ((Parsed) t).excessDays;
    } else {
       Period.ZERO;
    }
  }
  private final val PARSED_LEAP_SECOND: TemporalQuery[Boolean] = t -> {
    if (t instanceof Parsed) {
       ((Parsed) t).leapSecond;
    } else {
       Boolean.FALSE;
    }
  }

  /**
   * Implements the classic Java Format API.
   * @serial exclude
   */
  @SuppressWarnings(Array("serial")) private[format] class ClassicFormat extends Format {
    /** Constructor. */
    def this(formatter: DateTimeFormatter, parseType: TemporalQuery[_]) {

      this.formatter = formatter
      this.parseType = parseType
    }

    def format(obj: AnyRef, toAppendTo: StringBuffer, pos: FieldPosition): StringBuffer = {



      if (obj.isInstanceOf[TemporalAccessor] == false) {
        throw new IllegalArgumentException("Format target must implement TemporalAccessor")
      }
      pos.setBeginIndex(0)
      pos.setEndIndex(0)
      try {
        formatter.formatTo(obj.asInstanceOf[TemporalAccessor], toAppendTo)
      }
      catch {
        case ex: RuntimeException => {
          throw new IllegalArgumentException(ex.getMessage, ex)
        }
      }
       toAppendTo
    }

    override def parseObject(text: String): AnyRef = {

      try {
        if (parseType == null) {
           formatter.parseResolved0(text, null)
        }
         formatter.parse(text, parseType)
      }
      catch {
        case ex: DateTimeParseException => {
          throw new ParseException(ex.getMessage, ex.getErrorIndex)
        }
        case ex: RuntimeException => {
          throw new ParseException(ex.getMessage, 0).initCause(ex).asInstanceOf[ParseException]
        }
      }
    }

    def parseObject(text: String, pos: ParsePosition): AnyRef = {

      var unresolved: Parsed = null
      try {
        unresolved = formatter.parseUnresolved0(text, pos)
      }
      catch {
        case ex: IndexOutOfBoundsException => {
          if (pos.getErrorIndex < 0) {
            pos.setErrorIndex(0)
          }
           null
        }
      }
      if (unresolved == null) {
        if (pos.getErrorIndex < 0) {
          pos.setErrorIndex(0)
        }
         null
      }
      try {
        val resolved: TemporalAccessor = unresolved.resolve(formatter.resolverStyle, formatter.resolverFields)
        if (parseType == null) {
           resolved
        }
         resolved.query(parseType)
      }
      catch {
        case ex: RuntimeException => {
          pos.setErrorIndex(0)
           null
        }
      }
    }

    /** The formatter. */
    private final val formatter: DateTimeFormatter = null
    /** The type to be parsed. */
    private final val parseType: TemporalQuery[_] = null
  }

}

final class DateTimeFormatter {
  /**
   * Constructor.
   *
   * @param printerParser  the printer/parser to use, not null
   * @param locale  the locale to use, not null
   * @param decimalStyle  the DecimalStyle to use, not null
   * @param resolverStyle  the resolver style to use, not null
   * @param resolverFields  the fields to use during resolving, null for all fields
   * @param chrono  the chronology to use, null for no override
   * @param zone  the zone to use, null for no override
   */
  private[format] def this(printerParser: DateTimeFormatterBuilder.CompositePrinterParser, locale: Locale, decimalStyle: DecimalStyle, resolverStyle: ResolverStyle, resolverFields: Set[TemporalField], chrono: Chronology, zone: ZoneId) {

    this.printerParser =
    this.resolverFields = resolverFields
    this.locale =
    this.decimalStyle =
    this.resolverStyle =
    this.chrono = chrono
    this.zone = zone
  }

  /**
   * Gets the locale to be used during formatting.
   * <p>
   * This is used to lookup any part of the formatter needing specific
   * localization, such as the text or localized pattern.
   *
   * @return the locale of this formatter, not null
   */
  def getLocale: Locale = {
     locale
  }

  /**
   * Returns a copy of this formatter with a new locale.
   * <p>
   * This is used to lookup any part of the formatter needing specific
   * localization, such as the text or localized pattern.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param locale  the new locale, not null
   * @return a formatter based on this formatter with the requested locale, not null
   */
  def withLocale(locale: Locale): DateTimeFormatter = {
    if (this.locale == locale) {
       this
    }
     new DateTimeFormatter(printerParser, locale, decimalStyle, resolverStyle, resolverFields, chrono, zone)
  }

  /**
   * Gets the DecimalStyle to be used during formatting.
   *
   * @return the locale of this formatter, not null
   */
  def getDecimalStyle: DecimalStyle = {
     decimalStyle
  }

  /**
   * Returns a copy of this formatter with a new DecimalStyle.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param decimalStyle  the new DecimalStyle, not null
   * @return a formatter based on this formatter with the requested DecimalStyle, not null
   */
  def withDecimalStyle(decimalStyle: DecimalStyle): DateTimeFormatter = {
    if (this.decimalStyle == decimalStyle) {
       this
    }
     new DateTimeFormatter(printerParser, locale, decimalStyle, resolverStyle, resolverFields, chrono, zone)
  }

  /**
   * Gets the overriding chronology to be used during formatting.
   * <p>
   * This returns the override chronology, used to convert dates.
   * By default, a formatter has no override chronology, returning null.
   * See {@link #withChronology(Chronology)} for more details on overriding.
   *
   * @return the override chronology of this formatter, null if no override
   */
  def getChronology: Chronology = {
     chrono
  }

  /**
   * Returns a copy of this formatter with a new override chronology.
   * <p>
   * This returns a formatter with similar state to this formatter but
   * with the override chronology set.
   * By default, a formatter has no override chronology, returning null.
   * <p>
   * If an override is added, then any date that is formatted or parsed will be affected.
   * <p>
   * When formatting, if the temporal object contains a date, then it will
   * be converted to a date in the override chronology.
   * Whether the temporal contains a date is determined by querying the
   * {@link ChronoField#EPOCH_DAY EPOCH_DAY} field.
   * Any time or zone will be retained unaltered unless overridden.
   * <p>
   * If the temporal object does not contain a date, but does contain one
   * or more {@code ChronoField} date fields, then a {@code DateTimeException}
   * is thrown. In all other cases, the override chronology is added to the temporal,
   * replacing any previous chronology, but without changing the date/time.
   * <p>
   * When parsing, there are two distinct cases to consider.
   * If a chronology has been parsed directly from the text, perhaps because
   * {@link DateTimeFormatterBuilder#appendChronologyId()} was used, then
   * this override chronology has no effect.
   * If no zone has been parsed, then this override chronology will be used
   * to interpret the {@code ChronoField} values into a date according to the
   * date resolving rules of the chronology.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param chrono  the new chronology, null if no override
   * @return a formatter based on this formatter with the requested override chronology, not null
   */
  def withChronology(chrono: Chronology): DateTimeFormatter = {
    if (Objects.equals(this.chrono, chrono)) {
       this
    }
     new DateTimeFormatter(printerParser, locale, decimalStyle, resolverStyle, resolverFields, chrono, zone)
  }

  /**
   * Gets the overriding zone to be used during formatting.
   * <p>
   * This returns the override zone, used to convert instants.
   * By default, a formatter has no override zone, returning null.
   * See {@link #withZone(ZoneId)} for more details on overriding.
   *
   * @return the override zone of this formatter, null if no override
   */
  def getZone: ZoneId = {
     zone
  }

  /**
   * Returns a copy of this formatter with a new override zone.
   * <p>
   * This returns a formatter with similar state to this formatter but
   * with the override zone set.
   * By default, a formatter has no override zone, returning null.
   * <p>
   * If an override is added, then any instant that is formatted or parsed will be affected.
   * <p>
   * When formatting, if the temporal object contains an instant, then it will
   * be converted to a zoned date-time using the override zone.
   * Whether the temporal is an instant is determined by querying the
   * {@link ChronoField#INSTANT_SECONDS INSTANT_SECONDS} field.
   * If the input has a chronology then it will be retained unless overridden.
   * If the input does not have a chronology, such as {@code Instant}, then
   * the ISO chronology will be used.
   * <p>
   * If the temporal object does not contain an instant, but does contain
   * an offset then an additional check is made. If the normalized override
   * zone is an offset that differs from the offset of the temporal, then
   * a {@code DateTimeException} is thrown. In all other cases, the override
   * zone is added to the temporal, replacing any previous zone, but without
   * changing the date/time.
   * <p>
   * When parsing, there are two distinct cases to consider.
   * If a zone has been parsed directly from the text, perhaps because
   * {@link DateTimeFormatterBuilder#appendZoneId()} was used, then
   * this override zone has no effect.
   * If no zone has been parsed, then this override zone will be included in
   * the result of the parse where it can be used to build instants and date-times.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param zone  the new override zone, null if no override
   * @return a formatter based on this formatter with the requested override zone, not null
   */
  def withZone(zone: ZoneId): DateTimeFormatter = {
    if (Objects.equals(this.zone, zone)) {
       this
    }
     new DateTimeFormatter(printerParser, locale, decimalStyle, resolverStyle, resolverFields, chrono, zone)
  }

  /**
   * Gets the resolver style to use during parsing.
   * <p>
   * This returns the resolver style, used during the second phase of parsing
   * when fields are resolved into dates and times.
   * By default, a formatter has the {@link ResolverStyle#SMART SMART} resolver style.
   * See {@link #withResolverStyle(ResolverStyle)} for more details.
   *
   * @return the resolver style of this formatter, not null
   */
  def getResolverStyle: ResolverStyle = {
     resolverStyle
  }

  /**
   * Returns a copy of this formatter with a new resolver style.
   * <p>
   * This returns a formatter with similar state to this formatter but
   * with the resolver style set. By default, a formatter has the
   * {@link ResolverStyle#SMART SMART} resolver style.
   * <p>
   * Changing the resolver style only has an effect during parsing.
   * Parsing a text string occurs in two phases.
   * Phase 1 is a basic text parse according to the fields added to the builder.
   * Phase 2 resolves the parsed field-value pairs into date and/or time objects.
   * The resolver style is used to control how phase 2, resolving, happens.
   * See {@code ResolverStyle} for more information on the options available.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param resolverStyle  the new resolver style, not null
   * @return a formatter based on this formatter with the requested resolver style, not null
   */
  def withResolverStyle(resolverStyle: ResolverStyle): DateTimeFormatter = {

    if (Objects.equals(this.resolverStyle, resolverStyle)) {
       this
    }
     new DateTimeFormatter(printerParser, locale, decimalStyle, resolverStyle, resolverFields, chrono, zone)
  }

  /**
   * Gets the resolver fields to use during parsing.
   * <p>
   * This returns the resolver fields, used during the second phase of parsing
   * when fields are resolved into dates and times.
   * By default, a formatter has no resolver fields, and thus returns null.
   * See {@link #withResolverFields(Set)} for more details.
   *
   * @return the immutable set of resolver fields of this formatter, null if no fields
   */
  def getResolverFields: Set[TemporalField] = {
     resolverFields
  }

  /**
   * Returns a copy of this formatter with a new set of resolver fields.
   * <p>
   * This returns a formatter with similar state to this formatter but with
   * the resolver fields set. By default, a formatter has no resolver fields.
   * <p>
   * Changing the resolver fields only has an effect during parsing.
   * Parsing a text string occurs in two phases.
   * Phase 1 is a basic text parse according to the fields added to the builder.
   * Phase 2 resolves the parsed field-value pairs into date and/or time objects.
   * The resolver fields are used to filter the field-value pairs between phase 1 and 2.
   * <p>
   * This can be used to select between two or more ways that a date or time might
   * be resolved. For example, if the formatter consists of year, month, day-of-month
   * and day-of-year, then there are two ways to resolve a date.
   * Calling this method with the arguments {@link ChronoField#YEAR YEAR} and
   * {@link ChronoField#DAY_OF_YEAR DAY_OF_YEAR} will ensure that the date is
   * resolved using the year and day-of-year, effectively meaning that the month
   * and day-of-month are ignored during the resolving phase.
   * <p>
   * In a similar manner, this method can be used to ignore secondary fields that
   * would otherwise be cross-checked. For example, if the formatter consists of year,
   * month, day-of-month and day-of-week, then there is only one way to resolve a
   * date, but the parsed value for day-of-week will be cross-checked against the
   * resolved date. Calling this method with the arguments {@link ChronoField#YEAR YEAR},
   * {@link ChronoField#MONTH_OF_YEAR MONTH_OF_YEAR} and
   * {@link ChronoField#DAY_OF_MONTH DAY_OF_MONTH} will ensure that the date is
   * resolved correctly, but without any cross-check for the day-of-week.
   * <p>
   * In implementation terms, this method behaves as follows. The result of the
   * parsing phase can be considered to be a map of field to value. The behavior
   * of this method is to cause that map to be filtered between phase 1 and 2,
   * removing all fields other than those specified as arguments to this method.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param resolverFields  the new set of resolver fields, null if no fields
   * @return a formatter based on this formatter with the requested resolver style, not null
   */
  def withResolverFields(resolverFields: TemporalField*): DateTimeFormatter = {

    var fields: Set[TemporalField] = new HashSet[TemporalField](Arrays.asList(resolverFields))
    if (Objects.equals(this.resolverFields, fields)) {
       this
    }
    fields = Collections.unmodifiableSet(fields)
     new DateTimeFormatter(printerParser, locale, decimalStyle, resolverStyle, fields, chrono, zone)
  }

  /**
   * Returns a copy of this formatter with a new set of resolver fields.
   * <p>
   * This returns a formatter with similar state to this formatter but with
   * the resolver fields set. By default, a formatter has no resolver fields.
   * <p>
   * Changing the resolver fields only has an effect during parsing.
   * Parsing a text string occurs in two phases.
   * Phase 1 is a basic text parse according to the fields added to the builder.
   * Phase 2 resolves the parsed field-value pairs into date and/or time objects.
   * The resolver fields are used to filter the field-value pairs between phase 1 and 2.
   * <p>
   * This can be used to select between two or more ways that a date or time might
   * be resolved. For example, if the formatter consists of year, month, day-of-month
   * and day-of-year, then there are two ways to resolve a date.
   * Calling this method with the arguments {@link ChronoField#YEAR YEAR} and
   * {@link ChronoField#DAY_OF_YEAR DAY_OF_YEAR} will ensure that the date is
   * resolved using the year and day-of-year, effectively meaning that the month
   * and day-of-month are ignored during the resolving phase.
   * <p>
   * In a similar manner, this method can be used to ignore secondary fields that
   * would otherwise be cross-checked. For example, if the formatter consists of year,
   * month, day-of-month and day-of-week, then there is only one way to resolve a
   * date, but the parsed value for day-of-week will be cross-checked against the
   * resolved date. Calling this method with the arguments {@link ChronoField#YEAR YEAR},
   * {@link ChronoField#MONTH_OF_YEAR MONTH_OF_YEAR} and
   * {@link ChronoField#DAY_OF_MONTH DAY_OF_MONTH} will ensure that the date is
   * resolved correctly, but without any cross-check for the day-of-week.
   * <p>
   * In implementation terms, this method behaves as follows. The result of the
   * parsing phase can be considered to be a map of field to value. The behavior
   * of this method is to cause that map to be filtered between phase 1 and 2,
   * removing all fields other than those specified as arguments to this method.
   * <p>
   * This instance is immutable and unaffected by this method call.
   *
   * @param resolverFields  the new set of resolver fields, null if no fields
   * @return a formatter based on this formatter with the requested resolver style, not null
   */
  def withResolverFields(resolverFields: Set[TemporalField]): DateTimeFormatter = {

    if (Objects.equals(this.resolverFields, resolverFields)) {
       this
    }
    resolverFields = Collections.unmodifiableSet(new HashSet[TemporalField](resolverFields))
     new DateTimeFormatter(printerParser, locale, decimalStyle, resolverStyle, resolverFields, chrono, zone)
  }

  /**
   * Formats a date-time object using this formatter.
   * <p>
   * This formats the date-time to a String using the rules of the formatter.
   *
   * @param temporal  the temporal object to format, not null
   * @return the formatted string, not null
   * @throws DateTimeException if an error occurs during formatting
   */
  def format(temporal: TemporalAccessor): String = {
    val buf: StringBuilder = new StringBuilder(32)
    formatTo(temporal, buf)
     buf.toString
  }

  /**
   * Formats a date-time object to an {@code Appendable} using this formatter.
   * <p>
   * This outputs the formatted date-time to the specified destination.
   * {@link Appendable} is a general purpose interface that is implemented by all
   * key character output classes including {@code StringBuffer}, {@code StringBuilder},
   * {@code PrintStream} and {@code Writer}.
   * <p>
   * Although {@code Appendable} methods throw an {@code IOException}, this method does not.
   * Instead, any {@code IOException} is wrapped in a runtime exception.
   *
   * @param temporal  the temporal object to format, not null
   * @param appendable  the appendable to format to, not null
   * @throws DateTimeException if an error occurs during formatting
   */
  def formatTo(temporal: TemporalAccessor, appendable: Appendable) {


    try {
      val context: DateTimePrintContext = new DateTimePrintContext(temporal, this)
      if (appendable.isInstanceOf[StringBuilder]) {
        printerParser.format(context, appendable.asInstanceOf[StringBuilder])
      }
      else {
        val buf: StringBuilder = new StringBuilder(32)
        printerParser.format(context, buf)
        appendable.append(buf)
      }
    }
    catch {
      case ex: IOException => {
        throw new DateTimeException(ex.getMessage, ex)
      }
    }
  }

  /**
   * Fully parses the text producing a temporal object.
   * <p>
   * This parses the entire text producing a temporal object.
   * It is typically more useful to use {@link #parse(CharSequence, TemporalQuery)}.
   * The result of this method is {@code TemporalAccessor} which has been resolved,
   * applying basic validation checks to help ensure a valid date-time.
   * <p>
   * If the parse completes without reading the entire length of the text,
   * or a problem occurs during parsing or merging, then an exception is thrown.
   *
   * @param text  the text to parse, not null
   * @return the parsed temporal object, not null
   * @throws DateTimeParseException if unable to parse the requested result
   */
  def parse(text: CharSequence): TemporalAccessor = {

    try {
       parseResolved0(text, null)
    }
    catch {
      case ex: DateTimeParseException => {
        throw ex
      }
      case ex: RuntimeException => {
        throw createError(text, ex)
      }
    }
  }

  /**
   * Parses the text using this formatter, providing control over the text position.
   * <p>
   * This parses the text without requiring the parse to start from the beginning
   * of the string or finish at the end.
   * The result of this method is {@code TemporalAccessor} which has been resolved,
   * applying basic validation checks to help ensure a valid date-time.
   * <p>
   * The text will be parsed from the specified start {@code ParsePosition}.
   * The entire length of the text does not have to be parsed, the {@code ParsePosition}
   * will be updated with the index at the end of parsing.
   * <p>
   * The operation of this method is slightly different to similar methods using
   * {@code ParsePosition} on {@code java.text.Format}. That class will return
   * errors using the error index on the {@code ParsePosition}. By contrast, this
   * method will throw a {@link DateTimeParseException} if an error occurs, with
   * the exception containing the error index.
   * This change in behavior is necessary due to the increased complexity of
   * parsing and resolving dates/times in this API.
   * <p>
   * If the formatter parses the same field more than once with different values,
   * the result will be an error.
   *
   * @param text  the text to parse, not null
   * @param position  the position to parse from, updated with length parsed
   *                  and the index of any error, not null
   * @return the parsed temporal object, not null
   * @throws DateTimeParseException if unable to parse the requested result
   * @throws IndexOutOfBoundsException if the position is invalid
   */
  def parse(text: CharSequence, position: ParsePosition): TemporalAccessor = {


    try {
       parseResolved0(text, position)
    }
    catch {
      case ex: Any => {
        throw ex
      }
      case ex: RuntimeException => {
        throw createError(text, ex)
      }
    }
  }

  /**
   * Fully parses the text producing an object of the specified type.
   * <p>
   * Most applications should use this method for parsing.
   * It parses the entire text to produce the required date-time.
   * The query is typically a method reference to a {@code from(TemporalAccessor)} method.
   * For example:
   * {{{
   * DateTime dt = parser.parse(str, DateTime::from);
   * }}}
   * If the parse completes without reading the entire length of the text,
   * or a problem occurs during parsing or merging, then an exception is thrown.
   *
   * @param <T> the type of the parsed date-time
   * @param text  the text to parse, not null
   * @param query  the query defining the type to parse to, not null
   * @return the parsed date-time, not null
   * @throws DateTimeParseException if unable to parse the requested result
   */
  def parse(text: CharSequence, query: TemporalQuery[T]): T = {


    try {
       parseResolved0(text, null).query(query)
    }
    catch {
      case ex: DateTimeParseException => {
        throw ex
      }
      case ex: RuntimeException => {
        throw createError(text, ex)
      }
    }
  }

  /**
   * Fully parses the text producing an object of one of the specified types.
   * <p>
   * This parse method is convenient for use when the parser can handle optional elements.
   * For example, a pattern of 'uuuu-MM-dd HH.mm[ VV]' can be fully parsed to a {@code ZonedDateTime},
   * or partially parsed to a {@code DateTime}.
   * The queries must be specified in order, starting from the best matching full-parse option
   * and ending with the worst matching minimal parse option.
   * The query is typically a method reference to a {@code from(TemporalAccessor)} method.
   * <p>
   * The result is associated with the first type that successfully parses.
   * Normally, applications will use {@code instanceof} to check the result.
   * For example:
   * {{{
   * TemporalAccessor dt = parser.parseBest(str, ZonedDateTime::from, DateTime::from);
   * if (dt instanceof ZonedDateTime) {
   * ...
   * } else {
   * ...
   * }
   * }}}
   * If the parse completes without reading the entire length of the text,
   * or a problem occurs during parsing or merging, then an exception is thrown.
   *
   * @param text  the text to parse, not null
   * @param queries  the queries defining the types to attempt to parse to,
   *                 must implement { @code TemporalAccessor}, not null
   * @return the parsed date-time, not null
   * @throws IllegalArgumentException if less than 2 types are specified
   * @throws DateTimeParseException if unable to parse the requested result
   */
  def parseBest(text: CharSequence, queries: TemporalQuery[_]*): TemporalAccessor = {


    if (queries.length < 2) {
      throw new IllegalArgumentException("At least two queries must be specified")
    }
    try {
      val resolved: TemporalAccessor = parseResolved0(text, null)
      for (query <- queries) {
        try {
           resolved.query(query).asInstanceOf[TemporalAccessor]
        }
        catch {
          case ex: RuntimeException => {
          }
        }
      }
      throw new DateTimeException("Unable to convert parsed text using any of the specified queries")
    }
    catch {
      case ex: DateTimeParseException => {
        throw ex
      }
      case ex: RuntimeException => {
        throw createError(text, ex)
      }
    }
  }

  private def createError(text: CharSequence, ex: RuntimeException): DateTimeParseException = {
    var abbr: String = null
    if (text.length > 64) {
      abbr = text.subSequence(0, 64).toString + "..."
    }
    else {
      abbr = text.toString
    }
     new DateTimeParseException("Text '" + abbr + "' could not be parsed: " + ex.getMessage, text, 0, ex)
  }

  /**
   * Parses and resolves the specified text.
   * <p>
   * This parses to a {@code TemporalAccessor} ensuring that the text is fully parsed.
   *
   * @param text  the text to parse, not null
   * @param position  the position to parse from, updated with length parsed
   *                  and the index of any error, null if parsing whole string
   * @return the resolved result of the parse, not null
   * @throws DateTimeParseException if the parse fails
   * @throws DateTimeException if an error occurs while resolving the date or time
   * @throws IndexOutOfBoundsException if the position is invalid
   */
  private def parseResolved0(text: CharSequence, position: ParsePosition): TemporalAccessor = {
    val pos: ParsePosition = (if (position != null) position else new ParsePosition(0))
    val unresolved: Parsed = parseUnresolved0(text, pos)
    if (unresolved == null || pos.getErrorIndex >= 0 || (position == null && pos.getIndex < text.length)) {
      var abbr: String = null
      if (text.length > 64) {
        abbr = text.subSequence(0, 64).toString + "..."
      }
      else {
        abbr = text.toString
      }
      if (pos.getErrorIndex >= 0) {
        throw new DateTimeParseException("Text '" + abbr + "' could not be parsed at index " + pos.getErrorIndex, text, pos.getErrorIndex)
      }
      else {
        throw new DateTimeParseException("Text '" + abbr + "' could not be parsed, unparsed text found at index " + pos.getIndex, text, pos.getIndex)
      }
    }
     unresolved.resolve(resolverStyle, resolverFields)
  }

  /**
   * Parses the text using this formatter, without resolving the result, intended
   * for advanced use cases.
   * <p>
   * Parsing is implemented as a two-phase operation.
   * First, the text is parsed using the layout defined by the formatter, producing
   * a {@code Map} of field to value, a {@code ZoneId} and a {@code Chronology}.
   * Second, the parsed data is <em>resolved</em>, by validating, combining and
   * simplifying the various fields into more useful ones.
   * This method performs the parsing stage but not the resolving stage.
   * <p>
   * The result of this method is {@code TemporalAccessor} which represents the
   * data as seen in the input. Values are not validated, thus parsing a date string
   * of '2012-00-65' would result in a temporal with three fields - year of '2012',
   * month of '0' and day-of-month of '65'.
   * <p>
   * The text will be parsed from the specified start {@code ParsePosition}.
   * The entire length of the text does not have to be parsed, the {@code ParsePosition}
   * will be updated with the index at the end of parsing.
   * <p>
   * Errors are returned using the error index field of the {@code ParsePosition}
   * instead of {@code DateTimeParseException}.
   * The returned error index will be set to an index indicative of the error.
   * Callers must check for errors before using the context.
   * <p>
   * If the formatter parses the same field more than once with different values,
   * the result will be an error.
   * <p>
   * This method is intended for advanced use cases that need access to the
   * internal state during parsing. Typical application code should use
   * {@link #parse(CharSequence, TemporalQuery)} or the parse method on the target type.
   *
   * @param text  the text to parse, not null
   * @param position  the position to parse from, updated with length parsed
   *                  and the index of any error, not null
   * @return the parsed text, null if the parse results in an error
   * @throws DateTimeException if some problem occurs during parsing
   * @throws IndexOutOfBoundsException if the position is invalid
   */
  def parseUnresolved(text: CharSequence, position: ParsePosition): TemporalAccessor = {
     parseUnresolved0(text, position)
  }

  private def parseUnresolved0(text: CharSequence, position: ParsePosition): Parsed = {


    val context: DateTimeParseContext = new DateTimeParseContext(this)
    var pos: Int = position.getIndex
    pos = printerParser.parse(context, text, pos)
    if (pos < 0) {
      position.setErrorIndex(~pos)
       null
    }
    position.setIndex(pos)
     context.toParsed
  }

  /**
   * Returns the formatter as a composite printer parser.
   *
   * @param optional  whether the printer/parser should be optional
   * @return the printer/parser, not null
   */
  private[format] def toPrinterParser(optional: Boolean): DateTimeFormatterBuilder.CompositePrinterParser = {
     printerParser.withOptional(optional)
  }

  /**
   * Returns this formatter as a {@code java.text.Format} instance.
   * <p>
   * The returned {@link Format} instance will format any {@link TemporalAccessor}
   * and parses to a resolved {@link TemporalAccessor}.
   * <p>
   * Exceptions will follow the definitions of {@code Format}, see those methods
   * for details about {@code IllegalArgumentException} during formatting and
   * {@code ParseException} or null during parsing.
   * The format does not support attributing of the returned format string.
   *
   * @return this formatter as a classic format instance, not null
   */
  def toFormat: Format = {
     new DateTimeFormatter.ClassicFormat(this, null)
  }

  /**
   * Returns this formatter as a {@code java.text.Format} instance that will
   * parse using the specified query.
   * <p>
   * The returned {@link Format} instance will format any {@link TemporalAccessor}
   * and parses to the type specified.
   * The type must be one that is supported by {@link #parse}.
   * <p>
   * Exceptions will follow the definitions of {@code Format}, see those methods
   * for details about {@code IllegalArgumentException} during formatting and
   * {@code ParseException} or null during parsing.
   * The format does not support attributing of the returned format string.
   *
   * @param parseQuery  the query defining the type to parse to, not null
   * @return this formatter as a classic format instance, not null
   */
  def toFormat(parseQuery: TemporalQuery[_]): Format = {

     new DateTimeFormatter.ClassicFormat(this, parseQuery)
  }

  /**
   * Returns a description of the underlying formatters.
   *
   * @return a description of this formatter, not null
   */
  override def toString: String = {
    var pattern: String = printerParser.toString
    pattern = if (pattern.startsWith("[")) pattern else pattern.substring(1, pattern.length - 1)
     pattern
  }

  /**
   * The printer and/or parser to use, not null.
   */
  private final val printerParser: DateTimeFormatterBuilder.CompositePrinterParser = null
  /**
   * The locale to use for formatting, not null.
   */
  private final val locale: Locale = null
  /**
   * The symbols to use for formatting, not null.
   */
  private final val decimalStyle: DecimalStyle = null
  /**
   * The resolver style to use, not null.
   */
  private final val resolverStyle: ResolverStyle = null
  /**
   * The fields to use in resolving, null for all fields.
   */
  private final val resolverFields: Set[TemporalField] = null
  /**
   * The chronology to use for formatting, null for no override.
   */
  private final val chrono: Chronology = null
  /**
   * The zone to use for formatting, null for no override.
   */
  private final val zone: ZoneId = null
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
 * Copyright (c) 2008-2012, Stephen Colebourne & Michael Nascimento Santos
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
 * Builder to create date-time formatters.
 * <p>
 * This allows a {@code DateTimeFormatter} to be created.
 * All date-time formatters are created ultimately using this builder.
 * <p>
 * The basic elements of date-time can all be added:
 * <p><ul>
 * <li>Value - a numeric value</li>
 * <li>Fraction - a fractional value including the decimal place. Always use this when
 * outputting fractions to ensure that the fraction is parsed correctly</li>
 * <li>Text - the textual equivalent for the value</li>
 * <li>OffsetId/Offset - the {@linkplain ZoneOffset zone offset}</li>
 * <li>ZoneId - the {@linkplain ZoneId time-zone} id</li>
 * <li>ZoneText - the name of the time-zone</li>
 * <li>ChronologyId - the {@linkplain Chronology chronology} id</li>
 * <li>ChronologyText - the name of the chronology</li>
 * <li>Literal - a text literal</li>
 * <li>Nested and Optional - formats can be nested or made optional</li>
 * <li>Other - the printer and parser interfaces can be used to add user supplied formatting</li>
 * </ul><p>
 * In addition, any of the elements may be decorated by padding, either with spaces or any other character.
 * <p>
 * Finally, a shorthand pattern, mostly compatible with {@code java.text.SimpleDateFormat SimpleDateFormat}
 * can be used, see {@link #appendPattern(String)}.
 * In practice, this simply parses the pattern and calls other methods on the builder.
 *
 * @implSpec
 * This class is a mutable builder intended for use from a single thread.
 *
 * @since 1.8
 */
object DateTimeFormatterBuilder {
  /**
   * Gets the formatting pattern for date and time styles for a locale and chronology.
   * The locale and chronology are used to lookup the locale specific format
   * for the requested dateStyle and/or timeStyle.
   *
   * @param dateStyle  the FormatStyle for the date
   * @param timeStyle  the FormatStyle for the time
   * @param chrono  the Chronology, non-null
   * @param locale  the locale, non-null
   * @return the locale and Chronology specific formatting pattern
   * @throws IllegalArgumentException if both dateStyle and timeStyle are null
   */
  def getLocalizedDateTimePattern(dateStyle: FormatStyle, timeStyle: FormatStyle, chrono: Chronology, locale: Locale): String = {


    if (dateStyle == null && timeStyle == null) {
      throw new IllegalArgumentException("Either dateStyle or timeStyle must be non-null")
    }
    val lr: Nothing = LocaleProviderAdapter.getResourceBundleBased.getLocaleResources(locale)
    val pattern: String = lr.getJavaTimeDateTimePattern(convertStyle(timeStyle), convertStyle(dateStyle), chrono.getCalendarType)
     pattern
  }

  /**
   * Converts the given FormatStyle to the java.text.DateFormat style.
   *
   * @param style  the FormatStyle style
   * @return the int style, or -1 if style is null, indicating un-required
   */
  private def convertStyle(style: FormatStyle): Int = {
    if (style == null) {
       -1
    }
     style.ordinal
  }

  /**
   * Query for a time-zone that is region-only.
   */
  private final val QUERY_REGION_ONLY: TemporalQuery[ZoneId] = (temporal) -> {
    ZoneId zone = temporal.query(TemporalQuery.zoneId());
     (zone != null && zone instanceof ZoneOffset == false ? zone: null);
  }
  /** Map of letters to fields. */
  private final val FIELD_MAP: Map[Character, TemporalField] = new HashMap[Character, TemporalField]
  /**
   * Length comparator.
   */
  private[format] final val LENGTH_SORT: Nothing = new Nothing {
    def compare(str1: String, str2: String): Int = {
       if (str1.length == str2.length) str1.compareTo(str2) else str1.length - str2.length
    }
  }

  /**
   * Strategy for formatting/parsing date-time information.
   * <p>
   * The printer may format any part, or the whole, of the input date-time object.
   * Typically, a complete format is constructed from a number of smaller
   * units, each outputting a single field.
   * <p>
   * The parser may parse any piece of text from the input, storing the result
   * in the context. Typically, each individual parser will just parse one
   * field, such as the day-of-month, storing the value in the context.
   * Once the parse is complete, the caller will then resolve the parsed values
   * to create the desired object, such as a {@code Date}.
   * <p>
   * The parse position will be updated during the parse. Parsing will start at
   * the specified index and the return value specifies the new parse position
   * for the next parser. If an error occurs, the returned index will be negative
   * and will have the error position encoded using the complement operator.
   *
   * @implSpec
     * This interface must be implemented with care to ensure other classes operate correctly.
   *   All implementations that can be instantiated must be final, immutable and thread-safe.
   *   <p>
   *   The context is not a thread-safe object and a new instance will be created
   *   for each format that occurs. The context must not be stored in an instance
   *   variable or shared with any other threads.
   */
  private[format] trait DateTimePrinterParser {
    /**
     * Prints the date-time object to the buffer.
     * <p>
     * The context holds information to use during the format.
     * It also contains the date-time information to be printed.
     * <p>
     * The buffer must not be mutated beyond the content controlled by the implementation.
     *
     * @param context  the context to format using, not null
     * @param buf  the buffer to append to, not null
     * @return false if unable to query the value from the date-time, true otherwise
     * @throws DateTimeException if the date-time cannot be printed successfully
     */
    def format(context: DateTimePrintContext, buf: StringBuilder): Boolean

    /**
     * Parses text into date-time information.
     * <p>
     * The context holds information to use during the parse.
     * It is also used to store the parsed date-time information.
     *
     * @param context  the context to use and parse into, not null
     * @param text  the input text to parse, not null
     * @param position  the position to start parsing at, from 0 to the text length
     * @return the new parse position, where negative means an error with the
     *         error position encoded using the complement ~ operator
     * @throws NullPointerException if the context or text is null
     * @throws IndexOutOfBoundsException if the position is invalid
     */
    def parse(context: DateTimeParseContext, text: CharSequence, position: Int): Int
  }

  /**
   * Composite printer and parser.
   */
  private[format] final class CompositePrinterParser extends DateTimePrinterParser {
    private[format] def this(printerParsers: Nothing, optional: Boolean) {

      `this`(printerParsers.toArray(new Array[DateTimeFormatterBuilder.DateTimePrinterParser](printerParsers.size)), optional)
    }

    private[format] def this(printerParsers: Array[DateTimeFormatterBuilder.DateTimePrinterParser], optional: Boolean) {

      this.printerParsers = printerParsers
      this.optional = optional
    }

    /**
     * Returns a copy of this printer-parser with the optional flag changed.
     *
     * @param optional  the optional flag to set in the copy
     * @return the new printer-parser, not null
     */
    def withOptional(optional: Boolean): DateTimeFormatterBuilder.CompositePrinterParser = {
      if (optional == this.optional) {
         this
      }
       new DateTimeFormatterBuilder.CompositePrinterParser(printerParsers, optional)
    }

    def format(context: DateTimePrintContext, buf: StringBuilder): Boolean = {
      val length: Int = buf.length
      if (optional) {
        context.startOptional
      }
      try {
        for (pp <- printerParsers) {
          if (pp.format(context, buf) == false) {
            buf.setLength(length)
             true
          }
        }
      }
      finally {
        if (optional) {
          context.endOptional
        }
      }
       true
    }

    def parse(context: DateTimeParseContext, text: CharSequence, position: Int): Int = {
      if (optional) {
        context.startOptional
        var pos: Int = position
        for (pp <- printerParsers) {
          pos = pp.parse(context, text, pos)
          if (pos < 0) {
            context.endOptional(false)
             position
          }
        }
        context.endOptional(true)
         pos
      }
      else {
        for (pp <- printerParsers) {
          position = pp.parse(context, text, position)
          if (position < 0) {
            break //todo: break is not supported
          }
        }
         position
      }
    }

    override def toString: String = {
      val buf: StringBuilder = new StringBuilder
      if (printerParsers != null) {
        buf.append(if (optional) "[" else "(")
        for (pp <- printerParsers) {
          buf.append(pp)
        }
        buf.append(if (optional) "]" else ")")
      }
       buf.toString
    }

    private final val printerParsers: Array[DateTimeFormatterBuilder.DateTimePrinterParser] = null
    private final val optional: Boolean = false
  }

  /**
   * Pads the output to a fixed width.
   */
  private[format] final class PadPrinterParserDecorator extends DateTimePrinterParser {
    /**
     * Constructor.
     *
     * @param printerParser  the printer, not null
     * @param padWidth  the width to pad to, 1 or greater
     * @param padChar  the pad character
     */
    private[format] def this(printerParser: DateTimeFormatterBuilder.DateTimePrinterParser, padWidth: Int, padChar: Char) {

      this.printerParser = printerParser
      this.padWidth = padWidth
      this.padChar = padChar
    }

    def format(context: DateTimePrintContext, buf: StringBuilder): Boolean = {
      val preLen: Int = buf.length
      if (printerParser.format(context, buf) == false) {
         false
      }
      val len: Int = buf.length - preLen
      if (len > padWidth) {
        throw new DateTimeException("Cannot print as output of " + len + " characters exceeds pad width of " + padWidth)
      }
      {
        var i: Int = 0
        while (i < padWidth - len) {
          {
            buf.insert(preLen, padChar)
          }
          ({
            i += 1; i - 1
          })
        }
      }
       true
    }

    def parse(context: DateTimeParseContext, text: CharSequence, position: Int): Int = {
      val strict: Boolean = context.isStrict
      if (position > text.length) {
        throw new IndexOutOfBoundsException
      }
      if (position == text.length) {
         ~position
      }
      var endPos: Int = position + padWidth
      if (endPos > text.length) {
        if (strict) {
           ~position
        }
        endPos = text.length
      }
      var pos: Int = position
      while (pos < endPos && context.charEquals(text.charAt(pos), padChar)) {
        pos += 1
      }
      text = text.subSequence(0, endPos)
      val resultPos: Int = printerParser.parse(context, text, pos)
      if (resultPos != endPos && strict) {
         ~(position + pos)
      }
       resultPos
    }

    override def toString: String = {
       "Pad(" + printerParser + "," + padWidth + (if (padChar == ' ') ")" else ",'" + padChar + "')")
    }

    private final val printerParser: DateTimeFormatterBuilder.DateTimePrinterParser = null
    private final val padWidth: Int = 0
    private final val padChar: Char = 0
  }

  /**
   * Enumeration to apply simple parse settings.
   */
  private[format] object SettingsParser {
    final val SENSITIVE: = null
    final val INSENSITIVE: = null
    final val STRICT: = null
    final val LENIENT: = null
  }

  private[format] final class SettingsParser extends DateTimePrinterParser {
    def format(context: DateTimePrintContext, buf: StringBuilder): Boolean = {
       true
    }

    def parse(context: DateTimeParseContext, text: CharSequence, position: Int): Int = {
      ordinal match {
        case 0 =>
          context.setCaseSensitive(true)
          break //todo: break is not supported
        case 1 =>
          context.setCaseSensitive(false)
          break //todo: break is not supported
        case 2 =>
          context.setStrict(true)
          break //todo: break is not supported
        case 3 =>
          context.setStrict(false)
          break //todo: break is not supported
      }
       position
    }

    override def toString: String = {
      ordinal match {
        case 0 =>
           "ParseCaseSensitive(true)"
        case 1 =>
           "ParseCaseSensitive(false)"
        case 2 =>
           "ParseStrict(true)"
        case 3 =>
           "ParseStrict(false)"
      }
      throw new IllegalStateException("Unreachable")
    }
  }

  /**
   * Defaults a value into the parse if not currently present.
   */
  private[format] class DefaultValueParser extends DateTimePrinterParser {
    private[format] def this(field: TemporalField, value: Long) {

      this.field = field
      this.value = value
    }

    def format(context: DateTimePrintContext, buf: StringBuilder): Boolean = {
       true
    }

    def parse(context: DateTimeParseContext, text: CharSequence, position: Int): Int = {
      if (context.getParsed(field) == null) {
        context.setParsedField(field, value, position, position)
      }
       position
    }

    private final val field: TemporalField = null
    private final val value: Long = 0L
  }

  /**
   * Prints or parses a character literal.
   */
  private[format] final class CharLiteralPrinterParser extends DateTimePrinterParser {
    private[format] def this(literal: Char) {

      this.literal = literal
    }

    def format(context: DateTimePrintContext, buf: StringBuilder): Boolean = {
      buf.append(literal)
       true
    }

    def parse(context: DateTimeParseContext, text: CharSequence, position: Int): Int = {
      val length: Int = text.length
      if (position == length) {
         ~position
      }
      val ch: Char = text.charAt(position)
      if (ch != literal) {
        if (context.isCaseSensitive || (Character.toUpperCase(ch) != Character.toUpperCase(literal) && Character.toLowerCase(ch) != Character.toLowerCase(literal))) {
           ~position
        }
      }
       position + 1
    }

    override def toString: String = {
      if (literal == '\'') {
         "''"
      }
       "'" + literal + "'"
    }

    private final val literal: Char = 0
  }

  /**
   * Prints or parses a string literal.
   */
  private[format] final class StringLiteralPrinterParser extends DateTimePrinterParser {
    private[format] def this(literal: String) {

      this.literal = literal
    }

    def format(context: DateTimePrintContext, buf: StringBuilder): Boolean = {
      buf.append(literal)
       true
    }

    def parse(context: DateTimeParseContext, text: CharSequence, position: Int): Int = {
      val length: Int = text.length
      if (position > length || position < 0) {
        throw new IndexOutOfBoundsException
      }
      if (context.subSequenceEquals(text, position, literal, 0, literal.length) == false) {
         ~position
      }
       position + literal.length
    }

    override def toString: String = {
      val converted: String = literal.replace("'", "''")
       "'" + converted + "'"
    }

    private final val literal: String = null
  }

  /**
   * Prints and parses a numeric date-time field with optional padding.
   */
  private[format] object NumberPrinterParser {
    /**
     * Array of 10 to the power of n.
     */
    private[format] final val EXCEED_POINTS: Array[Long] = Array[Long](0L, 10L, 100L, 1000L, 10000L, 100000L, 1000000L, 10000000L, 100000000L, 1000000000L, 10000000000L)
  }

  private[format] class NumberPrinterParser extends DateTimePrinterParser {
    /**
     * Constructor.
     *
     * @param field  the field to format, not null
     * @param minWidth  the minimum field width, from 1 to 19
     * @param maxWidth  the maximum field width, from minWidth to 19
     * @param signStyle  the positive/negative sign style, not null
     */
    private[format] def this(field: TemporalField, minWidth: Int, maxWidth: Int, signStyle: SignStyle) {

      this.field = field
      this.minWidth = minWidth
      this.maxWidth = maxWidth
      this.signStyle = signStyle
      this.subsequentWidth = 0
    }

    /**
     * Constructor.
     *
     * @param field  the field to format, not null
     * @param minWidth  the minimum field width, from 1 to 19
     * @param maxWidth  the maximum field width, from minWidth to 19
     * @param signStyle  the positive/negative sign style, not null
     * @param subsequentWidth  the width of subsequent non-negative numbers, 0 or greater,
     *                         -1 if fixed width due to active adjacent parsing
     */
    protected def this(field: TemporalField, minWidth: Int, maxWidth: Int, signStyle: SignStyle, subsequentWidth: Int) {

      this.field = field
      this.minWidth = minWidth
      this.maxWidth = maxWidth
      this.signStyle = signStyle
      this.subsequentWidth = subsequentWidth
    }

    /**
     * Returns a new instance with fixed width flag set.
     *
     * @return a new updated printer-parser, not null
     */
    private[format] def withFixedWidth: DateTimeFormatterBuilder.NumberPrinterParser = {
      if (subsequentWidth == -1) {
         this
      }
       new DateTimeFormatterBuilder.NumberPrinterParser(field, minWidth, maxWidth, signStyle, -1)
    }

    /**
     * Returns a new instance with an updated subsequent width.
     *
     * @param subsequentWidth  the width of subsequent non-negative numbers, 0 or greater
     * @return a new updated printer-parser, not null
     */
    private[format] def withSubsequentWidth(subsequentWidth: Int): DateTimeFormatterBuilder.NumberPrinterParser = {
       new DateTimeFormatterBuilder.NumberPrinterParser(field, minWidth, maxWidth, signStyle, this.subsequentWidth + subsequentWidth)
    }

    def format(context: DateTimePrintContext, buf: StringBuilder): Boolean = {
      val valueLong: Long = context.getValue(field)
      if (valueLong == null) {
         false
      }
      val value: Long = getValue(valueLong)
      val decimalStyle: DecimalStyle = context.getDecimalStyle
      var str: String = (if (value == Long.MIN_VALUE) "9223372036854775808" else Long.toString(Math.abs(value)))
      if (str.length > maxWidth) {
        throw new DateTimeException("Field " + field + " cannot be printed as the value " + value + " exceeds the maximum print width of " + maxWidth)
      }
      str = decimalStyle.convertNumberToI18N(str)
      if (value >= 0) {
        signStyle match {
          case EXCEEDS_PAD =>
            if (minWidth < 19 && value >= EXCEED_POINTS(minWidth)) {
              buf.append(decimalStyle.getPositiveSign)
            }
            break //todo: break is not supported
          case ALWAYS =>
            buf.append(decimalStyle.getPositiveSign)
            break //todo: break is not supported
        }
      }
      else {
        signStyle match {
          case NORMAL =>
          case EXCEEDS_PAD =>
          case ALWAYS =>
            buf.append(decimalStyle.getNegativeSign)
            break //todo: break is not supported
          case NOT_NEGATIVE =>
            throw new DateTimeException("Field " + field + " cannot be printed as the value " + value + " cannot be negative according to the SignStyle")
        }
      }
      {
        var i: Int = 0
        while (i < minWidth - str.length) {
          {
            buf.append(decimalStyle.getZeroDigit)
          }
          ({
            i += 1; i - 1
          })
        }
      }
      buf.append(str)
       true
    }

    /**
     * Gets the value to output.
     *
     * @param value  the base value of the field, not null
     * @return the value
     */
    private[format] def getValue(value: Long): Long = {
       value
    }

    private[format] def isFixedWidth: Boolean = {
       subsequentWidth == -1
    }

    def parse(context: DateTimeParseContext, text: CharSequence, position: Int): Int = {
      val length: Int = text.length
      if (position == length) {
         ~position
      }
      val sign: Char = text.charAt(position)
      var negative: Boolean = false
      var positive: Boolean = false
      if (sign == context.getDecimalStyle.getPositiveSign) {
        if (signStyle.parse(true, context.isStrict, minWidth == maxWidth) == false) {
           ~position
        }
        positive = true
        position += 1
      }
      else if (sign == context.getDecimalStyle.getNegativeSign) {
        if (signStyle.parse(false, context.isStrict, minWidth == maxWidth) == false) {
           ~position
        }
        negative = true
        position += 1
      }
      else {
        if (signStyle eq SignStyle.ALWAYS && context.isStrict) {
           ~position
        }
      }
      val effMinWidth: Int = (if (context.isStrict || isFixedWidth) minWidth else 1)
      val minEndPos: Int = position + effMinWidth
      if (minEndPos > length) {
         ~position
      }
      var effMaxWidth: Int = (if (context.isStrict || isFixedWidth) maxWidth else 9) + Math.max(subsequentWidth, 0)
      var total: Long = 0
      var totalBig: Nothing = null
      var pos: Int = position
      {
        var pass: Int = 0
        while (pass < 2) {
          {
            val maxEndPos: Int = Math.min(pos + effMaxWidth, length)
            while (pos < maxEndPos) {
              val ch: Char = text.charAt(({
                pos += 1; pos - 1
              }))
              val digit: Int = context.getDecimalStyle.convertToDigit(ch)
              if (digit < 0) {
                pos -= 1
                if (pos < minEndPos) {
                   ~position
                }
                break //todo: break is not supported
              }
              if ((pos - position) > 18) {
                if (totalBig == null) {
                  totalBig = BigInteger.valueOf(total)
                }
                totalBig = totalBig.multiply(BigInteger.TEN).add(BigInteger.valueOf(digit))
              }
              else {
                total = total * 10 + digit
              }
            }
            if (subsequentWidth > 0 && pass == 0) {
              val parseLen: Int = pos - position
              effMaxWidth = Math.max(effMinWidth, parseLen - subsequentWidth)
              pos = position
              total = 0
              totalBig = null
            }
            else {
              break //todo: break is not supported
            }
          }
          ({
            pass += 1; pass - 1
          })
        }
      }
      if (negative) {
        if (totalBig != null) {
          if ((totalBig == BigInteger.ZERO) && context.isStrict) {
             ~(position - 1)
          }
          totalBig = totalBig.negate
        }
        else {
          if (total == 0 && context.isStrict) {
             ~(position - 1)
          }
          total = -total
        }
      }
      else if (signStyle eq SignStyle.EXCEEDS_PAD && context.isStrict) {
        val parseLen: Int = pos - position
        if (positive) {
          if (parseLen <= minWidth) {
             ~(position - 1)
          }
        }
        else {
          if (parseLen > minWidth) {
             ~position
          }
        }
      }
      if (totalBig != null) {
        if (totalBig.bitLength > 63) {
          totalBig = totalBig.divide(BigInteger.TEN)
          pos -= 1
        }
         setValue(context, totalBig.longValue, position, pos)
      }
       setValue(context, total, position, pos)
    }

    /**
     * Stores the value.
     *
     * @param context  the context to store into, not null
     * @param value  the value
     * @param errorPos  the position of the field being parsed
     * @param successPos  the position after the field being parsed
     * @return the new position
     */
    private[format] def setValue(context: DateTimeParseContext, value: Long, errorPos: Int, successPos: Int): Int = {
       context.setParsedField(field, value, errorPos, successPos)
    }

    override def toString: String = {
      if (minWidth == 1 && maxWidth == 19 && signStyle eq SignStyle.NORMAL) {
         "Value(" + field + ")"
      }
      if (minWidth == maxWidth && signStyle eq SignStyle.NOT_NEGATIVE) {
         "Value(" + field + "," + minWidth + ")"
      }
       "Value(" + field + "," + minWidth + "," + maxWidth + "," + signStyle + ")"
    }

    private[format] final val field: TemporalField = null
    private[format] final val minWidth: Int = 0
    private[format] final val maxWidth: Int = 0
    private final val signStyle: SignStyle = null
    private[format] final val subsequentWidth: Int = 0
  }

  /**
   * Prints and parses a reduced numeric date-time field.
   */
  private[format] final class ReducedPrinterParser extends NumberPrinterParser {
    /**
     * Constructor.
     *
     * @param field  the field to format, validated not null
     * @param minWidth  the minimum field width, from 1 to 10
     * @param maxWidth  the maximum field width, from 1 to 10
     * @param baseValue  the base value
     */
    private[format] def this(field: TemporalField, minWidth: Int, maxWidth: Int, baseValue: Int) {

      `this`(field, minWidth, maxWidth, baseValue, 0)
      if (minWidth < 1 || minWidth > 10) {
        throw new IllegalArgumentException("The minWidth must be from 1 to 10 inclusive but was " + minWidth)
      }
      if (maxWidth < 1 || maxWidth > 10) {
        throw new IllegalArgumentException("The maxWidth must be from 1 to 10 inclusive but was " + minWidth)
      }
      if (maxWidth < minWidth) {
        throw new IllegalArgumentException("Maximum width must exceed or equal the minimum width but " + maxWidth + " < " + minWidth)
      }
      if (field.range.isValidValue(baseValue) == false) {
        throw new IllegalArgumentException("The base value must be within the range of the field")
      }
      if (((baseValue.asInstanceOf[Long]) + EXCEED_POINTS(maxWidth)) > Integer.MAX_VALUE) {
        throw new DateTimeException("Unable to add printer-parser as the range exceeds the capacity of an int")
      }
    }

    /**
     * Constructor.
     * The arguments have already been checked.
     *
     * @param field  the field to format, validated not null
     * @param minWidth  the minimum field width, from 1 to 10
     * @param maxWidth  the maximum field width, from 1 to 10
     * @param baseValue  the base value
     * @param subsequentWidth the subsequentWidth for this instance
     */
    private def this(field: TemporalField, minWidth: Int, maxWidth: Int, baseValue: Int, subsequentWidth: Int) {

      `super`(field, minWidth, maxWidth, SignStyle.NOT_NEGATIVE, subsequentWidth)
      this.baseValue = baseValue
    }

    private[format] override def getValue(value: Long): Long = {
      val absValue: Long = Math.abs(value)
      if (value >= baseValue && value < baseValue + EXCEED_POINTS(minWidth)) {
         absValue % EXCEED_POINTS(minWidth)
      }
       absValue % EXCEED_POINTS(maxWidth)
    }

    private[format] override def setValue(context: DateTimeParseContext, value: Long, errorPos: Int, successPos: Int): Int = {
      val parseLen: Int = successPos - errorPos
      if (parseLen == minWidth && value >= 0) {
        val range: Long = EXCEED_POINTS(minWidth)
        val lastPart: Long = baseValue % range
        val basePart: Long = baseValue - lastPart
        if (baseValue > 0) {
          value = basePart + value
        }
        else {
          value = basePart - value
        }
        if (basePart != 0 && value < baseValue) {
          value += range
        }
      }
       context.setParsedField(field, value, errorPos, successPos)
    }

    /**
     * Returns a new instance with fixed width flag set.
     *
     * @return a new updated printer-parser, not null
     */
    private[format] override def withFixedWidth: DateTimeFormatterBuilder.ReducedPrinterParser = {
      if (subsequentWidth == -1) {
         this
      }
       new DateTimeFormatterBuilder.ReducedPrinterParser(field, minWidth, maxWidth, baseValue, -1)
    }

    /**
     * Returns a new instance with an updated subsequent width.
     *
     * @param subsequentWidth  the width of subsequent non-negative numbers, 0 or greater
     * @return a new updated printer-parser, not null
     */
    private[format] override def withSubsequentWidth(subsequentWidth: Int): DateTimeFormatterBuilder.ReducedPrinterParser = {
       new DateTimeFormatterBuilder.ReducedPrinterParser(field, minWidth, maxWidth, baseValue, this.subsequentWidth + subsequentWidth)
    }

    override def toString: String = {
       "ReducedValue(" + field + "," + minWidth + "," + maxWidth + "," + baseValue + ")"
    }

    private final val baseValue: Int = 0
  }

  /**
   * Prints and parses a numeric date-time field with optional padding.
   */
  private[format] final class FractionPrinterParser extends DateTimePrinterParser {
    /**
     * Constructor.
     *
     * @param field  the field to output, not null
     * @param minWidth  the minimum width to output, from 0 to 9
     * @param maxWidth  the maximum width to output, from 0 to 9
     * @param decimalPoint  whether to output the localized decimal point symbol
     */
    private[format] def this(field: TemporalField, minWidth: Int, maxWidth: Int, decimalPoint: Boolean) {


      if (field.range.isFixed == false) {
        throw new IllegalArgumentException("Field must have a fixed set of values: " + field)
      }
      if (minWidth < 0 || minWidth > 9) {
        throw new IllegalArgumentException("Minimum width must be from 0 to 9 inclusive but was " + minWidth)
      }
      if (maxWidth < 1 || maxWidth > 9) {
        throw new IllegalArgumentException("Maximum width must be from 1 to 9 inclusive but was " + maxWidth)
      }
      if (maxWidth < minWidth) {
        throw new IllegalArgumentException("Maximum width must exceed or equal the minimum width but " + maxWidth + " < " + minWidth)
      }
      this.field = field
      this.minWidth = minWidth
      this.maxWidth = maxWidth
      this.decimalPoint = decimalPoint
    }

    def format(context: DateTimePrintContext, buf: StringBuilder): Boolean = {
      val value: Long = context.getValue(field)
      if (value == null) {
         false
      }
      val decimalStyle: DecimalStyle = context.getDecimalStyle
      var fraction: Nothing = convertToFraction(value)
      if (fraction.scale eq 0) {
        if (minWidth > 0) {
          if (decimalPoint) {
            buf.append(decimalStyle.getDecimalSeparator)
          }
          {
            var i: Int = 0
            while (i < minWidth) {
              {
                buf.append(decimalStyle.getZeroDigit)
              }
              ({
                i += 1; i - 1
              })
            }
          }
        }
      }
      else {
        val outputScale: Int = Math.min(Math.max(fraction.scale, minWidth), maxWidth)
        fraction = fraction.setScale(outputScale, RoundingMode.FLOOR)
        var str: String = fraction.toPlainString.substring(2)
        str = decimalStyle.convertNumberToI18N(str)
        if (decimalPoint) {
          buf.append(decimalStyle.getDecimalSeparator)
        }
        buf.append(str)
      }
       true
    }

    def parse(context: DateTimeParseContext, text: CharSequence, position: Int): Int = {
      val effectiveMin: Int = (if (context.isStrict) minWidth else 0)
      val effectiveMax: Int = (if (context.isStrict) maxWidth else 9)
      val length: Int = text.length
      if (position == length) {
         (if (effectiveMin > 0) ~position else position)
      }
      if (decimalPoint) {
        if (text.charAt(position) != context.getDecimalStyle.getDecimalSeparator) {
           (if (effectiveMin > 0) ~position else position)
        }
        position += 1
      }
      val minEndPos: Int = position + effectiveMin
      if (minEndPos > length) {
         ~position
      }
      val maxEndPos: Int = Math.min(position + effectiveMax, length)
      var total: Int = 0
      var pos: Int = position
      while (pos < maxEndPos) {
        val ch: Char = text.charAt(({
          pos += 1; pos - 1
        }))
        val digit: Int = context.getDecimalStyle.convertToDigit(ch)
        if (digit < 0) {
          if (pos < minEndPos) {
             ~position
          }
          pos -= 1
          break //todo: break is not supported
        }
        total = total * 10 + digit
      }
      val fraction: Nothing = new Nothing(total).movePointLeft(pos - position)
      val value: Long = convertFromFraction(fraction)
       context.setParsedField(field, value, position, pos)
    }

    /**
     * Converts a value for this field to a fraction between 0 and 1.
     * <p>
     * The fractional value is between 0 (inclusive) and 1 (exclusive).
     * It can only be returned if the {@link java.time.temporal.TemporalField#range() value range} is fixed.
     * The fraction is obtained by calculation from the field range using 9 decimal
     * places and a rounding mode of {@link RoundingMode#FLOOR FLOOR}.
     * The calculation is inaccurate if the values do not run continuously from smallest to largest.
     * <p>
     * For example, the second-of-minute value of 15 would be returned as 0.25,
     * assuming the standard definition of 60 seconds in a minute.
     *
     * @param value  the value to convert, must be valid for this rule
     * @return the value as a fraction within the range, from 0 to 1, not null
     * @throws DateTimeException if the value cannot be converted to a fraction
     */
    private def convertToFraction(value: Long): Nothing = {
      val range: Nothing = field.range
      range.checkValidValue(value, field)
      val minBD: Nothing = BigDecimal.valueOf(range.getMinimum)
      val rangeBD: Nothing = BigDecimal.valueOf(range.getMaximum).subtract(minBD).add(BigDecimal.ONE)
      val valueBD: Nothing = BigDecimal.valueOf(value).subtract(minBD)
      val fraction: Nothing = valueBD.divide(rangeBD, 9, RoundingMode.FLOOR)
       if (fraction.compareTo(BigDecimal.ZERO) eq 0) BigDecimal.ZERO else fraction.stripTrailingZeros
    }

    /**
     * Converts a fraction from 0 to 1 for this field to a value.
     * <p>
     * The fractional value must be between 0 (inclusive) and 1 (exclusive).
     * It can only be returned if the {@link java.time.temporal.TemporalField#range() value range} is fixed.
     * The value is obtained by calculation from the field range and a rounding
     * mode of {@link RoundingMode#FLOOR FLOOR}.
     * The calculation is inaccurate if the values do not run continuously from smallest to largest.
     * <p>
     * For example, the fractional second-of-minute of 0.25 would be converted to 15,
     * assuming the standard definition of 60 seconds in a minute.
     *
     * @param fraction  the fraction to convert, not null
     * @return the value of the field, valid for this rule
     * @throws DateTimeException if the value cannot be converted
     */
    private def convertFromFraction(fraction: Nothing): Long = {
      val range: Nothing = field.range
      val minBD: Nothing = BigDecimal.valueOf(range.getMinimum)
      val rangeBD: Nothing = BigDecimal.valueOf(range.getMaximum).subtract(minBD).add(BigDecimal.ONE)
      val valueBD: Nothing = fraction.multiply(rangeBD).setScale(0, RoundingMode.FLOOR).add(minBD)
       valueBD.longValueExact
    }

    override def toString: String = {
      val decimal: String = (if (decimalPoint) ",DecimalPoint" else "")
       "Fraction(" + field + "," + minWidth + "," + maxWidth + decimal + ")"
    }

    private final val field: TemporalField = null
    private final val minWidth: Int = 0
    private final val maxWidth: Int = 0
    private final val decimalPoint: Boolean = false
  }

  /**
   * Prints or parses field text.
   */
  private[format] final class TextPrinterParser extends DateTimePrinterParser {
    /**
     * Constructor.
     *
     * @param field  the field to output, not null
     * @param textStyle  the text style, not null
     * @param provider  the text provider, not null
     */
    private[format] def this(field: TemporalField, textStyle: TextStyle, provider: DateTimeTextProvider) {

      this.field = field
      this.textStyle = textStyle
      this.provider = provider
    }

    def format(context: DateTimePrintContext, buf: StringBuilder): Boolean = {
      val value: Long = context.getValue(field)
      if (value == null) {
         false
      }
      var text: String = null
      val chrono: Chronology = context.getTemporal.query(TemporalQuery.chronology)
      if (chrono == null || chrono eq IsoChronology.INSTANCE) {
        text = provider.getText(field, value, textStyle, context.getLocale)
      }
      else {
        text = provider.getText(chrono, field, value, textStyle, context.getLocale)
      }
      if (text == null) {
         numberPrinterParser.format(context, buf)
      }
      buf.append(text)
       true
    }

    def parse(context: DateTimeParseContext, parseText: CharSequence, position: Int): Int = {
      val length: Int = parseText.length
      if (position < 0 || position > length) {
        throw new IndexOutOfBoundsException
      }
      val style: TextStyle = (if (context.isStrict) textStyle else null)
      val chrono: Chronology = context.getEffectiveChronology
      var it: Nothing = null
      if (chrono == null || chrono eq IsoChronology.INSTANCE) {
        it = provider.getTextIterator(field, style, context.getLocale)
      }
      else {
        it = provider.getTextIterator(chrono, field, style, context.getLocale)
      }
      if (it != null) {
        while (it.hasNext) {
          val entry: Nothing = it.next
          val itText: String = entry.getKey
          if (context.subSequenceEquals(itText, 0, parseText, position, itText.length)) {
             context.setParsedField(field, entry.getValue, position, position + itText.length)
          }
        }
        if (context.isStrict) {
           ~position
        }
      }
       numberPrinterParser.parse(context, parseText, position)
    }

    /**
     * Create and cache a number printer parser.
     * @return the number printer parser for this field, not null
     */
    private def numberPrinterParser: DateTimeFormatterBuilder.NumberPrinterParser = {
      if (numberPrinterParser == null) {
        numberPrinterParser = new DateTimeFormatterBuilder.NumberPrinterParser(field, 1, 19, SignStyle.NORMAL)
      }
       numberPrinterParser
    }

    override def toString: String = {
      if (textStyle eq TextStyle.FULL) {
         "Text(" + field + ")"
      }
       "Text(" + field + "," + textStyle + ")"
    }

    private final val field: TemporalField = null
    private final val textStyle: TextStyle = null
    private final val provider: DateTimeTextProvider = null
    /**
     * The cached number printer parser.
     * Immutable and volatile, so no synchronization needed.
     */
    @volatile
    private var numberPrinterParser: DateTimeFormatterBuilder.NumberPrinterParser = null
  }

  /**
   * Prints or parses an ISO-8601 instant.
   */
  private[format] object InstantPrinterParser {
    private final val SECONDS_PER_10000_YEARS: Long = 146097L * 25L * 86400L
    private final val SECONDS_0000_TO_1970: Long = ((146097L * 5L) - (30L * 365L + 7L)) * 86400L
  }

  private[format] final class InstantPrinterParser extends DateTimePrinterParser {
    private[format] def this(fractionalDigits: Int) {

      this.fractionalDigits = fractionalDigits
    }

    def format(context: DateTimePrintContext, buf: StringBuilder): Boolean = {
      val inSecs: Long = context.getValue(INSTANT_SECONDS)
      var inNanos: Long = null
      if (context.getTemporal.isSupported(NANO_OF_SECOND)) {
        inNanos = context.getTemporal.getLong(NANO_OF_SECOND)
      }
      if (inSecs == null) {
         false
      }
      val inSec: Long = inSecs
      var inNano: Int = NANO_OF_SECOND.checkValidIntValue(if (inNanos != null) inNanos else 0)
      if (inSec >= -SECONDS_0000_TO_1970) {
        val zeroSecs: Long = inSec - SECONDS_PER_10000_YEARS + SECONDS_0000_TO_1970
        val hi: Long = Math.floorDiv(zeroSecs, SECONDS_PER_10000_YEARS) + 1
        val lo: Long = Math.floorMod(zeroSecs, SECONDS_PER_10000_YEARS)
        val ldt: Nothing = LocalDateTime.ofEpochSecond(lo - SECONDS_0000_TO_1970, 0, ZoneOffset.UTC)
        if (hi > 0) {
          buf.append('+').append(hi)
        }
        buf.append(ldt)
        if (ldt.getSecond eq 0) {
          buf.append(":00")
        }
      }
      else {
        val zeroSecs: Long = inSec + SECONDS_0000_TO_1970
        val hi: Long = zeroSecs / SECONDS_PER_10000_YEARS
        val lo: Long = zeroSecs % SECONDS_PER_10000_YEARS
        val ldt: Nothing = LocalDateTime.ofEpochSecond(lo - SECONDS_0000_TO_1970, 0, ZoneOffset.UTC)
        val pos: Int = buf.length
        buf.append(ldt)
        if (ldt.getSecond eq 0) {
          buf.append(":00")
        }
        if (hi < 0) {
          if (ldt.getYear eq -10 _000) {
            buf.replace(pos, pos + 2, Long.toString(hi - 1))
          }
          else if (lo == 0) {
            buf.insert(pos, hi)
          }
          else {
            buf.insert(pos + 1, Math.abs(hi))
          }
        }
      }
      if ((fractionalDigits < 0 && inNano > 0) || fractionalDigits > 0) {
        buf.append('.')
        var div: Int = 100 _000_000
          {
            var i: Int = 0
            while (((fractionalDigits == -1 && inNano > 0) || (fractionalDigits == -2 && (inNano > 0 || (i % 3) != 0)) || i < fractionalDigits)) {
              {
                val digit: Int = inNano / div
                buf.append((digit + '0').asInstanceOf[Char])
                inNano = inNano - (digit * div)
                div = div / 10
              }
              ({
                i += 1; i - 1
              })
            }
          }
      }
      buf.append('Z')
       true
    }

    def parse(context: DateTimeParseContext, text: CharSequence, position: Int): Int = {
      val minDigits: Int = (if (fractionalDigits < 0) 0 else fractionalDigits)
      val maxDigits: Int = (if (fractionalDigits < 0) 9 else fractionalDigits)
      val parser: DateTimeFormatterBuilder.CompositePrinterParser = new DateTimeFormatterBuilder().append(DateTimeFormatter.ISO_LOCAL_DATE).appendLiteral('T').appendValue(HOUR_OF_DAY, 2).appendLiteral(':').appendValue(MINUTE_OF_HOUR, 2).appendLiteral(':').appendValue(SECOND_OF_MINUTE, 2).appendFraction(NANO_OF_SECOND, minDigits, maxDigits, true).appendLiteral('Z').toFormatter.toPrinterParser(false)
      val newContext: DateTimeParseContext = context.copy
      val pos: Int = parser.parse(newContext, text, position)
      if (pos < 0) {
         pos
      }
      val yearParsed: Long = newContext.getParsed(YEAR)
      val month: Int = newContext.getParsed(MONTH_OF_YEAR).intValue
      val day: Int = newContext.getParsed(DAY_OF_MONTH).intValue
      var hour: Int = newContext.getParsed(HOUR_OF_DAY).intValue
      val min: Int = newContext.getParsed(MINUTE_OF_HOUR).intValue
      val secVal: Long = newContext.getParsed(SECOND_OF_MINUTE)
      val nanoVal: Long = newContext.getParsed(NANO_OF_SECOND)
      var sec: Int = (if (secVal != null) secVal.intValue else 0)
      val nano: Int = (if (nanoVal != null) nanoVal.intValue else 0)
      var days: Int = 0
      if (hour == 24 && min == 0 && sec == 0 && nano == 0) {
        hour = 0
        days = 1
      }
      else if (hour == 23 && min == 59 && sec == 60) {
        context.setParsedLeapSecond
        sec = 59
      }
      val year: Int = yearParsed.asInstanceOf[Int] % 10 _000
      var instantSecs: Long = 0L
      try {
        val ldt: Nothing = LocalDateTime.of(year, month, day, hour, min, sec, 0).plusDays(days)
        instantSecs = ldt.toEpochSecond(ZoneOffset.UTC)
        instantSecs += Math.multiplyExact(yearParsed / 10 _000L, SECONDS_PER_10000_YEARS)
      }
      catch {
        case ex: RuntimeException => {
           ~position
        }
      }
      var successPos: Int = text.length
      successPos = context.setParsedField(INSTANT_SECONDS, instantSecs, position, successPos)
       context.setParsedField(NANO_OF_SECOND, nano, position, successPos)
    }

    override def toString: String = {
       "Instant()"
    }

    private final val fractionalDigits: Int = 0
  }

  /**
   * Prints or parses an offset ID.
   */
  private[format] object OffsetIdPrinterParser {
    private[format] final val PATTERNS: Array[String] = Array[String]("+HH", "+HHmm", "+HH:mm", "+HHMM", "+HH:MM", "+HHMMss", "+HH:MM:ss", "+HHMMSS", "+HH:MM:SS")
    private[format] final val INSTANCE_ID_Z: DateTimeFormatterBuilder.OffsetIdPrinterParser = new DateTimeFormatterBuilder.OffsetIdPrinterParser("+HH:MM:ss", "Z")
    private[format] final val INSTANCE_ID_ZERO: DateTimeFormatterBuilder.OffsetIdPrinterParser = new DateTimeFormatterBuilder.OffsetIdPrinterParser("+HH:MM:ss", "0")
  }

  private[format] final class OffsetIdPrinterParser extends DateTimePrinterParser {
    /**
     * Constructor.
     *
     * @param pattern  the pattern
     * @param noOffsetText  the text to use for UTC, not null
     */
    private[format] def this(pattern: String, noOffsetText: String) {



      this.`type` = checkPattern(pattern)
      this.noOffsetText = noOffsetText
    }

    private def checkPattern(pattern: String): Int = {
      {
        var i: Int = 0
        while (i < PATTERNS.length) {
          {
            if (PATTERNS(i) == pattern) {
               i
            }
          }
          ({
            i += 1; i - 1
          })
        }
      }
      throw new IllegalArgumentException("Invalid zone offset pattern: " + pattern)
    }

    def format(context: DateTimePrintContext, buf: StringBuilder): Boolean = {
      val offsetSecs: Long = context.getValue(OFFSET_SECONDS)
      if (offsetSecs == null) {
         false
      }
      val totalSecs: Int = Math.toIntExact(offsetSecs)
      if (totalSecs == 0) {
        buf.append(noOffsetText)
      }
      else {
        val absHours: Int = Math.abs((totalSecs / 3600) % 100)
        val absMinutes: Int = Math.abs((totalSecs / 60) % 60)
        val absSeconds: Int = Math.abs(totalSecs % 60)
        val bufPos: Int = buf.length
        var output: Int = absHours
        buf.append(if (totalSecs < 0) "-" else "+").append((absHours / 10 + '0').asInstanceOf[Char]).append((absHours % 10 + '0').asInstanceOf[Char])
        if (`type` >= 3 || (`type` >= 1 && absMinutes > 0)) {
          buf.append(if ((`type` % 2) == 0) ":" else "").append((absMinutes / 10 + '0').asInstanceOf[Char]).append((absMinutes % 10 + '0').asInstanceOf[Char])
          output += absMinutes
          if (`type` >= 7 || (`type` >= 5 && absSeconds > 0)) {
            buf.append(if ((`type` % 2) == 0) ":" else "").append((absSeconds / 10 + '0').asInstanceOf[Char]).append((absSeconds % 10 + '0').asInstanceOf[Char])
            output += absSeconds
          }
        }
        if (output == 0) {
          buf.setLength(bufPos)
          buf.append(noOffsetText)
        }
      }
       true
    }

    def parse(context: DateTimeParseContext, text: CharSequence, position: Int): Int = {
      val length: Int = text.length
      val noOffsetLen: Int = noOffsetText.length
      if (noOffsetLen == 0) {
        if (position == length) {
           context.setParsedField(OFFSET_SECONDS, 0, position, position)
        }
      }
      else {
        if (position == length) {
           ~position
        }
        if (context.subSequenceEquals(text, position, noOffsetText, 0, noOffsetLen)) {
           context.setParsedField(OFFSET_SECONDS, 0, position, position + noOffsetLen)
        }
      }
      val sign: Char = text.charAt(position)
      if (sign == '+' || sign == '-') {
        val negative: Int = (if (sign == '-') -1 else 1)
        val array: Array[Int] = new Array[Int](4)
        array(0) = position + 1
        if ((parseNumber(array, 1, text, true) || parseNumber(array, 2, text, `type` >= 3) || parseNumber(array, 3, text, false)) == false) {
          val offsetSecs: Long = negative * (array(1) * 3600L + array(2) * 60L + array(3))
           context.setParsedField(OFFSET_SECONDS, offsetSecs, position, array(0))
        }
      }
      if (noOffsetLen == 0) {
         context.setParsedField(OFFSET_SECONDS, 0, position, position + noOffsetLen)
      }
       ~position
    }

    /**
     * Parse a two digit zero-prefixed number.
     *
     * @param array  the array of parsed data, 0=pos,1=hours,2=mins,3=secs, not null
     * @param arrayIndex  the index to parse the value into
     * @param parseText  the offset ID, not null
     * @param required  whether this number is required
     * @return true if an error occurred
     */
    private def parseNumber(array: Array[Int], arrayIndex: Int, parseText: CharSequence, required: Boolean): Boolean = {
      if ((`type` + 3) / 2 < arrayIndex) {
         false
      }
      var pos: Int = array(0)
      if ((`type` % 2) == 0 && arrayIndex > 1) {
        if (pos + 1 > parseText.length || parseText.charAt(pos) != ':') {
           required
        }
        pos += 1
      }
      if (pos + 2 > parseText.length) {
         required
      }
      val ch1: Char = parseText.charAt(({
        pos += 1; pos - 1
      }))
      val ch2: Char = parseText.charAt(({
        pos += 1; pos - 1
      }))
      if (ch1 < '0' || ch1 > '9' || ch2 < '0' || ch2 > '9') {
         required
      }
      val value: Int = (ch1 - 48) * 10 + (ch2 - 48)
      if (value < 0 || value > 59) {
         required
      }
      array(arrayIndex) = value
      array(0) = pos
       false
    }

    override def toString: String = {
      val converted: String = noOffsetText.replace("'", "''")
       "Offset(" + PATTERNS(`type`) + ",'" + converted + "')"
    }

    private final val noOffsetText: String = null
    private final val `type`: Int = 0
  }

  /**
   * Prints or parses an offset ID.
   */
  private[format] object LocalizedOffsetIdPrinterParser {
    private def appendHMS(buf: StringBuilder, t: Int): StringBuilder = {
       buf.append((t / 10 + '0').asInstanceOf[Char]).append((t % 10 + '0').asInstanceOf[Char])
    }
  }

  private[format] final class LocalizedOffsetIdPrinterParser extends DateTimePrinterParser {
    /**
     * Constructor.
     *
     * @param style  the style, not null
     */
    private[format] def this(style: TextStyle) {

      this.style = style
    }

    def format(context: DateTimePrintContext, buf: StringBuilder): Boolean = {
      val offsetSecs: Long = context.getValue(OFFSET_SECONDS)
      if (offsetSecs == null) {
         false
      }
      val gmtText: String = "GMT"
      if (gmtText != null) {
        buf.append(gmtText)
      }
      val totalSecs: Int = Math.toIntExact(offsetSecs)
      if (totalSecs != 0) {
        val absHours: Int = Math.abs((totalSecs / 3600) % 100)
        val absMinutes: Int = Math.abs((totalSecs / 60) % 60)
        val absSeconds: Int = Math.abs(totalSecs % 60)
        buf.append(if (totalSecs < 0) "-" else "+")
        if (style eq TextStyle.FULL) {
          appendHMS(buf, absHours)
          buf.append(':')
          appendHMS(buf, absMinutes)
          if (absSeconds != 0) {
            buf.append(':')
            appendHMS(buf, absSeconds)
          }
        }
        else {
          if (absHours >= 10) {
            buf.append((absHours / 10 + '0').asInstanceOf[Char])
          }
          buf.append((absHours % 10 + '0').asInstanceOf[Char])
          if (absMinutes != 0 || absSeconds != 0) {
            buf.append(':')
            appendHMS(buf, absMinutes)
            if (absSeconds != 0) {
              buf.append(':')
              appendHMS(buf, absSeconds)
            }
          }
        }
      }
       true
    }

    private[format] def getDigit(text: CharSequence, position: Int): Int = {
      val c: Char = text.charAt(position)
      if (c < '0' || c > '9') {
         -1
      }
       c - '0'
    }

    def parse(context: DateTimeParseContext, text: CharSequence, position: Int): Int = {
      var pos: Int = position
      val end: Int = pos + text.length
      val gmtText: String = "GMT"
      if (gmtText != null) {
        if (!context.subSequenceEquals(text, pos, gmtText, 0, gmtText.length)) {
           ~position
        }
        pos += gmtText.length
      }
      var negative: Int = 0
      if (pos == end) {
         context.setParsedField(OFFSET_SECONDS, 0, position, pos)
      }
      val sign: Char = text.charAt(pos)
      if (sign == '+') {
        negative = 1
      }
      else if (sign == '-') {
        negative = -1
      }
      else {
         context.setParsedField(OFFSET_SECONDS, 0, position, pos)
      }
      pos += 1
      var h: Int = 0
      var m: Int = 0
      var s: Int = 0
      if (style eq TextStyle.FULL) {
        val h1: Int = getDigit(text, ({
          pos += 1; pos - 1
        }))
        val h2: Int = getDigit(text, ({
          pos += 1; pos - 1
        }))
        if (h1 < 0 || h2 < 0 || text.charAt(({
          pos += 1; pos - 1
        })) != ':') {
           ~position
        }
        h = h1 * 10 + h2
        val m1: Int = getDigit(text, ({
          pos += 1; pos - 1
        }))
        val m2: Int = getDigit(text, ({
          pos += 1; pos - 1
        }))
        if (m1 < 0 || m2 < 0) {
           ~position
        }
        m = m1 * 10 + m2
        if (pos + 2 < end && text.charAt(pos) == ':') {
          val s1: Int = getDigit(text, pos + 1)
          val s2: Int = getDigit(text, pos + 2)
          if (s1 >= 0 && s2 >= 0) {
            s = s1 * 10 + s2
            pos += 3
          }
        }
      }
      else {
        h = getDigit(text, ({
          pos += 1; pos - 1
        }))
        if (h < 0) {
           ~position
        }
        if (pos < end) {
          val h2: Int = getDigit(text, pos)
          if (h2 >= 0) {
            h = h * 10 + h2
            pos += 1
          }
          if (pos + 2 < end && text.charAt(pos) == ':') {
            if (pos + 2 < end && text.charAt(pos) == ':') {
              val m1: Int = getDigit(text, pos + 1)
              val m2: Int = getDigit(text, pos + 2)
              if (m1 >= 0 && m2 >= 0) {
                m = m1 * 10 + m2
                pos += 3
                if (pos + 2 < end && text.charAt(pos) == ':') {
                  val s1: Int = getDigit(text, pos + 1)
                  val s2: Int = getDigit(text, pos + 2)
                  if (s1 >= 0 && s2 >= 0) {
                    s = s1 * 10 + s2
                    pos += 3
                  }
                }
              }
            }
          }
        }
      }
      val offsetSecs: Long = negative * (h * 3600L + m * 60L + s)
       context.setParsedField(OFFSET_SECONDS, offsetSecs, position, pos)
    }

    override def toString: String = {
       "LocalizedOffset(" + style + ")"
    }

    private final val style: TextStyle = null
  }

  /**
   * Prints or parses a zone ID.
   */
  private[format] object ZoneTextPrinterParser {
    private final val STD: Int = 0
    private final val DST: Int = 1
    private final val GENERIC: Int = 2
    private final val cache: Map[String, Nothing] = new Nothing
  }

  private[format] final class ZoneTextPrinterParser extends ZoneIdPrinterParser {
    private[format] def this(textStyle: TextStyle, preferredZones: Set[ZoneId]) {

      `super`(TemporalQuery.zone, "ZoneText(" + textStyle + ")")
      this.textStyle =
      if (preferredZones != null && preferredZones.size != 0) {
        this.preferredZones = new HashSet[String]
        import scala.collection.JavaConversions._
        for (id <- preferredZones) {
          this.preferredZones.add(id.getId)
        }
      }
    }

    private def getDisplayName(id: String, `type`: Int, locale: Locale): String = {
      if (textStyle eq TextStyle.NARROW) {
         null
      }
      var names: Array[String] = null
      val ref: Nothing = cache.get(id)
      var perLocale: Map[Locale, Array[String]] = null
      if (ref == null || (({
        perLocale = ref.get; perLocale
      })) == null || (({
        names = perLocale.get(locale); names
      })) == null) {
        names = TimeZoneNameUtility.retrieveDisplayNames(id, locale)
        if (names == null) {
           null
        }
        names = Arrays.copyOfRange(names, 0, 7)
        names(5) = TimeZoneNameUtility.retrieveGenericDisplayName(id, TimeZone.LONG, locale)
        if (names(5) == null) {
          names(5) = names(0)
        }
        names(6) = TimeZoneNameUtility.retrieveGenericDisplayName(id, TimeZone.SHORT, locale)
        if (names(6) == null) {
          names(6) = names(0)
        }
        if (perLocale == null) {
          perLocale = new Nothing
        }
        perLocale.put(locale, names)
        cache.put(id, new Nothing(perLocale))
      }
      `type` match {
        case STD =>
           names(textStyle.zoneNameStyleIndex + 1)
        case DST =>
           names(textStyle.zoneNameStyleIndex + 3)
      }
       names(textStyle.zoneNameStyleIndex + 5)
    }

    override def format(context: DateTimePrintContext, buf: StringBuilder): Boolean = {
      val zone: ZoneId = context.getValue(TemporalQuery.zoneId)
      if (zone == null) {
         false
      }
      var zname: String = zone.getId
      if (!(zone.isInstanceOf[ZoneOffset])) {
        val dt: TemporalAccessor = context.getTemporal
        val name: String = getDisplayName(zname, if (dt.isSupported(ChronoField.INSTANT_SECONDS)) (if (zone.getRules.isDaylightSavings(Instant.from(dt))) DST else STD) else GENERIC, context.getLocale)
        if (name != null) {
          zname = name
        }
      }
      buf.append(zname)
       true
    }

    protected override def getTree(context: DateTimeParseContext): DateTimeFormatterBuilder.PrefixTree = {
      if (textStyle eq TextStyle.NARROW) {
         super.getTree(context)
      }
      val locale: Locale = context.getLocale
      val isCaseSensitive: Boolean = context.isCaseSensitive
      val regionIds: Set[String] = ZoneRulesProvider.getAvailableZoneIds
      val regionIdsSize: Int = regionIds.size
      val cached: Map[Locale, Nothing] = if (isCaseSensitive) cachedTree else cachedTreeCI
      var entry: Nothing = null
      var tree: DateTimeFormatterBuilder.PrefixTree = null
      var zoneStrings: Array[Array[String]] = null
      if ((({
        entry = cached.get(locale); entry
      })) == null || (entry.getKey ne regionIdsSize || (({
        tree = entry.getValue.get; tree
      })) == null)) {
        tree = PrefixTree.newTree(context)
        zoneStrings = TimeZoneNameUtility.getZoneStrings(locale)
        for (names <- zoneStrings) {
          var zid: String = names(0)
          if (!regionIds.contains(zid)) {
            continue //todo: continue is not supported
          }
          tree.add(zid, zid)
          zid = ZoneName.toZid(zid, locale)
          var i: Int = if (textStyle eq TextStyle.FULL) 1 else 2
          while (i < names.length) {
            {
              tree.add(names(i), zid)
            }
            i += 2
          }
        }
        if (preferredZones != null) {
          for (names <- zoneStrings) {
            val zid: String = names(0)
            if (!preferredZones.contains(zid) || !regionIds.contains(zid)) {
              continue //todo: continue is not supported
            }
            var i: Int = if (textStyle eq TextStyle.FULL) 1 else 2
            while (i < names.length) {
              {
                tree.add(names(i), zid)
              }
              i += 2
            }
          }
        }
        cached.put(locale, new Nothing(regionIdsSize, new Nothing(tree)))
      }
       tree
    }

    /** The text style to output. */
    private final val textStyle: TextStyle = null
    /** The preferred zoneid map */
    private var preferredZones: Set[String] = null
    private final val cachedTree: Map[Locale, Nothing] = new HashMap[K, V]
    private final val cachedTreeCI: Map[Locale, Nothing] = new HashMap[K, V]
  }

  /**
   * Prints or parses a zone ID.
   */
  private[format] object ZoneIdPrinterParser {
    /**
     * The cached tree to speed up parsing.
     */
    @volatile
    private var cachedPrefixTree: Nothing = null
    @volatile
    private var cachedPrefixTreeCI: Nothing = null
  }

  private[format] class ZoneIdPrinterParser extends DateTimePrinterParser {
    private[format] def this(query: TemporalQuery[ZoneId], description: String) {

      this.query = query
      this.description = description
    }

    def format(context: DateTimePrintContext, buf: StringBuilder): Boolean = {
      val zone: ZoneId = context.getValue(query)
      if (zone == null) {
         false
      }
      buf.append(zone.getId)
       true
    }

    protected def getTree(context: DateTimeParseContext): DateTimeFormatterBuilder.PrefixTree = {
      val regionIds: Set[String] = ZoneRulesProvider.getAvailableZoneIds
      val regionIdsSize: Int = regionIds.size
      var cached: Nothing = if (context.isCaseSensitive) cachedPrefixTree else cachedPrefixTreeCI
      if (cached == null || cached.getKey ne regionIdsSize) {
        this synchronized {
          cached = if (context.isCaseSensitive) cachedPrefixTree else cachedPrefixTreeCI
          if (cached == null || cached.getKey ne regionIdsSize) {
            cached = new Nothing(regionIdsSize, PrefixTree.newTree(regionIds, context))
            if (context.isCaseSensitive) {
              cachedPrefixTree = cached
            }
            else {
              cachedPrefixTreeCI = cached
            }
          }
        }
      }
       cached.getValue
    }

    /**
     * This implementation looks for the longest matching string.
     * For example, parsing Etc/GMT-2 will return Etc/GMC-2 rather than just
     * Etc/GMC although both are valid.
     */
    def parse(context: DateTimeParseContext, text: CharSequence, position: Int): Int = {
      val length: Int = text.length
      if (position > length) {
        throw new IndexOutOfBoundsException
      }
      if (position == length) {
         ~position
      }
      val nextChar: Char = text.charAt(position)
      if (nextChar == '+' || nextChar == '-') {
         parseOffsetBased(context, text, position, position, OffsetIdPrinterParser.INSTANCE_ID_Z)
      }
      else if (length >= position + 2) {
        val nextNextChar: Char = text.charAt(position + 1)
        if (context.charEquals(nextChar, 'U') && context.charEquals(nextNextChar, 'T')) {
          if (length >= position + 3 && context.charEquals(text.charAt(position + 2), 'C')) {
             parseOffsetBased(context, text, position, position + 3, OffsetIdPrinterParser.INSTANCE_ID_ZERO)
          }
           parseOffsetBased(context, text, position, position + 2, OffsetIdPrinterParser.INSTANCE_ID_ZERO)
        }
        else if (context.charEquals(nextChar, 'G') && length >= position + 3 && context.charEquals(nextNextChar, 'M') && context.charEquals(text.charAt(position + 2), 'T')) {
           parseOffsetBased(context, text, position, position + 3, OffsetIdPrinterParser.INSTANCE_ID_ZERO)
        }
      }
      val tree: DateTimeFormatterBuilder.PrefixTree = getTree(context)
      val ppos: ParsePosition = new ParsePosition(position)
      val parsedZoneId: String = tree.`match`(text, ppos)
      if (parsedZoneId == null) {
        if (context.charEquals(nextChar, 'Z')) {
          context.setParsed(ZoneOffset.UTC)
           position + 1
        }
         ~position
      }
      context.setParsed(ZoneId.of(parsedZoneId))
       ppos.getIndex
    }

    /**
     * Parse an offset following a prefix and set the ZoneId if it is valid.
     * To matching the parsing of ZoneId.of the values are not normalized
     * to ZoneOffsets.
     *
     * @param context the parse context
     * @param text the input text
     * @param prefixPos start of the prefix
     * @param position start of text after the prefix
     * @param parser parser for the value after the prefix
     * @return the position after the parse
     */
    private def parseOffsetBased(context: DateTimeParseContext, text: CharSequence, prefixPos: Int, position: Int, parser: DateTimeFormatterBuilder.OffsetIdPrinterParser): Int = {
      val prefix: String = text.toString.substring(prefixPos, position).toUpperCase
      if (position >= text.length) {
        context.setParsed(ZoneId.of(prefix))
         position
      }
      if (text.charAt(position) == '0' || context.charEquals(text.charAt(position), 'Z')) {
        context.setParsed(ZoneId.of(prefix))
         position
      }
      val newContext: DateTimeParseContext = context.copy
      val endPos: Int = parser.parse(newContext, text, position)
      try {
        if (endPos < 0) {
          if (parser eq OffsetIdPrinterParser.INSTANCE_ID_Z) {
             ~prefixPos
          }
          context.setParsed(ZoneId.of(prefix))
           position
        }
        val offset: Int = newContext.getParsed(OFFSET_SECONDS).longValue.asInstanceOf[Int]
        val zoneOffset: ZoneOffset = ZoneOffset.ofTotalSeconds(offset)
        context.setParsed(ZoneId.ofOffset(prefix, zoneOffset))
         endPos
      }
      catch {
        case dte: DateTimeException => {
           ~prefixPos
        }
      }
    }

    override def toString: String = {
       description
    }

    private final val query: TemporalQuery[ZoneId] = null
    private final val description: String = null
  }

  /**
   * A String based prefix tree for parsing time-zone names.
   */
  private[format] object PrefixTree {
    /**
     * Creates a new prefix parsing tree based on parse context.
     *
     * @param context  the parse context
     * @return the tree, not null
     */
    def newTree(context: DateTimeParseContext): DateTimeFormatterBuilder.PrefixTree = {
      if (context.isCaseSensitive) {
         new DateTimeFormatterBuilder.PrefixTree("", null, null)
      }
       new DateTimeFormatterBuilder.PrefixTree#CI("", null, null)
    }

    /**
     * Creates a new prefix parsing tree.
     *
     * @param keys  a set of strings to build the prefix parsing tree, not null
     * @param context  the parse context
     * @return the tree, not null
     */
    def newTree(keys: Set[String], context: DateTimeParseContext): DateTimeFormatterBuilder.PrefixTree = {
      val tree: DateTimeFormatterBuilder.PrefixTree = newTree(context)
      import scala.collection.JavaConversions._
      for (k <- keys) {
        tree.add0(k, k)
      }
       tree
    }

    /**
     * Case Insensitive prefix tree.
     */
    private class CI extends PrefixTree {
      private def this(k: String, v: String, child: DateTimeFormatterBuilder.PrefixTree) {

        `super`(k, v, child)
      }

      protected override def newNode(k: String, v: String, child: DateTimeFormatterBuilder.PrefixTree): DateTimeFormatterBuilder.PrefixTree#CI = {
         new DateTimeFormatterBuilder.PrefixTree#CI(k, v, child)
      }

      protected override def isEqual(c1: Char, c2: Char): Boolean = {
         DateTimeParseContext.charEqualsIgnoreCase(c1, c2)
      }

      protected override def prefixOf(text: CharSequence, off: Int, end: Int): Boolean = {
        var len: Int = key.length
        if (len > end - off) {
           false
        }
        val off0: Int = 0
        while (({
          len -= 1; len + 1
        }) > 0) {
          if (!isEqual(key.charAt(({
            off0 += 1; off0 - 1
          })), text.charAt(({
            off += 1; off - 1
          })))) {
             false
          }
        }
         true
      }
    }

    /**
     * Lenient prefix tree. Case insensitive and ignores characters
     * like space, underscore and slash.
     */
    private class LENIENT extends CI {
      private def this(k: String, v: String, child: DateTimeFormatterBuilder.PrefixTree) {

        `super`(k, v, child)
      }

      protected override def newNode(k: String, v: String, child: DateTimeFormatterBuilder.PrefixTree): DateTimeFormatterBuilder.PrefixTree#CI = {
         new DateTimeFormatterBuilder.PrefixTree#LENIENT(k, v, child)
      }

      private def isLenientChar(c: Char): Boolean = {
         c == ' ' || c == '_' || c == '/'
      }

      protected override def toKey(k: String): String = {
        {
          var i: Int = 0
          while (i < k.length) {
            {
              if (isLenientChar(k.charAt(i))) {
                val sb: StringBuilder = new StringBuilder(k.length)
                sb.append(k, 0, i)
                i += 1
                while (i < k.length) {
                  if (!isLenientChar(k.charAt(i))) {
                    sb.append(k.charAt(i))
                  }
                  i += 1
                }
                 sb.toString
              }
            }
            ({
              i += 1; i - 1
            })
          }
        }
         k
      }

      override def `match`(text: CharSequence, pos: ParsePosition): String = {
        var off: Int = pos.getIndex
        val end: Int = text.length
        val len: Int = key.length
        val koff: Int = 0
        while (koff < len && off < end) {
          if (isLenientChar(text.charAt(off))) {
            off += 1
            continue //todo: continue is not supported
          }
          if (!isEqual(key.charAt(({
            koff += 1; koff - 1
          })), text.charAt(({
            off += 1; off - 1
          })))) {
             null
          }
        }
        if (koff != len) {
           null
        }
        if (child != null && off != end) {
          var off0: Int = off
          while (off0 < end && isLenientChar(text.charAt(off0))) {
            off0 += 1
          }
          if (off0 < end) {
            var c: DateTimeFormatterBuilder.PrefixTree = child
            do {
              if (isEqual(c.c0, text.charAt(off0))) {
                pos.setIndex(off0)
                val found: String = c.`match`(text, pos)
                if (found != null) {
                   found
                }
                break //todo: break is not supported
              }
              c = c.sibling
            } while (c != null)
          }
        }
        pos.setIndex(off)
         value
      }
    }

  }

  private[format] class PrefixTree {
    private def this(k: String, v: String, child: DateTimeFormatterBuilder.PrefixTree) {

      this.key = k
      this.value = v
      this.child = child
      if (k.length == 0) {
        c0 = 0xffff
      }
      else {
        c0 = key.charAt(0)
      }
    }

    /**
     * Clone a copy of this tree
     */
    def copyTree: DateTimeFormatterBuilder.PrefixTree = {
      val copy: DateTimeFormatterBuilder.PrefixTree = new DateTimeFormatterBuilder.PrefixTree(key, value, null)
      if (child != null) {
        copy.child = child.copyTree
      }
      if (sibling != null) {
        copy.sibling = sibling.copyTree
      }
       copy
    }

    /**
     * Adds a pair of {key, value} into the prefix tree.
     *
     * @param k  the key, not null
     * @param v  the value, not null
     * @return  true if the pair is added successfully
     */
    def add(k: String, v: String): Boolean = {
       add0(k, v)
    }

    private def add0(k: String, v: String): Boolean = {
      k = toKey(k)
      val prefixLen: Int = prefixLength(k)
      if (prefixLen == key.length) {
        if (prefixLen < k.length) {
          val subKey: String = k.substring(prefixLen)
          var c: DateTimeFormatterBuilder.PrefixTree = child
          while (c != null) {
            if (isEqual(c.c0, subKey.charAt(0))) {
               c.add0(subKey, v)
            }
            c = c.sibling
          }
          c = newNode(subKey, v, null)
          c.sibling = child
          child = c
           true
        }
        value = v
         true
      }
      val n1: DateTimeFormatterBuilder.PrefixTree = newNode(key.substring(prefixLen), value, child)
      key = k.substring(0, prefixLen)
      child = n1
      if (prefixLen < k.length) {
        val n2: DateTimeFormatterBuilder.PrefixTree = newNode(k.substring(prefixLen), v, null)
        child.sibling = n2
        value = null
      }
      else {
        value = v
      }
       true
    }

    /**
     * Match text with the prefix tree.
     *
     * @param text  the input text to parse, not null
     * @param off  the offset position to start parsing at
     * @param end  the end position to stop parsing
     * @return the resulting string, or null if no match found.
     */
    def `match`(text: CharSequence, off: Int, end: Int): String = {
      if (!prefixOf(text, off, end)) {
         null
      }
      if (child != null && (({
        off += key.length; off
      })) != end) {
        var c: DateTimeFormatterBuilder.PrefixTree = child
        do {
          if (isEqual(c.c0, text.charAt(off))) {
            val found: String = c.`match`(text, off, end)
            if (found != null) {
               found
            }
             value
          }
          c = c.sibling
        } while (c != null)
      }
       value
    }

    /**
     * Match text with the prefix tree.
     *
     * @param text  the input text to parse, not null
     * @param pos  the position to start parsing at, from 0 to the text
     *             length. Upon return, position will be updated to the new parse
     *             position, or unchanged, if no match found.
     * @return the resulting string, or null if no match found.
     */
    def `match`(text: CharSequence, pos: ParsePosition): String = {
      var off: Int = pos.getIndex
      val end: Int = text.length
      if (!prefixOf(text, off, end)) {
         null
      }
      off += key.length
      if (child != null && off != end) {
        var c: DateTimeFormatterBuilder.PrefixTree = child
        do {
          if (isEqual(c.c0, text.charAt(off))) {
            pos.setIndex(off)
            val found: String = c.`match`(text, pos)
            if (found != null) {
               found
            }
            break //todo: break is not supported
          }
          c = c.sibling
        } while (c != null)
      }
      pos.setIndex(off)
       value
    }

    protected def toKey(k: String): String = {
       k
    }

    protected def newNode(k: String, v: String, child: DateTimeFormatterBuilder.PrefixTree): DateTimeFormatterBuilder.PrefixTree = {
       new DateTimeFormatterBuilder.PrefixTree(k, v, child)
    }

    protected def isEqual(c1: Char, c2: Char): Boolean = {
       c1 == c2
    }

    protected def prefixOf(text: CharSequence, off: Int, end: Int): Boolean = {
      if (text.isInstanceOf[String]) {
         (text.asInstanceOf[String]).startsWith(key, off)
      }
      var len: Int = key.length
      if (len > end - off) {
         false
      }
      val off0: Int = 0
      while (({
        len -= 1; len + 1
      }) > 0) {
        if (!isEqual(key.charAt(({
          off0 += 1; off0 - 1
        })), text.charAt(({
          off += 1; off - 1
        })))) {
           false
        }
      }
       true
    }

    private def prefixLength(k: String): Int = {
      var off: Int = 0
      while (off < k.length && off < key.length) {
        if (!isEqual(k.charAt(off), key.charAt(off))) {
           off
        }
        off += 1
      }
       off
    }

    protected var key: String = null
    protected var value: String = null
    protected var c0: Char = 0
    protected var child: DateTimeFormatterBuilder.PrefixTree = null
    protected var sibling: DateTimeFormatterBuilder.PrefixTree = null
  }

  /**
   * Prints or parses a chronology.
   */
  private[format] final class ChronoPrinterParser extends DateTimePrinterParser {
    private[format] def this(textStyle: TextStyle) {

      this.textStyle = textStyle
    }

    def format(context: DateTimePrintContext, buf: StringBuilder): Boolean = {
      val chrono: Chronology = context.getValue(TemporalQuery.chronology)
      if (chrono == null) {
         false
      }
      if (textStyle == null) {
        buf.append(chrono.getId)
      }
      else {
        buf.append(getChronologyName(chrono, context.getLocale))
      }
       true
    }

    def parse(context: DateTimeParseContext, text: CharSequence, position: Int): Int = {
      if (position < 0 || position > text.length) {
        throw new IndexOutOfBoundsException
      }
      val chronos: Set[Chronology] = Chronology.getAvailableChronologies
      var bestMatch: Chronology = null
      var matchLen: Int = -1
      import scala.collection.JavaConversions._
      for (chrono <- chronos) {
        var name: String = null
        if (textStyle == null) {
          name = chrono.getId
        }
        else {
          name = getChronologyName(chrono, context.getLocale)
        }
        val nameLen: Int = name.length
        if (nameLen > matchLen && context.subSequenceEquals(text, position, name, 0, nameLen)) {
          bestMatch = chrono
          matchLen = nameLen
        }
      }
      if (bestMatch == null) {
         ~position
      }
      context.setParsed(bestMatch)
       position + matchLen
    }

    /**
     * Returns the chronology name of the given chrono in the given locale
     * if available, or the chronology Id otherwise. The regular ResourceBundle
     * search path is used for looking up the chronology name.
     *
     * @param chrono  the chronology, not null
     * @param locale  the locale, not null
     * @return the chronology name of chrono in locale, or the id if no name is available
     * @throws NullPointerException if chrono or locale is null
     */
    private def getChronologyName(chrono: Chronology, locale: Locale): String = {
      val key: String = "calendarname." + chrono.getCalendarType
      val name: String = DateTimeTextProvider.getLocalizedResource(key, locale)
       if (name != null) name else chrono.getId
    }

    /** The text style to output, null means the ID. */
    private final val textStyle: TextStyle = null
  }

  /**
   * Prints or parses a localized pattern.
   */
  private[format] object LocalizedPrinterParser {
    /** Cache of formatters. */
    private final val FORMATTER_CACHE: Nothing = new Nothing(16, 0.75f, 2)
  }

  private[format] final class LocalizedPrinterParser extends DateTimePrinterParser {
    /**
     * Constructor.
     *
     * @param dateStyle  the date style to use, may be null
     * @param timeStyle  the time style to use, may be null
     */
    private[format] def this(dateStyle: FormatStyle, timeStyle: FormatStyle) {

      this.dateStyle = dateStyle
      this.timeStyle = timeStyle
    }

    def format(context: DateTimePrintContext, buf: StringBuilder): Boolean = {
      val chrono: Chronology = Chronology.from(context.getTemporal)
       formatter(context.getLocale, chrono).toPrinterParser(false).format(context, buf)
    }

    def parse(context: DateTimeParseContext, text: CharSequence, position: Int): Int = {
      val chrono: Chronology = context.getEffectiveChronology
       formatter(context.getLocale, chrono).toPrinterParser(false).parse(context, text, position)
    }

    /**
     * Gets the formatter to use.
     * <p>
     * The formatter will be the most appropriate to use for the date and time style in the locale.
     * For example, some locales will use the month name while others will use the number.
     *
     * @param locale  the locale to use, not null
     * @param chrono  the chronology to use, not null
     * @return the formatter, not null
     * @throws IllegalArgumentException if the formatter cannot be found
     */
    private def formatter(locale: Locale, chrono: Chronology): DateTimeFormatter = {
      val key: String = chrono.getId + '|' + locale.toString + '|' + dateStyle + timeStyle
      var formatter: DateTimeFormatter = FORMATTER_CACHE.get(key)
      if (formatter == null) {
        val pattern: String = getLocalizedDateTimePattern(dateStyle, timeStyle, chrono, locale)
        formatter = new DateTimeFormatterBuilder().appendPattern(pattern).toFormatter(locale)
        val old: DateTimeFormatter = FORMATTER_CACHE.putIfAbsent(key, formatter)
        if (old != null) {
          formatter = old
        }
      }
       formatter
    }

    override def toString: String = {
       "Localized(" + (if (dateStyle != null) dateStyle else "") + "," + (if (timeStyle != null) timeStyle else "") + ")"
    }

    private final val dateStyle: FormatStyle = null
    private final val timeStyle: FormatStyle = null
  }

  /**
   * Prints or parses a localized pattern from a localized field.
   * The specific formatter and parameters is not selected until the
   * the field is to be printed or parsed.
   * The locale is needed to select the proper WeekFields from which
   * the field for day-of-week, week-of-month, or week-of-year is selected.
   */
  private[format] final class WeekBasedFieldPrinterParser extends DateTimePrinterParser {
    /**
     * Constructor.
     *
     * @param chr the pattern format letter that added this PrinterParser.
     * @param count the repeat count of the format letter
     */
    private[format] def this(chr: Char, count: Int) {

      this.chr = chr
      this.count = count
    }

    def format(context: DateTimePrintContext, buf: StringBuilder): Boolean = {
       printerParser(context.getLocale).format(context, buf)
    }

    def parse(context: DateTimeParseContext, text: CharSequence, position: Int): Int = {
       printerParser(context.getLocale).parse(context, text, position)
    }

    /**
     * Gets the printerParser to use based on the field and the locale.
     *
     * @param locale  the locale to use, not null
     * @return the formatter, not null
     * @throws IllegalArgumentException if the formatter cannot be found
     */
    private def printerParser(locale: Locale): DateTimeFormatterBuilder.DateTimePrinterParser = {
      val weekDef: Nothing = WeekFields.of(locale)
      var field: TemporalField = null
      chr match {
        case 'Y' =>
          field = weekDef.weekBasedYear
          if (count == 2) {
             new DateTimeFormatterBuilder.ReducedPrinterParser(field, 2, 2, 2000, 0)
          }
          else {
             new DateTimeFormatterBuilder.NumberPrinterParser(field, count, 19, if ((count < 4)) SignStyle.NORMAL else SignStyle.EXCEEDS_PAD, -1)
          }
        case 'e' =>
        case 'c' =>
          field = weekDef.dayOfWeek
          break //todo: break is not supported
        case 'w' =>
          field = weekDef.weekOfWeekBasedYear
          break //todo: break is not supported
        case 'W' =>
          field = weekDef.weekOfMonth
          break //todo: break is not supported
        case _ =>
          throw new IllegalStateException("unreachable")
      }
       new DateTimeFormatterBuilder.NumberPrinterParser(field, (if (count == 2) 2 else 1), 2, SignStyle.NOT_NEGATIVE)
    }

    override def toString: String = {
      val sb: StringBuilder = new StringBuilder(30)
      sb.append("Localized(")
      if (chr == 'Y') {
        if (count == 1) {
          sb.append("WeekBasedYear")
        }
        else if (count == 2) {
          sb.append("ReducedValue(WeekBasedYear,2,2000)")
        }
        else {
          sb.append("WeekBasedYear,").append(count).append(",").append(19).append(",").append(if ((count < 4)) SignStyle.NORMAL else SignStyle.EXCEEDS_PAD)
        }
      }
      else {
        chr match {
          case 'c' =>
          case 'e' =>
            sb.append("DayOfWeek")
            break //todo: break is not supported
          case 'w' =>
            sb.append("WeekOfWeekBasedYear")
            break //todo: break is not supported
          case 'W' =>
            sb.append("WeekOfMonth")
            break //todo: break is not supported
          case _ =>
            break //todo: break is not supported
        }
        sb.append(",")
        sb.append(count)
      }
      sb.append(")")
       sb.toString
    }

    private var chr: Char = 0
    private var count: Int = 0
  }

}

final class DateTimeFormatterBuilder {
  /**
   * Constructs a new instance of the builder.
   */
  def  {

    `super`
    parent = null
    optional = false
  }

  /**
   * Constructs a new instance of the builder.
   *
   * @param parent  the parent builder, not null
   * @param optional  whether the formatter is optional, not null
   */
  private def this(parent: DateTimeFormatterBuilder, optional: Boolean) {

    `super`
    this.parent = parent
    this.optional = optional
  }

  /**
   * Changes the parse style to be case sensitive for the remainder of the formatter.
   * <p>
   * Parsing can be case sensitive or insensitive - by default it is case sensitive.
   * This method allows the case sensitivity setting of parsing to be changed.
   * <p>
   * Calling this method changes the state of the builder such that all
   * subsequent builder method calls will parse text in case sensitive mode.
   * See {@link #parseCaseInsensitive} for the opposite setting.
   * The parse case sensitive/insensitive methods may be called at any point
   * in the builder, thus the parser can swap between case parsing modes
   * multiple times during the parse.
   * <p>
   * Since the default is case sensitive, this method should only be used after
   * a previous call to {@code #parseCaseInsensitive}.
   *
   * @return this, for chaining, not null
   */
  def parseCaseSensitive: DateTimeFormatterBuilder = {
    appendInternal(SettingsParser.SENSITIVE)
     this
  }

  /**
   * Changes the parse style to be case insensitive for the remainder of the formatter.
   * <p>
   * Parsing can be case sensitive or insensitive - by default it is case sensitive.
   * This method allows the case sensitivity setting of parsing to be changed.
   * <p>
   * Calling this method changes the state of the builder such that all
   * subsequent builder method calls will parse text in case insensitive mode.
   * See {@link #parseCaseSensitive()} for the opposite setting.
   * The parse case sensitive/insensitive methods may be called at any point
   * in the builder, thus the parser can swap between case parsing modes
   * multiple times during the parse.
   *
   * @return this, for chaining, not null
   */
  def parseCaseInsensitive: DateTimeFormatterBuilder = {
    appendInternal(SettingsParser.INSENSITIVE)
     this
  }

  /**
   * Changes the parse style to be strict for the remainder of the formatter.
   * <p>
   * Parsing can be strict or lenient - by default its strict.
   * This controls the degree of flexibility in matching the text and sign styles.
   * <p>
   * When used, this method changes the parsing to be strict from this point onwards.
   * As strict is the default, this is normally only needed after calling {@link #parseLenient()}.
   * The change will remain in force until the end of the formatter that is eventually
   * constructed or until {@code parseLenient} is called.
   *
   * @return this, for chaining, not null
   */
  def parseStrict: DateTimeFormatterBuilder = {
    appendInternal(SettingsParser.STRICT)
     this
  }

  /**
   * Changes the parse style to be lenient for the remainder of the formatter.
   * Note that case sensitivity is set separately to this method.
   * <p>
   * Parsing can be strict or lenient - by default its strict.
   * This controls the degree of flexibility in matching the text and sign styles.
   * Applications calling this method should typically also call {@link #parseCaseInsensitive()}.
   * <p>
   * When used, this method changes the parsing to be lenient from this point onwards.
   * The change will remain in force until the end of the formatter that is eventually
   * constructed or until {@code parseStrict} is called.
   *
   * @return this, for chaining, not null
   */
  def parseLenient: DateTimeFormatterBuilder = {
    appendInternal(SettingsParser.LENIENT)
     this
  }

  /**
   * Appends a default value for a field to the formatter for use in parsing.
   * <p>
   * This appends an instruction to the builder to inject a default value
   * into the parsed result. This is especially useful in conjunction with
   * optional parts of the formatter.
   * <p>
   * For example, consider a formatter that parses the year, followed by
   * an optional month, with a further optional day-of-month. Using such a
   * formatter would require the calling code to check whether a full date,
   * year-month or just a year had been parsed. This method can be used to
   * default the month and day-of-month to a sensible value, such as the
   * first of the month, allowing the calling code to always get a date.
   * <p>
   * During formatting, this method has no effect.
   * <p>
   * During parsing, the current state of the parse is inspected.
   * If the specified field has no associated value, because it has not been
   * parsed successfully at that point, then the specified value is injected
   * into the parse result. Injection is immediate, thus the field-value pair
   * will be visible to any subsequent elements in the formatter.
   * As such, this method is normally called at the end of the builder.
   *
   * @param field  the field to default the value of, not null
   * @param value  the value to default the field to
   * @return this, for chaining, not null
   */
  def parseDefaulting(field: TemporalField, value: Long): DateTimeFormatterBuilder = {

    appendInternal(new DateTimeFormatterBuilder.DefaultValueParser(field, value))
     this
  }

  /**
   * Appends the value of a date-time field to the formatter using a normal
   * output style.
   * <p>
   * The value of the field will be output during a format.
   * If the value cannot be obtained then an exception will be thrown.
   * <p>
   * The value will be printed as per the normal format of an integer value.
   * Only negative numbers will be signed. No padding will be added.
   * <p>
   * The parser for a variable width value such as this normally behaves greedily,
   * requiring one digit, but accepting as many digits as possible.
   * This behavior can be affected by 'adjacent value parsing'.
   * See {@link #appendValue(java.time.temporal.TemporalField, int)} for full details.
   *
   * @param field  the field to append, not null
   * @return this, for chaining, not null
   */
  def appendValue(field: TemporalField): DateTimeFormatterBuilder = {

    appendValue(new DateTimeFormatterBuilder.NumberPrinterParser(field, 1, 19, SignStyle.NORMAL))
     this
  }

  /**
   * Appends the value of a date-time field to the formatter using a fixed
   * width, zero-padded approach.
   * <p>
   * The value of the field will be output during a format.
   * If the value cannot be obtained then an exception will be thrown.
   * <p>
   * The value will be zero-padded on the left. If the size of the value
   * means that it cannot be printed within the width then an exception is thrown.
   * If the value of the field is negative then an exception is thrown during formatting.
   * <p>
   * This method supports a special technique of parsing known as 'adjacent value parsing'.
   * This technique solves the problem where a value, variable or fixed width, is followed by one or more
   * fixed length values. The standard parser is greedy, and thus it would normally
   * steal the digits that are needed by the fixed width value parsers that follow the
   * variable width one.
   * <p>
   * No action is required to initiate 'adjacent value parsing'.
   * When a call to {@code appendValue} is made, the builder
   * enters adjacent value parsing setup mode. If the immediately subsequent method
   * call or calls on the same builder are for a fixed width value, then the parser will reserve
   * space so that the fixed width values can be parsed.
   * <p>
   * For example, consider {@code builder.appendValue(YEAR).appendValue(MONTH_OF_YEAR, 2);}
   * The year is a variable width parse of between 1 and 19 digits.
   * The month is a fixed width parse of 2 digits.
   * Because these were appended to the same builder immediately after one another,
   * the year parser will reserve two digits for the month to parse.
   * Thus, the text '201106' will correctly parse to a year of 2011 and a month of 6.
   * Without adjacent value parsing, the year would greedily parse all six digits and leave
   * nothing for the month.
   * <p>
   * Adjacent value parsing applies to each set of fixed width not-negative values in the parser
   * that immediately follow any kind of value, variable or fixed width.
   * Calling any other append method will end the setup of adjacent value parsing.
   * Thus, in the unlikely event that you need to avoid adjacent value parsing behavior,
   * simply add the {@code appendValue} to another {@code DateTimeFormatterBuilder}
   * and add that to this builder.
   * <p>
   * If adjacent parsing is active, then parsing must match exactly the specified
   * number of digits in both strict and lenient modes.
   * In addition, no positive or negative sign is permitted.
   *
   * @param field  the field to append, not null
   * @param width  the width of the printed field, from 1 to 19
   * @return this, for chaining, not null
   * @throws IllegalArgumentException if the width is invalid
   */
  def appendValue(field: TemporalField, width: Int): DateTimeFormatterBuilder = {

    if (width < 1 || width > 19) {
      throw new IllegalArgumentException("The width must be from 1 to 19 inclusive but was " + width)
    }
    val pp: DateTimeFormatterBuilder.NumberPrinterParser = new DateTimeFormatterBuilder.NumberPrinterParser(field, width, width, SignStyle.NOT_NEGATIVE)
    appendValue(pp)
     this
  }

  /**
   * Appends the value of a date-time field to the formatter providing full
   * control over formatting.
   * <p>
   * The value of the field will be output during a format.
   * If the value cannot be obtained then an exception will be thrown.
   * <p>
   * This method provides full control of the numeric formatting, including
   * zero-padding and the positive/negative sign.
   * <p>
   * The parser for a variable width value such as this normally behaves greedily,
   * accepting as many digits as possible.
   * This behavior can be affected by 'adjacent value parsing'.
   * See {@link #appendValue(java.time.temporal.TemporalField, int)} for full details.
   * <p>
   * In strict parsing mode, the minimum number of parsed digits is {@code minWidth}
   * and the maximum is {@code maxWidth}.
   * In lenient parsing mode, the minimum number of parsed digits is one
   * and the maximum is 19 (except as limited by adjacent value parsing).
   * <p>
   * If this method is invoked with equal minimum and maximum widths and a sign style of
   * {@code NOT_NEGATIVE} then it delegates to {@code appendValue(TemporalField,int)}.
   * In this scenario, the formatting and parsing behavior described there occur.
   *
   * @param field  the field to append, not null
   * @param minWidth  the minimum field width of the printed field, from 1 to 19
   * @param maxWidth  the maximum field width of the printed field, from 1 to 19
   * @param signStyle  the positive/negative output style, not null
   * @return this, for chaining, not null
   * @throws IllegalArgumentException if the widths are invalid
   */
  def appendValue(field: TemporalField, minWidth: Int, maxWidth: Int, signStyle: SignStyle): DateTimeFormatterBuilder = {
    if (minWidth == maxWidth && signStyle eq SignStyle.NOT_NEGATIVE) {
       appendValue(field, maxWidth)
    }


    if (minWidth < 1 || minWidth > 19) {
      throw new IllegalArgumentException("The minimum width must be from 1 to 19 inclusive but was " + minWidth)
    }
    if (maxWidth < 1 || maxWidth > 19) {
      throw new IllegalArgumentException("The maximum width must be from 1 to 19 inclusive but was " + maxWidth)
    }
    if (maxWidth < minWidth) {
      throw new IllegalArgumentException("The maximum width must exceed or equal the minimum width but " + maxWidth + " < " + minWidth)
    }
    val pp: DateTimeFormatterBuilder.NumberPrinterParser = new DateTimeFormatterBuilder.NumberPrinterParser(field, minWidth, maxWidth, signStyle)
    appendValue(pp)
     this
  }

  /**
   * Appends the reduced value of a date-time field with fixed width to the formatter.
   * <p>
   * This is typically used for formatting and parsing a two digit year.
   * The {@code width} is the printed and parsed width.
   * The {@code baseValue} is used during parsing to determine the valid range.
   * <p>
   * For formatting, the width is used to determine the number of characters to format.
   * The rightmost characters are output to match the width, left padding with zero.
   * <p>
   * For strict parsing, the number of characters allowed by the width are parsed.
   * For lenient parsing, the number of characters must be at least 1 and less than 10.
   * If the number of digits parsed is equal to {@code width} and the value is positive,
   * the value of the field is computed to be the first number greater than
   * or equal to the {@code baseValue} with the same least significant characters,
   * otherwise the value parsed is the field value.
   * This allows a reduced value to be entered for values in range of the baseValue
   * and width and absolute values can be entered for values outside the range.
   * <p>
   * For example, a base value of {@code 1980} and a width of {@code 2} will have
   * valid values from {@code 1980} to {@code 2079}.
   * During parsing, the text {@code "12"} will result in the value {@code 2012} as that
   * is the value within the range where the last two characters are "12".
   * Compare with lenient parsing the text {@code "1915"} that will result in the
   * value {@code 1915}.
   *
   * @param field  the field to append, not null
   * @param width  the field width of the printed and parsed field, from 1 to 10
   * @param baseValue  the base value of the range of valid values
   * @return this, for chaining, not null
   * @throws IllegalArgumentException if the width or base value is invalid
   * @see #appendValueReduced(java.time.temporal.TemporalField, int, int, int)
   */
  def appendValueReduced(field: TemporalField, width: Int, baseValue: Int): DateTimeFormatterBuilder = {
     appendValueReduced(field, width, width, baseValue)
  }

  /**
   * Appends the reduced value of a date-time field with a flexible width to the formatter.
   * <p>
   * This is typically used for formatting and parsing a two digit year
   * but allowing for the year value to be up to maxWidth.
   * <p>
   * For formatting, the {@code width} and {@code maxWidth} are used to
   * determine the number of characters to format.
   * If the value of the field is within the range of the {@code baseValue} using
   * {@code width} characters then the reduced value is formatted otherwise the value is
   * truncated to fit {@code maxWidth}.
   * The rightmost characters are output to match the width, left padding with zero.
   * <p>
   * For strict parsing, the number of characters allowed by {@code width} to {@code maxWidth} are parsed.
   * For lenient parsing, the number of characters must be at least 1 and less than 10.
   * If the number of digits parsed is equal to {@code width} and the value is positive,
   * the value of the field is computed to be the first number greater than
   * or equal to the {@code baseValue} with the same least significant characters,
   * otherwise the value parsed is the field value.
   * This allows a reduced value to be entered for values in range of the baseValue
   * and width and absolute values can be entered for values outside the range.
   * <p>
   * For example, a base value of {@code 1980} and a width of {@code 2} will have
   * valid values from {@code 1980} to {@code 2079}.
   * During parsing, the text {@code "12"} will result in the value {@code 2012} as that
   * is the value within the range where the last two characters are "12".
   * Compare with parsing the text {@code "1915"} that will result in the
   * value {@code 1915}.
   *
   * @param field  the field to append, not null
   * @param width  the field width of the printed and parsed field, from 1 to 10
   * @param maxWidth  the maximum field width of the printed field, from 1 to 10
   * @param baseValue  the base value of the range of valid values
   * @return this, for chaining, not null
   * @throws IllegalArgumentException if the width or base value is invalid
   */
  def appendValueReduced(field: TemporalField, width: Int, maxWidth: Int, baseValue: Int): DateTimeFormatterBuilder = {

    val pp: DateTimeFormatterBuilder.ReducedPrinterParser = new DateTimeFormatterBuilder.ReducedPrinterParser(field, width, maxWidth, baseValue)
    appendValue(pp)
     this
  }

  /**
   * Appends a fixed or variable width printer-parser handling adjacent value mode.
   * If a PrinterParser is not active then the new PrinterParser becomes
   * the active PrinterParser.
   * Otherwise, the active PrinterParser is modified depending on the new PrinterParser.
   * If the new PrinterParser is fixed width and has sign style {@code NOT_NEGATIVE}
   * then its width is added to the active PP and
   * the new PrinterParser is forced to be fixed width.
   * If the new PrinterParser is variable width, the active PrinterParser is changed
   * to be fixed width and the new PrinterParser becomes the active PP.
   *
   * @param pp  the printer-parser, not null
   * @return this, for chaining, not null
   */
  private def appendValue(pp: DateTimeFormatterBuilder.NumberPrinterParser): DateTimeFormatterBuilder = {
    if (active.valueParserIndex >= 0) {
      val activeValueParser: Int = active.valueParserIndex
      var basePP: DateTimeFormatterBuilder.NumberPrinterParser = active.printerParsers.get(activeValueParser).asInstanceOf[DateTimeFormatterBuilder.NumberPrinterParser]
      if (pp.minWidth == pp.maxWidth && pp.signStyle eq SignStyle.NOT_NEGATIVE) {
        basePP = basePP.withSubsequentWidth(pp.maxWidth)
        appendInternal(pp.withFixedWidth)
        active.valueParserIndex = activeValueParser
      }
      else {
        basePP = basePP.withFixedWidth
        active.valueParserIndex = appendInternal(pp)
      }
      active.printerParsers.set(activeValueParser, basePP)
    }
    else {
      active.valueParserIndex = appendInternal(pp)
    }
     this
  }

  /**
   * Appends the fractional value of a date-time field to the formatter.
   * <p>
   * The fractional value of the field will be output including the
   * preceding decimal point. The preceding value is not output.
   * For example, the second-of-minute value of 15 would be output as {@code .25}.
   * <p>
   * The width of the printed fraction can be controlled. Setting the
   * minimum width to zero will cause no output to be generated.
   * The printed fraction will have the minimum width necessary between
   * the minimum and maximum widths - trailing zeroes are omitted.
   * No rounding occurs due to the maximum width - digits are simply dropped.
   * <p>
   * When parsing in strict mode, the number of parsed digits must be between
   * the minimum and maximum width. When parsing in lenient mode, the minimum
   * width is considered to be zero and the maximum is nine.
   * <p>
   * If the value cannot be obtained then an exception will be thrown.
   * If the value is negative an exception will be thrown.
   * If the field does not have a fixed set of valid values then an
   * exception will be thrown.
   * If the field value in the date-time to be printed is invalid it
   * cannot be printed and an exception will be thrown.
   *
   * @param field  the field to append, not null
   * @param minWidth  the minimum width of the field excluding the decimal point, from 0 to 9
   * @param maxWidth  the maximum width of the field excluding the decimal point, from 1 to 9
   * @param decimalPoint  whether to output the localized decimal point symbol
   * @return this, for chaining, not null
   * @throws IllegalArgumentException if the field has a variable set of valid values or
   *                                  either width is invalid
   */
  def appendFraction(field: TemporalField, minWidth: Int, maxWidth: Int, decimalPoint: Boolean): DateTimeFormatterBuilder = {
    appendInternal(new DateTimeFormatterBuilder.FractionPrinterParser(field, minWidth, maxWidth, decimalPoint))
     this
  }

  /**
   * Appends the text of a date-time field to the formatter using the full
   * text style.
   * <p>
   * The text of the field will be output during a format.
   * The value must be within the valid range of the field.
   * If the value cannot be obtained then an exception will be thrown.
   * If the field has no textual representation, then the numeric value will be used.
   * <p>
   * The value will be printed as per the normal format of an integer value.
   * Only negative numbers will be signed. No padding will be added.
   *
   * @param field  the field to append, not null
   * @return this, for chaining, not null
   */
  def appendText(field: TemporalField): DateTimeFormatterBuilder = {
     appendText(field, TextStyle.FULL)
  }

  /**
   * Appends the text of a date-time field to the formatter.
   * <p>
   * The text of the field will be output during a format.
   * The value must be within the valid range of the field.
   * If the value cannot be obtained then an exception will be thrown.
   * If the field has no textual representation, then the numeric value will be used.
   * <p>
   * The value will be printed as per the normal format of an integer value.
   * Only negative numbers will be signed. No padding will be added.
   *
   * @param field  the field to append, not null
   * @param textStyle  the text style to use, not null
   * @return this, for chaining, not null
   */
  def appendText(field: TemporalField, textStyle: TextStyle): DateTimeFormatterBuilder = {


    appendInternal(new DateTimeFormatterBuilder.TextPrinterParser(field, textStyle, DateTimeTextProvider.getInstance))
     this
  }

  /**
   * Appends the text of a date-time field to the formatter using the specified
   * map to supply the text.
   * <p>
   * The standard text outputting methods use the localized text in the JDK.
   * This method allows that text to be specified directly.
   * The supplied map is not validated by the builder to ensure that formatting or
   * parsing is possible, thus an invalid map may throw an error during later use.
   * <p>
   * Supplying the map of text provides considerable flexibility in formatting and parsing.
   * For example, a legacy application might require or supply the months of the
   * year as "JNY", "FBY", "MCH" etc. These do not match the standard set of text
   * for localized month names. Using this method, a map can be created which
   * defines the connection between each value and the text:
   * {{{
   * Map&lt;Long, String&gt; map = new HashMap&lt;&gt;();
   * map.put(1, "JNY");
   * map.put(2, "FBY");
   * map.put(3, "MCH");
   * ...
   * builder.appendText(MONTH_OF_YEAR, map);
   * }}}
   * <p>
   * Other uses might be to output the value with a suffix, such as "1st", "2nd", "3rd",
   * or as Roman numerals "I", "II", "III", "IV".
   * <p>
   * During formatting, the value is obtained and checked that it is in the valid range.
   * If text is not available for the value then it is output as a number.
   * During parsing, the parser will match against the map of text and numeric values.
   *
   * @param field  the field to append, not null
   * @param textLookup  the map from the value to the text
   * @return this, for chaining, not null
   */
  def appendText(field: TemporalField, textLookup: Map[Long, String]): DateTimeFormatterBuilder = {


    val copy: Map[Long, String] = new Nothing(textLookup)
    val map: Map[TextStyle, Map[Long, String]] = Collections.singletonMap(TextStyle.FULL, copy)
    val store: Nothing = new Nothing(map)
    val provider: DateTimeTextProvider = new DateTimeTextProvider {
      override def getText(field: TemporalField, value: Long, style: TextStyle, locale: Locale): String = {
         store.getText(value, style)
      }

      override def getTextIterator(field: TemporalField, style: TextStyle, locale: Locale): Nothing = {
         store.getTextIterator(style)
      }
    }
    appendInternal(new DateTimeFormatterBuilder.TextPrinterParser(field, TextStyle.FULL, provider))
     this
  }

  /**
   * Appends an instant using ISO-8601 to the formatter, formatting fractional
   * digits in groups of three.
   * <p>
   * Instants have a fixed output format.
   * They are converted to a date-time with a zone-offset of UTC and formatted
   * using the standard ISO-8601 format.
   * With this method, formatting nano-of-second outputs zero, three, six
   * or nine digits digits as necessary.
   * The localized decimal style is not used.
   * <p>
   * The instant is obtained using {@link ChronoField#INSTANT_SECONDS INSTANT_SECONDS}
   * and optionally (@code NANO_OF_SECOND). The value of {@code INSTANT_SECONDS}
   * may be outside the maximum range of {@code DateTime}.
   * <p>
   * The {@linkplain ResolverStyle resolver style} has no effect on instant parsing.
   * The end-of-day time of '24:00' is handled as midnight at the start of the following day.
   * The leap-second time of '23:59:59' is handled to some degree, see
   * {@link DateTimeFormatter#parsedLeapSecond()} for full details.
   * <p>
   * An alternative to this method is to format/parse the instant as a single
   * epoch-seconds value. That is achieved using {@code appendValue(INSTANT_SECONDS)}.
   *
   * @return this, for chaining, not null
   */
  def appendInstant: DateTimeFormatterBuilder = {
    appendInternal(new DateTimeFormatterBuilder.InstantPrinterParser(-2))
     this
  }

  /**
   * Appends an instant using ISO-8601 to the formatter with control over
   * the number of fractional digits.
   * <p>
   * Instants have a fixed output format, although this method provides some
   * control over the fractional digits. They are converted to a date-time
   * with a zone-offset of UTC and printed using the standard ISO-8601 format.
   * The localized decimal style is not used.
   * <p>
   * The {@code fractionalDigits} parameter allows the output of the fractional
   * second to be controlled. Specifying zero will cause no fractional digits
   * to be output. From 1 to 9 will output an increasing number of digits, using
   * zero right-padding if necessary. The special value -1 is used to output as
   * many digits as necessary to avoid any trailing zeroes.
   * <p>
   * When parsing in strict mode, the number of parsed digits must match the
   * fractional digits. When parsing in lenient mode, any number of fractional
   * digits from zero to nine are accepted.
   * <p>
   * The instant is obtained using {@link ChronoField#INSTANT_SECONDS INSTANT_SECONDS}
   * and optionally (@code NANO_OF_SECOND). The value of {@code INSTANT_SECONDS}
   * may be outside the maximum range of {@code DateTime}.
   * <p>
   * The {@linkplain ResolverStyle resolver style} has no effect on instant parsing.
   * The end-of-day time of '24:00' is handled as midnight at the start of the following day.
   * The leap-second time of '23:59:59' is handled to some degree, see
   * {@link DateTimeFormatter#parsedLeapSecond()} for full details.
   * <p>
   * An alternative to this method is to format/parse the instant as a single
   * epoch-seconds value. That is achieved using {@code appendValue(INSTANT_SECONDS)}.
   *
   * @param fractionalDigits  the number of fractional second digits to format with,
   *                          from 0 to 9, or -1 to use as many digits as necessary
   * @return this, for chaining, not null
   */
  def appendInstant(fractionalDigits: Int): DateTimeFormatterBuilder = {
    if (fractionalDigits < -1 || fractionalDigits > 9) {
      throw new IllegalArgumentException("The fractional digits must be from -1 to 9 inclusive but was " + fractionalDigits)
    }
    appendInternal(new DateTimeFormatterBuilder.InstantPrinterParser(fractionalDigits))
     this
  }

  /**
   * Appends the zone offset, such as '+01:00', to the formatter.
   * <p>
   * This appends an instruction to format/parse the offset ID to the builder.
   * This is equivalent to calling {@code appendOffset("HH:MM:ss", "Z")}.
   *
   * @return this, for chaining, not null
   */
  def appendOffsetId: DateTimeFormatterBuilder = {
    appendInternal(OffsetIdPrinterParser.INSTANCE_ID_Z)
     this
  }

  /**
   * Appends the zone offset, such as '+01:00', to the formatter.
   * <p>
   * This appends an instruction to format/parse the offset ID to the builder.
   * <p>
   * During formatting, the offset is obtained using a mechanism equivalent
   * to querying the temporal with {@link TemporalQuery#offset()}.
   * It will be printed using the format defined below.
   * If the offset cannot be obtained then an exception is thrown unless the
   * section of the formatter is optional.
   * <p>
   * During parsing, the offset is parsed using the format defined below.
   * If the offset cannot be parsed then an exception is thrown unless the
   * section of the formatter is optional.
   * <p>
   * The format of the offset is controlled by a pattern which must be one
   * of the following:
   * <p><ul>
   * <li>{@code +HH} - hour only, ignoring minute and second
   * <li>{@code +HHmm} - hour, with minute if non-zero, ignoring second, no colon
   * <li>{@code +HH:mm} - hour, with minute if non-zero, ignoring second, with colon
   * <li>{@code +HHMM} - hour and minute, ignoring second, no colon
   * <li>{@code +HH:MM} - hour and minute, ignoring second, with colon
   * <li>{@code +HHMMss} - hour and minute, with second if non-zero, no colon
   * <li>{@code +HH:MM:ss} - hour and minute, with second if non-zero, with colon
   * <li>{@code +HHMMSS} - hour, minute and second, no colon
   * <li>{@code +HH:MM:SS} - hour, minute and second, with colon
   * </ul><p>
   * The "no offset" text controls what text is printed when the total amount of
   * the offset fields to be output is zero.
   * Example values would be 'Z', '+00:00', 'UTC' or 'GMT'.
   * Three formats are accepted for parsing UTC - the "no offset" text, and the
   * plus and minus versions of zero defined by the pattern.
   *
   * @param pattern  the pattern to use, not null
   * @param noOffsetText  the text to use when the offset is zero, not null
   * @return this, for chaining, not null
   */
  def appendOffset(pattern: String, noOffsetText: String): DateTimeFormatterBuilder = {
    appendInternal(new DateTimeFormatterBuilder.OffsetIdPrinterParser(pattern, noOffsetText))
     this
  }

  /**
   * Appends the localized zone offset, such as 'GMT+01:00', to the formatter.
   * <p>
   * This appends a localized zone offset to the builder, the format of the
   * localized offset is controlled by the specified {@link FormatStyle style}
   * to this method:
   * <p><ul>
   * <li>{@link TextStyle#FULL full} - formats with localized offset text, such
   * as 'GMT, 2-digit hour and minute field, optional second field if non-zero,
   * and colon.
   * <li>{@link TextStyle#SHORT short} - formats with localized offset text,
   * such as 'GMT, hour without leading zero, optional 2-digit minute and
   * second if non-zero, and colon.
   * </ul><p>
   * <p>
   * During formatting, the offset is obtained using a mechanism equivalent
   * to querying the temporal with {@link TemporalQuery#offset()}.
   * If the offset cannot be obtained then an exception is thrown unless the
   * section of the formatter is optional.
   * <p>
   * During parsing, the offset is parsed using the format defined above.
   * If the offset cannot be parsed then an exception is thrown unless the
   * section of the formatter is optional.
   * <p>
   * @param style  the format style to use, not null
   * @return this, for chaining, not null
   * @throws IllegalArgumentException if style is neither { @link TextStyle#FULL
   * full} nor { @link TextStyle#SHORT short}
   */
  def appendLocalizedOffset(style: TextStyle): DateTimeFormatterBuilder = {

    if (style ne TextStyle.FULL && style ne TextStyle.SHORT) {
      throw new IllegalArgumentException("Style must be either full or short")
    }
    appendInternal(new DateTimeFormatterBuilder.LocalizedOffsetIdPrinterParser(style))
     this
  }

  /**
   * Appends the time-zone ID, such as 'Europe/Paris' or '+02:00', to the formatter.
   * <p>
   * This appends an instruction to format/parse the zone ID to the builder.
   * The zone ID is obtained in a strict manner suitable for {@code ZonedDateTime}.
   * By contrast, {@code OffsetDateTime} does not have a zone ID suitable
   * for use with this method, see {@link #appendZoneOrOffsetId()}.
   * <p>
   * During formatting, the zone is obtained using a mechanism equivalent
   * to querying the temporal with {@link TemporalQuery#zoneId()}.
   * It will be printed using the result of {@link ZoneId#getId()}.
   * If the zone cannot be obtained then an exception is thrown unless the
   * section of the formatter is optional.
   * <p>
   * During parsing, the text must match a known zone or offset.
   * There are two types of zone ID, offset-based, such as '+01:30' and
   * region-based, such as 'Europe/London'. These are parsed differently.
   * If the parse starts with '+', '-', 'UT', 'UTC' or 'GMT', then the parser
   * expects an offset-based zone and will not match region-based zones.
   * The offset ID, such as '+02:30', may be at the start of the parse,
   * or prefixed by  'UT', 'UTC' or 'GMT'. The offset ID parsing is
   * equivalent to using {@link #appendOffset(String, String)} using the
   * arguments 'HH:MM:ss' and the no offset string '0'.
   * If the parse starts with 'UT', 'UTC' or 'GMT', and the parser cannot
   * match a following offset ID, then {@link ZoneOffset#UTC} is selected.
   * In all other cases, the list of known region-based zones is used to
   * find the longest available match. If no match is found, and the parse
   * starts with 'Z', then {@code ZoneOffset.UTC} is selected.
   * The parser uses the {@linkplain #parseCaseInsensitive() case sensitive} setting.
   * <p>
   * For example, the following will parse:
   * {{{
   * "Europe/London"           -- ZoneId.of("Europe/London")
   * "Z"                       -- ZoneOffset.UTC
   * "UT"                      -- ZoneId.of("UT")
   * "UTC"                     -- ZoneId.of("UTC")
   * "GMT"                     -- ZoneId.of("GMT")
   * "+01:30"                  -- ZoneOffset.of("+01:30")
   * "UT+01:30"                -- ZoneOffset.of("+01:30")
   * "UTC+01:30"               -- ZoneOffset.of("+01:30")
   * "GMT+01:30"               -- ZoneOffset.of("+01:30")
   * }}}
   *
   * @return this, for chaining, not null
   * @see #appendZoneRegionId()
   */
  def appendZoneId: DateTimeFormatterBuilder = {
    appendInternal(new DateTimeFormatterBuilder.ZoneIdPrinterParser(TemporalQuery.zoneId, "ZoneId()"))
     this
  }

  /**
   * Appends the time-zone region ID, such as 'Europe/Paris', to the formatter,
   * rejecting the zone ID if it is a {@code ZoneOffset}.
   * <p>
   * This appends an instruction to format/parse the zone ID to the builder
   * only if it is a region-based ID.
   * <p>
   * During formatting, the zone is obtained using a mechanism equivalent
   * to querying the temporal with {@link TemporalQuery#zoneId()}.
   * If the zone is a {@code ZoneOffset} or it cannot be obtained then
   * an exception is thrown unless the section of the formatter is optional.
   * If the zone is not an offset, then the zone will be printed using
   * the zone ID from {@link ZoneId#getId()}.
   * <p>
   * During parsing, the text must match a known zone or offset.
   * There are two types of zone ID, offset-based, such as '+01:30' and
   * region-based, such as 'Europe/London'. These are parsed differently.
   * If the parse starts with '+', '-', 'UT', 'UTC' or 'GMT', then the parser
   * expects an offset-based zone and will not match region-based zones.
   * The offset ID, such as '+02:30', may be at the start of the parse,
   * or prefixed by  'UT', 'UTC' or 'GMT'. The offset ID parsing is
   * equivalent to using {@link #appendOffset(String, String)} using the
   * arguments 'HH:MM:ss' and the no offset string '0'.
   * If the parse starts with 'UT', 'UTC' or 'GMT', and the parser cannot
   * match a following offset ID, then {@link ZoneOffset#UTC} is selected.
   * In all other cases, the list of known region-based zones is used to
   * find the longest available match. If no match is found, and the parse
   * starts with 'Z', then {@code ZoneOffset.UTC} is selected.
   * The parser uses the {@linkplain #parseCaseInsensitive() case sensitive} setting.
   * <p>
   * For example, the following will parse:
   * {{{
   * "Europe/London"           -- ZoneId.of("Europe/London")
   * "Z"                       -- ZoneOffset.UTC
   * "UT"                      -- ZoneId.of("UT")
   * "UTC"                     -- ZoneId.of("UTC")
   * "GMT"                     -- ZoneId.of("GMT")
   * "+01:30"                  -- ZoneOffset.of("+01:30")
   * "UT+01:30"                -- ZoneOffset.of("+01:30")
   * "UTC+01:30"               -- ZoneOffset.of("+01:30")
   * "GMT+01:30"               -- ZoneOffset.of("+01:30")
   * }}}
   * <p>
   * Note that this method is is identical to {@code appendZoneId()} except
   * in the mechanism used to obtain the zone.
   * Note also that parsing accepts offsets, whereas formatting will never
   * produce one.
   *
   * @return this, for chaining, not null
   * @see #appendZoneId()
   */
  def appendZoneRegionId: DateTimeFormatterBuilder = {
    appendInternal(new DateTimeFormatterBuilder.ZoneIdPrinterParser(QUERY_REGION_ONLY, "ZoneRegionId()"))
     this
  }

  /**
   * Appends the time-zone ID, such as 'Europe/Paris' or '+02:00', to
   * the formatter, using the best available zone ID.
   * <p>
   * This appends an instruction to format/parse the best available
   * zone or offset ID to the builder.
   * The zone ID is obtained in a lenient manner that first attempts to
   * find a true zone ID, such as that on {@code ZonedDateTime}, and
   * then attempts to find an offset, such as that on {@code OffsetDateTime}.
   * <p>
   * During formatting, the zone is obtained using a mechanism equivalent
   * to querying the temporal with {@link TemporalQuery#zone()}.
   * It will be printed using the result of {@link ZoneId#getId()}.
   * If the zone cannot be obtained then an exception is thrown unless the
   * section of the formatter is optional.
   * <p>
   * During parsing, the text must match a known zone or offset.
   * There are two types of zone ID, offset-based, such as '+01:30' and
   * region-based, such as 'Europe/London'. These are parsed differently.
   * If the parse starts with '+', '-', 'UT', 'UTC' or 'GMT', then the parser
   * expects an offset-based zone and will not match region-based zones.
   * The offset ID, such as '+02:30', may be at the start of the parse,
   * or prefixed by  'UT', 'UTC' or 'GMT'. The offset ID parsing is
   * equivalent to using {@link #appendOffset(String, String)} using the
   * arguments 'HH:MM:ss' and the no offset string '0'.
   * If the parse starts with 'UT', 'UTC' or 'GMT', and the parser cannot
   * match a following offset ID, then {@link ZoneOffset#UTC} is selected.
   * In all other cases, the list of known region-based zones is used to
   * find the longest available match. If no match is found, and the parse
   * starts with 'Z', then {@code ZoneOffset.UTC} is selected.
   * The parser uses the {@linkplain #parseCaseInsensitive() case sensitive} setting.
   * <p>
   * For example, the following will parse:
   * {{{
   * "Europe/London"           -- ZoneId.of("Europe/London")
   * "Z"                       -- ZoneOffset.UTC
   * "UT"                      -- ZoneId.of("UT")
   * "UTC"                     -- ZoneId.of("UTC")
   * "GMT"                     -- ZoneId.of("GMT")
   * "+01:30"                  -- ZoneOffset.of("+01:30")
   * "UT+01:30"                -- ZoneOffset.of("UT+01:30")
   * "UTC+01:30"               -- ZoneOffset.of("UTC+01:30")
   * "GMT+01:30"               -- ZoneOffset.of("GMT+01:30")
   * }}}
   * <p>
   * Note that this method is is identical to {@code appendZoneId()} except
   * in the mechanism used to obtain the zone.
   *
   * @return this, for chaining, not null
   * @see #appendZoneId()
   */
  def appendZoneOrOffsetId: DateTimeFormatterBuilder = {
    appendInternal(new DateTimeFormatterBuilder.ZoneIdPrinterParser(TemporalQuery.zone, "ZoneOrOffsetId()"))
     this
  }

  /**
   * Appends the time-zone name, such as 'British Summer Time', to the formatter.
   * <p>
   * This appends an instruction to format/parse the textual name of the zone to
   * the builder.
   * <p>
   * During formatting, the zone is obtained using a mechanism equivalent
   * to querying the temporal with {@link TemporalQuery#zoneId()}.
   * If the zone is a {@code ZoneOffset} it will be printed using the
   * result of {@link ZoneOffset#getId()}.
   * If the zone is not an offset, the textual name will be looked up
   * for the locale set in the {@link DateTimeFormatter}.
   * If the temporal object being printed represents an instant, then the text
   * will be the summer or winter time text as appropriate.
   * If the lookup for text does not find any suitable reuslt, then the
   * {@link ZoneId#getId() ID} will be printed instead.
   * If the zone cannot be obtained then an exception is thrown unless the
   * section of the formatter is optional.
   * <p>
   * During parsing, either the textual zone name, the zone ID or the offset
   * is accepted. Many textual zone names are not unique, such as CST can be
   * for both "Central Standard Time" and "China Standard Time". In this
   * situation, the zone id will be determined by the region information from
   * formatter's  {@link DateTimeFormatter#getLocale() locale} and the standard
   * zone id for that area, for example, America/New_York for the America Eastern
   * zone. The {@link #appendZoneText(TextStyle, Set)} may be used
   * to specify a set of preferred {@link ZoneId} in this situation.
   *
   * @param textStyle  the text style to use, not null
   * @return this, for chaining, not null
   */
  def appendZoneText(textStyle: TextStyle): DateTimeFormatterBuilder = {
    appendInternal(new DateTimeFormatterBuilder.ZoneTextPrinterParser(textStyle, null))
     this
  }

  /**
   * Appends the time-zone name, such as 'British Summer Time', to the formatter.
   * <p>
   * This appends an instruction to format/parse the textual name of the zone to
   * the builder.
   * <p>
   * During formatting, the zone is obtained using a mechanism equivalent
   * to querying the temporal with {@link TemporalQuery#zoneId()}.
   * If the zone is a {@code ZoneOffset} it will be printed using the
   * result of {@link ZoneOffset#getId()}.
   * If the zone is not an offset, the textual name will be looked up
   * for the locale set in the {@link DateTimeFormatter}.
   * If the temporal object being printed represents an instant, then the text
   * will be the summer or winter time text as appropriate.
   * If the lookup for text does not find any suitable reuslt, then the
   * {@link ZoneId#getId() ID} will be printed instead.
   * If the zone cannot be obtained then an exception is thrown unless the
   * section of the formatter is optional.
   * <p>
   * During parsing, either the textual zone name, the zone ID or the offset
   * is accepted. Many textual zone names are not unique, such as CST can be
   * for both "Central Standard Time" and "China Standard Time". In this
   * situation, the zone id will be determined by the region information from
   * formatter's  {@link DateTimeFormatter#getLocale() locale} and the standard
   * zone id for that area, for example, America/New_York for the America Eastern
   * zone. This method also allows a set of preferred {@link ZoneId} to be
   * specified for parsing. The matched preferred zone id will be used if the
   * textural zone name being parsed is not unique.
   *
   * If the zone cannot be parsed then an exception is thrown unless the
   * section of the formatter is optional.
   *
   * @param textStyle  the text style to use, not null
   * @param preferredZones  the set of preferred zone ids, not null
   * @return this, for chaining, not null
   */
  def appendZoneText(textStyle: TextStyle, preferredZones: Set[ZoneId]): DateTimeFormatterBuilder = {

    appendInternal(new DateTimeFormatterBuilder.ZoneTextPrinterParser(textStyle, preferredZones))
     this
  }

  /**
   * Appends the chronology ID, such as 'ISO' or 'ThaiBuddhist', to the formatter.
   * <p>
   * This appends an instruction to format/parse the chronology ID to the builder.
   * <p>
   * During formatting, the chronology is obtained using a mechanism equivalent
   * to querying the temporal with {@link TemporalQuery#chronology()}.
   * It will be printed using the result of {@link Chronology#getId()}.
   * If the chronology cannot be obtained then an exception is thrown unless the
   * section of the formatter is optional.
   * <p>
   * During parsing, the chronology is parsed and must match one of the chronologies
   * in {@link Chronology#getAvailableChronologies()}.
   * If the chronology cannot be parsed then an exception is thrown unless the
   * section of the formatter is optional.
   * The parser uses the {@linkplain #parseCaseInsensitive() case sensitive} setting.
   *
   * @return this, for chaining, not null
   */
  def appendChronologyId: DateTimeFormatterBuilder = {
    appendInternal(new DateTimeFormatterBuilder.ChronoPrinterParser(null))
     this
  }

  /**
   * Appends the chronology name to the formatter.
   * <p>
   * The calendar system name will be output during a format.
   * If the chronology cannot be obtained then an exception will be thrown.
   * The calendar system name is obtained from the Chronology.
   *
   * @param textStyle  the text style to use, not null
   * @return this, for chaining, not null
   */
  def appendChronologyText(textStyle: TextStyle): DateTimeFormatterBuilder = {

    appendInternal(new DateTimeFormatterBuilder.ChronoPrinterParser(textStyle))
     this
  }

  /**
   * Appends a localized date-time pattern to the formatter.
   * <p>
   * This appends a localized section to the builder, suitable for outputting
   * a date, time or date-time combination. The format of the localized
   * section is lazily looked up based on four items:
   * <p><ul>
   * <li>the {@code dateStyle} specified to this method
   * <li>the {@code timeStyle} specified to this method
   * <li>the {@code Locale} of the {@code DateTimeFormatter}
   * <li>the {@code Chronology}, selecting the best available
   * </ul><p>
   * During formatting, the chronology is obtained from the temporal object
   * being formatted, which may have been overridden by
   * {@link DateTimeFormatter#withChronology(Chronology)}.
   * <p>
   * During parsing, if a chronology has already been parsed, then it is used.
   * Otherwise the default from {@code DateTimeFormatter.withChronology(Chronology)}
   * is used, with {@code IsoChronology} as the fallback.
   * <p>
   * Note that this method provides similar functionality to methods on
   * {@code DateFormat} such as {@link java.text.DateFormat#getDateTimeInstance(int, int)}.
   *
   * @param dateStyle  the date style to use, null means no date required
   * @param timeStyle  the time style to use, null means no time required
   * @return this, for chaining, not null
   * @throws IllegalArgumentException if both the date and time styles are null
   */
  def appendLocalized(dateStyle: FormatStyle, timeStyle: FormatStyle): DateTimeFormatterBuilder = {
    if (dateStyle == null && timeStyle == null) {
      throw new IllegalArgumentException("Either the date or time style must be non-null")
    }
    appendInternal(new DateTimeFormatterBuilder.LocalizedPrinterParser(dateStyle, timeStyle))
     this
  }

  /**
   * Appends a character literal to the formatter.
   * <p>
   * This character will be output during a format.
   *
   * @param literal  the literal to append, not null
   * @return this, for chaining, not null
   */
  def appendLiteral(literal: Char): DateTimeFormatterBuilder = {
    appendInternal(new DateTimeFormatterBuilder.CharLiteralPrinterParser(literal))
     this
  }

  /**
   * Appends a string literal to the formatter.
   * <p>
   * This string will be output during a format.
   * <p>
   * If the literal is empty, nothing is added to the formatter.
   *
   * @param literal  the literal to append, not null
   * @return this, for chaining, not null
   */
  def appendLiteral(literal: String): DateTimeFormatterBuilder = {

    if (literal.length > 0) {
      if (literal.length == 1) {
        appendInternal(new DateTimeFormatterBuilder.CharLiteralPrinterParser(literal.charAt(0)))
      }
      else {
        appendInternal(new DateTimeFormatterBuilder.StringLiteralPrinterParser(literal))
      }
    }
     this
  }

  /**
   * Appends all the elements of a formatter to the builder.
   * <p>
   * This method has the same effect as appending each of the constituent
   * parts of the formatter directly to this builder.
   *
   * @param formatter  the formatter to add, not null
   * @return this, for chaining, not null
   */
  def append(formatter: DateTimeFormatter): DateTimeFormatterBuilder = {

    appendInternal(formatter.toPrinterParser(false))
     this
  }

  /**
   * Appends a formatter to the builder which will optionally format/parse.
   * <p>
   * This method has the same effect as appending each of the constituent
   * parts directly to this builder surrounded by an {@link #optionalStart()} and
   * {@link #optionalEnd()}.
   * <p>
   * The formatter will format if data is available for all the fields contained within it.
   * The formatter will parse if the string matches, otherwise no error is returned.
   *
   * @param formatter  the formatter to add, not null
   * @return this, for chaining, not null
   */
  def appendOptional(formatter: DateTimeFormatter): DateTimeFormatterBuilder = {

    appendInternal(formatter.toPrinterParser(true))
     this
  }

  /**
   * Appends the elements defined by the specified pattern to the builder.
   * <p>
   * All letters 'A' to 'Z' and 'a' to 'z' are reserved as pattern letters.
   * The characters '#', '{' and '}' are reserved for future use.
   * The characters '[' and ']' indicate optional patterns.
   * The following pattern letters are defined:
   * {{{
   * Symbol  Meaning                     Presentation      Examples
   * ------  -------                     ------------      -------
   * G       era                         text              AD; Anno Domini; A
   * u       year                        year              2004; 04
   * y       year-of-era                 year              2004; 04
   * D       day-of-year                 number            189
   * M/L     month-of-year               number/text       7; 07; Jul; July; J
   * d       day-of-month                number            10
   *
   * Q/q     quarter-of-year             number/text       3; 03; Q3; 3rd quarter
   * Y       week-based-year             year              1996; 96
   * w       week-of-week-based-year     number            27
   * W       week-of-month               number            4
   * E       day-of-week                 text              Tue; Tuesday; T
   * e/c     localized day-of-week       number/text       2; 02; Tue; Tuesday; T
   * F       week-of-month               number            3
   *
   * a       am-pm-of-day                text              PM
   * h       clock-hour-of-am-pm (1-12)  number            12
   * K       hour-of-am-pm (0-11)        number            0
   * k       clock-hour-of-am-pm (1-24)  number            0
   *
   * H       hour-of-day (0-23)          number            0
   * m       minute-of-hour              number            30
   * s       second-of-minute            number            55
   * S       fraction-of-second          fraction          978
   * A       milli-of-day                number            1234
   * n       nano-of-second              number            987654321
   * N       nano-of-day                 number            1234000000
   *
   * V       time-zone ID                zone-id           America/Los_Angeles; Z; -08:30
   * z       time-zone name              zone-name         Pacific Standard Time; PST
   * O       localized zone-offset       offset-O          GMT+8; GMT+08:00; UTC-08:00;
   * X       zone-offset 'Z' for zero    offset-X          Z; -08; -0830; -08:30; -083015; -08:30:15;
   * x       zone-offset                 offset-x          +0000; -08; -0830; -08:30; -083015; -08:30:15;
   * Z       zone-offset                 offset-Z          +0000; -0800; -08:00;
   *
   * p       pad next                    pad modifier      1
   *
   * '       escape for text             delimiter
   * ''      single quote                literal           '
   * [       optional section start
   * ]       optional section end
   * #       reserved for future use
   * {       reserved for future use
   * }       reserved for future use
   * }}}
   * <p>
   * The count of pattern letters determine the format.
   * See <a href="DateTimeFormatter.html#patterns">DateTimeFormatter</a> for a user-focused description of the patterns.
   * The following tables define how the pattern letters map to the builder.
   * <p>
   * <b>Date fields</b>: Pattern letters to output a date.
   * {{{
   * Pattern  Count  Equivalent builder methods
   * -------  -----  --------------------------
   * G       1      appendText(ChronoField.ERA, TextStyle.SHORT)
   * GG      2      appendText(ChronoField.ERA, TextStyle.SHORT)
   * GGG     3      appendText(ChronoField.ERA, TextStyle.SHORT)
   * GGGG    4      appendText(ChronoField.ERA, TextStyle.FULL)
   * GGGGG   5      appendText(ChronoField.ERA, TextStyle.NARROW)
   *
   * u       1      appendValue(ChronoField.YEAR, 1, 19, SignStyle.NORMAL);
   * uu      2      appendValueReduced(ChronoField.YEAR, 2, 2000);
   * uuu     3      appendValue(ChronoField.YEAR, 3, 19, SignStyle.NORMAL);
   * u..u    4..n   appendValue(ChronoField.YEAR, n, 19, SignStyle.EXCEEDS_PAD);
   * y       1      appendValue(ChronoField.YEAR_OF_ERA, 1, 19, SignStyle.NORMAL);
   * yy      2      appendValueReduced(ChronoField.YEAR_OF_ERA, 2, 2000);
   * yyy     3      appendValue(ChronoField.YEAR_OF_ERA, 3, 19, SignStyle.NORMAL);
   * y..y    4..n   appendValue(ChronoField.YEAR_OF_ERA, n, 19, SignStyle.EXCEEDS_PAD);
   * Y       1      append special localized WeekFields element for numeric week-based-year
   * YY      2      append special localized WeekFields element for reduced numeric week-based-year 2 digits;
   * YYY     3      append special localized WeekFields element for numeric week-based-year (3, 19, SignStyle.NORMAL);
   * Y..Y    4..n   append special localized WeekFields element for numeric week-based-year (n, 19, SignStyle.EXCEEDS_PAD);
   *
   * Q       1      appendValue(IsoFields.QUARTER_OF_YEAR);
   * QQ      2      appendValue(IsoFields.QUARTER_OF_YEAR, 2);
   * QQQ     3      appendText(IsoFields.QUARTER_OF_YEAR, TextStyle.SHORT)
   * QQQQ    4      appendText(IsoFields.QUARTER_OF_YEAR, TextStyle.FULL)
   * QQQQQ   5      appendText(IsoFields.QUARTER_OF_YEAR, TextStyle.NARROW)
   * q       1      appendValue(IsoFields.QUARTER_OF_YEAR);
   * qq      2      appendValue(IsoFields.QUARTER_OF_YEAR, 2);
   * qqq     3      appendText(IsoFields.QUARTER_OF_YEAR, TextStyle.SHORT_STANDALONE)
   * qqqq    4      appendText(IsoFields.QUARTER_OF_YEAR, TextStyle.FULL_STANDALONE)
   * qqqqq   5      appendText(IsoFields.QUARTER_OF_YEAR, TextStyle.NARROW_STANDALONE)
   *
   * M       1      appendValue(ChronoField.MONTH_OF_YEAR);
   * MM      2      appendValue(ChronoField.MONTH_OF_YEAR, 2);
   * MMM     3      appendText(ChronoField.MONTH_OF_YEAR, TextStyle.SHORT)
   * MMMM    4      appendText(ChronoField.MONTH_OF_YEAR, TextStyle.FULL)
   * MMMMM   5      appendText(ChronoField.MONTH_OF_YEAR, TextStyle.NARROW)
   * L       1      appendValue(ChronoField.MONTH_OF_YEAR);
   * LL      2      appendValue(ChronoField.MONTH_OF_YEAR, 2);
   * LLL     3      appendText(ChronoField.MONTH_OF_YEAR, TextStyle.SHORT_STANDALONE)
   * LLLL    4      appendText(ChronoField.MONTH_OF_YEAR, TextStyle.FULL_STANDALONE)
   * LLLLL   5      appendText(ChronoField.MONTH_OF_YEAR, TextStyle.NARROW_STANDALONE)
   *
   * w       1      append special localized WeekFields element for numeric week-of-year
   * ww      1      append special localized WeekFields element for numeric week-of-year, zero-padded
   * W       1      append special localized WeekFields element for numeric week-of-month
   * d       1      appendValue(ChronoField.DAY_OF_MONTH)
   * dd      2      appendValue(ChronoField.DAY_OF_MONTH, 2)
   * D       1      appendValue(ChronoField.DAY_OF_YEAR)
   * DD      2      appendValue(ChronoField.DAY_OF_YEAR, 2)
   * DDD     3      appendValue(ChronoField.DAY_OF_YEAR, 3)
   * F       1      appendValue(ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH)
   * E       1      appendText(ChronoField.DAY_OF_WEEK, TextStyle.SHORT)
   * EE      2      appendText(ChronoField.DAY_OF_WEEK, TextStyle.SHORT)
   * EEE     3      appendText(ChronoField.DAY_OF_WEEK, TextStyle.SHORT)
   * EEEE    4      appendText(ChronoField.DAY_OF_WEEK, TextStyle.FULL)
   * EEEEE   5      appendText(ChronoField.DAY_OF_WEEK, TextStyle.NARROW)
   * e       1      append special localized WeekFields element for numeric day-of-week
   * ee      2      append special localized WeekFields element for numeric day-of-week, zero-padded
   * eee     3      appendText(ChronoField.DAY_OF_WEEK, TextStyle.SHORT)
   * eeee    4      appendText(ChronoField.DAY_OF_WEEK, TextStyle.FULL)
   * eeeee   5      appendText(ChronoField.DAY_OF_WEEK, TextStyle.NARROW)
   * c       1      append special localized WeekFields element for numeric day-of-week
   * ccc     3      appendText(ChronoField.DAY_OF_WEEK, TextStyle.SHORT_STANDALONE)
   * cccc    4      appendText(ChronoField.DAY_OF_WEEK, TextStyle.FULL_STANDALONE)
   * ccccc   5      appendText(ChronoField.DAY_OF_WEEK, TextStyle.NARROW_STANDALONE)
   * }}}
   * <p>
   * <b>Time fields</b>: Pattern letters to output a time.
   * {{{
   * Pattern  Count  Equivalent builder methods
   * -------  -----  --------------------------
   * a       1      appendText(ChronoField.AMPM_OF_DAY, TextStyle.SHORT)
   * h       1      appendValue(ChronoField.CLOCK_HOUR_OF_AMPM)
   * hh      2      appendValue(ChronoField.CLOCK_HOUR_OF_AMPM, 2)
   * H       1      appendValue(ChronoField.HOUR_OF_DAY)
   * HH      2      appendValue(ChronoField.HOUR_OF_DAY, 2)
   * k       1      appendValue(ChronoField.CLOCK_HOUR_OF_DAY)
   * kk      2      appendValue(ChronoField.CLOCK_HOUR_OF_DAY, 2)
   * K       1      appendValue(ChronoField.HOUR_OF_AMPM)
   * KK      2      appendValue(ChronoField.HOUR_OF_AMPM, 2)
   * m       1      appendValue(ChronoField.MINUTE_OF_HOUR)
   * mm      2      appendValue(ChronoField.MINUTE_OF_HOUR, 2)
   * s       1      appendValue(ChronoField.SECOND_OF_MINUTE)
   * ss      2      appendValue(ChronoField.SECOND_OF_MINUTE, 2)
   *
   * S..S    1..n   appendFraction(ChronoField.NANO_OF_SECOND, n, n, false)
   * A       1      appendValue(ChronoField.MILLI_OF_DAY)
   * A..A    2..n   appendValue(ChronoField.MILLI_OF_DAY, n)
   * n       1      appendValue(ChronoField.NANO_OF_SECOND)
   * n..n    2..n   appendValue(ChronoField.NANO_OF_SECOND, n)
   * N       1      appendValue(ChronoField.NANO_OF_DAY)
   * N..N    2..n   appendValue(ChronoField.NANO_OF_DAY, n)
   * }}}
   * <p>
   * <b>Zone ID</b>: Pattern letters to output {@code ZoneId}.
   * {{{
   * Pattern  Count  Equivalent builder methods
   * -------  -----  --------------------------
   * VV      2      appendZoneId()
   * z       1      appendZoneText(TextStyle.SHORT)
   * zz      2      appendZoneText(TextStyle.SHORT)
   * zzz     3      appendZoneText(TextStyle.SHORT)
   * zzzz    4      appendZoneText(TextStyle.FULL)
   * }}}
   * <p>
   * <b>Zone offset</b>: Pattern letters to output {@code ZoneOffset}.
   * {{{
   * Pattern  Count  Equivalent builder methods
   * -------  -----  --------------------------
   * O       1      appendLocalizedOffsetPrefixed(TextStyle.SHORT);
   * OOOO    4      appendLocalizedOffsetPrefixed(TextStyle.FULL);
   * X       1      appendOffset("+HHmm","Z")
   * XX      2      appendOffset("+HHMM","Z")
   * XXX     3      appendOffset("+HH:MM","Z")
   * XXXX    4      appendOffset("+HHMMss","Z")
   * XXXXX   5      appendOffset("+HH:MM:ss","Z")
   * x       1      appendOffset("+HHmm","+00")
   * xx      2      appendOffset("+HHMM","+0000")
   * xxx     3      appendOffset("+HH:MM","+00:00")
   * xxxx    4      appendOffset("+HHMMss","+0000")
   * xxxxx   5      appendOffset("+HH:MM:ss","+00:00")
   * Z       1      appendOffset("+HHMM","+0000")
   * ZZ      2      appendOffset("+HHMM","+0000")
   * ZZZ     3      appendOffset("+HHMM","+0000")
   * ZZZZ    4      appendLocalizedOffset(TextStyle.FULL);
   * ZZZZZ   5      appendOffset("+HH:MM:ss","Z")
   * }}}
   * <p>
   * <b>Modifiers</b>: Pattern letters that modify the rest of the pattern:
   * {{{
   * Pattern  Count  Equivalent builder methods
   * -------  -----  --------------------------
   * [       1      optionalStart()
   * ]       1      optionalEnd()
   * p..p    1..n   padNext(n)
   * }}}
   * <p>
   * Any sequence of letters not specified above, unrecognized letter or
   * reserved character will throw an exception.
   * Future versions may add to the set of patterns.
   * It is recommended to use single quotes around all characters that you want
   * to output directly to ensure that future changes do not break your application.
   * <p>
   * Note that the pattern string is similar, but not identical, to
   * {@link java.text.SimpleDateFormat SimpleDateFormat}.
   * The pattern string is also similar, but not identical, to that defined by the
   * Unicode Common Locale Data Repository (CLDR/LDML).
   * Pattern letters 'X' and 'u' are aligned with Unicode CLDR/LDML.
   * By contrast, {@code SimpleDateFormat} uses 'u' for the numeric day of week.
   * Pattern letters 'y' and 'Y' parse years of two digits and more than 4 digits differently.
   * Pattern letters 'n', 'A', 'N', and 'p' are added.
   * Number types will reject large numbers.
   *
   * @param pattern  the pattern to add, not null
   * @return this, for chaining, not null
   * @throws IllegalArgumentException if the pattern is invalid
   */
  def appendPattern(pattern: String): DateTimeFormatterBuilder = {

    parsePattern(pattern)
     this
  }

  private def parsePattern(pattern: String) {
    {
      var pos: Int = 0
      while (pos < pattern.length) {
        {
          var cur: Char = pattern.charAt(pos)
          if ((cur >= 'A' && cur <= 'Z') || (cur >= 'a' && cur <= 'z')) {
            var start: Int = ({
              pos += 1; pos - 1
            })
            while (pos < pattern.length && pattern.charAt(pos) == cur) {
              ({
                pos += 1; pos - 1
              })
            }
            var count: Int = pos - start
            if (cur == 'p') {
              var pad: Int = 0
              if (pos < pattern.length) {
                cur = pattern.charAt(pos)
                if ((cur >= 'A' && cur <= 'Z') || (cur >= 'a' && cur <= 'z')) {
                  pad = count
                  start = ({
                    pos += 1; pos - 1
                  })
                  while (pos < pattern.length && pattern.charAt(pos) == cur) {
                    ({
                      pos += 1; pos - 1
                    })
                  }
                  count = pos - start
                }
              }
              if (pad == 0) {
                throw new IllegalArgumentException("Pad letter 'p' must be followed by valid pad pattern: " + pattern)
              }
              padNext(pad)
            }
            val field: TemporalField = FIELD_MAP.get(cur)
            if (field != null) {
              parseField(cur, count, field)
            }
            else if (cur == 'z') {
              if (count > 4) {
                throw new IllegalArgumentException("Too many pattern letters: " + cur)
              }
              else if (count == 4) {
                appendZoneText(TextStyle.FULL)
              }
              else {
                appendZoneText(TextStyle.SHORT)
              }
            }
            else if (cur == 'V') {
              if (count != 2) {
                throw new IllegalArgumentException("Pattern letter count must be 2: " + cur)
              }
              appendZoneId
            }
            else if (cur == 'Z') {
              if (count < 4) {
                appendOffset("+HHMM", "+0000")
              }
              else if (count == 4) {
                appendLocalizedOffset(TextStyle.FULL)
              }
              else if (count == 5) {
                appendOffset("+HH:MM:ss", "Z")
              }
              else {
                throw new IllegalArgumentException("Too many pattern letters: " + cur)
              }
            }
            else if (cur == 'O') {
              if (count == 1) {
                appendLocalizedOffset(TextStyle.SHORT)
              }
              else if (count == 4) {
                appendLocalizedOffset(TextStyle.FULL)
              }
              else {
                throw new IllegalArgumentException("Pattern letter count must be 1 or 4: " + cur)
              }
            }
            else if (cur == 'X') {
              if (count > 5) {
                throw new IllegalArgumentException("Too many pattern letters: " + cur)
              }
              appendOffset(OffsetIdPrinterParser.PATTERNS(count + (if (count == 1) 0 else 1)), "Z")
            }
            else if (cur == 'x') {
              if (count > 5) {
                throw new IllegalArgumentException("Too many pattern letters: " + cur)
              }
              val zero: String = (if (count == 1) "+00" else (if (count % 2 == 0) "+0000" else "+00:00"))
              appendOffset(OffsetIdPrinterParser.PATTERNS(count + (if (count == 1) 0 else 1)), zero)
            }
            else if (cur == 'W') {
              if (count > 1) {
                throw new IllegalArgumentException("Too many pattern letters: " + cur)
              }
              appendInternal(new DateTimeFormatterBuilder.WeekBasedFieldPrinterParser(cur, count))
            }
            else if (cur == 'w') {
              if (count > 2) {
                throw new IllegalArgumentException("Too many pattern letters: " + cur)
              }
              appendInternal(new DateTimeFormatterBuilder.WeekBasedFieldPrinterParser(cur, count))
            }
            else if (cur == 'Y') {
              appendInternal(new DateTimeFormatterBuilder.WeekBasedFieldPrinterParser(cur, count))
            }
            else {
              throw new IllegalArgumentException("Unknown pattern letter: " + cur)
            }
            pos -= 1
          }
          else if (cur == '\'') {
            val start: Int = ({
              pos += 1; pos - 1
            })
            while (pos < pattern.length) {
              {
                if (pattern.charAt(pos) == '\'') {
                  if (pos + 1 < pattern.length && pattern.charAt(pos + 1) == '\'') {
                    pos += 1
                  }
                  else {
                    break //todo: break is not supported
                  }
                }
              }
              ({
                pos += 1; pos - 1
              })
            }
            if (pos >= pattern.length) {
              throw new IllegalArgumentException("Pattern ends with an incomplete string literal: " + pattern)
            }
            val str: String = pattern.substring(start + 1, pos)
            if (str.length == 0) {
              appendLiteral('\'')
            }
            else {
              appendLiteral(str.replace("''", "'"))
            }
          }
          else if (cur == '[') {
            optionalStart
          }
          else if (cur == ']') {
            if (active.parent == null) {
              throw new IllegalArgumentException("Pattern invalid as it contains ] without previous [")
            }
            optionalEnd
          }
          else if (cur == '{' || cur == '}' || cur == '#') {
            throw new IllegalArgumentException("Pattern includes reserved character: '" + cur + "'")
          }
          else {
            appendLiteral(cur)
          }
        }
        ({
          pos += 1; pos - 1
        })
      }
    }
  }

  @SuppressWarnings(Array("fallthrough")) private def parseField(cur: Char, count: Int, field: TemporalField) {
    var standalone: Boolean = false
    cur match {
      case 'u' =>
      case 'y' =>
        if (count == 2) {
          appendValueReduced(field, 2, 2000)
        }
        else if (count < 4) {
          appendValue(field, count, 19, SignStyle.NORMAL)
        }
        else {
          appendValue(field, count, 19, SignStyle.EXCEEDS_PAD)
        }
        break //todo: break is not supported
      case 'c' =>
        if (count == 2) {
          throw new IllegalArgumentException("Invalid pattern \"cc\"")
        }
      case 'L' =>
      case 'q' =>
        standalone = true
      case 'M' =>
      case 'Q' =>
      case 'E' =>
      case 'e' =>
        count match {
          case 1 =>
          case 2 =>
            if (cur == 'c' || cur == 'e') {
              appendInternal(new DateTimeFormatterBuilder.WeekBasedFieldPrinterParser(cur, count))
            }
            else if (cur == 'E') {
              appendText(field, TextStyle.SHORT)
            }
            else {
              if (count == 1) {
                appendValue(field)
              }
              else {
                appendValue(field, 2)
              }
            }
            break //todo: break is not supported
          case 3 =>
            appendText(field, if (standalone) TextStyle.SHORT_STANDALONE else TextStyle.SHORT)
            break //todo: break is not supported
          case 4 =>
            appendText(field, if (standalone) TextStyle.FULL_STANDALONE else TextStyle.FULL)
            break //todo: break is not supported
          case 5 =>
            appendText(field, if (standalone) TextStyle.NARROW_STANDALONE else TextStyle.NARROW)
            break //todo: break is not supported
          case _ =>
            throw new IllegalArgumentException("Too many pattern letters: " + cur)
        }
        break //todo: break is not supported
      case 'a' =>
        if (count == 1) {
          appendText(field, TextStyle.SHORT)
        }
        else {
          throw new IllegalArgumentException("Too many pattern letters: " + cur)
        }
        break //todo: break is not supported
      case 'G' =>
        count match {
          case 1 =>
          case 2 =>
          case 3 =>
            appendText(field, TextStyle.SHORT)
            break //todo: break is not supported
          case 4 =>
            appendText(field, TextStyle.FULL)
            break //todo: break is not supported
          case 5 =>
            appendText(field, TextStyle.NARROW)
            break //todo: break is not supported
          case _ =>
            throw new IllegalArgumentException("Too many pattern letters: " + cur)
        }
        break //todo: break is not supported
      case 'S' =>
        appendFraction(NANO_OF_SECOND, count, count, false)
        break //todo: break is not supported
      case 'F' =>
        if (count == 1) {
          appendValue(field)
        }
        else {
          throw new IllegalArgumentException("Too many pattern letters: " + cur)
        }
        break //todo: break is not supported
      case 'd' =>
      case 'h' =>
      case 'H' =>
      case 'k' =>
      case 'K' =>
      case 'm' =>
      case 's' =>
        if (count == 1) {
          appendValue(field)
        }
        else if (count == 2) {
          appendValue(field, count)
        }
        else {
          throw new IllegalArgumentException("Too many pattern letters: " + cur)
        }
        break //todo: break is not supported
      case 'D' =>
        if (count == 1) {
          appendValue(field)
        }
        else if (count <= 3) {
          appendValue(field, count)
        }
        else {
          throw new IllegalArgumentException("Too many pattern letters: " + cur)
        }
        break //todo: break is not supported
      case _ =>
        if (count == 1) {
          appendValue(field)
        }
        else {
          appendValue(field, count)
        }
        break //todo: break is not supported
    }
  }

  /**
   * Causes the next added printer/parser to pad to a fixed width using a space.
   * <p>
   * This padding will pad to a fixed width using spaces.
   * <p>
   * During formatting, the decorated element will be output and then padded
   * to the specified width. An exception will be thrown during formatting if
   * the pad width is exceeded.
   * <p>
   * During parsing, the padding and decorated element are parsed.
   * If parsing is lenient, then the pad width is treated as a maximum.
   * If parsing is case insensitive, then the pad character is matched ignoring case.
   * The padding is parsed greedily. Thus, if the decorated element starts with
   * the pad character, it will not be parsed.
   *
   * @param padWidth  the pad width, 1 or greater
   * @return this, for chaining, not null
   * @throws IllegalArgumentException if pad width is too small
   */
  def padNext(padWidth: Int): DateTimeFormatterBuilder = {
     padNext(padWidth, ' ')
  }

  /**
   * Causes the next added printer/parser to pad to a fixed width.
   * <p>
   * This padding is intended for padding other than zero-padding.
   * Zero-padding should be achieved using the appendValue methods.
   * <p>
   * During formatting, the decorated element will be output and then padded
   * to the specified width. An exception will be thrown during formatting if
   * the pad width is exceeded.
   * <p>
   * During parsing, the padding and decorated element are parsed.
   * If parsing is lenient, then the pad width is treated as a maximum.
   * If parsing is case insensitive, then the pad character is matched ignoring case.
   * The padding is parsed greedily. Thus, if the decorated element starts with
   * the pad character, it will not be parsed.
   *
   * @param padWidth  the pad width, 1 or greater
   * @param padChar  the pad character
   * @return this, for chaining, not null
   * @throws IllegalArgumentException if pad width is too small
   */
  def padNext(padWidth: Int, padChar: Char): DateTimeFormatterBuilder = {
    if (padWidth < 1) {
      throw new IllegalArgumentException("The pad width must be at least one but was " + padWidth)
    }
    active.padNextWidth = padWidth
    active.padNextChar = padChar
    active.valueParserIndex = -1
     this
  }

  /**
   * Mark the start of an optional section.
   * <p>
   * The output of formatting can include optional sections, which may be nested.
   * An optional section is started by calling this method and ended by calling
   * {@link #optionalEnd()} or by ending the build process.
   * <p>
   * All elements in the optional section are treated as optional.
   * During formatting, the section is only output if data is available in the
   * {@code TemporalAccessor} for all the elements in the section.
   * During parsing, the whole section may be missing from the parsed string.
   * <p>
   * For example, consider a builder setup as
   * {@code builder.appendValue(HOUR_OF_DAY,2).optionalStart().appendValue(MINUTE_OF_HOUR,2)}.
   * The optional section ends automatically at the end of the builder.
   * During formatting, the minute will only be output if its value can be obtained from the date-time.
   * During parsing, the input will be successfully parsed whether the minute is present or not.
   *
   * @return this, for chaining, not null
   */
  def optionalStart: DateTimeFormatterBuilder = {
    active.valueParserIndex = -1
    active = new DateTimeFormatterBuilder(active, true)
     this
  }

  /**
   * Ends an optional section.
   * <p>
   * The output of formatting can include optional sections, which may be nested.
   * An optional section is started by calling {@link #optionalStart()} and ended
   * using this method (or at the end of the builder).
   * <p>
   * Calling this method without having previously called {@code optionalStart}
   * will throw an exception.
   * Calling this method immediately after calling {@code optionalStart} has no effect
   * on the formatter other than ending the (empty) optional section.
   * <p>
   * All elements in the optional section are treated as optional.
   * During formatting, the section is only output if data is available in the
   * {@code TemporalAccessor} for all the elements in the section.
   * During parsing, the whole section may be missing from the parsed string.
   * <p>
   * For example, consider a builder setup as
   * {@code builder.appendValue(HOUR_OF_DAY,2).optionalStart().appendValue(MINUTE_OF_HOUR,2).optionalEnd()}.
   * During formatting, the minute will only be output if its value can be obtained from the date-time.
   * During parsing, the input will be successfully parsed whether the minute is present or not.
   *
   * @return this, for chaining, not null
   * @throws IllegalStateException if there was no previous call to { @code optionalStart}
   */
  def optionalEnd: DateTimeFormatterBuilder = {
    if (active.parent == null) {
      throw new IllegalStateException("Cannot call optionalEnd() as there was no previous call to optionalStart()")
    }
    if (active.printerParsers.size > 0) {
      val cpp: DateTimeFormatterBuilder.CompositePrinterParser = new DateTimeFormatterBuilder.CompositePrinterParser(active.printerParsers, active.optional)
      active = active.parent
      appendInternal(cpp)
    }
    else {
      active = active.parent
    }
     this
  }

  /**
   * Appends a printer and/or parser to the internal list handling padding.
   *
   * @param pp  the printer-parser to add, not null
   * @return the index into the active parsers list
   */
  private def appendInternal(pp: DateTimeFormatterBuilder.DateTimePrinterParser): Int = {

    if (active.padNextWidth > 0) {
      if (pp != null) {
        pp = new DateTimeFormatterBuilder.PadPrinterParserDecorator(pp, active.padNextWidth, active.padNextChar)
      }
      active.padNextWidth = 0
      active.padNextChar = 0
    }
    active.printerParsers.add(pp)
    active.valueParserIndex = -1
     active.printerParsers.size - 1
  }

  /**
   * Completes this builder by creating the {@code DateTimeFormatter}
   * using the default locale.
   * <p>
   * This will create a formatter with the {@linkplain Locale#getDefault(Locale.Category) default FORMAT locale}.
   * Numbers will be printed and parsed using the standard DecimalStyle.
   * The resolver style will be {@link ResolverStyle#SMART SMART}.
   * <p>
   * Calling this method will end any open optional sections by repeatedly
   * calling {@link #optionalEnd()} before creating the formatter.
   * <p>
   * This builder can still be used after creating the formatter if desired,
   * although the state may have been changed by calls to {@code optionalEnd}.
   *
   * @return the created formatter, not null
   */
  def toFormatter: DateTimeFormatter = {
     toFormatter(Locale.getDefault(Locale.Category.FORMAT))
  }

  /**
   * Completes this builder by creating the {@code DateTimeFormatter}
   * using the specified locale.
   * <p>
   * This will create a formatter with the specified locale.
   * Numbers will be printed and parsed using the standard DecimalStyle.
   * The resolver style will be {@link ResolverStyle#SMART SMART}.
   * <p>
   * Calling this method will end any open optional sections by repeatedly
   * calling {@link #optionalEnd()} before creating the formatter.
   * <p>
   * This builder can still be used after creating the formatter if desired,
   * although the state may have been changed by calls to {@code optionalEnd}.
   *
   * @param locale  the locale to use for formatting, not null
   * @return the created formatter, not null
   */
  def toFormatter(locale: Locale): DateTimeFormatter = {
     toFormatter(locale, ResolverStyle.SMART, null)
  }

  /**
   * Completes this builder by creating the formatter.
   * This uses the default locale.
   *
   * @param resolverStyle  the resolver style to use, not null
   * @return the created formatter, not null
   */
  private[format] def toFormatter(resolverStyle: ResolverStyle, chrono: Chronology): DateTimeFormatter = {
     toFormatter(Locale.getDefault(Locale.Category.FORMAT), resolverStyle, chrono)
  }

  /**
   * Completes this builder by creating the formatter.
   *
   * @param locale  the locale to use for formatting, not null
   * @param chrono  the chronology to use, may be null
   * @return the created formatter, not null
   */
  private def toFormatter(locale: Locale, resolverStyle: ResolverStyle, chrono: Chronology): DateTimeFormatter = {

    while (active.parent != null) {
      optionalEnd
    }
    val pp: DateTimeFormatterBuilder.CompositePrinterParser = new DateTimeFormatterBuilder.CompositePrinterParser(printerParsers, false)
     new DateTimeFormatter(pp, locale, DecimalStyle.STANDARD, resolverStyle, null, chrono, null)
  }

  /**
   * The currently active builder, used by the outermost builder.
   */
  private var active: DateTimeFormatterBuilder = this
  /**
   * The parent builder, null for the outermost builder.
   */
  private final val parent: DateTimeFormatterBuilder = null
  /**
   * The list of printers that will be used.
   */
  private final val printerParsers: Nothing = new Nothing
  /**
   * Whether this builder produces an optional formatter.
   */
  private final val optional: Boolean = false
  /**
   * The width to pad the next field to.
   */
  private var padNextWidth: Int = 0
  /**
   * The character to pad the next field with.
   */
  private var padNextChar: Char = 0
  /**
   * The index of the last variable width value parser.
   */
  private var valueParserIndex: Int = -1
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
 * Copyright (c) 2008-2012, Stephen Colebourne & Michael Nascimento Santos
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
 * Context object used during date and time parsing.
 * <p>
 * This class represents the current state of the parse.
 * It has the ability to store and retrieve the parsed values and manage optional segments.
 * It also provides key information to the parsing methods.
 * <p>
 * Once parsing is complete, the {@link #toParsed()} is used to obtain the data.
 * It contains a method to resolve  the separate parsed fields into meaningful values.
 *
 * @implSpec
 * This class is a mutable context intended for use from a single thread.
 * Usage of the class is thread-safe within standard parsing as a new instance of this class
 * is automatically created for each parse and parsing is single-threaded
 *
 * @since 1.8
 */
object DateTimeParseContext {
  /**
   * Compares two characters ignoring case.
   *
   * @param c1  the first
   * @param c2  the second
   * @return true if equal
   */
  private[format] def charEqualsIgnoreCase(c1: Char, c2: Char): Boolean = {
     c1 == c2 || Character.toUpperCase(c1) == Character.toUpperCase(c2) || Character.toLowerCase(c1) == Character.toLowerCase(c2)
  }
}

final class DateTimeParseContext {
  /**
   * Creates a new instance of the context.
   *
   * @param formatter  the formatter controlling the parse, not null
   */
  private[format] def this(formatter: DateTimeFormatter) {

    `super`
    this.formatter = formatter
    parsed.add(new Parsed)
  }

  /**
   * Creates a copy of this context.
   * This retains the case sensitive and strict flags.
   */
  private[format] def copy: DateTimeParseContext = {
    val newContext: DateTimeParseContext = new DateTimeParseContext(formatter)
    newContext.caseSensitive = caseSensitive
    newContext.strict = strict
     newContext
  }

  /**
   * Gets the locale.
   * <p>
   * This locale is used to control localization in the parse except
   * where localization is controlled by the DecimalStyle.
   *
   * @return the locale, not null
   */
  private[format] def getLocale: Locale = {
     formatter.getLocale
  }

  /**
   * Gets the DecimalStyle.
   * <p>
   * The DecimalStyle controls the numeric parsing.
   *
   * @return the DecimalStyle, not null
   */
  private[format] def getDecimalStyle: DecimalStyle = {
     formatter.getDecimalStyle
  }

  /**
   * Gets the effective chronology during parsing.
   *
   * @return the effective parsing chronology, not null
   */
  private[format] def getEffectiveChronology: Chronology = {
    var chrono: Chronology = currentParsed.chrono
    if (chrono == null) {
      chrono = formatter.getChronology
      if (chrono == null) {
        chrono = IsoChronology.INSTANCE
      }
    }
     chrono
  }

  /**
   * Checks if parsing is case sensitive.
   *
   * @return true if parsing is case sensitive, false if case insensitive
   */
  private[format] def isCaseSensitive: Boolean = {
     caseSensitive
  }

  /**
   * Sets whether the parsing is case sensitive or not.
   *
   * @param caseSensitive  changes the parsing to be case sensitive or not from now on
   */
  private[format] def setCaseSensitive(caseSensitive: Boolean) {
    this.caseSensitive = caseSensitive
  }

  /**
   * Helper to compare two {@code CharSequence} instances.
   * This uses {@link #isCaseSensitive()}.
   *
   * @param cs1  the first character sequence, not null
   * @param offset1  the offset into the first sequence, valid
   * @param cs2  the second character sequence, not null
   * @param offset2  the offset into the second sequence, valid
   * @param length  the length to check, valid
   * @return true if equal
   */
  private[format] def subSequenceEquals(cs1: CharSequence, offset1: Int, cs2: CharSequence, offset2: Int, length: Int): Boolean = {
    if (offset1 + length > cs1.length || offset2 + length > cs2.length) {
       false
    }
    if (isCaseSensitive) {
      {
        var i: Int = 0
        while (i < length) {
          {
            val ch1: Char = cs1.charAt(offset1 + i)
            val ch2: Char = cs2.charAt(offset2 + i)
            if (ch1 != ch2) {
               false
            }
          }
          ({
            i += 1; i - 1
          })
        }
      }
    }
    else {
      {
        var i: Int = 0
        while (i < length) {
          {
            val ch1: Char = cs1.charAt(offset1 + i)
            val ch2: Char = cs2.charAt(offset2 + i)
            if (ch1 != ch2 && Character.toUpperCase(ch1) != Character.toUpperCase(ch2) && Character.toLowerCase(ch1) != Character.toLowerCase(ch2)) {
               false
            }
          }
          ({
            i += 1; i - 1
          })
        }
      }
    }
     true
  }

  /**
   * Helper to compare two {@code char}.
   * This uses {@link #isCaseSensitive()}.
   *
   * @param ch1  the first character
   * @param ch2  the second character
   * @return true if equal
   */
  private[format] def charEquals(ch1: Char, ch2: Char): Boolean = {
    if (isCaseSensitive) {
       ch1 == ch2
    }
     charEqualsIgnoreCase(ch1, ch2)
  }

  /**
   * Checks if parsing is strict.
   * <p>
   * Strict parsing requires exact matching of the text and sign styles.
   *
   * @return true if parsing is strict, false if lenient
   */
  private[format] def isStrict: Boolean = {
     strict
  }

  /**
   * Sets whether parsing is strict or lenient.
   *
   * @param strict  changes the parsing to be strict or lenient from now on
   */
  private[format] def setStrict(strict: Boolean) {
    this.strict = strict
  }

  /**
   * Starts the parsing of an optional segment of the input.
   */
  private[format] def startOptional {
    parsed.add(currentParsed.copy)
  }

  /**
   * Ends the parsing of an optional segment of the input.
   *
   * @param successful  whether the optional segment was successfully parsed
   */
  private[format] def endOptional(successful: Boolean) {
    if (successful) {
      parsed.remove(parsed.size - 2)
    }
    else {
      parsed.remove(parsed.size - 1)
    }
  }

  /**
   * Gets the currently active temporal objects.
   *
   * @return the current temporal objects, not null
   */
  private def currentParsed: Parsed = {
     parsed.get(parsed.size - 1)
  }

  /**
   * Gets the result of the parse.
   *
   * @return the result of the parse, not null
   */
  private[format] def toParsed: Parsed = {
    val parsed: Parsed = currentParsed
    parsed.effectiveChrono = getEffectiveChronology
     parsed
  }

  /**
   * Gets the first value that was parsed for the specified field.
   * <p>
   * This searches the results of the parse, returning the first value found
   * for the specified field. No attempt is made to derive a value.
   * The field may have an out of range value.
   * For example, the day-of-month might be set to 50, or the hour to 1000.
   *
   * @param field  the field to query from the map, null returns null
   * @return the value mapped to the specified field, null if field was not parsed
   */
  private[format] def getParsed(field: TemporalField): Long = {
     currentParsed.fieldValues.get(field)
  }

  /**
   * Stores the parsed field.
   * <p>
   * This stores a field-value pair that has been parsed.
   * The value stored may be out of range for the field - no checks are performed.
   *
   * @param field  the field to set in the field-value map, not null
   * @param value  the value to set in the field-value map
   * @param errorPos  the position of the field being parsed
   * @param successPos  the position after the field being parsed
   * @return the new position
   */
  private[format] def setParsedField(field: TemporalField, value: Long, errorPos: Int, successPos: Int): Int = {

    val old: Long = currentParsed.fieldValues.put(field, value)
     if ((old != null && old.longValue != value)) ~errorPos else successPos
  }

  /**
   * Stores the parsed chronology.
   * <p>
   * This stores the chronology that has been parsed.
   * No validation is performed other than ensuring it is not null.
   *
   * @param chrono  the parsed chronology, not null
   */
  private[format] def setParsed(chrono: Chronology) {

    currentParsed.chrono = chrono
  }

  /**
   * Stores the parsed zone.
   * <p>
   * This stores the zone that has been parsed.
   * No validation is performed other than ensuring it is not null.
   *
   * @param zone  the parsed zone, not null
   */
  private[format] def setParsed(zone: ZoneId) {

    currentParsed.zone = zone
  }

  /**
   * Stores the parsed leap second.
   */
  private[format] def setParsedLeapSecond {
    currentParsed.leapSecond = true
  }

  /**
   * Returns a string version of the context for debugging.
   *
   * @return a string representation of the context data, not null
   */
  override def toString: String = {
     currentParsed.toString
  }

  /**
   * The formatter, not null.
   */
  private var formatter: DateTimeFormatter = null
  /**
   * Whether to parse using case sensitively.
   */
  private var caseSensitive: Boolean = true
  /**
   * Whether to parse using strict rules.
   */
  private var strict: Boolean = true
  /**
   * The list of parsed data.
   */
  private final val parsed: Nothing = new Nothing
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
 * Copyright (c) 2008-2012, Stephen Colebourne & Michael Nascimento Santos
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
 * An exception thrown when an error occurs during parsing.
 * <p>
 * This exception includes the text being parsed and the error index.
 *
 * @implSpec
 * This class is intended for use in a single thread.
 *
 * @since 1.8
 */
object DateTimeParseException {
  /**
   * Serialization version.
   */
  private final val serialVersionUID: Long = 4304633501674722597L
}

class DateTimeParseException extends DateTimeException {
  /**
   * Constructs a new exception with the specified message.
   *
   * @param message  the message to use for this exception, may be null
   * @param parsedData  the parsed text, should not be null
   * @param errorIndex  the index in the parsed string that was invalid, should be a valid index
   */
  def this(message: String, parsedData: CharSequence, errorIndex: Int) {

    `super`(message)
    this.parsedString = parsedData.toString
    this.errorIndex = errorIndex
  }

  /**
   * Constructs a new exception with the specified message and cause.
   *
   * @param message  the message to use for this exception, may be null
   * @param parsedData  the parsed text, should not be null
   * @param errorIndex  the index in the parsed string that was invalid, should be a valid index
   * @param cause  the cause exception, may be null
   */
  def this(message: String, parsedData: CharSequence, errorIndex: Int, cause: Throwable) {

    `super`(message, cause)
    this.parsedString = parsedData.toString
    this.errorIndex = errorIndex
  }

  /**
   * Returns the string that was being parsed.
   *
   * @return the string that was being parsed, should not be null.
   */
  def getParsedString: String = {
     parsedString
  }

  /**
   * Returns the index where the error was found.
   *
   * @return the index in the parsed string that was invalid, should be a valid index
   */
  def getErrorIndex: Int = {
     errorIndex
  }

  /**
   * The text that was being parsed.
   */
  private final val parsedString: String = null
  /**
   * The error index in the text.
   */
  private final val errorIndex: Int = 0
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
 * Copyright (c) 2011-2012, Stephen Colebourne & Michael Nascimento Santos
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
 * Context object used during date and time printing.
 * <p>
 * This class provides a single wrapper to items used in the format.
 *
 * @implSpec
 * This class is a mutable context intended for use from a single thread.
 * Usage of the class is thread-safe within standard printing as the framework creates
 * a new instance of the class for each format and printing is single-threaded.
 *
 * @since 1.8
 */
object DateTimePrintContext {
  private def adjust(temporal: TemporalAccessor, formatter: DateTimeFormatter): TemporalAccessor = {
    var overrideChrono: Chronology = formatter.getChronology
    var overrideZone: ZoneId = formatter.getZone
    if (overrideChrono == null && overrideZone == null) {
       temporal
    }
    val temporalChrono: Chronology = temporal.query(TemporalQuery.chronology)
    val temporalZone: ZoneId = temporal.query(TemporalQuery.zoneId)
    if (Objects.equals(overrideChrono, temporalChrono)) {
      overrideChrono = null
    }
    if (Objects.equals(overrideZone, temporalZone)) {
      overrideZone = null
    }
    if (overrideChrono == null && overrideZone == null) {
       temporal
    }
    val effectiveChrono: Chronology = (if (overrideChrono != null) overrideChrono else temporalChrono)
    if (overrideZone != null) {
      if (temporal.isSupported(INSTANT_SECONDS)) {
        val chrono: Chronology = (if (effectiveChrono != null) effectiveChrono else IsoChronology.INSTANCE)
         chrono.zonedDateTime(Instant.from(temporal), overrideZone)
      }
      if (overrideZone.normalized.isInstanceOf[ZoneOffset] && temporal.isSupported(OFFSET_SECONDS) && temporal.get(OFFSET_SECONDS) != overrideZone.getRules.getOffset(Instant.EPOCH).getTotalSeconds) {
        throw new DateTimeException("Unable to apply override zone '" + overrideZone + "' because the temporal object being formatted has a different offset but" + " does not represent an instant: " + temporal)
      }
    }
    val effectiveZone: ZoneId = (if (overrideZone != null) overrideZone else temporalZone)
    val effectiveDate: Nothing = null
    if (overrideChrono != null) {
      if (temporal.isSupported(EPOCH_DAY)) {
        effectiveDate = effectiveChrono.date(temporal)
      }
      else {
        if (!(overrideChrono eq IsoChronology.INSTANCE && temporalChrono == null)) {
          for (f <- ChronoField.values) {
            if (f.isDateBased && temporal.isSupported(f)) {
              throw new DateTimeException("Unable to apply override chronology '" + overrideChrono + "' because the temporal object being formatted contains date fields but" + " does not represent a whole date: " + temporal)
            }
          }
        }
        effectiveDate = null
      }
    }
    else {
      effectiveDate = null
    }
     new TemporalAccessor {
      def isSupported(field: TemporalField): Boolean = {
        if (effectiveDate != null && field.isDateBased) {
           effectiveDate.isSupported(field)
        }
         temporal.isSupported(field)
      }

      override def range(field: TemporalField): Nothing = {
        if (effectiveDate != null && field.isDateBased) {
           effectiveDate.range(field)
        }
         temporal.range(field)
      }

      def getLong(field: TemporalField): Long = {
        if (effectiveDate != null && field.isDateBased) {
           effectiveDate.getLong(field)
        }
         temporal.getLong(field)
      }

      override def query(query: TemporalQuery[R]): R = {
        if (query eq TemporalQuery.chronology) {
           effectiveChrono.asInstanceOf[R]
        }
        if (query eq TemporalQuery.zoneId) {
           effectiveZone.asInstanceOf[R]
        }
        if (query eq TemporalQuery.precision) {
           temporal.query(query)
        }
         query.queryFrom(this)
      }
    }
  }
}

final class DateTimePrintContext {
  /**
   * Creates a new instance of the context.
   *
   * @param temporal  the temporal object being output, not null
   * @param formatter  the formatter controlling the format, not null
   */
  private[format] def this(temporal: TemporalAccessor, formatter: DateTimeFormatter) {

    `super`
    this.temporal = adjust(temporal, formatter)
    this.formatter = formatter
  }

  /**
   * Gets the temporal object being output.
   *
   * @return the temporal object, not null
   */
  private[format] def getTemporal: TemporalAccessor = {
     temporal
  }

  /**
   * Gets the locale.
   * <p>
   * This locale is used to control localization in the format output except
   * where localization is controlled by the DecimalStyle.
   *
   * @return the locale, not null
   */
  private[format] def getLocale: Locale = {
     formatter.getLocale
  }

  /**
   * Gets the DecimalStyle.
   * <p>
   * The DecimalStyle controls the localization of numeric output.
   *
   * @return the DecimalStyle, not null
   */
  private[format] def getDecimalStyle: DecimalStyle = {
     formatter.getDecimalStyle
  }

  /**
   * Starts the printing of an optional segment of the input.
   */
  private[format] def startOptional {
    this.optional += 1
  }

  /**
   * Ends the printing of an optional segment of the input.
   */
  private[format] def endOptional {
    this.optional -= 1
  }

  /**
   * Gets a value using a query.
   *
   * @param query  the query to use, not null
   * @return the result, null if not found and optional is true
   * @throws DateTimeException if the type is not available and the section is not optional
   */
  private[format] def getValue(query: TemporalQuery[R]): R = {
    val result: R = temporal.query(query)
    if (result == null && optional == 0) {
      throw new DateTimeException("Unable to extract value: " + temporal.getClass)
    }
     result
  }

  /**
   * Gets the value of the specified field.
   * <p>
   * This will return the value for the specified field.
   *
   * @param field  the field to find, not null
   * @return the value, null if not found and optional is true
   * @throws DateTimeException if the field is not available and the section is not optional
   */
  private[format] def getValue(field: TemporalField): Long = {
    try {
       temporal.getLong(field)
    }
    catch {
      case ex: DateTimeException => {
        if (optional > 0) {
           null
        }
        throw ex
      }
    }
  }

  /**
   * Returns a string version of the context for debugging.
   *
   * @return a string representation of the context, not null
   */
  override def toString: String = {
     temporal.toString
  }

  /**
   * The temporal being output.
   */
  private var temporal: TemporalAccessor = null
  /**
   * The formatter, not null.
   */
  private var formatter: DateTimeFormatter = null
  /**
   * Whether the current formatter is optional.
   */
  private var optional: Int = 0
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
 * Copyright (c) 2011-2012, Stephen Colebourne & Michael Nascimento Santos
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
 * A provider to obtain the textual form of a date-time field.
 *
 * @implSpec
 * Implementations must be thread-safe.
 * Implementations should cache the textual information.
 *
 * @since 1.8
 */
object DateTimeTextProvider {
  /**
   * Gets the provider of text.
   *
   * @return the provider, not null
   */
  private[format] def getInstance: DateTimeTextProvider = {
     new DateTimeTextProvider
  }

  private def toWeekDay(calWeekDay: Int): Int = {
    if (calWeekDay == Calendar.SUNDAY) {
       7
    }
    else {
       calWeekDay - 1
    }
  }

  /**
   * Helper method to create an immutable entry.
   *
   * @param text  the text, not null
   * @param field  the field, not null
   * @return the entry, not null
   */
  private def createEntry(text: A, field: B): Nothing = {
     new Nothing(text, field)
  }

  /**
   * Returns the localized resource of the given key and locale, or null
   * if no localized resource is available.
   *
   * @param key  the key of the localized resource, not null
   * @param locale  the locale, not null
   * @return the localized resource, or null if not available
   * @throws NullPointerException if key or locale is null
   */
  private[format] def getLocalizedResource(key: String, locale: Locale): T = {
    val lr: Nothing = LocaleProviderAdapter.getResourceBundleBased.getLocaleResources(locale)
    val rb: Nothing = lr.getJavaTimeFormatData
     if (rb.containsKey(key)) rb.getObject(key).asInstanceOf[T] else null
  }

  /** Cache. */
  private final val CACHE: Nothing = new Nothing(16, 0.75f, 2)
  /** Comparator. */
  private final val COMPARATOR: Nothing = new Nothing {
    def compare(obj1: Nothing, obj2: Nothing): Int = {
       obj2.getKey.length - obj1.getKey.length
    }
  }

  /**
   * Stores the text for a single locale.
   * <p>
   * Some fields have a textual representation, such as day-of-week or month-of-year.
   * These textual representations can be captured in this class for printing
   * and parsing.
   * <p>
   * This class is immutable and thread-safe.
   */
  private[format] final class LocaleStore {
    /**
     * Constructor.
     *
     * @param valueTextMap  the map of values to text to store, assigned and not altered, not null
     */
    private[format] def this(valueTextMap: Map[TextStyle, Map[Long, String]]) {

      this.valueTextMap = valueTextMap
      val map: Map[TextStyle, Nothing] = new HashMap[K, V]
      val allList: Nothing = new Nothing
      import scala.collection.JavaConversions._
      for (vtmEntry <- valueTextMap.entrySet) {
        val reverse: Map[String, Nothing] = new HashMap[K, V]
        import scala.collection.JavaConversions._
        for (entry <- vtmEntry.getValue.entrySet) {
          if (reverse.put(entry.getValue, createEntry(entry.getValue, entry.getKey)) != null) {
            continue //todo: continue is not supported
          }
        }
        val list: Nothing = new Nothing(reverse.values)
        Collections.sort(list, COMPARATOR)
        map.put(vtmEntry.getKey, list)
        allList.addAll(list)
        map.put(null, allList)
      }
      Collections.sort(allList, COMPARATOR)
      this.parsable = map
    }

    /**
     * Gets the text for the specified field value, locale and style
     * for the purpose of printing.
     *
     * @param value  the value to get text for, not null
     * @param style  the style to get text for, not null
     * @return the text for the field value, null if no text found
     */
    private[format] def getText(value: Long, style: TextStyle): String = {
      val map: Map[Long, String] = valueTextMap.get(style)
       if (map != null) map.get(value) else null
    }

    /**
     * Gets an iterator of text to field for the specified style for the purpose of parsing.
     * <p>
     * The iterator must be returned in order from the longest text to the shortest.
     *
     * @param style  the style to get text for, null for all parsable text
     * @return the iterator of text to field pairs, in order from longest text to shortest text,
     *         null if the style is not parsable
     */
    private[format] def getTextIterator(style: TextStyle): Nothing = {
      val list: Nothing = parsable.get(style)
       if (list != null) list.iterator else null
    }

    /**
     * Map of value to text.
     */
    private final val valueTextMap: Map[TextStyle, Map[Long, String]] = null
    /**
     * Parsable data.
     */
    private final val parsable: Map[TextStyle, Nothing] = null
  }

}

class DateTimeTextProvider {
  private[format] def  {

  }

  /**
   * Gets the text for the specified field, locale and style
   * for the purpose of formatting.
   * <p>
   * The text associated with the value is returned.
   * The null return value should be used if there is no applicable text, or
   * if the text would be a numeric representation of the value.
   *
   * @param field  the field to get text for, not null
   * @param value  the field value to get text for, not null
   * @param style  the style to get text for, not null
   * @param locale  the locale to get text for, not null
   * @return the text for the field value, null if no text found
   */
  def getText(field: TemporalField, value: Long, style: TextStyle, locale: Locale): String = {
    val store: AnyRef = findStore(field, locale)
    if (store.isInstanceOf[DateTimeTextProvider.LocaleStore]) {
       (store.asInstanceOf[DateTimeTextProvider.LocaleStore]).getText(value, style)
    }
     null
  }

  /**
   * Gets the text for the specified chrono, field, locale and style
   * for the purpose of formatting.
   * <p>
   * The text associated with the value is returned.
   * The null return value should be used if there is no applicable text, or
   * if the text would be a numeric representation of the value.
   *
   * @param chrono  the Chronology to get text for, not null
   * @param field  the field to get text for, not null
   * @param value  the field value to get text for, not null
   * @param style  the style to get text for, not null
   * @param locale  the locale to get text for, not null
   * @return the text for the field value, null if no text found
   */
  def getText(chrono: Chronology, field: TemporalField, value: Long, style: TextStyle, locale: Locale): String = {
    if (chrono eq IsoChronology.INSTANCE || !(field.isInstanceOf[ChronoField])) {
       getText(field, value, style, locale)
    }
    var fieldIndex: Int = 0
    var fieldValue: Int = 0
    if (field eq ERA) {
      fieldIndex = Calendar.ERA
      if (chrono eq JapaneseChronology.INSTANCE) {
        if (value == -999) {
          fieldValue = 0
        }
        else {
          fieldValue = value.asInstanceOf[Int] + 2
        }
      }
      else {
        fieldValue = value.asInstanceOf[Int]
      }
    }
    else if (field eq MONTH_OF_YEAR) {
      fieldIndex = Calendar.MONTH
      fieldValue = value.asInstanceOf[Int] - 1
    }
    else if (field eq DAY_OF_WEEK) {
      fieldIndex = Calendar.DAY_OF_WEEK
      fieldValue = value.asInstanceOf[Int] + 1
      if (fieldValue > 7) {
        fieldValue = Calendar.SUNDAY
      }
    }
    else if (field eq AMPM_OF_DAY) {
      fieldIndex = Calendar.AM_PM
      fieldValue = value.asInstanceOf[Int]
    }
    else {
       null
    }
     CalendarDataUtility.retrieveJavaTimeFieldValueName(chrono.getCalendarType, fieldIndex, fieldValue, style.toCalendarStyle, locale)
  }

  /**
   * Gets an iterator of text to field for the specified field, locale and style
   * for the purpose of parsing.
   * <p>
   * The iterator must be returned in order from the longest text to the shortest.
   * <p>
   * The null return value should be used if there is no applicable parsable text, or
   * if the text would be a numeric representation of the value.
   * Text can only be parsed if all the values for that field-style-locale combination are unique.
   *
   * @param field  the field to get text for, not null
   * @param style  the style to get text for, null for all parsable text
   * @param locale  the locale to get text for, not null
   * @return the iterator of text to field pairs, in order from longest text to shortest text,
   *         null if the field or style is not parsable
   */
  def getTextIterator(field: TemporalField, style: TextStyle, locale: Locale): Nothing = {
    val store: AnyRef = findStore(field, locale)
    if (store.isInstanceOf[DateTimeTextProvider.LocaleStore]) {
       (store.asInstanceOf[DateTimeTextProvider.LocaleStore]).getTextIterator(style)
    }
     null
  }

  /**
   * Gets an iterator of text to field for the specified chrono, field, locale and style
   * for the purpose of parsing.
   * <p>
   * The iterator must be returned in order from the longest text to the shortest.
   * <p>
   * The null return value should be used if there is no applicable parsable text, or
   * if the text would be a numeric representation of the value.
   * Text can only be parsed if all the values for that field-style-locale combination are unique.
   *
   * @param chrono  the Chronology to get text for, not null
   * @param field  the field to get text for, not null
   * @param style  the style to get text for, null for all parsable text
   * @param locale  the locale to get text for, not null
   * @return the iterator of text to field pairs, in order from longest text to shortest text,
   *         null if the field or style is not parsable
   */
  def getTextIterator(chrono: Chronology, field: TemporalField, style: TextStyle, locale: Locale): Nothing = {
    if (chrono eq IsoChronology.INSTANCE || !(field.isInstanceOf[ChronoField])) {
       getTextIterator(field, style, locale)
    }
    var fieldIndex: Int = 0
    field.asInstanceOf[ChronoField] match {
      case ERA =>
        fieldIndex = Calendar.ERA
        break //todo: break is not supported
      case MONTH_OF_YEAR =>
        fieldIndex = Calendar.MONTH
        break //todo: break is not supported
      case DAY_OF_WEEK =>
        fieldIndex = Calendar.DAY_OF_WEEK
        break //todo: break is not supported
      case AMPM_OF_DAY =>
        fieldIndex = Calendar.AM_PM
        break //todo: break is not supported
      case _ =>
         null
    }
    val calendarStyle: Int = if ((style == null)) Calendar.ALL_STYLES else style.toCalendarStyle
    val map: Map[String, Integer] = CalendarDataUtility.retrieveJavaTimeFieldValueNames(chrono.getCalendarType, fieldIndex, calendarStyle, locale)
    if (map == null) {
       null
    }
    val list: Nothing = new Nothing(map.size)
    fieldIndex match {
      case Calendar.ERA =>
        import scala.collection.JavaConversions._
        for (entry <- map.entrySet) {
          var era: Int = entry.getValue
          if (chrono eq JapaneseChronology.INSTANCE) {
            if (era == 0) {
              era = -999
            }
            else {
              era -= 2
            }
          }
          list.add(createEntry(entry.getKey, era.asInstanceOf[Long]))
        }
        break //todo: break is not supported
      case Calendar.MONTH =>
        import scala.collection.JavaConversions._
        for (entry <- map.entrySet) {
          list.add(createEntry(entry.getKey, (entry.getValue + 1).asInstanceOf[Long]))
        }
        break //todo: break is not supported
      case Calendar.DAY_OF_WEEK =>
        import scala.collection.JavaConversions._
        for (entry <- map.entrySet) {
          list.add(createEntry(entry.getKey, toWeekDay(entry.getValue).asInstanceOf[Long]))
        }
        break //todo: break is not supported
      case _ =>
        import scala.collection.JavaConversions._
        for (entry <- map.entrySet) {
          list.add(createEntry(entry.getKey, entry.getValue.asInstanceOf[Long]))
        }
        break //todo: break is not supported
    }
     list.iterator
  }

  private def findStore(field: TemporalField, locale: Locale): AnyRef = {
    val key: Nothing = createEntry(field, locale)
    var store: AnyRef = CACHE.get(key)
    if (store == null) {
      store = createStore(field, locale)
      CACHE.putIfAbsent(key, store)
      store = CACHE.get(key)
    }
     store
  }

  private def createStore(field: TemporalField, locale: Locale): AnyRef = {
    val styleMap: Map[TextStyle, Map[Long, String]] = new HashMap[TextStyle, Map[Long, String]]
    if (field eq ERA) {
      for (textStyle <- TextStyle.values) {
        if (textStyle.isStandalone) {
          continue //todo: continue is not supported
        }
        val displayNames: Map[String, Integer] = CalendarDataUtility.retrieveJavaTimeFieldValueNames("gregory", Calendar.ERA, textStyle.toCalendarStyle, locale)
        if (displayNames != null) {
          val map: Map[Long, String] = new HashMap[Long, String]
          import scala.collection.JavaConversions._
          for (entry <- displayNames.entrySet) {
            map.put(entry.getValue.asInstanceOf[Long], entry.getKey)
          }
          if (!map.isEmpty) {
            styleMap.put(textStyle, map)
          }
        }
      }
       new DateTimeTextProvider.LocaleStore(styleMap)
    }
    if (field eq MONTH_OF_YEAR) {
      for (textStyle <- TextStyle.values) {
        val displayNames: Map[String, Integer] = CalendarDataUtility.retrieveJavaTimeFieldValueNames("gregory", Calendar.MONTH, textStyle.toCalendarStyle, locale)
        val map: Map[Long, String] = new HashMap[Long, String]
        if (displayNames != null) {
          import scala.collection.JavaConversions._
          for (entry <- displayNames.entrySet) {
            map.put((entry.getValue + 1).asInstanceOf[Long], entry.getKey)
          }
        }
        else {
          {
            var month: Int = Calendar.JANUARY
            while (month <= Calendar.DECEMBER) {
              {
                var name: String = null
                name = CalendarDataUtility.retrieveJavaTimeFieldValueName("gregory", Calendar.MONTH, month, textStyle.toCalendarStyle, locale)
                if (name == null) {
                  break //todo: break is not supported
                }
                map.put((month + 1).asInstanceOf[Long], name)
              }
              ({
                month += 1; month - 1
              })
            }
          }
        }
        if (!map.isEmpty) {
          styleMap.put(textStyle, map)
        }
      }
       new DateTimeTextProvider.LocaleStore(styleMap)
    }
    if (field eq DAY_OF_WEEK) {
      for (textStyle <- TextStyle.values) {
        val displayNames: Map[String, Integer] = CalendarDataUtility.retrieveJavaTimeFieldValueNames("gregory", Calendar.DAY_OF_WEEK, textStyle.toCalendarStyle, locale)
        val map: Map[Long, String] = new HashMap[Long, String]
        if (displayNames != null) {
          import scala.collection.JavaConversions._
          for (entry <- displayNames.entrySet) {
            map.put(toWeekDay(entry.getValue).asInstanceOf[Long], entry.getKey)
          }
        }
        else {
          {
            var wday: Int = Calendar.SUNDAY
            while (wday <= Calendar.SATURDAY) {
              {
                var name: String = null
                name = CalendarDataUtility.retrieveJavaTimeFieldValueName("gregory", Calendar.DAY_OF_WEEK, wday, textStyle.toCalendarStyle, locale)
                if (name == null) {
                  break //todo: break is not supported
                }
                map.put(toWeekDay(wday).asInstanceOf[Long], name)
              }
              ({
                wday += 1; wday - 1
              })
            }
          }
        }
        if (!map.isEmpty) {
          styleMap.put(textStyle, map)
        }
      }
       new DateTimeTextProvider.LocaleStore(styleMap)
    }
    if (field eq AMPM_OF_DAY) {
      for (textStyle <- TextStyle.values) {
        if (textStyle.isStandalone) {
          continue //todo: continue is not supported
        }
        val displayNames: Map[String, Integer] = CalendarDataUtility.retrieveJavaTimeFieldValueNames("gregory", Calendar.AM_PM, textStyle.toCalendarStyle, locale)
        if (displayNames != null) {
          val map: Map[Long, String] = new HashMap[Long, String]
          import scala.collection.JavaConversions._
          for (entry <- displayNames.entrySet) {
            map.put(entry.getValue.asInstanceOf[Long], entry.getKey)
          }
          if (!map.isEmpty) {
            styleMap.put(textStyle, map)
          }
        }
      }
       new DateTimeTextProvider.LocaleStore(styleMap)
    }
    if (field eq IsoFields.QUARTER_OF_YEAR) {
      val keys: Array[String] = Array("QuarterNames", "standalone.QuarterNames", "QuarterAbbreviations", "standalone.QuarterAbbreviations", "QuarterNarrows", "standalone.QuarterNarrows")
      {
        var i: Int = 0
        while (i < keys.length) {
          {
            val names: Array[String] = getLocalizedResource(keys(i), locale)
            if (names != null) {
              val map: Map[Long, String] = new HashMap[Long, String]
              {
                var q: Int = 0
                while (q < names.length) {
                  {
                    map.put((q + 1).asInstanceOf[Long], names(q))
                  }
                  ({
                    q += 1; q - 1
                  })
                }
              }
              styleMap.put(TextStyle.values(i), map)
            }
          }
          ({
            i += 1; i - 1
          })
        }
      }
       new DateTimeTextProvider.LocaleStore(styleMap)
    }
     ""
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
 * This file is available under and governed by the GNU General Public
 * License version 2 only, as published by the Free Software Foundation.
 * However, the following notice accompanied the original version of this
 * file:
 *
 * Copyright (c) 2008-2012, Stephen Colebourne & Michael Nascimento Santos
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
 * Localized decimal style used in date and time formatting.
 * <p>
 * A significant part of dealing with dates and times is the localization.
 * This class acts as a central point for accessing the information.
 *
 * @implSpec
 * This class is immutable and thread-safe.
 *
 * @since 1.8
 */
object DecimalStyle {
  /**
   * Lists all the locales that are supported.
   * <p>
   * The locale 'en_US' will always be present.
   *
   * @return a Set of Locales for which localization is supported
   */
  def getAvailableLocales: Set[Locale] = {
    val l: Array[Locale] = DecimalFormatSymbols.getAvailableLocales
    val locales: Set[Locale] = new HashSet[Locale](l.length)
    Collections.addAll(locales, l)
     locales
  }

  /**
   * Obtains the DecimalStyle for the default
   * {@link java.util.Locale.Category#FORMAT FORMAT} locale.
   * <p>
   * This method provides access to locale sensitive decimal style symbols.
   * <p>
   * This is equivalent to calling
   * {@link #of(Locale)
     *     of(Locale.getDefault(Locale.Category.FORMAT))}.
   *
   * @see java.util.Locale.Category#FORMAT
   * @return the info, not null
   */
  def ofDefaultLocale: DecimalStyle = {
     of(Locale.getDefault(Locale.Category.FORMAT))
  }

  /**
   * Obtains the DecimalStyle for the specified locale.
   * <p>
   * This method provides access to locale sensitive decimal style symbols.
   *
   * @param locale  the locale, not null
   * @return the info, not null
   */
  def of(locale: Locale): DecimalStyle = {

    var info: DecimalStyle = CACHE.get(locale)
    if (info == null) {
      info = create(locale)
      CACHE.putIfAbsent(locale, info)
      info = CACHE.get(locale)
    }
     info
  }

  private def create(locale: Locale): DecimalStyle = {
    val oldSymbols: Nothing = DecimalFormatSymbols.getInstance(locale)
    val zeroDigit: Char = oldSymbols.getZeroDigit
    val positiveSign: Char = '+'
    val negativeSign: Char = oldSymbols.getMinusSign
    val decimalSeparator: Char = oldSymbols.getDecimalSeparator
    if (zeroDigit == '0' && negativeSign == '-' && decimalSeparator == '.') {
       STANDARD
    }
     new DecimalStyle(zeroDigit, positiveSign, negativeSign, decimalSeparator)
  }

  /**
   * The standard set of non-localized decimal style symbols.
   * <p>
   * This uses standard ASCII characters for zero, positive, negative and a dot for the decimal point.
   */
  final val STANDARD: DecimalStyle = new DecimalStyle('0', '+', '-', '.')
  /**
   * The cache of DecimalStyle instances.
   */
  private final val CACHE: Nothing = new Nothing(16, 0.75f, 2)
}

final class DecimalStyle {
  /**
   * Restricted constructor.
   *
   * @param zeroChar  the character to use for the digit of zero
   * @param positiveSignChar  the character to use for the positive sign
   * @param negativeSignChar  the character to use for the negative sign
   * @param decimalPointChar  the character to use for the decimal point
   */
  private def this(zeroChar: Char, positiveSignChar: Char, negativeSignChar: Char, decimalPointChar: Char) {

    this.zeroDigit = zeroChar
    this.positiveSign = positiveSignChar
    this.negativeSign = negativeSignChar
    this.decimalSeparator = decimalPointChar
  }

  /**
   * Gets the character that represents zero.
   * <p>
   * The character used to represent digits may vary by culture.
   * This method specifies the zero character to use, which implies the characters for one to nine.
   *
   * @return the character for zero
   */
  def getZeroDigit: Char = {
     zeroDigit
  }

  /**
   * Returns a copy of the info with a new character that represents zero.
   * <p>
   * The character used to represent digits may vary by culture.
   * This method specifies the zero character to use, which implies the characters for one to nine.
   *
   * @param zeroDigit  the character for zero
   * @return  a copy with a new character that represents zero, not null

   */
  def withZeroDigit(zeroDigit: Char): DecimalStyle = {
    if (zeroDigit == this.zeroDigit) {
       this
    }
     new DecimalStyle(zeroDigit, positiveSign, negativeSign, decimalSeparator)
  }

  /**
   * Gets the character that represents the positive sign.
   * <p>
   * The character used to represent a positive number may vary by culture.
   * This method specifies the character to use.
   *
   * @return the character for the positive sign
   */
  def getPositiveSign: Char = {
     positiveSign
  }

  /**
   * Returns a copy of the info with a new character that represents the positive sign.
   * <p>
   * The character used to represent a positive number may vary by culture.
   * This method specifies the character to use.
   *
   * @param positiveSign  the character for the positive sign
   * @return  a copy with a new character that represents the positive sign, not null
   */
  def withPositiveSign(positiveSign: Char): DecimalStyle = {
    if (positiveSign == this.positiveSign) {
       this
    }
     new DecimalStyle(zeroDigit, positiveSign, negativeSign, decimalSeparator)
  }

  /**
   * Gets the character that represents the negative sign.
   * <p>
   * The character used to represent a negative number may vary by culture.
   * This method specifies the character to use.
   *
   * @return the character for the negative sign
   */
  def getNegativeSign: Char = {
     negativeSign
  }

  /**
   * Returns a copy of the info with a new character that represents the negative sign.
   * <p>
   * The character used to represent a negative number may vary by culture.
   * This method specifies the character to use.
   *
   * @param negativeSign  the character for the negative sign
   * @return  a copy with a new character that represents the negative sign, not null
   */
  def withNegativeSign(negativeSign: Char): DecimalStyle = {
    if (negativeSign == this.negativeSign) {
       this
    }
     new DecimalStyle(zeroDigit, positiveSign, negativeSign, decimalSeparator)
  }

  /**
   * Gets the character that represents the decimal point.
   * <p>
   * The character used to represent a decimal point may vary by culture.
   * This method specifies the character to use.
   *
   * @return the character for the decimal point
   */
  def getDecimalSeparator: Char = {
     decimalSeparator
  }

  /**
   * Returns a copy of the info with a new character that represents the decimal point.
   * <p>
   * The character used to represent a decimal point may vary by culture.
   * This method specifies the character to use.
   *
   * @param decimalSeparator  the character for the decimal point
   * @return  a copy with a new character that represents the decimal point, not null
   */
  def withDecimalSeparator(decimalSeparator: Char): DecimalStyle = {
    if (decimalSeparator == this.decimalSeparator) {
       this
    }
     new DecimalStyle(zeroDigit, positiveSign, negativeSign, decimalSeparator)
  }

  /**
   * Checks whether the character is a digit, based on the currently set zero character.
   *
   * @param ch  the character to check
   * @return the value, 0 to 9, of the character, or -1 if not a digit
   */
  private[format] def convertToDigit(ch: Char): Int = {
    val `val`: Int = ch - zeroDigit
     if ((`val` >= 0 && `val` <= 9)) `val` else -1
  }

  /**
   * Converts the input numeric text to the internationalized form using the zero character.
   *
   * @param numericText  the text, consisting of digits 0 to 9, to convert, not null
   * @return the internationalized text, not null
   */
  private[format] def convertNumberToI18N(numericText: String): String = {
    if (zeroDigit == '0') {
       numericText
    }
    val diff: Int = zeroDigit - '0'
    val array: Array[Char] = numericText.toCharArray
    {
      var i: Int = 0
      while (i < array.length) {
        {
          array(i) = (array(i) + diff).asInstanceOf[Char]
        }
        ({
          i += 1; i - 1
        })
      }
    }
     new String(array)
  }

  /**
   * Checks if this DecimalStyle is equal another DecimalStyle.
   *
   * @param obj  the object to check, null returns false
   * @return true if this is equal to the other date
   */
  override def equals(obj: AnyRef): Boolean = {
    if (this eq obj) {
       true
    }
    if (obj.isInstanceOf[DecimalStyle]) {
      val other: DecimalStyle = obj.asInstanceOf[DecimalStyle]
       (zeroDigit == other.zeroDigit && positiveSign == other.positiveSign && negativeSign == other.negativeSign && decimalSeparator == other.decimalSeparator)
    }
     false
  }

  /**
   * A hash code for this DecimalStyle.
   *
   * @return a suitable hash code
   */
  override def hashCode: Int = {
     zeroDigit + positiveSign + negativeSign + decimalSeparator
  }

  /**
   * Returns a string describing this DecimalStyle.
   *
   * @return a string description, not null
   */
  override def toString: String = {
     "DecimalStyle[" + zeroDigit + positiveSign + negativeSign + decimalSeparator + "]"
  }

  /**
   * The zero digit.
   */
  private final val zeroDigit: Char = 0
  /**
   * The positive sign.
   */
  private final val positiveSign: Char = 0
  /**
   * The negative sign.
   */
  private final val negativeSign: Char = 0
  /**
   * The decimal separator.
   */
  private final val decimalSeparator: Char = 0
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
 * Copyright (c) 2008-2012, Stephen Colebourne & Michael Nascimento Santos
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
 * Enumeration of the style of a localized date, time or date-time formatter.
 * <p>
 * These styles are used when obtaining a date-time style from configuration.
 * See {@link DateTimeFormatter} and {@link DateTimeFormatterBuilder} for usage.
 *
 * @implSpec
 * This is an immutable and thread-safe enum.
 *
 * @since 1.8
 */
object FormatStyle {
  /**
   * Full text style, with the most detail.
   * For example, the format might be 'Tuesday, April 12, 1952 AD' or '3:30:42pm PST'.
   */
  final val FULL: = null
  /**
   * Long text style, with lots of detail.
   * For example, the format might be 'January 12, 1952'.
   */
  final val LONG: = null
  /**
   * Medium text style, with some detail.
   * For example, the format might be 'Jan 12, 1952'.
   */
  final val MEDIUM: = null
  /**
   * Short text style, typically numeric.
   * For example, the format might be '12.13.52' or '3:30pm'.
   */
  final val SHORT: = null
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
 * Copyright (c) 2008-2013, Stephen Colebourne & Michael Nascimento Santos
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
 * A store of parsed data.
 * <p>
 * This class is used during parsing to collect the data. Part of the parsing process
 * involves handling optional blocks and multiple copies of the data get created to
 * support the necessary backtracking.
 * <p>
 * Once parsing is completed, this class can be used as the resultant {@code TemporalAccessor}.
 * In most cases, it is only exposed once the fields have been resolved.
 *
 * @implSpec
 * This class is a mutable context intended for use from a single thread.
 * Usage of the class is thread-safe within standard parsing as a new instance of this class
 * is automatically created for each parse and parsing is single-threaded
 *
 * @since 1.8
 */
final class Parsed extends TemporalAccessor {
  /**
   * Creates an instance.
   */
  private[format] def  {

  }

  /**
   * Creates a copy.
   */
  private[format] def copy: Parsed = {
    val cloned: Parsed = new Parsed
    cloned.fieldValues.putAll(this.fieldValues)
    cloned.zone = this.zone
    cloned.chrono = this.chrono
    cloned.leapSecond = this.leapSecond
     cloned
  }

  def isSupported(field: TemporalField): Boolean = {
    if (fieldValues.containsKey(field) || (date != null && date.isSupported(field)) || (time != null && time.isSupported(field))) {
       true
    }
     field != null && (field.isInstanceOf[ChronoField] == false) && field.isSupportedBy(this)
  }

  def getLong(field: TemporalField): Long = {

    val value: Long = fieldValues.get(field)
    if (value != null) {
       value
    }
    if (date != null && date.isSupported(field)) {
       date.getLong(field)
    }
    if (time != null && time.isSupported(field)) {
       time.getLong(field)
    }
    if (field.isInstanceOf[ChronoField]) {
      throw new Nothing("Unsupported field: " + field)
    }
     field.getFrom(this)
  }

  override def query(query: TemporalQuery[R]): R = {
    if (query eq TemporalQuery.zoneId) {
       zone.asInstanceOf[R]
    }
    else if (query eq TemporalQuery.chronology) {
       chrono.asInstanceOf[R]
    }
    else if (query eq TemporalQuery.localDate) {
       (if (date != null) LocalDate.from(date) else null).asInstanceOf[R]
    }
    else if (query eq TemporalQuery.localTime) {
       time.asInstanceOf[R]
    }
    else if (query eq TemporalQuery.zone || query eq TemporalQuery.offset) {
       query.queryFrom(this)
    }
    else if (query eq TemporalQuery.precision) {
       null
    }
     query.queryFrom(this)
  }

  /**
   * Resolves the fields in this context.
   *
   * @param resolverStyle  the resolver style, not null
   * @param resolverFields  the fields to use for resolving, null for all fields
   * @return this, for method chaining
   * @throws DateTimeException if resolving one field results in a value for
   *                           another field that is in conflict
   */
  private[format] def resolve(resolverStyle: ResolverStyle, resolverFields: Set[TemporalField]): TemporalAccessor = {
    if (resolverFields != null) {
      fieldValues.keySet.retainAll(resolverFields)
    }
    this.resolverStyle = resolverStyle
    chrono = effectiveChrono
    resolveFields
    resolveTimeLenient
    crossCheck
    resolvePeriod
     this
  }

  private def resolveFields {
    resolveDateFields
    resolveTimeFields
    if (fieldValues.size > 0) {
      var changedCount: Int = 0
      while (changedCount < 50) {
        import scala.collection.JavaConversions._
        for (entry <- fieldValues.entrySet) {
          val targetField: TemporalField = entry.getKey
          var resolvedObject: TemporalAccessor = targetField.resolve(fieldValues, chrono, zone, resolverStyle)
          if (resolvedObject != null) {
            if (resolvedObject.isInstanceOf[Nothing]) {
              val czdt: Nothing = resolvedObject.asInstanceOf[Nothing]
              if ((zone == czdt.getZone) == false) {
                throw new DateTimeException("ChronoZonedDateTime must use the effective parsed zone: " + zone)
              }
              resolvedObject = czdt.toLocalDateTime
            }
            if (resolvedObject.isInstanceOf[Nothing]) {
              val cldt: Nothing = resolvedObject.asInstanceOf[Nothing]
              updateCheckConflict(cldt.toLocalTime, Period.ZERO)
              updateCheckConflict(cldt.toLocalDate)
              changedCount += 1
              continue //todo: continue is not supported
            }
            if (resolvedObject.isInstanceOf[Nothing]) {
              updateCheckConflict(resolvedObject.asInstanceOf[Nothing])
              changedCount += 1
              continue //todo: continue is not supported
            }
            if (resolvedObject.isInstanceOf[Nothing]) {
              updateCheckConflict(resolvedObject.asInstanceOf[Nothing], Period.ZERO)
              changedCount += 1
              continue //todo: continue is not supported
            }
            throw new DateTimeException("Method resolveFields() can only return ChronoZonedDateTime," + "ChronoLocalDateTime, ChronoLocalDate or Time")
          }
          else if (fieldValues.containsKey(targetField) == false) {
            changedCount += 1
            continue //todo: continue is not supported
          }
        }
        break //todo: break is not supported
      } //todo: labels is not supported
      if (changedCount == 50) {
        throw new DateTimeException("One of the parsed fields has an incorrectly implemented resolve method")
      }
      if (changedCount > 0) {
        resolveDateFields
        resolveTimeFields
      }
    }
  }

  private def updateCheckConflict(targetField: TemporalField, changeField: TemporalField, changeValue: Long) {
    val old: Long = fieldValues.put(changeField, changeValue)
    if (old != null && old.longValue != changeValue.longValue) {
      throw new DateTimeException("Conflict found: " + changeField + " " + old + " differs from " + changeField + " " + changeValue + " while resolving  " + targetField)
    }
  }

  private def resolveDateFields {
    updateCheckConflict(chrono.resolveDate(fieldValues, resolverStyle))
  }

  private def updateCheckConflict(cld: Nothing) {
    if (date != null) {
      if (cld != null && (date == cld) eq false) {
        throw new DateTimeException("Conflict found: Fields resolved to two different dates: " + date + " " + cld)
      }
    }
    else if (cld != null) {
      if ((chrono == cld.getChronology) == false) {
        throw new DateTimeException("ChronoLocalDate must use the effective parsed chronology: " + chrono)
      }
      date = cld
    }
  }

  private def resolveTimeFields {
    if (fieldValues.containsKey(CLOCK_HOUR_OF_DAY)) {
      val ch: Long = fieldValues.remove(CLOCK_HOUR_OF_DAY)
      if (resolverStyle eq ResolverStyle.STRICT || (resolverStyle eq ResolverStyle.SMART && ch != 0)) {
        CLOCK_HOUR_OF_DAY.checkValidValue(ch)
      }
      updateCheckConflict(CLOCK_HOUR_OF_DAY, HOUR_OF_DAY, if (ch == 24) 0 else ch)
    }
    if (fieldValues.containsKey(CLOCK_HOUR_OF_AMPM)) {
      val ch: Long = fieldValues.remove(CLOCK_HOUR_OF_AMPM)
      if (resolverStyle eq ResolverStyle.STRICT || (resolverStyle eq ResolverStyle.SMART && ch != 0)) {
        CLOCK_HOUR_OF_AMPM.checkValidValue(ch)
      }
      updateCheckConflict(CLOCK_HOUR_OF_AMPM, HOUR_OF_AMPM, if (ch == 12) 0 else ch)
    }
    if (fieldValues.containsKey(AMPM_OF_DAY) && fieldValues.containsKey(HOUR_OF_AMPM)) {
      val ap: Long = fieldValues.remove(AMPM_OF_DAY)
      val hap: Long = fieldValues.remove(HOUR_OF_AMPM)
      if (resolverStyle eq ResolverStyle.LENIENT) {
        updateCheckConflict(AMPM_OF_DAY, HOUR_OF_DAY, Math.addExact(Math.multiplyExact(ap, 12), hap))
      }
      else {
        AMPM_OF_DAY.checkValidValue(ap)
        HOUR_OF_AMPM.checkValidValue(ap)
        updateCheckConflict(AMPM_OF_DAY, HOUR_OF_DAY, ap * 12 + hap)
      }
    }
    if (fieldValues.containsKey(NANO_OF_DAY)) {
      val nod: Long = fieldValues.remove(NANO_OF_DAY)
      if (resolverStyle ne ResolverStyle.LENIENT) {
        NANO_OF_DAY.checkValidValue(nod)
      }
      updateCheckConflict(NANO_OF_DAY, HOUR_OF_DAY, nod / 3600 _000_000_000L)
      updateCheckConflict(NANO_OF_DAY, MINUTE_OF_HOUR, (nod / 60 _000_000_000L) % 60)
      updateCheckConflict(NANO_OF_DAY, SECOND_OF_MINUTE, (nod / 1 _000_000_000L) % 60)
      updateCheckConflict(NANO_OF_DAY, NANO_OF_SECOND, nod % 1 _000_000_000L)
    }
    if (fieldValues.containsKey(MICRO_OF_DAY)) {
      val cod: Long = fieldValues.remove(MICRO_OF_DAY)
      if (resolverStyle ne ResolverStyle.LENIENT) {
        MICRO_OF_DAY.checkValidValue(cod)
      }
      updateCheckConflict(MICRO_OF_DAY, SECOND_OF_DAY, cod / 1 _000_000L)
      updateCheckConflict(MICRO_OF_DAY, MICRO_OF_SECOND, cod % 1 _000_000L)
    }
    if (fieldValues.containsKey(MILLI_OF_DAY)) {
      val lod: Long = fieldValues.remove(MILLI_OF_DAY)
      if (resolverStyle ne ResolverStyle.LENIENT) {
        MILLI_OF_DAY.checkValidValue(lod)
      }
      updateCheckConflict(MILLI_OF_DAY, SECOND_OF_DAY, lod / 1 _000)
      updateCheckConflict(MILLI_OF_DAY, MILLI_OF_SECOND, lod % 1 _000)
    }
    if (fieldValues.containsKey(SECOND_OF_DAY)) {
      val sod: Long = fieldValues.remove(SECOND_OF_DAY)
      if (resolverStyle ne ResolverStyle.LENIENT) {
        SECOND_OF_DAY.checkValidValue(sod)
      }
      updateCheckConflict(SECOND_OF_DAY, HOUR_OF_DAY, sod / 3600)
      updateCheckConflict(SECOND_OF_DAY, MINUTE_OF_HOUR, (sod / 60) % 60)
      updateCheckConflict(SECOND_OF_DAY, SECOND_OF_MINUTE, sod % 60)
    }
    if (fieldValues.containsKey(MINUTE_OF_DAY)) {
      val mod: Long = fieldValues.remove(MINUTE_OF_DAY)
      if (resolverStyle ne ResolverStyle.LENIENT) {
        MINUTE_OF_DAY.checkValidValue(mod)
      }
      updateCheckConflict(MINUTE_OF_DAY, HOUR_OF_DAY, mod / 60)
      updateCheckConflict(MINUTE_OF_DAY, MINUTE_OF_HOUR, mod % 60)
    }
    if (fieldValues.containsKey(NANO_OF_SECOND)) {
      var nos: Long = fieldValues.get(NANO_OF_SECOND)
      if (resolverStyle ne ResolverStyle.LENIENT) {
        NANO_OF_SECOND.checkValidValue(nos)
      }
      if (fieldValues.containsKey(MICRO_OF_SECOND)) {
        val cos: Long = fieldValues.remove(MICRO_OF_SECOND)
        if (resolverStyle ne ResolverStyle.LENIENT) {
          MICRO_OF_SECOND.checkValidValue(cos)
        }
        nos = cos * 1000 + (nos % 1000)
        updateCheckConflict(MICRO_OF_SECOND, NANO_OF_SECOND, nos)
      }
      if (fieldValues.containsKey(MILLI_OF_SECOND)) {
        val los: Long = fieldValues.remove(MILLI_OF_SECOND)
        if (resolverStyle ne ResolverStyle.LENIENT) {
          MILLI_OF_SECOND.checkValidValue(los)
        }
        updateCheckConflict(MILLI_OF_SECOND, NANO_OF_SECOND, los * 1 _000_000L +(nos % 1 _000_000L))
      }
    }
    if (fieldValues.containsKey(HOUR_OF_DAY) && fieldValues.containsKey(MINUTE_OF_HOUR) && fieldValues.containsKey(SECOND_OF_MINUTE) && fieldValues.containsKey(NANO_OF_SECOND)) {
      val hod: Long = fieldValues.remove(HOUR_OF_DAY)
      val moh: Long = fieldValues.remove(MINUTE_OF_HOUR)
      val som: Long = fieldValues.remove(SECOND_OF_MINUTE)
      val nos: Long = fieldValues.remove(NANO_OF_SECOND)
      resolveTime(hod, moh, som, nos)
    }
  }

  private def resolveTimeLenient {
    if (time == null) {
      if (fieldValues.containsKey(MILLI_OF_SECOND)) {
        val los: Long = fieldValues.remove(MILLI_OF_SECOND)
        if (fieldValues.containsKey(MICRO_OF_SECOND)) {
          val cos: Long = los * 1 _000 +(fieldValues.get(MICRO_OF_SECOND) % 1 _000)
          updateCheckConflict(MILLI_OF_SECOND, MICRO_OF_SECOND, cos)
          fieldValues.remove(MICRO_OF_SECOND)
          fieldValues.put(NANO_OF_SECOND, cos * 1 _000L)
        }
        else {
          fieldValues.put(NANO_OF_SECOND, los * 1 _000_000L)
        }
      }
      else if (fieldValues.containsKey(MICRO_OF_SECOND)) {
        val cos: Long = fieldValues.remove(MICRO_OF_SECOND)
        fieldValues.put(NANO_OF_SECOND, cos * 1 _000L)
      }
      val hod: Long = fieldValues.get(HOUR_OF_DAY)
      if (hod != null) {
        val moh: Long = fieldValues.get(MINUTE_OF_HOUR)
        val som: Long = fieldValues.get(SECOND_OF_MINUTE)
        val nos: Long = fieldValues.get(NANO_OF_SECOND)
        if ((moh == null && (som != null || nos != null)) || (moh != null && som == null && nos != null)) {

        }
        val mohVal: Long = (if (moh != null) moh else 0)
        val somVal: Long = (if (som != null) som else 0)
        val nosVal: Long = (if (nos != null) nos else 0)
        resolveTime(hod, mohVal, somVal, nosVal)
        fieldValues.remove(HOUR_OF_DAY)
        fieldValues.remove(MINUTE_OF_HOUR)
        fieldValues.remove(SECOND_OF_MINUTE)
        fieldValues.remove(NANO_OF_SECOND)
      }
    }
    if (resolverStyle ne ResolverStyle.LENIENT && fieldValues.size > 0) {
      import scala.collection.JavaConversions._
      for (entry <- fieldValues.entrySet) {
        val field: TemporalField = entry.getKey
        if (field.isInstanceOf[ChronoField] && field.isTimeBased) {
          (field.asInstanceOf[ChronoField]).checkValidValue(entry.getValue)
        }
      }
    }
  }

  private def resolveTime(hod: Long, moh: Long, som: Long, nos: Long) {
    if (resolverStyle eq ResolverStyle.LENIENT) {
      var totalNanos: Long = Math.multiplyExact(hod, 3600 _000_000_000L)
      totalNanos = Math.addExact(totalNanos, Math.multiplyExact(moh, 60 _000_000_000L))
      totalNanos = Math.addExact(totalNanos, Math.multiplyExact(som, 1 _000_000_000L))
      totalNanos = Math.addExact(totalNanos, nos)
      val excessDays: Int = Math.floorDiv(totalNanos, 86400 _000_000_000L).asInstanceOf[Int]
      val nod: Long = Math.floorMod(totalNanos, 86400 _000_000_000L)
      updateCheckConflict(LocalTime.ofNanoOfDay(nod), Period.ofDays(excessDays))
    }
    else {
      val mohVal: Int = MINUTE_OF_HOUR.checkValidIntValue(moh)
      val nosVal: Int = NANO_OF_SECOND.checkValidIntValue(nos)
      if (resolverStyle eq ResolverStyle.SMART && hod == 24 && mohVal == 0 && som == 0 && nosVal == 0) {
        updateCheckConflict(LocalTime.MIDNIGHT, Period.ofDays(1))
      }
      else {
        val hodVal: Int = HOUR_OF_DAY.checkValidIntValue(hod)
        val somVal: Int = SECOND_OF_MINUTE.checkValidIntValue(som)
        updateCheckConflict(LocalTime.of(hodVal, mohVal, somVal, nosVal), Period.ZERO)
      }
    }
  }

  private def resolvePeriod {
    if (date != null && time != null && excessDays.isZero == false) {
      date = date.plus(excessDays)
      excessDays = Period.ZERO
    }
  }

  private def updateCheckConflict(timeToSet: Nothing, periodToSet: Period) {
    if (time != null) {
      if ((time == timeToSet) eq false) {
        throw new DateTimeException("Conflict found: Fields resolved to different times: " + time + " " + timeToSet)
      }
      if (excessDays.isZero == false && periodToSet.isZero == false && (excessDays == periodToSet) == false) {
        throw new DateTimeException("Conflict found: Fields resolved to different excess periods: " + excessDays + " " + periodToSet)
      }
      else {
        excessDays = periodToSet
      }
    }
    else {
      time = timeToSet
      excessDays = periodToSet
    }
  }

  private def crossCheck {
    if (date != null) {
      crossCheck(date)
    }
    if (time != null) {
      crossCheck(time)
      if (date != null && fieldValues.size > 0) {
        crossCheck(date.atTime(time))
      }
    }
  }

  private def crossCheck(target: TemporalAccessor) {
    {
      val it: Nothing = fieldValues.entrySet.iterator
      while (it.hasNext) {
        val entry: Nothing = it.next
        val field: TemporalField = entry.getKey
        var val1: Long = 0L
        try {
          val1 = target.getLong(field)
        }
        catch {
          case ex: RuntimeException => {
            continue //todo: continue is not supported
          }
        }
        val val2: Long = entry.getValue
        if (val1 != val2) {
          throw new DateTimeException("Conflict found: Field " + field + " " + val1 + " differs from " + field + " " + val2 + " derived from " + target)
        }
        it.remove
      }
    }
  }

  override def toString: String = {
    var str: String = fieldValues.toString + "," + chrono + "," + zone
    if (date != null || time != null) {
      str += " resolved to " + date + "," + time
    }
     str
  }

  /**
   * The parsed fields.
   */
  private[format] final val fieldValues: Map[TemporalField, Long] = new HashMap[TemporalField, Long]
  /**
   * The parsed zone.
   */
  private[format] var zone: ZoneId = null
  /**
   * The parsed chronology.
   */
  private[format] var chrono: Chronology = null
  /**
   * Whether a leap-second is parsed.
   */
  private[format] var leapSecond: Boolean = false
  /**
   * The effective chronology.
   */
  private[format] var effectiveChrono: Chronology = null
  /**
   * The resolver style to use.
   */
  private var resolverStyle: ResolverStyle = null
  /**
   * The resolved date.
   */
  private var date: Nothing = null
  /**
   * The resolved time.
   */
  private var time: Nothing = null
  /**
   * The excess period from time-only parsing.
   */
  private[format] var excessDays: Period = Period.ZERO
}

/*
 * Copyright (c) 2013, Oracle and/or its affiliates. All rights reserved.
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
 * Copyright (c) 2008-2013, Stephen Colebourne & Michael Nascimento Santos
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
 * Enumeration of different ways to resolve dates and times.
 * <p>
 * Parsing a text string occurs in two phases.
 * Phase 1 is a basic text parse according to the fields added to the builder.
 * Phase 2 resolves the parsed field-value pairs into date and/or time objects.
 * This style is used to control how phase 2, resolving, happens.
 *
 * @implSpec
 * This is an immutable and thread-safe enum.
 *
 * @since 1.8
 */
object ResolverStyle {
  /**
   * Style to resolve dates and times strictly.
   * <p>
   * Using strict resolution will ensure that all parsed values are within
   * the outer range of valid values for the field. Individual fields may
   * be further processed for strictness.
   * <p>
   * For example, resolving year-month and day-of-month in the ISO calendar
   * system using strict mode will ensure that the day-of-month is valid
   * for the year-month, rejecting invalid values.
   */
  final val STRICT: = null
  /**
   * Style to resolve dates and times in a smart, or intelligent, manner.
   * <p>
   * Using smart resolution will perform the sensible default for each
   * field, which may be the same as strict, the same as lenient, or a third
   * behavior. Individual fields will interpret this differently.
   * <p>
   * For example, resolving year-month and day-of-month in the ISO calendar
   * system using smart mode will ensure that the day-of-month is from
   * 1 to 31, converting any value beyond the last valid day-of-month to be
   * the last valid day-of-month.
   */
  final val SMART: = null
  /**
   * Style to resolve dates and times leniently.
   * <p>
   * Using lenient resolution will resolve the values in an appropriate
   * lenient manner. Individual fields will interpret this differently.
   * <p>
   * For example, lenient mode allows the month in the ISO calendar system
   * to be outside the range 1 to 12.
   * For example, month 15 is treated as being 3 months after month 12.
   */
  final val LENIENT: = null
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
 * Copyright (c) 2008-2012, Stephen Colebourne & Michael Nascimento Santos
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
 * Enumeration of ways to handle the positive/negative sign.
 * <p>
 * The formatting engine allows the positive and negative signs of numbers
 * to be controlled using this enum.
 * See {@link DateTimeFormatterBuilder} for usage.
 *
 * @implSpec
 * This is an immutable and thread-safe enum.
 *
 * @since 1.8
 */
object SignStyle {
  /**
   * Style to output the sign only if the value is negative.
   * <p>
   * In strict parsing, the negative sign will be accepted and the positive sign rejected.
   * In lenient parsing, any sign will be accepted.
   */
  final val NORMAL: = null
  /**
   * Style to always output the sign, where zero will output '+'.
   * <p>
   * In strict parsing, the absence of a sign will be rejected.
   * In lenient parsing, any sign will be accepted, with the absence
   * of a sign treated as a positive number.
   */
  final val ALWAYS: = null
  /**
   * Style to never output sign, only outputting the absolute value.
   * <p>
   * In strict parsing, any sign will be rejected.
   * In lenient parsing, any sign will be accepted unless the width is fixed.
   */
  final val NEVER: = null
  /**
   * Style to block negative values, throwing an exception on printing.
   * <p>
   * In strict parsing, any sign will be rejected.
   * In lenient parsing, any sign will be accepted unless the width is fixed.
   */
  final val NOT_NEGATIVE: = null
  /**
   * Style to always output the sign if the value exceeds the pad width.
   * A negative value will always output the '-' sign.
   * <p>
   * In strict parsing, the sign will be rejected unless the pad width is exceeded.
   * In lenient parsing, any sign will be accepted, with the absence
   * of a sign treated as a positive number.
   */
  final val EXCEEDS_PAD: = null
}

final class SignStyle {
  /**
   * Parse helper.
   *
   * @param positive  true if positive sign parsed, false for negative sign
   * @param strict  true if strict, false if lenient
   * @param fixedWidth  true if fixed width, false if not
   * @return
   */
  private[format] def parse(positive: Boolean, strict: Boolean, fixedWidth: Boolean): Boolean = {
    ordinal match {
      case 0 =>
         !positive || !strict
      case 1 =>
      case 4 =>
         true
      case _ =>
         !strict && !fixedWidth
    }
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
 * This file is available under and governed by the GNU General Public
 * License version 2 only, as published by the Free Software Foundation.
 * However, the following notice accompanied the original version of this
 * file:
 *
 * Copyright (c) 2008-2012, Stephen Colebourne & Michael Nascimento Santos
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
 * Enumeration of the style of text formatting and parsing.
 * <p>
 * Text styles define three sizes for the formatted text - 'full', 'short' and 'narrow'.
 * Each of these three sizes is available in both 'standard' and 'stand-alone' variations.
 * <p>
 * The difference between the three sizes is obvious in most languages.
 * For example, in English the 'full' month is 'January', the 'short' month is 'Jan'
 * and the 'narrow' month is 'J'. Note that the narrow size is often not unique.
 * For example, 'January', 'June' and 'July' all have the 'narrow' text 'J'.
 * <p>
 * The difference between the 'standard' and 'stand-alone' forms is trickier to describe
 * as there is no difference in English. However, in other languages there is a difference
 * in the word used when the text is used alone, as opposed to in a complete date.
 * For example, the word used for a month when used alone in a date picker is different
 * to the word used for month in association with a day and year in a date.
 *
 * @implSpec
 * This is immutable and thread-safe enum.
 */
object TextStyle {
  /**
   * Full text, typically the full description.
   * For example, day-of-week Monday might output "Monday".
   */
  final val FULL: = null
  /**
   * Full text for stand-alone use, typically the full description.
   * For example, day-of-week Monday might output "Monday".
   */
  final val FULL_STANDALONE: = null
  /**
   * Short text, typically an abbreviation.
   * For example, day-of-week Monday might output "Mon".
   */
  final val SHORT: = null
  /**
   * Short text for stand-alone use, typically an abbreviation.
   * For example, day-of-week Monday might output "Mon".
   */
  final val SHORT_STANDALONE: = null
  /**
   * Narrow text, typically a single letter.
   * For example, day-of-week Monday might output "M".
   */
  final val NARROW: = null
  /**
   * Narrow text for stand-alone use, typically a single letter.
   * For example, day-of-week Monday might output "M".
   */
  final val NARROW_STANDALONE: = null
}

final class TextStyle {
  private def this(calendarStyle: Int, zoneNameStyleIndex: Int) {

    this.calendarStyle = calendarStyle
    this.zoneNameStyleIndex = zoneNameStyleIndex
  }

  /**
   * Returns true if the Style is a stand-alone style.
   * @return true if the style is a stand-alone style.
   */
  def isStandalone: Boolean = {
     (ordinal & 1) == 1
  }

  /**
   * Returns the stand-alone style with the same size.
   * @return the stand-alone style with the same size
   */
  def asStandalone: TextStyle = {
     TextStyle.values(ordinal | 1)
  }

  /**
   * Returns the normal style with the same size.
   *
   * @return the normal style with the same size
   */
  def asNormal: TextStyle = {
     TextStyle.values(ordinal & ~1)
  }

  /**
   * Returns the {@code Calendar} style corresponding to this {@code TextStyle}.
   *
   * @return the corresponding { @code Calendar} style
   */
  private[format] def toCalendarStyle: Int = {
     calendarStyle
  }

  /**
   * Returns the relative index value to an element of the {@link
   * java.text.DateFormatSymbols#getZoneStrings() DateFormatSymbols.getZoneStrings()}
   * value, 0 for long names and 1 for short names (abbreviations). Note that these values
   * do <em>not</em> correspond to the {@link java.util.TimeZone#LONG} and {@link
   * java.util.TimeZone#SHORT} values.
   *
   * @return the relative index value to time zone names array
   */
  private[format] def zoneNameStyleIndex: Int = {
     zoneNameStyleIndex
  }

  private final val calendarStyle: Int = 0
  private final val zoneNameStyleIndex: Int = 0
}

/*
 * Copyright (c) 2013, Oracle and/or its affiliates. All rights reserved.
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
/**
 * A helper class to map a zone name to metazone and back to the
 * appropriate zone id for the particular locale.
 * <p>
 * The zid<->metazone mappings are based on CLDR metaZones.xml.
 * The alias mappings are based on Link entries in tzdb data files.
 */
object ZoneName {
  def toZid(zid: String, locale: Locale): String = {
    var mzone: String = zidToMzone.get(zid)
    if (mzone == null && aliases.containsKey(zid)) {
      zid = aliases.get(zid)
      mzone = zidToMzone.get(zid)
    }
    if (mzone != null) {
      val map: Map[String, String] = mzoneToZidL.get(mzone)
      if (map != null && map.containsKey(locale.getCountry)) {
        zid = map.get(locale.getCountry)
      }
      else {
        zid = mzoneToZid.get(mzone)
      }
    }
     toZid(zid)
  }

  def toZid(zid: String): String = {
    if (aliases.containsKey(zid)) {
       aliases.get(zid)
    }
     zid
  }

  private final val zidMap: Array[String] = Array[String]("Pacific/Rarotonga", "Cook", "Pacific/Rarotonga", "Europe/Tirane", "Europe_Central", "Europe/Paris", "America/Recife", "Brasilia", "America/Sao_Paulo", "America/Argentina/San_Juan", "Argentina", "America/Buenos_Aires", "Asia/Kolkata", "India", "Asia/Calcutta", "America/Guayaquil", "Ecuador", "America/Guayaquil", "Europe/Samara", "Moscow", "Europe/Moscow", "Indian/Antananarivo", "Africa_Eastern", "Africa/Nairobi", "America/Santa_Isabel", "America_Pacific", "America/Los_Angeles", "America/Montserrat", "Atlantic", "America/Halifax", "Pacific/Port_Moresby", "Papua_New_Guinea", "Pacific/Port_Moresby", "Europe/Paris", "Europe_Central", "Europe/Paris", "America/Argentina/Salta", "Argentina", "America/Buenos_Aires", "Asia/Omsk", "Omsk", "Asia/Omsk", "Africa/Ceuta", "Europe_Central", "Europe/Paris", "America/Argentina/San_Luis", "Argentina_Western", "America/Argentina/San_Luis", "America/Atikokan", "America_Eastern", "America/New_York", "Asia/Vladivostok", "Vladivostok", "Asia/Vladivostok", "America/Argentina/Jujuy", "Argentina", "America/Buenos_Aires", "Asia/Almaty", "Kazakhstan_Eastern", "Asia/Almaty", "Atlantic/Canary", "Europe_Western", "Atlantic/Canary", "Asia/Bangkok", "Indochina", "Asia/Saigon", "America/Caracas", "Venezuela", "America/Caracas", "Australia/Hobart", "Australia_Eastern", "Australia/Sydney", "America/Havana", "Cuba", "America/Havana", "Africa/Malabo", "Africa_Western", "Africa/Lagos", "Australia/Lord_Howe", "Lord_Howe", "Australia/Lord_Howe", "Pacific/Fakaofo", "Tokelau", "Pacific/Fakaofo", "America/Matamoros", "America_Central", "America/Chicago", "America/Guadeloupe", "Atlantic", "America/Halifax", "Europe/Helsinki", "Europe_Eastern", "Europe/Bucharest", "Asia/Calcutta", "India", "Asia/Calcutta", "Africa/Kinshasa", "Africa_Western", "Africa/Lagos", "America/Miquelon", "Pierre_Miquelon", "America/Miquelon", "Europe/Athens", "Europe_Eastern", "Europe/Bucharest", "Asia/Novosibirsk", "Novosibirsk", "Asia/Novosibirsk", "Indian/Cocos", "Cocos", "Indian/Cocos", "Africa/Bujumbura", "Africa_Central", "Africa/Maputo", "Europe/Mariehamn", "Europe_Eastern", "Europe/Bucharest", "America/Winnipeg", "America_Central", "America/Chicago", "America/Buenos_Aires", "Argentina", "America/Buenos_Aires", "America/Yellowknife", "America_Mountain", "America/Denver", "Pacific/Midway", "Samoa", "Pacific/Apia", "Africa/Dar_es_Salaam", "Africa_Eastern", "Africa/Nairobi", "Pacific/Tahiti", "Tahiti", "Pacific/Tahiti", "Asia/Gaza", "Europe_Eastern", "Europe/Bucharest", "Australia/Lindeman", "Australia_Eastern", "Australia/Sydney", "Europe/Kaliningrad", "Europe_Eastern", "Europe/Bucharest", "Europe/Bucharest", "Europe_Eastern", "Europe/Bucharest", "America/Lower_Princes", "Atlantic", "America/Halifax", "Pacific/Chuuk", "Truk", "Pacific/Truk", "America/Anchorage", "Alaska", "America/Juneau", "America/Rankin_Inlet", "America_Central", "America/Chicago", "America/Marigot", "Atlantic", "America/Halifax", "Africa/Juba", "Africa_Eastern", "Africa/Nairobi", "Africa/Algiers", "Europe_Central", "Europe/Paris", "Europe/Kiev", "Europe_Eastern", "Europe/Bucharest", "America/Santarem", "Brasilia", "America/Sao_Paulo", "Africa/Brazzaville", "Africa_Western", "Africa/Lagos", "Asia/Choibalsan", "Choibalsan", "Asia/Choibalsan", "Indian/Christmas", "Christmas", "Indian/Christmas", "America/Nassau", "America_Eastern", "America/New_York", "Africa/Tunis", "Europe_Central", "Europe/Paris", "Pacific/Noumea", "New_Caledonia", "Pacific/Noumea", "Africa/El_Aaiun", "Europe_Western", "Atlantic/Canary", "Europe/Sarajevo", "Europe_Central", "Europe/Paris", "America/Campo_Grande", "Amazon", "America/Manaus", "America/Puerto_Rico", "Atlantic", "America/Halifax", "Antarctica/Mawson", "Mawson", "Antarctica/Mawson", "Pacific/Galapagos", "Galapagos", "Pacific/Galapagos", "Asia/Tehran", "Iran", "Asia/Tehran", "America/Port-au-Prince", "America_Eastern", "America/New_York", "America/Scoresbysund", "Greenland_Eastern", "America/Scoresbysund", "Africa/Harare", "Africa_Central", "Africa/Maputo", "America/Dominica", "Atlantic", "America/Halifax", "Europe/Chisinau", "Europe_Eastern", "Europe/Bucharest", "America/Chihuahua", "America_Mountain", "America/Denver", "America/La_Paz", "Bolivia", "America/La_Paz", "Indian/Chagos", "Indian_Ocean", "Indian/Chagos", "Australia/Broken_Hill", "Australia_Central", "Australia/Adelaide", "America/Grenada", "Atlantic", "America/Halifax", "America/North_Dakota/New_Salem", "America_Central", "America/Chicago", "Pacific/Majuro", "Marshall_Islands", "Pacific/Majuro", "Australia/Adelaide", "Australia_Central", "Australia/Adelaide", "Europe/Warsaw", "Europe_Central", "Europe/Paris", "Europe/Vienna", "Europe_Central", "Europe/Paris", "Atlantic/Cape_Verde", "Cape_Verde", "Atlantic/Cape_Verde", "America/Mendoza", "Argentina", "America/Buenos_Aires", "Pacific/Gambier", "Gambier", "Pacific/Gambier", "Europe/Istanbul", "Europe_Eastern", "Europe/Bucharest", "America/Kentucky/Monticello", "America_Eastern", "America/New_York", "America/Chicago", "America_Central", "America/Chicago", "Asia/Ulaanbaatar", "Mongolia", "Asia/Ulaanbaatar", "Indian/Maldives", "Maldives", "Indian/Maldives", "America/Mexico_City", "America_Central", "America/Chicago", "Africa/Asmara", "Africa_Eastern", "Africa/Nairobi", "Asia/Chongqing", "China", "Asia/Shanghai", "America/Argentina/La_Rioja", "Argentina", "America/Buenos_Aires", "America/Tijuana", "America_Pacific", "America/Los_Angeles", "Asia/Harbin", "China", "Asia/Shanghai", "Pacific/Honolulu", "Hawaii_Aleutian", "Pacific/Honolulu", "Atlantic/Azores", "Azores", "Atlantic/Azores", "Indian/Mayotte", "Africa_Eastern", "Africa/Nairobi", "America/Guatemala", "America_Central", "America/Chicago", "America/Indianapolis", "America_Eastern", "America/New_York", "America/Halifax", "Atlantic", "America/Halifax", "America/Resolute", "America_Central", "America/Chicago", "Europe/London", "GMT", "Atlantic/Reykjavik", "America/Hermosillo", "America_Mountain", "America/Denver", "Atlantic/Madeira", "Europe_Western", "Atlantic/Canary", "Europe/Zagreb", "Europe_Central", "Europe/Paris", "America/Boa_Vista", "Amazon", "America/Manaus", "America/Regina", "America_Central", "America/Chicago", "America/Cordoba", "Argentina", "America/Buenos_Aires", "America/Shiprock", "America_Mountain", "America/Denver", "Europe/Luxembourg", "Europe_Central", "Europe/Paris", "America/Cancun", "America_Central", "America/Chicago", "Pacific/Enderbury", "Phoenix_Islands", "Pacific/Enderbury", "Africa/Bissau", "GMT", "Atlantic/Reykjavik", "Antarctica/Vostok", "Vostok", "Antarctica/Vostok", "Pacific/Apia", "Samoa", "Pacific/Apia", "Australia/Perth", "Australia_Western", "Australia/Perth", "America/Juneau", "Alaska", "America/Juneau", "Africa/Mbabane", "Africa_Southern", "Africa/Johannesburg", "Pacific/Niue", "Niue", "Pacific/Niue", "Europe/Zurich", "Europe_Central", "Europe/Paris", "America/Rio_Branco", "Amazon", "America/Manaus", "Africa/Ndjamena", "Africa_Western", "Africa/Lagos", "Asia/Macau", "China", "Asia/Shanghai", "America/Lima", "Peru", "America/Lima", "Africa/Windhoek", "Africa_Western", "Africa/Lagos", "America/Sitka", "Alaska", "America/Juneau", "America/Mazatlan", "America_Mountain", "America/Denver", "Asia/Saigon", "Indochina", "Asia/Saigon", "Asia/Kamchatka", "Magadan", "Asia/Magadan", "America/Menominee", "America_Central", "America/Chicago", "America/Belize", "America_Central", "America/Chicago", "America/Sao_Paulo", "Brasilia", "America/Sao_Paulo", "America/Barbados", "Atlantic", "America/Halifax", "America/Porto_Velho", "Amazon", "America/Manaus", "America/Costa_Rica", "America_Central", "America/Chicago", "Europe/Monaco", "Europe_Central", "Europe/Paris", "Europe/Riga", "Europe_Eastern", "Europe/Bucharest", "Europe/Vatican", "Europe_Central", "Europe/Paris", "Europe/Madrid", "Europe_Central", "Europe/Paris", "Africa/Dakar", "GMT", "Atlantic/Reykjavik", "Asia/Damascus", "Europe_Eastern", "Europe/Bucharest", "Asia/Hong_Kong", "Hong_Kong", "Asia/Hong_Kong", "America/Adak", "Hawaii_Aleutian", "Pacific/Honolulu", "Europe/Vilnius", "Europe_Eastern", "Europe/Bucharest", "America/Indiana/Indianapolis", "America_Eastern", "America/New_York", "Africa/Freetown", "GMT", "Atlantic/Reykjavik", "Atlantic/Reykjavik", "GMT", "Atlantic/Reykjavik", "Asia/Ho_Chi_Minh", "Indochina", "Asia/Saigon", "America/St_Kitts", "Atlantic", "America/Halifax", "America/Martinique", "Atlantic", "America/Halifax", "America/Thule", "Atlantic", "America/Halifax", "America/Asuncion", "Paraguay", "America/Asuncion", "Africa/Luanda", "Africa_Western", "Africa/Lagos", "America/Monterrey", "America_Central", "America/Chicago", "Pacific/Fiji", "Fiji", "Pacific/Fiji", "Africa/Banjul", "GMT", "Atlantic/Reykjavik", "America/Grand_Turk", "America_Eastern", "America/New_York", "Pacific/Pitcairn", "Pitcairn", "Pacific/Pitcairn", "America/Montevideo", "Uruguay", "America/Montevideo", "America/Bahia_Banderas", "America_Central", "America/Chicago", "America/Cayman", "America_Eastern", "America/New_York", "Pacific/Norfolk", "Norfolk", "Pacific/Norfolk", "Africa/Ouagadougou", "GMT", "Atlantic/Reykjavik", "America/Maceio", "Brasilia", "America/Sao_Paulo", "Pacific/Guam", "Chamorro", "Pacific/Saipan", "Africa/Monrovia", "GMT", "Atlantic/Reykjavik", "Africa/Bamako", "GMT", "Atlantic/Reykjavik", "Asia/Colombo", "India", "Asia/Calcutta", "Asia/Urumqi", "China", "Asia/Shanghai", "Asia/Kabul", "Afghanistan", "Asia/Kabul", "America/Yakutat", "Alaska", "America/Juneau", "America/Phoenix", "America_Mountain", "America/Denver", "Asia/Nicosia", "Europe_Eastern", "Europe/Bucharest", "Asia/Phnom_Penh", "Indochina", "Asia/Saigon", "America/Rainy_River", "America_Central", "America/Chicago", "Europe/Uzhgorod", "Europe_Eastern", "Europe/Bucharest", "Pacific/Saipan", "Chamorro", "Pacific/Saipan", "America/St_Vincent", "Atlantic", "America/Halifax", "Europe/Rome", "Europe_Central", "Europe/Paris", "America/Nome", "Alaska", "America/Juneau", "Africa/Mogadishu", "Africa_Eastern", "Africa/Nairobi", "Europe/Zaporozhye", "Europe_Eastern", "Europe/Bucharest", "Pacific/Funafuti", "Tuvalu", "Pacific/Funafuti", "Atlantic/South_Georgia", "South_Georgia", "Atlantic/South_Georgia", "Europe/Skopje", "Europe_Central", "Europe/Paris", "Asia/Yekaterinburg", "Yekaterinburg", "Asia/Yekaterinburg", "Australia/Melbourne", "Australia_Eastern", "Australia/Sydney", "America/Argentina/Cordoba", "Argentina", "America/Buenos_Aires", "Africa/Kigali", "Africa_Central", "Africa/Maputo", "Africa/Blantyre", "Africa_Central", "Africa/Maputo", "Africa/Tripoli", "Europe_Eastern", "Europe/Bucharest", "Africa/Gaborone", "Africa_Central", "Africa/Maputo", "Asia/Kuching", "Malaysia", "Asia/Kuching", "Pacific/Nauru", "Nauru", "Pacific/Nauru", "America/Aruba", "Atlantic", "America/Halifax", "America/Antigua", "Atlantic", "America/Halifax", "Europe/Volgograd", "Volgograd", "Europe/Volgograd", "Africa/Djibouti", "Africa_Eastern", "Africa/Nairobi", "America/Catamarca", "Argentina", "America/Buenos_Aires", "Asia/Manila", "Philippines", "Asia/Manila", "Pacific/Kiritimati", "Line_Islands", "Pacific/Kiritimati", "Asia/Shanghai", "China", "Asia/Shanghai", "Pacific/Truk", "Truk", "Pacific/Truk", "Pacific/Tarawa", "Gilbert_Islands", "Pacific/Tarawa", "Africa/Conakry", "GMT", "Atlantic/Reykjavik", "Asia/Bishkek", "Kyrgystan", "Asia/Bishkek", "Europe/Gibraltar", "Europe_Central", "Europe/Paris", "Asia/Rangoon", "Myanmar", "Asia/Rangoon", "Asia/Baku", "Azerbaijan", "Asia/Baku", "America/Santiago", "Chile", "America/Santiago", "America/El_Salvador", "America_Central", "America/Chicago", "America/Noronha", "Noronha", "America/Noronha", "America/St_Thomas", "Atlantic", "America/Halifax", "Atlantic/St_Helena", "GMT", "Atlantic/Reykjavik", "Asia/Krasnoyarsk", "Krasnoyarsk", "Asia/Krasnoyarsk", "America/Vancouver", "America_Pacific", "America/Los_Angeles", "Europe/Belgrade", "Europe_Central", "Europe/Paris", "America/St_Barthelemy", "Atlantic", "America/Halifax", "Asia/Pontianak", "Indonesia_Western", "Asia/Jakarta", "Africa/Lusaka", "Africa_Central", "Africa/Maputo", "America/Godthab", "Greenland_Western", "America/Godthab", "Asia/Dhaka", "Bangladesh", "Asia/Dhaka", "Asia/Dubai", "Gulf", "Asia/Dubai", "Europe/Moscow", "Moscow", "Europe/Moscow", "America/Louisville", "America_Eastern", "America/New_York", "Australia/Darwin", "Australia_Central", "Australia/Adelaide", "America/Santo_Domingo", "Atlantic", "America/Halifax", "America/Argentina/Ushuaia", "Argentina", "America/Buenos_Aires", "America/Tegucigalpa", "America_Central", "America/Chicago", "Asia/Aden", "Arabian", "Asia/Riyadh", "America/Inuvik", "America_Mountain", "America/Denver", "Asia/Beirut", "Europe_Eastern", "Europe/Bucharest", "Asia/Qatar", "Arabian", "Asia/Riyadh", "Europe/Oslo", "Europe_Central", "Europe/Paris", "Asia/Anadyr", "Magadan", "Asia/Magadan", "Pacific/Palau", "Palau", "Pacific/Palau", "Arctic/Longyearbyen", "Europe_Central", "Europe/Paris", "America/Anguilla", "Atlantic", "America/Halifax", "Asia/Aqtau", "Kazakhstan_Western", "Asia/Aqtobe", "Asia/Yerevan", "Armenia", "Asia/Yerevan", "Africa/Lagos", "Africa_Western", "Africa/Lagos", "America/Denver", "America_Mountain", "America/Denver", "Antarctica/Palmer", "Chile", "America/Santiago", "Europe/Stockholm", "Europe_Central", "Europe/Paris", "America/Bahia", "Brasilia", "America/Sao_Paulo", "America/Danmarkshavn", "GMT", "Atlantic/Reykjavik", "Indian/Mauritius", "Mauritius", "Indian/Mauritius", "Pacific/Chatham", "Chatham", "Pacific/Chatham", "Europe/Prague", "Europe_Central", "Europe/Paris", "America/Blanc-Sablon", "Atlantic", "America/Halifax", "America/Bogota", "Colombia", "America/Bogota", "America/Managua", "America_Central", "America/Chicago", "Pacific/Auckland", "New_Zealand", "Pacific/Auckland", "Atlantic/Faroe", "Europe_Western", "Atlantic/Canary", "America/Cambridge_Bay", "America_Mountain", "America/Denver", "America/Los_Angeles", "America_Pacific", "America/Los_Angeles", "Africa/Khartoum", "Africa_Eastern", "Africa/Nairobi", "Europe/Simferopol", "Europe_Eastern", "Europe/Bucharest", "Australia/Currie", "Australia_Eastern", "Australia/Sydney", "Europe/Guernsey", "GMT", "Atlantic/Reykjavik", "Asia/Thimphu", "Bhutan", "Asia/Thimphu", "America/Eirunepe", "Amazon", "America/Manaus", "Africa/Nairobi", "Africa_Eastern", "Africa/Nairobi", "Asia/Yakutsk", "Yakutsk", "Asia/Yakutsk", "America/Goose_Bay", "Atlantic", "America/Halifax", "Africa/Maseru", "Africa_Southern", "Africa/Johannesburg", "America/Swift_Current", "America_Central", "America/Chicago", "America/Guyana", "Guyana", "America/Guyana", "Asia/Tokyo", "Japan", "Asia/Tokyo", "Indian/Kerguelen", "French_Southern", "Indian/Kerguelen", "America/Belem", "Brasilia", "America/Sao_Paulo", "Pacific/Wallis", "Wallis", "Pacific/Wallis", "America/Whitehorse", "America_Pacific", "America/Los_Angeles", "America/North_Dakota/Beulah", "America_Central", "America/Chicago", "Asia/Jerusalem", "Israel", "Asia/Jerusalem", "Antarctica/Syowa", "Syowa", "Antarctica/Syowa", "America/Thunder_Bay", "America_Eastern", "America/New_York", "Asia/Brunei", "Brunei", "Asia/Brunei", "America/Metlakatla", "America_Pacific", "America/Los_Angeles", "Asia/Dushanbe", "Tajikistan", "Asia/Dushanbe", "Pacific/Kosrae", "Kosrae", "Pacific/Kosrae", "America/Coral_Harbour", "America_Eastern", "America/New_York", "America/Tortola", "Atlantic", "America/Halifax", "Asia/Karachi", "Pakistan", "Asia/Karachi", "Indian/Reunion", "Reunion", "Indian/Reunion", "America/Detroit", "America_Eastern", "America/New_York", "Australia/Eucla", "Australia_CentralWestern", "Australia/Eucla", "Asia/Seoul", "Korea", "Asia/Seoul", "Asia/Singapore", "Singapore", "Asia/Singapore", "Africa/Casablanca", "Europe_Western", "Atlantic/Canary", "Asia/Dili", "East_Timor", "Asia/Dili", "America/Indiana/Vincennes", "America_Eastern", "America/New_York", "Europe/Dublin", "GMT", "Atlantic/Reykjavik", "America/St_Johns", "Newfoundland", "America/St_Johns", "Antarctica/Macquarie", "Macquarie", "Antarctica/Macquarie", "America/Port_of_Spain", "Atlantic", "America/Halifax", "Europe/Budapest", "Europe_Central", "Europe/Paris", "America/Fortaleza", "Brasilia", "America/Sao_Paulo", "Australia/Brisbane", "Australia_Eastern", "Australia/Sydney", "Atlantic/Bermuda", "Atlantic", "America/Halifax", "Asia/Amman", "Europe_Eastern", "Europe/Bucharest", "Asia/Tashkent", "Uzbekistan", "Asia/Tashkent", "Antarctica/DumontDUrville", "DumontDUrville", "Antarctica/DumontDUrville", "Antarctica/Casey", "Australia_Western", "Australia/Perth", "Asia/Vientiane", "Indochina", "Asia/Saigon", "Pacific/Johnston", "Hawaii_Aleutian", "Pacific/Honolulu", "America/Jamaica", "America_Eastern", "America/New_York", "Africa/Addis_Ababa", "Africa_Eastern", "Africa/Nairobi", "Pacific/Ponape", "Ponape", "Pacific/Ponape", "Europe/Jersey", "GMT", "Atlantic/Reykjavik", "Africa/Lome", "GMT", "Atlantic/Reykjavik", "America/Manaus", "Amazon", "America/Manaus", "Africa/Niamey", "Africa_Western", "Africa/Lagos", "Asia/Kashgar", "China", "Asia/Shanghai", "Pacific/Tongatapu", "Tonga", "Pacific/Tongatapu", "Europe/Minsk", "Europe_Eastern", "Europe/Bucharest", "America/Edmonton", "America_Mountain", "America/Denver", "Asia/Baghdad", "Arabian", "Asia/Riyadh", "Asia/Kathmandu", "Nepal", "Asia/Katmandu", "America/Ojinaga", "America_Mountain", "America/Denver", "Africa/Abidjan", "GMT", "Atlantic/Reykjavik", "America/Indiana/Winamac", "America_Eastern", "America/New_York", "Asia/Qyzylorda", "Kazakhstan_Eastern", "Asia/Almaty", "Australia/Sydney", "Australia_Eastern", "Australia/Sydney", "Asia/Ashgabat", "Turkmenistan", "Asia/Ashgabat", "Europe/Amsterdam", "Europe_Central", "Europe/Paris", "America/Dawson_Creek", "America_Mountain", "America/Denver", "Africa/Cairo", "Europe_Eastern", "Europe/Bucharest", "Asia/Pyongyang", "Korea", "Asia/Seoul", "Africa/Kampala", "Africa_Eastern", "Africa/Nairobi", "America/Araguaina", "Brasilia", "America/Sao_Paulo", "Asia/Novokuznetsk", "Novosibirsk", "Asia/Novosibirsk", "Pacific/Kwajalein", "Marshall_Islands", "Pacific/Majuro", "Africa/Lubumbashi", "Africa_Central", "Africa/Maputo", "Asia/Sakhalin", "Sakhalin", "Asia/Sakhalin", "America/Indiana/Vevay", "America_Eastern", "America/New_York", "Africa/Maputo", "Africa_Central", "Africa/Maputo", "Atlantic/Faeroe", "Europe_Western", "Atlantic/Canary", "America/North_Dakota/Center", "America_Central", "America/Chicago", "Pacific/Wake", "Wake", "Pacific/Wake", "Pacific/Pago_Pago", "Samoa", "Pacific/Apia", "America/Moncton", "Atlantic", "America/Halifax", "Africa/Sao_Tome", "GMT", "Atlantic/Reykjavik", "America/Glace_Bay", "Atlantic", "America/Halifax", "Asia/Jakarta", "Indonesia_Western", "Asia/Jakarta", "Africa/Asmera", "Africa_Eastern", "Africa/Nairobi", "Europe/Lisbon", "Europe_Western", "Atlantic/Canary", "America/Dawson", "America_Pacific", "America/Los_Angeles", "America/Cayenne", "French_Guiana", "America/Cayenne", "Asia/Bahrain", "Arabian", "Asia/Riyadh", "Europe/Malta", "Europe_Central", "Europe/Paris", "America/Indiana/Tell_City", "America_Central", "America/Chicago", "America/Indiana/Petersburg", "America_Eastern", "America/New_York", "Antarctica/Rothera", "Rothera", "Antarctica/Rothera", "Asia/Aqtobe", "Kazakhstan_Western", "Asia/Aqtobe", "Europe/Vaduz", "Europe_Central", "Europe/Paris", "America/Indiana/Marengo", "America_Eastern", "America/New_York", "Europe/Brussels", "Europe_Central", "Europe/Paris", "Europe/Andorra", "Europe_Central", "Europe/Paris", "America/Indiana/Knox", "America_Central", "America/Chicago", "Pacific/Easter", "Easter", "Pacific/Easter", "America/Argentina/Rio_Gallegos", "Argentina", "America/Buenos_Aires", "Asia/Oral", "Kazakhstan_Western", "Asia/Aqtobe", "Europe/Copenhagen", "Europe_Central", "Europe/Paris", "Africa/Johannesburg", "Africa_Southern", "Africa/Johannesburg", "Pacific/Pohnpei", "Ponape", "Pacific/Ponape", "America/Argentina/Tucuman", "Argentina", "America/Buenos_Aires", "America/Toronto", "America_Eastern", "America/New_York", "Asia/Makassar", "Indonesia_Central", "Asia/Makassar", "Europe/Berlin", "Europe_Central", "Europe/Paris", "America/Argentina/Mendoza", "Argentina", "America/Buenos_Aires", "America/Cuiaba", "Amazon", "America/Manaus", "America/Creston", "America_Mountain", "America/Denver", "Asia/Samarkand", "Uzbekistan", "Asia/Tashkent", "Asia/Hovd", "Hovd", "Asia/Hovd", "Europe/Bratislava", "Europe_Central", "Europe/Paris", "Africa/Accra", "GMT", "Atlantic/Reykjavik", "Africa/Douala", "Africa_Western", "Africa/Lagos", "Africa/Nouakchott", "GMT", "Atlantic/Reykjavik", "Europe/Sofia", "Europe_Eastern", "Europe/Bucharest", "Antarctica/Davis", "Davis", "Antarctica/Davis", "Antarctica/McMurdo", "New_Zealand", "Pacific/Auckland", "Europe/San_Marino", "Europe_Central", "Europe/Paris", "Africa/Porto-Novo", "Africa_Western", "Africa/Lagos", "Asia/Jayapura", "Indonesia_Eastern", "Asia/Jayapura", "America/St_Lucia", "Atlantic", "America/Halifax", "America/Nipigon", "America_Eastern", "America/New_York", "America/Argentina/Catamarca", "Argentina", "America/Buenos_Aires", "Europe/Isle_of_Man", "GMT", "Atlantic/Reykjavik", "America/Kentucky/Louisville", "America_Eastern", "America/New_York", "America/Merida", "America_Central", "America/Chicago", "Pacific/Marquesas", "Marquesas", "Pacific/Marquesas", "Asia/Magadan", "Magadan", "Asia/Magadan", "Africa/Libreville", "Africa_Western", "Africa/Lagos", "Pacific/Efate", "Vanuatu", "Pacific/Efate", "Asia/Kuala_Lumpur", "Malaysia", "Asia/Kuching", "America/Iqaluit", "America_Eastern", "America/New_York", "Indian/Comoro", "Africa_Eastern", "Africa/Nairobi", "America/Panama", "America_Eastern", "America/New_York", "Asia/Hebron", "Europe_Eastern", "Europe/Bucharest", "America/Jujuy", "Argentina", "America/Buenos_Aires", "America/Pangnirtung", "America_Eastern", "America/New_York", "Asia/Tbilisi", "Georgia", "Asia/Tbilisi", "Europe/Podgorica", "Europe_Central", "Europe/Paris", "America/Boise", "America_Mountain", "America/Denver", "Asia/Muscat", "Gulf", "Asia/Dubai", "Indian/Mahe", "Seychelles", "Indian/Mahe", "America/Montreal", "America_Eastern", "America/New_York", "Africa/Bangui", "Africa_Western", "Africa/Lagos", "America/Curacao", "Atlantic", "America/Halifax", "Asia/Taipei", "Taipei", "Asia/Taipei", "Europe/Ljubljana", "Europe_Central", "Europe/Paris", "Atlantic/Stanley", "Falkland", "Atlantic/Stanley", "Pacific/Guadalcanal", "Solomon", "Pacific/Guadalcanal", "Asia/Kuwait", "Arabian", "Asia/Riyadh", "Asia/Riyadh", "Arabian", "Asia/Riyadh", "Europe/Tallinn", "Europe_Eastern", "Europe/Bucharest", "America/New_York", "America_Eastern", "America/New_York", "America/Paramaribo", "Suriname", "America/Paramaribo", "America/Argentina/Buenos_Aires", "Argentina", "America/Buenos_Aires", "Asia/Irkutsk", "Irkutsk", "Asia/Irkutsk", "Asia/Katmandu", "Nepal", "Asia/Katmandu", "America/Kralendijk", "Atlantic", "America/Halifax")
  private final val mzoneMap: Array[String] = Array[String]("GMT", "ST", "Africa/Sao_Tome", "GMT", "ML", "Africa/Bamako", "GMT", "IE", "Europe/Dublin", "GMT", "SN", "Africa/Dakar", "GMT", "GH", "Africa/Accra", "GMT", "CI", "Africa/Abidjan", "GMT", "BF", "Africa/Ouagadougou", "GMT", "MR", "Africa/Nouakchott", "GMT", "GM", "Africa/Banjul", "GMT", "SL", "Africa/Freetown", "GMT", "GN", "Africa/Conakry", "GMT", "SH", "Atlantic/St_Helena", "GMT", "GB", "Europe/London", "GMT", "LR", "Africa/Monrovia", "GMT", "TG", "Africa/Lome", "Africa_Western", "CF", "Africa/Bangui", "Africa_Western", "NE", "Africa/Niamey", "Africa_Western", "CM", "Africa/Douala", "Africa_Western", "CD", "Africa/Kinshasa", "Africa_Western", "CG", "Africa/Brazzaville", "Africa_Western", "GA", "Africa/Libreville", "Africa_Western", "TD", "Africa/Ndjamena", "Africa_Western", "AO", "Africa/Luanda", "Africa_Western", "GQ", "Africa/Malabo", "Africa_Eastern", "YT", "Indian/Mayotte", "Africa_Eastern", "UG", "Africa/Kampala", "Africa_Eastern", "ET", "Africa/Addis_Ababa", "Africa_Eastern", "MG", "Indian/Antananarivo", "Africa_Eastern", "TZ", "Africa/Dar_es_Salaam", "Africa_Eastern", "SO", "Africa/Mogadishu", "Africa_Eastern", "ER", "Africa/Asmera", "Africa_Eastern", "KM", "Indian/Comoro", "Africa_Eastern", "DJ", "Africa/Djibouti", "Europe_Central", "GI", "Europe/Gibraltar", "Europe_Central", "DK", "Europe/Copenhagen", "Europe_Central", "SE", "Europe/Stockholm", "Europe_Central", "CH", "Europe/Zurich", "Europe_Central", "AL", "Europe/Tirane", "Europe_Central", "RS", "Europe/Belgrade", "Europe_Central", "HU", "Europe/Budapest", "Europe_Central", "MT", "Europe/Malta", "Europe_Central", "PL", "Europe/Warsaw", "Europe_Central", "ME", "Europe/Podgorica", "Europe_Central", "ES", "Europe/Madrid", "Europe_Central", "CZ", "Europe/Prague", "Europe_Central", "IT", "Europe/Rome", "Europe_Central", "SI", "Europe/Ljubljana", "Europe_Central", "LI", "Europe/Vaduz", "Europe_Central", "AT", "Europe/Vienna", "Europe_Central", "VA", "Europe/Vatican", "Europe_Central", "DE", "Europe/Berlin", "Europe_Central", "NO", "Europe/Oslo", "Europe_Central", "SK", "Europe/Bratislava", "Europe_Central", "AD", "Europe/Andorra", "Europe_Central", "SM", "Europe/San_Marino", "Europe_Central", "MK", "Europe/Skopje", "Europe_Central", "TN", "Africa/Tunis", "Europe_Central", "HR", "Europe/Zagreb", "Europe_Central", "NL", "Europe/Amsterdam", "Europe_Central", "BE", "Europe/Brussels", "Europe_Central", "MC", "Europe/Monaco", "Europe_Central", "LU", "Europe/Luxembourg", "Europe_Central", "BA", "Europe/Sarajevo", "China", "MO", "Asia/Macau", "America_Pacific", "MX", "America/Tijuana", "America_Pacific", "CA", "America/Vancouver", "Indochina", "LA", "Asia/Vientiane", "Indochina", "KH", "Asia/Phnom_Penh", "Indochina", "TH", "Asia/Bangkok", "Korea", "KP", "Asia/Pyongyang", "America_Mountain", "MX", "America/Hermosillo", "America_Mountain", "CA", "America/Edmonton", "Africa_Southern", "LS", "Africa/Maseru", "Africa_Southern", "SZ", "Africa/Mbabane", "Chile", "AQ", "Antarctica/Palmer", "New_Zealand", "AQ", "Antarctica/McMurdo", "Gulf", "OM", "Asia/Muscat", "Europe_Western", "FO", "Atlantic/Faeroe", "America_Eastern", "TC", "America/Grand_Turk", "America_Eastern", "CA", "America/Toronto", "America_Eastern", "BS", "America/Nassau", "America_Eastern", "PA", "America/Panama", "America_Eastern", "JM", "America/Jamaica", "America_Eastern", "KY", "America/Cayman", "Africa_Central", "BI", "Africa/Bujumbura", "Africa_Central", "ZM", "Africa/Lusaka", "Africa_Central", "ZW", "Africa/Harare", "Africa_Central", "CD", "Africa/Lubumbashi", "Africa_Central", "BW", "Africa/Gaborone", "Africa_Central", "RW", "Africa/Kigali", "Africa_Central", "MW", "Africa/Blantyre", "America_Central", "MX", "America/Mexico_City", "America_Central", "HN", "America/Tegucigalpa", "America_Central", "CA", "America/Winnipeg", "America_Central", "GT", "America/Guatemala", "America_Central", "SV", "America/El_Salvador", "America_Central", "CR", "America/Costa_Rica", "America_Central", "BZ", "America/Belize", "Atlantic", "MS", "America/Montserrat", "Atlantic", "AG", "America/Antigua", "Atlantic", "TT", "America/Port_of_Spain", "Atlantic", "MQ", "America/Martinique", "Atlantic", "DM", "America/Dominica", "Atlantic", "KN", "America/St_Kitts", "Atlantic", "BM", "Atlantic/Bermuda", "Atlantic", "PR", "America/Puerto_Rico", "Atlantic", "AW", "America/Aruba", "Atlantic", "VG", "America/Tortola", "Atlantic", "GD", "America/Grenada", "Atlantic", "GL", "America/Thule", "Atlantic", "BB", "America/Barbados", "Atlantic", "BQ", "America/Kralendijk", "Atlantic", "SX", "America/Lower_Princes", "Atlantic", "VI", "America/St_Thomas", "Atlantic", "MF", "America/Marigot", "Atlantic", "AI", "America/Anguilla", "Atlantic", "AN", "America/Curacao", "Atlantic", "LC", "America/St_Lucia", "Atlantic", "GP", "America/Guadeloupe", "Atlantic", "VC", "America/St_Vincent", "Arabian", "QA", "Asia/Qatar", "Arabian", "YE", "Asia/Aden", "Arabian", "KW", "Asia/Kuwait", "Arabian", "BH", "Asia/Bahrain", "Arabian", "IQ", "Asia/Baghdad", "India", "LK", "Asia/Colombo", "Europe_Eastern", "SY", "Asia/Damascus", "Europe_Eastern", "BG", "Europe/Sofia", "Europe_Eastern", "GR", "Europe/Athens", "Europe_Eastern", "JO", "Asia/Amman", "Europe_Eastern", "CY", "Asia/Nicosia", "Europe_Eastern", "AX", "Europe/Mariehamn", "Europe_Eastern", "LB", "Asia/Beirut", "Europe_Eastern", "FI", "Europe/Helsinki", "Europe_Eastern", "EG", "Africa/Cairo", "Chamorro", "GU", "Pacific/Guam")
  private final val aliasMap: Array[String] = Array[String]("Brazil/Acre", "America/Rio_Branco", "US/Indiana-Starke", "America/Indiana/Knox", "America/Atka", "America/Adak", "America/St_Barthelemy", "America/Guadeloupe", "Australia/North", "Australia/Darwin", "Europe/Zagreb", "Europe/Belgrade", "Etc/Universal", "Etc/UTC", "NZ-CHAT", "Pacific/Chatham", "Asia/Macao", "Asia/Macau", "Pacific/Yap", "Pacific/Chuuk", "Egypt", "Africa/Cairo", "US/Central", "America/Chicago", "Canada/Atlantic", "America/Halifax", "Brazil/East", "America/Sao_Paulo", "America/Cordoba", "America/Argentina/Cordoba", "US/Hawaii", "Pacific/Honolulu", "America/Louisville", "America/Kentucky/Louisville", "America/Shiprock", "America/Denver", "Australia/Canberra", "Australia/Sydney", "Asia/Chungking", "Asia/Chongqing", "Universal", "Etc/UTC", "US/Alaska", "America/Anchorage", "Asia/Ujung_Pandang", "Asia/Makassar", "Japan", "Asia/Tokyo", "Atlantic/Faeroe", "Atlantic/Faroe", "Asia/Istanbul", "Europe/Istanbul", "US/Pacific", "America/Los_Angeles", "Mexico/General", "America/Mexico_City", "Poland", "Europe/Warsaw", "Africa/Asmera", "Africa/Asmara", "Asia/Saigon", "Asia/Ho_Chi_Minh", "US/Michigan", "America/Detroit", "America/Argentina/ComodRivadavia", "America/Argentina/Catamarca", "W-SU", "Europe/Moscow", "Australia/ACT", "Australia/Sydney", "Asia/Calcutta", "Asia/Kolkata", "Arctic/Longyearbyen", "Europe/Oslo", "America/Knox_IN", "America/Indiana/Knox", "ROC", "Asia/Taipei", "Zulu", "Etc/UTC", "Australia/Yancowinna", "Australia/Broken_Hill", "Australia/West", "Australia/Perth", "Singapore", "Asia/Singapore", "Europe/Mariehamn", "Europe/Helsinki", "ROK", "Asia/Seoul", "America/Porto_Acre", "America/Rio_Branco", "Etc/Zulu", "Etc/UTC", "Canada/Yukon", "America/Whitehorse", "Europe/Vatican", "Europe/Rome", "Africa/Timbuktu", "Africa/Bamako", "America/Buenos_Aires", "America/Argentina/Buenos_Aires", "Canada/Pacific", "America/Vancouver", "US/Pacific-New", "America/Los_Angeles", "Mexico/BajaNorte", "America/Tijuana", "Europe/Guernsey", "Europe/London", "Asia/Tel_Aviv", "Asia/Jerusalem", "Chile/Continental", "America/Santiago", "Jamaica", "America/Jamaica", "Mexico/BajaSur", "America/Mazatlan", "Canada/Eastern", "America/Toronto", "Australia/Tasmania", "Australia/Hobart", "NZ", "Pacific/Auckland", "America/Lower_Princes", "America/Curacao", "GMT-", "Etc/GMT", "America/Rosario", "America/Argentina/Cordoba", "Libya", "Africa/Tripoli", "Asia/Ashkhabad", "Asia/Ashgabat", "Australia/NSW", "Australia/Sydney", "America/Marigot", "America/Guadeloupe", "Europe/Bratislava", "Europe/Prague", "Portugal", "Europe/Lisbon", "Etc/GMT-", "Etc/GMT", "Europe/San_Marino", "Europe/Rome", "Europe/Sarajevo", "Europe/Belgrade", "Antarctica/South_Pole", "Antarctica/McMurdo", "Canada/Central", "America/Winnipeg", "Etc/GMT", "Etc/GMT", "Europe/Isle_of_Man", "Europe/London", "America/Fort_Wayne", "America/Indiana/Indianapolis", "Eire", "Europe/Dublin", "America/Coral_Harbour", "America/Atikokan", "Europe/Nicosia", "Asia/Nicosia", "US/Samoa", "Pacific/Pago_Pago", "Hongkong", "Asia/Hong_Kong", "Canada/Saskatchewan", "America/Regina", "Asia/Thimbu", "Asia/Thimphu", "Kwajalein", "Pacific/Kwajalein", "GB", "Europe/London", "Chile/EasterIsland", "Pacific/Easter", "US/East-Indiana", "America/Indiana/Indianapolis", "Australia/LHI", "Australia/Lord_Howe", "Cuba", "America/Havana", "America/Jujuy", "America/Argentina/Jujuy", "US/Mountain", "America/Denver", "Atlantic/Jan_Mayen", "Europe/Oslo", "Europe/Tiraspol", "Europe/Chisinau", "Europe/Podgorica", "Europe/Belgrade", "US/Arizona", "America/Phoenix", "Navajo", "America/Denver", "Etc/Greenwich", "Etc/GMT", "Canada/Mountain", "America/Edmonton", "Iceland", "Atlantic/Reykjavik", "Australia/Victoria", "Australia/Melbourne", "Australia/South", "Australia/Adelaide", "Brazil/West", "America/Manaus", "Pacific/Ponape", "Pacific/Pohnpei", "Europe/Ljubljana", "Europe/Belgrade", "Europe/Jersey", "Europe/London", "Australia/Queensland", "Australia/Brisbane", "UTC", "Etc/UTC", "Canada/Newfoundland", "America/St_Johns", "Europe/Skopje", "Europe/Belgrade", "Canada/East-Saskatchewan", "America/Regina", "PRC", "Asia/Shanghai", "UCT", "Etc/UCT", "America/Mendoza", "America/Argentina/Mendoza", "Israel", "Asia/Jerusalem", "US/Eastern", "America/New_York", "Asia/Ulan_Bator", "Asia/Ulaanbaatar", "Turkey", "Europe/Istanbul", "GMT", "Etc/GMT", "US/Aleutian", "America/Adak", "Brazil/DeNoronha", "America/Noronha", "GB-Eire", "Europe/London", "Asia/Dacca", "Asia/Dhaka", "America/Ensenada", "America/Tijuana", "America/Catamarca", "America/Argentina/Catamarca", "Iran", "Asia/Tehran", "Greenwich", "Etc/GMT", "Pacific/Truk", "Pacific/Chuuk", "Pacific/Samoa", "Pacific/Pago_Pago", "America/Virgin", "America/St_Thomas", "Asia/Katmandu", "Asia/Kathmandu", "America/Indianapolis", "America/Indiana/Indianapolis", "Europe/Belfast", "Europe/London", "America/Kralendijk", "America/Curacao")
  private final val zidToMzone: Map[String, String] = new HashMap[String, String]
  private final val mzoneToZid: Map[String, String] = new HashMap[String, String]
  private final val mzoneToZidL: Map[String, Map[String, String]] = new HashMap[String, Map[String, String]]
  private final val aliases: Map[String, String] = new HashMap[String, String]
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
 * <p>
 * Provides classes to print and parse dates and times.
 * </p>
 * <p>
 * Printing and parsing is based around the
 * {@link java.time.format.DateTimeFormatter DateTimeFormatter} class.
 * Instances are generally obtained from
 * {@link java.time.format.DateTimeFormatter DateTimeFormatter}, however
 * {@link java.time.format.DateTimeFormatterBuilder DateTimeFormatterBuilder}
 * can be used if more power is needed.
 * </p>
 * <p>
 * Localization occurs by calling
 * {@link java.time.format.DateTimeFormatter#withLocale(java.util.Locale) withLocale(Locale)}
 * on the formatter. Further customization is possible using
 * {@link java.time.format.DecimalStyle DecimalStyle}.
 * </p>
 *
 * <h3>Package specification</h3>
 * <p>
 * Unless otherwise noted, passing a null argument to a constructor or method in any class or interface
 * in this package will cause a {@link java.lang.NullPointerException NullPointerException} to be thrown.
 * The Javadoc "@param" definition is used to summarise the null-behavior.
 * The "@throws {@link java.lang.NullPointerException}" is not explicitly documented in each method.
 * </p>
 * <p>
 * All calculations should check for numeric overflow and throw either an {@link java.lang.ArithmeticException}
 * or a {@link java.time.DateTimeException}.
 * </p>
 * @since JDK1.8
 */




