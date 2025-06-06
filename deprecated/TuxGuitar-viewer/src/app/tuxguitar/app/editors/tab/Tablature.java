package app.tuxguitar.app.editors.tab;

import java.awt.Component;
import java.awt.Rectangle;

import app.tuxguitar.app.TuxGuitar;
import app.tuxguitar.app.editors.TGScrollBar;
import app.tuxguitar.app.system.config.TGConfig;
import app.tuxguitar.awt.graphics.AWTColor;
import app.tuxguitar.awt.graphics.AWTPainter;
import app.tuxguitar.awt.graphics.AWTResourceFactory;
import app.tuxguitar.document.TGDocumentManager;
import app.tuxguitar.graphics.control.TGBeatImpl;
import app.tuxguitar.graphics.control.TGController;
import app.tuxguitar.graphics.control.TGLayout;
import app.tuxguitar.graphics.control.TGLayoutStyles;
import app.tuxguitar.graphics.control.TGLayoutVertical;
import app.tuxguitar.graphics.control.TGMeasureImpl;
import app.tuxguitar.graphics.control.TGResourceBuffer;
import app.tuxguitar.song.managers.TGSongManager;
import app.tuxguitar.song.models.TGBeat;
import app.tuxguitar.song.models.TGDuration;
import app.tuxguitar.song.models.TGMeasure;
import app.tuxguitar.song.models.TGMeasureHeader;
import app.tuxguitar.song.models.TGSong;
import app.tuxguitar.ui.resource.UIRectangle;
import app.tuxguitar.ui.resource.UIResourceFactory;

public class Tablature implements TGController {

	private Component component;
	private TGScrollBar scroll;

	private TGDocumentManager documentManager;
	private Caret caret;
	private float height;
	private TGLayout viewLayout;

	private TGResourceBuffer resourceBuffer;

	private float scrollY;
	private boolean resetScroll;
	protected long lastVScrollTime;
	protected long lastHScrollTime;

	private boolean painting;

	public Tablature(Component component,TGScrollBar scroll) {
		this.component = component;
		this.scroll = scroll;
	}

	public void initDefaults(){
		this.caret = new Caret(this);
	}

	public void updateTablature(){
		getViewLayout().updateSong();
	}

	public void initCaret(){
		this.caret.update(1,TGDuration.QUARTER_TIME,1);
	}

	public synchronized void paintTablature(AWTPainter painter){
		TuxGuitar.instance().lock();
		this.setPainting(true);
		try{
			this.checkScroll();

			Rectangle area = this.component.getBounds();

			this.scrollY = this.scroll.getValue();

			this.getViewLayout().paint(painter, createRectangle(area) , 0, -this.scrollY );

			//this.width = this.viewLayout.getWidth();
			this.height = this.viewLayout.getHeight();

			this.updateScroll();

			if(TuxGuitar.instance().getPlayer().isRunning()){
				redrawPlayingMode(painter);
			}
			// Si no estoy reproduciendo y hay cambios
			// muevo el scroll al compas que tiene el caret
			else if(getCaret().hasChanges()){
				// Mover el scroll puede necesitar redibujar
				// por eso es importante desmarcar los cambios antes de hacer el moveScrollTo
				getCaret().setChanges(false);

				moveScrollTo(getCaret().getMeasure() , area);
			}
		}catch(Throwable throwable){
			throwable.printStackTrace();
		}
		this.setPainting(false);
		TuxGuitar.instance().unlock();
	}

	public void resetScroll(){
		this.resetScroll = true;
	}

	public void checkScroll(){
		if(this.resetScroll){
			this.scroll.setValue(0);
			this.resetScroll = false;
		}
	}

	public void updateScroll(){
		this.scroll.setMaximum((int)Math.max(0, (this.height - this.component.getSize().getHeight()) ) );
	}

	public boolean moveScrollTo(TGMeasureImpl measure){
		return this.moveScrollTo(measure, this.component.getBounds());
	}

	public boolean moveScrollTo(TGMeasureImpl measure, Rectangle area){
		boolean success = false;
		if(measure != null && measure.getTs() != null){
			float mX = measure.getPosY();
			float mWidth = measure.getTs().getSize();
			float marginWidth = getViewLayout().getFirstTrackSpacing();
			boolean forceRedraw = false;

			//Solo se ajusta si es necesario
			//si el largo del compas es mayor al de la pantalla. nunca se puede ajustar a la medida.
			if( mX < 0 || ( (mX + mWidth ) > area.height && (area.height >= mWidth + marginWidth || mX > marginWidth) )  ){
				this.scroll.setValue((this.scrollY + mX) - marginWidth );
				success = true;
			}

			if(!success){
				// Si la seleccion "real" del scroll es distinta a la anterior, se fuerza el redraw
				forceRedraw = (this.scrollY != this.scroll.getValue() );
			}

			if(forceRedraw || success){
				this.component.repaint();
			}
		}
		return success;
	}

	public void beforeRedraw(){
		TuxGuitar.instance().lock();
		this.setPainting(true);
		TuxGuitar.instance().unlock();
	}

	private void redrawPlayingMode(AWTPainter painter){
		try{
			TGMeasureImpl measure = TuxGuitar.instance().getEditorCache().getPlayMeasure();
			TGBeatImpl beat = TuxGuitar.instance().getEditorCache().getPlayBeat();
			if(measure != null && beat != null && measure.hasTrack(getCaret().getTrack().getNumber())){
				this.moveScrollTo(measure);

				if(!measure.isOutOfBounds()){
					getViewLayout().paintPlayMode(painter, measure, beat);
				}
			}
		}catch(Throwable throwable){
			throwable.printStackTrace();
		}
	}

