package net.mas0061.astah.plugin.sequence.util;

import java.awt.FileDialog;
import java.awt.Frame;
import java.util.Locale;

import javax.swing.JFrame;

import org.apache.commons.lang3.StringUtils;

public class FileChooseWrapper {
//	private static final String J_SAVE_TITLE = "XMLファイルを保存します";
	private static final String J_SAVE_TITLE = "Save as XML file.";
	private static final String E_SAVE_TITLE = "Save as XML file.";
	private static final String J_LOAD_TITLE = "読み込むXMLファイルを選択してください";
	private static final String E_LOAD_TITLE = "Load XML file name.";

	private JFrame mFrame;
	private FileDialog mSelectFileDlg;

	public FileChooseWrapper(JFrame frame) {
		mFrame = frame;
	}

	public String selectFile() {
		showDialog(FileDialog.LOAD);
		return mSelectFileDlg.getDirectory() + mSelectFileDlg.getFile();
	}

	public String selectDirectory() {
		showDialog(FileDialog.LOAD);
		return mSelectFileDlg.getDirectory();
	}

	public FileDialog selectDirectoryFile() {
		showDialog(FileDialog.LOAD);
		return mSelectFileDlg;
	}

	public String saveFile() {
		showDialog(FileDialog.SAVE);
		return mSelectFileDlg.getDirectory() + mSelectFileDlg.getFile();
	}

	public String saveDirectory() {
		showDialog(FileDialog.SAVE);
		return mSelectFileDlg.getDirectory();
	}

	public FileDialog saveDirectoryFile() {
		showDialog(FileDialog.SAVE);
		return mSelectFileDlg;
	}

	private void showDialog(int dialogType) {
		// 言語環境で、ファイルダイアログのタイトルを変える
		// 日本語か、それ以外での見分けしかしない
		String dialogTitle;

		if (isJapanese()) {
			dialogTitle = (dialogType == FileDialog.LOAD ? J_LOAD_TITLE
					: J_SAVE_TITLE);
		} else {
			dialogTitle = (dialogType == FileDialog.LOAD ? E_LOAD_TITLE
					: E_SAVE_TITLE);
		}

		FileDialog fdlg = new FileDialog((Frame) mFrame, dialogTitle,
				dialogType);
		fdlg.setDirectory(System.getProperty("user.home"));

		// Macの場合は、メニューバーをdisableにする
//		if (SystemUtils.IS_OS_MAC_OSX) {
//			mFrame.getJMenuBar().setEnabled(false);
//		} else {
//			mFrame.getJMenuBar().setEnabled(true);
//		}
		fdlg.setVisible(true);

		mSelectFileDlg = fdlg;
	}

	private boolean isJapanese() {
		return Locale.getDefault().equals(Locale.JAPANESE)
				|| Locale.getDefault().equals(Locale.JAPAN);
	}

	public boolean isFileSelected() {
		return StringUtils.isNotEmpty(mSelectFileDlg.getDirectory())
				&& StringUtils.isNotEmpty(mSelectFileDlg.getFile());
	}

	public boolean isDirectorySelected() {
		return StringUtils.isNotEmpty(mSelectFileDlg.getDirectory());
	}
}
