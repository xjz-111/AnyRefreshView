# AnyRefreshView

Any view can be refreshed which extends BaseAfvFragment (new style) or BasePastAfvFragment (ole style).

## Usage

```Java
public class MFragment extends BaseAfvFragment / BasePastAfvFragment {
    private WebView webView;

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected View getLayout() {
        return webView = new WebView(getContext());
    }

    @Override
    protected void initView() {
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                onLoadFinish();
            }
        });
    }

    @Override
    public void onLoad() {
        webView.loadUrl("https://www.baidu.com/");
    }
}
```
