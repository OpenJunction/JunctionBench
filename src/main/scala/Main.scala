import scala.actors.Actor
import scala.collection.mutable.ListBuffer
import scala.collection.JavaConversions._
import scala.util.control.Breaks._
import scala.util.Random._

import edu.stanford.junction.JunctionException
import edu.stanford.junction.JunctionMaker
import edu.stanford.junction.Junction
import edu.stanford.junction.api.activity.JunctionActor
import edu.stanford.junction.api.activity.JunctionExtra
import edu.stanford.junction.api.activity.ActivityScript
import edu.stanford.junction.api.messaging.MessageHeader
import edu.stanford.junction.provider.xmpp.XMPPSwitchboardConfig
import org.json.JSONObject
import java.util.ArrayList
import java.util.Random
import java.util.Date
import java.util.UUID
import java.net._
import java.io._
import java.util.concurrent.CountDownLatch

case class Wakeup()

import Helpers._

object Main {
  def main(args: Array[String]) {
    if (args.length == 2) runMob(args)
    if (args.length == 4) runTest(args)
  }

  def runTest(args: Array[String]) {
    val url = new URI(args(0));
    val numMsgs = args(1).toInt
    val msgSize = args(2).toInt
    val output = args(3)
    val writer = new BufferedWriter(new FileWriter(output));
    var timer = 0L
    val testMsg = new JSONObject()
    testMsg.put("data", List.fill(msgSize)(".").mkString)

    println(List(
      "Running with url=", url,
      " numMsgs=", numMsgs,
      " msgSize=", msgSize,
      " output=", output).mkString(""))

    val counter = new CountDownLatch(numMsgs)

    val jxActor = new JunctionActor("participant") {
      override def onActivityJoin() {
        println("Joined.")
        if (counter.getCount() > 0) {
          timer = System.nanoTime()
          sendMessage(this, testMsg)
        }
      }
      override def onActivityCreate() {
        println("Created.")
      }
      override def onMessageReceived(header: MessageHeader, msg: JSONObject) {
        try {
          val newTimer = System.nanoTime()
          val elapsed = newTimer - timer
          writer.write(elapsed.toString)
          writer.newLine()
	  writer.flush()
          println("Message Received - " + counter.getCount() + " more to go!")
          counter.countDown()
          if (counter.getCount() > 0) {
            timer = System.nanoTime()
            sendMessage(this, testMsg)
          }
        } catch {
          case e: Exception => e.printStackTrace(System.err)
        }
      }
      override def getInitialExtras(): java.util.List[JunctionExtra] = {
        new ArrayList[JunctionExtra]()
      }
    }
    initJunction(jxActor, url)
    counter.await()
    println("Shutting down...")
    jxActor.leave()
    writer.flush()
    writer.close()
  }

  def runMob(args: Array[String]) {
    val url = new URI(args(0));
    val numUsers = args(1).toInt

    println(List("Running with url=", url,
      " numUsers=", numUsers).mkString(""))

    def newActor(i: Int) = {
      new JunctionActor("participant") {
        override def onActivityJoin() {
          println(i + " joined.")
        }
        override def onActivityCreate() {
          println(i + " created.")
        }
        override def onMessageReceived(header: MessageHeader, msg: JSONObject) {
          println(i + " received message")
        }
        override def getInitialExtras(): java.util.List[JunctionExtra] = {
          new ArrayList[JunctionExtra]()
        }
      }
    }

    val initialCreator = newActor(-1)
    initJunction(initialCreator, url)

    val threads = new ListBuffer[Thread]
    for (i <- 1 to numUsers - 1) {
      val jxActor = newActor(i)
      val t = new Thread() {
        override def run() {
          initJunction(jxActor, url)
        }
      }
      t.start()
      threads += t
      Thread.sleep(200)
    }
    println("Waiting...")
    val counter = new CountDownLatch(1)
    counter.await()
  }

  def sendMessage(actor: JunctionActor, msg: JSONObject) {
    actor.sendMessageToSession(msg)
  }

  def initJunction(actor: JunctionActor, url: URI) {
    try {
      val sb = JunctionMaker.getDefaultSwitchboardConfig(url)
      val jxMaker = JunctionMaker.getInstance(sb)
      val jx = jxMaker.newJunction(url, actor)
      jx
    } catch {
      case e: JunctionException => {
        println("Failed to connect to junction activity!")
        e.printStackTrace(System.err)
        null
      }
      case e: Exception => {
        println("Failed to connect to junction activity!")
        e.printStackTrace(System.err)
        null
      }
    }
  }

}
