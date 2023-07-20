/* Generated by AN DISI Unibo */ 
package it.unibo.controllersonarled

import it.unibo.kactor.*
import alice.tuprolog.*
import unibo.basicomm23.*
import unibo.basicomm23.interfaces.*
import unibo.basicomm23.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Controllersonarled ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		 val DLIMIT = 70 ;
			   var stopped=false;
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						CommUtils.outyellow("${name} | START")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t034",targetState="doBusinessWork",cond=whenEvent("sonardata"))
					transition(edgeName="t035",targetState="ledoff",cond=whenEvent("robotisinhome"))
				}	 
				state("doBusinessWork") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("distance(D)"), Term.createTerm("distance(D)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 var D = payloadArg(0).toInt()  
								if(  D < DLIMIT  
								 ){ stopped = true  
								CommUtils.outyellow("${name} | Turn the Led on")
								forward("ledCmd", "ledCmd(on)" ,"led" ) 
								}
								else
								 {if(  stopped == true  
								  ){CommUtils.outyellow("$name | resume transport trolley")
								 emit("endalarm", "endalarm" ) 
								 CommUtils.outyellow("${name} - Blink Led")
								 forward("ledCmd", "ledCmd(blink)" ,"led" ) 
								 }
								 else
								  {CommUtils.outyellow("${name} - Blink Led")
								  forward("ledCmd", "ledCmd(blink)" ,"led" ) 
								  }
								 }
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t036",targetState="doBusinessWork",cond=whenEvent("sonardata"))
					transition(edgeName="t037",targetState="ledoff",cond=whenEvent("robotisinhome"))
				}	 
				state("ledoff") { //this:State
					action { //it:State
						CommUtils.outyellow("${name} - Turn the Led off")
						forward("ledCmd", "ledCmd(off)" ,"led" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t138",targetState="doBusinessWork",cond=whenEvent("robotismoving"))
				}	 
			}
		}
}
