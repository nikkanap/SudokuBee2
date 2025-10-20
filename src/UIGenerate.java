import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JLabel;
import javax.swing.JButton;

public class UIGenerate extends generalPanel{
	private JPanel pane;
	private JLabel bg;
	private String modes[] = {"img/exit/gameMode.png","img/exit/experimentMode.png"};
	protected int chosen = 1, sliderValue = 10;
	protected JButton okay, cancel, empty, manual, load;
	protected JSlider genSlider;
	
	UIGenerate(JPanel pane){
		sop("Entered UIGenerate");
		this.pane = pane;
		pane.setOpaque(true);		
		okay = addButton(pane, "img/exit/okay.png", "img/exit/h_okay.png", 289, 468);
		cancel = addButton(pane, "img/exit/cancel.png", "img/exit/h_cancel.png", 430, 468);

		empty = addButton(pane, modes[1], modes[1], 240,366);
		manual = addButton(pane, modes[0], modes[0], 240,406);
		load = addButton(pane, modes[0], modes[0], 417,366);

		genSlider = addSlider(pane, 285, 226, 220, 100);
		
		bg = addLabel(pane,"img/bg/generation.png",100,50);
		
		pane.setVisible(true);
	}

	protected void setSliderValue() {
		sliderValue = genSlider.getValue();
	}

	protected void changeMode(int chosen){
		this.chosen = chosen;

		switch (chosen) {
			case 1 -> {
				changePicture(empty, modes[1]);
				changePicture(manual, modes[0]);
				changePicture(load, modes[0]);
			}
			case 2 -> {
				changePicture(manual, modes[1]);
				changePicture(empty, modes[0]);
				changePicture(load, modes[0]);
			}
			case 3 -> {
				changePicture(load, modes[1]);
				changePicture(empty, modes[0]);
				changePicture(manual, modes[0]);
			}
		}
	}

	protected void decompose(){
		pane.removeAll();
		pane.setVisible(false);
		empty = manual = load = null;
		genSlider = null;
		bg = null;
	}
}