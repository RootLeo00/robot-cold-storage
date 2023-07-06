/* Generated by AN DISI Unibo */ 
package it.unibo.coldroom

import it.unibo.kactor.*
import alice.tuprolog.*
import unibo.basicomm23.*
import unibo.basicomm23.interfaces.*
import unibo.basicomm23.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Coldroom ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		 var SpaceKg = 100  
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						CommUtils.outblack("&&&  appl coldroom is now ACTIVE ...")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t04",targetState="getcoldroomspacestate",cond=whenRequest("getcoldroomspace"))
				}	 
				state("getcoldroomspacestate") { //this:State
					action { //it:State
						answer("getcoldroomspace", "coldroomspace", "coldroomspace($SpaceKg)"   )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t05",targetState="updateKg",cond=whenDispatch("updatestorage"))
				}	 
				state("updateKg") { //this:State
					action { //it:State
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t06",targetState="getcoldroomspacestate",cond=whenRequest("getcoldroomspace"))
				}	 
			}
		}
}
