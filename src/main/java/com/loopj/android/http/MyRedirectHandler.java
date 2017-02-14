package com.loopj.android.http;

import com.boohee.one.tinker.reporter.SampleTinkerReport;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.client.CircularRedirectException;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.impl.client.DefaultRedirectHandler;
import org.apache.http.impl.client.RedirectLocations;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

class MyRedirectHandler extends DefaultRedirectHandler {
    private static final String REDIRECT_LOCATIONS = "http.protocol.redirect-locations";
    private final boolean enableRedirects;

    public MyRedirectHandler(boolean allowRedirects) {
        this.enableRedirects = allowRedirects;
    }

    public boolean isRedirectRequested(HttpResponse response, HttpContext context) {
        if (!this.enableRedirects) {
            return false;
        }
        if (response == null) {
            throw new IllegalArgumentException("HTTP response may not be null");
        }
        switch (response.getStatusLine().getStatusCode()) {
            case SampleTinkerReport.KEY_LOADED_MISMATCH_LIB /*301*/:
            case SampleTinkerReport.KEY_LOADED_MISMATCH_RESOURCE /*302*/:
            case SampleTinkerReport.KEY_LOADED_MISSING_DEX /*303*/:
            case 307:
                return true;
            default:
                return false;
        }
    }

    public URI getLocationURI(HttpResponse response, HttpContext context) throws ProtocolException {
        if (response == null) {
            throw new IllegalArgumentException("HTTP response may not be null");
        }
        Header locationHeader = response.getFirstHeader("location");
        if (locationHeader == null) {
            throw new ProtocolException("Received redirect response " + response.getStatusLine()
                    + " but no location header");
        }
        String location = locationHeader.getValue().replaceAll(" ", "%20");
        try {
            URI uri = new URI(location);
            HttpParams params = response.getParams();
            if (!uri.isAbsolute()) {
                if (params.isParameterTrue("http.protocol.reject-relative-redirect")) {
                    throw new ProtocolException("Relative redirect location '" + uri + "' not " +
                            "allowed");
                }
                HttpHost target = (HttpHost) context.getAttribute("http.target_host");
                if (target == null) {
                    throw new IllegalStateException("Target host not available in the HTTP " +
                            "context");
                }
                try {
                    uri = URIUtils.resolve(URIUtils.rewriteURI(new URI(((HttpRequest) context
                            .getAttribute("http.request")).getRequestLine().getUri()), target,
                            true), uri);
                } catch (URISyntaxException ex) {
                    throw new ProtocolException(ex.getMessage(), ex);
                }
            }
            if (params.isParameterFalse("http.protocol.allow-circular-redirects")) {
                URI redirectURI;
                RedirectLocations redirectLocations = (RedirectLocations) context.getAttribute
                        (REDIRECT_LOCATIONS);
                if (redirectLocations == null) {
                    redirectLocations = new RedirectLocations();
                    context.setAttribute(REDIRECT_LOCATIONS, redirectLocations);
                }
                if (uri.getFragment() != null) {
                    try {
                        redirectURI = URIUtils.rewriteURI(uri, new HttpHost(uri.getHost(), uri
                                .getPort(), uri.getScheme()), true);
                    } catch (URISyntaxException ex2) {
                        throw new ProtocolException(ex2.getMessage(), ex2);
                    }
                }
                redirectURI = uri;
                if (redirectLocations.contains(redirectURI)) {
                    throw new CircularRedirectException("Circular redirect to '" + redirectURI +
                            "'");
                }
                redirectLocations.add(redirectURI);
            }
            return uri;
        } catch (URISyntaxException ex22) {
            throw new ProtocolException("Invalid redirect URI: " + location, ex22);
        }
    }
}
