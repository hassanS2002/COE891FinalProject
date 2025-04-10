package app.tuxguitar.song.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import app.tuxguitar.song.factory.TGFactory;
//import app.tuxguitar.song.models.TGBeat;

public class TGBeatTest {

    private TGFactory factory = new TGFactory();

    @Test
    public void testSetStart_BelowZero() {
        TGBeat beat = factory.newBeat();
        beat.setStart(-1);
        assertEquals(-1, beat.getStart(), "TGBeat allows negative values (no input validation)");
    }

    @Test
    public void testSetStart_AtZero() {
        TGBeat beat = factory.newBeat();
        beat.setStart(0);
        assertEquals(0, beat.getStart(), "Start time should be 0");
    }

    @Test
    public void testSetStart_Normal() {
        TGBeat beat = factory.newBeat();
        beat.setStart(960);
        assertEquals(960, beat.getStart(), "Start time should be 960");
    }

    @Test
    public void testSetStart_LargeValue() {
        TGBeat beat = factory.newBeat();
        beat.setStart(100000);
        assertEquals(100000, beat.getStart(), "Start time should be 100000");
    }
}
