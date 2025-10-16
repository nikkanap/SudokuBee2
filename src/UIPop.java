import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.Cursor;

// UI for the number selection popup in the sudoku game
public class UIPop extends generalPanel{
	private JPanel pane;
	private JPanel panel;
	private JLabel bg;
	protected int size, btnX, btnY;
	protected JButton cancel, erase;
	protected JButton btn[];
	protected JTextField field;
	
	UIPop(int size, JPanel pane){
		this.pane = pane;
		this.size = size;
		panel = addPanel(pane, 5, 84, 500,500);
		panel.setOpaque(false);
		
		if(size == 9)
			nineButtons();
		else if(size == 16) 
			sixteenButtons();
		else
			twentyFiveButtons();
		

		bg = addLabel(panel, "img/game control/"+size+"x"+size+".png",0,0);
		field.grabFocus();
	}

	private void nineButtons(){
		btn = new JButton[9];
		erase = addButton(panel, "img/box/misc/clear.png", 146,126);
		cancel = addButton(panel, "img/box/misc/cancel.png", 293,126);
		btn[0] = addButton(panel, "img/box/25x25/normal/1.png", 111,192);
		btn[1] = addButton(panel, "img/box/25x25/normal/2.png", 184,192);
		btn[2] = addButton(panel, "img/box/25x25/normal/3.png", 257,192);
		btn[3] = addButton(panel, "img/box/25x25/normal/4.png", 330,192);
		btn[4] = addButton(panel, "img/box/25x25/normal/5.png", 147,257);
		btn[5] = addButton(panel, "img/box/25x25/normal/6.png", 220,257);
		btn[6] = addButton(panel, "img/box/25x25/normal/7.png", 293,257);
		btn[7] = addButton(panel, "img/box/25x25/normal/8.png", 184,322);
		btn[8] = addButton(panel, "img/box/25x25/normal/9.png", 257,322);
		field = addTextField(panel, "", 220, 128,40,38);
	}

	private void sixteenButtons(){
		btn = new JButton[16];
		erase = addButton(panel, "img/box/misc/clear.png", 184,126);
		cancel = addButton(panel, "img/box/misc/cancel.png", 256,126);
		btn[0] = addButton(panel, "img/box/25x25/normal/1.png", 147,192);
		btn[1] = addButton(panel, "img/box/25x25/normal/2.png", 221,192);
		btn[2] = addButton(panel, "img/box/25x25/normal/3.png", 295,192);
		btn[3] = addButton(panel, "img/box/25x25/normal/4.png", 183,257);
		btn[4] = addButton(panel, "img/box/25x25/normal/5.png", 257,257);
		btn[5] = addButton(panel, "img/box/25x25/normal/6.png", 221,322);
		btn[6] = addButton(panel, "img/box/25x25/normal/7.png", 221,322);
		btn[7] = addButton(panel, "img/box/25x25/normal/8.png", 147,192);
		btn[8] = addButton(panel, "img/box/25x25/normal/9.png", 221,192);
		btn[9] = addButton(panel, "img/box/25x25/normal/10.png", 295,192);
		btn[10] = addButton(panel, "img/box/25x25/normal/11.png", 183,257);
		btn[11] = addButton(panel, "img/box/25x25/normal/12.png", 257,257);
		btn[12] = addButton(panel, "img/box/25x25/normal/13.png", 221,322);
		btn[13] = addButton(panel, "img/box/25x25/normal/14.png", 147,192);
		btn[14] = addButton(panel, "img/box/25x25/normal/15.png", 221,192);
		btn[15] = addButton(panel, "img/box/25x25/normal/16.png", 295,192);
		field = addTextField(panel, "", 221, 62,40,38);
	}

	private void twentyFiveButtons(){
		btn = new JButton[12];
		erase = addButton(panel, "img/box/misc/clear.png", 111,126);
		cancel = addButton(panel, "img/box/misc/cancel.png", 329,126);
		btn[0] = addButton(panel, "img/box/25x25/normal/1.png", 74,192);
		btn[1] = addButton(panel, "img/box/25x25/normal/2.png", 147,192);
		btn[2] = addButton(panel, "img/box/25x25/normal/3.png", 220,192);
		btn[3] = addButton(panel, "img/box/25x25/normal/4.png", 293,192);
		btn[4] = addButton(panel, "img/box/25x25/normal/5.png", 366,192);
		btn[5] = addButton(panel, "img/box/25x25/normal/6.png", 111,257);
		btn[6] = addButton(panel, "img/box/25x25/normal/7.png", 184,257);
		btn[7] = addButton(panel, "img/box/25x25/normal/8.png", 257,257);
		btn[8] = addButton(panel, "img/box/25x25/normal/9.png", 330,257);
		btn[9] = addButton(panel, "img/box/25x25/normal/10.png", 147,322);
		btn[10] = addButton(panel, "img/box/25x25/normal/11.png", 220,322);
		btn[11] = addButton(panel, "img/box/25x25/normal/12.png", 293,322);
		btn[12] = addButton(panel, "img/box/25x25/normal/13.png", 220,192);
		btn[13] = addButton(panel, "img/box/25x25/normal/14.png", 293,192);
		btn[14] = addButton(panel, "img/box/25x25/normal/15.png", 366,192);
		btn[15] = addButton(panel, "img/box/25x25/normal/16.png", 111,257);
		btn[16] = addButton(panel, "img/box/25x25/normal/17.png", 184,257);
		btn[17] = addButton(panel, "img/box/25x25/normal/18.png", 257,257);
		btn[18] = addButton(panel, "img/box/25x25/normal/19.png", 330,257);
		btn[19] = addButton(panel, "img/box/25x25/normal/20.png", 147,322);
		btn[20] = addButton(panel, "img/box/25x25/normal/21.png", 74,192);
		btn[21] = addButton(panel, "img/box/25x25/normal/22.png", 147,192);
		btn[22] = addButton(panel, "img/box/25x25/normal/23.png", 220,192);
		btn[23] = addButton(panel, "img/box/25x25/normal/24.png", 293,192);
		btn[24] = addButton(panel, "img/box/25x25/normal/25.png", 366,192);
		field = addTextField(panel, "", 200, 128,80,38);
	}

	protected void setVisible(boolean isVisible, int btnX, int btnY, int num){
		pane.setVisible(isVisible);
		if(num == 0)
			field.setText("");
		else
			field.setText(num+"");
		field.grabFocus();
		this.btnX = btnX;
		this.btnY = btnY;
	}

	protected void decompose(){
		pane.removeAll();
		panel = null;
		bg = null;
		cancel = erase = null;

		for(int ctr = 0; ctr<size; ctr++)
			btn[ctr] = null;
	}
}