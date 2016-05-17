package com.doterob.transparencia.connector.contract.extractor.pdf.itext;

import com.itextpdf.text.pdf.parser.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ScqLocationTextExtractionStrategy implements TextExtractionStrategy {

    private static final Logger LOG = LogManager.getLogger(ScqLocationTextExtractionStrategy.class);

    static boolean DUMP_STATE = false;
    private final List<ScqLocationTextExtractionStrategy.TextChunk> locationalResult;
    private final ScqLocationTextExtractionStrategy.TextChunkLocationStrategy tclStrat;

    public ScqLocationTextExtractionStrategy() {
        this(new ScqLocationTextExtractionStrategy.TextChunkLocationStrategy() {
            public ScqLocationTextExtractionStrategy.TextChunkLocation createLocation(TextRenderInfo renderInfo, LineSegment baseline) {
                return new ScqLocationTextExtractionStrategy.TextChunkLocationDefaultImp(baseline.getStartPoint(), baseline.getEndPoint(), renderInfo.getSingleSpaceWidth());
            }
        });
    }

    public ScqLocationTextExtractionStrategy(ScqLocationTextExtractionStrategy.TextChunkLocationStrategy strat) {
        this.locationalResult = new ArrayList();
        this.tclStrat = strat;
    }

    public void beginTextBlock() {
    }

    public void endTextBlock() {
    }

    private boolean startsWithSpace(String str) {
        return str.length() == 0?false:str.charAt(0) == 32;
    }

    private boolean endsWithSpace(String str) {
        return str.length() == 0?false:str.charAt(str.length() - 1) == 32;
    }

    private List<ScqLocationTextExtractionStrategy.TextChunk> filterTextChunks(List<ScqLocationTextExtractionStrategy.TextChunk> textChunks, ScqLocationTextExtractionStrategy.TextChunkFilter filter) {
        if(filter == null) {
            return textChunks;
        } else {
            ArrayList filtered = new ArrayList();
            Iterator var4 = textChunks.iterator();

            while(var4.hasNext()) {
                ScqLocationTextExtractionStrategy.TextChunk textChunk = (ScqLocationTextExtractionStrategy.TextChunk)var4.next();
                if(filter.accept(textChunk)) {
                    filtered.add(textChunk);
                }
            }

            return filtered;
        }
    }

    protected boolean isChunkAtWordBoundary(ScqLocationTextExtractionStrategy.TextChunk chunk, ScqLocationTextExtractionStrategy.TextChunk previousChunk) {
        return chunk.getLocation().isAtWordBoundary(previousChunk.getLocation());
    }

    public String getResultantText(ScqLocationTextExtractionStrategy.TextChunkFilter chunkFilter) {
        if(DUMP_STATE) {
            this.dumpState();
        }

        List filteredTextChunks = this.filterTextChunks(this.locationalResult, chunkFilter);
        Collections.sort(filteredTextChunks);
        StringBuilder sb = new StringBuilder();
        ScqLocationTextExtractionStrategy.TextChunk lastChunk = null;

        ScqLocationTextExtractionStrategy.TextChunk chunk;
        for(Iterator var5 = filteredTextChunks.iterator(); var5.hasNext(); lastChunk = chunk) {
            chunk = (ScqLocationTextExtractionStrategy.TextChunk)var5.next();
            if(lastChunk == null) {
                sb.append(getChuckText(chunk));
            } else if(chunk.sameLine(lastChunk)) {
                if(this.isChunkAtWordBoundary(chunk, lastChunk) && !this.startsWithSpace(chunk.text) && !this.endsWithSpace(lastChunk.text)) {
                    sb.append(" @@@ ");
                }
                sb.append('\n');
                sb.append(getChuckText(chunk));
            } else {
                sb.append('\n');
                sb.append(getChuckText(chunk));
            }
        }

        return sb.toString();
    }

    private String getChuckText(ScqLocationTextExtractionStrategy.TextChunk chunk){
        return "[" + Math.round(chunk.getStartLocation().get(0)) + "," + Math.round(chunk.getStartLocation().get(1)) +"," + Math.round(chunk.getEndLocation().get(0)) + "," + Math.round(chunk.getEndLocation().get(1)) +"]----->"+chunk.text;
    }

    public String getResultantText() {
        return this.getResultantText((ScqLocationTextExtractionStrategy.TextChunkFilter)null);
    }

    private void dumpState() {
        Iterator var1 = this.locationalResult.iterator();

        while(var1.hasNext()) {
            ScqLocationTextExtractionStrategy.TextChunk location = (ScqLocationTextExtractionStrategy.TextChunk)var1.next();
            location.printDiagnostics();
        }

    }

    public void renderText(TextRenderInfo renderInfo) {
        LineSegment segment = renderInfo.getBaseline();
        if(renderInfo.getRise() != 0.0F) {
            Matrix tc = new Matrix(0.0F, -renderInfo.getRise());
            segment = segment.transformBy(tc);
        }

        ScqLocationTextExtractionStrategy.TextChunk tc1 = new ScqLocationTextExtractionStrategy.TextChunk(renderInfo.getText(), this.tclStrat.createLocation(renderInfo, segment));
        this.locationalResult.add(tc1);
    }

    private static int compareInts(int int1, int int2) {
        return int1 == int2?0:(int1 < int2?-1:1);
    }

    public void renderImage(ImageRenderInfo renderInfo) {
    }

    public interface TextChunkFilter {
        boolean accept(ScqLocationTextExtractionStrategy.TextChunk var1);
    }

    public static class TextChunk implements Comparable<ScqLocationTextExtractionStrategy.TextChunk> {
        private final String text;
        private final ScqLocationTextExtractionStrategy.TextChunkLocation location;

        public TextChunk(String string, Vector startLocation, Vector endLocation, float charSpaceWidth) {
            this(string, new ScqLocationTextExtractionStrategy.TextChunkLocationDefaultImp(startLocation, endLocation, charSpaceWidth));
        }

        public TextChunk(String string, ScqLocationTextExtractionStrategy.TextChunkLocation loc) {
            this.text = string;
            this.location = loc;
        }

        public String getText() {
            return this.text;
        }

        protected ScqLocationTextExtractionStrategy.TextChunkLocation getLocation() {
            return this.location;
        }

        public Vector getStartLocation() {
            return this.location.getStartLocation();
        }

        public Vector getEndLocation() {
            return this.location.getEndLocation();
        }

        public float getCharSpaceWidth() {
            return this.location.getCharSpaceWidth();
        }

        public float distanceFromEndOf(ScqLocationTextExtractionStrategy.TextChunk other) {
            return this.location.distanceFromEndOf(other.location);
        }

        private void printDiagnostics() {
            LOG.debug("Text (@" + this.location.getStartLocation() + " -> " + this.location.getEndLocation() + "): " + this.text);
            LOG.debug("orientationMagnitude: " + this.location.orientationMagnitude());
            LOG.debug("distPerpendicular: " + this.location.distPerpendicular());
            LOG.debug("distParallel: " + this.location.distParallelStart());
        }

        public int compareTo(ScqLocationTextExtractionStrategy.TextChunk rhs) {
            return this.location.compareTo(rhs.location);
        }

        private boolean sameLine(ScqLocationTextExtractionStrategy.TextChunk lastChunk) {
            return this.getLocation().sameLine(lastChunk.getLocation());
        }
    }

    private static class TextChunkLocationDefaultImp implements ScqLocationTextExtractionStrategy.TextChunkLocation {
        private final Vector startLocation;
        private final Vector endLocation;
        private final Vector orientationVector;
        private final int orientationMagnitude;
        private final int distPerpendicular;
        private final float distParallelStart;
        private final float distParallelEnd;
        private final float charSpaceWidth;

        public TextChunkLocationDefaultImp(Vector startLocation, Vector endLocation, float charSpaceWidth) {
            this.startLocation = startLocation;
            this.endLocation = endLocation;
            this.charSpaceWidth = charSpaceWidth;
            Vector oVector = endLocation.subtract(startLocation);
            if(oVector.length() == 0.0F) {
                oVector = new Vector(1.0F, 0.0F, 0.0F);
            }

            this.orientationVector = oVector.normalize();
            this.orientationMagnitude = (int)(Math.atan2((double)this.orientationVector.get(1), (double)this.orientationVector.get(0)) * 1000.0D);
            Vector origin = new Vector(0.0F, 0.0F, 1.0F);
            this.distPerpendicular = (int)startLocation.subtract(origin).cross(this.orientationVector).get(2);
            this.distParallelStart = this.orientationVector.dot(startLocation);
            this.distParallelEnd = this.orientationVector.dot(endLocation);
        }

        public int orientationMagnitude() {
            return this.orientationMagnitude;
        }

        public int distPerpendicular() {
            return this.distPerpendicular;
        }

        public float distParallelStart() {
            return this.distParallelStart;
        }

        public float distParallelEnd() {
            return this.distParallelEnd;
        }

        public Vector getStartLocation() {
            return this.startLocation;
        }

        public Vector getEndLocation() {
            return this.endLocation;
        }

        public float getCharSpaceWidth() {
            return this.charSpaceWidth;
        }

        public boolean sameLine(ScqLocationTextExtractionStrategy.TextChunkLocation as) {
            return this.orientationMagnitude() == as.orientationMagnitude() && this.distPerpendicular() == as.distPerpendicular();
        }

        public float distanceFromEndOf(ScqLocationTextExtractionStrategy.TextChunkLocation other) {
            float distance = this.distParallelStart() - other.distParallelEnd();
            return distance;
        }

        public boolean isAtWordBoundary(ScqLocationTextExtractionStrategy.TextChunkLocation previous) {
            if(this.getCharSpaceWidth() < 0.1F) {
                return false;
            } else {
                float dist = this.distanceFromEndOf(previous);
                return dist < -this.getCharSpaceWidth() || dist > this.getCharSpaceWidth() / 2.0F;
            }
        }

        public int compareTo(ScqLocationTextExtractionStrategy.TextChunkLocation other) {
            if(this == other) {
                return 0;
            } else {

                int rslt = ScqLocationTextExtractionStrategy.compareInts(this.orientationMagnitude(), other.orientationMagnitude());
                if(rslt != 0) {
                    return rslt;
                } else {
                    rslt = ScqLocationTextExtractionStrategy.compareInts(this.distPerpendicular(), other.distPerpendicular());
                    return rslt != 0?rslt:Float.compare(this.distParallelStart(), other.distParallelStart());
                }
            }
        }
    }

    public interface TextChunkLocation extends Comparable<ScqLocationTextExtractionStrategy.TextChunkLocation> {
        float distParallelEnd();

        float distParallelStart();

        int distPerpendicular();

        float getCharSpaceWidth();

        Vector getEndLocation();

        Vector getStartLocation();

        int orientationMagnitude();

        boolean sameLine(ScqLocationTextExtractionStrategy.TextChunkLocation var1);

        float distanceFromEndOf(ScqLocationTextExtractionStrategy.TextChunkLocation var1);

        boolean isAtWordBoundary(ScqLocationTextExtractionStrategy.TextChunkLocation var1);
    }

    public interface TextChunkLocationStrategy {
        ScqLocationTextExtractionStrategy.TextChunkLocation createLocation(TextRenderInfo var1, LineSegment var2);
    }
}
