package com.icode.view.container;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;

import com.icode.data.domain.BaseModel;
import com.icode.view.component.fields.StringField;
import com.icode.view.table.Table;
import com.icode.view.table.Table.Listener;

@SuppressWarnings("serial")
public abstract class SearchFilter<E extends BaseModel> extends PopupDialog
		implements Listener<E> {
	protected Table<E> modelList;
	private List<E> models;
	private JScrollPane modelListScrollPane;

	public SearchFilter(List<E> models) {
		this.models = models;
		this.setTranslucent(false);
		StringField searchField = new StringField();
		searchField.setWatermark("Search Item");
		searchField.setFont(UIManager.getFont("Label.font").deriveFont(15f));
		add(searchField, BorderLayout.NORTH);

		modelList = new Table<E>() {
			@Override
			protected void paintComponent(Graphics g) {
				if (((DefaultListModel) getModel()).getSize() == 0) {
					int x = (getWidth() / 2) - 60;
					int y = (getHeight() / 2);
					String message = "Nothing Found.";

					((Graphics2D) g).setRenderingHint(
							RenderingHints.KEY_TEXT_ANTIALIASING,
							RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

					g.setColor(Color.WHITE);
					g.setFont((UIManager.getFont("Label.font").deriveFont(19f)));
					g.drawString(message, x, y);
				} else {
					super.paintComponent(g);
				}
			}
		};

		prepareCells();

		modelList.addItems(getModels());
		modelList.addListListener(this);

		JPanel panel = new JPanel(new BorderLayout());

		modelListScrollPane = new JScrollPane(modelList);
		modelListScrollPane.setBorder(BorderFactory.createEmptyBorder());

		panel.add(new JLabel("."), BorderLayout.NORTH);
		panel.add(modelListScrollPane, BorderLayout.CENTER);
		add(panel, BorderLayout.CENTER);

		add(createFooter("ok", "Cancel"), BorderLayout.SOUTH);
	}

	protected abstract void prepareCells();

	protected abstract Class<E> getModelClass();

	public void selectionChanged(ListSelectionEvent e) {
	}

	public void doubleClicked(E item) {

	}

	public List<E> getModels() {
		return models;
	}

	public void setModels(List<E> models) {
		this.models = models;
	}
}
