/*
 * MenuAppFrame.java
 *
 * Created on 27-Nov-2010, 21:01:08
 */
package com.icode.view.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingConstants;

import com.icode.resources.ResourceUtils;
import com.icode.view.iToolBarMenuControls;
import com.icode.view.border.LineBorder;
import com.icode.view.component.PressButton;
import com.icode.view.container.DialogStrip;
import com.icode.view.container.MenuBar;
import com.icode.view.container.MessageType;
import com.icode.view.container.TitlePanel;
import com.icode.view.container.ToolFooter;

/**
 * 
 * @author Nes
 */
public abstract class MenuAppFrame extends AppJFrame {

	Listener listener;

	/**
	 * @return the toolBar
	 */
	public static TitlePanel getToolBar() {
		return toolBar;
	}

	/**
	 * @param toolBar
	 *            the toolBar to set
	 */
	public static void setToolBar(TitlePanel toolBar) {
		MenuAppFrame.toolBar = toolBar;
	}

	/**
	 * @return the toolFooter
	 */
	public static ToolFooter getToolFooter() {
		return MenuAppFrame.toolFooter;
	}

	/**
	 * @param toolFooter
	 *            the toolFooter to set
	 */
	public static void setToolFooter(ToolFooter toolFooter) {
		MenuAppFrame.toolFooter = toolFooter;
	}

	/** Creates new form MenuAppFrame */
	public MenuAppFrame() {
		super();
		init();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	public void init() {
		listener = new Listener();
		menubarButtonGroup = new javax.swing.ButtonGroup();
		setToolBar(new TitlePanel());
		menuBar = new MenuBar();
		fileMenu = new javax.swing.JPopupMenu();
		connectMenuItem = new javax.swing.JMenuItem();
		disconnectMenuItem = new javax.swing.JMenuItem();
		quitMenuItem = new javax.swing.JMenuItem();
		userMenu = new javax.swing.JPopupMenu();
		preferencesMenuItem = new javax.swing.JMenuItem();
		sendRequestMenuItem = new javax.swing.JMenuItem();
		readRequestsMenuItem = new javax.swing.JMenuItem();
		waitingRequestsMenuItem = new javax.swing.JMenuItem();
		nextTabMenuItem = new javax.swing.JMenuItem();
		goToResourceIDMenuItem = new javax.swing.JMenuItem();
		previewInPDFMenuItem = new javax.swing.JMenuItem();
		OptionsMenu = new javax.swing.JPopupMenu();
		extensioManagerMenuItem = new javax.swing.JMenuItem();
		menubarMenu = new javax.swing.JPopupMenu();
		testAndIconsMenuItem = new javax.swing.JRadioButtonMenuItem();
		iconsOnlyMenuItem = new javax.swing.JRadioButtonMenuItem();
		textOnlyMenuItem = new javax.swing.JRadioButtonMenuItem();
		formsMenu = new javax.swing.JPopupMenu();
		rightToolbarMenuItem = new javax.swing.JCheckBoxMenuItem();
		printMenu = new javax.swing.JMenu();
		previewBeforePrintMenuItem = new javax.swing.JCheckBoxMenuItem();
		saveOptionsMenuItem = new javax.swing.JMenuItem();
		PluginsMenu = new javax.swing.JPopupMenu();
		executePluginMenuItem = new javax.swing.JMenuItem();
		shortcutMenu = new javax.swing.JPopupMenu();
		editShortcutMenuItem = new javax.swing.JMenuItem();
		helpMenu = new javax.swing.JPopupMenu();
		userManualMenuItem = new javax.swing.JMenuItem();
		contextualHelpMenuItem = new javax.swing.JMenuItem();
		tipsMenuItem = new javax.swing.JMenuItem();
		keyboardShortcutsMenuItem = new javax.swing.JMenuItem();
		jSeparator9 = new javax.swing.JPopupMenu.Separator();
		aboutMenuItem = new javax.swing.JMenuItem();

		panel = new JPanel(new BorderLayout());

		titlePanel = new TitlePanel() {

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(300, 40);
			}
		};

		titlePanel.setOpaque(false);
		titlePanel.setVisible(false);
		menuBar.setOpaque(false);

		titlePanel.setBorder(null);
		menuBar.setBorder(null);

		titlePanel.addAppButton("/icons/32/iMRS3.png", null);
		titlePanel.addGlue(java.awt.Toolkit.getDefaultToolkit().getScreenSize().width - 350);

		panel.add(titlePanel, BorderLayout.CENTER);

		menuWrapper = new WrapperPanel(new BorderLayout());

		blueRibbon = new JPanel(new BorderLayout()) {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				int w = getWidth(), h = getHeight();
				g2.setPaint(new GradientPaint(0, 2, new Color(182, 186, 191),
						w * 3, h, new Color(107, 123, 132)));
				g.fillRect(0, 0, w, 1);
				g2.setPaint(new GradientPaint(0, 1, new Color(76, 148, 240), w,
						h, Color.WHITE));
				g.fillRect(0, 1, w, 1);
				g2.setPaint(new GradientPaint(0, 1, new Color(59, 118, 218), w,
						h, Color.WHITE));
				g.fillRect(0, 2, w, 1);
			}

			@Override
			public Dimension getPreferredSize() {
				Dimension d = super.getPreferredSize();
				d.height = Math.max(d.height, 3);
				return d;
			}
		};
		blueRibbon.setPreferredSize(new Dimension(ImageObserver.WIDTH, 2));
		blueRibbon.add(Box.createVerticalStrut(1));

