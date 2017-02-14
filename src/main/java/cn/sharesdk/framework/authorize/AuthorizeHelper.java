package cn.sharesdk.framework.authorize;

import cn.sharesdk.framework.Platform;

public interface AuthorizeHelper {
    AuthorizeListener getAuthorizeListener();

    String getAuthorizeUrl();

    b getAuthorizeWebviewClient(g gVar);

    Platform getPlatform();

    String getRedirectUri();

    SSOListener getSSOListener();

    f getSSOProcessor(e eVar);
}
