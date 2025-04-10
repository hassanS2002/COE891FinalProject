import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlaybackEngineTest {

    private PlaybackEngine engine;

    @BeforeEach
    public void setUp() {
        engine = new PlaybackEngine();
    }

    @Test
    public void testStartPlayback_WhenStopped() {
        assertTrue(engine.startPlayback());
        assertTrue(engine.isPlaying());
    }

    @Test
    public void testStartPlayback_WhenAlreadyPlaying() {
        engine.startPlayback();
        assertFalse(engine.startPlayback());
    }

    @Test
    public void testStopPlayback_WhenPlaying() {
        engine.startPlayback();
        assertTrue(engine.stopPlayback());
        assertFalse(engine.isPlaying());
    }

    @Test
    public void testStopPlayback_WhenAlreadyStopped() {
        assertFalse(engine.stopPlayback());
    }

    @Test
    public void testAdjustTempo_Valid() {
        assertTrue(engine.adjustTempo(100));
    }

    @Test
    public void testAdjustTempo_Invalid() {
        assertFalse(engine.adjustTempo(20));
        assertFalse(engine.adjustTempo(300));
    }

    @Test
    public void testAdjustTempo_LowerBoundary() {
        assertTrue(engine.adjustTempo(40)); // ✅ Lower boundary value
    }

    @Test
    public void testAdjustTempo_UpperBoundary() {
        assertTrue(engine.adjustTempo(240)); // ✅ Upper boundary value
    }

    @Test
    public void testAdjustTempo_JustBelowLowerBoundary() {
        assertFalse(engine.adjustTempo(39)); // ✅ Test below lower boundary
    }

    @Test
    public void testAdjustTempo_JustAboveUpperBoundary() {
        assertFalse(engine.adjustTempo(241)); // ✅ Test above upper boundary
    }

    @Test
    public void testPlaybackState_Toggle() {
        engine.startPlayback();
        engine.stopPlayback();
        assertFalse(engine.isPlaying()); // ✅ Check state after start/stop
    }

    // ✅ Add this new test here:
    @Test
    public void testStartStopStartSequence() {
        engine.startPlayback();
        engine.stopPlayback();
        assertTrue(engine.startPlayback()); // ✅ Start again after stopping
    }
}
