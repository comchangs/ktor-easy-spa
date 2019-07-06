package work.jeong.murry.ktor.features

import io.ktor.application.Application
import io.ktor.application.ApplicationCallPipeline
import io.ktor.application.ApplicationFeature
import io.ktor.application.call
import io.ktor.http.content.default
import io.ktor.http.content.files
import io.ktor.http.content.static
import io.ktor.http.content.staticRootFolder
import io.ktor.request.uri
import io.ktor.response.ApplicationSendPipeline
import io.ktor.response.respondFile
import io.ktor.routing.routing
import io.ktor.util.AttributeKey
import java.io.File

class EasySpaFeature(configuration: Configuration) {
    private val staticRootDocs = configuration.staticRootDocs
    private val defaultFile = configuration.defaultFile
    private val apiUrl = configuration.apiUrl

    class Configuration {
        var staticRootDocs = "./htdocs"
        var defaultFile = "index.html"
        var apiUrl = "/api"
    }

    companion object Feature : ApplicationFeature<Application, Configuration, EasySpaFeature> {
        override val key = AttributeKey<EasySpaFeature>("EasySpaFeature")

        override fun install(pipeline: Application, configure: Configuration.() -> Unit): EasySpaFeature {

            val configuration = Configuration().apply(configure)

            val feature = EasySpaFeature(configuration)

            pipeline.routing {
                static("/") {
                    staticRootFolder = File(configuration.staticRootDocs)
                    files("./")
                    default(configuration.defaultFile)
                }
            }

            pipeline.sendPipeline.intercept(ApplicationSendPipeline.After) {
                if (!call.request.uri.startsWith(configuration.apiUrl)) {
                    val path = call.request.uri.split("/")
                    if(path.last().matches(Regex("\\w+\\.\\w+"))) {
                        // NOTE: *.css, *.js 등 리소스 파일인 경우
                        call.respondFile(File(configuration.staticRootDocs, path.last()))
                    } else {
                        // NOTE: 일반적인 경우
                        call.respondFile(File(configuration.staticRootDocs, configuration.defaultFile))
                    }
                }
                return@intercept finish()
            }

            return feature
        }
    }
}
