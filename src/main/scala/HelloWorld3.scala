import akka.actor.{ActorSystem, Actor, Props}
import akka.util.Timeout
import akka.pattern.ask
import concurrent.duration._
import concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await

/**
 * User: joe
 * Date: 1/21/14
 */

object Shape{
  case object Describe
  case object ChangeShape
  case class Description(desc: String)
}

class Shape extends Actor {

  val receive = normal

  def normal: Receive = {
    case Shape.Describe => sender ! Shape.Description("I'm an abstract shape!")
    case Shape.ChangeShape => context.become(circle)
  }

  def circle: Receive = {
    case Shape.Describe => sender ! Shape.Description("I'm a circle!")
    case Shape.ChangeShape => context.become(triangle)
  }

  def triangle: Receive = {
    case Shape.Describe => sender ! Shape.Description("I'm a triangle!")
    case Shape.ChangeShape => context.become(square)
  }

  def square: Receive = {
    case Shape.Describe => sender ! Shape.Description("I'm a square!")
    case Shape.ChangeShape => context.become(normal)
  }
}

object HelloWorld3 extends App {
  val system = ActorSystem("HelloSystem")
  // default Actor constructor
  val helloActor = system.actorOf(Props[Shape], "helloactor")

  implicit val timeout = Timeout(5 seconds)

  println( Await.result(helloActor ? Shape.Describe, Duration.Inf))

  helloActor ! Shape.ChangeShape
  println( Await.result(helloActor ? Shape.Describe, Duration.Inf))

  helloActor ! Shape.ChangeShape
  println( Await.result(helloActor ? Shape.Describe, Duration.Inf))

  helloActor ! Shape.ChangeShape
  println( Await.result(helloActor ? Shape.Describe, Duration.Inf))

  system.shutdown()
}