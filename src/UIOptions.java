import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;

// the UI for the options (settings)
public class UIOptions extends generalPanel{
	private JPanel panel[];
	private JLabel bg;

	private String size[]={"img\\exit\\size\\9x9.png","img\\exit\\size\\16x16.png","img\\exit\\size\\25x25.png"};
	private String sound[]={"img\\exit\\sound\\on.png","img\\exit\\sound\\off.png"};
	private String functionPenalty[] = {"img\\exit\\function penalty\\mode_0.png", "img\\exit\\function penalty\\mode_1.png", "img\\exit\\function penalty\\mode_2.png"};

	protected JLabel upperOption, levelLabel, lowerOption;
	protected JButton exit, no, rightNav, leftNav;
	protected JButton left[]=new JButton[2];
	protected JButton right[]=new JButton[2];

	protected int sz, lvl, snd, num, page, fp;

	UIOptions(JPanel panel[]){
		this.panel = panel;
		panel[1].setOpaque(true);

		exit = addButton(panel[1], "img/exit/okay.png", "img/exit/h_okay.png",385,401);
		rightNav = addButton(panel[1], "img/exit/nav_right.png", "img/exit/h_nav_right.png", 595, 398);
		leftNav = addButton(panel[1], "img/exit/nav_left.png", "img/exit/h_nav_left.png", 160, 398);

		for(int ctr = 0; ctr<2 ; ctr++){
			left[ctr] = addButton(panel[1], "img/exit/left.png", "img/exit/h_left.png",356,235+70*ctr);
			right[ctr] = addButton(panel[1], "img/exit/right.png", "img/exit/h_right.png",568,235+70*ctr);
		}

		// set to size and sound at page 1
		upperOption = addLabel(panel[1], size[0], 389,237);
		lowerOption = addLabel(panel[1], sound[0], 389,308);
		bg = addLabel(panel[1],"img/bg/options_1.png",100,99);

		sz = 0;
		num = lvl = snd = fp = 0;
		page = 1;
		//panel[1].setVisible(true);
	}

	protected void increase(){
		page++;
		page = (page == 3) ? 1 : page;
		refreshPage(0);
	}

	protected void decrease(){
		page--;
		page = (page == 0) ? 2 : page;
		refreshPage(0);
	}

	protected void refreshPage(int specificPage) {
		// if no page is given, then we maintain the current page.
		// otherwise, we refresh to a specific page
		page = (specificPage == 0) ? page : specificPage;
		sop("page " + page);
		changePicture(upperOption, (page == 1) ? size[sz] : functionPenalty[fp]);
		changePicture(lowerOption, (page == 1) ? sound[0] : "");
		changePicture(bg, "img/bg/options_" + page + ".png");
	}

	protected void setUpperOption(boolean isRight){
		if (page == 1) {
			sz += (isRight) ? 1 : -1;
			sz = (sz == 3) ? 0 : (sz == -1) ? 2 : sz;
			changePicture(upperOption, size[sz]);
		} else {
			fp += (isRight) ? 1 : -1;
			fp = (fp == 3) ? 0 : (fp == -1) ? 2 : fp;
			changePicture(upperOption, functionPenalty[fp]);
		}
	}

	protected void setLowerOption(boolean isRight){
		if (page == 1) {
			snd += (isRight) ? 1 : -1;
			snd = (snd == 2) ? 0 : (snd == -1) ? 1 : snd;
			changePicture(lowerOption, sound[snd]);
		}
	}

	protected void setVisible(boolean isVisible, int num){
		this.num = num;
		panel[1].setVisible(isVisible);
	}

	protected void decompose(){
		panel[1].removeAll();
		bg = upperOption = levelLabel = lowerOption = null;
		exit = no = null;
		left[0] = right[0] = left[1] = right[1] = null;
	}

}