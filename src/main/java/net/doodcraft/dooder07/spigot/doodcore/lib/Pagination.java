package net.doodcraft.dooder07.spigot.doodcore.lib;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.*;

/**
 * Represents a part of a pagination book.<br/><br/>
 *
 * However, Paginator extends this class so that other classes
 * extending PaginationAbstract can use copyFormattingFrom(PaginationAbstract) on the Paginator
 * class. Therefore, adding PaginationAbstract elements to the Paginator class will have no real effect.<br/><br/>
 *
 * PaginationEntry is similar to Paginator in that adding PaginationEntries to it will have no effect.
 * PaginationEntry extends PaginationAbstract so that it can be automatically changed when its parent's
 * format, maxLineLength, wrapStyle, color code, html entity variables are changed.
 *
 * @param <T> Type extending Pagination
 */
public abstract class Pagination<T extends Pagination<?>> extends ArrayList<T> {
    // Variables
    private static final long serialVersionUID = -3564690340215825581L;
    protected String pageFormat = "%entriesPage %page/%pagemax";
    protected String entryFormat = "%entry%n";
    protected int maxLineLength = -1;
    protected int wrapStyle = 2;
    /** Line will break between any two characters */
    public static final int CHARACTER_WRAP = 1;
    /** Line will break only at spaces (default) */
    public static final int WORD_WRAP = 2;
    /** Anything after the maximum line length will not be included, line will break only at spaces */
    public static final int CUT_OFF_WORD = 3;
    /** Anthing after the maximum line length, line will break between any two characters */
    public static final int CUT_OFF_CHARACTER = 4;
    public boolean indirectlyUnchangeable = false;
    private boolean decodeColorCodes = true;
    private char decodeColorCodesWith = '&';
    private boolean decodeHtmlEntities = false;
    private boolean immutable = false;

    // Static methods
    /**
     * Paginate a List of any generic type.
     *
     * @param list List to paginate
     * @param listSize Maximum amount of list items a page can have.
     * @return PaginationBook
     */
    public static <S> PaginationBook paginate(List<S> list, int listSize) throws IllegalArgumentException {
        if (list == null)
            throw new IllegalArgumentException("List argument cannot be null.");
        if (listSize <= 0)
            throw new IllegalArgumentException("listSize argument cannot be less than or equal to zero.");

        PaginationBook book = new PaginationBook();

        List<List<S>> bookData = partition(list, listSize);
        for (List<S> page : bookData) {
            book.add(new PaginationPage(book, page));
        }

        return book;
    }

    /**
     * Paginate a String.
     *
     * @param s String to paginate.
     * @param lineLength Maximum line length of
     * @param listSize Maximum list size
     * @param wordWrap If true, lines will be broken only at spaces, if false, lines will be broken at any two characters
     * @return PaginationBook
     */
    public static PaginationBook paginate(String s, int lineLength, int listSize, boolean wordWrap) throws IllegalArgumentException {
        if (s == null)
            throw new IllegalArgumentException("\"s\" argument cannot be null.");
        if (lineLength <= 0)
            throw new IllegalArgumentException("\"lineLength\" argument must be greater than zero.");
        if (listSize <= 0)
            throw new IllegalArgumentException("\"listSize\" argument must be greater than zero.");

        return (wordWrap) ? paginate(wordWrap(s, lineLength), listSize) :
                paginate(characterWrap(s, lineLength), listSize);
    }

    /**
     * Break up a String into several Strings with the maximum length defined by the lineLength argument.<br/>
     * Lines will only be broken at spaces
     *
     * @param s String to break up
     * @param lineLength Maximum line length
     * @return A String List
     * @throws IllegalArgumentException If the max line length is shorter than the longest word
     * and the wrap style is <code>CUT_OFF_WORD</code> or <code>WORD_WRAP</code>
     */
    public static List<String> wordWrap(String s, int lineLength) throws IllegalArgumentException {
        return partition(s, lineLength, true);
    }

    /**
     * Break up a String into several Strings with the maximum length defined by the lineLength argument.<br/>
     * Lines will be broken between any two characters.
     *
     * @param s String to break up
     * @param lineLength Maximum line length
     * @return A String List
     */
    public static List<String> characterWrap(String s, int lineLength) {
        return partition(s, lineLength, false);
    }

    // Non-static things
    public Pagination(boolean immutability) {
        this.immutable = immutability;
    }

    public boolean isImmutable() {
        return immutable;
    }

    /**
     * Copy formatting from to supplied PaginationAbstract
     *
     * @param p PaginationAbstract to copy to
     */
    public void copyFormattingTo(Pagination<?> p) {
        copyFormattingTo(p, true);
    }

