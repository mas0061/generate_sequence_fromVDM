package net.mas0061.astah.plugin.sequence;

import java.io.File;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import net.mas0061.astah.plugin.sequence.vdm.CreateSequenceDiagram;
import net.mas0061.astah.plugin.sequence.vdm.ParseCallLog;
import net.mas0061.astah.plugin.sequence.vdm.node.CallNode;

import com.change_vision.jude.api.inf.ui.IPluginActionDelegate;
import com.change_vision.jude.api.inf.ui.IWindow;

public class TemplateAction implements IPluginActionDelegate {

	public Object run(IWindow window) throws UnExpectedException {
		JFileChooser chooser = new JFileChooser();
		int selected = chooser.showOpenDialog(window.getParent());

		if (selected == JFileChooser.APPROVE_OPTION) {
			createSequenceDiagram(chooser.getSelectedFile());
		} else if (selected == JFileChooser.CANCEL_OPTION
				|| selected == JFileChooser.ERROR_OPTION) {
			JOptionPane.showMessageDialog(window.getParent(), "ファイルを選択して下さい。",
					"Warning", JOptionPane.WARNING_MESSAGE);
		}

		return null;
	}

	private void createSequenceDiagram(File selectedFile) {
		CreateSequenceDiagram crSeqDiag = new CreateSequenceDiagram();
		crSeqDiag.create("SampleProject");
		ParseCallLog parser = new ParseCallLog();
		List<CallNode> nodes = parser.parse(selectedFile.getAbsolutePath());
		crSeqDiag.createSequenceDiagram("VDMcalllog", nodes);
	}

}
