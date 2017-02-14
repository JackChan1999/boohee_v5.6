package com.squareup.okhttp.internal.http;

import com.boohee.one.tinker.reporter.SampleTinkerReport;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

public final class CacheStrategy {
    public final Response cacheResponse;
    public final Request  networkRequest;

    private CacheStrategy(Request networkRequest, Response cacheResponse) {
        this.networkRequest = networkRequest;
        this.cacheResponse = cacheResponse;
    }

    public static boolean isCacheable(Response response, Request request) {
        switch (response.code()) {
            case 200:
            case 203:
            case 204:
            case 300:
            case SampleTinkerReport.KEY_LOADED_MISMATCH_LIB /*301*/:
            case 308:
            case SampleTinkerReport.KEY_LOADED_SUCC_COST_OTHER /*404*/:
            case 405:
            case 410:
            case 414:
            case 501:
                break;
            case SampleTinkerReport.KEY_LOADED_MISMATCH_RESOURCE /*302*/:
            case 307:
                if (response.header("Expires") == null && response.cacheControl().maxAgeSeconds()
                        == -1 && !response.cacheControl().isPublic() && !response.cacheControl()
                        .isPrivate()) {
                    return false;
                }
            default:
                return false;
        }
        return (response.cacheControl().noStore() || request.cacheControl().noStore()) ? false :
                true;
    }
}