    /**
     * Copy formatting from to supplied PaginationAbstract
     *
     * @param p PaginationAbstract to copy to
     * @param changeChildren If true, the supplied PaginationAbstract's children will be changed to match this
     */
    public void copyFormattingTo(Pagination<?> p, boolean changeChildren) {
        p.pageFormat = this.pageFormat;
        p.entryFormat = this.entryFormat;
        p.maxLineLength = this.maxLineLength;
        p.wrapStyle = this.wrapStyle;
        p.decodeHtmlEntities = this.decodeHtmlEntities;
        p.decodeColorCodes = this.decodeColorCodes;
        p.decodeColorCodesWith = this.decodeColorCodesWith;

        if (changeChildren) {
            for (Pagination<?> p2 : p) {
                if (!p2.indirectlyUnchangeable) {
                    p2.copyFormattingFrom(this, changeChildren);
                }
            }
        }
    }

    /**
     * Copy formatting from the supplied PaginationAbstract
     *
     * @param p PaginationAbstract to copy from
     * @return Self
     */
    public Pagination<?> copyFormattingFrom(Pagination<?> p) {
        return copyFormattingFrom(p, true);
    }

    /**
     * Copy formatting from the supplied PaginationAbstract
     *
     * @param p PaginationAbstract to copy from
     * @param changeChildren If true, this class's children will be changed to match this
     * @return Self
     */
    public Pagination<?> copyFormattingFrom(Pagination<?> p, boolean changeChildren) {
        this.pageFormat = p.pageFormat;
        this.entryFormat = p.entryFormat;
        this.maxLineLength = p.maxLineLength;
        this.wrapStyle = p.wrapStyle;
        this.decodeHtmlEntities = p.decodeHtmlEntities;
        this.decodeColorCodes = p.decodeColorCodes;
        this.decodeColorCodesWith = p.decodeColorCodesWith;

        if (changeChildren) {
            for (Pagination<?> p2 : this) {
                if (!p2.indirectlyUnchangeable) {
                    p2.copyFormattingFrom(p, changeChildren);
                }
            }
        }
        return this;
    }

    /**
     * Set whether or not color html entities be decoded.
     *
     * @param decodeHtmlEntities Whether or not html entities will be decoded
     * @return Self
     */
    public Pagination<?> decodeHtmlEntities(boolean decodeHtmlEntities) {
        return decodeHtmlEntities(decodeHtmlEntities, true);
    }

    /**
     * Set whether or not color html entities be decoded.
     *
     * @param decodeHtmlEntities Whether or not html entities will be decoded
     * @param changeChildren If true, this class's children will be changed to match this
     * @return Self
     */
    public Pagination<?> decodeHtmlEntities(boolean decodeHtmlEntities, boolean changeChildren) {
        this.decodeHtmlEntities = decodeHtmlEntities;
        if (changeChildren) {
            for (Pagination<?> p : this) {
                if (!p.indirectlyUnchangeable) {
                    p.decodeHtmlEntities(decodeHtmlEntities, changeChildren);
                }
            }
        }
        return this;
    }

    /**
     * Returns whether or not html entities will be decoded.
     *
     * @return If true, html entities will be decoded, if false, html entities won't be decoded
     */
    public boolean getDecodeHtmlEntities() {
        return decodeHtmlEntities;
    }

    /**
     * Set whether or not color codes will be translated.
     *
     * @param decodeColorCodes Whether or not color codes will be translated
     * @return Self
     */
    public Pagination<?> decodeColorCodes(boolean decodeColorCodes) {
        return decodeColorCodes(decodeColorCodes, true);
    }

    /**
     * Set whether or not color codes will be translated.
     *
     * @param decodeColorCodes Whether or not color codes will be translated
     * @param changeChildren If true, this class's children will be changed to match this
     * @return Self
     */
    public Pagination<?> decodeColorCodes(boolean decodeColorCodes, boolean changeChildren) {
        this.decodeColorCodes = decodeColorCodes;
        if (changeChildren) {
            for (Pagination<?> p : this) {
                if (!p.indirectlyUnchangeable) {
                    p.decodeColorCodes(decodeColorCodes, changeChildren);
                }
            }
        }
        return this;
    }

    /**
     * Returns whether or not color codes will be translated.
     *
     * @return If true, color codes will be translated, if false, color codes won't be translated
     */
    public boolean getDecodeColorCodes() {
        return decodeColorCodes;
    }

    /**
     * Sets the character that will be used with ChatColor.translateAlternateColorCodes()
     *
     * @param decodeColorCodesWith The character that will be used with ChatColor.translateAlternateColorCodes()
     * @return Self
     */
    public Pagination<?> decodeColorCodesWith(char decodeColorCodesWith) {
        return decodeColorCodesWith(decodeColorCodesWith, true);
    }

    /**
     * Sets the character that will be used with ChatColor.translateAlternateColorCodes()
     *
     * @param decodeColorCodesWith The character that will be used with ChatColor.translateAlternateColorCodes()
     * @param changeChildren If true, this class's children will be changed to match this
     * @return Self
     */
    public Pagination<?> decodeColorCodesWith(char decodeColorCodesWith, boolean changeChildren) {
        this.decodeColorCodesWith = decodeColorCodesWith;
        if (changeChildren) {
            for (Pagination<?> p : this) {
                if (!p.indirectlyUnchangeable) {
                    p.decodeColorCodesWith(decodeColorCodesWith, changeChildren);
                }
            }
        }
        return this;
    }

