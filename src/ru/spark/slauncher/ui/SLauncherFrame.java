package ru.spark.slauncher.ui;

import com.spark.slauncher.PreloaderFrame;
import ru.spark.slauncher.Bootstrapper.LoadingStep;
import ru.spark.slauncher.SLauncher;
import ru.spark.slauncher.configuration.Configuration;
import ru.spark.slauncher.configuration.LangConfiguration;
import ru.spark.slauncher.ui.alert.Alert;
import ru.spark.slauncher.ui.block.Blocker;
import ru.spark.slauncher.ui.console.Console;
import ru.spark.slauncher.ui.loc.Localizable;
import ru.spark.slauncher.ui.loc.LocalizableMenuItem;
import ru.spark.slauncher.ui.scenes.SwingFXWebViewNew;
import ru.spark.slauncher.ui.scenes.SwingFxWebViewNewListener;
import ru.spark.slauncher.ui.swing.Dragger;
import ru.spark.slauncher.ui.swing.extended.ExtendedComponentAdapter;
import ru.spark.util.IntegerArray;
import ru.spark.util.OS;
import ru.spark.util.SwingUtil;
import ru.spark.util.U;
import ru.spark.util.async.ExtendedThread;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.File;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SLauncherFrame extends JFrame implements SwingFxWebViewNewListener {
	public static final Dimension minSize = new Dimension(1200, 700), maxSize = new Dimension(1920, 1080);
	public static final float fontSize = OS.WINDOWS.isCurrent()? 12f : 14f;

	private final SLauncherFrame instance;

	private final SLauncher tlauncher;
	private final Configuration settings;
	private final LangConfiguration lang;
	public SwingFXWebViewNew swingfx;
	//
	private final int[] windowSize;
	private final Point maxPoint;
	//
	public final MainPane mp;

	public static SLauncherFrame ref;
	//
	//final TitleUpdaterThread titleUpdater;

	public SLauncherFrame(SLauncher t) {
		if (PreloaderFrame.getInstance() != null) {
			PreloaderFrame.getInstance().setProgress(0.55F, "Загрузка менеджеров...");
		}
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double screenWidth = screenSize.getWidth();
		double screenHeight = screenSize.getHeight();

		double minWidth = 0.0D;
		double minHeight = 0.0D;
		if (screenWidth > 1366.0D)
		{
			minWidth = 1280.0D;

			minHeight = 700.0D;
		}
		else
		{
			minWidth = 1024.0D;
			minHeight = 700.0D;
		}
		minSize.setSize(minWidth, minHeight);
		this.instance = this;

		this.tlauncher = t;
		ref = this;
		this.settings = t.getSettings();
		this.lang = t.getLang();
		this.setPreferredSize(minSize);
		this.windowSize = settings.getLauncherWindowSize();
		this.maxPoint = new Point();

		SwingUtil.initFontSize((int) fontSize);
		SwingUtil.setFavicons(this);

		this.setUILocale();
		this.setWindowSize();
		this.setWindowTitle();

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				instance.setVisible(false);
				SLauncher.kill();
			}
		});
		this.setDefaultCloseOperation(EXIT_ON_CLOSE); // If code above isn't working under running os.

		this.addComponentListener(new ExtendedComponentAdapter(this) {
			@Override
			public void onComponentResized(ComponentEvent e) {
				updateMaxPoint();
				Dragger.update();

				boolean lock = getExtendedState() != NORMAL;
				Blocker.setBlocked(mp.defaultScene.settingsForm.launcherResolution, "extended", lock);

				if(lock)
					return;

				IntegerArray arr = new IntegerArray(getWidth(), getHeight());
				mp.defaultScene.settingsForm.launcherResolution.setValue(arr);
				settings.set("gui.size", arr);
			}

			@Override
			public void componentShown(ComponentEvent e) {
				instance.validate();
				instance.repaint();
				instance.toFront();
			}

			@Override
			public void componentHidden(ComponentEvent e) {

			}
		});
		if (PreloaderFrame.getInstance() != null) {
			PreloaderFrame.getInstance().setProgress(0.75F, "Загрузка менеджеров...");
		}
		this.addWindowStateListener(new WindowStateListener() {
			@Override
			public void windowStateChanged(WindowEvent e) {
				int newState = getExtendedStateFor(e.getNewState());
				if(newState == -1) return;

				settings.set("gui.window", newState);
			}
		});

		U.setLoadingStep(LoadingStep.PREPARING_MAINPANE);
		mp = new MainPane(this);

		add(mp);

		U.setLoadingStep(LoadingStep.POSTINIT_GUI);

		log("Packing main frame...");
		pack();

		log("Resizing main pane...");
		mp.onResize();

		updateMaxPoint();
		Dragger.ready(settings, maxPoint);

		if(SLauncher.isBeta())
			new TitleUpdaterThread();
		else
			setWindowTitle();
		if (PreloaderFrame.getInstance() != null) {
			PreloaderFrame.getInstance().setProgress(0.8F, "Загрузка менеджеров...");
		}

		int windowState = getExtendedStateFor(settings.getInteger("gui.window"));

		if(windowState == NORMAL)
			setLocationRelativeTo(null);
		else
			setExtendedState(windowState);

		if(settings.isFirstRun())
			Alert.showLocAsyncWarning("firstrun");
	}

	public void completeLoad()
	{

		ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);

		exec.schedule(new Runnable()
		{
			public void run()
			{
				int width = 0;
				int height = 0;


					int windowState = SLauncherFrame.getExtendedStateFor(SLauncherFrame.this.settings.getInteger("gui.window"));
					if (windowState == -1) {
						windowState = 6;
					}
					Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
					width = (int)screenSize.getSize().getWidth();
					height = (int)screenSize.getSize().getHeight();
					if (windowState == 0)
					{

						int winWidth = Double.valueOf(SLauncherFrame.minSize.getWidth()).intValue();
						int winHeight = Double.valueOf(SLauncherFrame.minSize.getHeight()).intValue();

						SLauncherFrame.this.setBounds((width - winWidth) / 2, (height - winHeight) / 2, winWidth, winHeight);
						SLauncherFrame.this.setLocationRelativeTo(null);
					}
					else
					{
						SLauncherFrame.this.setExtendedState(6);
					}

                if (PreloaderFrame.getInstance() != null)
                {
                    PreloaderFrame.getInstance().setVisible(false);

                    PreloaderFrame.getInstance().dispose();
                }
					SLauncherFrame.this.setVisible(true);

					SwingFXWebViewNew.ref.updateSize();

					SLauncher.getInstance().getUpdater().asyncFindUpdate();

					Configuration global = SLauncher.getInstance().getSettings();

					String path = global.get("old_version_exe_path");
					if ((path != null) && (!path.equals("")))
					{
						File file = new File(path);
						if (file.exists()) {
							file.delete();
						}
					}
					global.set("old_version_exe_path", "");
					global.store();
				}
			//}
		}, 5L, TimeUnit.SECONDS);
	}
	public SLauncher getLauncher() {
		return tlauncher;
	}

	public Point getMaxPoint() {
		return maxPoint;
	}

	public Configuration getConfiguration() {
		return settings;
	}

	public void updateLocales() {

		try {
			tlauncher.reloadLocale();
		} catch (Exception e) {
			log("Cannot reload settings!", e);
			return;
		}

		Console.updateLocale();
		LocalizableMenuItem.updateLocales();

		//setWindowTitle();
		setUILocale();

		Localizable.updateContainer(this);
	}

	private String brand;

	public void updateTitle() {
		//String translator = lang.nget("translator");
		//copyright = "by " + SLauncher.getDeveloper() + (translator != null ? ", translated by "+ translator : "");

		brand = SLauncher.getVersion() + (SLauncher.isBeta()? " BETA" : "");
	}

	public void setWindowTitle() {
		updateTitle();

		String title;

		if(SLauncher.isBeta())
			title = String.format("SLauncher %s [%s]", brand, U.memoryStatus());
		else
			title = String.format("SLauncher %s", brand);

		setTitle(title);
	}

	private void setWindowSize() {
		int width = (windowSize[0] > maxSize.width) ? maxSize.width : windowSize[0];
		int height = (windowSize[1] > maxSize.height) ? maxSize.height : windowSize[1];

		Dimension curSize = new Dimension(width, height);

		this.setMinimumSize(minSize);
		this.setPreferredSize(curSize);
	}

	private void setUILocale() {
		UIManager.put("OptionPane.yesButtonText", lang.nget("ui.yes"));
		UIManager.put("OptionPane.noButtonText", lang.nget("ui.no"));
		UIManager.put("OptionPane.cancelButtonText", lang.nget("ui.cancel"));

		// I know that I could place this into (Extended)ResourceBundle
		// But, you know...
		// NO.

		UIManager.put("FileChooser.acceptAllFileFilterText",
				lang.nget("explorer.extension.all"));

		UIManager.put("FileChooser.lookInLabelText",
				lang.nget("explorer.lookin"));
		UIManager.put("FileChooser.saveInLabelText",
				lang.nget("explorer.lookin"));

		UIManager.put("FileChooser.fileNameLabelText",
				lang.nget("explorer.input.filename"));
		UIManager.put("FileChooser.folderNameLabelText",
				lang.nget("explorer.input.foldername"));
		UIManager.put("FileChooser.filesOfTypeLabelText",
				lang.nget("explorer.input.type"));

		UIManager.put("FileChooser.upFolderToolTipText",
				lang.nget("explorer.button.up.tip"));
		UIManager.put("FileChooser.upFolderAccessibleName",
				lang.nget("explorer.button.up"));

		UIManager.put("FileChooser.newFolderToolTipText",
				lang.nget("explorer.button.newfolder.tip"));
		UIManager.put("FileChooser.newFolderAccessibleName",
				lang.nget("explorer.button.newfolder"));
		UIManager.put("FileChooser.newFolderButtonToolTipText",
				lang.nget("explorer.button.newfolder.tip"));
		UIManager.put("FileChooser.newFolderButtonText",
				lang.nget("explorer.button.newfolder"));

		UIManager.put("FileChooser.other.newFolder",
				lang.nget("explorer.button.newfolder.name"));
		UIManager.put("FileChooser.other.newFolder.subsequent",
				lang.nget("explorer.button.newfolder.name"));
		UIManager.put("FileChooser.win32.newFolder",
				lang.nget("explorer.button.newfolder.name"));
		UIManager.put("FileChooser.win32.newFolder.subsequent",
				lang.nget("explorer.button.newfolder.name"));

		UIManager.put("FileChooser.homeFolderToolTipText",
				lang.nget("explorer.button.home.tip"));
		UIManager.put("FileChooser.homeFolderAccessibleName",
				lang.nget("explorer.button.home"));

		UIManager.put("FileChooser.listViewButtonToolTipText",
				lang.nget("explorer.button.list.tip"));
		UIManager.put("FileChooser.listViewButtonAccessibleName",
				lang.nget("explorer.button.list"));

		UIManager.put("FileChooser.detailsViewButtonToolTipText",
				lang.nget("explorer.button.details.tip"));
		UIManager.put("FileChooser.detailsViewButtonAccessibleName",
				lang.nget("explorer.button.details"));

		UIManager.put("FileChooser.viewMenuButtonToolTipText",
				lang.nget("explorer.button.view.tip"));
		UIManager.put("FileChooser.viewMenuButtonAccessibleName",
				lang.nget("explorer.button.view"));

		UIManager.put("FileChooser.newFolderErrorText",
				lang.nget("explorer.error.newfolder"));
		UIManager.put("FileChooser.newFolderErrorSeparator", ": ");

		UIManager.put("FileChooser.newFolderParentDoesntExistTitleText",
				lang.nget("explorer.error.newfolder-nopath"));
		UIManager.put("FileChooser.newFolderParentDoesntExistText",
				lang.nget("explorer.error.newfolder-nopath"));

		UIManager.put("FileChooser.fileDescriptionText",
				lang.nget("explorer.details.file"));
		UIManager.put("FileChooser.directoryDescriptionText",
				lang.nget("explorer.details.dir"));

		UIManager.put("FileChooser.saveButtonText",
				lang.nget("explorer.button.save"));
		UIManager.put("FileChooser.openButtonText",
				lang.nget("explorer.button.open"));

		UIManager.put("FileChooser.saveDialogTitleText",
				lang.nget("explorer.title.save"));
		UIManager.put("FileChooser.openDialogTitleText",
				lang.nget("explorer.title.open"));
		UIManager.put("FileChooser.cancelButtonText",
				lang.nget("explorer.button.cancel"));
		UIManager.put("FileChooser.updateButtonText",
				lang.nget("explorer.button.update"));
		UIManager.put("FileChooser.helpButtonText",
				lang.nget("explorer.button.help"));
		UIManager.put("FileChooser.directoryOpenButtonText",
				lang.nget("explorer.button.open-dir"));

		UIManager.put("FileChooser.saveButtonToolTipText",
				lang.nget("explorer.title.save.tip"));
		UIManager.put("FileChooser.openButtonToolTipText",
				lang.nget("explorer.title.open.tip"));
		UIManager.put("FileChooser.cancelButtonToolTipText",
				lang.nget("explorer.button.cancel.tip"));
		UIManager.put("FileChooser.updateButtonToolTipText",
				lang.nget("explorer.button.update.tip"));
		UIManager.put("FileChooser.helpButtonToolTipText",
				lang.nget("explorer.title.help.tip"));
		UIManager.put("FileChooser.directoryOpenButtonToolTipText",
				lang.nget("explorer.button.open-dir.tip"));

		UIManager.put("FileChooser.viewMenuLabelText",
				lang.nget("explorer.button.view"));
		UIManager.put("FileChooser.refreshActionLabelText",
				lang.nget("explorer.context.refresh"));
		UIManager.put("FileChooser.newFolderActionLabelText",
				lang.nget("explorer.context.newfolder"));

		UIManager.put("FileChooser.listViewActionLabelText",
				lang.nget("explorer.view.list"));
		UIManager.put("FileChooser.detailsViewActionLabelText",
				lang.nget("explorer.view.details"));

		UIManager.put("FileChooser.filesListAccessibleName",
				lang.nget("explorer.view.list.name"));
		UIManager.put("FileChooser.filesDetailsAccessibleName",
				lang.nget("explorer.view.details.name"));

		UIManager.put("FileChooser.renameErrorTitleText",
				lang.nget("explorer.error.rename.title"));
		UIManager.put("FileChooser.renameErrorText",
				lang.nget("explorer.error.rename") + "\n{0}");
		UIManager.put("FileChooser.renameErrorFileExistsText",
				lang.nget("explorer.error.rename-exists"));

		UIManager.put("FileChooser.readOnly", Boolean.FALSE);
		UIManager.put("TabbedPane.contentOpaque", false);

		UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));
		UIManager.put("TabbedPane.tabInsets", new Insets(0, 8, 6, 8));
	}

	private void updateMaxPoint() {
		maxPoint.x = getWidth();
		maxPoint.y = getHeight();
	}

	@Override
	public void setSize(int width, int height) {
		if(getWidth() == width && getHeight() == height)
			return;

		if(getExtendedState() != NORMAL)
			return;

		boolean show = isVisible();

		if(show) {
			setVisible(false);
		}

		super.setSize(width, height);

		if(show) {
			setVisible(true);
			setLocationRelativeTo(null);
		}
	}

	@Override
	public void setSize(Dimension d) {
		setSize(d.width, d.height);
	}

	private static int getExtendedStateFor(int state) {
		switch(state) {
		case MAXIMIZED_BOTH:
		case MAXIMIZED_HORIZ:
		case MAXIMIZED_VERT:
		case NORMAL:
			return state;
		}
		return -1;
	}

	public static URL getRes(String uri) {
		return SLauncherFrame.class.getResource(uri);
	}

	private static void log(Object... o) {
		U.log("[Frame]", o);
	}

	private class TitleUpdaterThread extends ExtendedThread {

		TitleUpdaterThread() {
			super("TitleUpdater");

			updateTitle();
			start();
		}

		@Override
		public void run() {
			while(SLauncherFrame.this.isDisplayable()) {
				U.sleepFor(100);
				setWindowTitle();
			}

			log("Title updater is shut down.");
			interrupt();
		}
	}
}
