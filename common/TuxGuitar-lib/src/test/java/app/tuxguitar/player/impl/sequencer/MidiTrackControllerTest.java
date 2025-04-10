package app.tuxguitar.player.impl.sequencer;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import app.tuxguitar.player.impl.sequencer.MidiTrackController;
import app.tuxguitar.player.impl.sequencer.MidiTrack;
import app.tuxguitar.player.base.MidiPlayerException;

public class MidiTrackControllerTest {
    private MidiTrackController controller;
    @Before
    public void setUp() {
        // Create a controller with 3 tracks for testing
        controller = new MidiTrackController(null); // Pass in null or mock sequencer if necessary
        controller.init(3);
    }
    @Test
    public void testSetSolo() throws MidiPlayerException {
        // Test setting a track to solo
        controller.setSolo(0, true);
        assertTrue("Track 0 should be in solo", controller.isSolo(0));

        // Test that soloing a track un-mutes it
        controller.setMute(0, true); // Initially mute
        controller.setSolo(0, true); // Solo should override mute
        assertFalse("Track 0 should not be muted when soloed", controller.isMute(0));

        // Test that only one track can be soloed at a time
        controller.setSolo(1, true);
        assertTrue("Track 1 should be in solo", controller.isSolo(1));
        assertFalse("Track 0 should no longer be soloed", controller.isSolo(0));
    }
    @Test
    public void testSetMute() throws MidiPlayerException {
        // Test setting a track to mute
        controller.setMute(1, true);
        assertTrue("Track 1 should be muted", controller.isMute(1));

        // Test that muting a track disables solo
        controller.setSolo(1, true); // Initially solo
        controller.setMute(1, true); // Should automatically disable solo
        assertFalse("Track 1 should no longer be soloed after mute", controller.isSolo(1));

        // Test that muting does not affect other tracks
        controller.setMute(0, true);
        assertTrue("Track 0 should be muted", controller.isMute(0));
        assertFalse("Track 2 should not be muted", controller.isMute(2));
    }
    @Test
    public void testCheckAnySolo() throws MidiPlayerException {
        // Test checking if any track is soloed
        controller.setSolo(0, true);
        assertTrue("There should be at least one soloed track", controller.isAnySolo());

        // Test that if no tracks are soloed, isAnySolo returns false
        controller.setSolo(0, false);
        assertFalse("There should be no soloed tracks", controller.isAnySolo());
    }
    @Test
    public void testClearTracks() throws MidiPlayerException {
        // Initially clear tracks and check that there are no solos or mutes
        controller.clearTracks();
        assertFalse("There should be no tracks left", controller.isSolo(0));
        assertFalse("There should be no tracks left", controller.isMute(0));
    }
}
