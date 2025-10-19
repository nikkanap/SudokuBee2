import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Cursor;

public class UIStatus extends generalPanel{
	private JPanel pane;
	private JLabel bg;
	protected JButton yes1, no1, yes2, no2, save, open, reset;
	
	UIStatus(String str, JPanel pane){
		this.pane = pane;
		// for create mode
		yes1 = addButton(pane, "img/exit/yes.png", "img/exit/h_yes.png",296,27);
		no1 = addButton(pane, "img/exit/no.png", "img/exit/h_no.png",403,27);

		// for game mode
		save = addButton(pane, "img/game/save.png","img/game/save.png",385,18);
		open = addButton(pane, "img/game/open.png","img/game/open.png",327,18);
		reset = addButton(pane, "img/game/reset.png","img/game/reset.png",443,18);

		// for fill mode
		yes2 = addButton(pane, "img/exit/yes.png", "img/exit/h_yes.png",296,27);
		no2 = addButton(pane, "img/exit/no.png", "img/exit/h_no.png",403,27);
		
		yes1.setVisible(str.equals("create"));
		no1.setVisible(yes1.isVisible());

		yes2.setVisible(str.equals("fill"));
		no2.setVisible(yes2.isVisible());

		save.setVisible(!yes1.isVisible() && !yes2.isVisible());
		open.setVisible(!yes1.isVisible() && !yes2.isVisible());
		reset.setVisible(!yes1.isVisible() && !yes2.isVisible());
		
		bg = addLabel(pane, "img/status/"+ (str.equals("fill") ? "create" : str) +".png",0,0);
	}

	protected void setVisible(boolean isVisible){
		pane.setVisible(isVisible);
		pane.repaint();
	}

	protected void decompose(){
		pane.removeAll();
		bg = null;
		yes1 = no1 = yes2 = no2 = save = open = reset = null;
	}
}