    /**
     * Returns the character that will be used with ChatColor.translateAlternateColorCodes()
     *
     * @return The character that will be used with ChatColor.translateAlternateColorCodes()
     */
    public char getDecodeColorCodesWith() {
        return decodeColorCodesWith;
    }

    /**
     * Set wrap style to be used if there's a maximum line length.<br/><br/>
     *
     * <b>Wrap Styles:</b>
     * <ul>
     * <li><b>Pagination.CHARACTER_WRAP</b> - Line will break between any two characters.</li>
     * <li><b>Pagination.WORD_WRAP</b> - Line will break only at spaces (default).</li>
     * <li><b>Pagination.CUT_OFF_WORD</b> - Anything after the maximum line length will not be included, line will break only at spaces.</li>
     * <li><b>Pagination.CUT_OFF_CHARACTER</b> - Anything after the maximum line length will not be included, line will break between any two characters.</li>
     * </ul>
     *
     * @param style Wrap style
     * @return Self
     */
    public Pagination<?> setWrapStyle(int style) {
        return setWrapStyle(style, true);
    }

    /**
     * Set wrap style to be used if there's a maximum line length.<br/><br/>
     *
     * <b>Wrap Styles:</b>
     * <ul>
     * <li><b>Pagination.CHARACTER_WRAP</b> - Line will break between any two characters.</li>
     * <li><b>Pagination.WORD_WRAP</b> - Line will break only at spaces (default).</li>
     * <li><b>Pagination.CUT_OFF_WORD</b> - Anything after the maximum line length will not be included, line will break only at spaces.</li>
     * <li><b>Pagination.CUT_OFF_CHARACTER</b> - Anything after the maximum line length will not be included, line will break between any two characters.</li>
     * </ul>
     *
     * @param style Wrap style
     * @param changeChildren If true, this class's children will be changed to match this
     * @return Self
     */
    public Pagination<?> setWrapStyle(int style, boolean changeChildren) {
        this.wrapStyle = style;
        if (changeChildren) {
            for (Pagination<?> p : this) {
                if (!p.indirectlyUnchangeable) {
                    p.setWrapStyle(style, changeChildren);
                }
            }
        }
        return this;
    }

    /**
     * Set the maximum length a line can have.<br/>
     * -1 for no limit.
     *
     * @param length Maximum length compared to the number of Unicode code units in the string
     * @return Self
     */
    public Pagination<?> setMaxLineLength(int length) {
        return setMaxLineLength(length, true);
    }

    /**
     * Set the maximum length a line can have.<br/>
     * -1 for no limit.
     *
     * @param length Maximum length compared to the number of Unicode code units in the string
     * @param changeChildren If true, this class's children will be changed to match this
     * @return Self
     */
    public Pagination<?> setMaxLineLength(int length, boolean changeChildren) {
        this.maxLineLength  = length;
        if (changeChildren) {
            for (Pagination<?> p : this) {
                if (!p.indirectlyUnchangeable) {
                    p.setMaxLineLength(length, changeChildren);
                }
            }
        }
        return this;
    }

    /**
     * Set page format.<br/><br/>
     *
     * <div style="border-bottom:1px solid #ccc;padding:5px;">
     * <b>Variables:</b>
     * </div>
     * <div style="border-bottom:1px solid #ccc;padding:5px;">
     * <div style="float:left;width:45%;">%n</div>
     * <div style="float:right;width:50%;">New line</div>
     * <div style="clear:both;"></div>
     * </div>
     *
     * <div style="border-bottom:1px solid #ccc;padding:5px;">
     * <div style="float:left;width:45%;">%page</div>
     * <div style="float:right;width:50%;">Integer showing current page number</div>
     * <div style="clear:both;"></div>
     * </div>
     *
     * <div style="border-bottom:1px solid #ccc;padding:5px;">
     * <div style="float:left;width:45%;">%pagemax</div>
     * <div style="float:right;width:50%;">Integer showing total amount of pages</div>
     * <div style="clear:both;"></div>
     * </div>
     *
     * <div style="border-bottom:1px solid #ccc;padding:5px;">
     * <div style="float:left;width:45%;">%entries</div>
     * <div style="float:right;width:50%;">The list of entries</div>
     * <div style="clear:both;"></div>
     * </div>
     *
     * <div style="border-bottom:1px solid #ccc;padding:5px;">
     * <div style="float:left;width:45%;">%entrypagemaxnumber</div>
     * <div style="float:right;width:50%;">Total amount of entry numbers for this page</div>
     * <div style="clear:both;"></div>
     * </div>
     *
     * <div style="border-bottom:1px solid #ccc;padding:5px;">
     * <div style="float:left;width:45%;">%entrymaxnumber</div>
     * <div style="float:right;width:50%;">Total amount of entry numbers counting every page</div>
     * <div style="clear:both;"></div>
     * </div>
     * <br/>
     *
     * <b>Default format:</b><br/>
     * <pre>%entriesPage %page/%pagemax</pre>
     * The above would show up as:<br/><br/>
     * <pre>
     * Entry 1
     * Entry 2
     * Entry 3
     * Entry 4
     * Entry 5
     * Page 2/5</pre>
     *
     * Unless you change the entry format, there is a new line after each entry by default.<br/><br/>
     *
     * @param format Format
     * @return Self
     */
    public Pagination<?> setPageFormat(String format) {
        return setPageFormat(format, true);
    }

