package app.tuxguitar.player.base;

/**
 * PlaybackEngine handles playback-related functionality including tempo
 * control.
 */
public class PlaybackEngine {

    private static final int MIN_TEMPO = 40;
    private static final int MAX_TEMPO = 208;
    private int tempo;

    public PlaybackEngine() {
        this.tempo = 120; // Default tempo
    }

    /**
     * Sets the tempo in beats per minute (BPM).
     * 
     * @param tempo The tempo to set (40-208 BPM)
     * @throws IllegalArgumentException if tempo is outside valid range
     */
    public void setTempo(int tempo) {
        if (tempo < MIN_TEMPO || tempo > MAX_TEMPO) {
            throw new IllegalArgumentException("Tempo must be between " + MIN_TEMPO + " and " + MAX_TEMPO + " BPM");
        }
        this.tempo = tempo;
    }

    /**
     * Gets the current tempo in beats per minute (BPM).
     * 
     * @return The current tempo
     */
    public int getTempo() {
        return tempo;
    }
}