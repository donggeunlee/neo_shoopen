package com.jdplus.neo_shoopen;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jdplus.neo_shoopen.util.Web_url;

public class Activity_Main extends AppCompatActivity {

    private static final String TAG = "Activity_Main";
    public static Activity_Main m_Ref;

    WebView m_WebView;
    boolean m_TimerPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_Ref = Activity_Main.this;

        webView_Load(Web_url.demo);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(m_TimerPaused == false) {
            m_WebView.resumeTimers();
            m_WebView.onResume();
            m_TimerPaused = true;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(m_TimerPaused == true) {
            m_WebView.resumeTimers();
            m_WebView.onResume();
            m_TimerPaused = false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        m_WebView.destroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && m_WebView.canGoBack()) {
            m_WebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void webView_Load (String url) {
        m_WebView = (WebView) findViewById(R.id.main_view);
        m_WebView.setWebViewClient(new customWebClient());
        m_WebView.setWebChromeClient(new WebChromeClient());
        m_WebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {

            }
        });
        m_WebView.loadUrl(url);
        setDefaultSetting(m_WebView);
    }

    private void setDefaultSetting(WebView wb) {
        WebSettings ws = wb.getSettings();

        // 자바스크립트 사용
        ws.setJavaScriptEnabled(true);

        // 화면 줌 (핀치줌과 줌컨트롤),  풀스크린 (리사이즈되서 화면에 꽉차게 보여짐)
        ws.setBuiltInZoomControls(true);
        ws.setSupportZoom(true);
        ws.setUseWideViewPort(true);
        ws.setLoadWithOverviewMode(true);

        ws.setDefaultTextEncodingName("euc-kr");

        // 위치정보 허용
        ws.setGeolocationEnabled(true);

        // 플래시 지원 (4.4.2 이상 다른 방법)
        ws.setPluginState(WebSettings.PluginState.ON);

        // 새 창을 띄우지 않는다
        ws.setSupportMultipleWindows(false);

        // 돔스토리지 (key - value 로 값 저장, 쿠키는 지워져도 돔스토리지는 유지된다.)
        ws.setDomStorageEnabled(true);

        // HTML5 Web DB
        ws.setDatabaseEnabled(true);
        ws.setDatabasePath("data/data" + m_Ref.getPackageName());

        // HTML5 Web Cache
        ws.setAppCacheEnabled(true);
        ws.setAppCacheMaxSize(1024 * 1024 * 8);  // 8M 이 모자라서 죽으면 WebChromeClient 시드쿼터 콜백함수에서 늘려줘야함
        ws.setAppCachePath("data/data" + m_Ref.getPackageName());

        // Overlay Scrollbar on top of WebContents
        wb.setVerticalScrollbarOverlay(true);
        wb.setHorizontalScrollbarOverlay(true);
    }

