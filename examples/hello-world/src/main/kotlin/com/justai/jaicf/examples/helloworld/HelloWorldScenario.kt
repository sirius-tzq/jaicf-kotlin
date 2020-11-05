package com.justai.jaicf.examples.helloworld

import com.justai.jaicf.activator.caila.caila
import com.justai.jaicf.activator.dialogflow.dialogflow
import com.justai.jaicf.activator.event.event
import com.justai.jaicf.builder.generic
import com.justai.jaicf.channel.aimybox.AimyboxEvent
import com.justai.jaicf.channel.aimybox.aimybox
import com.justai.jaicf.channel.alexa.*
import com.justai.jaicf.channel.alexa.model.AlexaEvent
import com.justai.jaicf.channel.alexa.model.AlexaIntent
import com.justai.jaicf.channel.facebook.api.facebook
import com.justai.jaicf.channel.facebook.facebook
import com.justai.jaicf.builder.genericAction
import com.justai.jaicf.channel.googleactions.dialogflow.DialogflowIntent
import com.justai.jaicf.channel.telegram.intent
import com.justai.jaicf.channel.telegram.telegram
import com.justai.jaicf.model.scenario.Scenario

object HelloWorldScenario : Scenario(
    dependencies = listOf(HelperScenario)
) {

    init {
        state("main") {

            activators {
                catchAll()
                event(AlexaEvent.LAUNCH)
                event(DialogflowIntent.WELCOME)
                event(AimyboxEvent.START)
            }

            action {
                var name = context.client["name"]

                if (name == null) {
                    request.telegram?.run {
                        name = message.chat.firstName ?: message.chat.username
                    }
                    request.facebook?.run {
                        name = reactions.facebook?.queryUserProfile()?.firstName()
                    }
                }

                if (name == null) {
                    reactions.askForName("Hello! What is your name?", "name")
                } else {
                    reactions.run {
                        image("https://www.bluecross.org.uk/sites/default/files/d8/assets/images/118809lprLR.jpg")
                        sayRandom("Hello $name!", "Hi $name!", "Glad to hear you $name!")
                        aimybox?.endConversation()
                    }
                }
            }

            state("inner", modal = true) {
                activators {
                    catchAll()
                }

                action {
                    reactions.apply {
                        sayRandom("What?", "Sorry, I didn't get that.")
                        say("Could you repeat please?")
                        changeState("/")
                    }
                }
            }

            state("name") {
                action {
                    context.client["name"] = context.result
                    reactions.say("Nice to meet you ${context.result}")
                }
            }
        }

        state("x") {
//            activators {
//                intent("Hello")
//            }
//
//            genericAction {
//
//                (event to telegram) {
//
//                }
//                telegram {
//                    intent {
//                        reactions.api
//                        request.chatId
//                        activator
//                    }
//
//                    event {
//                        activator.event
//                    }
//
//                    caila {
//
//                    }
//
//                    alexa {
//
//                    }
//                }
//
//                alexa {
//
//                }
//            }
//
//            genericAction(caila) {
//                activator.entities
//            }
//
//            genericAction(telegram) {
//                request.chatId
//            }
//
//            genericAction(alexa) {
//                reactions.endSession()
//            }
//
//            genericAction(caila, telegram) {
//                (caila to telegram) {
//                    reactions.sendMessage("")
//                    activator.entities
//                }
//
//                val b: Int? = caila {
//                    val a: String? = telegram {
//                        reactions.sendMessage("")
//                        activator.entities
//                        ""
//                    }
//                    42
//                }
//
//                alexa {
//                    intent {
//                        request.intent?.intentRequest?.dialogState
//                        activator.intent
//                    }
//
//                    event {
//                        reactions.endSession()
//                        activator.event
//                    }
//                }
//            }
        }

        state("stop") {
            activators {
                intent(AlexaIntent.STOP)
            }

            action {
                reactions.alexa?.endSession("See you latter! Bye bye!")
            }
        }

        state("mew") {
            activators {
                regex("/mew")
            }

            generic {
                val x = telegram.intent.caila.caila.caila.telegram
                intent.caila.telegram
            }

            generic({ telegram.intent.caila }) {

            }

            action {
                reactions.image("https://www.bluecross.org.uk/sites/default/files/d8/assets/images/118809lprLR.jpg")
            }
        }

        state("wakeup") {
            activators {
                intent("wake_up")
            }
            action {
                activator.dialogflow?.run {
                    val dt = slots["date-time"]
                    reactions.say("Okay! I'll wake you up ${dt?.stringValue}")
                }
            }
        }
    }
}