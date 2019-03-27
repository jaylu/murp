package io.muserver.murp;

import io.muserver.MuRequest;

import java.net.URI;

/**
 * A function that maps an incoming request to a target URI.
 */
public interface UriMapper {

    /**
     * Gets a URI to proxy to based on the given request.
     * @param request The client request to potentially proxy.
     * @return A URI if this request should be proxied; otherwise null.
     * @throws Exception Unhandled exceptions will result in an HTTP 500 error being sent to the client
     */
    URI mapFrom(MuRequest request) throws Exception;

    /**
     * Creates a mapper that directs all requests to a new target domain.
     * @param uri The target URI to send proxied requests to. Any path or query strings will be ignored.
     * @return Returns a URI mapper that can be passed to {@link ReverseProxyBuilder#withUriMapper(UriMapper)}
     */
    static UriMapper toDomain(URI uri) {
        return request -> {
            String pathAndQuery = request.uri().getRawPath();
            String rawQuery = request.uri().getRawQuery();
            if (rawQuery != null) {
                pathAndQuery += "?" + rawQuery;
            }
            return uri.resolve(pathAndQuery);
        };
    }

}