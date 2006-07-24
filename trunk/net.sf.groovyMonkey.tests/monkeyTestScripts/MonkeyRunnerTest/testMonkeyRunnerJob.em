/*
 * Menu: testMonkeyRunnerJob
 * Kudos: James E. Ervin
 * License: EPL 1.0
 * LANG: Groovy
 */
map.message = 'testMonkeyRunnerJob'
map.returnedValue = []
runnerDOM.runScript( window, '/TestMonkeyProject/includedScripts/monkeyRunner.em', map )
if( map.returnedValue[0] != true )
	throw new RuntimeException( "Error no returned value: " + map.returnedValue )