    /**
     * Set page format.<br/><br/>
     *
     * <div style="border-bottom:1px solid #ccc;padding:5px;">
     * <b>Variables:</b>
     * </div>
     * <div style="border-bottom:1px solid #ccc;padding:5px;">
     * <div style="float:left;width:45%;">%n</div>
     * <div style="float:right;width:50%;">New line</div>
     * <div style="clear:both;"></div>
     * </div>
     *
     * <div style="border-bottom:1px solid #ccc;padding:5px;">
     * <div style="float:left;width:45%;">%page</div>
     * <div style="float:right;width:50%;">Integer showing current page number</div>
     * <div style="clear:both;"></div>
     * </div>
     *
     * <div style="border-bottom:1px solid #ccc;padding:5px;">
     * <div style="float:left;width:45%;">%pagemax</div>
     * <div style="float:right;width:50%;">Integer showing total amount of pages</div>
     * <div style="clear:both;"></div>
     * </div>
     *
     * <div style="border-bottom:1px solid #ccc;padding:5px;">
     * <div style="float:left;width:45%;">%entries</div>
     * <div style="float:right;width:50%;">The list of entries</div>
     * <div style="clear:both;"></div>
     * </div>
     *
     * <div style="border-bottom:1px solid #ccc;padding:5px;">
     * <div style="float:left;width:45%;">%entrypagemaxnumber</div>
     * <div style="float:right;width:50%;">Total amount of entry numbers for this page</div>
     * <div style="clear:both;"></div>
     * </div>
     *
     * <div style="border-bottom:1px solid #ccc;padding:5px;">
     * <div style="float:left;width:45%;">%entrymaxnumber</div>
     * <div style="float:right;width:50%;">Total amount of entry numbers counting every page</div>
     * <div style="clear:both;"></div>
     * </div>
     * <br/>
     *
     * <b>Default format:</b><br/>
     * <pre>%entriesPage %page/%pagemax</pre>
     * The above would show up as:<br/><br/>
     * <pre>
     * Entry 1
     * Entry 2
     * Entry 3
     * Entry 4
     * Entry 5
     * Page 2/5</pre>
     *
     * Unless you change the entry format, there is a new line after each entry by default.<br/><br/>
     *
     * @param format Format
     * @param changeChildren If true, this class's children will be changed to match this
     * @return Self
     */
    public Pagination<?> setPageFormat(String format, boolean changeChildren) {
        this.pageFormat = format;
        if (changeChildren) {
            for (Pagination<?> p : this) {
                if (!p.indirectlyUnchangeable) {
                    p.setPageFormat(format, changeChildren);
                }
            }
        }
        return this;
    }

    /**
     * Returns the page format.<br/><br/>
     *
     * See {@link #setPageFormat} for a list of variables that can be used in the page format.
     *
     * @return Page format
     */
    public String getPageFormat() {
        return pageFormat;
    }

    /**
     * Returns the entry format.<br/><br/>
     *
     * See {@link #setEntryFormat} for a list of variables that can be used in the entry format.
     *
     * @return Entry format
     */
    public String getEntryFormat() {
        return entryFormat;
    }

    /**
     * Set entry format.<br/><br/>
     *
     * <div style="border-bottom:1px solid #ccc;padding:5px;">
     * <b>Variables:</b>
     * </div>
     * <div style="border-bottom:1px solid #ccc;padding:5px;">
     * <div style="float:left;width:45%;">%n</div>
     * <div style="float:right;width:50%;">New line</div>
     * <div style="clear:both;"></div>
     * </div>
     *
     * <div style="border-bottom:1px solid #ccc;padding:5px;">
     * <div style="float:left;width:45%;">%page</div>
     * <div style="float:right;width:50%;">Integer showing current page number</div>
     * <div style="clear:both;"></div>
     * </div>
     *
     * <div style="border-bottom:1px solid #ccc;padding:5px;">
     * <div style="float:left;width:45%;">%pagemax</div>
     * <div style="float:right;width:50%;">Integer showing maximum amount of pages</div>
     * <div style="clear:both;"></div>
     * </div>
     *
     * <div style="border-bottom:1px solid #ccc;padding:5px;">
     * <div style="float:left;width:45%;">%entry</div>
     * <div style="float:right;width:50%;">Entry text</div>
     * <div style="clear:both;"></div>
     * </div>
     *
     * <div style="border-bottom:1px solid #ccc;padding:5px;">
     * <div style="float:left;width:45%;">%entrynumber</div>
     * <div style="float:right;width:50%;">Entry number</div>
     * <div style="clear:both;"></div>
     * </div>
     *
     * <div style="border-bottom:1px solid #ccc;padding:5px;">
     * <div style="float:left;width:45%;">%entrypagemaxnumber</div>
     * <div style="float:right;width:50%;">Total amount of entry numbers for this page</div>
     * <div style="clear:both;"></div>
     * </div>
     *
     * <div style="border-bottom:1px solid #ccc;padding:5px;">
     * <div style="float:left;width:45%;">%entrymaxnumber</div>
     * <div style="float:right;width:50%;">Total amount of entry numbers counting every page</div>
     * <div style="clear:both;"></div>
     * </div>
     * <br/>
     *
     * <b>Default format:</b><br/>
     * <pre>%entry%n</pre>
     *
     * @param format Format
     * @return Self
     */
    public Pagination<?> setEntryFormat(String format) {
        return setEntryFormat(format, true);
    }

