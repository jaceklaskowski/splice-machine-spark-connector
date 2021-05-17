import sbt._

/**
 * https://github.com/sbt/sbt/issues/3618#issuecomment-424924293
 */
object PackagingTypePlugin extends AutoPlugin {
  override val buildSettings = {
    sys.props += "envClassifier" -> "cdh6.3.0"
    Nil
  }
}
