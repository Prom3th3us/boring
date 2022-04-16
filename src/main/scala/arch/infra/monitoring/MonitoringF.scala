package arch.infra.monitoring

import arch.common.Program.MError
import arch.infra.monitoring.MonitoringLibrary.{CounterF, GaugeF, HistogramF}

// @TODO implement on top of some dependency (ie: grafana, kamon)
class MonitoringF[F[_]: MError] extends MonitoringLibrary[F] {
  override def counter(
      name: String,
      context: Map[String, String]
  ): CounterF[F] = new CounterF[F] {
    override def increment(): F[Unit] =
      MError[F].pure(println(s"counter increment"))

    override def add(num: Int): F[Unit] =
      MError[F].pure(println(s"counter add $num"))
  }

  override def gauge(name: String): GaugeF[F] = new GaugeF[F] {
    override def increment(): F[Unit] =
      MError[F].pure(println(s"gauge increment"))

    override def decrement(): F[Unit] =
      MError[F].pure(println(s"gauge decrement"))

    override def add(num: Int): F[Unit] =
      MError[F].pure(println(s"gauge add $num"))

    override def subtract(num: Int): F[Unit] =
      MError[F].pure(println(s"gauge subtract $num"))

    override def set(num: Int): F[Unit] =
      MError[F].pure(println(s"gauge set $num"))
  }

  override def histogram(name: String): HistogramF[F] = new HistogramF[F] {
    override def record(value: Long): F[Unit] =
      MError[F].pure(println(s"histogram record $value"))
  }
}