    /**
     * Set entry format.<br/><br/>
     *
     * <div style="border-bottom:1px solid #ccc;padding:5px;">
     * <b>Variables:</b>
     * </div>
     * <div style="border-bottom:1px solid #ccc;padding:5px;">
     * <div style="float:left;width:45%;">%n</div>
     * <div style="float:right;width:50%;">New line</div>
     * <div style="clear:both;"></div>
     * </div>
     *
     * <div style="border-bottom:1px solid #ccc;padding:5px;">
     * <div style="float:left;width:45%;">%page</div>
     * <div style="float:right;width:50%;">Integer showing current page number</div>
     * <div style="clear:both;"></div>
     * </div>
     *
     * <div style="border-bottom:1px solid #ccc;padding:5px;">
     * <div style="float:left;width:45%;">%pagemax</div>
     * <div style="float:right;width:50%;">Integer showing maximum amount of pages</div>
     * <div style="clear:both;"></div>
     * </div>
     *
     * <div style="border-bottom:1px solid #ccc;padding:5px;">
     * <div style="float:left;width:45%;">%entry</div>
     * <div style="float:right;width:50%;">Entry text</div>
     * <div style="clear:both;"></div>
     * </div>
     *
     * <div style="border-bottom:1px solid #ccc;padding:5px;">
     * <div style="float:left;width:45%;">%entrynumber</div>
     * <div style="float:right;width:50%;">Entry number</div>
     * <div style="clear:both;"></div>
     * </div>
     *
     * <div style="border-bottom:1px solid #ccc;padding:5px;">
     * <div style="float:left;width:45%;">%entrypagemaxnumber</div>
     * <div style="float:right;width:50%;">Total amount of entry numbers for this page</div>
     * <div style="clear:both;"></div>
     * </div>
     *
     * <div style="border-bottom:1px solid #ccc;padding:5px;">
     * <div style="float:left;width:45%;">%entrymaxnumber</div>
     * <div style="float:right;width:50%;">Total amount of entry numbers counting every page</div>
     * <div style="clear:both;"></div>
     * </div>
     * <br/>
     *
     * <b>Default format:</b><br/>
     * <pre>%entry%n</pre>
     *
     * @param format Format
     * @param changeChildren If true, this class's children will be changed to match this
     * @return Self
     */
    public Pagination<?> setEntryFormat(String format, boolean changeChildren) {
        this.entryFormat = format;
        if (changeChildren) {
            for (Pagination<?> p : this) {
                if (!p.indirectlyUnchangeable) {
                    p.setEntryFormat(format, changeChildren);
                }
            }
        }
        return this;
    }

    /**
     * Partition the supplied List<T> argument into separate lists with the
     * targetSize argument being the maximum items per a list.
     * <br/><br/>
     * No items will be left out, If the list size is 11 and the targetSize is 3,
     * then the last list will only have 2 items while the other 3 have 3 parameters.
     *
     * @param list List to be partitioned
     * @param targetSize Maximum items per list
     * @return A List with type List<T> containing all the lists from the partition
     */
    public static <S extends Object> List<List<S>> partition(List<S> list, int targetSize) {
        List<List<S>> lists = new ArrayList<List<S>>();
        for (int i = 0; i < list.size(); i += targetSize)
            lists.add(list.subList(i, Math.min(i + targetSize, list.size())));
        return lists;
    }

    /**
     * Partition the supplied String into a List of Strings with the targetSize
     * argument being the maximum length of a String in the returned list.
     *
     * @param s String to be partitioned
     * @param targetSize Maximum length of String
     * @param wordWrap if true, the string will only be broken at break points,
     * if false, the string will be broken between any two characters
     * @return A String List containing all the Strings from the partition
     * @throws IllegalArgumentException If the max line length is shorter than the longest word
     * and wordWrap equals true
     */
    public static List<String> partition(String s, int targetSize, boolean wordWrap) throws IllegalArgumentException {
        List<String> list = new ArrayList<String>();
        if (wordWrap) {
            String[] a = s.split(" ");
            String toAdd = "";
            for (String k : a) {
                if (k.length() >= targetSize) {
                    throw new IllegalArgumentException("targetSize argument must be larger than the length of the longest word when wordWrap is true.");
                }
                if ((toAdd.length() + k.length()) >= targetSize) {
                    list.add(toAdd);
                    toAdd = k;
                } else {
                    toAdd=(toAdd == "") ? toAdd+k : toAdd+" "+k;
                }
            }
            list.add(toAdd);
        } else {
            for (int i = 0; i < s.length(); i+=targetSize)
                list.add(s.substring(i, Math.min(i + targetSize, s.length())));
        }
        return list;
    }



