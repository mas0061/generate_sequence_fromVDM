package net.mas0061.astah.plugin.sequence.vdm

import net.mas0061.astah.plugin.sequence.vdm.node.CallNode

import com.change_vision.jude.api.inf.AstahAPI
import com.change_vision.jude.api.inf.editor.SequenceDiagramEditor
import com.change_vision.jude.api.inf.editor.TransactionManager
import com.change_vision.jude.api.inf.exception.InvalidEditingException
import com.change_vision.jude.api.inf.model.ISequenceDiagram
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
		save()
		prjAccsr.close()
	}
	
	def createSequenceDiagram(String diagramName, List<CallNode> llList) {
		TransactionManager.beginTransaction()
		
		SequenceDiagramEditor seqDiagEditor = prjAccsr.getDiagramEditorFactory().getSequenceDiagramEditor()
		ISequenceDiagram seqDiag = seqDiagEditor.createSequenceDiagram(prjAccsr.getProject(), diagramName)
		
		List<INodePresentation> lifelines = []
		
		def pos = 0
		llList.collect { it.className }.unique().each {
			INodePresentation node = seqDiagEditor.createLifeline(it, pos)
			lifelines.add(node)
			pos += 200
		}
		
		pos = 100
		CallNode prevNode
		llList.each { node ->
			INodePresentation to = lifelines.find { it.getLabel() == node.className }
			
			try {
				if (node.spaceNum == 0) {			
					seqDiagEditor.createMessage(node.fnOpName, to, to, pos)
				} else {
					INodePresentation from = lifelines.find { it.getLabel() == prevNode.className }
					seqDiagEditor.createMessage(node.fnOpName, from, to, pos)
				}
			} catch (InvalidEditingException e) {
				println e.key
			}
			
			prevNode = node
			pos += 50
		}		
		
		TransactionManager.endTransaction()
	}
}
