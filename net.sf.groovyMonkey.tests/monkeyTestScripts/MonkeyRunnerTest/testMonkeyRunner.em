/*
 * Menu: testMonkeyRunner
 * Kudos: James E. Ervin
 * License: EPL 1.0
 * LANG: Groovy
 */
map.message = 'testMonkeyRunner'
map.returnedValue = []
runnerDOM.runScript( window, '/TestMonkeyProject/includedScripts/monkeyRunner.em', map, monitor )
if( map.returnedValue[0] != true )
	throw new RuntimeException( "Error no returned value: " + map.returnedValue )
