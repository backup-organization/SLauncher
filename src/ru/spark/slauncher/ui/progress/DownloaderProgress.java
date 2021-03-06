package ru.spark.slauncher.ui.progress;

import ru.spark.slauncher.SLauncher;
import ru.spark.slauncher.downloader.Downloadable;
import ru.spark.slauncher.downloader.Downloader;
import ru.spark.slauncher.downloader.DownloaderListener;
import ru.spark.slauncher.ui.loc.LocalizableProgressBar;

import java.awt.*;

public class DownloaderProgress extends LocalizableProgressBar implements
		DownloaderListener {
	private static final long serialVersionUID = -8382205925341380876L;

	private DownloaderProgress(Component parentComp, Downloader downloader) {
		super(parentComp);

		if (downloader == null)
			throw new NullPointerException();

		downloader.addListener(this);
		stopProgress(); // Hide the bar
	}

	public DownloaderProgress(Component parentComp) {
		this(parentComp, SLauncher.getInstance().getDownloader());
	}

	@Override
	public void onDownloaderStart(Downloader d, int files) {
		startProgress();

		setIndeterminate(true);

		setCenterString("progressBar.init");
		setEastString("progressBar.downloading", files);
	}

	@Override
	public void onDownloaderAbort(Downloader d) {
		stopProgress();
	}

	@Override
	public void onDownloaderProgress(Downloader d, double dprogress,
			double speed) {
		if (dprogress > 0) {
			int progress = (int) (dprogress * 100);
			if (getValue() > progress)
				return; // Something from a "lazy" thread, ignore.

			setIndeterminate(false);
			setValue(progress);
			setCenterString(progress + "%");
		}
	}

	@Override
	public void onDownloaderFileComplete(Downloader d, Downloadable file) {
		setIndeterminate(false);

		setEastString("progressBar.remaining", d.getRemaining());
	}

	@Override
	public void onDownloaderComplete(Downloader d) {
		stopProgress();
	}
}
