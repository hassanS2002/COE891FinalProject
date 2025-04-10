import app.tuxguitar.player.base.MidiOutputPortProvider;
import app.tuxguitar.player.base.MidiPlayer;
import app.tuxguitar.util.TGContext;
import app.tuxguitar.util.plugin.TGPluginException;
import org.junit.Test;
import static org.junit.Assert.*;

public class TGMidiOutputPortProviderPluginTest {

    // Mock class for MidiOutputPortProvider
    private static class MockMidiOutputPortProvider implements MidiOutputPortProvider {
        @Override
        public void openAll() throws MidiPluginException {
            // Mocked behavior
        }

        @Override
        public void closeAll() throws MidiPluginException {
            // Mocked behavior
        }
    }

    // Mock Plugin for testing
    private static class MockTGMidiOutputPortProviderPlugin extends TGMidiOutputPortProviderPlugin {
        private MidiOutputPortProvider provider;

        public MockTGMidiOutputPortProviderPlugin(MidiOutputPortProvider provider) {
            this.provider = provider;
        }

        @Override
        protected MidiOutputPortProvider createProvider(TGContext context) throws TGPluginException {
            return provider;
        }
    }

    @Test
    public void testConnect() throws TGPluginException {
        TGContext context = new TGContext();
        MidiOutputPortProvider mockProvider = new MockMidiOutputPortProvider();
        TGMidiOutputPortProviderPlugin plugin = new MockTGMidiOutputPortProviderPlugin(mockProvider);

        plugin.connect(context);

        assertTrue("The provider should be added to MidiPlayer", MidiPlayer.getInstance(context).hasOutputPortProvider(mockProvider));
    }

    @Test
    public void testDisconnect() throws TGPluginException {
        TGContext context = new TGContext();
        MidiOutputPortProvider mockProvider = new MockMidiOutputPortProvider();
        TGMidiOutputPortProviderPlugin plugin = new MockTGMidiOutputPortProviderPlugin(mockProvider);

        plugin.connect(context);
        plugin.disconnect(context);

        assertFalse("The provider should be removed from MidiPlayer", MidiPlayer.getInstance(context).hasOutputPortProvider(mockProvider));
    }

    @Test(expected = TGPluginException.class)
    public void testConnectException() throws TGPluginException {
        TGContext context = new TGContext();
        MidiOutputPortProvider mockProvider = new MockMidiOutputPortProvider() {
            @Override
            public void openAll() throws MidiPluginException {
                throw new MidiPluginException("Test exception");
            }
        };
        TGMidiOutputPortProviderPlugin plugin = new MockTGMidiOutputPortProviderPlugin(mockProvider);

        plugin.connect(context);
    }

    @Test
    public void testDisconnectWithNullProvider() throws TGPluginException {
        TGContext context = new TGContext();
        TGMidiOutputPortProviderPlugin plugin = new MockTGMidiOutputPortProviderPlugin(null);

        plugin.disconnect(context); // Should not throw any exceptions
    }

    @Test
    public void testMultipleConnect() throws TGPluginException {
        TGContext context = new TGContext();
        MidiOutputPortProvider mockProvider = new MockMidiOutputPortProvider();
        TGMidiOutputPortProviderPlugin plugin = new MockTGMidiOutputPortProviderPlugin(mockProvider);

        plugin.connect(context);
        plugin.connect(context); // Call again, should not add again

        assertTrue("The provider should be added only once", MidiPlayer.getInstance(context).hasOutputPortProvider(mockProvider));
    }
}
