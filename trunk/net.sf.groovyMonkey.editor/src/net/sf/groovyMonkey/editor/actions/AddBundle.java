package net.sf.groovyMonkey.editor.actions;
import static net.sf.groovyMonkey.ScriptMetadata.getMetadataLines;
import static net.sf.groovyMonkey.ScriptMetadata.getScriptMetadata;
import static net.sf.groovyMonkey.ScriptMetadata.stripMetadata;
import static net.sf.groovyMonkey.dom.Utilities.activePage;
import static net.sf.groovyMonkey.dom.Utilities.error;
import static net.sf.groovyMonkey.dom.Utilities.getAllAvailableBundles;
import static net.sf.groovyMonkey.dom.Utilities.getContents;
import static net.sf.groovyMonkey.dom.Utilities.getUpdateSiteForDOMPlugin;
import static net.sf.groovyMonkey.dom.Utilities.shell;
import static net.sf.groovyMonkey.editor.ScriptContentProvider.getBundles;
import static net.sf.groovyMonkey.editor.actions.AddDialog.createAddBundleDialog;
import static org.apache.commons.lang.StringUtils.join;
import static org.eclipse.core.resources.IResource.DEPTH_ONE;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import net.sf.groovyMonkey.ScriptMetadata;
import net.sf.groovyMonkey.editor.ScriptEditor;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;

public class AddBundle 
extends Action
implements IObjectActionDelegate
{
    private ScriptEditor targetEditor = null;
    private IStructuredSelection selection = null;
    
    public AddBundle()
    {
    }
    public AddBundle( final ScriptEditor targetEditor )
    {
        this.targetEditor = targetEditor;
        setText( "Add Bundle to Script" );
        setToolTipText( "Add existing Bundle to Monkey Script" );
    }
    public void run( final IAction action )
    {
        run();
    }
    @Override
    public void run()
    {
        final IFile script = getTargetScript();
        if( script == null )
            return;
        try
        {
            final IEditorPart editor = getEditor( script );
            if( editor.isDirty() )
            {
                final SaveEditorDialog dialog = new SaveEditorDialog( shell(), script );
                final int returnCode = dialog.open();
                if( returnCode != Window.OK )
                    return;
                saveChangesInEditor( editor );
            }
            final Set< String > selectedBundles = openSelectBundlesDialog( getUnusedBundles( script ) );
            if( selectedBundles.size() == 0 )
                return;
            addBundlesToScript( script, selectedBundles );
        }
        catch( final CoreException e )
        {
            error( "IO Error", "Error getting the contents of: " + script.getName() + ". " + e, e );
        }
        catch( final IOException e )
        {
            error( "IO Error", "Error getting the contents of: " + script.getName() + ". " + e, e );
        }
    }
    private void addBundlesToScript( final IFile script, 
                                     final Set< String > selectedBundles ) 
    throws CoreException, IOException
    {
        final List< String > metadata = getMetadataLines( getContents( script ) );
        for( final String selectedDOMPlugin : selectedBundles )
            metadata.add( metadata.size() - 1, " * Include-Bundle: " + getUpdateSiteForDOMPlugin( selectedDOMPlugin ) );
        final String contents = join( metadata.toArray( new String[ 0 ] ), "\n" ) + "\n" + stripMetadata( getContents( script ) );
        script.setContents( new ByteArrayInputStream( contents.getBytes() ), true, false, null );
        script.refreshLocal( DEPTH_ONE, null );
    }
    private Set< String > openSelectBundlesDialog( final Set< String > availablePlugins )
    {
        if( availablePlugins == null || availablePlugins.size() == 0 )
            return new TreeSet< String >();
        final AddDialog dialog = createAddBundleDialog( shell(), availablePlugins );
        final int returnCode = dialog.open();
        if( returnCode != Window.OK )
            return new TreeSet< String >();
        return dialog.selected();
    }
    private Set< String > getUnusedBundles( final IFile script ) 
    throws CoreException, IOException
    {
        final ScriptMetadata data = getScriptMetadata( getContents( script ) );
        final Set< String > installedBundles = getAllAvailableBundles();
        final Set< String > alreadyIncludedBundles = getBundles( data );
        for( final Iterator< String > iterator = installedBundles.iterator(); iterator.hasNext(); )
        {
            final String pluginID = iterator.next();
            if( alreadyIncludedBundles.contains( pluginID ) )
                iterator.remove();
        }
        return installedBundles;
    }
    private void saveChangesInEditor( final IEditorPart editor )
    {
        final boolean[] done = new boolean[ 1 ];
        final boolean[] cancelled = new boolean[ 1 ];
        final IProgressMonitor monitor = new NullProgressMonitor()
        {
            @Override
            public void done()
            {
                done[ 0 ] = true;
            }
            @Override
            public void setCanceled( final boolean cancel )
            {
                cancelled[ 0 ] = cancel;
            }
        };
        editor.doSave( monitor );
        while( !done[ 0 ] && !cancelled[ 0 ] )
        {
            try
            {
                Thread.sleep( 500 );
            }
            catch( final InterruptedException e ) {}
        }
    }
    private IEditorPart getEditor( final IFile script )
    throws PartInitException
    {
        if( targetEditor != null )
            return targetEditor;
        return activePage().openEditor( new FileEditorInput( script ), 
                                        ScriptEditor.class.getName() );
    }
    private IFile getTargetScript()
    {
        if( targetEditor != null )
            return ( IFile )targetEditor.getAdapter( IFile.class );
        if( selection != null )
        {
            final Object selected = selection.getFirstElement();
            if( !( selected instanceof IFile ) )
                return null;
            return ( IFile )selected;
        }
        return null;
    }
    public void selectionChanged( final IAction action, 
                                  final ISelection selection )
    {
        if( !( selection instanceof IStructuredSelection ) )
            return;
        this.selection = ( IStructuredSelection )selection;
    }
    public void setActivePart( final IAction action, 
                               final IWorkbenchPart targetPart )
    {
    }
}
