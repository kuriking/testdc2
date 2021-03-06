/*
 * Copyright (C) 2012  Nagoya University All rights reserved.
 */
package net.dependableos.dcase.diagram.editor.wizard;

import java.lang.reflect.InvocationTargetException;

import net.dependableos.dcase.diagram.common.exception.DcaseSystemException;
import net.dependableos.dcase.diagram.common.util.FileUtil;
import net.dependableos.dcase.diagram.common.util.MessageTypeImpl;
import net.dependableos.dcase.diagram.editor.common.util.MessageWriter;
import net.dependableos.dcase.diagram.editor.message.Messages;

import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

/**
 * A wizard to convert a GMF model file to a Text file.
 */
public class XmlConversionToTextWizard extends Wizard implements INewWizard {

	/**
	 * the wizard page to select an input file and an output file.
	 */
	private XmlConversionToTextWizardPage convertDefinitionPage;

	/**
	 * the selected object.
	 */
	private IStructuredSelection selection;

	/**
	 * Initializes.
	 * 
	 * @param workbench
	 *            the workbench.
	 * @param selection
	 *            the selected object.
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
	 *      org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
		setWindowTitle(Messages.XmlConversionToTextWizardPage_0);

		// sets whether this wizard needs a progress monitor.
		setNeedsProgressMonitor(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		convertDefinitionPage = new XmlConversionToTextWizardPage(selection);
		addPage(convertDefinitionPage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		IRunnableWithProgress operation = new XmlConversionToTextRunnable(
				FileUtil.getWorksapceFileFromPath(convertDefinitionPage
						.getInputFilePath().getText()),
				FileUtil.getWorksapceFileFromPath(convertDefinitionPage
						.getOutputFilePath().getText()),
				convertDefinitionPage.isOverwriteOption());

		IWizardContainer container = getContainer();
		try {
			container.run(true, true, operation);
		} catch (InvocationTargetException e) {
			DcaseSystemException dcaseSystemException = new DcaseSystemException(
					Messages.XmlConversionToTextWizard_1, null,
					MessageTypeImpl.CONVERT_TO_TEXT_FAILED);
			MessageWriter.writeMessageToErrorLog(dcaseSystemException);
			MessageWriter.showMessageBoxSeeErroLog();
			return false;
		} catch (InterruptedException e) {
			DcaseSystemException dcaseSystemException = new DcaseSystemException(
					Messages.XmlConversionToTextWizard_2, null,
					MessageTypeImpl.CONVERT_TO_TEXT_FAILED);
			MessageWriter.writeMessageToErrorLog(dcaseSystemException);
			MessageWriter.showMessageBoxSeeErroLog();
			return false;
		}
		return true;
	}

}
