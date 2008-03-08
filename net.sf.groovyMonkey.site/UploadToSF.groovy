new File( '.' ).eachFile
{ file ->
	def path = file.getPath().tokenize( File.separator )
	path.remove( 0 )
	path = path.join( '/' )
	println 'path: ' + path
	def scp
	if( !file.isDirectory() )
		scp = 'scp -p'
	else
	    scp = 'scp -r -p'
	def execString = "${scp} ${path} jervin@shell.sourceforge.net:/home/groups/g/gr/groovy-monkey/htdocs/update/${path}"
	println "${execString}"
	def process = execString.execute()
    println '  exitCode: ' + process.waitFor()
	println '  stdout: ' + process.getText()
	println '  stderr: ' + process.getErr().getText()
}
