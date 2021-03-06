package ru.spark.slauncher.ui.login.buttons;

import ru.spark.slauncher.SLauncher;
import ru.spark.slauncher.ui.block.Blockable;
import ru.spark.slauncher.ui.loc.LocalizableComponent;
import ru.spark.slauncher.ui.loc.LocalizableMenuItem;
import ru.spark.slauncher.ui.login.LoginForm;
import ru.spark.slauncher.ui.swing.ImageButton;
import ru.spark.util.OS;
import ru.spark.util.U;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;

public class SupportButton extends ImageButton implements Blockable, LocalizableComponent {

	private final JPopupMenu[] popups = new JPopupMenu[SupportType.values().length]; {
		for(int i=0;i<popups.length;i++) {
			SupportType.values()[i].setupMenu(popups[i] = new JPopupMenu());
		}
	}

	private SupportType type;
	private int i;

	SupportButton(LoginForm loginForm) {
		this.rotation = ImageRotation.CENTER;

		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				popups[i].show(SupportButton.this, 0, getHeight());
			}
		});

		updateLocale();
	}

	public SupportType getType() {
		return type;
	}

	public void setType(SupportType type) {
		if(type == null)
			throw new NullPointerException("type");

		this.type = type;
		this.i = U.find(type, SupportType.values());

		setImage(type.image);
		repaint();
	}

	@Override
	public void updateLocale() {
		String locale = SLauncher.getInstance().getSettings().getLocale().toString();
		setType(locale.equals("ru_RU") || locale.equals("uk_UA")? SupportType.VK : SupportType.GMAIL);
	}

	@Override
	public void block(Object reason) {
	}

	@Override
	public void unblock(Object reason) {
	}

	public enum SupportType {
		VK("vk.png") {
			@Override
			public void setupMenu(JPopupMenu menu) {
				menu.add(newItem("loginform.button.support.follow", new ActionListener() {
					final URI followURI = U.makeURI("http://vk.com/slauncher");
					@Override
					public void actionPerformed(ActionEvent e) {
						OS.openLink(followURI);
					}
				}));

				menu.add(newItem("loginform.button.support.report", new ActionListener() {
					final URI reportURI = U.makeURI("http://goo.gl/NBlzdI");
					@Override
					public void actionPerformed(ActionEvent e) {
						OS.openLink(reportURI);
					}
				}));

				menu.add(newItem("loginform.button.support.author", new ActionListener() {
					final URI helpURI = U.makeURI("http://goo.gl/WVlo1o");
					@Override
					public void actionPerformed(ActionEvent e) {
						OS.openLink(helpURI);
					}
				}));
			}
		},
		GMAIL("mail.png") {
			@Override
			public void setupMenu(JPopupMenu menu) {
				menu.add(newItem("loginform.button.support.email", new ActionListener() {
					final URI devURI = U.makeURI("http://spark.ru/");
					@Override
					public void actionPerformed(ActionEvent e) {
						OS.openLink(devURI);
					}
				}));
			}
		};

		private final Image image;

		SupportType(String imagePath) {
			this.image = loadImage(imagePath);
		}

		public Image getImage() {
			return image;
		}

		public abstract void setupMenu(JPopupMenu menu);

		private static final LocalizableMenuItem newItem(String key, ActionListener action) {
			LocalizableMenuItem item = new LocalizableMenuItem(key);
			item.addActionListener(action);
			return item;
		}
	}

}
