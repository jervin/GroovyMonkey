package net.sf.groovyMonkey.editor.actions;
import static net.sf.groovyMonkey.util.ListUtil.array;
import java.util.List;
import net.sf.groovyMonkey.editor.ScriptEditor;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.Action;

/**
 * This class is meant as a delegate that invokes the PublishScript action from within the Editor.
 */
public class PublishScriptForText
extends Action
{
	private final ScriptEditor editor;

	public PublishScriptForText( final ScriptEditor editor )
	{
		this.editor = editor;
		setText( "as Bugzilla (text)" );
		setToolTipText( "Publish script straight text format (Bugzilla)" );
	}
	@Override
	public void run()
	{
		final net.sf.groovyMonkey.actions.PublishScript publishAction = new net.sf.groovyMonkey.actions.PublishScript()
		{
			@Override
			protected List< IFile > getScripts()
			{
				return array( ( IFile )editor.getAdapter( IFile.class ) );
			}

		};
		publishAction.run( this );
	}
}