    public class customWebClient extends WebViewClient {
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//            handler.proceed(); // SSL 에러가 발생해도 계속 진행!
            handler.cancel();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // do your stuff here
        }

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            final Uri uri = Uri.parse(url);
            return handleUri(uri);
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            final Uri uri = request.getUrl();
            return handleUri(uri);
        }

        private boolean handleUri(final Uri uri) {
            Log.i(TAG, "Uri =" + uri);

            final String host = uri.getHost();
            final String scheme = uri.getScheme();

            String naverHost = "m.naver.com";
            Uri shoopenShutcut_scheme = Uri.parse("intent://addshortcut?version=9&url=http%3A%2F%2Fm.smartstore.naver.com%2Finflow%2Foutlink%2Fs%2Flfsquaregy132%3Ftr%3Ddv%26gtme%3D1&icon=http%3A%2F%2Fshop2.phinf.naver.net%2F20170623_244%2Fshoopang7727_1498207939860FRcbu_JPEG%2F21513904484094868_67081880.JPG%3Ftype%3Dround_160&title=LF%EC%8A%A4%ED%80%98%EC%96%B4%20%EA%B4%91%EC%96%91%20%EC%8A%88%ED%8E%9C&serviceCode=shopN#Intent;scheme=naversearchapp;action=android.intent.action.VIEW;category=android.intent.category.BROWSABLE;package=com.nhn.android.search;end");

            if(URLUtil.isValidUrl(uri.toString()) == false) {  // URL 이 URL 로써 유효한 것인지 체크
                if(uri.equals(shoopenShutcut_scheme)) {
                    return true;
                }
                else if(scheme.equals("sms") || scheme.equals("mailto")) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setData(uri);
                    m_WebView.getContext().startActivity(intent);
                }
                else {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(uri);
                    m_WebView.getContext().startActivity(intent);
                }
            }
            else {
                if(host.equals(naverHost)) {
                    return true;
                }
                else if(host.equals("play.google.com") == true || host.equals("store/apps/details")) {
                    Intent marketintent = new Intent(Intent.ACTION_VIEW);
                    marketintent.setData(Uri.parse("market://details?id=" + uri.getQueryParameter("id")));
                    m_WebView.getContext().startActivity(marketintent);
                    return true;
                }
            }
            return false;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);

            switch (errorCode) {
                case ERROR_AUTHENTICATION:
                    break;               // 서버에서 사용자 인증 실패
                case ERROR_BAD_URL:
                    break;               // 잘못된 URL
                case ERROR_CONNECT:
                    break;               // 서버로 연결 실패
                case ERROR_FAILED_SSL_HANDSHAKE:
                    break;               // SSL handshake 수행 실패
                case ERROR_FILE:
                    break;               // 일반 파일 오류
                case ERROR_FILE_NOT_FOUND:
                    break;               // 파일을 찾을 수 없습니다
                case ERROR_HOST_LOOKUP:
                    break;               // 서버 또는 프록시 호스트 이름 조회 실패
                case ERROR_IO:
                    break;               // 서버에서 읽거나 서버로 쓰기 실패
                case ERROR_PROXY_AUTHENTICATION:
                    break;               // 프록시에서 사용자 인증 실패
                case ERROR_REDIRECT_LOOP:
                    break;               // 너무 많은 리디렉션
                case ERROR_TIMEOUT:
                    break;               // 연결 시간 초과
                case ERROR_TOO_MANY_REQUESTS:
                    break;               // 페이지 로드중 너무 많은 요청 발생
                case ERROR_UNKNOWN:
                    break;               // 일반 오류
                case ERROR_UNSUPPORTED_AUTH_SCHEME:
                    break;               // 지원되지 않는 인증 체계
                case ERROR_UNSUPPORTED_SCHEME:
                    break;               // URI가 지원되지 않는 방식
            }
        }
    }

    private class CustomWebChromeClient extends WebChromeClient {

        /**
         * 페이지를 로딩하는 현재 진행 상황을 전해줍니다.
         * newProgress  현재 페이지 로딩 진행 상황, 0과 100 사이의 정수로 표현.(0% ~ 100%)
         */
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            Log.i(TAG, "Progress: " + String.valueOf(newProgress));
            super.onProgressChanged(view, newProgress);
        }

        /* 결과 :
        Progress: 10
        Progress: 15
        Progress: 35
        ...
        Progress: 76
        Progress: 100
        */

        /**
         * 현재 페이지에 새로운 favicon 이 있다고 알립니다.
         * icon 현재 페이지의 favicon 이 들어있는 비트맵
         */
        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
        }

        /**
         * favicon이란:
         * 일반적으로 웹 브라우저의 URL이 표시되기 전에 특정 웹사이트와 관련된 16 × 16 픽셀 아이콘
         */

        /**
         * 문서 제목에 변경이 있다고 알립니다.
         * title  문서의 새로운 타이틀이 들어있는 문자열
         */
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }

        /**
         * 아래처럼 title 태그 사이의 값을 가져옵니다.
         * <title> LG텔레콤 전자결제 서비스 </title>
         * @return
         */

        @Override
        public Bitmap getDefaultVideoPoster() {
            return super.getDefaultVideoPoster();
        }

        @Override
        public View getVideoLoadingProgressView() {
            return super.getVideoLoadingProgressView();
        }

        @Override
        public void getVisitedHistory(ValueCallback<String[]> callback) {
            super.getVisitedHistory(callback);
        }

        @Override
        public void onCloseWindow(WebView window) {
            super.onCloseWindow(window);
        }

        @Override
        public void onConsoleMessage(String message, int lineNumber, String sourceID) {
            super.onConsoleMessage(message, lineNumber, sourceID);
        }

        @Override
        public boolean onCreateWindow(WebView view, boolean dialog, boolean userGesture, Message resultMsg) {
            return super.onCreateWindow(view, dialog, userGesture, resultMsg);
        }

        @Override
        public void onExceededDatabaseQuota(String url, String databaseIdentifier, long currentQuota, long estimatedSize,
                                            long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater) {
            super.onExceededDatabaseQuota(url, databaseIdentifier, currentQuota, estimatedSize, totalUsedQuota, quotaUpdater);
        }

        @Override
        public void onGeolocationPermissionsHidePrompt() {
            super.onGeolocationPermissionsHidePrompt();
        }

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }

        @Override
        public void onHideCustomView() {
            super.onHideCustomView();
        }

        /**
         * 자바 스크립트 경고 대화 상자를 디스플레이한다고 클라이언트에게 알려줍니다.
         * 클라이언트가 true를 반환할 경우, WebView는 클라이언트가 대화 상자를 처리할 수 있다고
         * 여깁니다. 클라이언트가 false를 반환할 경우, WebView는 실행을 계속합니다.
         */
         @Override
         public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
             return super.onJsAlert(view, url, message, result);
         }

         /**
          * 현재 페이지에서 나와 탐색을 확정하는 대화 상자를 디스플레이한다고 클라이언트에게
          * 알려줍니다. 이것은 자바 스크립트 이벤트 onbeforeunload()의 결과입니다. 클라이언트가
          * true를 반환하는 경우, WebView는 클라이언트가 대화 상자를 처리하고 적절한 JsResult
          * 메쏘드를 호출할 것이라고 여깁니다. 클라이언트가 false를 반환하는 경우, true의 기본값은
          * 현재 페이지에서 나와 탐색하기를 수락하기 위한 자바 스크립트를 반환하게 될 것입니다.
          * 기본 동작은 false를 반환하는 것입니다. JsResult를 true로 설정한 것은 현재 페이지에서 나와
          * 탐색할 것이고 false로 설정한 것은 탐색을 취소할 것입니다.
          */
         @Override
         public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
            return super.onJsBeforeUnload(view, url, message, result);
         }

         /**
          * 사용자에게 확인 대화 상자를 디스플레이한다고 클라이언트에게 알려줍니다. 클라이언트가
          * true를 반환하는 경우, WebView는 클라이언트가 확인 대화 상자를 처리하고 적절한
          * JsResult 메쏘드를 호출할 수 있다고 여깁니다. 클라이언트가 false를 반환하는 경우 false의
          * 기본값은 자바 스크립트로 반환될 것 입니다. 기본 동작은 false를 반환하는 것입니다.
          */
         @Override
         public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
            return super.onJsConfirm(view, url, message, result);
         }

         /**
          * 사용자에게 프롬프트 대화 상자를 디스플레이한다고 클라이언트에게 알려줍니다.
          * 클라이언트가 true를 반환하는 경우, WebView는 클라이언트가 확인 대화 상자를 처리하고
          * 적절한 JsResult 메쏘드를 호출할 수 있다고 여깁니다. 클라이언트가 false를 반환하는 경우
          * false의 기본값은 자바 스크립트로 반환될 것 입니다. 기본 동작은 false를 반환하는 것입니다.
          */
         @Override
         public boolean onJsPrompt(WebView view, String url, String message,String defaultValue, JsPromptResult result) {
            return super.onJsPrompt(view, url, message, defaultValue, result);
         }

         /**
          * 자바 스크립트 실행 제한 시간을 초과했다고 클라이언트에게 알려줍니다. 그리고
          * 클라이언트가 실행을 중단할지 여부를 결정할 수 있습니다. 클라이언트가 true를 반환하는
          * 경우, 자바 스크립트가 중단됩니다. 클라이언트가 false를 반환하는 경우, 계속 실행됩니다.
          * 참고로 지속적인 실행 상태에서는 제한 시간 카운터가 재설정되고  스크립트가 다음 체크
          * 포인트에서 완료되지 않을 경우 계속 콜백되어질 것집니다.
          */
         @Override
         public boolean onJsTimeout() {
            return super.onJsTimeout();
         }

         @Override
         public void onReachedMaxAppCacheSize(long spaceNeeded, long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater) {
         super.onReachedMaxAppCacheSize(spaceNeeded, totalUsedQuota, quotaUpdater);
         }

         @Override
         public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
            super.onReceivedTouchIconUrl(view, url, precomposed);
         }

         @Override
         public void onRequestFocus(WebView view) {
            super.onRequestFocus(view);
         }

         @Override
         public void onShowCustomView(View view, CustomViewCallback callback) {
            super.onShowCustomView(view, callback);
         }
    }



}