		grayRibbon = new JPanel(new BorderLayout()) {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				int w = getWidth(), h = getHeight();
				g2.setColor(new Color(212, 216, 219));
				g.fillRect(0, 0, w, h);
			}

			@Override
			public Dimension getPreferredSize() {
				Dimension d = super.getPreferredSize();
				d.height = Math.max(d.height, 2);
				return d;
			}
		};

		DialogStrip dialogStrip = new DialogStrip();
		dialogStrip.setTitle("<HTML><B>Security Warning!</B>");
		dialogStrip.setMessageType(MessageType.INFORMATION);
		dialogStrip.setMessage("Some content has been disabled");
		dialogStrip.addStringButton("Enable Content");
		dialogStrip.addGlue(java.awt.Toolkit.getDefaultToolkit()
				.getScreenSize().width - 480);
		dialogStrip.addPlusButton("Close this Message");

		toolFooter.setBorder(new LineBorder(new Color(11184810), 1, 0, 0, 0));
		toolFooter.add(Box.createHorizontalStrut(8));

		toolBar.addTheTitleLabel("MEDICAL");

		toolBar.setBackground(Color.white);
		toolBar.setBorder(new LineBorder(Color.gray, 0, 0, 1, 0));

		toolBar.addGlue(10);
		newButton = toolBar.addButton("/icons/16/add.png", "Create a new entry");
		newButton.addActionListener(listener);
		saveButton = toolBar.addButton("/icons/16/save.png", "Save/Edit this entry");
		saveButton.addActionListener(listener);
		deleteButton = toolBar.addButton("/icons/16/delete.png", "Delete Record");
		deleteButton.addActionListener(listener);
		findButton = toolBar.addButton("/icons/16/search.png", "find record");
		findButton.addActionListener(listener);
		firstButton = toolBar.addButton("/icons/16/first.png", "Go to First");
		firstButton.addActionListener(listener);
		prevButton = toolBar.addButton("/icons/16/prev.png", "Go to Previous");
		prevButton.addActionListener(listener);
		nextButton = toolBar.addButton("/icons/16/next.png", "Go to Next");
		nextButton.addActionListener(listener);
		lastButton = toolBar.addButton("/icons/16/last.png", "Go to Last");
		lastButton.addActionListener(listener);
		reloadButton = MenuAppFrame.toolBar.addButton("/icons/16/refresh.png", "Reload");
		reloadButton.addActionListener(listener);
		connectMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_O,
				java.awt.event.InputEvent.CTRL_MASK));
		connectMenuItem.setText("Connect");
		fileMenu.add(connectMenuItem);

		disconnectMenuItem.setText("Disconnect");
		fileMenu.add(disconnectMenuItem);

		quitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_Q,
				java.awt.event.InputEvent.CTRL_MASK));
		quitMenuItem.setText("Quit");
		fileMenu.add(quitMenuItem);

		menuBar.addMenu("File", fileMenu);

		preferencesMenuItem.setText("Preferences");
		userMenu.add(preferencesMenuItem);

		sendRequestMenuItem.setText("Send a request");
		userMenu.add(sendRequestMenuItem);

		readRequestsMenuItem.setText("Read my requests");
		userMenu.add(readRequestsMenuItem);

		waitingRequestsMenuItem.setText("Waiting Request");
		userMenu.add(waitingRequestsMenuItem);

		menuBar.addMenu("User", userMenu);

		nextTabMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_PAGE_DOWN,
				java.awt.event.InputEvent.CTRL_MASK));
		nextTabMenuItem.setText("Next Tab");

		goToResourceIDMenuItem.setAccelerator(javax.swing.KeyStroke
				.getKeyStroke(java.awt.event.KeyEvent.VK_G,
						java.awt.event.InputEvent.CTRL_MASK));
		goToResourceIDMenuItem.setText("Go to Resource ID");

		previewInPDFMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_P,
				java.awt.event.InputEvent.CTRL_MASK));
		previewInPDFMenuItem.setText("Preview in PDF");

		extensioManagerMenuItem.setText("Extension Manager");
		OptionsMenu.add(extensioManagerMenuItem);

		menubarButtonGroup.add(testAndIconsMenuItem);
		testAndIconsMenuItem.setSelected(true);
		testAndIconsMenuItem.setText("Text and Icons");
		menubarMenu.add(testAndIconsMenuItem);

		menubarButtonGroup.add(iconsOnlyMenuItem);
		iconsOnlyMenuItem.setText("Icons only");
		menubarMenu.add(iconsOnlyMenuItem);

		menubarButtonGroup.add(textOnlyMenuItem);
		textOnlyMenuItem.setText("Text only");
		menubarMenu.add(textOnlyMenuItem);

		OptionsMenu.add(menubarMenu);

		rightToolbarMenuItem.setSelected(true);
		rightToolbarMenuItem.setText("Right Toolbar");
		formsMenu.add(rightToolbarMenuItem);

		OptionsMenu.add(formsMenu);

		printMenu.setText("Print");
		previewBeforePrintMenuItem.setSelected(true);
		previewBeforePrintMenuItem.setText("Preview before print");
		printMenu.add(previewBeforePrintMenuItem);

		OptionsMenu.add(printMenu);

		saveOptionsMenuItem.setText("Save Options");
		OptionsMenu.add(saveOptionsMenuItem);

		menuBar.addMenu("Options", OptionsMenu);

		executePluginMenuItem.setText("Execute a plugin");
		PluginsMenu.add(executePluginMenuItem);

		menuBar.addMenu("Plugins", PluginsMenu);

		editShortcutMenuItem.setText("Edit");
		shortcutMenu.add(editShortcutMenuItem);

		menuBar.addMenu("Shortcut", shortcutMenu);

		userManualMenuItem.setText("User Manual");
		helpMenu.add(userManualMenuItem);

		contextualHelpMenuItem.setAccelerator(javax.swing.KeyStroke
				.getKeyStroke(java.awt.event.KeyEvent.VK_H,
						java.awt.event.InputEvent.CTRL_MASK));
		contextualHelpMenuItem.setText("Contextual Help");
		helpMenu.add(contextualHelpMenuItem);

		tipsMenuItem.setText("Tips");
		helpMenu.add(tipsMenuItem);

		keyboardShortcutsMenuItem.setText("Keyboard Shortcuts");
		helpMenu.add(keyboardShortcutsMenuItem);
		helpMenu.add(jSeparator9);

		aboutMenuItem.setText("About");
		helpMenu.add(aboutMenuItem);

		menuBar.addMenu("Help", helpMenu);
		menuBar.addGlue(8);
		final MenuBar.ToolButton p = (MenuBar.ToolButton) menuBar.addButton(
				"/icons/16/arrow_down_alt1_16x16.png", "Minimize");
		p.setIcon(new Icon() {
			public void paintIcon(Component c, Graphics g, int x, int y) {
				Graphics2D g2 = (Graphics2D) g;
				g2.rotate(Math.toRadians(-45), x, y);
				g.fillRect((x - 2), (y + 3), 2, 6);
				g.fillRect((x - 2), (y + 7), 6, 2);
			}

			public int getIconWidth() {
				return 8;
			}

			public int getIconHeight() {
				return 8;
			}
		});

		p.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (MenuAppFrame.this.titlePanel.isVisible()) {
					p.setIcon(new Icon() {
						public void paintIcon(Component c, Graphics g, int x,
								int y) {
							Graphics2D g2 = (Graphics2D) g;
							g2.rotate(Math.toRadians(-45), x, y);
							g.fillRect((x - 2), (y + 3), 2, 6);
							g.fillRect((x - 2), (y + 7), 6, 2);
						}

						public int getIconWidth() {
							return 8;
						}

						public int getIconHeight() {
							return 8;
						}
					});
					MenuAppFrame.this.titlePanel.setVisible(false);
					p.setToolTipText("Expand");
				} else {
					p.setIcon(new Icon() {
						public void paintIcon(Component c, Graphics g, int x,
								int y) {

							Graphics2D g2 = (Graphics2D) g;
							g2.rotate(Math.toRadians(135), x, y);
							g.fillRect(x - 2, y - 10, 2, 6);
							g.fillRect(x - 2, y - 6, 6, 2);
						}

						public int getIconWidth() {
							return 8;
						}

						public int getIconHeight() {
							return 8;
						}
					});
					MenuAppFrame.this.titlePanel.setVisible(true);
					p.setToolTipText("Minimize");
				}
			}
		});
		menuBar.addButton("/icons/16/cog_16x16.png", "Settings");
		menuBar.addButton("/icons/16/Help.png", "Help");

		setupHeaderLayouts();
	}

	protected void setupHeaderLayouts() {
		menuWrapper.add(menuBar, BorderLayout.NORTH);
		menuWrapper.add(titlePanel, BorderLayout.CENTER);
		menuWrapper.add(blueRibbon, BorderLayout.SOUTH);
		
		JPanel nPanel = new JPanel(new BorderLayout());
		nPanel.add(menuWrapper, BorderLayout.NORTH);
		this.getContentPane().add(nPanel, BorderLayout.NORTH);
	}

	private TitlePanel titlePanel;
	private static TitlePanel toolBar;
	private static MenuBar menuBar;
	private static ToolFooter toolFooter = new ToolFooter();
	private ButtonGroup menubarButtonGroup;
	private JPanel panel;
	private JPanel menuWrapper;
	private JPanel blueRibbon;
	private JPanel grayRibbon;
	private JMenu printMenu;
	private PressButton newButton;
	private PressButton saveButton;
	private PressButton deleteButton;
	private PressButton findButton;
	private PressButton nextButton;
	private PressButton prevButton;
	private PressButton firstButton;
	private PressButton lastButton;
	private PressButton reloadButton;
	private JPopupMenu OptionsMenu;
	private JMenuItem aboutMenuItem;
	private JMenuItem connectMenuItem;
	private JMenuItem contextualHelpMenuItem;
	private JMenuItem disconnectMenuItem;
	private JMenuItem editShortcutMenuItem;
	private JMenuItem executePluginMenuItem;
	private JMenuItem extensioManagerMenuItem;
	private JMenuItem keyboardShortcutsMenuItem;
	private JMenuItem quitMenuItem;
	private JMenuItem readRequestsMenuItem;
	private JMenuItem saveOptionsMenuItem;
	private JMenuItem sendRequestMenuItem;
	private JMenuItem nextTabMenuItem;
	private JMenuItem preferencesMenuItem;
	private JMenuItem goToResourceIDMenuItem;
	private JMenuItem tipsMenuItem;
	private JMenuItem userManualMenuItem;
	private JMenuItem previewInPDFMenuItem;
	private JMenuItem waitingRequestsMenuItem;
	private JCheckBoxMenuItem previewBeforePrintMenuItem;
	private JCheckBoxMenuItem rightToolbarMenuItem;
	private JPopupMenu PluginsMenu;
	private JPopupMenu fileMenu;
	private JPopupMenu formsMenu;
	private JPopupMenu shortcutMenu;
	private JPopupMenu helpMenu;
	private JPopupMenu userMenu;
	private JPopupMenu menubarMenu;
	private JPopupMenu.Separator jSeparator9;
	private JRadioButtonMenuItem iconsOnlyMenuItem;
	private JRadioButtonMenuItem testAndIconsMenuItem;
	private JRadioButtonMenuItem textOnlyMenuItem;

	// Hajamarimasu yo
	public void showPreferences() {
		JComponent comp = getRootPane();
		int w = comp.getWidth(), h = comp.getHeight();
		BufferedImage image = new BufferedImage(w, h,
				BufferedImage.TYPE_INT_ARGB);
		comp.paint(image.getGraphics());
		BufferedImage small = new BufferedImage(w * 3 / 4, h * 3 / 4,
				BufferedImage.TYPE_INT_ARGB);
		small.getGraphics().drawImage(
				image.getScaledInstance(w * 3 / 4, h * 3 / 4,
						Image.SCALE_SMOOTH), 0, 0, null);
		try {
			String home = System.getProperty("user.home");
			ImageIO.write(small, "png", new File(home, "screenshot.png"));
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	private static final Logger LOG = Logger.getLogger(MenuAppFrame.class
			.getName());

	public void showAbout() {
		JPanel panel = new JPanel(new BorderLayout(0, 8));
		panel.add(new JLabel(ResourceUtils.getIcon(getClass(), "icon/icon.png")),
				BorderLayout.CENTER);
		panel.add(new JLabel("title", SwingConstants.CENTER),
				BorderLayout.SOUTH);
		setContentPane(panel);
	}

	public boolean close() {
		return true;
	}

	public void menu() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void open() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void newHomeScreen() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void exportData() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void importData() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void lockScreen() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void closeApplication() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void newHomeWindow() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void closeWindow() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	private class WrapperPanel extends JPanel {

		WrapperPanel(LayoutManager layout) {
			super(layout);
		}

		@Override
		protected void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			int w = getWidth(), h = getHeight();
			// GradientPaint paint = new GradientPaint(0.0f, 0.0f, new
			// Color(230, 230, 230), 0.0f, getHeight(), new Color(157, 157,
			// 157));
			// g2.setPaint(paint);
			// g2.setPaint(new GradientPaint(0, 0, new Color(0xa5a5a5), 0, h -
			// 1, new Color(0x989898)));
			// g.fillRect(0, 0, w, h);
			g2.setPaint(new GradientPaint(0, 1, Color.white, 0, h - 3,
					new Color(204, 207, 211)));
			// g.fillRect(1, 1, w - 2, h - 2);

			g.fillRect(0, 0, w, h);
		}
	}

	// Owari

	public void addToolBarListener(iToolBarMenuControls itbm) {
		listener.controlListener = itbm;
	}

	private class Listener implements ActionListener {
		private iToolBarMenuControls controlListener;

		private Listener() {
		}

		public void actionPerformed(ActionEvent e) {

			if (this.controlListener != null) {
				if (e.getSource().equals(MenuAppFrame.this.nextButton)) {
					controlListener.nextEntry(e);
				} else if (e.getSource().equals(MenuAppFrame.this.firstButton)) {
					controlListener.firstEntry(e);
				} else if (e.getSource().equals(MenuAppFrame.this.prevButton)) {
					controlListener.previousEntry(e);
				} else if (e.getSource().equals(MenuAppFrame.this.lastButton)) {
					controlListener.lastEntry(e);
				} else if (e.getSource().equals(MenuAppFrame.this.saveButton)) {
					controlListener.saveEntry(e);
				} else if (e.getSource().equals(MenuAppFrame.this.deleteButton)) {
					controlListener.deleteEntry(e);
				} else if (e.getSource().equals(MenuAppFrame.this.newButton)) {
					controlListener.newEntry(e);
				} else if (e.getSource().equals(MenuAppFrame.this.reloadButton)) {
					controlListener.reloadEntry(e);
				} else if (e.getSource().equals(MenuAppFrame.this.findButton)) {
					controlListener.findEntry(e);
				}
			}
		}
	}
}
