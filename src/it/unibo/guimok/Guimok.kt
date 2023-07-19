/* Generated by AN DISI Unibo */ 
package it.unibo.guimok

import it.unibo.kactor.*
import alice.tuprolog.*
import unibo.basicomm23.*
import unibo.basicomm23.interfaces.*
import unibo.basicomm23.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Guimok ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						discardMessages = false
						CommUtils.outmagenta("$name | start request")
						request("storefood", "storefood(25)" ,"coldstorageservice" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t125",targetState="sendticket",cond=whenReply("ticketaccepted"))
				}	 
				state("sendticket") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("ticketaccepted(TICKETCODE,TICKETSECRET,TIMESTAMP)"), Term.createTerm("ticketaccepted(TICKETCODE,TICKETSECRET,TIMESTAMP)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 
												var Ticketcode= payloadArg(0);
												var Ticketsecret= payloadArg(1);
								CommUtils.outmagenta("$name | sending ticket to coldstorageservice")
								request("sendticket", "sendticket($Ticketcode,$Ticketsecret)" ,"coldstorageservice" )  
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t226",targetState="end",cond=whenReply("chargetaken"))
				}	 
				state("end") { //this:State
					action { //it:State
						CommUtils.outmagenta("$name | robot has taken the load")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="s0", cond=doswitch() )
				}	 
			}
		}
}
