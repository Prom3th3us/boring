package arch.infra.monitoring

import arch.common.Program.MError
import cats.implicits._

trait MonitoringLibrary[F[_]] {
  import MonitoringLibrary._
  def counter(
      name: String,
      context: Map[String, String] = Map.empty
  ): CounterF[F]
  def gauge(name: String): GaugeF[F]
  def histogram(name: String): HistogramF[F]
}

object MonitoringLibrary {

  trait CounterF[F[_]] {
    def increment(): F[Unit]
    def add(num: Int): F[Unit]
  }

  trait GaugeF[F[_]] {
    def increment(): F[Unit]
    def decrement(): F[Unit]
    def add(num: Int): F[Unit]
    def subtract(num: Int): F[Unit]
    def set(num: Int): F[Unit]
  }

  abstract class HistogramF[F[_]: MError] {
    def record(value: Long): F[Unit]

    def record[T](codeToBenchmark: => T): F[T] = {
      val before = System.nanoTime()
      val result = codeToBenchmark
      val after = System.nanoTime()
      for {
        _ <- record(after - before)
      } yield result
    }

    def recordF[T](codeToBenchmark: => F[T]): F[T] = {
      val before = System.currentTimeMillis()
      for {
        result <- codeToBenchmark
        _ = record(System.currentTimeMillis() - before)
      } yield result
    }
  }
}
