/*
 * Menu: testIncludeInBundleTcl
 * Kudos: James E. Ervin
 * License: EPL 1.0
 * LANG: Tcl
 * Include-Bundle: net.sf.groovyMonkey.tests
 * Include-Bundle: org.apache.ant
 */
package require java
set antMain [java::new org.apache.tools.ant.Main]
set testDOMVar [java::new net.sf.groovymonkey.tests.fixtures.dom.TestDOM]
$testDOMVar callDOM "testIncludeInBundleTcl"