	public boolean isPainting() {
		return this.painting;
	}

	public void setPainting(boolean painting) {
		this.painting = painting;
	}

	public Caret getCaret(){
		return this.caret;
	}

	public TGSongManager getSongManager() {
		return this.documentManager.getSongManager();
	}

	public TGSong getSong() {
		return this.documentManager.getSong();
	}

	public TGDocumentManager getDocumentManager() {
		return documentManager;
	}

	public void setDocumentManager(TGDocumentManager documentManager) {
		this.documentManager = documentManager;
	}

	public TGLayout getViewLayout(){
		return this.viewLayout;
	}

	public void setViewLayout(TGLayout viewLayout){
		if(getViewLayout() != null){
			getViewLayout().disposeLayout();
		}
		this.viewLayout = viewLayout;

		this.reloadStyles();
	}

	public void reloadStyles(){
		if(this.getViewLayout() != null){
			this.getViewLayout().loadStyles(1.0f);
			this.component.setBackground(((AWTColor)getViewLayout().getResources().getBackgroundColor()).getHandle() );
		}
	}

	public UIRectangle createRectangle( Rectangle rectangle ){
		return new UIRectangle(rectangle.x,rectangle.y,rectangle.width,rectangle.height);
	}

	public void reloadViewLayout(){
		setViewLayout(new TGLayoutVertical(this,TGConfig.LAYOUT_STYLE));
	}

	public void dispose(){
		this.getViewLayout().disposeLayout();
	}

	public UIResourceFactory getResourceFactory() {
		return new AWTResourceFactory();
	}

	public TGResourceBuffer getResourceBuffer(){
		if( this.resourceBuffer == null ){
			this.resourceBuffer = new TGResourceBuffer();
		}
		return this.resourceBuffer;
	}

	public int getTrackSelection(){
		if( (getViewLayout().getStyle() & TGLayout.DISPLAY_MULTITRACK) == 0 ){
			return getCaret().getTrack().getNumber();
		}
		return -1;
	}

	public boolean isRunning(TGBeat beat) {
		return ( isRunning( beat.getMeasure() ) && TuxGuitar.instance().getEditorCache().isPlaying(beat.getMeasure(),beat) );
	}

	public boolean isRunning(TGMeasure measure) {
		return ( measure.getTrack().equals(getCaret().getTrack()) && TuxGuitar.instance().getEditorCache().isPlaying( measure ) );
	}

	public boolean isLoopSHeader(TGMeasureHeader measureHeader) {
		return false;
	}

	public boolean isLoopEHeader(TGMeasureHeader measureHeader) {
		return false;
	}

	public TGLayoutStyles getStyles() {
		TGLayoutStyles styles = new TGLayoutStyles();
		styles.setBufferEnabled(true);
		styles.setStringSpacing( TGConfig.TAB_LINE_SPACING );
		styles.setScoreLineSpacing( TGConfig.SCORE_LINE_SPACING );
		styles.setFirstMeasureSpacing(0);
		styles.setMinBufferSeparator(20);
		styles.setMinTopSpacing(30);
		styles.setMinScoreTabSpacing(TGConfig.MIN_SCORE_TABLATURE_SPACING);
		styles.setFirstTrackSpacing(TGConfig.FIRST_TRACK_SPACING);
		styles.setTrackSpacing(TGConfig.TRACK_SPACING);
		styles.setFirstNoteSpacing(10);
		styles.setMeasureLeftSpacing(15);
		styles.setMeasureRightSpacing(15);
		styles.setClefSpacing(30);
		styles.setKeySignatureSpacing(15);
		styles.setTimeSignatureSpacing(15);

		styles.setChordFretIndexSpacing(8);
		styles.setChordStringSpacing(5);
		styles.setChordFretSpacing(6);
		styles.setChordNoteSize(4);
		styles.setRepeatEndingSpacing(20);
		styles.setTextSpacing(15);
		styles.setMarkerSpacing(15);
		styles.setDivisionTypeSpacing(10);
		styles.setEffectSpacing(8);
		styles.setLineWidths(new float[] {0f, 1f, 2f, 3f, 4f, 5f});
		styles.setDurationWidths(new float[] {30f, 25f, 21f, 20f, 19f,18f});

		styles.setDefaultFont(TGConfig.FONT_DEFAULT);
		styles.setNoteFont(TGConfig.FONT_NOTE);
		styles.setLyricFont(TGConfig.FONT_LYRIC);
		styles.setTextFont(TGConfig.FONT_TEXT);
		styles.setMarkerFont(TGConfig.FONT_MARKER);
		styles.setGraceFont(TGConfig.FONT_GRACE);
		styles.setChordFont(TGConfig.FONT_CHORD);
		styles.setChordFretFont(TGConfig.FONT_CHORD_FRET);
		styles.setForegroundColor(TGConfig.COLOR_FOREGROUND);
		styles.setBackgroundColor(TGConfig.COLOR_BACKGROUND);
		styles.setLineColor(TGConfig.COLOR_LINE);
		styles.setScoreNoteColor(TGConfig.COLOR_SCORE_NOTE);
		styles.setTabNoteColor(TGConfig.COLOR_TAB_NOTE);
		styles.setPlayNoteColor(TGConfig.COLOR_PLAY_NOTE);
		styles.setMeasureNumberColor(TGConfig.COLOR_MEASURE_NUMBER);

		return styles;
	}

}