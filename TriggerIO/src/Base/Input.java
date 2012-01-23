/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Base;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author dan
 */
public class Input {

    private int number;
    private int channel;
    private int note;
    public static final String ROOT = "input";
    public static final String PNUMBER = "number";
    public static final String PCHANNEL = "channel";
    public static final String PNOTE = "note";
    public static final Map<Integer, String> lovChannel = new LinkedHashMap<Integer, String>();
    public static final Map<Integer, String> lovNote = new LinkedHashMap<Integer, String>();

    //---------------------------------------------------------------------
    static {
        // build LOV midi channel
        buildChannelLov();

        // build LOV midi note
        buildNoteLov();
    }

    //---------------------------------------------------------------------
    public Input(int inNumber, int inChannel, int inNote) {
        setinputNumber(inNumber);
        setChannel(inChannel);
        setNote(inNote);
    }

    //---------------------------------------------------------------------
    public Input(Element element) {
        setInput(element);
    }

    //---------------------------------------------------------------------
    public Element getInput(Document doc) {
        Element element = doc.createElement(ROOT);
        element.setAttribute(PNOTE, String.valueOf(note));
        element.setAttribute(PCHANNEL, String.valueOf(channel));
        element.setAttribute(PNUMBER, String.valueOf(number));

        return element;
    }

    //---------------------------------------------------------------------
    public final void setInput(Element element) {
        setinputNumber(Integer.parseInt(element.getAttribute(PNUMBER)));
        setChannel(Integer.parseInt(element.getAttribute(PCHANNEL)));
        setNote(Integer.parseInt(element.getAttribute(PNOTE)));
    }

    //----------------------------------------------
    public int getinputNumber() {
        return this.number;
    }

    //----------------------------------------------
    public int getChannel() {
        return this.channel;
    }

    //----------------------------------------------
    public int getNote() {
        return this.note;
    }

    //----------------------------------------------
    private void setinputNumber(int value) {
        this.number = value;
        Common.logger.log(Level.FINEST, "inputNumber<{0}>", this.number);
    }

    //----------------------------------------------
    public final void setChannel(int value) {
        this.channel = value;
        Common.logger.log(Level.FINEST, "channel<{0}>", this.channel);
    }

    //----------------------------------------------
    public final void setNote(int value) {
        if (lovNote.containsKey(value)) {
            this.note = value;
            Common.logger.log(Level.FINEST, "note<{0}>", this.note);
        } else {
            Common.logger.log(Level.WARNING, "Unrecognised triggerMidiNote<{0}>", value);
        }
    }

    //----------------------------------------------
    private static void buildChannelLov() {
        // build LOV midi channel
        for (int i = 0; i <= 15; i++) {
            lovChannel.put(i, Integer.toString(i + 1));
        }
    }

