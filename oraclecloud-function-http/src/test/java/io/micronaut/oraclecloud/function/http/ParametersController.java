package io.micronaut.oraclecloud.function.http;

import com.fnproject.fn.api.OutputEvent;
import com.fnproject.fn.api.RuntimeContext;
import edu.umd.cs.findbugs.annotations.Nullable;
import io.micronaut.context.annotation.Property;
import io.micronaut.core.io.Writable;
import io.micronaut.http.*;
import io.micronaut.http.annotation.*;
import io.micronaut.http.cookie.Cookie;
import org.junit.jupiter.api.Assertions;

import java.awt.event.InputEvent;
import java.io.IOException;


@Controller("/parameters")
public class ParametersController {
    private final RuntimeContext runtimeContext;
    private final String fnAppId;
    private final String another;

    public ParametersController(
            RuntimeContext runtimeContext,
            @Nullable @Property(name="fn.app.id") String fnAppId,
            @Nullable @Property(name="foo.bar") String another) {
        this.runtimeContext = runtimeContext;
        this.fnAppId = fnAppId;
        this.another = another;
    }

    @Get("/uri/{name}")
    String uriParam(String name) {
        return "Hello " + name;
    }

    @Get("/context")
    String context(RuntimeContext runtimeContext) {
        Assertions.assertEquals(this.runtimeContext, runtimeContext);
        Assertions.assertNotNull(this.runtimeContext.getMethod());
        return "Got " + another + " context: " + fnAppId;
    }

    @Get("/query")
    String queryValue(@QueryValue("q") String name) {
        return "Hello " + name;
    }

    @Get("/allParams")
    String allParams(HttpParameters parameters) {
        return "Hello " + parameters.get("name") + " " + parameters.get("age", int.class).orElse(null);
    }

    @Get("/header")
    String headerValue(@Header(HttpHeaders.CONTENT_TYPE) String contentType) {
        return "Hello " + contentType;
    }

    @Get("/cookies")
    io.micronaut.http.HttpResponse<String> cookies(@CookieValue String myCookie) {
        return io.micronaut.http.HttpResponse.ok(myCookie)
                .cookie(Cookie.of("foo", "bar").httpOnly(true).domain("https://foo.com"));
    }

    @Get("/reqAndRes")
    OutputEvent requestAndResponse(InputEvent request) throws IOException {
        return OutputEvent.fromBytes(
                "Good".getBytes(),
                OutputEvent.Status.Success,
                MediaType.TEXT_PLAIN
        );
    }

    @Post("/stringBody")
    @Consumes("text/plain")
    String stringBody(@Body String body) {
        return "Hello " + body;
    }

    @Post("/bytesBody")
    @Consumes("text/plain")
    String bytesBody(@Body byte[] body) {
        return "Hello " + new String(body);
    }

    @Post(value = "/jsonBody", processes = "application/json")
    Person jsonBody(@Body Person body) {
        return body;
    }

    @Post(value = "/jsonBodySpread", processes = "application/json")
    Person jsonBody(String name, int age) {
        return new Person(name, age);
    }

    @Post(value = "/fullRequest", processes = "application/json")
    io.micronaut.http.HttpResponse<Person> fullReq(io.micronaut.http.HttpRequest<Person> request) {
        final Person person = request.getBody().orElseThrow(() -> new RuntimeException("No body"));
        final MutableHttpResponse<Person> response = io.micronaut.http.HttpResponse.ok(person);
        response.header("Foo", "Bar");
        return response;
    }

    @Post(value = "/writable", processes = "text/plain")
    @Header(name = "Foo", value = "Bar")
    @Status(HttpStatus.CREATED)
    Writable fullReq(@Body String text) {
        return out -> out.append("Hello ").append(text);
    }


//    @Post(value = "/multipart", consumes = MediaType.MULTIPART_FORM_DATA, produces = "text/plain")
//    String multipart(
//            String foo,
//            @Part("one") Person person,
//            @Part("two") String text,
//            @Part("three") byte[] bytes,
//            @Part("four") HttpRequest.HttpPart raw) throws IOException {
//        return "Good: " + (foo.equals("bar") &&
//                person.getName().equals("bar") &&
//                text.equals("Whatever") &&
//                new String(bytes).equals("My Doc") &&
//                IOUtils.readText(raw.getReader()).equals("Another Doc"));
//    }

}

