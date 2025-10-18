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
			//nineButtons();
			twentyFiveButtons();
		else if(size == 16) 
			sixteenButtons();
		else
			twentyFiveButtons();
		

		//bg = addLabel(panel, "img/game control/"+size+"x"+size+".png",0,0);
		bg = addLabel(panel, "img/game control/25x25.png",0,0);
		field.grabFocus();
	}

	private void nineButtons(){
		btn = new JButton[9];
		erase = addButton(panel, "img/box/misc/clear.png", 146,126);
		cancel = addButton(panel, "img/box/misc/cancel.png", 293,126);
		btn[0] = addButton(panel, "img/box/12x12/normal/1.png", 111,192);
		btn[1] = addButton(panel, "img/box/12x12/normal/2.png", 184,192);
		btn[2] = addButton(panel, "img/box/12x12/normal/3.png", 257,192);
		btn[3] = addButton(panel, "img/box/12x12/normal/4.png", 330,192);
		btn[4] = addButton(panel, "img/box/12x12/normal/5.png", 147,257);
		btn[5] = addButton(panel, "img/box/12x12/normal/6.png", 220,257);
		btn[6] = addButton(panel, "img/box/12x12/normal/7.png", 293,257);
		btn[7] = addButton(panel, "img/box/12x12/normal/8.png", 184,322);
		btn[8] = addButton(panel, "img/box/12x12/normal/9.png", 257,322);
		field = addTextField(panel, "", 220, 128,40,38);
	}

	private void sixteenButtons(){
		btn = new JButton[16];
		int incrX = 58, incrY = 53;
		int x1 = 116, x2 = 87;
		int y = 198;
		erase = addButton(panel, "img/box/misc/clear_16x16.png", 144,147);
		cancel = addButton(panel, "img/box/misc/cancel_16x16.png", 319,147);
		btn[0] = addButton(panel, "img/box/16x16/normal/1.png", x1, y);
		btn[1] = addButton(panel, "img/box/16x16/normal/2.png", x1 + incrX, y);
		btn[2] = addButton(panel, "img/box/16x16/normal/3.png", x1 + incrX * 2, y);
		btn[3] = addButton(panel, "img/box/16x16/normal/4.png", x1 + incrX * 3, y);
		btn[4] = addButton(panel, "img/box/16x16/normal/5.png", x1 + incrX * 4, y);
		btn[5] = addButton(panel, "img/box/16x16/normal/6.png", x2,251);
		btn[6] = addButton(panel, "img/box/16x16/normal/7.png", x2 + incrX, y + incrY);
		btn[7] = addButton(panel, "img/box/16x16/normal/8.png", x2 + incrX * 2, y + incrY);
		btn[8] = addButton(panel, "img/box/16x16/normal/9.png", x2 + incrX * 3, y + incrY);
		btn[9] = addButton(panel, "img/box/16x16/normal/10.png", x2 + incrX * 4, y + incrY);
		btn[10] = addButton(panel, "img/box/16x16/normal/11.png", x2 + incrX * 5, y + incrY);
		btn[11] = addButton(panel, "img/box/16x16/normal/12.png", x1, y + incrY * 2);
		btn[12] = addButton(panel, "img/box/16x16/normal/13.png", x1 + incrX, y + incrY * 2);
		btn[13] = addButton(panel, "img/box/16x16/normal/14.png", x1 + incrX * 2, y + incrY * 2);
		btn[14] = addButton(panel, "img/box/16x16/normal/15.png", x1 + incrX * 3, y + incrY * 2);
		btn[15] = addButton(panel, "img/box/16x16/normal/16.png", x1 + incrX * 4, y + incrY * 2);
		field = addTextField(panel, "", 230, 140,40,38);
	}

	// WORK IN PROGRESS
	private void twentyFiveButtons(){
		btn = new JButton[25];
		erase = addButton(panel, "img/box/misc/clear_25x25.png", 173,130);
		cancel = addButton(panel, "img/box/misc/cancel_25x25.png", 310,130);
		btn[0] = addButton(panel, "img/box/25x25/normal/1.png", 154,170);
		btn[1] = addButton(panel, "img/box/25x25/normal/2.png", 199,170);
		btn[2] = addButton(panel, "img/box/25x25/normal/3.png", 244,170);
		btn[3] = addButton(panel, "img/box/25x25/normal/4.png", 289,170);
		btn[4] = addButton(panel, "img/box/25x25/normal/5.png", 366,170);
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