/* Generated by AN DISI Unibo */ 
package it.unibo.transporttrolley

import it.unibo.kactor.*
import alice.tuprolog.*
import unibo.basicomm23.*
import unibo.basicomm23.interfaces.*
import unibo.basicomm23.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Transporttrolley ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
		
				var COLDROOMX=4;
				var COLDROOMY=3;
				var INDOORX=0;
				var INDOORY=4;
				var HOMEX=0;
				var HOMEY=0;
				var TICKETCODE=-1;
				
				var lastmove=""; //var per gestire la alarm/endalarm
		
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						discardMessages = false
						CommUtils.outmagenta("${name} |  request engage")
						request("engage", "engage(transporttrolley,300)" ,"basicrobot" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t07",targetState="waitforcommands",cond=whenReply("engagedone"))
					transition(edgeName="t08",targetState="waitrobotfree",cond=whenReply("engagerefused"))
				}	 
				state("waitrobotfree") { //this:State
					action { //it:State
						CommUtils.outmagenta("${name} | Sorry, the robot is already engaged.")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
				}	 
				state("waitforcommands") { //this:State
					action { //it:State
						CommUtils.outmagenta("${name} | waiting for commands.")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t19",targetState="moverobottoindoor",cond=whenRequest("dodepositaction"))
				}	 
				state("moverobottoindoor") { //this:State
					action { //it:State
						emit("robotstate", "robotstate(false,true,false)" ) 
						 lastmove = "moverobottoindoor"  
						if( checkMsgContent( Term.createTerm("dodepositaction(TICKETCODE)"), Term.createTerm("dodepositaction(TICKETCODE)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								
								 				TICKETCODE=payloadArg(0).toInt();
						}
						CommUtils.outmagenta("${name} | moving robot to indoor.")
						request("moverobot", "moverobot($INDOORX,$INDOORY)" ,"basicrobot" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t210",targetState="handleobstacle",cond=whenDispatch("stopobstacle"))
					transition(edgeName="t211",targetState="moverobottocoldroom",cond=whenReply("moverobotdone"))
					transition(edgeName="t212",targetState="robotmovefailed",cond=whenReply("moverobotfailed"))
				}	 
				state("moverobottocoldroom") { //this:State
					action { //it:State
						emit("robotstate", "robotstate(false,true,false)" ) 
						 lastmove = "moverobottocoldroom"  
						CommUtils.outmagenta("${name} | robot is in indoor")
						CommUtils.outmagenta("${name} | moving robot to coldroom")
						
									var event = MsgUtil.buildEvent( "transporttrolley","pickupindoordone","ok");	
									emitLocalEvent(event); //not propagated to remote actors
						request("moverobot", "moverobot($COLDROOMX,$COLDROOMY)" ,"basicrobot" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t313",targetState="handleobstacle",cond=whenDispatch("stopobstacle"))
					transition(edgeName="t314",targetState="depositactionended",cond=whenReply("moverobotdone"))
					transition(edgeName="t315",targetState="robotmovefailed",cond=whenReply("moverobotfailed"))
				}	 
				state("depositactionended") { //this:State
					action { //it:State
						answer("dodepositaction", "depositactionended", "depositactionended($TICKETCODE)"   )  
						CommUtils.outmagenta("${name} | robot is in coldroom")
						CommUtils.outmagenta("${name} | depositaction ended")
						CommUtils.outmagenta("${name} | waiting for next move")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
				 	 		stateTimer = TimerActor("timer_depositactionended", 
				 	 					  scope, context!!, "local_tout_transporttrolley_depositactionended", 100.toLong() )
					}	 	 
					 transition(edgeName="t416",targetState="moverobottohome",cond=whenTimeout("local_tout_transporttrolley_depositactionended"))   
					transition(edgeName="t417",targetState="moverobottoindoor",cond=whenRequest("dodepositaction"))
				}	 
				state("moverobottohome") { //this:State
					action { //it:State
						emit("robotstate", "robotstate(false,true,false)" ) 
						 lastmove = "moverobottohome"  
						request("moverobot", "moverobot($HOMEX,$HOMEY)" ,"basicrobot" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t518",targetState="handleobstacle",cond=whenDispatch("stopobstacle"))
					transition(edgeName="t519",targetState="emitrobotisinhome",cond=whenReply("moverobotdone"))
					transition(edgeName="t520",targetState="robotmovefailed",cond=whenReply("moverobotfailed"))
				}	 
				state("emitrobotisinhome") { //this:State
					action { //it:State
						emit("robotstate", "robotstate(true,false,false)" ) 
						CommUtils.outmagenta("${name} | robot is in home")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="waitforcommands", cond=doswitch() )
				}	 
				state("robotmovefailed") { //this:State
					action { //it:State
						CommUtils.outmagenta("${name} | robot failed to move $lastmove")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="restorelastmove", cond=doswitch() )
				}	 
				state("handleobstacle") { //this:State
					action { //it:State
						CommUtils.outmagenta("${name} handleobstacle ALARM")
						emit("alarm", "alarm(obstacle)" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t621",targetState="alarmconsidered",cond=whenReply("moverobotfailed"))
					transition(edgeName="t622",targetState="restorelastmove",cond=whenReply("moverobotdone"))
				}	 
				state("alarmconsidered") { //this:State
					action { //it:State
						emit("robotstate", "robotstate(false,false,true)" ) 
						CommUtils.outmagenta("${name} waiting for endalarm...")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t723",targetState="restorelastmove",cond=whenDispatch("endalarm"))
				}	 
				state("restorelastmove") { //this:State
					action { //it:State
						CommUtils.outmagenta("${name} | restore to $lastmove")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="moverobottoindoor", cond=doswitchGuarded({ lastmove == "moverobottoindoor"  
					}) )
					transition( edgeName="goto",targetState="option2", cond=doswitchGuarded({! ( lastmove == "moverobottoindoor"  
					) }) )
				}	 
				state("option2") { //this:State
					action { //it:State
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="moverobottohome", cond=doswitchGuarded({ lastmove == "moverobottohome"  
					}) )
					transition( edgeName="goto",targetState="option3", cond=doswitchGuarded({! ( lastmove == "moverobottohome"  
					) }) )
				}	 
				state("option3") { //this:State
					action { //it:State
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="moverobottocoldroom", cond=doswitch() )
				}	 
			}
		}
}
