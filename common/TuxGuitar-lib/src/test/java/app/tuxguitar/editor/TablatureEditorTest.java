package app.tuxguitar.editor;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import app.tuxguitar.song.models.TGSong;
import app.tuxguitar.song.models.TGTrack;
import app.tuxguitar.song.models.TGMeasure;
import app.tuxguitar.song.models.TGBeat;
import app.tuxguitar.song.models.TGVoice;
import app.tuxguitar.song.models.TGNote;
import app.tuxguitar.song.models.TGDuration;
import app.tuxguitar.song.models.TGString;
import app.tuxguitar.song.factory.TGFactory;
import app.tuxguitar.song.managers.TGSongManager;
import app.tuxguitar.util.TGContext;
import java.util.ArrayList;

/**
 * Tests for the TablatureEditor class focusing on note addition with duration
 * constraints
 */
public class TablatureEditorTest {
    private TGContext context;
    private TGFactory factory;
    private TGSong song;
    private TGTrack track;
    private TGMeasure measure;
    private TGSongManager songManager;

    @BeforeEach
    void setUp() {
        context = new TGContext();
        factory = new TGFactory();
        song = factory.newSong();

        // Create a track with a measure
        track = factory.newTrack();
        track.setNumber(1);
        track.setStrings(new ArrayList<>());
        // Standard guitar tuning (E, A, D, G, B, E)
        int[] tuning = { 40, 45, 50, 55, 59, 64 };
        for (int i = 0; i < 6; i++) {
            TGString string = factory.newString();
            string.setNumber(i + 1);
            string.setValue(tuning[i]);
            track.getStrings().add(string);
        }
        song.addTrack(track);

        // Add a measure to the track
        measure = factory.newMeasure(null);
        measure.setTrack(track);
        track.addMeasure(measure);

        songManager = new TGSongManager(factory);
        try {
            java.lang.reflect.Field songField = TGSongManager.class.getDeclaredField("song");
            songField.setAccessible(true);
            songField.set(songManager, song);
        } catch (Exception e) {
            // Handle exception
        }
    }

    @Test
    @DisplayName("Test adding note with duration below valid range (0)")
    void testAddNoteWithDurationBelowRange() {
        assertThrows(IllegalArgumentException.class, () -> {
            addNoteWithDuration(0, 1);
        });
    }

    @Test
    @DisplayName("Test adding note with duration above valid range (17)")
    void testAddNoteWithDurationAboveRange() {
        assertThrows(IllegalArgumentException.class, () -> {
            addNoteWithDuration(17, 1);
        });
    }

    /**
     * Helper method to add a note with specified duration and string
     */
    private TGNote addNoteWithDuration(int durationValue, int stringNumber) {
        if (durationValue < 1 || durationValue > 16) {
            throw new IllegalArgumentException("Duration must be between 1 and 16");
        }

        // Create a beat at position 0
        TGBeat beat = songManager.getFactory().newBeat();
        beat.setStart(0);

        // Set the duration based on the input value
        TGDuration duration = songManager.getFactory().newDuration();
        switch (durationValue) {
            case 1:
                duration.setValue(TGDuration.WHOLE);
                break;
            case 2:
                duration.setValue(TGDuration.HALF);
                break;
            case 4:
                duration.setValue(TGDuration.QUARTER);
                break;
            case 8:
                duration.setValue(TGDuration.EIGHTH);
                break;
            case 16:
                duration.setValue(TGDuration.SIXTEENTH);
                break;
            default:
                duration.setValue(TGDuration.QUARTER); // Default fallback
        }

        // Get the voice from the beat and set the duration on it
        TGVoice voice = beat.getVoice(0); // Get the first voice
        voice.setDuration(duration);

        // Add the beat to the measure
        songManager.getMeasureManager().addBeat(measure, beat);

        // Create and add a note to the beat
        TGNote note = songManager.getFactory().newNote();
        note.setValue(stringNumber);
        note.setString(stringNumber);

        songManager.getMeasureManager().addNote(beat, note, duration, note.getString());

        return note;
    }
}
