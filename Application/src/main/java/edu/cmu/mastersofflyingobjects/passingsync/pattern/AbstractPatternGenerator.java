package edu.cmu.mastersofflyingobjects.passingsync.pattern;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * this contains the core mechanisms for patterns
 * <p/>
 * subclasses for siteswaps, synchronous patterns and random patterns possible
 */
public abstract class AbstractPatternGenerator {

    public abstract StartPos getStart(Passer p);

    public abstract Display getDisplay(Passer p);

    /**
     * returns actions for zero to all both passers
     */
    public abstract Map<Passer, Pair<Side, Character>> step();

    public enum Passer {
        A,//A has straight singles
        B//B has crossing singles
    }

    public enum Side {LEFT, RIGHT}

    public static class StartPos {
        final int leftHand;
        final int rightHand;
        final Side firstHand;
        final List<Character> initialSequence;

        StartPos(int rightHand, int leftHand, Side firstHand, List<Character> initialSequence) {
            this.leftHand = leftHand;
            this.rightHand = rightHand;
            this.firstHand = firstHand;
            this.initialSequence = initialSequence;
        }

        @Override
        public String toString() {
            return String.format("Start with %d right and %d left; start %s with %s", rightHand, leftHand, firstHand.toString(), initialSequence);
        }
    }

    public static class Display {

        public final List<Character> seqA;
        public final List<Character> seqB;
        public final int highlight;//index in first+second list (between zero and (seqA++seqB).length

        Display(List<Character> seqA,
                List<Character> seqB,
                int highlight) {
            this.seqA = seqA;
            this.seqB = seqB;
            assert (seqA.size() == seqB.size());
            this.highlight = highlight;
        }

        public static Display parse(String s) {
            String[] parts = s.split(";");
            final List<Character> seqA = new ArrayList<>();
            final List<Character> seqB = new ArrayList<>();
            assert parts.length == 3;
            for (char c : parts[0].toCharArray())
                seqA.add(c);
            for (char c : parts[1].toCharArray())
                seqB.add(c);
            return new Display(seqA, seqB, Integer.valueOf(parts[2]));
        }

        @Override
        public String toString() {
            StringBuffer out = new StringBuffer();
            StringBuffer highlightStr = new StringBuffer();
            int idx = -1;
            for (Character a : seqA) {
                out.append(a);
                out.append(" ");
                idx++;
                if (idx == highlight)
                    highlightStr.append("* ");
                else
                    highlightStr.append("  ");
            }
            out.append("\n");
            out.append(highlightStr);
            out.append("\n");
            for (Character a : seqB) {
                out.append(a);
                out.append(" ");
            }
            out.append("\n");
            return out.toString();
        }

        public String serialize() {
            StringBuffer out = new StringBuffer();
            for (Character a : seqA)
                out.append(a);
            out.append(';');
            for (Character a : seqB)
                out.append(a);
            out.append(';');
            out.append(highlight);
            return out.toString();
        }
    }


}
