package app.tuxguitar.song.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import app.tuxguitar.song.factory.TGFactory;
import app.tuxguitar.song.models.TGTrack;

public class TGTrackTest {

    private TGFactory factory = new TGFactory();

    // ----------------------
    // Method 1: setChannelId
    // ----------------------

    @Test
    public void testSetChannelId_BelowMin() {
        TGTrack track = factory.newTrack();
        track.setChannelId(-1);
        assertEquals(-1, track.getChannelId(), "TGTrack allows negative channel ID");
    }

    @Test
    public void testSetChannelId_AtMin() {
        TGTrack track = factory.newTrack();
        track.setChannelId(0);
        assertEquals(0, track.getChannelId(), "Channel ID should be 0");
    }

    @Test
    public void testSetChannelId_Normal() {
        TGTrack track = factory.newTrack();
        track.setChannelId(8);
        assertEquals(8, track.getChannelId(), "Channel ID should be 8");
    }

    @Test
    public void testSetChannelId_AtMax() {
        TGTrack track = factory.newTrack();
        track.setChannelId(15);
        assertEquals(15, track.getChannelId(), "Channel ID should be 15");
    }

    @Test
    public void testSetChannelId_AboveMax() {
        TGTrack track = factory.newTrack();
        track.setChannelId(16);
        assertEquals(16, track.getChannelId(), "TGTrack allows values above 15");
    }

    // ----------------------
    // Method 2: setNumber
    // ----------------------

    @Test
    public void testSetNumber_Negative() {
        TGTrack track = factory.newTrack();
        track.setNumber(-1);
        assertEquals(-1, track.getNumber(), "Track number accepts negative values");
    }

    @Test
    public void testSetNumber_AtZero() {
        TGTrack track = factory.newTrack();
        track.setNumber(0);
        assertEquals(0, track.getNumber(), "Track number should be 0");
    }

    @Test
    public void testSetNumber_Normal() {
        TGTrack track = factory.newTrack();
        track.setNumber(1);
        assertEquals(1, track.getNumber(), "Track number should be 1");
    }

    @Test
    public void testSetNumber_LargeValue() {
        TGTrack track = factory.newTrack();
        track.setNumber(99);
        assertEquals(99, track.getNumber(), "Track number should be 99");
    }
}
