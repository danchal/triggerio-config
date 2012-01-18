/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Base;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 *
 * @author DrumTrigger
 */
public class MidiNote {

    private int triggerInputNumber;
    private int triggerMidiNote;

    public static final Map<Integer, String> lovTriggerMidiNote = new LinkedHashMap<Integer, String>();

    static {
        // build LOV midi note
        buildMidiNote();
    }
    
    //----------------------------------------------
    public MidiNote(int triggerInputNumber, int triggerMidiNote) {
        setTriggerInputNumber(triggerInputNumber);
        setTriggerMidiNote(triggerMidiNote);
    }

    //----------------------------------------------
    public final int getTriggerInputNumber() {
        return this.triggerInputNumber;
    }

    //----------------------------------------------
    public final int getTriggerMidiNote() {
        return this.triggerMidiNote;
    }

    //----------------------------------------------
    private void setTriggerInputNumber(int triggerInputNumber) {
        this.triggerInputNumber = triggerInputNumber;
        Common.logger.log(Level.FINEST, "triggerInputNumber<{0}>", this.triggerInputNumber);
    }

    //----------------------------------------------
    public final void setTriggerMidiNote(int triggerMidiNote) {
        if ( lovTriggerMidiNote.containsKey(triggerMidiNote) ){
            this.triggerMidiNote = triggerMidiNote;
            Common.logger.log(Level.FINEST, "triggerMidiNote<{0}>", this.triggerMidiNote);
        }
        else{
            Common.logger.log(Level.WARNING, "Unrecognised triggerMidiNote<{0}>", triggerMidiNote);
        }
    }
    
