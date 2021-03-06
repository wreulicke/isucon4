<#-- @ftlvariable name="createdAt" type="java.util.Date" -->
<#-- @ftlvariable name="ip" type="java.lang.String" -->
<#-- @ftlvariable name="login" type="java.lang.String" -->

<#include "include/header.html.ftl">

<div class="alert alert-success" role="alert">
    ログインに成功しました。<br/>
    未読のお知らせが０件、残っています。
</div>

<dl class="dl-horizontal">
    <dt>前回ログイン</dt>
    <dd id="last-logined-at">${createdAt?string["yyyy-MM-dd HH:mm:ss"]}</dd>
    <dt>最終ログインIPアドレス</dt>
    <dd id="last-logined-ip">${ip}</dd>
</dl>

<div class="panel panel-default">
    <div class="panel-heading">
    ${login}
    </div>
    <div class="panel-body">
        <div class="row">
            <div class="col-sm-4">
                普通預金<br/>
                <small>東京支店　1111111111</small>
                <br/>
            </div>
            <div class="col-sm-4">
                <p id="zandaka" class="text-right">
                    ―――円
                </p>
            </div>

            <div class="col-sm-4">
                <p>
                    <a class="btn btn-success btn-block">入出金明細を表示</a>
                    <a class="btn btn-default btn-block">振込・振替はこちらから</a>
                </p>
            </div>

            <div class="col-sm-12">
                <a class="btn btn-link btn-block">定期預金・住宅ローンのお申込みはこちら</a>
            </div>
        </div>
    </div>
</div>

<#include "include/footer.html.ftl">
