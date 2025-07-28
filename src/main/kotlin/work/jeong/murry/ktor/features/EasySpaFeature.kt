/*
 * MIT License
 *
 * Copyright (c) 2019 Murry Jeong (comchangs@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package work.jeong.murry.ktor.features

import io.ktor.application.Application
import io.ktor.application.ApplicationCallPipeline
import io.ktor.application.ApplicationFeature
import io.ktor.application.call
import io.ktor.http.content.default
import io.ktor.http.content.files
import io.ktor.http.content.static
import io.ktor.http.content.staticRootFolder
import io.ktor.request.path
import io.ktor.request.uri
import io.ktor.response.respondFile
import io.ktor.response.respondRedirect
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.util.AttributeKey
import java.io.File

/**
 * EasySpaFeature
 * A feature of ktor for setting up single page application like Angular, React and so on
 *
 * @author Murry Jeong (comchangs@gmail.com)
 */
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

                get("/*") {
                    call.respondRedirect("/")
                }
            }

            pipeline.intercept(ApplicationCallPipeline.Features) {
                if (!call.request.uri.startsWith(configuration.apiUrl)) {
                    val path = call.request.path().split("/")
                    if (path.last().matches(Regex("[\\S]+\\.[\\S]+"))) {
                        // NOTE: resource files like *.css, *.js and so on
                        val urlPathString = String.joinUrlPath(configuration.staticRootDocs, path.subList(1, path.lastIndex))
                        call.respondFile(File(urlPathString, path.last()))
                    } else {
                        // NOTE: any paths
                        call.respondFile(File(configuration.staticRootDocs, configuration.defaultFile))
                    }
                    return@intercept finish()
                }
            }

            return feature
        }
    }
}

private fun String.Companion.joinUrlPath(start: String, list: List<String>): String {
    var url = start
    list.forEach { url = "$url/$it" }
    return url
}