    private static void buildMidiNote() {
        lovTriggerMidiNote.put(0, "0 (C 0)");
        lovTriggerMidiNote.put(1, "1 (C#0)");
        lovTriggerMidiNote.put(2, "2 (D 0)");
        lovTriggerMidiNote.put(3, "3 (D#0)");
        lovTriggerMidiNote.put(4, "4 (E 0)");
        lovTriggerMidiNote.put(5, "5 (F 0)");
        lovTriggerMidiNote.put(6, "6 (F#0)");
        lovTriggerMidiNote.put(7, "7 (G 0)");
        lovTriggerMidiNote.put(8, "8 (G#0)");
        lovTriggerMidiNote.put(9, "9 (A 0)");
        lovTriggerMidiNote.put(10, "10 (A#0)");
        lovTriggerMidiNote.put(11, "11 (B 0)");
        lovTriggerMidiNote.put(12, "12 (C 1)");
        lovTriggerMidiNote.put(13, "13 (C#1)");
        lovTriggerMidiNote.put(14, "14 (D 1)");
        lovTriggerMidiNote.put(15, "15 (D#1)");
        lovTriggerMidiNote.put(16, "16 (E 1)");
        lovTriggerMidiNote.put(17, "17 (F 1)");
        lovTriggerMidiNote.put(18, "18 (F#1)");
        lovTriggerMidiNote.put(19, "19 (G 1)");
        lovTriggerMidiNote.put(20, "20 (G#1)");
        lovTriggerMidiNote.put(21, "21 (A 1)");
        lovTriggerMidiNote.put(22, "22 (A#1)");
        lovTriggerMidiNote.put(23, "23 (B 1)");
        lovTriggerMidiNote.put(24, "24 (C 2)");
        lovTriggerMidiNote.put(25, "25 (C#2)");
        lovTriggerMidiNote.put(26, "26 (D 2)");
        lovTriggerMidiNote.put(27, "27 (D#2)");
        lovTriggerMidiNote.put(28, "28 (E 2)");
        lovTriggerMidiNote.put(29, "29 (F 2)");
        lovTriggerMidiNote.put(30, "30 (F#2)");
        lovTriggerMidiNote.put(31, "31 (G 2)");
        lovTriggerMidiNote.put(32, "32 (G#2)");
        lovTriggerMidiNote.put(33, "33 (A 2)");
        lovTriggerMidiNote.put(34, "34 (A#2)");
        lovTriggerMidiNote.put(35, "35 (B 2)");
        lovTriggerMidiNote.put(36, "36 (C 3)");
        lovTriggerMidiNote.put(37, "37 (C#3)");
        lovTriggerMidiNote.put(38, "38 (D 3)");
        lovTriggerMidiNote.put(39, "39 (D#3)");
        lovTriggerMidiNote.put(40, "40 (E 3)");
        lovTriggerMidiNote.put(41, "41 (F 3)");
        lovTriggerMidiNote.put(42, "42 (F#3)");
        lovTriggerMidiNote.put(43, "43 (G 3)");
        lovTriggerMidiNote.put(44, "44 (G#3)");
        lovTriggerMidiNote.put(45, "45 (A 3)");
        lovTriggerMidiNote.put(46, "46 (A#3)");
        lovTriggerMidiNote.put(47, "47 (B 3)");
        lovTriggerMidiNote.put(48, "48 (C 4)");
        lovTriggerMidiNote.put(49, "49 (C#4)");
        lovTriggerMidiNote.put(50, "50 (D 4)");
        lovTriggerMidiNote.put(51, "51 (D#4)");
        lovTriggerMidiNote.put(52, "52 (E 4)");
        lovTriggerMidiNote.put(53, "53 (F 4)");
        lovTriggerMidiNote.put(54, "54 (F#4)");
        lovTriggerMidiNote.put(55, "55 (G 4)");
        lovTriggerMidiNote.put(56, "56 (G#4)");
        lovTriggerMidiNote.put(57, "57 (A 4)");
        lovTriggerMidiNote.put(58, "58 (A#4)");
        lovTriggerMidiNote.put(59, "59 (B 4)");
        lovTriggerMidiNote.put(60, "60 (C 5)");
        lovTriggerMidiNote.put(61, "61 (C#5)");
        lovTriggerMidiNote.put(62, "62 (D 5)");
        lovTriggerMidiNote.put(63, "63 (D#5)");
        lovTriggerMidiNote.put(64, "64 (E 5)");
        lovTriggerMidiNote.put(65, "65 (F 5)");
        lovTriggerMidiNote.put(66, "66 (F#5)");
        lovTriggerMidiNote.put(67, "67 (G 5)");
        lovTriggerMidiNote.put(68, "68 (G#5)");
        lovTriggerMidiNote.put(69, "69 (A 5)");
        lovTriggerMidiNote.put(70, "70 (A#5)");
        lovTriggerMidiNote.put(71, "71 (B 5)");
        lovTriggerMidiNote.put(72, "72 (C 6)");
        lovTriggerMidiNote.put(73, "73 (C#6)");
        lovTriggerMidiNote.put(74, "74 (D 6)");
        lovTriggerMidiNote.put(75, "75 (D#6)");
        lovTriggerMidiNote.put(76, "76 (E 6)");
        lovTriggerMidiNote.put(77, "77 (F 6)");
        lovTriggerMidiNote.put(78, "78 (F#6)");
        lovTriggerMidiNote.put(79, "79 (G 6)");
        lovTriggerMidiNote.put(80, "80 (G#6)");
        lovTriggerMidiNote.put(81, "81 (A 6)");
        lovTriggerMidiNote.put(82, "82 (A#6)");
        lovTriggerMidiNote.put(83, "83 (B 6)");
        lovTriggerMidiNote.put(84, "84 (C 7)");
        lovTriggerMidiNote.put(85, "85 (C#7)");
        lovTriggerMidiNote.put(86, "86 (D 7)");
        lovTriggerMidiNote.put(87, "87 (D#7)");
        lovTriggerMidiNote.put(88, "88 (E 7)");
        lovTriggerMidiNote.put(89, "89 (F 7)");
        lovTriggerMidiNote.put(90, "90 (F#7)");
        lovTriggerMidiNote.put(91, "91 (G 7)");
        lovTriggerMidiNote.put(92, "92 (G#7)");
        lovTriggerMidiNote.put(93, "93 (A 7)");
        lovTriggerMidiNote.put(94, "94 (A#7)");
        lovTriggerMidiNote.put(95, "95 (B 7)");
        lovTriggerMidiNote.put(96, "96 (C 8)");
        lovTriggerMidiNote.put(97, "97 (C#8)");
        lovTriggerMidiNote.put(98, "98 (D 8)");
        lovTriggerMidiNote.put(99, "99 (D#8)");
        lovTriggerMidiNote.put(100, "100 (E 8)");
        lovTriggerMidiNote.put(101, "101 (F 8)");
        lovTriggerMidiNote.put(102, "102 (F#8)");
        lovTriggerMidiNote.put(103, "103 (G 8)");
        lovTriggerMidiNote.put(104, "104 (G#8)");
        lovTriggerMidiNote.put(105, "105 (A 8)");
        lovTriggerMidiNote.put(106, "106 (A#8)");
        lovTriggerMidiNote.put(107, "107 (B 8)");
        lovTriggerMidiNote.put(108, "108 (C 9)");
        lovTriggerMidiNote.put(109, "109 (C#9)");
        lovTriggerMidiNote.put(110, "110 (D 9)");
        lovTriggerMidiNote.put(111, "111 (D#9)");
        lovTriggerMidiNote.put(112, "112 (E 9)");
        lovTriggerMidiNote.put(113, "113 (C#9)");
        lovTriggerMidiNote.put(114, "114 (D 9)");
        lovTriggerMidiNote.put(115, "115 (D#9)");
        lovTriggerMidiNote.put(116, "116 (E 9)");
        lovTriggerMidiNote.put(117, "117 (F 9)");
        lovTriggerMidiNote.put(118, "118 (F#9)");
        lovTriggerMidiNote.put(119, "119 (G 9)");
        lovTriggerMidiNote.put(120, "120 (G#9)");
        lovTriggerMidiNote.put(121, "121 (A 9)");
        lovTriggerMidiNote.put(122, "122 (A#9)");
        lovTriggerMidiNote.put(123, "123 (B 9)");
        lovTriggerMidiNote.put(124, "124 (C 10)");
        lovTriggerMidiNote.put(125, "125 (C#10)");
        lovTriggerMidiNote.put(126, "126 (D 10)");
        lovTriggerMidiNote.put(127, "127 (D#10)");
    }
}
