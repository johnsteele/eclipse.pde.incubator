/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipsecon.browser.view.ui.listeners;

import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipsecon.browser.view.BrowserMessages;
import org.eclipsecon.browser.view.BrowserPlugin;
import org.eclipsecon.browser.view.model.EclipseLinkObject;
import org.eclipsecon.browser.view.ui.EclipseLinksView;
import org.eclipsecon.browser.view.util.ImageLinkManager;

/**
 * FormTextHyperlinkListener
 *
 */
public class FormTextHyperlinkListener implements IHyperlinkListener {

	private EclipseLinksView fView;
	
	private EclipseLinkObject fCurrentLinkObject;
	
	/**
	 * @param view
	 */
	public FormTextHyperlinkListener(EclipseLinksView view) {
		fView = view;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.events.IHyperlinkListener#linkActivated(org.eclipse.ui.forms.events.HyperlinkEvent)
	 */
	public void linkActivated(HyperlinkEvent e) {
		handleLinkActivatedFormText(e);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.events.IHyperlinkListener#linkEntered(org.eclipse.ui.forms.events.HyperlinkEvent)
	 */
	public void linkEntered(HyperlinkEvent e) {
		handleLinkEnteredFormText(e);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.events.IHyperlinkListener#linkExited(org.eclipse.ui.forms.events.HyperlinkEvent)
	 */
	public void linkExited(HyperlinkEvent e) {
		handleLinkExitedFormText();
	}

	/**
	 * 
	 */
	private void handleLinkExitedFormText() {
		fView.getViewSite().getActionBars().getStatusLineManager().setMessage(null);
	}

	/**
	 * @param e
	 */
	private void handleLinkEnteredFormText(HyperlinkEvent e) {

		fCurrentLinkObject = null;
		StringBuffer message = new StringBuffer();
		// 
		fCurrentLinkObject = resolveHrefUsefulLinks(e.getHref());
		//
		if (fCurrentLinkObject == null) {
			fCurrentLinkObject = resolveHrefImageLinks(e.getHref());
		}
		//
		if (fCurrentLinkObject != null) {
			message.append(fCurrentLinkObject.getFieldName());
			message.append(" - [ "); //$NON-NLS-1$
			message.append(fCurrentLinkObject.getFieldLink());
			message.append(" ] - "); //$NON-NLS-1$
			message.append(fCurrentLinkObject.getFieldDescription());
		}
		
		fView.getViewSite().getActionBars().getStatusLineManager().setMessage(
				message.toString());
	}		
	
	/**
	 * @param e
	 */
	private void handleLinkActivatedFormText(HyperlinkEvent e) {
		// 
		if (fCurrentLinkObject == null) {
			BrowserPlugin.logException(
					BrowserMessages.OpenLinkAction_errorLinkAction, 
					BrowserMessages.FormTextHyperlinkListener_errorCannotResolveHref + e.getHref(), 
					new Exception());		
		}
		//
		fView.getOpenLinkAction().update(fCurrentLinkObject);
		fView.getOpenLinkAction().run();		
	}		
	
	/**
	 * @param href
	 * @return
	 */
	private EclipseLinkObject resolveHrefUsefulLinks(Object href) {
		EclipseLinkObject[] linkObjects = 
			BrowserPlugin.getUsefulLinkManager().getUsefulLinks(fView.getModel());
		//
		for (int i = 0; i < linkObjects.length; i++) {
			String thisHref = 
				new Integer(linkObjects[i].getFieldLink().hashCode()).toString();
			if (href.equals(thisHref)) {
				return linkObjects[i];
			}
		}	
		return null;
	}
	
	/**
	 * @param href
	 * @return
	 */
	private EclipseLinkObject resolveHrefImageLinks(Object href) {
		//
		if (href.equals("eclipse.org")) { //$NON-NLS-1$
			return ImageLinkManager.getEclipseImageLink(fView.getModel());
		} else if (href.equals("google.com")) { //$NON-NLS-1$
			return ImageLinkManager.getGoogleImageLink(fView.getModel());
		}
		return null;
	}
	
}