    //----------------------------------------------
    private static void buildNoteLov() {
        lovNote.put(0, "0 (C 0)");
        lovNote.put(1, "1 (C#0)");
        lovNote.put(2, "2 (D 0)");
        lovNote.put(3, "3 (D#0)");
        lovNote.put(4, "4 (E 0)");
        lovNote.put(5, "5 (F 0)");
        lovNote.put(6, "6 (F#0)");
        lovNote.put(7, "7 (G 0)");
        lovNote.put(8, "8 (G#0)");
        lovNote.put(9, "9 (A 0)");
        lovNote.put(10, "10 (A#0)");
        lovNote.put(11, "11 (B 0)");
        lovNote.put(12, "12 (C 1)");
        lovNote.put(13, "13 (C#1)");
        lovNote.put(14, "14 (D 1)");
        lovNote.put(15, "15 (D#1)");
        lovNote.put(16, "16 (E 1)");
        lovNote.put(17, "17 (F 1)");
        lovNote.put(18, "18 (F#1)");
        lovNote.put(19, "19 (G 1)");
        lovNote.put(20, "20 (G#1)");
        lovNote.put(21, "21 (A 1)");
        lovNote.put(22, "22 (A#1)");
        lovNote.put(23, "23 (B 1)");
        lovNote.put(24, "24 (C 2)");
        lovNote.put(25, "25 (C#2)");
        lovNote.put(26, "26 (D 2)");
        lovNote.put(27, "27 (D#2)");
        lovNote.put(28, "28 (E 2)");
        lovNote.put(29, "29 (F 2)");
        lovNote.put(30, "30 (F#2)");
        lovNote.put(31, "31 (G 2)");
        lovNote.put(32, "32 (G#2)");
        lovNote.put(33, "33 (A 2)");
        lovNote.put(34, "34 (A#2)");
        lovNote.put(35, "35 (B 2)");
        lovNote.put(36, "36 (C 3)");
        lovNote.put(37, "37 (C#3)");
        lovNote.put(38, "38 (D 3)");
        lovNote.put(39, "39 (D#3)");
        lovNote.put(40, "40 (E 3)");
        lovNote.put(41, "41 (F 3)");
        lovNote.put(42, "42 (F#3)");
        lovNote.put(43, "43 (G 3)");
        lovNote.put(44, "44 (G#3)");
        lovNote.put(45, "45 (A 3)");
        lovNote.put(46, "46 (A#3)");
        lovNote.put(47, "47 (B 3)");
        lovNote.put(48, "48 (C 4)");
        lovNote.put(49, "49 (C#4)");
        lovNote.put(50, "50 (D 4)");
        lovNote.put(51, "51 (D#4)");
        lovNote.put(52, "52 (E 4)");
        lovNote.put(53, "53 (F 4)");
        lovNote.put(54, "54 (F#4)");
        lovNote.put(55, "55 (G 4)");
        lovNote.put(56, "56 (G#4)");
        lovNote.put(57, "57 (A 4)");
        lovNote.put(58, "58 (A#4)");
        lovNote.put(59, "59 (B 4)");
        lovNote.put(60, "60 (C 5)");
        lovNote.put(61, "61 (C#5)");
        lovNote.put(62, "62 (D 5)");
        lovNote.put(63, "63 (D#5)");
        lovNote.put(64, "64 (E 5)");
        lovNote.put(65, "65 (F 5)");
        lovNote.put(66, "66 (F#5)");
        lovNote.put(67, "67 (G 5)");
        lovNote.put(68, "68 (G#5)");
        lovNote.put(69, "69 (A 5)");
        lovNote.put(70, "70 (A#5)");
        lovNote.put(71, "71 (B 5)");
        lovNote.put(72, "72 (C 6)");
        lovNote.put(73, "73 (C#6)");
        lovNote.put(74, "74 (D 6)");
        lovNote.put(75, "75 (D#6)");
        lovNote.put(76, "76 (E 6)");
        lovNote.put(77, "77 (F 6)");
        lovNote.put(78, "78 (F#6)");
        lovNote.put(79, "79 (G 6)");
        lovNote.put(80, "80 (G#6)");
        lovNote.put(81, "81 (A 6)");
        lovNote.put(82, "82 (A#6)");
        lovNote.put(83, "83 (B 6)");
        lovNote.put(84, "84 (C 7)");
        lovNote.put(85, "85 (C#7)");
        lovNote.put(86, "86 (D 7)");
        lovNote.put(87, "87 (D#7)");
        lovNote.put(88, "88 (E 7)");
        lovNote.put(89, "89 (F 7)");
        lovNote.put(90, "90 (F#7)");
        lovNote.put(91, "91 (G 7)");
        lovNote.put(92, "92 (G#7)");
        lovNote.put(93, "93 (A 7)");
        lovNote.put(94, "94 (A#7)");
        lovNote.put(95, "95 (B 7)");
        lovNote.put(96, "96 (C 8)");
        lovNote.put(97, "97 (C#8)");
        lovNote.put(98, "98 (D 8)");
        lovNote.put(99, "99 (D#8)");
        lovNote.put(100, "100 (E 8)");
        lovNote.put(101, "101 (F 8)");
        lovNote.put(102, "102 (F#8)");
        lovNote.put(103, "103 (G 8)");
        lovNote.put(104, "104 (G#8)");
        lovNote.put(105, "105 (A 8)");
        lovNote.put(106, "106 (A#8)");
        lovNote.put(107, "107 (B 8)");
        lovNote.put(108, "108 (C 9)");
        lovNote.put(109, "109 (C#9)");
        lovNote.put(110, "110 (D 9)");
        lovNote.put(111, "111 (D#9)");
        lovNote.put(112, "112 (E 9)");
        lovNote.put(113, "113 (C#9)");
        lovNote.put(114, "114 (D 9)");
        lovNote.put(115, "115 (D#9)");
        lovNote.put(116, "116 (E 9)");
        lovNote.put(117, "117 (F 9)");
        lovNote.put(118, "118 (F#9)");
        lovNote.put(119, "119 (G 9)");
        lovNote.put(120, "120 (G#9)");
        lovNote.put(121, "121 (A 9)");
        lovNote.put(122, "122 (A#9)");
        lovNote.put(123, "123 (B 9)");
        lovNote.put(124, "124 (C 10)");
        lovNote.put(125, "125 (C#10)");
        lovNote.put(126, "126 (D 10)");
        lovNote.put(127, "127 (D#10)");
    }
}
