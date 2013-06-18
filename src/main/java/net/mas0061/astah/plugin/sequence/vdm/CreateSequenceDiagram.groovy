package net.mas0061.astah.plugin.sequence.vdm

import net.mas0061.astah.plugin.sequence.vdm.node.CallNode

import com.change_vision.jude.api.inf.AstahAPI
import com.change_vision.jude.api.inf.editor.SequenceDiagramEditor
import com.change_vision.jude.api.inf.editor.TransactionManager
import com.change_vision.jude.api.inf.exception.InvalidEditingException
import com.change_vision.jude.api.inf.model.ISequenceDiagram
import com.change_vision.jude.api.inf.presentation.ILinkPresentation;
import com.change_vision.jude.api.inf.presentation.INodePresentation
import com.change_vision.jude.api.inf.project.ProjectAccessor

class CreateSequenceDiagram {
	ProjectAccessor prjAccsr

	CreateSequenceDiagram() {
		try {
			prjAccsr = AstahAPI.getAstahAPI().getProjectAccessor()
		} catch(e) {
			e.stackTrace()
		}
	}

	def create(String prjName) {
		prjAccsr.create("${prjName}.asta")
	}

	def save() {
		prjAccsr.save()
	}

	def close() {
		prjAccsr.close()
	}

	def createSequenceDiagram(String diagramName, List<CallNode> llList) {
		try {
			TransactionManager.beginTransaction()

			SequenceDiagramEditor seqDiagEditor = prjAccsr.getDiagramEditorFactory().getSequenceDiagramEditor()
			ISequenceDiagram seqDiag = seqDiagEditor.createSequenceDiagram(prjAccsr.getProject(), diagramName)

			List<INodePresentation> lifelines = []

			def posX = 0
			llList.collect { it.className }.unique().each {
				INodePresentation node = seqDiagEditor.createLifeline(it, pos)
				lifelines.add(node)
				posX += 200
			}

			def posY = 100
			ILinkPresentation prevLink;
			INodePresentation from = lifelines.first()

			llList.eachWithIndex { node, idx ->
				INodePresentation to = lifelines.find { it.getLabel() == node.className }

				if (node.spaceNum == 0) {
					prevLink = seqDiagEditor.createMessage(node.fnOpName, to, to, posY)
				} else {
					CallNode prevNode = llList[idx - 1]
					if (prevNode.spaceNum <= node.spaceNum) {
						prevLink = seqDiagEditor.createMessage(node.fnOpName, prevLink.getTarget(), to, posY)
					} else {
						prevLink = seqDiagEditor.createMessage(node.fnOpName, from, to, posY)
						from = to
					}
				}

				posY += 70
			}

			TransactionManager.endTransaction()
		} catch (InvalidEditingException e) {
			println e.key
		} finally {
			TransactionManager.abortTransaction()
		}
	}
}
