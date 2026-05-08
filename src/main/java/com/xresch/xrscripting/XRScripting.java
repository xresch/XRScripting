package com.xresch.xrscripting;

import java.util.List;

import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/***************************************************************************
 * 
 * Copyright Owner: Performetriks GmbH, Switzerland
 * License: Eclipse Public License v2.0
 * 
 * @author Reto Scheiwiller
 * 
 ***************************************************************************/
public class XRScripting {
	
	private static final Logger logger = LoggerFactory.getLogger(XRScripting.class.getName());
	
	private static ScriptEngineManager manager = new ScriptEngineManager();
	
	/******************************************************************************************************
	 * Print all available scripting engines. 
	 ******************************************************************************************************/
	public static void printAvailableEngines() {

	    List<ScriptEngineFactory> factories = manager.getEngineFactories();
	   
	    for (ScriptEngineFactory factory : factories){
			System.out.println("ScriptEngineFactory Info");
			String engName = factory.getEngineName();
			String engVersion = factory.getEngineVersion();
			String langName = factory.getLanguageName();
			String langVersion = factory.getLanguageVersion();
			System.out.printf("\tScript Engine: %s (%s, %s, %s)\n", engName, engVersion, langName, langVersion);
			List<String> engNames = factory.getNames();
			for (String name : engNames)
			{
			   System.out.printf("\tEngine Alias: %s\n", name);
			}
			System.out.printf("\tLanguage: %s (%s)\n", langName, langVersion);
	    }
	}

	/******************************************************************************************************
	 *  Create a new Javascript engine.
	 * 
	 * @param the name of the engine to use, e.g. Nashorn. Use printAvailableEngines() for a list of 
	 * available engines.
	 ******************************************************************************************************/
	public static XRScriptingContext createContext(String language) {
		
		//-----------------------------------
		// Create Engine

		Context context = Context.newBuilder(language)
		        .allowHostClassLookup(s -> true)
		        .allowHostAccess(HostAccess.ALL)
		        .build();

		return new XRScriptingContext(language, context);
		
	}
	/******************************************************************************************************
	 *  Create a new Javascript engine.
	 * 
	 * @param the name of the engine to use, e.g. Nashorn. Use printAvailableEngines() for a list of 
	 * available engines.
	 ******************************************************************************************************/
	public static XRScriptingContext createJavascriptContext() {
		
		return createContext("js");
		
	}
	
	/******************************************************************************************************
	 *  Create a new Javascript engine.
	 * 
	 * @param the name of the engine to use, e.g. Nashorn. Use printAvailableEngines() for a list of 
	 * available engines.
	 ******************************************************************************************************/
	public static XRScriptingContext createPolyglotJavascript(Object objectToBind) {
		
		return createPolyglotWithAdditionalBindings("js", objectToBind);
		
	}
	/******************************************************************************************************
	 * Execute a javascript call with defined parameters.
	 * 
	 * @param the name of the engine to use, e.g. Nashorn. Use printAvailableEngines() for a list of 
	 * available engines.
	 ******************************************************************************************************/
	private static XRScriptingContext createPolyglotWithAdditionalBindings(String language, Object objectToBind) {
		
		//-----------------------------------
		// Create Engine

		XRScriptingContext polyglot = createContext(language);

		polyglot.getBindings().putMember(objectToBind.getClass().getSimpleName(), objectToBind);

		return polyglot;
		
		
	}
	
}
