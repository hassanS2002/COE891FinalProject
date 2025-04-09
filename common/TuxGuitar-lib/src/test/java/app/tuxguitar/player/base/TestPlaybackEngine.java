package app.tuxguitar.player.base;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class TestPlaybackEngine {

    private PlaybackEngine playbackEngine;

    @BeforeEach
    public void setUp() {
        playbackEngine = new PlaybackEngine();
    }

    @Test
    @DisplayName("Test setTempo with valid values")
    public void testSetTempoValidValues() {
        // Test lower boundary (40 BPM)
        playbackEngine.setTempo(40);
        assertEquals(40, playbackEngine.getTempo());

        // Test upper boundary (208 BPM)
        playbackEngine.setTempo(208);
        assertEquals(208, playbackEngine.getTempo());

        // Test middle value
        playbackEngine.setTempo(120);
        assertEquals(120, playbackEngine.getTempo());
    }

    @Test
    @DisplayName("Test setTempo with below lower boundary value")
    public void testSetTempoBelowLowerBoundary() {
        // Test value below lower boundary (39 BPM)
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            playbackEngine.setTempo(39);
        });

        String expectedMessage = "Tempo must be between 40 and 208 BPM";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("Test setTempo with above upper boundary value")
    public void testSetTempoAboveUpperBoundary() {
        // Test value above upper boundary (209 BPM)
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            playbackEngine.setTempo(209);
        });

        String expectedMessage = "Tempo must be between 40 and 208 BPM";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}