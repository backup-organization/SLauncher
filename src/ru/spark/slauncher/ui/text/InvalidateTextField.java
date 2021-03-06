package ru.spark.slauncher.ui.text;

import ru.spark.slauncher.ui.center.CenterPanel;

/**
 * <code>InvalidateTextField</code> is non-abstract implementation of
 * <code>CheckableTextField</code> that can't be set invalid by itself. This can
 * be used to implement more complicated validity check.
 * 
 * @author Artur Khusainov
 * @see com.spark.slauncher.ui.settings.ResolutionField
 * @see ru.spark.slauncher.ui.console.SearchField
 * 
 */
public class InvalidateTextField extends CheckableTextField {
	private static final long serialVersionUID = -4076362911409776688L;

	protected InvalidateTextField(CenterPanel panel, String placeholder,
			String value) {
		super(panel, placeholder, value);
	}

	public InvalidateTextField(CenterPanel panel) {
		this(panel, null, null);
	}

	@Override
	protected String check(String text) {
		return null;
	}
}