    @Override
    public boolean add(T e) {
        if (immutable) throw new UnsupportedOperationException();
        return super.add(e);
    }
    @Override
    public void add(int index, T element) {
        if (immutable) throw new UnsupportedOperationException();
        super.add(index, element);
    }
    @Override
    public boolean addAll(Collection<? extends T> c) {
        if (immutable) throw new UnsupportedOperationException();
        return super.addAll(c);
    }
    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        if (immutable) throw new UnsupportedOperationException();
        return super.addAll(index, c);
    }
    @Override
    public void clear() {
        if (immutable) throw new UnsupportedOperationException();
        super.clear();
    }
    @Override
    public boolean contains(Object o) {
        return super.contains(o);
    }
    @Override
    public boolean containsAll(Collection<?> c) {
        return super.containsAll(c);
    }
    @Override
    public void ensureCapacity(int minCapacity) {
        if (immutable) throw new UnsupportedOperationException();
        super.ensureCapacity(minCapacity);
    }
    @Override
    public T get(int index) {
        return super.get(index);
    }
    @Override
    public int indexOf(Object o) {
        return super.indexOf(o);
    }
    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }
    @Override
    public Iterator<T> iterator() {
        return super.iterator();
    }
    @Override
    public int lastIndexOf(Object o) {
        return super.lastIndexOf(o);
    }
    @Override
    public ListIterator<T> listIterator() {
        return super.listIterator();
    }
    @Override
    public ListIterator<T> listIterator(int index) {
        return super.listIterator(index);
    }
    @Override
    public T remove(int index) {
        if (immutable) throw new UnsupportedOperationException();
        return super.remove(index);
    }
    @Override
    public boolean remove(Object o) {
        if (immutable) throw new UnsupportedOperationException();
        return super.remove(o);
    }
    @Override
    public boolean removeAll(Collection<?> c) {
        if (immutable) throw new UnsupportedOperationException();
        return super.removeAll(c);
    }
    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        if (immutable) throw new UnsupportedOperationException();
        super.removeRange(fromIndex, toIndex);
    }
    @Override
    public boolean retainAll(Collection<?> c) {
        if (immutable) throw new UnsupportedOperationException();
        return super.retainAll(c);
    }
    @Override
    public T set(int index, T element) {
        if (immutable) throw new UnsupportedOperationException();
        return super.set(index, element);
    }
    @Override
    public int size() {
        return super.size();
    }
    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return super.subList(fromIndex, toIndex);
    }
    @Override
    public Object[] toArray() {
        return super.toArray();
    }
    @Override
    public <S> S[] toArray(S[] a) {
        return super.toArray(a);
    }
    @Override
    public void trimToSize() {
        super.trimToSize();
    }

    /**
     * Represents a group of pages, i.e. a book.
     *
     */
    public static class PaginationBook extends Pagination<PaginationPage> {
        private static final long serialVersionUID = 753035731329454098L;

        /**
         * Construct a new, empty PaginationBook.
         */
        public PaginationBook() {
            super(false);
        }

        /**
         * Construct a new, empty PaginationBook.
         *
         * @param immutability Immutable if true
         */
        public PaginationBook(boolean immutability) {
            super(immutability);
        }

        /**
         * Construct a new PaginationBook containing the supplied pages.
         *
         * @param pages Initial pages.
         */
        public PaginationBook(PaginationPage... pages) {
            super(false);
            this.addAll(Arrays.asList(pages));
        }

        /**
         * Construct a new PaginationBook containing the supplied pages.
         *
         * @param pages Initial pages.
         * @param immutability Immutable if true
         */
        public PaginationBook(boolean immutability, PaginationPage... pages) {
            super(immutability);
            this.addAll(Arrays.asList(pages));
        }

        /**
         * Display a page.
         *
         * @param recipient The CommandSender (player or console) recieving the contents of the page.
         * If null, System.out.println() will be used
         * @param pageNumber Page number to display
         * @throws IllegalArgumentException If the page number is less than or equal to zero.<br/>
         * Or if the max line length is shorter than the longest word and the wrap style is
         * <code>CUT_OFF_WORD</code> or <code>WORD_WRAP</code>
         * @throws IndexOutOfBoundsException If the page number is out of bounds
         */
        public void displayPage(CommandSender recipient, int pageNumber) throws IllegalArgumentException, IndexOutOfBoundsException {
            if (pageNumber <= 0)
                throw new IllegalArgumentException("Page number must be greater than zero.");

            get(pageNumber-1).display(recipient);
        }

        /**
         * Returns the total amount of entries in the entire PaginationBook.
         *
         * @return Total amount of PaginationEntries
         */
        public int getTotalEntries() {
            int entryTotal = 0;
            for (PaginationPage p : this)
                entryTotal+=p.size();
            return entryTotal;
        }

        /**
         * Replaces this book's pages with the supplied book argument's pages.
         *
         * @param book PaginationBook to copy pages from
         * @return Self
         */
        public PaginationBook copyPagesFrom(PaginationBook book) {
            this.clear();
            this.addAll(book);
            return this;
        }

        /**
         * Replaces the supplied book argument's pages with this book's pages.
         *
         * @param book Pagination book to copy pages to
         */
        public void copyPagesTo(PaginationBook book) {
            book.clear();
            book.addAll(this);
        }
    }

    /**
     * Represents a page.
     */
    public static class PaginationPage extends Pagination<PaginationEntry> {
        private static final long serialVersionUID = -7575882715875036235L;
        public PaginationBook book;

        /**
         * Construct a new PaginationPage
         *
         * @param book Parent
         */
        public PaginationPage(PaginationBook book) {
            super(false);
            this.book = book;
        }

        /**
         * Construct a new PaginationPage
         *
         * @param book Parent
         * @param immutability Immutable if true
         */
        public PaginationPage(PaginationBook book, boolean immutability) {
            super(immutability);
            this.book = book;
        }

        /**
         * Construct a new PaginationPage
         * @param book Parent
         * @param lines Initial lines
         */
        public <T> PaginationPage(PaginationBook book, List<T> lines) {
            super(false);
            this.book = book;

            for (Object o : lines) {
                PaginationEntry line = new PaginationEntry(this, o);
                line.copyFormattingFrom(this, true);
                add(line);
            }
        }

        /**
         * Construct a new PaginationPage
         * @param book Parent
         * @param lines Initial lines
         * @param immutability Immutable if true
         */
        public <T> PaginationPage(PaginationBook book, List<T> lines, boolean immutability) {
            super(immutability);
            this.book = book;

            for (Object o : lines) {
                PaginationEntry line = new PaginationEntry(this, o);
                line.copyFormattingFrom(this, true);
                add(line);
            }
        }

        /**
         * Replaces this page's PaginationEntries with the supplied page's PaginationEntries
         *
         * @param page PaginationPage to copy from
         * @return Self
         */
        public PaginationPage copyFrom(PaginationPage page) {
            this.clear();
            this.addAll(page);
            return this;
        }

        /**
         * Replaces the suppliled page's PaginationEntries with the this page's PaginationEntries
         *
         * @param page PaginationPage to copy to
         */
        public void copyTo(PaginationPage page) {
            page.clear();
            page.addAll(this);
        }

        /**
         * Returns the page number of this page
         *
         * @return Page number, starts at 1
         */
        public int getPageNumber() {
            return book.indexOf(this)-1;
        }

        /**
         * Returns the List index of this page.
         *
         * @return Index number
         */
        public int getIndex() {
            return book.indexOf(this);
        }

        /**
         * Display this page.
         *
         * @param recipient The CommandSender (player or console) recieving the contents of the page.
         * If null, System.out.println() will be used
         * @throws IllegalArgumentException If the max line length is shorter than the longest word
         * and the wrap style is <code>CUT_OFF_WORD</code> or <code>WORD_WRAP</code>
         */
        public void display(CommandSender recipient) throws IllegalArgumentException {
            List<String> pageLines = getPageLines();
            for (String l : pageLines) {
                if (recipient == null) {
                    System.out.println(l);
                } else {
                    recipient.sendMessage(l);
                }
            }
        }

        /**
         * Returns the final, fully formatted list of lines.<br/><br/>
         * The display(CommandSender) method only retrieves the return value of this method and sends each
         * element as a new line to the recipient.
         *
         * @return Page lines
         * @throws IllegalArgumentException If the max line length is shorter than the longest word
         * and the wrap style is <code>CUT_OFF_WORD</code> or <code>WORD_WRAP</code>
         */
        public List<String> getPageLines() throws IllegalArgumentException {
            int pageNum = book.indexOf(this)+1;
            int pageMax = book.size();
            int entryPageMax = this.size();

            int entryNum = 0;
            for (PaginationEntry l : this) {
                entryNum++;
                String eFormat = l.entryFormat.replace("%pagemax", pageMax+"").replaceAll("%page", pageNum + "")
                        .replace("%entrypagemaxnumber", entryPageMax+"").replace("%entrymaxnumber", book.getTotalEntries()+"");
                l.tempText = (eFormat.replace("%entrynumber", entryNum+"").replace("%entry", l.getText()));

                if (l.getDecodeHtmlEntities())
                    l.tempText = (HtmlEntity.decode(l.tempText));
                if (l.getDecodeColorCodes())
                    l.tempText = (ChatColor.translateAlternateColorCodes(l.getDecodeColorCodesWith(), l.tempText));
            }

            int size = this.size();
            for (int i = 0; i < size; i++) {
                size = applyWrapStyle(i, size);
            }

            String pFormat = (this.getDecodeHtmlEntities()) ?
                    HtmlEntity.decode(pageFormat.replace("%pagemax", pageMax+"").replaceAll("%page", pageNum + "")
                            .replace("%entrypagemaxnumber", entryPageMax+"").replace("%entrymaxnumber", book.getTotalEntries()+""))
                    : pageFormat.replace("%pagemax", pageMax+"").replaceAll("%page", pageNum+"")
                    .replace("%entrypagemaxnumber", entryPageMax+"").replace("%entrymaxnumber", book.getTotalEntries()+"");

            String allEntries = "";
            for (PaginationEntry l : this) {
                allEntries=allEntries+l.tempText;
            }
            pFormat = pFormat.replace("%entries", allEntries);

            return new ArrayList<String>(Arrays.asList(pFormat.split("%n")));
        }

        private int applyWrapStyle(int i, int size) {
            PaginationEntry s = this.get(i);
            if (s.maxLineLength > 0) {
                try {
                    if (s.length() > maxLineLength) {
                        wrapStyle = (wrapStyle > 4 || wrapStyle < 1) ? 2 : wrapStyle;
                        String text = (s.tempText.endsWith("%n")) ?
                                s.tempText.substring(0, s.length()-2) : s.tempText;
                        if (wrapStyle == 1) { // Character wrap
                            List<String> partionedLines = partition(text, maxLineLength, false);
                            this.remove(i);
                            this.addAll(i, toEntries(partionedLines, s));
                            size+=partionedLines.size()-1;
                        } else if (wrapStyle == 2) { // Word wrap
                            List<String> partionedLines = partition(text, maxLineLength, true);
                            this.remove(i);
                            this.addAll(i, toEntries(partionedLines, s));
                            size+=partionedLines.size()-1;
                        } else if (wrapStyle == 3) { // Cut off word
                            List<String> partionedLines = partition(text, maxLineLength, true);
                            this.set(i, new PaginationEntry(this, partionedLines.get(0)+"%n", s));
                        } else if (wrapStyle == 4) { // Cut off character
                            List<String> partionedLines = partition(text, maxLineLength, false);
                            this.set(i, new PaginationEntry(this, partionedLines.get(0)+"%n", s));
                        }
                    }
                } catch(IndexOutOfBoundsException e) {
                }
            }
            return size;
        }

        private List<PaginationEntry> toEntries(List<String> c, Pagination<?> copyFormattingFrom) {
            List<PaginationEntry> lines = new ArrayList<PaginationEntry>();
            for (String s : c) {
                PaginationEntry line = new PaginationEntry(this, s+"%n", copyFormattingFrom);
                lines.add(line);
            }
            return lines;
        }
    }

    /**
     * Represents an element in a PaginationPage. Not necessarily each individual line in the page.
     */
    public static class PaginationEntry extends Pagination<PaginationEntry> {
        private static final long serialVersionUID = 5976140324907027602L;
        private Object obj;
        private String text;
        protected String tempText = "";
        public PaginationPage page;

        /**
         * Construct a new PaginationEntry.
         *
         * @param page Parent
         * @param obj Object
         */
        public PaginationEntry(PaginationPage page, Object obj) {
            super(false);
            this.page = page;
            this.obj = obj;
            this.text = obj.toString();
        }

        /**
         * Construct a new PaginationEntry.
         *
         * @param page Parent
         * @param obj Object
         * @param immutability Immutable if true
         */
        public PaginationEntry(PaginationPage page, Object obj, boolean immutability) {
            super(immutability);
            this.page = page;
            this.obj = obj;
            this.text = obj.toString();
        }

        /**
         * Construct a new PaginationEntry.
         *
         * @param page Parent
         * @param obj Object
         * @param copyFormattingFrom PaginationAbstract to copy format froma
         */
        public PaginationEntry(PaginationPage page, Object obj, Pagination<?> copyFormattingFrom) {
            super(false);
            this.page = page;
            this.obj = obj;
            this.text = obj.toString();
            this.copyFormattingFrom(copyFormattingFrom, true);
        }

        /**
         * Construct a new PaginationEntry.
         *
         * @param page Parent
         * @param obj Object
         * @param copyFormattingFrom PaginationAbstract to copy format froma
         * @param immutability Immutable if true
         */
        public PaginationEntry(PaginationPage page, Object obj, Pagination<?> copyFormattingFrom, boolean immutability) {
            super(immutability);
            this.page = page;
            this.obj = obj;
            this.text = obj.toString();
            this.copyFormattingFrom(copyFormattingFrom, true);
        }

        /**
         * Set only the object.
         *
         * @param obj Object to set.
         */
        public void setObject1(Object obj) {
            this.obj = obj;
        }

        /**
         * Sets the object and sets the text to the supplied obj.toString()
         *
         * @param obj Object to set.
         */
        public void setObject2(Object obj) {
            this.obj = obj;
            this.text = obj.toString();
        }

        /**
         * Return the object of this entry.
         *
         * @return Object
         */
        public Object getObject() {
            return obj;
        }

        /**
         * Set the text of this entry.
         *
         * @param text New text
         */
        public void setText(String text) {
            this.text = text;
        }

        /**
         * Returns the text of this entry.
         *
         * @return Text
         */
        public String getText() {
            return text;
        }

        public int length() {
            return text.length();
        }

        /**
         * Returns the text of this entry.
         */
        @Override
        public String toString() {
            return text;
        }
    }
}