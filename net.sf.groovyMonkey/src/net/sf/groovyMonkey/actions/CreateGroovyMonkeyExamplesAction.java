package net.sf.groovyMonkey.actions;
import static net.sf.groovyMonkey.GroovyMonkeyPlugin.FILE_EXTENSION_WILDCARD;
import static net.sf.groovyMonkey.GroovyMonkeyPlugin.getDefault;
import static org.apache.commons.io.IOUtils.closeQuietly;
import static org.eclipse.core.resources.ResourcesPlugin.getWorkspace;
import static org.eclipse.jface.dialogs.MessageDialog.openInformation;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.osgi.framework.Bundle;

public class CreateGroovyMonkeyExamplesAction 
implements IWorkbenchWindowActionDelegate 
{
	private IWorkbenchWindow window;

	public CreateGroovyMonkeyExamplesAction() {}

	public void run( final IAction action ) 
    {
		final IWorkspace workspace = getWorkspace();
		final IProject project = workspace.getRoot().getProject( "GroovyMonkeyExamples" );
		try 
        {
            final List< URL > examples = getExampleScripts( getDefault().getBundle() );
			if( !project.exists() )
				project.create( null );
			project.open( null );

			String errors = "";
			for( final URL example : examples )
            {
                try 
                {
					final String filePath = example.getFile();
					final String[] words = filePath.split( "/" );
                    final String fileName = words[ words.length - 1 ];
					final IFolder folder = project.getFolder( "/monkey" );
					if( !folder.exists() )
						folder.create( IResource.NONE, true, null );
                    final InputStream input = example.openStream();
                    try
                    {
                        final IFile file = folder.getFile( fileName );
                        file.create( input, false, null );
                    }
                    finally
                    {
                        closeQuietly( input );
                    }
				} 
                catch( final CoreException x ) 
                {
					errors += x.toString() + "\n";
				} 
                catch( final IOException x ) 
                {
					errors += x.toString() + "\n";
				}
            }
			if( errors.length() > 0 )
                openInformation( window.getShell(), "Eclipse Monkey", "Errors creating the Examples project: " + errors );
		} 
        catch( final CoreException x ) 
        {
			openInformation( window.getShell(), "Eclipse Monkey", "Unable to create the Examples project due to " + x );
		}
	}
    public List< URL > getExampleScripts( final Bundle bundle )
    {
        final List< URL > list = new ArrayList< URL >();
        findEntries( bundle, list, "/samples", FILE_EXTENSION_WILDCARD );
        findEntries( bundle, list, "/monkey", FILE_EXTENSION_WILDCARD );
        return list;
    }
    private void findEntries( final Bundle bundle, 
                              final List< URL > list,
                              final String directory,
                              final String namePattern )
    {
        final Enumeration enumeration = bundle.findEntries( directory, namePattern, true );
        if( enumeration == null )
            return;
        while( enumeration.hasMoreElements() )
            list.add( ( URL )enumeration.nextElement() );
    }
	public void selectionChanged( final IAction action, 
                                  final ISelection selection )
    {
    }
    public void dispose()
    {
    }
    public void init( final IWorkbenchWindow window )
    {
        this.window = window;
    }
}