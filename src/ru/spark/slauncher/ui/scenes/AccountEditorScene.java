package ru.spark.slauncher.ui.scenes;

import ru.spark.slauncher.ui.MainPane;
import ru.spark.slauncher.ui.accounts.AccountEditor;
import ru.spark.slauncher.ui.accounts.AccountHandler;
import ru.spark.slauncher.ui.accounts.AccountList;
import ru.spark.slauncher.ui.accounts.helper.AccountEditorHelper;
import ru.spark.slauncher.ui.accounts.helper.HelperState;

public class AccountEditorScene extends PseudoScene {
	private static final long serialVersionUID = -151325577614420989L;

	private final int ELEMENT_WIDTH = 225;
	private final int ELEMENT_HEIGHT = ELEMENT_WIDTH;

	public final AccountEditor editor;
	public final AccountList list;
	public final AccountEditorHelper helper;

	public final AccountHandler handler;

	public AccountEditorScene(MainPane main) {
		super(main);

		this.editor = new AccountEditor(this);
		this.editor.setSize(ELEMENT_WIDTH, ELEMENT_HEIGHT);
		this.add(editor);

		this.list = new AccountList(this);
		this.list.setSize(ELEMENT_WIDTH, ELEMENT_HEIGHT);
		this.add(list);

		this.handler = new AccountHandler(this);

		this.helper = new AccountEditorHelper(this);
		this.add(helper);
		
		handler.notifyEmpty();
	}

	@Override
	public void setShown(boolean shown, boolean animate) {
		super.setShown(shown, animate);

		if (shown && list.model.isEmpty())
			helper.setState(HelperState.HELP);
		else
			helper.setState(HelperState.NONE);
	}

	@Override
	public void onResize() {
		super.onResize();

		int hw = getWidth() / 2, hh = getHeight() / 2, heh = ELEMENT_HEIGHT / 2, y = hh
				- heh;

		int MARGIN = 10;
		this.editor.setLocation(hw - ELEMENT_WIDTH - MARGIN, y);
		this.list.setLocation(hw + MARGIN, y);
	}

}
