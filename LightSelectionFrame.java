import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;

public class LightSelectionFrame extends JFrame{
	MyImageProcessing mip;
	
	public LightSelectionFrame(MyImageProcessing mip) {
		
		JLabel value_label = new JLabel("1");
		value_label.setBounds(233, 40, 61, 16);
		getContentPane().add(value_label);
		
		this.mip=mip;
		getContentPane().setLayout(null);
		setSize(300,130);
		JSlider slider = new JSlider();
		slider.setMinimum(0);
		slider.setMaximum(100);
		slider.setForeground(Color.BLACK);
		slider.setBackground(Color.WHITE);
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				value_label.setText(String.valueOf(slider.getValue()*0.02));
				mip.set_brigthenfactor(slider.getValue()*0.02);
				if(mip.is_gray) {
					mip.image = mip.change_brightness(mip.change_to_gray(mip.previous_image));
				}
				else mip.image = mip.change_brightness(mip.previous_image);
				if(mip.synthesis_image!=null) {
					if(mip.is_gray) {
						mip.synthesis_image = mip.change_brightness(mip.change_to_gray(mip.previous_synthesis));
					}
					else mip.synthesis_image = mip.change_brightness(mip.previous_synthesis);
					mip.image = mip.synthesize(mip.image, mip.synthesis_image);
				}
				ImageIcon icon = new ImageIcon(mip.image);
				
				mip.image_label.setIcon(icon);
				mip.image_label.revalidate();
				mip.image_label.repaint();
			}
		});
		slider.setPaintLabels(true);
		slider.setBounds(0, 0, 300, 50);
		getContentPane().add(slider);
		
		JButton button_confirm = new JButton("Confirm");
		button_confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mip.set_brigthenfactor(slider.getValue()*0.02);
				if(mip.is_gray) {
					mip.image = mip.change_brightness(mip.change_to_gray(mip.previous_image));
				}
				else mip.image = mip.change_brightness(mip.previous_image);
				if(mip.synthesis_image!=null) {
					if(mip.is_gray) {
						mip.synthesis_image = mip.change_brightness(mip.change_to_gray(mip.previous_synthesis));
					}
					else mip.synthesis_image = mip.change_brightness(mip.previous_synthesis);
					mip.image = mip.synthesize(mip.image, mip.synthesis_image);
				}
				ImageIcon icon = new ImageIcon(mip.image);
				mip.image_label.setIcon(icon);
				mip.image_label.revalidate();
				mip.image_label.repaint();
				quit();
			}
		});
		button_confirm.setBounds(177, 62, 117, 29);
		getContentPane().add(button_confirm);
		
		JButton button_cancel = new JButton("Cancel");
		button_cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mip.set_brigthenfactor(1);
				if(mip.is_gray) {
					mip.image = mip.change_brightness(mip.change_to_gray(mip.previous_image));
				}
				else mip.image = mip.change_brightness(mip.previous_image);
				if(mip.synthesis_image!=null) {
					if(mip.is_gray) {
						mip.synthesis_image = mip.change_brightness(mip.change_to_gray(mip.previous_synthesis));
					}
					else mip.synthesis_image = mip.change_brightness(mip.previous_synthesis);
					mip.image = mip.synthesize(mip.image, mip.synthesis_image);
				}
				ImageIcon icon = new ImageIcon(mip.image);
				mip.image_label.setIcon(icon);
				mip.image_label.revalidate();
				mip.image_label.repaint();
				quit();
			}
		});
		button_cancel.setBounds(10, 62, 117, 29);
		getContentPane().add(button_cancel);
		setVisible(true);

	}
	
	public void quit() {
		this.dispose();
	}
}
