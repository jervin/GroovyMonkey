package net.sf.groovyMonkey;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IconAndMessageDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public class ErrorDialog
extends IconAndMessageDialog
{
    /**
    Static to prevent opening of error dialogs for automated testing.
    */
   public static boolean AUTOMATED_MODE = false;

   /**
    * Reserve room for this many list items.
    */
   private static final int LIST_ITEM_COUNT = 7;

   /**
    * The nesting indent.
    */
   private static final String NESTING_INDENT = "  "; //$NON-NLS-1$

   /**
    * The Details button.
    */
   private Button detailsButton;

   /**
    * The title of the dialog.
    */
   private String title;

   /**
    * The SWT list control that displays the error details.
    */
   private List list;

   /**
    * Indicates whether the error details viewer is currently created.
    */
   private boolean listCreated = false;

   /**
    * Filter mask for determining which status items to display.
    */
   private int displayMask = 0xFFFF;

   /**
    * The main status object.
    */
   private IStatus status;

   /**
    * The current clipboard. To be disposed when closing the dialog.
    */
   private Clipboard clipboard;

    private boolean shouldIncludeTopLevelErrorInDetails = true;

   /**
    * Creates an error dialog. Note that the dialog will have no visual
    * representation (no widgets) until it is told to open.
    * <p>
    * Normally one should use <code>openError</code> to create and open one
    * of these. This constructor is useful only if the error object being
    * displayed contains child items <it>and </it> you need to specify a mask
    * which will be used to filter the displaying of these children.
    * </p>
    * 
    * @param parentShell
    *            the shell under which to create this dialog
    * @param dialogTitle
    *            the title to use for this dialog, or <code>null</code> to
    *            indicate that the default title should be used
    * @param message
    *            the message to show in this dialog, or <code>null</code> to
    *            indicate that the error's message should be shown as the
    *            primary message
    * @param status
    *            the error to show to the user
    * @param displayMask
    *            the mask to use to filter the displaying of child items, as
    *            per <code>IStatus.matches</code>
    * @see org.eclipse.core.runtime.IStatus#matches(int)
    */
   public ErrorDialog(Shell parentShell, String dialogTitle, String message,
           IStatus status, int displayMask) {
       super(parentShell);
       this.title = dialogTitle == null ? JFaceResources
               .getString("Problem_Occurred") : //$NON-NLS-1$
               dialogTitle;
       this.message = message == null ? status.getMessage()
               : JFaceResources
                       .format(
                               "Reason", new Object[] { message, status.getMessage() }); //$NON-NLS-1$
       this.status = status;
       this.displayMask = displayMask;
       setShellStyle(getShellStyle() | SWT.RESIZE);
   }

   /*
    * (non-Javadoc) Method declared on Dialog. Handles the pressing of the Ok
    * or Details button in this dialog. If the Ok button was pressed then close
    * this dialog. If the Details button was pressed then toggle the displaying
    * of the error details area. Note that the Details button will only be
    * visible if the error being displayed specifies child details.
    */
   protected void buttonPressed(int id) {
       if (id == IDialogConstants.DETAILS_ID) {
           // was the details button pressed?
           toggleDetailsArea();
       } else {
           super.buttonPressed(id);
       }
   }

   /*
    * (non-Javadoc) Method declared in Window.
    */
   protected void configureShell(Shell shell) {
       super.configureShell(shell);
       shell.setText(title);
   }

   /*
    * (non-Javadoc) Method declared on Dialog.
    */
   protected void createButtonsForButtonBar(Composite parent) {
       // create OK and Details buttons
       createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
               true);
       createDetailsButton(parent);
   }

   /**
    * Create the details button if it should be included.
    * @param parent the parent composite
    * @since 3.2
    */
    protected void createDetailsButton(Composite parent) {
        if (shouldShowDetailsButton()) {
           detailsButton = createButton(parent, IDialogConstants.DETAILS_ID,
                   IDialogConstants.SHOW_DETAILS_LABEL, false);
       }
    }

   /**
    * This implementation of the <code>Dialog</code> framework method creates
    * and lays out a composite and calls <code>createMessageArea</code> and
    * <code>createCustomArea</code> to populate it. Subclasses should
    * override <code>createCustomArea</code> to add contents below the
    * message.
    */
   protected Control createDialogArea(Composite parent) {
       createMessageArea(parent);
       // create a composite with standard margins and spacing
       Composite composite = new Composite(parent, SWT.NONE);
       GridLayout layout = new GridLayout();
       layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
       layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
       layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
       layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
       layout.numColumns = 2;
       composite.setLayout(layout);
       GridData childData = new GridData(GridData.FILL_BOTH);
       childData.horizontalSpan = 2;
       composite.setLayoutData(childData);
       composite.setFont(parent.getFont());
       return composite;
   }

   /*
    * @see IconAndMessageDialog#createDialogAndButtonArea(Composite)
    */
   protected void createDialogAndButtonArea(Composite parent) {
       super.createDialogAndButtonArea(parent);
       if (this.dialogArea instanceof Composite) {
           //Create a label if there are no children to force a smaller layout
           Composite dialogComposite = (Composite) dialogArea;
           if (dialogComposite.getChildren().length == 0) {
                new Label(dialogComposite, SWT.NULL);
            }
       }
   }

   /*
    *  (non-Javadoc)
    * @see org.eclipse.jface.dialogs.IconAndMessageDialog#getImage()
    */
   protected Image getImage() {
       if (status != null) {
           if (status.getSeverity() == IStatus.WARNING) {
                return getWarningImage();
            }
           if (status.getSeverity() == IStatus.INFO) {
                return getInfoImage();
            }
       }
       //If it was not a warning or an error then return the error image
       return getErrorImage();
   }

   /**
    * Create this dialog's drop-down list component.
    * 
    * @param parent
    *            the parent composite
    * @return the drop-down list component
    */
   protected List createDropDownList(Composite parent) {
       // create the list
       list = new List(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL
               | SWT.MULTI | SWT.RESIZE );
       // fill the list
       populateList(list);
       GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL
               | GridData.GRAB_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL
               | GridData.GRAB_VERTICAL);
       data.heightHint = list.getItemHeight() * ( LIST_ITEM_COUNT > list.getItemCount() ? LIST_ITEM_COUNT : list.getItemCount() );
       data.horizontalSpan = 2;
       list.setLayoutData(data);
       list.setFont(parent.getFont());
       Menu copyMenu = new Menu(list);
       MenuItem copyItem = new MenuItem(copyMenu, SWT.NONE);
       copyItem.addSelectionListener(new SelectionListener() {
           /*
            * @see SelectionListener.widgetSelected (SelectionEvent)
            */
           public void widgetSelected(SelectionEvent e) {
               copyToClipboard();
           }

           /*
            * @see SelectionListener.widgetDefaultSelected(SelectionEvent)
            */
           public void widgetDefaultSelected(SelectionEvent e) {
               copyToClipboard();
           }
       });
       copyItem.setText(JFaceResources.getString("copy")); //$NON-NLS-1$
       list.setMenu(copyMenu);
       listCreated = true;
       return list;
   }

   /*
    * (non-Javadoc) Method declared on Window.
    */
   /**
    * Extends <code>Window.open()</code>. Opens an error dialog to display
    * the error. If you specified a mask to filter the displaying of these
    * children, the error dialog will only be displayed if there is at least
    * one child status matching the mask.
    */
   public int open() {
       if (!AUTOMATED_MODE && shouldDisplay(status, displayMask)) {
           return super.open();
       }
       setReturnCode(OK);
       return OK;
   }

   /**
    * Opens an error dialog to display the given error. Use this method if the
    * error object being displayed does not contain child items, or if you wish
    * to display all such items without filtering.
    * 
    * @param parent
    *            the parent shell of the dialog, or <code>null</code> if none
    * @param dialogTitle
    *            the title to use for this dialog, or <code>null</code> to
    *            indicate that the default title should be used
    * @param message
    *            the message to show in this dialog, or <code>null</code> to
    *            indicate that the error's message should be shown as the
    *            primary message
    * @param status
    *            the error to show to the user
    * @return the code of the button that was pressed that resulted in this
    *         dialog closing. This will be <code>Dialog.OK</code> if the OK
    *         button was pressed, or <code>Dialog.CANCEL</code> if this
    *         dialog's close window decoration or the ESC key was used.
    */
   public static int openError(Shell parent, String dialogTitle,
           String message, IStatus status) {
       return openError(parent, dialogTitle, message, status, IStatus.OK
               | IStatus.INFO | IStatus.WARNING | IStatus.ERROR);
   }

   /**
    * Opens an error dialog to display the given error. Use this method if the
    * error object being displayed contains child items <it>and </it> you wish
    * to specify a mask which will be used to filter the displaying of these
    * children. The error dialog will only be displayed if there is at least
    * one child status matching the mask.
    * 
    * @param parentShell
    *            the parent shell of the dialog, or <code>null</code> if none
    * @param title
    *            the title to use for this dialog, or <code>null</code> to
    *            indicate that the default title should be used
    * @param message
    *            the message to show in this dialog, or <code>null</code> to
    *            indicate that the error's message should be shown as the
    *            primary message
    * @param status
    *            the error to show to the user
    * @param displayMask
    *            the mask to use to filter the displaying of child items, as
    *            per <code>IStatus.matches</code>
    * @return the code of the button that was pressed that resulted in this
    *         dialog closing. This will be <code>Dialog.OK</code> if the OK
    *         button was pressed, or <code>Dialog.CANCEL</code> if this
    *         dialog's close window decoration or the ESC key was used.
    * @see org.eclipse.core.runtime.IStatus#matches(int)
    */
   public static int openError(Shell parentShell, String title,
           String message, IStatus status, int displayMask) {
       ErrorDialog dialog = new ErrorDialog(parentShell, title, message,
               status, displayMask);
       return dialog.open();
   }

   /**
    * Populates the list using this error dialog's status object. This walks
    * the child static of the status object and displays them in a list. The
    * format for each entry is status_path : status_message If the status's
    * path was null then it (and the colon) are omitted.
    * @param listToPopulate The list to fill.
    */
   private void populateList(List listToPopulate) {
       populateList(listToPopulate, status, 0, shouldIncludeTopLevelErrorInDetails);
   }

   /**
    * Populate the list with the messages from the given status. Traverse the
    * children of the status deeply and also traverse CoreExceptions that appear
    * in the status.
    * @param listToPopulate the list to populate
    * @param buildingStatus the status being displayed
    * @param nesting the nesting level (increases one level for each level of children)
    * @param includeStatus whether to include the buildingStatus in the display or
    * just its children
    */
   private void populateList(List listToPopulate, IStatus buildingStatus,
           int nesting, boolean includeStatus) {
       
       if (!buildingStatus.matches(displayMask)) {
           return;
       }

       Throwable t = buildingStatus.getException();
       boolean isCoreException= t instanceof CoreException;
       boolean incrementNesting= false;
       
        if (includeStatus) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < nesting; i++) {
                sb.append(NESTING_INDENT);
            }
            String message = buildingStatus.getMessage();
           sb.append(message);
            listToPopulate.add(sb.toString());
            incrementNesting= true;
        }
        
       if (!isCoreException && t != null) {
        // Include low-level exception message
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < nesting; i++) {
                sb.append(NESTING_INDENT);
            }
            String message = t.getLocalizedMessage();
            if (message == null) {
                message = t.toString();
            }
                
            sb.append(message);
            listToPopulate.add(sb.toString());
            for( final String line : getStackTrace( t ) )
                listToPopulate.add( new String( NESTING_INDENT + StringUtils.strip( line ) ) );
            incrementNesting= true;
       }
       
       if (incrementNesting) {
            nesting++;
        }
       
       // Look for a nested core exception
       if (isCoreException) {
           CoreException ce = (CoreException)t;
           IStatus eStatus = ce.getStatus();
           // Only print the exception message if it is not contained in the parent message
           if (message == null || message.indexOf(eStatus.getMessage()) == -1) {
               populateList(listToPopulate, eStatus, nesting, true);
           }
       }

       
       // Look for child status
       IStatus[] children = buildingStatus.getChildren();
       for (int i = 0; i < children.length; i++) {
           populateList(listToPopulate, children[i], nesting, true);
       }
   }

   private java.util.List< String > getStackTrace( final Throwable t )
   {
       final String stackTrace = new StringPrintWriter( t ).getString();
       final String[] array = StringUtils.split( StringUtils.replace( stackTrace, "\r\n", "\n" ), "\n" );
       final java.util.List< String > lines = new ArrayList< String >();
       for( final String line : array )
           lines.add( line );
       if( t.getCause() != null )
           lines.addAll( getStackTrace( t.getCause() ) );
       return lines;
   }

   /**
    * Returns whether the given status object should be displayed.
    * 
    * @param status
    *            a status object
    * @param mask
    *            a mask as per <code>IStatus.matches</code>
    * @return <code>true</code> if the given status should be displayed, and
    *         <code>false</code> otherwise
    * @see org.eclipse.core.runtime.IStatus#matches(int)
    */
   protected static boolean shouldDisplay(IStatus status, int mask) {
       IStatus[] children = status.getChildren();
       if (children == null || children.length == 0) {
           return status.matches(mask);
       }
       for (int i = 0; i < children.length; i++) {
           if (children[i].matches(mask)) {
                return true;
            }
       }
       return false;
   }

   /**
    * Toggles the unfolding of the details area. This is triggered by the user
    * pressing the details button.
    */
   private void toggleDetailsArea() {
       Point windowSize = getShell().getSize();
       Point oldSize = getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT);
       if (listCreated) {
           list.dispose();
           listCreated = false;
           detailsButton.setText(IDialogConstants.SHOW_DETAILS_LABEL);
       } else {
           list = createDropDownList((Composite) getContents());
           detailsButton.setText(IDialogConstants.HIDE_DETAILS_LABEL);
       }
       Point newSize = getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT);
       getShell()
               .setSize(
                       new Point(windowSize.x, windowSize.y
                               + (newSize.y - oldSize.y)));
   }

   /**
    * Put the details of the status of the error onto the stream.
    * 
    * @param buildingStatus
    * @param buffer
    * @param nesting
    */
   private void populateCopyBuffer(IStatus buildingStatus,
           StringBuffer buffer, int nesting) {
       if (!buildingStatus.matches(displayMask)) {
           return;
       }
       for (int i = 0; i < nesting; i++) {
           buffer.append(NESTING_INDENT);
       }
       buffer.append(buildingStatus.getMessage());
       buffer.append("\n"); //$NON-NLS-1$
       
       // Look for a nested core exception
       Throwable t = buildingStatus.getException();
       if (t instanceof CoreException) {
           CoreException ce = (CoreException)t;
           populateCopyBuffer(ce.getStatus(), buffer, nesting + 1);
       }
       
       IStatus[] children = buildingStatus.getChildren();
       for (int i = 0; i < children.length; i++) {
           populateCopyBuffer(children[i], buffer, nesting + 1);
       }
   }

   /**
    * Copy the contents of the statuses to the clipboard.
    */
   private void copyToClipboard() {
       if (clipboard != null) {
            clipboard.dispose();
        }
       StringBuffer statusBuffer = new StringBuffer();
       populateCopyBuffer(status, statusBuffer, 0);
       clipboard = new Clipboard(list.getDisplay());
       clipboard.setContents(new Object[] { statusBuffer.toString() },
               new Transfer[] { TextTransfer.getInstance() });
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.eclipse.jface.window.Window#close()
    */
   public boolean close() {
       if (clipboard != null) {
            clipboard.dispose();
        }
       return super.close();
   }
   
   /**
    * Show the details portion of the dialog if it is not already visible.
    * This method will only work when it is invoked after the control of the dialog
    * has been set. In other words, after the <code>createContents</code> method
    * has been invoked and has returned the control for the content area of the dialog.
    * Invoking the method before the content area has been set or after the dialog has been
    * disposed will have no effect.
    * @since 3.1
    */
   protected final void showDetailsArea() {
       if (!listCreated) {
           Control control = getContents();
           if (control != null && ! control.isDisposed()) {
                toggleDetailsArea();
            }
       }
   }
   
   /**
    * Return whether the Details button should be included.
    * This method is invoked once when the dialog is built.
    * By default, the Details button is only included if
    * the status used when creating the dialog was a multi-status
    * or if the status contains an exception.
    * Subclasses may override.
    * @return whether the Details button should be included
    * @since 3.1
    */
   protected boolean shouldShowDetailsButton() {
       return status.isMultiStatus() || status.getException() != null;
   }
   
   /**
    * Set the status displayed by this error dialog to the given status.
    * This only affects the status displayed by the Details list.
    * The message, image and title should be updated by the subclass,
    * if desired.
    * @param status the status to be displayed in the details list
    * @since 3.1
    */
   protected final void setStatus(IStatus status) {
       if (this.status != status) {
            this.status = status;
       }
       shouldIncludeTopLevelErrorInDetails = true;
       if (listCreated) {
           repopulateList();
       }
   }
   
   /**
    * Repopulate the supplied list widget.
    */
   private void repopulateList() {
       if (list != null && !list.isDisposed()) {
            list.removeAll();
            populateList(list);
       }
   }
   
   class StringPrintWriter extends PrintWriter {
       
       /**
        * Constructs a new instance.
        */
       public StringPrintWriter() {
           super(new StringWriter());
       }

       /**
        * Constructs a new instance using the specified initial string-buffer
        * size.
        *
        * @param initialSize  an int specifying the initial size of the buffer.
        */
       public StringPrintWriter(int initialSize) {
           super(new StringWriter(initialSize));
       }

       public StringPrintWriter( Throwable exception )
       {
           this();
           exception.printStackTrace( this );
       }

       /**
        * <p>Since toString() returns information *about* this object, we
        * want a separate method to extract just the contents of the
        * internal buffer as a String.</p>
        *
        * @return the contents of the internal string buffer
        */
       public String getString() {
           flush();
           return ((StringWriter) this.out).toString();
       }
       
   }
}