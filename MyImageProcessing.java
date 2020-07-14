import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.RescaleOp;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JLabel;

public class MyImageProcessing extends JFrame implements MouseListener, MouseMotionListener{
	
	MyImageProcessing imgprocess_frame = this;
	BufferedImage image;
	BufferedImage previous_image;
	BufferedImage previous_synthesis;
	BufferedImage synthesis_image;
	double brightenfactor = 1;
	JLabel image_label;
	JLabel synthesis_label;
	JPanel panel;
	JPanel synthesis_panel;
	boolean is_gray = false;
	EdgeDetection edgedetection = new EdgeDetection();
	boolean is_crop = false;
	HashMap<MyPoint, Color> croped_colorset = new HashMap<MyPoint, Color>();
	Point2D mouse_point;
	int x, y=0;

	public MyImageProcessing() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		setSize(640,480);
		this.setTitle("Image Processing");
		synthesis_panel = new JPanel();
		panel = new JPanel();
		getContentPane().add(synthesis_panel);
		getContentPane().add(panel);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu menu_file = new JMenu("FILE");
		menuBar.add(menu_file);  
		
		JMenuItem item_open = new JMenuItem("Open Image");
		menu_file.add(item_open);		
		
		image_label = new JLabel("");
		synthesis_label = new JLabel("");
		panel.add(image_label);
		synthesis_panel.add(synthesis_label);

		
		JMenuItem item_save = new JMenuItem("Save as Image");
		item_save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser();
				File selected_file = null;
				jfc.setCurrentDirectory(new File(System.getProperty("user.home")));
				int result = jfc.showSaveDialog(null);
				String path = "";
				if(result == JFileChooser.APPROVE_OPTION) {
					selected_file = jfc.getSelectedFile();
					path = selected_file.getAbsolutePath();
			        try {
						ImageIO.write(image, "jpg", selected_file);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				    	new AutoExitFrame(AutoExitFrame.SUCCESS);
			    } else {
			    	new AutoExitFrame(AutoExitFrame.CANCEL);
			    }
			}
		});
		menu_file.add(item_save);
		
		JMenuItem item_newProject = new JMenuItem("New Project");
		item_newProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imgprocess_frame.dispose();
				new MyImageProcessing();
			}
		});
		menu_file.add(item_newProject);
		
		JMenu mnEdit = new JMenu("EDIT");
		menuBar.add(mnEdit);
		
		JMenuItem item_gray = new JMenuItem("GrayScale");
		item_gray.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				is_gray=true;
				image = change_brightness(previous_image);
				image = change_to_gray(image);
				if(synthesis_image != null) {
					synthesis_image = change_to_gray(change_brightness(previous_synthesis));
					image = synthesize(image, synthesis_image);
				}
				ImageIcon icon = new ImageIcon(image);
				image_label.setIcon(icon);
				image_label.revalidate(); // ADDED
                image_label.repaint();     
			}	
		});
		mnEdit.add(item_gray);
		
		JMenuItem menuItem = new JMenuItem("Edge Detection");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					image = edgedetection.detectEdges(image);
					ImageIcon icon = new ImageIcon(image);
					image_label.setIcon(icon);
					image_label.revalidate(); // ADDED
	                image_label.repaint();					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		mnEdit.add(menuItem);
		
		JMenuItem item_lightness = new JMenuItem("Lightness");
		item_lightness.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				new LightSelectionFrame(imgprocess_frame);
			}
		});
		mnEdit.add(item_lightness);
		
		JMenuItem item_contrast = new JMenuItem("Synthesis");
		item_contrast.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				is_crop = true;
				JFileChooser jfc = new JFileChooser();
				jfc.setAcceptAllFileFilterUsed(false);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("ONLYE IMAGES","jpeg","png","jpg","gif");
				jfc.setFileFilter(filter);
				int return_value = jfc.showOpenDialog(null);
				if(return_value ==0) {
					File file = jfc.getSelectedFile();
					try {
						synthesis_image = ImageIO.read(file);
						previous_synthesis = ImageIO.read(file);
						synthesis_image = set_image_size(synthesis_image);
						previous_synthesis = set_image_size(previous_synthesis);
						ImageIcon icon = new ImageIcon(synthesis_image);
						Dimension imageSize = new Dimension(icon.getIconWidth(),icon.getIconHeight()); 
						//System.out.println(imageSize.toString());
						synthesis_panel.setBounds((int)previous_image.getWidth()+20, 20, synthesis_image.getWidth(), synthesis_image.getHeight());
						//panel.setBounds(20, 20, previous_image.getWidth(), previous_image.getHeight());
						imgprocess_frame.setSize((int)imageSize.getWidth()+previous_image.getWidth()+60, Math.max((int)previous_image.getHeight(),(int)imageSize.getHeight())+80);
		                synthesis_label.setPreferredSize(imageSize); 
						synthesis_label.setIcon(icon);
						synthesis_panel.add(synthesis_label);
						synthesis_panel.repaint();
//		                image_label.revalidate();
//		                image_label.repaint();
					}catch(Exception exp) {
						exp.printStackTrace();
					}
				}
				else {}
			}
		});
		mnEdit.add(item_contrast);
		
		item_open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				item_save.setEnabled(true);
				item_lightness.setEnabled(true);
				item_contrast.setEnabled(true);
				item_gray.setEnabled(true);
				menuItem.setEnabled(true);
				get_image();
			}
		});
		
		item_save.setEnabled(false);
		item_lightness.setEnabled(false);
		item_contrast.setEnabled(false);
		item_gray.setEnabled(false);
		menuItem.setEnabled(false);
		panel.addMouseListener(this);
		panel.addMouseMotionListener(this);
		this.setVisible(true);
	}
	
	public static BufferedImage toBufferedImage(Image img)
	{
	    if (img instanceof BufferedImage)
	    {
	        return (BufferedImage) img;
	    }
	    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
	    Graphics2D bGr = bimage.createGraphics();
	    bGr.drawImage(img, 0, 0, null);
	    bGr.dispose();
	    return bimage;
	}
	
	public void get_image() {
		JFileChooser jfc = new JFileChooser();
		jfc.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("ONLYE IMAGES","jpeg","png","jpg","gif");
		jfc.setFileFilter(filter);
		int return_value = jfc.showOpenDialog(null);
		if(return_value ==0) {
			File file = jfc.getSelectedFile();
			try {
				image = ImageIO.read(file);
				previous_image = ImageIO.read(file);
				image = set_image_size(image);
				previous_image = set_image_size(previous_image);
				ImageIcon icon = new ImageIcon(image);
				Dimension imageSize = new Dimension(icon.getIconWidth(),icon.getIconHeight()); 
				//System.out.println(imageSize.toString());
				//synthesis_panel.setBounds(0, 0, (int)imageSize.getWidth(), (int)imageSize.getHeight());
				panel.setBounds(20, 20, (int)imageSize.getWidth(), (int)imageSize.getHeight());
				imgprocess_frame.setSize((int)imageSize.getWidth()+40, (int)imageSize.getHeight()+80);
                image_label.setPreferredSize(imageSize); 
				image_label.setIcon(icon);
                image_label.revalidate();
                image_label.repaint();
			}catch(Exception exp) {
				exp.printStackTrace();
			}
		}
		else {}
	}
	
	public BufferedImage set_image_size(BufferedImage image) {
		if(image.getWidth()>750 && image.getHeight()>750) {
			if(image.getWidth()>image.getHeight()) image = toBufferedImage(image.getScaledInstance(750, image.getHeight()*750/image.getWidth(), Image.SCALE_SMOOTH));
			else image = toBufferedImage(image.getScaledInstance(image.getWidth()*750/image.getHeight(), 750, Image.SCALE_SMOOTH));
		}
		else if(image.getHeight()>750) image = toBufferedImage(image.getScaledInstance(image.getWidth()*750/image.getHeight(), 750, Image.SCALE_SMOOTH));
		else if (image.getWidth()>750) image = toBufferedImage(image.getScaledInstance(750, image.getHeight()*750/image.getWidth(), Image.SCALE_SMOOTH));
		return image;
	}
	
	public BufferedImage change_to_gray(BufferedImage image) {
		BufferedImage temp = deepCopy(image);
		for(int y = 0; y < temp.getHeight(); y++) {
			for(int x = 0; x < temp.getWidth(); x++) {
				Color color = new Color(temp.getRGB(x, y));
			    //int Y = (int) (0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue());
			    int Y = (int) (0.2126 * color.getRed() + 0.7152 * color.getGreen() + 0.0722 * color.getBlue());
			    temp.setRGB(x, y, new Color(Y, Y, Y).getRGB());
			}
		}
		return temp;
	}
	public BufferedImage change_brightness(BufferedImage image) {
		BufferedImage temp = deepCopy(image);
		RescaleOp rescale_operator = new RescaleOp((float)brightenfactor, 0, null);
		temp = rescale_operator.filter(temp, temp);
		return temp;
	}
	public void set_brigthenfactor(double x) {brightenfactor = x;}
	public double get_brightenfactor() {return brightenfactor;}
	
	public static BufferedImage deepCopy(BufferedImage bi) {
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		if(is_crop && e.getPoint().getX()<image.getWidth()-synthesis_image.getWidth() && e.getPoint().getY()<image.getHeight()-synthesis_image.getHeight()) {
			x = e.getPoint().x;
			y = e.getPoint().y;
			//System.out.println("mousePressed");
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		//crop_object();
		if(is_crop) image = synthesize(image, synthesis_image);
		panel.revalidate();
		panel.repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
	}
	
	public HashMap<MyPoint, Color> crop_object(BufferedImage object){
		//System.out.println("Cropping");
		HashMap<MyPoint, Color> temp_hash = new HashMap<MyPoint, Color>();
		for(int i=0; i<object.getWidth(); i++) {
			for(int j=0; j<object.getHeight(); j++) {
				if(object.getRGB(i, j)==object.getRGB(0, 0)){
					continue;
				}
				else {
					temp_hash.put(new MyPoint(i,j), new Color(object.getRGB(i,j)));
				}
			}
		}
		return temp_hash;
	}
	
	public BufferedImage synthesize(BufferedImage background, BufferedImage object) {
		HashMap<MyPoint, Color> temp_hash = new HashMap<MyPoint, Color>();
		temp_hash = crop_object(object);
		if(is_gray) object = change_to_gray(object);
		for(int i=0; i<background.getWidth(); i++) {
			for(int j=0; j<background.getHeight(); j++) {
				//System.out.println(croped_colorset.containsKey(new MyPoint(i-x,j-y)));
				if(temp_hash.containsKey(new MyPoint(i-x,j-y))) {
					background.setRGB(i, j, temp_hash.get(new MyPoint(i-x, j-y)).getRGB());
					//System.out.println("i,j contains");
				}
				else {
					background.setRGB(i, j, background.getRGB(i, j));
				}
			}
		}
		return background;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
}
