import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.JButton;

public class AutoExitFrame extends JFrame{
	int countdown=10;
	JLabel exist_message;
	public static final int SELECTION = 1;
	public static final int CANCEL = 0;
	public static final int SUCCESS = 2;
	
	public AutoExitFrame(int type){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("System Message");
		setAlwaysOnTop(true);
		this.setSize(350,150);
		setLocation(630,500);
		getContentPane().setLayout(null);
		exist_message = new JLabel("");
		exist_message.setBounds(0, 0, 350, 78);
		exist_message.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(exist_message);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(28, 90, 117, 29);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		getContentPane().add(btnCancel);
		
		JButton btnConfirm = new JButton("Confirm");
		btnConfirm.setBounds(206, 90, 117, 29);
		btnConfirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		getContentPane().add(btnConfirm);
		this.setVisible(true);
		if(type==SELECTION) {
			auto_selection_exit();
		}
		else {
			setSize(350,100);
			btnConfirm.setVisible(false);
			btnCancel.setVisible(false);
			if(type==CANCEL) exist_message.setText("Canceled...");
			else if(type==SUCCESS) exist_message.setText("Saved SUCCESS!");
			auto_exit();
		}
		setVisible(true);
		
	}
	public void auto_selection_exit() {
		Timer timer = new Timer(1000,new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				countdown--;
				exist_message.setText("Do you want to save Changes??  <<"+String.valueOf(countdown)+">>");
				if(countdown<0)
					quit();
			}
		});
		timer.start();
	}
	
	public void auto_exit() {
		Timer timer = new Timer(1000,new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				quit();
			}
		});
		timer.start();
	}
	
	public void quit() {
		this.dispose();
	}

}
