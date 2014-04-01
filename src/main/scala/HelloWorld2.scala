import akka.actor.{ActorSystem, Actor, Props}
import akka.util.Timeout
import akka.pattern.ask
import concurrent.duration._
import concurrent.ExecutionContext.Implicits.global

/**
 * User: joe
 * Date: 1/21/14
 */

object Counter {
  case object Greet
  case object Done
  case object GetCount
  case class Count(count: Int)
}

class Counter extends Actor {

  var helloCount = 0

  def receive = {
    case Counter.Greet => {
      println("Hello World!")
      helloCount +=1
      sender ! Greeter.Done
    }
    case Counter.GetCount => sender ! Counter.Count(helloCount)
  }
}

object HelloWorld2 extends App {
  val system = ActorSystem("HelloSystem")
  // default Actor constructor
  val helloActor = system.actorOf(Props[Counter], "helloactor")

  helloActor ! Counter.Greet
  helloActor ! Counter.Greet
  helloActor ! Counter.Greet

  implicit val timeout = Timeout(5 seconds)
  (helloActor ? Counter.GetCount).map { response =>
    println(response)
  }

  system.shutdown()
}