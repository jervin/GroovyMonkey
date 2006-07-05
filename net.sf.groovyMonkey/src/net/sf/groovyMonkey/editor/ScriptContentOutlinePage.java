package net.sf.groovyMonkey.editor;
import static org.eclipse.core.resources.ResourcesPlugin.getWorkspace;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

public class ScriptContentOutlinePage 
extends ContentOutlinePage
{
    private IEditorInput editorInput = null;
    private ScriptResourceChangeListener listener;
    
    @Override
    public void createControl( final Composite parent )
    {
        super.createControl( parent );
        final TreeViewer viewer = getTreeViewer();
        final ScriptContentProvider contentProvider = new ScriptContentProvider();
        viewer.setContentProvider( contentProvider );
        final ILabelProvider labelProvider = new ScriptLabelProvider();
        viewer.setLabelProvider( labelProvider );
        if( editorInput != null )
            viewer.setInput( editorInput );
        listener = new ScriptResourceChangeListener( viewer, contentProvider );
        getWorkspace().addResourceChangeListener( listener, IResourceChangeEvent.POST_CHANGE );
        viewer.addDoubleClickListener( new ScriptOutlineDoubleClickAction() );
    }
    public void setInput( final IEditorInput editorInput )
    {
        this.editorInput = editorInput;
    }
    @Override
    public void dispose()
    {
        super.dispose();
        getWorkspace().removeResourceChangeListener( listener );        
    }
}