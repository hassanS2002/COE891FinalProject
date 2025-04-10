public class PlaybackEngine {

    private boolean isPlaying;
    private int currentTempo;

    // Constructor
    public PlaybackEngine() {
        this.isPlaying = false;
        this.currentTempo = 120; // Default tempo in BPM
    }

    /**
     * Starts playback if it is not already playing.
     * @return true if playback starts successfully, false otherwise.
     */
    public boolean startPlayback() {
        if (isPlaying) {
            return false; // Already playing
        }
        isPlaying = true;
        System.out.println("Playback started at " + currentTempo + " BPM");
        return true;
    }

    /**
     * Stops playback if it is currently playing.
     * @return true if playback stops successfully, false otherwise.
     */
    public boolean stopPlayback() {
        if (!isPlaying) {
            return false; // Already stopped
        }
        isPlaying = false;
        System.out.println("Playback stopped");
        return true;
    }

    /**
     * Adjusts the playback tempo within an acceptable range.
     * @param newTempo The new tempo in BPM.
     * @return true if the tempo is successfully adjusted, false otherwise.
     */
    public boolean adjustTempo(int newTempo) {
        if (newTempo < 40 || newTempo > 240) {
            return false; // Tempo out of bounds
        }
        currentTempo = newTempo;
        System.out.println("Tempo adjusted to " + newTempo + " BPM");
        return true;
    }

    /**
     * Checks if playback is currently active.
     * @return true if playing, false otherwise.
     */
    public boolean isPlaying() {
        return isPlaying;
    }
}
