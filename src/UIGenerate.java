import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JLabel;
import javax.swing.JButton;

public class UIGenerate extends generalPanel{
	private JPanel pane;
	private JLabel bg;
	private String modes[] = {"img/exit/gameMode.png","img/exit/experimentMode.png"};
	protected int modeNum = 0, sliderValue = 10;
	protected JButton okay,  mode, cancel;
	protected JSlider genSlider;
	
	UIGenerate(JPanel pane){
		sop("Entered UIGenerate");
		this.pane = pane;
		pane.setOpaque(true);
		modeNum = 0;
		
		okay = addButton(pane, "img/exit/okay.png", "img/exit/h_okay.png", 260, 443);
		cancel = addButton(pane, "img/exit/cancel.png", "img/exit/h_cancel.png", 445, 443);
		mode = addButton(pane, modes[modeNum],modes[modeNum], 254,362);
		
		genSlider = addSlider(pane, 275, 256, 220, 100);
		
		bg = addLabel(pane,"img/bg/generation.png",100,50);
		
		pane.setVisible(true);
	}

	protected void setSliderValue() {
		sliderValue = genSlider.getValue();
	}

	protected void changeMode(){
		modeNum = (modeNum == 0) ? 1 : 0;
		changePicture(mode, modes[modeNum]);
	}

	protected void decompose(){
		pane.removeAll();
		pane.setVisible(false);
		mode = null;
		genSlider = null;
		bg = null;
	}
}