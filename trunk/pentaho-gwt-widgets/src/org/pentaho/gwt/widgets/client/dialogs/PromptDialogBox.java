/*
 * This program is free software; you can redistribute it and/or modify it under the 
 * terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software 
 * Foundation.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this 
 * program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html 
 * or from the Free Software Foundation, Inc., 
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * Copyright 2008 Pentaho Corporation.  All rights reserved.
 *
 * Created Mar 25, 2008
 * @author Michael D'Amour
 */
package org.pentaho.gwt.widgets.client.dialogs;

import org.pentaho.gwt.widgets.client.buttons.RoundedButton;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Widget;

public class PromptDialogBox extends DialogBox {

  IDialogCallback callback;
  IDialogValidatorCallback validatorCallback;
  Widget content;
  final FlexTable dialogContent = new FlexTable();

  public PromptDialogBox(String title, String okText, String cancelText, boolean autoHide, boolean modal) {
    super(autoHide, modal);
    setText(title);
    RoundedButton ok = new RoundedButton(okText);
    ok.getElement().setAttribute("id", "okButton"); //$NON-NLS-1$ //$NON-NLS-2$
    ok.addClickListener(new ClickListener() {

      public void onClick(Widget sender) {
        if (validatorCallback == null || (validatorCallback != null && validatorCallback.validate())) {
          try {
            if (callback != null) {
              callback.okPressed();
            }
          } catch (Throwable dontCare) {
          }
          hide();
        }
      }
    });
    final HorizontalPanel dialogButtonPanel = new HorizontalPanel();
    dialogButtonPanel.setSpacing(2);
    dialogButtonPanel.add(ok);
    if (cancelText != null) {
      RoundedButton cancel = new RoundedButton(cancelText);
      cancel.getElement().setAttribute("id", "cancelButton"); //$NON-NLS-1$ //$NON-NLS-2$
      cancel.addClickListener(new ClickListener() {

        public void onClick(Widget sender) {
          try {
            if (callback != null) {
              callback.cancelPressed();
            }
          } catch (Throwable dontCare) {
          }
          hide();
        }
      });
      dialogButtonPanel.add(cancel);
    }
    HorizontalPanel dialogButtonPanelWrapper = new HorizontalPanel();
    if (okText != null && cancelText != null) {
      dialogButtonPanelWrapper.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    } else {
      dialogButtonPanelWrapper.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
    }
    dialogButtonPanelWrapper.setStyleName("dialogButtonPanel"); //$NON-NLS-1$
    dialogButtonPanelWrapper.setWidth("100%"); //$NON-NLS-1$
    dialogButtonPanelWrapper.add(dialogButtonPanel);

    if (content instanceof FocusWidget) {
      setFocusWidget((FocusWidget) content);
    }
    dialogContent.setCellPadding(0);
    dialogContent.setCellSpacing(0);
    dialogContent.getFlexCellFormatter().setVerticalAlignment(1, 0, HasVerticalAlignment.ALIGN_TOP);
    dialogContent.getFlexCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_LEFT);
    // add button panel
    dialogContent.setWidget(2, 0, dialogButtonPanelWrapper);
    dialogContent.getCellFormatter().setVerticalAlignment(2, 0, HasVerticalAlignment.ALIGN_BOTTOM);
    // dialogContent.getFlexCellFormatter().setColSpan(2, 0, 2);
    dialogContent.setWidth("100%"); //$NON-NLS-1$
    setWidget(dialogContent);
  }

  public PromptDialogBox(String title, String okText, String cancelText, boolean autoHide, boolean modal, Widget content) {

    this(title, okText, cancelText, autoHide, modal);
    setContent(content);
  }

  public boolean onKeyDownPreview(char key, int modifiers) {
    // Use the popup's key preview hooks to close the dialog when either
    // enter or escape is pressed.
    switch (key) {
    case KeyboardListener.KEY_ENTER:
      if (validatorCallback == null || (validatorCallback != null && validatorCallback.validate())) {
        hide();
        if (callback != null) {
          callback.okPressed();
        }
      }
      break;
    case KeyboardListener.KEY_ESCAPE:
      if (callback != null) {
        callback.cancelPressed();
      }
      hide();
      break;
    }
    return true;
  }

  public IDialogCallback getCallback() {
    return callback;
  }

  public void setContent(Widget content) {
    this.content = content;
    if (content != null) {
      dialogContent.getFlexCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_MIDDLE);
      dialogContent.getFlexCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
      dialogContent.setWidget(0, 0, content);
      DOM.setStyleAttribute(dialogContent.getCellFormatter().getElement(0,0), "padding", "5px 10px 10px 10px"); //$NON-NLS-1$ //$NON-NLS-2$
      content.setHeight("100%"); //$NON-NLS-1$
      content.setWidth("100%"); //$NON-NLS-1$
    }
  }

  public Widget getContent() {
    return content;
  }

  public void setCallback(IDialogCallback callback) {
    this.callback = callback;
  }

  public IDialogValidatorCallback getValidatorCallback() {
    return validatorCallback;
  }

  public void setValidatorCallback(IDialogValidatorCallback validatorCallback) {
    this.validatorCallback = validatorCallback;
  }

}
