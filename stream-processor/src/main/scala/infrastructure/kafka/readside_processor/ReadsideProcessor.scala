package infrastructure.kafka.readside_processor

trait ReadsideProcessor[Output] {
  def process: Output
}

object ReadsideProcessor {

  trait EndOfPipeline extends ReadsideProcessor[Unit]
  trait Composable[Output] extends ReadsideProcessor[Output]

}
