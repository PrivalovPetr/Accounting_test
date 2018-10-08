# Solution for create accounting system

## Requirements:
 oracle java 1.8

## Launching:
 when project is cloned it can be launched by
''' 
	./startServer.sh
'''
 or, direcly from command line from project root
'''
	java -Dfile.encoding=UTF-8 -classpath bin:lib/stubbornjava-all.jar test.privalov.RestServer		
'''
 
### Test Api
 to test that api is working, from another terminal launch Client sample
'''
	./startClient.sh
'''
 or directly from command line from project root
'''
	java -Dfile.encoding=UTF-8 -classpath bin:lib/stubbornjava-all.jar test.privalov.TestApiClient
'''

Project is based on Undertow library:
 https://www.stubbornjava.com/java-libraries/Undertow 
